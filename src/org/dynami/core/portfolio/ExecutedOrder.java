/*
 * Copyright 2015 Alessandro Atria - a.atria@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dynami.core.portfolio;

public class ExecutedOrder {
	public final long requestId;
	public final String symbol;
	public final double price;
	public final long quantity;
	public final long time;
	
	public ExecutedOrder(long requestId, String symbol, double price, long quantity, long time) {
		this.requestId = requestId;
		this.symbol = symbol;
		this.price = price;
		this.quantity = quantity;
		this.time = time;
	}

	@Override
	public String toString() {
		return "ExecutedOrder [requestId=" + requestId + ", symbol=" + symbol + ", price=" + price + ", quantity="
				+ quantity + ", time=" + time + "]";
	}
}
