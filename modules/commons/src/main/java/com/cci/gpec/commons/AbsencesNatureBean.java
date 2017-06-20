package com.cci.gpec.commons;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class AbsencesNatureBean {

	private Double nbAbsenceByNatureTwoYearAgo;
	private Double nbAbsenceByNatureOneYearAgo;
	private Double nbAbsenceByNatureCurrentYear;

	private String nbAbsenceByNatureTwoYearAgoTaux = null;
	private String nbAbsenceByNatureOneYearAgoTaux = null;
	private String nbAbsenceByNatureCurrentYearTaux = null;

	DecimalFormat df = new DecimalFormat();
	DecimalFormatSymbols symbols = new DecimalFormatSymbols();

	private boolean footer = false;

	private String nomTypeAbsence;

	public Double getNbAbsenceByNatureTwoYearAgo() {
		if (nbAbsenceByNatureTwoYearAgo == null)
			return null;
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.0");
		return Double.valueOf(df.format(nbAbsenceByNatureTwoYearAgo));
	}

	public void setNbAbsenceByNatureTwoYearAgo(
			Double nbAbsenceByNatureTwoYearAgo) {
		this.nbAbsenceByNatureTwoYearAgo = nbAbsenceByNatureTwoYearAgo;
	}

	public Double getNbAbsenceByNatureOneYearAgo() {
		if (nbAbsenceByNatureOneYearAgo == null)
			return null;
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.0");
		return Double.valueOf(df.format(nbAbsenceByNatureOneYearAgo));
	}

	public void setNbAbsenceByNatureOneYearAgo(
			Double nbAbsenceByNatureOneYearAgo) {
		this.nbAbsenceByNatureOneYearAgo = nbAbsenceByNatureOneYearAgo;
	}

	public Double getNbAbsenceByNatureCurrentYear() {
		if (nbAbsenceByNatureCurrentYear == null)
			return null;
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.0");
		return Double.valueOf(df.format(nbAbsenceByNatureCurrentYear));
	}

	public void setNbAbsenceByNatureCurrentYear(
			Double nbAbsenceByNatureCurrentYear) {
		this.nbAbsenceByNatureCurrentYear = nbAbsenceByNatureCurrentYear;
	}

	public String getNomTypeAbsence() {
		return nomTypeAbsence;
	}

	public void setNomTypeAbsence(String nomTypeAbsence) {
		this.nomTypeAbsence = nomTypeAbsence;
	}

	public String getNbAbsenceByNatureTwoYearAgoTaux() {
		return nbAbsenceByNatureTwoYearAgoTaux;
	}

	public void setNbAbsenceByNatureTwoYearAgoTaux(
			String nbAbsenceByNatureTwoYearAgoTaux) {
		this.nbAbsenceByNatureTwoYearAgoTaux = nbAbsenceByNatureTwoYearAgoTaux;
	}

	public String getNbAbsenceByNatureOneYearAgoTaux() {
		return nbAbsenceByNatureOneYearAgoTaux;
	}

	public void setNbAbsenceByNatureOneYearAgoTaux(
			String nbAbsenceByNatureOneYearAgoTaux) {
		this.nbAbsenceByNatureOneYearAgoTaux = nbAbsenceByNatureOneYearAgoTaux;
	}

	public String getNbAbsenceByNatureCurrentYearTaux() {
		return nbAbsenceByNatureCurrentYearTaux;
	}

	public void setNbAbsenceByNatureCurrentYearTaux(
			String nbAbsenceByNatureCurrentYearTaux) {
		this.nbAbsenceByNatureCurrentYearTaux = nbAbsenceByNatureCurrentYearTaux;
	}

	public boolean isFooter() {
		return footer;
	}

	public void setFooter(boolean footer) {
		this.footer = footer;
	}

}
