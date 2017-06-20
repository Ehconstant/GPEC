package com.cci.gpec.db.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the TYPEHABILITATION table.
 * Do not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="TYPEHABILITATION"
 */

public abstract class BaseTypehabilitation implements Serializable {

	public static String REF = "Typehabilitation";
	public static String PROP_NOM_TYPE_HABILITATION = "NomTypeHabilitation";
	public static String PROP_ID = "Id";
	public static String PROP_ID_GROUPE = "IdGroupe";

	// many to one
	private com.cci.gpec.db.Groupe groupe;

	// constructors
	public BaseTypehabilitation() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTypehabilitation(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomTypeHabilitation;

	// collections
	private java.util.Set<com.cci.gpec.db.Habilitation> hABILITATIONs;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID_TYPE_HABILITATION"
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
	 * Return the value associated with the column: NOM_TYPE_HABILITATION
	 */
	public java.lang.String getNomTypeHabilitation() {
		return nomTypeHabilitation;
	}

	/**
	 * Set the value related to the column: NOM_TYPE_HABILITATION
	 * 
	 * @param nomTypeHabilitation
	 *            the NOM_TYPE_HABILITATION value
	 */
	public void setNomTypeHabilitation(java.lang.String nomTypeHabilitation) {
		this.nomTypeHabilitation = nomTypeHabilitation;
	}

	/**
	 * Return the value associated with the column: HABILITATIONs
	 */
	public java.util.Set<com.cci.gpec.db.Habilitation> getHABILITATIONs() {
		return hABILITATIONs;
	}

	/**
	 * Set the value related to the column: HABILITATIONs
	 * 
	 * @param hABILITATIONs
	 *            the HABILITATIONs value
	 */
	public void setHABILITATIONs(
			java.util.Set<com.cci.gpec.db.Habilitation> hABILITATIONs) {
		this.hABILITATIONs = hABILITATIONs;
	}

	public void addToHABILITATIONs(com.cci.gpec.db.Habilitation habilitation) {
		if (null == getHABILITATIONs())
			setHABILITATIONs(new java.util.TreeSet<com.cci.gpec.db.Habilitation>());
		getHABILITATIONs().add(habilitation);
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.Typehabilitation))
			return false;
		else {
			com.cci.gpec.db.Typehabilitation typehabilitation = (com.cci.gpec.db.Typehabilitation) obj;
			if (null == this.getId() || null == typehabilitation.getId())
				return false;
			else
				return (this.getId().equals(typehabilitation.getId()));
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