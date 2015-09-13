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
package org.dynami.core.orders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.dynami.core.assets.Book;

/**
 * @author Atria
 *
 */
public abstract class OrderRequest {
	private static final AtomicLong counter = new AtomicLong(0);
	
	public final long id;
	public final long time;
	public final String symbol;
	public final long price;
	public final long quantity;
	public final String note;
	private final List<IOrderCondition> conditions = new ArrayList<>();
	
	public OrderRequest(String symbol, long quantity, long price, String note){
		this.time = System.currentTimeMillis();
		this.symbol = symbol;
		this.price = price;
		this.quantity = quantity;
		this.note = note;
		this.id = generateUniqueId();
	}
	
	public OrderRequest(String symbol, long quantity, long price){
		this.time = System.currentTimeMillis();
		this.symbol = symbol;
		this.price = price;
		this.quantity = quantity;
		this.note = "";
		this.id = generateUniqueId();
	}
	
	public long getId() {
		return id;
	}
		
	public OrderRequest add(IOrderCondition condition){
		conditions.add(condition);
		return this;
	}
	
	public Collection<IOrderCondition> conditions(){
		return Collections.unmodifiableCollection(conditions);
	}
	
	
	public static interface IOrderCondition {
		public boolean check(long quantity, Book.Orders bid, Book.Orders ask);
	}
	
	private long generateUniqueId() {
		return counter.getAndIncrement();
	}

	@Override
	public String toString() {
		return "OrderRequest [id=" + id + ", time=" + time + ", symbol=" + symbol + ", price=" + price + ", quantity="
				+ quantity + ", note=" + note + ", conditions=" + conditions + "]";
	}
	
	
}
