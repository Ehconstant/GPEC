package com.cci.gpec.commons;

public class DifficulteBean {

	private Integer id;
	private String categorie;
	private String intitule;

	public DifficulteBean(Integer id, String categorie, String intitule) {
		super();
		this.id = id;
		this.categorie = categorie;
		this.intitule = intitule;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
