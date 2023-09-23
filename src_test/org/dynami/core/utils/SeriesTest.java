package org.dynami.core.utils;

import org.dynami.core.data.Series;

import java.util.Arrays;

public class SeriesTest {
	public  static void print(Object obj){
		System.out.println(obj);
	}

	public static void main(String[] args) {
		try {
			final double[][] data = {
					{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
					{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
					{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
			};

			double[][] out = Series.inline(data, 5);
			for(double[] o : out){
				print(Arrays.toString(o));
			}

			print("-------- colonna "+2);
			print("");
			double[] column = Series.column(out, 2);
			print(Arrays.toString(column));


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
