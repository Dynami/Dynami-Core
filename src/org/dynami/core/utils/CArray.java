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

import java.util.Arrays;

public class CArray {
	private final double[] data;
	public final int length;
	private int cursor = 0;
	
	public CArray(final int length) {
		this(length, Double.NaN, false);
	}
	
	public CArray(final int length, final double fill) {
		this(length, fill, true);
	}
	
	private CArray(final int length, final double fill, boolean moveCursorToLength) {
		this.length = length;
		this.data = new double[length];
		Arrays.fill(data, fill);
		if(moveCursorToLength) cursor = length-1;
	}
	
	public void clear(){
		Arrays.fill(data, Double.NaN);
		this.cursor = 0;
	}
	
	public double[] toArray(){
		final double[] out = new double[size()];
		for(int i = 0; i < out.length; i++){
//		for (int j = 0, i = out.length-1; i >=0 ; i--,j++) {
			out[i] = get(i);
		}
		return out;
	}
 	
	public void add(final double v){
		data[(cursor++)%length] = v;
	}
	
	public double last(int idx){
		return data[(cursor-1-idx)%length];
	}
	
	public double last(){
		return last(0);
	}
	
	public double[] last(final int from, final int to) {
		final int len = to-from;
		assert len <= length : "Slice lenght greater than size array";
		final double[] out = new double[len];
		
		for(int i = 0; i < len; i++) {
			out[i] = last(from-i);
		}
		
		return out;
	}
	
	public double get(final int idx){
		return data[(cursor<=length)?idx:(cursor+idx)%length];
	}
	
	public double[] get(final int from, final int to) {
		final int len = to-from;
		assert len <= length : "Slice lenght greater than size array";
		final double[] out = new double[len];
		
		for(int i = 0; i < len; i++) {
			out[i] = get(from+i);
		}
		
		return out;
	}
	
	public double min(){
		return min(size());
	}
	
	public double min(int period){
		assert period > 0;
		double min = Double.MAX_VALUE;
		double d = 0;
		int size = Math.min(size(), period);
		for(int i = 0; i < size ;i++){
			d = last(i);
			if(!Double.isNaN(d) && d < min)
				min = d;
		}
		return min;
	}
	
	public int argmin(int period){
		assert period > 0;
		double min = Double.MAX_VALUE;
		double d = 0;
		int size = Math.min(size(), period);
		int minIdx = 0;
		for(int i = 0; i < size ;i++){
			d = get(i);
			if(!Double.isNaN(d) && d < min) {
				min = d;
				minIdx = i;
			}
		}
		return minIdx;
	}
	
	public int argmin() {
		return argmin(size());
	}
	
	public int size(){
		return Math.min(length, cursor);
	}
	
	public double max(){
		return max(size());
	}
	
	public double max(int period){
		assert period > 0;
		double max = -Double.MAX_VALUE;
		double d = 0;
		int size = Math.min(size(), period);
		for(int i = 0; i < size ;i++){
			d = last(i);
			if(!Double.isNaN(d) && d > max)
				max = d;
		}
		return max;
	}
	
	public int argmax() {
		return argmax(size());
	}
	
	public int argmax(int period){
		assert period > 0;
		double max = -Double.MAX_VALUE;
		double d = 0;
		int size = Math.min(size(), period);
		int maxIdx = 0;
		for(int i = 0; i < size ;i++){
			d = get(i);
			if(!Double.isNaN(d) && d > max) {
				max = d;
				maxIdx = i;
			}
		}
		return maxIdx;
	}
	
	public double avg(int period){
		assert period > 0;
		int size = Math.min(size(), period);
		double sum = 0;
		for(int i = 0; i < size ;i++){
			sum += last(i);
		}
		return sum/size;
	}
	
	public double sum() {
		final int len = size();
		double sum = 0;
		for(int i = 0; i < len; i++) {
			sum += get(i);
		}
		return sum;
	}
	
	public double avg(){
		return avg(size());
	}
	
	public double mean(){
		return mean(size());
	}
	
	public double mean(int period){
		assert period > 0;
		int size = Math.min(size(), period);
		double[] tmp = new double[size];
		for(int i = 0; i < size ;i++){
			tmp[i] = last(i);
		}
		Arrays.sort(tmp);
		return size%2!=0?tmp[size/2]:(tmp[size/2-1]+tmp[size/2])/2.;
	}
	
	public double std(){
		return std(size());
	}
	
	public double slope(int period){
		assert period > 0;
		int size = Math.min(size(), period);
		double mean_y = mean(size);
		double mean_x = (size>1)?((size/2.0)+.5):1;
		double summNum = 0;
		double sumDenom = 0;
		double y = 0;
		double x = 0;
		for(int i = 0; i < size ;i++){
			y = last(i);
			x = (i+1);
			summNum += (y-mean_y)*x;
			sumDenom += (x-mean_x)*x;
		}
		return -(summNum/sumDenom);
	}
	
	public double slope(){
		return slope(size());
	}
	
	public double[] diff(int period) {
		assert period > 0 && period < size();
		
		double[] out = new double[size() - period];
		
		for(int i = period; i < out.length; i++) {
			out[i-period] = get(i) - get(i-period);
		}
		return out;
	}
	
	public double std(final int period){
		assert period > 0;
		int size = Math.min(size(), period);
		double avg = avg(period);
		double sum = 0;
		for(int i = 0; i < size ;i++){
			sum += Math.pow(last(i)-avg, 2);
		}
		return Math.sqrt(sum/((double)size-1));
	}
}
