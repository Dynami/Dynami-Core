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

import java.util.function.BiFunction;

import org.dynami.core.data.IPricingEngine;
import org.dynami.core.utils.DUtils;

public abstract class Asset implements Comparable<Asset> {
	public final Family family;
	public final Market market;
	public final String symbol;
	public final String isin;
	public final String name;
	public final double pointValue;
	public final double tick;
	
	public int compareTo(Asset o) {
		return symbol.compareTo(o.symbol);
	};
	
	public boolean equals(Object obj) {
		if(obj instanceof Asset){
			return symbol.equals(((Asset)obj).symbol);
		}
		return super.equals(obj);
	};
	
	public Asset.Tradable asTradable(){
		if(this instanceof Asset.Tradable)
			return (Asset.Tradable)this;
		else
			return null;
	}
	
	public boolean is(Family family){
		return this.family.equals(family);
	}
	
	private Asset(Family family, String symbol, String isin, String name, double pointValue, double tick, Market market){
		this.family = family;
		this.symbol = symbol;
		this.isin = isin;
		this.name = name;
		this.pointValue = pointValue;
		this.tick = tick;
		this.market = market;
	}
	
	public static class Index extends Asset {
		public Index(Family family, String symbol, String isin, String name, double pointValue, double tick, Market market) {
			super(family, symbol, isin, name, pointValue, tick, market);
		}
	}

	public static abstract class Tradable extends Asset {
		public final double requiredMargin;
		public final Book book = new Book();
		public final BiFunction<Book.Orders, Book.Orders, Double> lastPriceEngine;
		public Tradable(Family family, String symbol, String isin, String name, double pointValue, double tick, Market market, final double requiredMargin, final BiFunction<Book.Orders, Book.Orders, Double> lastPriceEngine) {
			super(family, symbol, isin, name, pointValue, tick, market);
			this.requiredMargin = requiredMargin;
			this.lastPriceEngine = lastPriceEngine;
		}
		
		public double lastPrice(){
			return lastPriceEngine.apply(book.bid(), book.ask());
		}
		
		public double getValueAt(double price, long time, IPricingEngine pricingEngine){
			return price;
		}
	}
	
	public static class Share extends Tradable {
		public Share(String symbol, String isin, String name, double pointValue, double tick, double requiredMargin, Market market, final BiFunction<Book.Orders, Book.Orders, Double> lastPriceEngine) {
			super(Family.Equity, symbol, isin, name, pointValue, tick, market, requiredMargin, lastPriceEngine);
		}
	}
	
	public static abstract class DerivativeInstr extends Tradable {
		public final String parentSymbol;
		public DerivativeInstr(Family family, String symbol, String isin, String name, double pointValue, double tick, Market market, double requiredMargin, final BiFunction<Book.Orders, Book.Orders, Double> lastPriceEngine, String parentSymbol) {
			super(family, symbol, isin, name, pointValue, tick, market, requiredMargin, lastPriceEngine);
			this.parentSymbol = parentSymbol;
		}
	}
	
	public static abstract class ExpiringInstr extends DerivativeInstr {
		public final long expire;
		public final long lotSize;
		public final RiskFreeRate riskFreeRate;
		
		public ExpiringInstr(Family family, String symbol, String isin, String name, double pointValue, double tick, Market market, double requiredMargin, final BiFunction<Book.Orders, Book.Orders, Double> lastPriceEngine, long expire, long lotSize, String parentSymbol, RiskFreeRate riskFreeRate) {
			super(family, symbol, isin, name, pointValue, tick, market, requiredMargin, lastPriceEngine, parentSymbol);
			this.expire = expire;
			this.lotSize = lotSize;
			this.riskFreeRate = riskFreeRate;
		}
		
		public boolean isExpired(long date){
			return expire < date;
		}
		
		public int daysToExpiration(long date){
			return (int)((expire-date)/DUtils.DAY_MILLIS);
		}
	}
	
	public static class Option extends ExpiringInstr {
		public final double strike;
		public final Type type;
		public final Greeks greeks = new Greeks();
		public final Exercise exercise;
		public final Greeks.Engine greeksEngine;
		public final Greeks.ImpliedVolatility implVola;
		private double volatility;
		public static enum Type { CALL, PUT }
		public static enum Exercise {European, American}
		
		public Option(String symbol, String isin, String name, double pointValue, double tick, 
				double requiredMargin, 
				final BiFunction<Book.Orders, Book.Orders, Double> lastPriceEngine,  
				Market market, long expire, long lotSize, String parentSymbol, RiskFreeRate riskFreeRate, 
				double strike, Type type,  Exercise exercise, 
				Greeks.Engine greeksEngine, 
				Greeks.ImpliedVolatility implVola){
			super(Family.Option, symbol, isin, name, pointValue, tick, market, requiredMargin, lastPriceEngine, expire, lotSize, parentSymbol, riskFreeRate);
			this.strike = strike;
			this.type = type;
			this.greeksEngine = greeksEngine;
			this.implVola = implVola;
			this.exercise = exercise;
			
			book.addBookListener((ask, bid)->{
				if(ask != null && bid != null){
					double rf = riskFreeRate.get();
					double optionMidPrice = lastPriceEngine.apply(bid, ask);
					long time = Math.max(ask.time, bid.time);
					volatility = implVola.estimate(parentSymbol, time, type, expire, strike, optionMidPrice, rf);
					greeksEngine.evaluate(greeks, parentSymbol, time, type, expire, strike,  volatility, rf);
				}
			});
		}
		
		public Greeks greeks() {
			return greeks;
		}
		
		public double getStrike() {
			return strike;
		}
		
		public Type getType() {
			return type;
		}
		public double getVolatility() {
			return volatility;
		}
		// FIXME - TO REMOVE!!!!
//		public void setVolatility(double vola) {
//			volatility = vola; 
//		}
		
		public Exercise getExercise(){
			return exercise;
		}
		
		public double getValueAtExpiration(double price){
			if(Type.CALL.equals(type)){
				return Math.max(price-strike, 0);
			} else {
				return Math.max(strike-price, 0);
			}
		}
		
		@Override
		public double getValueAt(double price, long time, IPricingEngine pricingEngine){
			if(time > expire){
				return 0.;
			} else {
				return pricingEngine.compute(this, time, price, getVolatility(), riskFreeRate.get());
			}
		}
	}
	
	public static class Future extends ExpiringInstr {
		public Future(String symbol, String isin, String name, double pointValue, double tick, double requiredMargin,
				final BiFunction<Book.Orders, Book.Orders, Double> lastPriceEngine,
				Market market, long expire, long lotSize, String parentSymbol, RiskFreeRate riskFreeRate) {
			super(Family.Future, symbol, isin, name, pointValue, tick, market, requiredMargin, lastPriceEngine, expire, lotSize, parentSymbol, riskFreeRate);
		}
	}
	
	@FunctionalInterface
	public static interface RiskFreeRate {
		public double get();
	}
	
	public enum Family{
		Index, Equity, Future, Option;
	}
}
