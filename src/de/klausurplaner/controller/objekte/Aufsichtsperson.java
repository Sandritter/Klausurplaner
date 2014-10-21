package de.klausurplaner.controller.objekte;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Objekt-Klasse fuer eine Aufsichtsperson
 * @author Michael Sandritter, Merle Hiort, Benjamin Christiani
 *
 */
@SuppressWarnings("serial")
public class Aufsichtsperson implements Serializable{
	
	/**
	 * Nachname
	 */
	private String name;
	/**
	 * Vorname
	 */
	private String vorname;
	/**
	 * Kuerzel
	 */
	private String kuerzel;
	/**
	 * Telefonnummer
	 */
	private String telefon;
	/**
	 * Handynummer
	 */
	private String handy;
	/**
	 * Emailadresse
	 */
	private String email;
	/**
	 * Liste mit Zeiten, zu welchen die Person fuer Klausuraufsichten verfuegbar ist
	 */
	private List verfuegbarkeiten;
	/**
	 * Map aus den verfuegbaren Zeiten im Format SortedMap<Wochentag, Map<Startuhrzeit, Enduhrzeit>>
	 */
	private SortedMap<String, Map<Date, Date>> verfuegbarkeit;
	
	/**
	 * Parameterloser Konstruktor
	 */
	public Aufsichtsperson(){
		verfuegbarkeiten = new ArrayList<String>();
		verfuegbarkeit = new TreeMap<String, Map<Date, Date>>();
	}
	
	/**
	 * Parametrisierter Konstruktor
	 * @param name der Aufsichtsperson
	 * @param vorname der Aufsichtsperson
	 * @param telefon - nummer der Aufsichtsperson
	 * @param handy - nummer der Aufsichtsperson
	 * @param email - adresse der aufsichtsperson
	 * @param verfuegbarkeiten Wochentage und Zeiten an der die Aufsichtsperson zur Verfuegung steht
	 */
	public Aufsichtsperson(String name, String vorname, String telefon, String handy, String email, List<String> verfuegbarkeiten){
		this.name = name;
		this.vorname = vorname;
		this.telefon = telefon;
		this.handy = handy;
		this.email = email;
	}
	
	/**
	 * Methode um die Verfuegbarkeiten aus der Liste in die Map zu formatieren
	 */
	public void formatVerfuegbarkeit() {
		verfuegbarkeit = new TreeMap<String, Map<Date, Date>>();
		
		for(String v: (ArrayList<String>)verfuegbarkeiten){
			
			String [] tag = v.split(";"); // tag[0] = Wochentag, tag[1] = von, tag[2] = bis
			SimpleDateFormat format = new SimpleDateFormat("kk:mm");
			SortedMap<Date, Date> uhrzeitMap = new TreeMap<Date, Date>();
			Date von = null;
			Date bis = null;
			try {
				von = format.parse(tag[1]);
				bis = format.parse(tag[2]);
			} catch (ParseException e) {
				// oeffne Dialogbox - Fehlermeldung: in XML - File wurde falsches Zeitformat angegeben
				System.out.println("______ FEHLER IM XML - FILE - : [FALSCHES ZEITFORMAT]");
				e.printStackTrace();
			}
			
			// wenn Tag schon existiert
			if (this.verfuegbarkeit.containsKey(tag[0])){
				// hole dir diesen Tag und fuege diesem neue Zeiten hinzu
				this.verfuegbarkeit.get(tag[0]).put(von, bis);
			} else {
				// ansonsten fuege neuen Tag mit dementsprechenden Zeiten hinzu
				uhrzeitMap.put(von, bis);
				this.verfuegbarkeit.put(tag[0], uhrzeitMap);
			}
		}
		
	}

	public List getVerfuegbarkeiten() {
		return verfuegbarkeiten;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	
	public String getKuerzel() {
		return kuerzel;
	}

	public void setKuerzel(String kuerzel) {
		this.kuerzel = kuerzel;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getHandy() {
		return handy;
	}

	public void setHandy(String handy) {
		this.handy = handy;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public SortedMap<String, Map<Date, Date>> getVerfuegbarkeit() {
		return verfuegbarkeit;
	}

	public void setVerfuegbarkeit(
			SortedMap<String, Map<Date, Date>> verfuegbarkeit) {
		this.verfuegbarkeit = verfuegbarkeit;
	}
	
	@Override
	/**
	 * ToString Methode fuer die Aufsichtsperson
	 */
	public String toString() {
		return vorname + " " + name;
	}
	
	/**
	 * Equals-Methode fuer die Aufsichtsperson
	 */
	public boolean equals(Object o){
		Aufsichtsperson a = (Aufsichtsperson)o;
		if(a.vorname.compareTo(this.vorname) != 0){
			return false;
		}
		if(a.name.compareTo(this.name) != 0){
			return false;
		}
		if(a.kuerzel.compareTo(this.kuerzel) != 0){
			return false;
		}
		return true;
	}

}
