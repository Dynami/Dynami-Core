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

import org.dynami.core.assets.Asset.Option;
import org.dynami.core.utils.DUtils;

public class OptionChain {
	private final String parentSymbol;
	private long frontMonthExpire = Long.MAX_VALUE, farMonthExpire = 0;
	private final List<OptionRecord> options = new ArrayList<>();
	
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
	
	public OptionChain(String parentSymbol) {
		this.parentSymbol = parentSymbol;
	}
	
	public String getParentSymbol() {
		return parentSymbol;
	}
	
	public void add(Asset.Option opt){
		options.add(new OptionRecord(opt.expire, opt.strike, opt.type, opt));
		
		if(frontMonthExpire > opt.expire) frontMonthExpire = opt.expire;
		if(farMonthExpire < opt.expire) farMonthExpire = opt.expire;
		
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
		OptionRecord[] subset = options.stream()
									.filter(o->o.expire == expire)
									.filter(o->o.type.equals(type))
									.sorted((o1, o2)-> (int)(o1.strike-o2.strike))
									.toArray(OptionRecord[]::new);
		
		OptionRecord upper = subset[subset.length-1], lower = subset[0];
		
		for(int i = 0; i < subset.length; i++){
			if(subset[i].strike <= price) lower = subset[i];
			if(subset[subset.length-i-1].strike >= price) upper = subset[subset.length-i-1];
		}
		
		long upperDistance = upper.strike - price;
		long lowerDistance = price - lower.strike;
		
		if(upperDistance < lowerDistance){
			return upper.option;
		} else {
			return lower.option;
		}
	}
	
	private void checkExpirationDate(long expire){
		assert expire >= frontMonthExpire && expire <= farMonthExpire : "Expiration date ["+expire+"] - "+DUtils.DATE_FORMAT.format(expire)+" is out of range";
	}
}
