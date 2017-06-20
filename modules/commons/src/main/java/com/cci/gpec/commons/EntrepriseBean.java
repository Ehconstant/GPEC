package com.cci.gpec.commons;

import java.io.File;
import java.util.Date;

import com.cci.gpec.ModelBean;

public class EntrepriseBean extends ModelBean implements
		Comparable<EntrepriseBean> {

	private long numSiret;
	private String codeApe;
	private Date dateCreation;
	private String cciRattachement;
	private boolean suiviFormations;
	private boolean suiviAccidents;
	private boolean suiviAbsences;
	private boolean suiviCompetences;
	private boolean suiviDIF;
	private Integer DIFMax;
	private int idGroupe;

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

	public boolean isSuiviFormations() {
		return suiviFormations;
	}

	public void setSuiviFormations(boolean suiviFormations) {
		this.suiviFormations = suiviFormations;
	}

	public boolean isSuiviAccidents() {
		return suiviAccidents;
	}

	public void setSuiviAccidents(boolean suiviAccidents) {
		this.suiviAccidents = suiviAccidents;
	}

	public boolean isSuiviAbsences() {
		return suiviAbsences;
	}

	public void setSuiviAbsences(boolean suiviAbsences) {
		this.suiviAbsences = suiviAbsences;
	}

	public boolean isSuiviCompetences() {
		return suiviCompetences;
	}

	public void setSuiviCompetences(boolean suiviCompetences) {
		this.suiviCompetences = suiviCompetences;
	}

	public boolean isSuiviDIF() {
		return suiviDIF;
	}

	public void setSuiviDIF(boolean suiviDIF) {
		this.suiviDIF = suiviDIF;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public String getCciRattachement() {
		return cciRattachement;
	}

	public void setCciRattachement(String cciRattachement) {
		this.cciRattachement = cciRattachement;
	}

	public EntrepriseBean() {
		super();

	}

	public EntrepriseBean(int id, String nom) {
		super(id, nom);
		// TODO Auto-generated constructor stub
	}

	public long getNumSiret() {
		return numSiret;
	}

	public void setNumSiret(long numSiret) {
		this.numSiret = numSiret;
	}

	public String getCodeApe() {
		return codeApe;
	}

	public void setCodeApe(String codeApe) {
		this.codeApe = codeApe;
	}

	public int compareTo(EntrepriseBean o) {
		EntrepriseBean p = o;
		return getNom().toUpperCase().compareTo(p.getNom().toUpperCase());
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
		if (!(obj instanceof EntrepriseBean))
			return false;
		else {
			EntrepriseBean entreprise = (EntrepriseBean) obj;
			return this.getId() == entreprise.getId();
		}
	}

	public Integer getDIFMax() {
		if (DIFMax == null)
			return 126;
		else
			return DIFMax;
	}

	public void setDIFMax(Integer max) {
		DIFMax = max;
	}

	public int getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(int idGroupe) {
		this.idGroupe = idGroupe;
	}

}
