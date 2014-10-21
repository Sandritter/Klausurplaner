package de.klausurplaner.benutzeroberflaeche;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import de.klausurplaner.dateneinausgabe.ausgeben.DatenausgabeInterface;

/**
 * Klasse zum Erstellen einer Menueleiste
 * @author Michael Sandritter, Benjamin Christiani, Merle Hiort
 *
 */
public class MenuPanel extends JMenuBar{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3741022861693699495L;
	/**
	 * MenueItem Datei mit Untermenues
	 */
	private JMenu datei;
	private JMenuItem speichern;
	private JMenuItem projektLaden; 
	
	/**
	 * MenueItem Bearbeiten mit Untermenues
	 */
	private JMenu bearbeiten;
	private JMenuItem kopieren;
	private JMenuItem ausschneiden;
	private JMenuItem einfuegen;
	
	/**
	 * MenuItem Exportieren mit Untermenues
	 */
	private JMenu exportieren;
	private JMenuItem gesamt;
	
	private JMenu objekte;
	private JMenuItem hinzufuegen;
	private JMenuItem loeschen;
	private JMenuItem aendern;
	
	private static DatenausgabeInterface datenausgabe;
	private JFileChooser fc; 

	/**
	 * Konstruktor fuer das die Menueleiste
	 */
	@SuppressWarnings("static-access")
	public MenuPanel(final DatenausgabeInterface datenausgabe){
		this.datenausgabe = datenausgabe;

		addComponents();
	}
	
	/**
	 * Methode um die MenueItems und Untermenues zur Menueleiste hinzuzufuegen
	 */
	public void addComponents(){
		this.fc = new JFileChooser();
		fc.setDialogTitle("Exportieren...");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		datei = new JMenu("Datei");
		speichern = new JMenuItem("Speichern");
		projektLaden = new JMenuItem("Projekt laden");
		speichern.setEnabled(false);
		projektLaden.setEnabled(false);
		datei.add(speichern);
		datei.add(projektLaden);
		
		bearbeiten = new JMenu("Bearbeiten");
		kopieren = new JMenuItem("Kopieren");
		einfuegen = new JMenuItem("Einfuegen");
		ausschneiden = new JMenuItem("Ausschneiden");
		kopieren.setEnabled(false);
		einfuegen.setEnabled(false);
		ausschneiden.setEnabled(false);
		bearbeiten.add(kopieren);
		bearbeiten.add(ausschneiden);
		bearbeiten.add(einfuegen);
		
		exportieren = new JMenu("Exportieren");
		gesamt = new JMenuItem("Gesamtansicht...");
		exportieren.add(gesamt);
		
		objekte = new JMenu("Objekte");
		hinzufuegen = new JMenuItem("Hinzufuegen");
		loeschen = new JMenuItem("Loeschen");
		aendern = new JMenuItem("Aendern");
		hinzufuegen.setEnabled(false);
		loeschen.setEnabled(false);
		aendern.setEnabled(false);
		objekte.add(hinzufuegen);
		objekte.add(loeschen);
		objekte.add(aendern);

		add(datei);
		add(bearbeiten);
		add(exportieren);
		add(objekte);
		
		this.gesamt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showOpenDialog(MenuPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String directory = fc.getSelectedFile().getAbsolutePath();
		            datenausgabe.exportiereDaten(directory);
		        } 
			}
		});
	}
	
	
	
	public JMenu getDatei() {
		return datei;
	}

	public void setDatei(JMenu datei) {
		this.datei = datei;
	}

	public JMenu getBearbeiten() {
		return bearbeiten;
	}

	public void setBearbeiten(JMenu bearbeiten) {
		this.bearbeiten = bearbeiten;
	}

	public JMenuItem getSpeichern() {
		return speichern;
	}

	public void setSpeichern(JMenuItem speichern) {
		this.speichern = speichern;
	}

	public JMenu getExportieren() {
		return exportieren;
	}

	public void setExportieren(JMenu exportieren) {
		this.exportieren = exportieren;
	}

	public JMenuItem getGesamt() {
		return gesamt;
	}

	public void setGesamt(JMenuItem gesamt) {
		this.gesamt = gesamt;
	}

	public JMenu getObjekte() {
		return objekte;
	}

	public void setObjekte(JMenu objekte) {
		this.objekte = objekte;
	}

	public JMenuItem getHinzufuegen() {
		return hinzufuegen;
	}

	public void setHinzufuegen(JMenuItem hinzufuegen) {
		this.hinzufuegen = hinzufuegen;
	}

	public JMenuItem getLoeschen() {
		return loeschen;
	}

	public void setLoeschen(JMenuItem loeschen) {
		this.loeschen = loeschen;
	}

	public JMenuItem getAendern() {
		return aendern;
	}

	public void setAendern(JMenuItem aendern) {
		this.aendern = aendern;
	}

}
