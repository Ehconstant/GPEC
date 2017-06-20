package com.cci.gpec.db.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the PARAMSGENERAUX table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="PARAMSGENERAUX"
 */

public abstract class BaseParamsgeneraux implements Serializable {

	public static String REF = "Paramsgeneraux";
	public static String PROP_ID_ENTREPRISE = "IdEntreprise";
	public static String PROP_DUREE_TRAVAIL_AN_N_JOURS_SAL = "DureeTravailAnNJoursSal";
	public static String PROP_DUREE_TRAVAIL_AN_N1_JOURS_SAL = "DureeTravailAnN1JoursSal";
	public static String PROP_DUREE_TRAVAIL_AN_N2_JOURS_SAL = "DureeTravailAnN2JoursSal";
	public static String PROP_DUREE_TRAVAIL_AN_N_HEURES_SAL = "DureeTravailAnNHeuresSal";
	public static String PROP_DUREE_TRAVAIL_AN_N1_HEURES_SAL = "DureeTravailAnN1HeuresSal";
	public static String PROP_DUREE_TRAVAIL_AN_N2_HEURES_SAL = "DureeTravailAnN2HeuresSal";
	public static String PROP_DUREE_TRAVAIL_AN_N_HEURES_REALISEES_EFFECTIF_TOT = "DureeTravailAnNHeuresRealiseesEffectifTot";
	public static String PROP_DUREE_TRAVAIL_AN_N1_HEURES_REALISEES_EFFECTIF_TOT = "DureeTravailAnN1HeuresRealiseesEffectifTot";
	public static String PROP_DUREE_TRAVAIL_AN_N2_HEURES_REALISEES_EFFECTIF_TOT = "DureeTravailAnN2HeuresRealiseesEffectifTot";
	public static String PROP_DUREE_TRAVAIL_AN_N_JOURS_EFFECTIF_TOT = "DureeTravailAnNJoursEffectifTot";
	public static String PROP_DUREE_TRAVAIL_AN_N1_JOURS_EFFECTIF_TOT = "DureeTravailAnN1JoursEffectifTot";
	public static String PROP_DUREE_TRAVAIL_AN_N2_JOURS_EFFECTIF_TOT = "DureeTravailAnN2JoursEffectifTot";
	public static String PROP_ID = "Id";
	public static String PROP_AGE_LEGAL_RETRAITE_AN_N = "AgeLegalRetraiteAnN";
	public static String PROP_AGE_LEGAL_RETRAITE_AN_N_1 = "AgeLegalRetraiteAnN1";
	public static String PROP_AGE_LEGAL_RETRAITE_AN_N_2 = "AgeLegalRetraiteAnN2";
	public static String PROP_EFFECTIF_MOYEN_AN_N = "EffectifMoyenAnN";
	public static String PROP_EFFECTIF_MOYEN_AN_N_1 = "EffectifMoyenAnN1";
	public static String PROP_EFFECTIF_MOYEN_AN_N_2 = "EffectifMoyenAnN2";
	public static String PROP_POURCENTAGE_FORMATION_AN_N = "PourcentageFormationAnN";
	public static String PROP_POURCENTAGE_FORMATION_AN_N_1 = "PourcentageFormationAnN1";
	public static String PROP_POURCENTAGE_FORMATION_AN_N_2 = "PourcentageFormationAnN2";
	public static String PROP_MASSE_SALARIALE_AN_N = "MasseSalarialeAnN";
	public static String PROP_MASSE_SALARIALE_AN_N_1 = "MasseSalarialeAnN1";
	public static String PROP_MASSE_SALARIALE_AN_N_2 = "MasseSalarialeAnN2";
	public static String PROP_EFFECTIF_PHYSIQUE_AN_N = "EffectifPhysiqueAnN";
	public static String PROP_EFFECTIF_PHYSIQUE_AN_N_1 = "EffectifPhysiqueAnN1";
	public static String PROP_EFFECTIF_PHYSIQUE_AN_N_2 = "EffectifPhysiqueAnN2";
	public static String PROP_EFFECTIF_PHYSIQUE_APPRENTIS_AN_N = "EffectifPhysiqueApprentisAnN";
	public static String PROP_EFFECTIF_PHYSIQUE_APPRENTIS_AN_N_1 = "EffectifPhysiqueApprentisAnN1";
	public static String PROP_EFFECTIF_PHYSIQUE_APPRENTIS_AN_N_2 = "EffectifPhysiqueApprentisAnN2";
	public static String PROP_EFFECTIF_ETP_AN_N = "EffectifEtpAnN";
	public static String PROP_EFFECTIF_ETP_AN_N_1 = "EffectifEtpAnN1";
	public static String PROP_EFFECTIF_ETP_AN_N_2 = "EffectifEtpAnN2";

