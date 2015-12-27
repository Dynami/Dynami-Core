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

import org.dynami.core.data.IData;
import org.dynami.core.data.IVolatilityEngine;
import org.dynami.core.data.Series;
import org.dynami.core.utils.StatUtils;

public class CloseToCloseVolatilityEngine implements IVolatilityEngine {
	private final int MIN_COMPUTATION_SAMPLE_SIZE;
	
	public CloseToCloseVolatilityEngine(int minComputationSampleSize) {
		this.MIN_COMPUTATION_SAMPLE_SIZE = minComputationSampleSize;
	}
	
	public CloseToCloseVolatilityEngine() {
		this.MIN_COMPUTATION_SAMPLE_SIZE = 10;
	}
	
	@Override
	public double compute(IData data, int period) {
		Series close = data.close();
		final int size = close.size();
		final int _period = Math.max(period, MIN_COMPUTATION_SAMPLE_SIZE);
		if(size > 0 && size > period){
			return StatUtils.relativeStd(close.subset(size-_period, size));
		} else {
			return 0;
		}
	}
}
