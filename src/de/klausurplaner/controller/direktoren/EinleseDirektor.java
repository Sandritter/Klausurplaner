package de.klausurplaner.controller.direktoren;


import java.util.Observable;

import de.klausurplaner.controller.builder.AufsichtspersonBuilder;
import de.klausurplaner.controller.builder.AusstattungBuilder;
import de.klausurplaner.controller.builder.BuilderInterface;
import de.klausurplaner.controller.builder.KlausurBuildable;
import de.klausurplaner.controller.builder.KlausurBuilder;
import de.klausurplaner.controller.builder.RaumBuilder;
import de.klausurplaner.dateneinausgabe.einlesen.DatenladerInterface;
import de.klausurplaner.dateneinausgabe.einlesen.XMLDatenlader;
import de.klausurplaner.dateneinausgabe.einlesen.XMLNotValideException;
import de.klausurplaner.validatoren.Validable;
import de.klausurplaner.validatoren.XMLValidator;

/**
 * Enthaelt die gesamten Builder, den Datenlader und den Einlese-Validator.
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public class EinleseDirektor extends Observable implements DirektorInterface{
	
	/**
	 * Schnittstelle um Stammdaten zu laden.
	 */
	private DatenladerInterface datenlader;
	
	/**
	 * Builder fuer Ausstattungen.
	 */
	private BuilderInterface ausstattungsBuilder;
	
	/**
	 * Builder fuer Aufsichtspersonen.
	 */
	private BuilderInterface aufsichtspersonBuilder;
	
	/**
	 * Builder fuer Klausuren.
	 */
	private BuilderInterface klausurBuilder;
	
	/**
	 * Builder fuer Raeume.
	 */
	private BuilderInterface raumBuilder;
	
	/**
	 * Schnittstelle um eingelesene Daten auf Richtigkeit zu pruefen.
	 */
	private Validable validator;
	
	/**
	 * Parametisierter Konstruktor 
	 */
	public EinleseDirektor(){
	}
	
	/**
	 * Die ausgewaehlte Datei wird auf gueltigkeit ueberprueft und dann wird ggf. mit der init-Methode fortgesetzt.
	 * @param pfad - Pfad der ausgewaehlten Datei.
	 * @throws XMLNotValideException
	 */
	public void start(String pfad) throws XMLNotValideException{
		// XML-Validator wird erstellt
		this.validator = new XMLValidator(pfad);	
		// Falls ausgewaehlte XML-Datei nicht gueltig ist, wird eine Exception geworfen.
		if(!this.validator.valide()){
			throw new XMLNotValideException("Dokument ist nicht gueltig");
		}
		
		// ansonsten werden die benoetigten Objekte instanziiert.
		init(pfad);
	}
	
	/**
	 * Hier werden die Builder und der Datenlader instanziiert.
	 * @param pfad - Pfad zu den Stammdaten.
	 */
	private void init(String pfad){
		this.datenlader = new XMLDatenlader(pfad);
		this.ausstattungsBuilder = new AusstattungBuilder(this.datenlader);
		this.raumBuilder = new RaumBuilder(this.datenlader);
		this.klausurBuilder = new KlausurBuilder(this.datenlader);
		((KlausurBuildable)this.klausurBuilder).initListen();
		
		// Die Liste der Semester wird an alle Beobachter gefeuert.
		setChanged();
		notifyObservers(((KlausurBuilder)this.klausurBuilder).getSemesterListe());
		
		this.aufsichtspersonBuilder = new AufsichtspersonBuilder(this.datenlader);
	}
	

	/**
	 * Getter, der je nach mitgegebenem Objekt den passenden Builder zurueckgibt.
	 */
	@Override
	public BuilderInterface getBuilder(Object o){
		if(o instanceof AusstattungBuilder)
			return this.ausstattungsBuilder;
		else if(o instanceof KlausurBuilder)
			return this.klausurBuilder;
		else if(o instanceof RaumBuilder)
			return this.raumBuilder;
		else if(o instanceof AufsichtspersonBuilder)
			return this.aufsichtspersonBuilder;
		return null;
	}
}
