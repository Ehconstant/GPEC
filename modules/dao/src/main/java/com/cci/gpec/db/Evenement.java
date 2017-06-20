package com.cci.gpec.db;

import com.cci.gpec.db.base.BaseEvenement;

/**
 * This is the object class that relates to the EVENEMENT table.
 * Any customizations belong here.
 */
public class Evenement extends BaseEvenement {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Evenement () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Evenement (java.lang.Integer _id) {
		super(_id);
	}

/*[CONSTRUCTOR MARKER END]*/
}