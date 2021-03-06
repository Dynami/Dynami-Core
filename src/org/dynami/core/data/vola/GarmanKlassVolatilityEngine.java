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
package org.dynami.core.data.vola;

import org.dynami.core.data.Bar;
import org.dynami.core.data.IData;
import org.dynami.core.data.IVolatilityEngine;

public class GarmanKlassVolatilityEngine implements IVolatilityEngine {
	
	@Override
	public double compute(IData data, int period) {
		int size = data.size();
		if(size >= period){
			double r_pow = 0, sigma;
			Bar bar;
			final double c = 2.*Math.log(2.)-1;
			for(int i = size-period; i < size; i++){
				bar = data.get(i);
				r_pow += (0.5*Math.pow(Math.log(bar.close/bar.low), 2)) 
						- c*Math.pow(Math.log(bar.close/bar.open), 2);
			}
			sigma = Math.sqrt(r_pow);
			return sigma;
		}
		return 0;
	}
}
