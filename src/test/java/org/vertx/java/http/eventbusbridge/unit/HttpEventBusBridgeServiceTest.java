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
package org.vertx.java.http.eventbusbridge.unit;

import static org.mockito.Mockito.*;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.WebApplicationException;

import org.junit.Test;
import org.mockito.Mockito;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.http.eventbusbridge.model.EventBusBridgeRequest;
import org.vertx.java.http.eventbusbridge.model.EventBusInstruction;
import org.vertx.java.http.eventbusbridge.model.EventBusMessageType;
import org.vertx.java.http.eventbusbridge.model.HttpResponseHandler;
import org.vertx.java.http.eventbusbridge.service.HttpEventBusBridgeService;
import org.vertx.java.platform.Container;

/**
 * Tests for HttpEventBusBridgeService.
 *
 * @author j2ro
 *
 */
public class HttpEventBusBridgeServiceTest {

	@Test
	public void testForwardSendNoReply() throws MalformedURLException {
		String address = "testaddress";
		String message = "HelloWorld";
		String responseMediaType = "application/xml";
		long timeout = 60000;
		JsonObject config = createConfig(address, timeout);
		HttpEventBusBridgeService service = new HttpEventBusBridgeService();
		EventBusBridgeRequest request = createRequest(address, message, null, responseMediaType);
		
		Vertx vertx = Mockito.mock(Vertx.class);
		EventBus eventBus = Mockito.mock(EventBus.class);
		when(vertx.eventBus()).thenReturn(eventBus);
		when(eventBus.send(address, message)).thenReturn(eventBus);
		
		Container container = Mockito.mock(Container.class); 
		when(container.config()).thenReturn(config);

		service.forward(request, EventBusInstruction.send, vertx, container);
		verify(eventBus, times(1)).sendWithTimeout(address, (Object) message, timeout, (Handler<AsyncResult<Message<Object>>>) null);
	}
	
	@Test
	public void testForwardSendReply() throws MalformedURLException {
		String address = "testaddress";
		String message = "HelloWorld";
		URL responseUrl = new URL("http://localhost:8080/ebbresponse");
		String responseMediaType = "application/xml";
		HttpClient httpClient = Mockito.mock(HttpClient.class);
		long timeout = 60000;
		JsonObject config = createConfig(address, timeout);
		
		HttpEventBusBridgeService service = new HttpEventBusBridgeService();
		EventBusBridgeRequest request = createRequest(address, message, responseUrl, responseMediaType);
		
		Vertx vertx = Mockito.mock(Vertx.class);
		EventBus eventBus = Mockito.mock(EventBus.class);
		when(vertx.eventBus()).thenReturn(eventBus);
		when(eventBus.send(address, message)).thenReturn(eventBus);
		when(vertx.createHttpClient()).thenReturn(httpClient);
		
		Container container = Mockito.mock(Container.class); 
		when(container.config()).thenReturn(config);

		service.forward(request, EventBusInstruction.send, vertx, container);
		verify(eventBus, times(1)).sendWithTimeout(eq(address), eq((Object)message), eq(timeout), any(HttpResponseHandler.class));
	}
	
	@Test(expected = WebApplicationException.class)
	public void testForwardSendInvalidAddress() throws MalformedURLException {
		String invalidAddress = "illegaladdress";
		String allowedAddress = "validaddress";
		String message = "HelloWorld";
		String responseMediaType = "application/xml";
		long timeout = 60000;
		JsonObject config = createConfig(allowedAddress, timeout);
		HttpEventBusBridgeService service = new HttpEventBusBridgeService();
		EventBusBridgeRequest request = createRequest(invalidAddress, message, null, responseMediaType);
		
		Vertx vertx = Mockito.mock(Vertx.class);
		
		Container container = Mockito.mock(Container.class); 
		when(container.config()).thenReturn(config);

		service.forward(request, EventBusInstruction.send, vertx, container);
	}
	
	@Test
	public void testForwardPublish() throws MalformedURLException {
		String address = "testaddress";
		String message = "HelloWorld";
		String responseMediaType = "application/xml";
		long timeout = 60000;
		JsonObject config = createConfig(address, timeout);
		HttpEventBusBridgeService service = new HttpEventBusBridgeService();
		EventBusBridgeRequest request = createRequest(address, message, null, responseMediaType);
		
		Vertx vertx = Mockito.mock(Vertx.class);
		EventBus eventBus = Mockito.mock(EventBus.class);
		when(vertx.eventBus()).thenReturn(eventBus);
		when(eventBus.send(address, message)).thenReturn(eventBus);
		
		Container container = Mockito.mock(Container.class); 
		when(container.config()).thenReturn(config);

		service.forward(request, EventBusInstruction.publish, vertx, container);
		verify(eventBus, times(1)).publish(address, (Object) message);
	}
	
	@Test(expected = WebApplicationException.class)
	public void testForwardPublishInvalidAddress() throws MalformedURLException {
		String invalidAddress = "illegaladdress";
		String allowedAddress = "validaddress";
		String message = "HelloWorld";
		String responseMediaType = "application/xml";
		long timeout = 60000;
		JsonObject config = createConfig(allowedAddress, timeout);
		HttpEventBusBridgeService service = new HttpEventBusBridgeService();
		EventBusBridgeRequest request = createRequest(invalidAddress, message, null, responseMediaType);
		
		Vertx vertx = Mockito.mock(Vertx.class);

		Container container = Mockito.mock(Container.class); 
		when(container.config()).thenReturn(config);

		service.forward(request, EventBusInstruction.publish, vertx, container);
	}

	private EventBusBridgeRequest createRequest(String address, String message, URL responseUrl, String responseMediaType) {
		EventBusBridgeRequest request = new EventBusBridgeRequest();
		request.setAddress(address);
		request.setEventBusMessageType(EventBusMessageType.String);
		request.setMessage(message.getBytes());
		request.setResponseMediaType(responseMediaType);
		if (responseUrl != null) {
			request.setResponseUrl(responseUrl.toExternalForm());
		}
		return request;
	}
	
	private JsonObject createConfig(String allowedAddress, long timeout) {
		JsonObject config = new JsonObject();
		JsonObject whitelist = new JsonObject();
		JsonObject inbound = new JsonObject();
		JsonArray whitelistAddresses = new JsonArray();
		whitelistAddresses.addString(allowedAddress);
		inbound.putArray("address", whitelistAddresses);
		whitelist.putObject("inbound", inbound);
		config.putObject("whitelist", whitelist);
		config.putNumber("timeout", timeout);
		return config;
	}
}
