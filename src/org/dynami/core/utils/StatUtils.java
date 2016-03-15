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

import org.dynami.core.data.Series;

public class StatUtils {
	public static double cumSum(Series series){
		double sum = 0;
		for(Double d: series){
			sum += d;
		}
		return sum;
	}

	public static double avg(Series series){
		return cumSum(series)/series.size();
	}

	public static double std(Series series){
		double avg = avg(series);
		double sum = 0;
		for(double d: series){
			sum += Math.pow(d-avg, 2);
		}
		return Math.sqrt(sum/(series.size()-1.));
	}

	public static double relativeStd(Series series){
		double std = std(series);
		double avg = avg(series);
		return (std/avg);
	}
}
