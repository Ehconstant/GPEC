package com.cci.gpec.web.backingBean.Entretien;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.DomaineFormationBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.ObjectifsEntretienBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.DomaineFormationServiceImpl;
import com.cci.gpec.metier.implementation.EntretienServiceImpl;
import com.cci.gpec.metier.implementation.FormationServiceImpl;
import com.cci.gpec.metier.implementation.ObjectifsEntretienServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.effects.JavascriptContext;

public class SalarieEntretiensFormBB {

	// primary key
	private int id;
	private int idSalarie;

	// fields
	private Integer anneeReference;
	private String principaleConclusion;
	private String souhait;
	private String competence;
	private Date dateEntretien;
	private String crSigne;
	private boolean signe;
	private boolean modalRendered = false;
	private boolean modalRenderedDelFile = false;
	private String formations;
	private String formations2;
	private String formations3;
	private String formations4;
	private String formations5;
	private String nomManager;
	private String serviceManager;
	private String bilanDessous;
	private String bilanEgal;
	private String bilanDessus;
	private String commentaireBilan;
	private String formationNMoinsUn;
	private String commentaireFormation;
	private int domaineFormation;
	private int domaineFormation2;
	private int domaineFormation3;
	private int domaineFormation4;
	private int domaineFormation5;
	private String synthese;
	private String objCriteres;
	private String evolutionPerso;
	private String observations;
	private String modifProfil;
	private String rappelObj;
	private String evaluationGlobale;
	private ArrayList<SelectItem> domaineFormationList;

	private List<ObjectifsEntretienBean> objectifsAnneeNList;
	private List<ObjectifsEntretienBean> objectifsAnneeNListDeleted;
	private List<ObjectifsEntretienBean> objectifsAnneeNListTemp;
	private ObjectifsEntretienBean objTemp;
	private boolean disableButton = false;
	private boolean modifObj = false;

	private List<ObjectifsEntretienBean> objectifsAnneeNMoins1List;
	private List<ObjectifsEntretienBean> objectifsAnneeNMoins1ListDeleted;
	private List<ObjectifsEntretienBean> objectifsAnneeNMoins1ListTemp;
	private ObjectifsEntretienBean objTempNMoins1;
	private boolean disableButtonNMoins1 = false;
	private boolean modifObjNMoins1 = false;

	private int idServiceSelected;
	private int idTypeMetierSelected;

	private int indexSelectedRow = -1;
	private boolean isModif = false;
	private boolean init = true;
	private boolean add = false;
	private boolean newFile = false;
	private boolean ficheDePosteExiste = false;

	private boolean disabledDomaine = true;
	private boolean disabledDomaine2 = true;
	private boolean disabledDomaine3 = true;
	private boolean disabledDomaine4 = true;
	private boolean disabledDomaine5 = true;

	private boolean hasAnnee;

	private String libelleService;
	private String libelleMetier;

	private List<File> deletedFiles = new ArrayList<File>();

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");
	private List<InputFileData> fileListEntretienTemp = new ArrayList<InputFileData>();

	private int fileProgressEntretien = 0;

	private boolean fileError = false;

	public SalarieEntretiensFormBB() throws Exception {
		// init();
	}

	public void init() throws Exception {

		this.fileProgressEntretien = 0;
		this.fileListEntretienTemp.clear();

		DomaineFormationServiceImpl domaineFormationService = new DomaineFormationServiceImpl();

		List<DomaineFormationBean> domaineFormationBeanList = domaineFormationService
				.getDomaineFormationsList();
		domaineFormationList = new ArrayList<SelectItem>();
		SelectItem selectItem;
		for (DomaineFormationBean domaineFormationBean : domaineFormationBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(domaineFormationBean.getId());
			selectItem.setLabel(domaineFormationBean.getNom());
			domaineFormationList.add(selectItem);
		}
		ServiceImpl service = new ServiceImpl();

		this.id = 0;

		SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
				.getCurrentInstance().getCurrentInstance().getExternalContext()
				.getSessionMap().get("salarieFormBB");

		this.idSalarie = salarieFormBB.getId();

		this.anneeReference = null;

		this.principaleConclusion = new String();
		this.souhait = new String();

		this.crSigne = "Non";
		this.modifProfil = "Non";

		this.dateEntretien = new Date();

		this.indexSelectedRow = -1;
		this.isModif = false;

		this.formations = new String();
		this.formations2 = new String();
		this.formations3 = new String();
		this.formations4 = new String();
		this.formations5 = new String();

		this.libelleMetier = "";
		this.libelleService = "";
		this.idServiceSelected = 0;
		this.idTypeMetierSelected = 0;
		this.nomManager = new String();
		this.serviceManager = new String();
		this.bilanDessous = new String();
		this.bilanEgal = new String();
		this.bilanDessus = new String();
		this.commentaireBilan = new String();
		this.commentaireFormation = new String();
		this.domaineFormation = -1;
		this.domaineFormation2 = -1;
		this.domaineFormation3 = -1;
		this.domaineFormation4 = -1;
		this.domaineFormation5 = -1;
		this.synthese = new String();
		this.objCriteres = new String();
		this.evolutionPerso = new String();
		this.observations = new String();
		this.evaluationGlobale = new String();

		this.formationNMoinsUn = new String();

		ArrayList<ParcoursBean> parcoursBeanList = (ArrayList<ParcoursBean>) salarieFormBB
				.getParcoursBeanList();
		if (parcoursBeanList != null && parcoursBeanList.size() > 0) {
			Collections.sort(parcoursBeanList);
			Collections.reverse(parcoursBeanList);
			ParcoursBean parcoursBean = parcoursBeanList.get(0);
			this.libelleMetier = parcoursBean.getNomMetier();
			this.idTypeMetierSelected = parcoursBean.getIdMetierSelected();
		}
		if (salarieFormBB.getIdServiceSelected() > 0) {
			this.libelleService = service.getServiceBeanById(
					salarieFormBB.getIdServiceSelected()).getNom();
			this.idServiceSelected = salarieFormBB.getIdServiceSelected();
		}
		this.rappelObj = "Cette case sera mise à jour automatiquement en fonction de la date choisie à la validation de cet entretien";
		this.competence = new String();

		this.objectifsAnneeNList = new ArrayList<ObjectifsEntretienBean>();
		this.objectifsAnneeNListDeleted = new ArrayList<ObjectifsEntretienBean>();
		this.objectifsAnneeNListTemp = new ArrayList<ObjectifsEntretienBean>();
		this.objectifsAnneeNListTemp.add(new ObjectifsEntretienBean());

		this.objectifsAnneeNMoins1List = new ArrayList<ObjectifsEntretienBean>();
		this.objectifsAnneeNMoins1ListDeleted = new ArrayList<ObjectifsEntretienBean>();
		this.objectifsAnneeNMoins1ListTemp = new ArrayList<ObjectifsEntretienBean>();
		this.objTempNMoins1 = new ObjectifsEntretienBean();
		init = false;
	}

