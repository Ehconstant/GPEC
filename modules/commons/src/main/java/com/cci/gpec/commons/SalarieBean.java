package com.cci.gpec.commons;

import java.text.Collator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SalarieBean implements Comparable<SalarieBean> {

	// primary key
	private Integer id;

	// fields
	private String civilite;
	private String nom;
	private String prenom;
	private String adresse;
	private Date dateNaissance;
	private String lieuNaissance;
	private String telephone;
	private Double creditDif;
	private Integer idLienSubordination;
	private String nivFormationInit;
	private String nivFormationAtteint;
	private String cv;
	private boolean present;
	private String telephonePortable;
	private String situationFamiliale;
	private String personneAPrevenirNom;
	private String personneAPrevenirPrenom;
	private String personneAPrevenirAdresse;
	private String personneAPrevenirTelephone;
	private String personneAPrevenirLienParente;
	private boolean impression;
	private boolean possedePermisConduire;
	private String commentaire;
	private boolean canDeleteSalarie;

	private Date dateLastSaisieDif;

	private List<HabilitationBean> habilitationBeanList;
	private List<AbsenceBean> absenceBeanList;
	private List<AccidentBean> accidentBeanList;
	// private List<CVBean>cvBeanList;
	private List<EntretienBean> entretienBeanList;
	private List<FormationBean> formationBeanList;
	private List<ParcoursBean> parcoursBeanList;
	private List<PersonneAChargeBean> personneAChargeBeanList;

	private boolean hasEvenement;
	private boolean hasRemu;

	private FicheDePosteBean ficheDePoste;

	private Integer idEntrepriseSelected;
	private String nomEntreprise;
	private Integer idServiceSelected;
	private String nomService;
	private boolean changeCouleur;

	private final Collator collator = Collator.getInstance(Locale.FRENCH);

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public String getCivilite() {
		return civilite;
	}

	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getLieuNaissance() {
		return lieuNaissance;
	}

	public void setLieuNaissance(String lieuNaissance) {
		this.lieuNaissance = lieuNaissance;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Double getCreditDif() {
		return creditDif;
	}

	public void setCreditDif(Double creditDif) {
		this.creditDif = creditDif;
	}

	public List<HabilitationBean> getHabilitationBeanList() {
		return habilitationBeanList;
	}

	public void setHabilitationBeanList(
			List<HabilitationBean> habilitationBeanList) {
		this.habilitationBeanList = habilitationBeanList;
	}

	public List<AbsenceBean> getAbsenceBeanList() {
		return absenceBeanList;
	}

	public void setAbsenceBeanList(List<AbsenceBean> absenceBeanList) {
		this.absenceBeanList = absenceBeanList;
	}

	public List<AccidentBean> getAccidentBeanList() {
		return accidentBeanList;
	}

	public void setAccidentBeanList(List<AccidentBean> accidentBeanList) {
		this.accidentBeanList = accidentBeanList;
	}

	// public List<CVBean> getCvBeanList() {
	// return cvBeanList;
	// }
	// public void setCvBeanList(List<CVBean> cvBeanList) {
	// this.cvBeanList = cvBeanList;
	// }

	public List<EntretienBean> getEntretienBeanList() {
		return entretienBeanList;
	}

	public void setEntretienBeanList(List<EntretienBean> entretienBeanList) {
		this.entretienBeanList = entretienBeanList;
	}

	public List<FormationBean> getFormationBeanList() {
		return formationBeanList;
	}

	public void setFormationBeanList(List<FormationBean> formationBeanList) {
		this.formationBeanList = formationBeanList;
	}

	public List<ParcoursBean> getParcoursBeanList() {
		return parcoursBeanList;
	}

	public void setParcoursBeanList(List<ParcoursBean> parcoursBeanList) {
		this.parcoursBeanList = parcoursBeanList;
	}

	public Integer getIdLienSubordination() {
		return idLienSubordination;
	}

	public void setIdLienSubordination(Integer idLienSubordination) {
		this.idLienSubordination = idLienSubordination;
	}

	public String getNivFormationInit() {
		return nivFormationInit;
	}

	public void setNivFormationInit(String nivFormationInit) {
		this.nivFormationInit = nivFormationInit;
	}

	public String getNivFormationAtteint() {
		return nivFormationAtteint;
	}

	public void setNivFormationAtteint(String nivFormationAtteint) {
		this.nivFormationAtteint = nivFormationAtteint;
	}

	public String getCv() {
		return cv;
	}

	public void setCv(String cv) {
		this.cv = cv;
	}

	public Integer getIdEntrepriseSelected() {
		return idEntrepriseSelected;
	}

	public void setIdEntrepriseSelected(Integer idEntrepriseSelected) {
		this.idEntrepriseSelected = idEntrepriseSelected;
	}

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public Integer getIdServiceSelected() {
		return idServiceSelected;
	}

	public void setIdServiceSelected(Integer idServiceSelected) {
		this.idServiceSelected = idServiceSelected;
	}

	public int compareTo(SalarieBean o) {
		SalarieBean p = o;
		if (idEntrepriseSelected != -1 && idEntrepriseSelected != 0
				&& p.getIdEntrepriseSelected() != -1
				&& p.getIdEntrepriseSelected() != 0) {
			if (idEntrepriseSelected == p.getIdEntrepriseSelected()) {
				if (nom.toUpperCase().equals(p.nom.toUpperCase())) {
					return prenom.toUpperCase().compareTo(
							p.prenom.toUpperCase());
				}
				return nom.toUpperCase().compareTo(p.nom.toUpperCase());
			} else {
				if (idEntrepriseSelected > p.getIdEntrepriseSelected())
					return 1;
				else
					return -1;
			}
		} else {
			if (nom.toUpperCase().equals(p.nom.toUpperCase())) {
				return prenom.toUpperCase().compareTo(p.prenom.toUpperCase());
			}
			return nom.toUpperCase().compareTo(p.nom.toUpperCase());
		}
	}

	public boolean isChangeCouleur() {
		return changeCouleur;
	}

	public void setChangeCouleur(boolean changeCouleur) {
		this.changeCouleur = changeCouleur;
	}

	public Date getDateLastSaisieDif() {
		return dateLastSaisieDif;
	}

	public void setDateLastSaisieDif(Date dateLastSaisieDif) {
		this.dateLastSaisieDif = dateLastSaisieDif;
	}

	public boolean isPresent() {
		return present;
	}

	public void setPresent(boolean present) {
		this.present = present;
	}

	public FicheDePosteBean getFicheDePoste() {
		return ficheDePoste;
	}

	public void setFicheDePoste(FicheDePosteBean ficheDePoste) {
		this.ficheDePoste = ficheDePoste;
	}

	public String getTelephonePortable() {
		return telephonePortable;
	}

	public void setTelephonePortable(String telephonePortable) {
		this.telephonePortable = telephonePortable;
	}

	public String getSituationFamiliale() {
		return situationFamiliale;
	}

	public void setSituationFamiliale(String situationFamiliale) {
		this.situationFamiliale = situationFamiliale;
	}

	public String getPersonneAPrevenirNom() {
		return personneAPrevenirNom;
	}

	public void setPersonneAPrevenirNom(String personneAPrevenirNom) {
		this.personneAPrevenirNom = personneAPrevenirNom;
	}

	public String getPersonneAPrevenirPrenom() {
		return personneAPrevenirPrenom;
	}

	public void setPersonneAPrevenirPrenom(String personneAPrevenirPrenom) {
		this.personneAPrevenirPrenom = personneAPrevenirPrenom;
	}

	public String getPersonneAPrevenirAdresse() {
		return personneAPrevenirAdresse;
	}

	public void setPersonneAPrevenirAdresse(String personneAPrevenirAdresse) {
		this.personneAPrevenirAdresse = personneAPrevenirAdresse;
	}

	public String getPersonneAPrevenirTelephone() {
		return personneAPrevenirTelephone;
	}

	public void setPersonneAPrevenirTelephone(String personneAPrevenirTelephone) {
		this.personneAPrevenirTelephone = personneAPrevenirTelephone;
	}

	public String getPersonneAPrevenirLienParente() {
		return personneAPrevenirLienParente;
	}

	public void setPersonneAPrevenirLienParente(
			String personneAPrevenirLienParente) {
		this.personneAPrevenirLienParente = personneAPrevenirLienParente;
	}

	public boolean isImpression() {
		return impression;
	}

	public void setImpression(boolean impression) {
		this.impression = impression;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public String getNomService() {
		return nomService;
	}

	public void setNomService(String nomService) {
		this.nomService = nomService;
	}

	public List<PersonneAChargeBean> getPersonneAChargeBeanList() {
		return personneAChargeBeanList;
	}

	public void setPersonneAChargeBeanList(
			List<PersonneAChargeBean> personneAChargeBeanList) {
		this.personneAChargeBeanList = personneAChargeBeanList;
	}

	public boolean isPossedePermisConduire() {
		return possedePermisConduire;
	}

	public void setPossedePermisConduire(boolean possedePermisConduire) {
		this.possedePermisConduire = possedePermisConduire;
	}

	public boolean isCanDeleteSalarie() throws Exception {
		// if (getAbsenceBeanList().isEmpty() && getAccidentBeanList().isEmpty()
		// && getEntretienBeanList().isEmpty()
		// && getFormationBeanList().isEmpty()
		// && getHabilitationBeanList().isEmpty() && !hasRemu
		// && !hasEvenement)
		// return true;
		// else
		// return false;
		return canDeleteSalarie;
	}

	public void setCanDeleteSalarie(boolean canDeleteSalarie) {
		this.canDeleteSalarie = canDeleteSalarie;
	}

	public boolean isHasRemu() {
		return hasRemu;
	}

	public void setHasRemu(boolean hasRemu) {
		this.hasRemu = hasRemu;
	}

	public boolean isHasEvenement() {
		return hasEvenement;
	}

	public void setHasEvenement(boolean hasEvenement) {
		this.hasEvenement = hasEvenement;
	}

}
