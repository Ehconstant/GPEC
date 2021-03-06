package com.cci.gpec.db.base;

import java.io.Serializable;

/**
 * This class has been automatically generated by Hibernate Synchronizer. For
 * more information or documentation, visit The Hibernate Synchronizer page at
 * http://www.binamics.com/hibernatesync or contact Joe Hudson at
 * joe@binamics.com.
 * 
 * This is an object that contains data related to the REMUNERATION table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="REMUNERATION"
 */
public abstract class BaseRevenuComplementaire implements Serializable {

	public static String PROP_ID_REVENU_COMPLEMTAIRE = "Id";
	public static String PROP_LIBELLE = "Libelle";
	public static String PROP_TYPE = "Type";
	public static String PROP_ID_GROUPE = "IdGroupe";

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String libelle;
	private java.lang.String type;

	// many to one
	private com.cci.gpec.db.Groupe groupe;

	// collections
	private java.util.Set _lienRemunerationRevenuComplementaireSet;

	// constructors
	public BaseRevenuComplementaire() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRevenuComplementaire(java.lang.Integer _id) {
		this.setId(_id);
		initialize();
	}

	protected void initialize() {
	}

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity"
	 *               column="ID_REVENU_COMPLEMENTAIRE"
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param _idRemuneration
	 *            the new ID
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: TYPE
	 */
	public java.lang.String getType() {
		return type;
	}

	/**
	 * Set the value related to the column: TYPE
	 * 
	 * @param _type
	 *            the TYPE value
	 */
	public void setType(java.lang.String type) {
		this.type = type;
	}

	/**
	 * Return the value associated with the column: LIBELLE
	 */
	public java.lang.String getLibelle() {
		return libelle;
	}

	/**
	 * Set the value related to the column: LIBELLE
	 * 
	 * @param _libelle
	 *            the LIBELLE value
	 */
	public void setLibelle(java.lang.String libelle) {
		this.libelle = libelle;
	}

	/**
	 * Return the value associated with the column:
	 * LienRemunerationRevenuComplementaireSet
	 */
	public java.util.Set getLienRemunerationRevenuComplementaireSet() {
		return this._lienRemunerationRevenuComplementaireSet;
	}

	/**
	 * Set the value related to the column:
	 * LienRemunerationRevenuComplementaireSet
	 * 
	 * @param _lienRemunerationRevenuComplementaireSet
	 *            the LienRemunerationRevenuComplementaireSet value
	 */
	public void setLienRemunerationRevenuComplementaireSet(
			java.util.Set _lienRemunerationRevenuComplementaireSet) {
		this._lienRemunerationRevenuComplementaireSet = _lienRemunerationRevenuComplementaireSet;
	}

	public void addToLienRemunerationRevenuComplementaireSet(Object obj) {
		if (null == this._lienRemunerationRevenuComplementaireSet)
			this._lienRemunerationRevenuComplementaireSet = new java.util.HashSet();
		this._lienRemunerationRevenuComplementaireSet.add(obj);
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.base.BaseRevenuComplementaire))
			return false;
		else {
			com.cci.gpec.db.base.BaseRevenuComplementaire mObj = (com.cci.gpec.db.base.BaseRevenuComplementaire) obj;
			if (null == this.getId() || null == mObj.getId())
				return false;
			else
				return (this.getId().equals(mObj.getId()));
		}
	}

	@Override
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

	@Override
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