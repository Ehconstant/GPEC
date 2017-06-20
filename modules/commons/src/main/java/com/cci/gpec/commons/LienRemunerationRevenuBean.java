package com.cci.gpec.commons;

public class LienRemunerationRevenuBean implements
		Comparable<LienRemunerationRevenuBean> {

	// primary key
	private Integer idLienRemunerationRevenuComplementaire;

	// fields
	private Integer idRemuneration;
	private Integer idRevenuComplementaire;
	private String montant;
	private String actualisation;
	private String commentaire;

	private String montantNPrec;
	private String libelle;

	private boolean remonteNPrec;

	public String getMontantNPrec() {
		return montantNPrec;
	}

	public void setMontantNPrec(String montantNPrec) {
		this.montantNPrec = montantNPrec;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public int compareTo(LienRemunerationRevenuBean o) {
		if (this.idLienRemunerationRevenuComplementaire == o
				.getIdLienRemunerationRevenuComplementaire())
			return 1;
		return 0;
	}

	public Integer getIdLienRemunerationRevenuComplementaire() {
		return idLienRemunerationRevenuComplementaire;
	}

	public void setIdLienRemunerationRevenuComplementaire(
			Integer idLienRemunerationRevenuComplementaire) {
		this.idLienRemunerationRevenuComplementaire = idLienRemunerationRevenuComplementaire;
	}

	public Integer getIdRemuneration() {
		return idRemuneration;
	}

	public void setIdRemuneration(Integer idRemuneration) {
		this.idRemuneration = idRemuneration;
	}

	public Integer getIdRevenuComplementaire() {
		return idRevenuComplementaire;
	}

	public void setIdRevenuComplementaire(Integer idRevenuComplementaire) {
		this.idRevenuComplementaire = idRevenuComplementaire;
	}

	public String getMontant() {
		return montant;
	}

	public void setMontant(String montant) {
		this.montant = montant;
	}

	public String getActualisation() {
		return actualisation;
	}

	public void setActualisation(String actualisation) {
		this.actualisation = actualisation;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public boolean isRemonteNPrec() {
		return remonteNPrec;
	}

	public void setRemonteNPrec(boolean remonteNPrec) {
		this.remonteNPrec = remonteNPrec;
	}

}
