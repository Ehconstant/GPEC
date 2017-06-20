package com.cci.gpec.commons;

public class CompetencesExportBean implements Comparable {

	private Integer annee;
	private String entreprise;
	private String nom;
	private String prenom;
	private String ancienneteEntreprise;
	private String service;
	private String posteOccupe;
	private String anciennetePoste;
	private String savoir;
	private String savoirFaire;
	private String savoirEtre;
	private String evaluationGlobale;
	private String competencesSpecifiques;
	private String activitesSpecifiques;
	private String decisionEvolution;

	public int compareTo(Object o) {
		CompetencesExportBean c = (CompetencesExportBean) o;
		if (this.annee.equals(c.annee)) {
			if (this.entreprise != null && !this.entreprise.equals("")) {
				if (this.entreprise.equals(c.entreprise)) {
					return this.nom.compareTo(c.nom);
				} else {
					return this.entreprise.compareTo(c.entreprise);
				}
			} else {
				return this.nom.compareTo(c.nom);
			}
		} else {
			return c.annee.compareTo(this.annee);
		}
	}

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
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

	public String getAncienneteEntreprise() {
		return ancienneteEntreprise;
	}

	public void setAncienneteEntreprise(String ancienneteEntreprise) {
		this.ancienneteEntreprise = ancienneteEntreprise;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPosteOccupe() {
		return posteOccupe;
	}

	public void setPosteOccupe(String posteOccupe) {
		this.posteOccupe = posteOccupe;
	}

	public String getAnciennetePoste() {
		return anciennetePoste;
	}

	public void setAnciennetePoste(String anciennetePoste) {
		this.anciennetePoste = anciennetePoste;
	}

	public String getSavoir() {
		return savoir;
	}

	public void setSavoir(String savoir) {
		this.savoir = savoir;
	}

	public String getSavoirFaire() {
		return savoirFaire;
	}

	public void setSavoirFaire(String savoirFaire) {
		this.savoirFaire = savoirFaire;
	}

	public String getSavoirEtre() {
		return savoirEtre;
	}

	public void setSavoirEtre(String savoirEtre) {
		this.savoirEtre = savoirEtre;
	}

	public String getEvaluationGlobale() {
		return evaluationGlobale;
	}

	public void setEvaluationGlobale(String evaluationGlobale) {
		this.evaluationGlobale = evaluationGlobale;
	}

	public String getCompetencesSpecifiques() {
		return competencesSpecifiques;
	}

	public void setCompetencesSpecifiques(String competencesSpecifiques) {
		this.competencesSpecifiques = competencesSpecifiques;
	}

	public String getActivitesSpecifiques() {
		return activitesSpecifiques;
	}

	public void setActivitesSpecifiques(String activitesSpecifiques) {
		this.activitesSpecifiques = activitesSpecifiques;
	}

	public String getDecisionEvolution() {
		return decisionEvolution;
	}

	public void setDecisionEvolution(String decisionEvolution) {
		this.decisionEvolution = decisionEvolution;
	}

	public String getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(String entreprise) {
		this.entreprise = entreprise;
	}

}
