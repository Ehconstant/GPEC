package com.cci.gpec.commons;

import java.io.File;
import java.util.Date;

public class ParcoursBean implements Comparable<ParcoursBean> {

	// primary key
	private int id;

	// fields
	private Date debutFonction;
	private Date finFonction;

	// private Date debutContrat;
	// private Date finContrat;

	// private String natureContrat;

	private Integer equivalenceTempsPlein;
	private Integer idMetierSelected;
	private String nomMetier;
	private Integer idSalarie;

	private Integer idTypeStatutSelected;
	private String nomTypeStatut;

	private Integer idTypeContratSelected;
	private String nomTypeContrat;

	private Integer idTypeRecoursSelected;
	private String nomTypeRecours;

	// private Integer idMotifRuptureContrat;
	// private String nomMotifRuptureContrat;

	private String echelon;
	private String niveau;
	private String coefficient;

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

	public Date getDebutFonction() {
		return debutFonction;
	}

	public void setDebutFonction(Date debutFonction) {
		this.debutFonction = debutFonction;
	}

	public Date getFinFonction() {
		return finFonction;
	}

	public void setFinFonction(Date finFonction) {
		this.finFonction = finFonction;
	}

	public Integer getEquivalenceTempsPlein() {
		return equivalenceTempsPlein;
	}

	public void setEquivalenceTempsPlein(Integer equivalenceTempsPlein) {
		this.equivalenceTempsPlein = equivalenceTempsPlein;
	}

	public Integer getIdMetierSelected() {
		return idMetierSelected;
	}

	public void setIdMetierSelected(Integer idMetierSelected) {
		this.idMetierSelected = idMetierSelected;
	}

	public Integer getIdTypeStatutSelected() {
		return idTypeStatutSelected;
	}

	public void setIdTypeStatutSelected(Integer idTypeStatutSelected) {
		this.idTypeStatutSelected = idTypeStatutSelected;
	}

	public Integer getIdTypeContratSelected() {
		return idTypeContratSelected;
	}

	public void setIdTypeContratSelected(Integer idTypeContratSelected) {
		this.idTypeContratSelected = idTypeContratSelected;
	}

	public String getNomMetier() {
		return nomMetier;
	}

	public String getNomTypeStatut() {
		return nomTypeStatut;
	}

	public String getNomTypeContrat() {
		return nomTypeContrat;
	}

	public void setNomMetier(String nomMetier) {
		this.nomMetier = nomMetier;
	}

	public void setNomTypeStatut(String nomTypeStatut) {
		this.nomTypeStatut = nomTypeStatut;
	}

	public void setNomTypeContrat(String nomTypeContrat) {
		this.nomTypeContrat = nomTypeContrat;
	}

	public int compareTo(ParcoursBean o) {

		// 1.8
		if (o.debutFonction == null && debutFonction == null)
			return 0;
		if (o.debutFonction == null)
			return 1;
		if (debutFonction == null)
			return -1;
		//
		if (debutFonction.equals(o.debutFonction))
			return id < o.id ? -1 : 1;
		else
			return debutFonction.compareTo(o.debutFonction);
	}

	public Integer getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(Integer idSalarie) {
		this.idSalarie = idSalarie;
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

	public String getEchelon() {
		return echelon;
	}

	public void setEchelon(String echelon) {
		this.echelon = echelon;
	}

	public String getNiveau() {
		return niveau;
	}

	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}

	public String getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(String coefficient) {
		this.coefficient = coefficient;
	}

	public Integer getIdTypeRecoursSelected() {
		return idTypeRecoursSelected;
	}

	public void setIdTypeRecoursSelected(Integer idTypeRecoursSelected) {
		this.idTypeRecoursSelected = idTypeRecoursSelected;
	}

	public String getNomTypeRecours() {
		return nomTypeRecours;
	}

	public void setNomTypeRecours(String nomTypeRecours) {
		this.nomTypeRecours = nomTypeRecours;
	}

	/*
	 * public Date getDebutContrat() { return debutContrat; }
	 * 
	 * public void setDebutContrat(Date debutContrat) { this.debutContrat =
	 * debutContrat; }
	 * 
	 * public Date getFinContrat() { return finContrat; }
	 * 
	 * public void setFinContrat(Date finContrat) { this.finContrat =
	 * finContrat; }
	 * 
	 * public String getNatureContrat() { return natureContrat; }
	 * 
	 * public void setNatureContrat(String natureContrat) { this.natureContrat =
	 * natureContrat; }
	 * 
	 * public Integer getIdMotifRuptureContrat() { return idMotifRuptureContrat;
	 * }
	 * 
	 * public void setIdMotifRuptureContrat(Integer idMotifRuptureContrat) {
	 * this.idMotifRuptureContrat = idMotifRuptureContrat; }
	 * 
	 * public String getNomMotifRuptureContrat() { return
	 * nomMotifRuptureContrat; }
	 * 
	 * public void setNomMotifRuptureContrat(String nomMotifRuptureContrat) {
	 * this.nomMotifRuptureContrat = nomMotifRuptureContrat; }
	 */

}
