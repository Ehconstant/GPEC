package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.cci.gpec.ModelBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.metier.implementation.ServiceGeneriqueServiceImpl;

public class TableBeanServiceGeneriques {

	private List<ServiceBean>serviceGeneriquesInventory = new ArrayList<ServiceBean>();

	private int id;
	private List<SelectItem> selectItems;
    
    public List<ServiceBean> getServiceGeneriquesInventory() throws Exception {
    	
		init();
		return serviceGeneriquesInventory;
	}


	public void setServiceGeneriquesInventory(List<ServiceBean> serviceGeneriquesInventory) {
		this.serviceGeneriquesInventory = serviceGeneriquesInventory;
	}

	public List<SelectItem> getSelectItems() throws Exception {
        selectItems = new ArrayList<SelectItem>();
        init();
        for (ServiceBean mb : serviceGeneriquesInventory) {
        	selectItems.add(new SelectItem(mb.getId(), mb.getNom()));
        }
		return selectItems;
	}
	
	public void init() throws Exception{
		serviceGeneriquesInventory = new ArrayList<ServiceBean>();
		ServiceGeneriqueServiceImpl serviceGenerique = new ServiceGeneriqueServiceImpl();
		List<ModelBean> l = serviceGenerique.getServiceGeneriquesList();
		for (int i=0; i<l.size(); i++) {
			ServiceBean bean = new ServiceBean();
			bean.setId(l.get(i).getId());
			bean.setNom(l.get(i).getNom());
			serviceGeneriquesInventory.add(bean);
		}
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
