package de.klausurplaner.tests;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.helper.DimensionHelper;
import de.klausurplaner.helper.Helper;


public class HelperTester {
	
	@Test
	public void testUhrzeitGerundet(){
		Date d = null;
		try {
			d = Helper.parseUhrzeit("08:45");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertTrue(DimensionHelper.istGerundetUhrzeit(15, d));
	}
	
	@Test
	public void testBerechneKorrekteHoeheKlausur(){
		Klausur k = new Klausur();
		k.setDauer(90);
		assertEquals(120, DimensionHelper.berechneHoehe(k));
	}
	
	@Test 
	public void testTagAmWochenende(){
		Date d = null;
		try {
			d = Helper.parseDatum("06.07.2013");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertTrue(Helper.isWochenende(d));
	}
	
	@Test
	public void testTageAmSelbenTagImJahr(){
		Date d1 = null;
		Date d2 = null;
		
		try {
			d1 = Helper.parseDatum("17.07.2013");
			d2 = Helper.parseDatum("17.07.2013");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		assertTrue(Helper.isInKlausurPhase(d1, d2));
	}
	
	@Test
	public void testTageNichtAmSelbenTagImJahr(){
		Date d1 = null;
		Date d2 = null;
		
		try {
			d1 = Helper.parseDatum("17.07.2013");
			d2 = Helper.parseDatum("18.07.2013");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		assertFalse(Helper.isInKlausurPhase(d1, d2));
	}
	
	@Test
	public void testDifferenzTage(){
		Date d1 = null;
		Date d2 = null;
		
		try {
			d1 = Helper.parseDatum("17.07.2013");
			d2 = Helper.parseDatum("19.07.2013");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		assertEquals(2, Helper.diffTage(d1, d2));
	}
	
	@Test
	public void testDifferenzStunden(){
		Date d1 = null;
		Date d2 = null;
		
		try {
			d1 = Helper.parseUhrzeit("17:00");
			d2 = Helper.parseUhrzeit("19:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		assertEquals(2, Helper.diffStunden(d1, d2));
	}
	
}
