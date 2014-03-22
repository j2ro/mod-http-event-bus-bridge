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

import java.io.StringWriter;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.vertx.java.http.eventbusbridge.model.EventBusBridgeResponse;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Class provides some utility methods for serialisation to JSON and XML.
 *
 * @author j2ro
 */
public final class SerializationHelper {

	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	private static final JAXBContext JAXB_CONTEXT;

	static {
		try {
			JAXB_CONTEXT = JAXBContext.newInstance(EventBusBridgeResponse.class);
			JSON_MAPPER.setSerializationInclusion(Include.NON_NULL);
			JSON_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
		} catch (JAXBException e) {
			throw new RuntimeException("Class initialization failed due to JAXBException", e);
		}
	}

	private SerializationHelper() {
	}

	/**
	 * Serialises object to JSON or XML depending on the media type provided.
	 * @param object Object to serialise
	 * @param mediaType Media type to serialise to ('application/xml' or 'application/json')
	 * @return Serialised object
	 * @throws JsonProcessingException if there was a problem serializing to Json
	 * @throws JAXBException if there was a problem serializing to XML
	 */
	public static String serialize(final Object object, final String mediaType) throws JsonProcessingException, JAXBException {
		if (MediaType.APPLICATION_JSON.equals(mediaType)) {
			return toJson(object);
		} else if (MediaType.APPLICATION_XML.equals(mediaType)) {
			return toXml(object);
		} else {
			throw new IllegalArgumentException("Unsupported media type serialization requested for response: " + mediaType);
		}
	}

	private static String toJson(final Object jsonObject) throws JsonProcessingException {
		return JSON_MAPPER.writeValueAsString(jsonObject);
	}

	private static String toXml(final Object xmlObject) throws JAXBException {
		StringWriter writer = new StringWriter();
		Marshaller jaxbMarshaller = JAXB_CONTEXT.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(xmlObject, writer);
		return writer.toString();
	}
}
