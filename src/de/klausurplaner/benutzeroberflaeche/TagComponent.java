package de.klausurplaner.benutzeroberflaeche;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import de.klausurplaner.controller.direktoren.KalenderDirektable;
import de.klausurplaner.controller.objekte.Aenderung;
import de.klausurplaner.controller.objekte.Aufsichtsperson;
import de.klausurplaner.controller.objekte.Einstellung;
import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.controller.objekte.KlausurSetzer;
import de.klausurplaner.controller.objekte.Muellabfuhr;
import de.klausurplaner.controller.objekte.Raum;
import de.klausurplaner.controller.objekte.Tag;
import de.klausurplaner.helper.DimensionHelper;
import de.klausurplaner.helper.Helper;

/**
 * 
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter 
 * Gui-Komponente, die im KalenderPanel einen Tag darstellt
 */
public class TagComponent extends JComponent implements Observer, MouseListener, DragGestureListener {

	private Tag tag;
	private int width;
	private int height;
	private Color background;
	private final int HOUR_GAP = 80;
	private int dauer;
	private KalenderDirektable kalDirektor;
	private Einstellung einstellung;
	// protected static DataFlavor klausurFlavor = new
	// DataFlavor(KlausurComponent.class, "KlausurComponent");
	// protected static DataFlavor[] supportedFlavors = {klausurFlavor};
	private static final long serialVersionUID = -5393043228780333534L;

