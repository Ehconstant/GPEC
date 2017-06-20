package com.cci.gpec.web.backingBean.domaineformation;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.DomaineFormationBean;
import com.cci.gpec.metier.implementation.DomaineFormationServiceImpl;
import com.cci.gpec.web.backingBean.BackingBeanForm;

public class DomaineFormationFormBB extends BackingBeanForm {

	private int idFamilleDomaineFormation;
	private String nomFamilleDomaineFormation;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public DomaineFormationFormBB() {
		super();
	}

	public DomaineFormationFormBB(int id, String nom) {
		super(id, nom);
	}

	public String init() {
		super.id = 0;
		super.nom = "";
		if (idFamilleDomaineFormation != 0) {
			idFamilleDomaineFormation = 0;
			nomFamilleDomaineFormation = "";
		}
		return "addDomaineFormationForm";
	}

	public void supprimerDomaineFormation() throws Exception {

		DomaineFormationServiceImpl domaineFormationService = new DomaineFormationServiceImpl();
		DomaineFormationBean DomaineFormationBean = new DomaineFormationBean();
		DomaineFormationBean.setId(super.id);
		boolean b = domaineFormationService.supprimer(DomaineFormationBean,
				Integer.parseInt(session.getAttribute("groupe").toString()));
		if (!b) {
			FacesMessage message = new FacesMessage(
					"Erreur, il existe une formation qui utilise ce domaine de formation");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"idForm:dataTable:idSupprimer", message);
		}
	}

	public String saveOrUpdateDomaineFormation() {
		DomaineFormationServiceImpl DomaineFormationService = new DomaineFormationServiceImpl();
		DomaineFormationBean DomaineFormationBean = new DomaineFormationBean();
		DomaineFormationBean.setId(super.id);
		DomaineFormationBean.setNom(super.nom);
		DomaineFormationBean
				.setIdFamilleDomaineFormation(idFamilleDomaineFormation);
		DomaineFormationBean
				.setNomFamilleDomaineFormation(nomFamilleDomaineFormation);
		DomaineFormationService.saveOrUppdate(DomaineFormationBean);

		return "addDomaineFormationForm";
	}

	public String saveOrUpdateDomaineFormationFin() {
		DomaineFormationServiceImpl DomaineFormationService = new DomaineFormationServiceImpl();
		DomaineFormationBean DomaineFormationBean = new DomaineFormationBean();
		DomaineFormationBean.setId(super.id);
		DomaineFormationBean.setNom(super.nom);
		DomaineFormationBean
				.setIdFamilleDomaineFormation(idFamilleDomaineFormation);
		DomaineFormationService.saveOrUppdate(DomaineFormationBean);

		return "saveOrUpdateDomaineFormation";
	}

	public String annuler() {
		return "saveOrUpdateDomaineFormation";
	}

	public int getIdFamilleDomaineFormation() {
		return idFamilleDomaineFormation;
	}

	public void setIdFamilleDomaineFormation(int idFamilleDomaineFormation) {
		this.idFamilleDomaineFormation = idFamilleDomaineFormation;
	}

	public String getNomFamilleDomaineFormation() {
		return nomFamilleDomaineFormation;
	}

	public void setNomFamilleDomaineFormation(String nomFamilleDomaineFormation) {
		this.nomFamilleDomaineFormation = nomFamilleDomaineFormation;
	}

}
