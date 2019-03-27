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
package org.dynami.core.utils;

import java.util.function.BiFunction;

import org.dynami.core.assets.Book;

public class LastPriceEngine {
	
	public static final BiFunction<Book.Orders, Book.Orders, Double> MidPrice = new BiFunction<Book.Orders, Book.Orders, Double>(){
		
		public Double apply(Book.Orders bid, Book.Orders ask) {
			if(bid != null && ask != null){
				return (bid.price+ask.price)/2.;
			}
			return 0.;
		};
	};
	
	public static final BiFunction<Book.Orders, Book.Orders, Double> AskPrice = new BiFunction<Book.Orders, Book.Orders, Double>(){
		
		public Double apply(Book.Orders bid, Book.Orders ask) {
			if(ask != null){
				return ask.price;
			}
			return 0.;
		};
	};
	
	public static final BiFunction<Book.Orders, Book.Orders, Double> BidPrice = new BiFunction<Book.Orders, Book.Orders, Double>(){
		
		public Double apply(Book.Orders bid, Book.Orders ask) {
			if(bid != null){
				return bid.price;
			}
			return 0.;
		};
	};

}
