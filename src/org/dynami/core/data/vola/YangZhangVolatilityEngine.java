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

import org.apache.commons.math3.stat.StatUtils;
import org.dynami.core.data.Bar;
import org.dynami.core.data.IData;
import org.dynami.core.data.IVolatilityEngine;
import org.dynami.core.data.Series;

public class YangZhangVolatilityEngine implements IVolatilityEngine {
	private RogersSatchellVolatilityEngine rogerSatchellVolaEngine = new RogersSatchellVolatilityEngine();
	@Override
	public double compute(IData data, int period) {
		int size = data.size();
		final double k = 0.34/(1.34* ((double)(period+1)/(double)(period-1)) );
		if(size > period){
			double sigma, overnightSum = 0, openToCloseSum = 0, overnightSigma = 0, openToCloseSigma = 0;
			double rogerSatchellVola = rogerSatchellVolaEngine.compute(data, period);
			Series overNigthJumpAvg = new Series();
			Series openToCloseJumpAvg = new Series();
			Bar bar, bar_1;
			for(int i = size-period; i < size; i++){
				bar = data.get(i);
				bar_1 = data.get(i-1);
				overNigthJumpAvg.append(Math.log(bar.open/bar_1.close));
				openToCloseJumpAvg.append(Math.log(bar.close/bar.open));
			}

			double overNightAvg = StatUtils.mean(overNigthJumpAvg.toArray());
			double openToCloseAvg = StatUtils.mean(openToCloseJumpAvg.toArray());
			for(int i = size-period; i < size; i++){
				bar = data.get(i);
				bar_1 = data.get(i-1);

				overnightSum += Math.pow( Math.log(bar.open/bar_1.close)-overNightAvg, 2);
				openToCloseSum += Math.pow( Math.log(bar.close/bar.open)-openToCloseAvg, 2);
			}

			overnightSigma = (1./((double)period-1.))*overnightSum;
			openToCloseSigma = (1./((double)period-1.))*openToCloseSum;

			sigma = Math.sqrt( overnightSigma+k*openToCloseSigma+(1-k)*rogerSatchellVola);
			return sigma;
		}
		return 0;
	}
}
