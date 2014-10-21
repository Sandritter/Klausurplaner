package de.klausurplaner.benutzeroberflaeche;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JSlider;
import javax.swing.border.Border;

import de.klausurplaner.controller.direktoren.KalenderDirektable;
import de.klausurplaner.controller.objekte.Einstellung;
import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.controller.objekte.Tag;

/**
 * Klasse, welche von JPanel erbt und dem Benutzer die Moeglichkeit gibt, die
 * beim Start gemachten Einstellungen spaeter noch zu aendern.
 * 
 * @author Michael Sandritter, Benjamin Christiani, Merle Hiort
 * 
 */
public class EinstellungsPanel extends JLayeredPane {

	/**
	 * Button zum Uebernehmen der eingegebenen Werte
	 */
	private JButton uebernehmen;
	/**
	 * Button zum Verwerfen der eingegeben Werte
	 */
	private JButton verwerfen;

	/**
	 * ComboBox, mit der ausgewaehlt wird, ob die Klausurphase am Anfang oder
	 * am Ende verlaengert werden soll
	 */
	private String klausurphase_options[] = { "am Anfang", "am Ende" };
	private JComboBox klausurphaseVerlaengern;

	/**
	 * ComboBox, mit der ausgewaehlt wird, ob der Klausurtag am Anfang oder
	 * am Ende verlaengert werden soll
	 */
	private String klausurtag_options[] = { "am Anfang", "am Ende" };
	private JComboBox klausurtagVerlaengern;
	
	/**
	 * ComboBox, mit der die Anzahl der Wochen angegeben wird, um die die
	 * Klausurphase verlaengert wird
	 */
	private JComboBox klausurphase_wochen;
	private String wochen_options[] = { "0", "1", "2", "3", "4", "5", "6" };

	/**
	 * JCombobox, mit der die Anzahl der Stunden angegeben wird, um die der
	 * Klausurtag verlaengert wird
	 */
	private JComboBox klausurtag_stunden;
	private String stunden_options[] = { "1", "2", "3", "4", "5", "6" };

	/**
	 * CheckBox, die angibt, ob mehrere Klausuren pro Tag zugelassen werden
	 * sollen
	 */
	private JCheckBox mehrere_klausuren;
	/**
	 * CheckBox, die angibt, ob mehrere Klausuren eines Semesters pro Tag
	 * zugelassen werden sollen
	 */
	private JCheckBox mehrere_klausuren_semester;

	/**
	 * JSlider, mit welchem der Mindestabstand zwischen zwei Klausuren an einem
	 * Tag in Minuten definiert wird
	 */
	private JSlider min_abstand_klausuren;

	/**
	 * CheckBox, die angibt, ob Klausuren am Wochenende zugelassen werden sollen
	 */
	private JCheckBox wochenende;
	/**
	 * CheckBox, die angibt, ob der Zeitabstand zwischen zwei Klausuren je nach
	 * Schwierigkeitsgrad beachtet werden soll
	 */
	private JCheckBox kategorie_beachten;
	/**
	 * ComboBox, mit welcher die Startzeit einer Klausur auf
	 * Viertelstunden-/Halbstunden-/...-betraege gerundet werden kann
	 */
	private JComboBox startzeit_gerundet;
	/**
	 * Stringarray, in welchem die Optionen fuer die Startzeitrundung angegeben
	 * sind
	 */
	private String startzeit_options[] = { "0", "5", "10", "15", "30", "60" };


	/**
	 * Beschriftende Texte fuer das EinstellungsPanelStart
	 */
	private JLabel message_startzeit;
	private JLabel message_startzeit2;
	private JLabel message_klausurtag;
	private JLabel message_klausurtag3;
	private JLabel message_klausurphase;
	private JLabel message_klausurphase3;
	private JLabel message_mindestabstand;
	@SuppressWarnings("unused")
	private String klausurPhaseVerlaengernOption;
	@SuppressWarnings("unused")
	private Integer anzahlWochenVerlaengern;

