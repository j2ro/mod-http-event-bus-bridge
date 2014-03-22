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
package org.vertx.java.http.eventbusbridge.integration;

import static org.vertx.testtools.VertxAssert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.internal.util.Base64;
import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.impl.VertxInternal;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.http.eventbusbridge.integration.handler.MessageSendWithReplyHandler;
import org.vertx.java.http.eventbusbridge.integration.util.HttpRequestHelper;
import org.vertx.java.http.eventbusbridge.integration.util.TemplateHelper;
import org.vertx.java.http.eventbusbridge.model.EventBusInstruction;
import org.vertx.java.http.eventbusbridge.model.EventBusMessageType;
import org.vertx.testtools.TestVerticle;

import com.englishtown.vertx.jersey.JerseyModule;

/**
 * Tests sending of messages to event bus via the HTTP Event Bus Bridge HTTP Service, and validates responses.
 * 
 * @author j2ro
 * 
 */
@SuppressWarnings("rawtypes")
public class MessageSendWithReplyTest extends TestVerticle {

	private static final String SEND_REQUEST_TEMPLATE_JSON = "/templates/send_with_reply_request_template.json";
	private static final String SEND_REQUEST_TEMPLATE_XML = "/templates/send_with_reply_request_template.xml";
	private static final String SEND_RESPONSE_TEMPLATE_JSON = "/templates/send_with_reply_response_template.json";
	private static final String SEND_RESPONSE_TEMPLATE_XML = "/templates/send_with_reply_response_template.xml";
	private static final String SEND_RESPONSE_FAILURE_TEMPLATE_JSON = "/templates/send_with_reply_response_failure_template.json";
	private static final String SEND_RESPONSE_FAILURE_TEMPLATE_XML = "/templates/send_with_reply_response_failure_template.xml";

	private JsonObject config;
	private String url;

	@Override
    public void start(final Future<Void> startedResult) {
		try {
			InputStream input = MessageSendWithReplyTest.class.getResourceAsStream("/config/integration-test-config.json");
			String configString;
		
			configString = IOUtils.toString(input, Charset.defaultCharset());
		
			config = new JsonObject(configString);
			url = HttpRequestHelper.constructUrlStringFromConfig(config, EventBusInstruction.send);
			System.out.println("URL: " + url);
	        container.deployVerticle(JerseyModule.class.getName(), config, new Handler<AsyncResult<String>>() {	        
				public void handle(AsyncResult<String> event) {
					if (event.succeeded()) {
						startedResult.setResult(null);
						MessageSendWithReplyTest.super.start();
					} else {
						startedResult.setFailure(event.cause());
					}
				}	         					
	         });

		} catch (IOException e) {
			startedResult.setFailure(e);
		}
    }

