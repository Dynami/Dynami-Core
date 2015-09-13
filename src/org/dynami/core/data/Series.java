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

public class Series implements Cloneable {
	private static final int BUFFER_SIZE = 1024;
	private long[] data;
	private int cursor;
	private long min, max;
	
	public Series(){
		clear();
	}
	
	public Series(long...values){
		clear();
		for(long d:values){
			append(d);
		}
	}
	
	public long set(int idx, long v){
		assert idx >= 0 && idx < cursor : "Out of range index";
		long tmp = data[idx]; 
		data[idx] = v;
		return tmp;
	}
	
	public void append(long d){
		if(cursor >= data.length){
			long[] tmp = new long[cursor+BUFFER_SIZE];
			System.arraycopy(data, 0, tmp, 0, cursor);
			data = tmp;
		}
		if(d > max) max = d;
		if(d < min) min = d;
		data[cursor++] = d;
	}
	
	public void clear(){
		cursor = 0; 
		data = new long[BUFFER_SIZE];
		min = Long.MAX_VALUE; 
		max = Long.MIN_VALUE;
	}
	
	public long last(){
		assert cursor > 0 : "No data!";
		return data[cursor-1];
	}
	
	public long last(int retro){
		assert cursor - retro > 0 : "No data!";
		return data[cursor-1-retro];
	}
	
	public long first(){
		assert cursor > 0 : "No data!";
		return data[0];
	}
	
	public long get(int index){
		assert cursor >= index  : "No data!";
		return data[index];
	}
	
	public long max(){
		return max;
	}
	
	public long min(){
		return min;
	}
	
	public int size(){
		return cursor;
	}
	
	public Iterator<Long> iterator(){
		return new Iterator<Long>() {
			int index = 0;
			@Override
			public boolean hasNext() {
				return index < cursor;
			}
			 
			@Override
			public Long next() {
				return data[index++];
			}
		};
	}
	
	private Series math(Series other, BiFunction<Long, Long, Long> operand){
		int _otherLength = other.size();
		int _innerlength = size();
		assert _otherLength != _innerlength : "Series have different size";
		Series _new = clone();
		for(int i = 0; i < _innerlength; i++){
			_new.data[i] = operand.apply(data[i], other.get(i));
		}
		return _new;
	}
	
	private Series math(long other, BiFunction<Long, Long, Long> operand){
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
	
	public Series add(long other){
		return math(other, (a, b)-> a+b);
	}
	
	public Series substract(long other){
		return math(other, (a, b)-> a-b);
	}
	
	public Series multiply(long other){
		return math(other, (a, b)-> a*b);
	}
	
	public Series divide(long other){
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
		long first = last(lastNvalues), last = last(), max = Long.MIN_VALUE, min=Long.MAX_VALUE;
		for(int i = 0 ; i < lastNvalues; i++){
			if(last(i) > max) max = last(i);
			if(last(i) < min) min = last(i);
		}
		
		return first > min && last > min;
	}
	
	public boolean lowerInversion(int lastNvalues){
		assert size() < lastNvalues : "lastNvalues is bigger than size";
		long first = last(lastNvalues), last = last(), max = Long.MIN_VALUE, min=Long.MAX_VALUE;
		for(int i = 0 ; i < lastNvalues; i++){
			if(last(i) > max) max = last(i);
			if(last(i) < min) min = last(i);
		}
		
		return first < max && last < max;
	}
	
	public long[] toArray(){
		long[] out = new long[cursor];
		System.arraycopy(data, 0, out, 0, cursor);
		return out;
	}
	
	public long[] toArray(final int start, final int end){
		final int length = end - start;
		final long[] out = new long[length];
		System.arraycopy(data, start, out, 0, length);
		return out;
	}
	
	public long[] toArray(final int start){
		int length = this.cursor - start;
		long[] out = new long[length];
		System.arraycopy(data, start, out, 0, length);
		return out;
	}
	
	@Override
	protected Series clone() {
		Series copy = new Series();
		copy.data = new long[data.length];
		System.arraycopy(data, 0, copy.data, 0, data.length);
		copy.cursor = cursor;
		copy.max = max;
		copy.min = min;
		return copy;
	}
}
