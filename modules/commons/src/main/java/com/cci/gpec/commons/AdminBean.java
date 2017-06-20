package com.cci.gpec.commons;

public class AdminBean {
	private int id;
	private String logo;
	private boolean migration;
	private int idGroupe;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public boolean isMigration() {
		return migration;
	}

	public void setMigration(boolean migration) {
		this.migration = migration;
	}

	public int getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(int idGroupe) {
		this.idGroupe = idGroupe;
	}

}
