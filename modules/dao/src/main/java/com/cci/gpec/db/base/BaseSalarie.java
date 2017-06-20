package com.cci.gpec.db.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the SALARIE table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="SALARIE"
 */

public abstract class BaseSalarie implements Serializable {

	public static String REF = "Salarie";
	public static String PROP_CIVILITE = "Civilite";
	public static String PROP_TELEPHONE = "Telephone";
	public static String PROP_CREDIT_DIF = "CreditDif";
	public static String PROP_ID_LIEN_SUBORDINATION = "IdLienSubordination";
	public static String PROP_NIV_FORMATION_INIT = "NivFormationInit";
	public static String PROP_PRENOM = "Prenom";
	public static String PROP_CV = "Cv";
	public static String PROP_ADRESSE = "Adresse";
	public static String PROP_DATE_NAISSANCE = "DateNaissance";
	public static String PROP_NOM = "Nom";
	public static String PROP_NIV_FORMATION_ATTEINT = "NivFormationAtteint";
	public static String PROP_ID_SERVICE = "IdService";
	public static String PROP_ID_ENTREPRISE = "IdEntreprise";
	public static String PROP_LIEU_NAISSANCE = "LieuNaissance";
	public static String PROP_DATE_LAST_SAISIE_DIF = "DateLastSaisieDif";
	public static String PROP_ID = "Id";
	public static String PROP_PRESENT = "Present";
	public static String PROP_TELEPHONE_PORTABLE = "TelephonePortable";
	public static String PROP_SITUATION_FAMILIALE = "SituationFamiliale";
	public static String PROP_PERSONNE_A_PREVENIR_NOM = "PersonneAPrevenirNom";
	public static String PROP_PERSONNE_A_PREVENIR_PRENOM = "PersonneAPrevenirPrenom";
	public static String PROP_PERSONNE_A_PREVENIR_ADRESSE = "PersonneAPrevenirAdresse";
	public static String PROP_PERSONNE_A_PREVENIR_TELEPHONE = "PersonneAPrevenirTelephone";
	public static String PROP_PERSONNE_A_PREVENIR_LIEN_PARENTE = "PersonneAPrevenirLienParente";
	public static String PROP_IMPRESSION = "Impression";
	public static String PROP_COMMENTAIRE = "Commentaire";
	public static String PROP_POSSEDE_PERMIS_CONDUIRE = "PossedePermisConduire";

