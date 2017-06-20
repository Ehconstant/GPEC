package com.cci.gpec.web.backingBean.typecauseaccident;

import com.cci.gpec.commons.TypeCauseAccidentBean;
import com.cci.gpec.metier.implementation.TypeCauseAccidentServiceImpl;
import com.cci.gpec.web.backingBean.BackingBeanForm;

public class TypeCauseAccidentFormBB extends BackingBeanForm {

	public TypeCauseAccidentFormBB() {
		super();
	}

	public TypeCauseAccidentFormBB(int id, String nom) {
		super(id, nom);
	}

	public String init() {
		super.id = 0;
		super.nom = "";
		return "addTypeCauseAccidentForm";
	}

	public String saveOrUpdateTypeCauseAccident() {

		TypeCauseAccidentServiceImpl typeCauseAccidentService = new TypeCauseAccidentServiceImpl();
		TypeCauseAccidentBean typeCauseAccidentBean = new TypeCauseAccidentBean();
		typeCauseAccidentBean.setId(super.id);
		typeCauseAccidentBean.setNom(super.nom);
		typeCauseAccidentService.saveOrUppdate(typeCauseAccidentBean);

		return "addTypeCauseAccidentForm";
	}

	public String saveOrUpdateTypeCauseAccidentFin() {

		TypeCauseAccidentServiceImpl typeCauseAccidentService = new TypeCauseAccidentServiceImpl();
		TypeCauseAccidentBean typeCauseAccidentBean = new TypeCauseAccidentBean();
		typeCauseAccidentBean.setId(super.id);
		typeCauseAccidentBean.setNom(super.nom);
		typeCauseAccidentService.saveOrUppdate(typeCauseAccidentBean);

		return "saveOrUpdateTypeCauseAccident";
	}

	public String annuler() {
		return "saveOrUpdateTypeCauseAccident";
	}
}
