package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.cci.gpec.commons.DomaineFormationBean;
import com.cci.gpec.metier.implementation.DomaineFormationServiceImpl;
import com.cci.gpec.web.backingBean.domaineformation.DomaineFormationFormBB;

public class TableBeanDomaineFormations {

	private List<DomaineFormationBean>domaineFormationsInventory = new ArrayList<DomaineFormationBean>();

	private int id;
	private List<SelectItem> selectItems;
	
    
    public List<DomaineFormationBean> getDomaineFormationsInventory() throws Exception {
		init();
		return domaineFormationsInventory;
	}


	public void setDomaineFormationsInventory(List<DomaineFormationBean> DomaineFormationsInventory) {
		this.domaineFormationsInventory = DomaineFormationsInventory;
	}

	public List<SelectItem> getSelectItems() {
        selectItems = new ArrayList<SelectItem>();
        for (DomaineFormationBean mb : domaineFormationsInventory) {
        	selectItems.add(new SelectItem(mb.getId(), mb.getNom()));
        }
		return selectItems;
	}
	
	public void init() throws Exception{
		DomaineFormationServiceImpl DomaineFormation = new DomaineFormationServiceImpl();
		domaineFormationsInventory = DomaineFormation.getDomaineFormationsList();
//		FamilleDomaineFormationServiceImpl familleService = new FamilleDomaineFormationServiceImpl();
		for (int i=0; i<domaineFormationsInventory.size();i++) {
			DomaineFormationBean bean = domaineFormationsInventory.get(i);
//			int idFamille = bean.getIdFamilleDomaineFormation();
//			FamilleDomaineFormationBean famBean = familleService.getFamilleDomaineFormationBeanById(new Integer(idFamille));
//			bean.setNomFamilleDomaineFormation(famBean.getNom());
			domaineFormationsInventory.set(i, bean);
		}
    }
	
	
	public String modifierService() throws Exception{
		DomaineFormationServiceImpl DomaineFormation = new DomaineFormationServiceImpl();
		DomaineFormationBean DomaineFormationBean= DomaineFormation.getDomaineFormationBeanById(this.id);
		DomaineFormationFormBB entrepriseFormBB = new DomaineFormationFormBB();
		entrepriseFormBB.setId(DomaineFormationBean.getId());
		entrepriseFormBB.setNom(DomaineFormationBean.getNom());
		
		return "addDomaineFormationForm";
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}
	
}
