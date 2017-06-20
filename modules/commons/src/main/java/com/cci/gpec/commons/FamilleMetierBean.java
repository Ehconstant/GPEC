package com.cci.gpec.commons;

import com.cci.gpec.ModelBean;

public class FamilleMetierBean extends ModelBean implements
		Comparable<FamilleMetierBean> {

	private int id;
	private String nom;

	public FamilleMetierBean() {
		super();

	}

	public FamilleMetierBean(int id, String nom) {
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

	public int compareTo(FamilleMetierBean o) {
		FamilleMetierBean familleMetierBean = (FamilleMetierBean) o;
		return this.nom.compareTo(familleMetierBean.nom);
	}
}
