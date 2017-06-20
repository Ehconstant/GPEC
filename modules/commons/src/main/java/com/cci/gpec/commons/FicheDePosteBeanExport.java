package com.cci.gpec.commons;

import java.io.File;

public class FicheDePosteBeanExport {

	private int idSalarie;
	private int idEntreprise;
	private String dateCreation;
	private String dateModification;
	private String dateCreationAffiche;
	private String dateModificationAffiche;
	private String activitesSpecifiques;
	private String competencesSpecifiques;
	private String competencesSpecifiquesTexte;
	private String competencesNouvelles;
	private String competencesNouvelles2;
	private String competencesNouvelles3;
	private String competencesNouvelles4;
	private String competencesNouvelles5;
	private String commentaire;
	private String csp;
	private String savoir;
	private String savoirFaire;
	private String savoirEtre;
	private String evaluationGlobale;
	private String activiteResponsabilite;
	private String ancienneteEntreprise;
	private String anciennetePoste;
	private String ficheMetier;
	private String savoirMetier;
	private String savoirFaireMetier;
	private String savoirEtreMetier;
	private String particularitePoste;
	private String cspRef;
	// private String mobiliteEnvisagee;
	private String finalitePoste;
	private String nom;
	private String prenom;
	private String formation;
	private String formationRef;
	private String intitulePoste;
	private String categorieCompetence;
	private String categorieCompetence2;
	private String categorieCompetence3;
	private String categorieCompetence4;
	private String categorieCompetence5;
	private String libelleService;
	private String libelleEntreprise;
	private boolean display;

	private String justificatif;

	private File justif;

	private boolean fileError;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public File getJustif() {
		if (justificatif != null)
			return new File(justificatif);
		else
			return null;
	}

	public void setJustif(File justif) {
		this.justif = justif;
	}

	public String getLibelleEntreprise() {
		return libelleEntreprise;
	}

	public void setLibelleEntreprise(String libelleEntreprise) {
		this.libelleEntreprise = libelleEntreprise;
	}

	public String getLibelleService() {
		return libelleService;
	}

	public void setLibelleService(String libelleService) {
		this.libelleService = libelleService;
	}

	public String getCategorieCompetence() {
		return categorieCompetence;
	}

	public void setCategorieCompetence(String categorieCompetence) {
		this.categorieCompetence = categorieCompetence;
	}

	public String getIntitulePoste() {
		return intitulePoste;
	}

	public void setIntitulePoste(String intitulePoste) {
		this.intitulePoste = intitulePoste;
	}

	public String getFormation() {
		return formation;
	}

	public void setFormation(String formation) {
		this.formation = formation;
	}

	public String getFormationRef() {
		return formationRef;
	}

	public void setFormationRef(String formationRef) {
		this.formationRef = formationRef;
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

	public String getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}

	public String getDateModification() {
		return dateModification;
	}

	public void setDateModification(String dateModification) {
		this.dateModification = dateModification;
	}

	public String getDateCreationAffiche() {
		return dateCreationAffiche;
	}

	public void setDateCreationAffiche(String dateCreationAffiche) {
		this.dateCreationAffiche = dateCreationAffiche;
	}

	public String getDateModificationAffiche() {
		return dateModificationAffiche;
	}

	public void setDateModificationAffiche(String dateModificationAffiche) {
		this.dateModificationAffiche = dateModificationAffiche;
	}

	public String getActivitesSpecifiques() {
		return activitesSpecifiques;
	}

	public void setActivitesSpecifiques(String activitesSpecifiques) {
		this.activitesSpecifiques = activitesSpecifiques;
	}

	public String getCompetencesSpecifiques() {
		return competencesSpecifiques;
	}

	public void setCompetencesSpecifiques(String competencesSpecifiques) {
		switch (Integer.parseInt(competencesSpecifiques)) {
		case -1:
			this.competencesSpecifiques = "-";
			break;
		case 0:
			this.competencesSpecifiques = "=";
			break;
		case 1:
			this.competencesSpecifiques = "+";
			break;
		default:
			this.competencesSpecifiques = "Non évalué";
			break;
		}
	}

	public void setCompetencesSpecifiques2(String competencesSpecifiques) {
		this.competencesSpecifiques = competencesSpecifiques;
	}

	public String getCompetencesSpecifiquesTexte() {
		return competencesSpecifiquesTexte;
	}

	public void setCompetencesSpecifiquesTexte(
			String competencesSpecifiquesTexte) {
		this.competencesSpecifiquesTexte = competencesSpecifiquesTexte;
	}

	public String getCompetencesNouvelles() {
		return competencesNouvelles;
	}

