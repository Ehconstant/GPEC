package com.cci.gpec.commons;

import com.cci.gpec.ModelBean;

public class FicheMetierEntrepriseBean extends ModelBean {

	private Integer idFicheMetier;
	private Integer idEntreprise;

	public FicheMetierEntrepriseBean() {
		super();
	}

	public FicheMetierEntrepriseBean(Integer id, String nom) {
		super(id, nom);
		// TODO Auto-generated constructor stub
	}

	public Integer getIdFicheMetier() {
		return idFicheMetier;
	}

	public void setIdFicheMetier(Integer idFicheMetier) {
		this.idFicheMetier = idFicheMetier;
	}

	public Integer getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(Integer idEntreprise) {
		this.idEntreprise = idEntreprise;
	}

}