	/**
	 * 
	 * @param width - Breite der TagComponent
	 * @param dauerTag - Dauer der Klausur
	 * @param tag - aktueller Tag
	 * @param kalDirektor KalenderDirektor
	 */
	public TagComponent(int width, int dauerTag, Tag tag,
			KalenderDirektable kalDirektor) {
		this.kalDirektor = kalDirektor;
		this.einstellung = this.kalDirektor.getPreferenzen();
		this.dauer = dauerTag;
		this.width = width;
		this.height = dauerTag * HOUR_GAP + 40;
		this.background = Color.WHITE;
		this.tag = tag;

		new MyDropTargetListener(this);
		setLayout(null);
		setPreferredSize(new Dimension(this.width, this.height));
		addMouseListener(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		g.setColor(background);
		g.fillRect(0, 40, this.width, this.height);
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect(0, 40, this.width, this.height);
		g.drawRect(0, 0, 120, 40);
		g.setColor(Color.GRAY);
		g.setFont(font);
		g.drawString(this.tag.getKuerzel(), 10, 20);
		font = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
		g.setFont(font);
		g.drawString(this.tag.getInfoHeader(), 45, 20);
		g.setColor(new Color(240, 240, 240));
		for (int i = 0; i < this.dauer; i++) {
			g.fillRect(0, 40 + i * HOUR_GAP, this.width, 1);
		}
	}

	// WENN IN DER WOCHENANSICHT NAVIGIERT WIRD OEDER WENN NEUE KLAUSUR GESETZTWURDE
	/**
	 * setzt neuen Tag und repainted wenn in der Wochenansicht navigiert wird oder eine neue Klausur gesetzt wurde
	 * @param tag
	 */
	public void setTag(Tag tag) {
		this.tag = tag;
		repaint();
		if (tag != null && tag.getKlausuren().size() > 0) {
			removeAll();
			for (Klausur k : tag.getKlausuren()) {
				for (int semester : einstellung.getSelektierteSemester()) {
					if (semester == k.getSemester()) {
						Date tag_start = kalDirektor.getTagBuilder().startUhrzeit();
						int yPos = DimensionHelper.berechneStartPos(k.getStartZeit(), tag_start);
						KlausurComponent kc = new KlausurComponent(0, yPos,getWidth(), k, tag, kalDirektor);
						DragSource ds = new DragSource();
						ds.createDefaultDragGestureRecognizer(kc,DnDConstants.ACTION_MOVE, this);
						add(kc);
						repaint();
					}
				}
			}
		} else {
			removeAll();
			repaint();
		}
	}

	/**
	 * aktualisiert die aktuelle Wochenansicht
	 */
	public void aktualisiereAnsicht() {
		removeAll();
		for (Klausur k : tag.getKlausuren()) {
			for (int semester : einstellung.getSelektierteSemester()) {
				if (semester == k.getSemester()) {
					Date tag_start = kalDirektor.getTagBuilder().startUhrzeit();
					int yPos = DimensionHelper.berechneStartPos(k.getStartZeit(), tag_start);
					KlausurComponent kc = new KlausurComponent(0, yPos,getWidth(), k, tag, kalDirektor);
					add(kc);
					repaint();
				}
			}
		}
		repaint();
	}

	// UPDATE METHODE DES OBSERVERS
	@Override
	public void update(Observable observa, Object o) {
		if (o instanceof Tag) {
			Tag tag = (Tag) o;
			if (Helper.isGleichesDatum(tag.getDatum(), this.tag.getDatum())) {
				setTag(tag);
			}
		} else if (o instanceof Aenderung) {
			Aenderung a = (Aenderung) o;
			if (Helper.isInKlausurPhase(a.getNeuerTag().getDatum(),
					this.tag.getDatum())
					|| Helper.isInKlausurPhase(a.getAlterTag().getDatum(),
							this.tag.getDatum())) {
				setTag(tag);
			}
		} else if (o instanceof Muellabfuhr) {
			Muellabfuhr m = (Muellabfuhr) o;
			if (Helper.isInKlausurPhase(m.getTag().getDatum(),
					this.tag.getDatum())) {
				setTag(m.getTag());
			}
		} else if (o instanceof String) {
			String s = (String) o;
			if (s.compareTo("ansichtChanged") == 0) {
				aktualisiereAnsicht();
			}
		}
	}

	// GUCKSTE WENN DU KLICKST
	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getClickCount() > 1 && !(tag.getKuerzel().compareTo("") == 0)) {
			KlausurSetzer ks = new KlausurSetzer();
			KlausurChooser klausurChooser = new KlausurChooser(this.tag,
					e.getY(), kalDirektor);
			add(klausurChooser);
			boolean valideZurKlausur = false;
			boolean valideZuEinstellungen = false;
			kalDirektor.getValidator().resetIgnorieren();
			do {
				// wenn Abbrechen geklickt
				if (klausurChooser.getOk() == 2) {
					// mache nichts
					return;
					// wenn klausurChooser bestaetigt
				} else if (klausurChooser.getOk() == 0) {
					// beziehe Informationen aus KlausurChooser
					int semester = (Integer) klausurChooser.getSemesterComboBox().getSelectedItem();
					String klausurName = (String) klausurChooser.getKlausurComboBox().getSelectedItem();
					Date startzeit = klausurChooser.getUhrzeitSpinner();
					int dauer = klausurChooser.getDauerText();
					int teilnehmerAnzahl = klausurChooser.getTeilnehmerText();
					int kategorie = klausurChooser.getKategorieSpinner();
					List<Raum> raeume = new ArrayList<Raum>();
					for (Object o : klausurChooser.getRaeumeList().getSelectedValues()) {
						raeume.add((Raum) o);
					}
					List<Aufsichtsperson> aufsPersonen = new ArrayList<Aufsichtsperson>();
					for (Object o : klausurChooser.getAufsichtList().getSelectedValues()) {
						aufsPersonen.add((Aufsichtsperson) o);
					}
					// sende Werte an KlausurSetzer
					ks.init(this.tag, semester, klausurName, startzeit,teilnehmerAnzahl, dauer, raeume, aufsPersonen,kategorie);
					// validiere KlausurSetzer
					String fehler = kalDirektor.getValidator().validateKlausurSetzer(ks);
					if (fehler.compareTo("ok") != 0) {
						JOptionPane.showMessageDialog(this, fehler,"Fehlerhafte Eingabe",JOptionPane.ERROR_MESSAGE);
						klausurChooser.fillOptionPane();
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
							int ret = JOptionPane.showOptionDialog(this,hinweisNachricht, "Fehlerhafte Eingabe",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, options,options[2]);
							if (ret == 0) { // Werte aendern
								klausurChooser.fillOptionPane();
								valideZurKlausur = false;
							} else if (ret == 1) { // Ignorieren
								kalDirektor.getValidator()
										.setZuIgnorierendenHinweis(hinweis);
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
			kalDirektor.setzeTermin(ks);
		}
	}

	public void mouseExited(MouseEvent e) {
		background = Color.WHITE;
		repaint();
	}

	public void mouseEntered(MouseEvent e) {
		// background = new Color(191,239,255);
		background = new Color(208, 249, 255);
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	public Tag getTag() {
		return tag;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getDauer() {
		return dauer;
	}

	public void setDauer(int dauer) {
		this.dauer = dauer;
	}

	public int getHOUR_GAP() {
		return HOUR_GAP;
	}

	//TODO Methode muss noch implementiert werden
	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		Cursor cursor = null;
		KlausurComponent klausurComponent = (KlausurComponent) dge.getComponent();
		if (dge.getDragAction() == DnDConstants.ACTION_MOVE) {
			cursor = DragSource.DefaultMoveDrop;
		}
		dge.startDrag(cursor, new KlausurComponent(klausurComponent.getKlausur()));
	}

	//TODO Klasse muss noch implementiert werden
	class MyDropTargetListener extends DropTargetAdapter {
		@SuppressWarnings("unused")
		private DropTarget dropTarget;
		private JComponent tagComponent;

		public MyDropTargetListener(JComponent tagComponent) {
			this.tagComponent = tagComponent;
			dropTarget = new DropTarget(tagComponent, DnDConstants.ACTION_MOVE, this, true, null);
			new DropTarget();
		}

		@Override
		public void drop(DropTargetDropEvent event) {
			try {
				Transferable transfer = event.getTransferable();
				KlausurComponent klausurComponent = (KlausurComponent) transfer.getTransferData(KlausurComponent.klausurComponentFlavor);
				if (event.isDataFlavorSupported(KlausurComponent.klausurComponentFlavor)) {
					event.acceptDrop(DnDConstants.ACTION_MOVE);
					this.tagComponent.add(klausurComponent);
					event.dropComplete(true);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				event.rejectDrop();
			}
		}
	}

}

