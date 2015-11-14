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
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dynami.core.assets.Asset.Option;
import org.dynami.core.utils.DUtils;

public class OptionChain {
	private final String parentSymbol;
	private final List<OptionRecord> options = new ArrayList<>(100);
	private final SortedSet<Long> expirations = new TreeSet<>();
	
	public OptionChain(String parentSymbol, Asset.Option... options) {
		this.parentSymbol = parentSymbol;
		for(Asset.Option o:options){
			add(o);
		}
	}
	
	private OptionRecord[] getByExpire(long expire){
		return options.stream()
				.filter(r->r.expire == expire)
				.sorted((r1, r2)->Long.compare(r1.expire, r2.expire))
				.toArray(OptionRecord[]::new);
	}
	
	private OptionRecord[] getByStrike(long strike){
		return options.stream()
				.filter(r->r.strike == strike)
				.sorted((r1, r2)->Long.compare(r1.strike, r2.strike))
				.toArray(OptionRecord[]::new);
	}
	
	public Asset.Option upperStrike(Asset.Option opt){
		final OptionRecord[] items = getByExpire(opt.expire);
		final int idx = Arrays.binarySearch(items, opt, (r1, r2)->Long.compare(((OptionRecord)r1).strike, ((OptionRecord)r1).strike));
		return (idx >= 0 && idx < items.length-1)?items[idx+1].option:null;
	}
	
	public Asset.Option lowerStrike(Asset.Option opt){
		final OptionRecord[] items = getByExpire(opt.expire);
		final int idx = Arrays.binarySearch(items, opt, (r1, r2)->Long.compare(((OptionRecord)r1).strike, ((OptionRecord)r1).strike));
		return (idx > 0 && idx < items.length)?items[idx-1].option:null;
	}
	
	public Asset.Option forwardExpiration(Asset.Option opt){
		final OptionRecord[] items = getByStrike(DUtils.d2l(opt.strike));
		final int idx = Arrays.binarySearch(items, opt, (r1, r2)->Long.compare(((OptionRecord)r1).expire, ((OptionRecord)r1).expire));
		return (idx >= 0 && idx < items.length-1)?items[idx+1].option:null;
	}
	
	public Asset.Option backwardExpiration(Asset.Option opt){
		final OptionRecord[] items = getByStrike(DUtils.d2l(opt.strike));
		final int idx = Arrays.binarySearch(items, opt, (r1, r2)->Long.compare(((OptionRecord)r1).expire, ((OptionRecord)r1).expire));
		return (idx > 0 && idx < items.length)?items[idx-1].option:null;
	}
	
	public String getParentSymbol() {
		return parentSymbol;
	}
	
	private void add(Asset.Option opt){
		options.add(new OptionRecord(opt.expire, DUtils.d2l(opt.strike), opt.type, opt));
		expirations.add(opt.expire);
	}
	
	public Asset.Option getCall(double price){
		return getOptionAtPrice(expirations.first(), Asset.Option.Type.CALL, price);
	}
	
	public Asset.Option getPut(double price){
		return getOptionAtPrice(expirations.first(), Asset.Option.Type.PUT, price);
	}
	
	public Asset.Option getCall(long expire, double price){
		checkExpirationDate(expire);
		return getOptionAtPrice(expire, Asset.Option.Type.CALL, price);
	}	
	
	public Asset.Option getPut(long expire, double price){
		checkExpirationDate(expire);
		return getOptionAtPrice(expire, Asset.Option.Type.PUT, price);
	}

	public Asset.Option getCallAtDelta(long expire, double delta){
		return getOptionAtDelta(expire, Option.Type.CALL, delta);
	}

	public Asset.Option getCallAtDelta(double delta){
		return getOptionAtDelta(expirations.first(), Option.Type.CALL, delta);
	}


	public Asset.Option getPutAtDelta(long expire, double delta){
		return getOptionAtDelta(expire, Option.Type.PUT, delta);
	}

	public Asset.Option getPutAtDelta(double delta){
		return getOptionAtDelta(expirations.first(), Option.Type.PUT, delta);
	}
	
	private Asset.Option getOptionAtPrice(final long expire, final Asset.Option.Type type, final double price){
		long _price = DUtils.d2l(price);
		OptionRecord[] subset = options.stream()
									.filter(o->o.expire == expire)
									.filter(o->o.type.equals(type))
									.sorted((o1, o2)-> Long.compare(o1.strike, o2.strike))
									.toArray(OptionRecord[]::new);
		
		OptionRecord upper = subset[subset.length-1], lower = subset[0];
		
		for(int i = 0; i < subset.length; i++){
			if(subset[i].strike <= _price) lower = subset[i];
			if(subset[subset.length-i-1].strike >= _price) upper = subset[subset.length-i-1];
		}
		
		long upperDistance = upper.strike - _price;
		long lowerDistance = _price - lower.strike;
		
		if(upperDistance < lowerDistance){
			return upper.option;
		} else {
			return lower.option;
		}
	}

	private Asset.Option getOptionAtDelta(final long expire, final Asset.Option.Type type, final double delta){
		long _delta = DUtils.d2l(delta);
		OptionRecord[] subset = options.stream()
				.filter(o->o.expire == expire)
				.filter(o->o.type.equals(type))
				.sorted((o1, o2)-> Double.compare(o1.option.greeks().delta(), o2.option.greeks().delta()))
				.toArray(OptionRecord[]::new);

		OptionRecord upper = subset[subset.length-1], lower = subset[0];

		for(int i = 0; i < subset.length; i++){
			if(subset[i].option.greeks().delta() <= _delta) lower = subset[i];
			if(subset[subset.length-i-1].option.greeks().delta() >= _delta) upper = subset[subset.length-i-1];
		}

		double upperDistance = upper.option.greeks().delta() - _delta;
		double lowerDistance = _delta - lower.option.greeks().delta();

		if(upperDistance < lowerDistance){
			return upper.option;
		} else {
			return lower.option;
		}
	}
	
	private void checkExpirationDate(long expire){
		assert expire >= expirations.first() && expire <= expirations.last() : "Expiration date ["+expire+"] - "+DUtils.DATE_FORMAT.format(expire)+" is out of range";
	}
	
	private static class OptionRecord {
		private final long expire;
		private final long strike;
		private final Option.Type type;
		private final Option option;
		
		public OptionRecord(long expire, long strike, Option.Type type, Option option) {
			this.expire = expire;
			this.strike = strike;
			this.type = type;
			this.option = option;
		}
	}
}
