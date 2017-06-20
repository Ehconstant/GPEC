package com.cci.gpec.commons;

import java.io.File;
import java.util.Date;

public class AbsenceBean implements Comparable<AbsenceBean> {

	// primary key
	private Integer id;

	// fields
	private Date debutAbsence;
	private Date finAbsence;
	private Integer idTypeAbsenceSelected;
	private String nomTypeAbsence;
	private Integer idSalarie;
	private boolean warning = false;
	private boolean auto = false;
	private Double nombreJourOuvre;
	private String justificatif;
	private boolean fileError;

	private File justif;

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

	public String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(String justificatif) {
		this.justificatif = justificatif;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDebutAbsence() {
		return debutAbsence;
	}

	public void setDebutAbsence(Date debutAbsence) {
		this.debutAbsence = debutAbsence;
	}

	public Date getFinAbsence() {
		return finAbsence;
	}

	public void setFinAbsence(Date finAbsence) {
		this.finAbsence = finAbsence;
	}

	public Integer getIdTypeAbsenceSelected() {
		return idTypeAbsenceSelected;
	}

	public void setIdTypeAbsenceSelected(Integer idTypeAbsenceSelected) {
		this.idTypeAbsenceSelected = idTypeAbsenceSelected;
	}

	public String getNomTypeAbsence() {
		return nomTypeAbsence;
	}

	public void setNomTypeAbsence(String nomTypeAbsence) {
		this.nomTypeAbsence = nomTypeAbsence;
	}

	public int compareTo(AbsenceBean o) {
		// 1.8
		if (o.debutAbsence == null && debutAbsence == null) {
			return 0;
		}
		if (o.debutAbsence == null) {
			return 1;
		}
		if (debutAbsence == null) {
			return -1;
		}
		//
		if (debutAbsence.equals(o.debutAbsence)) {
			if (finAbsence == null && o.finAbsence == null) {
				return -1;
			} else {
				return finAbsence.compareTo(o.finAbsence);
			}
		} else {
			return debutAbsence.compareTo(o.debutAbsence);
		}
	}

	public Integer getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(Integer idSalarie) {
		this.idSalarie = idSalarie;
	}

	public boolean isWarning() {
		return warning;
	}

	public void setWarning(boolean warning) {
		this.warning = warning;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public Double getNombreJourOuvre() {
		return nombreJourOuvre;
	}

	public void setNombreJourOuvre(Double nombreJourOuvre) {
		this.nombreJourOuvre = nombreJourOuvre;
	}

	public boolean isFileError() {
		if (getJustif() != null) {
			if (getJustif().exists() && getJustif().isFile()
					&& getJustif().canRead()) {
				return false;
			} else {
				return true;
			}
		} else
			return false;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof AbsenceBean)) {
			return false;
		} else {
			AbsenceBean absence = (AbsenceBean) obj;
			if (null == this.getId() || null == absence.getId()) {
				return false;
			} else {
				return (this.getId().equals(absence.getId()));
			}
		}
	}

}
