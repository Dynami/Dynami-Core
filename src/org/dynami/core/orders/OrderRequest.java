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

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.dynami.core.assets.Book;
import org.dynami.core.services.IOrderService;
import org.dynami.core.utils.DUtils;

public class OrderRequest {
	public final long id;
	public final long time;
	public final String symbol;
	public final double price;
	public final long quantity;
	public final String note;
	public final IOrderService.IOrderHandler handler;
	public final AtomicReference<IOrderService.Status> status = new AtomicReference<IOrderService.Status>(IOrderService.Status.Pending);
	public final AtomicLong executionTime = new AtomicLong(0L);

	public OrderRequest(long id, long time, String symbol, long quantity, double price, String note, IOrderService.IOrderHandler handler){
		this.time = time;
		this.symbol = symbol;
		this.price = price;
		this.quantity = quantity;
		this.note = note;
		this.id = id;
		this.handler = handler;
	}

	public void updateStatus(IOrderService.Status status){
		this.status.set(status);
	}
	
	public void setExecutionTime(long time){
		executionTime.set(time);
	}
	
	public long getExecutionTime(){
		return executionTime.get();
	}

	public long getId() {
		return id;
	}

	public IOrderService.Status getStatus() {
		return status.get();
	}

//	public OrderRequest add(IOrderCondition condition){
//		conditions.add(condition);
//		return this;
//	}

//	public Collection<IOrderCondition> conditions(){
//		return Collections.unmodifiableCollection(conditions);
//	}


	public static interface IOrderCondition {
		public boolean check(long quantity, Book.Orders bid, Book.Orders ask);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderRequest other = (OrderRequest) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderRequest [id=" + id + ", time=" + DUtils.LONG_DATE_FORMAT.format(time) + ", symbol=" + symbol + ", price=" + String.format("%5.2f", price) + ", quantity="
				+ quantity + ", note=" + note + "]";
	}
}
