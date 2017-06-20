package com.cci.gpec.commons;

import com.cci.gpec.ModelBean;

public class ServiceBean extends ModelBean implements Comparable<ServiceBean> {

	private Integer idEntreprise;
	private String nomEntreprise;

	public ServiceBean() {
		super();
	}

	public ServiceBean(int id, String nom) {
		super(id, nom);
	}

	public Integer getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(Integer idEntreprise) {
		this.idEntreprise = idEntreprise;
	}

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public int compareTo(ServiceBean o) {
		ServiceBean p = o;
		if (this.nomEntreprise.toUpperCase().equals(
				p.nomEntreprise.toUpperCase())) {
			return getNom().toUpperCase().compareTo(p.getNom().toUpperCase());
		}
		return nomEntreprise.toUpperCase().compareTo(
				p.nomEntreprise.toUpperCase());
	}
}
