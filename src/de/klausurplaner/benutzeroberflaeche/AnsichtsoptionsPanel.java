package de.klausurplaner.benutzeroberflaeche;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.border.Border;

import de.klausurplaner.controller.direktoren.KalenderDirektable;
import de.klausurplaner.controller.direktoren.KalenderDirektor;

/**
 * 
 * @author Ben Christo, Merlina Hiorte, Miquele Sandito
 * Klasse, die Checkboxen fuer die Semester beinhaltet die anzeigt werden sollen oder nicht angezeigt werden sollen
 */
public class AnsichtsoptionsPanel extends JLayeredPane implements ItemListener, Observer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8936904857694378180L;
	/**
	 * Liste von Semestern 
	 */
	private List<Integer> semesterList;
	private final String ADD_ON = ". Semester";
	private final int ANSICHTS_ZEILEN_HOEHE = 45;
	/**
	 * zentrale Aufgabenverteiler, der durch Events in der Gui angestossen wird und auf der anderen Seite Gui ueber Aenderungen informiert
	 */
	private KalenderDirektable kalDirektor;
	/**
	 * Liste der Checkboxen im AnsichtsoptionsPanel
	 */
	private List<JCheckBox> checkBoxList;
	/**
	 * Liste von Images die ImageIcons erzeugen
	 */
	private List<Image> colorImages;
	
	private Image one;
	private Image two;
	private Image three;
	private Image four;
	private Image five;
	private Image six;
	private Image seven;
	private Image eight;

	/**
	 * 
	 * @param semesterlist Liste der Semester
	 * @param kalD KalenderDirektor
	 */
	public AnsichtsoptionsPanel(List<Integer> semesterlist, KalenderDirektable kalD){
		
		this.colorImages = new ArrayList<Image>();
		this.kalDirektor = kalD;
		this.checkBoxList = new ArrayList<JCheckBox>();
		
		Font f = new Font(Font.DIALOG, Font.PLAIN, 12);
		Border border = BorderFactory.createTitledBorder("Ansicht aendern");
		setBorder(border);
		
		setBackground(Color.WHITE);
		
		// versuche Images aus dem Resource-Ordner "images/.."  zu laden
		try{
			one = ImageIO.read(new File("images/ansichtIcons/blue.png"));
			two = ImageIO.read(new File("images/ansichtIcons/green.png"));
			three = ImageIO.read(new File("images/ansichtIcons/orange.png"));
			four = ImageIO.read(new File("images/ansichtIcons/pink.png"));
			five = ImageIO.read(new File("images/ansichtIcons/lila.png"));
			six = ImageIO.read(new File("images/ansichtIcons/choco.png"));
			seven = ImageIO.read(new File("images/ansichtIcons/greengray.png"));
			eight = ImageIO.read(new File("images/ansichtIcons/darkblue.png"));
			
			colorImages.add(one);
			colorImages.add(two);
			colorImages.add(three);
			colorImages.add(four);
			colorImages.add(five);
			colorImages.add(six);
			colorImages.add(seven);
			colorImages.add(eight);
			
		} catch (IOException e){
			e.printStackTrace();
		}
		
		
		// dynamisches Erstellen eines 2-SpalteLayout fuer die Ansichtsoptions-Einstellungen
		this.semesterList = semesterlist; 
		int rows = (this.semesterList.size() % 2 == 0) ? this.semesterList.size()/2 : this.semesterList.size()/2 + 1; // Anzahl zeilen im Layout
		int hoehe = 30;
		int count = 0;
		JCheckBox check = null;
		JLabel label = null;
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < 4; j++){
				if (j == 0){
					check = new JCheckBox(semesterList.get(count) + ADD_ON);
					check.setBackground(Color.WHITE);
					check.addItemListener(this);
					check.setFont(f);
					check.setSelected(true);
					check.setBounds(20, 20+i*hoehe, 120, hoehe);
					add(check);
					checkBoxList.add(check);
				} else if (j == 1){
					label = new JLabel(new ImageIcon(colorImages.get(count)));
					label.setPreferredSize(new Dimension(10,10));
					label.setBounds(130, 20+i*hoehe, 10, hoehe);
					add(label);
					count += 1;
				} else if (j == 2){
					check = new JCheckBox(semesterList.get(count) + ADD_ON);
					check.setBackground(Color.WHITE);
					check.addItemListener(this);
					check.setFont(f);
					check.setSelected(true);
					check.setBounds(180, 20+i*hoehe, 120, hoehe);
					add(check);
					checkBoxList.add(check);
				} else if (j == 3){
					label = new JLabel(new ImageIcon(colorImages.get(count)));
					label.setPreferredSize(new Dimension(10,10));
					label.setBounds(290, 20+i*hoehe, 10, hoehe);
					add(label);
					count += 1;
				}
			}
		}
		
		setLayout(null);
		setPreferredSize(new Dimension(340, rows * ANSICHTS_ZEILEN_HOEHE)); // eine Zeile besitzt eine Hoehe von 45 Pixel
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		int statechanged = e.getStateChange();
		String source = e.paramString();
		// wenn CheckBox deselektiert wurde
		if (ItemEvent.DESELECTED == statechanged){
			for (int i = 0; i < this.semesterList.size(); i++){
				if (source.contains(this.semesterList.get(i)+ADD_ON)){
					// sage KalenderDirektor, dass er in der Logik die akutell ausgewaehlten Semester aktualisieren soll
					((KalenderDirektor)kalDirektor).updateSelektierteSemester(false, this.semesterList.get(i));
				}
			}
			// wenn CheckBox selektiert wurde
		} else if (ItemEvent.SELECTED == statechanged){
			for (int i = 0; i < this.semesterList.size(); i++){
				if (source.contains(this.semesterList.get(i)+ADD_ON)){
					// sage KalenderDirektor, dass er in der Logik die akutell ausgewaehlten Semester aktualisieren soll
					((KalenderDirektor)kalDirektor).updateSelektierteSemester(true, this.semesterList.get(i));
				}
			}
		}
	}

	/**
	 * update(Observer observ, Object o) - bekommt von Observable Benachrichtung zur Aktualisierung
	 */
	public void update(Observable observa, Object o) {
		if (o instanceof Integer){
			int semester = (Integer) o;
			for (int j = 0; j < semesterList.size(); j++){
				if (semesterList.get(j) == semester){
					checkBoxList.get(j).setSelected(true);
				}
			}
			
		}
	}
		
	

}
