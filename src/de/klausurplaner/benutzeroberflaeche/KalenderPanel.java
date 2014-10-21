package de.klausurplaner.benutzeroberflaeche;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import de.klausurplaner.controller.objekte.Navigation;

/**
 * 
 * @author Benjanim Inaitsirhc, Merel Troih, Macheil Rettirdnas
 *
 *KalenderPanel dient als Platzhalter fuer die TagComponents
 */
public class KalenderPanel extends JPanel implements Observer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8298377039266245121L;

	private TagComponentBuilder tagComponentBuilder;
	private UhrzeitSkalaComponent uhrzeitSkalaComponent;
	
	/**
	 * 
	 * @param tagComponentBuilder, beinhaltet alle TagComponents die einen Tag im Kalender darstellen
	 */
	public KalenderPanel(TagComponentBuilder tagComponentBuilder){
		
		this.tagComponentBuilder = tagComponentBuilder;
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.RIGHT);
		fl.setVgap(0);
		fl.setHgap(1);
		setLayout(fl);
		setPreferredSize(new Dimension(900, ((int)tagComponentBuilder.getDauer() * 80) + 40));
		setBackground(Color.WHITE);
		
		this.uhrzeitSkalaComponent = new UhrzeitSkalaComponent(tagComponentBuilder.getDauer(), tagComponentBuilder.getTagBuilder().startUhrzeit());
		add(uhrzeitSkalaComponent);
		
		//fuege TagComponents dem KalenderPanel hinzu
		for (int i = 0; i < 7; i++){
			add(this.tagComponentBuilder.getTagComponentList().get(i));
		}
	}

	@Override
	public void update(Observable observa, Object o) {
		
		if(o instanceof Navigation){
			Navigation nav = (Navigation) o;
			// navigiere eine Woche zurueck
			if (nav.isNavZurueck()){
				tagComponentBuilder.setTagComponentListZurueck(nav.getDate());
			//navigiere eine Woche vor
			} else {
				tagComponentBuilder.setTagComponentListVor(nav.getDate());
			}
			repaint();
		}
	}
}
