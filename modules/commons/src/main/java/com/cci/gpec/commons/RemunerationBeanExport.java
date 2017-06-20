package com.cci.gpec.commons;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class RemunerationBeanExport implements Comparable<RemunerationBean> {

	// primary key
	private Integer idRemuneration;

	// fields
	private Integer annee;
	private Integer idSalarie;
	private String nom;
	private String prenom;
	private Integer idMetier;
	private Integer idStatut;
	private Integer idRevenuComplementaire;

	private String nomMetier;
	private String nomCsp;
	private String nomEntreprise;

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
	private String tauxHoraireBrut;
	private String salaireMensuelBrut;
	private String salaireMensuelConventionnelBrut;

	private String salaireMinimumConventionnel;
	private String salaireMinimumConventionnelActualisation;
	private String salaireMinimumConventionnelNPrec;
	private String salaireMinimumConventionnelEvol;

	private String salaireDeBase;
	private String salaireDeBaseActualisation;
	private String salaireDeBaseNPrec;
	private String salaireDeBaseEvol;

	private String augmentationCollective;
	private String augmentationCollectiveActualisation;
	private String augmentationCollectiveNPrec;
	private String augmentationCollectiveEvol;

	private String augmentationIndividuelle;
	private String augmentationIndividuelleActualisation;
	private String augmentationIndividuelleNPrec;
	private String augmentationIndividuelleEvol;

	private String heuresSup25;
	private String heuresSup25Actualisation;
	private String heuresSup25NPrec;
	private String heuresSup25Evol;

	private String heuresSup50;
	private String heuresSup50Actualisation;
	private String heuresSup50NPrec;
	private String heuresSup50Evol;

	private String baseBruteAnnuelle;
	private String baseBruteAnnuelleNPrec;
	private String baseBruteAnnuelleActu;
	private String baseBruteAnnuelleEvol;

	private String remuGlobale;
	private String remuGlobaleNPrec;
	private String remuGlobaleActu;
	private String remuGlobaleEvol;

	private String remunerationBruteAnnuelle;
	private String remunerationBruteAnnuelleNPrec;
	private String remunerationBruteAnnuelleActu;
	private String remunerationBruteAnnuelleEvol;

	private String totalNCommission;
	private String totalNPrecCommission;
	private String totalActuCommission;
	private String totalComEvol;

	private String totalNPrimeFixe;
	private String totalNPrecPrimeFixe;
	private String totalActuPrimeFixe;
	private String totalPfEvol;

	private String totalNPrimeVariable;
	private String totalNPrecPrimeVariable;
	private String totalActuPrimeVariable;
	private String totalPvEvol;

	private String totalNAvantageAssujetti;
	private String totalNPrecAvantageAssujetti;
	private String totalActuAvantageAssujetti;
	private String totalAaEvol;

	private String totalNAvantageNonAssujetti;
	private String totalNPrecAvantageNonAssujetti;
	private String totalActuAvantageNonAssujetti;
	private String totalAnaEvol;

	private String totalNFraisProf;
	private String totalNPrecFraisProf;
	private String totalActuFraisProf;
	private String totalFpEvol;

	private String heuresSupAnnuelles;
	private String avantagesNonAssujettisAnnuels;

	private List<LienRemunerationRevenuBean> comList;
	private List<LienRemunerationRevenuBean> pfList;
	private List<LienRemunerationRevenuBean> pvList;
	private List<LienRemunerationRevenuBean> aaList;
	private List<LienRemunerationRevenuBean> anaList;
	private List<LienRemunerationRevenuBean> fpList;

	private String justificatif;
	private String url;

	public String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(String justificatif) {
		this.justificatif = justificatif;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int compareTo(RemunerationBean o) {
		if (this.annee == o.getAnnee())
			return 0;
		if (this.annee < o.getAnnee())
			return -1;
		else
			return 1;
	}

	public String getHoraireMensuelTravailleDisplay() {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		return df.format(Double.valueOf(horaireMensuelTravaille));
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

	public Integer getIdRemuneration() {
		return idRemuneration;
	}

	public void setIdRemuneration(Integer idRemuneration) {
		this.idRemuneration = idRemuneration;
	}

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
	}

	public Integer getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(Integer idSalarie) {
		this.idSalarie = idSalarie;
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

	public Integer getIdRevenuComplementaire() {
		return idRevenuComplementaire;
	}

	public void setIdRevenuComplementaire(Integer idRevenuComplementaire) {
		this.idRevenuComplementaire = idRevenuComplementaire;
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

	public String getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(String coefficient) {
		this.coefficient = coefficient;
	}

	public String getSalaireDeBaseCommentaire() {
		return salaireDeBaseCommentaire;
	}

	public void setSalaireDeBaseCommentaire(String salaireDeBaseCommentaire) {
		this.salaireDeBaseCommentaire = salaireDeBaseCommentaire;
	}

	public String getSalaireMinimumConventionnelCommentaire() {
		return salaireMinimumConventionnelCommentaire;
	}

	public void setSalaireMinimumConventionnelCommentaire(
			String salaireMinimumConventionnelCommentaire) {
		this.salaireMinimumConventionnelCommentaire = salaireMinimumConventionnelCommentaire;
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

	public String getHeuresSup25Commentaire() {
		return heuresSup25Commentaire;
	}

	public void setHeuresSup25Commentaire(String heuresSup25Commentaire) {
		this.heuresSup25Commentaire = heuresSup25Commentaire;
	}

	public String getHoraireMensuelTravaille() {
		return horaireMensuelTravaille;
	}

	public void setHoraireMensuelTravaille(String horaireMensuelTravaille) {
		this.horaireMensuelTravaille = horaireMensuelTravaille;
	}

	public String getTauxHoraireBrut() {
		return tauxHoraireBrut;
	}

	public void setTauxHoraireBrut(String tauxHoraireBrut) {
		this.tauxHoraireBrut = tauxHoraireBrut;
	}

	public String getSalaireMensuelBrut() {
		return salaireMensuelBrut;
	}

	public void setSalaireMensuelBrut(String salaireMensuelBrut) {
		this.salaireMensuelBrut = salaireMensuelBrut;
	}

	public String getSalaireMensuelConventionnelBrut() {
		return salaireMensuelConventionnelBrut;
	}

	public void setSalaireMensuelConventionnelBrut(
			String salaireMensuelConventionnelBrut) {
		this.salaireMensuelConventionnelBrut = salaireMensuelConventionnelBrut;
	}

	public String getSalaireMinimumConventionnel() {
		return salaireMinimumConventionnel;
	}

	public void setSalaireMinimumConventionnel(
			String salaireMinimumConventionnel) {
		this.salaireMinimumConventionnel = salaireMinimumConventionnel;
	}

	public String getSalaireMinimumConventionnelActualisation() {
		return salaireMinimumConventionnelActualisation;
	}

	public void setSalaireMinimumConventionnelActualisation(
			String salaireMinimumConventionnelActualisation) {
		this.salaireMinimumConventionnelActualisation = salaireMinimumConventionnelActualisation;
	}

	public String getSalaireMinimumConventionnelNPrec() {
		return salaireMinimumConventionnelNPrec;
	}

	public void setSalaireMinimumConventionnelNPrec(
			String salaireMinimumConventionnelNPrec) {
		this.salaireMinimumConventionnelNPrec = salaireMinimumConventionnelNPrec;
	}

	public String getSalaireMinimumConventionnelEvol() {
		return salaireMinimumConventionnelEvol;
	}

	public void setSalaireMinimumConventionnelEvol(
			String salaireMinimumConventionnelEvol) {
		this.salaireMinimumConventionnelEvol = (!salaireMinimumConventionnelEvol
				.equals("0.00")) ? salaireMinimumConventionnelEvol : "";
	}

	public String getSalaireDeBase() {
		return salaireDeBase;
	}

	public void setSalaireDeBase(String salaireDeBase) {
		this.salaireDeBase = salaireDeBase;
	}

	public String getSalaireDeBaseActualisation() {
		return salaireDeBaseActualisation;
	}

	public void setSalaireDeBaseActualisation(String salaireDeBaseActualisation) {
		this.salaireDeBaseActualisation = salaireDeBaseActualisation;
	}

	public String getSalaireDeBaseNPrec() {
		return salaireDeBaseNPrec;
	}

	public void setSalaireDeBaseNPrec(String salaireDeBaseNPrec) {
		this.salaireDeBaseNPrec = salaireDeBaseNPrec;
	}

	public String getSalaireDeBaseEvol() {
		return salaireDeBaseEvol;
	}

	public void setSalaireDeBaseEvol(String salaireDeBaseEvol) {
		this.salaireDeBaseEvol = (!salaireDeBaseEvol.equals("0.00")) ? salaireDeBaseEvol
				: "";
	}

	public String getAugmentationCollective() {
		return augmentationCollective;
	}

	public void setAugmentationCollective(String augmentationCollective) {
		this.augmentationCollective = augmentationCollective;
	}

	public String getAugmentationCollectiveActualisation() {
		return augmentationCollectiveActualisation;
	}

	public void setAugmentationCollectiveActualisation(
			String augmentationCollectiveActualisation) {
		this.augmentationCollectiveActualisation = augmentationCollectiveActualisation;
	}

	public String getAugmentationCollectiveNPrec() {
		return augmentationCollectiveNPrec;
	}

	public void setAugmentationCollectiveNPrec(
			String augmentationCollectiveNPrec) {
		this.augmentationCollectiveNPrec = augmentationCollectiveNPrec;
	}

	public String getAugmentationCollectiveEvol() {
		return augmentationCollectiveEvol;
	}

	public void setAugmentationCollectiveEvol(String augmentationCollectiveEvol) {
		this.augmentationCollectiveEvol = (!augmentationCollectiveEvol
				.equals("0.00")) ? augmentationCollectiveEvol : "";
	}

	public String getAugmentationIndividuelle() {
		return augmentationIndividuelle;
	}

	public void setAugmentationIndividuelle(String augmentationIndividuelle) {
		this.augmentationIndividuelle = augmentationIndividuelle;
	}

	public String getAugmentationIndividuelleActualisation() {
		return augmentationIndividuelleActualisation;
	}

	public void setAugmentationIndividuelleActualisation(
			String augmentationIndividuelleActualisation) {
		this.augmentationIndividuelleActualisation = augmentationIndividuelleActualisation;
	}

	public String getAugmentationIndividuelleNPrec() {
		return augmentationIndividuelleNPrec;
	}

	public void setAugmentationIndividuelleNPrec(
			String augmentationIndividuelleNPrec) {
		this.augmentationIndividuelleNPrec = augmentationIndividuelleNPrec;
	}

	public String getAugmentationIndividuelleEvol() {
		return augmentationIndividuelleEvol;
	}

	public void setAugmentationIndividuelleEvol(
			String augmentationIndividuelleEvol) {
		this.augmentationIndividuelleEvol = (!augmentationIndividuelleEvol
				.equals("0.00")) ? augmentationIndividuelleEvol : "";
	}

	public String getHeuresSup25() {
		return heuresSup25;
	}

	public void setHeuresSup25(String heuresSup25) {
		this.heuresSup25 = heuresSup25;
	}

	public String getHeuresSup25Actualisation() {
		return heuresSup25Actualisation;
	}

	public void setHeuresSup25Actualisation(String heuresSup25Actualisation) {
		this.heuresSup25Actualisation = heuresSup25Actualisation;
	}

	public String getHeuresSup25NPrec() {
		return heuresSup25NPrec;
	}

	public void setHeuresSup25NPrec(String heuresSup25NPrec) {
		this.heuresSup25NPrec = heuresSup25NPrec;
	}

	public String getHeuresSup25Evol() {
		return heuresSup25Evol;
	}

	public void setHeuresSup25Evol(String heuresSup25Evol) {
		this.heuresSup25Evol = (!heuresSup25Evol.equals("0.00")) ? heuresSup25Evol
				: "";
	}

	public String getHeuresSup50() {
		return heuresSup50;
	}

	public void setHeuresSup50(String heuresSup50) {
		this.heuresSup50 = heuresSup50;
	}

	public String getHeuresSup50Actualisation() {
		return heuresSup50Actualisation;
	}

	public void setHeuresSup50Actualisation(String heuresSup50Actualisation) {
		this.heuresSup50Actualisation = heuresSup50Actualisation;
	}

	public String getHeuresSup50NPrec() {
		return heuresSup50NPrec;
	}

	public void setHeuresSup50NPrec(String heuresSup50NPrec) {
		this.heuresSup50NPrec = heuresSup50NPrec;
	}

	public String getHeuresSup50Evol() {
		return heuresSup50Evol;
	}

	public void setHeuresSup50Evol(String heuresSup50Evol) {
		this.heuresSup50Evol = (!heuresSup50Evol.equals("0.00")) ? heuresSup50Evol
				: "";
	}

	public String getBaseBruteAnnuelle() {
		return baseBruteAnnuelle;
	}

	public void setBaseBruteAnnuelle(String baseBruteAnnuelle) {
		this.baseBruteAnnuelle = baseBruteAnnuelle;
	}

	public String getBaseBruteAnnuelleNPrec() {
		return baseBruteAnnuelleNPrec;
	}

	public void setBaseBruteAnnuelleNPrec(String baseBruteAnnuelleNPrec) {
		this.baseBruteAnnuelleNPrec = baseBruteAnnuelleNPrec;
	}

	public String getBaseBruteAnnuelleActu() {
		return baseBruteAnnuelleActu;
	}

	public void setBaseBruteAnnuelleActu(String baseBruteAnnuelleActu) {
		this.baseBruteAnnuelleActu = baseBruteAnnuelleActu;
	}

	public String getBaseBruteAnnuelleEvol() {
		return baseBruteAnnuelleEvol;
	}

	public void setBaseBruteAnnuelleEvol(String baseBruteAnnuelleEvol) {
		this.baseBruteAnnuelleEvol = (!baseBruteAnnuelleEvol.equals("0.00")) ? baseBruteAnnuelleEvol
				: "";
	}

	public String getRemuGlobale() {
		return remuGlobale;
	}

	public void setRemuGlobale(String remuGlobale) {
		this.remuGlobale = remuGlobale;
	}

	public String getRemuGlobaleNPrec() {
		return remuGlobaleNPrec;
	}

	public void setRemuGlobaleNPrec(String remuGlobaleNPrec) {
		this.remuGlobaleNPrec = remuGlobaleNPrec;
	}

	public String getRemuGlobaleActu() {
		return remuGlobaleActu;
	}

	public void setRemuGlobaleActu(String remuGlobaleActu) {
		this.remuGlobaleActu = remuGlobaleActu;
	}

	public String getRemuGlobaleEvol() {
		return remuGlobaleEvol;
	}

	public void setRemuGlobaleEvol(String remuGlobaleEvol) {
		this.remuGlobaleEvol = remuGlobaleEvol;
	}

	public String getTotalComEvol() {
		return totalComEvol;
	}

	public void setTotalComEvol(String totalComEvol) {
		this.totalComEvol = totalComEvol;
	}

	public String getTotalPfEvol() {
		return totalPfEvol;
	}

	public void setTotalPfEvol(String totalPfEvol) {
		this.totalPfEvol = totalPfEvol;
	}

	public String getTotalPvEvol() {
		return totalPvEvol;
	}

	public void setTotalPvEvol(String totalPvEvol) {
		this.totalPvEvol = totalPvEvol;
	}

	public String getTotalAaEvol() {
		return totalAaEvol;
	}

	public void setTotalAaEvol(String totalAaEvol) {
		this.totalAaEvol = totalAaEvol;
	}

	public String getTotalAnaEvol() {
		return totalAnaEvol;
	}

	public void setTotalAnaEvol(String totalAnaEvol) {
		this.totalAnaEvol = totalAnaEvol;
	}

	public String getTotalNPrecCommission() {
		return totalNPrecCommission;
	}

	public void setTotalNPrecCommission(String totalNPrecCommission) {
		this.totalNPrecCommission = totalNPrecCommission;
	}

	public String getTotalNPrecPrimeFixe() {
		return totalNPrecPrimeFixe;
	}

	public void setTotalNPrecPrimeFixe(String totalNPrecPrimeFixe) {
		this.totalNPrecPrimeFixe = totalNPrecPrimeFixe;
	}

	public String getTotalNPrecPrimeVariable() {
		return totalNPrecPrimeVariable;
	}

	public void setTotalNPrecPrimeVariable(String totalNPrecPrimeVariable) {
		this.totalNPrecPrimeVariable = totalNPrecPrimeVariable;
	}

	public String getTotalNPrecAvantageAssujetti() {
		return totalNPrecAvantageAssujetti;
	}

	public void setTotalNPrecAvantageAssujetti(
			String totalNPrecAvantageAssujetti) {
		this.totalNPrecAvantageAssujetti = totalNPrecAvantageAssujetti;
	}

	public String getTotalNPrecAvantageNonAssujetti() {
		return totalNPrecAvantageNonAssujetti;
	}

	public void setTotalNPrecAvantageNonAssujetti(
			String totalNPrecAvantageNonAssujetti) {
		this.totalNPrecAvantageNonAssujetti = totalNPrecAvantageNonAssujetti;
	}

	public String getTotalNPrecFraisProf() {
		return totalNPrecFraisProf;
	}

	public void setTotalNPrecFraisProf(String totalNPrecFraisProf) {
		this.totalNPrecFraisProf = totalNPrecFraisProf;
	}

	public String getTotalNCommission() {
		return totalNCommission;
	}

	public void setTotalNCommission(String totalNCommission) {
		this.totalNCommission = totalNCommission;
	}

	public String getTotalActuCommission() {
		return totalActuCommission;
	}

	public void setTotalActuCommission(String totalActuCommission) {
		this.totalActuCommission = totalActuCommission;
	}

	public String getTotalNPrimeFixe() {
		return totalNPrimeFixe;
	}

	public void setTotalNPrimeFixe(String totalNPrimeFixe) {
		this.totalNPrimeFixe = totalNPrimeFixe;
	}

	public String getTotalActuPrimeFixe() {
		return totalActuPrimeFixe;
	}

	public void setTotalActuPrimeFixe(String totalActuPrimeFixe) {
		this.totalActuPrimeFixe = totalActuPrimeFixe;
	}

	public String getTotalNPrimeVariable() {
		return totalNPrimeVariable;
	}

	public void setTotalNPrimeVariable(String totalNPrimeVariable) {
		this.totalNPrimeVariable = totalNPrimeVariable;
	}

	public String getTotalActuPrimeVariable() {
		return totalActuPrimeVariable;
	}

	public void setTotalActuPrimeVariable(String totalActuPrimeVariable) {
		this.totalActuPrimeVariable = totalActuPrimeVariable;
	}

	public String getTotalNAvantageAssujetti() {
		return totalNAvantageAssujetti;
	}

	public void setTotalNAvantageAssujetti(String totalNAvantageAssujetti) {
		this.totalNAvantageAssujetti = totalNAvantageAssujetti;
	}

	public String getTotalActuAvantageAssujetti() {
		return totalActuAvantageAssujetti;
	}

	public void setTotalActuAvantageAssujetti(String totalActuAvantageAssujetti) {
		this.totalActuAvantageAssujetti = totalActuAvantageAssujetti;
	}

	public String getTotalNAvantageNonAssujetti() {
		return totalNAvantageNonAssujetti;
	}

	public void setTotalNAvantageNonAssujetti(String totalNAvantageNonAssujetti) {
		this.totalNAvantageNonAssujetti = totalNAvantageNonAssujetti;
	}

	public String getTotalActuAvantageNonAssujetti() {
		return totalActuAvantageNonAssujetti;
	}

	public void setTotalActuAvantageNonAssujetti(
			String totalActuAvantageNonAssujetti) {
		this.totalActuAvantageNonAssujetti = totalActuAvantageNonAssujetti;
	}

	public String getTotalNFraisProf() {
		return totalNFraisProf;
	}

	public void setTotalNFraisProf(String totalNFraisProf) {
		this.totalNFraisProf = totalNFraisProf;
	}

	public String getTotalActuFraisProf() {
		return totalActuFraisProf;
	}

	public void setTotalActuFraisProf(String totalActuFraisProf) {
		this.totalActuFraisProf = totalActuFraisProf;
	}

	public String getTotalFpEvol() {
		return totalFpEvol;
	}

	public void setTotalFpEvol(String totalFpEvol) {
		this.totalFpEvol = totalFpEvol;
	}

	public String getHeuresSupAnnuelles() {
		return heuresSupAnnuelles;
	}

	public void setHeuresSupAnnuelles(String heuresSupAnnuelles) {
		this.heuresSupAnnuelles = heuresSupAnnuelles;
	}

	public String getAvantagesNonAssujettisAnnuels() {
		return avantagesNonAssujettisAnnuels;
	}

	public void setAvantagesNonAssujettisAnnuels(
			String avantagesNonAssujettisAnnuels) {
		this.avantagesNonAssujettisAnnuels = avantagesNonAssujettisAnnuels;
	}

	public List<LienRemunerationRevenuBean> getPfList() {
		return pfList;
	}

	public void setPfList(List<LienRemunerationRevenuBean> pfList) {
		this.pfList = pfList;
	}

	public List<LienRemunerationRevenuBean> getPvList() {
		return pvList;
	}

	public void setPvList(List<LienRemunerationRevenuBean> pvList) {
		this.pvList = pvList;
	}

	public List<LienRemunerationRevenuBean> getAaList() {
		return aaList;
	}

	public void setAaList(List<LienRemunerationRevenuBean> aaList) {
		this.aaList = aaList;
	}

	public List<LienRemunerationRevenuBean> getAnaList() {
		return anaList;
	}

	public void setAnaList(List<LienRemunerationRevenuBean> anaList) {
		this.anaList = anaList;
	}

	public List<LienRemunerationRevenuBean> getFpList() {
		return fpList;
	}

	public void setFpList(List<LienRemunerationRevenuBean> fpList) {
		this.fpList = fpList;
	}

	public String getRemunerationBruteAnnuelle() {
		return remunerationBruteAnnuelle;
	}

	public void setRemunerationBruteAnnuelle(String remunerationBruteAnnuelle) {
		this.remunerationBruteAnnuelle = remunerationBruteAnnuelle;
	}

	public String getRemunerationBruteAnnuelleNPrec() {
		return remunerationBruteAnnuelleNPrec;
	}

	public void setRemunerationBruteAnnuelleNPrec(
			String remunerationBruteAnnuelleNPrec) {
		this.remunerationBruteAnnuelleNPrec = remunerationBruteAnnuelleNPrec;
	}

	public String getRemunerationBruteAnnuelleActu() {
		return remunerationBruteAnnuelleActu;
	}

	public void setRemunerationBruteAnnuelleActu(
			String remunerationBruteAnnuelleActu) {
		this.remunerationBruteAnnuelleActu = remunerationBruteAnnuelleActu;
	}

	public String getRemunerationBruteAnnuelleEvol() {
		return remunerationBruteAnnuelleEvol;
	}

	public void setRemunerationBruteAnnuelleEvol(
			String remunerationBruteAnnuelleEvol) {
		this.remunerationBruteAnnuelleEvol = remunerationBruteAnnuelleEvol;
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

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public List<LienRemunerationRevenuBean> getComList() {
		return comList;
	}

	public void setComList(List<LienRemunerationRevenuBean> comList) {
		this.comList = comList;
	}

}
