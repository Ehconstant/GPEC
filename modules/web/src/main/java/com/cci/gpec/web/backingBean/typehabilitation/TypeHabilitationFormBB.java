package com.cci.gpec.web.backingBean.typehabilitation;

import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.TypeHabilitationBean;
import com.cci.gpec.metier.implementation.HabilitationServiceImpl;
import com.cci.gpec.metier.implementation.TypeHabilitationServiceImpl;
import com.cci.gpec.web.backingBean.BackingBeanForm;

public class TypeHabilitationFormBB extends BackingBeanForm {

	private boolean affiche;
	private boolean modalRenderedConfirm = false;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public TypeHabilitationFormBB() {
		super();
	}

	public TypeHabilitationFormBB(int id, String nom) {
		super(id, nom);
	}

	public String init() {
		super.id = 0;
		super.nom = "";
		return "addTypeHabilitationForm";
	}

	public String saveOrUpdateTypeHabilitation() {

		TypeHabilitationServiceImpl habilitationService = new TypeHabilitationServiceImpl();
		TypeHabilitationBean habilitationBean = new TypeHabilitationBean();
		habilitationBean.setId(super.id);
		habilitationBean.setNom(super.nom);
		habilitationBean.setIdGroupe(Integer.parseInt(session.getAttribute(
				"groupe").toString()));
		habilitationService.saveOrUppdate(habilitationBean);

		return "addTypeHabilitationForm";
	}

	public void supprimerTypeHabilitation() throws Exception {

		TypeHabilitationServiceImpl typeHabilitationService = new TypeHabilitationServiceImpl();
		TypeHabilitationBean typeHabilitationBean = new TypeHabilitationBean();
		typeHabilitationBean.setId(super.id);
		boolean b = typeHabilitationService.supprimer(typeHabilitationBean,
				Integer.parseInt(session.getAttribute("groupe").toString()));
		if (!b) {
			ResourceBundle rb = ResourceBundle.getBundle("errors");
			FacesMessage message = new FacesMessage(
					rb.getString("suppressionNatureHabilitation"));
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"idForm:dataTable:idSupprimer", message);
		}
	}

	public String saveOrUpdateTypeHabilitationFin() {

		TypeHabilitationServiceImpl habilitationService = new TypeHabilitationServiceImpl();
		TypeHabilitationBean habilitationBean = new TypeHabilitationBean();
		habilitationBean.setId(super.id);
		habilitationBean.setNom(super.nom);
		habilitationBean.setIdGroupe(Integer.parseInt(session.getAttribute(
				"groupe").toString()));
		habilitationService.saveOrUppdate(habilitationBean);

		return "saveOrUpdateTypeHabilitation";
	}

	public String annuler() {
		return "saveOrUpdateTypeHabilitation";
	}

	public boolean isAffiche() throws Exception {
		HabilitationServiceImpl serv = new HabilitationServiceImpl();
		List<HabilitationBean> l = serv.getHabilitationsList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		for (HabilitationBean hab : l) {
			if (hab.getIdTypeHabilitationSelected() == this.id)
				return true;
		}
		return false;
	}

	public boolean isModalRenderedConfirm() {
		return modalRenderedConfirm;
	}

	public void setModalRenderedConfirm(boolean modalRenderedConfirm) {
		this.modalRenderedConfirm = modalRenderedConfirm;
	}

	public String validModif() {
		modalRenderedConfirm = !modalRenderedConfirm;

		return saveOrUpdateTypeHabilitationFin();
	}

	public String annulModif() {
		modalRenderedConfirm = !modalRenderedConfirm;

		return "typesHabilitation";
	}

	public void updateTest(ActionEvent event) {
		modalRenderedConfirm = !modalRenderedConfirm;
	}

}
