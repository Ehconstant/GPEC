package com.cci.gpec.db.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the METIER table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="METIER"
 */

public abstract class BaseMetier implements Serializable {

	public static String REF = "Metier";
	public static String PROP_ID_FAMILLE_METIER = "IdFamilleMetier";
	public static String PROP_ID = "Id";
	public static String PROP_NOM_METIER = "NomMetier";
	public static String PROP_DIFFICULTES = "Difficultes";
	public static String PROP_ID_GROUPE = "IdGroupe";

	// constructors
	public BaseMetier() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMetier(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String nomMetier;
	private java.lang.String difficultes;

	// many to one
	private com.cci.gpec.db.Famillemetier familleMetier;
	private com.cci.gpec.db.Groupe groupe;

	// collections
	private java.util.Set<com.cci.gpec.db.Parcours> pARCOURSs;
	private java.util.Set<com.cci.gpec.db.Entretien> eNTRETIENs;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID_METIER"
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
	 * Return the value associated with the column: NOM_METIER
	 */
	public java.lang.String getNomMetier() {
		return nomMetier;
	}

	/**
	 * Set the value related to the column: NOM_METIER
	 * 
	 * @param nomMetier
	 *            the NOM_METIER value
	 */
	public void setNomMetier(java.lang.String nomMetier) {
		this.nomMetier = nomMetier;
	}

	/**
	 * Return the value associated with the column: ID_FAMILLE_METIER
	 */
	public com.cci.gpec.db.Famillemetier getFamilleMetier() {
		return familleMetier;
	}

	/**
	 * Set the value related to the column: ID_FAMILLE_METIER
	 * 
	 * @param idFamilleMetier
	 *            the ID_FAMILLE_METIER value
	 */
	public void setFamilleMetier(com.cci.gpec.db.Famillemetier familleMetier) {
		this.familleMetier = familleMetier;
	}

	/**
	 * Return the value associated with the column: PARCOURSs
	 */
	public java.util.Set<com.cci.gpec.db.Parcours> getPARCOURSs() {
		return pARCOURSs;
	}

	/**
	 * Set the value related to the column: PARCOURSs
	 * 
	 * @param pARCOURSs
	 *            the PARCOURSs value
	 */
	public void setPARCOURSs(java.util.Set<com.cci.gpec.db.Parcours> pARCOURSs) {
		this.pARCOURSs = pARCOURSs;
	}

	public void addToPARCOURSs(com.cci.gpec.db.Parcours parcours) {
		if (null == getPARCOURSs())
			setPARCOURSs(new java.util.TreeSet<com.cci.gpec.db.Parcours>());
		getPARCOURSs().add(parcours);
	}

	/**
	 * Return the value associated with the column: ENTRETIENs
	 */
	public java.util.Set<com.cci.gpec.db.Entretien> getENTRETIENs() {
		return eNTRETIENs;
	}

	/**
	 * Set the value related to the column: ENTRETIENs
	 * 
	 * @param eNTRETIENs
	 *            the ENTRETIENs value
	 */
	public void setENTRETIENs(
			java.util.Set<com.cci.gpec.db.Entretien> eNTRETIENs) {
		this.eNTRETIENs = eNTRETIENs;
	}

	public void addToENTRETIENs(com.cci.gpec.db.Entretien entretien) {
		if (null == getENTRETIENs())
			setENTRETIENs(new java.util.TreeSet<com.cci.gpec.db.Entretien>());
		getENTRETIENs().add(entretien);
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.Metier))
			return false;
		else {
			com.cci.gpec.db.Metier metier = (com.cci.gpec.db.Metier) obj;
			if (null == this.getId() || null == metier.getId())
				return false;
			else
				return (this.getId().equals(metier.getId()));
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

	public java.lang.String getDifficultes() {
		return difficultes;
	}

	public void setDifficultes(java.lang.String difficultes) {
		this.difficultes = difficultes;
	}

	public com.cci.gpec.db.Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(com.cci.gpec.db.Groupe groupe) {
		this.groupe = groupe;
	}

}