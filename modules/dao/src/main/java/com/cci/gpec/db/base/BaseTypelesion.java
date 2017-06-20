package com.cci.gpec.db.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the TYPELESION table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TYPELESION"
 */

public abstract class BaseTypelesion  implements Serializable {

	public static String REF = "Typelesion";
	public static String PROP_NOM_TYPE_LESION = "NomTypeLesion";
	public static String PROP_ID = "Id";


	// constructors
	public BaseTypelesion () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTypelesion (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomTypeLesion;

	// collections
	private java.util.Set<com.cci.gpec.db.Accident> aCCIDENTs;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID_TYPE_LESION"
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
	 * Return the value associated with the column: NOM_TYPE_LESION
	 */
	public java.lang.String getNomTypeLesion () {
		return nomTypeLesion;
	}

	/**
	 * Set the value related to the column: NOM_TYPE_LESION
	 * @param nomTypeLesion the NOM_TYPE_LESION value
	 */
	public void setNomTypeLesion (java.lang.String nomTypeLesion) {
		this.nomTypeLesion = nomTypeLesion;
	}



	/**
	 * Return the value associated with the column: ACCIDENTs
	 */
	public java.util.Set<com.cci.gpec.db.Accident> getACCIDENTs () {
		return aCCIDENTs;
	}

	/**
	 * Set the value related to the column: ACCIDENTs
	 * @param aCCIDENTs the ACCIDENTs value
	 */
	public void setACCIDENTs (java.util.Set<com.cci.gpec.db.Accident> aCCIDENTs) {
		this.aCCIDENTs = aCCIDENTs;
	}

	public void addToACCIDENTs (com.cci.gpec.db.Accident accident) {
		if (null == getACCIDENTs()) setACCIDENTs(new java.util.TreeSet<com.cci.gpec.db.Accident>());
		getACCIDENTs().add(accident);
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.cci.gpec.db.Typelesion)) return false;
		else {
			com.cci.gpec.db.Typelesion typelesion = (com.cci.gpec.db.Typelesion) obj;
			if (null == this.getId() || null == typelesion.getId()) return false;
			else return (this.getId().equals(typelesion.getId()));
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