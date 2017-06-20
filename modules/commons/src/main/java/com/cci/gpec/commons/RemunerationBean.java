package com.cci.gpec.commons;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class RemunerationBean implements Comparable<RemunerationBean> {

	// primary key
	private Integer idRemuneration;

	// fields
	private Integer annee;
	private Integer idSalarie;
	private Integer idMetier;
	private Integer idStatut;
	private Integer idRevenuComplementaire;

	private String nomMetier;
	private String nomCsp;

	private String niveau;
	private String echelon;
	private String coefficient;
	private String salaireDeBaseCommentaire;
	private String salaireMinimumConventionnelCommentaire;
	private String augmentationCollectiveCommentaire;
	private String augmentationIndividuelleCommentaire;
	private String heuresSup50Commentaire;
	private String commentaire;
	private String heuresSup25Commentaire;

	/* Pour affichage avec format désiré (x.xx) dans table onglet rému */
	private String horaireMensuelTravailleDisplay;
	private String tauxHoraireBrutDisplay;
	private String salaireMensuelBrutDisplay;

	private String horaireMensuelTravaille;
	private Double tauxHoraireBrut;
	private Double salaireMensuelBrut;
	private Double salaireMensuelConventionnelBrut;
	private Double salaireMinimumConventionnel;
	private Double salaireMinimumConventionnelActualisation;
	private Double salaireDeBase;
	private Double salaireDeBaseActualisation;
	private Double augmentationCollective;
	private Double augmentationCollectiveActualisation;
	private Double augmentationIndividuelle;
	private Double augmentationIndividuelleActualisation;
	private Double heuresSup25;
	private Double heuresSup25Actualisation;
	private Double heuresSup50;
	private Double heuresSup50Actualisation;

	private Double baseBruteAnnuelle;
	private Double remuGlobale;
	private Double heuresSupAnnuelles;
	private Double avantagesNonAssujettisAnnuels;

	public Double getBaseBruteAnnuelle() {
		return baseBruteAnnuelle;
	}

	public void setBaseBruteAnnuelle(Double baseBruteAnnuelle) {
		this.baseBruteAnnuelle = baseBruteAnnuelle;
	}

	public Double getRemuGlobale() {
		return remuGlobale;
	}

	public void setRemuGlobale(Double remuGlobale) {
		this.remuGlobale = remuGlobale;
	}

	public Double getHeuresSupAnnuelles() {
		return heuresSupAnnuelles;
	}

	public void setHeuresSupAnnuelles(Double heuresSupAnnuelles) {
		this.heuresSupAnnuelles = heuresSupAnnuelles;
	}

	public Double getAvantagesNonAssujettisAnnuels() {
		return avantagesNonAssujettisAnnuels;
	}

	public void setAvantagesNonAssujettisAnnuels(
			Double avantagesNonAssujettisAnnuels) {
		this.avantagesNonAssujettisAnnuels = avantagesNonAssujettisAnnuels;
	}

	public int compareTo(RemunerationBean o) {
		if (this.annee == o.getAnnee())
			return 0;
		if (this.annee < o.getAnnee())
			return -1;
		else
			return 1;
	}

	public Integer getIdRemuneration() {
		return idRemuneration;
	}

	public void setIdRemuneration(Integer idRemuneration) {
		this.idRemuneration = idRemuneration;
	}

	public Integer getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(Integer idSalarie) {
		this.idSalarie = idSalarie;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public Integer getIdMetier() {
		return idMetier;
	}

	public void setIdMetier(Integer idMetier) {
		this.idMetier = idMetier;
	}

	public Integer getIdStatut() {
		return idStatut;
	}

	public void setIdStatut(Integer idStatut) {
		this.idStatut = idStatut;
	}

	public String getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(String coefficient) {
		this.coefficient = coefficient;
	}

	public String getSalaireMinimumConventionnelCommentaire() {
		return salaireMinimumConventionnelCommentaire;
	}

	public void setSalaireMinimumConventionnelCommentaire(
			String salaireMinimumConventionnelCommentaire) {
		this.salaireMinimumConventionnelCommentaire = salaireMinimumConventionnelCommentaire;
	}

	public String getSalaireDeBaseCommentaire() {
		return salaireDeBaseCommentaire;
	}

	public void setSalaireDeBaseCommentaire(String salaireDeBaseCommentaire) {
		this.salaireDeBaseCommentaire = salaireDeBaseCommentaire;
	}

	public String getAugmentationCollectiveCommentaire() {
		return augmentationCollectiveCommentaire;
	}

	public void setAugmentationCollectiveCommentaire(
			String augmentationCollectiveCommentaire) {
		this.augmentationCollectiveCommentaire = augmentationCollectiveCommentaire;
	}

	public String getAugmentationIndividuelleCommentaire() {
		return augmentationIndividuelleCommentaire;
	}

	public void setAugmentationIndividuelleCommentaire(
			String augmentationIndividuelleCommentaire) {
		this.augmentationIndividuelleCommentaire = augmentationIndividuelleCommentaire;
	}

	public String getHeuresSup25Commentaire() {
		return heuresSup25Commentaire;
	}

	public void setHeuresSup25Commentaire(String heuresSup25Commentaire) {
		this.heuresSup25Commentaire = heuresSup25Commentaire;
	}

	public String getHeuresSup50Commentaire() {
		return heuresSup50Commentaire;
	}

	public void setHeuresSup50Commentaire(String heuresSup50Commentaire) {
		this.heuresSup50Commentaire = heuresSup50Commentaire;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Integer getIdRevenuComplementaire() {
		return idRevenuComplementaire;
	}

	public void setIdRevenuComplementaire(Integer idRevenuComplementaire) {
		this.idRevenuComplementaire = idRevenuComplementaire;
	}

	public String getHoraireMensuelTravaille() {
		return horaireMensuelTravaille;
	}

	public void setHoraireMensuelTravaille(String horaireMensuelTravaille) {
		this.horaireMensuelTravaille = horaireMensuelTravaille;
	}

	public Double getTauxHoraireBrut() {
		return tauxHoraireBrut;
	}

	public void setTauxHoraireBrut(Double tauxHoraireBrut) {
		this.tauxHoraireBrut = tauxHoraireBrut;
	}

	public Double getSalaireMensuelBrut() {
		return salaireMensuelBrut;
	}

	public void setSalaireMensuelBrut(Double salaireMensuelBrut) {
		this.salaireMensuelBrut = salaireMensuelBrut;
	}

	public Double getSalaireMensuelConventionnelBrut() {
		return salaireMensuelConventionnelBrut;
	}

	public void setSalaireMensuelCenventionnelBrut(
			Double salaireMensuelCenventionnelBrut) {
		this.salaireMensuelConventionnelBrut = salaireMensuelCenventionnelBrut;
	}

	public Double getSalaireMinimumConventionnel() {
		return salaireMinimumConventionnel;
	}

	public void setSalaireMinimumConventionnel(
			Double salaireMinimumConventionnel) {
		this.salaireMinimumConventionnel = salaireMinimumConventionnel;
	}

	public Double getSalaireMinimumConventionnelActualisation() {
		return salaireMinimumConventionnelActualisation;
	}

	public void setSalaireMinimumConventionnelActualisation(
			Double salaireMinimumConventionnelActualisation) {
		this.salaireMinimumConventionnelActualisation = salaireMinimumConventionnelActualisation;
	}

	public Double getSalaireDeBase() {
		return salaireDeBase;
	}

	public void setSalaireDeBase(Double salaireDeBase) {
		this.salaireDeBase = salaireDeBase;
	}

	public Double getSalaireDeBaseActualisation() {
		return salaireDeBaseActualisation;
	}

	public void setSalaireDeBaseActualisation(Double salaireDeBaseActualisation) {
		this.salaireDeBaseActualisation = salaireDeBaseActualisation;
	}

	public Double getAugmentationCollective() {
		return augmentationCollective;
	}

	public void setAugmentationCollective(Double augmentationCollective) {
		this.augmentationCollective = augmentationCollective;
	}

	public Double getAugmentationCollectiveActualisation() {
		return augmentationCollectiveActualisation;
	}

	public void setAugmentationCollectiveActualisation(
			Double augmentationCollectiveActualisation) {
		this.augmentationCollectiveActualisation = augmentationCollectiveActualisation;
	}

	public Double getAugmentationIndividuelle() {
		return augmentationIndividuelle;
	}

	public void setAugmentationIndividuelle(Double augmentationIndividuelle) {
		this.augmentationIndividuelle = augmentationIndividuelle;
	}

	public Double getAugmentationIndividuelleActualisation() {
		return augmentationIndividuelleActualisation;
	}

	public void setAugmentationIndividuelleActualisation(
			Double augmentationIndividuelleActualisation) {
		this.augmentationIndividuelleActualisation = augmentationIndividuelleActualisation;
	}

	public Double getHeuresSup25() {
		return heuresSup25;
	}

	public void setHeuresSup25(Double heuresSup25) {
		this.heuresSup25 = heuresSup25;
	}

	public Double getHeuresSup25Actualisation() {
		return heuresSup25Actualisation;
	}

	public void setHeuresSup25Actualisation(Double heuresSup25Actualisation) {
		this.heuresSup25Actualisation = heuresSup25Actualisation;
	}

	public Double getHeuresSup50() {
		return heuresSup50;
	}

	public void setHeuresSup50(Double heuresSup50) {
		this.heuresSup50 = heuresSup50;
	}

	public Double getHeuresSup50Actualisation() {
		return heuresSup50Actualisation;
	}

	public void setHeuresSup50Actualisation(Double heuresSup50Actualisation) {
		this.heuresSup50Actualisation = heuresSup50Actualisation;
	}

	public String getNomMetier() {
		return nomMetier;
	}

	public void setNomMetier(String nomMetier) {
		this.nomMetier = nomMetier;
	}

	public String getNomCsp() {
		return nomCsp;
	}

	public void setNomCsp(String nomCsp) {
		this.nomCsp = nomCsp;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
	}

	public void setSalaireMensuelConventionnelBrut(
			Double salaireMensuelConventionnelBrut) {
		this.salaireMensuelConventionnelBrut = salaireMensuelConventionnelBrut;
	}

	public String getNiveau() {
		return niveau;
	}

	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}

	public String getEchelon() {
		return echelon;
	}

	public void setEchelon(String echelon) {
		this.echelon = echelon;
	}

	public String getHoraireMensuelTravailleDisplay() {
		return horaireMensuelTravaille;
	}

	public void setHoraireMensuelTravailleDisplay(
			String horaireMensuelTravailleDisplay) {
		this.horaireMensuelTravailleDisplay = horaireMensuelTravailleDisplay;
	}

	public String getTauxHoraireBrutDisplay() {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		return df.format(Double.valueOf(tauxHoraireBrut));
	}

	public void setTauxHoraireBrutDisplay(String tauxHoraireBrutDisplay) {
		this.tauxHoraireBrutDisplay = tauxHoraireBrutDisplay;
	}

	public String getSalaireMensuelBrutDisplay() {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		return df.format(Double.valueOf(salaireMensuelBrut));
	}

	public void setSalaireMensuelBrutDisplay(String salaireMensuelBrutDisplay) {
		this.salaireMensuelBrutDisplay = salaireMensuelBrutDisplay;
	}

}
