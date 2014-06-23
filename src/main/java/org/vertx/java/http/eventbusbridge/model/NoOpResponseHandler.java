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

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

/**
 * Handler class that simply ignores response object.
 *
 * @author j2ro
 *
 */
public final class NoOpResponseHandler implements Handler<AsyncResult<Message<Object>>> {

	/**
	 * Handle the event bus reply.
	 *
	 * @param event The event
	 */
	@Override
	public void handle(final AsyncResult<Message<Object>> event) {
		// Do nothing.
	}
}
