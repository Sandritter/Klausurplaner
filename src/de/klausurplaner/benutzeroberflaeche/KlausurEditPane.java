package de.klausurplaner.benutzeroberflaeche;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.toedter.calendar.JDateChooser;

import de.klausurplaner.controller.builder.TagBuildable;
import de.klausurplaner.controller.direktoren.KalenderDirektable;
import de.klausurplaner.controller.direktoren.KalenderDirektor;
import de.klausurplaner.controller.objekte.Aufsichtsperson;
import de.klausurplaner.controller.objekte.Klausur;
import de.klausurplaner.controller.objekte.Raum;
import de.klausurplaner.controller.objekte.Tag;
import de.klausurplaner.helper.Helper;

/**
 * bei Doppelklick auf die KlausurComponent oeffnet sich diese Pane
 * dient zum Editieren der Klausur nachdem diese gesetzt wurde
 * 
 * @author Benjamin Furted, Michael Sandritter, Merle Hiort
 *
 */
public class KlausurEditPane extends JOptionPane {

	private static final long serialVersionUID = 5294243939142044557L;
	private String klausurName;
	private JLabel tagLabel;
	private JLabel uhrzeitLabel;
	private JLabel dauerLabel;
	private JLabel teilnehmerLabel;
	private JLabel raumListLabel;
	private JLabel aufListLabel;
	private JLabel kategorieLabel;
	private JDateChooser calendar;
	private JSpinner uhrzeit;
	private JSpinner dauer;
	private JSpinner teilnehmer;
	private JSpinner kategorie;
	private JList raeumeList;
	private JScrollPane raeumeListScroller;
	private JList aufsichtList;
	private JScrollPane aufsichtListScroller;
	private KalenderDirektable kalDirektor;
	private TagBuildable tagBuilder;
	private Klausur klausur;
	private Tag tag;
	private int ok;

