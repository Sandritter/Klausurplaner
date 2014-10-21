package de.klausurplaner.controller.objekte;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Hilfsklasse um die Informationen einer zu setzenden Klausur zu speichern und weiterzugeben
 * @author Merle Hiort, Benjamin Christiani, Michael Sandritter
 */
public class KlausurSetzer {
	/**
	 * Name der Klausur
	 */
	private String klausurName;
	/**
	 * Semester, in welchem die Klausur geschrieben wird
	 */
	private int semester; 
	/**
	 * Startzeit der Klausur
	 */
	private Date startzeit;
	/**
	 * Anzahl der Teilnehmer pro Pruefungstermin
	 */
	private int teilnehmerZahl;
	/**
	 * Dauer der Klausur in Minuten
	 */
	private int dauer; 
	/**
	 * Liste der Raeume, in welchen die Klausur geschrieben wird
	 */
	private List<Raum> raeume;
	/**
	 * Liste der Aufsichtspersonen, welche fuer die Klausur eingeteilt sind
	 */
	private List<Aufsichtsperson> aufsPersonen;
	/**
	 * Kategorie der Klausur, d.h. wie viele Tage (= Lerntage) Abstand die Klausur zu anderen Klausuren des gleichen
	 * Semesters haben muss 
	 */
	private int kategorie;
	/**
	 * Tag, an welchen die Klausur gesetzt werden soll
	 */
	private Tag tag;
	/**
	 * Tag, von welchem die Klausur verschoben wurde
	 */
	private Tag alterTag;
	
	/**
	 * Parameterloser Konstruktor
	 */
	public KlausurSetzer(){
		raeume = new ArrayList<Raum>();
		aufsPersonen = new ArrayList<Aufsichtsperson>();
	}
	
	/**
	 * Parametrisierter Konstruktor
	 * @param tag, aktuell angeklickter Tag
	 * @param klausurName, name der ausgewaehlten klausur
	 * @param yPos, y-Koordinate des ClickEvents 
	 * @param teilnehmerZahl, Anzahl Teilnehmer einer Klausur
	 * @param dauer, einer Klausur in Minuten
	 */
	public KlausurSetzer(Tag tag, int semester, String klausurName, Date startZeit, int teilnehmerZahl, int dauer, List<Raum> raeume , List<Aufsichtsperson> aufsPersonen, int kategorie){
		this.semester = semester;
		this.klausurName = klausurName;
		this.startzeit = startZeit;
		this.teilnehmerZahl = teilnehmerZahl;
		this.dauer = dauer;
		this.raeume = raeume;
		this.aufsPersonen = aufsPersonen;
		this.tag = tag;
		this.kategorie = kategorie;
	}
	
	/**
	 * Initialisierungsmethode
	 * @param tag aktuell angeklickter Tag
	 * @param semester Semester
	 * @param klausurName Name 
	 * @param startZeit Startzeit
	 * @param teilnehmerZahl Teilnehmerzahl
	 * @param dauer Dauer
	 * @param raeume Raeume
	 * @param aufsPersonen Aufsichtspersonen
	 * @param kategorie Kategorie
	 */
	public void init(Tag tag, int semester, String klausurName, Date startZeit, int teilnehmerZahl, int dauer, List<Raum> raeume , List<Aufsichtsperson> aufsPersonen, int kategorie){
		this.semester = semester;
		this.klausurName = klausurName;
		this.startzeit = startZeit;
		this.teilnehmerZahl = teilnehmerZahl;
		this.dauer = dauer;
		this.raeume = raeume;
		this.aufsPersonen = aufsPersonen;
		this.tag = tag;
		this.kategorie = kategorie;
	}
	
	/**
	 * Initialisierungsmethode
	 * @param tag aktuell angeklickter Tag
	 * @param semester Semester
	 * @param klausurName Name 
	 * @param startZeit Startzeit
	 * @param teilnehmerZahl Teilnehmerzahl
	 * @param dauer Dauer
	 * @param raeume Raeume
	 * @param aufsPersonen Aufsichtspersonen
	 * @param kategorie Kategorie
	 */
	public void init(Tag tag, Tag alterTag, int semester, String klausurName, Date startZeit, int teilnehmerZahl, int dauer, List<Raum> raeume , List<Aufsichtsperson> aufsPersonen, int kategorie){
		this.semester = semester;
		this.klausurName = klausurName;
		this.startzeit = startZeit;
		this.teilnehmerZahl = teilnehmerZahl;
		this.dauer = dauer;
		this.raeume = raeume;
		this.aufsPersonen = aufsPersonen;
		this.tag = tag;
		this.setAlterTag(alterTag);
		this.kategorie = kategorie;
	}
	
	/**
	 * Initialisierungsmethode
	 * @param tag aktuell angeklickter Tag
	 * @param semester Semester
	 * @param klausurName Name 
	 * @param startZeit Startzeit
	 * @param teilnehmerZahl Teilnehmerzahl
	 * @param dauer Dauer
	 * @param raeume Raeume
	 * @param aufsPersonen Aufsichtspersonen
	 */
	public void init(Tag tag, int semester, String klausurName, Date startZeit, int teilnehmerZahl, int dauer, List<Raum> raeume , List<Aufsichtsperson> aufsPersonen){
		this.semester = semester;
		this.klausurName = klausurName;
		this.startzeit = startZeit;
		this.teilnehmerZahl = teilnehmerZahl;
		this.dauer = dauer;
		this.raeume = raeume;
		this.aufsPersonen = aufsPersonen;
		this.tag = tag;
	}

	public int getSemester(){
		return this.semester;
	}
	
	public void setSemester(int semester){
		this.semester = semester;
	}
	
	public String getKlausurName() {
		return klausurName;
	}

	public void setKlausurName(String klausurName) {
		this.klausurName = klausurName;
	}

	public Date getStartzeit() {
		return startzeit;
	}

	public void setStartzeit(Date startzeit) {
		this.startzeit = startzeit;
	}

	public int getTeilnehmerZahl() {
		return teilnehmerZahl;
	}

	public void setTeilnehmerZahl(int teilnehmerZahl) {
		this.teilnehmerZahl = teilnehmerZahl;
	}

	public int getDauer() {
		return dauer;
	}

	public void setDauer(int dauer) {
		this.dauer = dauer;
	}

	public List<Raum> getRaeume() {
		return raeume;
	}

	public void setRaeume(List<Raum> raeume) {
		this.raeume = raeume;
	}

	public List<Aufsichtsperson> getAufsPersonen() {
		return aufsPersonen;
	}

	public void setAufsPersonen(List<Aufsichtsperson> aufsPersonen) {
		this.aufsPersonen = aufsPersonen;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	public int getKategorie(){
		return this.kategorie;
	}
	
	public void setKategorie(int k){
		this.kategorie = k;
	}

	public Tag getAlterTag() {
		return alterTag;
	}

	public void setAlterTag(Tag alterTag) {
		this.alterTag = alterTag;
	}

}
