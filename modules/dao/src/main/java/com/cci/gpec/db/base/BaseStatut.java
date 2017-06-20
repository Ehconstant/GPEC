package com.cci.gpec.db.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the STATUT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="STATUT"
 */

public abstract class BaseStatut  implements Serializable {

	public static String REF = "Statut";
	public static String PROP_ID = "Id";
	public static String PROP_NOM_STATUT = "NomStatut";


	// constructors
	public BaseStatut () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseStatut (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomStatut;

	// collections
	private java.util.Set<com.cci.gpec.db.Parcours> pARCOURSs;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID_STATUT"
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
	 * Return the value associated with the column: NOM_STATUT
	 */
	public java.lang.String getNomStatut () {
		return nomStatut;
	}

	/**
	 * Set the value related to the column: NOM_STATUT
	 * @param nomStatut the NOM_STATUT value
	 */
	public void setNomStatut (java.lang.String nomStatut) {
		this.nomStatut = nomStatut;
	}



	/**
	 * Return the value associated with the column: PARCOURSs
	 */
	public java.util.Set<com.cci.gpec.db.Parcours> getPARCOURSs () {
		return pARCOURSs;
	}

	/**
	 * Set the value related to the column: PARCOURSs
	 * @param pARCOURSs the PARCOURSs value
	 */
	public void setPARCOURSs (java.util.Set<com.cci.gpec.db.Parcours> pARCOURSs) {
		this.pARCOURSs = pARCOURSs;
	}

	public void addToPARCOURSs (com.cci.gpec.db.Parcours parcours) {
		if (null == getPARCOURSs()) setPARCOURSs(new java.util.TreeSet<com.cci.gpec.db.Parcours>());
		getPARCOURSs().add(parcours);
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.cci.gpec.db.Statut)) return false;
		else {
			com.cci.gpec.db.Statut statut = (com.cci.gpec.db.Statut) obj;
			if (null == this.getId() || null == statut.getId()) return false;
			else return (this.getId().equals(statut.getId()));
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