/**
 * Copyright (c) 2014 j2ro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.java.http.eventbusbridge.service;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.JsonParseException;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.http.eventbusbridge.model.EventBusBridgeRequest;
import org.vertx.java.http.eventbusbridge.model.EventBusInstruction;
import org.vertx.java.http.eventbusbridge.model.HttpResponseHandler;
import org.vertx.java.http.eventbusbridge.security.EventBusBridgeRequestValidator;
import org.vertx.java.http.eventbusbridge.util.EventBusMessageTypeConverter;
import org.vertx.java.platform.Container;
import org.xml.sax.SAXParseException;


/**
 * Class provides REST services for the HTTP Event Bus Bridge Gateway.
 *
 * @author j2ro
 */
@Provider
@Path("eventbus")
public final class HttpEventBusBridgeService {

	private static final long DEFAULT_TIMEOUT = 0;
	private static final String DEFAULT_RESPONSE_MEDIA_TYPE = MediaType.APPLICATION_JSON;

	/**
	 * Service to forward HTTP request onto the vertx event bus.
	 * @param request EventBusBridgeRequest object
	 * @param instruction Instruction (i.e. send or publish)
	 * @param vertx Vertx instance
	 * @param container Container instance
	 * @return Message
	 * @throws MalformedURLException If the response url contained in the request is invalid
	 */
	@POST
	@Path("/{instruction}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response forward(final EventBusBridgeRequest request, @PathParam("instruction") final EventBusInstruction instruction,
			                @Context final Vertx vertx, @Context final Container container) throws MalformedURLException {
		String address = request.getAddress();
		JsonObject config = container.config();

		validateRequest(request, config);

		Object messageObject = EventBusMessageTypeConverter.convertIncomingMessage(request.getMessage(), request.getEventBusMessageType());
		switch (instruction) {
			case send:
				Long timeout = config.getLong("timeout", DEFAULT_TIMEOUT);
				String mediaType = request.getResponseMediaType() != null ? request.getResponseMediaType() : DEFAULT_RESPONSE_MEDIA_TYPE;
				URL responseUrl = request.getResponseUrl() != null ? new URL(request.getResponseUrl()) : null;
				send(address, messageObject, responseUrl, mediaType, vertx, timeout);
				break;
			case publish:
				publish(address, messageObject, vertx);
				break;
			default:
				throw new IllegalArgumentException("Illegal event bus instruction provided: " + instruction);
		}
		return Response.accepted().build();
	}

	private void send(final String address, final Object messageObject, final URL responseUrl,
			          final String mediaType, final Vertx vertx, final Long timeout) throws MalformedURLException {
		HttpResponseHandler responseHandler = createHandler(responseUrl, mediaType, vertx.createHttpClient(), address);
		vertx.eventBus().sendWithTimeout(address, messageObject, timeout, responseHandler);
	}

	private void publish(final String address, final Object messageObject, final Vertx vertx) {
		vertx.eventBus().publish(address, messageObject);
	}

	private void validateRequest(final EventBusBridgeRequest request, final JsonObject config) {
		String address = request.getAddress();
		String responseMediaType = request.getResponseMediaType();
		if (!EventBusBridgeRequestValidator.validateIncomingAddress(address, config)) {
			throw new WebApplicationException("Not authorized to forward messages to address: " + address, Response.Status.FORBIDDEN);
		} else if (!EventBusBridgeRequestValidator.validateResponseMediaType(responseMediaType)) {
			throw new WebApplicationException("Unsupported response media type requested: " + responseMediaType,
					                          Response.Status.UNSUPPORTED_MEDIA_TYPE);
		}
	}

	/**
	 * Creates new instance of HttpResponse if url is provided.
	 * @param url URL to post the reply to
	 * @param mediaType MediaType of the reply
	 * @param httpClient HttpClient
	 * @param address address
	 * @return new HttpResponseHandler instance if url was specified, otherwise null
	 * @throws MalformedURLException If response URL was in an invalid format
	 */
	private HttpResponseHandler createHandler(final URL url, final String mediaType,
			                                  final HttpClient httpClient, final String address) throws MalformedURLException {
		return url == null ? null : new HttpResponseHandler(url, mediaType, httpClient, address);
	}

	@Provider
	public static final class SAXParseExceptionMapper implements ExceptionMapper<SAXParseException> {
	    public Response toResponse(final SAXParseException exception) {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}

	@Provider
	public static final class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {
		public Response toResponse(final JsonParseException exception) {
		    return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@Provider
	public static final class MalformedURLExceptionMapper implements ExceptionMapper<MalformedURLException> {
		public Response toResponse(final MalformedURLException exception) {
		    return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
