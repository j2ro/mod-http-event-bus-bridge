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
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.http.eventbusbridge.security.EventBusBridgeRequestValidator;

/**
 * Tests for EventBusBridgeRequestValidator.
 *
 * @author j2ro
 *
 */
public class EventBusBridgeRequestValidatorTest {

	private JsonObject config;
	
	@Before
	public void setUpClass() throws IOException {
		InputStream input = getClass().getResourceAsStream("/unit/security-test-config.json");
		String configString = IOUtils.toString(input, Charset.defaultCharset());
		config = new JsonObject(configString);
	}

	@Test
	public void testAddress() {
		assertTrue(EventBusBridgeRequestValidator.validateIncomingAddress("someaddress", config));
		assertTrue(EventBusBridgeRequestValidator.validateIncomingAddress("anotheraddress", config));
		
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress("invalidaddress", config));
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress("anotheraddress ", config));
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress(" anotheraddress", config));
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress("anotheraddressi", config));
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress("ianotheraddress", config));
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress("anotheraddress.*inv", config));
	}
	
	@Test
	public void testAddressRe() {
		assertTrue(EventBusBridgeRequestValidator.validateIncomingAddress("address_test", config));
		assertTrue(EventBusBridgeRequestValidator.validateIncomingAddress("_test", config));
		assertTrue(EventBusBridgeRequestValidator.validateIncomingAddress("  _test", config));
		assertTrue(EventBusBridgeRequestValidator.validateIncomingAddress("all_addresses", config));
		assertTrue(EventBusBridgeRequestValidator.validateIncomingAddress("all_", config));
		assertTrue(EventBusBridgeRequestValidator.validateIncomingAddress("all_  ", config));
		
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress("all", config));
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress("all addresses", config));
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress(" all_addresses", config));
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress("address_test ", config));
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress("address test", config));
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress(" test", config));
		assertFalse(EventBusBridgeRequestValidator.validateIncomingAddress("test", config));
	}

}
