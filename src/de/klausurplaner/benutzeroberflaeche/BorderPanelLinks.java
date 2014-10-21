package de.klausurplaner.benutzeroberflaeche;

import java.awt.Dimension;

import javax.swing.JLayeredPane;
import javax.swing.SpringLayout;

/**
 * 
 * @author Michael Sandritter, Benjamin Christiani, Merle Hiort
 * JPanel, welches AnsichtsoptionsPanel und EinstellungsPanel inne hat
 */
public class BorderPanelLinks extends JLayeredPane{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5291724392214451412L;
	private AnsichtsoptionsPanel aop;
	private EinstellungsPanel ep;
	
	/**
	 * 
	 * @param aop AnsichtsoptionsPanel -> enthaelt Checkboxen zur selektierten Ansicht von ausgewaehlten Semestern
	 * @param ep EinstellungsPanel -> enthaelt Einstellungsmoeglichkeiten, die beim Validierungsprozess uebernommen werden
	 */
	public BorderPanelLinks(AnsichtsoptionsPanel aop, EinstellungsPanel ep){
		
		this.aop = aop;
		this.ep = ep;		
		
		setPreferredSize(new Dimension(350, 570));
		
		SpringLayout sl = new SpringLayout();
		sl.putConstraint(SpringLayout.WEST, ep, 5, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.NORTH, ep, 0, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.NORTH, aop, 420, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, aop, 5, SpringLayout.WEST, this);
		setLayout(sl);
		
		add(this.ep);
		add(this.aop);
	}
}
