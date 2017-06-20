package com.cci.gpec.db.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the FAMILLEMETIER table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="FAMILLEMETIER"
 */

public abstract class BaseFamillemetier  implements Serializable {

	public static String REF = "Famillemetier";
	public static String PROP_ID = "Id";
	public static String PROP_NOM_FAMILLE_METIER = "NomFamilleMetier";


	// constructors
	public BaseFamillemetier () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseFamillemetier (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomFamilleMetier;

	// collections
	private java.util.Set<com.cci.gpec.db.Metier> mETIERs;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID_FAMILLE_METIER"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: NOM_FAMILLE_METIER
	 */
	public java.lang.String getNomFamilleMetier () {
		return nomFamilleMetier;
	}

	/**
	 * Set the value related to the column: NOM_FAMILLE_METIER
	 * @param nomFamilleMetier the NOM_FAMILLE_METIER value
	 */
	public void setNomFamilleMetier (java.lang.String nomFamilleMetier) {
		this.nomFamilleMetier = nomFamilleMetier;
	}



	/**
	 * Return the value associated with the column: METIERs
	 */
	public java.util.Set<com.cci.gpec.db.Metier> getMETIERs () {
		return mETIERs;
	}

	/**
	 * Set the value related to the column: METIERs
	 * @param mETIERs the METIERs value
	 */
	public void setMETIERs (java.util.Set<com.cci.gpec.db.Metier> mETIERs) {
		this.mETIERs = mETIERs;
	}

	public void addToMETIERs (com.cci.gpec.db.Metier metier) {
		if (null == getMETIERs()) setMETIERs(new java.util.TreeSet<com.cci.gpec.db.Metier>());
		getMETIERs().add(metier);
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.cci.gpec.db.Famillemetier)) return false;
		else {
			com.cci.gpec.db.Famillemetier famillemetier = (com.cci.gpec.db.Famillemetier) obj;
			if (null == this.getId() || null == famillemetier.getId()) return false;
			else return (this.getId().equals(famillemetier.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}


}