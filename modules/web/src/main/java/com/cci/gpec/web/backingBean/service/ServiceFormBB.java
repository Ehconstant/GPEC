package com.cci.gpec.web.backingBean.service;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.web.backingBean.BackingBeanForm;
import com.icesoft.faces.component.ext.HtmlDataTable;

public class ServiceFormBB extends BackingBeanForm {

	private int idEntreprise;
	private String nomEntreprise;

	public ServiceFormBB() {
		super();
	}

	public ServiceFormBB(int id, String nom) {
		super(id, nom);
	}

	public String init() {
		super.id = 0;
		super.nom = "";
		if (idEntreprise != 0) {
			idEntreprise = 0;
			nomEntreprise = "";
		}
		return "addServiceForm";
	}

	public String modificationService() {

		return "addServiceForm";
	}

	public void supprimerService(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ServiceBean serviceBean = (ServiceBean) table.getRowData();

		ServiceImpl service = new ServiceImpl();
		boolean b = service.supprimer(serviceBean);
		if (!b) {
			ResourceBundle rb = ResourceBundle.getBundle("errors");
			FacesMessage message = new FacesMessage(
					rb.getString("suppressionService"));
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"idForm:dataTable:idSupprimer", message);
		}
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

	public String saveOrUpdateService() {

		ServiceImpl serviceService = new ServiceImpl();
		ServiceBean serviceBean = new ServiceBean();
		serviceBean.setId(this.id);
		serviceBean.setNom(this.nom);
		serviceBean.setIdEntreprise(idEntreprise);
		serviceService.saveOrUppdate(serviceBean);

		return "saveOrUpdateService";
	}

	public String annuler() {
		return "saveOrUpdateService";
	}

	public int getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(int idEntreprise) {
		this.idEntreprise = idEntreprise;
	}

	public String getNomEntreprise() throws Exception {
		return nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}
}
