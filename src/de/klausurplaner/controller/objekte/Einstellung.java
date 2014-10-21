package de.klausurplaner.controller.objekte;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import de.klausurplaner.controller.builder.TagBuildable;
import de.klausurplaner.controller.builder.TagBuilder;

/**
 * Objekt-Klasse fuer die Einstellungen
 * 
 * @author Merle Hiort, Michael Sandritter, Benjamin Christiani
 * 
 */
public class Einstellung extends Observable {
	/**
	 * Datum an dem Klausurphase beginnt
	 */
	private Date von;
	/**
	 * Datum an dem Klausurphase endet
	 */
	private Date bis;
	/**
	 * Wert der bei false mehrere Klausuren an einem Tag zulaesst, umgekehrt
	 * halt nicht
	 */
	private boolean mehrereKlausuren;
	/**
	 * Wert der bei false mehrere Klausuren eines Semesters an einem Tag
	 * zulaesst, umgekehrt halt nicht
	 */
	private boolean mehrereKlausurenSemester;
	/**
	 * zeitlicher MindestAbstand zwischen zwei unterschiedlichen Klausuren an
	 * einem Tag
	 */
	private int mindAbstand;
	/**
	 * Wert der bei false Klausuren am Wochenende zulaesst, ansonsten nicht
	 */
	private boolean wochenende;
	/**
	 * Wert, der angibt auf welchen Wert die Startzeit gerundet werden soll
	 * (z.b. Viertelstunden)
	 */
	private int runde_startzeit;
	/**
	 * Datum an welchem der Klausurtag beginnt
	 */
	private Date tag_start;
	/**
	 * Datum, an welchem der Klausurtag endet
	 */
	private Date tag_ende;
	/**
	 * Wert, der bei false zulaesst, dass der Kategorieabstand zwischen zwei
	 * Klasuren ignoriert wird, sonst nicht
	 */
	private boolean kategorieAbstand;
	/**
	 * Liste, in welcher die im AnsichtsoptionsPanel anzuzeigenden Semester
	 * gespeichert werden
	 */
	private List<Integer> selektierteSemester;
	/**
	 * Wenn true wird die Klausurphase nach hinten verlaengert, wenn false nach
	 * vorne verlaengert
	 */
	private boolean klausurPhaserVerlaengernDirekt;
	/**
	 * Anzahl der Wochen, um die die Klausurphase verlaengert werden soll
	 */
	private int anzahlWochenVerlaengern;
	/**
	 * TagBuilder
	 */
	private TagBuildable tagBuilder;

	/**
	 * Parameterloser Konstruktor
	 */
	public Einstellung() {
		this.selektierteSemester = new ArrayList<Integer>();
	}

	/**
	 * Initialisierungsmethode
	 * @param tagBuilder
	 */
	public void init(TagBuildable tagBuilder) {
		this.tagBuilder = tagBuilder;
	}

	/**
	 * Initialisierungsmethode
	 * @param klausurPhaseVerlaengernDirekt
	 * @param anzahlWochenVerlaengern
	 * @param mehrereKlausuren
	 * @param mehrereKlausurenSemester
	 * @param mindAbstand
	 * @param wochenende
	 * @param runde_startzeit
	 * @param kategorieAbstand
	 */
	public void init(boolean klausurPhaseVerlaengernDirekt, int anzahlWochenVerlaengern, boolean mehrereKlausuren, boolean mehrereKlausurenSemester, int mindAbstand,boolean wochenende, int runde_startzeit, boolean kategorieAbstand) {
		this.klausurPhaserVerlaengernDirekt = klausurPhaseVerlaengernDirekt;
		this.anzahlWochenVerlaengern = anzahlWochenVerlaengern;
		this.mehrereKlausuren = mehrereKlausuren;
		this.mehrereKlausurenSemester = mehrereKlausurenSemester;
		this.mindAbstand = mindAbstand;
		this.wochenende = wochenende;
		this.runde_startzeit = runde_startzeit;
		this.kategorieAbstand = kategorieAbstand;
		// Methode die Klausurphaser verlaengert
		klausurPhaseVerlaengern();
	}

	/**
	 * Initialisierungsmethode
	 * @param von1
	 * @param bis1
	 * @param mehrereKlausuren
	 * @param mindAbstand
	 * @param wochenende
	 * @param feiertage
	 * @param runde_startzeit
	 * @param tag_start1
	 * @param tag_ende1
	 * @param merfachKlausuren
	 * @param kategorieAbstand
	 */
	public void init(Date von, Date bis, boolean mehrereKlausuren,boolean mehrereKlausurenSemester, int mindAbstand,boolean wochenende, int runde_startzeit, Date tag_start, Date tag_ende, boolean kategorieAbstand) {
		this.von = von;
		this.bis = bis;
		this.tag_start = tag_start;
		this.tag_ende = tag_ende;
		this.mehrereKlausuren = mehrereKlausuren;
		this.mehrereKlausurenSemester = mehrereKlausurenSemester;
		this.mindAbstand = mindAbstand;
		this.wochenende = wochenende;
		this.runde_startzeit = runde_startzeit;
		this.kategorieAbstand = kategorieAbstand;
	}

	/**
	 * Initialisierungsmethode
	 * @param mehrereKlausuren
	 * @param mehrereKlausurenSemester
	 * @param mindAbstand
	 * @param wochenende
	 * @param runde_startzeit
	 * @param kategorieAbstand
	 */
	public void init(boolean mehrereKlausuren,boolean mehrereKlausurenSemester, int mindAbstand,boolean wochenende, int runde_startzeit, boolean kategorieAbstand) {
		this.mehrereKlausurenSemester = mehrereKlausurenSemester;
		this.mehrereKlausuren = mehrereKlausuren;
		this.mindAbstand = mindAbstand;
		this.wochenende = wochenende;
		this.runde_startzeit = runde_startzeit;
		this.kategorieAbstand = kategorieAbstand;
	}