	@Test
	public void testSendingStringJsonWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentString), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentString, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingBooleanJsonWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Boolean;
		final Boolean sentBoolean = Boolean.TRUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentBoolean.toString()), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentBoolean, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingByteJsonWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Byte;
		final Byte sentByte = Byte.MAX_VALUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentByte.toString()), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentByte, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingByteArrayJsonWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.ByteArray;
		final byte[] sentByteArray = new byte[] {0, 1, 2, 3, 4, 5, 6, 7};
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentByteArray), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentByteArray, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}

	@Test
	public void testSendingCharacterJsonWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Character;
		final Character sentCharacter = new Character('T');
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentCharacter.toString()), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentCharacter, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingDoubleJsonWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Double;
		final Double sentDouble = Double.MAX_VALUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentDouble.toString()), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentDouble, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingFloatJsonWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Float;
		final Float sentFloat = Float.MAX_VALUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentFloat.toString()), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentFloat, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingIntegerJsonWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Integer;
		final Integer sentInteger = Integer.MAX_VALUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentInteger.toString()), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentInteger, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingJsonArrayJsonWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.JsonArray;
		final JsonArray sentJsonArray = new JsonArray("[\"TestString\",2147483647,true]");
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentJsonArray.toString()), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentJsonArray, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingJsonObjectJsonWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.JsonObject;
		final JsonObject sentJsonObject = new JsonObject("{\"hello\":\"world\"}");
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentJsonObject.toString()), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentJsonObject, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingLongJsonWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Long;
		final Long sentLong = Long.MAX_VALUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentLong.toString()), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentLong, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingShortJsonWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Short;
		final Short sentShort = Short.MAX_VALUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentShort.toString()), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentShort, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingStringXmlWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentString), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentString, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingBooleanXmlWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Boolean;
		final Boolean sentBoolean = Boolean.TRUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentBoolean.toString()), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentBoolean, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingByteXmlWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Byte;
		final Byte sentByte = Byte.MAX_VALUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentByte.toString()), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentByte, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingByteArrayXmlWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.ByteArray;
		final byte[] sentByteArray = new byte[] {0, 1, 2, 3, 4, 5, 6, 7};
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentByteArray), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentByteArray, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}

	@Test
	public void testSendingCharacterXmlWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Character;
		final Character sentCharacter = new Character('T');
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentCharacter.toString()), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentCharacter, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingDoubleXmlWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Double;
		final Double sentDouble = Double.MAX_VALUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentDouble.toString()), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentDouble, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingFloatXmlWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Float;
		final Float sentFloat = Float.MAX_VALUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentFloat.toString()), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentFloat, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingIntegerXmlWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Integer;
		final Integer sentInteger = Integer.MAX_VALUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentInteger.toString()), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentInteger, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingJsonArrayXmlWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.JsonArray;
		final JsonArray sentJsonArray = new JsonArray("[\"TestString\",2147483647,true]");
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentJsonArray.toString()), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentJsonArray, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingJsonObjectXmlWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.JsonObject;
		final JsonObject sentJsonObject = new JsonObject("{\"hello\":\"world\"}");
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentJsonObject.toString()), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentJsonObject, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingLongXmlWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Long;
		final Long sentLong = Long.MAX_VALUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentLong.toString()), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentLong, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingShortXmlWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Short;
		final Short sentShort = Short.MAX_VALUE;
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentShort.toString()), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentShort, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingStringXmlWithResponseJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentString), messageType, responseUrl, MediaType.APPLICATION_JSON);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentString, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingStringJsonWithResponseXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentString), messageType, responseUrl, MediaType.APPLICATION_XML);
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		Handler<Message> messageConsumerHandler = new MessageSendWithReplyHandler(sentString, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingJsonWithReplyToInvalidAddress() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		String replyUrl = "http://testserver:8080/";
		Map<String, String> expectations = createExpectations("invalidaddress", Base64.encodeAsString(sentString), messageType, replyUrl, MediaType.APPLICATION_JSON);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.FORBIDDEN.getStatusCode(), MediaType.APPLICATION_JSON, true);
	}

	@Test
	public void testSendingXmlWithReplyToInvalidAddress() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		String replyUrl = "http://testserver:8080/";
		Map<String, String> expectations = createExpectations("invalidaddress", Base64.encodeAsString(sentString), messageType, replyUrl, MediaType.APPLICATION_XML);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.FORBIDDEN.getStatusCode(), MediaType.APPLICATION_XML, true);
	}
	
	@Test
	public void testSendingJsonWithReplyWithInvalidResponseMediaType() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		String replyUrl = "http://testserver:8080/";
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentString), messageType, replyUrl, MediaType.APPLICATION_SVG_XML);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode(), MediaType.APPLICATION_JSON, true);
	}
	
	@Test
	public void testSendingXmlWithReplyWithInvalidResponseMediaType() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		String replyUrl = "http://testserver:8080/";
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentString), messageType, replyUrl, MediaType.APPLICATION_SVG_XML);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode(), MediaType.APPLICATION_XML, true);
	}
	
	@Test
	public void testSendingStringJsonWithResponseJsonNoListener() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createFailureExpectations("someaddress", Base64.encodeAsString(sentString), messageType, responseUrl, MediaType.APPLICATION_JSON, "NO_HANDLERS");
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_FAILURE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingStringXmlWithResponseXmlNoListener() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createFailureExpectations("someaddress", Base64.encodeAsString(sentString), messageType, responseUrl, MediaType.APPLICATION_XML, "NO_HANDLERS");
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_FAILURE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingStringXmlWithResponseJsonNoListener() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createFailureExpectations("someaddress", Base64.encodeAsString(sentString), messageType, responseUrl, MediaType.APPLICATION_JSON, "NO_HANDLERS");
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_FAILURE_TEMPLATE_JSON, expectations);
		createHttpServer(port, MediaType.APPLICATION_JSON, responseBody);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingStringJsonWithResponseXmlNoListener() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		int port = findFreePort();
		String responseUrl = createHttpServerUrl(port);
		Map<String, String> expectations = createFailureExpectations("someaddress", Base64.encodeAsString(sentString), messageType, responseUrl, MediaType.APPLICATION_XML, "NO_HANDLERS");
		String responseBody = TemplateHelper.generateOutputUsingTemplate(SEND_RESPONSE_FAILURE_TEMPLATE_XML, expectations);
		createHttpServer(port, MediaType.APPLICATION_XML, responseBody);
		String requestBody = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, requestBody, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	private Map<String, String> createExpectations(String address, String message, EventBusMessageType messageType, String responseUrl, String responseMediaType) {
		Map<String, String> expectations = new HashMap<String, String>();
		expectations.put("address", address);
		expectations.put("message", message);
		expectations.put("messageType", messageType.name());
		expectations.put("responseUrl", responseUrl);
		expectations.put("responseMediaType", responseMediaType);
		return expectations;
	}
	
	private Map<String, String> createFailureExpectations(String address, String message, EventBusMessageType messageType, String responseUrl, String responseMediaType, String cause) {
		Map<String, String> expectations = new HashMap<String, String>();
		expectations.put("address", address);
		expectations.put("message", message);
		expectations.put("messageType", messageType.name());
		expectations.put("responseUrl", responseUrl);
		expectations.put("responseMediaType", responseMediaType);
		expectations.put("cause", cause);
		return expectations;
	}

	private void createHttpServer(int listenPort, final String expectedResponseMediaType, final String expectedResponse) {
		HttpServer server = vertx.createHttpServer();
		server.requestHandler(new Handler<HttpServerRequest>() {

			@Override
			public void handle(final HttpServerRequest request) {
				request.bodyHandler(new Handler<Buffer>() {
					@Override
					public void handle(Buffer body) {
						String contentType = request.headers().get("Content-Type");
						assertEquals(expectedResponseMediaType, contentType);
						assertEquals(expectedResponse, body.toString());
						testComplete();
					}
				});
			}
		});
		server.listen(listenPort);
	}

	private String createHttpServerUrl(int port) {
		return "http://localhost:" + port + "/";
	}

	private int findFreePort() throws IOException {
		try (ServerSocket socket = new ServerSocket(0)) {
			return socket.getLocalPort();
		}
	}
}
