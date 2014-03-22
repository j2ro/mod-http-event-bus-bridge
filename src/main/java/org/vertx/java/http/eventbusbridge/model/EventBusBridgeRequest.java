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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Class encapsulates requests sent to the HTTP Event Bus Bridge.
 *
 * @author j2ro
 *
 */
@XmlRootElement(name = "eventBusBridgeRequest")
@JsonRootName(value = "eventBusBridgeRequest")
public final class EventBusBridgeRequest {

	private String address;
	private String responseUrl;
	private byte[] message;
	private EventBusMessageType eventBusMessageType;
	private String responseMediaType;

	/**
	 * Get the event bus address.
	 * @return The event bus address
	 */
	@XmlElement(name = "address", required = true)
	@JsonProperty(value = "address", required = true)
	public String getAddress() {
		return address;
	}

	/**
	 * Set the event bus address.
	 * @param address The event bus address
	 */
	public void setAddress(final String address) {
		this.address = address;
	}

	/**
	 * Get the response url.
	 * @return The response url
	 */
	@XmlElement(name = "responseUrl", required = false)
	@JsonProperty(value = "responseUrl", required = false)
	public String getResponseUrl() {
		return responseUrl;
	}

	/**
	 * Set the response url.
	 * @param responseUrl The response url
	 */
	public void setResponseUrl(final String responseUrl) {
		this.responseUrl = responseUrl;
	}

	/**
	 * Get the message.
	 * @return the message
	 */
	@XmlElement(name = "message", required = true)
	@JsonProperty(value = "message", required = true)
	public byte[] getMessage() {
		return message;
	}

	/**
	 * Set the message.
	 * @param message the message
	 */
	public void setMessage(final byte[] message) {
		this.message = message;
	}

	/**
	 * Get the event bus message type.
	 * @return event bus message type
	 */
	@XmlElement(name = "messageType", required = true)
	@JsonProperty(value = "messageType", required = true)
	public EventBusMessageType getEventBusMessageType() {
		return eventBusMessageType;
	}

	/**
	 * Set the event bus message type.
	 * @param eventBusMessageType event bus message type
	 */
	public void setEventBusMessageType(final EventBusMessageType eventBusMessageType) {
		this.eventBusMessageType = eventBusMessageType;
	}

	/**
	 * Get response media type.
	 * @return the media type of the response
	 */
	@XmlElement(name = "responseMediaType", required = false)
	@JsonProperty(value = "responseMediaType", required = false)
	public String getResponseMediaType() {
		return responseMediaType;
	}

	/**
	 * Set response media type.
	 * @param responseMediaType the media type of the response
	 */
	public void setResponseMediaType(final String responseMediaType) {
		this.responseMediaType = responseMediaType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "address: " + address + '\n'
				+ "message: " + message + '\n'
				+ "message_type: " + eventBusMessageType + '\n'
				+ "response_url: " + responseUrl + "\n"
				+ "response_media_type: " + responseMediaType;
	}
}
