package de.klausurplaner.dateneinausgabe.einlesen;

/**
 * Schnittstelle, um Daten einzuladen.
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public interface DatenladerInterface {
	
	/**
	 * Lade Stammdaten
	 * 
	 * @param typ - Welche Daten man benoetigt
	 * @return angeforderten Daten
	 */
	Object ladeStammDaten(Object typ);
}
