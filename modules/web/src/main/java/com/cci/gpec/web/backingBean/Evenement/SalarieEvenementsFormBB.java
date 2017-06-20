package com.cci.gpec.web.backingBean.Evenement;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.EvenementBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.EvenementServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.effects.JavascriptContext;

public class SalarieEvenementsFormBB {

	// primary key
	private int id;
	private int idSalarie;

	// fields
	private String commentaire;
	private String nature;
	private String decision;
	private Date dateEvenement;
	private boolean modalRendered = false;
	private boolean modalRenderedDelFile = false;

	// private String hierarchique;
	private String interlocuteur;

	private int indexSelectedRow = -1;
	private boolean isModif = false;
	private boolean init = true;
	private boolean add = false;
	private boolean newFile = false;

	private Date debutExtraction;
	private Date finExtraction;

	private List<File> deletedFiles = new ArrayList<File>();

	// private String url;
	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");
	private List<InputFileData> fileListEvenementTemp = new ArrayList<InputFileData>();

	private int fileProgressEvenement = 0;

	private boolean fileError = false;

	public SalarieEvenementsFormBB() throws Exception {
		// init();
	}

	public void init() throws Exception {

		this.fileProgressEvenement = 0;
		this.fileListEvenementTemp.clear();

		// this.idSalarie = salarieFormBB.getId();

		ServiceImpl service = new ServiceImpl();

		this.id = 0;

		this.dateEvenement = null;
		this.debutExtraction = null;
		this.finExtraction = null;
		this.commentaire = "";
		this.decision = "";
		this.nature = "";
		// this.hierarchique = "";
		this.interlocuteur = "";

		this.indexSelectedRow = -1;
		this.isModif = false;

		init = false;
	}

	public void uploadFileEvenement(ActionEvent event) throws Exception {

		InputFile inputFile = (InputFile) event.getSource();

		FileInfo fileInfo = inputFile.getFileInfo();

		if (fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.",
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:inputFileNameEvenement",
					message);
			return;
		}

