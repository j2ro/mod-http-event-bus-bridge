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
package org.vertx.java.http.eventbusbridge.integration.handler;

import static org.vertx.testtools.VertxAssert.*;

import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

@SuppressWarnings("rawtypes")
public class MessageSendWithReplyHandler implements Handler<Message> {

	private Object sentObject;
	private Map<String, String> expectations;
	
	public MessageSendWithReplyHandler(Object sentObject, Map<String, String> expectations) {
		this.sentObject = sentObject;
		this.expectations = expectations;
	}

	@Override
	public void handle(Message event) {
		assertEquals(expectations.get("address"), event.address());
		assertTrue(sentObject.getClass().isInstance(event.body()));
		if (sentObject instanceof byte[]) {
			assertArrayEquals((byte[]) sentObject, (byte[]) event.body());
		} else {
			assertEquals(sentObject, event.body());
		}
		// echo back received message
		event.reply(event.body());
	}
}
