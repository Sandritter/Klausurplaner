package de.klausurplaner.controller.objekte;

/**
 * Hilfsklasse um beim Aendern des Datums einer Klausur die Informationen gesammelt weiterzugeben
 * @author Michael Sandritter, Benjamin Christani, Merle Hiort
 *
 */
public class Aenderung {
	
	/**
	 * Tag, an dem die Klausur vorher war
	 */
	private Tag alterTag;
	/**
	 * Tag, an den die Klausur jetzt gesetzt werden soll
	 */
	private Tag neuerTag;
	/**
	 * Die zu verschiebende Klausur
	 */
	private Klausur klausur;
	
	/**
	 * Parametrisierter Konstruktor fuer die Aenderungsklasse
	 * @param nT neuer Tag
	 * @param aT alter Tag
	 * @param k Klausur
	 */
	public Aenderung(Tag nT, Tag aT, Klausur k){
		this.neuerTag = nT;
		this.alterTag = aT;
		this.klausur = k;
	}

	public Tag getAlterTag() {
		return alterTag;
	}

	public void setAlterTag(Tag alterTag) {
		this.alterTag = alterTag;
	}

	public Tag getNeuerTag() {
		return neuerTag;
	}

	public void setNeuerTag(Tag neuerTag) {
		this.neuerTag = neuerTag;
	}

	public Klausur getKlausur() {
		return klausur;
	}

	public void setKlausur(Klausur klausur) {
		this.klausur = klausur;
	}
}
