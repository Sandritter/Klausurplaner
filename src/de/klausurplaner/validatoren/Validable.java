package de.klausurplaner.validatoren;

import java.io.IOException;

/**
 * Schnittstelle, um eingelesene Daten auf Richtigkeit zu ueberpruefen.
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public interface Validable {
	
	/**
	 * Prueft ob Daten gueltig sind.
	 * @return
	 */
	boolean valide();
}
