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
import java.util.concurrent.atomic.AtomicInteger;

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
import org.vertx.java.http.eventbusbridge.integration.handler.MessagePublishCheckHandler;
import org.vertx.java.http.eventbusbridge.integration.handler.MessagePublishHandler;
import org.vertx.java.http.eventbusbridge.integration.util.HttpRequestHelper;
import org.vertx.java.http.eventbusbridge.integration.util.TemplateHelper;
import org.vertx.java.http.eventbusbridge.model.EventBusInstruction;
import org.vertx.java.http.eventbusbridge.model.EventBusMessageType;
import org.vertx.testtools.TestVerticle;

import com.englishtown.vertx.jersey.JerseyModule;

/**
 * Tests publishing of messages to event bus via the HTTP Event Bus Bridge HTTP Service.
 * 
 * @author j2ro
 * 
 */
@SuppressWarnings("rawtypes")
public class MessagePublishTest extends TestVerticle {

	private static final String SEND_REQUEST_TEMPLATE_JSON = "/templates/publish_request_template.json";
	private static final String SEND_REQUEST_TEMPLATE_XML = "/templates/publish_request_template.xml";
	
	private static final String PUB_ADDRESS_PREFIX = "address_pub_";
	
	private static final int NUMBER_OF_PUBLISH_HANDLERS = 10;
	
	private static int addressNumber = 0;
	
	private JsonObject config;
	private String url;
	
	@Override
    public void start(final Future<Void> startedResult) {
		try {
			InputStream input = MessagePublishTest.class.getResourceAsStream("/config/integration-test-config.json");
			String configString;
		
			configString = IOUtils.toString(input, Charset.defaultCharset());
		
			config = new JsonObject(configString);
			url = HttpRequestHelper.constructUrlStringFromConfig(config, EventBusInstruction.publish);
			
	        container.deployVerticle(JerseyModule.class.getName(), config, new Handler<AsyncResult<String>>() {	        
				public void handle(AsyncResult<String> event) {
					if (event.succeeded()) {
						startedResult.setResult(null);
						MessagePublishTest.super.start();
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
	public void testPublishingStringJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentString), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentString, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testPublishingBooleanJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Boolean;
		final Boolean sentBoolean = Boolean.FALSE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentBoolean.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentBoolean, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testPublishingByteJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Byte;
		final Byte sentByte = Byte.MIN_VALUE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentByte.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentByte, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testPublishingByteArrayJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.ByteArray;
		final byte[] sentByteArray = new byte[] {0, 1, 2, 3, 4, 5, 6, 7};
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentByteArray), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentByteArray, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testPublishingCharacterJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Character;
		final Character sentCharacter = new Character('T');
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentCharacter.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentCharacter, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testPublishingDoubleJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Double;
		final Double sentDouble = Double.MIN_VALUE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentDouble.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentDouble, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testPublishingFloatJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Float;
		final Float sentFloat = Float.MIN_VALUE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentFloat.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentFloat, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testPublishingIntegerJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Integer;
		final Integer sentInteger = Integer.MIN_VALUE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentInteger.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentInteger, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testPublishingJsonArrayJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.JsonArray;
		final JsonArray sentJsonArray = new JsonArray("[\"TestString\",2147483647,false]");
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentJsonArray.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentJsonArray, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testPublishingJsonObjectJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.JsonObject;
		final JsonObject sentJsonObject = new JsonObject("{\"hello\":\"world\"}");
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentJsonObject.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentJsonObject, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testPublishingLongJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Long;
		final Long sentLong = Long.MIN_VALUE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentLong.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentLong, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testPublishingShortJson() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Short;
		final Short sentShort = Short.MIN_VALUE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentShort.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentShort, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_JSON, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testPublishingStringXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.String;
		final String sentString = new String("HelloWorld");
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentString), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentString, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testPublishingBooleanXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Boolean;
		final Boolean sentBoolean = Boolean.FALSE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentBoolean.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentBoolean, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testPublishingByteXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Byte;
		final Byte sentByte = Byte.MIN_VALUE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentByte.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentByte, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testPublishingByteArrayXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.ByteArray;
		final byte[] sentByteArray = new byte[] {0, 1, 2, 3, 4, 5, 6, 7};
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentByteArray), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentByteArray, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testPublishingCharacterXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Character;
		final Character sentCharacter = new Character('T');
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentCharacter.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentCharacter, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testPublishingDoubleXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Double;
		final Double sentDouble = Double.MIN_VALUE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentDouble.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentDouble, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testPublishingFloatXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Float;
		final Float sentFloat = Float.MIN_VALUE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentFloat.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentFloat, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testPublishingIntegerXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Integer;
		final Integer sentInteger = Integer.MIN_VALUE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentInteger.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentInteger, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testPublishingJsonArrayXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.JsonArray;
		final JsonArray sentJsonArray = new JsonArray("[\"TestString\",2147483647,false]");
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentJsonArray.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentJsonArray, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testPublishingJsonObjectXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.JsonObject;
		final JsonObject sentJsonObject = new JsonObject("{\"hello\":\"world\"}");
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentJsonObject.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentJsonObject, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testPublishingLongXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Long;
		final Long sentLong = Long.MIN_VALUE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentLong.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentLong, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	@Test
	public void testPublishingShortXml() throws IOException {
		final EventBusMessageType messageType = EventBusMessageType.Short;
		final Short sentShort = Short.MIN_VALUE;
		Map<String, String> expectations = createExpectations(generateUniqueAddress(), Base64.encodeAsString(sentShort.toString()), messageType);		
		final AtomicInteger completedCount = new AtomicInteger(0);
		Handler<Message> messagePublishHandler = new MessagePublishHandler(sentShort, expectations, completedCount);
		registerListenersAndCheckForResponses(messagePublishHandler, expectations, NUMBER_OF_PUBLISH_HANDLERS, completedCount);		
		String body = TemplateHelper.generateOutputUsingTemplate(SEND_REQUEST_TEMPLATE_XML, expectations);
		HttpRequestHelper.sendHttpPostRequest(url, body, (VertxInternal) vertx, Status.ACCEPTED.getStatusCode(), MediaType.APPLICATION_XML);
	}
	
	private Map<String, String> createExpectations(String address, String message, EventBusMessageType messageType) {
		Map<String, String> expectations = new HashMap<String, String>();
		expectations.put("address", address);
		expectations.put("message", message);
		expectations.put("messageType", messageType.name());
		return expectations;
	}
	
	private void registerListenersAndCheckForResponses(Handler<Message> handler, Map<String, String> expectations, int numOfHandlers, AtomicInteger completedCount) {
		for (int i = 0; i < numOfHandlers; i++) {
			vertx.eventBus().registerHandler(expectations.get("address"), handler);
		}
		Handler<Long> checkHandler = new MessagePublishCheckHandler(numOfHandlers, completedCount);
		vertx.setPeriodic(200, checkHandler);
	}
	
	private synchronized String generateUniqueAddress() {
		return PUB_ADDRESS_PREFIX + addressNumber++;
	}
}
