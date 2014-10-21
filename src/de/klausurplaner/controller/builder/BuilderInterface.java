package de.klausurplaner.controller.builder;

/**
 * Interface fuer jegliche Builder.
 * 
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public interface BuilderInterface {
	
	/**
	 * Objekte, welche der Builder verwaltet, werden geladen.
	 */
	void ladeBuilderDaten();
	
	/**
	 * Die zu verwaltende Daten werden zurueckgeliefert.
	 * @return Liste der Objekte.
	 */
	Object getBuilderDaten();
}
