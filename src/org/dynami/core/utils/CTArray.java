package org.dynami.core.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Stream;

public class CTArray<T> {
	private final Class<T> clazz;
	private final T[] data;
	public final int length;
	private int cursor = 0;
	
	public CTArray(final Class<T> T, final int length) {
		this(T, length, null, false);
	}
	
	public CTArray(final Class<T> T, final int length, final T fill) {
		this(T, length, fill, true);
	}
	
	@SuppressWarnings("unchecked")
	private CTArray(final Class<T> T, final int length, final T fill, boolean moveCursorToLength) {
		this.clazz = T;
		this.length = length;
		
		this.data = (T[])Array.newInstance(clazz, length);
		
		Arrays.fill(data, fill);
		if(moveCursorToLength) cursor = length-1;
	}
	
	public void clear(){
		Arrays.fill(data, null);
		this.cursor = 0;
	}
	
	@SuppressWarnings("unchecked")
	public T[] toArray(){
		final T[] out =(T[])Array.newInstance(clazz, size());
		for(int i = 0; i < out.length; i++){
			out[i] = get(i);
		}
		return out;
	}
 	
	public void add(final T v){
		data[(cursor++)%length] = v;
	}
	
	public T last(int idx){
		return data[(cursor-1-idx)%length];
	}
	
	public T last(){
		return last(0);
	}
	
	public T get(final int idx){
		return data[(cursor<=length)?idx:(cursor+idx)%length];
	}
	
	public int size(){
		return Math.min(length, cursor);
	}
	
	public Stream<T> stream() {
		return Stream.of(toArray());
	}
}
