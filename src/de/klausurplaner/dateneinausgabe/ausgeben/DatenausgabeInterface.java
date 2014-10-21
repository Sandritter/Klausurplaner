package de.klausurplaner.dateneinausgabe.ausgeben;

/**
 * Schnittstelle, um Ergebnisse abzuspeichern und exportieren.
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public interface DatenausgabeInterface {
	
	/**
	 * Daten werden exportiert
	 * @param pfad
	 */
	void exportiereDaten(String pfad);
}
