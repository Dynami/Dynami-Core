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

import java.util.TreeMap;

import org.dynami.core.utils.DUtils;

public class OptionChain {
	private final String parentSymbol;
	private final TreeMap<Long, TreeMap<Long, TreeMap<Asset.Option.Type, Asset.Option>>> options_tree = new TreeMap<>();
	private long frontMonthExpire = Long.MAX_VALUE, farMonthExpire = 0;
//	private final List<OptionRecord> options = new ArrayList<>(); 
	
//	static class OptionRecord {
//		private final long expire;
//		private final long strike;
//		private final Option.Type type;
//		private final Option option;
//		
//		public OptionRecord(long expire, long strike, Option.Type type, Option option) {
//			this.expire = expire;
//			this.strike = strike;
//			this.type = type;
//			this.option = option;
//		}
//
//		public long getExpire() { return expire; }
//		public long getPrice() { return strike; }
//		public Option.Type getType() { return type; }
//		public Option getOption() { return option; }
//	}
	
	public OptionChain(String parentSymbol) {
		this.parentSymbol = parentSymbol;
	}
	
	public String getParentSymbol() {
		return parentSymbol;
	}
	
	public void add(Asset.Option opt){
		options_tree.putIfAbsent(opt.expire, new TreeMap<Long, TreeMap<Asset.Option.Type, Asset.Option>>());
		options_tree.get(opt.expire).putIfAbsent(opt.strike, new TreeMap<Asset.Option.Type,Asset.Option>());
		options_tree.get(opt.expire).get(opt.strike).putIfAbsent(opt.type, opt);
		
//		options.add(new OptionRecord(opt.expire, opt.strike, opt.type, opt));
		
		frontMonthExpire = options_tree.firstKey();
		farMonthExpire = options_tree.lastKey();
		
//		if(frontMonthExpire > opt.expire) frontMonthExpire = opt.expire;
//		if(farMonthExpire < opt.expire) farMonthExpire = opt.expire;
		
	}
	
	public Asset.Option getCall(long price){
		return getOptionAtPrice(frontMonthExpire, Asset.Option.Type.CALL, price);
	}
	
	public Asset.Option getPut(long price){
		return getOptionAtPrice(frontMonthExpire, Asset.Option.Type.PUT, price);
	}
	
	public Asset.Option getCall(long expire, long price){
		checkExpirationDate(expire);
		return getOptionAtPrice(expire, Asset.Option.Type.CALL, price);
	}	
	
	public Asset.Option getPut(long expire, long price){
		checkExpirationDate(expire);
		return getOptionAtPrice(expire, Asset.Option.Type.PUT, price);
	}
	
	private Asset.Option getOptionAtPrice(final long expire, final Asset.Option.Type type, final long price){
		long upperStrike = options_tree.get(expire).ceilingKey(price);
		long lowerStrike = options_tree.get(expire).floorKey(price);
		long upperDistance = upperStrike - price;
		long lowerDistance = price - lowerStrike;
		if(upperDistance <= lowerDistance){
			return options_tree.get(expire).get(upperStrike).get(type);
		} else {
			return options_tree.get(expire).get(lowerStrike).get(type);
		}
	}
	
	private void checkExpirationDate(long expire){
		assert expire >= frontMonthExpire && expire <= farMonthExpire : "Expiration date ["+expire+"] - "+DUtils.DATE_FORMAT.format(expire)+" is out of range";
	}
}
