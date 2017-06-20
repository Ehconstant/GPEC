package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.entreprise.EntrepriseFormBB;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;

public class TableBeanEntreprises {

	private List<EntrepriseBean> entreprisesInventory = new ArrayList<EntrepriseBean>();
	private int id;
	private List<SelectItem> selectItems;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");

	/*
	 * public TableBeanEntreprises() throws Exception { init(); }
	 */

	public List<EntrepriseBean> getEntreprisesInventory() throws Exception {
		init();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		for (EntrepriseBean e : entreprisesInventory) {
			if (e.getJustificatif() != null
					&& !e.getJustificatif().contains(
							Utils.getSessionFileUploadPath(session, e.getId(),
									"logo_entreprise", 0, false, true,
									salarieFormBB.getNomGroupe())))
				e.setJustificatif(Utils.getSessionFileUploadPath(session,
						e.getId(), "logo_entreprise", 0, false, true,
						salarieFormBB.getNomGroupe()) + e.getJustificatif());
		}
		Collections.sort(entreprisesInventory);
		Collections.reverse(entreprisesInventory);
		return entreprisesInventory;
	}

	public void setEntreprisesInventory(
			List<EntrepriseBean> enterprisesInventory) {
		this.entreprisesInventory = enterprisesInventory;
	}

	public void init() throws Exception {
		EntrepriseServiceImpl entreprise = new EntrepriseServiceImpl();
		entreprisesInventory = entreprise.getEntreprisesList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
	}

	public String modifierService() throws Exception {
		EntrepriseServiceImpl entreprise = new EntrepriseServiceImpl();
		EntrepriseBean entrepriseBean = entreprise
				.getEntrepriseBeanById(this.id);
		EntrepriseFormBB entrepriseFormBB = new EntrepriseFormBB();
		entrepriseFormBB.setId(entrepriseBean.getId());
		entrepriseFormBB.setNom(entrepriseBean.getNom());
		entrepriseFormBB
				.setCciRattachement(entrepriseBean.getCciRattachement());
		entrepriseFormBB.setDateCreation(entrepriseBean.getDateCreation());

		return "addEntrepriseForm";
	}

	public List<SelectItem> getSelectItems() throws Exception {
		init();
		selectItems = new ArrayList<SelectItem>();
		for (EntrepriseBean eb : entreprisesInventory) {
			selectItems.add(new SelectItem(eb.getId(), eb.getNom()));
		}
		return selectItems;
	}

	public void setSelectItems(List<SelectItem> selectItems) {
		this.selectItems = selectItems;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
