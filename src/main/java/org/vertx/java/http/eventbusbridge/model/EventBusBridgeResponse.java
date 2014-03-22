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
 * Class encapsulates replies received from the HTTP Event Bus Bridge.
 *
 * @author j2ro
 *
 */
@XmlRootElement(name = "eventBusBridgeResponse")
@JsonRootName(value = "eventBusBridgeResponse")
public final class EventBusBridgeResponse {

	private String address;
	private byte[] responseMessage;
	private EventBusMessageType eventBusMessageType;
	private boolean successful;
	private String cause;

	/**
	 * Get the address.
	 * @return The address
	 */
	@XmlElement(name = "address", required = false)
	@JsonProperty(value = "address", required = false)
	public String getAddress() {
		return address;
	}

	/**
	 * Set the address.
	 * @param address The address
	 */
	public void setAddress(final String address) {
		this.address = address;
	}

	/**
	 * Get the response message.
	 * @return The response message
	 */
	@XmlElement(name = "message", required = false)
	@JsonProperty(value = "message", required = false)
	public byte[] getResponseMessage() {
		return responseMessage;
	}

	/**
	 * Set the response message.
	 * @param responseMessage The response message
	 */
	public void setResponseMessage(final byte[] responseMessage) {
		this.responseMessage = responseMessage;
	}

	/**
	 * Gets the event bus message type.
	 * @return The event bus message type
	 */
	@XmlElement(name = "messageType", required = false)
	@JsonProperty(value = "messageType", required = false)
	public EventBusMessageType getEventBusMessageType() {
		return eventBusMessageType;
	}

	public void setEventBusMessageType(final EventBusMessageType eventBusMessageType) {
		this.eventBusMessageType = eventBusMessageType;
	}

	/**
	 * Gets whether the request was successful.
	 * @return Whether or not the request was successful
	 */
	@XmlElement(name = "successful", required = true)
	@JsonProperty(value = "successful", required = true)
	public boolean isSuccessful() {
		return successful;
	}

	/**
	 * Gets whether the request was successful.
	 * @param successful Whether or not the request was successful
	 */
	public void setSuccessful(final boolean successful) {
		this.successful = successful;
	}

	/**
	 * Gets the cause of any error (if any occurred).
	 * @return The cause
	 */
	@XmlElement(name = "cause", required = false)
	@JsonProperty(value = "cause", required = false)
	public String getCause() {
		return cause;
	}

	/**
	 * Sets the cause of any error (if any occurred).
	 * @param cause The cause
	 */
	public void setCause(final String cause) {
		this.cause = cause;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "address: " + address + '\n'
				+ "response message: " + responseMessage + '\n'
				+ "event bus message type: " + eventBusMessageType + '\n'
				+ "successful: " + successful + '\n'
				+ "cause: " + cause;
	}

}
