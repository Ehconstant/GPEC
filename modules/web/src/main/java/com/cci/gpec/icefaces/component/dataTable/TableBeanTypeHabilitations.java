package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.TypeHabilitationBean;
import com.cci.gpec.metier.implementation.TypeHabilitationServiceImpl;
import com.cci.gpec.web.backingBean.typehabilitation.TypeHabilitationFormBB;

public class TableBeanTypeHabilitations {

	private List<TypeHabilitationBean> typeHabilitationInventory = new ArrayList<TypeHabilitationBean>();

	private int id;
	private boolean afficheActions;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public TableBeanTypeHabilitations() throws Exception {
		init();
	}

	public List<TypeHabilitationBean> getTypeHabilitationInventory()
			throws Exception {
		init();
		return typeHabilitationInventory;
	}

	public void setTypeHabilitationInventory(
			List<TypeHabilitationBean> typeHabilitationInventory) {
		this.typeHabilitationInventory = typeHabilitationInventory;
	}

	public void init() throws Exception {
		TypeHabilitationServiceImpl groupe = new TypeHabilitationServiceImpl();
		typeHabilitationInventory = groupe.getTypeHabilitationList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		afficheActions = false;

		for (TypeHabilitationBean h : typeHabilitationInventory) {
			if (h.getId() > 12)
				afficheActions = true;
		}
		Collections.sort(typeHabilitationInventory);

		for (TypeHabilitationBean typeHabilitationBean : typeHabilitationInventory) {
			if (typeHabilitationBean.getNom().equals("Autre")) {
				typeHabilitationInventory.remove(typeHabilitationBean);
				typeHabilitationInventory.add(typeHabilitationBean);
				break;
			}
		}
	}

	public String modifierService() throws Exception {
		TypeHabilitationServiceImpl typeHabilitation = new TypeHabilitationServiceImpl();
		TypeHabilitationBean typeHabilitationBean = typeHabilitation
				.getTypeHabilitationBeanById(this.id);
		TypeHabilitationFormBB typeHabilitationFormBB = new TypeHabilitationFormBB();
		typeHabilitationFormBB.setId(typeHabilitationBean.getId());
		typeHabilitationFormBB.setNom(typeHabilitationBean.getNom());

		return "addTypeHabilitationForm";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isAfficheActions() {
		return afficheActions;
	}

}
