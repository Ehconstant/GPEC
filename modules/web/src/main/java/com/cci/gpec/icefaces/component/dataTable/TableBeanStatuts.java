package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.List;

import com.cci.gpec.commons.StatutBean;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.cci.gpec.web.backingBean.statut.StatutFormBB;

public class TableBeanStatuts {

private List<StatutBean>statutInventory = new ArrayList<StatutBean>();
    
	private int id;
	
    public TableBeanStatuts() throws Exception {
		init();
	}

    
    public List<StatutBean> getStatutsInventory() throws Exception {
		init();
		return statutInventory;
	}


	public void setStatutInventory(List<StatutBean> statutInventory) {
		this.statutInventory = statutInventory;
	}

	

	public void init() throws Exception{
		StatutServiceImpl statut = new StatutServiceImpl();
		statutInventory = statut.getStatutsList();
    }
	
	public String modifierService() throws Exception{
		StatutServiceImpl statut = new StatutServiceImpl();
		StatutBean statutBean= statut.getStatutBeanById(this.id);
		StatutFormBB serviceFormBB = new StatutFormBB();
		serviceFormBB.setId(statutBean.getId());
		serviceFormBB.setNom(statutBean.getNom());
		
		return "addStatutForm";
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}
	
}
