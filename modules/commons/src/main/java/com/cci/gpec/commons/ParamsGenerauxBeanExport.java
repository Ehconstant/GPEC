package com.cci.gpec.commons;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParamsGenerauxBeanExport {

	// primary key
	private Integer id;

	// fields

	DecimalFormat df = new DecimalFormat();
	DecimalFormatSymbols symbols = new DecimalFormatSymbols();

	private Integer ageLegalRetraiteAnN;
	private Integer ageLegalRetraiteAnN1;
	private Integer ageLegalRetraiteAnN2;

	private Integer effectifPhysiqueAnN;
	private Integer effectifPhysiqueAnN1;
	private Integer effectifPhysiqueAnN2;

	private String effectifMoyenAnN;
	private String effectifMoyenAnN1;
	private String effectifMoyenAnN2;

	private String effectifEtpAnN;
	private String effectifEtpAnN1;
	private String effectifEtpAnN2;

	private Integer dureeTravailAnNHeuresSal;
	private Integer dureeTravailAnN1HeuresSal;
	private Integer dureeTravailAnN2HeuresSal;

	private Integer dureeTravailAnNJoursSal;
	private Integer dureeTravailAnN1JoursSal;
	private Integer dureeTravailAnN2JoursSal;

	private String dureeTravailAnNJoursEffectifTot;
	private String dureeTravailAnN1JoursEffectifTot;
	private String dureeTravailAnN2JoursEffectifTot;

	private String dureeTravailAnNHeuresRealiseesEffectifTot;
	private String dureeTravailAnN1HeuresRealiseesEffectifTot;
	private String dureeTravailAnN2HeuresRealiseesEffectifTot;

	private String masseSalarialeAnN;
	private String masseSalarialeAnN1;
	private String masseSalarialeAnN2;

	private String pourcentageFormationAnN;
	private String pourcentageFormationAnN1;
	private String pourcentageFormationAnN2;

	private Integer idEntreprise;
	private String justificatif;
	private String libelleEntreprise;

	private String anneeAnN;
	private String anneeAnN1;
	private String anneeAnN2;

	private String date;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

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

	public Integer getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(Integer idEntreprise) {
		this.idEntreprise = idEntreprise;
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

	public String getEffectifMoyenAnN() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (effectifMoyenAnN == null)
			return null;
		else
			return (!effectifMoyenAnN.equals("")) ? df.format(Double
					.valueOf(effectifMoyenAnN)) : null;
	}

	public void setEffectifMoyenAnN(String effectifMoyenAnN) {
		this.effectifMoyenAnN = effectifMoyenAnN;
	}

	public String getEffectifMoyenAnN1() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (effectifMoyenAnN1 == null) {
			return null;
		} else {
			return (!effectifMoyenAnN1.equals("")) ? df.format(Double
					.valueOf(effectifMoyenAnN1)) : null;
		}
	}

	public void setEffectifMoyenAnN1(String effectifMoyenAnN1) {
		this.effectifMoyenAnN1 = effectifMoyenAnN1;
	}

	public String getEffectifMoyenAnN2() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (effectifMoyenAnN2 == null) {
			return null;
		} else {
			return (!effectifMoyenAnN2.equals("")) ? df.format(Double
					.valueOf(effectifMoyenAnN2)) : null;
		}
	}

	public void setEffectifMoyenAnN2(String effectifMoyenAnN2) {
		this.effectifMoyenAnN2 = effectifMoyenAnN2;
	}

	public String getEffectifEtpAnN() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (effectifEtpAnN == null) {
			return null;
		} else {
			return (!effectifEtpAnN.equals("")) ? df.format(Double
					.valueOf(effectifEtpAnN)) : null;
		}
	}

	public void setEffectifEtpAnN(String effectifEtpAnN) {
		this.effectifEtpAnN = effectifEtpAnN;
	}

	public String getEffectifEtpAnN1() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (effectifEtpAnN1 == null) {
			return null;
		} else {
			return (!effectifEtpAnN1.equals("")) ? df.format(Double
					.valueOf(effectifEtpAnN1)) : null;
		}
	}

	public void setEffectifEtpAnN1(String effectifEtpAnN1) {
		this.effectifEtpAnN1 = effectifEtpAnN1;
	}

	public String getEffectifEtpAnN2() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (effectifEtpAnN2 == null) {
			return null;
		} else {
			return (!effectifEtpAnN2.equals("")) ? df.format(Double
					.valueOf(effectifEtpAnN2)) : null;
		}
	}

	public void setEffectifEtpAnN2(String effectifEtpAnN2) {
		this.effectifEtpAnN2 = effectifEtpAnN2;
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

	public String getDureeTravailAnNJoursEffectifTot() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (dureeTravailAnNJoursEffectifTot == null) {
			return null;
		} else {
			return (!dureeTravailAnNJoursEffectifTot.equals("")) ? df
					.format(Double.valueOf(dureeTravailAnNJoursEffectifTot))
					: null;
		}
	}

	public void setDureeTravailAnNJoursEffectifTot(
			String dureeTravailAnNJoursEffectifTot) {

		this.dureeTravailAnNJoursEffectifTot = dureeTravailAnNJoursEffectifTot;
	}

	public String getDureeTravailAnN1JoursEffectifTot() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (dureeTravailAnN1JoursEffectifTot == null) {
			return null;
		} else {
			return (!dureeTravailAnN1JoursEffectifTot.equals("")) ? df
					.format(Double.valueOf(dureeTravailAnN1JoursEffectifTot))
					: null;
		}
	}

	public void setDureeTravailAnN1JoursEffectifTot(
			String dureeTravailAnN1JoursEffectifTot) {
		this.dureeTravailAnN1JoursEffectifTot = dureeTravailAnN1JoursEffectifTot;
	}

	public String getDureeTravailAnN2JoursEffectifTot() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (dureeTravailAnN2JoursEffectifTot == null) {
			return null;
		} else {
			return (!dureeTravailAnN2JoursEffectifTot.equals("")) ? df
					.format(Double.valueOf(dureeTravailAnN2JoursEffectifTot))
					: null;
		}
	}

	public void setDureeTravailAnN2JoursEffectifTot(
			String dureeTravailAnN2JoursEffectifTot) {
		this.dureeTravailAnN2JoursEffectifTot = dureeTravailAnN2JoursEffectifTot;
	}

	public String getDureeTravailAnNHeuresRealiseesEffectifTot() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (dureeTravailAnNHeuresRealiseesEffectifTot == null) {
			return null;
		} else {
			return (!dureeTravailAnNHeuresRealiseesEffectifTot.equals("")) ? df
					.format(Double
							.valueOf(dureeTravailAnNHeuresRealiseesEffectifTot))
					: null;
		}
	}

	public void setDureeTravailAnNHeuresRealiseesEffectifTot(
			String dureeTravailAnNHeuresRealiseesEffectifTot) {
		this.dureeTravailAnNHeuresRealiseesEffectifTot = dureeTravailAnNHeuresRealiseesEffectifTot;
	}

	public String getDureeTravailAnN1HeuresRealiseesEffectifTot() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (dureeTravailAnN1HeuresRealiseesEffectifTot == null) {
			return null;
		} else {
			return (!dureeTravailAnN1HeuresRealiseesEffectifTot.equals("")) ? df
					.format(Double
							.valueOf(dureeTravailAnN1HeuresRealiseesEffectifTot))
					: null;
		}
	}

	public void setDureeTravailAnN1HeuresRealiseesEffectifTot(
			String dureeTravailAnN1HeuresRealiseesEffectifTot) {
		this.dureeTravailAnN1HeuresRealiseesEffectifTot = dureeTravailAnN1HeuresRealiseesEffectifTot;
	}

	public String getDureeTravailAnN2HeuresRealiseesEffectifTot() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (dureeTravailAnN2HeuresRealiseesEffectifTot == null) {
			return null;
		} else {
			return (!dureeTravailAnN2HeuresRealiseesEffectifTot.equals("")) ? df
					.format(Double
							.valueOf(dureeTravailAnN2HeuresRealiseesEffectifTot))
					: null;
		}
	}

	public void setDureeTravailAnN2HeuresRealiseesEffectifTot(
			String dureeTravailAnN2HeuresRealiseesEffectifTot) {
		this.dureeTravailAnN2HeuresRealiseesEffectifTot = dureeTravailAnN2HeuresRealiseesEffectifTot;
	}

	public String getMasseSalarialeAnN() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (masseSalarialeAnN == null) {
			return null;
		} else {
			return (!masseSalarialeAnN.equals("")) ? df.format(Double
					.valueOf(masseSalarialeAnN)) : null;
		}
	}

	public void setMasseSalarialeAnN(String masseSalarialeAnN) {
		this.masseSalarialeAnN = masseSalarialeAnN;
	}

	public String getMasseSalarialeAnN1() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (masseSalarialeAnN1 == null) {
			return null;
		} else {
			return (!masseSalarialeAnN1.equals("")) ? df.format(Double
					.valueOf(masseSalarialeAnN1)) : null;
		}
	}

	public void setMasseSalarialeAnN1(String masseSalarialeAnN1) {
		this.masseSalarialeAnN1 = masseSalarialeAnN1;
	}

	public String getMasseSalarialeAnN2() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (masseSalarialeAnN2 == null) {
			return null;
		} else {
			return (!masseSalarialeAnN2.equals("")) ? df.format(Double
					.valueOf(masseSalarialeAnN2)) : null;
		}
	}

	public void setMasseSalarialeAnN2(String masseSalarialeAnN2) {
		this.masseSalarialeAnN2 = masseSalarialeAnN2;
	}

	public String getPourcentageFormationAnN() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (pourcentageFormationAnN == null) {
			return null;
		} else {
			return (!pourcentageFormationAnN.equals("")) ? df.format(Double
					.valueOf(pourcentageFormationAnN)) : null;
		}
	}

	public void setPourcentageFormationAnN(String pourcentageFormationAnN) {
		this.pourcentageFormationAnN = pourcentageFormationAnN;
	}

	public String getPourcentageFormationAnN1() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (pourcentageFormationAnN1 == null) {
			return null;
		} else {
			return (!pourcentageFormationAnN1.equals("")) ? df.format(Double
					.valueOf(pourcentageFormationAnN1)) : null;
		}
	}

	public void setPourcentageFormationAnN1(String pourcentageFormationAnN1) {
		this.pourcentageFormationAnN1 = pourcentageFormationAnN1;
	}

	public String getPourcentageFormationAnN2() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (pourcentageFormationAnN2 == null) {
			return null;
		} else {
			return (!pourcentageFormationAnN2.equals("")) ? df.format(Double
					.valueOf(pourcentageFormationAnN2)) : null;
		}
	}

	public void setPourcentageFormationAnN2(String pourcentageFormationAnN2) {
		this.pourcentageFormationAnN2 = pourcentageFormationAnN2;
	}

	public String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(String justificatif) {
		this.justificatif = justificatif;
	}

}
