package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.FamilleMetierBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.metier.implementation.FamilleMetierServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.web.backingBean.metier.MetierFormBB;

public class TableBeanMetiers {

	private List<MetierBean> metiersInventory = new ArrayList<MetierBean>();

	private int id;
	private List<SelectItem> selectItems;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public void init() throws Exception {
		MetierServiceImpl metier = new MetierServiceImpl();

		metiersInventory = metier.getMetiersList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		Collections.sort(metiersInventory);

		FamilleMetierServiceImpl familleService = new FamilleMetierServiceImpl();

		for (int i = 0; i < metiersInventory.size(); i++) {
			MetierBean bean = metiersInventory.get(i);
			int idFamille = bean.getIdFamilleMetier();
			FamilleMetierBean famBean = familleService
					.getFamilleMetierBeanById(new Integer(idFamille));
			bean.setNomFamilleMetier(famBean.getNom());
			metiersInventory.set(i, bean);
		}
	}

	public List<MetierBean> getMetiersInventory() throws Exception {
		init();
		return metiersInventory;
	}

	public void setMetiersInventory(List<MetierBean> metiersInventory) {
		this.metiersInventory = metiersInventory;
	}

	public List<SelectItem> getSelectItems() {
		selectItems = new ArrayList<SelectItem>();
		for (MetierBean mb : metiersInventory) {
			selectItems.add(new SelectItem(mb.getId(), mb.getNom()));
		}
		return selectItems;
	}

	public String modifierService() throws Exception {
		MetierServiceImpl metier = new MetierServiceImpl();
		MetierBean metierBean = metier.getMetierBeanById(this.id);
		MetierFormBB entrepriseFormBB = new MetierFormBB();
		entrepriseFormBB.setId(metierBean.getId());
		entrepriseFormBB.setNom(metierBean.getNom());

		return "addMetierForm";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
