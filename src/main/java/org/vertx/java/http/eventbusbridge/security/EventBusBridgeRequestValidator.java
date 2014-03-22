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
package org.vertx.java.http.eventbusbridge.security;

import javax.ws.rs.core.MediaType;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

/**
 * Class provides utility methods for validating requests.
 *
 * @author j2ro
 *
 */
public final class EventBusBridgeRequestValidator {

	private static final String WHITELIST_CONFIG = "whitelist";
	private static final String INBOUND_CONFIG = "inbound";
	private static final String ADDRESS = "address";
	private static final String ADDRESS_REGEX = "address_re";

	private EventBusBridgeRequestValidator() {
	}

	/**
	 * Method checks if incoming address is on the whitelist.
	 * @param address Address to check
	 * @param config Configuration
	 * @return true if address was found on the whitelist, otherwise false
	 */
	public static boolean validateIncomingAddress(final String address, final JsonObject config) {
		JsonObject whitelist = config.getObject(WHITELIST_CONFIG);
		JsonObject inboundWhitelist = whitelist.getObject(INBOUND_CONFIG);

		if (checkAddresses(address, inboundWhitelist)) {
			return true;
		}

		if (checkAddressRegexes(address, inboundWhitelist)) {
			return true;
		}
		return false;
	}

	/**
	 * Method checks if response media type is supported. Currently only JSon and XML are supported.
	 * @param responseMediaType Media type to check
	 * @return true if media type is supported, otherwise false
	 */
	public static boolean validateResponseMediaType(final String responseMediaType) {
		return responseMediaType == null || MediaType.APPLICATION_JSON.equals(responseMediaType) || MediaType.APPLICATION_XML.equals(responseMediaType);
	}

	private static boolean checkAddresses(final String address, final JsonObject inboundWhitelist) {
		JsonArray addresses = inboundWhitelist.getArray(ADDRESS);
		if (addresses != null) {
			for (Object object : addresses) {
				if (address.equals(object)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean checkAddressRegexes(final String address, final JsonObject inboundWhitelist) {
		JsonArray addressRegexes = inboundWhitelist.getArray(ADDRESS_REGEX);
		if (addressRegexes != null) {
			for (Object object : addressRegexes) {
				String regex = (String) object;
				if (address.matches(regex)) {
					return true;
				}
			}
		}
		return false;
	}
}
