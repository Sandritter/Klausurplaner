package de.klausurplaner.benutzeroberflaeche;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalToolTipUI;

import de.klausurplaner.controller.direktoren.KalenderDirektable;
import de.klausurplaner.controller.objekte.Aufsichtsperson;
import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.controller.objekte.KlausurSetzer;
import de.klausurplaner.controller.objekte.Raum;
import de.klausurplaner.controller.objekte.Tag;
import de.klausurplaner.helper.ColorHelper;
import de.klausurplaner.helper.DimensionHelper;

/**
 * 
 * @author Merle Hiort, Benjamin Christiani, Michael Sandritter zeichnet die
 *  JComponent die in der TagComponent gezeichnet wird sobald eine Klausur gesetzt wurde
 */
public class KlausurComponent extends JLayeredPane implements MouseListener, Transferable {

	private static final long serialVersionUID = 4272274489473987288L;
	
	/**
	 * DataFlavor um DragDrop zu ermoeglichen
	 */
	protected static DataFlavor klausurComponentFlavor = new DataFlavor(KlausurComponent.class, "KlausurComponent YEAH");
	protected static DataFlavor[] supportedFlavors = { klausurComponentFlavor,DataFlavor.stringFlavor, };
	
	private KlausurComponent klausurComponent; // TODO wird fuer Drag und Drop benoetigt, muss noch implementiert werden
	/**
	 * Breite in Pixel der zu zeichnenden KlausurComponente
	 */
	private int breite;
	/**
	 * Hoehe in Pixel der zu zeichnenden KlausurComponente
	 */
	private int hoehe;
	/**
	 * Name der Klausur
	 */
	private String klausurName;
	/**
	 * Klausur die visuell dargestellt werden soll
	 */
	private Klausur klausur;
	/**
	 * yPos  der KlausurComponente
	 */
	private int x;
	/**
	 * xPos der KlausurComponente
	 */
	private int y;
	/**
	 * Background-Color der KlausurComponente
	 */
	private Color bgColor;
	private Color tmp;
	/**
	 * Tag an dem die KlausurComponent gezeichnet werden soll
	 */
	private Tag tag;
	/**
	 * falls Klausur umgesetzt wird greift der neue Tag
	 */
	private Tag neuerTag;
	/**
	 * KalenderDirektor der Aufgaben von der Gui erhaelt diese bearbeitet und die Gui anschliessend benachrichtigt
	 */
	private KalenderDirektable kalDirektor;
	
	private JButton deleteButton;
	private Image delete;
	private Image hDelete;
	@SuppressWarnings("unused")
	private boolean flag;
	private Image hinweisImage;
	private JLabel hinweisIcon;
	@SuppressWarnings("unused")
	private DragSource dragSource; // TODO muss noch implementiert werden

