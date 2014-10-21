package de.klausurplaner.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Helper-Klasse bietet Hilfsfunktionen an.
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public class Helper {
	
	/**
	 * Array fuer die Wochentage
	 */
	private static int [] weekDays = {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY};
	
	/**
	 * Anzeigeformat Datum
	 */
	private static DateFormat date_format = new SimpleDateFormat("dd.MM.yyyy");
	
	/**
	 * Anzeigeformat Uhrzeit
	 */
	private static DateFormat time_format = new SimpleDateFormat("kk:mm");
	
	/**
	 * Methode, um Datum fuer die Anzeige zu formatieren.
	 * @param d - Datum
	 * @return formatiertes Datum
	 */
	public static String formatiertesDatum(Date d){
		return date_format.format(d);
	}
	
	/**
	 * Methode, um Uhrzeit fuer die Anzeige zu formatieren.
	 * @param d - Datum.
	 * @return formatierte Uhrzeit
	 */
	public static String formatierteUhrzeit(Date d){
		return time_format.format(d);
	}
	
	/**
	 * Methode, um Datum aus einem String zu parsen.
	 * @param d - Datum
	 * @return geparstes Datum
	 * @throws ParseException 
	 */
	public static Date parseDatum(String d) throws ParseException{
		return date_format.parse(d);
	}
	
	/**
	 * Methode, um Uhrzeit aus einem String zu parsen.
	 * @param d - Uhrzeit.
	 * @return geparste Uhrzeit
	 * @throws ParseException 
	 */
	public static Date parseUhrzeit(String d) throws ParseException{
		return time_format.parse(d);
	}
	
	/**
	 * Methode um die Differenz zwischen zwei Daten (in Minuten) zu berechnen
	 * @param a Datum 1
	 * @param b Datum 2
	 * @return die Differenz in Minuten
	 */
	public static long diffMinuten(Date a, Date b){
		return (b.getTime()-a.getTime())/(1000L*60L*60L*24L*60L);
	}
	
	/**
	 * Methode um die Differenz zwischen zwei Daten (in Tagen) zu berechnen
	 * @param a Datum 1
	 * @param b Datum 2
	 * @return die Differenz in Tagen
	 */
	public static long diffTage(Date a, Date b){
		return (b.getTime()-a.getTime())/(1000L*60L*60L*24L);
	}
	
	/**
	 * Methode um die Differenz zwischen zwei Daten (in Stunden) zu berechnen
	 * @param a Datum 1
	 * @param b Datum 2
	 * @return die Differenz in Stunden
	 */
	public static long diffStunden(Date a, Date b){
		return (b.getTime()-a.getTime())/(1000L*60L*60L);
	}
	
	/**
	 * 
	 * @param cal aktueller Calendar
	 * @param i Anzahl der Tage um die der Kalendar weiter springen soll
	 * @return Calendar mit neuem akutellen Datum
	 */
	public static Calendar addTage(Calendar cal, int i){
		cal.add(Calendar.DATE, i);
		return cal;
	}
	
	/**
	 * 
	 * @param cal aktueller Calendar
	 * @param i Anzahl der Tage um die der Kalendar zurueck springen soll
	 * @return Calendar mit neuem akutellen Datum
	 */
	public static Calendar removeTage(Calendar cal, int i){
		cal.add(Calendar.DATE, -i);
		return cal;
	}
	
	/**
	 * Methode, die prueft ob zwei Daten gleich sind (Datum UND Uhrzeit)
	 * @param date1 Datum 1
	 * @param date2 Datum 2
	 * @return true wenn sie gleich sind, sonst false
	 */
	public static boolean isGleichesDatum(Date date1, Date date2){
		if (date1.compareTo(date2)== 0) 
			return true;
		return false;
	}
	
	/**
	 * Methode, die prueft ob zwei Daten gleich sind (nur Datum, nicht Uhrzeit)
	 * @param date1 Datum 1
	 * @param date2 Datum 2
	 * @return true wenn sie gleich sind, sonst false
	 */
	public static boolean isInKlausurPhase(Date date1, Date date2){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		if(cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Methode um zu pruefen ob ein Wochentag einem Index entspricht
	 * @param cal das zu pruefende Datum
	 * @param i der Index
	 * @return true wenn sie gleich sind, sonst false
	 */
	public static boolean checkWochentagStart(Calendar cal, int i){
		if (cal.get(Calendar.DAY_OF_WEEK) == weekDays[i]){
			return true;
		}
		return false;
	}
	
	/**
	 * Methode, welche einen Wochentag anhand eines Indexes zwischen 0 und 6 zurueck gibt
	 * @param i zu pruefender Index (0 = Montag, 1=Dienstag, ... 6 = Sonntag)
	 * @return Wochentag-Kuerzel
	 */
	public static String getWochentagByIndex(int i){
		switch(i){
			case 0: return "Mo"; 
			case 1: return "Di"; 
			case 2: return "Mi";
			case 3: return "Do";
			case 4: return "Fr";
			case 5: return "Sa";
			case 6: return "So";
		}
		return null;
	}
	
	/**
	 * Methode um zu pruefen, ob ein Datum an einem Wochenende liegt
	 * @param date das zu pruefende Datum
	 * @return true wenn Wochenende, ansonsten false
	 */
	public static boolean isWochenende(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			return true;
		}
		return false;
	}
}
