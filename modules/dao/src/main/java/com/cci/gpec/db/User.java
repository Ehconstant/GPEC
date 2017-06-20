package com.cci.gpec.db;

import com.cci.gpec.db.base.BaseUser;

/**
 * This is the object class that relates to the USER table.
 * Any customizations belong here.
 */
public class User extends BaseUser {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public User () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public User (java.lang.Integer _id) {
		super(_id);
	}

/*[CONSTRUCTOR MARKER END]*/
}