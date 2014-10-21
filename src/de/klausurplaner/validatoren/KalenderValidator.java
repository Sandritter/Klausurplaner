package de.klausurplaner.validatoren;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.SortedMap;

import de.klausurplaner.controller.builder.TagBuildable;
import de.klausurplaner.controller.objekte.Aufsichtsperson;
import de.klausurplaner.controller.objekte.Einstellung;
import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.controller.objekte.KlausurSetzer;
import de.klausurplaner.controller.objekte.Raum;
import de.klausurplaner.controller.objekte.Tag;
import de.klausurplaner.helper.DimensionHelper;
import de.klausurplaner.helper.Helper;

/**
 * Klasse, in welcher die beim Setzen/Editieren einer Klausur vom Anwender eingegebenen Werte validiert werden.
 * Zudem werden die Klausuren auf die vom User festgelegten Einstellungen wie z.b. Abstand zu anderen Klausuren geprueft.
 * Ausserdem wird beim nachtraeglichen Aendern der Einstellungen jede schon gesetze Klausur noch einmal auf Gueltigkeit gemaess der neuen Einstellungen ueberprueft.
 * 
 * @author Michael Sandritter, Benjamin Christiani, Merle Hiort
 *
 */
public class KalenderValidator extends Observable implements KalenderValidable {
	/**
	 * Der TagBuilder
	 */
	private TagBuildable tagBuilder;
	/**
	 * Die vom Anwender gemachten Einstellungen
	 */
	private Einstellung einstellung;
	/**
	 * Map, in welcher die ggf. auszugebenden Warnhinweise mit ihrem zugehoerigen Index stehen
	 */
	private Map<Integer, String> warnhinweise;
	/**
	 * Array, in welchem jeweils bei der Indexposition des Warnhinweises steht, ob diese Einstellung in diesem Validierungsvorgang ueberprueft werden soll oder nicht
	 */
	private boolean zuIgnorieren[];

	/**
	 * Parametrisierter Konstruktor fuer den KalenderValidator
	 * @param tagbuilder
	 * @param einstellung
	 */
	public KalenderValidator(TagBuildable tagbuilder, Einstellung einstellung) {
		this.tagBuilder = tagbuilder;
		this.einstellung = einstellung;
		// Warnhinweise werden generiert und ihren Indizes zugeordnet
		warnhinweise = new HashMap<Integer, String>();
		warnhinweise.put(0,"Hinweis: An diesem Tag gibt es schon eine Klausur des gleichen Semesters.");
		warnhinweise.put(1,"Hinweis: Die Klausur liegt zu dicht an einer anderen des gleichen Semesters.");
		warnhinweise.put(2, "Hinweis: Die Klausur befindet sich am Wochenende.");
		warnhinweise.put(3,"Hinweis: Die Startzeit der Klausur ist nicht gerundet.");
		warnhinweise.put(4, "Hinweis: Der noetige Kategorieabstand dieser oder einer anderen Klausur ist nicht gewahrt.");
		warnhinweise.put(5,"Hinweis: An diesem Tag wird bereits eine Klausur geschrieben.");
		//fuer die eben gesetzten Warnhinweise wird das zuIgnorierenArray initialisiert
		this.zuIgnorieren = new boolean[warnhinweise.size()];
	}

