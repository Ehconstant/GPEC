package com.cci.gpec.commons;


public class ObjectifsEntretienBean {

	private Integer idObjectif;
	private Integer idEntretien;

	private String intitule;
	private String delai;
	private String moyen;
	private String resultat;
	private String commentaire;

	public Integer getIdObjectif() {
		return idObjectif;
	}

	public void setIdObjectif(Integer idObjectif) {
		this.idObjectif = idObjectif;
	}

	public Integer getIdEntretien() {
		return idEntretien;
	}

	public void setIdEntretien(Integer idEntretien) {
		this.idEntretien = idEntretien;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getDelai() {
		return delai;
	}

	public void setDelai(String delai) {
		this.delai = delai;
	}

	public String getMoyen() {
		return moyen;
	}

	public void setMoyen(String moyen) {
		this.moyen = moyen;
	}

	public String getResultat() {
		return resultat;
	}

	public void setResultat(String resultat) {
		this.resultat = resultat;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

}
