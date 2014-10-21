package de.klausurplaner.dateneinausgabe.ausgeben;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.google.common.base.Joiner;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.klausurplaner.controller.builder.BuilderInterface;
import de.klausurplaner.controller.builder.TagBuildable;
import de.klausurplaner.controller.builder.TagBuilder;
import de.klausurplaner.controller.objekte.Aufsichtsperson;
import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.controller.objekte.Raum;
import de.klausurplaner.controller.objekte.Tag;
import de.klausurplaner.helper.Helper;

/**
 * 
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public class PDFExportierer implements Observer, DatenausgabeInterface{
	
	/**
	 * Pfad, wohin die PDF im Filesystem gespeichert werden.
	 */
	private String file;
	
	/**
	 * Legenden-Text in der PDF
	 */
	private final String LEGENDE_1 = "K = Klausur, P = Projekt, MP = Muendliche Pruefung, FG = Fachgespraech";
	private final String LEGENDE_2 = "Pruefungstermine fuer Fachgespraeche und Muendliche Pruefungen werden gesondert Ausgehangen.";
	
	/**
	 * Fonts
	 */
	private Font titelFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
	private Font subFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
	private Font tableHeadFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
	private Font legendeFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
	
	private Document doc;
	
	/**
	 * Enthaelt alle Tage und deren Klausuren, die abgespeichert werden sollen.
	 */
	private TagBuildable tagBuilder;
	
	/**
	 * Liste der verfuegbaren Semester
	 */
	private List<Integer> semester;
	
	/**
	 * Parametisierter Konstruktor
	 * @param tagBuilder
	 */
	public PDFExportierer(TagBuildable tagBuilder){
		this.tagBuilder = tagBuilder;
	}
	
	/**
	 * Fuegt einen Titel auf den PDF Seiten hinzu.
	 * @throws DocumentException
	 */
	private void titelEinfuegen() throws DocumentException{
		Paragraph p = new Paragraph();
		String von = Helper.formatiertesDatum(tagBuilder.ersterTag());
		String bis = Helper.formatiertesDatum(tagBuilder.letzterTag());
		p.add(new Paragraph("Klausurphase vom " + von + " bis " + bis, titelFont));
		Date now = new Date();
		p.add(new Paragraph("Stand: " + Helper.formatiertesDatum(now) + "        " + Helper.formatierteUhrzeit(now)));
		leereZeile(p, 1);
		doc.add(p);
	}
	
	/**
	 * Fuer jedes Semester wird eine neue Seite mit einer Tabelle erstellt.
	 * @throws DocumentException
	 */
	private void erstelleSemesterTabellen() throws DocumentException{
		for(int s: this.semester){
			erstelleTabelle(s);
			doc.newPage();
		}
	}
	
	/**
	 * Hier wird fuer ein bestimmtes Semester eine Tabelle generiert.
	 * @param semester
	 * @throws DocumentException
	 */
	private void erstelleTabelle(int semester) throws DocumentException{
		titelEinfuegen();
		
		Paragraph p = new Paragraph(semester + ". Semester ", this.subFont);
		
		float [] colsWidth = {1f, 1.5f, 0.5f, 1f, 0.5f, 0.5f, 1f}; 
		PdfPTable tabelle = new PdfPTable(colsWidth);
		tabelle.setWidthPercentage(100);
		tabelle.setHorizontalAlignment(Element.ALIGN_CENTER);
		erstelleTabellenKoepfe(tabelle);
		
		for(Tag t: this.tagBuilder.tage()){
			fuegeTagTabelleHinzu(tabelle, t, semester);
		}
		
		leereZeile(p, 1);
		doc.add(p);
		doc.add(tabelle);
		erstelleLegende();
	}
	
	/**
	 * Eine Zeile wird der Tabelle hinzugefuegt.
	 * @param tabelle
	 * @param tag
	 * @param semester
	 */
	private void fuegeTagTabelleHinzu(PdfPTable tabelle, Tag tag, int semester){
		
		ArrayList<Klausur> klausuren = (ArrayList<Klausur>) tag.getKlausuren();
		for(Klausur k: klausuren){
			if(k.getSemester() == semester){
				// Datum einfuegen
				tabelle.addCell(tag.getFormatiertesDatum());
				// Klausurname
				tabelle.addCell(k.getKlausurname());
				// LV-Nr.
				tabelle.addCell(k.getLvNo());
				// Aufsichtspersonen
				ArrayList<Aufsichtsperson> ap = (ArrayList<Aufsichtsperson>) k.getAufsichtspersonen();
				ArrayList<String> al = new ArrayList<String>();
				for(Aufsichtsperson a: ap){
					al.add(a.getName());
				}
				tabelle.addCell(Joiner.on(", ").join(al));
				// Art
				tabelle.addCell(k.getArt());
				// Zeit
				tabelle.addCell(Helper.formatierteUhrzeit(k.getStartZeit()));
				// Raum
				ArrayList<Raum> r = (ArrayList<Raum>) k.getRaeume();
				ArrayList<String> rl = new ArrayList<String>();
				for(Raum raum: r){
					rl.add(raum.getBezeichnung());
				}
				tabelle.addCell(Joiner.on(", ").join(rl));
			}
		}
	}
	
	/**
	 * Die Tabellenkopfe werden generiert.
	 * @param tabelle
	 */
	private void erstelleTabellenKoepfe(PdfPTable tabelle){
		
		PdfPCell c1 = new PdfPCell(new Phrase("Datum", this.tableHeadFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tabelle.addCell(c1);
		
		PdfPCell c2 = new PdfPCell(new Phrase("Klausur", this.tableHeadFont));
		c2.setHorizontalAlignment(Element.ALIGN_CENTER);
		c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tabelle.addCell(c2);
		
		PdfPCell c3 = new PdfPCell(new Phrase("LV-Nr.", this.tableHeadFont));
		c3.setHorizontalAlignment(Element.ALIGN_CENTER);
		c3.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tabelle.addCell(c3);
		
		PdfPCell c4 = new PdfPCell(new Phrase("Pruefer", this.tableHeadFont));
		c4.setHorizontalAlignment(Element.ALIGN_CENTER);
		c4.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tabelle.addCell(c4);
		
		PdfPCell c5 = new PdfPCell(new Phrase("Art der Pruefung", this.tableHeadFont));
		c5.setHorizontalAlignment(Element.ALIGN_CENTER);
		c5.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tabelle.addCell(c5);
		
		PdfPCell c6 = new PdfPCell(new Phrase("Zeit", this.tableHeadFont));
		c6.setHorizontalAlignment(Element.ALIGN_CENTER);
		c6.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tabelle.addCell(c6);
		
		PdfPCell c7 = new PdfPCell(new Phrase("Raum", this.tableHeadFont));
		c7.setHorizontalAlignment(Element.ALIGN_CENTER);
		c7.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tabelle.addCell(c7);
		
		tabelle.setHeaderRows(1);
	}
	
	/**
	 * Legenden werden der PDF hinzugefuegt.
	 * @throws DocumentException
	 */
	private void erstelleLegende() throws DocumentException{
		Paragraph p1 = new Paragraph(" ");
		Paragraph p2 = new Paragraph(this.LEGENDE_1, this.legendeFont);
		Paragraph p3 = new Paragraph(this.LEGENDE_2, this.legendeFont);
		doc.add(p1);
		doc.add(p2);
		doc.add(p1);
		doc.add(p3);
	}
	
	/**
	 * Generiert eine Leerzeile in der PDF.
	 * @param para
	 * @param anzahl
	 */
	private void leereZeile(Paragraph para, int anzahl){
		for(int i = 0; i < anzahl; i++){
			para.add(new Paragraph(" "));
		}
	}

	/**
	 * Die PDF mit den ganzen Terminen wird hier erstellt und unter dem mitgegebenem Pfad gespeichert.
	 */
	@Override
	public void exportiereDaten(String pfad) {
		this.file = pfad + "/Klausurplan.pdf";
		try{
			doc = new Document(PageSize.A4.rotate());
			PdfWriter.getInstance(doc, new FileOutputStream(this.file));
			doc.open();
			erstelleSemesterTabellen();
			doc.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Wird darueber Benachrichtigt wenn die Klausurlisten fertig generiert wurden.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable arg0, Object arg1) {
		this.semester = (ArrayList<Integer>)arg1;
	}
}
