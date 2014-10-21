package de.klausurplaner.benutzeroberflaeche;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;

import de.klausurplaner.controller.direktoren.KalenderDirektable;

/**
 * 
 * @author Merle Hiort, Benjamin Christiani, Michael Sandritter
 * stellt die JButtons bereit, die das navigieren in der Kalenderansicht ermoeglichen
 */
public class NavigationPanel extends JLayeredPane{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4023301719200175593L;
	
	private JButton vor;
	private JButton zurueck;
	@SuppressWarnings("unused")
	private static KalenderDirektable ked;

	/**
	 * Konstruktor
	 * @param ked KalenderDirektor 
	 */
	public NavigationPanel(final KalenderDirektable ked){
		
		FlowLayout fl = new FlowLayout();
		setLayout(fl);
		
		this.zurueck = new JButton();
		this.zurueck.setBorder(BorderFactory.createEmptyBorder());
		this.zurueck.setContentAreaFilled(false);
		this.vor = new JButton();
		this.vor.setBorder(BorderFactory.createEmptyBorder());
		this.vor.setContentAreaFilled(false);
		
		try {
			Image zurueckImage = ImageIO.read(new File("images/shortcutIcons/left.png"));
			Image vorImage = ImageIO.read(new File("images/shortcutIcons/right.png"));
			Image pZurueckImage = ImageIO.read(new File("images/shortcutIcons/pLeft.png"));
			Image pVorImage = ImageIO.read(new File("images/shortcutIcons/pRight.png"));
			
			this.zurueck.setIcon(new ImageIcon(zurueckImage));
			this.vor.setIcon(new ImageIcon(vorImage));
			this.zurueck.setPressedIcon(new ImageIcon(pZurueckImage));
			this.vor.setPressedIcon(new ImageIcon(pVorImage));
			
		} catch(IOException e){
			e.printStackTrace();
		}
		
		add(this.zurueck);
		add(this.vor);
	
		this.zurueck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// wenn der zurueck-Button betaetigt wurde wird dem KalenderDirektor bescheid gesagt, dass er navigieren soll
				ked.navigiereWocheZurueck();
			}
		});
		
		this.vor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// wenn der vor-Button betaetigt wurde wird dem KalenderDirektor bescheid gesagt, dass er navigieren soll
				ked.navigiereWocheVor();
			}
		});
	}

}
