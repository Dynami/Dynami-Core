package org.dynami.core.utils;

import org.dynami.core.data.Series;

public class SeriesTest {
	public static void main(String[] args) {
		try {
			double[] _data = {10.1, 10.4, 10.8, 10.1, 10, 9.8, 9.7, 10.5, 10.7, 10.1, 10.4, 10.8, 10.6};
			Series r1 = new Series(_data).lagAndDivide(1).omitNaN();
			System.out.println("------------");
			System.out.println(r1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
