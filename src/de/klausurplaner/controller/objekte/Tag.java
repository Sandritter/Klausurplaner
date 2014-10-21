package de.klausurplaner.controller.objekte;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * ObjektKlasse Tag 
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public class Tag {
	/**
	 * Liste der Klausuren an dem Tag
	 */
	private List<Klausur> klausuren;
	/**
	 * Datum des Tags
	 */
	private Date datum;
	/**
	 * Abkuerzung des Tags
	 */
	private String kuerzel;
	/**
	 * Formatierte Anzeige aus Datum und Abkuerzung
	 */
	private String infoHeader;
	
	/**
	 * Parameterloser Konstruktor
	 */
	public Tag(){
		this.kuerzel = "";
		this.infoHeader = "";
		this.klausuren = new ArrayList<Klausur>();
		this.datum = new Date();
	}
	
	/**
	 * Parametrisierter Konstruktor
	 * @param datum aktuelles Datum
	 */
	public Tag(Date datum){
		this.datum = datum;
		this.klausuren = new ArrayList<Klausur>();
		SimpleDateFormat wochentag = new SimpleDateFormat("EE");
		this.kuerzel = wochentag.format(datum);
		SimpleDateFormat formatD = new SimpleDateFormat("dd MMM");
		this.setInfoHeader(formatD.format(datum));
	}
	

	/**
	 * Parametrisierter Konstruktor
	 * @param von
	 * @param i
	 */
	public Tag(Date von, int i){
		Calendar cal = Calendar.getInstance();
		cal.setTime(von);
		cal.add(Calendar.DATE, i);
		this.datum = cal.getTime();
		this.klausuren = new ArrayList<Klausur>();
		SimpleDateFormat wochentag = new SimpleDateFormat("EE");
		this.kuerzel = wochentag.format(datum);
		SimpleDateFormat formatD = new SimpleDateFormat("dd MMM");
		this.setInfoHeader(formatD.format(datum));
		this.klausuren = new ArrayList<Klausur>();
	}
	
	public List<Klausur> getKlausuren() {
		return klausuren;
	}

	public void setKlausuren(List<Klausur> klausuren) {
		this.klausuren = klausuren;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public String getKuerzel() {
		return kuerzel;
	}

	public void setKuerzel(String kuerzel) {
		this.kuerzel = kuerzel;
	}
	
	/**
	 * Fuege Klausur zum Liste der Klausuren des Tags hinzu
	 * @param k Klausur
	 */
	public void addKlausur(Klausur k){
		this.klausuren.add(k);
	}
	
	/**
	 * Loesche Klausur aus der Liste der Klausuren des Tags
	 * @param klausurName
	 */
	public void removeKlausur(String klausurName){
		int index = 0;
		for (int i = 0; i < klausuren.size(); i++){
			if (klausuren.get(i).getKlausurname().compareTo(klausurName)==0){
				index = i;
			}
		}
		klausuren.remove(index);
	}
	
	/**
	 * Vergleicht, ob es sich bei einem mitgegebenem Datum um das Datum dieses Tages handelt.
	 * Dabei kann das Datum als String oder bereits als Datum Typ mitgegeben werden.
	 * 
	 * @param datum - Datum das ueberprueft werden soll.
	 * @return true --> es handelt sich um das gleiche Datum / false --> andernfalls
	 */
	public boolean vergleichDatum(Object datum){
		if(datum instanceof String){
			SimpleDateFormat format_day = new SimpleDateFormat("dd.MM.yyyy");
			try {
				Date tmpDate = format_day.parse((String)datum);
				if(tmpDate.compareTo(this.datum)==0)
					return true;
				else
					return false;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else if(datum instanceof Date){
			if(this.datum.compareTo((Date)datum)==0){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Gibt das Datum als Kombination aus WochentagsAbkuerzung und Datum zurueck
	 * @return die formatierte Ausgabe
	 */
	public String getFormatiertesDatum(){
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return this.kuerzel + " " + df.format(this.datum);
	}
	
	/**
	 * ToString-Methode des Tags
	 */
	@Override
	public String toString() {
		return this.kuerzel + ", " + this.datum;
	}

	public String getInfoHeader() {
		return infoHeader;
	}

	public void setInfoHeader(String infoHeader) {
		this.infoHeader = infoHeader;
	}

}
