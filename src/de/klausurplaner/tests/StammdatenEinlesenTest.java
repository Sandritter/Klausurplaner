package de.klausurplaner.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import de.klausurplaner.controller.builder.AufsichtspersonBuilder;
import de.klausurplaner.controller.builder.AusstattungBuilder;
import de.klausurplaner.controller.builder.BuilderInterface;
import de.klausurplaner.controller.builder.KlausurBuilder;
import de.klausurplaner.controller.builder.RaumBuilder;
import de.klausurplaner.controller.direktoren.DirektorInterface;
import de.klausurplaner.controller.direktoren.EinleseDirektor;
import de.klausurplaner.controller.objekte.Aufsichtsperson;
import de.klausurplaner.controller.objekte.Ausstattung;
import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.controller.objekte.Raum;
import de.klausurplaner.dateneinausgabe.einlesen.XMLNotValideException;
import de.klausurplaner.validatoren.Validable;
import de.klausurplaner.validatoren.XMLValidator;

public class StammdatenEinlesenTest {
	
	@Test
	public void testInvalideStammdaten(){
		String stammdatenPfad = "testdaten/invalideStammdaten.xml";
		Validable validator = new XMLValidator(stammdatenPfad);
		assertFalse(validator.valide());
	}
	
	@Test
	public void testValideStammdaten(){
		String stammdatenPfad = "testdaten/valideStammdaten.xml";
		Validable validator = new XMLValidator(stammdatenPfad);
		assertTrue(validator.valide());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testXMLParserAnzahlKlausuren(){
		String stammdatenPfad = "testdaten/valideStammdaten.xml";
		DirektorInterface ed = new EinleseDirektor();
		
		try {
			((EinleseDirektor)ed).start(stammdatenPfad);
		} catch (XMLNotValideException e) {
			System.exit(-1);
		}
		BuilderInterface kb = (KlausurBuilder)ed.getBuilder(new KlausurBuilder());
		
		assertTrue(((ArrayList<Klausur>)kb.getBuilderDaten()).size() == 21);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testXMLParserAnzahlAusstattungen(){
		String stammdatenPfad = "testdaten/valideStammdaten.xml";
		DirektorInterface ed = new EinleseDirektor();
		
		try {
			((EinleseDirektor)ed).start(stammdatenPfad);
		} catch (XMLNotValideException e) {
			System.exit(-1);
		}
		BuilderInterface ab = (AusstattungBuilder)ed.getBuilder(new AusstattungBuilder());
		
		assertTrue(((ArrayList<Ausstattung>)ab.getBuilderDaten()).size() == 2);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testXMLParserAnzahlAufsichtpersonen(){
		String stammdatenPfad = "testdaten/valideStammdaten.xml";
		DirektorInterface ed = new EinleseDirektor();
		
		try {
			((EinleseDirektor)ed).start(stammdatenPfad);
		} catch (XMLNotValideException e) {
			System.exit(-1);
		}
		BuilderInterface ab = (AufsichtspersonBuilder)ed.getBuilder(new AufsichtspersonBuilder());
		
		assertTrue(((ArrayList<Aufsichtsperson>)ab.getBuilderDaten()).size() == 13);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testXMLParserAnzahlRaeume(){
		String stammdatenPfad = "testdaten/valideStammdaten.xml";
		DirektorInterface ed = new EinleseDirektor();
		
		try {
			((EinleseDirektor)ed).start(stammdatenPfad);
		} catch (XMLNotValideException e) {
			System.exit(-1);
		}
		BuilderInterface rb = (RaumBuilder)ed.getBuilder(new RaumBuilder());
		
		assertTrue(((ArrayList<Raum>)rb.getBuilderDaten()).size() == 4);
	}
	
}
