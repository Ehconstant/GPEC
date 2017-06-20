package com.cci.gpec.commons;

public class ParametreBean {
	
	private int id;
	private String nom;

	public ParametreBean(int id, String nom) {
		super();
		this.id = id;
		this.nom = nom;
	}
	
	public ParametreBean() {
		super();
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
