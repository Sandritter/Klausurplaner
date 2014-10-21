package de.klausurplaner.controller.direktoren;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import de.klausurplaner.controller.builder.AufsichtspersonBuilder;
import de.klausurplaner.controller.builder.KlausurBuildable;
import de.klausurplaner.controller.builder.KlausurBuilder;
import de.klausurplaner.controller.builder.RaumBuilder;
import de.klausurplaner.controller.builder.TagBuildable;
import de.klausurplaner.controller.objekte.Aenderung;
import de.klausurplaner.controller.objekte.Aufsichtsperson;
import de.klausurplaner.controller.objekte.Einstellung;
import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.controller.objekte.KlausurSetzer;
import de.klausurplaner.controller.objekte.Muellabfuhr;
import de.klausurplaner.controller.objekte.Navigation;
import de.klausurplaner.controller.objekte.Raum;
import de.klausurplaner.controller.objekte.Tag;
import de.klausurplaner.helper.Helper;
import de.klausurplaner.validatoren.KalenderValidable;
import de.klausurplaner.validatoren.KalenderValidator;

/**
 * DirektorKlasse, verteilt die Aufgaben des Kalenders an die Logik
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter 
 */
public class KalenderDirektor extends Observable implements KalenderDirektable,Serializable {
	private static final long serialVersionUID = 3799604232774870014L;
	private TagBuildable tagBuilder;
	private DirektorInterface einleseDirektor;
	private KlausurBuildable klausurBuilder;
	private RaumBuilder raumBuilder;
	private AufsichtspersonBuilder aufsichtspersonBuilder;
	private KalenderValidable kalenderValidator;
	private Einstellung einstellung;

	/**
	 * Parametrisierter Konstruktor
	 * @param tagBuilder, beinhaltet alle Tage der Klausurphase
	 */
	public KalenderDirektor(TagBuildable tagBuilder, DirektorInterface einleseDirektor, Einstellung einstellung) {
		this.tagBuilder = tagBuilder;
		this.einleseDirektor = einleseDirektor;
		this.einstellung = einstellung;
	}
	
	
	/**
	 * initialisiert den Kalender
	 */
	public void initKalender(Date von, Date bis, Date tag_start, Date tag_ende) {
		tagBuilder.init(von, bis, tag_start, tag_ende);
	}
	
	/**
	 * Initialisierungsmethode
	 */
	@Override
	public void init() {
		this.kalenderValidator = new KalenderValidator(tagBuilder, einstellung);
		this.klausurBuilder = (KlausurBuildable) einleseDirektor.getBuilder(new KlausurBuilder());
		this.aufsichtspersonBuilder = (AufsichtspersonBuilder) einleseDirektor.getBuilder(new AufsichtspersonBuilder());
		this.raumBuilder = (RaumBuilder) (einleseDirektor.getBuilder(new RaumBuilder()));
	}
	
	/**
	 * navigiert eine Woche zurueck
	 */
	public void navigiereWocheZurueck() {
		if (kalenderValidator.navigationZurueckValide(tagBuilder.ersterTagAktWoche())) {
			setChanged();
			Navigation nav = new Navigation(true,tagBuilder.ersterTagAktWoche());
			notifyObservers(nav);
		}
	}

	/**
	 * navigiert eine Woche weiter
	 */
	public void navigiereWocheVor() {
		if (kalenderValidator.navigationVorValide(tagBuilder.letzterTagAktWoche())) {
			setChanged();
			Navigation nav = new Navigation(false,tagBuilder.letzterTagAktWoche());
			notifyObservers(nav);
		}
	}

	/**
	 * Methode um eine die Woche neu zu zeichen, wenn sich die Einstellungen geaendert haben
	 */
	public void aktualisiereAnsicht() {
		setChanged();
		notifyObservers("isValid");
	}


	/**
	 * Methode um eine neue Klausur in den Kalender zu setzen und alle 
	 * Werte vom KlausurSetzer in die Klausur zu schreiben
	 */
	@Override
	public void setzeTermin(Object o) {
		KlausurSetzer klausurSetzer = (KlausurSetzer) o;
		Klausur klausur = klausurBuilder.getKlausurByName(klausurSetzer.getKlausurName());
		if (klausur.isMehrtaegig()) {
			klausur = new Klausur(klausur);
			if (klausurSetzer.getRaeume().size() > 0) {
				klausur.setRaeume(klausurSetzer.getRaeume());
			}
			if (klausurSetzer.getAufsPersonen().size() > 0) {
				klausur.setAufsichtspersonen(klausurSetzer.getAufsPersonen());
			}
		} else {
			klausur.removeRaeume();
			if (klausurSetzer.getRaeume().size() > 0) {
				for (Raum r : klausurSetzer.getRaeume()) {
					klausur.addRaum(r);
				}
			}
			klausur.removeAufsichtspersonen();
			if (klausurSetzer.getAufsPersonen().size() > 0) {
				for (Aufsichtsperson p : klausurSetzer.getAufsPersonen()) {
					klausur.addAufsichtsperson(p);
				}
			}
			// zeige sie nicht mehr in der Verfuegbarkeitsliste an
			klausur.setGesetzt(true);
		}
		Date startZeit = klausurSetzer.getStartzeit();
		klausur.setStartZeit(startZeit);
		klausur.setDauer(klausurSetzer.getDauer());
		klausur.setTeilnehmer(klausurSetzer.getTeilnehmerZahl());
		klausur.setKategorie(klausurSetzer.getKategorie());
		
		// Beim Setzen sind Klausuren grundsaetzlich valide, da alle
		// Validierungen durchgelaufen sind
		klausur.setValide(true);
		// fuege Klausur zum Tag hinzu
		klausurSetzer.getTag().addKlausur(klausur);
		setChanged();
		notifyObservers(klausurSetzer.getTag());
		setChanged();
		notifyObservers(klausur.getSemester());
		if (klausur.isValide()) {
			setChanged();
			notifyObservers("isValid");
		}
	}

