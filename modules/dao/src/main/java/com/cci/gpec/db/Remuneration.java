package com.cci.gpec.db;

import com.cci.gpec.db.base.BaseRemuneration;

/**
 * This is the object class that relates to the REMUNERATION table.
 * Any customizations belong here.
 */
public class Remuneration extends BaseRemuneration {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Remuneration () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Remuneration (java.lang.Integer _idRemuneration) {
		super(_idRemuneration);
	}

/*[CONSTRUCTOR MARKER END]*/
}