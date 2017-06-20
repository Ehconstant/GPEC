package com.cci.gpec;

import com.cci.gpec.commons.ParametreBean;

public class ModelBean extends ParametreBean {

	public ModelBean() {

	}

	public ModelBean(int id, String nom) {
		super(id, nom);
	}

	@Override
	public int getId() {
		return super.getId();
	}

	@Override
	public String getNom() {
		return super.getNom();
	}

	@Override
	public void setId(int id) {
		super.setId(id);
	}

	@Override
	public void setNom(String nom) {
		super.setNom(nom);
	}

	@Override
	public String toString() {
		return getNom();
	}
}
