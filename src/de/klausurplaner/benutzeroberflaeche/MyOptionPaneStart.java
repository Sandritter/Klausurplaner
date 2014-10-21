package de.klausurplaner.benutzeroberflaeche;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Startfenster in dem auswaehlen kann ob man ein neues Projekt erstellen moechte 
 * ein bereits bestehendes Projekt einladen moechte
 * @author Merle Hiort, Benjamin Christiani, Michael Sandritter
 *
 */
public class MyOptionPaneStart extends JOptionPane implements ItemListener {


	private static final long serialVersionUID = -1628383501930222491L;

	/**
	 * JCheckBox, um auszuwaehlen, dass ein Projekt geladen werden soll
	 */
	private JCheckBox projekt_laden;
	
	/**
	 * JCheckBox, um auszuwaehlen, dass ein neues Projekt erstellt werden soll
	 */
	private JCheckBox projekt_neu;
	
	/**
	 * FileChooser, um einen Dateipfad fuer die zu ladende Datei anzugeben
	 */
	private JFileChooser laden;
	
	/**
	 * Textfeld, in welchem der Pfad steht
	 */
	private JTextField laden_pfad;
	
	/**
	 * Button um den FileChooser zu oeffnen und durch das Dateisystem zu browsen
	 */
	private JButton durchsuchen;
	

	private JLabel platzhalter;

	/**
	 * boolean, der angibt, was ausgewaehlt wurde: true=neues Projekt, false=Projekt laden
	 */
	private boolean wahl;

	private int ok = 10;
	
	/**
	 * Konstruktor fuer das OptionPane
	 */
	public MyOptionPaneStart(){
		addComponents();
	}
	
	/**
	 * Methode um Komponenten hinzuzufuegen
	 */
	public void addComponents(){
		projekt_neu = new JCheckBox("Neues Projekt erstellen");
		projekt_laden = new JCheckBox("Projekt laden");
		projekt_laden.setEnabled(false);
		laden = createFileChooser();
		laden.setEnabled(false);
		laden.setVisible(false);
		laden_pfad = new JTextField("Dateipfad");
		laden_pfad.setVisible(false);
		durchsuchen = new JButton("Durchsuchen");
		durchsuchen.setVisible(false);
		addActionListenerButtons();
		fillOptionPane();
	}
	
	/**
	 * Methode, um das OptionPane mit den Komponenten zu fuellen und anzuzeigen
	 */
	public void fillOptionPane(){
		Object[] params = {projekt_neu, projekt_laden, laden_pfad, laden, durchsuchen};  
		
		ok = JOptionPane.showConfirmDialog(this, params, "Start", JOptionPane.OK_CANCEL_OPTION);  
	}
	
	/**
	 * Methode, in welcher festgelegt ist, was passiert, wenn eine bestimmte CheckBox ausgewählt wurde
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
		//Wenn neues Projekt ausgewaehlt, gehe zum Hauptfenster weiter
		if (source == projekt_neu){
			wahl = true;
		}
		//wenn projekt laden ausgewaehlt, oeffne FileChooser
		if (source == projekt_laden){ 
			wahl = false;
			if(e.getStateChange() == ItemEvent.SELECTED){
				laden_pfad.setVisible(true);
				durchsuchen.setVisible(true);
			}else{
				laden_pfad.setVisible(false);
				durchsuchen.setVisible(false);
			}
		}	
	}
	
	public void addActionListenerButtons(){
		//Wenn auf Durchsuchen geklickt wird, wird der FileChooser geoeffnet
		
		durchsuchen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				laden.setVisible(true);
				int rueckgabeWert = laden.showOpenDialog(null);		        
		        if(rueckgabeWert == JFileChooser.APPROVE_OPTION){ //wenn Oeffnen geklickt wurde
	        		String dateiname = laden.getSelectedFile().getName();
	        		laden_pfad.setText(dateiname);
					//TODO: schicke Daten an Director
		        }
			}
		});
		
		projekt_laden.addItemListener(this);
		projekt_neu.addItemListener(this);
	}
	
	/**
	 * Methode, in welcher ein FileChooser inklusive Filter erzeugt wird, sodass nur Dateien im "richtigen" Format fuer den Benutzer zur Auswahl stehen
	 * @return
	 */
	public JFileChooser createFileChooser(){
		JFileChooser fc = new JFileChooser();	
		String [] acceptableFiles = {"xml"}; //auswaehlbare Formate 
        MyFileExtensionFilter filter = new MyFileExtensionFilter(acceptableFiles); //Filter
        fc.setFileFilter(filter);
        fc.setDialogTitle("Datei auswählen");
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false); //keine anderen Formate duerfen ausgewaehlt werden
        return fc;
	}

	public JCheckBox getProjekt_laden() {
		return projekt_laden;
	}

	public void setProjekt_laden(JCheckBox projekt_laden) {
		this.projekt_laden = projekt_laden;
	}

	public JCheckBox getProjekt_neu() {
		return projekt_neu;
	}

	public void setProjekt_neu(JCheckBox projekt_neu) {
		this.projekt_neu = projekt_neu;
	}

	public JFileChooser getLaden() {
		return laden;
	}

	public void setLaden(JFileChooser laden) {
		this.laden = laden;
	}

	public JTextField getLaden_pfad() {
		return laden_pfad;
	}

	public void setLaden_pfad(JTextField laden_pfad) {
		this.laden_pfad = laden_pfad;
	}

	public JButton getDurchsuchen() {
		return durchsuchen;
	}

	public void setDurchsuchen(JButton durchsuchen) {
		this.durchsuchen = durchsuchen;
	}

	public JLabel getPlatzhalter() {
		return platzhalter;
	}

	public void setPlatzhalter(JLabel platzhalter) {
		this.platzhalter = platzhalter;
	}

	public boolean isWahl() {
		return wahl;
	}

	public void setWahl(boolean wahl) {
		this.wahl = wahl;
	}
	
	public int getOk(){
		return this.ok;
	}
}
