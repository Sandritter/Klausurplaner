package de.klausurplaner.benutzeroberflaeche;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComponent;

/**
 * Gui-Componente die dem Benutzer in der Kalenderansicht die Uhrzeitskale anzeigt
 * die Uhrzeiten werden dabei stuendlich angezeigt mit einer jeweiligen Hoehe von 80px
 * 
 * @author Merle Thior, Benjamin Arschniti, Michael Rasttadin
 * 
 */
public class UhrzeitSkalaComponent extends JComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8613567549301691044L;
	private int width;
	private int height;
	private final int HOUR_GAP = 80;
	private int dauer;
	private Calendar cal;
	private ArrayList<String> uhrzeiten;
	
	public UhrzeitSkalaComponent(int dauer, Date tag_start) {
		this.cal = new GregorianCalendar();
		this.cal.setTime(tag_start);
		this.dauer = dauer;
		this.width = 50;
		this.height = dauer * (HOUR_GAP + 1);
		this.uhrzeiten = new ArrayList<String>();
		for (int i = 0; i < this.dauer; i++){
			SimpleDateFormat format = new SimpleDateFormat("kk:mm");
			String start = format.format(this.cal.getTime());
			uhrzeiten.add(start);
			this.cal.add(Calendar.HOUR, 1);
		}
		setPreferredSize(new Dimension(this.width, this.height));
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 40, this.width, this.height);
		g.setColor(Color.LIGHT_GRAY);
		for (int i = 0; i < this.uhrzeiten.size(); i++){
			g.drawString(this.uhrzeiten.get(i), 5, 35 + i * HOUR_GAP);
		}
	}
	
}
