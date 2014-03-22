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
import org.vertx.java.http.eventbusbridge.util.EventBusMessageTypeConverter;

/**
 * Tests for the EventBusBridgeRequestHelper utility class.
 * 
 * @author j2ro
 *
 */
public class EventBusBridgeRequestHelperTest {

	@Test
	public void testConvertString() {
		String originalString = "TestString";
		Object convertedObject = EventBusMessageTypeConverter.convertIncomingMessage(originalString.getBytes(), EventBusMessageType.String);		
		assertTrue(convertedObject instanceof String);
		assertEquals(originalString, convertedObject);	
	}
	
	@Test
	public void testConvertBoolean() {
		Boolean originalBoolean = Boolean.TRUE;
		byte[] booleanByteArray = originalBoolean.toString().getBytes();
		Object convertedObject = EventBusMessageTypeConverter.convertIncomingMessage(booleanByteArray, EventBusMessageType.Boolean);		
		assertTrue(convertedObject instanceof Boolean);
		assertEquals(originalBoolean, convertedObject);
		
	}
	
	@Test
	public void testConvertByte() {
		Byte originalByte = Byte.valueOf((byte) 3);
		byte[] byteByteArray = originalByte.toString().getBytes();
		Object convertedObject = EventBusMessageTypeConverter.convertIncomingMessage(byteByteArray, EventBusMessageType.Byte);		
		assertTrue(convertedObject instanceof Byte);
		assertEquals(originalByte, convertedObject);
		
	}
	
	@Test
	public void testConvertByteArray() {
		byte[] originalByteArray = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		Object convertedObject = EventBusMessageTypeConverter.convertIncomingMessage(originalByteArray, EventBusMessageType.ByteArray);
		assertTrue(convertedObject instanceof byte[]);
		assertArrayEquals(originalByteArray, (byte[]) convertedObject);
	}
	
	@Test
	public void testConvertCharacter() {
		Character originalCharacter = Character.valueOf('T');
		byte[] characterByteArray = originalCharacter.toString().getBytes();
		Object convertedObject = EventBusMessageTypeConverter.convertIncomingMessage(characterByteArray, EventBusMessageType.Character);		
		assertTrue(convertedObject instanceof Character);
		assertEquals(originalCharacter, convertedObject);
	}
	
	@Test
	public void testConvertDouble() {
		Double originalDouble = Double.valueOf(1.7);
		byte[] doubleByteArray = originalDouble.toString().getBytes();
		Object convertedObject = EventBusMessageTypeConverter.convertIncomingMessage(doubleByteArray, EventBusMessageType.Double);		
		assertTrue(convertedObject instanceof Double);
		assertEquals(originalDouble, convertedObject);
	}
	
	@Test
	public void testConvertFloat() {
		Float originalFloat = Float.valueOf(1.4F);
		byte[] floatByteArray = originalFloat.toString().getBytes();
		Object convertedObject = EventBusMessageTypeConverter.convertIncomingMessage(floatByteArray, EventBusMessageType.Float);		
		assertTrue(convertedObject instanceof Float);
		assertEquals(originalFloat, convertedObject);
	}
	
	@Test
	public void testConvertInteger() {
		Integer originalInteger = Integer.valueOf(102);
		byte[] integerByteArray = originalInteger.toString().getBytes();
		Object convertedObject = EventBusMessageTypeConverter.convertIncomingMessage(integerByteArray, EventBusMessageType.Integer);		
		assertTrue(convertedObject instanceof Integer);
		assertEquals(originalInteger, convertedObject);
	}
	
	@Test
	public void testConvertJsonArray() {
		JsonArray originalJsonArray = new JsonArray();
		originalJsonArray.addString("TestString");
		originalJsonArray.addNumber(Integer.MAX_VALUE);
		originalJsonArray.addBoolean(Boolean.TRUE);
		byte[] jsonArrayByteArray = originalJsonArray.toString().getBytes();
		Object convertedObject = EventBusMessageTypeConverter.convertIncomingMessage(jsonArrayByteArray, EventBusMessageType.JsonArray);
		assertTrue(convertedObject instanceof JsonArray);
		assertEquals(originalJsonArray, convertedObject);
	}
	
	@Test
	public void testConvertJsonObject() {
		JsonObject originalJsonObject = new JsonObject();
		originalJsonObject.putString("string", "TestString");
		originalJsonObject.putNumber("number", Integer.MIN_VALUE);
		originalJsonObject.putBoolean("boolean", Boolean.TRUE);
		byte[] jsonObjectByteArray = originalJsonObject.toString().getBytes();
		Object convertedObject = EventBusMessageTypeConverter.convertIncomingMessage(jsonObjectByteArray, EventBusMessageType.JsonObject);
		assertTrue(convertedObject instanceof JsonObject);
		assertEquals(originalJsonObject, convertedObject);
	}
	
	@Test
	public void testConvertLong() {
		Long originalLong = Long.valueOf(1024335533223L);
		byte[] longByteArray = originalLong.toString().getBytes();
		Object convertedObject = EventBusMessageTypeConverter.convertIncomingMessage(longByteArray, EventBusMessageType.Long);		
		assertTrue(convertedObject instanceof Long);
		assertEquals(originalLong, convertedObject);
	}
	
	@Test
	public void testConvertShort() {
		Short originalShort = Short.valueOf((short)259);
		byte[] shortByteArray = originalShort.toString().getBytes();
		Object convertedObject = EventBusMessageTypeConverter.convertIncomingMessage(shortByteArray, EventBusMessageType.Short);		
		assertTrue(convertedObject instanceof Short);
		assertEquals(originalShort, convertedObject);
	}	
}
