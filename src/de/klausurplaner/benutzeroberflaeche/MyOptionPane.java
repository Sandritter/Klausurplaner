package de.klausurplaner.benutzeroberflaeche;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

import de.klausurplaner.controller.builder.TagBuildable;
import de.klausurplaner.controller.direktoren.DirektorInterface;
import de.klausurplaner.controller.direktoren.EinleseDirektor;
import de.klausurplaner.controller.objekte.Einstellung;
import de.klausurplaner.dateneinausgabe.einlesen.XMLNotValideException;
import de.klausurplaner.helper.Helper;
import de.klausurplaner.validatoren.EinstellungsValidator;

/**
 * Klasse, welche von JOptionPane erbt und die EinstellunsgPanelStart-Dialogbox mit den Inhalten fuellt
 * @author Michael Sandritter, Benjamin Christiani, Merle Hiort
 *
 */
public class MyOptionPane extends JOptionPane{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2972881269081340441L;
	
	String dateiname; 
	/**
	 * Button, mit welchem der FileChooser geoeffnet wird
	 */
	private JButton durchsuchen;
	/**
	 * Textfeld, in welchem der absolute Pfad der ausgwaehlten Datei angezeigt wird
	 */
	private JTextField stammdaten_pfad;
	/**
	 * FileChooser, mit dem eine Stammdatendatei gewaehlt werden kann
	 */
	private JFileChooser stammdaten;
	/**
	 * CheckBox, die angibt, ob mehrere Klausuren eines Semesters pro Tag zugelassen werden sollen
	 */
	private JCheckBox mehrere_klausuren_semester; 
	/**
	 * CheckBox, die angibt, ob mehrere Klausuren verschiedener Semester pro Tag zugelassen werden sollen
	 */
	private JCheckBox mehrere_klausuren; 
	/**
	 * CheckBox, die angibt, ob Klausuren am Wochenende zugelassen werden sollen
	 */
	private JCheckBox wochenende;
	/**
	 * CheckBox, die angibt, ob der Zeitabstand zwischen zwei Klausuren je nach Schwierigkeitsgrad beachtet werden soll
	 */
	private JCheckBox kategorie_beachten;
	/**
	 * ComboBox, mit welcher die Startzeit einer Klausur auf Viertelstunden-/Halbstunden-/...-betraege gerundet werden kann
	 */
	private JComboBox startzeit_gerundet;
	/**
	 * JSpinner, mit welchem der Beginn der Klausurphase definiert wird
	 */
	private JSpinner klausurphase_von;
	/**
	 * JSpinner, mit welchem das Ende der Klausurphase definiert wird
	 */
	private JSpinner klausurphase_bis;
	/**
	 * JSpinner, mit welchem die Startuhrzeit eines Pruefungstages definiert wird
	 */
	private JComboBox tag_start;
	/**
	 * JSpinner, mit welchem die Enduhrzeit eines Pruefungstages definiert wird
	 */
	private JComboBox tag_ende;
	/**
	 * JSlider, mit welchem der Mindestabstand zwischen zwei Klausuren an einem Tag in Minuten definiert wird
	 */
	private JSlider min_abstand_klausuren;
	
	/**
	 * Beschriftende Texte fuer das EinstellungsPanelStart
	 */
	private JLabel message_stammdaten;
	private JLabel message_klausurphase;
	private JLabel message_klausurphase2;
	private JLabel message_mindestabstand;
	private JLabel message_startzeit;
	private JLabel message_klausurtag; 
	private JLabel message_klausurtag2;
	/**
	 * Platzhalter fuer eventuelle Fehlermeldungen
	 */
	private JLabel error_message;
	/**
	 * Stringarray, in welchem die Optionen fuer die Startzeitrundung angegeben sind
	 */
	private String startzeit_options[] = {"0","5", "10", "15", "30", "60"};
	private ArrayList<String> tagzeit_options; 
	private int ok;
	Date tagS;
	Date tagE;
	Date klausurphaseV;
	Date klausurphaseB;
	
	
	/**
	 * Konstruktor fuer das MyOptionPane
	 */
	public MyOptionPane(){
		addComponents();
	}
	
