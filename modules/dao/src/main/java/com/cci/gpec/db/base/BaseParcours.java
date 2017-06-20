package com.cci.gpec.db.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the PARCOURS table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="PARCOURS"
 */

public abstract class BaseParcours implements Serializable {

	public static String REF = "Parcours";
	public static String PROP_ID_STATUT = "IdStatut";
	public static String PROP_ID_SALARIE = "IdSalarie";
	public static String PROP_ID_TYPE_CONTRAT = "IdTypeContrat";
	public static String PROP_EQUIVALENCE_TEMPS_PLEIN = "EquivalenceTempsPlein";
	public static String PROP_FIN_FONCTION = "FinFonction";
	public static String PROP_DEBUT_FONCTION = "DebutFonction";
	public static String PROP_ID = "Id";
	public static String PROP_ID_METIER = "IdMetier";
	public static String PROP_JUSTIFICATIF = "Justificatif";
	public static String PROP_ECHELON = "Echelon";
	public static String PROP_NIVEAU = "Niveau";
	public static String PROP_COEFFICIENT = "Coefficient";

	// constructors
	public BaseParcours() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseParcours(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.util.Date debutFonction;
	private java.util.Date finFonction;

	private java.util.Date debutContrat;
	private java.util.Date finContrat;

	private java.lang.String natureContrat;

	private java.lang.Integer equivalenceTempsPlein;
	private java.lang.String justificatif;
	private java.lang.String niveau;
	private java.lang.String echelon;
	private java.lang.String coefficient;

	// many to one
	private com.cci.gpec.db.Metier metier;
	private com.cci.gpec.db.Typecontrat typeContrat;
	private com.cci.gpec.db.Salarie salarie;
	private com.cci.gpec.db.Statut statut;
	private com.cci.gpec.db.Typerecours typeRecours;
	private com.cci.gpec.db.Motifrupturecontrat motifRecoursContrat;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID_PARCOURS"
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
	 * Return the value associated with the column: DEBUT_FONCTION
	 */
	public java.util.Date getDebutFonction() {
		return debutFonction;
	}

	/**
	 * Set the value related to the column: DEBUT_FONCTION
	 * 
	 * @param debutFonction
	 *            the DEBUT_FONCTION value
	 */
	public void setDebutFonction(java.util.Date debutFonction) {
		this.debutFonction = debutFonction;
	}

	/**
	 * Return the value associated with the column: FIN_FONCTION
	 */
	public java.util.Date getFinFonction() {
		return finFonction;
	}

	/**
	 * Set the value related to the column: FIN_FONCTION
	 * 
	 * @param finFonction
	 *            the FIN_FONCTION value
	 */
	public void setFinFonction(java.util.Date finFonction) {
		this.finFonction = finFonction;
	}

	/**
	 * Return the value associated with the column: EQUIVALENCE_TEMPS_PLEIN
	 */
	public java.lang.Integer getEquivalenceTempsPlein() {
		return equivalenceTempsPlein;
	}

	/**
	 * Set the value related to the column: EQUIVALENCE_TEMPS_PLEIN
	 * 
	 * @param equivalenceTempsPlein
	 *            the EQUIVALENCE_TEMPS_PLEIN value
	 */
	public void setEquivalenceTempsPlein(java.lang.Integer equivalenceTempsPlein) {
		this.equivalenceTempsPlein = equivalenceTempsPlein;
	}

	/**
	 * Return the value associated with the column: ID_METIER
	 */
	public com.cci.gpec.db.Metier getMetier() {
		return metier;
	}

	/**
	 * Set the value related to the column: ID_METIER
	 * 
	 * @param idMetier
	 *            the ID_METIER value
	 */
	public void setMetier(com.cci.gpec.db.Metier metier) {
		this.metier = metier;
	}

	/**
	 * Return the value associated with the column: ID_TYPE_CONTRAT
	 */
	public com.cci.gpec.db.Typecontrat getTypeContrat() {
		return typeContrat;
	}

	/**
	 * Set the value related to the column: ID_TYPE_CONTRAT
	 * 
	 * @param idTypeContrat
	 *            the ID_TYPE_CONTRAT value
	 */
	public void setTypeContrat(com.cci.gpec.db.Typecontrat typeContrat) {
		this.typeContrat = typeContrat;
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

	/**
	 * Return the value associated with the column: ID_STATUT
	 */
	public com.cci.gpec.db.Statut getStatut() {
		return statut;
	}

	/**
	 * Set the value related to the column: ID_STATUT
	 * 
	 * @param idStatut
	 *            the ID_STATUT value
	 */
	public void setStatut(com.cci.gpec.db.Statut statut) {
		this.statut = statut;
	}

	public java.lang.String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(java.lang.String justificatif) {
		this.justificatif = justificatif;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.Parcours))
			return false;
		else {
			com.cci.gpec.db.Parcours parcours = (com.cci.gpec.db.Parcours) obj;
			if (null == this.getId() || null == parcours.getId())
				return false;
			else
				return (this.getId().equals(parcours.getId()));
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

	public java.lang.String getNiveau() {
		return niveau;
	}

	public void setNiveau(java.lang.String niveau) {
		this.niveau = niveau;
	}

	public java.lang.String getEchelon() {
		return echelon;
	}

	public void setEchelon(java.lang.String echelon) {
		this.echelon = echelon;
	}

	public java.lang.String getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(java.lang.String coefficient) {
		this.coefficient = coefficient;
	}

	public com.cci.gpec.db.Typerecours getTypeRecours() {
		return typeRecours;
	}

	public void setTypeRecours(com.cci.gpec.db.Typerecours typeRecours) {
		this.typeRecours = typeRecours;
	}

	public java.util.Date getDebutContrat() {
		return debutContrat;
	}

	public void setDebutContrat(java.util.Date debutContrat) {
		this.debutContrat = debutContrat;
	}

	public java.util.Date getFinContrat() {
		return finContrat;
	}

	public void setFinContrat(java.util.Date finContrat) {
		this.finContrat = finContrat;
	}

	public java.lang.String getNatureContrat() {
		return natureContrat;
	}

	public void setNatureContrat(java.lang.String natureContrat) {
		this.natureContrat = natureContrat;
	}

	public com.cci.gpec.db.Motifrupturecontrat getMotifRuptureContrat() {
		return motifRecoursContrat;
	}

	public void setMotifRuptureContrat(
			com.cci.gpec.db.Motifrupturecontrat motifRuptureContrat) {
		this.motifRecoursContrat = motifRuptureContrat;
	}

}