	/**
	 * Einstellungsklasse, an welche die eingegebenen Werte gesendet werden
	 */
	private Einstellung einstellung;
	private KalenderDirektable kalDirektor;
	private static final long serialVersionUID = 1483463812722227765L;

	/**
	 * 
	 * @param einstellung -> beinhaltet alle Validationseinstellungen 
	 * @param kalDirektor -> erhaelt Informationen aus Gui verarbeitet diese und benachrichtigt Gui anschliessend
	 */
	public EinstellungsPanel(Einstellung einstellung, KalenderDirektable kalDirektor) {
		this.einstellung = einstellung;
		this.kalDirektor = kalDirektor;
		setPreferredSize(new Dimension(340, 400));
		setMaximumSize(new Dimension(340, 420));
		Font f = new Font(Font.DIALOG, Font.PLAIN, 12);
		Font fBold = new Font(Font.DIALOG, Font.BOLD, 12);
		Border border = BorderFactory
				.createTitledBorder("Einstellungen aendern");
		setBorder(border);

		// Buttons zum Uebernehmen/Verwerfen der Eingaben
		uebernehmen = new JButton("Uebernehmen");
		verwerfen = new JButton("Verwerfen");
		uebernehmen.setPreferredSize(new Dimension(50, 20));
		verwerfen.setPreferredSize(new Dimension(100, 20));

		// Klausurphasendauer
		message_klausurphase = new JLabel("Klausurphase verlaengern");
		klausurphaseVerlaengern = new JComboBox(klausurphase_options);
		klausurphase_wochen = new JComboBox(wochen_options);
		message_klausurphase3 = new JLabel(" Wochen");

		// Mehrere Klausuren und Mindestabstand
		mehrere_klausuren = new JCheckBox("Nur eine Klausur pro Tag erlauben");
		mehrere_klausuren.setBackground(Color.WHITE);
		mehrere_klausuren_semester = new JCheckBox(
				"Je Semester nur eine Klausur pro Tag erlauben");
		mehrere_klausuren_semester.setBackground(Color.WHITE);
		message_mindestabstand = new JLabel(
				"Mindestabstand zw. Klausuren eines Semesters (Minuten)");
		min_abstand_klausuren = createJSlider(0, 60, 0, 10);
		min_abstand_klausuren.setBackground(Color.WHITE);

		// Wochenende/Feiertage
		wochenende = new JCheckBox("Keine Klausuren am Wochenende");
		wochenende.setBackground(Color.WHITE);

		// Startzeit runden
		message_startzeit = new JLabel("Startzeit runden auf ");
		startzeit_gerundet = new JComboBox(startzeit_options);
		// startzeit_gerundet.setPreferredSize(new Dimension(45, 10));
		message_startzeit2 = new JLabel(" Minuten");

		// Klausurtagdauer
		message_klausurtag = new JLabel("Klausurtag verlaengern");
		message_klausurtag.setEnabled(false);
		klausurtagVerlaengern = new JComboBox(klausurtag_options);
		klausurtagVerlaengern.setEnabled(false);
		klausurtag_stunden = new JComboBox(stunden_options);
		klausurtag_stunden.setEnabled(false);
		message_klausurtag3 = new JLabel(" Stunden");
		message_klausurtag3.setEnabled(false);

		// Kategorie
		kategorie_beachten = new JCheckBox(
				"Klausurabstand je nach Kategorie beachten");
		kategorie_beachten.setBackground(Color.WHITE);

		// Fonts setzen
		message_klausurphase.setFont(fBold);
		klausurphaseVerlaengern.setFont(f);
		klausurphase_wochen.setFont(f);
		message_klausurphase3.setFont(f);
		mehrere_klausuren.setFont(f);
		mehrere_klausuren_semester.setFont(f);
		message_mindestabstand.setFont(f);
		min_abstand_klausuren.setFont(f);
		wochenende.setFont(f);
		message_startzeit.setFont(f);
		startzeit_gerundet.setFont(f);
		message_startzeit2.setFont(f);
		message_klausurtag.setFont(fBold);
		klausurtagVerlaengern.setFont(f);
		klausurtag_stunden.setFont(f);
		message_klausurtag3.setFont(f);
		kategorie_beachten.setFont(f);
		uebernehmen.setFont(f);
		verwerfen.setFont(f);

		// alle Komponenten zum Panel hinzufuegen
		addComponents();
		addListeners();
		setValues();
	}

