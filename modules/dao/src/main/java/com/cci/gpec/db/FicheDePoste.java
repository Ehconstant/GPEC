package com.cci.gpec.db;

import com.cci.gpec.db.base.BaseFicheDePoste;

/**
 * This is the object class that relates to the FICHEDEPOSTE table.
 * Any customizations belong here.
 */
public class FicheDePoste extends BaseFicheDePoste {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public FicheDePoste () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public FicheDePoste (java.lang.Integer _id) {
		super(_id);
	}

/*[CONSTRUCTOR MARKER END]*/
}