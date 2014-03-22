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
package org.vertx.java.http.eventbusbridge.integration.util;

import static org.vertx.testtools.VertxAssert.*;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.core.http.impl.DefaultHttpClient;
import org.vertx.java.core.impl.VertxInternal;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.http.eventbusbridge.model.EventBusInstruction;

public class HttpRequestHelper {
	
	public static void sendHttpPostRequest(String url, final String body, VertxInternal vertx, final int expectedStatusCode, String mediaType) {
		sendHttpPostRequest(url, body, vertx, expectedStatusCode, mediaType, false);
	}

	public static void sendHttpPostRequest(String url, final String body, VertxInternal vertx, final int expectedStatusCode, String mediaType, final boolean completeTestOnResponse) {
		HttpClient client = new DefaultHttpClient(vertx);
		client.setPort(8080);
		client.setHost("localhost");
		HttpClientRequest request = client.post(url, new Handler<HttpClientResponse>() {
			
			@Override
			public void handle(HttpClientResponse event) {
				assertEquals(expectedStatusCode, event.statusCode());

				event.exceptionHandler(new Handler<Throwable>() {
					@Override
					public void handle(Throwable event) {
						fail(event.toString());						
					}					
				});
				
				event.endHandler(new Handler<Void>() {
					@Override
					public void handle(Void event) {
						if (completeTestOnResponse) {
							testComplete();
						}
					}					
				});
				
				if (completeTestOnResponse) {
					testComplete();
				}
			}
		});
		request.headers().add("Content-Type", mediaType);
		request.setChunked(true);
		request.setTimeout(2000);
		request.write(body);
		request.end();
	}
	
	public static String constructUrlStringFromConfig(JsonObject config, EventBusInstruction instruction) {
		return "http://" + config.getString("host") + ":" + config.getNumber("port") + config.getString("base_path") + "eventbus/" + instruction;
	}
}
