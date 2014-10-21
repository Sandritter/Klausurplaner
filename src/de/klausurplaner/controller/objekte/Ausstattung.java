package de.klausurplaner.controller.objekte;

/**
 * Objekt-Klasse Ausstattung
 * @author Benjamin Christiani, Michael Sandritter, Merle Hiort
 *
 */
public class Ausstattung {
	/**
	 * Name der Ausstattung
	 */
	private String bezeichnung;
	
	
	/**
	 * Default-Konstruktor fuer das Objekt Ausstattung
	 */
	public Ausstattung(){	
	}
	
	/**
	 * Parametrisierter Konstruktor fuer das Objekt Ausstattung
	 * @param bezeichnung Name der Ausstattung
	 * @param verfuegbarkeit Angabe, ob die Ausstattung gerade verfuegbar ist
	 */
	public Ausstattung(String bezeichnung){
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Getter fuer das Attribut Bezeichnung
	 * @return bezeichnung
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * Setter fuer das Attribut Bezeichnung
	 * @param bezeichnung
	 */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
}
