package com.cci.gpec.commons;

import java.io.File;
import java.util.Date;

public class ContratTravailBean implements Comparable<ContratTravailBean> {

	private int id;
	private Integer idSalarie;

	private Date debutContrat;
	private Date finContrat;

	private Integer idMetierSelected;
	private String nomMetier;

	private Integer idTypeContratSelected;
	private String nomTypeContrat;

	private Integer idMotifRuptureContrat;
	private String nomMotifRuptureContrat;

	private boolean renouvellementCDD;

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

	public Date getDebutContrat() {
		return debutContrat;
	}

	public void setDebutContrat(Date debutContrat) {
		this.debutContrat = debutContrat;
	}

	public Date getFinContrat() {
		return finContrat;
	}

	public void setFinContrat(Date finContrat) {
		this.finContrat = finContrat;
	}

	public Integer getIdMetierSelected() {
		return idMetierSelected;
	}

	public void setIdMetierSelected(Integer idMetierSelected) {
		this.idMetierSelected = idMetierSelected;
	}

	public String getNomMetier() {
		return nomMetier;
	}

	public void setNomMetier(String nomMetier) {
		this.nomMetier = nomMetier;
	}

	public Integer getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(Integer idSalarie) {
		this.idSalarie = idSalarie;
	}

	public Integer getIdTypeContratSelected() {
		return idTypeContratSelected;
	}

	public void setIdTypeContratSelected(Integer idTypeContratSelected) {
		this.idTypeContratSelected = idTypeContratSelected;
	}

	public String getNomTypeContrat() {
		return nomTypeContrat;
	}

	public void setNomTypeContrat(String nomTypeContrat) {
		this.nomTypeContrat = nomTypeContrat;
	}

	public Integer getIdMotifRuptureContrat() {
		return idMotifRuptureContrat;
	}

	public void setIdMotifRuptureContrat(Integer idMotifRuptureContrat) {
		this.idMotifRuptureContrat = idMotifRuptureContrat;
	}

	public String getNomMotifRuptureContrat() {
		return nomMotifRuptureContrat;
	}

	public void setNomMotifRuptureContrat(String nomMotifRuptureContrat) {
		this.nomMotifRuptureContrat = nomMotifRuptureContrat;
	}

	public boolean isRenouvellementCDD() {
		return renouvellementCDD;
	}

	public void setRenouvellementCDD(boolean renouvellementCDD) {
		this.renouvellementCDD = renouvellementCDD;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int compareTo(ContratTravailBean o) {

		if (o.debutContrat == null && debutContrat == null)
			return 0;
		if (o.debutContrat == null)
			return 1;
		if (debutContrat == null)
			return -1;

		if (debutContrat.equals(o.debutContrat))
			return id < o.id ? -1 : 1;
		else
			return debutContrat.compareTo(o.debutContrat);
	}

}