	/**
	 * Methode um eine schon gesetzte Klausur zu editieren
	 */
	@Override
	public void aendereTermin(Object o) {
		KlausurSetzer klausurSetzer = (KlausurSetzer) o;
		Tag neuerTag = klausurSetzer.getTag();
		Tag alterTag = klausurSetzer.getAlterTag();

		// setze neue Werte in die Klausur
		Klausur klausur = null;
		for (Klausur k : tagBuilder.getTagByDatum2(alterTag.getDatum())
				.getKlausuren()) {
			if (k.getKlausurname().compareTo(klausurSetzer.getKlausurName()) == 0
					&& Helper.isInKlausurPhase(k.getStartZeit(),
							klausurSetzer.getStartzeit())) {
				klausur = k;
			}
		}
		klausur.setStartZeit(klausurSetzer.getStartzeit());
		klausur.setDauer(klausurSetzer.getDauer());
		klausur.setTeilnehmer(klausurSetzer.getTeilnehmerZahl());
		klausur.setKategorie(klausurSetzer.getKategorie());
		
		klausur.removeAufsichtspersonen();
		for (Aufsichtsperson p : klausurSetzer.getAufsPersonen()) {
			klausur.addAufsichtsperson(p);
		}
		klausur.removeRaeume();
		for (Raum r : klausurSetzer.getRaeume()) {
			klausur.addRaum(r);
		}
		// ggf. aufgrund der Einstellungen entstandene rote Markierung wieder
		// entfernen
		klausur.setValide(true);
		klausur.removeWarnhinweise();
		// loesche Klausur vom alten Tag und fuege sie zum neuen hinzu
		alterTag.removeKlausur(klausur.getKlausurname());
		neuerTag.addKlausur(klausur);
		Aenderung aenderung = new Aenderung(neuerTag, alterTag, klausur);
		setChanged();
		notifyObservers(aenderung);
		setChanged();
		notifyObservers("isValid");
	}

	public TagBuildable getTagBuilder() {
		return tagBuilder;
	}

	public DirektorInterface getEinDirektorInterface() {
		return einleseDirektor;
	}

	/**
	 * aktualisiert die im Ansichtsoptionspanel selektierten Semester
	 * 
	 * @param selektiert
	 *            boolean, true wenn item selektiert wurde, false im
	 *            gegenbeispiel
	 * @param semester
	 *            selktiertes oder deselektiertes Semester
	 */
	public void updateSelektierteSemester(boolean selektiert, int semester) {
		if (selektiert) {
			einstellung.addSelektierteSemester(semester);
		} else {
			einstellung.removeSelektierteSemester(semester);
		}
		setChanged();
		notifyObservers("ansichtChanged");
	}

	/**
	 * liefert eine Liste von freien Raeumen zurueck
	 * @param datum
	 * @param uhrzeit
	 * @param dauer
	 * @return List
	 */
	public List<Raum> getFreieRaeume(Date datum, Date uhrzeit, int dauer) {
		List<Raum> freieRaeume = new ArrayList<Raum>();
		@SuppressWarnings("unchecked")
		ArrayList<Raum> raeume = (ArrayList<Raum>) raumBuilder.getBuilderDaten();
		for (Raum r : raeume) {
			if (kalenderValidator.raumFrei(datum, uhrzeit, dauer, r)) { // wenn Raum nicht belegt ist
				freieRaeume.add(r);
			}
		}
		return freieRaeume;
	}

	/**
	 * liefert eine Liste von freien Aufsichtspersonen zurueck
	 * @param datum
	 * @param uhrzeit
	 * @param dauer
	 * @return List
	 */
	public List<Aufsichtsperson> getFreieAufsichten(Date datum, Date uhrzeit,
			int dauer) {
		List<Aufsichtsperson> freieAufsichten = new ArrayList<Aufsichtsperson>();
		@SuppressWarnings("unchecked")
		ArrayList<Aufsichtsperson> aufsichten = (ArrayList<Aufsichtsperson>) aufsichtspersonBuilder.getBuilderDaten();
		for (Aufsichtsperson a : aufsichten) {
			if (kalenderValidator.aufsichtVerfuegbar(datum, uhrzeit, dauer, a)) { // wenn Aufsicht verfuegbar ist
				freieAufsichten.add(a);
			}
		}
		return freieAufsichten;
	}

	/**
	 * entfernt mitgegebenen Termin aus dem Tag
	 */
	public void entferneTermin(Object tag, Object termin) {
		Tag derTag = tagBuilder.getTagByDatum2(((Tag) tag).getDatum());
		Klausur klausur = (Klausur) termin;
		klausur.setGesetzt(false);
		derTag.removeKlausur(klausur.getKlausurname());
		Muellabfuhr muellabfuhr = new Muellabfuhr(derTag, klausur);
		setChanged();
		notifyObservers(muellabfuhr);
	}

	@Override
	public KalenderValidable getValidator() {
		return this.kalenderValidator;
	}

	@Override
	public Einstellung getPreferenzen() {
		return this.einstellung;
	}

}
