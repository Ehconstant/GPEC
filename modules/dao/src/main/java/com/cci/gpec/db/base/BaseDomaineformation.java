package com.cci.gpec.db.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the DOMAINEFORMATION table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="DOMAINEFORMATION"
 */

public abstract class BaseDomaineformation  implements Serializable {

	public static String REF = "Domaineformation";
	public static String PROP_NOM_DOMAINE_FORMATION = "NomDomaineFormation";
	public static String PROP_ID_FAMILLE_FORMATION = "IdFamilleFormation";
	public static String PROP_ID = "Id";


	// constructors
	public BaseDomaineformation () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseDomaineformation (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomDomaineFormation;
	private java.lang.Integer idFamilleFormation;

	// collections
	private java.util.Set<com.cci.gpec.db.Formation> fORMATIONs;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID_DOMAINE_FORMATION"
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
	 * Return the value associated with the column: NOM_DOMAINE_FORMATION
	 */
	public java.lang.String getNomDomaineFormation () {
		return nomDomaineFormation;
	}

	/**
	 * Set the value related to the column: NOM_DOMAINE_FORMATION
	 * @param nomDomaineFormation the NOM_DOMAINE_FORMATION value
	 */
	public void setNomDomaineFormation (java.lang.String nomDomaineFormation) {
		this.nomDomaineFormation = nomDomaineFormation;
	}



	/**
	 * Return the value associated with the column: ID_FAMILLE_FORMATION
	 */
	public java.lang.Integer getIdFamilleFormation () {
		return idFamilleFormation;
	}

	/**
	 * Set the value related to the column: ID_FAMILLE_FORMATION
	 * @param idFamilleFormation the ID_FAMILLE_FORMATION value
	 */
	public void setIdFamilleFormation (java.lang.Integer idFamilleFormation) {
		this.idFamilleFormation = idFamilleFormation;
	}



	/**
	 * Return the value associated with the column: FORMATIONs
	 */
	public java.util.Set<com.cci.gpec.db.Formation> getFORMATIONs () {
		return fORMATIONs;
	}

	/**
	 * Set the value related to the column: FORMATIONs
	 * @param fORMATIONs the FORMATIONs value
	 */
	public void setFORMATIONs (java.util.Set<com.cci.gpec.db.Formation> fORMATIONs) {
		this.fORMATIONs = fORMATIONs;
	}

	public void addToFORMATIONs (com.cci.gpec.db.Formation formation) {
		if (null == getFORMATIONs()) setFORMATIONs(new java.util.TreeSet<com.cci.gpec.db.Formation>());
		getFORMATIONs().add(formation);
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.cci.gpec.db.Domaineformation)) return false;
		else {
			com.cci.gpec.db.Domaineformation domaineformation = (com.cci.gpec.db.Domaineformation) obj;
			if (null == this.getId() || null == domaineformation.getId()) return false;
			else return (this.getId().equals(domaineformation.getId()));
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