		if (fileInfo.getStatus() == FileInfo.SAVED) {

			fileListEvenementTemp.clear();
			fileListEvenementTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("evenement")));
			if (isModif)
				newFile = true;
		}
	}

	public void fileUploadProgressEvenement(EventObject event) {

		InputFile ifile = (InputFile) event.getSource();

		fileProgressEvenement = ifile.getFileInfo().getPercent();
	}

	public void remove(ActionEvent evt) {
		modalRenderedDelFile = true;
	}

	public void cancelRemove(ActionEvent evt) {
		modalRenderedDelFile = false;
	}

	public void removeUploadedFileEvenement(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EvenementBean evenementBean = (EvenementBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		evenementBean.setJustificatif(null);
		EvenementServiceImpl serv = new EvenementServiceImpl();
		serv.saveOrUppdate(evenementBean);
		this.id = evenementBean.getId();

		init = true;
	}

	public void removeUploadedFileEvenementTemp(ActionEvent event) {
		modalRenderedDelFile = false;
		if (fileListEvenementTemp.get(0).getFile().exists())
			deletedFiles.add(fileListEvenementTemp.get(0).getFile());
		fileListEvenementTemp.clear();
		this.fileProgressEvenement = 0;
		newFile = false;
	}

	public List<InputFileData> getFileListEvenementTemp() {
		if (!fileListEvenementTemp.isEmpty()
				&& (!fileListEvenementTemp.get(0).getFile().exists()
						|| !fileListEvenementTemp.get(0).getFile().isFile() || !fileListEvenementTemp
						.get(0).getFile().canRead()))
			fileError = true;
		else
			fileError = false;
		return fileListEvenementTemp;
	}

	public int getFileProgressEvenement() {
		return fileProgressEvenement;
	}

	public void printEvenementIndividuelSalarie(ActionEvent evt)
			throws Exception {

		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EvenementBean evenementBean = (EvenementBean) table.getRowData();

		indexSelectedRow = table.getRowIndex();

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("idEvenement", evenementBean.getId());
		eContext.getSessionMap().put("nom", salarieFormBB.getNom());
		eContext.getSessionMap().put("prenom", salarieFormBB.getPrenom());
		eContext.getSessionMap().put("idEntreprise",
				salarieFormBB.getIdEntrepriseSelected());
		eContext.getSessionMap().put("nomEntreprise",
				salarieFormBB.getNomEntreprise());
		eContext.getSessionMap().put("indexSelectedRow", indexSelectedRow);
		eContext.getSessionMap().put("nomGroupe", salarieFormBB.getNomGroupe());

		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.printEvenementReport?contentType=pdf \",\"_Reports\");");

	}

	public void printEvenementsSalarie(ActionEvent evt) throws Exception {

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		if (this.debutExtraction != null) {
			eContext.getSessionMap().put("debutExtraction",
					this.debutExtraction);
		} else {
			eContext.getSessionMap().put("debutExtraction", null);
		}
		if (this.finExtraction != null) {
			eContext.getSessionMap().put("finExtraction", this.finExtraction);
		} else {
			eContext.getSessionMap().put("finExtraction", null);
		}
		eContext.getSessionMap().put("idEvenement", null);
		eContext.getSessionMap().put("nom", salarieFormBB.getNom());
		eContext.getSessionMap().put("prenom", salarieFormBB.getPrenom());
		eContext.getSessionMap().put("idEntreprise",
				salarieFormBB.getIdEntrepriseSelected());
		eContext.getSessionMap().put("nomEntreprise",
				salarieFormBB.getEntreprise());
		eContext.getSessionMap().put("idSalarie", salarieFormBB.getId());
		eContext.getSessionMap().put("nomGroupe", salarieFormBB.getNomGroupe());

		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.printEvenementReport?contentType=pdf \",\"_Reports\");");

	}

	public void download(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EvenementBean evenementBean;
		InputFileData fileData;
		String fileName = "";
		String filePath = "";
		if (add || isModif) {
			fileData = (InputFileData) table.getRowData();
			fileName = fileData.getFile().getName();
			filePath = fileData.getPath();
		} else {
			evenementBean = (EvenementBean) table.getRowData();
			fileName = evenementBean.getJustif().getName();
			filePath = evenementBean.getJustificatif();
		}
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		filePath = Utils.getSessionFileUploadPath(session, getIdSalarie(),
				"evenement", 0, false, false, salarieFormBB.getNomGroupe())
				+ new File(filePath).getName();

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", filePath);

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(filePath).toPath())
						+ " \",\"_Download\");");

	}

	public void initSalarieEvenementForm() throws Exception {
		init();
		modalRendered = !modalRendered;
		add = true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isModalRendered() {
		return modalRendered;
	}

	public void setModalRendered(boolean modalRendered) {
		this.modalRendered = modalRendered;
	}

	public void toggleModal(ActionEvent event) {
		if (newFile) {
			if (!fileListEvenementTemp.isEmpty()
					&& fileListEvenementTemp.get(0).getFile().exists()) {
				fileListEvenementTemp.clear();
			}
			newFile = false;
		}
		deletedFiles.clear();
		add = false;
		isModif = false;
		modalRendered = !modalRendered;
	}

	public void saveOrUpdateSalarieEvenement(ActionEvent event)
			throws Exception {

		newFile = false;
		EvenementBean evenementBean = new EvenementBean();

		List<ParcoursBean> listParcoursSalarie = salarieFormBB
				.getParcoursBeanList();

		if (!Utils.isInEntreprise(listParcoursSalarie, dateEvenement,
				dateEvenement)) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Aucune pr\u00E9sence \u00e0 cette date.",
					"Aucune pr\u00E9sence \u00e0 cette date.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:dateEvenement", message);
			return;
		}

		if (this.id == 0) {
			this.id = -1;
		}
		evenementBean.setId(this.id);
		evenementBean.setIdSalarie(getIdSalarie());
		evenementBean.setDateEvenement(this.dateEvenement);
		evenementBean.setCommentaire(this.commentaire);
		evenementBean.setDecision(this.decision);
		evenementBean.setNature(this.nature);
		evenementBean.setInterlocuteur(this.interlocuteur);

		deletedFiles.clear();
		if (!fileListEvenementTemp.isEmpty())
			evenementBean.setJustificatif(fileListEvenementTemp.get(0)
					.getFile().getName());

		modalRendered = !modalRendered;
		EvenementServiceImpl evserv = new EvenementServiceImpl();
		evserv.saveOrUppdate(evenementBean);

		salarieFormBB.setDebutEv(null);
		salarieFormBB.setFinEv(null);

		add = false;
		isModif = false;
		init = true;
	}

	public void modifEvenement(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EvenementBean evenementBean = (EvenementBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		this.id = evenementBean.getId();
		this.nature = evenementBean.getNature();
		this.commentaire = evenementBean.getCommentaire();
		this.dateEvenement = evenementBean.getDateEvenement();
		this.decision = evenementBean.getDecision();
		this.interlocuteur = evenementBean.getInterlocuteur();

		this.fileProgressEvenement = 0;

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		fileListEvenementTemp.clear();
		if (evenementBean.getJustificatif() != null) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(evenementBean.getJustificatif())
					.getName());
			fileInfo.setFile(new File(evenementBean.getJustificatif()));

			fileListEvenementTemp.clear();
			fileListEvenementTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("evenement")));
		}

		isModif = true;

		modalRendered = !modalRendered;

	}

	public void deleteEvenement(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EvenementBean evenementBean = (EvenementBean) table.getRowData();
		// On récupère aussi son index
		int index = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		if (evenementBean.getId() > 0) {
			EvenementServiceImpl evenementService = new EvenementServiceImpl();
			evenementService.deleteEvenement(evenementBean);
		}
		this.fileListEvenementTemp.clear();
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

	public int getIndexSelectedRow() {
		return indexSelectedRow;
	}

	public void setIndexSelectedRow(int indexSelectedRow) {
		this.indexSelectedRow = indexSelectedRow;
	}

	public boolean isModif() {
		return isModif;
	}

	public void setModif(boolean isModif) {
		this.isModif = isModif;
	}

	public String getUrl() throws Exception {
		return salarieFormBB.getUrl("evenement");
	}

	public void setFileListEvenementTemp(
			List<InputFileData> fileListEvenementTemp) {
		this.fileListEvenementTemp = fileListEvenementTemp;
	}

	public boolean isFileError() {
		return fileError;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public void setFileProgressEvenement(int fileProgressEvenement) {
		this.fileProgressEvenement = fileProgressEvenement;
	}

	public boolean isInit() throws Exception {
		if (init && !isModif && !add) {
			init();
			init = false;
		}
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public int getIdSalarie() {
		return salarieFormBB.getId();
	}

	public boolean isModalRenderedDelFile() {
		return modalRenderedDelFile;
	}

	public void setModalRenderedDelFile(boolean modalRenderedDelFile) {
		this.modalRenderedDelFile = modalRenderedDelFile;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public Date getDateEvenement() {
		return dateEvenement;
	}

	public void setDateEvenement(Date dateEvenement) {
		this.dateEvenement = dateEvenement;
	}

	public String getHierarchique() throws Exception {
		SalarieServiceImpl sal = new SalarieServiceImpl();
		if (salarieFormBB.getIdLienSubordination() != null
				&& salarieFormBB.getIdLienSubordination() != -1)
			return sal.getSalarieBeanById(
					salarieFormBB.getIdLienSubordination()).getNom();
		else
			return "";
	}

	public String getInterlocuteur() {
		return interlocuteur;
	}

	public void setInterlocuteur(String interlocuteur) {
		this.interlocuteur = interlocuteur;
	}

	public Date getDebutExtraction() {
		return debutExtraction;
	}

	public void setDebutExtraction(Date debutExtraction) {
		this.debutExtraction = debutExtraction;
	}

	public Date getFinExtraction() {
		return finExtraction;
	}

	public void setFinExtraction(Date finExtraction) {
		this.finExtraction = finExtraction;
	}

	public void setIdSalarie(int idSalarie) {
		this.idSalarie = idSalarie;
	}

	// public void setHierarchique(String hierarchique) {
	// this.hierarchique = hierarchique;
	// }

}
