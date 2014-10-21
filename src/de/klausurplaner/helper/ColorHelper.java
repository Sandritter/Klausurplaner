package de.klausurplaner.helper;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;


import de.klausurplaner.enums.KalColor;

/**
 * 
 * @author Michael Sandritter, Merle Hiort, Benjamin Christiani
 * ColorHelper stellt Maps bereit die zum einen die Farben selbst beinhalten als auch
 * JLabels von Farben
 */
public class ColorHelper {
	
	/**
	 * Map aus Farben 
	 */
	private static Map<Integer, Color> colors ;
	static {
		colors = new HashMap<Integer, Color>();
		colors.put(1, KalColor.DOGERBLUE3.getColor());
		colors.put(2, KalColor.SPRINGGREEN.getColor());
		colors.put(3, KalColor.CHOCOLATE1.getColor());
		colors.put(4, KalColor.DEEPPINK.getColor());
		colors.put(5, KalColor.BLUEVIOLET.getColor());
		colors.put(6, KalColor.CORAL.getColor());
		colors.put(7, KalColor.DARKSLATEGREEN.getColor());
		colors.put(8, KalColor.DARKBLUE.getColor());
		colors.put(9, KalColor.REDBROWN.getColor());
		colors.put(10, Color.YELLOW);
	}
	
	/**
	 * 
	 * @param i index
	 * @return Color des jeweiligen Index in Map
	 */
	public static Color getColorBySemester(int i){
		return colors.get(i);
	}
	
}
