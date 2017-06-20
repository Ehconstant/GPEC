package com.cci.gpec.commons;

public class ArretTravailBean extends TypeAccidentBean{
	
	private int nbJourTwoYearAgo;
	private int nbJourOneYearAgo;
	private int nbJourCurYear;
	private String tauxN2;
	private String tauxN1;
	private String tauxN;
	private boolean boolTitre;
	private boolean boolTaux;
	private boolean espace;
	private String style;
	
	public ArretTravailBean() {
		boolTitre = false;
		boolTaux = false;
		espace = false;
	}
	
	public int getNbJourTwoYearAgo() {
		return nbJourTwoYearAgo;
	}
	public void setNbJourTwoYearAgo(int nbJourTwoYearAgo) {
		this.nbJourTwoYearAgo = nbJourTwoYearAgo;
	}
	public int getNbJourOneYearAgo() {
		return nbJourOneYearAgo;
	}
	public void setNbJourOneYearAgo(int nbJourOneYearAgo) {
		this.nbJourOneYearAgo = nbJourOneYearAgo;
	}
	public int getNbJourCurYear() {
		return nbJourCurYear;
	}
	public void setNbJourCurYear(int nbJourCurYear) {
		this.nbJourCurYear = nbJourCurYear;
	}

	public String getTauxN2() {
		return tauxN2;
	}

	public void setTauxN2(String tauxN2) {
		this.tauxN2 = tauxN2;
	}

	public String getTauxN1() {
		return tauxN1;
	}

	public void setTauxN1(String tauxN1) {
		this.tauxN1 = tauxN1;
	}

	public String getTauxN() {
		return tauxN;
	}

	public void setTauxN(String tauxN) {
		this.tauxN = tauxN;
	}

	public boolean isBoolTitre() {
		return boolTitre;
	}

	public void setBoolTitre(boolean boolTitre) {
		this.boolTitre = boolTitre;
	}

	public boolean isBoolTaux() {
		return boolTaux;
	}

	public void setBoolTaux(boolean boolTaux) {
		this.boolTaux = boolTaux;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public boolean isEspace() {
		return espace;
	}

	public void setEspace(boolean espace) {
		this.espace = espace;
	}

}