	/**
	 * 
	 * @param k KLausur 
	 * @param t Tag an dem Klausur bis Instanziierung stattfindet
	 * @param kalD KalenderDirektor
	 */
	public KlausurEditPane(Klausur k, Tag t, KalenderDirektable kalD) {
		this.kalDirektor = kalD;
		this.tagBuilder = kalDirektor.getTagBuilder();
		this.klausurName = k.getKlausurname() + " - " + k.getSemester() + ". Semester";
		this.tagLabel = new JLabel("Datum");
		this.uhrzeitLabel = new JLabel("Startzeit");
		this.dauerLabel = new JLabel("Dauer");
		this.teilnehmerLabel = new JLabel("Teilnehmer");
		this.raumListLabel = new JLabel("Raeume");
		this.aufListLabel = new JLabel("Aufsicht");
		this.kategorieLabel = new JLabel("Kategorie");
		this.klausur = k;
		this.tag = t;
		this.calendar = new JDateChooser();
		calendar.setDate(tag.getDatum());
		calendar.setSelectableDateRange(tagBuilder.ersterTag(),tagBuilder.letzterTag());

		this.uhrzeit = createTimeSpinner(k.getStartZeit());
		SpinnerNumberModel numberModel1 = new SpinnerNumberModel();
		this.dauer = new JSpinner(numberModel1);
		dauer.setValue(klausur.getDauer());
		SpinnerNumberModel numberModel2 = new SpinnerNumberModel();
		this.teilnehmer = new JSpinner(numberModel2);
		teilnehmer.setValue(klausur.getTeilnehmer());
		SpinnerNumberModel numberModel3 = new SpinnerNumberModel();

		this.kategorie = new JSpinner(numberModel3);
		kategorie.setValue(klausur.getKategorie());
		DefaultListModel listModel = new DefaultListModel();
		// Fuege alle zur gleichen Zeit noch nicht belegten Raeume zur Liste hinzu
		List<Raum> freieRaeume = ((KalenderDirektor) kalDirektor).getFreieRaeume(tag.getDatum(), (Date) uhrzeit.getValue(),(Integer) dauer.getValue());
		List<Integer> index = new ArrayList<Integer>();
		for (int i = 0; i < freieRaeume.size(); i++) {
			if (klausur.getRaeume().size() > 0) {
				for (Raum raum : klausur.getRaeume()) {
					if (freieRaeume.get(i).equals(raum)) {
						index.add(i);
					}
				}
			}
			listModel.addElement(freieRaeume.get(i));
		}
		// setze alle vorher schon ausgewaehlten Raeume
		this.raeumeList = new JList(listModel);
		if (index.size() > 0) {
			for (int j = 0; j < index.size(); j++) {
				raeumeList.setSelectedIndex(index.get(j));
			}
		}
		this.raeumeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.raeumeList.setLayoutOrientation(JList.VERTICAL);
		this.raeumeList.setVisibleRowCount(2);
		raeumeListScroller = new JScrollPane(raeumeList);
		raeumeListScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		raeumeListScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		DefaultListModel listModel2 = new DefaultListModel();
		// Fuege alle zur gleichen Zeit noch nicht belegten Aufsichten zur Liste hinzu
		List<Aufsichtsperson> freieAufsichten = ((KalenderDirektor) kalDirektor).getFreieAufsichten(tag.getDatum(), (Date) uhrzeit.getValue(),(Integer) dauer.getValue());
		index.clear();
		for (int i = 0; i < freieAufsichten.size(); i++) {
			if (klausur.getAufsichtspersonen().size() > 0) {
				for (Aufsichtsperson aufs : (ArrayList<Aufsichtsperson>) klausur
						.getAufsichtspersonen()) {
					if (freieAufsichten.get(i).equals(aufs)) {
						index.add(i);
					}
				}
			}
			listModel2.addElement(freieAufsichten.get(i));
		}
		// setze vorher schon ausgewaehlte Aufsichten
		this.aufsichtList = new JList(listModel2);
		if (index.size() > 0) {
			for (int j = 0; j < index.size(); j++) {
				aufsichtList.setSelectedIndex(index.get(j));
			}
		}
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
		params.add(klausurName);
		params.add(tagLabel);
		params.add(calendar);
		params.add(uhrzeitLabel);
		params.add(uhrzeit);
		params.add(dauerLabel);
		params.add(dauer);
		params.add(teilnehmerLabel);
		params.add(teilnehmer);
		params.add(kategorieLabel);
		params.add(kategorie);
		params.add(raumListLabel);
		params.add(raeumeListScroller);
		params.add(aufListLabel);
		params.add(aufsichtListScroller);
		ok = JOptionPane.showConfirmDialog(this, params.toArray(),"Klausur-Termin", JOptionPane.OK_CANCEL_OPTION);
	}

