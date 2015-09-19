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

public class ClosedPosition {
	public final String symbol;
	public final long quantity;
	public final double entryPrice;
	public final long entryTime;
	public final double exitPrice;
	public final long exitTime;
	public final double pointValue;
	
	public ClosedPosition(OpenPosition o, double exitPrice, long exitTime){
		this(o.symbol, o.quantity, o.entryPrice, o.entryTime, exitPrice, exitTime, o.pointValue);
	}
	
	public ClosedPosition(String symbol, long quantity, double entryPrice, long entryTime, double exitPrice, long exitTime, double pointValue) {
		this.symbol = symbol;
		this.quantity = quantity;
		this.entryPrice = entryPrice;
		this.entryTime = entryTime;
		this.exitPrice = exitPrice;
		this.exitTime = exitTime;
		this.pointValue = pointValue;
	}
	
	public double roi(){
		return (exitPrice-entryPrice)*quantity*pointValue;
	}

	@Override
	public String toString() {
		return "ClosedPosition [symbol=" + symbol + ", quantity=" + quantity + ", entryPrice=" + entryPrice
				+ ", entryTime=" + entryTime + ", exitPrice=" + exitPrice + ", exitTime=" + exitTime + ", pointValue="
				+ pointValue + "]";
	}
}
