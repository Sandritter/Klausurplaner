package de.klausurplaner.benutzeroberflaeche;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import de.klausurplaner.controller.builder.TagBuildable;
import de.klausurplaner.controller.direktoren.KalenderDirektable;
import de.klausurplaner.controller.direktoren.KalenderDirektor;
import de.klausurplaner.controller.objekte.Einstellung;
import de.klausurplaner.controller.objekte.Tag;
import de.klausurplaner.helper.Helper;

/**
 * 
 * @author Michael Sandritter, Merle Hiort, Benjamin Christiani 
 * Erstellt TagComponents
 * aktualisiert TagComponents mit neuen Tagen 
 */
public class TagComponentBuilder implements Observer {

	private List<TagComponent> tagComponentList;
	private final int ANZAHLTAGE_IM_KALENDER = 7;
	private final int KALENDERWIDTH = 840;
	private TagBuildable tagBuilder;
	private int dauer;
	private KalenderDirektable kalDirektor;

	/**
	 * 
	 * @param tagBuilder Verwalter der Klausurphasen-Tage
	 */
	public TagComponentBuilder(KalenderDirektable kalDirektor,
			TagBuildable tagBuilder) {
		this.kalDirektor = kalDirektor;
		this.tagBuilder = tagBuilder;
		this.tagComponentList = new ArrayList<TagComponent>();
	}

	/**
	 * Hilfsmethode die den TagComponentBuilder initialisiert
	 */
	public void init() {
		// Bereche wie viele Stunden ein Klausurtag hat
		Date date1 = this.tagBuilder.startUhrzeit();
		Date date2 = this.tagBuilder.endeUhrzeit();
		this.dauer = (int) ((date2.getTime() - date1.getTime()) / (1000L * 60L * 60L));

		// hole dir den ersten Tag
		Tag tag = this.tagBuilder.tage().get(0);
		// und setze je nach wochentag die Anzahl der Dummy-Tage um eine Woche
		// immer an einem Montag beginnen zu lassen
		int dummyCount = 0;
		Calendar cal = Calendar.getInstance();
		for (int j = 0; j < ANZAHLTAGE_IM_KALENDER; j++) {
			cal.setTime(tag.getDatum());
			if (Helper.checkWochentagStart(cal, j)) {
				dummyCount = j;
				break;
			}
		}
		boolean isErsterTag = false;
		for (int i = 0; i < ANZAHLTAGE_IM_KALENDER; i++) {
			TagComponent tagComponent = null;
			// solange Tag nicht in der Klausurphase setzte in der TagComponent
			// einen DummyTag
			if (i < dummyCount) {
				Tag dummyTag = new Tag();
				tagComponent = new TagComponent(KALENDERWIDTH/ ANZAHLTAGE_IM_KALENDER, dauer, dummyTag, kalDirektor);
				tagComponent.setEnabled(false);
			} else {
				Tag echterTag = (this.tagBuilder.tage()).get(i - dummyCount);
				if (!isErsterTag) {
					this.tagBuilder.aenderErstenTagAktWoche(echterTag.getDatum());
					isErsterTag = true;
				}
				tagComponent = new TagComponent(KALENDERWIDTH/ ANZAHLTAGE_IM_KALENDER, dauer, echterTag, kalDirektor);
			}
			this.tagComponentList.add(tagComponent);
		}
		this.tagBuilder.aenderLetztenTagAktWoche(tagComponentList.get(6).getTag().getDatum());
		for (TagComponent t : tagComponentList) {
			((KalenderDirektor) kalDirektor).addObserver(t);
		}
	}

	public List<TagComponent> getTagComponentList() {
		return tagComponentList;
	}

	public void aktualisiereAnsicht() {
		for (TagComponent tc : tagComponentList) {
			tc.aktualisiereAnsicht();
		}
	}

