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

public class Greeks {
	private final double[] greeks = new double[5];
	private final Engine engine;
	public Greeks(Engine engine){
		this.engine = engine;
	}
	
	public double delta() {return greeks[DELTA];}
	public double gamma() {return greeks[GAMMA];}
	public double vega() {return greeks[VEGA];}
	public double theta() {return greeks[THETA];}
	public double rho() {return greeks[RHO];}
	
	@FunctionalInterface
	public static interface Engine {
		public double[] evaluate(long date, Asset.Option.Type type, long price, double vola, double interestRate);
	}
	
	private static final int DELTA = 0;
	private static final int GAMMA = 1;
	private static final int VEGA = 2;
	private static final int THETA = 3;
	private static final int RHO = 4;
}
