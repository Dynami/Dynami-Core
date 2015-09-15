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

public abstract class Asset implements Comparable<Asset> {
	public final Family family;
	public final String market;
	public final String symbol;
	public final String isin;
	public final String name;
	public final double pointValue;
	public final long tick;
	
	public int compareTo(Asset o) {
		return symbol.compareTo(o.symbol);
	};
	
	public boolean equals(Object obj) {
		if(obj instanceof Asset){
			return symbol.equals(((Asset)obj).symbol);
		}
		return super.equals(obj);
	};
	
	private Asset(Family family, String symbol, String isin, String name, double pointValue, long tick, String market){
		this.family = family;
		this.symbol = symbol;
		this.isin = isin;
		this.name = name;
		this.pointValue = pointValue;
		this.tick = tick;
		this.market = market;
	}
	
	public static class Index extends Asset {
		public Index(Family family, String symbol, String isin, String name, double pointValue, long tick, String market) {
			super(family, symbol, isin, name, pointValue, tick, market);
		}
	}

	public static abstract class Tradable extends Asset {
		public final double requiredMargin;
		public final Book book = new Book();
		public Tradable(Family family, String symbol, String isin, String name, double pointValue, long tick, String market, final double requiredMargin) {
			super(family, symbol, isin, name, pointValue, tick, market);
			this.requiredMargin = requiredMargin;
		}
	}
	
	public static class Share extends Tradable {
		public Share(String symbol, String isin, String name, double pointValue, long tick, double requiredMargin, String market) {
			super(Family.Equity, symbol, isin, name, pointValue, tick, market, requiredMargin);
		}
	}
	
	public static abstract class DerivativeInstr extends Tradable {
		public final String parentSymbol;
		public DerivativeInstr(Family family, String symbol, String isin, String name, double pointValue, long tick, String market, double requiredMargin, String parentSymbol) {
			super(family, symbol, isin, name, pointValue, tick, market, requiredMargin);
			this.parentSymbol = parentSymbol;
		}
	}
	
	private static abstract class ExpiringInstr extends DerivativeInstr {
		public final long expire;
		public final long lotSize;
		public ExpiringInstr(Family family, String symbol, String isin, String name, double pointValue, long tick, String market, double requiredMargin, long expire, long lotSize, String parentSymbol) {
			super(family, symbol, isin, name, pointValue, tick, market, requiredMargin, parentSymbol);
			this.expire = expire;
			this.lotSize = lotSize;
		}
	}
	
	public static class Option extends ExpiringInstr {
		public final long strike;
		public final Type type;
		public final Greeks greeks;
		public static enum Type { CALL, PUT }
		
		public Option(String symbol, String isin, String name, double pointValue, long tick, double requiredMargin,  String market, long expire, long lotSize, String parentSymbol, long strike, Type type, Greeks.Engine greeksEngine){
			super(Family.Option, symbol, isin, name, pointValue, tick, market, requiredMargin, expire, lotSize, parentSymbol);
			this.strike = strike;
			this.type = type;
			this.greeks = new Greeks(greeksEngine);
		}
		
		public Greeks greeks() {
			return greeks;
		}
	}
	
	public static class Future extends ExpiringInstr {
		public Future(String symbol, String isin, String name, double pointValue, long tick, double requiredMargin, String market, long expire, long lotSize, String parentSymbol) {
			super(Family.Future, symbol, isin, name, pointValue, tick, market, requiredMargin, expire, lotSize, parentSymbol);
		}
	}
	
	public enum Family{
		Index, Equity, Future, Option;
	}
}
