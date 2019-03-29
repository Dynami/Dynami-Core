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
package org.dynami.core.orders.cond;

import org.dynami.core.assets.Book.Orders;
import org.dynami.core.orders.OrderRequest.IOrderCondition;

public class TrailProfit implements IOrderCondition {
	private final double entryPrice;
	private final double trail;
	private double exitLongPrice, exitShortPrice;
	
	public TrailProfit(double entryPrice, double trail) {
		this.entryPrice = entryPrice;
		this.trail = trail;
		this.exitLongPrice = entryPrice - trail;
		this.exitShortPrice = entryPrice + trail;
	}

	@Override
	public boolean check(long quantity, Orders bid, Orders ask) {
		final double price = (quantity > 0) ? bid.price : ask.price;
		if(quantity > 0 ){
			if(exitLongPrice + trail < price){
				exitLongPrice = price - trail;
			}
			return (entryPrice < exitLongPrice && exitLongPrice > price);
		} else {
			if(exitShortPrice - trail > price){
				exitShortPrice = price - trail;
			}
			return (entryPrice > exitShortPrice && exitShortPrice < price);
		}
	}
	
	@Override
	public String toString() {
		return "TrailProfit@"+String.format("%5.2f", exitLongPrice);
	}
}
