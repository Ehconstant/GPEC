package com.cci.gpec.commons;

import com.cci.gpec.ModelBean;

public class MetierBean extends ModelBean implements Comparable<MetierBean> {

	private Integer idFamilleMetier;
	private String nomFamilleMetier;
	private String difficultes;
	private String difficultesRencontrees;

	private Integer idGroupe;

	public MetierBean() {
		super();
	}

	public MetierBean(int id, String nom) {
		super(id, nom);
		// TODO Auto-generated constructor stub
	}

	public Integer getIdFamilleMetier() {
		return idFamilleMetier;
	}

	public void setIdFamilleMetier(Integer idFamilleMetier) {
		this.idFamilleMetier = idFamilleMetier;
	}

	public String getNomFamilleMetier() {
		return nomFamilleMetier;
	}

	public void setNomFamilleMetier(String nomFamilleMetier) {
		this.nomFamilleMetier = nomFamilleMetier;
	}

	public int compareTo(MetierBean o) {
		MetierBean p = o;
		if (getNom().toUpperCase().equals(p.getNom().toUpperCase())) {
			return this.nomFamilleMetier.toUpperCase().compareTo(
					p.getNomFamilleMetier().toUpperCase());
		}
		return getNom().toUpperCase().compareTo(p.getNom().toUpperCase());

	}

	public String getDifficultes() {
		return difficultes;
	}

	public void setDifficultes(String difficultes) {
		this.difficultes = difficultes;
	}

	public String getDifficultesRencontrees() {
		if (difficultes != null) {
			if (difficultes.split(";").length >= 1) {
				if (difficultes.split(";").length == 1
						&& Integer.valueOf(difficultes.split(";")[0]) == 0)
					return "non";
				return "oui";
			} else
				return "non";
		} else
			return "non";
	}

	public void setDifficultesRencontrees(String difficultesRencontrees) {
		this.difficultesRencontrees = difficultesRencontrees;
	}

	public Integer getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(Integer idGroupe) {
		this.idGroupe = idGroupe;
	}

}
