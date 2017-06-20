package com.cci.gpec.db.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the TYPEABSENCE table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="TYPEABSENCE"
 */

public abstract class BaseTypeabsence implements Serializable {

	public static String REF = "Typeabsence";
	public static String PROP_ID = "Id";
	public static String PROP_NOM_TYPE_ABSENCE = "NomTypeAbsence";
	public static String PROP_ID_GROUPE = "IdGroupe";

	// many to one
	private com.cci.gpec.db.Groupe groupe;

	// constructors
	public BaseTypeabsence() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTypeabsence(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomTypeAbsence;

	// collections
	private java.util.Set<com.cci.gpec.db.Absence> aBSENCEs;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID_TYPE_ABSENCE"
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: NOM_TYPE_ABSENCE
	 */
	public java.lang.String getNomTypeAbsence() {
		return nomTypeAbsence;
	}

	/**
	 * Set the value related to the column: NOM_TYPE_ABSENCE
	 * 
	 * @param nomTypeAbsence
	 *            the NOM_TYPE_ABSENCE value
	 */
	public void setNomTypeAbsence(java.lang.String nomTypeAbsence) {
		this.nomTypeAbsence = nomTypeAbsence;
	}

	/**
	 * Return the value associated with the column: ABSENCEs
	 */
	public java.util.Set<com.cci.gpec.db.Absence> getABSENCEs() {
		return aBSENCEs;
	}

	/**
	 * Set the value related to the column: ABSENCEs
	 * 
	 * @param aBSENCEs
	 *            the ABSENCEs value
	 */
	public void setABSENCEs(java.util.Set<com.cci.gpec.db.Absence> aBSENCEs) {
		this.aBSENCEs = aBSENCEs;
	}

	public void addToABSENCEs(com.cci.gpec.db.Absence absence) {
		if (null == getABSENCEs())
			setABSENCEs(new java.util.TreeSet<com.cci.gpec.db.Absence>());
		getABSENCEs().add(absence);
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.Typeabsence))
			return false;
		else {
			com.cci.gpec.db.Typeabsence typeabsence = (com.cci.gpec.db.Typeabsence) obj;
			if (null == this.getId() || null == typeabsence.getId())
				return false;
			else
				return (this.getId().equals(typeabsence.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":"
						+ this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public com.cci.gpec.db.Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(com.cci.gpec.db.Groupe groupe) {
		this.groupe = groupe;
	}

}