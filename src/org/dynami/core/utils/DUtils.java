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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.dynami.core.assets.Asset.Option;

public class DUtils {
	public static final long DAY_MILLIS = 24*60*60*1000L;
	
	public static final SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	public static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,###.00");
	public static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#.###");
	public static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#.0000");
	public static final List<OptionExpire> OPTION_EXPIRES = Collections.unmodifiableList(Arrays.asList(
			new OptionExpire(0, 'A', 'M'),
			new OptionExpire(1, 'B', 'N'),
			new OptionExpire(2, 'C', 'O'),
			new OptionExpire(3, 'D', 'P'),
			new OptionExpire(4, 'E', 'Q'),
			new OptionExpire(5, 'F', 'R'),
			new OptionExpire(6, 'G', 'S'),
			new OptionExpire(7, 'H', 'T'),
			new OptionExpire(8, 'I', 'U'),
			new OptionExpire(9, 'J', 'V'),
			new OptionExpire(10, 'K', 'W'),
			new OptionExpire(11, 'L', 'X') ));
	
	public static String getOptionName(String prefix, Option.Type type,long expire, double strike){
		StringBuilder builder = new StringBuilder(prefix);
		builder.append(" ");
		builder.append(type.name());
		builder.append(" ");
		builder.append(MONEY_FORMAT.format(strike));
		builder.append(" ");
		builder.append(DATE_FORMAT.format(expire));
		return builder.toString();
	}
	
	public static String getOptionSymbol(String prefix, Option.Type type,long expire, double strike){
		StringBuilder builder = new StringBuilder(prefix);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(expire);
		OptionExpire exp = OPTION_EXPIRES.stream()
				.filter(o->o.month==cal.get(Calendar.MONTH))
				.findFirst().get();
		if(exp != null){
			builder.append(Option.Type.CALL.equals(type)?exp.callLetter:exp.putLetter);
		}
		builder.append(String.format("%6.0f", strike));
		return builder.toString();
	}
	
	public static final double DECIMALS = 4;
	private static final double FACTOR = Math.pow(10, DECIMALS);
	
	public static long d2l(double d){
		return (long)Math.round((d*FACTOR));
	}
	
	public static double l2d(long l){
		return (double)l/FACTOR;
	}
	
	public static long max(long ...ds){
		long out = Long.MIN_VALUE;
		for(long d : ds){
			if(d > out)
				out = d;
		}
		return out;
	}
	
	public static long min(long ...ds){
		long out = Long.MAX_VALUE;
		for(long d : ds){
			if(d < out)
				out = d;
		}
		return out;
	}
	
	public static boolean in(Number in, Number...compare){
		for(Number n:compare){
			if(n.equals(in)){
				return true;
			}
		}
		return false;
	}
	
	public static String getErrorMessage(Throwable e){
		StringBuilder buffer = new StringBuilder();
		Throwable thr;
		if(e.getCause()!= null && e.getCause().getStackTrace().length >0){
			thr = e.getCause();
		} else {
			thr = e;
		}
		
		String message = (thr.getMessage() != null)?thr.getMessage():e.getMessage();
		buffer.append((message!=null)?message:e.getMessage());
		buffer.append(" - ");
		if(thr.getStackTrace() != null && thr.getStackTrace().length > 0){
			buffer.append(thr.getClass().getName());
			buffer.append(" occurred in ");
			buffer.append(thr.getStackTrace()[0].getClassName());
			buffer.append("::");
			buffer.append(thr.getStackTrace()[0].getMethodName());
			buffer.append("() # ");
			buffer.append(thr.getStackTrace()[0].getFileName());
			buffer.append(" at line: ");
			buffer.append(thr.getStackTrace()[0].getLineNumber());
		}
		return buffer.toString();
	}
	
	public static void copy(final long[] src, final double[] dest){
		for(int i = 0; i < src.length; i++){
			dest[i] = src[i];
		}
	}
	
	public static void shift(final double[] input, final int shift){
		for (int i = input.length-1; i >= 0; i--) {
			if(i >= shift)
				input[i] = input[i-shift];
			else
				input[i] = 0;
		}
	}
	
	public static void shift(final int[] input, final int shift){
		for (int i = input.length-1; i >= 0; i--) {
			if(i >= shift)
				input[i] = input[i-shift];
			else
				input[i] = 0;
		}
	}
	
	public static void shift(final long[] input, final int shift){
		for (int i = input.length-1; i >= 0; i--) {
			if(i >= shift)
				input[i] = input[i-shift];
			else
				input[i] = 0;
		}
	}
	
	public static int max(int...n){
		int v = Integer.MIN_VALUE;
		for(int i:n){
			if(i > v) v = i;
		}
		return v;
	}
	
	public static class OptionExpire{
		public final int month;
		public final char callLetter;
		public final char putLetter;
		
		public OptionExpire(int month, char callLetter, char putLetter) {
			this.month = month;
			this.callLetter = callLetter;
			this.putLetter = putLetter;
		}
	}
}
