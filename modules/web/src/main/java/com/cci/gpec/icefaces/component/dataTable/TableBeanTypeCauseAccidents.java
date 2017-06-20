package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.List;

import com.cci.gpec.commons.TypeCauseAccidentBean;
import com.cci.gpec.metier.implementation.TypeCauseAccidentServiceImpl;
import com.cci.gpec.web.backingBean.typecauseaccident.TypeCauseAccidentFormBB;

public class TableBeanTypeCauseAccidents {

private List<TypeCauseAccidentBean>typeCauseAccidentInventory = new ArrayList<TypeCauseAccidentBean>();
    
	private int id;
	
    public TableBeanTypeCauseAccidents() throws Exception {
		init();
	}

    
    public List<TypeCauseAccidentBean> getTypeCauseAccidentInventory() throws Exception {
		init();
		return typeCauseAccidentInventory;
	}


	public void setTypeCauseAccidentInventory(List<TypeCauseAccidentBean> typeCauseAccidentInventory) {
		this.typeCauseAccidentInventory = typeCauseAccidentInventory;
	}

	

	public void init() throws Exception{
		TypeCauseAccidentServiceImpl typeCauseAccident = new TypeCauseAccidentServiceImpl();
		typeCauseAccidentInventory = typeCauseAccident.getTypeCauseAccidentList();
    }
	
	public String modifierService() throws Exception{
		TypeCauseAccidentServiceImpl typeCauseAccident = new TypeCauseAccidentServiceImpl();
		TypeCauseAccidentBean typeCauseAccidentBean= typeCauseAccident.getTypeCauseAccidentBeanById(this.id);
		TypeCauseAccidentFormBB typeCauseAccidentFormBB = new TypeCauseAccidentFormBB();
		typeCauseAccidentFormBB.setId(typeCauseAccidentBean.getId());
		typeCauseAccidentFormBB.setNom(typeCauseAccidentBean.getNom());
		
		return "addTypeCauseAccidentForm";
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}
	
}
