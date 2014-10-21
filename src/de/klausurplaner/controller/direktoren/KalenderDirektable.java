package de.klausurplaner.controller.direktoren;

import java.util.Date;

import de.klausurplaner.controller.builder.TagBuildable;
import de.klausurplaner.controller.objekte.Einstellung;
import de.klausurplaner.validatoren.KalenderValidable;

/**
 * Interface Kalenderdirektable
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public interface KalenderDirektable {
	/**
	 * initialisiert den KalenderDirektor
	 */
	public void init();

	/**
	 * sagt dem TagComponentBuilder, dass die TagComponent mit neuen Tagen aktualisieren soll
	 */
	public void navigiereWocheZurueck();

	/**
	 * sagt dem TagComponentBuilder, dass die TagComponent mit neuen Tagen aktualisieren soll
	 */
	public void navigiereWocheVor();

	/**
	 * setzt einen Termin 
	 * @param o
	 */
	public void setzeTermin(Object o);

	/**
	 * aendert einen Termin
	 * @param o
	 */
	public void aendereTermin(Object o);

	/**
	 * entfernt einen Termin
	 * @param tag
	 * @param termin
	 */
	public void entferneTermin(Object tag, Object termin);

	/**
	 * initialisiert einen Kalender
	 * @param von
	 * @param bis
	 * @param tag_start
	 * @param tag_ende
	 */
	public void initKalender(Date von, Date bis, Date tag_start, Date tag_ende);

	/**
	 * gibt einen Validator zurueck
	 * @return Validator
	 */
	public KalenderValidable getValidator();

	/**
	 * gibt die Einstellungen zureck
	 * @return Einstellung
	 */
	public Einstellung getPreferenzen();

	/**
	 * gibt einen TagBuilder zurueck
	 * @return TagBuilder
	 */
	public TagBuildable getTagBuilder();

	/**
	 * aktualisiert die Ansicht
	 */
	public void aktualisiereAnsicht();

}