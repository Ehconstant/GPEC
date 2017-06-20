package com.cci.gpec.commons;

import com.cci.gpec.ModelBean;

public class FamilleDomaineFormationBean extends ModelBean {
	
	private int id;
	private String nom;
	
	public FamilleDomaineFormationBean() {
		super();
		
	}

	public FamilleDomaineFormationBean(int id, String nom) {
		super(id, nom);
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
}

