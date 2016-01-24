package org.dynami.core.utils;

import org.dynami.core.data.Series;

public class SeriesTest {
	public static void main(String[] args) {
		try {
			double[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
			Series s = new Series(data);

			System.out.println(s.last());
			System.out.println(s.lastNValues(3));


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
