package de.klausurplaner.validatoren;

import java.util.Date;

import de.klausurplaner.controller.objekte.Aufsichtsperson;
import de.klausurplaner.controller.objekte.Raum;

public interface KalenderValidable {
	
	public boolean navigationVorValide(Date date);
	
	public boolean navigationZurueckValide(Date date);
	
	public boolean raumFrei(Date datum, Date uhrzeit, int dauer, Object raum);
	
	public boolean aufsichtVerfuegbar(Date datum, Date uhrzeit, int dauer, Object aufsicht);
	
	public String validateKlausurSetzer(Object ks);
	
	public void setZuIgnorierendenHinweis(int i);
	
	public String getHinweisTexte(int i);
	
	public void resetIgnorieren();
	
	public int validateEinstellungen(Object klausurSetzer);
	
	public void validateEinstellungen2(Object klausur, Object tag);
}
