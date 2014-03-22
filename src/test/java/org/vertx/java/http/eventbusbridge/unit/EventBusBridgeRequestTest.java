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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.glassfish.jersey.internal.util.Base64;
import org.junit.Test;
import org.vertx.java.http.eventbusbridge.model.EventBusBridgeRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests unmarshalling of EventBusBridgeRequest objects.
 * 
 * @author j2ro
 *
 */
public class EventBusBridgeRequestTest {

	@Test
	public void testUnmarshallingFromXml() throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(EventBusBridgeRequest.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 
		InputStream input = getClass().getResourceAsStream("/unit/test-request.xml");
		EventBusBridgeRequest request = (EventBusBridgeRequest) jaxbUnmarshaller.unmarshal(input);		
		assertEquals("testaddress", request.getAddress());
		assertEquals(Base64.decodeAsString("SGVsbG8gV29ybGQ="), new String(request.getMessage()));
		assertEquals("http://www.test.com/response", request.getResponseUrl());
		assertEquals("String", request.getEventBusMessageType().toString());
		assertEquals("application/xml", request.getResponseMediaType().toString());		
	}
	
	@Test
	public void testUnmarshallingFromJson() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		InputStream input = getClass().getResourceAsStream("/unit/test-request.json");
		EventBusBridgeRequest request = mapper.readValue(input, EventBusBridgeRequest.class);
		assertEquals("testaddress", request.getAddress());
		assertEquals(Base64.decodeAsString("SGVsbG8gV29ybGQ="), new String(request.getMessage()));
		assertEquals("http://www.test.com/response", request.getResponseUrl());
		assertEquals("String", request.getEventBusMessageType().toString());
		assertEquals("application/json", request.getResponseMediaType().toString());
	}
}
