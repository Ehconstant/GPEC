package com.cci.gpec.commons;

public class MappingRepriseBean {

	// primary key
	private int id;

	// fields
	private String entite;
	private Integer oldId;
	private Integer newId;

	// many to one
	private int idGroupe;

	public MappingRepriseBean(String entite, Integer oldId, Integer newId,
			int idGroupe) {
		this.entite = entite;
		this.oldId = oldId;
		this.newId = newId;
		this.idGroupe = idGroupe;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(int idGroupe) {
		this.idGroupe = idGroupe;
	}

	public String getEntite() {
		return entite;
	}

	public void setEntite(String entite) {
		this.entite = entite;
	}

	public Integer getOldId() {
		return oldId;
	}

	public void setOldId(Integer oldId) {
		this.oldId = oldId;
	}

	public Integer getNewId() {
		return newId;
	}

	public void setNewId(Integer newId) {
		this.newId = newId;
	}
}
