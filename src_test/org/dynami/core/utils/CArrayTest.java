package org.dynami.core.utils;

public class CArrayTest {
	public static void main(String[] args) {
		CArray test = new CArray(5);
		double[] data = {9, 4, 6, 5, 1, 3, 5, 7, 2, 3};
		
		for(double d:data){
			test.add(d);
		}
		
		System.out.println("last( ) "+test.last(0));
		System.out.println("last(5) "+test.last(5));
		
		System.out.println("get( ) "+test.get(0));
		System.out.println("get(5) "+test.get(5));
		
		System.out.println("Min( ) "+test.min());
		System.out.println("Min(5) "+test.min(5));
		
		System.out.println("Max( ) "+test.max());
		System.out.println("Max(5) "+test.max(5));
		
		
		System.out.println("Mean( ) "+test.mean());
		System.out.println("Mean(5) "+test.mean(5));
		
		System.out.println("Avg( ) "+test.avg());
		System.out.println("Avg(5) "+test.avg(5));
		
		System.out.println("Std( ) "+test.std());
		System.out.println("Std(5) "+test.std(5));
	}
}
