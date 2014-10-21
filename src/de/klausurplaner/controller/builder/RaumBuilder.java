package de.klausurplaner.controller.builder;

import java.util.ArrayList;
import java.util.List;

import de.klausurplaner.controller.objekte.Raum;
import de.klausurplaner.dateneinausgabe.einlesen.DatenladerInterface;

/**
 * Die Klasse dient als Verwalter der Raumobjekte.
 * 
 * @author Benjamin Christiani, Michael Sandritter, Merle Hiort
 *
 */
public class RaumBuilder implements BuilderInterface{
	
	/**
	 * Schnittstelle zum laden der Stammdaten
	 */
	private DatenladerInterface datenlader;
	
	/**
	 * Liste der Raeume
	 */
	private List<Raum> raeume;
	
	/**
	 * Standardkonstruktor
	 */
	public RaumBuilder(){
	}
	
	public RaumBuilder(DatenladerInterface datenlader){
		this.datenlader = datenlader;
		ladeBuilderDaten();
	}
	
	/**
	 * Raeume werden mithilfe des Datenladers erzeugt
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ladeBuilderDaten() {
		this.raeume = (ArrayList<Raum>)datenlader.ladeStammDaten(new Raum());
	}
	
	/**
	 * Raumliste wird zurueckgegeben
	 */
	@Override
	public Object getBuilderDaten() {
		return this.raeume;
	}
	
	/**
	 * Alle Raume, welche die gesuchte Ausstattung beinhalten, werden zurueckgegeben
	 * @param ausstattung - Bezeichnung der Ausstattung
	 * @return Liste der Raeume
	 */
	@SuppressWarnings("unchecked")
	public List<Raum> getRaumByAusstattung(String ausstattung){
		ArrayList<Raum> rl = new ArrayList<Raum>();
		for(Raum raum: raeume){
			ArrayList<String> al = (ArrayList<String>)raum.getAusstattungen();
			for(String a: al){
				if(a.compareTo(ausstattung)==0){
					rl.add(raum);
				}
			}
		}
		return rl;
	}
	
	/**
	 * Alle Raeume eines bestimmten Gebaeudes werden angefordert.
	 * @param geb - Gebaudebezeichnung
	 * @return Liste aller gefundenen Raeume
	 */
	public List<Raum> getRaumByGebaude(String geb){
		ArrayList<Raum> rl = new ArrayList<Raum>();
		for(Raum raum: raeume){
			if(raum.getGebaeude().compareTo(geb)==0){
				rl.add(raum);
			}
		}
		return rl;
	}
	
	/**
	 * Alle Raeume eines bestimmten Campus werden angefordert.
	 * @param geb - Gebaudebezeichnung
	 * @return Liste aller gefundenen Raeume
	 */
	public List<Raum> getRaumByCampus(String camp){
		ArrayList<Raum> rl = new ArrayList<Raum>();
		for(Raum raum: raeume){
			if(raum.getGebaeude().compareTo(camp)==0){
				rl.add(raum);
			}
		}
		return rl;
	}

}
