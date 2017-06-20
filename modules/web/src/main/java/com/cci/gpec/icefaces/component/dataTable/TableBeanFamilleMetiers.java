package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.model.SelectItem;

import com.cci.gpec.commons.FamilleMetierBean;
import com.cci.gpec.metier.implementation.FamilleMetierServiceImpl;

public class TableBeanFamilleMetiers {

	private List<FamilleMetierBean> familleMetiersInventory = new ArrayList<FamilleMetierBean>();

	private int id;
	private List<SelectItem> selectItems;

	public List<FamilleMetierBean> getFamilleMetiersInventory()
			throws Exception {
		init();
		return familleMetiersInventory;
	}

	public void setFamilleMetiersInventory(
			List<FamilleMetierBean> metiersInventory) {
		this.familleMetiersInventory = metiersInventory;
	}

	public List<SelectItem> getSelectItems() throws Exception {
		selectItems = new ArrayList<SelectItem>();
		init();
		Collections.sort(familleMetiersInventory);
		for (FamilleMetierBean fmb : familleMetiersInventory) {
			selectItems.add(new SelectItem(fmb.getId(), fmb.getNom()));
		}
		return selectItems;
	}

	public void init() throws Exception {
		FamilleMetierServiceImpl metier_serv = new FamilleMetierServiceImpl();
		familleMetiersInventory = metier_serv.getFamilleMetiersList();

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
