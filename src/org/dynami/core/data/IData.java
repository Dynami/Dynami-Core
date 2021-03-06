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

import org.dynami.core.assets.Market;

public interface IData {
	public Series open();

	public Series high();

	public Series low();
	
	public Series close();

	public Series volume();

	public Series openInterest();
	
	public double getMax();

	public double getMin();

	public long getBegin();

	public long getEnd();
	
	public Bar last();
	
	public Bar get(int idx);

	public Bar[] toArray();

	public int size();

	public Bar getByTime(long time);

	public IData getPeriod(long begin, long end);
	
	public IData getLastBars(int number);
	
	/**
	 * @see {@link COMPRESSION_UNIT}
	 * @return
	 */
	public long getCompression();
	
	public IData changeCompression(long compression);
	
	
	public default double getVolatility(IVolatilityEngine engine, int period){
		return engine.compute(this, period);
	}
	
	public default double getAnnualizedVolatility(IVolatilityEngine engine, int period, Market market){
		setAutoCompressionRate();
		return engine.compute(this, period)*engine.annualizationFactor(getCompression(), period, market);
	}
	
	public boolean isCompressionRateSat();
	
	public boolean setAutoCompressionRate();
	
	public static enum TimeUnit {
		Tick(0),
		Second(1_000L),
		Minute(Second.tu*60),
		Hour(Minute.tu*60),
		Day(Hour.tu*24),
		Week(Day.tu*7);
		
		final long tu;
		TimeUnit(long compression) {
			this.tu = compression;
		}
		
		public long millis(){
			return this.tu;
		}
		
		public static TimeUnit getTimeUnit(long compression){
			if(compression >= TimeUnit.Week.millis()){
				return TimeUnit.Week;
			} else if(compression >= TimeUnit.Day.millis()){
				return TimeUnit.Day;
			} else if(compression >= TimeUnit.Hour.millis()){
				return TimeUnit.Hour;
			} else if(compression >= TimeUnit.Minute.millis()){
				return TimeUnit.Minute;
			} else if(compression >= TimeUnit.Second.millis()){
				return TimeUnit.Second;
			} else {
				return TimeUnit.Tick;
			}
		}
		
		public static long getUnits(long compression){
			if(compression >= TimeUnit.Week.millis()){
				return compression/TimeUnit.Week.millis();
			} else if(compression >= TimeUnit.Day.millis()){
				return compression/TimeUnit.Day.millis();
			} else if(compression >= TimeUnit.Hour.millis()){
				return compression/TimeUnit.Hour.millis();
			} else if(compression >= TimeUnit.Minute.millis()){
				return compression/TimeUnit.Minute.millis();
			} else if(compression >= TimeUnit.Second.millis()){
				return compression/TimeUnit.Second.millis();
			} else {
				return 0L;
			}
		}
	}
	
//	public static class COMPRESSION_UNIT {
//		public static final long TICK = 0L;
//		public static final long SECOND = 1000L;
//		public static final long MINUTE = 60*SECOND;
//		public static final long HOUR = 60*MINUTE;
//		public static final long DAY = 24*HOUR;
//		public static final long WEEK = 7*DAY;
//	}
}
