package de.klausurplaner.benutzeroberflaeche;


import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Klasse zum Erstellen des Einstellungsfensters, in welchem der Anwender seine initialen Einstellungen machen kann und die Stammdaten laedt
 * @author Michael Sandritter, Merle Hiort, Benjamin Christiani
 *
 */
public class EinstellungsPanelStart extends JDialog{

	private static final long serialVersionUID = -5184588984988151078L;
	/**
	 * Inhalt des EinstellungsPanels
	 */
	private MyOptionPane myOptionPane;
		
	/**
	 * Konstruktor fuer das EinstellungsPanel
	 * @param owner das Hauptfenster, an welches das Einstellungspanel gekoppelt wird
	 * @param title Titel des Fensters
	 * @param modal true, wenn alle andere Fenster deaktiviert sind, solange das Einstellungspanel angezeigt wird
	 */
	public EinstellungsPanelStart(JFrame owner, String title, boolean modal){
		super(owner, title);
		addComponents();
		setBounds(800,800,0,0);
		setVisible(true);
		setResizable(false);
	}
	
	/**
	 * Methode, um die Komponenten hinzuzufuegen
	 */
	public void addComponents(){
		myOptionPane = new MyOptionPane();
	}

	
	public MyOptionPane getMyOptionPane() {
		return myOptionPane;
	}

	public void setMyOptionPane(MyOptionPane myOptionPane) {
		this.myOptionPane = myOptionPane;
	}
}

