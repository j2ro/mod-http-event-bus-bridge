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

import org.junit.Test;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.http.eventbusbridge.model.EventBusMessageType;

/**
 * Tests for EventBusMessageType.
 * 
 * @author j2ro
 *
 */
public class EventBusMessageTypeTest {

	@Test
	public void testLookupByClass() {
		assertEquals(EventBusMessageType.String, EventBusMessageType.lookupByClass(String.class));
		assertEquals(EventBusMessageType.Boolean, EventBusMessageType.lookupByClass(Boolean.class));
		assertEquals(EventBusMessageType.Byte, EventBusMessageType.lookupByClass(Byte.class));
		assertEquals(EventBusMessageType.ByteArray, EventBusMessageType.lookupByClass(byte[].class));
		assertEquals(EventBusMessageType.Character, EventBusMessageType.lookupByClass(Character.class));
		assertEquals(EventBusMessageType.Double, EventBusMessageType.lookupByClass(Double.class));
		assertEquals(EventBusMessageType.Float, EventBusMessageType.lookupByClass(Float.class));
		assertEquals(EventBusMessageType.Integer, EventBusMessageType.lookupByClass(Integer.class));
		assertEquals(EventBusMessageType.JsonArray, EventBusMessageType.lookupByClass(JsonArray.class));
		assertEquals(EventBusMessageType.JsonObject, EventBusMessageType.lookupByClass(JsonObject.class));
		assertEquals(EventBusMessageType.Long, EventBusMessageType.lookupByClass(Long.class));
		assertEquals(EventBusMessageType.Short, EventBusMessageType.lookupByClass(Short.class));
		assertNull(EventBusMessageType.lookupByClass(Object.class));
	}
}
