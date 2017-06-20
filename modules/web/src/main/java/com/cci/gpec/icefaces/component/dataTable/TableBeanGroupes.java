package com.cci.gpec.icefaces.component.dataTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.groupe.GroupeFormBB;

public class TableBeanGroupes {

	private List<GroupeBean> groupesInventory = new ArrayList<GroupeBean>();

	private int id;
	private String nom;
	private String oldName = "";

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public TableBeanGroupes() throws Exception {
		init();
	}

	public List<GroupeBean> getGroupesInventory() throws Exception {
		// init();
		return groupesInventory;
	}

	public void setGroupesInventory(List<GroupeBean> groupesInventory) {
		this.groupesInventory = groupesInventory;
	}

	public void init() throws Exception {
		GroupeServiceImpl groupe = new GroupeServiceImpl();
		groupesInventory.clear();
		groupesInventory.add(groupe.getGroupeBeanById(Integer.parseInt(session
				.getAttribute("groupe").toString())));
	}

	public String add() {
		this.id = 0;
		this.nom = "";
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

	public String saveOrUpdateGroupe() throws NumberFormatException, Exception {

		this.oldName = this.nom;

		GroupeServiceImpl groupeService = new GroupeServiceImpl();
		GroupeBean groupeBean = new GroupeBean();
		groupeBean.setId(this.id);
		groupeBean.setNom(this.nom);
		groupeBean.setDeleted(false);
		groupeService.saveOrUppdate(groupeBean);

		GroupeServiceImpl groupe = new GroupeServiceImpl();
		groupesInventory.clear();
		groupesInventory.add(groupe.getGroupeBeanById(Integer.parseInt(session
				.getAttribute("groupe").toString())));

		return "addGroupeForm";
	}

	public String saveOrUpdateGroupeFin() throws NumberFormatException,
			Exception {

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

		GroupeServiceImpl groupe = new GroupeServiceImpl();
		groupesInventory.clear();
		groupesInventory.add(groupe.getGroupeBeanById(Integer.parseInt(session
				.getAttribute("groupe").toString())));

		return "saveOrUpdateGroupe";
	}

	public String annuler() {
		return "saveOrUpdateGroupe";
	}

	public String modifierService() throws Exception {
		GroupeServiceImpl groupe = new GroupeServiceImpl();
		GroupeBean groupeBean = groupe.getGroupeBeanById(this.id);
		GroupeFormBB groupeFormBB = new GroupeFormBB();
		groupeFormBB.setId(groupeBean.getId());
		groupeFormBB.setNom(groupeBean.getNom());

		return "addGroupeForm";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

}
