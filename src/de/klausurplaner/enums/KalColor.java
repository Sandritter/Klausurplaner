package de.klausurplaner.enums;

import java.awt.Color;

public enum KalColor {
	BLUEVIOLET(new Color(138, 43, 226,150)), 
	DOGERBLUE3(new Color(24, 116, 205, 150)),
	CHOCOLATE1(new Color(238, 118, 33, 150)),
	REDBROWN(new Color(139, 35, 35,200)), 
	SPRINGGREEN(new Color(0, 139, 69, 150)),
	DEEPPINK(new Color(205, 16, 118, 150)),
	DARKSLATEGREEN(new Color(47, 79, 79,150)),
	CORAL(new Color(205,91, 69,150)),
	DARKBLUE(new Color(109,176,7,150));
	private Color color;
	
	private KalColor(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return this.color;
	}
}
