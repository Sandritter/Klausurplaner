package de.klausurplaner.benutzeroberflaeche;

import java.awt.Dimension;

import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

/**
 * 
 * @author Michael Sandritter, Merle Hiort, Benjamin Christiani
 * BorderPanelDown dient als Container fuer das BorderPanelLinks (EinstellungsPanel, AnsichtsOptionsPanel) und den Kalender(TagComponenten)
 */
public class BorderPanelDown extends JLayeredPane{

	private static final long serialVersionUID = 6085505711866822660L;
	
	private BorderPanelLinks bpl;
	private JScrollPane kp;
	
	/**
	 * 
	 * @param bpl BorderPanelLinks
	 * @param kp JScrollPane -> beinhaltet den Kalender, der wiederrum die TagComponenten zeichnet
	 */
	public BorderPanelDown(BorderPanelLinks bpl, JScrollPane kp){
		this.bpl = bpl;
		this.kp = kp;
		
		SpringLayout sl = new SpringLayout();
		setLayout(sl);
		setPreferredSize(new Dimension(1280, 570));
		add(this.bpl);
		add(this.kp);
		sl.putConstraint(SpringLayout.WEST, this.bpl, 0, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.EAST, this.kp, 0, SpringLayout.EAST, this);
	}
}