	/**
	 * Methode um die Klausurphase um eine bestimme Anzahl von Wochen zu verlaengern
	 */
	public void klausurPhaseVerlaengern() {
		// hole dir auch die gesamt Tag-Liste
		List<Tag> tage = tagBuilder.tage(); 
		int anzahlneuerTage = anzahlWochenVerlaengern * 7;
		
		// wenn Klausurphase nach hinten verlaengert werden soll
		if (klausurPhaserVerlaengernDirekt) {
			// hole den ersten Tag der Klausurphase
			Date ersterTag = tagBuilder.ersterTag(); 
			Calendar cal = Calendar.getInstance();
			cal.setTime(ersterTag);
			for (int i = 0; i < anzahlneuerTage; i++) {
				// setze akutelles Datum einen Tag zurueck
				cal.add(Calendar.DATE, -1); 
				// erstelle neuen Tag mit diesem Tag
				Tag tag = new Tag(cal.getTime()); 
				tage.add(0, tag); //
			}
			tagBuilder.aenderErsterTagKlausurphase(tage.get(0).getDatum());
			setChanged();
			notifyObservers();
			// ansonsten verlaengere Klausurphase nach vorne
		} else {
			Date letzterTag = tagBuilder.letzterTag();
			Calendar cal = Calendar.getInstance();
			cal.setTime(letzterTag);
			for (int i = 0; i < anzahlneuerTage; i++) {
				cal.add(Calendar.DATE, +1);
				Tag tag = new Tag(cal.getTime());
				((TagBuilder) tagBuilder).addTag(tag);
			}
			int index = tage.size() - 1;
			tagBuilder.aenderLetzterTagKlausurphase(tage.get(index).getDatum());
			setChanged();
			notifyObservers();
		}
	}

	public Date getVon() {
		return von;
	}

	public void setVon(String von) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd");
		try {
			this.von = format.parse(von);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Date getBis() {
		return bis;
	}

	public void setBis(String bis) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd");
		try {
			this.bis = format.parse(bis);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public boolean isMehrereKlausren() {
		return mehrereKlausuren;
	}

	public void setMehrereKlausren(boolean mehrereKlausren) {
		this.mehrereKlausuren = mehrereKlausren;
	}

	public boolean isMehrereKlausrenSemester() {
		return mehrereKlausurenSemester;
	}

	public void setMehrereKlausrenSemester(boolean mehrereKlausurenSemester) {
		this.mehrereKlausurenSemester = mehrereKlausurenSemester;
	}

	public int getMindAbstand() {
		return mindAbstand;
	}

	public void setMindAbstand(int mindAbstand) {
		this.mindAbstand = mindAbstand;
	}

	public boolean isWochenende() {
		return wochenende;
	}

	public void setWochenende(boolean wochenende) {
		this.wochenende = wochenende;
	}

	public int getRunde_startzeit() {
		return runde_startzeit;
	}

	public void setRunde_startzeit(int runde_startzeit) {
		this.runde_startzeit = runde_startzeit;
	}

	public Date getTag_start() {
		return tag_start;
	}

	public void setTag_start(String tag_start) {
		SimpleDateFormat format = new SimpleDateFormat("hh:mm");
		try {
			this.tag_start = format.parse(tag_start);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Date getTag_ende() {
		return tag_ende;
	}

	public void setTag_ende(String tag_ende) {
		SimpleDateFormat format = new SimpleDateFormat("kk:mm");
		try {
			this.tag_ende = format.parse(tag_ende);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public boolean isKategorieAbstand() {
		return kategorieAbstand;
	}

	public void setKategorieAbstand(boolean kategorieAbstand) {
		this.kategorieAbstand = kategorieAbstand;
	}

	@Override
	/**
	 * ToString-Methode fuer die Einstellungen
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nMehrere Klausuren: " + this.mehrereKlausuren);
		sb.append("\nMehrere Klausuren Semester: "
				+ this.mehrereKlausurenSemester);
		sb.append("\nMin Abstand: " + this.mindAbstand);
		sb.append("\nRunde Startzeit: " + this.runde_startzeit);
		sb.append("\nWochenden: " + this.wochenende);
		sb.append("\nKategorie: " + this.kategorieAbstand);
		return sb.toString();
	}

	public List<Integer> getSelektierteSemester() {
		return selektierteSemester;
	}

	public void addSelektierteSemester(int semester) {
		selektierteSemester.add(semester);
	}

	/**
	 * Methode, um selektierte Semester wieder aus der Liste zu entfernen
	 * @param semester
	 */
	public void removeSelektierteSemester(int semester) {
		int index = 0;
		for (int i = 0; i < selektierteSemester.size(); i++) {
			if (selektierteSemester.get(i) == semester) {
				index = i;
			}
		}
		selektierteSemester.remove(index);
	}

	public boolean isKlausurPhaserVerlaengernDirekt() {
		return klausurPhaserVerlaengernDirekt;
	}

	public void setKlausurPhaserVerlaengernDirekt(
			boolean klausurPhaserVerlaengernDirekt) {
		this.klausurPhaserVerlaengernDirekt = klausurPhaserVerlaengernDirekt;
	}

	public int getAnzahlWochenVerlaengern() {
		return anzahlWochenVerlaengern;
	}

	public void setAnzahlWochenVerlaengern(int anzahlWochenVerlaengern) {
		this.anzahlWochenVerlaengern = anzahlWochenVerlaengern;
	}
}
