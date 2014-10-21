package de.klausurplaner.controller.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.dateneinausgabe.einlesen.DatenladerInterface;

/**
 * Die Klasse dient als Verwalter der Klausurobjekte.
 * 
 * @author Benjamin Christiani, Michael Sandritter, Merle Hiort
 *
 */
public class KlausurBuilder implements BuilderInterface, KlausurBuildable{
	
	/**
	 * Schnittstelle zum laden der Stammdaten
	 */
	private DatenladerInterface datenlader;
	
	/**
	 * Liste der Klausuren
	 */
	private List<Klausur> klausuren;
	
	/**
	 * Map aus Klausurlisten sotiert nach Semester
	 */
	private SortedMap<Integer, List<Klausur>> semesterMap;
	

	private List<Integer> semesterListe;
	/**
	 * Standardkonstruktor
	 */
	public KlausurBuilder(){
	}
	
	public KlausurBuilder(DatenladerInterface datenlader){
		this.datenlader = datenlader;
		ladeBuilderDaten();
	}
	
	/**
	 * Klausuren werden mithilfe des Datenladers erzeugt
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ladeBuilderDaten() {
		this.klausuren = (ArrayList<Klausur>)datenlader.ladeStammDaten(new Klausur());
		
		// erstelle eine Map aus KlausurListen mit dem Semester als Key
		this.semesterMap = new TreeMap<Integer, List<Klausur>>();
		for (Klausur k : this.klausuren){
			int semester = k.getSemester();
			if (!semesterMap.containsKey(semester)){
				List<Klausur> list = new ArrayList<Klausur>();
				list.add(k);
				semesterMap.put(semester, list);
			} else {
				semesterMap.get(semester).add(k);
			}
		}
	}
	
	/**
	 * Klausurliste wird zurueckgegeben
	 */
	@Override
	public Object getBuilderDaten() {
		return this.klausuren;
	}
	
	/**
	 * Klausur wird nach Namen angefordert.
	 * 
	 * @param klausurname
	 * @return Klausur mit zutreffendem Namen
	 */
	public Klausur getKlausurByName(String klausurname){
		for(Klausur k: klausuren){
			if(k.getKlausurname().compareTo(klausurname)==0){
				return k;
			}
		}
		return null;
	}
	
	/**
	 * Alle Klausuren, welche im gesuchten Semester stattfinden, werden zurueckgeliefert
	 * @param semester - Semester in dem die Klausur geschrieben wird
	 * @return Liste von Klausuren
	 */
	public ArrayList<Klausur> getKlausurBySemeser(int semester){
		ArrayList<Klausur> list = new ArrayList<Klausur>();
		
		for(Klausur k: klausuren){
			if(k.getSemester() == semester){
				list.add(k);
			}
		}
		return list;
	}
	
	/**
	 * Beim Einlesen und Erzeugen der Objekte aus dem XML-File wird der Konstruktor der Klausur
	 * nicht aufgerufen und deswegen muessen die vorhanden List-Attribute noch instanziiert werden.
	 */
	public void initListen(){
		for(Klausur k: this.klausuren){
			k.init();
		}
		this.semesterListe = new ArrayList<Integer>();
		for(Klausur k: this.klausuren){
			int semester = k.getSemester();
			if(!semesterListe.contains(semester)){
				semesterListe.add(semester);
			}
		}
	}
	
	public List<Integer> getSemesterListe(){
		return this.semesterListe;
	}
	
	// ist nur zu testzwecken noch static
	
	public Map<Integer, List<Klausur>> getSemesterMap() {
		return semesterMap;
	}

	public void setSemesterMap(SortedMap<Integer, List<Klausur>> semesterMap) {
		this.semesterMap = semesterMap;
	}
}