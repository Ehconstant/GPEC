package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.List;

import com.cci.gpec.commons.TypeContratBean;
import com.cci.gpec.metier.implementation.TypeContratServiceImpl;
import com.cci.gpec.web.backingBean.typecontrat.TypeContratFormBB;

public class TableBeanTypeContrats {

private List<TypeContratBean>typeContratInventory = new ArrayList<TypeContratBean>();
    
	private int id;
	
    public TableBeanTypeContrats() throws Exception {
		init();
	}

    
    public List<TypeContratBean> getTypeContratInventory() throws Exception {
		init();
		return typeContratInventory;
	}


	public void setTypeContratInventory(List<TypeContratBean> typeContratInventory) {
		this.typeContratInventory = typeContratInventory;
	}

	

	public void init() throws Exception{
		TypeContratServiceImpl typeAbsence = new TypeContratServiceImpl();
		typeContratInventory = typeAbsence.getTypeContratList();
    }
	
	public String modifierService() throws Exception{
		TypeContratServiceImpl typeContrat = new TypeContratServiceImpl();
		TypeContratBean typeContratBean= typeContrat.getTypeContratBeanById(this.id);
		TypeContratFormBB typeContratFormBB = new TypeContratFormBB();
		typeContratFormBB.setId(typeContratBean.getId());
		typeContratFormBB.setNom(typeContratBean.getNom());
		
		return "addTypeContratForm";
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}
	
}
