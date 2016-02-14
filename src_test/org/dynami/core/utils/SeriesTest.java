package org.dynami.core.utils;

import org.dynami.core.data.Series;

public class SeriesTest {
	public static void main(String[] args) {
		try {
			double[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
			Series s = new Series(data);
			Series d = s.divide(10).log();
			System.out.println(d);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
