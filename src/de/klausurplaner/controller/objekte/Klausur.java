package de.klausurplaner.controller.objekte;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.base.Joiner;

/**
 * Klausurobjekt welches alle Information bezueglich der Pruefung beinhaltet
 * 

 * @author Merle Hiort, Benjamin Christiani, Michael Sandritter

 */

public class Klausur implements Serializable {
	private static final long serialVersionUID = -7698812660709603112L;
	/**
	 * Name der Klausur;
	 */
	private String klausurname;
	/**
	 * Nummer der Lehrveranstalungsnummer
	 */
	private String lvNo;
	/**
	 * Semester, in welchem die Klausur geschrieben wird 
	 */
	private int semester;
	/**
	 * Anzahl der Teilnehmer pro Pruefungstermin 
	 */
	private int teilnehmer;
	/**
	 * True wenn die Pruefung ueber mehrere Tage verteilt geschrieben wird, false, wenn sie nur an einem Tag gesetzt werden darf
	 */
	private boolean mehrtaegig;
	/**
	 * True, wenn die Klausur schon im Kalender gesetzt wurde
	 */
	private boolean gesetzt;
	/**
	 * Dauer der Klausur in Minuten
	 */
	private int dauer;
	/**
	 * Art der Klausur (MP = muendliche Pruefung, FG = Fachgespraech)
	 */
	private String art;
	/**
	 * Liste der fuer die Klausur benoetigten Ausstattungen (z.B. Beamer)
	 */
	private List<String> ausstattungen;
	/**
	 * Liste der Raeume, in welchen die Klausur geschrieben wird
	 */
	private List<Raum> raeume;
	/**
	 * Liste der Aufsichtspersonen, welche fuer diese Klausur eingeteilt sind
	 */
	private List<Aufsichtsperson> aufsichtspersonen;
	/**
	 * Startzeit der Klausur
	 */
	private Date startZeit;
	/**
	 * Kategorie der Klausur, d.h. wie viele Tage (= Lerntage) Abstand die Klausur zu anderen Klausuren des gleichen
	 * Semesters haben muss 
	 */
	private int kategorie;
	/**
	 * True, wenn die Klausur valide gemaess der Einstellungen ist, sonst false
	 */
	private boolean valide;
	/**
	 * Liste der Warnhinweise, falls die Klausur nicht valide ist
	 */
	private List<String> warnhinweise;

	/**
	 * Parameterloser Konstruktor
	 */
	public Klausur() {
		ausstattungen = new ArrayList<String>();
		aufsichtspersonen = new ArrayList<Aufsichtsperson>();
		raeume = new ArrayList<Raum>();
		warnhinweise = new ArrayList<String>();
	}

	/**
	 * Parametrisierter Konstruktor
	 * @param k aktuelle Klausur
	 */
	public Klausur(Klausur k) {
		this.klausurname = k.getKlausurname();
		this.lvNo = k.getLvNo();
		this.semester = k.getSemester();
		this.teilnehmer = k.getTeilnehmer();
		this.mehrtaegig = k.isMehrtaegig();
		this.gesetzt = k.isGesetzt();
		this.dauer = k.getDauer();
		this.art = k.getArt();
		this.ausstattungen = k.getAusstattungen();
		this.raeume = k.getRaeume();
		this.aufsichtspersonen = k.getAufsichtspersonen();
		this.startZeit = k.getStartZeit();
		this.kategorie = k.getKategorie();
		this.valide = k.isValide();
		this.warnhinweise = k.getWarnhinweise();
	}


	/**
	 * Muss zusaetzlich aufgerufen werden, da beim Einlesen des XML-Files der
	 * Konstruktor nicht aufgerufen wird.
	 */
	public void init() {
		aufsichtspersonen = new ArrayList<Aufsichtsperson>();
		raeume = new ArrayList<Raum>();
		warnhinweise = new ArrayList<String>();
		this.setGesetzt(false);
		this.setStartZeit(null);
	}

	/**
	 * Fuege Warnhinweise zur Liste hinzu
	 * @param w Warnhinweis
	 */
	public void addWarnhinweise(String w) {
		warnhinweise.add(w);
	}

	/**
	 * Gib alle Warnhinweise als ein String formatiert zurueck
	 * @return
	 */
	public String getFormatierteWarnhinweise() {
		return Joiner.on("\n").join(this.warnhinweise);
	}


	/**
	 * Methode um Warnhinweise aus der Liste zu loeschen
	 */
	public void removeWarnhinweise() {
		this.warnhinweise = new ArrayList<String>();
	}

	public void removeRaeume(){
		this.raeume = new ArrayList<Raum>();
	}
	
	public void removeAufsichtspersonen(){
		this.aufsichtspersonen = new ArrayList<Aufsichtsperson>();
	}
	
	public List<String> getWarnhinweise() {
		return warnhinweise;
	}

	public boolean isValide() {
		return valide;
	}

	public void setValide(boolean valide) {
		this.valide = valide;
	}

	public void addAusstattung(String a) {
		ausstattungen.add(a);
	}

	public List<String> getAusstattungen() {
		return ausstattungen;
	}

	public void addAufsichtsperson(Aufsichtsperson a) {
		aufsichtspersonen.add(a);
	}

	public List<Aufsichtsperson> getAufsichtspersonen() {
		return aufsichtspersonen;
	}

	public void setAufsichtspersonen(List<Aufsichtsperson> aufsichtspersonen) {
		this.aufsichtspersonen = aufsichtspersonen;
	}

	public String getLvNo() {
		return lvNo;
	}


	public String getFormatierteRaeume(){
		String s = "Raum: ";
		for(Raum raum: this.raeume){
			s += raum.getBezeichnung() + " ";
		}
		return s;
	}

	public void setLvNo(String lvNo) {
		this.lvNo = lvNo;
	}

	public void addRaum(Raum r) {
		this.raeume.add(r);
	}

	public List<Raum> getRaeume() {
		return this.raeume;
	}

	public void setRaeume(List<Raum> raeume) {
		this.raeume = raeume;
	}

	public String getKlausurname() {
		return klausurname;
	}

	public void setKlausurname(String klausurname) {
		this.klausurname = klausurname;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public boolean isMehrtaegig() {
		return mehrtaegig;
	}

	public void setMehrtaegig(boolean mehrtaegig) {
		this.mehrtaegig = mehrtaegig;
	}

	public String getArt() {
		return art;
	}

	public void setArt(String art) {
		this.art = art;
	}

	public int getDauer() {
		return dauer;
	}

	public void setDauer(int dauer) {
		this.dauer = dauer;
	}

	public int getTeilnehmer() {
		return teilnehmer;
	}

	public void setTeilnehmer(int teilnehmer) {
		this.teilnehmer = teilnehmer;
	}


	public boolean isGesetzt() {
		return gesetzt;
	}

	public void setGesetzt(boolean gesetzt) {
		this.gesetzt = gesetzt;
	}

	public Date getStartZeit() {
		return startZeit;
	}

	public void setStartZeit(Date startZeit) {
		this.startZeit = startZeit;
	}

	public int getKategorie() {
		return kategorie;
	}

	public void setKategorie(int kategorie) {
		this.kategorie = kategorie;
	}
	
	/**
	 * ToString-Methoder der Klausur
	 */
	@Override
	public String toString() {
		return this.klausurname + ", " + this.semester + ", " + dauer + ", "
				+ this.mehrtaegig + ", " + this.art + ", " + this.lvNo;
	}

}