	/**
	 * HilfsMethode die die TagComponentListe aktualisiert
	 * @param ersterTag der neue generierten Woche
	 */
	public void setTagComponentList(Date ersterTag) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(ersterTag);
		for (int i = 0; i < this.tagComponentList.size(); i++) {
			Tag tag = this.tagBuilder.getTagByDatum(cal.getTime());
			this.tagComponentList.get(i).setTag(tag); // setze Tag
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
	}

	public TagBuildable getTagBuilder() {
		return tagBuilder;
	}

	public int getDauer() {
		return dauer;
	}

	public void setDauer(int dauer) {
		this.dauer = dauer;
	}

	/**
	 * navigiert eine Woche zurueck und setzt Dummy-Tage wenn nicht alle Tage
	 * der Woche in der Klausurphase liegen
	 * 
	 * @param date, erster Tag der akutellen Woche von der aus zurueck navigiert werden soll
	 */
	public void setTagComponentListZurueck(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal = Helper.removeTage(cal, 7);
		for (int i = 0; i < ANZAHLTAGE_IM_KALENDER; i++) {
			Tag tag = tagBuilder.getTagByDatum2(cal.getTime());
			// wenn keine Dummy-Tage in der Woche vorhanden sind
			if (i == 0 && tag != null)
				this.tagBuilder.aenderErstenTagAktWoche(cal.getTime()); 
				// wenn Dummy-Tage vorhanden
			if (tag == null) {
				tag = new Tag();
				this.tagBuilder.aenderErstenTagAktWoche(tagBuilder.ersterTag()); 
			}
			if (i == ANZAHLTAGE_IM_KALENDER - 1)
				this.tagBuilder.aenderLetztenTagAktWoche(cal.getTime());
			tagComponentList.get(i).setTag(null);
			tagComponentList.get(i).setTag(tag);
			cal = Helper.addTage(cal, 1);
		}
	}

	/**
	 * navigiert eine Woche weiter und setzt Dummy-Tage wenn nicht alle Tage der
	 * Woche in der Klausurphase liegen
	 * 
	 * @param date, letzter Tag der akutellen Woche von der aus zurueck navigiert werden soll
	 */
	public void setTagComponentListVor(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		boolean flag = false;
		// mache 7 Schritte
		for (int i = 0; i < ANZAHLTAGE_IM_KALENDER; i++) {
			// rechne einen Tag auf
			cal = Helper.addTage(cal, 1);
			// setze ersten Tag der Berechnung auf den ersten Tag der ersten Woche
			if (i == 0)
				tagBuilder.aenderErstenTagAktWoche(cal.getTime());
			// hole dir den Tag mit entsprechendem Datum aus dem TagBuilder
			Tag tag = tagBuilder.getTagByDatum2(cal.getTime());
			// wenn Tag leer ist
			if (tag == null) {
				// erstelle einen Dummy-Tag
				tag = new Tag();
				// und setzte den letzten Tag der akutellen Wochen auf den letzten KlausurTag
				tagBuilder.aenderLetztenTagAktWoche(tagBuilder.letzterTag());
				flag = true;
			}
			if (i == ANZAHLTAGE_IM_KALENDER - 1 && !flag)
				tagBuilder.aenderLetztenTagAktWoche(cal.getTime());
			tagComponentList.get(i).setTag(null);
			tagComponentList.get(i).setTag(tag);
		}
	}

	@Override
	public void update(Observable observ, Object o) {
		if (observ instanceof Einstellung) {
			// erster Tag der ausgewaehlten Woche
		} else if (observ instanceof TagBuildable) {
			init(); // initialisiere TagComponentBuilder sobald TagBuilder da ist
		} else if (observ instanceof KalenderDirektable) {
			if (o instanceof String) {
				String s = (String) o;
				if (s.compareTo("isValid") == 0) {
					aktualisiereAnsicht();
				}
			}
		} else {
			Date ersterTag = (Date) o;
			setTagComponentList(ersterTag);
		}
	}

}
