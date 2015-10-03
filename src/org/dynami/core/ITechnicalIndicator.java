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
package org.dynami.core;

import java.util.List;
import java.util.function.Supplier;

import org.dynami.core.data.Series;

public interface ITechnicalIndicator {
	
	public List<Supplier<Series>> series();
	
	public String[] seriesNames();
	
	public boolean isReady();
	
	public String getName();
	
	public String getDescription();
	
	public void reset();
	
//	public default void compute(Series in1){};
//	public default void compute(Series in1, Series in2){}
//	public default void compute(Series in1, Series in2, Series in3){};
//	public default void compute(Series in1, Series in2, Series in3, Series in4){};
//	public default void compute(Series in1, Series in2, Series in3, Series in4, Series in5){};
//	public default void compute(Series in1, Series in2, Series in3, Series in4, Series in5, Series in6){};
//	public default void compute(Series in1, Series in2, Series in3, Series in4, Series in5, Series in6, Series in7){};
//	public default void compute(Series in1, Series in2, Series in3, Series in4, Series in5, Series in6, Series in7, Series in8){};
}
