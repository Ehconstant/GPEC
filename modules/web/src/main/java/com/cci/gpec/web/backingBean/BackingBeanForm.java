package com.cci.gpec.web.backingBean;


public class BackingBeanForm {

	public int id;
	public String nom;
	
	public BackingBeanForm() {}
	
	public BackingBeanForm(int id, String nom) {
		super();
		this.id = id;
		this.nom = nom;
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
