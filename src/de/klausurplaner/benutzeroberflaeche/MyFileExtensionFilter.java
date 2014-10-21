package de.klausurplaner.benutzeroberflaeche;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Klasse, in welcher ein Filter fuer einen FileChooser erzeugt wird, mit welchem geprueft wird, welche Dateiformate zulaessig sind 
 * @author Michael Sandritter, Benjamin Christiani, Merle Hiort
 *
 */
public class MyFileExtensionFilter extends FileFilter{

	/**
	 * Array aus zugelassenen Formaten (z.B. {"xml", "txt",...})
	 */
	private String [] formats;
	
	/**
	 * Konstruktor fuer den MyFileExtensionFilter
	 * @param formats Array aus zugelassenen Formaten
	 */
	public MyFileExtensionFilter(String [] formats){
		this.formats = formats; 
	}
	
	
	@Override
	/**
	 * Methode, in welcher ueberfrueft wird, ob das Format einer Datei zu den zugelassenen Formaten gehoert
	 */
	public boolean accept(File f) {
		if (f.isDirectory()) {
	        return true;
	    }
		
		String dateiname = f.getName();
		for (int i = 0; i<formats.length; i++){
			if(dateiname.endsWith(formats[i])){ //wenn Format in Vorgabewerten enthalten
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return null;
	}

	public String[] getFormats() {
		return formats;
	}

	public void setFormats(String[] formats) {
		this.formats = formats;
	}
}
