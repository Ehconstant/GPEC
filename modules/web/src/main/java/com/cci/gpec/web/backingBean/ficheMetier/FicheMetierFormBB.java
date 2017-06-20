package com.cci.gpec.web.backingBean.ficheMetier;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.FicheMetierBean;
import com.cci.gpec.commons.FicheMetierEntrepriseBean;
import com.cci.gpec.commons.StatutBean;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierEntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierServiceImpl;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.BackingBeanForm;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.effects.JavascriptContext;

public class FicheMetierFormBB extends BackingBeanForm {

	private int id;

	private String intitule;
	private String finalite;
	private int cspReference;
	private String cspLibelle;
	private String ActiviteResponsabilite;
	private String savoir;
	private String savoirFaire;
	private String savoirEtre;
	private String niveauFormation;
	private String niveauFormationLibelle;
	private String particularite;
	private ArrayList<SelectItem> typeStatutList;
	private int indexSelectedRow = -1;
	private List<FicheMetierEntrepriseBean> ficheMetierEntreprisesInventory = new ArrayList<FicheMetierEntrepriseBean>();
	private List<EntrepriseBean> EntreprisesInventory = new ArrayList<EntrepriseBean>();
	private List<SelectItem> listEntreprise = new ArrayList<SelectItem>();
	private List<SelectItem> listEntrepriseExport = new ArrayList<SelectItem>();
	private List<FicheMetierBean> fichesMetierInventoryByEntreprise = new ArrayList<FicheMetierBean>();
	private UIInput idFicheMetierBinding;
	private UIInput idEntrepriseBinding;
	private int idFicheMetierEntreprise = -1;
	private int idEntrepriseSelected = -1;
	private int idEntrepriseExport = -1;
	private int idFicheMetierSelected = -1;
	private boolean modalRenderedEntreprise = false;
	private boolean modalRenderedAideComp = false;

	private boolean consultation = false;

	private boolean affichage = true;

	private boolean init = true;

	private ArrayList<SelectItem> entreprisesList;
	private ArrayList<SelectItem> nivFormationInitList;
	private String libelleAffichage = "Afficher par entreprise";

	// private String url;
	private List<InputFileData> fileListFicheMetierTemp = new ArrayList<InputFileData>();

	private boolean fileError = false;

	private int fileProgressFicheMetier = 0;

	private boolean newFile = false;
	private boolean isModif = false;
	private boolean modalRenderedDelFile = false;

	private List<File> deletedFiles = new ArrayList<File>();

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");

	public FicheMetierFormBB() throws Exception {
		super();
		init();
	}

	public FicheMetierFormBB(int id, String nom) throws Exception {
		super(id, nom);
		init();
	}

	public String init() throws Exception {
		this.id = 0;
		this.intitule = "";
		this.finalite = "";
		this.cspReference = 0;
		this.ActiviteResponsabilite = "";
		this.savoir = "";
		this.savoirFaire = "";
		this.savoirEtre = "";
		this.niveauFormation = "";
		this.particularite = "";
		this.indexSelectedRow = -1;
		this.idFicheMetierEntreprise = 0;
		this.idFicheMetierSelected = 0;
		this.modalRenderedEntreprise = false;

		this.fileListFicheMetierTemp.clear();
		this.fileProgressFicheMetier = 0;

		this.isModif = false;

		nivFormationInitList = new ArrayList<SelectItem>();

		nivFormationInitList.add(new SelectItem("VB", "V bis"));
		nivFormationInitList.add(new SelectItem("V", "V"));
		nivFormationInitList.add(new SelectItem("IV", "IV"));
		nivFormationInitList.add(new SelectItem("III", "III"));
		nivFormationInitList.add(new SelectItem("II", "II"));
		nivFormationInitList.add(new SelectItem("I", "I"));

		listEntreprise = new ArrayList<SelectItem>();
		listEntrepriseExport = new ArrayList<SelectItem>();

		typeStatutList = new ArrayList<SelectItem>();
		StatutServiceImpl statutService = new StatutServiceImpl();
		List<StatutBean> statutBeanList = statutService.getStatutsList();
		SelectItem selectItem;
		for (StatutBean statutBean : statutBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(statutBean.getId());
			selectItem.setLabel(statutBean.getNom());
			typeStatutList.add(selectItem);
		}

		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();

		List<EntrepriseBean> entrepriseBeanList = entrepriseService
				.getEntreprisesList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		entreprisesList = new ArrayList<SelectItem>();
		SelectItem selectItem2;
		for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
			selectItem2 = new SelectItem();
			selectItem2.setValue(entrepriseBean.getId());
			selectItem2.setLabel(entrepriseBean.getNom());
			entreprisesList.add(selectItem2);
		}

