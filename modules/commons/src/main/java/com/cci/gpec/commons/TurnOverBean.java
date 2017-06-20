package com.cci.gpec.commons;

public class TurnOverBean implements Comparable {

	private Integer entreesN2;
	private Integer sortiesN2;
	private Integer entreesN1;
	private Integer sortiesN1;
	private Integer entreesN;
	private Integer sortiesN;

	private String tauxN;
	private String tauxN1;
	private String styleTauxN;
	private String styleTauxN1;
	private String style;
	private Integer id;
	private boolean footer = false;
	private boolean taux = false;
	private String libelle;

	public TurnOverBean() {
		footer = false;
		tauxN = null;
		tauxN1 = null;
	}

	public Integer getEntreesN2() {
		return entreesN2;
	}

	public void setEntreesN2(Integer entreesN2) {
		this.entreesN2 = entreesN2;
	}

	public Integer getSortiesN2() {
		return sortiesN2;
	}

	public void setSortiesN2(Integer sortiesN2) {
		this.sortiesN2 = sortiesN2;
	}

	public Integer getEntreesN1() {
		return entreesN1;
	}

	public void setEntreesN1(Integer entreesN1) {
		this.entreesN1 = entreesN1;
	}

	public Integer getSortiesN1() {
		return sortiesN1;
	}

	public void setSortiesN1(Integer sortiesN1) {
		this.sortiesN1 = sortiesN1;
	}

	public Integer getEntreesN() {
		return entreesN;
	}

	public void setEntreesN(Integer entreesN) {
		this.entreesN = entreesN;
	}

	public Integer getSortiesN() {
		return sortiesN;
	}

	public void setSortiesN(Integer sortiesN) {
		this.sortiesN = sortiesN;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getTauxN() {
		return tauxN;
	}

	public void setTauxN(String tauxN) {
		this.tauxN = tauxN;
	}

	public String getTauxN1() {
		return tauxN1;
	}

	public void setTauxN1(String tauxN1) {
		this.tauxN1 = tauxN1;
	}

	public boolean isFooter() {
		return footer;
	}

	public void setFooter(boolean footer) {
		this.footer = footer;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isTaux() {
		return taux;
	}

	public void setTaux(boolean taux) {
		this.taux = taux;
	}

	public String getStyleTauxN() {
		return styleTauxN;
	}

	public void setStyleTauxN(String styleTauxN) {
		this.styleTauxN = styleTauxN;
	}

	public String getStyleTauxN1() {
		return styleTauxN1;
	}

	public void setStyleTauxN1(String styleTauxN1) {
		this.styleTauxN1 = styleTauxN1;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int compareTo(Object o) {
		return this.libelle.compareTo(((TurnOverBean) o).libelle);
	}

}
