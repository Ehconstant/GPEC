package com.cci.gpec.db;

import com.cci.gpec.db.base.BaseFicheMetier;

/**
 * This is the object class that relates to the FICHEMETIER table.
 * Any customizations belong here.
 */
public class FicheMetier extends BaseFicheMetier {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public FicheMetier () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public FicheMetier (java.lang.Integer _id) {
		super(_id);
	}

/*[CONSTRUCTOR MARKER END]*/
}