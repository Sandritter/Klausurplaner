package de.klausurplaner.dateneinausgabe.einlesen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.thoughtworks.xstream.XStream;

import de.klausurplaner.controller.objekte.Aufsichtsperson;
import de.klausurplaner.controller.objekte.Ausstattung;
import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.controller.objekte.Raum;

/**
 * Klasse um die Stammdaten aus einem XML-File einzulesen
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public class XMLDatenlader implements DatenladerInterface{
	
	/**
	 * XStream
	 */
	private XStream xs;
	
	/**
	 * Pfad der XML-Datei
	 */
	private String filePath;
	
	/**
	 * Inhalt der XML-Datei
	 */
	private List<String> xml; // wird befuellt durch die Methode loadXML2String()
	
	/**
	 * Konstruktor fuer den XML-Datenlader
	 */
	public XMLDatenlader(){
		this.xml = new ArrayList<String>();
		xs = new XStream();
		initAlias();
	}
	
	/**
	 * 
	 * @param filePath - Pfad der XML-Datei im Filysystem
	 */
	public XMLDatenlader(String filePath){
		this.filePath = filePath;
		this.xs = new XStream();
		this.xml = new ArrayList<String>();
		loadXML2String();
		initAlias();
	}
	
	/**
	 * Hier werden die Daten aus der XML-Datei geladen
	 * @param typ - Welche Daten geholt werden sollen
	 */
	@Override
	public List<Object> ladeStammDaten(Object typ) {
	
		if(typ instanceof Klausur){
			List<Klausur> liste = ladeDatenByTag("klausurliste", "klausur");
			return (List<Object>)(List<?>) liste;
		}
		if(typ instanceof Ausstattung){
			List<Ausstattung> liste = ladeDatenByTag("ausstattungsliste", "ausstattung");
			return (List<Object>)(List<?>) liste;
		}
		if(typ instanceof Raum){
			List<Raum> liste = ladeDatenByTag("raumliste", "raum");
			return (List<Object>)(List<?>) liste;
		}
		if(typ instanceof Aufsichtsperson){
			List<Aufsichtsperson> liste = ladeDatenByTag("aufsichtspersonliste", "aufsichtsperson");
			return (List<Object>)(List<?>) liste;
		}
		return null;
	}
	
	/**
	 * Erzeugt aus den Stammdaten des XML-Files die Objekte die zwischen den
	 * beiden mitgegebenen Tag-Namen liegen.
	 * 
	 * @param tagNamePl - Name der zu erzeugenden Tags im Plural
	 * @param tagNameSing - Name der zu erzeugenden Tags im Singular
	 * @return Liste der erzeugten Objekte
	 */
	private <T> List<T> ladeDatenByTag(String tagNamePl, String tagNameSing){
		List<T> tList = new ArrayList<T>();
		String startTag = "<t>";
		String endTag = "</t>";
		
		int startBasis = xml.indexOf("<stammdaten>");
		int endBasis = xml.indexOf("</stammdaten>");
		
		List<String> basisdaten = xml.subList(startBasis+1, endBasis);
		
		int start = basisdaten.indexOf(startTag.replaceAll("t", tagNamePl));
		int end = basisdaten.indexOf(endTag.replaceAll("t", tagNamePl));
		
		List<String> liste = basisdaten.subList(start+1, end);
		
		while(liste.indexOf(startTag.replaceAll("t", tagNameSing)) != -1){
			start = liste.indexOf(startTag.replaceAll("t", tagNameSing));
			end = liste.indexOf(endTag.replaceAll("t", tagNameSing));
			List<String> subListe = liste.subList(start, end+1);
			
			String k = Joiner.on("").join(subListe);
			tList.add((T)xs.fromXML(k));
			
			liste = liste.subList(end+1, liste.size());
		}
		
		return tList;
	}
	
	/**
	 * Pfad der XML-Datei wird gesetzt.
	 * 
	 * @param filePath - Pfad im Filesystem
	 */
	public void setPath(String filePath){
		this.filePath = filePath;
		loadXML2String();
	}
	
	/**
	 * Das XML-File wird Zeilenweise eingelesen und als String einer Liste hinzugefuegt.
	 */
	private void loadXML2String(){
		try {
			for(String line: Files.readLines(new File(this.filePath), Charsets.UTF_8)){
				xml.add(line.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Hier wird dafuer gesorgt, dass die XML-Tags den jeweiligen Klassen zugeordnet werden.
	 */
	private void initAlias(){
		xs.alias("klausur", Klausur.class);
		xs.alias("ausstattung", Ausstattung.class);
		xs.alias("geraet", String.class);
		xs.alias("raum", Raum.class);
		xs.alias("aufsichtsperson", Aufsichtsperson.class);
		xs.alias("verfuegbar", String.class);
	}
}
