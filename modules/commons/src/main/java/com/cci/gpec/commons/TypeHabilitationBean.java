package com.cci.gpec.commons;

import com.cci.gpec.ModelBean;

public class TypeHabilitationBean extends ModelBean implements
		Comparable<TypeHabilitationBean> {

	private Integer idGroupe;

	public TypeHabilitationBean() {
		super();

	}

	public TypeHabilitationBean(int id, String nom) {
		super(id, nom);
	}

	public int compareTo(TypeHabilitationBean o) {
		TypeHabilitationBean p = o;
		return getNom().toUpperCase().compareTo(p.getNom().toUpperCase());
	}

	public Integer getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(Integer idGroupe) {
		this.idGroupe = idGroupe;
	}
}