	/**
	 * Hilfsmethode, die einzelnen Gui-Komponenten Listener hinzufuegt
	 */
	public void addListeners() {
		klausurphaseVerlaengern.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				ItemSelectable is = e.getItemSelectable();
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String klausurphaseVerlaengernOption = selectedString(is);
					klausurPhaseVerlaengernOption = klausurphaseVerlaengernOption;
				}
			}
		});
		klausurphase_wochen.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				ItemSelectable is = e.getItemSelectable();
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Integer wochen = selectedInteger(is);
					anzahlWochenVerlaengern = wochen;
				}
			}
		});
	}

	/**
	 * Quasi eine toString()-Methode
	 * 
	 * @param is - Object [] selectedItems
	 * @return String des selktiereten Items
	 */
	private static String selectedString(ItemSelectable is) {
		Object selected[] = is.getSelectedObjects();
		return ((selected.length == 0) ? "null" : (String) selected[0]);
	}

	/**
	 * 
	 * @param is Object [] selectedItems
	 * @return Integer des selektierten Items
	 */
	private static Integer selectedInteger(ItemSelectable is) {
		Object selected[] = is.getSelectedObjects();
		return (Integer) ((selected.length == 0) ? "null" : Integer
				.parseInt((String) selected[0]));
	}

	/**
	 *  fuegt den Gui-Komponent Werte aus den Einstellungen hinzu
	 */
	private void setValues() {
		this.mehrere_klausuren.setSelected(this.einstellung.isMehrereKlausren());
		this.mehrere_klausuren_semester.setSelected(this.einstellung.isMehrereKlausrenSemester());
		this.min_abstand_klausuren.setValue(this.einstellung.getMindAbstand());
		this.kategorie_beachten.setSelected(this.einstellung.isKategorieAbstand());
		this.startzeit_gerundet.setSelectedItem(String.valueOf(this.einstellung.getRunde_startzeit()));
		this.wochenende.setSelected(this.einstellung.isWochenende());
	}

	/**
	 * Methode um die Komponenten zum Panel hinzuzufuegen und ins Layout einzupassen
	 */
	public void addComponents() {
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		GridBagConstraints c = new GridBagConstraints();

		// TOP LEVEL SpringLayout

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 5;
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 5;
		add(message_klausurphase, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		add(klausurphaseVerlaengern, c);
		c.gridwidth = 1;
		c.gridx = 3;
		add(klausurphase_wochen, c);
		c.gridx = 4;
		add(message_klausurphase3, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 5;
		add(message_klausurtag, c);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		add(klausurtagVerlaengern, c);
		c.gridx = 3;
		c.gridwidth = 1;
		add(klausurtag_stunden, c);
		c.gridx = 4;
		add(message_klausurtag3, c);
		
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 5;
		add(wochenende, c);
		
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 5;
		add(kategorie_beachten, c);
		
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 5;
		add(mehrere_klausuren, c);

		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 5;
		add(mehrere_klausuren_semester, c);

		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 5;
		add(message_mindestabstand, c);
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 5;
		add(min_abstand_klausuren, c);

		c.gridx = 0;
		c.gridy = 10;
		c.gridwidth = 3;
		add(message_startzeit, c);
		c.gridx = 3;
		c.gridy = 10;
		c.gridwidth = 1;
		add(startzeit_gerundet, c);
		c.gridx = 4;
		c.gridy = 10;
		c.gridwidth = 1;
		add(message_startzeit2, c);


		c.gridx = 0;
		c.gridy = 11;
		c.gridwidth = 2;
		add(uebernehmen, c);

		c.gridx = 3;
		c.gridy = 11;
		c.gridwidth = 2;
		add(verwerfen, c);

		// ActionListener hinzufuegen
		addActionListenerButtons();
	}

	/**
	 * Methode, in welcher die ActionListener zu den Buttons hinzugefuegt werden
	 */
	public void addActionListenerButtons() {
		uebernehmen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendeDaten(); // sende Daten an Einstellungsklasse
				for (Tag t : kalDirektor.getTagBuilder().tage()) {
					for (Klausur k : t.getKlausuren()) {
						kalDirektor.getValidator().validateEinstellungen2(k, t);
					}
				}
				kalDirektor.aktualisiereAnsicht();
			}
		});
		verwerfen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// mache nichts, setze Werte zurueck
				klausurphaseVerlaengern.setSelectedIndex(0);
				klausurphase_wochen.setSelectedIndex(0);
				mehrere_klausuren.setSelected(false);
				mehrere_klausuren_semester.setSelected(false);
				min_abstand_klausuren.setValue(0);
				wochenende.setSelected(false);
				startzeit_gerundet.setSelectedIndex(0);
				klausurtagVerlaengern.setSelectedIndex(0);
				klausurtag_stunden.setSelectedIndex(0);
				kategorie_beachten.setSelected(false);
				sendeDaten();
			}

		});
	}

	/**
	 * Methode, mit welcher beim Klicken des OK-Buttons die Daten an die Logik
	 * geschickt werden
	 */
	public void sendeDaten() {
		// wenn ausgewaehlt wurde ob man eine Klausur am Amfang oder Ender
		// verlaengern moechte und eine Anzahl Wochen angegeben hat
		if (Integer.parseInt((String)(klausurphase_wochen.getSelectedItem())) != 0) {
			boolean direkt = false; // in welche Richtung soll verlaengert werden
			direkt = ((String)klausurphaseVerlaengern.getSelectedItem()).compareTo("am Anfang")==0 ? true : false;
			
			einstellung.init(direkt,Integer.parseInt((String) (klausurphase_wochen.getSelectedItem())), mehrere_klausuren.isSelected(), mehrere_klausuren_semester.isSelected(), min_abstand_klausuren.getValue(),wochenende.isSelected(), Integer.parseInt((String) startzeit_gerundet.getSelectedItem()), kategorie_beachten.isSelected());
			klausurphase_wochen.setModel(new DefaultComboBoxModel(wochen_options));
			klausurphaseVerlaengern.setModel(new DefaultComboBoxModel(klausurphase_options));
		} else {
			einstellung.init(mehrere_klausuren.isSelected(),mehrere_klausuren_semester.isSelected(),min_abstand_klausuren.getValue(), wochenende.isSelected(),Integer.parseInt((String) startzeit_gerundet.getSelectedItem()), kategorie_beachten.isSelected());
		}
	}

	/**
	 * Hilfsmethode um einen JSlider zu erzeugen
	 * 
	 * @param min Minimaler Wert
	 * @param max Maximaler Wert
	 * @param init Initialer Wert
	 * @param step Schrittgroesse der Markierungen
	 * @return JSlider
	 */
	public JSlider createJSlider(int min, int max, int init, int step) {
		JSlider jslider = new JSlider(JSlider.HORIZONTAL, min, max, init);
		jslider.setMajorTickSpacing(step);
		jslider.setMinorTickSpacing(1);
		jslider.setPreferredSize(new Dimension(250, 40));
		jslider.setPaintTicks(true);
		jslider.setPaintLabels(true);
		return jslider;
	}

}
