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

import java.io.Serializable;
import java.util.Date;

import org.dynami.core.utils.DUtils;

public interface ITraceService {
	public static final String ID = "ITraceService";

	public void info(String stage, String line);

	public void debug(String stage, String line);

	public void warn(String stage, String line);

	public void error(String stage, String line);

	public void error(String stage, Throwable e);

	public void market(String line);

	public static class Trace{
		public final Type type;
		public final long time;
		public final String stage;
		public final String line;

		public Trace(Type type, final long time, final String stage, final String line) {
			this.type = type;
			this.time = time;
			this.stage = stage;
			this.line = line;
		}

		@Override
		public String toString() {
			return String.format("[%5s][%19s][%20s] "+line, type, DUtils.LONG_DATE_FORMAT.format(new Date(time)), stage);
		}

		public static enum Type {
			Debug(4), Info(3), Warn(2), Error(1), Market(0);

			public final int level;
			private Type(int level) {
				this.level = level;
			}
		}
	}
}
