package com.cci.gpec.web.backingBean.statut;

import com.cci.gpec.commons.StatutBean;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.cci.gpec.web.backingBean.BackingBeanForm;

public class StatutFormBB extends BackingBeanForm {
	
	public StatutFormBB() {
		super();
	}
	
	public StatutFormBB(int id, String nom) {
		super(id, nom);
	}

	public String init() {
	    super.id = 0;
	    super.nom = "";
	    return "addStatutForm";
	}
	
	public String supprimerStatut(){
		
		StatutServiceImpl statutService = 
			new StatutServiceImpl();
		StatutBean statutBean =
			new StatutBean();
		statutBean.setId(super.id);
		statutService.supprimer(statutBean);
		
		return "saveOrUpdateStatut";
	}

	public String saveOrUpdateStatut(){
		
		StatutServiceImpl statutService = 
			new StatutServiceImpl();
		StatutBean statutBean =
			new StatutBean();
		statutBean.setId(super.id);
		statutBean.setNom(super.nom);
		statutService.saveOrUppdate(statutBean);
		
		return "addStatutForm";
	}
	
	public String saveOrUpdateStatutFin(){
		
		StatutServiceImpl statutService = 
			new StatutServiceImpl();
		StatutBean statutBean =
			new StatutBean();
		statutBean.setId(super.id);
		statutBean.setNom(super.nom);
		statutService.saveOrUppdate(statutBean);
		
		return "saveOrUpdateStatut";
	}
	
	public String annuler() {
	    return "saveOrUpdateStatut";
	}
}