	// constructors
	public BaseSalarie() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseSalarie(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String adresse;
	private java.lang.String civilite;
	private java.lang.Double creditDif;
	private java.lang.String cv;
	private java.util.Date dateLastSaisieDif;
	private java.util.Date dateNaissance;
	private java.lang.Integer idLienSubordination;
	private java.lang.String lieuNaissance;
	private java.lang.String nivFormationAtteint;
	private java.lang.String nivFormationInit;
	private java.lang.String nom;
	private java.lang.String prenom;
	private java.lang.String telephone;
	private java.lang.Boolean present;
	private java.lang.String telephonePortable;
	private java.lang.String situationFamiliale;
	private java.lang.String personneAPrevenirNom;
	private java.lang.String personneAPrevenirPrenom;
	private java.lang.String personneAPrevenirAdresse;
	private java.lang.String personneAPrevenirTelephone;
	private java.lang.String personneAPrevenirLienParente;
	private java.lang.Boolean impression;
	private java.lang.Boolean possedePermisConduire;
	private java.lang.String commentaire;

	// many to one
	private com.cci.gpec.db.Entreprise entreprise;
	private com.cci.gpec.db.Service service;

	// collections
	private java.util.Set<com.cci.gpec.db.Absence> aBSENCEs;
	private java.util.Set<com.cci.gpec.db.Accident> aCCIDENTs;
	private java.util.Set<com.cci.gpec.db.Entretien> eNTRETIENs;
	private java.util.Set<com.cci.gpec.db.Formation> fORMATIONs;
	private java.util.Set<com.cci.gpec.db.Habilitation> hABILITATIONs;
	private java.util.Set<com.cci.gpec.db.Parcours> pARCOURSs;
	private java.util.Set<com.cci.gpec.db.RevenuComplementaire> rEVENUSCOMPLEMENTAIREs;
	private java.util.Set<com.cci.gpec.db.FicheDePoste> fICHEDEPOSTEs;
	private java.util.Set<com.cci.gpec.db.ContratTravail> cONTRATSTRAVAILs;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID_SALARIE"
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
	 * Return the value associated with the column: ADRESSE
	 */
	public java.lang.String getAdresse() {
		return adresse;
	}

	/**
	 * Set the value related to the column: ADRESSE
	 * 
	 * @param adresse
	 *            the ADRESSE value
	 */
	public void setAdresse(java.lang.String adresse) {
		this.adresse = adresse;
	}

	/**
	 * Return the value associated with the column: CIVILITE
	 */
	public java.lang.String getCivilite() {
		return civilite;
	}

	/**
	 * Set the value related to the column: CIVILITE
	 * 
	 * @param civilite
	 *            the CIVILITE value
	 */
	public void setCivilite(java.lang.String civilite) {
		this.civilite = civilite;
	}

	/**
	 * Return the value associated with the column: CREDIT_DIF
	 */
	public java.lang.Double getCreditDif() {
		return creditDif;
	}

	/**
	 * Set the value related to the column: CREDIT_DIF
	 * 
	 * @param creditDif
	 *            the CREDIT_DIF value
	 */
	public void setCreditDif(java.lang.Double creditDif) {
		this.creditDif = creditDif;
	}

	/**
	 * Return the value associated with the column: CV
	 */
	public java.lang.String getCv() {
		return cv;
	}

	/**
	 * Set the value related to the column: CV
	 * 
	 * @param cv
	 *            the CV value
	 */
	public void setCv(java.lang.String cv) {
		this.cv = cv;
	}

	/**
	 * Return the value associated with the column: DATE_LAST_SAISIE_DIF
	 */
	public java.util.Date getDateLastSaisieDif() {
		return dateLastSaisieDif;
	}

	/**
	 * Set the value related to the column: DATE_LAST_SAISIE_DIF
	 * 
	 * @param dateLastSaisieDif
	 *            the DATE_LAST_SAISIE_DIF value
	 */
	public void setDateLastSaisieDif(java.util.Date dateLastSaisieDif) {
		this.dateLastSaisieDif = dateLastSaisieDif;
	}

	/**
	 * Return the value associated with the column: DATE_NAISSANCE
	 */
	public java.util.Date getDateNaissance() {
		return dateNaissance;
	}

	/**
	 * Set the value related to the column: DATE_NAISSANCE
	 * 
	 * @param dateNaissance
	 *            the DATE_NAISSANCE value
	 */
	public void setDateNaissance(java.util.Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	/**
	 * Return the value associated with the column: ID_LIEN_SUBORDINATION
	 */
	public java.lang.Integer getIdLienSubordination() {
		return idLienSubordination;
	}

	/**
	 * Set the value related to the column: ID_LIEN_SUBORDINATION
	 * 
	 * @param idLienSubordination
	 *            the ID_LIEN_SUBORDINATION value
	 */
	public void setIdLienSubordination(java.lang.Integer idLienSubordination) {
		this.idLienSubordination = idLienSubordination;
	}

	/**
	 * Return the value associated with the column: LIEU_NAISSANCE
	 */
	public java.lang.String getLieuNaissance() {
		return lieuNaissance;
	}

	/**
	 * Set the value related to the column: LIEU_NAISSANCE
	 * 
	 * @param lieuNaissance
	 *            the LIEU_NAISSANCE value
	 */
	public void setLieuNaissance(java.lang.String lieuNaissance) {
		this.lieuNaissance = lieuNaissance;
	}

	/**
	 * Return the value associated with the column: NIV_FORMATION_ATTEINT
	 */
	public java.lang.String getNivFormationAtteint() {
		return nivFormationAtteint;
	}

	/**
	 * Set the value related to the column: NIV_FORMATION_ATTEINT
	 * 
	 * @param nivFormationAtteint
	 *            the NIV_FORMATION_ATTEINT value
	 */
	public void setNivFormationAtteint(java.lang.String nivFormationAtteint) {
		this.nivFormationAtteint = nivFormationAtteint;
	}

	/**
	 * Return the value associated with the column: NIV_FORMATION_INIT
	 */
	public java.lang.String getNivFormationInit() {
		return nivFormationInit;
	}

	/**
	 * Set the value related to the column: NIV_FORMATION_INIT
	 * 
	 * @param nivFormationInit
	 *            the NIV_FORMATION_INIT value
	 */
	public void setNivFormationInit(java.lang.String nivFormationInit) {
		this.nivFormationInit = nivFormationInit;
	}

	/**
	 * Return the value associated with the column: NOM
	 */
	public java.lang.String getNom() {
		return nom;
	}

	/**
	 * Set the value related to the column: NOM
	 * 
	 * @param nom
	 *            the NOM value
	 */
	public void setNom(java.lang.String nom) {
		this.nom = nom;
	}

	/**
	 * Return the value associated with the column: PRENOM
	 */
	public java.lang.String getPrenom() {
		return prenom;
	}

	/**
	 * Set the value related to the column: PRENOM
	 * 
	 * @param prenom
	 *            the PRENOM value
	 */
	public void setPrenom(java.lang.String prenom) {
		this.prenom = prenom;
	}

	/**
	 * Return the value associated with the column: TELEPHONE
	 */
	public java.lang.String getTelephone() {
		return telephone;
	}

	/**
	 * Set the value related to the column: TELEPHONE
	 * 
	 * @param telephone
	 *            the TELEPHONE value
	 */
	public void setTelephone(java.lang.String telephone) {
		this.telephone = telephone;
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

	/**
	 * Return the value associated with the column: ID_SERVICE
	 */
	public com.cci.gpec.db.Service getService() {
		return service;
	}

	/**
	 * Set the value related to the column: ID_SERVICE
	 * 
	 * @param idService
	 *            the ID_SERVICE value
	 */
	public void setService(com.cci.gpec.db.Service service) {
		this.service = service;
	}

	/**
	 * Return the value associated with the column: ABSENCEs
	 */
	public java.util.Set<com.cci.gpec.db.Absence> getABSENCEs() {
		return aBSENCEs;
	}

	/**
	 * Set the value related to the column: ABSENCEs
	 * 
	 * @param aBSENCEs
	 *            the ABSENCEs value
	 */
	public void setABSENCEs(java.util.Set<com.cci.gpec.db.Absence> aBSENCEs) {
		this.aBSENCEs = aBSENCEs;
	}

	public void addToABSENCEs(com.cci.gpec.db.Absence absence) {
		if (null == getABSENCEs()) {
			setABSENCEs(new java.util.TreeSet<com.cci.gpec.db.Absence>());
		}
		getABSENCEs().add(absence);
	}

	/**
	 * Return the value associated with the column: ACCIDENTs
	 */
	public java.util.Set<com.cci.gpec.db.Accident> getACCIDENTs() {
		return aCCIDENTs;
	}

	/**
	 * Set the value related to the column: ACCIDENTs
	 * 
	 * @param aCCIDENTs
	 *            the ACCIDENTs value
	 */
	public void setACCIDENTs(java.util.Set<com.cci.gpec.db.Accident> aCCIDENTs) {
		this.aCCIDENTs = aCCIDENTs;
	}

	public void addToACCIDENTs(com.cci.gpec.db.Accident accident) {
		if (null == getACCIDENTs()) {
			setACCIDENTs(new java.util.TreeSet<com.cci.gpec.db.Accident>());
		}
		getACCIDENTs().add(accident);
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
		if (null == getENTRETIENs()) {
			setENTRETIENs(new java.util.TreeSet<com.cci.gpec.db.Entretien>());
		}
		getENTRETIENs().add(entretien);
	}

	/**
	 * Return the value associated with the column: FORMATIONs
	 */
	public java.util.Set<com.cci.gpec.db.Formation> getFORMATIONs() {
		return fORMATIONs;
	}

	/**
	 * Set the value related to the column: FORMATIONs
	 * 
	 * @param fORMATIONs
	 *            the FORMATIONs value
	 */
	public void setFORMATIONs(
			java.util.Set<com.cci.gpec.db.Formation> fORMATIONs) {
		this.fORMATIONs = fORMATIONs;
	}

	public void addToFORMATIONs(com.cci.gpec.db.Formation formation) {
		if (null == getFORMATIONs()) {
			setFORMATIONs(new java.util.TreeSet<com.cci.gpec.db.Formation>());
		}
		getFORMATIONs().add(formation);
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
		if (null == getHABILITATIONs()) {
			setHABILITATIONs(new java.util.TreeSet<com.cci.gpec.db.Habilitation>());
		}
		getHABILITATIONs().add(habilitation);
	}

	/**
	 * Return the value associated with the column: rEVENUSCOMPLEMENTAIREs
	 */
	public java.util.Set<com.cci.gpec.db.RevenuComplementaire> getREVENUSCOMPLEMENTAIREs() {
		return rEVENUSCOMPLEMENTAIREs;
	}

	/**
	 * Set the value related to the column: PARCOURSs
	 * 
	 * @param pARCOURSs
	 *            the PARCOURSs value
	 */
	public void setREVENUSCOMPLEMENTAIREs(
			java.util.Set<com.cci.gpec.db.RevenuComplementaire> rEVENUSCOMPLEMENTAIREs) {
		this.rEVENUSCOMPLEMENTAIREs = rEVENUSCOMPLEMENTAIREs;
	}

	public void addToREVENUSCOMPLEMENTAIREs(
			com.cci.gpec.db.RevenuComplementaire rEVENUSCOMPLEMENTAIREs) {
		if (null == getREVENUSCOMPLEMENTAIREs()) {
			setREVENUSCOMPLEMENTAIREs(new java.util.TreeSet<com.cci.gpec.db.RevenuComplementaire>());
		}
		getREVENUSCOMPLEMENTAIREs().add(rEVENUSCOMPLEMENTAIREs);
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
		if (null == getPARCOURSs()) {
			setPARCOURSs(new java.util.TreeSet<com.cci.gpec.db.Parcours>());
		}
		getPARCOURSs().add(parcours);
	}

	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof com.cci.gpec.db.Salarie)) {
			return false;
		} else {
			com.cci.gpec.db.Salarie salarie = (com.cci.gpec.db.Salarie) obj;
			if (null == this.getId() || null == salarie.getId()) {
				return false;
			} else {
				return (this.getId().equals(salarie.getId()));
			}
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) {
				return super.hashCode();
			} else {
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

	public java.lang.Boolean getPresent() {
		return present;
	}

	public void setPresent(java.lang.Boolean present) {
		this.present = present;
	}

	public java.lang.String getTelephonePortable() {
		return telephonePortable;
	}

	public void setTelephonePortable(java.lang.String telephonePortable) {
		this.telephonePortable = telephonePortable;
	}

	public java.lang.String getSituationFamiliale() {
		return situationFamiliale;
	}

	public void setSituationFamiliale(java.lang.String situationFamiliale) {
		this.situationFamiliale = situationFamiliale;
	}

	public java.lang.String getPersonneAPrevenirNom() {
		return personneAPrevenirNom;
	}

	public void setPersonneAPrevenirNom(java.lang.String personneAPrevenirNom) {
		this.personneAPrevenirNom = personneAPrevenirNom;
	}

	public java.lang.String getPersonneAPrevenirPrenom() {
		return personneAPrevenirPrenom;
	}

	public void setPersonneAPrevenirPrenom(
			java.lang.String personneAPrevenirPrenom) {
		this.personneAPrevenirPrenom = personneAPrevenirPrenom;
	}

	public java.lang.String getPersonneAPrevenirAdresse() {
		return personneAPrevenirAdresse;
	}

	public void setPersonneAPrevenirAdresse(
			java.lang.String personneAPrevenirAdresse) {
		this.personneAPrevenirAdresse = personneAPrevenirAdresse;
	}

	public java.lang.String getPersonneAPrevenirTelephone() {
		return personneAPrevenirTelephone;
	}

	public void setPersonneAPrevenirTelephone(
			java.lang.String personneAPrevenirTelephone) {
		this.personneAPrevenirTelephone = personneAPrevenirTelephone;
	}

	public java.lang.String getPersonneAPrevenirLienParente() {
		return personneAPrevenirLienParente;
	}

	public void setPersonneAPrevenirLienParente(
			java.lang.String personneAPrevenirLienParente) {
		this.personneAPrevenirLienParente = personneAPrevenirLienParente;
	}

	public java.lang.Boolean getImpression() {
		return impression;
	}

	public void setImpression(java.lang.Boolean impression) {
		this.impression = impression;
	}

	public java.lang.String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(java.lang.String commentaire) {
		this.commentaire = commentaire;
	}

	public java.lang.Boolean getPossedePermisConduire() {
		return possedePermisConduire;
	}

	public void setPossedePermisConduire(java.lang.Boolean possedePermisConduire) {
		this.possedePermisConduire = possedePermisConduire;
	}

	public java.util.Set<com.cci.gpec.db.FicheDePoste> getFICHEDEPOSTEs() {
		return fICHEDEPOSTEs;
	}

	public void setFICHEDEPOSTEs(java.util.Set<com.cci.gpec.db.FicheDePoste> es) {
		fICHEDEPOSTEs = es;
	}

	public java.util.Set<com.cci.gpec.db.ContratTravail> getCONTRATSTRAVAILs() {
		return cONTRATSTRAVAILs;
	}

	public void setCONTRATSTRAVAILs(
			java.util.Set<com.cci.gpec.db.ContratTravail> cONTRATSTRAVAILs) {
		this.cONTRATSTRAVAILs = cONTRATSTRAVAILs;
	}

}