package com.cci.gpec.db.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the ENTRETIEN table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="ENTRETIEN"
 */

public abstract class BaseEntretien implements Serializable {

	public static String REF = "Entretien";
	public static String PROP_ID_SALARIE = "IdSalarie";
	public static String PROP_SOUHAIT = "Souhait";
	public static String PROP_FORMATIONS = "Formations";
	public static String PROP_FORMATIONS2 = "Formations2";
	public static String PROP_FORMATIONS3 = "Formations3";
	public static String PROP_FORMATIONS4 = "Formations4";
	public static String PROP_FORMATIONS5 = "Formations5";
	public static String PROP_CR_SIGNE = "CrSigne";
	public static String PROP_COMPETENCE = "Competence";
	public static String PROP_PRINCIPALE_CONCLUSION = "PrincipaleConclusion";
	public static String PROP_ID = "Id";
	public static String PROP_DATE_ENTRETIEN = "DateEntretien";
	public static String PROP_NOM_MANAGER = "NomManager";
	public static String PROP_SERVICE_MANAGER = "ServiceManager";
	public static String PROP_BILAN_DESSOUS = "BilanDessous";
	public static String PROP_BILAN_EGAL = "BilanEgal";
	public static String PROP_BILAN_DESSUS = "BilanDessus";
	public static String PROP_COMMENTAIRE_BILAN = "CommentaireBilan";
	public static String PROP_FORMATION_N_MOINS_UN = "FormationNMoinsUn";
	public static String PROP_COMMENTAIRE_FORMATION = "CommentaireFormation";
	public static String PROP_DOMAINES_FORMATION = "DomainesFormation";
	public static String PROP_DOMAINES_FORMATION2 = "DomainesFormation2";
	public static String PROP_DOMAINES_FORMATION3 = "DomainesFormation3";
	public static String PROP_DOMAINES_FORMATION4 = "DomainesFormation4";
	public static String PROP_DOMAINES_FORMATION5 = "DomainesFormation5";
	public static String PROP_SYNTHESE = "Synthese";
	public static String PROP_OBJ_INTITULE = "ObjIntitule";
	public static String PROP_OBJ_DELAIS = "ObjDelais";
	public static String PROP_OBJ_MOYENS = "ObjMoyens";
	public static String PROP_OBJ_CRITERES = "ObjCriteres";
	public static String PROP_EVOLUTION_PERSO = "EvolutionPerso";
	public static String PROP_OBSERVATIONS = "Observations";
	public static String PROP_MODIF_PROFIL = "ModifProfil";
	public static String PROP_JUSTIFICATIF = "Justificatif";

