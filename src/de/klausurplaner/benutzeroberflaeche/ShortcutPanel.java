package de.klausurplaner.benutzeroberflaeche;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;

import de.klausurplaner.dateneinausgabe.ausgeben.DatenausgabeInterface;
/**
 * 
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 * Jpanel, welches die Shortcut-Buttons zum schnellen speichern, kopieren, ausschneiden und einfuegen beinhaltet
 */
@SuppressWarnings("serial")
public class ShortcutPanel extends JLayeredPane{
	
	private JButton exportButton; 
	private JButton saveAsButton;
	private JButton copyButton;
	private JButton cutButton;
	private JButton pasteButton;
	private JFileChooser fc;
	private Image saveHoverImage;
	private Image saveImage;

	
	@SuppressWarnings("unused")
	private static DatenausgabeInterface datenausgabe;
	
	/**
	 * 
	 * @param datenausgabe DatenausgabeInterface welches das Exportieren ermoeglicht
	 */
	@SuppressWarnings("static-access")
	public ShortcutPanel(final DatenausgabeInterface datenausgabe){
		this.datenausgabe = datenausgabe;
		FlowLayout fl = new FlowLayout();
		setLayout(fl);
		this.fc = new JFileChooser();
		fc.setDialogTitle("Exportieren...");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		this.saveAsButton = new JButton();
		this.saveAsButton.setBorder(BorderFactory.createEmptyBorder());
		this.saveAsButton.setContentAreaFilled(false);
		this.exportButton = new JButton();
		this.exportButton.setBorder(BorderFactory.createEmptyBorder());
		this.exportButton.setContentAreaFilled(false);
		this.copyButton = new JButton();
		this.copyButton.setBorder(BorderFactory.createEmptyBorder());
		this.copyButton.setContentAreaFilled(false);
		this.cutButton = new JButton();
		this.cutButton.setBorder(BorderFactory.createEmptyBorder());
		this.cutButton.setContentAreaFilled(false);
		this.pasteButton = new JButton();
		this.pasteButton.setBorder(BorderFactory.createEmptyBorder());
		this.pasteButton.setContentAreaFilled(false);
		
		try {
			saveImage = ImageIO.read(new File("images/shortcutIcons/export.png"));
			saveHoverImage = ImageIO.read(new File("images/shortcutIcons/hExport.png"));
			Image saveAsImage = ImageIO.read(new File("images/shortcutIcons/saveas.png"));
			Image copyImage = ImageIO.read(new File("images/shortcutIcons/copy.png"));
			Image cutImage = ImageIO.read(new File("images/shortcutIcons/cut.png"));
			Image pasteImage = ImageIO.read(new File("images/shortcutIcons/paste.png"));
			
			Image psaveImage = ImageIO.read(new File("images/shortcutIcons/pExport.png"));
			Image psaveAsImage = ImageIO.read(new File("images/shortcutIcons/pSaveas.png"));
			Image pcopyImage = ImageIO.read(new File("images/shortcutIcons/pCopy.png"));
			Image pcutImage = ImageIO.read(new File("images/shortcutIcons/pCut.png"));
			Image ppasteImage = ImageIO.read(new File("images/shortcutIcons/pPaste.png"));
			
			
			this.exportButton.setIcon(new ImageIcon(saveImage));
			this.saveAsButton.setIcon(new ImageIcon(saveAsImage));
			this.copyButton.setIcon(new ImageIcon(copyImage));
			this.cutButton.setIcon(new ImageIcon(cutImage));
			this.pasteButton.setIcon(new ImageIcon(pasteImage));
			
			this.exportButton.setRolloverIcon(new ImageIcon(saveImage));
			
			this.exportButton.setPressedIcon(new ImageIcon(psaveImage));
			this.saveAsButton.setPressedIcon(new ImageIcon(psaveAsImage));
			this.copyButton.setPressedIcon(new ImageIcon(pcopyImage));
			this.cutButton.setPressedIcon(new ImageIcon(pcutImage));
			this.pasteButton.setPressedIcon(new ImageIcon(ppasteImage));
			
			this.copyButton.setEnabled(false);
			this.cutButton.setEnabled(false);
			this.pasteButton.setEnabled(false);
			this.saveAsButton.setEnabled(false);
		} catch(IOException e){
			e.printStackTrace();
		}

		this.exportButton.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e){
				exportButton.setIcon(new ImageIcon(saveHoverImage));
				repaint();
			}
			
			public void mouseExited(MouseEvent e){
				exportButton.setIcon(new ImageIcon(saveImage));
				repaint();
			}
		});
		
		add(this.exportButton , fl);
		add(this.saveAsButton, fl);
		add(this.copyButton, fl);
		add(this.cutButton, fl);
		add(this.pasteButton, fl);
		
		this.exportButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showOpenDialog(ShortcutPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String directory = fc.getSelectedFile().getAbsolutePath();
					
					// exportiere Kalender wenn export-Button betaetigt wurde 
		            datenausgabe.exportiereDaten(directory);
		        } 
			}
		});
		
		this.saveAsButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.copyButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.cutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.pasteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

}
