package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.FicheMetierBean;
import com.cci.gpec.commons.FicheMetierEntrepriseBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierEntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.ficheMetier.FicheMetierFormBB;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;

public class TableBeanFicheMetier {

	private List<FicheMetierBean> ficheMetiersInventory = new ArrayList<FicheMetierBean>();

	private int id;
	private List<SelectItem> selectItems;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");

	public TableBeanFicheMetier() throws Exception {
		init();
	}

	public void init() throws Exception {
		FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();

		// FicheMetierEntrepriseServiceImpl ficheMetierEntrepriseService = new
		// FicheMetierEntrepriseServiceImpl();
		//
		// List<FicheMetierEntrepriseBean> ficheMetierEntrepriseBeanList =
		// ficheMetierEntrepriseService
		// .getFicheMetierEntrepriseList(Integer.parseInt(session
		// .getAttribute("groupe").toString()));

		ficheMetiersInventory.clear();
		// List<Integer> idList = new ArrayList<Integer>();
		//
		// for (FicheMetierEntrepriseBean fme : ficheMetierEntrepriseBeanList) {
		// FicheMetierBean fm = ficheMetierService.getFicheMetierBeanById(fme
		// .getIdFicheMetier());
		// if (!idList.contains(fm.getId())) {
		// ficheMetiersInventory.add(fm);
		// idList.add(fm.getId());
		// }
		// }

		ficheMetiersInventory = ficheMetierService.getFichesMetierList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		Collections.sort(ficheMetiersInventory);

		for (FicheMetierBean fm : ficheMetiersInventory) {
			FicheMetierEntrepriseServiceImpl ficheMetierEntreprise = new FicheMetierEntrepriseServiceImpl();
			List<FicheMetierEntrepriseBean> ficheMetierEntreprisesInventory = ficheMetierEntreprise
					.getFicheMetierEntrepriseBeanListByIdFicheMetier(fm
							.getIdFicheMetier());
			List<EntrepriseBean> entreprisesInventory = new ArrayList<EntrepriseBean>();
			for (FicheMetierEntrepriseBean ficheMetierEntrepriseBean : ficheMetierEntreprisesInventory) {
				EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
				EntrepriseBean entrepriseBean = entrepriseService
						.getEntrepriseBeanById(ficheMetierEntrepriseBean
								.getIdEntreprise());
				entreprisesInventory.add(entrepriseBean);
			}
			fm.setEntreprises(entreprisesInventory);
		}

	}

	public List<FicheMetierBean> getFicheMetiersInventory() throws Exception {
		init();
		FicheMetierServiceImpl s = new FicheMetierServiceImpl();
		for (FicheMetierBean f : ficheMetiersInventory) {
			if (s.getJustificatif(f.getId()) != null
					&& !s.getJustificatif(f.getId()).contains(
							Utils.getSessionFileUploadPath(session, f.getId(),
									"FicheMetier", 0, true, false,
									salarieFormBB.getNomGroupe())))
				f.setJustificatif(Utils.getSessionFileUploadPath(session,
						f.getId(), "FicheMetier", 0, true, false,
						salarieFormBB.getNomGroupe())
						+ f.getJustificatif());
		}
		return ficheMetiersInventory;
	}

	public void setFicheMetiersInventory(
			List<FicheMetierBean> ficheMetiersInventory) {
		this.ficheMetiersInventory = ficheMetiersInventory;
	}

	public List<SelectItem> getSelectItems() {
		selectItems = new ArrayList<SelectItem>();
		for (FicheMetierBean mb : ficheMetiersInventory) {
			selectItems.add(new SelectItem(mb.getId(), mb.getNom()));
		}
		return selectItems;
	}

	public String modifierService() throws Exception {
		FicheMetierServiceImpl ficheMetier = new FicheMetierServiceImpl();
		FicheMetierBean ficheMetierBean = ficheMetier
				.getFicheMetierBeanById(this.id);
		FicheMetierFormBB entrepriseFormBB = new FicheMetierFormBB();
		entrepriseFormBB.setId(ficheMetierBean.getId());
		entrepriseFormBB.setIntitule(ficheMetierBean.getIntituleFicheMetier());

		return "addFicheMetierForm";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
