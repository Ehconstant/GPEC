package com.cci.gpec.web.backingBean.typeaccident;

import com.cci.gpec.commons.TypeAccidentBean;
import com.cci.gpec.metier.implementation.TypeAccidentServiceImpl;
import com.cci.gpec.web.backingBean.BackingBeanForm;

public class TypeAccidentFormBB extends BackingBeanForm {

	public TypeAccidentFormBB() {
		super();
	}

	public TypeAccidentFormBB(int id, String nom) {
		super(id, nom);
	}

	public String init() {
		super.id = 0;
		super.nom = "";
		return "addTypeAccidentForm";
	}

	public String saveOrUpdateTypeAccident() {

		TypeAccidentServiceImpl typeAccidentService = new TypeAccidentServiceImpl();
		TypeAccidentBean typeAccidentBean = new TypeAccidentBean();
		typeAccidentBean.setId(super.id);
		typeAccidentBean.setNom(super.nom);
		typeAccidentService.saveOrUppdate(typeAccidentBean);

		return "addTypeAccidentForm";
	}

	public String saveOrUpdateTypeAccidentFin() {

		TypeAccidentServiceImpl typeAccidentService = new TypeAccidentServiceImpl();
		TypeAccidentBean typeAccidentBean = new TypeAccidentBean();
		typeAccidentBean.setId(super.id);
		typeAccidentBean.setNom(super.nom);
		typeAccidentService.saveOrUppdate(typeAccidentBean);

		return "saveOrUpdateTypeAccident";
	}

	public String annuler() {
		return "saveOrUpdateTypeAccident";
	}
}
