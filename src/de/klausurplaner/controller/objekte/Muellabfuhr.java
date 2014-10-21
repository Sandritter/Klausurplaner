package de.klausurplaner.controller.objekte;

/**
 * Hilfsmethode fuer das Loeschen einer Klausur
 * @author Michael Sandritter, Merle Hiort, Benjamin Christiani
 *
 */
public class Muellabfuhr {
	/**
	 * Tag, an welchem die zu loeschende Klausur ist
	 */
	private Tag tag;
	/**
	 * die zu loeschende Klausur
	 */
	private Klausur klausur;
	
	/**
	 * Parametrisierter Konstruktor
	 * @param tagi aktueller Tag
	 * @param klausuri aktuelle Klausur
	 */
	public Muellabfuhr(Tag tagi, Klausur klausuri){
		this.tag = tagi;
		this.klausur = klausuri;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Klausur getKlausur() {
		return klausur;
	}

	public void setKlausur(Klausur klausur) {
		this.klausur = klausur;
	}
}
