package com.cci.gpec.web.backingBean.typeabsence;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.TypeAbsenceBean;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.TypeAbsenceServiceImpl;
import com.cci.gpec.web.backingBean.BackingBeanForm;
import com.icesoft.faces.component.ext.HtmlDataTable;

public class TypeAbsenceFormBB extends BackingBeanForm {

	private boolean modalRenderedConfirm = false;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public TypeAbsenceFormBB() {
		super();
	}

	public TypeAbsenceFormBB(int id, String nom) {
		super(id, nom);
	}

	public String init() {
		super.id = 0;
		super.nom = "";
		return "addTypeAbsenceForm";
	}

	public String saveOrUpdateTypeAbsence() {

		TypeAbsenceServiceImpl entrepriseService = new TypeAbsenceServiceImpl();
		TypeAbsenceBean typeAbsenceBean = new TypeAbsenceBean();
		typeAbsenceBean.setId(super.id);
		typeAbsenceBean.setNom(super.nom);
		typeAbsenceBean.setIdGroupe(Integer.parseInt(session.getAttribute(
				"groupe").toString()));
		entrepriseService.saveOrUppdate(typeAbsenceBean);

		return "addTypeAbsenceForm";
	}

	public String saveOrUpdateTypeAbsenceFin() {

		TypeAbsenceServiceImpl typeAbsenceService = new TypeAbsenceServiceImpl();
		TypeAbsenceBean typeAbsenceBean = new TypeAbsenceBean();
		typeAbsenceBean.setId(super.id);
		typeAbsenceBean.setNom(super.nom);
		typeAbsenceBean.setIdGroupe(Integer.parseInt(session.getAttribute(
				"groupe").toString()));
		typeAbsenceService.saveOrUppdate(typeAbsenceBean);

		return "saveOrUpdateTypeAbsence";
	}

	public String annuler() {
		return "saveOrUpdateTypeAbsence";
	}

	public boolean isAffiche() throws Exception {
		AbsenceServiceImpl serv = new AbsenceServiceImpl();
		List<AbsenceBean> l = serv.getAbsencesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		for (AbsenceBean ab : l) {
			if (ab.getIdTypeAbsenceSelected() == this.id)
				return true;
		}
		return false;
	}

	public void supprimerTypeAbsence(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		TypeAbsenceBean typeAbsenceBean = (TypeAbsenceBean) table.getRowData();

		TypeAbsenceServiceImpl typeAbsenceService = new TypeAbsenceServiceImpl();
		typeAbsenceService.suppression(typeAbsenceBean);

	}

	private HtmlDataTable getParentDatatable(UIComponent compo) {
		if (compo == null) {
			return null;
		}
		if (compo instanceof HtmlDataTable) {
			return (HtmlDataTable) compo;
		}
		return getParentDatatable(compo.getParent());
	}

	public boolean isModalRenderedConfirm() {
		return modalRenderedConfirm;
	}

	public void setModalRenderedConfirm(boolean modalRenderedConfirm) {
		this.modalRenderedConfirm = modalRenderedConfirm;
	}

	public String validModif() {
		modalRenderedConfirm = !modalRenderedConfirm;

		return saveOrUpdateTypeAbsenceFin();
	}

	public String annulModif() {
		modalRenderedConfirm = !modalRenderedConfirm;

		return "typesAbsences";
	}

	public void updateTest(ActionEvent event) {
		modalRenderedConfirm = !modalRenderedConfirm;
	}
}
