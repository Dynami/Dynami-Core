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

import java.util.Iterator;
import java.util.function.BiFunction;

public class Series implements Cloneable, Iterable<Double> {
	private static final int BUFFER_SIZE = 1024;
	private double[] data;
	private int cursor;
	private double min, max;
	
	public Series(){
		clear();
	}
	
	public Series(double...values){
		clear();
		for(double d:values){
			append(d);
		}
	}
	
	public double set(int idx, double v){
		assert idx >= 0 && idx < cursor : "Out of range index";
		double tmp = data[idx]; 
		data[idx] = v;
		return tmp;
	}
	
	public void append(double d){
		if(cursor >= data.length){
			double[] tmp = new double[cursor+BUFFER_SIZE];
			System.arraycopy(data, 0, tmp, 0, cursor);
			data = tmp;
		}
		if(d > max) max = d;
		if(d < min) min = d;
		data[cursor++] = d;
	}
	
	public void clear(){
		cursor = 0; 
		data = new double[BUFFER_SIZE];
		min = Long.MAX_VALUE; 
		max = Long.MIN_VALUE;
	}
	
	public double last(){
		assert cursor < 1 : "No data!";
		return data[cursor-1];
	}
	
	public double last(int retro){
		assert cursor - retro < 1 : "No data!";
		return data[cursor-1-retro];
	}
	
	public double first(){
		assert cursor > 0 : "No data!";
		return data[0];
	}
	
	public double get(int index){
		assert cursor >= index  : "No data!";
		return data[index];
	}
	
	public double max(){
		return max;
	}
	
	public double min(){
		return min;
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
				return data[index++];
			}
		};
	}
	
	private Series math(Series other, BiFunction<Double, Double, Double> operand){
		int _otherLength = other.size();
		int _innerlength = size();
		assert _otherLength != _innerlength : "Series have different size";
		Series _new = clone();
		for(int i = 0; i < _innerlength; i++){
			_new.data[i] = operand.apply(data[i], other.get(i));
		}
		return _new;
	}
	
	private Series math(double other, BiFunction<Double, Double, Double> operand){
		int _innerlength = size();
		Series _new = clone();
		for(int i = 0; i < _innerlength; i++){
			_new.data[i] = operand.apply(data[i], other);
		}
		return _new;
	}
	
	
	public Series add(Series other){
		return math(other, (a, b)-> a+b);
	}
	
	public Series substract(Series other){
		return math(other, (a, b)-> a-b);
	}
	
	public Series multiply(Series other){
		return math(other, (a, b)-> a*b);
	}
	
	public Series divide(Series other){
		return math(other, (a, b)-> a/b);
	}
	
	public Series add(double other){
		return math(other, (a, b)-> a+b);
	}
	
	public Series substract(double other){
		return math(other, (a, b)-> a-b);
	}
	
	public Series multiply(double other){
		return math(other, (a, b)-> a*b);
	}
	
	public Series divide(double other){
		return math(other, (a, b)-> a/b);
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
		double[] out = new double[cursor];
		System.arraycopy(data, 0, out, 0, cursor);
		return out;
	}
	
	public Series subset(int start, int end){
		return new Series(toArray(start, end));
	}
	
	public double[] toArray(final int start, final int end){
		final int length = end - start;
		final double[] out = new double[length];
		System.arraycopy(data, start, out, 0, length);
		return out;
	}
	
	public double[] toArray(final int start){
		int length = this.cursor - start;
		double[] out = new double[length];
		System.arraycopy(data, start, out, 0, length);
		return out;
	}
	
	@Override
	protected Series clone() {
		Series copy = new Series();
		copy.data = new double[data.length];
		System.arraycopy(data, 0, copy.data, 0, data.length);
		copy.cursor = cursor;
		copy.max = max;
		copy.min = min;
		return copy;
	}
}
