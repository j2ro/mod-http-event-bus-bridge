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

import java.util.concurrent.atomic.AtomicInteger;

import org.vertx.java.core.Handler;

public class MessagePublishCheckHandler implements Handler<Long> {

	private int expectedCompletions;
	private AtomicInteger completedCount;
	
	public MessagePublishCheckHandler(int expectedCompletions, AtomicInteger completedCount) {
		this.expectedCompletions = expectedCompletions;
		this.completedCount = completedCount;
	}

	@Override
	public void handle(Long event) {
		if (completedCount.intValue() >= expectedCompletions) {
			testComplete();
		}
	}
}
