package com.cci.gpec.commons;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

public class FormationBean implements Comparable<FormationBean> {

	// primary key
	private int id;

	// fields
	private Date debutFormation;
	private Date finFormation;
	private String nomFormation;
	private String organismeFormation;
	private String mode;
	private Integer volumeHoraire;
	private Integer dif;
	private Integer horsDif;

	private Integer idDomaineFormationBeanSelected;
	private Integer idTypeAbsenceGenere;
	private String nomDomaineFormation;
	private Integer idSalarie;
	private Integer idAbsence;
	private Double nombreJourOuvre;
	private boolean generationAbs;

	private String coutOpcaDisplay;
	private String coutEntrepriseDisplay;
	private String coutAutreDisplay;

	private Double coutOpca;
	private Double coutEntreprise;
	private Double coutAutre;

	private String justificatif;

	private File justif;

	private boolean fileError;

	public File getJustif() {
		if (justificatif != null) {
			return new File(justificatif);
		} else {
			return null;
		}
	}

	public void setJustif(File justif) {
		this.justif = justif;
	}

	public boolean isGenerationAbs() {
		return generationAbs;
	}

	public void setGenerationAbs(boolean generationAbs) {
		this.generationAbs = generationAbs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDebutFormation() {
		return debutFormation;
	}

	public void setDebutFormation(Date debutFormation) {
		this.debutFormation = debutFormation;
	}

	public Date getFinFormation() {
		return finFormation;
	}

	public void setFinFormation(Date finFormation) {
		this.finFormation = finFormation;
	}

	public String getNomFormation() {
		return nomFormation;
	}

	public void setNomFormation(String nomFormation) {
		this.nomFormation = nomFormation;
	}

	public String getOrganismeFormation() {
		return organismeFormation;
	}

	public void setOrganismeFormation(String organismeFormation) {
		this.organismeFormation = organismeFormation;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getVolumeHoraire() {
		return volumeHoraire;
	}

	public void setVolumeHoraire(Integer volumeHoraire) {
		this.volumeHoraire = volumeHoraire;
	}

	public Integer getDif() {
		return dif;
	}

	public void setDif(Integer dif) {
		this.dif = dif;
	}

	public Integer getIdDomaineFormationBeanSelected() {
		return idDomaineFormationBeanSelected;
	}

	public void setIdDomaineFormationBeanSelected(
			Integer idDomaineFormationBeanSelected) {
		this.idDomaineFormationBeanSelected = idDomaineFormationBeanSelected;
	}

	public String getNomDomaineFormation() {
		return nomDomaineFormation;
	}

	public void setNomDomaineFormation(String nomDomaineFormation) {
		this.nomDomaineFormation = nomDomaineFormation;
	}

	public int compareTo(FormationBean o) {
		if (debutFormation.equals(o.debutFormation)) {
			return finFormation.compareTo(o.finFormation);
		} else {
			return debutFormation.compareTo(o.debutFormation);
		}
	}

	public Integer getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(Integer idSalarie) {
		this.idSalarie = idSalarie;
	}

	public Integer getIdTypeAbsenceGenere() {
		return idTypeAbsenceGenere;
	}

	public void setIdTypeAbsenceGenere(Integer idTypeAbsenceGenere) {
		this.idTypeAbsenceGenere = idTypeAbsenceGenere;
	}

	public Integer getIdAbsence() {
		return idAbsence;
	}

	public void setIdAbsence(Integer idAbsence) {
		this.idAbsence = idAbsence;
	}

	public Double getNombreJourOuvre() {
		return nombreJourOuvre;
	}

	public void setNombreJourOuvre(Double nombreJourOuvre) {
		this.nombreJourOuvre = nombreJourOuvre;
	}

	public String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(String justificatif) {
		this.justificatif = justificatif;
	}

	public boolean isFileError() {
		if (getJustif() != null) {
			if (getJustif().exists() && getJustif().isFile()
					&& getJustif().canRead()) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof FormationBean)) {
			return false;
		} else {
			FormationBean formation = (FormationBean) obj;
			return this.getId() == formation.getId();
		}
	}

	public Double getCoutOpca() {
		return coutOpca;
	}

	public void setCoutOpca(Double coutOpca) {
		this.coutOpca = coutOpca;
	}

	public Double getCoutEntreprise() {
		return coutEntreprise;
	}

	public void setCoutEntreprise(Double coutEntreprise) {
		this.coutEntreprise = coutEntreprise;
	}

	public String getCoutOpcaDisplay() {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		if (coutOpca != null) {
			return df.format(coutOpca);
		} else {
			return null;
		}
	}

	public void setCoutOpcaDisplay(String coutOpcaDisplay) {
		this.coutOpcaDisplay = coutOpcaDisplay;
	}

	public String getCoutEntrepriseDisplay() {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		if (coutEntreprise != null) {
			return df.format(coutEntreprise);
		} else {
			return null;
		}
	}

	public void setCoutEntrepriseDisplay(String coutEntrepriseDisplay) {
		this.coutEntrepriseDisplay = coutEntrepriseDisplay;
	}

	public String getCoutAutreDisplay() {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		if (coutAutre != null) {
			return df.format(coutAutre);
		} else {
			return null;
		}
	}

	public void setCoutAutreDisplay(String coutAutreDisplay) {
		this.coutAutreDisplay = coutAutreDisplay;
	}

	public Double getCoutAutre() {
		return coutAutre;
	}

	public void setCoutAutre(Double coutAutre) {
		this.coutAutre = coutAutre;
	}

	public Integer getHorsDif() {
		return horsDif;
	}

	public void setHorsDif(Integer horsDif) {
		this.horsDif = horsDif;
	}

}
