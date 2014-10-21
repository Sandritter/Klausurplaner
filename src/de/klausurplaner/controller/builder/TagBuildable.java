package de.klausurplaner.controller.builder;

import java.util.Date;
import java.util.List;

import de.klausurplaner.controller.objekte.Tag;

/**
 * Interface fuer einen TagBuilder
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public interface TagBuildable {
	
	/**
	 * initialisiert den TagBuilder
	 * @param von
	 * @param bis
	 * @param tag_start
	 * @param tag_ende
	 */
	public void init(Date von, Date bis, Date tag_start, Date tag_ende);
	/**
	 * liefert den Tag anhand seines Datums zurueck
	 * @param d
	 * @return Tag
	 */
	public Tag getTagByDatum(Date d);
	/**
	 * liefert eine Liste der Tage zurueck
	 * @return List
	 */
	public List<Tag> tage();
	/**
	 * liefert den ersten Tag der Klausurphase zurueck
	 * @return Date
	 */
	public Date ersterTag();
	/**
	 * liefert den letzten Tag der Klausurphase zurueck
	 * @return Date
	 */
	public Date letzterTag();
	/**
	 * liefert die Startzeit eines jeden Tages zurueck
	 * @return Date
	 */
	public Date startUhrzeit();
	/**
	 * liefert die Endzeit eines jeden Tages zurueck
	 * @return Date
	 */
	public Date endeUhrzeit();
	/**
	 * liefert den letzten Tag der aktuellen Woche zurueck
	 * @return Date
	 */
	public Date letzterTagAktWoche();
	/**
	 * liefert den ersten Tag der aktuellen Wochen zurueck
	 * @return Date
	 */
	public Date ersterTagAktWoche();
	/**
	 * setzt den letzten Tag der aktuellen Woche
	 * @param d Date
	 */
	public void aenderLetztenTagAktWoche(Date d);
	/**
	 * setzt den ersten Tag der aktuellen Woche
	 * @param d Date
	 */
	public void aenderErstenTagAktWoche(Date d);
	/**
	 * gibt einen Tag zurueck wenn Datum uebereinstimmt
	 * @param datum
	 * @return Tag
	 */
	public Tag getTagByDatum2(Date datum);
	/**
	 * setzt den ersten Tag der Klausurphase
	 * @param d
	 */
	public void aenderErsterTagKlausurphase(Date d);
	/**
	 * setzt den letzte Tag der Klausurphase
	 * @param d
	 */
	public void aenderLetzterTagKlausurphase(Date d);
}