	/**
	 * Fuege Komponenten zum MyOptionPane hinzu
	 */
	public void addComponents(){
		message_stammdaten = new JLabel("Basisinfo laden");
		stammdaten_pfad = new JTextField("Dateipfad...");
		stammdaten = createFileChooser();
		stammdaten.setVisible(false);
		durchsuchen = new JButton("Durchsuchen...");
		
		message_klausurphase = new JLabel("Klausurphase von");
		klausurphase_von = createDateSpinner();
		message_klausurphase2 = new JLabel("bis");
		klausurphase_bis = createDateSpinner();
		
		mehrere_klausuren_semester = new JCheckBox("Je Semester nur eine Klausur pro Tag erlauben");
		mehrere_klausuren =  new JCheckBox("Nur eine Klausur pro Tag erlauben");
		message_mindestabstand = new JLabel("Mindestabstand zwischen zwei Klausuren eines Semesters (Minuten)");
		min_abstand_klausuren = createJSlider(0,60,0,10);
		
		wochenende = new JCheckBox("Keine Klausuren am Wochenende");  
		
		message_startzeit = new JLabel("Startzeit runden auf (Minuten)");
		startzeit_gerundet = new JComboBox(startzeit_options);
		
		tagzeit_options = new ArrayList<String>();
		for (int i = 0; i<25; i++){
			tagzeit_options.add(i+ ":00");
		}
		
		
		message_klausurtag = new JLabel("Klausurtag beginnt um");
		tag_start = new JComboBox(tagzeit_options.toArray());
		tag_start.setSelectedIndex(8);
		message_klausurtag2 = new JLabel("und endet um");
		tag_ende = new JComboBox(tagzeit_options.toArray());
		tag_ende.setSelectedIndex(18);
		
		kategorie_beachten = new JCheckBox("Abstand zwischen Klausuren je nach Kategorie beachten");  
		
		addActionListenerButtons();
		fillOptionPane();
	}
	
	/**
	 * Methode, um das OptionPane mit den Komponenten zu fuellen und anzuzeigen
	 */
	public void fillOptionPane(){
		Object[] params = {message_stammdaten, stammdaten_pfad, durchsuchen, stammdaten,
				message_klausurphase, klausurphase_von, message_klausurphase2, klausurphase_bis, 
				mehrere_klausuren_semester, mehrere_klausuren, message_mindestabstand, min_abstand_klausuren, 
				wochenende, 
				message_startzeit, startzeit_gerundet, 
				message_klausurtag, tag_start, message_klausurtag2, tag_ende, 
				kategorie_beachten,
				error_message};  
		
		ok = JOptionPane.showConfirmDialog(this, params, "Einstellungen", JOptionPane.OK_CANCEL_OPTION);  
	}
	
