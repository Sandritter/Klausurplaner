package de.klausurplaner.benutzeroberflaeche;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.klausurplaner.controller.builder.KlausurBuildable;
import de.klausurplaner.controller.builder.KlausurBuilder;
import de.klausurplaner.controller.direktoren.DirektorInterface;
import de.klausurplaner.controller.direktoren.KalenderDirektable;
import de.klausurplaner.controller.direktoren.KalenderDirektor;
import de.klausurplaner.controller.objekte.Aufsichtsperson;
import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.controller.objekte.Raum;
import de.klausurplaner.controller.objekte.Tag;
import de.klausurplaner.helper.DimensionHelper;

/**
 * 
 * @author Merle Hiort, Benjamin Christiani, Michael Sandritter wird bei
 *         doppelklick auf Kalender zum hinzufuegen von Klausuren geoeffnet
 */
public class KlausurChooser extends JOptionPane {

	private static final long serialVersionUID = 3390105499906556936L;
	private JLabel semesterLabel;
	private JLabel klausurLabel;
	private JLabel teilnehmerLabel;
	private JTextArea teilnehmerLabel2;
	private JLabel dauerLabel;
	private JLabel uhrzeitLabel;
	private JLabel raumLabel;
	private JLabel aufsichtLabel;
	private JLabel kategorieLabel;
	private JComboBox semesterComboBox;
	private JComboBox klausurComboBox;
	private JSpinner uhrzeitSpinner;
	private JSpinner dauerText;
	private JSpinner teilnehmerText;
	private JList raeumeList;
	private JScrollPane raeumeListScroller;
	private JList aufsichtList;
	private JScrollPane aufsichtListScroller;
	private JSpinner kategorieSpinner;
	private JPanel teilnehmerInfo;
	@SuppressWarnings("unused")
	private int counter = 0;
	@SuppressWarnings("unused")
	private Integer actSem;
	private Tag tag;
	private KalenderDirektable kalDirektor;
	private DirektorInterface ed;
	private KlausurBuildable klausurBuilder;
	private List<Integer> semester;
	private int ok;

