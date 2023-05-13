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
package org.dynami.core.assets;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.BiConsumer;

import org.dynami.core.bus.IMsg;

public class Book {
	
	private final int BOOK_DEEP = 5;
	public static final int NONE = -1;
	private transient final AtomicReferenceArray<Book.Orders> askSide = new AtomicReferenceArray<>(BOOK_DEEP);
	private transient final AtomicReferenceArray<Book.Orders> bidSide = new AtomicReferenceArray<>(BOOK_DEEP);
	
	private final List<BiConsumer<Book.Orders, Book.Orders>> listeners = new ArrayList<>();
	public void addBookListener(BiConsumer<Book.Orders, Book.Orders> listener){
		listeners.add(listener);
	}
	
	public final IMsg.Handler askBookOrdersHandler = new IMsg.Handler() {
		@Override
		public void update(boolean last, Object msg) {
			if(last && msg instanceof Orders){
				Orders item = (Orders)msg;
				askSide.set(item.level-1, item);
				/*
				if(item.price == NONE || item.quantity == NONE){
					final Orders prev = askSide.get(item.level-1);
					final double price = (item.price == -1)?prev.price : item.price;
					final long quantity = (item.quantity == -1)?prev.quantity:item.quantity;
					Orders newItem = new Orders(item.symbol, item.time, item.side, item.level, price, quantity);
					askSide.set(newItem.level-1, newItem);

				} else {
					askSide.set(item.level-1, item);
				}
				*/
				listeners.forEach(f->f.accept(ask(), bid()));
			}
		}
	};
	
	public final IMsg.Handler bidBookOrdersHandler = new IMsg.Handler() {
		@Override
		public void update(boolean last, Object msg) {
			if(last && msg instanceof Orders){
				Orders item = (Orders)msg;
				bidSide.set(item.level-1, item);
				/*
				if(item.price == NONE || item.quantity == NONE){
					final Orders prev = askSide.get(item.level-1);
					final double price = (item.price == -1)?prev.price : item.price;
					final long quantity = (item.quantity == -1)?prev.quantity:item.quantity;
					Orders newItem = new Orders(item.symbol, item.time, item.side, item.level, price, quantity);
					bidSide.set(newItem.level-1, newItem);

				} else {
					bidSide.set(item.level-1, item);
				}
				*/
				listeners.forEach(f->f.accept(ask(), bid()));
			}
		}
	};
	
	public Orders ask(){
		return askSide.get(0);
	}
	
	public Orders ask(int level){
		assert level >= 1 && level <= BOOK_DEEP : "Invalid book level";
		return askSide.get(level-1);
	}
	
	public Orders bid(){
		return bidSide.get(0);
	}
	
	public Orders bid(int level){
		assert level >= 1 && level <= BOOK_DEEP : "Invalid book level";
		return bidSide.get(level-1);
	}
	
	public Orders getBookOrder(Side side, int level){
		assert level >= 1 && level <= BOOK_DEEP : "Invalid book level";
		return Side.ASK.equals(side)?askSide.get(level-1):bidSide.get(level-1);
	}
	
	public static class Orders {
		public final String symbol;
		public final long time;
		public final Side side;
		public final int level;
		public final double price;
		public final long quantity;
		
		public Orders(String symbol, long time, Side side, int level, double price, long quantity) {
			this.symbol = symbol;
			this.time = time;
			this.side = side;
			this.level = level;
			this.price = price;
			this.quantity = quantity;
		}

		@Override
		public String toString() {
			return "Orders [symbol=" + symbol + ", time=" + time + ", side=" + side + ", level=" + level + ", price="
					+ price + ", quantity=" + quantity + "]";
		}
		
		
	}
	
	public static enum Side { ASK, BID }
	
//	@FunctionalInterface
//	public static interface QuotationListener {
//		public void changed(OrdersBook.Item bid, OrdersBook.Item ask);
//	}
}
