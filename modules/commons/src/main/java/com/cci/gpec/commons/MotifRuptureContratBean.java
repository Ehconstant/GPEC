package com.cci.gpec.commons;

import com.cci.gpec.ModelBean;

public class MotifRuptureContratBean extends ModelBean {

	private Integer ordreAffichage;

	public MotifRuptureContratBean() {
		super();

	}

	public MotifRuptureContratBean(int id, String nom) {
		super(id, nom);
		// TODO Auto-generated constructor stub
	}

	public Integer getOrdreAffichage() {
		return ordreAffichage;
	}

	public void setOrdreAffichage(Integer ordreAffichage) {
		this.ordreAffichage = ordreAffichage;
	}

}