	/**
	 * Methode, in welcher die ActionListener zu den Buttons hinzugefuegt werden
	 */
	public void addActionListenerButtons(){
		//Wenn auf Durchsuchen geklickt wird, wird der FileChooser geoeffnet
		durchsuchen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stammdaten.setVisible(true);
				int rueckgabeWert = stammdaten.showOpenDialog(null);		        
		        if(rueckgabeWert == JFileChooser.APPROVE_OPTION){ //wenn Oeffnen geklickt wurde
	        		dateiname = stammdaten.getSelectedFile().getAbsolutePath();
	        		stammdaten_pfad.setText(dateiname);
		        }
	    		stammdaten.setVisible(false);
			}

		});
	}

	/**
	 * Methode, mit welcher beim Klicken des OK-Buttons die Daten an die Logik geschickt werden
	 * Validierung der eingegeben Werte des Benutzers
	 * @param einleseDirektor
	 * @param ausgabeDirektor
	 * @return 
	 */
	public boolean validate(TagBuildable tagBuilder, DirektorInterface einleseDirektor){
		if(dateiname != null){
			try {
				((EinleseDirektor)einleseDirektor).start(dateiname);
			} catch (XMLNotValideException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
		}else{
			JOptionPane.showMessageDialog(this, "Bitte gueltigen Stammdaten-Dateipfad angeben!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
			
		try {
			tagS = Helper.parseUhrzeit((String) tag_start.getSelectedItem());
			tagE = Helper.parseUhrzeit((String) tag_ende.getSelectedItem());
			klausurphaseV = (Date)klausurphase_von.getValue();
			klausurphaseB = (Date)klausurphase_bis.getValue();
			
			EinstellungsValidator eValidator = new EinstellungsValidator(klausurphaseB, klausurphaseV, tagS, tagE);
			if(eValidator.valide()){
				return true;
			}else{
				JOptionPane.showMessageDialog(this, "Bitte ueberpruefen Sie Datum und Uhrzeit! \n(Hinweis: Die Klausurphase muss mindestens 7 Tage lang sein.)", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Hilfsmethode die das initialisieren des TagBuilders anstoesst
	 * @param tagBuilder
	 */
	public void sendeDaten(TagBuildable tagBuilder){
		tagBuilder.init(klausurphaseV, klausurphaseB, tagS, tagE);
	}
	
	/**
	 * Hilfsmethode die das initialisieren der Einstellungen anstoesst
	 * @param einstellung
	 */
	public void sendeDaten(Einstellung einstellung){
		einstellung.init(klausurphaseV, klausurphaseB, mehrere_klausuren.isSelected(), mehrere_klausuren_semester.isSelected(), min_abstand_klausuren.getValue(), wochenende.isSelected(), Integer.parseInt((String)startzeit_gerundet.getSelectedItem()), tagS, tagE, kategorie_beachten.isSelected());
	}
	
	/**
	 * Hilfsmethode resetet Eingaben im MyOptionPane
	 */
	public void reset(){
		stammdaten_pfad.setText("Dateipfad...");
		dateiname = null;
		mehrere_klausuren_semester.setSelected(false);
		mehrere_klausuren.setSelected(false);
		min_abstand_klausuren.setValue(0);
		wochenende.setSelected(false);
		startzeit_gerundet.setSelectedIndex(0);
		kategorie_beachten.setSelected(false);
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
        fc.setDialogTitle("Datei auswaehlen");
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false); //keine anderen Formate duerfen ausgewaehlt werden
        return fc;
	}
	
	/**
	 * Hilfsmethode um einen DateSpinner (Format yyyy-mm-dd) zu erzeugen
	 * @return JSpinner
	 */
	public JSpinner createDateSpinner(){
		SpinnerModel model = new SpinnerDateModel();
		JSpinner jSpinner = new JSpinner(model);
		jSpinner.setEditor(new JSpinner.DateEditor(jSpinner, "yyyy-MM-dd"));
		jSpinner.setSize(20, 20);
		return jSpinner;
	}
	
	/**
	 * Hilfsmethode um einen TimeSpinner (Format hh:mm a) zu erzeugen
	 * @return JSpinner
	 */
	public JSpinner createTimeSpinnerAM(){
		SpinnerModel model = new SpinnerDateModel();
		JSpinner jSpinner = new JSpinner(model);
		jSpinner.setEditor(new JSpinner.DateEditor(jSpinner, "kk:mm"));
		jSpinner.setSize(20, 20);
		return jSpinner;
	}
	
	/**
	 * Hilfsmethode um einen JSlider zu erzeugen
	 * @param min Minimaler Wert
	 * @param max Maximaler Wert
	 * @param init Initialer Wert
	 * @param step Schrittgroesse der Markierungen
	 * @return JSlider
	 */
	public JSlider createJSlider(int min, int max, int init, int step){
		JSlider jslider = new JSlider(JSlider.HORIZONTAL, min, max, init);
		jslider.setMajorTickSpacing(step);
		jslider.setMinorTickSpacing(1);
		jslider.setPaintTicks(true);
		jslider.setPaintLabels(true);
		return jslider;
	}
	
	public int getOk(){
		return this.ok;
	}
}


