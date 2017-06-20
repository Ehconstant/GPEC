package com.cci.gpec.commons;

import java.io.File;
import java.util.Date;

import com.cci.gpec.ModelBean;

public class FicheDePosteBean extends ModelBean implements
		Comparable<FicheDePosteBean> {

	private int idFicheDePoste;
	private Date dateCreation;
	private Date dateModification;
	private String activitesSpecifiques;
	private String competencesSpecifiques;
	private String competencesSpecifiquesTexte;
	private String competencesNouvelles;
	private String competencesNouvelles2;
	private String competencesNouvelles3;
	private String competencesNouvelles4;
	private String competencesNouvelles5;
	private String commentaire;
	private String savoir;
	private String savoirFaire;
	private String savoirEtre;
	private String evaluationGlobale;
	private String mobiliteEnvisagee;
	private String categorieCompetence;
	private String categorieCompetence2;
	private String categorieCompetence3;
	private String categorieCompetence4;
	private String categorieCompetence5;
	private Integer idSalarie;
	private Integer idFicheMetierType;
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

	public FicheDePosteBean() {
		super();
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

	public String getMobiliteEnvisagee() {
		return mobiliteEnvisagee;
	}

	public void setMobiliteEnvisagee(String mobiliteEnvisagee) {
		this.mobiliteEnvisagee = mobiliteEnvisagee;
	}

	public int compareTo(FicheDePosteBean o) {
		if (dateCreation.equals(o.dateCreation))
			return idFicheDePoste < o.idFicheDePoste ? -1 : 1;
		else
			return dateCreation.compareTo(o.dateCreation);
	}

	public int getIdFicheDePoste() {
		return idFicheDePoste;
	}

	public void setIdFicheDePoste(int idFicheDePoste) {
		this.idFicheDePoste = idFicheDePoste;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateModification() {
		return dateModification;
	}

	public void setDateModification(Date dateModification) {
		this.dateModification = dateModification;
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
		this.competencesSpecifiques = competencesSpecifiques;
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

	public Integer getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(Integer idSalarie) {
		this.idSalarie = idSalarie;
	}

	public Integer getIdFicheMetierType() {
		return idFicheMetierType;
	}

	public void setIdFicheMetierType(Integer idFicheMetierType) {
		this.idFicheMetierType = idFicheMetierType;
	}

	public String getCompetencesSpecifiquesTexte() {
		return competencesSpecifiquesTexte;
	}

	public void setCompetencesSpecifiquesTexte(
			String competencesSpecifiquesTexte) {
		this.competencesSpecifiquesTexte = competencesSpecifiquesTexte;
	}

	public String getEvaluationGlobale() {
		return evaluationGlobale;
	}

	public void setEvaluationGlobale(String evaluationGlobale) {
		this.evaluationGlobale = evaluationGlobale;
	}

	public String getCategorieCompetence() {
		return categorieCompetence;
	}

	public void setCategorieCompetence(String categorieCompetence) {
		this.categorieCompetence = categorieCompetence;
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

}
