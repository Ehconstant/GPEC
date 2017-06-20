package com.cci.gpec.commons;

import java.util.Date;

public class PersonneAChargeBean implements Comparable {

	private int id;

	private String nom;
	private String prenom;
	private String lienParente;
	private Date dateNaissance;

	private Integer idSalarie;

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

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getLienParente() {
		return lienParente;
	}

	public void setLienParente(String lienParente) {
		this.lienParente = lienParente;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public Integer getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(Integer idSalarie) {
		this.idSalarie = idSalarie;
	}

	public int compareTo(Object p) {
		if (this.getLienParente().compareTo(
				((PersonneAChargeBean) p).getLienParente()) == 0) {
			if (this.getNom().compareTo(((PersonneAChargeBean) p).getNom()) == 0) {
				return this.getPrenom().compareTo(
						((PersonneAChargeBean) p).getPrenom());
			} else {
				return this.getNom().compareTo(
						((PersonneAChargeBean) p).getNom());
			}
		} else {
			return this.getLienParente().compareTo(
					((PersonneAChargeBean) p).getLienParente());
		}
	}

}
