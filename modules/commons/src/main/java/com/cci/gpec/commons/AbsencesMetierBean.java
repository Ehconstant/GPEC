package com.cci.gpec.commons;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.cci.gpec.ModelBean;

public class AbsencesMetierBean extends ModelBean {

	private Double nbAbsenceByMetierTwoYearAgo;
	private Double nbAbsenceByMetierOneYearAgo;
	private Double nbAbsenceByMetierCurYear;

	DecimalFormat df = new DecimalFormat();
	DecimalFormatSymbols symbols = new DecimalFormatSymbols();

	public Double getNbAbsenceByMetierTwoYearAgo() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.0");
		return Double.valueOf(df.format(nbAbsenceByMetierTwoYearAgo));
	}

	public void setNbAbsenceByMetierTwoYearAgo(
			Double nbAbsenceByMetierTwoYearAgo) {
		this.nbAbsenceByMetierTwoYearAgo = nbAbsenceByMetierTwoYearAgo;
	}

	public Double getNbAbsenceByMetierOneYearAgo() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.0");
		return Double.valueOf(df.format(nbAbsenceByMetierOneYearAgo));
	}

	public void setNbAbsenceByMetierOneYearAgo(
			Double nbAbsenceByMetierOneYearAgo) {
		this.nbAbsenceByMetierOneYearAgo = nbAbsenceByMetierOneYearAgo;
	}

	public Double getNbAbsenceByMetierCurYear() {
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.0");
		return Double.valueOf(df.format(nbAbsenceByMetierCurYear));
	}

	public void setNbAbsenceByMetierCurYear(Double nbAbsenceByMetierCurYear) {
		this.nbAbsenceByMetierCurYear = nbAbsenceByMetierCurYear;
	}
}
