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
package org.dynami.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.atomic.AtomicLong;

import org.dynami.core.assets.Book;
import org.dynami.core.data.Bar;

public class Event implements Comparable<Event> {
	public final long time;
	public final long id;
	public final String symbol;
	public final int types;
	public final Bar bar;
	public final Book.Orders item;

	private Event(long id, long time, String symbol, Bar bar, Book.Orders item, Type...type) {
		this.id = id;
		this.symbol = symbol;
		this.bar = bar;
		this.item = item;
		int sum = 0;
		this.time = time; //(type != null && type.length > 0 && type[0].equals(Type.NoMoreData))?0:((bar != null)?bar.time:item.time);
		for(Type t:type){
			sum |= t.value();
		}
		types = sum;
	}

	public boolean is(Type type){
		return (types^type.value()) < types;
	}

	public boolean isOneOfThese(Type...type){
		for(Type t:type){
			if(is(t)) return true;
		}
		return false;
	}


	@Override
	public int compareTo(Event o) {
		return (symbol+"."+types).compareTo(o.symbol+"."+o.types);
	}

	/**
	 * Handler method needs IDyanmi as only mandatory parameter
	 * @author Atria
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Handler {
		Type type() default Type.OnBarClose;
		String symbol() default "*";
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface OnBarOpen {
		String[] symbol() default "*";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface OnDayOpen {
		String[] symbol() default "*";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface OnDayClose {
		String[] symbol() default "*";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface OnTick {
		String[] symbol() default "*";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface OnBarClose {
		String[] symbol() default "*";
		/**
		 * Defines bar time frame using the following notation:<b>[number]*[unit symbol]</b><br>
		 * E.g. <b>5*m</b> stands for 5 minutes time frame<br>
		 *
		 * Useful unit symbols are:<br>
		 * <ul>
		 * <li><b>s</b>: stands for second</li>
		 * <li><b>m</b>: stands for minute</li>
		 * <li><b>h</b>: stands for hour 0-24 format</li>
		 * <li><b>D</b>: stands for day</li>
		 * <li><b>W</b>: stands for week</li>
		 * <li><b>M</b>: stands for month</li>
		 * </ul>
		 *
		 *
		 * @return
		 */
		//String timeFrame() default "1*m";
	}

	public static enum Type {
		OnBarClose(1),
		OnBarOpen(2),
		OnDayClose(4),
		OnDayOpen(8),
		OnTick(16),
		NoMoreData(32);

		private final int idx;
		Type(int i){
			idx = i;
		}

		int value() {
			return idx;
		}
	}

	public final static class Factory {
		private static final AtomicLong count = new AtomicLong(0L);

		public static Event create(String symbol, long time, Bar bar, Type...type){
			return new Event(count.incrementAndGet(), time, symbol, bar, null, type);
		}

		public static Event create(String symbol, Book.Orders item){
			long time = item.time;
			return new Event(count.incrementAndGet(), time, symbol, null, item, Type.OnTick);
		}

		public static Event noMoreDataEvent(String symbol){
			long time = 0L;
			return new Event(count.incrementAndGet(), time, symbol, null, null, Type.NoMoreData);
		}
	}
}
