package de.klausurplaner.controller.objekte;

import java.util.Date;

/**
 * 
 * Diese Klasse wird als Instanz vom KalenderDirektor dem KalenderPanel uebergeben, wenn der Benutzer in der Kalenderansicht navigiert.
 * @author Merle Hiort, Benjamin Christiani, Michael Sandritter
 */
public class Navigation {
	/**
	 * Boolean ob vor oder zurueck navigiert werden soll
	 */
	private boolean isNavZurueck;
	/**
	 * aktuelles Datum
	 */
	private Date date;
	
	/**
	 * Parametrisierter Konstruktor
	 * @param isZurueck vor oder zurueck
	 * @param date aktuelles Datum
	 */
	public Navigation(boolean isZurueck, Date date){
		this.isNavZurueck = isZurueck;
		this.date = date;
	}

	public boolean isNavZurueck() {
		return isNavZurueck;
	}

	public void setNavZurueck(boolean isNavZurueck) {
		this.isNavZurueck = isNavZurueck;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
