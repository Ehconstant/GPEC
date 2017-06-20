package com.cci.gpec.web.backingBean.typelesion;

import com.cci.gpec.commons.TypeLesionBean;
import com.cci.gpec.metier.implementation.TypeLesionServiceImpl;
import com.cci.gpec.web.backingBean.BackingBeanForm;

public class TypeLesionFormBB extends BackingBeanForm {

	public TypeLesionFormBB() {
		super();
	}

	public TypeLesionFormBB(int id, String nom) {
		super(id, nom);
	}

	public String init() {
		super.id = 0;
		super.nom = "";
		return "addTypeLesionForm";
	}

	public String saveOrUpdateTypeLesion() {

		TypeLesionServiceImpl typeLesionService = new TypeLesionServiceImpl();
		TypeLesionBean typeLesionBean = new TypeLesionBean();
		typeLesionBean.setId(super.id);
		typeLesionBean.setNom(super.nom);
		typeLesionService.saveOrUppdate(typeLesionBean);

		return "addTypeLesionForm";
	}

	public String saveOrUpdateTypeLesionFin() {

		TypeLesionServiceImpl typeLesionService = new TypeLesionServiceImpl();
		TypeLesionBean typeLesionBean = new TypeLesionBean();
		typeLesionBean.setId(super.id);
		typeLesionBean.setNom(super.nom);
		typeLesionService.saveOrUppdate(typeLesionBean);

		return "saveOrUpdateTypeLesion";
	}

	public String annuler() {
		super.id = 0;
		super.nom = "";
		return "saveOrUpdateTypeLesion";
	}
}
