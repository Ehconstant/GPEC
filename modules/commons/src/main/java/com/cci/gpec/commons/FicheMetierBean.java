package com.cci.gpec.commons;

import java.io.File;
import java.util.List;

import com.cci.gpec.ModelBean;

public class FicheMetierBean extends ModelBean implements
		Comparable<FicheMetierBean> {

	private int idFicheMetier;
	private String intituleFicheMetier;
	private String finaliteFicheMetier;
	private Integer cspReference;
	private String activiteResponsabilite;
	private String savoir;
	private String savoirFaire;
	private String savoirEtre;
	private String niveauFormationRequis;
	private String particularite;
	private String csp;
	private String libelleEntreprise;
	private Integer idEntreprise;
	private String url;
	private int idGroupe;

	private boolean catalogue;

	private String justificatif;
	private boolean fileError;

	private File justif;

	private List<EntrepriseBean> entreprises;

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

	public String getCsp() {
		return csp;
	}

	public void setCsp(String csp) {
		this.csp = csp;
	}

	public FicheMetierBean() {
		super();
	}

	public FicheMetierBean(int id, String nom) {
		super(id, nom);
		// TODO Auto-generated constructor stub
	}

	public int compareTo(FicheMetierBean o) {
		FicheMetierBean p = o;
		return getIntituleFicheMetier().toUpperCase().compareTo(
				p.getIntituleFicheMetier().toUpperCase());

	}

	public int getIdFicheMetier() {
		return idFicheMetier;
	}

	public void setIdFicheMetier(int idFicheMetier) {
		this.idFicheMetier = idFicheMetier;
	}

	public String getIntituleFicheMetier() {
		return intituleFicheMetier;
	}

	public void setIntituleFicheMetier(String intituleFicheMetier) {
		this.intituleFicheMetier = intituleFicheMetier;
	}

	public String getFinaliteFicheMetier() {
		return finaliteFicheMetier;
	}

	public void setFinaliteFicheMetier(String finaliteFicheMetier) {
		this.finaliteFicheMetier = finaliteFicheMetier;
	}

	public Integer getCspReference() {
		return cspReference;
	}

	public void setCspReference(Integer cspReference) {
		this.cspReference = cspReference;
	}

	public String getActiviteResponsabilite() {
		return activiteResponsabilite;
	}

	public void setActiviteResponsabilite(String activiteResponsabilite) {
		this.activiteResponsabilite = activiteResponsabilite;
	}

	public String getSavoir() {
		return savoir;
	}

	public void setSavoir(String savoir) {
		this.savoir = savoir;
	}

	public String getSavoirFaire() {
		return savoirFaire;
	}

	public void setSavoirFaire(String savoirFaire) {
		this.savoirFaire = savoirFaire;
	}

	public String getSavoirEtre() {
		return savoirEtre;
	}

	public void setSavoirEtre(String savoirEtre) {
		this.savoirEtre = savoirEtre;
	}

	public String getNiveauFormationRequis() {
		return niveauFormationRequis;
	}

	public void setNiveauFormationRequis(String niveauFormationRequis) {
		this.niveauFormationRequis = niveauFormationRequis;
	}

	public String getParticularite() {
		return particularite;
	}

	public void setParticularite(String particularite) {
		this.particularite = particularite;
	}

	public String getLibelleEntreprise() {
		return libelleEntreprise;
	}

	public void setLibelleEntreprise(String libelleEntreprise) {
		this.libelleEntreprise = libelleEntreprise;
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

	public Integer getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(Integer idEntreprise) {
		this.idEntreprise = idEntreprise;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isCatalogue() {
		return catalogue;
	}

	public void setCatalogue(boolean catalogue) {
		this.catalogue = catalogue;
	}

	public int getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(int idGroupe) {
		this.idGroupe = idGroupe;
	}

	public List<EntrepriseBean> getEntreprises() {
		return entreprises;
	}

	public void setEntreprises(List<EntrepriseBean> entreprises) {
		this.entreprises = entreprises;
	}

}
