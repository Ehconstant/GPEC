package com.cci.gpec.commons;

public class RevenuComplementaireBean implements
		Comparable<RevenuComplementaireBean> {

	private String libelle;
	private String type;

	private Integer id;

	private int idGroupe;

	public int compareTo(RevenuComplementaireBean o) {
		RevenuComplementaireBean p = o;
		if (this.type.toUpperCase().equals(p.type.toUpperCase())) {
			return this.libelle.toUpperCase().compareTo(
					p.getLibelle().toUpperCase());
		}
		return type.toUpperCase().compareTo(p.getType().toUpperCase());
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getType2() {
		if (type.equals("commission")) {
			return "Commission";
		}
		if (type.equals("prime_fixe")) {
			return "Prime fixe";
		}
		if (type.equals("prime_variable")) {
			return "Prime variable";
		}
		if (type.equals("avantage_assujetti")) {
			return "Avantage assujetti";
		}
		if (type.equals("avantage_non_assujetti")) {
			return "Avantage non assujetti";
		}
		if (type.equals("frais_professionnel")) {
			return "Frais professionnel";
		}
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(int idGroupe) {
		this.idGroupe = idGroupe;
	}

}
