package de.klausurplaner.benutzeroberflaeche;

import javax.swing.*;

import de.klausurplaner.controller.builder.KlausurBuildable;
import de.klausurplaner.controller.builder.KlausurBuilder;
import de.klausurplaner.controller.direktoren.KalenderDirektable;
import de.klausurplaner.controller.direktoren.KalenderDirektor;
import de.klausurplaner.controller.objekte.Einstellung;
import de.klausurplaner.dateneinausgabe.ausgeben.DatenausgabeInterface;

import java.awt.*;

/**
 * Klasse, in welcher das Hauptfenster der Benutzeroberflaeche mit seinen
 * Komponenten erzeugt wird
 * 
 * @author Michael Sandritter, Merle Hiort, Benjamin Christiani
 * 
 */
public class Hauptfenster extends JFrame {


	private static final long serialVersionUID = -1473453087053534133L;
	/**
	 * MenuPanel, welches die obere Menuleiste darstellt
	 */
	private MenuPanel menuPanel;
	/**
	 * Startfenster, in welchem der Benutzer auswaehlt, ob er ein neues Projekt
	 * beginnen moechte
	 */
	private Startfenster startfenster;
	/**
	 * Einstellungsfenster, in welchem der Benutzer die initialen Einstellungen
	 * macht und die Stammdaten laedt
	 */
	private EinstellungsPanelStart einstellungsPanelStart;
	/**
	 * ShortcutPanel, in welchem sich die Shortcut-Buttons befinden
	 */
	private ShortcutPanel shortcutPanel;
	/**
	 * NavigationsPanel, beinhaltet die Navigationsbutton um in der
	 * Kalenderansicht zu navigieren
	 */
	private NavigationPanel navigationPanel;
	/**
	 * AnsichtsoptionsPanel, beinhaltet Checkboxen zur selektiven Ansicht
	 * verschiedener Klausuren
	 */
	private AnsichtsoptionsPanel ansichtsoptionsPanel;
	/**
	 * BorderPanelLinks beinhaltet das AnsichtsoptionsPanel und
	 * EinstellungsPanel
	 */
	private BorderPanelLinks borderPanelLinks;
	/**
	 * BorderPanelTop beinhaltet das ShortcutPanel und NavigationsPanel
	 */
	private BorderPanelTop borderPanelTop;
	/**
	 * BOrderPanelDown beinhaltet das borderPanelLinks und das KalenderPanel
	 */
	private BorderPanelDown borderPanelDown;
	/**
	 * KalenderPanel, der die Anzeige Kalenderansicht realisiert
	 */
	private KalenderPanel kalenderPanel;
	/**
	 * EinstellungsPanel, in welchem die beim Start gemachten Einstellungen
	 * spaeter noch veraendert werden koennen
	 */
	private EinstellungsPanel einstellungsPanel;

	private JScrollPane scrollPane;

	private TagComponentBuilder tagComponentBuilder;
	private KalenderDirektable kalDirektor;
	private DatenausgabeInterface datenausgabe;
	private Einstellung einstellung;
	private KlausurBuildable klausurBuilder;

	/**
	 * Konstruktor
	 * @param name -> Name des Hauptfensters
	 * @param tagComponentBuilder -> verwaltet TagComponents
	 * @param ked -> EinstellungsDirektor beinhaltet alle Object-Builder
	 * @param datenausgabe -> Datenausgabe 
	 * @param einstellung -> beinhaltet Validationseinstellungen
	 */
	public Hauptfenster(String name, TagComponentBuilder tagComponentBuilder,KalenderDirektable ked, DatenausgabeInterface datenausgabe,Einstellung einstellung) {
		super(name);
		setSize(1280, 700);
		this.tagComponentBuilder = tagComponentBuilder;
		this.datenausgabe = datenausgabe;
		this.kalDirektor = ked;
		this.einstellung = einstellung;
		this.klausurBuilder = (KlausurBuildable) ((KalenderDirektor) kalDirektor)
				.getEinDirektorInterface().getBuilder(new KlausurBuilder());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = getContentPane();
		addComponents(container);
		// pack();
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Methode, um die Komponenten zum Hauptfenster hinzuzufuegen
	 * 
	 * @param c Hauptcontainer
	 */
	public boolean addComponents(Container container) {

		container.setPreferredSize(new Dimension(1280, 700));
		container.setMinimumSize(new Dimension(1280, 700));
		container.setBackground(Color.WHITE);

		menuPanel = new MenuPanel(datenausgabe);
		menuPanel.setMaximumSize(new Dimension(310, 25));
		// MID LEVEL - PANELs
		this.ansichtsoptionsPanel = new AnsichtsoptionsPanel(klausurBuilder.getSemesterListe(), kalDirektor);

		this.einstellungsPanel = new EinstellungsPanel(this.einstellung,kalDirektor);

		this.shortcutPanel = new ShortcutPanel(this.datenausgabe);
		this.navigationPanel = new NavigationPanel(this.kalDirektor);

		// TOP LEVEL - PANELs
		this.borderPanelTop = new BorderPanelTop(this.shortcutPanel,this.navigationPanel);
		this.borderPanelLinks = new BorderPanelLinks(this.ansichtsoptionsPanel,this.einstellungsPanel);
		this.kalenderPanel = new KalenderPanel(this.tagComponentBuilder);
		// fuege dem KalenderDirektor Observer hinzu
		((KalenderDirektor) this.kalDirektor).addObserver(tagComponentBuilder);
		((KalenderDirektor) this.kalDirektor).addObserver(kalenderPanel);
		((KalenderDirektor) this.kalDirektor).addObserver(ansichtsoptionsPanel);
		this.scrollPane = new JScrollPane(this.kalenderPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setPreferredSize(new Dimension(920, 570));
		this.borderPanelDown = new BorderPanelDown(this.borderPanelLinks,this.scrollPane);

		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		fl.setHgap(0);
		fl.setVgap(0);
		container.setLayout(fl);

		container.add(borderPanelTop);
		container.add(borderPanelDown);

		setJMenuBar(menuPanel);
		return true;
	}

	public MenuPanel getMenuPanel() {
		return menuPanel;
	}

	public void setMenuPanel(MenuPanel menuPanel) {
		this.menuPanel = menuPanel;
	}

	public Startfenster getStartfenster() {
		return startfenster;
	}

	public void setStartfenster(Startfenster startfenster) {
		this.startfenster = startfenster;
	}

	public EinstellungsPanelStart getEinstellungsPanelStart() {
		return einstellungsPanelStart;
	}

	public void setEinstellungsPanelStart(
			EinstellungsPanelStart einstellungsPanelStart) {
		this.einstellungsPanelStart = einstellungsPanelStart;
	}

}
