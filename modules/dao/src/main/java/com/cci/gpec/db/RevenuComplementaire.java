package com.cci.gpec.db;

import com.cci.gpec.db.base.BaseRevenuComplementaire;

/**
 * This is the object class that relates to the REMUNERATION table.
 * Any customizations belong here.
 */
public class RevenuComplementaire extends BaseRevenuComplementaire {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public RevenuComplementaire () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public RevenuComplementaire (java.lang.Integer _idRevenuComplementaire) {
		super(_idRevenuComplementaire);
	}

/*[CONSTRUCTOR MARKER END]*/
}