	/**
	 * Methode um die ItemListeners an ComboBoxen etc anzumelden
	 */
	private void addItemListeners() {
		calendar.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				JDateChooser eo = (JDateChooser) e.getSource();
				if (!Helper.isInKlausurPhase(eo.getDate(), tag.getDatum())) {
					tag = tagBuilder.getTagByDatum2(calendar.getDate());
					// setze neue verfuegbare Aufsichtspersonen
					DefaultListModel listModel2 = new DefaultListModel();
					// Fuege alle zur gleichen Zeit noch nicht belegten Raeume zur Liste hinzu
					List<Aufsichtsperson> freieAufsichten = ((KalenderDirektor) kalDirektor).getFreieAufsichten(eo.getDate(),(Date) uhrzeit.getValue(),(Integer) dauer.getValue());
					for (Aufsichtsperson a : freieAufsichten) {
						listModel2.addElement(a);
					}
					aufsichtList.setModel(listModel2);
					// setze neue verfuegbare Raeume
					DefaultListModel listModel = new DefaultListModel();
					// Fuege alle zur gleichen Zeit noch nicht belegten Raeume
					// zur Liste hinzu
					List<Raum> freieRaeume = ((KalenderDirektor) kalDirektor).getFreieRaeume(eo.getDate(),(Date) uhrzeit.getValue(),(Integer) dauer.getValue());
					for (Raum r : freieRaeume) {
						listModel.addElement(r);
					}
					raeumeList.setModel(listModel);
				}
			}
		});
		uhrzeit.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner eo = (JSpinner) e.getSource();
				// setze neue verfuegbare Aufsichtspersonen
				DefaultListModel listModel2 = new DefaultListModel();
				// Fuege alle zur gleichen Zeit noch nicht belegten Raeume zur Liste hinzu
				List<Aufsichtsperson> freieAufsichten = ((KalenderDirektor) kalDirektor).getFreieAufsichten(tag.getDatum(),(Date) eo.getValue(),(Integer) dauer.getValue());
				for (Aufsichtsperson a : freieAufsichten) {
					listModel2.addElement(a);
				}
				aufsichtList.setModel(listModel2);
				// setze neue verfuegbare Raeume
				DefaultListModel listModel = new DefaultListModel();
				// Fuege alle zur gleichen Zeit noch nicht belegten Raeume zur Liste hinzu
				List<Raum> freieRaeume = ((KalenderDirektor) kalDirektor).getFreieRaeume(tag.getDatum(), (Date) eo.getValue(), (Integer) dauer.getValue());
				for (Raum r : freieRaeume) {
					listModel.addElement(r);
				}
				raeumeList.setModel(listModel);
			}
		});
		dauer.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner eo = (JSpinner) e.getSource();
				// setze neue verfuegbare Aufsichtspersonen
				DefaultListModel listModel2 = new DefaultListModel();
				// Fuege alle zur gleichen Zeit noch nicht belegten Raeume zur Liste hinzu
				List<Aufsichtsperson> freieAufsichten = ((KalenderDirektor) kalDirektor).getFreieAufsichten(tag.getDatum(),(Date) uhrzeit.getValue(),(Integer) eo.getValue());
				for (Aufsichtsperson a : freieAufsichten) {
					listModel2.addElement(a);
				}
				aufsichtList.setModel(listModel2);
				// setze neue verfuegbare Raeume
				DefaultListModel listModel = new DefaultListModel();
				// Fuege alle zur gleichen Zeit noch nicht belegten Raeume zur Liste hinzu
				List<Raum> freieRaeume = ((KalenderDirektor) kalDirektor).getFreieRaeume(tag.getDatum(),(Date) uhrzeit.getValue(), (Integer) eo.getValue());
				for (Raum r : freieRaeume) {
					listModel.addElement(r);
				}
				raeumeList.setModel(listModel);
			}
		});
	}

	/**
	 * Hilfsmethode um einen TimeSpinner (Format hh:mm a) zu erzeugen
	 * 
	 * @param date
	 * @return JSpinner
	 */
	public JSpinner createTimeSpinner(Date date) {
		// //
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		SpinnerDateModel dateModel = new SpinnerDateModel(calendar.getTime(),null, null, Calendar.HOUR_OF_DAY);
		JSpinner jSpinner = new JSpinner(dateModel);
		jSpinner.setEditor(new JSpinner.DateEditor(jSpinner, "kk:mm"));
		jSpinner.setSize(20, 20);
		return jSpinner;
	}

	public String getKlausurName() {
		return klausurName;
	}

	public void setKlausurName(String klausurName) {
		this.klausurName = klausurName;
	}

	public Date getCalendar() {
		return calendar.getDate();
	}

	public void setCalendar(JDateChooser calendar) {
		this.calendar = calendar;
	}

	public Date getUhrzeit() {
		return (Date) uhrzeit.getValue();
	}

	public void setUhrzeit(JSpinner uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

	public Integer getDauer() {
		return (Integer) dauer.getValue();
	}

	public void setDauer(JSpinner dauer) {
		this.dauer = dauer;
	}

	public Integer getTeilnehmer() {
		return (Integer) teilnehmer.getValue();
	}

	public void setTeilnehmer(JSpinner teilnehmer) {
		this.teilnehmer = teilnehmer;
	}

	public JList getRaeumeList() {
		return raeumeList;
	}

	public Tag getTag() {
		return tag;
	}

	public void setRaeumeList(JList raeumeList) {
		this.raeumeList = raeumeList;
	}

	public JList getAufsichtList() {
		return aufsichtList;
	}

	public void setAufsichtList(JList aufsichtList) {
		this.aufsichtList = aufsichtList;
	}

	public int getOk() {
		return ok;
	}

	public void setOk(int ok) {
		this.ok = ok;
	}

	public int getKategorie() {
		return (Integer) this.kategorie.getValue();
	}

	public void setKategorie(JSpinner k) {
		this.kategorie = k;
	}
}