	// constructors
	public BaseEntretien() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseEntretien(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer anneeReference;
	private java.lang.String principaleConclusion;
	private java.lang.String souhait;
	private java.lang.String competence;
	private java.lang.String evaluationGlobale;
	private java.util.Date dateEntretien;
	private java.lang.String crSigne;
	private java.lang.String formations;
	private java.lang.String formations2;
	private java.lang.String formations3;
	private java.lang.String formations4;
	private java.lang.String formations5;
	private java.lang.String nomManager;
	private java.lang.String serviceManager;
	private java.lang.String bilanDessous;
	private java.lang.String bilanEgal;
	private java.lang.String bilanDessus;
	private java.lang.String commentaireBilan;
	private java.lang.String formationNMoinsUn;
	private java.lang.String commentaireFormation;
	private java.lang.Integer domainesFormation;
	private java.lang.Integer domainesFormation2;
	private java.lang.Integer domainesFormation3;
	private java.lang.Integer domainesFormation4;
	private java.lang.Integer domainesFormation5;
	private java.lang.String synthese;
	private java.lang.String objIntitule;
	private java.lang.String objDelais;
	private java.lang.String objMoyens;
	private java.lang.String objCriteres;
	private java.lang.String evolutionPerso;
	private java.lang.String observations;
	private java.lang.String modifProfil;
	private java.lang.String justificatif;

	// many to one
	private com.cci.gpec.db.Salarie salarie;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID_ENTRETIEN"
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
	 * Return the value associated with the column: PRINCIPALE_CONCLUSION
	 */
	public java.lang.String getPrincipaleConclusion() {
		return principaleConclusion;
	}

	/**
	 * Set the value related to the column: PRINCIPALE_CONCLUSION
	 * 
	 * @param principaleConclusion
	 *            the PRINCIPALE_CONCLUSION value
	 */
	public void setPrincipaleConclusion(java.lang.String principaleConclusion) {
		this.principaleConclusion = principaleConclusion;
	}

	/**
	 * Return the value associated with the column: SOUHAIT
	 */
	public java.lang.String getSouhait() {
		return souhait;
	}

	/**
	 * Set the value related to the column: SOUHAIT
	 * 
	 * @param souhait
	 *            the SOUHAIT value
	 */
	public void setSouhait(java.lang.String souhait) {
		this.souhait = souhait;
	}

	/**
	 * Return the value associated with the column: COMPETENCE
	 */
	public java.lang.String getCompetence() {
		return competence;
	}

	/**
	 * Set the value related to the column: COMPETENCE
	 * 
	 * @param competence
	 *            the COMPETENCE value
	 */
	public void setCompetence(java.lang.String competence) {
		this.competence = competence;
	}

	/**
	 * Return the value associated with the column: DATE_ENTRETIEN
	 */
	public java.util.Date getDateEntretien() {
		return dateEntretien;
	}

	/**
	 * Set the value related to the column: DATE_ENTRETIEN
	 * 
	 * @param dateEntretien
	 *            the DATE_ENTRETIEN value
	 */
	public void setDateEntretien(java.util.Date dateEntretien) {
		this.dateEntretien = dateEntretien;
	}

	/**
	 * Return the value associated with the column: CR_SIGNE
	 */
	public java.lang.String getCrSigne() {
		return crSigne;
	}

	/**
	 * Set the value related to the column: CR_SIGNE
	 * 
	 * @param crSigne
	 *            the CR_SIGNE value
	 */
	public void setCrSigne(java.lang.String crSigne) {
		this.crSigne = crSigne;
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

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.Entretien))
			return false;
		else {
			com.cci.gpec.db.Entretien entretien = (com.cci.gpec.db.Entretien) obj;
			if (null == this.getId() || null == entretien.getId())
				return false;
			else
				return (this.getId().equals(entretien.getId()));
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

	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public java.lang.String getFormations() {
		return formations;
	}

	public void setFormations(java.lang.String formations) {
		this.formations = formations;
	}

	public java.lang.String getNomManager() {
		return nomManager;
	}

	public void setNomManager(java.lang.String nomManager) {
		this.nomManager = nomManager;
	}

	public java.lang.String getServiceManager() {
		return serviceManager;
	}

	public void setServiceManager(java.lang.String serviceManager) {
		this.serviceManager = serviceManager;
	}

	public java.lang.String getBilanDessous() {
		return bilanDessous;
	}

	public void setBilanDessous(java.lang.String bilanDessous) {
		this.bilanDessous = bilanDessous;
	}

	public java.lang.String getBilanEgal() {
		return bilanEgal;
	}

	public void setBilanEgal(java.lang.String bilanEgal) {
		this.bilanEgal = bilanEgal;
	}

	public java.lang.String getBilanDessus() {
		return bilanDessus;
	}

	public void setBilanDessus(java.lang.String bilanDessus) {
		this.bilanDessus = bilanDessus;
	}

	public java.lang.String getCommentaireBilan() {
		return commentaireBilan;
	}

	public void setCommentaireBilan(java.lang.String commentaireBilan) {
		this.commentaireBilan = commentaireBilan;
	}

	public java.lang.String getFormationNMoinsUn() {
		return formationNMoinsUn;
	}

	public void setFormationNMoinsUn(java.lang.String formationNMoinsUn) {
		this.formationNMoinsUn = formationNMoinsUn;
	}

	public java.lang.String getCommentaireFormation() {
		return commentaireFormation;
	}

	public void setCommentaireFormation(java.lang.String commentaireFormation) {
		this.commentaireFormation = commentaireFormation;
	}

	public java.lang.Integer getDomainesFormation() {
		return domainesFormation;
	}

	public void setDomainesFormation(java.lang.Integer domainesFormation) {
		this.domainesFormation = domainesFormation;
	}

	public java.lang.String getSynthese() {
		return synthese;
	}

	public void setSynthese(java.lang.String synthese) {
		this.synthese = synthese;
	}

	public java.lang.String getObjIntitule() {
		return objIntitule;
	}

	public void setObjIntitule(java.lang.String objIntitule) {
		this.objIntitule = objIntitule;
	}

	public java.lang.String getObjDelais() {
		return objDelais;
	}

	public void setObjDelais(java.lang.String objDelais) {
		this.objDelais = objDelais;
	}

	public java.lang.String getObjMoyens() {
		return objMoyens;
	}

	public void setObjMoyens(java.lang.String objMoyens) {
		this.objMoyens = objMoyens;
	}

	public java.lang.String getObjCriteres() {
		return objCriteres;
	}

	public void setObjCriteres(java.lang.String objCriteres) {
		this.objCriteres = objCriteres;
	}

	public java.lang.String getEvolutionPerso() {
		return evolutionPerso;
	}

	public void setEvolutionPerso(java.lang.String evolutionPerso) {
		this.evolutionPerso = evolutionPerso;
	}

	public java.lang.String getObservations() {
		return observations;
	}

	public void setObservations(java.lang.String observations) {
		this.observations = observations;
	}

	public java.lang.String getModifProfil() {
		return modifProfil;
	}

	public void setModifProfil(java.lang.String modifProfil) {
		this.modifProfil = modifProfil;
	}

	public java.lang.String getFormations2() {
		return formations2;
	}

	public void setFormations2(java.lang.String formations2) {
		this.formations2 = formations2;
	}

	public java.lang.String getFormations3() {
		return formations3;
	}

	public void setFormations3(java.lang.String formations3) {
		this.formations3 = formations3;
	}

	public java.lang.String getFormations4() {
		return formations4;
	}

	public void setFormations4(java.lang.String formations4) {
		this.formations4 = formations4;
	}

	public java.lang.String getFormations5() {
		return formations5;
	}

	public void setFormations5(java.lang.String formations5) {
		this.formations5 = formations5;
	}

	public java.lang.Integer getDomainesFormation2() {
		return domainesFormation2;
	}

	public void setDomainesFormation2(java.lang.Integer domainesFormation2) {
		this.domainesFormation2 = domainesFormation2;
	}

	public java.lang.Integer getDomainesFormation3() {
		return domainesFormation3;
	}

	public void setDomainesFormation3(java.lang.Integer domainesFormation3) {
		this.domainesFormation3 = domainesFormation3;
	}

	public java.lang.Integer getDomainesFormation4() {
		return domainesFormation4;
	}

	public void setDomainesFormation4(java.lang.Integer domainesFormation4) {
		this.domainesFormation4 = domainesFormation4;
	}

	public java.lang.Integer getDomainesFormation5() {
		return domainesFormation5;
	}

	public void setDomainesFormation5(java.lang.Integer domainesFormation5) {
		this.domainesFormation5 = domainesFormation5;
	}

	public java.lang.String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(java.lang.String justificatif) {
		this.justificatif = justificatif;
	}

	public java.lang.String getEvaluationGlobale() {
		return evaluationGlobale;
	}

	public void setEvaluationGlobale(java.lang.String evaluationGlobale) {
		this.evaluationGlobale = evaluationGlobale;
	}

	public java.lang.Integer getAnneeReference() {
		return anneeReference;
	}

	public void setAnneeReference(java.lang.Integer anneeReference) {
		this.anneeReference = anneeReference;
	}

}