package com.cci.gpec.db.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the ABSENCE table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="ABSENCE"
 */

public abstract class BaseAbsence implements Serializable {

	public static String REF = "Absence";
	public static String PROP_ID_SALARIE = "IdSalarie";
	public static String PROP_ID_TYPE_ABSENCE = "IdTypeAbsence";
	public static String PROP_FIN_ABSENCE = "FinAbsence";
	public static String PROP_ID = "Id";
	public static String PROP_DEBUT_ABSENCE = "DebutAbsence";
	public static String PROP_NOMBRE_JOUR_OUVRE = "NombreJourOuvre";
	public static String PROP_JUSTIFICATIF = "Justificatif";

	// constructors
	public BaseAbsence() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseAbsence(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.util.Date debutAbsence;
	private java.util.Date finAbsence;
	private java.lang.Double nombreJourOuvre;
	private java.lang.String justificatif;

	// many to one
	private com.cci.gpec.db.Typeabsence typeAbsence;
	private com.cci.gpec.db.Salarie salarie;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID_ABSENCE"
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
	 * Return the value associated with the column: DEBUT_ABSENCE
	 */
	public java.util.Date getDebutAbsence() {
		return debutAbsence;
	}

	/**
	 * Set the value related to the column: DEBUT_ABSENCE
	 * 
	 * @param debutAbsence
	 *            the DEBUT_ABSENCE value
	 */
	public void setDebutAbsence(java.util.Date debutAbsence) {
		this.debutAbsence = debutAbsence;
	}

	/**
	 * Return the value associated with the column: FIN_ABSENCE
	 */
	public java.util.Date getFinAbsence() {
		return finAbsence;
	}

	/**
	 * Set the value related to the column: FIN_ABSENCE
	 * 
	 * @param finAbsence
	 *            the FIN_ABSENCE value
	 */
	public void setFinAbsence(java.util.Date finAbsence) {
		this.finAbsence = finAbsence;
	}

	/**
	 * Return the value associated with the column: ID_TYPE_ABSENCE
	 */
	public com.cci.gpec.db.Typeabsence getTypeAbsence() {
		return typeAbsence;
	}

	/**
	 * Set the value related to the column: ID_TYPE_ABSENCE
	 * 
	 * @param idTypeAbsence
	 *            the ID_TYPE_ABSENCE value
	 */
	public void setTypeAbsence(com.cci.gpec.db.Typeabsence typeAbsence) {
		this.typeAbsence = typeAbsence;
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

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.Absence))
			return false;
		else {
			com.cci.gpec.db.Absence absence = (com.cci.gpec.db.Absence) obj;
			if (null == this.getId() || null == absence.getId())
				return false;
			else
				return (this.getId().equals(absence.getId()));
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

	public java.lang.Double getNombreJourOuvre() {
		return this.nombreJourOuvre;
	}

	public void setNombreJourOuvre(java.lang.Double nombreJourOuvre) {
		this.nombreJourOuvre = nombreJourOuvre;
	}

	public java.lang.String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(java.lang.String justificatif) {
		this.justificatif = justificatif;
	}

}