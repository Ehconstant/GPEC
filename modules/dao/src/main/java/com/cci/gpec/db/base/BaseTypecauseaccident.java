package com.cci.gpec.db.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the TYPECAUSEACCIDENT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TYPECAUSEACCIDENT"
 */

public abstract class BaseTypecauseaccident  implements Serializable {

	public static String REF = "Typecauseaccident";
	public static String PROP_ID = "Id";
	public static String PROP_NOM_TYPE_CAUSE_ACCIDENT = "NomTypeCauseAccident";


	// constructors
	public BaseTypecauseaccident () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTypecauseaccident (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomTypeCauseAccident;

	// collections
	private java.util.Set<com.cci.gpec.db.Accident> aCCIDENTs;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID_TYPE_CAUSE_ACCIDENT"
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
	 * Return the value associated with the column: NOM_TYPE_CAUSE_ACCIDENT
	 */
	public java.lang.String getNomTypeCauseAccident () {
		return nomTypeCauseAccident;
	}

	/**
	 * Set the value related to the column: NOM_TYPE_CAUSE_ACCIDENT
	 * @param nomTypeCauseAccident the NOM_TYPE_CAUSE_ACCIDENT value
	 */
	public void setNomTypeCauseAccident (java.lang.String nomTypeCauseAccident) {
		this.nomTypeCauseAccident = nomTypeCauseAccident;
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
		if (!(obj instanceof com.cci.gpec.db.Typecauseaccident)) return false;
		else {
			com.cci.gpec.db.Typecauseaccident typecauseaccident = (com.cci.gpec.db.Typecauseaccident) obj;
			if (null == this.getId() || null == typecauseaccident.getId()) return false;
			else return (this.getId().equals(typecauseaccident.getId()));
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