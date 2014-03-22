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

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.internal.util.Base64;
import org.junit.Test;
import org.vertx.java.http.eventbusbridge.model.EventBusBridgeResponse;
import org.vertx.java.http.eventbusbridge.model.EventBusMessageType;
import org.vertx.java.http.eventbusbridge.util.SerializationHelper;

/**
 * Tests marshalling of EventBusBridgeResponse object.
 * 
 * @author j2ro
 *
 */
public class EventBusBridgeResponseTest {

	@Test
	public void testUnmarshallingSuccessResponseToXml() throws JAXBException, IOException {
		InputStream input = getClass().getResourceAsStream("/unit/test-response-success.xml");
		String expectedXml = IOUtils.toString(input);
		EventBusBridgeResponse response = createSuccessfulResponse();
		String actualXml = SerializationHelper.serialize(response, MediaType.APPLICATION_XML);
		assertEquals(expectedXml, actualXml);
	}
	
	@Test
	public void testUnmarshallingFailureResponseToXml() throws JAXBException, IOException {
		InputStream input = getClass().getResourceAsStream("/unit/test-response-failure.xml");
		String expectedXml = IOUtils.toString(input);
		EventBusBridgeResponse response = createFailureResponse();
		String actualXml = SerializationHelper.serialize(response, MediaType.APPLICATION_XML);
		assertEquals(expectedXml, actualXml);
	}
	
	@Test
	public void testUnmarshallingSuccessResponseToJson() throws IOException, JAXBException {
		InputStream input = getClass().getResourceAsStream("/unit/test-response-success.json");
		String expectedJson = IOUtils.toString(input);
		EventBusBridgeResponse response = createSuccessfulResponse();
		String actualJson = SerializationHelper.serialize(response, MediaType.APPLICATION_JSON);
		assertEquals(expectedJson, actualJson);
	}
	
	@Test
	public void testUnmarshallingFailureResponseToJson() throws IOException, JAXBException {
		InputStream input = getClass().getResourceAsStream("/unit/test-response-failure.json");
		String expectedJson = IOUtils.toString(input);
		EventBusBridgeResponse response = createFailureResponse();
		String actualJson = SerializationHelper.serialize(response, MediaType.APPLICATION_JSON);
		assertEquals(expectedJson, actualJson);
	}
	
	private EventBusBridgeResponse createSuccessfulResponse() {
		EventBusBridgeResponse response = new EventBusBridgeResponse();
		response.setAddress("originaladdress");
		response.setSuccessful(true);
		response.setResponseMessage(Base64.encode("TestResponse".getBytes()));
		response.setEventBusMessageType(EventBusMessageType.String);
		return response;
	}
	
	private EventBusBridgeResponse createFailureResponse() {
		EventBusBridgeResponse response = new EventBusBridgeResponse();
		response.setAddress("originaladdress");
		response.setSuccessful(false);
		response.setCause(new IOException("Unable to contact server").getMessage());	
		return response;
	}
}
