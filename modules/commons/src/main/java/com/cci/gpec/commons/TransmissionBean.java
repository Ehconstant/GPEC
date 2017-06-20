package com.cci.gpec.commons;

import java.util.Date;

public class TransmissionBean {
	private int id;
	private Date dateTransmission;
	private Date dateDerniereDemande;
	private int idGroupe;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateTransmission() {
		return dateTransmission;
	}

	public void setDateTransmission(Date dateTransmission) {
		this.dateTransmission = dateTransmission;
	}

	public Date getDateDerniereDemande() {
		return dateDerniereDemande;
	}

	public void setDateDerniereDemande(Date dateDerniereDemande) {
		this.dateDerniereDemande = dateDerniereDemande;
	}

	public int getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(int idGroupe) {
		this.idGroupe = idGroupe;
	}
}
