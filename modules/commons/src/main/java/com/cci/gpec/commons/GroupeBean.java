package com.cci.gpec.commons;

import java.util.Date;

import com.cci.gpec.ModelBean;

public class GroupeBean extends ModelBean {

	private boolean deleted;
	private Date finPeriodeEssai;

	public GroupeBean() {
		super();

	}

	public GroupeBean(int id, String nom) {
		super(id, nom);
		// TODO Auto-generated constructor stub
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Date getFinPeriodeEssai() {
		return finPeriodeEssai;
	}

	public void setFinPeriodeEssai(Date finPeriodeEssai) {
		this.finPeriodeEssai = finPeriodeEssai;
	}
}
