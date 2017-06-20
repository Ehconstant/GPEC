package com.cci.gpec.db;

import com.cci.gpec.db.base.BaseTransmission;

/**
 * This is the object class that relates to the TRANSMISSION table.
 * Any customizations belong here.
 */
public class Transmission extends BaseTransmission {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public Transmission () {
		super();
	}

	/**
	 * Constructor for required fields
	 */
	public Transmission (
		java.lang.Integer _idTransmission) {

		super (
			_idTransmission);
	}

/*[CONSTRUCTOR MARKER END]*/
}