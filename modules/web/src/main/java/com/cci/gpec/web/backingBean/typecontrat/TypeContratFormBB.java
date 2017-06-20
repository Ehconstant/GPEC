package com.cci.gpec.web.backingBean.typecontrat;

import com.cci.gpec.commons.TypeContratBean;
import com.cci.gpec.metier.implementation.TypeContratServiceImpl;
import com.cci.gpec.web.backingBean.BackingBeanForm;

public class TypeContratFormBB extends BackingBeanForm {
	
	public TypeContratFormBB() {
		super();
	}
	
	public TypeContratFormBB(int id, String nom) {
		super(id, nom);
	}

	public String init() {
	    super.id = 0;
	    super.nom = "";
	    return "addTypeContratForm";
	}
	
	public String supprimerTypeContrat(){
		
		TypeContratServiceImpl typeContratService = 
			new TypeContratServiceImpl();
		TypeContratBean typeContratBean =
			new TypeContratBean();
		typeContratBean.setId(super.id);
		typeContratService.supprimer(typeContratBean);
		
		return "saveOrUpdateTypeContrat";
	}

	public String saveOrUpdateTypeContrat(){
		
		TypeContratServiceImpl typeContratService = 
			new TypeContratServiceImpl();
		TypeContratBean typeContratBean =
			new TypeContratBean();
		typeContratBean.setId(super.id);
		typeContratBean.setNom(super.nom);
		typeContratService.saveOrUppdate(typeContratBean);
		
		return "addTypeContratForm";
	}
	
	public String saveOrUpdateTypeContratFin(){
		
		TypeContratServiceImpl typeContratService = 
			new TypeContratServiceImpl();
		TypeContratBean typeContratBean =
			new TypeContratBean();
		typeContratBean.setId(super.id);
		typeContratBean.setNom(super.nom);
		typeContratService.saveOrUppdate(typeContratBean);
		
		return "saveOrUpdateTypeContrat";
	}
	
	public String annuler() {
	    return "saveOrUpdateTypeContrat";
	}
}
