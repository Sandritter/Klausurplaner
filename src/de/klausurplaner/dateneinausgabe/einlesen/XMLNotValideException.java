package de.klausurplaner.dateneinausgabe.einlesen;

/**
 * Exception, die geworfen wird, wenn ein XML-File nicht valide ist
 * @author Michael Sandritter, Benjamin Christiani, Merle Hiort
 *
 */
@SuppressWarnings("serial")
public class XMLNotValideException extends Exception{
	
	public XMLNotValideException(){
	}
	
	public XMLNotValideException(String m){
		super(m);
	}
}
