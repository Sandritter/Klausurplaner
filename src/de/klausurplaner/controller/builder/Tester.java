package de.klausurplaner.controller.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observer;

import de.klausurplaner.controller.direktoren.DirektorInterface;
import de.klausurplaner.controller.direktoren.EinleseDirektor;
import de.klausurplaner.controller.objekte.Aufsichtsperson;
import de.klausurplaner.controller.objekte.Ausstattung;
import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.controller.objekte.Raum;
import de.klausurplaner.controller.objekte.Tag;
import de.klausurplaner.dateneinausgabe.ausgeben.DatenausgabeInterface;
import de.klausurplaner.dateneinausgabe.ausgeben.PDFExportierer;
import de.klausurplaner.dateneinausgabe.einlesen.DatenladerInterface;
import de.klausurplaner.dateneinausgabe.einlesen.XMLDatenlader;
import de.klausurplaner.dateneinausgabe.einlesen.XMLNotValideException;
import de.klausurplaner.validatoren.Validable;
import de.klausurplaner.validatoren.XMLValidator;

public class Tester {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String pfad = "stammdaten.xml";
		
		// TagBuilder wird einmal erstellt
		TagBuilder tb = new TagBuilder();
		//tb.init("08.07.2013", "09.08.2013", "08:00", "20:00");
		// Datenausgeber
		DatenausgabeInterface datenausgabe = new PDFExportierer(tb);
		// Einlesedirektor
		DirektorInterface bi = new EinleseDirektor();
		
		// Der Datenausgeber meldet sich beim EinleseDirektor an
		((EinleseDirektor)bi).addObserver((Observer)datenausgabe);
		
		try {
			((EinleseDirektor)bi).start(pfad);
		} catch (XMLNotValideException e) {
			System.exit(-1);
		}
		
		// hole Builder
		KlausurBuilder kb = (KlausurBuilder)bi.getBuilder(new KlausurBuilder());
		AufsichtspersonBuilder aufs = (AufsichtspersonBuilder)bi.getBuilder(new AufsichtspersonBuilder());
		RaumBuilder raumb = (RaumBuilder)bi.getBuilder(new RaumBuilder());
		
		// Bef�lle den Shit
		kb.getKlausurByName("Mathematik f�r Informatiker").addAufsichtsperson(aufs.getAufsichtsperosnByName("Bernhard", "Geib"));
		kb.getKlausurByName("Mathematik f�r Informatiker").addRaum(((ArrayList<Raum>)raumb.getBuilderDaten()).get(0));
		
		kb.getKlausurByName("Datenbanken").addAufsichtsperson(aufs.getAufsichtsperosnByName("Dirk", "Krechel"));
		kb.getKlausurByName("Datenbanken").addRaum(((ArrayList<Raum>)raumb.getBuilderDaten()).get(2));
		
		kb.getKlausurByName("Programmieren 1").addAufsichtsperson(aufs.getAufsichtsperosnByName("J�rg", "Berdux"));
		kb.getKlausurByName("Programmieren 1").addRaum(((ArrayList<Raum>)raumb.getBuilderDaten()).get(2));
		
		kb.getKlausurByName("Analysis").addAufsichtsperson(aufs.getAufsichtsperosnByName("Roland", "Reichenauer"));
		kb.getKlausurByName("Analysis").addRaum(((ArrayList<Raum>)raumb.getBuilderDaten()).get(2));
		
		kb.getKlausurByName("Web-basierte Anwendungen").addAufsichtsperson(aufs.getAufsichtsperosnByName("Peter", "Barth"));
		kb.getKlausurByName("Web-basierte Anwendungen").addAufsichtsperson(aufs.getAufsichtsperosnByName("Wolfgang", "Weitz"));
		kb.getKlausurByName("Web-basierte Anwendungen").addRaum(((ArrayList<Raum>)raumb.getBuilderDaten()).get(1));
		
		kb.getKlausurByName("Computergrafik").addAufsichtsperson(aufs.getAufsichtsperosnByName("Ulrich", "Schwanecke"));
		kb.getKlausurByName("Computergrafik").addRaum(((ArrayList<Raum>)raumb.getBuilderDaten()).get(2));
	
//		tb.getTagByDatum("18.07.2013").addKlausur(kb.getKlausurByName("Mathematik f�r Informatiker"));
//		tb.getTagByDatum("02.08.2013").addKlausur(kb.getKlausurByName("Datenbanken"));
//		tb.getTagByDatum("22.07.2013").addKlausur(kb.getKlausurByName("Web-basierte Anwendungen"));
//		tb.getTagByDatum("23.07.2013").addKlausur(kb.getKlausurByName("Web-basierte Anwendungen"));
//		tb.getTagByDatum("24.07.2013").addKlausur(kb.getKlausurByName("Web-basierte Anwendungen"));
//		tb.getTagByDatum("25.07.2013").addKlausur(kb.getKlausurByName("Web-basierte Anwendungen"));
//		tb.getTagByDatum("01.08.2013").addKlausur(kb.getKlausurByName("Programmieren 1"));
//		tb.getTagByDatum("24.07.2013").addKlausur(kb.getKlausurByName("Analysis"));
//		tb.getTagByDatum("16.07.2013").addKlausur(kb.getKlausurByName("Computergrafik"));
		
		// Als PDF exportieren
		datenausgabe.exportiereDaten(pfad);
		
	}

}
