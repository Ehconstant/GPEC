package com.cci.gpec.commons;

import java.io.File;
import java.util.Date;

public class EntretienBean implements Comparable<EntretienBean> {

	// primary key
	private int id;

	// fields
	private Integer anneeReference;
	private String principaleConclusion;
	private String souhait;
	private String competence;
	private String evaluationGlobale;
	private Date dateEntretien;
	private String crSigne;
	private String formations;
	private String formations2;
	private String formations3;
	private String formations4;
	private String formations5;
	private Integer idSalarie;
	private String nomManager;
	private String serviceManager;
	private String bilanDessous;
	private String bilanEgal;
	private String bilanDessus;
	private String commentaireBilan;
	private String formationNMoinsUn;
	private String commentaireFormation;
	private Integer domainesFormation;
	private Integer domainesFormation2;
	private Integer domainesFormation3;
	private Integer domainesFormation4;
	private Integer domainesFormation5;
	private String synthese;
	private String objIntitule;
	private String objDelais;
	private String objMoyens;
	private String objCriteres;
	private String evolutionPerso;
	private String observations;
	private String modifProfil;
	private String justificatif;

	private File justif;

	private boolean fileError;

	public File getJustif() {
		if (justificatif != null)
			return new File(justificatif);
		else
			return null;
	}

	public void setJustif(File justif) {
		this.justif = justif;
	}

	public String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(String justificatif) {
		this.justificatif = justificatif;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPrincipaleConclusion() {
		return principaleConclusion;
	}

	public void setPrincipaleConclusion(String principaleConclusion) {
		this.principaleConclusion = principaleConclusion;
	}

	public String getSouhait() {
		return souhait;
	}

	public void setSouhait(String souhait) {
		this.souhait = souhait;
	}

	public String getCompetence() {
		return competence;
	}

	public void setCompetence(String competence) {
		this.competence = competence;
	}

	public Date getDateEntretien() {
		return dateEntretien;
	}

	public void setDateEntretien(Date dateEntretien) {
		this.dateEntretien = dateEntretien;
	}

	public String getCrSigne() {
		return crSigne;
	}

	public void setCrSigne(String crSigne) {
		this.crSigne = crSigne;
	}

	public String getFormations() {
		return formations;
	}

	public void setFormations(String formations) {
		this.formations = formations;
	}

	public String getNomManager() {
		return nomManager;
	}

	public void setNomManager(String nomManager) {
		this.nomManager = nomManager;
	}

	public String getServiceManager() {
		return serviceManager;
	}

	public void setServiceManager(String serviceManager) {
		this.serviceManager = serviceManager;
	}

	public String getBilanDessous() {
		return bilanDessous;
	}

	public void setBilanDessous(String bilanDessous) {
		this.bilanDessous = bilanDessous;
	}

	public String getBilanEgal() {
		return bilanEgal;
	}

	public void setBilanEgal(String bilanEgal) {
		this.bilanEgal = bilanEgal;
	}

	public String getBilanDessus() {
		return bilanDessus;
	}

	public void setBilanDessus(String bilanDessus) {
		this.bilanDessus = bilanDessus;
	}

	public String getCommentaireBilan() {
		return commentaireBilan;
	}

	public void setCommentaireBilan(String commentaireBilan) {
		this.commentaireBilan = commentaireBilan;
	}

	public String getFormationNMoinsUn() {
		return formationNMoinsUn;
	}

	public void setFormationNMoinsUn(String formationNMoinsUn) {
		this.formationNMoinsUn = formationNMoinsUn;
	}

	public String getCommentaireFormation() {
		return commentaireFormation;
	}

	public void setCommentaireFormation(String commentaireFormation) {
		this.commentaireFormation = commentaireFormation;
	}

	public Integer getDomainesFormation() {
		return domainesFormation;
	}

	public void setDomainesFormation(Integer domainesFormation) {
		this.domainesFormation = domainesFormation;
	}

	public String getSynthese() {
		return synthese;
	}

	public void setSynthese(String synthese) {
		this.synthese = synthese;
	}

	public String getObjIntitule() {
		return objIntitule;
	}

	public void setObjIntitule(String objIntitule) {
		this.objIntitule = objIntitule;
	}

	public String getObjDelais() {
		return objDelais;
	}

	public void setObjDelais(String objDelais) {
		this.objDelais = objDelais;
	}

	public String getObjMoyens() {
		return objMoyens;
	}

	public void setObjMoyens(String objMoyens) {
		this.objMoyens = objMoyens;
	}

	public String getObjCriteres() {
		return objCriteres;
	}

	public void setObjCriteres(String objCriteres) {
		this.objCriteres = objCriteres;
	}

	public String getEvolutionPerso() {
		return evolutionPerso;
	}

	public void setEvolutionPerso(String evolutionPerso) {
		this.evolutionPerso = evolutionPerso;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public String getModifProfil() {
		return modifProfil;
	}

	public void setModifProfil(String modifProfil) {
		this.modifProfil = modifProfil;
	}

	public int compareTo(EntretienBean o) {
		if (dateEntretien.equals(o.dateEntretien))
			return id < o.id ? -1 : 1;
		else
			return dateEntretien.compareTo(o.dateEntretien);
	}

	public Integer getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(Integer idSalarie) {
		this.idSalarie = idSalarie;
	}

	public String getFormations2() {
		return formations2;
	}

	public void setFormations2(String formations2) {
		this.formations2 = formations2;
	}

	public String getFormations3() {
		return formations3;
	}

	public void setFormations3(String formations3) {
		this.formations3 = formations3;
	}

	public String getFormations4() {
		return formations4;
	}

	public void setFormations4(String formations4) {
		this.formations4 = formations4;
	}

	public String getFormations5() {
		return formations5;
	}

	public void setFormations5(String formations5) {
		this.formations5 = formations5;
	}

	public Integer getDomainesFormation2() {
		return domainesFormation2;
	}

	public void setDomainesFormation2(Integer domainesFormation2) {
		this.domainesFormation2 = domainesFormation2;
	}

	public Integer getDomainesFormation3() {
		return domainesFormation3;
	}

	public void setDomainesFormation3(Integer domainesFormation3) {
		this.domainesFormation3 = domainesFormation3;
	}

	public Integer getDomainesFormation4() {
		return domainesFormation4;
	}

	public void setDomainesFormation4(Integer domainesFormation4) {
		this.domainesFormation4 = domainesFormation4;
	}

	public Integer getDomainesFormation5() {
		return domainesFormation5;
	}

	public void setDomainesFormation5(Integer domainesFormation5) {
		this.domainesFormation5 = domainesFormation5;
	}

	public boolean isFileError() {
		if (getJustif() != null) {
			if (getJustif().exists() && getJustif().isFile()
					&& getJustif().canRead())
				return false;
			else
				return true;
		} else
			return false;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public String getEvaluationGlobale() {
		return evaluationGlobale;
	}

	public void setEvaluationGlobale(String evaluationGlobale) {
		this.evaluationGlobale = evaluationGlobale;
	}

	public Integer getAnneeReference() {
		return anneeReference;
	}

	public void setAnneeReference(Integer anneeReference) {
		this.anneeReference = anneeReference;
	}
}
