package org.dynami.core.utils;

import java.lang.reflect.Field;

import javafx.scene.paint.Color;

public class GenerateColorsConstant {
	public static void main(String[] args) {
		try {
			Field[] fields = Color.class.getDeclaredFields();
			Color c = Color.ALICEBLUE;
			for(Field f:fields){
				if(f.getType().equals(Color.class)){
					String color = f.getName();
					String hex = format((Color)f.get(c));
					System.out.println("public static final String "+color+" = \""+hex+"\";");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String format(Color c){
		return String.format( "#%02X%02X%02X",
            (int)( c.getRed() * 255 ),
            (int)( c.getGreen() * 255 ),
            (int)( c.getBlue() * 255 ) );
	}
}
