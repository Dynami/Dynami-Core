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

import java.util.Collection;
import java.util.List;

import org.dynami.core.portfolio.ClosedPosition;
import org.dynami.core.portfolio.ExecutedOrder;
import org.dynami.core.portfolio.OpenPosition;

public interface IPortfolioService {
	public static final String ID = "IPortfolioService";

	public void setInitialBudget(double budget);

	public double getInitialBudget();

	public double getCurrentBudget();

	public boolean isOnMarket();

	public boolean isFlat();

	public boolean isOnMarket(String symbol);

	public boolean isFlat(String symbol);

	public boolean isLong(String symbol);

	public boolean isShort(String symbol);

	public List<OpenPosition> getOpenPosition();

	public OpenPosition getPosition(String symbol);

	public List<ClosedPosition> getClosedPosition();

	public List<ClosedPosition> getClosedPosition(String symbol);

	public double realized();

	public double unrealized(String symbol);

	public double unrealized();

	public double requiredMargin();

	public Collection<ExecutedOrder> executedOrdersLog();

	public Greeks getPortfolioGreeks();

	public static class Greeks{
		public final double delta;
		public final double gamma;
		public final double vega;
		public final double theta;

		public Greeks(double delta, double gamma, double vega, double theta){
			this.delta = delta;
			this.gamma = gamma;
			this.vega = vega;
			this.theta = theta;
		}
	}
}
