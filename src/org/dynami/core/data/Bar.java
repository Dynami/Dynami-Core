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
package org.dynami.core.data;

import org.dynami.core.utils.DUtils;

public class Bar implements Comparable<Bar> {
	
	public final String symbol;
	public final long time;
	public final long open;
	public final long high;
	public final long low;
	public final long close;
	public final long volume;
	public final long openInterest;
	
	public Bar(final String symbol, final long open, final long high, final long low, final long close, final long volume, final long openInterest, final long time){
		this.symbol = symbol;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.openInterest = openInterest;
		this.time = time;
	}
	
	public Bar(final String symbol, final long open, final long high, final long low, final long close, final long volume, final long time){
		this(symbol, open, high, low, close, volume, 0, time);
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public long getOpen() {
		return open;
	}
	public long getHigh() {
		return high;
	}
	public long getLow() {
		return low;
	}
	public long getClose() {
		return close;
	}
	public long getVolume() {
		return volume;
	}
	
	public long getOpenInterest() {
		return openInterest;
	}
	
	public long getTime() {
		return time;
	}
	
	@Override
	public int compareTo(Bar o) {
		return Long.compare(time, o.time);
	}

	@Override
	public String toString() {
		return symbol + " " + DUtils.LONG_DATE_FORMAT.format(time) + " O:" + open + " H:" + high + " L:" + low
				+ " C:" + close + " V:" + volume + " OI:" + openInterest;
	}
}
