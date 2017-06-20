package com.cci.gpec.db.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the SERVICEGENERIQUE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="SERVICEGENERIQUE"
 */

public abstract class BaseServicegenerique  implements Serializable {

	public static String REF = "Servicegenerique";
	public static String PROP_NOM_SERVICE_GENERIQUE = "NomServiceGenerique";
	public static String PROP_ID = "Id";


	// constructors
	public BaseServicegenerique () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseServicegenerique (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomServiceGenerique;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID_SERVICE_GENERIQUE"
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
	 * Return the value associated with the column: NOM_SERVICE_GENERIQUE
	 */
	public java.lang.String getNomServiceGenerique () {
		return nomServiceGenerique;
	}

	/**
	 * Set the value related to the column: NOM_SERVICE_GENERIQUE
	 * @param nomServiceGenerique the NOM_SERVICE_GENERIQUE value
	 */
	public void setNomServiceGenerique (java.lang.String nomServiceGenerique) {
		this.nomServiceGenerique = nomServiceGenerique;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.cci.gpec.db.Servicegenerique)) return false;
		else {
			com.cci.gpec.db.Servicegenerique servicegenerique = (com.cci.gpec.db.Servicegenerique) obj;
			if (null == this.getId() || null == servicegenerique.getId()) return false;
			else return (this.getId().equals(servicegenerique.getId()));
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