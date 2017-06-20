package com.cci.gpec.db.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the TYPEACCIDENT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TYPEACCIDENT"
 */

public abstract class BaseTypeaccident  implements Serializable {

	public static String REF = "Typeaccident";
	public static String PROP_NOM_TYPE_ACCIDENT = "NomTypeAccident";
	public static String PROP_ID = "Id";


	// constructors
	public BaseTypeaccident () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTypeaccident (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomTypeAccident;

	// collections
	private java.util.Set<com.cci.gpec.db.Accident> aCCIDENTs;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID_TYPE_ACCIDENT"
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
	 * Return the value associated with the column: NOM_TYPE_ACCIDENT
	 */
	public java.lang.String getNomTypeAccident () {
		return nomTypeAccident;
	}

	/**
	 * Set the value related to the column: NOM_TYPE_ACCIDENT
	 * @param nomTypeAccident the NOM_TYPE_ACCIDENT value
	 */
	public void setNomTypeAccident (java.lang.String nomTypeAccident) {
		this.nomTypeAccident = nomTypeAccident;
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
		if (!(obj instanceof com.cci.gpec.db.Typeaccident)) return false;
		else {
			com.cci.gpec.db.Typeaccident typeaccident = (com.cci.gpec.db.Typeaccident) obj;
			if (null == this.getId() || null == typeaccident.getId()) return false;
			else return (this.getId().equals(typeaccident.getId()));
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