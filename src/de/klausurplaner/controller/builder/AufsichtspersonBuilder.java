package de.klausurplaner.controller.builder;

import java.util.ArrayList;
import java.util.List;

import de.klausurplaner.controller.objekte.Aufsichtsperson;
import de.klausurplaner.dateneinausgabe.einlesen.DatenladerInterface;

/**
 * Erstellt die Aufsichtspersonen
 * und verwaltet die Aufsichtspersonen
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public class AufsichtspersonBuilder implements BuilderInterface{
	
	/**
	 * Schnittstelle zum laden der Stammdaten
	 */
	private DatenladerInterface datenlader;
	
	/**
	 * Liste der Aufsichtspersonen
	 */
	private List<Aufsichtsperson> aufsichtspersonen;
	
	/**
	 * Standardkonstruktor
	 */
	public AufsichtspersonBuilder(){
	}
	
	public AufsichtspersonBuilder(DatenladerInterface datenlader){
		this.datenlader = datenlader;
		ladeBuilderDaten();
	}
	
	/**
	 * Ausstattungen werden mithilfe des Datenladers erzeugt
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ladeBuilderDaten() {
		this.aufsichtspersonen = (ArrayList<Aufsichtsperson>)datenlader.ladeStammDaten(new Aufsichtsperson());
		for(Aufsichtsperson a: this.aufsichtspersonen){
			a.formatVerfuegbarkeit();
		}
	}

	@Override
	public Object getBuilderDaten() {
		return this.aufsichtspersonen;
	}
	
	/**
	 * Aufsichtsperosn wird nach seinem Namen angefordert.
	 * 
	 * @param vorname
	 * @param nachname
	 * @return Aufsichtsperson mit zutreffender Bezeichnung
	 */
	public Aufsichtsperson getAufsichtsperosnByName(String vorname, String nachname){
		for(Aufsichtsperson a: this.aufsichtspersonen){
			if(a.getVorname().compareTo(vorname)==0 && a.getName().compareTo(nachname)==0){
				return a;
			}
		}
		return null;
	}

}
