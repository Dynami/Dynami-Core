package org.dynami.core.plot;

import javafx.scene.paint.Color;

public enum Colors {
	NONE(null),
	BLUE(Color.BLUE),
	GREEN(Color.GREEN),
	GREENYELLOW(Color.GREENYELLOW),
	DARKGREEN(Color.DARKGREEN),
	RED(Color.RED),
	DARKRED(Color.DARKRED),	
	;
	
	private final String hexa;
	private Colors(Color c){
		
		this.hexa = (c!= null)?c.toString():"";
	}
	public String toString(){
		return hexa;
	}
}
