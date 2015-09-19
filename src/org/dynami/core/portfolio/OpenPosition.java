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

public class OpenPosition {
	public final String symbol;
	public final long quantity;
	public final double entryPrice;
	public final long entryTime;
	public final double pointValue;
	public long currentTime;
	
	public OpenPosition(String symbol, long quantity, double entryPrice, long entryTime, double pointValue, long currentTime) {
		this.symbol = symbol;
		this.quantity = quantity;
		this.entryPrice = entryPrice;
		this.entryTime = entryTime;
		this.pointValue = pointValue;
		this.currentTime = currentTime;
	}
	
	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}
	
	public long getCurrentTime() {
		return currentTime;
	}

	@Override
	public String toString() {
		return "OpenPosition [symbol=" + symbol + ", quantity=" + quantity + ", entryPrice=" + entryPrice
				+ ", entryTime=" + entryTime + ", pointValue=" + pointValue + ", currentTime=" + currentTime + "]";
	}
}
