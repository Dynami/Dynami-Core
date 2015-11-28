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

import org.dynami.core.assets.Asset;
import org.dynami.core.utils.DUtils;

public class OpenPosition {
	public final Asset.Tradable asset;
	public final long quantity;
	public final double entryPrice;
	public final long entryTime;
	public long currentTime;
	
	public OpenPosition(Asset.Tradable asset, long quantity, double entryPrice, long entryTime, long currentTime) {
		this.asset = asset;
		this.quantity = quantity;
		this.entryPrice = entryPrice;
		this.entryTime = entryTime;
		this.currentTime = currentTime;
	}
	
	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}
	
	public double getCurrentPrice(){
		return (quantity > 0)?asset.book.bid().price:asset.book.ask().price;
	}
	
	public long getCurrentTime() {
		return currentTime;
	}

	@Override
	public String toString() {
		return "OpenPosition [family="+asset.family+", symbol=" + asset.symbol + ", quantity=" + quantity + ", entryPrice=" + String.format("%5.2f", entryPrice)
				+ ", entryTime=" + DUtils.LONG_DATE_FORMAT.format(entryTime) + ", pointValue=" + asset.pointValue + ", currentTime=" + DUtils.LONG_DATE_FORMAT.format(currentTime) + "]";
	}
}
