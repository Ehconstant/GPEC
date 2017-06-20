package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.List;

import com.cci.gpec.commons.TypeAccidentBean;
import com.cci.gpec.metier.implementation.TypeAccidentServiceImpl;
import com.cci.gpec.web.backingBean.typeaccident.TypeAccidentFormBB;

public class TableBeanTypeAccidents {

private List<TypeAccidentBean>typeAccidentInventory = new ArrayList<TypeAccidentBean>();
    
	private int id;
	
    public TableBeanTypeAccidents() throws Exception {
		init();
	}

    
    public List<TypeAccidentBean> getTypeAccidentInventory() throws Exception {
		init();
		return typeAccidentInventory;
	}


	public void setTypeAccidentInventory(List<TypeAccidentBean> typeAccidentInventory) {
		this.typeAccidentInventory = typeAccidentInventory;
	}

	

	public void init() throws Exception{
		TypeAccidentServiceImpl typeAccident = new TypeAccidentServiceImpl();
		typeAccidentInventory = typeAccident.getTypeAccidentList();
    }
	
	public String modifierService() throws Exception{
		TypeAccidentServiceImpl typeAccident = new TypeAccidentServiceImpl();
		TypeAccidentBean typeAccidentBean= typeAccident.getTypeAccidentBeanById(this.id);
		TypeAccidentFormBB typeAccidentFormBB = new TypeAccidentFormBB();
		typeAccidentFormBB.setId(typeAccidentBean.getId());
		typeAccidentFormBB.setNom(typeAccidentBean.getNom());
		
		return "addTypeAccidentForm";
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}
	
}
