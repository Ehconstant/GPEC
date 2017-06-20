package com.cci.gpec.db;

import com.cci.gpec.db.base.BaseFicheMetierEntreprise;

/**
 * This is the object class that relates to the FICHEMETIERENTREPRISE table. Any
 * customizations belong here.
 */
public class FicheMetierEntreprise extends BaseFicheMetierEntreprise {

	/* [CONSTRUCTOR MARKER BEGIN] */
	public FicheMetierEntreprise() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public FicheMetierEntreprise(com.cci.gpec.db.FicheMetier ficheMetier,
			com.cci.gpec.db.Entreprise entreprise) {

		super(ficheMetier, entreprise);
	}

	/* [CONSTRUCTOR MARKER END] */
}