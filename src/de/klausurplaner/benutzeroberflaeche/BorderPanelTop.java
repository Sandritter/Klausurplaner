package de.klausurplaner.benutzeroberflaeche;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 * 
 * @author Michael Sandritter, Benjamin Christiani, Merle Hiort
 * Die BorderPanelTop-Klasse ist Container fuer das ShortcutPanel und NavigationsPanel
 */
public class BorderPanelTop extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4136697162036078155L;
	private ShortcutPanel scp;
	private NavigationPanel nvp;
	private BufferedImage bg;
	
	/**
	 * 
	 * @param scp ShortcutPanel	-> beinhaltet ShortcutButtons 
	 * @param nvp NavigationsPanel -> beinhaltet Navigationselemente
	 */
	public BorderPanelTop(ShortcutPanel scp, NavigationPanel nvp){
		this.scp = scp;
		this.nvp = nvp;
		
		// Versuch BackgroundImage aus dem resource Order zu laden
		try {
			this.bg = ImageIO.read(new File("images/shortcutIcons/leder_header.png"));
		} catch (IOException e){
			e.printStackTrace();
		}
	
		setPreferredSize(new Dimension(1280, 90));
		
		SpringLayout sl = new SpringLayout();
		setLayout(sl);
		
		add(this.scp);
		add(this.nvp);
		
		sl.putConstraint(SpringLayout.WEST, scp, 20, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.NORTH, scp, 10, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.NORTH, nvp, 10, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, nvp, 700, SpringLayout.EAST, scp);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.drawImage(this.bg, 0,0, this);
	}
}
