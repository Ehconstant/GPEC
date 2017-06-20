package com.cci.gpec.web.backingBean.revenuComplementaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.LienRemunerationRevenuBean;
import com.cci.gpec.commons.RevenuComplementaireBean;
import com.cci.gpec.metier.implementation.LienRemunerationRevenuServiceImpl;
import com.cci.gpec.metier.implementation.RevenuComplementaireServiceImpl;
import com.cci.gpec.web.backingBean.BackingBeanForm;
import com.icesoft.faces.component.ext.HtmlDataTable;

public class RevenuComplementaireFormBB extends BackingBeanForm {

	private List<RevenuComplementaireBean> revenuComplementaireInventory;
	private List<RevenuComplementaireBean> fraisProfInventory;

	private List<SelectItem> typeList;

	private String typeSelected;

	private String libelle;
	private String type;
	private String oldLibelle;
	private String oldType;

	private int maj = 0;
	private int save = 0;
	private int modif = 0;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public RevenuComplementaireFormBB() {
		super();
		init();
	}

	public RevenuComplementaireFormBB(int id, String nom) {
		super(id, nom);
		init();
	}

	public String init() {
		super.id = 0;
		super.nom = "";

		this.libelle = "";
		if (this.typeSelected != null && !this.typeSelected.equals(""))
			this.type = this.typeSelected;
		else {
			this.type = "";
			this.typeSelected = "tous";
		}
		this.oldLibelle = "";
		this.oldType = "";

		RevenuComplementaireServiceImpl revenuComplementaireService = new RevenuComplementaireServiceImpl();

		List<RevenuComplementaireBean> beanList = revenuComplementaireService
				.getRevenuComplementaireList(Integer.parseInt(session
						.getAttribute("groupe").toString()));

		typeList = new ArrayList<SelectItem>();

		// List<String> type = new ArrayList<String>();
		typeList.add(new SelectItem("avantage_assujetti", "Avantage assujetti"));
		typeList.add(new SelectItem("avantage_non_assujetti",
				"Avantage non assujetti"));
		typeList.add(new SelectItem("commission", "Commission"));
		typeList.add(new SelectItem("prime_fixe", "Prime fixe"));
		typeList.add(new SelectItem("prime_variable", "Prime variable"));

		revenuComplementaireInventory = new ArrayList<RevenuComplementaireBean>();
		RevenuComplementaireServiceImpl service = new RevenuComplementaireServiceImpl();
		List<RevenuComplementaireBean> list = new ArrayList<RevenuComplementaireBean>();
		list = service.getRevenuComplementaireByType(this.typeSelected,
				Integer.parseInt(session.getAttribute("groupe").toString()));
		this.revenuComplementaireInventory.clear();
		this.revenuComplementaireInventory = list;

		fraisProfInventory = new ArrayList<RevenuComplementaireBean>();
		fraisProfInventory = revenuComplementaireService
				.getFraisProfList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		return "addRevenuComplementaireForm";
	}

	public void test(ActionEvent event) {
		this.setLibelle((String) event.getComponent().getAttributes()
				.get("libelle"));
		this.setType((String) event.getComponent().getAttributes().get("type"));
	}

	public String initFraisProf() {
		super.id = 0;
		super.nom = "";

		this.libelle = "";

		return "addFraisProfForm";
	}

