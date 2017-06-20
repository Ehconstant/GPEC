package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.List;

import com.cci.gpec.commons.TypeLesionBean;
import com.cci.gpec.metier.implementation.TypeLesionServiceImpl;
import com.cci.gpec.web.backingBean.typelesion.TypeLesionFormBB;

public class TableBeanTypeLesions {

private List<TypeLesionBean>typeLesionInventory = new ArrayList<TypeLesionBean>();
    
	private int id;
	
    public TableBeanTypeLesions() throws Exception {
		init();
	}

    
    public List<TypeLesionBean> getTypeLesionInventory() throws Exception {
		init();
		return typeLesionInventory;
	}


	public void setTypeLesionInventory(List<TypeLesionBean> typeLesionInventory) {
		this.typeLesionInventory = typeLesionInventory;
	}

	

	public void init() throws Exception{
		TypeLesionServiceImpl groupe = new TypeLesionServiceImpl();
		typeLesionInventory = groupe.getTypeLesionList();
    }
	
	public String modifierService() throws Exception{
		TypeLesionServiceImpl typeLesion = new TypeLesionServiceImpl();
		TypeLesionBean typeLesionBean= typeLesion.getTypeLesionBeanById(this.id);
		TypeLesionFormBB typeLesionFormBB = new TypeLesionFormBB();
		typeLesionFormBB.setId(typeLesionBean.getId());
		typeLesionFormBB.setNom(typeLesionBean.getNom());
		
		return "addTypeLesionForm";
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}
	
}