	// constructors
	public BaseParamsgeneraux() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseParamsgeneraux(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer annee;

	private java.lang.Integer ageLegalRetraiteAnN;
	private java.lang.Integer ageLegalRetraiteAnN1;
	private java.lang.Integer ageLegalRetraiteAnN2;

	private java.lang.Double effectifMoyenAnN;
	private java.lang.Double effectifMoyenAnN1;
	private java.lang.Double effectifMoyenAnN2;

	private java.lang.Double masseSalarialeAnN;
	private java.lang.Double masseSalarialeAnN1;
	private java.lang.Double masseSalarialeAnN2;

	private java.lang.Double pourcentageFormationAnN;
	private java.lang.Double pourcentageFormationAnN1;
	private java.lang.Double pourcentageFormationAnN2;

	private java.lang.Double dureeTravailAnNHeuresRealiseesEffectifTot;
	private java.lang.Double dureeTravailAnN1HeuresRealiseesEffectifTot;
	private java.lang.Double dureeTravailAnN2HeuresRealiseesEffectifTot;

	private java.lang.Double dureeTravailAnNJoursEffectifTot;
	private java.lang.Double dureeTravailAnN1JoursEffectifTot;
	private java.lang.Double dureeTravailAnN2JoursEffectifTot;

	private java.lang.Integer dureeTravailAnNJoursSal;
	private java.lang.Integer dureeTravailAnN1JoursSal;
	private java.lang.Integer dureeTravailAnN2JoursSal;

	private java.lang.Integer dureeTravailAnNHeuresSal;
	private java.lang.Integer dureeTravailAnN1HeuresSal;
	private java.lang.Integer dureeTravailAnN2HeuresSal;

	private java.lang.Integer effectifPhysiqueAnN;
	private java.lang.Integer effectifPhysiqueAnN1;
	private java.lang.Integer effectifPhysiqueAnN2;

	private java.lang.Double effectifEtpAnN;
	private java.lang.Double effectifEtpAnN1;
	private java.lang.Double effectifEtpAnN2;

	// many to one
	private com.cci.gpec.db.Entreprise entreprise;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID_PARAMS_GENERAUX"
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
	 * Return the value associated with the column: AGE_LEGAL_RETRAITE_AN_N
	 */
	public java.lang.Integer getAgeLegalRetraiteAnN() {
		return ageLegalRetraiteAnN;
	}

	/**
	 * Set the value related to the column: AGE_LEGAL_RETRAITE_AN_N
	 * 
	 * @param ageLegalRetraite
	 *            the AGE_LEGAL_RETRAITE_AN_N value
	 */
	public void setAgeLegalRetraiteAnN(java.lang.Integer ageLegalRetraiteAnN) {
		this.ageLegalRetraiteAnN = ageLegalRetraiteAnN;
	}

	/**
	 * Return the value associated with the column: AGE_LEGAL_RETRAITE_AN_N_1
	 */
	public java.lang.Integer getAgeLegalRetraiteAnN1() {
		return ageLegalRetraiteAnN1;
	}

	/**
	 * Set the value related to the column: AGE_LEGAL_RETRAITE_AN_N_1
	 * 
	 * @param ageLegalRetraite
	 *            the AGE_LEGAL_RETRAITE_AN_N_1 value
	 */
	public void setAgeLegalRetraiteAnN1(java.lang.Integer ageLegalRetraiteAnN1) {
		this.ageLegalRetraiteAnN1 = ageLegalRetraiteAnN1;
	}

	/**
	 * Return the value associated with the column: AGE_LEGAL_RETRAITE_AN_N_2
	 */
	public java.lang.Integer getAgeLegalRetraiteAnN2() {
		return ageLegalRetraiteAnN2;
	}

	/**
	 * Set the value related to the column: AGE_LEGAL_RETRAITE_AN_N_2
	 * 
	 * @param ageLegalRetraite
	 *            the AGE_LEGAL_RETRAITE_AN_N_2 value
	 */
	public void setAgeLegalRetraiteAnN2(java.lang.Integer ageLegalRetraiteAnN2) {
		this.ageLegalRetraiteAnN2 = ageLegalRetraiteAnN2;
	}

	/**
	 * Return the value associated with the column:
	 * DUREE_TRAVAIL_AN_N_HEURES_REALISEES_EFFECTIF_TOT
	 */
	public java.lang.Double getDureeTravailAnNHeuresRealiseesEffectifTot() {
		return dureeTravailAnNHeuresRealiseesEffectifTot;
	}

	/**
	 * Set the value related to the column:
	 * DUREE_TRAVAIL_AN_N_HEURES_REALISEES_EFFECTIF_TOT
	 * 
	 * @param dureeTravailAnNJoursEffectifTot
	 *            the DUREE_TRAVAIL_AN_N_HEURES_REALISEES_EFFECTIF_TOT value
	 */
	public void setDureeTravailAnNHeuresRealiseesEffectifTot(
			java.lang.Double dureeTravailAnNHeuresRealiseesEffectifTot) {
		this.dureeTravailAnNHeuresRealiseesEffectifTot = dureeTravailAnNHeuresRealiseesEffectifTot;
	}

	/**
	 * Return the value associated with the column:
	 * DUREE_TRAVAIL_AN_N_1_HEURES_REALISEES_EFFECTIF_TOT
	 */
	public java.lang.Double getDureeTravailAnN1HeuresRealiseesEffectifTot() {
		return dureeTravailAnN1HeuresRealiseesEffectifTot;
	}

	/**
	 * Set the value related to the column:
	 * DUREE_TRAVAIL_AN_N_1_HEURES_REALISEES_EFFECTIF_TOT
	 * 
	 * @param dureeTravailAnN1JoursEffectifTot
	 *            the DUREE_TRAVAIL_AN_N_1_HEURES_REALISEES_EFFECTIF_TOT value
	 */
	public void setDureeTravailAnN1HeuresRealiseesEffectifTot(
			java.lang.Double dureeTravailAnN1HeuresRealiseesEffectifTot) {
		this.dureeTravailAnN1HeuresRealiseesEffectifTot = dureeTravailAnN1HeuresRealiseesEffectifTot;
	}

	/**
	 * Return the value associated with the column:
	 * DUREE_TRAVAIL_AN_N_2_HEURES_REALISEES_EFFECTIF_TOT
	 */
	public java.lang.Double getDureeTravailAnN2HeuresRealiseesEffectifTot() {
		return dureeTravailAnN2HeuresRealiseesEffectifTot;
	}

	/**
	 * Set the value related to the column:
	 * DUREE_TRAVAIL_AN_N_2_HEURES_REALISEES_EFFECTIF_TOT
	 * 
	 * @param dureeTravailAnN2JoursEffectifTot
	 *            the DUREE_TRAVAIL_AN_N_2_HEURES_REALISEES_EFFECTIF_TOT value
	 */
	public void setDureeTravailAnN2HeuresRealiseesEffectifTot(
			java.lang.Double dureeTravailAnN2HeuresRealiseesEffectifTot) {
		this.dureeTravailAnN2HeuresRealiseesEffectifTot = dureeTravailAnN2HeuresRealiseesEffectifTot;
	}

	/**
	 * Return the value associated with the column:
	 * DUREE_TRAVAIL_AN_N_JOURS_EFFECTIF_TOT
	 */
	public java.lang.Double getDureeTravailAnNJoursEffectifTot() {
		return dureeTravailAnNJoursEffectifTot;
	}

	/**
	 * Set the value related to the column:
	 * DUREE_TRAVAIL_AN_N_JOURS_EFFECTIF_TOT
	 * 
	 * @param dureeTravailAnNJoursEffectifTot
	 *            the DUREE_TRAVAIL_AN_N_JOURS_EFFECTIF_TOT value
	 */
	public void setDureeTravailAnNJoursEffectifTot(
			java.lang.Double dureeTravailAnNJoursEffectifTot) {
		this.dureeTravailAnNJoursEffectifTot = dureeTravailAnNJoursEffectifTot;
	}

	/**
	 * Return the value associated with the column:
	 * DUREE_TRAVAIL_AN_N_1_JOURS_EFFECTIF_TOT
	 */
	public java.lang.Double getDureeTravailAnN1JoursEffectifTot() {
		return dureeTravailAnN1JoursEffectifTot;
	}

	/**
	 * Set the value related to the column:
	 * DUREE_TRAVAIL_AN_N_1_JOURS_EFFECTIF_TOT
	 * 
	 * @param dureeTravailAnN1HeuresEffectifTot
	 *            the DUREE_TRAVAIL_AN_N_1_JOURS_EFFECTIF_TOT value
	 */
	public void setDureeTravailAnN1JoursEffectifTot(
			java.lang.Double dureeTravailAnN1JoursEffectifTot) {
		this.dureeTravailAnN1JoursEffectifTot = dureeTravailAnN1JoursEffectifTot;
	}

	/**
	 * Return the value associated with the column:
	 * DUREE_TRAVAIL_AN_N_2_JOURS_EFFECTIF_TOT
	 */
	public java.lang.Double getDureeTravailAnN2JoursEffectifTot() {
		return dureeTravailAnN2JoursEffectifTot;
	}

	/**
	 * Set the value related to the column:
	 * DUREE_TRAVAIL_AN_N_2_JOURS_EFFECTIF_TOT
	 * 
	 * @param dureeTravailAnN2JoursEffectifTot
	 *            the DUREE_TRAVAIL_AN_N_2_JOURS_EFFECTIF_TOT value
	 */
	public void setDureeTravailAnN2JoursEffectifTot(
			java.lang.Double dureeTravailAnN2JoursEffectifTot) {
		this.dureeTravailAnN2JoursEffectifTot = dureeTravailAnN2JoursEffectifTot;
	}

	/**
	 * Return the value associated with the column: DUREE_TRAVAIL_AN_N_JOURS_SAL
	 */
	public java.lang.Integer getDureeTravailAnNJoursSal() {
		return dureeTravailAnNJoursSal;
	}

	/**
	 * Set the value related to the column: DUREE_TRAVAIL_AN_N_JOURS_SAL
	 * 
	 * @param dureeTravailAnNJoursSal
	 *            the DUREE_TRAVAIL_AN_N_JOURS_SAL value
	 */
	public void setDureeTravailAnNJoursSal(
			java.lang.Integer dureeTravailAnNJoursSal) {
		this.dureeTravailAnNJoursSal = dureeTravailAnNJoursSal;
	}

	/**
	 * Return the value associated with the column:
	 * DUREE_TRAVAIL_AN_N_1_JOURS_SAL
	 */
	public java.lang.Integer getDureeTravailAnN1JoursSal() {
		return dureeTravailAnN1JoursSal;
	}

	/**
	 * Set the value related to the column: DUREE_TRAVAIL_AN_N_1_JOURS_SAL
	 * 
	 * @param dureeTravailAnN1JoursSal
	 *            the DUREE_TRAVAIL_AN_N_1_JOURS_SAL value
	 */
	public void setDureeTravailAnN1JoursSal(
			java.lang.Integer dureeTravailAnN1JoursSal) {
		this.dureeTravailAnN1JoursSal = dureeTravailAnN1JoursSal;
	}

	/**
	 * Return the value associated with the column:
	 * DUREE_TRAVAIL_AN_N_2_JOURS_SAL
	 */
	public java.lang.Integer getDureeTravailAnN2JoursSal() {
		return dureeTravailAnN2JoursSal;
	}

	/**
	 * Set the value related to the column: DUREE_TRAVAIL_AN_N_2_JOURS_SAL
	 * 
	 * @param dureeTravailAnN2JoursSal
	 *            the DUREE_TRAVAIL_AN_N_2_JOURS_SAL value
	 */
	public void setDureeTravailAnN2JoursSal(
			java.lang.Integer dureeTravailAnN2JoursSal) {
		this.dureeTravailAnN2JoursSal = dureeTravailAnN2JoursSal;
	}

	/**
	 * Return the value associated with the column:
	 * DUREE_TRAVAIL_AN_N_HEURES_SAL
	 */
	public java.lang.Integer getDureeTravailAnNHeuresSal() {
		return dureeTravailAnNHeuresSal;
	}

	/**
	 * Set the value related to the column: DUREE_TRAVAIL_AN_N_HEURES_SAL
	 * 
	 * @param dureeTravailAnNHeuresSal
	 *            the DUREE_TRAVAIL_AN_N_HEURES_SAL value
	 */
	public void setDureeTravailAnNHeuresSal(
			java.lang.Integer dureeTravailAnNHeuresSal) {
		this.dureeTravailAnNHeuresSal = dureeTravailAnNHeuresSal;
	}

	/**
	 * Return the value associated with the column:
	 * DUREE_TRAVAIL_AN_N_1_HEURES_SAL
	 */
	public java.lang.Integer getDureeTravailAnN1HeuresSal() {
		return dureeTravailAnN1HeuresSal;
	}

	/**
	 * Set the value related to the column: DUREE_TRAVAIL_AN_N_1_HEURES_SAL
	 * 
	 * @param dureeTravailAnN1HeuresSal
	 *            the DUREE_TRAVAIL_AN_N_1_HEURES_SAL value
	 */
	public void setDureeTravailAnN1HeuresSal(
			java.lang.Integer dureeTravailAnN1HeuresSal) {
		this.dureeTravailAnN1HeuresSal = dureeTravailAnN1HeuresSal;
	}

	/**
	 * Return the value associated with the column:
	 * DUREE_TRAVAIL_AN_N_2_HEURES_SAL
	 */
	public java.lang.Integer getDureeTravailAnN2HeuresSal() {
		return dureeTravailAnN2HeuresSal;
	}

	/**
	 * Set the value related to the column: DUREE_TRAVAIL_AN_N_2_HEURES_SAL
	 * 
	 * @param dureeTravailAnN2HeuresSal
	 *            the DUREE_TRAVAIL_AN_N_2_HEURES_SAL value
	 */
	public void setDureeTravailAnN2HeuresSal(
			java.lang.Integer dureeTravailAnN2HeuresSal) {
		this.dureeTravailAnN2HeuresSal = dureeTravailAnN2HeuresSal;
	}

	/**
	 * Return the value associated with the column: ID_ENTREPRISE
	 */
	public com.cci.gpec.db.Entreprise getEntreprise() {
		return entreprise;
	}

	/**
	 * Set the value related to the column: ID_ENTREPRISE
	 * 
	 * @param idEntreprise
	 *            the ID_ENTREPRISE value
	 */
	public void setEntreprise(com.cci.gpec.db.Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.Paramsgeneraux))
			return false;
		else {
			com.cci.gpec.db.Paramsgeneraux paramsgeneraux = (com.cci.gpec.db.Paramsgeneraux) obj;
			if (null == this.getId() || null == paramsgeneraux.getId())
				return false;
			else
				return (this.getId().equals(paramsgeneraux.getId()));
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

	public java.lang.Double getEffectifMoyenAnN() {
		return effectifMoyenAnN;
	}

	public void setEffectifMoyenAnN(java.lang.Double effectifMoyenAnN) {
		this.effectifMoyenAnN = effectifMoyenAnN;
	}

	public java.lang.Double getEffectifMoyenAnN1() {
		return effectifMoyenAnN1;
	}

	public void setEffectifMoyenAnN1(java.lang.Double effectifMoyenAnN1) {
		this.effectifMoyenAnN1 = effectifMoyenAnN1;
	}

	public java.lang.Double getEffectifMoyenAnN2() {
		return effectifMoyenAnN2;
	}

	public void setEffectifMoyenAnN2(java.lang.Double effectifMoyenAnN2) {
		this.effectifMoyenAnN2 = effectifMoyenAnN2;
	}

	public java.lang.Double getMasseSalarialeAnN() {
		return masseSalarialeAnN;
	}

	public void setMasseSalarialeAnN(java.lang.Double masseSalarialeAnN) {
		this.masseSalarialeAnN = masseSalarialeAnN;
	}

	public java.lang.Double getMasseSalarialeAnN1() {
		return masseSalarialeAnN1;
	}

	public void setMasseSalarialeAnN1(java.lang.Double masseSalarialeAnN1) {
		this.masseSalarialeAnN1 = masseSalarialeAnN1;
	}

	public java.lang.Double getMasseSalarialeAnN2() {
		return masseSalarialeAnN2;
	}

	public void setMasseSalarialeAnN2(java.lang.Double masseSalarialeAnN2) {
		this.masseSalarialeAnN2 = masseSalarialeAnN2;
	}

	public java.lang.Double getPourcentageFormationAnN() {
		return pourcentageFormationAnN;
	}

	public void setPourcentageFormationAnN(
			java.lang.Double pourcentageFormationAnN) {
		this.pourcentageFormationAnN = pourcentageFormationAnN;
	}

	public java.lang.Double getPourcentageFormationAnN1() {
		return pourcentageFormationAnN1;
	}

	public void setPourcentageFormationAnN1(
			java.lang.Double pourcentageFormationAnN1) {
		this.pourcentageFormationAnN1 = pourcentageFormationAnN1;
	}

	public java.lang.Double getPourcentageFormationAnN2() {
		return pourcentageFormationAnN2;
	}

	public void setPourcentageFormationAnN2(
			java.lang.Double pourcentageFormationAnN2) {
		this.pourcentageFormationAnN2 = pourcentageFormationAnN2;
	}

	public java.lang.Integer getEffectifPhysiqueAnN() {
		return effectifPhysiqueAnN;
	}

	public void setEffectifPhysiqueAnN(java.lang.Integer effectifPhysiqueAnN) {
		this.effectifPhysiqueAnN = effectifPhysiqueAnN;
	}

	public java.lang.Integer getEffectifPhysiqueAnN1() {
		return effectifPhysiqueAnN1;
	}

	public void setEffectifPhysiqueAnN1(java.lang.Integer effectifPhysiqueAnN1) {
		this.effectifPhysiqueAnN1 = effectifPhysiqueAnN1;
	}

	public java.lang.Integer getEffectifPhysiqueAnN2() {
		return effectifPhysiqueAnN2;
	}

	public void setEffectifPhysiqueAnN2(java.lang.Integer effectifPhysiqueAnN2) {
		this.effectifPhysiqueAnN2 = effectifPhysiqueAnN2;
	}

	public java.lang.Double getEffectifEtpAnN() {
		return effectifEtpAnN;
	}

	public void setEffectifEtpAnN(java.lang.Double effectifEtpAnN) {
		this.effectifEtpAnN = effectifEtpAnN;
	}

	public java.lang.Double getEffectifEtpAnN1() {
		return effectifEtpAnN1;
	}

	public void setEffectifEtpAnN1(java.lang.Double effectifEtpAnN1) {
		this.effectifEtpAnN1 = effectifEtpAnN1;
	}

	public java.lang.Double getEffectifEtpAnN2() {
		return effectifEtpAnN2;
	}

	public void setEffectifEtpAnN2(java.lang.Double effectifEtpAnN2) {
		this.effectifEtpAnN2 = effectifEtpAnN2;
	}

	public java.lang.Integer getAnnee() {
		return annee;
	}

	public void setAnnee(java.lang.Integer annee) {
		this.annee = annee;
	}

}