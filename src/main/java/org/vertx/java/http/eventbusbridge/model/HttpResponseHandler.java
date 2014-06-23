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
package org.vertx.java.http.eventbusbridge.model;

import java.net.URL;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.eventbus.ReplyException;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.http.eventbusbridge.util.EventBusMessageTypeConverter;
import org.vertx.java.http.eventbusbridge.util.SerializationHelper;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Handler class for managing replies to sent messages.
 *
 * @author j2ro
 *
 */
public final class HttpResponseHandler implements Handler<AsyncResult<Message<Object>>> {

	private static final Logger LOGGER = Logger.getLogger(HttpResponseHandler.class);

	private URL url;
	private String mediaType;
	private HttpClient httpClient;
	private HttpReplyResponseHandler responseHandler;
	private String address;

	/*
	 * Creates new instance of HttpResponseHandler.
	 * @param url URL to post the reply to
	 * @param mediaType MediaType of the reply
	 * @param httpClient The HTTP client
	 * @param address The address
	 */
	public HttpResponseHandler(final URL url, final String mediaType, final HttpClient httpClient, final String address) {
		this.url = url;
		this.mediaType = mediaType;
		this.httpClient = httpClient;
		this.responseHandler = new HttpReplyResponseHandler();
		this.address = address;
	}

	/**
	 * Gets the response url.
	 * @return the response url
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * Gets the media type of the response.
	 * @return the response media type
	 */
	public String getMediaType() {
		return mediaType;
	}

	/**
	 * Gets the Http client.
	 * @return the Http client
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}

	/**
	 * Gets the address the request was sent to.
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Handle the event bus reply.
	 *
	 * @param event The event
	 */
	@Override
	public void handle(final AsyncResult<Message<Object>> event) {
		EventBusBridgeResponse response = new EventBusBridgeResponse();
		response.setSuccessful(event.succeeded());
		response.setAddress(address);
		if (event.succeeded()) {
			Message<Object> result = event.result();

			Object responseMessage = result.body();
			response.setResponseMessage(EventBusMessageTypeConverter.convertOutgoingMessage(result.body()));
			response.setEventBusMessageType(EventBusMessageType.lookupByClass(responseMessage.getClass()));
		} else {
			Throwable cause = event.cause();
			if (cause != null) {
				if (cause instanceof ReplyException) {
					ReplyException replyException = (ReplyException) cause;
					response.setCause(replyException.failureType().name());
				} else {
					response.setCause(cause.getMessage());
				}
			}
		}
		postResponse(response);
	}

	private void postResponse(final EventBusBridgeResponse response) {
		httpClient.setHost(url.getHost());
		httpClient.setPort(url.getPort());
		HttpClientRequest request = httpClient.post(url.toExternalForm(), responseHandler);
		request.setChunked(true);
		request.headers().add("Content-Type", mediaType);
		try {
			String responseBody = SerializationHelper.serialize(response, mediaType);
			request.write(responseBody);
			request.end();
		} catch (JsonProcessingException | JAXBException e) {
			LOGGER.error("Unable to serialize response\n" + response + "\n to: " + mediaType);
		}
	}

	private class HttpReplyResponseHandler implements Handler<HttpClientResponse> {

		private Logger logger = Logger.getLogger(HttpReplyResponseHandler.class);

		@Override
		public void handle(final HttpClientResponse response) {
			if (response.statusCode() == Status.ACCEPTED.getStatusCode()) {
				logger.debug("Reply sent successfully");
			} else {
				logger.warn("Error sending the reply to client. status code:" + response.statusCode()
						    + "  message: " + response.statusMessage());
			}
		}
	}
}
