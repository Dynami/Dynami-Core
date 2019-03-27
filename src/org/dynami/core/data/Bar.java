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
	public final double open;
	public final double high;
	public final double low;
	public final double close;
	public final long volume;
	public final long openInterest;
	
	public Bar(final String symbol, final double open, final double high, final double low, final double close, final long volume, final long openInterest, final long time){
		this.symbol = symbol;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.openInterest = openInterest;
		this.time = time;
	}
	
	public Bar(final String symbol, final double open, final double high, final double low, final double close, final long volume, final long time){
		this(symbol, open, high, low, close, volume, 0, time);
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public double getOpen() {
		return open;
	}
	public double getHigh() {
		return high;
	}
	public double getLow() {
		return low;
	}
	public double getClose() {
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
		return String.format("%10s\t%18s\tO:%8.5f\tH:%8.5f\tL:%8.5f\tC:%8.5f", symbol, DUtils.LONG_DATE_FORMAT.format(time), open, high, low, close); //\tV:%12s\tOI:%12s , volume, openInterest
	}
	
	public String toCsv(String delim, boolean newLine) {
		StringBuilder builder = new StringBuilder();
		builder.append(symbol);
		builder.append(delim);
		builder.append(DUtils.LONG_DATE_FORMAT.format(time));
		builder.append(delim);
		builder.append(DUtils.NUMBER_FORMAT.format(open));
		builder.append(delim);
		builder.append(DUtils.NUMBER_FORMAT.format(high));
		builder.append(delim);
		builder.append(DUtils.NUMBER_FORMAT.format(low));
		builder.append(delim);
		builder.append(DUtils.NUMBER_FORMAT.format(close));
		if(newLine)
			builder.append('\n');
		
		return builder.toString();
	}
}