	/**
	 * Methode, welche prueft, ob eine Navigation nach vorne noch moeglich ist (d.h. noch innerhalb der Klausurphase liegt)
	 * @param date der letzte Tag der aktuellen Woche
	 * @return true wenn Navigation noch moeglich ist, ansonsten false
	 */
	public boolean navigationVorValide(Date date) {
		Date letzterTagDerAktWoche = date;
		long diff = Helper.diffTage(letzterTagDerAktWoche,tagBuilder.letzterTag());
		if (diff > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Methode, welche prueft, ob eine Navigation zurueck noch moeglich ist (d.h. noch innerhalb der Klausurphase liegt)
	 * @param date der letzte Tag der aktuellen Woche
	 * @return true wenn Navigation noch moeglich ist, ansonsten false
	 */
	public boolean navigationZurueckValide(Date date) {
		Date ersterTagDerAktWoche = date;
		long diff = Helper.diffTage(tagBuilder.ersterTag(),
				ersterTagDerAktWoche);
		if (diff > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Methode um zu pruefen, ob ein Raum zu gegebenem Datum und Uhrzeit schon
	 * durch eine Klausur belegt ist
	 * 
	 * @param datum aktuelles Datum
	 * @param uhrzeit zu pruefende Uhrzeit
	 * @param dauer Dauer der Klausur
	 * @param raum zu pruefender Raum
	 * @return true wenn Raum noch frei ist, ansonsten false
	 */
	public boolean raumFrei(Date datum, Date uhrzeit, int dauer, Object raum) {
		List<Klausur> klausuren = this.tagBuilder.getTagByDatum2(datum).getKlausuren();
		long endzeit = uhrzeit.getTime() + DimensionHelper.min2milli(dauer);
		long startzeit = uhrzeit.getTime();
		boolean status = true;
		// schaue ob Raum zur gleichen Zeit schon einer anderen Klausur zugewiesen ist
		for (Klausur k : klausuren) {
			for (Raum r : k.getRaeume()) {
				if (r.equals((Raum)raum)) { // wenn der Raum schon einer Klausurzugeteilt ist
					long kEndzeit = k.getStartZeit().getTime()+ DimensionHelper.min2milli(k.getDauer());
					long kStartzeit = k.getStartZeit().getTime();
					if ((startzeit < kStartzeit && endzeit > kStartzeit)
							|| (startzeit < kEndzeit && endzeit > kEndzeit)) { // wenn sich die Zeit ueberschneidet
						status = false;
					}
				}
			}
		}
		return status;
	}

	/**
	 * Methode um zu pruefen, ob eine Aufsicht zum gegebenen Datum und Uhrzeit
	 * verfuegbar, oder schon fuer eine andere Klausur eingeteilt ist
	 * 
	 * @param datum aktuelles Datum
	 * @param uhrzeit zu pruefende Uhzeit
	 * @param dauer Dauer der Klausur
	 * @param aufsicht zu pruefende Aufsicht
	 * @return true wenn verfuegbar, sonst false
	 */
	public boolean aufsichtVerfuegbar(Date datum, Date uhrzeit, int dauer, Object aufsicht) {
		List<Klausur> klausuren = this.tagBuilder.getTagByDatum2(datum).getKlausuren();
		
		// Umrechnungen der als Parameter mitgegebenen Werte
		long endzeit = uhrzeit.getTime() + DimensionHelper.min2milli(dauer);
		long startzeit = uhrzeit.getTime();
		
		// Hole aktuellen Wochentag als String
		String aktWochentag = null;
		Calendar c = Calendar.getInstance();
		c.setTime(datum);
		for (int i = 0; i < 7; i++) {
			if (Helper.checkWochentagStart(c, i)) {
				aktWochentag = Helper.getWochentagByIndex(i);
			}
		}
		// Hole Verfuegbarkeitszeiten der Aufsicht
		SortedMap<String, Map<Date, Date>> verfuegbarkeit = ((Aufsichtsperson)aufsicht).getVerfuegbarkeit();
		boolean verf = false;
		for (Map.Entry<String, Map<Date, Date>> entry : verfuegbarkeit.entrySet()) {
			Map<Date, Date> value = entry.getValue();
			String key = entry.getKey();
			if (key.compareTo(aktWochentag) == 0) { // Wenn Aufsicht an diesem Wochentag kann
				for (Map.Entry<Date, Date> entry2 : value.entrySet()) {
					long aStart = entry2.getKey().getTime();
					long aEnde = entry2.getValue().getTime();
					if ((startzeit >= aStart && endzeit > aStart && startzeit < aEnde && endzeit <= aEnde)
							|| (startzeit < aEnde && endzeit <= aEnde && startzeit >= aStart && endzeit > aStart)) {
						verf = true;
					}
				}
			}
		}
		if (verf == false)
			return false;
		
		boolean status = true;
		// Ueberpruefung, ob Aufsichtsperson schon zu einer Klausur zur gleichen
		// Zeit eingeteilt ist
		for (Klausur k : klausuren) {
			for (Object a : k.getAufsichtspersonen()) {
				// wenn die Aufsichtsperson schon einer Klausur zugeteiltist
				if (((Aufsichtsperson)aufsicht).equals((Aufsichtsperson) a)) { 
					long kEndzeit = k.getStartZeit().getTime() + DimensionHelper.min2milli(k.getDauer());
					long kStartzeit = k.getStartZeit().getTime();
					// wenn sich die Zeit ueberschneidet
					if ((startzeit < kStartzeit && endzeit > kStartzeit) || (startzeit < kEndzeit && endzeit > kEndzeit)) { 
						status = false;
					}
				}
			}
		}
		return status;
	}

	/**
	 * Methode um beim Setzen einer Klausur zu pruefen ob alle vom Anwender gemachten Einstellungen valide sind
	 * @param ks aktuelles KlausurSetzer-Objekt mit den Daten der zu setzenden Klausur
	 * @return "ok" wenn alle Einstellungen valide, ansonsten return den jeweiligen Warnhinweis
	 */
	public String validateKlausurSetzer(Object klausurSetzer) {
		KlausurSetzer ks= (KlausurSetzer)klausurSetzer;
		int totalKap = 0;
		//Berechne die Gesamtkapazitaet der ausgewaehlten Raeume
		for (Raum r : ks.getRaeume()) {
			totalKap += r.getKapazitaet();
		}
		if (ks.getStartzeit().getTime() < tagBuilder.startUhrzeit().getTime()
				|| ks.getStartzeit().getTime() > tagBuilder.endeUhrzeit().getTime()) {
			return "Startzeit der Klausur liegt ausserhalb des gueltigen Bereichs.";
		} else if (ks.getDauer() < 1) {
			return "Dauer der Klausur muss muss mindestens 1 Minute sein,";
		} else if (ks.getTeilnehmerZahl() < 1) {
			return "Die Teilnehmerzahl muss mindestens 1 betragen.";
		} else if (ks.getRaeume().size() == 0) {
			return "Es muss mindestens ein Raum ausgewaehlt werden.";
		} else if (ks.getAufsPersonen().size() == 0) {
			return "Es muss mindestens eine Aufsichtsperson ausgewaehlt werden.";
		} else if (ks.getTeilnehmerZahl() > totalKap) {
			return "Die Kapazitaet der gewaehlten Raeume ist nicht ausreichend fuer die Teilnehmerzahl.";
		}
		return "ok";
	}

	/**
	 * Methode, mit welcher festgelegt wird, dass eine Einstellung nicht mehr geprueft werden soll
	 * @param i Index des Warnhinweises
	 */
	public void setZuIgnorierendenHinweis(int i) {
		this.zuIgnorieren[i] = true;
	}
	
	/**
	 * Methode mit welcher ein Warnhinweis ueber den jeweiligen Index geholt werden kann
	 * @param i der Index des Warnhinweises
	 * @return den Warnhinweis als String
	 */
	public String getHinweisTexte(int i) {
		return this.warnhinweise.get(i);
	}

	/**
	 * Methode mit welcher das Array der zu ignorierenden Einstellungen zurueckgesetzt wird, 
	 * d.h. alle Einstellungen sollen ueberprueft werden 
	 */
	public void resetIgnorieren() {
		this.zuIgnorieren = new boolean[warnhinweise.size()];
	}

	
	/**
	 * Methode mit welcher beim Setzen einer Klausur die vom Anwender gemachten Eingaben auf die definierten Einstellungen ueberprueft werden
	 * @param ks aktuelles KlausurSetzer-Objekt mit den Daten der zu setzenden Klausur
	 * @return den jeweiligen Index des Warnhinweises, der ausgegeben werden muss
	 */
	public int validateEinstellungen(Object klausurSetzer) {
		KlausurSetzer ks = (KlausurSetzer)klausurSetzer;
		
		List<Klausur> tagKlausuren = ks.getTag().getKlausuren();
		int status = -1;
		// nur eine Klausur eines Semesters pro Tag
		if (!this.zuIgnorieren[0]) {
			status = validiereMehrereKlausurenSemester(ks, tagKlausuren);
			if (status >= 0)
				return status;
		}
		// Mindestabstand wahren zwischen Klausuren des gleichen Semesters
		if (!this.zuIgnorieren[1]) {
			status = validiereMinAbstand(ks, tagKlausuren);
			if (status >= 0)
				return status;
		}
		// Klausuren nicht am Wochenende zulassen
		if (!this.zuIgnorieren[2]) {
			status = validiereWochenende(ks);
			if (status >= 0)
				return status;
		}
		// Startzeit runden
		if (!this.zuIgnorieren[3]) {
			status = validiereStartzeitRunden(ks);
			if (status >= 0)
				return status;
		}
		// Kategorie wahren
		if (!this.zuIgnorieren[4]) {
			status = validiereKategorie(ks);
			if (status >= 0)
				return status;
		}
		// nur eine Klausur pro Tag
		if (!this.zuIgnorieren[5]) {
			status = validiereMehrereKlausuren(ks, tagKlausuren);
			if (status >= 0)
				return status;
		}

		return status;
	}
	
	/**
	 * Methode, mit welcher ueberprueft wird, ob am selben Tag schon eine andere Klausur des gleichen Semesters geschrieben wird
	 * @param ks aktuelles KlausurSetzer-Objekt mit den Daten der zu setzenden Klausur
	 * @param tagKlausuren alle Klausuren des Tages
	 * @return 0 wenn schon eine Klausur geschrieben wird, ansonsten -1
	 */
	private int validiereMehrereKlausurenSemester(KlausurSetzer ks,List<Klausur> tagKlausuren) {
		if (einstellung.isMehrereKlausrenSemester()) {
			for (Klausur k : tagKlausuren) {
				if(k.isMehrtaegig()){
					if(k.getSemester() == ks.getSemester()){
						return 0;
					}
				}
				if (ks.getKlausurName().compareTo(k.getKlausurname()) != 0
						&& !Helper.isGleichesDatum(ks.getStartzeit(),k.getStartZeit())) {
					if (k.getSemester() == ks.getSemester()) {
						return 0;
					}
				}
			}
		}
		return -1;
	}
	
	
	/**
	 * Methode mit welcher ueberprueft wird, ob eine Klausur am Wochenende liegt
	 * @param ks aktuelles KlausurSetzer-Objekt mit den Daten der zu setzenden Klausur
	 * @return 2 wenn Klausur am Wochenende, ansonsten -1
	 */
	private int validiereWochenende(KlausurSetzer ks) {
		if (einstellung.isWochenende()) {
			if (Helper.isWochenende(ks.getTag().getDatum())) {
				return 2;
			}
		}
		return -1;
	}

	
	/**
	 * Methode mit welcher ueberprueft wird, ob die Startzeit einer Klausur gerundet ist
	 * @param ks aktuelles KlausurSetzer-Objekt mit den Daten der zu setzenden Klausur
	 * @return 3 wenn Startzeit nicht gerundet ist, ansonsten -1
	 */
	private int validiereStartzeitRunden(KlausurSetzer ks) {
		if (einstellung.getRunde_startzeit() > 0) {
			if (!DimensionHelper.istGerundetUhrzeit(
					einstellung.getRunde_startzeit(), ks.getStartzeit())) {
				return 3;
			}
		}
		return -1;
	}

	/**
	 * Methode mit welcher validiert wird, ob der Abstand zwischen zwei Klausuren gemaess der definierten Kategorien gewahrt ist.
	 * @param ks aktuelles KlausurSetzer-Objekt mit den Daten der zu setzenden Klausur
	 * @return 4 wenn der Abstand nicht gewahrt ist, ansonsten -1
	 */
	private int validiereKategorie(KlausurSetzer ks) {
		if (einstellung.isKategorieAbstand()) {
			for (Tag t : tagBuilder.tage()) {
				//pruefe Tage danach
				if (Helper.diffTage(ks.getTag().getDatum(), t.getDatum()) >= 0
						&& t.getKlausuren().size() > 0) {
					for (Klausur k : t.getKlausuren()) {
						if (ks.getKlausurName().compareTo(k.getKlausurname()) != 0
								&& !Helper.isGleichesDatum(ks.getStartzeit(),k.getStartZeit())) {
							if (k.getSemester() == ks.getSemester()) {
								int kat = k.getKategorie();
								if (Helper.diffTage(ks.getTag().getDatum(),t.getDatum()) <= kat) {
									return 4;
								}
							}
						}
					}
					//pruefe Tage davor
				} else if (t.getKlausuren().size() > 0) {
					for (Klausur k : t.getKlausuren()) {
						if (ks.getKlausurName().compareTo(k.getKlausurname()) != 0
								&& !Helper.isGleichesDatum(ks.getStartzeit(),k.getStartZeit())) {
							if (k.getSemester() == ks.getSemester()) {
								int kat = ks.getKategorie();
								if (Helper.diffTage(t.getDatum(), ks.getTag().getDatum()) <= kat) {
									return 4;
								}
							}
						}
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * Methode, mit welcher ueberprueft wird, ob eine Klausur den Mindestabstand zu anderen Klausuren des gleichen Semesters am gleichen Tag einhaelt
	 * @param ks aktuelles KlausurSetzer-Objekt mit den Daten der zu setzenden Klausur
	 * @param tagKlausuren Klausuren des aktuellen Tags
	 * @return 1 wenn Mindestabstand nicht eingehalten, ansonsten -1
	 */
	private int validiereMinAbstand(KlausurSetzer ks, List<Klausur> tagKlausuren) {
		if (einstellung.getMindAbstand() != 0) {
			for (Klausur kl : tagKlausuren) {
				if (kl.getSemester() == ks.getSemester()) {
					long klStart = kl.getStartZeit().getTime();
					long klEnde = kl.getStartZeit().getTime() + DimensionHelper.min2milli(kl.getDauer());
					long ksStart = ks.getStartzeit().getTime();
					long ksEnde = ks.getStartzeit().getTime() + DimensionHelper.min2milli(ks.getDauer());
					if (ksStart < klStart) {
						long diff = klStart - ksEnde;
						if (DimensionHelper.milli2min(diff) < einstellung.getMindAbstand()) {
							return 1;
						}
					} else if (klStart < ksStart) {
						long diff = ksStart - klEnde;
						if (DimensionHelper.milli2min(diff) < einstellung.getMindAbstand()) {
							return 1;
						}
					}
				}
			}
		}
		return -1;
	}

	

	/**
	 * Methode, mit welcher ueberprueft wird, ob am selben Tag schon eine andere Klausur geschrieben wird
	 * @param ks aktuelles KlausurSetzer-Objekt mit den Daten der zu setzenden Klausur
	 * @param tagKlausuren alle Klausuren des Tages
	 * @return 0 wenn schon eine Klausur geschrieben wird, ansonsten -1
	 */
	private int validiereMehrereKlausuren(KlausurSetzer ks, List<Klausur> tagKlausuren) {
		if (einstellung.isMehrereKlausren()) {
			if(tagKlausuren.size() == 1){
				Klausur k = tagKlausuren.get(0);
				if(k.getKlausurname().compareTo(ks.getKlausurName())== 0 && 
						Helper.isGleichesDatum(ks.getStartzeit(), k.getStartZeit())){
					return -1;
				}
			}
			if (tagKlausuren.size() > 0)
				return 5;
		}
		return -1;
	}

	/**
	 * Methode, mit welcher ueberprueft wird, ob am selben Tag schon eine andere Klausur geschrieben wird
	 * @param k aktuelle Klausur
	 * @param tagKlausuren alle Klausuren des Tages
	 * @return 0 wenn schon eine Klausur geschrieben wird, ansonsten -1
	 */
	private int validiereMehrereKlausuren2(List<Klausur> tagKlausuren) {
		if (einstellung.isMehrereKlausren()) {
			if (tagKlausuren.size() > 1)
				return 5;
		}
		return -1;
	}

	
	/**
	 * Methode mit welcher beim Aendern der Einstellungen alle schon gesetzten Klausuren erneut auf ihre Gueltigkeit ueberprueft werden
	 * @param k aktuell zu pruefende Klausur
	 * @param t aktueller Tag
	 */
	public void validateEinstellungen2(Object klausur, Object tag) {
		Klausur k = (Klausur)klausur;
		Tag t = (Tag)tag;
		
		List<Klausur> tagKlausuren = t.getKlausuren();
		List<Integer> warnhinweise = new ArrayList<Integer>();
		int status = -1;
		
		//loesche alle ggf. schon gesetzten Warnhinweise der Klausur
		k.removeWarnhinweise();
		// nur eine Klausur eines Semester pro Tag
		status = validiereMehrereKlausurenSemester2(k, tagKlausuren);
		if (status >= 0)
			warnhinweise.add(status);
		// Mindestabstand wahren zwischen Klausuren des gleichen Semesters
		status = validiereMinAbstand2(k, tagKlausuren);
		if (status >= 0)
			warnhinweise.add(status);
		// Klausuren nicht am Wochenende zulassen
		status = validiereWochenende2(k, t);
		if (status >= 0)
			warnhinweise.add(status);
		// Startzeit runden
		status = validiereStartzeitRunden2(k);
		if (status >= 0)
			warnhinweise.add(status);
		// Kategorie wahren
		status = validiereKategorie2(k, t);
		if (status >= 0)
			warnhinweise.add(status);
		// nur eine Klausur pro Tag
		status = validiereMehrereKlausuren2(tagKlausuren);
		if (status >= 0)
			warnhinweise.add(status);
		
		//wenn Warnhinweise aufgetreten sind, setze die Klausur auf nicht valide und fuege ihrer Liste die betreffenden Warnhinweise hinzu
		if (warnhinweise.size() > 0) {
			k.setValide(false);
			
			for (int i : warnhinweise) {
				k.getWarnhinweise().add(this.warnhinweise.get(i));
			}
		}
		//zeichne die Klausuren neu
		setChanged();
		notifyObservers(k);
	}
	
	/**
	 * Methode, mit welcher ueberprueft wird, ob am selben Tag schon eine andere Klausur des gleichen Semesters geschrieben wird
	 * @param k aktuelle Klausur
	 * @param tagKlausuren alle Klausuren des Tages
	 * @return 0 wenn schon eine Klausur geschrieben wird, ansonsten -1
	 */
	private int validiereMehrereKlausurenSemester2(Klausur k,List<Klausur> tagKlausuren) {
		if (einstellung.isMehrereKlausrenSemester()) {
			for (Klausur kl : tagKlausuren) {
				if(kl.isMehrtaegig() && !Helper.isGleichesDatum(kl.getStartZeit(), k.getStartZeit())){
					if(kl.getSemester() == k.getSemester()){
						return 0;
					}
				}
				if (kl.getKlausurname().compareTo(k.getKlausurname()) != 0
						&& !Helper.isGleichesDatum(kl.getStartZeit(),k.getStartZeit())) {
					if (kl.getSemester() == k.getSemester()) {
						return 0;
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * Methode mit welcher ueberprueft wird, ob eine Klausur am Wochenende liegt
	 * @param k aktuelle Klausur
	 * @param t aktueller Tag
	 * @return 2 wenn Klausur am Wochenende, ansonsten -1
	 */
	private int validiereWochenende2(Klausur k, Tag t) {
		if (einstellung.isWochenende()) {
			if (Helper.isWochenende(t.getDatum())) {
				return 2;
			}
		}
		return -1;
	}
	
	/**
	 * Methode mit welcher ueberprueft wird, ob die Startzeit einer Klausur gerundet ist
	 * @param k aktuelle Klausur
	 * @return 3 wenn Startzeit nicht gerundet ist, ansonsten -1
	 */
	private int validiereStartzeitRunden2(Klausur k) {
		if (einstellung.getRunde_startzeit() > 0) {
			if (!DimensionHelper.istGerundetUhrzeit(
					einstellung.getRunde_startzeit(), k.getStartZeit())) {
				return 3;
			}
		}
		return -1;
	}
	
	/**
	 * Methode mit welcher validiert wird, ob der Abstand zwischen zwei Klausuren gemaess der definierten Kategorien gewahrt ist.
	 * @param kl aktuelle Klausur
	 * @param tag aktueller Tag
	 * @return 4 wenn der Abstand nicht gewahrt ist, ansonsten -1
	 */
	private int validiereKategorie2(Klausur kl, Tag tag) {
		if (einstellung.isKategorieAbstand()) {
			for (Tag t : tagBuilder.tage()) {
				//pruefe Tage danach
				if (Helper.diffTage(tag.getDatum(), t.getDatum()) >= 0
						&& t.getKlausuren().size() > 0) {
					for (Klausur k : t.getKlausuren()) {
						if (kl.getKlausurname().compareTo(k.getKlausurname()) != 0
								&& !Helper.isGleichesDatum(kl.getStartZeit(),
										k.getStartZeit())) {
							if (k.getSemester() == kl.getSemester()) {
								int kat = k.getKategorie();
								if (Helper.diffTage(tag.getDatum(),
										t.getDatum()) <= kat) {
									return 4;
								}
							}
						}
					}
					//pruefe Tage davor
				} else if (t.getKlausuren().size() > 0) {
					for (Klausur k : t.getKlausuren()) {
						if (kl.getKlausurname().compareTo(k.getKlausurname()) != 0
								&& !Helper.isGleichesDatum(kl.getStartZeit(),
										k.getStartZeit())) {
							if (k.getSemester() == kl.getSemester()) {
								int kat = kl.getKategorie();
								if (Helper.diffTage(t.getDatum(),
										tag.getDatum()) <= kat) {
									return 4;
								}
							}
						}
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Methode, mit welcher ueberprueft wird, ob eine Klausur den Mindestabstand zu anderen Klausuren des gleichen Semesters am gleichen Tag einhaelt
	 * @param k aktuelle Klausur
	 * @param tagKlausuren alle Klausuren des Tages
	 * @return 1 wenn Mindestabstand nicht eingehalten, ansonsten -1
	 */
	private int validiereMinAbstand2(Klausur k, List<Klausur> tagKlausuren) {
		if (einstellung.getMindAbstand() != 0) {
			for (Klausur kl : tagKlausuren) {
				
				if (kl.getKlausurname().compareTo(k.getKlausurname()) !=0 && !Helper.isGleichesDatum(kl.getStartZeit(), k.getStartZeit()) && kl.getSemester() == k.getSemester()) {
					long klStart = kl.getStartZeit().getTime();
					long klEnde = kl.getStartZeit().getTime() + DimensionHelper.min2milli(kl.getDauer());
					long ksStart = k.getStartZeit().getTime();
					long ksEnde = k.getStartZeit().getTime() + DimensionHelper.min2milli(k.getDauer());
					if (ksStart < klStart) {
						long diff = klStart - ksEnde;
						if (DimensionHelper.milli2min(diff) < einstellung.getMindAbstand()) {
							return 1;
						}
					} else if (klStart < ksStart) {
						long diff = ksStart - klEnde;
						if (DimensionHelper.milli2min(diff) < einstellung.getMindAbstand()) {
							return 1;
						}
					}
				}
			}
		}
		return -1;
	}
}