	public void supprimerRevenuComplementaire(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		RevenuComplementaireBean revenuComplementaireBean = (RevenuComplementaireBean) table
				.getRowData();
		int index = table.getRowIndex();

		LienRemunerationRevenuServiceImpl lien = new LienRemunerationRevenuServiceImpl();
		RevenuComplementaireServiceImpl serv = new RevenuComplementaireServiceImpl();

		id = revenuComplementaireBean.getId();

		List<LienRemunerationRevenuBean> lrr = new ArrayList<LienRemunerationRevenuBean>();
		lrr = lien.getLienRemunerationRevenuList(Integer.parseInt(session
				.getAttribute("groupe").toString()));

		int isUsed = 0;
		for (LienRemunerationRevenuBean lrrb : lrr) {
			if (lrrb.getIdRevenuComplementaire() == id)
				isUsed = 1;
		}

		if (isUsed == 1) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Impossible de supprimer un revenu attribué à un salarié.",
					"Impossible de supprimer un revenu attribué à un salarié.");
			FacesContext.getCurrentInstance().addMessage(
					"idForm:dataTable:idSupprimer", message);
		} else {
			RevenuComplementaireServiceImpl revenuComplementaire = new RevenuComplementaireServiceImpl();

			boolean b = revenuComplementaire
					.supprimer(revenuComplementaireBean);
			if (!b) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Suppression impossible",
						"Suppression impossible");
				FacesContext.getCurrentInstance().addMessage(
						"idForm:dataTable:idSupprimer", message);
			}

			this.fraisProfInventory.clear();
			this.revenuComplementaireInventory.clear();
			this.fraisProfInventory = serv.getFraisProfList(Integer
					.parseInt(session.getAttribute("groupe").toString()));
			this.revenuComplementaireInventory = serv
					.getRevenuComplementaireByType(this.typeSelected,
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));

		}
	}

	private HtmlDataTable getParentDatatable(UIComponent compo) {
		if (compo == null) {
			return null;
		}
		if (compo instanceof HtmlDataTable) {
			return (HtmlDataTable) compo;
		}
		return getParentDatatable(compo.getParent());
	}

	public String saveOrUpdateFraisProf() {

		RevenuComplementaireServiceImpl revenuComplementaire = new RevenuComplementaireServiceImpl();

		List<RevenuComplementaireBean> l = new ArrayList<RevenuComplementaireBean>();
		l = revenuComplementaire.getFraisProfList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		int exist = 0;

		if (modif == 1) {
			int id = revenuComplementaire
					.getIdFromFraisProf(oldLibelle, "frais_professionnel",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));

			for (RevenuComplementaireBean rc : l) {
				if (rc.getLibelle().equals(libelle) && rc.getId() != id)
					exist = 1;
			}
		} else {
			for (RevenuComplementaireBean rc : l) {
				if (rc.getLibelle().equals(libelle))
					exist = 1;
			}
		}

		if (libelle.length() == 0 || libelle == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Le libellé est obligatoire.",
					"Le libellé est obligatoire.");
			FacesContext.getCurrentInstance().addMessage("Formulaire:nom",
					message);

			return "";
		} else {

			if (exist == 1) {

				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Impossible de créer deux frais professionnels de même libellé",
						"Impossible de créer deux frais professionnels de même libellé");
				FacesContext.getCurrentInstance().addMessage(
						"Formulaire:valider", message);

				return "";

			} else {

				RevenuComplementaireServiceImpl serv = new RevenuComplementaireServiceImpl();

				// pas besoin de déterminer l'id puisque le fairs est forcément
				// un nouveau
				// int id = serv.getIdFromRevenuComplementaire(this.libelle,
				// this.type);

				int id = 0;
				if (modif == 1) {
					id = serv.getIdFromFraisProf(oldLibelle,
							"frais_professionnel", Integer.parseInt(session
									.getAttribute("groupe").toString()));
					modif = 0;
				}

				RevenuComplementaireBean revenuComplementaireBean = new RevenuComplementaireBean();
				revenuComplementaireBean.setId(id);
				// revenuComplementaireBean.setNom(this.nom);
				revenuComplementaireBean.setLibelle(libelle);
				revenuComplementaireBean.setType("frais_professionnel");
				revenuComplementaire.saveOrUpdate(revenuComplementaireBean,
						Integer.parseInt(session.getAttribute("groupe")
								.toString()));

				this.fraisProfInventory.clear();
				this.fraisProfInventory = serv.getFraisProfList(Integer
						.parseInt(session.getAttribute("groupe").toString()));

				return "saveOrUpdateRevenuComplementaireFraisProf";
			}
		}
	}

	public String annulerRevenuComplementaire() {
		modif = 0;
		return "saveOrUpdateRevenuComplementaire";
	}

	public String annulerFraisProf() {
		modif = 0;
		return "saveOrUpdateRevenuComplementaireFraisProf";
	}

	public String modificationRevenuComplementaire() {
		modif = 1;
		oldLibelle = this.libelle;
		oldType = this.type;
		return "addRevenuComplementaireForm";
	}

	public String modificationFraisProf() {
		modif = 1;
		oldLibelle = this.libelle;
		oldType = this.type;
		return "addFraisProfForm";
	}

	public String saveOrUpdateRevenuComplementaire() {

		RevenuComplementaireServiceImpl revenuComplementaire = new RevenuComplementaireServiceImpl();

		List<RevenuComplementaireBean> l = new ArrayList<RevenuComplementaireBean>();
		l = revenuComplementaire.getRevenuComplementaireByType(type,
				Integer.parseInt(session.getAttribute("groupe").toString()));
		int exist = 0;

		if (modif == 1) {
			int id = revenuComplementaire
					.getIdFromRevenuComplementaire(oldLibelle, oldType,
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));

			for (RevenuComplementaireBean rc : l) {
				if (rc.getLibelle().equals(libelle) && rc.getId() != id)
					exist = 1;
			}
		} else {
			for (RevenuComplementaireBean rc : l) {
				if (rc.getLibelle().equals(libelle))
					exist = 1;
			}
		}

		if (libelle == null || libelle.length() == 0) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Le libellé est obligatoire.",
					"Le libellé est obligatoire.");
			FacesContext.getCurrentInstance().addMessage("Formulaire:libelle",
					message);

			return "";
		} else {

			if (type == null || type.length() == 0) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le type est obligatoire.", "Le type est obligatoire.");
				FacesContext.getCurrentInstance().addMessage(
						"Formulaire:idRevCompl", message);

				return "";
			} else {

				if (exist == 1) {

					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Impossible de créer deux revenus complémentaires de même type avec le même libellé",
							"Impossible de créer deux revenus complémentaires de même type avec le même libellé");
					FacesContext.getCurrentInstance().addMessage(
							"Formulaire:valider", message);

					return "";

				} else {

					RevenuComplementaireServiceImpl serv = new RevenuComplementaireServiceImpl();
					int id = 0;
					if (modif == 1) {
						id = serv.getIdFromRevenuComplementaire(oldLibelle,
								oldType, Integer.parseInt(session.getAttribute(
										"groupe").toString()));
						modif = 0;
					}

					RevenuComplementaireBean revenuComplementaireBean = new RevenuComplementaireBean();
					revenuComplementaireBean.setId(id);
					// revenuComplementaireBean.setNom(this.nom);
					revenuComplementaireBean.setLibelle(libelle);
					revenuComplementaireBean.setType(type);
					revenuComplementaire.saveOrUpdate(revenuComplementaireBean,
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));

					save = 1;

					this.revenuComplementaireInventory.clear();
					this.revenuComplementaireInventory = serv
							.getRevenuComplementaireList(Integer
									.parseInt(session.getAttribute("groupe")
											.toString()));
					if (!this.typeSelected.equals("tous"))
						this.typeSelected = type;

					return "saveOrUpdateRevenuComplementaire";
				}
			}
		}
	}

	public void majRevenuComplementaire(ValueChangeEvent evt) {
		this.typeSelected = (String) evt.getNewValue();
		RevenuComplementaireServiceImpl service = new RevenuComplementaireServiceImpl();
		List<RevenuComplementaireBean> list = new ArrayList<RevenuComplementaireBean>();
		list = service.getRevenuComplementaireByType(this.typeSelected,
				Integer.parseInt(session.getAttribute("groupe").toString()));
		this.revenuComplementaireInventory.clear();
		this.revenuComplementaireInventory = list;
	}

	public List<RevenuComplementaireBean> getRevenuComplementaireInventory() {
		if (save == 1) {
			RevenuComplementaireServiceImpl revenuComplementaireService = new RevenuComplementaireServiceImpl();

			List<RevenuComplementaireBean> beanList = revenuComplementaireService
					.getRevenuComplementaireByType(this.typeSelected,
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
			this.revenuComplementaireInventory.clear();
			this.revenuComplementaireInventory = beanList;
			save = 0;
		}

		return revenuComplementaireInventory;
	}

	public void setRevenuComplementaireInventory(
			List<RevenuComplementaireBean> revenuComplementaireInventory) {
		this.revenuComplementaireInventory = revenuComplementaireInventory;
	}

	public List<SelectItem> getTypeList() {
		// if (maj == 0)
		// init();
		return typeList;
	}

	public void setTypeList(List<SelectItem> typeList) {
		this.typeList = typeList;
	}

	public String getTypeSelected() {
		return typeSelected;
	}

	public void setTypeSelected(String typeSelected) {
		this.typeSelected = typeSelected;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMaj() {
		return maj;
	}

	public void setMaj(int maj) {
		this.maj = maj;
	}

	public String getOldLibelle() {
		return oldLibelle;
	}

	public void setOldLibelle(String oldLibelle) {
		this.oldLibelle = oldLibelle;
	}

	public int getSave() {
		return save;
	}

	public void setSave(int save) {
		this.save = save;
	}

	public int getModif() {
		return modif;
	}

	public void setModif(int modif) {
		this.modif = modif;
	}

	public List<RevenuComplementaireBean> getFraisProfInventory() {
		Collections.sort(fraisProfInventory);
		return fraisProfInventory;
	}

	public void setFraisProfInventory(
			List<RevenuComplementaireBean> fraisProfInventory) {
		this.fraisProfInventory = fraisProfInventory;
	}

	public String getOldType() {
		return oldType;
	}

	public void setOldType(String oldType) {
		this.oldType = oldType;
	}
}
