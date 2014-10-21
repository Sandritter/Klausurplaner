package de.klausurplaner.helper;

import java.util.Calendar;
import java.util.Date;

import de.klausurplaner.controller.objekte.Klausur;

/**
 * Hilfsklasse mit verschiedenen Methoden um mit Datums- und Pixel-Formaten zu rechnen
 * @author Michael Sandritter, Benjamin Christiani, Merle Hiort
 */
public class DimensionHelper {
	
	/**
	 * eine Stunde im Kalender werden anzeigt mit 80 Pixel in der Hoehe
	 */
	private static double pixelProStunde = 80f;
	
	private static double stundeInMilli = 60 * 60 * 1000;
	
	/**
	 * yPos, ab der Klausuren gezeichnet werden koennen
	 */
	private static int zeroPos = 40;
	
	/**
	 * Methode, die die Y-Position der Klausurkomponente im TagComponent zurueckliefert
	 * @param y, yPos der Maus beim Klick auf TagComponent
	 * @return yPos 
	 */
	public static int berechneStartPos(int y){
		int yPix = y - zeroPos; 
		double tmp = (yPix * stundeInMilli / pixelProStunde) / stundeInMilli;
		int ret = (int) tmp;
		return ret;
	}
	
	/**
	 * Methode, die die Y-Position der Klausurkomponente im TagComponent zurueckliefert
	 * @param klausur_start
	 * @param tag_start
	 * @return yPos
	 */
	public static int berechneStartPos(Date klausur_start, Date tag_start){
		Calendar cal = Calendar.getInstance();
		cal.setTime(klausur_start);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(tag_start);
		long diff = (cal.getTimeInMillis() - cal2.getTimeInMillis());
		double d =  ((diff / (60f*60f*1000f)) * pixelProStunde);
		return (int) d + 40;
	}
	
	/**
	 * Berechnet anhand der geklickten y-Position und der Startuhrzeit
	 * des Tages die angezeigte Uhrzeit.
	 * @param y
	 * @param tag_start
	 * @return
	 */
	public static Date berechneTime(int y, Date tag_start){
		Calendar cal = Calendar.getInstance();
		cal.setTime(tag_start);
		int range = y - zeroPos;
		double milli = ((range / pixelProStunde) * stundeInMilli);
		cal.add(Calendar.MILLISECOND, (int)milli);
		return cal.getTime();
	}
	
	/**
	 * Methode berechnet anhand der geklickten y-Position, dem Rundungswert und 
	 * der Startuhrzeit des Tages die angezeigte Uhrzeit. 
	 * @param y
	 * @param runden
	 * @param tag_start
	 * @return
	 */
	public static Date berechneTime(int y, int runden, Date tag_start){
		Date d = berechneTime(y, tag_start);
		return rundeUhrzeit(runden, d);
	}
	
	/**
	 * Methode, die die Anzahl der Pixel einer Klausur anhand deren Dauer berechnet
	 * @param k, Klausur die Informationen beinhaltet ueber dessen Dauer
	 * @return
	 */
	public static int berechneHoehe(Klausur k){
		int minuten = k.getDauer();
		double pixel = minuten / 60f * pixelProStunde;
		return (int) pixel;
	}
	
	/**
	 * Methode um Minuten in Millisekunden umzurechnen
	 * @param min Minuten
	 * @return Millisekunden
	 */
	public static long min2milli(int min){
		return min * 60 * 1000;
	}
	
	/**
	 * Methode um Millisekunden in Minuten umzurechnen
	 * @param milli Millisekunden
	 * @return Minuten
	 */
	public static long milli2min(long milli){
		return milli / 60 / 1000;
	}
	
	/**
	 * Methode, um eine Uhrzeit auf einen bestimmten Rundungswert (z.B. ganze Stunden, halbe Stunden...) zu runden
	 * @param wert der Wert, auf den gerundet werden soll
	 * @param d zu rundende Uhrzeit
	 * @return gerundete Uhrzeit
	 */
	private static Date rundeUhrzeit(int wert, Date d){
		if(wert > 0){
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			int unroundedMinutes = cal.get(Calendar.MINUTE);
			int mod = unroundedMinutes % wert;
			cal.add(Calendar.MINUTE, mod < (wert/2)+1 ? -mod : (wert-mod));
			return cal.getTime();
		}
		return d;
	}
	
	/**
	 * Methode, die prueft ob eine Uhrzeit auf einen Rundungswert gerundet ist
	 * @param wert Wert, auf den die Uhrzeit gerundet sein sollte
	 * @param d die zu pruefende Uhrzeit
	 * @return true wenn Uhrzeit gerundet ist, ansonsten false
	 */
	public static boolean istGerundetUhrzeit(int wert, Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int min = c.get(Calendar.MINUTE);
		int mod = min % wert;
		if(mod != 0){
			return false;
		}
		return true;
	}
	
	
	public static void main(String args[]){
		int round = 15;
		
		Calendar cal = Calendar.getInstance();
		Date current = cal.getTime();
		cal.setTime(current);
		int unroundedMinutes = cal.get(Calendar.MINUTE);
		int mod = unroundedMinutes % round;
		cal.add(Calendar.MINUTE, mod < (round/2)+1 ? -mod : (round-mod));
	}

}
