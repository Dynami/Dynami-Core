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
	private final double startTrail;
	private final double threshold;
	private double exitThreshold ;
	
	public TrailProfit(double startTrail, double trail) {
		this.startTrail = startTrail;
		threshold = trail;
		exitThreshold = startTrail-trail;
	}

	@Override
	public boolean check(long quantity, Orders bid, Orders ask) {
		if(quantity > 0 ){
			if(exitThreshold+threshold < bid.price){
				exitThreshold = bid.price-threshold;
			}
		} else {
			if(exitThreshold-threshold > ask.price){
				exitThreshold = ask.price-threshold;
			}
		}
		
		if(quantity > 0){
			return (exitThreshold > bid.price && startTrail < exitThreshold);
		} else {
			return (exitThreshold < ask.price && startTrail > exitThreshold);
		}
	}
	
	@Override
	public String toString() {
		return "TrailProfit@"+String.format("%5.2f", exitThreshold);
	}
}
