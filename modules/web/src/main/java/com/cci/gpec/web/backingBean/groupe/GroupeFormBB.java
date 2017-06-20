package com.cci.gpec.web.backingBean.groupe;

import java.io.File;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.BackingBeanForm;

public class GroupeFormBB extends BackingBeanForm {

	String oldName = "";

	public GroupeFormBB() {
		super();
	}

	public GroupeFormBB(int id, String nom) {
		super(id, nom);
	}

	public String init() {
		super.id = 0;
		super.nom = "";
		return "addGroupeForm";
	}

	public String supprimerGroupe() throws Exception {

		/*
		 * GroupeServiceImpl groupeService = new GroupeServiceImpl(); GroupeBean
		 * groupeBean = new GroupeBean(); groupeBean.setId(super.id);
		 * //groupeService.supprimer(groupeBean);
		 */

		return "saveOrUpdateGroupe";
	}

	public String saveOrUpdateGroupe() {

		this.oldName = this.nom;

		GroupeServiceImpl groupeService = new GroupeServiceImpl();
		GroupeBean groupeBean = new GroupeBean();
		groupeBean.setId(this.id);
		groupeBean.setNom(this.nom);
		groupeBean.setDeleted(false);
		groupeService.saveOrUppdate(groupeBean);

		return "addGroupeForm";
	}

	public String saveOrUpdateGroupeFin() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		String uploadFolderPath = Utils.getSessionFileUploadPath(session)
				+ Utils.FILE_SEPARATOR + oldName.replace(" ", "_")
				+ Utils.FILE_SEPARATOR;
		oldName = "";

		File uploadFolder = new File(uploadFolderPath);

		uploadFolder.renameTo(new File(Utils.getSessionFileUploadPath(session)
				+ Utils.FILE_SEPARATOR + this.nom.replace(" ", "_")
				+ Utils.FILE_SEPARATOR));

		GroupeServiceImpl groupeService = new GroupeServiceImpl();
		GroupeBean groupeBean = new GroupeBean();
		groupeBean.setId(this.id);
		groupeBean.setNom(this.nom);
		groupeBean.setDeleted(false);
		groupeService.saveOrUppdate(groupeBean);

		return "saveOrUpdateGroupe";
	}

	public String annuler() {
		return "saveOrUpdateGroupe";
	}
}
