package com.cci.gpec.db.base;

import java.io.Serializable;
import java.util.Date;

/**
 * This is an object that contains data related to the GROUPE table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="GROUPE"
 */

public abstract class BaseGroupe implements Serializable {

	public static String REF = "Groupe";
	public static String PROP_ID = "Id";
	public static String PROP_NOM_GROUPE = "NomGroupe";
	public static String PROP_DELETED = "Deleted";

	// constructors
	public BaseGroupe() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseGroupe(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomGroupe;
	private Boolean deleted;
	private Date finPeriodeEssai;

	// collections
	private java.util.Set<com.cci.gpec.db.Entreprise> eNTREPRISEs;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID_GROUPE"
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
	 * Return the value associated with the column: NOM_GROUPE
	 */
	public java.lang.String getNomGroupe() {
		return nomGroupe;
	}

	/**
	 * Set the value related to the column: NOM_GROUPE
	 * 
	 * @param nomGroupe
	 *            the NOM_GROUPE value
	 */
	public void setNomGroupe(java.lang.String nomGroupe) {
		this.nomGroupe = nomGroupe;
	}

	/**
	 * Return the value associated with the column: ENTREPRISEs
	 */
	public java.util.Set<com.cci.gpec.db.Entreprise> getENTREPRISEs() {
		return eNTREPRISEs;
	}

	/**
	 * Set the value related to the column: ENTREPRISEs
	 * 
	 * @param eNTREPRISEs
	 *            the ENTREPRISEs value
	 */
	public void setENTREPRISEs(
			java.util.Set<com.cci.gpec.db.Entreprise> eNTREPRISEs) {
		this.eNTREPRISEs = eNTREPRISEs;
	}

	public void addToENTREPRISEs(com.cci.gpec.db.Entreprise entreprise) {
		if (null == getENTREPRISEs())
			setENTREPRISEs(new java.util.TreeSet<com.cci.gpec.db.Entreprise>());
		getENTREPRISEs().add(entreprise);
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.Groupe))
			return false;
		else {
			com.cci.gpec.db.Groupe groupe = (com.cci.gpec.db.Groupe) obj;
			if (null == this.getId() || null == groupe.getId())
				return false;
			else
				return (this.getId().equals(groupe.getId()));
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

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Date getFinPeriodeEssai() {
		return finPeriodeEssai;
	}

	public void setFinPeriodeEssai(Date finPeriodeEssai) {
		this.finPeriodeEssai = finPeriodeEssai;
	}

}