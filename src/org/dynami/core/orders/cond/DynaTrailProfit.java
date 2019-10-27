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

public class DynaTrailProfit implements IOrderCondition {
	private final double entryPrice;
	private final double trail;
	private double dynaTrail;
	private final double DOWN_COEFF;
	private final double UP_COEFF;
	private final double MIN_TRAIL;
	private double exitLongPrice, exitShortPrice;
	
	public DynaTrailProfit(double entryPrice, double trail, double acceleration, double minTrail) {
		this.entryPrice = entryPrice;
		this.trail = trail;
		this.dynaTrail = trail;
		this.DOWN_COEFF =  1. - acceleration;
		this.UP_COEFF = 1. + acceleration;
		this.MIN_TRAIL = minTrail;
		this.exitLongPrice = entryPrice - trail;
		this.exitShortPrice = entryPrice + trail;
	}
	
	public DynaTrailProfit(double entryPrice, double trail, double minTrail) {
		this(entryPrice, trail, 0.1, minTrail);
	}

	@Override
	public boolean check(long quantity, Orders bid, Orders ask) {
		final double price = (quantity > 0) ? bid.price : ask.price;
//		System.out.println(String.format("TrailProfit.check() %1s\t%.5f\t%.5f\t%.5f\t--> %.5f", 
//				(quantity>0?"L":"S"), 
//				entryPrice,
//				price, 
//				(quantity*(price-entryPrice)),
//				((quantity > 0)? Math.max(entryPrice, exitLongPrice) : Math.min(entryPrice, exitShortPrice)) 
//				));
		if(quantity > 0 ){
			if(exitLongPrice + dynaTrail < price){
				dynaTrail *= UP_COEFF;
				dynaTrail = Math.min(trail, dynaTrail);
				
				exitLongPrice = price - dynaTrail;
			} else {
				dynaTrail *= DOWN_COEFF;
				dynaTrail = Math.max(MIN_TRAIL, dynaTrail);
			}
			return (entryPrice < exitLongPrice &&  Math.max(entryPrice, exitLongPrice) > price);
		} else {
			if(exitShortPrice - dynaTrail > price){
				dynaTrail *= UP_COEFF;
				dynaTrail = Math.min(trail, dynaTrail);
				
				exitShortPrice = price + dynaTrail;
			} else {
				dynaTrail *= DOWN_COEFF;
				dynaTrail = Math.max(MIN_TRAIL, dynaTrail);
			}
			return (entryPrice > exitShortPrice &&  Math.min(entryPrice, exitShortPrice) < price);
		}
	}
	
	@Override
	public String toString() {
		return "TrailProfit@"+String.format("%5.5f", exitLongPrice);
	}
}
