package com.cci.gpec.commons;

import java.io.File;
import java.util.Date;

public class AccidentBean implements Comparable<AccidentBean> {

	// primary key
	private int id;

	// fields
	private Date dateAccident;
	private Date dateRechute;
	private Integer nombreJourArret;
	private Integer nombreJourArretRechute;
	private Integer idTypeAccidentBeanSelected;
	private String nomTypeAccident;
	private String commentaire;

	private Boolean aggravation;
	private Boolean initial;

	private Integer idTypeLesionBeanSelected;
	private Integer idTypeLesionRechuteBeanSelected;
	private String nomTypeLesion;
	private String nomTypeLesionRechute;

	private Integer idTypeCauseAccidentBeanSelected;
	private String nomTypeCauseAccident;
	private Integer idSalarie;
	private Integer idAbsence;

	private Date dateDebutAbsence;
	private Date dateFinAbsence;

	private String justificatif;

	private File justif;

	private boolean fileError;

	public File getJustif() {
		if (justificatif != null)
			return new File(justificatif);
		else
			return null;
	}

	public void setJustif(File justif) {
		this.justif = justif;
	}

	public String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(String justificatif) {
		this.justificatif = justificatif;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateAccident() {
		return dateAccident;
	}

	public void setDateAccident(Date dateAccident) {
		this.dateAccident = dateAccident;
	}

	public Integer getNombreJourArret() {
		return nombreJourArret;
	}

	public void setNombreJourArret(Integer nombreJourArret) {
		this.nombreJourArret = nombreJourArret;
	}

	public Integer getIdTypeAccidentBeanSelected() {
		return idTypeAccidentBeanSelected;
	}

	public void setIdTypeAccidentBeanSelected(Integer idTypeAccidentBeanSelected) {
		this.idTypeAccidentBeanSelected = idTypeAccidentBeanSelected;
	}

	public Integer getIdTypeLesionBeanSelected() {
		return idTypeLesionBeanSelected;
	}

	public void setIdTypeLesionBeanSelected(Integer idTypeLesionBeanSelected) {
		this.idTypeLesionBeanSelected = idTypeLesionBeanSelected;
	}

	public Integer getIdTypeCauseAccidentBeanSelected() {
		return idTypeCauseAccidentBeanSelected;
	}

	public void setIdTypeCauseAccidentBeanSelected(
			Integer idTypeCauseAccidentBeanSelected) {
		this.idTypeCauseAccidentBeanSelected = idTypeCauseAccidentBeanSelected;
	}

	public String getNomTypeAccident() {
		return nomTypeAccident;
	}

	public void setNomTypeAccident(String nomTypeAccident) {
		this.nomTypeAccident = nomTypeAccident;
	}

	public String getNomTypeLesion() {
		return nomTypeLesion;
	}

	public void setNomTypeLesion(String nomTypeLesion) {
		this.nomTypeLesion = nomTypeLesion;
	}

	public String getNomTypeCauseAccident() {
		return nomTypeCauseAccident;
	}

	public void setNomTypeCauseAccident(String nomTypeCauseAccident) {
		this.nomTypeCauseAccident = nomTypeCauseAccident;
	}

	public Integer getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(Integer idSalarie) {
		this.idSalarie = idSalarie;
	}

	public int compareTo(AccidentBean o) {

		return dateAccident.compareTo(o.dateAccident);
	}

	public Date getDateDebutAbsence() {
		return dateDebutAbsence;
	}

	public void setDateDebutAbsence(Date dateDebutAbsence) {
		this.dateDebutAbsence = dateDebutAbsence;
	}

	public Date getDateFinAbsence() {
		return dateFinAbsence;
	}

	public void setDateFinAbsence(Date dateFinAbsence) {
		this.dateFinAbsence = dateFinAbsence;
	}

	public Integer getIdAbsence() {
		return idAbsence;
	}

	public void setIdAbsence(Integer idAbsence) {
		this.idAbsence = idAbsence;
	}

	public boolean isFileError() {
		if (getJustif() != null) {
			if (getJustif().exists() && getJustif().isFile()
					&& getJustif().canRead())
				return false;
			else
				return true;
		} else
			return false;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof AccidentBean))
			return false;
		else {
			AccidentBean accident = (AccidentBean) obj;
			return this.getId() == accident.getId();
		}
	}

	public Date getDateRechute() {
		return dateRechute;
	}

	public void setDateRechute(Date dateRechute) {
		this.dateRechute = dateRechute;
	}

	public Integer getNombreJourArretRechute() {
		return nombreJourArretRechute;
	}

	public void setNombreJourArretRechute(Integer nombreJourArretRechute) {
		this.nombreJourArretRechute = nombreJourArretRechute;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Boolean isAggravation() {
		return aggravation;
	}

	public void setAggravation(Boolean aggravation) {
		this.aggravation = aggravation;
	}

	public Boolean isInitial() {
		return initial;
	}

	public void setInitial(Boolean initial) {
		this.initial = initial;
	}

	public Integer getIdTypeLesionRechuteBeanSelected() {
		return idTypeLesionRechuteBeanSelected;
	}

	public void setIdTypeLesionRechuteBeanSelected(
			Integer idTypeLesionRechuteBeanSelected) {
		this.idTypeLesionRechuteBeanSelected = idTypeLesionRechuteBeanSelected;
	}

	public String getNomTypeLesionRechute() {
		return nomTypeLesionRechute;
	}

	public void setNomTypeLesionRechute(String nomTypeLesionRechute) {
		this.nomTypeLesionRechute = nomTypeLesionRechute;
	}

	public Boolean getAggravation() {
		return aggravation;
	}

	public Boolean getInitial() {
		return initial;
	}

}
