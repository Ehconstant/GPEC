package com.cci.gpec.commons;

import java.util.ArrayList;
import java.util.List;

import com.cci.gpec.ModelBean;

public class FicheMetierBeanExport extends ModelBean implements
		Comparable<FicheMetierBeanExport> {

	private List<FicheMetierBean> fichesMetierList = new ArrayList<FicheMetierBean>();
	private String intituleFicheMetier;

	public List<FicheMetierBean> getFichesMetierList() {
		return fichesMetierList;
	}

	public void setFichesMetierList(List<FicheMetierBean> fichesMetierList) {
		this.fichesMetierList = fichesMetierList;
	}

	public int compareTo(FicheMetierBeanExport arg0) {
		return 0;
	}

	public String getIntituleFicheMetier() {
		return intituleFicheMetier;
	}

	public void setIntituleFicheMetier(String intituleFicheMetier) {
		this.intituleFicheMetier = intituleFicheMetier;
	}

}
