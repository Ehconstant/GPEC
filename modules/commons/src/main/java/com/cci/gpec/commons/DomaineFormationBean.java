package com.cci.gpec.commons;

import com.cci.gpec.ModelBean;

public class DomaineFormationBean extends ModelBean {
	
	private int idFamilleDomaineFormation;
	private String nomFamilleDomaineFormation;
	
	
	public DomaineFormationBean() {
		super();
		
	}

	public DomaineFormationBean(int id, String nom) {
		super(id, nom);
		// TODO Auto-generated constructor stub
	}

	public int getIdFamilleDomaineFormation() {
		return idFamilleDomaineFormation;
	}

	public void setIdFamilleDomaineFormation(int idFamilleDomaineFormation) {
		this.idFamilleDomaineFormation = idFamilleDomaineFormation;
	}

	public String getNomFamilleDomaineFormation() {
		return nomFamilleDomaineFormation;
	}

	public void setNomFamilleDomaineFormation(String nomFamilleDomaineFormation) {
		this.nomFamilleDomaineFormation = nomFamilleDomaineFormation;
	}
}
