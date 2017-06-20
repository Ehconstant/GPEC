package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.web.backingBean.service.ServiceFormBB;

public class TableBeanServices {

	private List<ServiceBean> servicesInventory = new ArrayList<ServiceBean>();

	private int id;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public TableBeanServices() throws Exception {
		init();
	}

	public List<ServiceBean> getServicesInventory() throws Exception {
		init();
		return servicesInventory;
	}

	public void setServicesInventory(List<ServiceBean> servicesInventory) {
		this.servicesInventory = servicesInventory;
	}

	public void init() throws Exception {
		ServiceImpl service = new ServiceImpl();
		servicesInventory = service.getServicesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		Collections.sort(servicesInventory);
	}

	public String modifierService() throws Exception {
		ServiceImpl service = new ServiceImpl();
		ServiceBean serviceBean = service.getServiceBeanById(this.id);
		ServiceFormBB serviceFormBB = new ServiceFormBB();
		serviceFormBB.setId(serviceBean.getId());
		serviceFormBB.setNom(serviceBean.getNom());
		serviceFormBB.setIdEntreprise(serviceBean.getIdEntreprise());
		serviceFormBB.setNomEntreprise(serviceBean.getNomEntreprise());

		return "addServiceForm";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
