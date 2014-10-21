package de.klausurplaner.controller.direktoren;

import de.klausurplaner.controller.builder.BuilderInterface;

/**
 * DirektorInterface 
 * @author Benjamin Christiani, Michael Sandritter, Merle Hiort
 *
 */
public interface DirektorInterface {
	
	/**
	 * liefert einen Builder zurueck
	 * @param o -> Instanz eines Builders
	 * @return Builder
	 */
	BuilderInterface getBuilder(Object o);

}
