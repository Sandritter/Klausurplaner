package de.klausurplaner.controller.builder;

import java.util.ArrayList;
import java.util.List;

import de.klausurplaner.controller.objekte.Ausstattung;
import de.klausurplaner.dateneinausgabe.einlesen.DatenladerInterface;

/**
 * Die Klasse dient als Verwalter der Ausstattungsobjekte.
 * 
 * @author Benjamin Christiani, Michael Sandritter, Merle Hiort
 *
 */
public class AusstattungBuilder implements BuilderInterface{
	
	/**
	 * Schnittstelle zum laden der Stammdaten
	 */
	private DatenladerInterface datenlader;
	
	/**
	 * Liste der Aussattungen
	 */
	private List<Ausstattung> ausstattungen;
	
	/**
	 * Standardkonstruktor
	 */
	public AusstattungBuilder(){
	}
	
	public AusstattungBuilder(DatenladerInterface datenlader){
		this.datenlader = datenlader;
		ladeBuilderDaten();
	}
	
	/**
	 * Ausstattungen werden mithilfe des Datenladers erzeugt
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ladeBuilderDaten(){
		this.ausstattungen = (ArrayList<Ausstattung>)datenlader.ladeStammDaten(new Ausstattung());		
	}
	
	/**
	 * Ausstattungsliste wird zurueckgegeben
	 */
	@Override
	public Object getBuilderDaten(){
		return this.ausstattungen;
	}
	
	/**
	 * Ausstattung wird nach Bezeichnung angefordert.
	 * 
	 * @param bezeichnung
	 * @return Ausstattung mit zutreffender Bezeichnung
	 */
	public Ausstattung getAusstattungByBezeichnung(String bezeichnung){
		for(Ausstattung a: ausstattungen){
			if(a.getBezeichnung().compareTo(bezeichnung)==0){
				return a;
			}
		}
		return null;
	}
	
}
