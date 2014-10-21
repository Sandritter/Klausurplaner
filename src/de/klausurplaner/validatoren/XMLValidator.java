package de.klausurplaner.validatoren;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * Diese Klasse ueberprueft die eingelesene XML-Datei auf ihre Richtigkeit.
 * @author Benjamin Christiani, Merle Hiort, Michael Sandritter
 *
 */
public class XMLValidator implements Validable{
	
	/**
	 * Pfad zum XML-File
	 */
	private String xmlPath;
	
	/**
	 * Pfad zum XML-Schema
	 */
	private String xsdPath = "XMLSchema/XMLSchema.xsd";
	
	/**
	 * Parametrisierter Konstruktor
	 * @param xmlPath - Pfad zum einzulesendem XML-File 
	 */
	public XMLValidator(String xmlPath){
		this.xmlPath = xmlPath;
	}
	
	/**
	 * Prueft, ob das XML-File valide gegenueber dem XML-Schema ist.
	 */
	@Override
	public boolean valide() {
		
		if(!this.xmlPath.endsWith(".xml")){
			return false;
		}
		
		boolean status = false;
		Document document = null;
		Validator validator = null;
		try{			
			// parse ein XML Dokument in einen DOM Baum
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			document = parser.parse(new File(this.xmlPath));
			
			// erstellt eine SchemaFactory, welche faehig ist, WSX Schemas zu verstehen.
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			// laedt ein WXS Schema, das von einer Schema Instanz repraesentiert wird.
			Source schemaFile = new StreamSource(new File(this.xsdPath));
			Schema schema = factory.newSchema(schemaFile);
			
			// Erstellt eine Validator Instanz, welche benutzt werden kann um ein Dokument zu validieren.
			validator = schema.newValidator();
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		// validiere einen DOM Baum
		try {
			validator.validate(new DOMSource(document));
			status = true;
		} catch (SAXException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return status;
	}

}
