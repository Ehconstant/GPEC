package com.cci.gpec.db.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the HABILITATION table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="HABILITATION"
 */

public abstract class BaseHabilitation implements Serializable {

	public static String REF = "Habilitation";
	public static String PROP_ID_SALARIE = "IdSalarie";
	public static String PROP_ID_TYPE_HABILITATION = "IdTypeHabilitation";
	public static String PROP_EXPIRATION = "Expiration";
	public static String PROP_DELIVRANCE = "Delivrance";
	public static String PROP_DUREE_VALIDITE = "DureeValidite";
	public static String PROP_ID = "Id";
	public static String PROP_JUSTIFICATIF = "Justificatif";

	// constructors
	public BaseHabilitation() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseHabilitation(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.util.Date delivrance;
	private java.util.Date expiration;
	private java.lang.Integer dureeValidite;
	private java.lang.String justificatif;
	private java.lang.String commentaire;

	// many to one
	private com.cci.gpec.db.Salarie salarie;
	private com.cci.gpec.db.Typehabilitation typeHabilitation;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID_HABILITATION"
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
	 * Return the value associated with the column: DELIVRANCE
	 */
	public java.util.Date getDelivrance() {
		return delivrance;
	}

	/**
	 * Set the value related to the column: DELIVRANCE
	 * 
	 * @param delivrance
	 *            the DELIVRANCE value
	 */
	public void setDelivrance(java.util.Date delivrance) {
		this.delivrance = delivrance;
	}

	/**
	 * Return the value associated with the column: EXPIRATION
	 */
	public java.util.Date getExpiration() {
		return expiration;
	}

	/**
	 * Set the value related to the column: EXPIRATION
	 * 
	 * @param expiration
	 *            the EXPIRATION value
	 */
	public void setExpiration(java.util.Date expiration) {
		this.expiration = expiration;
	}

	/**
	 * Return the value associated with the column: DUREE_VALIDITE
	 */
	public java.lang.Integer getDureeValidite() {
		return dureeValidite;
	}

	/**
	 * Set the value related to the column: DUREE_VALIDITE
	 * 
	 * @param dureeValidite
	 *            the DUREE_VALIDITE value
	 */
	public void setDureeValidite(java.lang.Integer dureeValidite) {
		this.dureeValidite = dureeValidite;
	}

	/**
	 * Return the value associated with the column: JUSTIFICATIF
	 */
	public java.lang.String getJustificatif() {
		return justificatif;
	}

	/**
	 * Set the value related to the column: JUSTIFICATIF
	 * 
	 * @param justificatif
	 *            the JUSTIFICATIF value
	 */
	public void setJustificatif(java.lang.String justificatif) {
		this.justificatif = justificatif;
	}

	/**
	 * Return the value associated with the column: ID_SALARIE
	 */
	public com.cci.gpec.db.Salarie getSalarie() {
		return salarie;
	}

	/**
	 * Set the value related to the column: ID_SALARIE
	 * 
	 * @param idSalarie
	 *            the ID_SALARIE value
	 */
	public void setSalarie(com.cci.gpec.db.Salarie salarie) {
		this.salarie = salarie;
	}

	/**
	 * Return the value associated with the column: ID_TYPE_HABILITATION
	 */
	public com.cci.gpec.db.Typehabilitation getTypeHabilitation() {
		return typeHabilitation;
	}

	/**
	 * Set the value related to the column: ID_TYPE_HABILITATION
	 * 
	 * @param idTypeHabilitation
	 *            the ID_TYPE_HABILITATION value
	 */
	public void setTypeHabilitation(
			com.cci.gpec.db.Typehabilitation typeHabilitation) {
		this.typeHabilitation = typeHabilitation;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.Habilitation))
			return false;
		else {
			com.cci.gpec.db.Habilitation habilitation = (com.cci.gpec.db.Habilitation) obj;
			if (null == this.getId() || null == habilitation.getId())
				return false;
			else
				return (this.getId().equals(habilitation.getId()));
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

	public java.lang.String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(java.lang.String commentaire) {
		this.commentaire = commentaire;
	}

}