	/**
	 * 
	 * @param tag aktuelle Tag
	 * @param y yPos des Klicks
	 * @param kal KalenderDirektor
	 */
	public KlausurChooser(Tag tag, int y, KalenderDirektable kal) {
		this.tag = tag;
		this.kalDirektor = kal;
		this.actSem = 1;
		this.ed = ((KalenderDirektor) kal).getEinDirektorInterface();
		this.klausurBuilder = (KlausurBuildable) ed.getBuilder(new KlausurBuilder());
		this.semesterLabel = new JLabel("Semester");
		this.klausurLabel = new JLabel("Klausur");
		
		// TEILNEHMER INFO
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		this.teilnehmerInfo = new JPanel(fl);
		Image infoImage = null;
		try {
			infoImage = ImageIO.read(new File(
					"images/hinweisIcons/infoIcon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JLabel infoIcon = new JLabel();
		infoIcon.setIcon(new ImageIcon(infoImage));
		infoIcon.setToolTipText("Hinweis: Bei FG und MP bitte nur Anzahl der Teilnehmer pro Pruefungstermin!");
		this.teilnehmerLabel = new JLabel("Teilnehmeranzahl");
		teilnehmerInfo.add(teilnehmerLabel);
		teilnehmerInfo.add(infoIcon);
		this.kategorieLabel = new JLabel("Kategorie");
		this.uhrzeitLabel = new JLabel("Startuhrzeit");
		this.dauerLabel = new JLabel("Dauer (Minuten)");
		this.raumLabel = new JLabel("Raeume (Mehrfachauswahl moeglich)");
		this.aufsichtLabel = new JLabel("Aufsichtspersonen (Mehrfachauswahl moeglich)");
		this.semester = new ArrayList<Integer>();
		this.semesterComboBox = new JComboBox();
		semesterComboBox.setPreferredSize(new Dimension(120, 20));
		this.klausurComboBox = new JComboBox();
		klausurComboBox.setPreferredSize(new Dimension(120, 20));
		
		for (Integer sem : klausurBuilder.getSemesterMap().keySet()) {
			boolean flag = false;
			for (Klausur k : klausurBuilder.getSemesterMap().get(sem)) {
				// wenn alle Klausuren des Semester schon angezeigt wurden, wird das Semester nicht mehr angezeigt
				if (!k.isGesetzt() || k.isMehrtaegig()) {
					flag = true;
					break;
				}
			}
			if (flag) {
				semesterComboBox.addItem(sem);
				semester.add(sem);
			}
		}
		// setze Klausuren des Semesters
		for (Klausur k : klausurBuilder.getSemesterMap().get(semester.get(0))) {
			// fuege nur Klausuren der ComboBox die noch nicht gesetzt wurden oder mehrtaetig gesetzt werden koennen
			if (k.isMehrtaegig() || !k.isGesetzt()) {
				klausurComboBox.addItem(k.getKlausurname());
			}
		}
		SpinnerNumberModel numberModel = new SpinnerNumberModel();
		this.dauerText = new JSpinner(numberModel);
		dauerText.setValue(klausurBuilder.getSemesterMap().get(semester.get(0)).get(0).getDauer());
		// hier wird der Auswahlkomponente der Startuhrzeit die aus den Pixeln berechnete Uhrzeit mitgegeben
		this.uhrzeitSpinner = createTimeSpinnerAM(DimensionHelper.berechneTime(y, kalDirektor.getPreferenzen().getRunde_startzeit(), ((KalenderDirektor) kalDirektor).getTagBuilder().startUhrzeit()));
		SpinnerNumberModel numberModel2 = new SpinnerNumberModel();
		this.teilnehmerText = new JSpinner(numberModel2);
		teilnehmerText.setValue(klausurBuilder.getSemesterMap().get(semester.get(0)).get(0).getTeilnehmer());

		SpinnerNumberModel numberModel3 = new SpinnerNumberModel();

		this.kategorieSpinner = new JSpinner(numberModel3);
		kategorieSpinner.setValue(klausurBuilder.getKlausurByName((String) klausurComboBox.getSelectedItem()).getKategorie());
		DefaultListModel listModel = new DefaultListModel();
		
		// Fuege alle zur gleichen Zeit noch nicht belegten Raeume zur Liste hinzu
		List<Raum> freieRaeume = ((KalenderDirektor) kalDirektor).getFreieRaeume(tag.getDatum(),(Date) uhrzeitSpinner.getValue(),(Integer) dauerText.getValue());
		for (Raum r : freieRaeume) {
			listModel.addElement(r);
		}
		this.raeumeList = new JList(listModel);
		this.raeumeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.raeumeList.setLayoutOrientation(JList.VERTICAL);
		this.raeumeList.setVisibleRowCount(2);
		raeumeListScroller = new JScrollPane(raeumeList);
		raeumeListScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		raeumeListScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		DefaultListModel listModel2 = new DefaultListModel();
		
		// Fuege alle zur gleichen Zeit noch nicht belegten Raeume zur Listes hinzu
		List<Aufsichtsperson> freieAufsichten = ((KalenderDirektor) kalDirektor)
				.getFreieAufsichten(tag.getDatum(),
						(Date) uhrzeitSpinner.getValue(),
						(Integer) dauerText.getValue());
		for (Aufsichtsperson a : freieAufsichten) {
			listModel2.addElement(a);
		}
		this.aufsichtList = new JList(listModel2);
		this.aufsichtList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.aufsichtList.setLayoutOrientation(JList.VERTICAL);
		this.aufsichtList.setVisibleRowCount(2);
		aufsichtListScroller = new JScrollPane(aufsichtList);
		aufsichtListScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		aufsichtListScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		addItemListeners();
		fillOptionPane();
	}

	/**
	 * Methode, um das OptionPane mit den Komponenten zu fuellen und anzuzeigen
	 */
	public void fillOptionPane() {
		List<Object> params = new ArrayList<Object>();
		params.add(semesterLabel);
		params.add(semesterComboBox);
		params.add(klausurLabel);
		params.add(klausurComboBox);
		params.add(uhrzeitLabel);
		params.add(uhrzeitSpinner);
		params.add(dauerLabel);
		params.add(dauerText);
		params.add(teilnehmerInfo);
		params.add(teilnehmerLabel2);
		params.add(teilnehmerText);
		params.add(kategorieLabel);
		params.add(kategorieSpinner);
		params.add(raumLabel);
		params.add(raeumeListScroller);
		params.add(aufsichtLabel);
		params.add(aufsichtListScroller);
		ok = JOptionPane.showConfirmDialog(this, params.toArray(),
				"Klausur-Termin", JOptionPane.OK_CANCEL_OPTION);
	}

	/**
	 * Methode um die ItemListeners an ComboBoxen etc anzumelden
	 */
	private void addItemListeners() {
		semesterComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				ItemSelectable is = e.getItemSelectable();
				// wenn ein Semester ausgewaehlt wurde
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Integer sem = selectedInteger(is);
					actSem = sem;
					klausurComboBox.removeAllItems();
					// dann passe die KlausurListe mit entsprecheden Klausuren des Semesters an
					for (Klausur k : klausurBuilder.getKlausurBySemeser(sem)) { 
						if (!k.isGesetzt()) {
							klausurComboBox.addItem(k.getKlausurname());
						}
					}
				}
			}
		});
		klausurComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				ItemSelectable is = e.getItemSelectable();
				// wenn eine Klausur ausgewaehlt wurde
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// setze die Daten der Klausur in den restlichen Eingabefeldern
					String selected = selectedString(is);
					Klausur k = klausurBuilder.getKlausurByName(selected);
					dauerText.setValue(k.getDauer());
					teilnehmerText.setValue(k.getTeilnehmer());
					kategorieSpinner.setValue(k.getKategorie());
				}
			}
		});
		uhrzeitSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner eo = (JSpinner) e.getSource();
				// setze neue verfuegbare Aufsichtspersonen
				DefaultListModel listModel2 = new DefaultListModel();
				// Fuege alle zur gleichen Zeit noch nicht belegten Raeume zur Liste hinzu
				List<Aufsichtsperson> freieAufsichten = ((KalenderDirektor) kalDirektor).getFreieAufsichten(tag.getDatum(),(Date) eo.getValue(),(Integer) dauerText.getValue());
				for (Aufsichtsperson a : freieAufsichten) {
					listModel2.addElement(a);
				}
				aufsichtList.setModel(listModel2);
				// setze neue verfuegbare Raeume
				DefaultListModel listModel = new DefaultListModel();
				// Fuege alle zur gleichen Zeit noch nicht belegten Raeume zur Liste hinzu
				List<Raum> freieRaeume = ((KalenderDirektor) kalDirektor).getFreieRaeume(tag.getDatum(), (Date) eo.getValue(),(Integer) dauerText.getValue());
				for (Raum r : freieRaeume) {
					listModel.addElement(r);
				}
				raeumeList.setModel(listModel);
			}
		});
		dauerText.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner eo = (JSpinner) e.getSource();
				// setze neue verfuegbare Aufsichtspersonen
				DefaultListModel listModel2 = new DefaultListModel();
				// Fuege alle zur gleichen Zeit noch nicht belegten Raeume zur Liste hinzu
				List<Aufsichtsperson> freieAufsichten = ((KalenderDirektor) kalDirektor).getFreieAufsichten(tag.getDatum(),(Date) uhrzeitSpinner.getValue(),(Integer) eo.getValue());
				for (Aufsichtsperson a : freieAufsichten) {
					listModel2.addElement(a);
				}
				aufsichtList.setModel(listModel2);
				// setze neue verfuegbare Raeume
				DefaultListModel listModel = new DefaultListModel();
				// Fuege alle zur gleichen Zeit noch nicht belegten Raeume zur Liste hinzu
				List<Raum> freieRaeume = ((KalenderDirektor) kalDirektor).getFreieRaeume(tag.getDatum(),(Date) uhrzeitSpinner.getValue(),(Integer) eo.getValue());
				for (Raum r : freieRaeume) {
					listModel.addElement(r);
				}
				raeumeList.setModel(listModel);
			}
		});
	}

	/**
	 * Quasi eine toString()-Methode
	 * 
	 * @param is
	 * @return String des selktiereten Items
	 */
	private static String selectedString(ItemSelectable is) {
		Object selected[] = is.getSelectedObjects();
		return ((selected.length == 0) ? "null" : (String) selected[0]);
	}

	private static Integer selectedInteger(ItemSelectable is) {
		Object selected[] = is.getSelectedObjects();
		return (Integer) ((selected.length == 0) ? "null"
				: (Integer) selected[0]);
	}

	/**
	 * Hilfsmethode um einen TimeSpinner (Format hh:mm a) zu erzeugen
	 * 
	 * @param date
	 * @return JSpinner
	 */
	public JSpinner createTimeSpinnerAM(Date date) {
		// //
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		SpinnerDateModel dateModel = new SpinnerDateModel(calendar.getTime(),null, null, Calendar.HOUR_OF_DAY);
		JSpinner jSpinner = new JSpinner(dateModel);
		jSpinner.setEditor(new JSpinner.DateEditor(jSpinner, "kk:mm"));
		jSpinner.setSize(20, 20);
		return jSpinner;
	}

	public int getOk() {
		return ok;
	}

	public JLabel getSemesterLabel() {
		return semesterLabel;
	}

	public void setSemesterLabel(JLabel semesterLabel) {
		this.semesterLabel = semesterLabel;
	}

	public JLabel getKlausurLabel() {
		return klausurLabel;
	}

	public void setKlausurLabel(JLabel klausurLabel) {
		this.klausurLabel = klausurLabel;
	}

	public JLabel getTeilnehmerLabel() {
		return teilnehmerLabel;
	}

	public void setTeilnehmerLabel(JLabel teilnehmerLabel) {
		this.teilnehmerLabel = teilnehmerLabel;
	}

	public JLabel getDauerLabel() {
		return dauerLabel;
	}

	public void setDauerLabel(JLabel dauerLabel) {
		this.dauerLabel = dauerLabel;
	}

	public JLabel getUhrzeitLabel() {
		return uhrzeitLabel;
	}

	public void setUhrzeitLabel(JLabel uhrzeitLabel) {
		this.uhrzeitLabel = uhrzeitLabel;
	}

	public JLabel getRaumLabel() {
		return raumLabel;
	}

	public void setRaumLabel(JLabel raumLabel) {
		this.raumLabel = raumLabel;
	}

	public JLabel getAufsichtLabel() {
		return aufsichtLabel;
	}

	public void setAufsichtLabel(JLabel aufsichtLabel) {
		this.aufsichtLabel = aufsichtLabel;
	}

	public JComboBox getSemesterComboBox() {
		return semesterComboBox;
	}

	public void setSemesterComboBox(JComboBox semesterComboBox) {
		this.semesterComboBox = semesterComboBox;
	}

	public JComboBox getKlausurComboBox() {
		return klausurComboBox;
	}

	public void setKlausurComboBox(JComboBox klausurComboBox) {
		this.klausurComboBox = klausurComboBox;
	}

	public Date getUhrzeitSpinner() {
		return (Date) uhrzeitSpinner.getValue();
	}

	public void setUhrzeitSpinner(JSpinner uhrzeitSpinner) {
		this.uhrzeitSpinner = uhrzeitSpinner;
	}

	public Integer getDauerText() {
		return (Integer) dauerText.getValue();
	}

	public void setDauerText(JSpinner dauerText) {
		this.dauerText = dauerText;
	}

	public Integer getTeilnehmerText() {
		return (Integer) teilnehmerText.getValue();
	}

	public void setTeilnehmerText(JSpinner teilnehmerText) {
		this.teilnehmerText = teilnehmerText;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public List<Integer> getSemester() {
		return semester;
	}

	public void setSemester(List<Integer> semester) {
		this.semester = semester;
	}

	public void setOk(int ok) {
		this.ok = ok;
	}

	public JList getRaeumeList() {
		return raeumeList;
	}

	public JList getAufsichtList() {
		return aufsichtList;
	}

	public int getKategorieSpinner() {
		return (Integer) kategorieSpinner.getValue();
	}

	public void setKategorieSpinner(JSpinner k) {
		this.kategorieSpinner = k;
	}
}
