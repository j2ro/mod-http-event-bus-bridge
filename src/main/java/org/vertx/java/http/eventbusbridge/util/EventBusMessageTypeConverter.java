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
package org.vertx.java.http.eventbusbridge.util;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.http.eventbusbridge.model.EventBusMessageType;

/**
 * Utility class providing methods for use when dealing with incoming requests.
 *
 * @author j2ro
 *
 */
public final class EventBusMessageTypeConverter {

	private EventBusMessageTypeConverter() {
	}

	/**
	 * Method attempts to convert event bus message from byte[] into the desired type.
	 * @param message Incoming message as array of bytes
	 * @param messageType Desired type
	 * @return Encapsulation of object in desired type
	 */
	public static Object convertIncomingMessage(final byte[] message, final EventBusMessageType messageType) {
		String messageString = new String(message);
		switch(messageType) {
			case String:
				return messageString;
			case Character:
				return new Character(messageString.charAt(0));
			case Boolean:
				return new Boolean(messageString);
			case Byte:
				return new Byte(messageString);
			case Short:
				return new Short(messageString);
			case Integer:
				return new Integer(messageString);
			case Long:
				return new Long(messageString);
			case Float:
				return new Float(messageString);
			case Double:
				return new Double(messageString);
			case JsonArray:
				return new JsonArray(messageString);
			case JsonObject:
				return new JsonObject(messageString);
			case ByteArray:
				return message;
			default:
				throw new IllegalArgumentException("EventBusMessageType '" + messageType + "' not recognised. "
						+ "This exception should never be thrown!");
		}
	}

	/**
	 * Method attempts to convert event bus message from Object into a byte[].
	 * @param message Incoming message as an object
	 * @return byte[] representation of object
	 */
	public static byte[] convertOutgoingMessage(final Object message) {
		String messageString = message.toString();
		if (message instanceof String) {
			return messageString.getBytes();
		} else if (message instanceof Character) {
			return messageString.substring(0, 1).getBytes();
		} else if (message instanceof Boolean) {
			return messageString.getBytes();
		} else if (message instanceof Byte) {
			return messageString.getBytes();
		} else if (message instanceof Short) {
			return messageString.getBytes();
		} else if (message instanceof Integer) {
			return messageString.getBytes();
		} else if (message instanceof Long) {
			return messageString.getBytes();
		} else if (message instanceof Float) {
			return messageString.getBytes();
		} else if (message instanceof Double) {
			return messageString.getBytes();
		} else if (message instanceof JsonArray) {
			return messageString.getBytes();
		} else if (message instanceof JsonObject) {
			return messageString.getBytes();
		} else if (message instanceof byte[]) {
			return (byte[]) message;
		} else {
			throw new IllegalArgumentException("Class type '" + message.getClass() + "' not supported. This exception should never be thrown!");
		}
	}
}
