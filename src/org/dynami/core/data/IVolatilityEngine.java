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
package org.dynami.core.data;

import java.time.Duration;

import org.dynami.core.assets.Market;
import org.dynami.core.utils.DUtils;

@FunctionalInterface
public interface IVolatilityEngine {	
	public double compute(IData data, int period);
	
	public default double annualizationFactor(long compresssionRate, int period, Market market){
		double factor = DUtils.YEAR_WORKDAYS;
		if (compresssionRate >= IData.TimeUnit.Day.millis()) {
			factor = ((IData.TimeUnit.Day.millis() / (double)compresssionRate) * DUtils.YEAR_WORKDAYS)/period;
		} else {
			final Duration duration = Duration.between(market.getOpenTime(), market.getCloseTime());
			final long milliSeconds = duration.getSeconds() * 1_000L;
			factor = ((double)DUtils.YEAR_WORKDAYS * ((double)milliSeconds / (double)compresssionRate))/(double)period;
		}
		return Math.sqrt(factor);
	}
}