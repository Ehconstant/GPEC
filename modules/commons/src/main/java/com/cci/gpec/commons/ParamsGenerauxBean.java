package com.cci.gpec.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParamsGenerauxBean {

	// primary key
	private Integer id;

	private Integer annee;

	// fields
	private Integer ageLegalRetraiteAnN;
	private Integer ageLegalRetraiteAnN1;
	private Integer ageLegalRetraiteAnN2;

	private Integer effectifPhysiqueAnN;
	private Integer effectifPhysiqueAnN1;
	private Integer effectifPhysiqueAnN2;

	private Double effectifMoyenAnN;
	private Double effectifMoyenAnN1;
	private Double effectifMoyenAnN2;

	private Double effectifEtpAnN;
	private Double effectifEtpAnN1;
	private Double effectifEtpAnN2;

	private Integer dureeTravailAnNHeuresSal;
	private Integer dureeTravailAnN1HeuresSal;
	private Integer dureeTravailAnN2HeuresSal;

	private Integer dureeTravailAnNJoursSal;
	private Integer dureeTravailAnN1JoursSal;
	private Integer dureeTravailAnN2JoursSal;

	private Double dureeTravailAnNJoursEffectifTot;
	private Double dureeTravailAnN1JoursEffectifTot;
	private Double dureeTravailAnN2JoursEffectifTot;

	private Double dureeTravailAnNHeuresRealiseesEffectifTot;
	private Double dureeTravailAnN1HeuresRealiseesEffectifTot;
	private Double dureeTravailAnN2HeuresRealiseesEffectifTot;

	private Double masseSalarialeAnN;
	private Double masseSalarialeAnN1;
	private Double masseSalarialeAnN2;

	private Double pourcentageFormationAnN;
	private Double pourcentageFormationAnN1;
	private Double pourcentageFormationAnN2;

	/*
	 * private Integer effectifMoyAnN; private Integer effectifMoyAnN1; private
	 * Integer effectifMoyAnN2;
	 * 
	 * private Integer effectifPhysiqueApprentisAnN; private Integer
	 * effectifPhysiqueApprentisAnN1; private Integer
	 * effectifPhysiqueApprentisAnN2;
	 */

	private Integer idEntreprise;
	private String libelleEntreprise;

	private String anneeAnN;
	private String anneeAnN1;
	private String anneeAnN2;

	private String date;

	public String getDate() {
		Date actu = new Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		return "Date de l'export " + df.format(actu);
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAgeLegalRetraiteAnN() {
		return ageLegalRetraiteAnN;
	}

	public void setAgeLegalRetraiteAnN(Integer ageLegalRetraiteAnN) {
		this.ageLegalRetraiteAnN = ageLegalRetraiteAnN;
	}

	public Integer getAgeLegalRetraiteAnN1() {
		return ageLegalRetraiteAnN1;
	}

	public void setAgeLegalRetraiteAnN1(Integer ageLegalRetraiteAnN1) {
		this.ageLegalRetraiteAnN1 = ageLegalRetraiteAnN1;
	}

	public Integer getAgeLegalRetraiteAnN2() {
		return ageLegalRetraiteAnN2;
	}

	public void setAgeLegalRetraiteAnN2(Integer ageLegalRetraiteAnN2) {
		this.ageLegalRetraiteAnN2 = ageLegalRetraiteAnN2;
	}

	/*
	 * public Integer getEffectifMoyAnN() { return effectifMoyAnN; }
	 * 
	 * public void setEffectifMoyAnN(Integer effectifMoyAnN) {
	 * this.effectifMoyAnN = effectifMoyAnN; }
	 * 
	 * public Integer getEffectifMoyAnN1() { return effectifMoyAnN1; }
	 * 
	 * public void setEffectifMoyAnN1(Integer effectifMoyAnN1) {
	 * this.effectifMoyAnN1 = effectifMoyAnN1; }
	 * 
	 * public Integer getEffectifMoyAnN2() { return effectifMoyAnN2; }
	 * 
	 * public void setEffectifMoyAnN2(Integer effectifMoyAnN2) {
	 * this.effectifMoyAnN2 = effectifMoyAnN2; }
	 */

	public Double getDureeTravailAnNHeuresRealiseesEffectifTot() {
		return dureeTravailAnNHeuresRealiseesEffectifTot;
	}

	public void setDureeTravailAnNHeuresRealiseesEffectifTot(
			Double dureeTravailAnNHeuresRealiseesEffectifTot) {
		this.dureeTravailAnNHeuresRealiseesEffectifTot = dureeTravailAnNHeuresRealiseesEffectifTot;
	}

	public Double getDureeTravailAnN1HeuresRealiseesEffectifTot() {
		return dureeTravailAnN1HeuresRealiseesEffectifTot;
	}

	public void setDureeTravailAnN1HeuresRealiseesEffectifTot(
			Double dureeTravailAnN1HeuresRealiseesEffectifTot) {
		this.dureeTravailAnN1HeuresRealiseesEffectifTot = dureeTravailAnN1HeuresRealiseesEffectifTot;
	}

	public Double getDureeTravailAnN2HeuresRealiseesEffectifTot() {
		return dureeTravailAnN2HeuresRealiseesEffectifTot;
	}

	public void setDureeTravailAnN2HeuresRealiseesEffectifTot(
			Double dureeTravailAnN2HeuresRealiseesEffectifTot) {
		this.dureeTravailAnN2HeuresRealiseesEffectifTot = dureeTravailAnN2HeuresRealiseesEffectifTot;
	}

	public Double getDureeTravailAnNJoursEffectifTot() {
		return dureeTravailAnNJoursEffectifTot;
	}

	public void setDureeTravailAnNJoursEffectifTot(
			Double dureeTravailAnNJoursEffectifTot) {
		this.dureeTravailAnNJoursEffectifTot = dureeTravailAnNJoursEffectifTot;
	}

	public Double getDureeTravailAnN1JoursEffectifTot() {
		return dureeTravailAnN1JoursEffectifTot;
	}

	public void setDureeTravailAnN1JoursEffectifTot(
			Double dureeTravailAnN1JoursEffectifTot) {
		this.dureeTravailAnN1JoursEffectifTot = dureeTravailAnN1JoursEffectifTot;
	}

	public Double getDureeTravailAnN2JoursEffectifTot() {
		return dureeTravailAnN2JoursEffectifTot;
	}

	public void setDureeTravailAnN2JoursEffectifTot(
			Double dureeTravailAnN2JoursEffectifTot) {
		this.dureeTravailAnN2JoursEffectifTot = dureeTravailAnN2JoursEffectifTot;
	}

	public Integer getDureeTravailAnNJoursSal() {
		return dureeTravailAnNJoursSal;
	}

	public void setDureeTravailAnNJoursSal(Integer dureeTravailAnNJoursSal) {
		this.dureeTravailAnNJoursSal = dureeTravailAnNJoursSal;
	}

	public Integer getDureeTravailAnN1JoursSal() {
		return dureeTravailAnN1JoursSal;
	}

	public void setDureeTravailAnN1JoursSal(Integer dureeTravailAnN1JoursSal) {
		this.dureeTravailAnN1JoursSal = dureeTravailAnN1JoursSal;
	}

	public Integer getDureeTravailAnN2JoursSal() {
		return dureeTravailAnN2JoursSal;
	}

	public void setDureeTravailAnN2JoursSal(Integer dureeTravailAnN2JoursSal) {
		this.dureeTravailAnN2JoursSal = dureeTravailAnN2JoursSal;
	}

	public Integer getDureeTravailAnNHeuresSal() {
		return dureeTravailAnNHeuresSal;
	}

	public void setDureeTravailAnNHeuresSal(Integer dureeTravailAnNHeuresSal) {
		this.dureeTravailAnNHeuresSal = dureeTravailAnNHeuresSal;
	}

	public Integer getDureeTravailAnN1HeuresSal() {
		return dureeTravailAnN1HeuresSal;
	}

	public void setDureeTravailAnN1HeuresSal(Integer dureeTravailAnN1HeuresSal) {
		this.dureeTravailAnN1HeuresSal = dureeTravailAnN1HeuresSal;
	}

	public Integer getDureeTravailAnN2HeuresSal() {
		return dureeTravailAnN2HeuresSal;
	}

	public void setDureeTravailAnN2HeuresSal(Integer dureeTravailAnN2HeuresSal) {
		this.dureeTravailAnN2HeuresSal = dureeTravailAnN2HeuresSal;
	}

	public Integer getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(Integer idEntreprise) {
		this.idEntreprise = idEntreprise;
	}

	public Double getMasseSalarialeAnN() {
		return masseSalarialeAnN;
	}

	public void setMasseSalarialeAnN(Double masseSalarialeAnN) {
		this.masseSalarialeAnN = masseSalarialeAnN;
	}

	public Double getMasseSalarialeAnN1() {
		return masseSalarialeAnN1;
	}

	public void setMasseSalarialeAnN1(Double masseSalarialeAnN1) {
		this.masseSalarialeAnN1 = masseSalarialeAnN1;
	}

	public Double getMasseSalarialeAnN2() {
		return masseSalarialeAnN2;
	}

	public void setMasseSalarialeAnN2(Double masseSalarialeAnN2) {
		this.masseSalarialeAnN2 = masseSalarialeAnN2;
	}

	public Double getEffectifMoyenAnN() {
		return effectifMoyenAnN;
	}

	public void setEffectifMoyenAnN(Double effectifMoyenAnN) {
		this.effectifMoyenAnN = effectifMoyenAnN;
	}

	public Double getEffectifMoyenAnN1() {
		return effectifMoyenAnN1;
	}

	public void setEffectifMoyenAnN1(Double effectifMoyenAnN1) {
		this.effectifMoyenAnN1 = effectifMoyenAnN1;
	}

	public Double getEffectifMoyenAnN2() {
		return effectifMoyenAnN2;
	}

	public void setEffectifMoyenAnN2(Double effectifMoyenAnN2) {
		this.effectifMoyenAnN2 = effectifMoyenAnN2;
	}

	public Double getPourcentageFormationAnN() {
		return pourcentageFormationAnN;
	}

	public void setPourcentageFormationAnN(Double pourcentageFormationAnN) {
		this.pourcentageFormationAnN = pourcentageFormationAnN;
	}

	public Double getPourcentageFormationAnN1() {
		return pourcentageFormationAnN1;
	}

	public void setPourcentageFormationAnN1(Double pourcentageFormationAnN1) {
		this.pourcentageFormationAnN1 = pourcentageFormationAnN1;
	}

	public Double getPourcentageFormationAnN2() {
		return pourcentageFormationAnN2;
	}

	public void setPourcentageFormationAnN2(Double pourcentageFormationAnN2) {
		this.pourcentageFormationAnN2 = pourcentageFormationAnN2;
	}

	public String getLibelleEntreprise() {
		return "Entreprise " + libelleEntreprise;
	}

	public void setLibelleEntreprise(String libelleEntreprise) {
		this.libelleEntreprise = libelleEntreprise;
	}

	public String getAnneeAnN() {
		Date aujourdhui = new Date();
		int annee = aujourdhui.getYear();
		return "Année " + (1900 + annee);
	}

	public void setAnneeAnN(String anneeAnN) {
		this.anneeAnN = anneeAnN;
	}

	public String getAnneeAnN1() {
		Date aujourdhui = new Date();
		int annee = aujourdhui.getYear();
		return "Année " + (1900 + (annee - 1));
	}

	public void setAnneeAnN1(String anneeAnN1) {
		this.anneeAnN1 = anneeAnN1;
	}

	public String getAnneeAnN2() {
		Date aujourdhui = new Date();
		int annee = aujourdhui.getYear();
		return "Année " + (1900 + (annee - 2));
	}

	public void setAnneeAnN2(String anneeAnN2) {
		this.anneeAnN2 = anneeAnN2;
	}

	public Integer getEffectifPhysiqueAnN() {
		return effectifPhysiqueAnN;
	}

	public void setEffectifPhysiqueAnN(Integer effectifPhysiqueAnN) {
		this.effectifPhysiqueAnN = effectifPhysiqueAnN;
	}

	public Integer getEffectifPhysiqueAnN1() {
		return effectifPhysiqueAnN1;
	}

	public void setEffectifPhysiqueAnN1(Integer effectifPhysiqueAnN1) {
		this.effectifPhysiqueAnN1 = effectifPhysiqueAnN1;
	}

	public Integer getEffectifPhysiqueAnN2() {
		return effectifPhysiqueAnN2;
	}

	public void setEffectifPhysiqueAnN2(Integer effectifPhysiqueAnN2) {
		this.effectifPhysiqueAnN2 = effectifPhysiqueAnN2;
	}

	/*
	 * public Integer getEffectifPhysiqueApprentisAnN() { return
	 * effectifPhysiqueApprentisAnN; }
	 * 
	 * public void setEffectifPhysiqueApprentisAnN( Integer
	 * effectifPhysiqueApprentisAnN) { this.effectifPhysiqueApprentisAnN =
	 * effectifPhysiqueApprentisAnN; }
	 * 
	 * public Integer getEffectifPhysiqueApprentisAnN1() { return
	 * effectifPhysiqueApprentisAnN1; }
	 * 
	 * public void setEffectifPhysiqueApprentisAnN1( Integer
	 * effectifPhysiqueApprentisAnN1) { this.effectifPhysiqueApprentisAnN1 =
	 * effectifPhysiqueApprentisAnN1; }
	 * 
	 * public Integer getEffectifPhysiqueApprentisAnN2() { return
	 * effectifPhysiqueApprentisAnN2; }
	 * 
	 * public void setEffectifPhysiqueApprentisAnN2( Integer
	 * effectifPhysiqueApprentisAnN2) { this.effectifPhysiqueApprentisAnN2 =
	 * effectifPhysiqueApprentisAnN2; }
	 */

	public Double getEffectifEtpAnN() {
		return effectifEtpAnN;
	}

	public void setEffectifEtpAnN(Double effectifEtpAnN) {
		this.effectifEtpAnN = effectifEtpAnN;
	}

	public Double getEffectifEtpAnN1() {
		return effectifEtpAnN1;
	}

	public void setEffectifEtpAnN1(Double effectifEtpAnN1) {
		this.effectifEtpAnN1 = effectifEtpAnN1;
	}

	public Double getEffectifEtpAnN2() {
		return effectifEtpAnN2;
	}

	public void setEffectifEtpAnN2(Double effectifEtpAnN2) {
		this.effectifEtpAnN2 = effectifEtpAnN2;
	}

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
	}

}
