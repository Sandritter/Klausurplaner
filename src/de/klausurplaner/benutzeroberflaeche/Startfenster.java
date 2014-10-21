package de.klausurplaner.benutzeroberflaeche;

import javax.swing.*;

/**
 * Klasse fuer das erste Fenster, welches der Benutzer sieht, in welchem er auswaehlen kann, ob er ein Projekt laden oder ein neues Projekt beginnen moechte.
 *
 * @author Michael Sandritter, Merle Hiort, Benjamin Christiani
 *
 */
public class Startfenster extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2935974312359433263L;
	
	/**
	 * Inhalt des Startfensters
	 */
	private MyOptionPaneStart myOptionPaneStart;
	
	/**
	 * 
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public Startfenster(JDialog owner, String title, boolean modal){
		super(owner, title);
		addComponents();
		setSize(800, 650);
		setVisible(true);
		setResizable(false);
	}

	/**
	 * Methode, um die Komponenten hinzuzufuegen
	 */
	public void addComponents(){
		myOptionPaneStart = new MyOptionPaneStart();		
	}

	public void setMyOptionPaneStart(MyOptionPaneStart myOptionPane) {
		this.myOptionPaneStart = myOptionPane;
	}
	public MyOptionPaneStart getMyOptionPaneStart(){
		return this.myOptionPaneStart;
	}
}