	public Integer getCurYear() {
		Calendar cal = new GregorianCalendar();
		Date d = new Date();
		cal.setTime(d);
		return cal.get(Calendar.YEAR);
	}

	public void uploadFileEntretien(ActionEvent event) throws Exception {

		InputFile inputFile = (InputFile) event.getSource();

		FileInfo fileInfo = inputFile.getFileInfo();

		if (fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.",
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:inputFileNameEntretien",
					message);
			return;
		}

		if (fileInfo.getStatus() == FileInfo.SAVED) {

			fileListEntretienTemp.clear();
			fileListEntretienTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("entretien")));
			if (isModif) {
				newFile = true;
			}
		}
	}

	public void fileUploadProgressEntretien(EventObject event) {

		InputFile ifile = (InputFile) event.getSource();

		fileProgressEntretien = ifile.getFileInfo().getPercent();
	}

	public void remove(ActionEvent evt) {
		modalRenderedDelFile = true;
	}

	public void cancelRemove(ActionEvent evt) {
		modalRenderedDelFile = false;
	}

	public void removeUploadedFileEntretien(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EntretienBean entretienBean = (EntretienBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		entretienBean.setJustificatif(null);
		EntretienServiceImpl serv = new EntretienServiceImpl();
		serv.saveOrUppdate(entretienBean);
		this.id = entretienBean.getId();

		init = true;
	}

	public void removeUploadedFileEntretienTemp(ActionEvent event) {
		modalRenderedDelFile = false;
		if (fileListEntretienTemp.get(0).getFile().exists()) {
			deletedFiles.add(fileListEntretienTemp.get(0).getFile());
		}
		fileListEntretienTemp.clear();
		this.fileProgressEntretien = 0;
		newFile = false;
	}

	public List<InputFileData> getFileListEntretienTemp() {
		if (!fileListEntretienTemp.isEmpty()
				&& (!fileListEntretienTemp.get(0).getFile().exists()
						|| !fileListEntretienTemp.get(0).getFile().isFile() || !fileListEntretienTemp
						.get(0).getFile().canRead())) {
			fileError = true;
		} else {
			fileError = false;
		}
		return fileListEntretienTemp;
	}

	public int getFileProgressEntretien() {
		return fileProgressEntretien;
	}

	public void printEntretienIndividuelSalarie(ActionEvent evt)
			throws Exception {

		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EntretienBean entretienBean = (EntretienBean) table.getRowData();

		indexSelectedRow = table.getRowIndex();

		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.printEntretienReport?contentType=pdf \",\"_Reports\");");

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("idEntretien", entretienBean.getId());
		eContext.getSessionMap().put("indexSelectedRow", indexSelectedRow);
		eContext.getSessionMap().put("nomGroupe", salarieFormBB.getNomGroupe());

	}

	public void download(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EntretienBean entretienBean;
		InputFileData fileData;
		String fileName = "";
		String filePath = "";
		if (add || isModif) {
			fileData = (InputFileData) table.getRowData();
			fileName = fileData.getFile().getName();
			filePath = fileData.getPath();
		} else {
			entretienBean = (EntretienBean) table.getRowData();
			fileName = entretienBean.getJustif().getName();
			filePath = entretienBean.getJustificatif();
		}
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		filePath = Utils.getSessionFileUploadPath(session, getIdSalarie(),
				"entretien", 0, false, false, salarieFormBB.getNomGroupe())
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

	public void majFormations(ValueChangeEvent evt) throws Exception {
		PhaseId phaseId = evt.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			evt.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			evt.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			if (evt.getComponent().getId().equals("idFormation")) {
				formations = (String) evt.getNewValue();
				if (formations.equals("")) {
					domaineFormation = -1;
					disabledDomaine = true;
				} else {
					disabledDomaine = false;
				}
			}
			if (evt.getComponent().getId().equals("idFormation2")) {
				formations2 = (String) evt.getNewValue();
				if (formations2.equals("")) {
					domaineFormation2 = -1;
					disabledDomaine2 = true;
				} else {
					disabledDomaine2 = false;
				}
			}
			if (evt.getComponent().getId().equals("idFormation3")) {
				formations3 = (String) evt.getNewValue();
				if (formations3.equals("")) {
					domaineFormation3 = -1;
					disabledDomaine3 = true;
				} else {
					disabledDomaine3 = false;
				}
			}
			if (evt.getComponent().getId().equals("idFormation4")) {
				formations4 = (String) evt.getNewValue();
				if (formations4.equals("")) {
					domaineFormation4 = -1;
					disabledDomaine4 = true;
				} else {
					disabledDomaine4 = false;
				}
			}
			if (evt.getComponent().getId().equals("idFormation5")) {
				formations5 = (String) evt.getNewValue();
				if (formations5.equals("")) {
					domaineFormation5 = -1;
					disabledDomaine5 = true;
				} else {
					disabledDomaine5 = false;
				}
			}
		}
	}

	public void initSalarieEntretienForm() throws Exception {
		init();
		EntretienServiceImpl serv = new EntretienServiceImpl();
		if (id != 0 && serv.getJustificatif(id) != null) {

			String justif = serv.getJustificatif(id);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(justif).getName());
			fileInfo.setFile(new File(justif));

			fileListEntretienTemp.clear();
			fileListEntretienTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("entretien")));
		}
		modalRendered = !modalRendered;
		add = true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPrincipaleConclusion() {
		return principaleConclusion;
	}

	public void setPrincipaleConclusion(String principaleConclusion) {
		this.principaleConclusion = principaleConclusion;
	}

	public String getSouhait() {
		return souhait;
	}

	public void setSouhait(String souhait) {
		this.souhait = souhait;
	}

	public String getCompetence() {
		return competence;
	}

	public void setCompetence(String competence) {
		this.competence = competence;
	}

	public Date getDateEntretien() {
		return dateEntretien;
	}

	public void setDateEntretien(Date dateEntretien) {
		this.dateEntretien = dateEntretien;
	}

	public String getCrSigne() {
		return crSigne;
	}

	public void setCrSigne(String crSigne) {
		this.crSigne = crSigne;
	}

	public boolean isModalRendered() {
		return modalRendered;
	}

	public void setModalRendered(boolean modalRendered) {
		this.modalRendered = modalRendered;
	}

	public void toggleModal(ActionEvent event) {
		if (newFile) {
			if (!fileListEntretienTemp.isEmpty()
					&& fileListEntretienTemp.get(0).getFile().exists()) {
				fileListEntretienTemp.clear();
			}
			newFile = false;
		}
		deletedFiles.clear();
		modalRendered = !modalRendered;
	}

	public int getIdServiceSelected() {
		return idServiceSelected;
	}

	public void setIdServiceSelected(int idServiceSelected) {
		this.idServiceSelected = idServiceSelected;
	}

	public int getIdTypeMetierSelected() {
		return idTypeMetierSelected;
	}

	public void setIdTypeMetierSelected(int idTypeMetierSelected) {
		this.idTypeMetierSelected = idTypeMetierSelected;
	}

	public String getLibelleService() {
		return libelleService;
	}

	public void setLibelleService(String libelleService) {
		this.libelleService = libelleService;
	}

	public String getLibelleMetier() {
		return libelleMetier;
	}

	public void setLibelleMetier(String libelleMetier) {
		this.libelleMetier = libelleMetier;
	}

	private boolean after(GregorianCalendar d1, GregorianCalendar d2) {
		// On teste si les dates sont egales, car GregorianCalendar ne prend pas
		// en comtpe ce cas, sinon on renvoi
		// true si d1 est avant d2
		if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
			if (d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)) {
				if (d1.get(Calendar.DATE) == d2.get(Calendar.DATE)) {
					return true;
				}
			}
		}
		return (d1.after(d2));
	}

	private boolean before(GregorianCalendar d1, GregorianCalendar d2) {
		// On teste si les dates sont egales, car GregorianCalendar ne prend pas
		// en comtpe ce cas, sinon on renvoi
		// true si d1 est avant d2
		if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
			if (d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)) {
				if (d1.get(Calendar.DATE) == d2.get(Calendar.DATE)) {
					return true;
				}
			}
		}
		return (d1.before(d2));
	}

	private boolean isInPeriode(GregorianCalendar debutA,
			GregorianCalendar finA, GregorianCalendar debutF,
			GregorianCalendar finF) {
		// Teste si les dates de début et de fin appartiennent au début et fin
		// de période
		if (before(debutA, debutF) && before(debutF, finA)) {
			return true;
		}

		if (before(debutF, debutA) && after(finF, finA)) {
			return true;
		}

		if (before(debutF, debutA) && before(finF, finA)
				&& before(debutA, finF)) {
			return true;
		}

		return false;
	}

	private boolean isInEntrepriseWithBorne(GregorianCalendar debutC,
			GregorianCalendar finC, GregorianCalendar debutDate,
			GregorianCalendar finDate) {

		if (before(debutDate, debutC)) {
			return false;
		}
		if (before(finDate, debutC)) {
			return false;
		}
		if (after(finDate, finC)) {
			return false;
		}
		if (after(debutDate, finC)) {
			return false;
		}

		return true;
	}

	private boolean isInEntreprise(GregorianCalendar debutC,
			GregorianCalendar debutDate, GregorianCalendar finDate) {

		if (before(debutDate, debutC)) {
			return false;
		}
		if (before(finDate, debutC)) {
			return false;
		}

		return true;
	}

	public void refreshFormation(ValueChangeEvent evt) throws Exception {
		PhaseId phaseId = evt.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			evt.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			evt.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			this.anneeReference = (Integer) evt.getNewValue();
			this.formationNMoinsUn = new String();
			FormationServiceImpl form = new FormationServiceImpl();
			List<FormationBean> listForm = new ArrayList<FormationBean>();
			listForm = form
					.getFormationBeanListByIdSalarie(this.getIdSalarie());
			for (FormationBean f : listForm) {
				Calendar cal = new GregorianCalendar();
				cal.setTime(f.getDebutFormation());
				if (this.anneeReference == cal.get(Calendar.YEAR)) {
					this.formationNMoinsUn += f.getNomFormation() + "\n";
				}
			}
			EntretienServiceImpl entServ = new EntretienServiceImpl();
			ObjectifsEntretienServiceImpl objServ = new ObjectifsEntretienServiceImpl();
			if (entServ.getEntretienBeanListByIdSalarieAndAnnee(
					this.getIdSalarie(), anneeReference - 1).size() >= 1) {
				this.objectifsAnneeNMoins1List = objServ
						.getObjectifsEntretienListByIdEntretien(entServ
								.getEntretienBeanListByIdSalarieAndAnnee(
										this.getIdSalarie(), anneeReference - 1)
								.get(0).getId());
			} else {
				this.objectifsAnneeNMoins1List.clear();
			}

		}
	}

	public void saveOrUpdateSalarieEntretien(ActionEvent event)
			throws Exception {

		newFile = false;
		EntretienBean entretienBean = new EntretienBean();

		List<ParcoursBean> listParcoursSalarie = salarieFormBB
				.getParcoursBeanList();

		if (!Utils.isInEntreprise(listParcoursSalarie, dateEntretien,
				dateEntretien)) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Aucune pr\u00E9sence \u00e0 cette date.",
					"Aucune pr\u00E9sence \u00e0 cette date.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:dateEntretien", message);
			return;
		}

		if (this.id == 0) {
			this.id = -1;
		}
		entretienBean.setId(this.id);
		entretienBean.setAnneeReference(this.anneeReference);
		entretienBean.setCompetence(this.competence);
		entretienBean.setEvaluationGlobale(this.evaluationGlobale);
		entretienBean.setCrSigne(this.crSigne);
		entretienBean.setDateEntretien(this.dateEntretien);
		entretienBean.setPrincipaleConclusion(this.principaleConclusion);
		entretienBean.setSouhait(this.souhait);
		entretienBean.setBilanDessous(this.bilanDessous);
		entretienBean.setBilanDessus(this.bilanDessus);
		entretienBean.setBilanEgal(this.bilanEgal);
		entretienBean.setCommentaireBilan(this.commentaireBilan);
		entretienBean.setCommentaireFormation(this.commentaireFormation);
		entretienBean.setDomainesFormation(this.domaineFormation);
		entretienBean.setDomainesFormation2(this.domaineFormation2);
		entretienBean.setDomainesFormation3(this.domaineFormation3);
		entretienBean.setDomainesFormation4(this.domaineFormation4);
		entretienBean.setDomainesFormation5(this.domaineFormation5);
		entretienBean.setEvolutionPerso(this.evolutionPerso);
		entretienBean.setFormationNMoinsUn(this.formationNMoinsUn);
		entretienBean.setModifProfil(this.modifProfil);
		entretienBean.setNomManager(this.nomManager);
		entretienBean.setObjCriteres(this.objCriteres);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		entretienBean.setObservations(this.observations);
		entretienBean.setServiceManager(this.serviceManager);
		entretienBean.setSynthese(this.synthese);

		entretienBean.setFormations(this.formations);
		entretienBean.setFormations2(this.formations2);
		entretienBean.setFormations3(this.formations3);
		entretienBean.setFormations4(this.formations4);
		entretienBean.setFormations5(this.formations5);
		entretienBean.setIdSalarie(salarieFormBB.getId());
		deletedFiles.clear();
		if (!fileListEntretienTemp.isEmpty()) {
			entretienBean.setJustificatif(fileListEntretienTemp.get(0)
					.getFile().getName());
		}

		if (isModif == true) {
			salarieFormBB.getEntretienBeanList().set(indexSelectedRow,
					entretienBean);
			isModif = false;
		} else {
			EntretienServiceImpl entServ = new EntretienServiceImpl();
			entServ.saveOrUppdate(entretienBean);
			salarieFormBB.getEntretienBeanList().add(entretienBean);
		}

		ObjectifsEntretienServiceImpl obj = new ObjectifsEntretienServiceImpl();
		for (ObjectifsEntretienBean o : this.objectifsAnneeNListDeleted) {
			obj.suppression(o);
		}
		this.objectifsAnneeNListDeleted.clear();
		for (ObjectifsEntretienBean o : this.objectifsAnneeNList) {
			o.setIdEntretien(entretienBean.getId());
			obj.saveOrUppdate(o);
		}

		for (ObjectifsEntretienBean o : this.objectifsAnneeNMoins1ListDeleted) {
			obj.suppression(o);
		}
		this.objectifsAnneeNMoins1ListDeleted.clear();
		for (ObjectifsEntretienBean o : this.objectifsAnneeNMoins1List) {
			obj.saveOrUppdate(o);
		}

		modalRendered = !modalRendered;
		salarieFormBB.saveOrUpdateSalarie();
		add = false;
		isModif = false;
		init = true;
		disabledDomaine = true;
		disabledDomaine2 = true;
		disabledDomaine3 = true;
		disabledDomaine4 = true;
		disabledDomaine5 = true;
	}

	public void addObj(ActionEvent evt) {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		ObjectifsEntretienBean objectifsEntretienBean = (ObjectifsEntretienBean) table
				.getRowData();
		if (!objectifsEntretienBean.getIntitule().equals("")
				|| !objectifsEntretienBean.getDelai().equals("")
				|| !objectifsEntretienBean.getMoyen().equals("")) {

			this.objectifsAnneeNList.add(objectifsEntretienBean);
			this.objectifsAnneeNListTemp.clear();
			this.objectifsAnneeNListTemp.add(new ObjectifsEntretienBean());
			modifObj = false;
			disableButton = false;
			objTemp = null;
		}
	}

	public void removeObj(ActionEvent evt) {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		ObjectifsEntretienBean objectifsEntretienBean = (ObjectifsEntretienBean) table
				.getRowData();

		this.objectifsAnneeNList.remove(objectifsEntretienBean);
		this.objectifsAnneeNListDeleted.add(objectifsEntretienBean);
	}

	public void modifObj(ActionEvent evt) {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		ObjectifsEntretienBean objectifsEntretienBean = (ObjectifsEntretienBean) table
				.getRowData();

		this.objectifsAnneeNList.remove(objectifsEntretienBean);
		this.objectifsAnneeNListTemp.clear();
		this.objectifsAnneeNListTemp.add(objectifsEntretienBean);

		modifObj = true;
		disableButton = true;
		this.objTemp = new ObjectifsEntretienBean();
		this.objTemp.setIdEntretien(objectifsEntretienBean.getIdEntretien());
		this.objTemp.setIdObjectif(objectifsEntretienBean.getIdObjectif());
		this.objTemp.setCommentaire(objectifsEntretienBean.getCommentaire());
		this.objTemp.setResultat(objectifsEntretienBean.getResultat());

		this.objTemp.setIntitule(objectifsEntretienBean.getIntitule());
		this.objTemp.setDelai(objectifsEntretienBean.getDelai());
		this.objTemp.setMoyen(objectifsEntretienBean.getMoyen());
	}

	public void cancelObj() {
		if (modifObj) {
			this.objectifsAnneeNList.add(objTemp);
			this.objTemp = null;
			modifObj = false;
		}
		this.objectifsAnneeNListTemp.clear();
		this.objectifsAnneeNListTemp.add(new ObjectifsEntretienBean());

		disableButton = false;
	}

	public void addObjNMoins1(ActionEvent evt) {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		ObjectifsEntretienBean objectifsEntretienBean = (ObjectifsEntretienBean) table
				.getRowData();

		if (!objectifsEntretienBean.getIntitule().equals("")
				|| !objectifsEntretienBean.getResultat().equals("")
				|| !objectifsEntretienBean.getCommentaire().equals("")) {
			this.objectifsAnneeNMoins1List.add(objectifsEntretienBean);
			this.objectifsAnneeNMoins1ListTemp.clear();
			modifObjNMoins1 = false;
			disableButtonNMoins1 = false;
			objTempNMoins1 = null;
		}
	}

	public void modifObjNMoins1(ActionEvent evt) {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		ObjectifsEntretienBean objectifsEntretienBean = (ObjectifsEntretienBean) table
				.getRowData();

		this.objectifsAnneeNMoins1List.remove(objectifsEntretienBean);
		this.objectifsAnneeNMoins1ListTemp.clear();
		this.objectifsAnneeNMoins1ListTemp.add(objectifsEntretienBean);

		modifObjNMoins1 = true;
		disableButtonNMoins1 = true;
		this.objTempNMoins1 = new ObjectifsEntretienBean();
		this.objTempNMoins1.setIdEntretien(objectifsEntretienBean
				.getIdEntretien());
		this.objTempNMoins1.setIdObjectif(objectifsEntretienBean
				.getIdObjectif());
		this.objTempNMoins1.setCommentaire(objectifsEntretienBean
				.getCommentaire());
		this.objTempNMoins1.setResultat(objectifsEntretienBean.getResultat());

		this.objTempNMoins1.setIntitule(objectifsEntretienBean.getIntitule());
		this.objTempNMoins1.setDelai(objectifsEntretienBean.getDelai());
		this.objTempNMoins1.setMoyen(objectifsEntretienBean.getMoyen());
	}

	public void cancelObjNMoins1(ActionEvent evt) {

		if (modifObjNMoins1) {
			this.objectifsAnneeNMoins1List.add(objTempNMoins1);
			this.objTempNMoins1 = null;
			modifObjNMoins1 = false;
		}
		this.objectifsAnneeNMoins1ListTemp.clear();
		this.objectifsAnneeNMoins1ListTemp.add(new ObjectifsEntretienBean());

		disableButtonNMoins1 = false;
	}

	public void modifEntretien(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EntretienBean entretienBean = (EntretienBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		this.fileProgressEntretien = 0;

		ObjectifsEntretienServiceImpl objServ = new ObjectifsEntretienServiceImpl();
		if ((indexSelectedRow + 1) < salarieFormBB.getEntretienBeanList()
				.size()) {
			EntretienBean entretienBean2 = salarieFormBB.getEntretienBeanList()
					.get(indexSelectedRow + 1);
			this.rappelObj = entretienBean2.getObjIntitule();
			this.objectifsAnneeNMoins1List = objServ
					.getObjectifsEntretienListByIdEntretien(entretienBean2
							.getId());
		} else {
			this.rappelObj = "Il n'y a pas eu d'entretien avant celui-ci.";
			this.objectifsAnneeNMoins1List.clear();
		}

		this.objectifsAnneeNList = objServ
				.getObjectifsEntretienListByIdEntretien(entretienBean.getId());

		if (this.objectifsAnneeNList.isEmpty()) {
			this.objectifsAnneeNListTemp.clear();
			this.objectifsAnneeNListTemp.add(new ObjectifsEntretienBean());
		}

		if (this.objectifsAnneeNMoins1List.isEmpty()) {
			this.objectifsAnneeNMoins1ListTemp.clear();
		}

		if (entretienBean.getAnneeReference() == null
				|| entretienBean.getAnneeReference() == 0) {
			this.anneeReference = getCurYear();
		} else {
			this.anneeReference = entretienBean.getAnneeReference();
		}
		this.competence = entretienBean.getCompetence();
		this.evaluationGlobale = entretienBean.getEvaluationGlobale();
		this.crSigne = entretienBean.getCrSigne();
		this.dateEntretien = entretienBean.getDateEntretien();
		this.id = entretienBean.getId();
		this.principaleConclusion = entretienBean.getPrincipaleConclusion();
		this.souhait = entretienBean.getSouhait();
		this.formations = entretienBean.getFormations();
		this.disabledDomaine = formations.isEmpty();
		this.formations2 = entretienBean.getFormations2();
		this.disabledDomaine2 = formations2.isEmpty();
		this.formations3 = entretienBean.getFormations3();
		this.disabledDomaine3 = formations3.isEmpty();
		this.formations4 = entretienBean.getFormations4();
		this.disabledDomaine4 = formations4.isEmpty();
		this.formations5 = entretienBean.getFormations5();
		this.disabledDomaine5 = formations5.isEmpty();
		this.nomManager = entretienBean.getNomManager();
		this.serviceManager = entretienBean.getServiceManager();
		this.bilanDessous = entretienBean.getBilanDessous();
		this.bilanEgal = entretienBean.getBilanEgal();
		this.bilanDessus = entretienBean.getBilanDessus();
		this.commentaireBilan = entretienBean.getCommentaireBilan();
		this.formationNMoinsUn = entretienBean.getFormationNMoinsUn();
		this.commentaireFormation = entretienBean.getCommentaireFormation();
		this.domaineFormation = entretienBean.getDomainesFormation();
		this.domaineFormation2 = entretienBean.getDomainesFormation2();
		this.domaineFormation3 = entretienBean.getDomainesFormation3();
		this.domaineFormation4 = entretienBean.getDomainesFormation4();
		this.domaineFormation5 = entretienBean.getDomainesFormation5();
		this.synthese = entretienBean.getSynthese();
		this.objCriteres = entretienBean.getObjCriteres();
		this.evolutionPerso = entretienBean.getEvolutionPerso();
		this.observations = entretienBean.getObservations();
		this.modifProfil = entretienBean.getModifProfil();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		fileListEntretienTemp.clear();
		if (entretienBean.getJustificatif() != null) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(entretienBean.getJustificatif())
					.getName());
			fileInfo.setFile(new File(entretienBean.getJustificatif()));

			fileListEntretienTemp.clear();
			fileListEntretienTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("entretien")));
		}

		isModif = true;

		modalRendered = !modalRendered;

	}

	public void deleteEntretien(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EntretienBean entretienBean = (EntretienBean) table.getRowData();
		// On récupère aussi son index
		int index = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ObjectifsEntretienServiceImpl objServ = new ObjectifsEntretienServiceImpl();
		List<ObjectifsEntretienBean> l = new ArrayList<ObjectifsEntretienBean>();
		l = objServ.getObjectifsEntretienListByIdEntretien(entretienBean
				.getId());

		for (ObjectifsEntretienBean o : l) {
			objServ.suppression(o);
		}

		salarieFormBB.getEntretienBeanList().remove(index);

		if (entretienBean.getId() > 0) {
			EntretienServiceImpl entretienService = new EntretienServiceImpl();
			entretienService.deleteEntretien(entretienBean);
		}
		this.fileListEntretienTemp.clear();
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

	public String getFormations() {
		return formations;
	}

	public void setFormations(String formations) {
		this.formations = formations;
	}

	public String getNomManager() {
		return nomManager;
	}

	public void setNomManager(String nomManager) {
		this.nomManager = nomManager;
	}

	public String getServiceManager() {
		return serviceManager;
	}

	public void setServiceManager(String serviceManager) {
		this.serviceManager = serviceManager;
	}

	public String getBilanDessous() {
		return bilanDessous;
	}

	public void setBilanDessous(String bilanDessous) {
		this.bilanDessous = bilanDessous;
	}

	public String getBilanEgal() {
		return bilanEgal;
	}

	public void setBilanEgal(String bilanEgal) {
		this.bilanEgal = bilanEgal;
	}

	public String getBilanDessus() {
		return bilanDessus;
	}

	public void setBilanDessus(String bilanDessus) {
		this.bilanDessus = bilanDessus;
	}

	public String getCommentaireBilan() {
		return commentaireBilan;
	}

	public void setCommentaireBilan(String commentaireBilan) {
		this.commentaireBilan = commentaireBilan;
	}

	public String getFormationNMoinsUn() {
		return formationNMoinsUn;
	}

	public void setFormationNMoinsUn(String formationNMoinsUn) {
		this.formationNMoinsUn = formationNMoinsUn;
	}

	public String getCommentaireFormation() {
		return commentaireFormation;
	}

	public void setCommentaireFormation(String commentaireFormation) {
		this.commentaireFormation = commentaireFormation;
	}

	public int getDomaineFormation() {
		return domaineFormation;
	}

	public void setDomaineFormation(int domaineFormation) {
		this.domaineFormation = domaineFormation;
	}

	public String getSynthese() {
		return synthese;
	}

	public void setSynthese(String synthese) {
		this.synthese = synthese;
	}

	public String getObjCriteres() {
		return objCriteres;
	}

	public void setObjCriteres(String objCriteres) {
		this.objCriteres = objCriteres;
	}

	public String getEvolutionPerso() {
		return evolutionPerso;
	}

	public void setEvolutionPerso(String evolutionPerso) {
		this.evolutionPerso = evolutionPerso;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public String getModifProfil() {
		return modifProfil;
	}

	public void setModifProfil(String modifProfil) {
		this.modifProfil = modifProfil;
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

	public String getRappelObj() {
		return rappelObj;
	}

	public void setRappelObj(String rappelObj) {
		this.rappelObj = rappelObj;
	}

	public String getEvaluationGlobale() {
		return evaluationGlobale;
	}

	public void setEvaluationGlobale(String evaluationGlobale) {
		this.evaluationGlobale = evaluationGlobale;
	}

	public String getFormations2() {
		return formations2;
	}

	public void setFormations2(String formations2) {
		this.formations2 = formations2;
	}

	public String getFormations3() {
		return formations3;
	}

	public void setFormations3(String formations3) {
		this.formations3 = formations3;
	}

	public String getFormations4() {
		return formations4;
	}

	public void setFormations4(String formations4) {
		this.formations4 = formations4;
	}

	public String getFormations5() {
		return formations5;
	}

	public void setFormations5(String formations5) {
		this.formations5 = formations5;
	}

	public int getDomaineFormation2() {
		return domaineFormation2;
	}

	public void setDomaineFormation2(int domaineFormation2) {
		this.domaineFormation2 = domaineFormation2;
	}

	public int getDomaineFormation3() {
		return domaineFormation3;
	}

	public void setDomaineFormation3(int domaineFormation3) {
		this.domaineFormation3 = domaineFormation3;
	}

	public int getDomaineFormation4() {
		return domaineFormation4;
	}

	public void setDomaineFormation4(int domaineFormation4) {
		this.domaineFormation4 = domaineFormation4;
	}

	public int getDomaineFormation5() {
		return domaineFormation5;
	}

	public void setDomaineFormation5(int domaineFormation5) {
		this.domaineFormation5 = domaineFormation5;
	}

	public boolean isFicheDePosteExiste() {
		return ficheDePosteExiste;
	}

	public void setFicheDePosteExiste(boolean ficheDePosteExiste) {
		this.ficheDePosteExiste = ficheDePosteExiste;
	}

	public ArrayList<SelectItem> getDomaineFormationList() {
		return domaineFormationList;
	}

	public void setDomaineFormationList(
			ArrayList<SelectItem> domaineFormationList) {
		this.domaineFormationList = domaineFormationList;
	}

	public String getUrl() throws Exception {
		return salarieFormBB.getUrl("entretien");
	}

	public void setFileListEntretienTemp(
			List<InputFileData> fileListEntretienTemp) {
		this.fileListEntretienTemp = fileListEntretienTemp;
	}

	public boolean isFileError() {
		return fileError;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public void setFileProgressEntretien(int fileProgressEntretien) {
		this.fileProgressEntretien = fileProgressEntretien;
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

	public boolean isSigne() throws Exception {
		EntretienServiceImpl entServ = new EntretienServiceImpl();
		if (this.id != 0 && this.id != -1) {
			EntretienBean e = entServ.getEntretienBeanById(this.id);
			if (crSigne.equals("Oui") && e.getCrSigne().equals("Oui")) {
				return true;
			} else {
				return false;
			}
		} else {

			return false;
		}
	}

	public void setSigne(boolean signe) {
		this.signe = signe;
	}

	public Integer getAnneeReference() {
		return anneeReference;
	}

	public void setAnneeReference(Integer anneeReference) {
		this.anneeReference = anneeReference;
	}

	public List<ObjectifsEntretienBean> getObjectifsAnneeNList() {
		return objectifsAnneeNList;
	}

	public void setObjectifsAnneeNList(
			List<ObjectifsEntretienBean> objectifsAnneeNList) {
		this.objectifsAnneeNList = objectifsAnneeNList;
	}

	public List<ObjectifsEntretienBean> getObjectifsAnneeNMoins1List() {
		return objectifsAnneeNMoins1List;
	}

	public void setObjectifsAnneeNMoins1List(
			List<ObjectifsEntretienBean> objectifsAnneeNMoins1List) {
		this.objectifsAnneeNMoins1List = objectifsAnneeNMoins1List;
	}

	public List<ObjectifsEntretienBean> getObjectifsAnneeNListTemp() {
		return objectifsAnneeNListTemp;
	}

	public void setObjectifsAnneeNListTemp(
			List<ObjectifsEntretienBean> objectifsAnneeNListTemp) {
		this.objectifsAnneeNListTemp = objectifsAnneeNListTemp;
	}

	public List<ObjectifsEntretienBean> getObjectifsAnneeNMoins1ListTemp() {
		return objectifsAnneeNMoins1ListTemp;
	}

	public void setObjectifsAnneeNMoins1ListTemp(
			List<ObjectifsEntretienBean> objectifsAnneeNMoins1ListTemp) {
		this.objectifsAnneeNMoins1ListTemp = objectifsAnneeNMoins1ListTemp;
	}

	public ObjectifsEntretienBean getObjTemp() {
		return objTemp;
	}

	public void setObjTemp(ObjectifsEntretienBean objTemp) {
		this.objTemp = objTemp;
	}

	public ObjectifsEntretienBean getObjTempNMoins1() {
		return objTempNMoins1;
	}

	public void setObjTempNMoins1(ObjectifsEntretienBean objTempNMoins1) {
		this.objTempNMoins1 = objTempNMoins1;
	}

	public boolean isDisableButton() {
		return disableButton;
	}

	public void setDisableButton(boolean disableButton) {
		this.disableButton = disableButton;
	}

	public boolean isDisableButtonNMoins1() {
		return disableButtonNMoins1;
	}

	public void setDisableButtonNMoins1(boolean disableButtonNMoins1) {
		this.disableButtonNMoins1 = disableButtonNMoins1;
	}

	public List<ObjectifsEntretienBean> getObjectifsAnneeNListDeleted() {
		return objectifsAnneeNListDeleted;
	}

	public void setObjectifsAnneeNListDeleted(
			List<ObjectifsEntretienBean> objectifsAnneeNListDeleted) {
		this.objectifsAnneeNListDeleted = objectifsAnneeNListDeleted;
	}

	public List<ObjectifsEntretienBean> getObjectifsAnneeNMoins1ListDeleted() {
		return objectifsAnneeNMoins1ListDeleted;
	}

	public void setObjectifsAnneeNMoins1ListDeleted(
			List<ObjectifsEntretienBean> objectifsAnneeNMoins1ListDeleted) {
		this.objectifsAnneeNMoins1ListDeleted = objectifsAnneeNMoins1ListDeleted;
	}

	public boolean isDisabledDomaine() {
		return disabledDomaine;
	}

	public void setDisabledDomaine(boolean disabledDomaine) {
		this.disabledDomaine = disabledDomaine;
	}

	public boolean isDisabledDomaine2() {
		return disabledDomaine2;
	}

	public void setDisabledDomaine2(boolean disabledDomaine2) {
		this.disabledDomaine2 = disabledDomaine2;
	}

	public boolean isDisabledDomaine3() {
		return disabledDomaine3;
	}

	public void setDisabledDomaine3(boolean disabledDomaine3) {
		this.disabledDomaine3 = disabledDomaine3;
	}

	public boolean isDisabledDomaine4() {
		return disabledDomaine4;
	}

	public void setDisabledDomaine4(boolean disabledDomaine4) {
		this.disabledDomaine4 = disabledDomaine4;
	}

	public boolean isDisabledDomaine5() {
		return disabledDomaine5;
	}

	public void setDisabledDomaine5(boolean disabledDomaine5) {
		this.disabledDomaine5 = disabledDomaine5;
	}

	public boolean isHasAnnee() {
		return anneeReference != null && anneeReference != 0;
	}

	public void setHasAnnee(boolean hasAnnee) {
		this.hasAnnee = hasAnnee;
	}

	public void setIdSalarie(int idSalarie) {
		this.idSalarie = idSalarie;
	}

}