	/**
	 * 
	 * @param k Klausur
	 */
	public KlausurComponent(Klausur k){
		setLayout(null);
	}
	
	
	/**
	 * 
	 * @param x xPos
	 * @param y yPos
	 * @param breite width
	 * @param hoehe height
	 * @param k KLausur
	 */
	public KlausurComponent(int x, int y, int breite, Klausur k, Tag actTag, KalenderDirektable kalD) {

		setLayout(null);
		this.kalDirektor = kalD;
		this.tag = actTag;
		this.neuerTag = actTag;
		this.breite = breite;
		this.hoehe = DimensionHelper.berechneHoehe(k);
		this.klausur = k;
		this.bgColor = ColorHelper.getColorBySemester(klausur.getSemester());
		this.klausurName = klausur.getKlausurname();
		this.y = y;
		this.x = x;
		this.deleteButton = new JButton();
		deleteButton.setOpaque(false);
		deleteButton.setBackground(new Color(0, 0, 0, 0));
		Border border = BorderFactory.createEmptyBorder();
		deleteButton.setBorder(border);
		this.flag = false;

		try {
			delete = ImageIO.read(new File("images/buttons/delete.png"));
			hinweisImage = ImageIO.read(new File("images/hinweisIcons/warnhinweis.png"));
			Image pDelete = ImageIO.read(new File("images/buttons/pDelete.png"));
			hDelete = ImageIO.read(new File("images/buttons/hDelete.png"));

			this.deleteButton.setIcon(new ImageIcon(delete));
			this.deleteButton.setPressedIcon(new ImageIcon(pDelete));

		} catch (IOException e) {
			e.printStackTrace();
		}
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				kalDirektor.entferneTermin(tag, klausur); // entferne Termin
			}
		});

		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				deleteButton.setIcon(new ImageIcon(hDelete));
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				deleteButton.setIcon(new ImageIcon(delete));
			}
		});

		deleteButton.setBounds(breite - 20, 0, 20, 20);
		hinweisIcon = new JLabel(new ImageIcon(hinweisImage)) {
			private static final long serialVersionUID = -7323821802904520702L;
			/**
			 * erzeuge ein mehrzeiliges ToolTip
			 */
			public JToolTip createToolTip() {
				MultiLineToolTip tip = new MultiLineToolTip();
				tip.setComponent(this);
				return tip;
			}
		};
		hinweisIcon.setBounds(breite -50, hoehe - 30, 40, 41);
		add(deleteButton);
		if (!klausur.isValide()) {hinweisIcon.setToolTipText(k.getFormatierteWarnhinweise());
			add(hinweisIcon);
		} else {
			remove(hinweisIcon);
		}
		
		setBounds(this.x, this.y - 20, this.breite + 1, this.hoehe + 20);
		addMouseListener(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		// wenn Klausur nicht valide ist 
		if (!klausur.isValide()) {
			// zeichne dich rot
			g2d.setPaint(new Color(139, 35, 35, 200));
			g2d.fillRoundRect(0, 20, this.breite, this.hoehe, 5, 20);
		} else {
			// ansonsten in deiner angestammten Farbe
			g2d.setPaint(bgColor);
			g2d.fillRoundRect(0, 20, this.breite, this.hoehe, 5, 20);
		}
		g2d.setColor(Color.WHITE);
		g2d.drawString(this.klausurName, 5, 45);
		g2d.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
		g2d.drawString(klausur.getFormatierteRaeume(), 10, hoehe+5);
		// TODO raum muss noch gezeichnet werden
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// wenn DoppelKlick auf KlausurComponent
		if (e.getClickCount() > 1) {
			KlausurSetzer ks = new KlausurSetzer();
			KlausurEditPane kep = new KlausurEditPane(klausur, tag, kalDirektor);
			kalDirektor.getValidator().resetIgnorieren();

			boolean valideZurKlausur = false;
			boolean valideZuEinstellungen = false;
			// wenn Abbrechen geklickt
			do {
				// wenn Abbrechen geklickt
				if (kep.getOk() == 2) {
					// mache nichts
					return;
					// wenn klausurChooser bestaetigt
				} else if (kep.getOk() == 0) {
					// beziehe Informationen aus KlausurChooser
					Date startzeit = kep.getUhrzeit();
					int dauer = kep.getDauer();
					int teilnehmerAnzahl = kep.getTeilnehmer();
					int kategorie = kep.getKategorie();
					neuerTag = kep.getTag();
					List<Raum> raeume = new ArrayList<Raum>();
					for (Object o : kep.getRaeumeList().getSelectedValues()) {
						raeume.add((Raum) o);
					}
					List<Aufsichtsperson> aufsPersonen = new ArrayList<Aufsichtsperson>();
					for (Object o : kep.getAufsichtList().getSelectedValues()) {
						aufsPersonen.add((Aufsichtsperson) o);
					}
					// sende Werte an KlausurSetzer
					ks.init(neuerTag, tag, klausur.getSemester(), klausurName, startzeit, teilnehmerAnzahl, dauer, raeume, aufsPersonen, kategorie);
					// validiere KlausurSetzer
					String fehler = kalDirektor.getValidator().validateKlausurSetzer(ks);
					if (fehler.compareTo("ok") != 0) {
						JOptionPane.showMessageDialog(this, fehler,"Fehlerhafte Eingabe",JOptionPane.ERROR_MESSAGE);
						kep.fillOptionPane();
						valideZurKlausur = false;
					} else {
						valideZurKlausur = true;
					}
					if (valideZurKlausur) {
						// validiere Einstellungen
						int hinweis = kalDirektor.getValidator().validateEinstellungen(ks);
						if (hinweis >= 0) {
							Object[] options = { "Werte aendern", "Ignorieren","Abbrechen" };
							String hinweisNachricht = kalDirektor.getValidator().getHinweisTexte(hinweis);
							int ret = JOptionPane.showOptionDialog(this,hinweisNachricht, "Fehlerhafte Eingabe",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options,options[2]);
							if (ret == 0) { // Werte aendern
								kep.fillOptionPane();
								valideZurKlausur = false;
							} else if (ret == 1) { // Ignorieren
								kalDirektor.getValidator().setZuIgnorierendenHinweis(hinweis);
								valideZuEinstellungen = true;
								valideZurKlausur = false;
							} else if (ret == 2) { // Abbrechen
								return;
							}
						} else {
							valideZuEinstellungen = true;
						}
					}
				}
			} while (!valideZurKlausur || !valideZuEinstellungen);
			kalDirektor.aendereTermin(ks);							// aendere Termin
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		deleteButton.setIcon(new ImageIcon(hDelete));
		tmp = bgColor;
		bgColor = new Color(12, 43, 156, 120);
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		deleteButton.setIcon(new ImageIcon(delete));
		bgColor = tmp;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * 
	 * @author Michael Sandritter, Benjamin Christiani, Merle Hiort
	 * Klasse die ein mehrzeilige ToolTip erstellt
	 */
	class MultiLineToolTip extends JToolTip {
		private static final long serialVersionUID = 4883880749440804933L;

		public MultiLineToolTip() {
			setUI(new MultiLineToolTipUI());
		}
	}

	/**
	 * 
	 * @author Michael Sandritter, Benjamin Christiani, Merle Hiort
	 * Klasse die ein mehrzeilige ToolTip erstellt
	 */
	class MultiLineToolTipUI extends MetalToolTipUI {
		private String[] strs;
		@SuppressWarnings("unused")
		private int maxWidth = 0;

		public void paint(Graphics g, JComponent c) {
			@SuppressWarnings("deprecation")
			FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(g.getFont());
			Dimension size = c.getSize();
			g.setColor(c.getBackground());
			g.fillRect(0, 0, size.width, size.height);
			g.setColor(c.getForeground());
			if (strs != null) {
				for (int i = 0; i < strs.length; i++) {
					g.drawString(strs[i], 3, (metrics.getHeight()) * (i + 1));
				}
			}
		}

		@SuppressWarnings("unchecked")
		public Dimension getPreferredSize(JComponent c) {
			@SuppressWarnings("deprecation")
			FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(c.getFont());
			String tipText = ((JToolTip) c).getTipText();
			if (tipText == null) {
				tipText = "";
			}
			BufferedReader br = new BufferedReader(new StringReader(tipText));
			String line;
			int maxWidth = 0;
			@SuppressWarnings("rawtypes")
			Vector v = new Vector();
			try {
				while ((line = br.readLine()) != null) {
					int width = SwingUtilities.computeStringWidth(metrics, line);
					maxWidth = (maxWidth < width) ? width : maxWidth;
					v.addElement(line);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			int lines = v.size();
			if (lines < 1) {
				strs = null;
				lines = 1;
			} else {
				strs = new String[lines];
				int i = 0;
				for (@SuppressWarnings("rawtypes")Enumeration e = v.elements(); e.hasMoreElements(); i++) {
					strs[i] = (String) e.nextElement();
				}
			}
			int height = metrics.getHeight() * lines;
			this.maxWidth = maxWidth;
			return new Dimension(maxWidth + 6, height + 4);
		}
	}


	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if (flavor.equals(klausurComponentFlavor) || flavor.equals(DataFlavor.stringFlavor))
			return true;
		return false;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.equals(klausurComponentFlavor)) {
			return klausurComponent;
		} else if (flavor.equals(DataFlavor.stringFlavor)) {
			return klausurComponent.toString();
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return supportedFlavors;
	}
	
	public Klausur getKlausur(){
		return klausur;
	}
	

}
