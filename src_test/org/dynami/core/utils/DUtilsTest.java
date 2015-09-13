package org.dynami.core.utils;

public class DUtilsTest {
	public static void main(String[] args) {
		try {
			double d1 = 23001.001;
			long l1 = DUtils.d2l(d1);
			
			System.out.println(l1);
			
			double d2 = DUtils.l2d(l1);
			
			System.out.println(d1 +" --> "+d2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
