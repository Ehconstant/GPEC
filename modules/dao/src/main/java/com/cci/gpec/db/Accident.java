package com.cci.gpec.db;

import com.cci.gpec.db.base.BaseAccident;

/**
 * This is the object class that relates to the ACCIDENT table.
 * Any customizations belong here.
 */
public class Accident extends BaseAccident {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Accident () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Accident (java.lang.Integer _id) {
		super(_id);
	}

/*[CONSTRUCTOR MARKER END]*/
}