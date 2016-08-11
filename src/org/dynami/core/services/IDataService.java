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
package org.dynami.core.services;

import java.util.Date;

import org.dynami.core.data.IData;
import org.dynami.core.data.IVolatilityEngine;

public interface IDataService {
	public static final String ID = "IDataService";

	/**
	 * Retrieves all historical data with default compression for the given symbol
	 * @param symbol
	 * @return {@link IData}
	 */
	public IData history(String symbol);

	/**
	 * Retrieves all historical data with desired compression for the given symbol
	 * @param symbol
	 * @param timeFrame @see {@link COMPRESSION_UNIT}
	 * @param units es. with COMPRESSION_UNIT.MINUTE and 5 units, all historical data are compressed in five minutes bars.
	 * @return {@link IData}
	 */
	public IData history(String symbol, long timeFrame, int units);

	/**
	 * Retrieves all historical data with default compression for the given symbol and time range
	 * @param symbol
	 * @param from
	 * @param to nullable, whether null parameter is ignored
	 * @return {@link IData}
	 */
	public IData history(String symbol, Date from, Date to);
	
	/**
	 * Retrieves last n bars
	 * @param symbol
	 * @param last
	 * @return {@link IData}
	 */
	public IData history(String symbol, int last);

	/**
	 * Retrieves the historical data for the given range with the desired compression for the given symbol
	 * @param symbol
	 * @param from
	 * @param to
	 * @param timeFrame @see {@link COMPRESSION_UNIT}
	 * @param units es. with COMPRESSION_UNIT.MINUTE and 5 units, all historical data are compressed in five minutes bars.
	 * @return {@link IData}
	 */
	public IData history(String symbol, Date from, Date to, long timeFrame, int units);
	
	/**
	 * Gets annualized historical volatility
	 * @param symbol
	 * @param units
	 * @return annualized historica volatility
	 */
	public double histVola(String symbol, int units);
	
	/**
	 * Gets annualized historical volatility computed by given volatility estimator
	 * @param engine
	 * @param symbol
	 * @param units
	 * @return
	 */
	public double histVola(IVolatilityEngine engine, String symbol, int units);
	
}
