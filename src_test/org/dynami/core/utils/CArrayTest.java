package org.dynami.core.utils;

import java.util.Arrays;

public class CArrayTest {
	
	public static void main(String[] args) {
		CArray test = new CArray(10);
		double[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		
		for(double d:data){
			test.add(d);
		}
		
		double[] diff = test.diff(2);
		System.out.println(diff.length +" | "+test.size());
		
		System.out.println(Arrays.toString(diff));
	}
}
