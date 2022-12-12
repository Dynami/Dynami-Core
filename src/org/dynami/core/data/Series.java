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

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Series implements Cloneable, Iterable<Double> {
//	private static final int BUFFER_SIZE = 100;
//	private SummaryStatistics stats = new SummaryStatistics();
//	private ResizableDoubleArray stats = new ResizableDoubleArray(BUFFER_SIZE);
	private DescriptiveStatistics stats = new DescriptiveStatistics();
//	private double[] data;
	private int cursor;
//	private double min, max;

	public Series(){
		clear();
	}

	public Series(double...values){
		clear();
		for(double d:values){
			append(d);
		}
	}

//	public double set(int idx, double v){
//		stats.getValues();
//		assert idx >= 0 && idx < cursor : "Out of range index";
//		double tmp = data[idx];
//		data[idx] = v;
//		return tmp;
//	}

	public void append(double d){
//		if(cursor >= data.length){
//			double[] tmp = new double[cursor+BUFFER_SIZE];
//			System.arraycopy(data, 0, tmp, 0, cursor);
//			data = tmp;
//		}
//		if(d > max) max = d;
//		if(d < min) min = d;
		stats.addValue(d);
		cursor++;
//		data[cursor++] = d;
	}

	public void clear(){
		cursor = 0;
//		data = new double[BUFFER_SIZE];
		stats.clear();
//		min = Long.MAX_VALUE;
//		max = Long.MIN_VALUE;
	}

	public double last(){
		assert cursor < 1 : "No data!";
//		return data[cursor-1];
		return stats.getElement(cursor-1);
	}
	
	

	public double last(int retro){
		assert cursor - retro < 1 : "No data!";
		return stats.getElement(cursor-1-retro);
//		return data[cursor-1-retro];
	}

	public double first(){
		assert cursor > 0 : "No data!";
//		return data[0];
		return stats.getElement(0);
	}

	public double get(int index){
		assert cursor >= index  : "No data!";
//		return data[index];
		return stats.getElement(index);
	}

	public double max(){
//		return max;
		return stats.getMax();
	}

	public double min(){
		return stats.getMin();
		//return min;
	}

	public int size(){
		return cursor;
	}

	public Iterator<Double> iterator(){
		return new Iterator<Double>() {
			int index = 0;
			@Override
			public boolean hasNext() {
				return index < cursor;
			}

			@Override
			public Double next() {
				return stats.getElement(index++);
				//return data[index++];
			}
		};
	}
	
	public static double[] rollapply(double[] data, Function<double[], Double> func, int windowSize){
		double[] out = new double[data.length];
		double[] tmp;
		for(int i = 0; i < data.length; i++){
			if(i < windowSize-1){
				out[i] = Double.NaN;
			} else {
				tmp = new double[windowSize];
				System.arraycopy(data, i-(windowSize-1), tmp, 0, windowSize);
				out[i] = func.apply(tmp);
			}
		}
		return out;
	}
	
	public Series rollapplyForArrays(Function<double[], Double> func, int windowSize){
		return new Series(rollapply(toArray(), func, windowSize));
	}
	
	public Series rollapply(Function<Series, Double> func, int windowSize) {
		final Series out = new Series();
		final int size = size();
		for(int i = 0; i < size; i++){
			if(i < windowSize-1){
				out.append(Double.NaN);
			} else {
				Series s = subset(i-windowSize+1, i);
				out.append(func.apply(s));
			}
		}
		return out;
	}
	
	private Series math(Function<Double, Double> operand){
		int _innerlength = size();
		Series _new = new Series();
		for(int i = 0; i < _innerlength; i++){
			_new.append(operand.apply(get(i)));
		}
		return _new;
	}

	private Series math(int shift, BiFunction<Double, Double, Double> operand){
		int _innerlength = size();
		assert shift > _innerlength : "Shift must be lower equals than size";
		Series _new = new Series();
		for(int i = 0; i < _innerlength; i++){
			if(i < shift){
				_new.append(Double.NaN);
			} else {
				_new.append(operand.apply(get(i), get(i-shift)));
			}
		}
		return _new;
	}

	private Series math(Series other, BiFunction<Double, Double, Double> operand){
		int _otherLength = other.size();
		int _innerlength = size();
		assert _otherLength != _innerlength : "Series have different size";
		Series _new = new Series();
		for(int i = 0; i < _innerlength; i++){
			_new.append(operand.apply(get(i), other.get(i)));
		}
		return _new;
	}

	private Series math(double other, BiFunction<Double, Double, Double> operand){
		int _innerlength = size();
		Series _new = new Series();
		for(int i = 0; i < _innerlength; i++){
			_new.append(operand.apply(get(i), other));
		}
		return _new;
	}

	public Series log(){
		return math(a-> Math.log(a));
	}

	public Series sqrt(){
		return math(a->Math.sqrt(a));
	}

	public Series pow(double power){
		return math(a-> Math.pow(a, power));
	}

	public Series sin(){
		return math(a-> Math.sin(a));
	}

	public Series add(int shift){
		return math(shift, (a, b)-> a+b);
	}

	public Series lagAndSubstract(int shift){
		return math(shift, (a, b)-> a-b);
	}

	public Series lagAndMultiply(int shift){
		return math(shift, (a, b)-> a*b);
	}

	public Series lagAndDivide(int shift){
		return math(shift, (a, b)-> a/b);
	}

	public Series add(Series other){
		return math(other, (a, b)-> a+b);
	}

	public Series substract(Series other){
		return math(other, (a, b)-> a-b);
	}

	public Series times(Series other){
		return math(other, (a, b)-> a*b);
	}

	public Series divide(Series other){
		return math(other, (a, b)-> a/b);
	}

	public Series plus(double other){
		return math(other, (a, b)-> a+b);
	}

	public Series minus(double other){
		return math(other, (a, b)-> a-b);
	}

	public Series multiply(double other){
		return math(other, (a, b)-> a*b);
	}

	public Series divide(double other){
		return math(other, (a, b)-> a/b);
	}

	public double max(int lastNvalues){
		double out = -Double.MAX_VALUE;
		for(int i = 0 ; i < lastNvalues; i++){
			if(last(i) > out) out = last(i);
		}
		return out;
	}

	public DescriptiveStatistics stats(){
		return stats;
	}

	public double min(int lastNvalues){
		double out = Double.MAX_VALUE;
		for(int i = 0 ; i < lastNvalues; i++){
			if(last(i) < out) out = last(i);
		}
		return out;
	}

	public boolean goesUp(int lastNvalues){
		assert size() < lastNvalues : "lastNvalues is bigger than size";
		return last(lastNvalues) < last();
	}


	public boolean goesDown(int lastNvalues){
		assert size() < lastNvalues : "lastNvalues is bigger than size";
		return last(lastNvalues) > last();
	}


	public boolean upperInversion(int lastNvalues){
		assert size() < lastNvalues : "lastNvalues is bigger than size";
		double first = last(lastNvalues), last = last(), max = Long.MIN_VALUE, min=Long.MAX_VALUE;
		for(int i = 0 ; i < lastNvalues; i++){
			if(last(i) > max) max = last(i);
			if(last(i) < min) min = last(i);
		}

		return first > min && last > min;
	}

	public boolean lowerInversion(int lastNvalues){
		assert size() < lastNvalues : "lastNvalues is bigger than size";
		double first = last(lastNvalues), last = last(), max = Long.MIN_VALUE, min=Long.MAX_VALUE;
		for(int i = 0 ; i < lastNvalues; i++){
			if(last(i) > max) max = last(i);
			if(last(i) < min) min = last(i);
		}

		return first < max && last < max;
	}

	public boolean crossesOver(Series s2){
		return last(1) <= s2.last(1) && last() > s2.last();
	}

	public boolean crossesOver(double threshold){
		return last(1) <= threshold && last() > threshold;
	}

	public boolean crossesUnder(Series s2){
		return last(1) >= s2.last(1) && last() < s2.last();
	}

	public boolean crossesUnder(double threshold){
		return last(1) >= threshold && last() < threshold;
	}

	public boolean isGreaterThan(Series s2){
		return last() > s2.last();
	}

	public boolean isGreaterThan(double threshold){
		return last() > threshold;
	}

	public boolean isLowerThan(Series s2){
		return last() < s2.last();
	}

	public boolean isLowerThan(double threshold){
		return last() < threshold;
	}

	public double[] toArray(){
		return stats.getValues();
//		double[] out = new double[cursor];
//		System.arraycopy(data, 0, out, 0, cursor);
//		return out;
	}

	public Series subset(int start, int end){
		return new Series(toArray(start, end));
	}

	public Series lastNValues(int lastNValues){
		return subset(size()-lastNValues, size()-1);
	}

	public Series lag(int shift){
		return lag(shift, true);
	}

	public Series omitNaN(){
		Series s = new Series();
		int lenght = size();
		for(int i = 0; i < lenght; i++){
			double t = get(i);
			if(!Double.isNaN(t)){
				s.append(t);
			}
		}
		return s;
	}

	public Series lag(int shift, boolean sameSize){
		if(sameSize){
			final int length = size();
			final double[] out = new double[length];
			for(int i = length-shift-1 ; i >= 0; i--){
				out[i+shift] = get(i);
			}
			for(int i= 0; i < shift; i++){
				out[i] = Double.NaN;
			}
			return new Series(out);
		} else {
			final int length = size();
			final double[] out = new double[length-shift];
			for(int i = length-shift-1 ; i >= 0; i--){
				out[i] = get(i);
			}
			return new Series(out);
		}
	}

	public double[] toArray(final int start, final int end){
		double[] tmp = stats.getValues();
		final int length = end - start +1;
		final double[] out = new double[length];
		System.arraycopy(tmp, start, out, 0, length);
		return out;
	}

	public double[] toArray(final int start){
		double[] tmp = stats.getValues();
		int length = this.cursor - start;
		double[] out = new double[length];
		System.arraycopy(tmp, start, out, 0, length);
		return out;
	}

	@Override
	public String toString() {
		if(size() <= 30){
			return Arrays.toString(toArray());
		} else {
			return "Last 30 on "+size()+" values ..."+Arrays.toString(lastNValues(30).toArray());
		}
	}

	public Series concat(Series series, boolean inPlace){
		if(inPlace) {
			for(double v : series){
				append(v);
			}
			return this;
		} else {
			Series out = clone();
			for(double v : series){
				out.append(v);
			}
			return out;
		}
	}

	@Override
	protected Series clone() {
		Series copy = new Series();
//		copy.data = new double[data.length];
//		System.arraycopy(data, 0, copy.data, 0, data.length);
		copy.cursor = cursor;
//		copy.max = max;
//		copy.min = min;
		copy.stats = stats.copy();
		return copy;
	}
}