		return "addFicheMetierForm";
	}

	public String initFicheMetier() throws Exception {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		this.idEntrepriseExport = -1;
		this.fileProgressFicheMetier = 0;

		FicheMetierServiceImpl serv = new FicheMetierServiceImpl();
		fileListFicheMetierTemp.clear();

		if (id != 0 && serv.getJustificatif(id) != null) {
			String justif = Utils
					.getSessionFileUploadPath(session, this.id, "FicheMetier",
							0, true, false, salarieFormBB.getNomGroupe())
					+ serv.getJustificatif(id);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(justif).getName());
			fileInfo.setFile(new File(justif));

			fileListFicheMetierTemp.add(new InputFileData(fileInfo, Utils
					.getFileUrl(this.id, "FicheMetier", false, true, false,
							false, salarieFormBB.getNomGroupe())));
		}

		FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
		try {
			StatutServiceImpl statutService = new StatutServiceImpl();

			FicheMetierBean ficheMetierBean = ficheMetierService
					.getFicheMetierBeanById(this.id);
			this.intitule = ficheMetierBean.getNom();
			this.finalite = ficheMetierBean.getFinaliteFicheMetier();
			this.cspReference = ficheMetierBean.getCspReference();
			this.ActiviteResponsabilite = ficheMetierBean
					.getActiviteResponsabilite();
			this.savoir = ficheMetierBean.getSavoir();
			this.savoirFaire = ficheMetierBean.getSavoirFaire();
			this.savoirEtre = ficheMetierBean.getSavoirEtre();
			this.niveauFormation = ficheMetierBean.getNiveauFormationRequis();
			this.particularite = ficheMetierBean.getParticularite();
			typeStatutList = new ArrayList<SelectItem>();

			List<StatutBean> statutBeanList = statutService.getStatutsList();
			SelectItem selectItem;
			for (StatutBean statutBean : statutBeanList) {
				selectItem = new SelectItem();
				selectItem.setValue(statutBean.getId());
				selectItem.setLabel(statutBean.getNom());
				typeStatutList.add(selectItem);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "addFicheMetierForm";
	}

	public void uploadFileFicheMetier(ActionEvent event) throws Exception {

		InputFile inputFile = (InputFile) event.getSource();

		FileInfo fileInfo = inputFile.getFileInfo();

		if (fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.",
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:inputFileNameFicheMetier",
					message);
			return;
		}

		if (fileInfo.getStatus() == FileInfo.SAVED) {

			fileListFicheMetierTemp.clear();
			fileListFicheMetierTemp.add(new InputFileData(fileInfo, Utils
					.getFileUrl(this.id, "FicheMetier", false, true, false,
							false, salarieFormBB.getNomGroupe())));
			if (isModif)
				newFile = true;
		}
	}

	public void fileUploadProgressFicheMetier(EventObject event) {

		InputFile ifile = (InputFile) event.getSource();

		fileProgressFicheMetier = ifile.getFileInfo().getPercent();
	}

	public void remove(ActionEvent evt) {
		modalRenderedDelFile = true;
	}

	public void cancelRemove(ActionEvent evt) {
		modalRenderedDelFile = false;
	}

	public void removeUploadedFileFicheMetier(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FicheMetierBean FicheMetierBean = (FicheMetierBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		// if (new File(FicheMetierBean.getJustificatif()).exists())
		// new File(FicheMetierBean.getJustificatif()).delete();

		FicheMetierBean.setJustificatif(null);
		FicheMetierServiceImpl serv = new FicheMetierServiceImpl();
		serv.saveOrUppdate(FicheMetierBean,
				Integer.parseInt(session.getAttribute("groupe").toString()));
		this.id = FicheMetierBean.getId();

		fileListFicheMetierTemp.clear();

		init = true;
	}

	public void removeUploadedFileFicheMetierTemp(ActionEvent event) {
		modalRenderedDelFile = false;

		if (fileListFicheMetierTemp.get(0).getFile().exists())
			deletedFiles.add(fileListFicheMetierTemp.get(0).getFile());
		fileListFicheMetierTemp.clear();
		this.fileProgressFicheMetier = 0;
		newFile = false;
	}

	public int getFileProgressFicheMetier() {
		return fileProgressFicheMetier;
	}

	public void toggleAffichage(ActionEvent event) {
		affichage = !affichage;
		libelleAffichage = affichage ? "Afficher par entreprise"
				: "Afficher par fiche métier";

	}

	public List<EntrepriseBean> getEntreprisesInventory() throws Exception {
		// // On récupère la datatable.
		// HtmlDataTable table = getParentDatatable((UIComponent)
		// event.getSource());
		// // On récupère l'objet affiché à la bonne ligne de la datatable.
		// FicheMetierBean ficheMetierBean = (FicheMetierBean)
		// table.getRowData();

		FicheMetierEntrepriseServiceImpl ficheMetierEntreprise = new FicheMetierEntrepriseServiceImpl();
		ficheMetierEntreprisesInventory = ficheMetierEntreprise
				.getFicheMetierEntrepriseBeanListByIdFicheMetier((Integer) idFicheMetierBinding
						.getValue());
		EntreprisesInventory = new ArrayList<EntrepriseBean>();
		for (FicheMetierEntrepriseBean ficheMetierEntrepriseBean : ficheMetierEntreprisesInventory) {
			EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
			EntrepriseBean entrepriseBean = entrepriseService
					.getEntrepriseBeanById(ficheMetierEntrepriseBean
							.getIdEntreprise());
			EntreprisesInventory.add(entrepriseBean);
		}
		return EntreprisesInventory;
	}

	public void supprimerFicheMetier(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FicheMetierBean ficheMetierBean = (FicheMetierBean) table.getRowData();

		FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
		boolean b = ficheMetierService.supprimer(ficheMetierBean);
		if (!b) {
			ResourceBundle rb = ResourceBundle.getBundle("errors");
			FacesMessage message = new FacesMessage(
					rb.getString("suppressionFicheMetier"));
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"idForm:dataTable:idSupprimer", message);
		}
	}

	public void printFicheMetier(ActionEvent evt) throws Exception {

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("idFicheMetier", 0);
		eContext.getSessionMap().put("idEntreprise", this.idEntrepriseExport);
		eContext.getSessionMap().put("groupe",
				Integer.parseInt(session.getAttribute("groupe").toString()));
		eContext.getSessionMap().put("nomGroupe", salarieFormBB.getNomGroupe());
		eContext.getSessionMap().put("empty", false);

		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.printFicheMetier?contentType=pdf \",\"_Reports\");");

	}

	public void printFicheMetierEntreprise(ActionEvent evt) throws Exception {

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		if (this.idEntrepriseExport != -1) {

			eContext.getSessionMap().put("idEntreprise",
					this.idEntrepriseExport);
			JavascriptContext
					.addJavascriptCall(
							FacesContext.getCurrentInstance(),
							"window.open(\"servlet.printFicheMetierEntreprise?contentType=pdf \",\"_Reports\");");
		}
		eContext.getSessionMap().put("groupe",
				Integer.parseInt(session.getAttribute("groupe").toString()));
		eContext.getSessionMap().put("nomGroupe", salarieFormBB.getNomGroupe());
	}

	public void updateEntrepriseRattachee(ValueChangeEvent evt) {
		this.idEntrepriseExport = (Integer) evt.getNewValue();
	}

	public void printOneFicheMetier(ActionEvent evt) throws Exception {

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("idFicheMetier", this.id);
		EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
		if (this.idEntrepriseExport != -1) {
			eContext.getSessionMap()
					.put("idEntreprise",
							serv.getEntrepriseBeanById(this.idEntrepriseExport)
									.getId());
			eContext.getSessionMap().put(
					"entrepriseExport",
					serv.getEntrepriseBeanById(this.idEntrepriseExport)
							.getNom());
		} else {
			eContext.getSessionMap().put("idEntreprise", null);
			eContext.getSessionMap().put("entrepriseExport", null);
		}
		eContext.getSessionMap().put("groupe",
				Integer.parseInt(session.getAttribute("groupe").toString()));
		eContext.getSessionMap().put("nomGroupe", salarieFormBB.getNomGroupe());
		if (this.id == 0) {
			eContext.getSessionMap().put("empty", true);
		} else {
			eContext.getSessionMap().put("empty", false);
		}

		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.printFicheMetier?contentType=pdf \",\"_Reports\");");

	}

	public void download(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FicheMetierBean ficheMetierBean = (FicheMetierBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier",
				ficheMetierBean.getJustif().getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(ficheMetierBean
								.getJustificatif()).toPath())
						+ " \",\"_Download\");");

	}

	public void downloadFromForm(ActionEvent evt) throws Exception {

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier",
				fileListFicheMetierTemp.get(0).getFile().getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(fileListFicheMetierTemp.get(0)
								.getFile().toPath()) + " \",\"_Download\");");

	}

	public String supprimerFicheMetierEntreprise() throws Exception {

		FicheMetierEntrepriseServiceImpl ficheMetierEntrepriseService = new FicheMetierEntrepriseServiceImpl();
		FicheMetierEntrepriseBean ficheMetierEntrepriseBean = new FicheMetierEntrepriseBean();
		ficheMetierEntrepriseBean.setIdEntreprise(idFicheMetierEntreprise);
		ficheMetierEntrepriseBean.setIdFicheMetier(idFicheMetierSelected);
		ficheMetierEntrepriseService.suppression(ficheMetierEntrepriseBean);
		return "saveOrUpdateFicheMetier";

	}

	public void ajouterFicheMetierEntreprise(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FicheMetierBean ficheMetierBean = (FicheMetierBean) table.getRowData();

		this.idFicheMetierSelected = ficheMetierBean.getIdFicheMetier();
		modalRenderedEntreprise = true;
	}

	public String ajouterFicheMetierEntrepriseFin(ActionEvent evt)
			throws Exception {

		try {
			FicheMetierEntrepriseServiceImpl ficheMetierEntrepriseService = new FicheMetierEntrepriseServiceImpl();

			FicheMetierEntrepriseBean ficheMetierEntrepriseBean = new FicheMetierEntrepriseBean();
			ficheMetierEntrepriseBean.setIdEntreprise(idEntrepriseSelected);
			ficheMetierEntrepriseBean.setIdFicheMetier(idFicheMetierSelected);
			ficheMetierEntrepriseService
					.saveOrUppdate(ficheMetierEntrepriseBean);
		} catch (Exception e) {
			// TODO: handle exception
		}
		modalRenderedEntreprise = !modalRenderedEntreprise;
		return "saveOrUpdateFicheMetier";
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

	public String saveOrUpdateFicheMetier() {
		FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
		FicheMetierBean ficheMetierBean = new FicheMetierBean();
		ficheMetierBean.setId(this.id);
		// ficheMetierBean.setIdGroupe(Integer.parseInt(session.getAttribute(
		// "groupe").toString()));
		ficheMetierBean.setNom(this.intitule);
		ficheMetierBean.setFinaliteFicheMetier(this.finalite);
		ficheMetierBean.setCspReference(this.cspReference);
		ficheMetierBean.setActiviteResponsabilite(this.ActiviteResponsabilite);
		ficheMetierBean.setSavoir(this.savoir);
		ficheMetierBean.setSavoirFaire(this.savoirFaire);
		ficheMetierBean.setSavoirEtre(this.savoirEtre);
		ficheMetierBean.setNiveauFormationRequis(this.niveauFormation);
		ficheMetierBean.setParticularite(this.particularite);
		// if (!deletedFiles.isEmpty()) {
		// for (File f : deletedFiles) {
		// f.delete();
		// }
		// }
		if (!fileListFicheMetierTemp.isEmpty()) {

			ficheMetierBean.setJustificatif(fileListFicheMetierTemp.get(0)
					.getFile().getName());

		}

		ficheMetierService.saveOrUppdate(ficheMetierBean,
				Integer.parseInt(session.getAttribute("groupe").toString()));

		isModif = true;

		return "addFicheMetierForm";
	}

	//
	public String saveOrUpdateFicheMetierFin() {
		FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
		FicheMetierBean ficheMetierBean = new FicheMetierBean();
		ficheMetierBean.setId(this.id);
		// ficheMetierBean.setIdGroupe(Integer.parseInt(session.getAttribute(
		// "groupe").toString()));
		ficheMetierBean.setNom(this.intitule);
		ficheMetierBean.setFinaliteFicheMetier(this.finalite);
		ficheMetierBean.setCspReference(this.cspReference);
		ficheMetierBean.setActiviteResponsabilite(this.ActiviteResponsabilite);
		ficheMetierBean.setSavoir(this.savoir);
		ficheMetierBean.setSavoirFaire(this.savoirFaire);
		ficheMetierBean.setSavoirEtre(this.savoirEtre);
		ficheMetierBean.setNiveauFormationRequis(this.niveauFormation);
		ficheMetierBean.setParticularite(this.particularite);

		// if (!deletedFiles.isEmpty()) {
		// for (File f : deletedFiles) {
		// f.delete();
		// }
		// }
		if (!fileListFicheMetierTemp.isEmpty()) {

			ficheMetierBean.setJustificatif(fileListFicheMetierTemp.get(0)
					.getFile().getName());

		}

		ficheMetierService.saveOrUppdate(ficheMetierBean,
				Integer.parseInt(session.getAttribute("groupe").toString()));

		isModif = false;

		return "saveOrUpdateFicheMetier";
	}

	public boolean isModalRenderedAideComp() {
		return modalRenderedAideComp;
	}

	public void setModalRenderedAideComp(boolean modalRenderedAideComp) {
		this.modalRenderedAideComp = modalRenderedAideComp;
	}

	public void toggleModalAideComp(ActionEvent event) {
		modalRenderedAideComp = !modalRenderedAideComp;
	}

	public String annuler() {
		this.consultation = false;
		return "saveOrUpdateFicheMetier";
	}

	public int getId() {
		return id;
	}

	public void setId(int idFicheMetier) {
		this.id = idFicheMetier;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getFinalite() {
		return finalite;
	}

	public void setFinalité(String finalite) {
		this.finalite = finalite;
	}

	public int getCspReference() {
		return cspReference;
	}

	public void setCspReference(int cspReference) {
		this.cspReference = cspReference;
	}

	public String getActiviteResponsabilite() {
		return ActiviteResponsabilite;
	}

	public void setActiviteResponsabilite(String activiteResponsabilite) {
		ActiviteResponsabilite = activiteResponsabilite;
	}

	public String getSavoir() {
		return savoir;
	}

	public void setSavoir(String savoir) {
		this.savoir = savoir;
	}

	public String getSavoirFaire() {
		return savoirFaire;
	}

	public void setSavoirFaire(String savoirFaire) {
		this.savoirFaire = savoirFaire;
	}

	public String getSavoirEtre() {
		return savoirEtre;
	}

	public void setSavoirEtre(String savoirEtre) {
		this.savoirEtre = savoirEtre;
	}

	public String getNiveauFormation() {
		return niveauFormation;
	}

	public void setNiveauFormation(String niveauFormation) {
		this.niveauFormation = niveauFormation;
	}

	public String getParticularite() {
		return particularite;
	}

	public void setParticularite(String particularite) {
		this.particularite = particularite;
	}

	public void setFinalite(String finalite) {
		this.finalite = finalite;
	}

	public UIInput getIdFicheMetierBinding() {
		return idFicheMetierBinding;
	}

	public void setIdFicheMetierBinding(UIInput idFicheMetierBinding) {
		this.idFicheMetierBinding = idFicheMetierBinding;
	}

	public void setEntreprisesInventory(
			List<EntrepriseBean> entreprisesInventory) {
		EntreprisesInventory = entreprisesInventory;
	}

	public int getIdFicheMetierEntreprise() {
		return idFicheMetierEntreprise;
	}

	public void setIdFicheMetierEntreprise(int idFicheMetierEntreprise) {
		this.idFicheMetierEntreprise = idFicheMetierEntreprise;
	}

	public int getIdFicheMetierSelected() {
		return idFicheMetierSelected;
	}

	public void setIdFicheMetierSelected(int idFicheMetierSelected) {
		this.idFicheMetierSelected = idFicheMetierSelected;
	}

	public boolean isModalRenderedEntreprise() {
		return modalRenderedEntreprise;
	}

	public void setModalRenderedEntreprise(boolean modalRenderedEntreprise) {
		this.modalRenderedEntreprise = modalRenderedEntreprise;
	}

	public void toggleModal(ActionEvent event) {
		modalRenderedEntreprise = !modalRenderedEntreprise;
	}

	public int getIdEntrepriseSelected() {
		return idEntrepriseSelected;
	}

	public void setIdEntrepriseSelected(int idEntrepriseSelected) {
		this.idEntrepriseSelected = idEntrepriseSelected;
	}

	public ArrayList<SelectItem> getEntreprisesList() {
		return entreprisesList;
	}

	public void setEntreprisesList(ArrayList<SelectItem> entreprisesList) {
		this.entreprisesList = entreprisesList;
	}

	public List<FicheMetierBean> getFichesMetierInventoryByEntreprise()
			throws Exception {
		FicheMetierEntrepriseServiceImpl ficheMetierEntreprise = new FicheMetierEntrepriseServiceImpl();
		ficheMetierEntreprisesInventory = ficheMetierEntreprise
				.getFicheMetierEntrepriseBeanListByIdEntreprise((idEntrepriseBinding
						.getValue() != null) ? Integer
						.parseInt(idEntrepriseBinding.getValue().toString())
						: null);
		fichesMetierInventoryByEntreprise = new ArrayList<FicheMetierBean>();
		for (FicheMetierEntrepriseBean ficheMetierEntrepriseBean : ficheMetierEntreprisesInventory) {
			FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
			FicheMetierBean ficheMetierBean = ficheMetierService
					.getFicheMetierBeanById(ficheMetierEntrepriseBean
							.getIdFicheMetier());
			fichesMetierInventoryByEntreprise.add(ficheMetierBean);
		}
		return fichesMetierInventoryByEntreprise;
	}

	public void setFichesMetierInventoryByEntreprise(
			List<FicheMetierBean> fichesMetierInventoryByEntreprise) {

		this.fichesMetierInventoryByEntreprise = fichesMetierInventoryByEntreprise;
	}

	public UIInput getIdEntrepriseBinding() {
		return idEntrepriseBinding;
	}

	public void setIdEntrepriseBinding(UIInput idEntrepriseBinding) {
		this.idEntrepriseBinding = idEntrepriseBinding;
	}

	public List<SelectItem> getListEntreprise() throws Exception {
		listEntreprise = new ArrayList<SelectItem>();

		List<Integer> l = new ArrayList<Integer>();

		FicheMetierEntrepriseServiceImpl fme = new FicheMetierEntrepriseServiceImpl();
		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
		for (FicheMetierEntrepriseBean f : fme
				.getFicheMetierEntrepriseList(Integer.parseInt(session
						.getAttribute("groupe").toString()))
		// .getFicheMetierEntrepriseBeanListByIdFicheMetier(this.id)
		) {
			SelectItem item = new SelectItem();
			item.setLabel(entrepriseService.getEntrepriseBeanById(
					f.getIdEntreprise()).getNom());
			item.setValue(entrepriseService.getEntrepriseBeanById(
					f.getIdEntreprise()).getId());
			if (!l.contains(f.getIdEntreprise())) {
				listEntreprise.add(item);
				l.add(f.getIdEntreprise());
			}
		}
		return listEntreprise;
	}

	public void setListEntreprise(List<SelectItem> listEntreprise) {
		this.listEntreprise = listEntreprise;
	}

	public List<SelectItem> getListEntrepriseExport() throws Exception {
		listEntrepriseExport = new ArrayList<SelectItem>();

		List<Integer> l = new ArrayList<Integer>();

		FicheMetierEntrepriseServiceImpl fme = new FicheMetierEntrepriseServiceImpl();
		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
		for (FicheMetierEntrepriseBean f : fme
				.getFicheMetierEntrepriseBeanListByIdFicheMetier(this.id)) {
			SelectItem item = new SelectItem();
			item.setLabel(entrepriseService.getEntrepriseBeanById(
					f.getIdEntreprise()).getNom());
			item.setValue(entrepriseService.getEntrepriseBeanById(
					f.getIdEntreprise()).getId());
			if (!l.contains(f.getIdEntreprise())) {
				listEntrepriseExport.add(item);
				l.add(f.getIdEntreprise());
			}
		}
		return listEntrepriseExport;
	}

	public void setListEntrepriseExport(List<SelectItem> listEntrepriseExport) {
		this.listEntrepriseExport = listEntrepriseExport;
	}

	public ArrayList<SelectItem> getNivFormationInitList() {
		return nivFormationInitList;
	}

	public void setNivFormationInitList(
			ArrayList<SelectItem> nivFormationInitList) {
		this.nivFormationInitList = nivFormationInitList;
	}

	public boolean isAffichage() {
		return affichage;
	}

	public void setAffichage(boolean affichage) {
		this.affichage = affichage;
	}

	public String getLibelleAffichage() {
		return libelleAffichage;
	}

	public void setLibelleAffichage(String libelleAffichage) {
		this.libelleAffichage = libelleAffichage;
	}

	public int getIdEntrepriseExport() {
		return idEntrepriseExport;
	}

	public void setIdEntrepriseExport(int idEntrepriseExport) {
		this.idEntrepriseExport = idEntrepriseExport;
	}

	public boolean isConsultation() {
		return consultation;
	}

	public void setConsultation(boolean consultation) {
		this.consultation = consultation;
	}

	public String viewFiche() throws Exception {
		this.consultation = true;
		initFicheMetier();
		return "addFicheMetierForm";
	}

	public ArrayList<SelectItem> getTypeStatutList() {
		return typeStatutList;
	}

	public void setTypeStatutList(ArrayList<SelectItem> typeStatutList) {
		this.typeStatutList = typeStatutList;
	}

	public boolean isInit() throws Exception {
		init();
		return true;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public List<InputFileData> getFileListFicheMetierTemp() {
		if (!fileListFicheMetierTemp.isEmpty()
				&& (!fileListFicheMetierTemp.get(0).getFile().exists()
						|| !fileListFicheMetierTemp.get(0).getFile().isFile() || !fileListFicheMetierTemp
						.get(0).getFile().canRead()))
			fileError = true;
		else
			fileError = false;
		return fileListFicheMetierTemp;
	}

	public void setFileListFicheMetierTemp(
			List<InputFileData> fileListFicheMetierTemp) {
		this.fileListFicheMetierTemp = fileListFicheMetierTemp;
	}

	public boolean isFileError() {
		return fileError;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public boolean isModalRenderedDelFile() {
		return modalRenderedDelFile;
	}

	public void setModalRenderedDelFile(boolean modalRenderedDelFile) {
		this.modalRenderedDelFile = modalRenderedDelFile;
	}

	public void setFileProgressFicheMetier(int fileProgressFicheMetier) {
		this.fileProgressFicheMetier = fileProgressFicheMetier;
	}

	public String getUrl() throws Exception {
		return Utils.getFileUrl(this.id, "FicheMetier", false, true, false,
				false, salarieFormBB.getNomGroupe());
	}

	public String getCspLibelle() throws Exception {
		StatutServiceImpl serv = new StatutServiceImpl();
		if (cspReference != -1 && cspReference != 0) {
			return serv.getStatutBeanById(cspReference).getNom();
		} else {
			return "";
		}
	}

	public String getNiveauFormationLibelle() {
		if (niveauFormation.equals("VB"))
			return "V bis";
		else
			return niveauFormation;
	}

}
