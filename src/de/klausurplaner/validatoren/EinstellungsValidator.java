package de.klausurplaner.validatoren;

import java.util.Date;

import de.klausurplaner.helper.Helper;

/**
 * Klasse mit welcher die vom User beim Starten des Programms gemachten Einstellungen validiert werden
 * 
 * @author Michael Sandritter, Merle Hiort, Benjamin Christiani
 *
 */
public class EinstellungsValidator implements Validable{
	
	/**
	 * Startdatum der Klausurphase
	 */
	Date klausurphase_von;
	/**
	 * Enddatum der Klausurphase
	 */
	Date klausurphase_bis;
	/**
	 * Startuhrzeit eines Klausurtages
	 */
	Date tag_start; 
	/**
	 * Enduhrzeit eines Klausurtages
	 */
	Date tag_ende;
	/**
	 * Maximaldauer der Klausurphase
	 */
	long abstand_tage;
	
	/**
	 * Parametrisierter Konstruktor fuer den EinstellungsValidator
	 * @param klausurphase_bis 
	 * @param klausurphase_von
	 * @param tag_start
	 * @param tag_ende
	 */
	public EinstellungsValidator(Date klausurphase_bis, Date klausurphase_von, Date tag_start, Date tag_ende){
		this.klausurphase_bis = klausurphase_bis;
		this.klausurphase_von = klausurphase_von;
		this.tag_start = tag_start;
		this.tag_ende = tag_ende;
		this.abstand_tage = 365;
	}

	
	/**
	 * Methode, in welcher die gemachten Einstellungen auf Wertebereiche etc. ueberprueft werden
	 * Return true wenn alle Werte gueltig sind, ansonsten false
	 */
	@Override
	public boolean valide() {
		
		long diff = Helper.diffTage(klausurphase_von, klausurphase_bis);
				
		if(diff < 6 || diff > abstand_tage){
			return false;
		}
		if((tag_start).compareTo((tag_ende)) >= 0){
			return false;
		}
		return true;
	}
}
