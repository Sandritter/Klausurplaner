package de.klausurplaner.controller.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import de.klausurplaner.controller.objekte.Klausur;

/**
 * Interface fuer einen KlausurBuilder
 * dieser kann Klausuren erstellen
 * und Klausuren verwalten
 * 
 * @author michaelsandritter
 *
 */
public interface KlausurBuildable {
	
	/**
	 * Methode liefert eine Klausur anhand ihres Klausurnames zurueck
	 * @param klausurname
	 * @return Klausur
	 */
	public Klausur getKlausurByName(String klausurname);
	
	/**
	 * liefert eine ArrayList aus Klausuren zurueck
	 * @param semester
	 * @return ArrayList
	 */
	public ArrayList<Klausur> getKlausurBySemeser(int semester);
	
	/**
	 * initialisiert die Klausurliste
	 */
	public void initListen();
	
	/**
	 * liefert eine Liste der Semester als Integerwerte zurueck
	 * @return List
	 */
	public List<Integer> getSemesterListe();
	
	/**
	 * liefert eine Map mit Semestern als Key und einer List aus Klausuren als value zurueck
	 * @return Map
	 */
	public Map<Integer, List<Klausur>> getSemesterMap();
	
	/**
	 * 
	 * @param semesterMap
	 */
	public void setSemesterMap(SortedMap<Integer, List<Klausur>> semesterMap);
}
