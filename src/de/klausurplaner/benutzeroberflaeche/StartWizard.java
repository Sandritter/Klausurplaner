package de.klausurplaner.benutzeroberflaeche;

import javax.swing.JFrame;

import de.klausurplaner.controller.builder.TagBuildable;
import de.klausurplaner.controller.direktoren.DirektorInterface;
import de.klausurplaner.controller.objekte.Einstellung;

/**
 * StartWizard regelt das Startfenster
 * 
 * @author Merle Hiort, Benjamin Christiani, Michael Sandritter
 *
 */
public class StartWizard extends JFrame{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -473104915700177646L;
	
	/**
	 * Startfenster, in welchem der Benutzer auswaehlt, ob er ein neues Projekt beginnen moechte
	 */
	private Startfenster startfenster;
	
	/**
	 * Einstellungsfenster, in welchem der Benutzer die initialen Einstellungen macht und die Stammdaten laedt
	 */
	private EinstellungsPanelStart einstellungsPanelStart;
	
	public StartWizard(){

	}
	
	/**
	 * Methode zum Initialisieren des StartWizard
	 * @param tagBuilder
	 * @param einleseDirektor
	 * @param einstellung
	 * @return
	 */
	public boolean init(TagBuildable tagBuilder, DirektorInterface einleseDirektor, Einstellung einstellung){
		boolean ok = false;
		
		do{
			startfenster = new Startfenster(einstellungsPanelStart, "Start", true);

			if(startfenster.getMyOptionPaneStart().getOk() == 2){ //wenn Abbrechen
				if(einstellungsPanelStart != null){
					einstellungsPanelStart.dispose();
				}
				startfenster.dispose();
				return false;
				
			}else if(startfenster.getMyOptionPaneStart().getOk() == 0 && startfenster.getMyOptionPaneStart().isWahl()){ //wenn ok und neues Projekt
				startfenster.dispose();
				einstellungsPanelStart = new EinstellungsPanelStart(this, "Einstellungen", true);
				
				boolean ok2 = false;
				
				do{
					if(einstellungsPanelStart.getMyOptionPane().getOk() == 0){
						if(einstellungsPanelStart.getMyOptionPane().validate(tagBuilder, einleseDirektor)){
							einstellungsPanelStart.getMyOptionPane().sendeDaten(tagBuilder);
							einstellungsPanelStart.getMyOptionPane().sendeDaten(einstellung);
							einstellungsPanelStart.getMyOptionPane().reset();
							einstellungsPanelStart.dispose();
							return true;
						}else{
							einstellungsPanelStart.getMyOptionPane().fillOptionPane();
							ok = false;
						}
					}else{
						ok2 = true;
					}
				}while(!ok2);
			}
		}while(!ok);
		return ok;
	}
}
