package com.cci.gpec.commons;

import com.cci.gpec.ModelBean;

public class TypeAbsenceBean extends ModelBean implements
		Comparable<TypeAbsenceBean> {

	private Integer idGroupe;

	public TypeAbsenceBean() {
		super();

	}

	public TypeAbsenceBean(int id, String nom) {
		super(id, nom);
		// TODO Auto-generated constructor stub
	}

	public int compareTo(TypeAbsenceBean o) {
		TypeAbsenceBean p = o;

		return getNom().toUpperCase().compareTo(p.getNom().toUpperCase());
	}

	public Integer getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(Integer idGroupe) {
		this.idGroupe = idGroupe;
	}
}
