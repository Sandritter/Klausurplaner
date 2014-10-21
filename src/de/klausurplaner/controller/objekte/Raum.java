package de.klausurplaner.controller.objekte;

import java.util.ArrayList;
import java.util.List;

/**
 * Objekt-Klasse Raum
 * @author Benjamin Christiani, Michael Sandritter, Merle Hiort
 *
 */
public class Raum {
	
	/**
	 * Name des Raums
	 */
	private String bezeichnung;
	
	/**
	 * Name des Campus'
	 */
	private String campus;
	
	/**
	 * moegliche Maximalanzahl von Studenten im Raum
	 */
	private int kapazitaet;
	
	/**
	 * Name des Gebaeudes
	 */
	private String gebaeude;
	
	/**
	 * Liste der vorhandenen Ausstattungen als String
	 */
	private List ausstattungen;
	
	/**
	 * Default-Konstruktor fuer das Objekt Raum
	 */
	public Raum(){
		ausstattungen = new ArrayList();
	}
	
	/**
	 * Parametrisierter Konstruktor fuer das Objekt Raum
	 * @param bezeichnung Name des Raums
	 * @param campus Name des Campus'
	 * @param kapazitaet moegliche Maximalanzahl von Studenten im Raum
	 * @param gebaeude Name des Gebaeudes
	 * @param ausstattung Liste der vorhandenen Ausstattungen
	 */
	public Raum(String bezeichnung, String campus, int kapazitaet, String gebaeude, ArrayList<Ausstattung> ausstattung){
		this.bezeichnung = bezeichnung;
		this.campus = campus;
		this.kapazitaet = kapazitaet;
		this.gebaeude = gebaeude;
	}
	
	/**
	 * Getter fuer das Attribut Bezeichnung
	 * @return bezeichnung
	 */
	public String getBezeichnung(){
		return this.bezeichnung;
	}
	
	/**
	 * Getter fuer das Attribut Campus
	 * @return campus
	 */
	public String getCampus(){
		return this.campus;
	}
	
	/**
	 * Getter fuer das Attribut Kapazitaet
	 * @return kapazitaet
	 */
	public int getKapazitaet(){
		return this.kapazitaet;
	}
	
	/**
	 * Getter fuer das Attribut Gebaeude
	 * @return gebaeude
	 */
	public String getGebaeude(){
		return this.gebaeude;
	}
	
	/**
	 * Getter fuer das Attribut Ausstattung
	 * @return ausstattung
	 */
	public List getAusstattungen(){
		return this.ausstattungen;
	}
	
	/**
	 * Setter fuer das Attribut Bezeichnung
	 * @param bezeichnung
	 */
	public void setBezeichnung(String bezeichnung){
		this.bezeichnung = bezeichnung;
	}
	
	/**
	 * Setter fuer das Attribut Campus
	 * @param campus
	 */
	public void setCampus(String campus){
		this.campus = campus;
	}
	
	/**
	 *  Setter fuer das Attribut Kapazitaet
	 * @param kapazitaet
	 */
	public void setKapazitaet(int kapazitaet){
		this.kapazitaet = kapazitaet;
	}
	
	/**
	 *  Setter fuer das Attribut Gebaeude
	 * @param gebaeude
	 */
	public void setGebaeude(String gebaeude){
		this.gebaeude = gebaeude;
	}
	
	public void addAusstattung(String a){
		this.ausstattungen.add(a);
	}
	
	/**
	 * Equals-Methode fuer den Raum
	 */
	@Override
	public boolean equals(Object obj) {
		Raum r = (Raum)obj;
		
		if(this.bezeichnung.compareTo(r.bezeichnung)==0 && this.gebaeude.compareTo(r.gebaeude)==0
				&& this.campus.compareTo(this.campus)==0){
			return true;
		}
		return false;
	}
	
	/**
	 * ToString-Methode fuer den Raum
	 */
	@Override
	public String toString() {
		return "Raum: " + bezeichnung + ", Gebaeude: " + gebaeude + ", Kapazitaet: " + kapazitaet;
	}
	
	
}
