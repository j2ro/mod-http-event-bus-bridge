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
package org.vertx.java.http.eventbusbridge.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of the different types that can be put on the event bus.
 *
 * @author j2ro
 *
 */
@SuppressWarnings("rawtypes")
public enum EventBusMessageType {

	/** String. */
	String(String.class),
	/** boolean. */
	Boolean(Boolean.class),
	/** byte. */
	Byte(Byte.class),
	/** short. */
	Short(Short.class),
	/** int. */
	Integer(Integer.class),
	/** long. */
	Long(Long.class),
	/** float. */
	Float(Float.class),
	/** double. */
	Double(Double.class),
	/** char. */
	Character(Character.class),
	/** byte[]. */
	ByteArray(byte[].class),
	/** JsonArray. */
	JsonArray(org.vertx.java.core.json.JsonArray.class),
	/** JsonObject. */
	JsonObject(org.vertx.java.core.json.JsonObject.class);

	/* creates a simple lookup table to find relevant EventBusMessageType by Class */
	private static Map<Class, EventBusMessageType> lookupByClass = new HashMap<Class, EventBusMessageType>();
	private Class clazz;
	static {
		for (EventBusMessageType messageType : EventBusMessageType.values()) {
			lookupByClass.put(messageType.clazz, messageType);
		}
	}

	private EventBusMessageType(final Class clazz) {
		this.clazz = clazz;
	}

	/**
	 * Look up equivalent EventBusMessageType for class.
	 * @param clazz Class to search for a suitable EventBusMessageType for
	 * @return EventBusMessageType for class, or null if non found
	 */
	public static EventBusMessageType lookupByClass(final Class clazz) {
		return lookupByClass.get(clazz);
	}
}
