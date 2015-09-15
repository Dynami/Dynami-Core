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
	
	/**
	 * @see {@link COMPRESSION_UNIT}
	 * @return
	 */
	public long getCompression();
	
	public IData changeCompression(long compression);
	
	public static class COMPRESSION_UNIT {
		public static final long TICK = 0L;
		public static final long SECOND = 1000L;
		public static final long MINUTE = 60*SECOND;
		public static final long HOUR = 60*MINUTE;
		public static final long DAY = 24*HOUR;
		public static final long WEEK = 7*DAY;
	}
}
