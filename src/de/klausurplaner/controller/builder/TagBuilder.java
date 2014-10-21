package de.klausurplaner.controller.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import de.klausurplaner.controller.objekte.Tag; 
import de.klausurplaner.helper.Helper;

/**
 * 
 * @author Merle Hiort, Benjamin Christiani, Michael Sandritter
 * Diese Klasse erstellt und verwaltet die einzelnen Tage, die wiederum Container fuer die gesetzten Klausuren darstellen
 */
public class TagBuilder extends Observable implements TagBuildable{
	
	private List<Tag> tage;
	private Date von;
	private Date bis;
	private Date tag_start;
	private Date tag_ende;
	private Date letzterTagAktWoche;
	private Date ersterTagAktWoche;
	private KlausurBuilder klausurBuilder;
	
	public TagBuilder(){
		this.tage = new ArrayList<Tag>();
	}
	
	/**
	 * Methode initialisiert Attribute dieser Klasse und ruft die Methode ladeBuilderDaten() auf.
	 * @param von, Datum an dem die Klausurphase beginnt
	 * @param bis, Datum an dem die Klausurphase endet
	 * @param tag_start, Uhrzeit an dem ein Klausurtag anfaengt
	 * @param tag_ende, Uhrzeit an dem ein Klausurtag endet
	 */
	public void init(Date von, Date bis, Date tag_start, Date tag_ende){
		
		this.von = von;
		this.bis = bis;
		this.tag_start = tag_start;
		this.tag_ende = tag_ende;
		
		this.tage = erstelleTage(this.von, this.bis);
		
		setChanged();
		notifyObservers();
	}

	/**
	 * Erstellt eine Liste mit Tag-Objekten, welche zwischen den beiden Daten liegen.
	 * @param von Datum an dem Klausurphase beginnt
	 * @param bis Datum an dem Klausurphase endet
	 * @return gibt eine ArrayList bestehend aus Tagen zurueck
	 */
	private List<Tag> erstelleTage(Date von, Date bis) {
		// wie viele Tage hat eine Klausurphase
		long diff = (bis.getTime() - von.getTime()) / (1000L * 60L * 60L * 24L) + 2;
		List<Tag> ret = new ArrayList<Tag>();
		for (int i = 0; i < diff; i++){
			Tag tag = new Tag(von, i);
			ret.add(tag);
		}
		return ret;
	}
	
	/**
	 * Liefert anhand eines Datum den passenden Tag zurueck.
	 * Das Datum kann als String oder als Datentyp Date mitgegeben werden.
	 * @param datum
	 * @return den passenden Tag.
	 */
	public Tag getTagByDatum(Date datum){
		for(Tag t: this.tage){
			if(Helper.isGleichesDatum(datum, t.getDatum())){	
				return t;
			}
		}
		return null;
	}
	
	public Tag getTagByDatum2(Date datum){
		for (Tag t : this.tage){
			if(Helper.isInKlausurPhase(datum, t.getDatum())){
				return t;
			}
		}
		return null;
	}

	public void addTag(Tag tag) {
		this.tage.add(tag);
	}

	public Date getVon() {
		return von;
	}


	@Override
	public Date ersterTag() {
		return this.von;
	}

	@Override
	public Date letzterTag() {
		return this.bis;
	}

	@Override
	public Date startUhrzeit() {
		return this.tag_start;
	}

	@Override
	public Date endeUhrzeit() {
		return this.tag_ende;
	}

	@Override
	public Date letzterTagAktWoche() {
		return this.letzterTagAktWoche;
	}

	@Override
	public Date ersterTagAktWoche() {
		return this.ersterTagAktWoche;
	}

	@Override
	public void aenderLetztenTagAktWoche(Date d) {
		this.letzterTagAktWoche = d;
	}

	@Override
	public void aenderErstenTagAktWoche(Date d) {
		this.ersterTagAktWoche = d;
	}

	@Override
	public List<Tag> tage() {
		return this.tage;
	}
	
	public void setKlausurBuilder(KlausurBuilder klausurBuilder){
		this.klausurBuilder = klausurBuilder;
	}
	
	public KlausurBuilder getKlausurBuilder(){
		return this.klausurBuilder;
	}
	
	@Override
	public void aenderErsterTagKlausurphase(Date d) {
		this.von = d;
		
	}

	@Override
	public void aenderLetzterTagKlausurphase(Date d) {
		this.bis = d;
	}
}
