package de.klausurplaner.starter;

import java.util.Observer;

import de.klausurplaner.benutzeroberflaeche.Hauptfenster;
import de.klausurplaner.benutzeroberflaeche.StartWizard;
import de.klausurplaner.benutzeroberflaeche.TagComponentBuilder;
import de.klausurplaner.controller.builder.TagBuildable;
import de.klausurplaner.controller.builder.TagBuilder;
import de.klausurplaner.controller.direktoren.DirektorInterface;
import de.klausurplaner.controller.direktoren.EinleseDirektor;
import de.klausurplaner.controller.direktoren.KalenderDirektable;
import de.klausurplaner.controller.direktoren.KalenderDirektor;
import de.klausurplaner.controller.objekte.Einstellung;
import de.klausurplaner.dateneinausgabe.ausgeben.DatenausgabeInterface;
import de.klausurplaner.dateneinausgabe.ausgeben.PDFExportierer;

/**
 * Starter-Klasse, mit welcher die gesamte Klausurplaner-Anwendung gestartet wird.
 * @author Michael Sandritter, Benjamin Christiani, Merle Hiort
 *
 */
public class Starter {
	
	/**
	 * Main-Methode der gesamten Anwendung, in welcher die fuer den Start benoetigten Klassen instanziiert werden
	 * und die ersten Observer zu den Instanzen hinzugefuegt werden.
	 */
	public static void main(String[] args) {
		String programmName = "Klausurplaner";
		
		//EinleseDirektor
		DirektorInterface einleseDirektor = new EinleseDirektor();
		//Einstellungen
		Einstellung einstellung = new Einstellung();
		//TagBuilder
		TagBuildable tagBuilder = new TagBuilder();
		//KalenderDirektor
		KalenderDirektable kalDirektor = new KalenderDirektor(tagBuilder,einleseDirektor, einstellung);
		//TagComponentBuilder
		TagComponentBuilder tagComponentBuilder = new TagComponentBuilder(kalDirektor, tagBuilder);
		((TagBuilder) tagBuilder).addObserver(tagComponentBuilder);
		// Datenausgeber
		DatenausgabeInterface datenausgabe = new PDFExportierer(tagBuilder);
		((EinleseDirektor) einleseDirektor).addObserver((Observer) datenausgabe);
		
		// Hilfsframe fuer das Startfenster und EinstellungsPanelStart
		StartWizard w = new StartWizard();
		 // Falls alle Einstellungen valide, alle Builder im Einlesedirektor
		 // mit den Stammdaten befuellt sind und alle Einstellungen gesetzt wurden.
		if (w.init(tagBuilder, einleseDirektor, einstellung)) {
			kalDirektor.init();
			einstellung.init(tagBuilder);
			einstellung.addObserver(tagComponentBuilder);
			// erzeuge Kalender und Hauptfenster und somit die Hauptanwendung
			@SuppressWarnings("unused")
			Hauptfenster hauptfenster = new Hauptfenster(programmName,tagComponentBuilder, kalDirektor, datenausgabe, einstellung);
		} else {
			//ansonsten beende das Programm
			w.dispose();
		}
	}
}
