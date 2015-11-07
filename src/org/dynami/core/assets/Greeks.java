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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.dynami.core.assets.Asset.Option.Type;

public class Greeks {
	private double delta, gamma, vega, theta, rho;
	
	public double delta() {return delta;}
	public double gamma() {return gamma;}
	public double vega() {return vega;}
	public double theta() {return theta;}
	public double rho() {return rho;}
	
	public void setGreeks(double delta, double gamma, double vega, double theta, double rho){
		Lock lock = new ReentrantLock();
		if(lock.tryLock()){
			try {
				this.delta=delta;
				this.gamma = gamma;
				this.vega = vega;
				this.theta= theta;
				this.rho = rho;
			} finally {
				lock.unlock();
			}
		}
	}

	@FunctionalInterface
	public static interface Engine {
		public void evaluate(Greeks output, long time, Asset.Option.Type type, long expire, double strike, double price, double vola, double interestRate);
	}
	
	@FunctionalInterface
	public static interface ImpliedVolatility {
		public double estimate(String underlyingSymbol, long time, Type type, long expire, double strike, double optionPrice, double riskFreeRate);
	}
}