	public void setCompetencesNouvelles(String competencesNouvelles) {
		this.competencesNouvelles = competencesNouvelles;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public String getCsp() {
		return csp;
	}

	public void setCsp(String csp) {
		this.csp = csp;
	}

	public String getSavoir() {
		return savoir;
	}

	public void setSavoir(String savoir) {
		switch (Integer.parseInt(savoir)) {
		case -1:
			this.savoir = "-";
			break;
		case 0:
			this.savoir = "=";
			break;
		case 1:
			this.savoir = "+";
			break;
		case 2:
			this.savoir = "Non évalué";
			break;
		default:
			this.savoir = "Non évalué";
			break;
		}
	}

	public void setSavoir2(String savoir) {
		this.savoir = savoir;
	}

	public String getSavoirFaire() {
		return savoirFaire;
	}

	public void setSavoirFaire(String savoirFaire) {
		switch (Integer.parseInt(savoirFaire)) {
		case -1:
			this.savoirFaire = "-";
			break;
		case 0:
			this.savoirFaire = "=";
			break;
		case 1:
			this.savoirFaire = "+";
			break;
		case 2:
			this.savoirFaire = "Non évalué";
			break;
		default:
			this.savoirFaire = "Non évalué";
			break;
		}
	}

	public void setSavoirFaire2(String savoirFaire) {
		this.savoirFaire = savoirFaire;
	}

	public String getSavoirEtre() {
		return savoirEtre;
	}

	public void setSavoirEtre(String savoirEtre) {
		switch (Integer.parseInt(savoirEtre)) {
		case -1:
			this.savoirEtre = "-";
			break;
		case 0:
			this.savoirEtre = "=";
			break;
		case 1:
			this.savoirEtre = "+";
			break;
		case 2:
			this.savoirEtre = "Non évalué";
			break;
		default:
			this.savoirEtre = "Non évalué";
			break;
		}
	}

	public void setSavoirEtre2(String savoirEtre) {
		this.savoirEtre = savoirEtre;
	}

	public String getEvaluationGlobale() {
		return evaluationGlobale;
	}

	public void setEvaluationGlobale(String evaluationGlobale) {
		switch (Integer.parseInt(evaluationGlobale)) {
		case -1:
			this.evaluationGlobale = "-";
			break;
		case 0:
			this.evaluationGlobale = "=";
			break;
		case 1:
			this.evaluationGlobale = "+";
			break;
		case 2:
			this.evaluationGlobale = "Non évalué";
			break;
		default:
			this.evaluationGlobale = "Non évalué";
			break;
		}
	}

	public void setEvaluationGlobale2(String evaluationGlobale) {
		this.evaluationGlobale = evaluationGlobale;
	}

	public String getActiviteResponsabilite() {
		return activiteResponsabilite;
	}

	public void setActiviteResponsabilite(String activiteResponsabilite) {
		this.activiteResponsabilite = activiteResponsabilite;
	}

	public String getAncienneteEntreprise() {
		return ancienneteEntreprise;
	}

	public void setAncienneteEntreprise(String ancienneteEntreprise) {
		this.ancienneteEntreprise = ancienneteEntreprise;
	}

	public String getAnciennetePoste() {
		return anciennetePoste;
	}

	public void setAnciennetePoste(String anciennetePoste) {
		this.anciennetePoste = anciennetePoste;
	}

	public String getFicheMetier() {
		return ficheMetier;
	}

	public void setFicheMetier(String ficheMetier) {
		this.ficheMetier = ficheMetier;
	}

	public String getSavoirMetier() {
		return savoirMetier;
	}

	public void setSavoirMetier(String savoirMetier) {
		this.savoirMetier = savoirMetier;
	}

	public String getSavoirFaireMetier() {
		return savoirFaireMetier;
	}

	public void setSavoirFaireMetier(String savoirFaireMetier) {
		this.savoirFaireMetier = savoirFaireMetier;
	}

	public String getSavoirEtreMetier() {
		return savoirEtreMetier;
	}

	public void setSavoirEtreMetier(String savoirEtreMetier) {
		this.savoirEtreMetier = savoirEtreMetier;
	}

	public String getCspRef() {
		return cspRef;
	}

	public void setCspRef(String cspRef) {
		this.cspRef = cspRef;
	}

	public String getFinalitePoste() {
		return finalitePoste;
	}

	public void setFinalitePoste(String finalitePoste) {
		this.finalitePoste = finalitePoste;
	}

	public int getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(int idSalarie) {
		this.idSalarie = idSalarie;
	}

	public String getCompetencesNouvelles2() {
		return competencesNouvelles2;
	}

	public void setCompetencesNouvelles2(String competencesNouvelles2) {
		this.competencesNouvelles2 = competencesNouvelles2;
	}

	public String getCompetencesNouvelles3() {
		return competencesNouvelles3;
	}

	public void setCompetencesNouvelles3(String competencesNouvelles3) {
		this.competencesNouvelles3 = competencesNouvelles3;
	}

	public String getCompetencesNouvelles4() {
		return competencesNouvelles4;
	}

	public void setCompetencesNouvelles4(String competencesNouvelles4) {
		this.competencesNouvelles4 = competencesNouvelles4;
	}

	public String getCompetencesNouvelles5() {
		return competencesNouvelles5;
	}

	public void setCompetencesNouvelles5(String competencesNouvelles5) {
		this.competencesNouvelles5 = competencesNouvelles5;
	}

	public String getCategorieCompetence2() {
		return categorieCompetence2;
	}

	public void setCategorieCompetence2(String categorieCompetence2) {
		this.categorieCompetence2 = categorieCompetence2;
	}

	public String getCategorieCompetence3() {
		return categorieCompetence3;
	}

	public void setCategorieCompetence3(String categorieCompetence3) {
		this.categorieCompetence3 = categorieCompetence3;
	}

	public String getCategorieCompetence4() {
		return categorieCompetence4;
	}

	public void setCategorieCompetence4(String categorieCompetence4) {
		this.categorieCompetence4 = categorieCompetence4;
	}

	public String getCategorieCompetence5() {
		return categorieCompetence5;
	}

	public void setCategorieCompetence5(String categorieCompetence5) {
		this.categorieCompetence5 = categorieCompetence5;
	}

	public String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(String justificatif) {
		this.justificatif = justificatif;
	}

	public boolean isFileError() {
		if (justif != null && justif.exists() && justif.isFile()
				&& justif.canRead())
			return true;
		else
			return false;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public int getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(int idEntreprise) {
		this.idEntreprise = idEntreprise;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public String getParticularitePoste() {
		return particularitePoste;
	}

	public void setParticularitePoste(String particularitePoste) {
		this.particularitePoste = particularitePoste;
	}
}
