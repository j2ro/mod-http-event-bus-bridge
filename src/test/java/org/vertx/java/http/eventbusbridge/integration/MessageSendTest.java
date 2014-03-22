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

import java.io.IOException;
import java.io.InputStream;
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
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.impl.VertxInternal;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.http.eventbusbridge.integration.handler.MessageSendHandler;
import org.vertx.java.http.eventbusbridge.integration.util.HttpRequestHelper;
import org.vertx.java.http.eventbusbridge.integration.util.TemplateHelper;
import org.vertx.java.http.eventbusbridge.model.EventBusInstruction;
import org.vertx.java.http.eventbusbridge.model.EventBusMessageType;
import org.vertx.testtools.TestVerticle;

import com.englishtown.vertx.jersey.JerseyModule;

/**
 * Tests sending of messages to event bus via the HTTP Event Bus Bridge HTTP Service.
 * 
 * @author j2ro
 * 
 */
@SuppressWarnings("rawtypes")
public class MessageSendTest extends TestVerticle {

	private static final String SEND_REQUEST_TEMPLATE_JSON = "/templates/send_request_template.json";
	private static final String SEND_REQUEST_TEMPLATE_XML = "/templates/send_request_template.xml";
	
	private JsonObject config;
	private String url;
	
	@Override
    public void start(final Future<Void> startedResult) {
		try {
			InputStream input = MessageSendTest.class.getResourceAsStream("/config/integration-test-config.json");
			String configString;
		
			configString = IOUtils.toString(input, Charset.defaultCharset());
		
			config = new JsonObject(configString);
			url = HttpRequestHelper.constructUrlStringFromConfig(config, EventBusInstruction.send);
			
	        container.deployVerticle(JerseyModule.class.getName(), config, new Handler<AsyncResult<String>>() {	        
				public void handle(AsyncResult<String> event) {
					if (event.succeeded()) {
						startedResult.setResult(null);
						MessageSendTest.super.start();
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
	public void testSendingStringJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentString), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentString, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingBooleanJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Boolean;
		final Boolean sentBoolean = Boolean.TRUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentBoolean.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentBoolean, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingByteJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Byte;
		final Byte sentByte = Byte.MAX_VALUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentByte.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentByte, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingCharacterJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Character;
		final Character sentCharacter = new Character('T');
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentCharacter.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentCharacter, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingDoubleJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Double;
		final Double sentDouble = Double.MAX_VALUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentDouble.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentDouble, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingFloatJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Float;
		final Float sentFloat = Float.MAX_VALUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentFloat.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentFloat, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingIntegerJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Integer;
		final Integer sentInteger = Integer.MAX_VALUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentInteger.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentInteger, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingLongJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Long;
		final Long sentLong = Long.MAX_VALUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentLong.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentLong, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingShortJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Short;
		final Short sentShort = Short.MAX_VALUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentShort.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentShort, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingJsonArrayJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.JsonArray;
		final JsonArray sentJsonArray = new JsonArray("[\"TestString\",2147483647,true]");
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentJsonArray.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentJsonArray, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingJsonObjectJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.JsonObject;
		final JsonObject sentJsonObject = new JsonObject("{\"hello\":\"world\"}");
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentJsonObject.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentJsonObject, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingByteArrayJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.ByteArray;
		final byte[] sentByteArray = new byte[] {0, 1, 2, 3, 4, 5, 6, 7};
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentByteArray), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentByteArray, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testSendingToInvalidAddressJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		Map<String, String> expectations = createExpectations("invalidaddress", Base64.encodeAsString(sentString), messageType);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.FORBIDDEN.getStatusCode(), MediaType.APPLICATION_JSON, true);
	}
	
	@Test
	public void testSendingInvalidMessageJson() throws IOException {
		String body = "INVALIDMESSAGE";
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.BAD_REQUEST.getStatusCode(), MediaType.APPLICATION_JSON, true);
	}
	
	@Test
	public void testSendingStringXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentString), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentString, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingBooleanXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Boolean;
		final Boolean sentBoolean = Boolean.TRUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentBoolean.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentBoolean, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingByteXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Byte;
		final Byte sentByte = Byte.MAX_VALUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentByte.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentByte, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingCharacterXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Character;
		final Character sentCharacter = new Character('T');
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentCharacter.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentCharacter, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingDoubleXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Double;
		final Double sentDouble = Double.MAX_VALUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentDouble.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentDouble, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingFloatXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Float;
		final Float sentFloat = Float.MAX_VALUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentFloat.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentFloat, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingIntegerXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Integer;
		final Integer sentInteger = Integer.MAX_VALUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentInteger.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentInteger, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingLongXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Long;
		final Long sentLong = Long.MAX_VALUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentLong.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentLong, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingShortXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Short;
		final Short sentShort = Short.MAX_VALUE;
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentShort.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentShort, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingJsonArrayXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.JsonArray;
		final JsonArray sentJsonArray = new JsonArray("[\"TestString\",2147483647,true]");
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentJsonArray.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentJsonArray, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingJsonObjectXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.JsonObject;
		final JsonObject sentJsonObject = new JsonObject("{\"hello\":\"world\"}");
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentJsonObject.toString()), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentJsonObject, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingByteArrayXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.ByteArray;
		final byte[] sentByteArray = new byte[] {0, 1, 2, 3, 4, 5, 6, 7};
		Map<String, String> expectations = createExpectations("someaddress", Base64.encodeAsString(sentByteArray), messageType);
		Handler<Message> messageConsumerHandler = new MessageSendHandler(sentByteArray, expectations);
		vertx.eventBus().registerHandler(expectations.get("address"), messageConsumerHandler);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testSendingToInvalidAddressXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		Map<String, String> expectations = createExpectations("invalidaddress", Base64.encodeAsString(sentString), messageType);
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.FORBIDDEN.getStatusCode(), MediaType.APPLICATION_XML, true);
	}
	
	@Test
	public void testSendingInvalidMessageXml() throws IOException {
		String body = "INVALIDMESSAGE";
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.BAD_REQUEST.getStatusCode(), MediaType.APPLICATION_XML, true);
	}
	
	private Map<String, String> createExpectations(String address, String message, EventBusMessageType messageType) {
		Map<String, String> expectations = new HashMap<String, String>();
		expectations.put("address", address);
		expectations.put("message", message);
		expectations.put("messageType", messageType.name());
		return expectations;
	}
}
