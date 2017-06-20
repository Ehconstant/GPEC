package com.cci.gpec.db.base;

import java.io.Serializable;

import com.cci.gpec.db.Groupe;

public abstract class BaseMappingReprise implements Serializable {

	public static String PROP_ENTITE = "Entite";
	public static String PROP_OLD_ID = "OldId";
	public static String PROP_NEW_ID = "NewId";
	public static String PROP_ID = "Id";
	public static String PROP_ID_GROUPE = "IdGroupe";

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private Integer id;

	// fields
	private String entite;
	private Integer oldId;
	private Integer newId;

	// many to one
	private Groupe groupe;

	// constructors
	public BaseMappingReprise() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMappingReprise(java.lang.Integer _id) {
		this.setId(_id);
		initialize();
	}

	protected void initialize() {
	}

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.cci.gpec.db.base.BaseMappingReprise))
			return false;
		else {
			com.cci.gpec.db.base.BaseMappingReprise mObj = (com.cci.gpec.db.base.BaseMappingReprise) obj;
			if (null == this.getId() || null == mObj.getId())
				return false;
			else
				return (this.getId().equals(mObj.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":"
						+ this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public com.cci.gpec.db.Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(com.cci.gpec.db.Groupe groupe) {
		this.groupe = groupe;
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