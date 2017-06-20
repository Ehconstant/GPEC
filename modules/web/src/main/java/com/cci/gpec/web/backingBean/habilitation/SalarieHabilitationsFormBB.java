package com.cci.gpec.web.backingBean.habilitation;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
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
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.TypeHabilitationBean;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.HabilitationServiceImpl;
import com.cci.gpec.metier.implementation.TypeHabilitationServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.effects.JavascriptContext;

public class SalarieHabilitationsFormBB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6111237584746049216L;

	private boolean modalRendered = false;
	private boolean modalRenderedDelFile = false;
	// primary key
	private int id;
	private int idSalarie;

	// fields
	private Date delivrance;
	private Date expiration;
	private Integer dureeValidite;
	private String justificatif;
	private String commentaire;
	private ArrayList<SelectItem> typeHabilitationList;
	private int idTypeHabilitationSelected;
	private boolean modalRenderedAbs = false;

	private boolean fail = false;

	// File sizes used to generate formatted label
	public static final long MEGABYTE_LENGTH_BYTES = 1048000l;
	public static final long KILOBYTE_LENGTH_BYTES = 1024l;

	// files associated with the current user
	private final List<InputFileData> fileList = new ArrayList<InputFileData>();

	private int indexSelectedRow = -1;

	private boolean isModif = false;
	private boolean add = false;
	private boolean init = true;
	private boolean newFile = false;

	private boolean dureeValiditePositif = false;

	private int fileProgress = 0;

	private boolean fileError = false;

	HabilitationBean habilitationBeanSave = null;

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public SalarieHabilitationsFormBB() throws Exception {
		init();
	}

	public void init() throws Exception {

		salarieFormBB = (SalarieFormBB) FacesContext.getCurrentInstance()
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("salarieFormBB");

		HabilitationServiceImpl serv = new HabilitationServiceImpl();

		if (id != -1 && id != 0 && serv.getJustificatif(id) != null) {

			String justif = serv.getJustificatif(id);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(justif).getName());
			fileInfo.setFile(new File(justif));

			fileList.clear();
			fileList.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("habilitation")));
		} else {
			fileList.clear();
		}

		TypeHabilitationServiceImpl typeHabilitationService = new TypeHabilitationServiceImpl();

		List<TypeHabilitationBean> domaineHabilitationBeanList = typeHabilitationService
				.getTypeHabilitationList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		Collections.sort(domaineHabilitationBeanList);

		for (TypeHabilitationBean typeHabilitationBean : domaineHabilitationBeanList) {
			if (typeHabilitationBean.getNom().equals("Autre")) {
				domaineHabilitationBeanList.remove(typeHabilitationBean);
				domaineHabilitationBeanList.add(typeHabilitationBean);
				break;
			}
		}

		typeHabilitationList = new ArrayList<SelectItem>();
		SelectItem selectItem;
		for (TypeHabilitationBean typeHabilitationBean : domaineHabilitationBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(typeHabilitationBean.getId());
			selectItem.setLabel(typeHabilitationBean.getNom());
			typeHabilitationList.add(selectItem);
		}

		this.delivrance = null;
		this.dureeValidite = null;
		this.expiration = null;
		this.commentaire = "";
		this.id = 0;
		this.idTypeHabilitationSelected = 0;
		this.fileList.clear();
		this.indexSelectedRow = -1;
		this.isModif = false;
		this.fileProgress = 0;

	}

	public void initHabilitationForm() throws Exception {
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

	public Date getDelivrance() {
		return delivrance;
	}

	public void setDelivrance(Date delivrance) {
		this.delivrance = delivrance;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public Integer getDureeValidite() {

		if (isModif) {
			Long nbJoursValidite = getNbJoursValidite();
			this.dureeValidite = nbJoursValidite.intValue();
		}

		if (this.delivrance != null && this.expiration != null) {
			Long nbJoursValidite = getNbJoursValidite();
			this.dureeValidite = nbJoursValidite.intValue();
		}

		return dureeValidite;
	}

	public void setDureeValidite(Integer dureeValidite) {
		this.dureeValidite = dureeValidite;
	}

	public void toggleModal(ActionEvent event) {
		if (newFile) {
			if (!fileList.isEmpty() && fileList.get(0).getFile().exists()) {
				fileList.clear();
			}
			newFile = false;
		}
		modalRendered = !modalRendered;
	}

	public boolean isModalRendered() {
		return modalRendered;
	}

	public void setModalRendered(boolean modalRendered) {
		this.modalRendered = modalRendered;
	}

	public ArrayList<SelectItem> getTypeHabilitationList() {
		return typeHabilitationList;
	}

	public void saveOrUpdateSalarieHabilitation() throws Exception {
		newFile = false;
		modalRenderedAbs = false;

		if (habilitationBeanSave != null) {

			this.id = habilitationBeanSave.getId();
			this.delivrance = habilitationBeanSave.getDelivrance();
			this.expiration = habilitationBeanSave.getExpiration();
			this.idTypeHabilitationSelected = habilitationBeanSave
					.getIdTypeHabilitationSelected();
			this.commentaire = habilitationBeanSave.getCommentaire();
			this.dureeValidite = habilitationBeanSave.getDureeValidite();
			this.justificatif = habilitationBeanSave.getJustificatif();

			habilitationBeanSave = null;
		}

		if (this.idTypeHabilitationSelected != -1 && this.delivrance != null
				&& this.expiration != null) {

			// Teste si date fin > date debut
			Calendar debut = new GregorianCalendar();
			Calendar fin = new GregorianCalendar();
			debut.setTime(this.delivrance);
			fin.setTime(this.expiration);
			if (debut.after(fin)) {
				// Erreur

				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"La date d'expiration est ant\u00E9rieure \u00E0 celle de d\u00E9livrance",
						"La date d'expiration est ant\u00E9rieure \u00E0 celle de d\u00E9livrance");
				FacesContext
						.getCurrentInstance()
						.addMessage(
								"idSalarieForm:idSalarieTabSet:0:dateExpirationHabilitation",
								message);
				fail = true;

			} else {

				HabilitationBean habilitationBean = new HabilitationBean();

				List<ParcoursBean> listParcoursSalarie = salarieFormBB
						.getParcoursBeanList();

				if (this.id == 0) {
					this.id = -1;
				}
				habilitationBean.setId(this.id);
				habilitationBean.setDelivrance(this.delivrance);

				habilitationBean.setExpiration(this.expiration);
				habilitationBean
						.setIdTypeHabilitationSelected(this.idTypeHabilitationSelected);
				habilitationBean.setCommentaire(this.commentaire);
				if (!fileList.isEmpty())
					habilitationBean.setJustificatif(fileList.get(0).getFile()
							.getName());

				habilitationBean.setDureeValidite(this.dureeValidite);

				TypeHabilitationServiceImpl typeHabilitationService = new TypeHabilitationServiceImpl();
				String nomTypeHabilitation = new String();

				try {
					nomTypeHabilitation = typeHabilitationService
							.getTypeHabilitationBeanById(
									this.idTypeHabilitationSelected).getNom();
				} catch (Exception e) {
					e.printStackTrace();
				}

				habilitationBean.setNomTypeHabilitation(nomTypeHabilitation);

				if (isModif == true) {
					salarieFormBB.getHabilitationBeanList().set(
							indexSelectedRow, habilitationBean);
					isModif = false;
				} else {

					salarieFormBB.getHabilitationBeanList().add(
							habilitationBean);
				}
				modalRendered = !modalRendered;
				salarieFormBB.saveOrUpdateSalarie();
			}
		} else {
			fail = true;
			if (this.idTypeHabilitationSelected == -1) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:typeHabilitationList",
						message);
			}
			if (this.delivrance == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext
						.getCurrentInstance()
						.addMessage(
								"idSalarieForm:idSalarieTabSet:0:dateDelivranceHabilitation",
								message);
			}
			if (this.expiration == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext
						.getCurrentInstance()
						.addMessage(
								"idSalarieForm:idSalarieTabSet:0:dateExpirationHabilitation",
								message);
			}
		}
	}

	public int getIdTypeHabilitationSelected() {
		return idTypeHabilitationSelected;
	}

	public void setIdTypeHabilitationSelected(int idTypeHabilitationSelected) {
		this.idTypeHabilitationSelected = idTypeHabilitationSelected;
	}

	public void modifHabilitation(ActionEvent evt) throws Exception {

		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		HabilitationBean habilitationBean = (HabilitationBean) table
				.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		this.commentaire = habilitationBean.getCommentaire();
		this.delivrance = habilitationBean.getDelivrance();
		this.expiration = habilitationBean.getExpiration();
		this.id = habilitationBean.getId();
		this.idTypeHabilitationSelected = habilitationBean
				.getIdTypeHabilitationSelected();
		this.fileProgress = 0;

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		fileList.clear();
		if ((habilitationBean.getJustificatif() != null)
				&& (!habilitationBean.getJustificatif().equals(""))) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(habilitationBean.getJustificatif())
					.getName());
			fileInfo.setFile(new File(habilitationBean.getJustificatif()));

			fileList.clear();
			fileList.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("habilitation")));

		}

		isModif = true;

		modalRendered = !modalRendered;

	}

	private Long getNbJoursValidite() {

		Long diffMillis;
		Long diffenjours = null;
		GregorianCalendar dateExpiration = new GregorianCalendar();
		GregorianCalendar dateDuJour = new GregorianCalendar();
		GregorianCalendar dateDelivrance = new GregorianCalendar();

		dateExpiration.setTime(this.expiration);
		dateDelivrance.setTime(this.delivrance);

		if (dateDelivrance.before(dateDuJour)) {
			diffMillis = dateExpiration.getTimeInMillis()
					- dateDuJour.getTimeInMillis();
			if (!dateExpiration.equals(dateDuJour)) {
				diffenjours = (diffMillis / (24 * 60 * 60 * 1000)) + 1;
			} else {
				diffenjours = diffMillis / (24 * 60 * 60 * 1000);
			}
		} else {
			diffMillis = dateExpiration.getTimeInMillis()
					- dateDelivrance.getTimeInMillis();

			diffenjours = diffMillis / (24 * 60 * 60 * 1000);
		}
		return diffenjours;
	}

	public void deleteHabilitation(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		HabilitationBean habilitationBean = (HabilitationBean) table
				.getRowData();
		// On récupère aussi son index
		int index = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		salarieFormBB.getHabilitationBeanList().remove(index);

		if (habilitationBean.getId() > 0) {
			HabilitationServiceImpl habilitationService = new HabilitationServiceImpl();
			habilitationService.delete(habilitationBean);
		}
		this.fileList.clear();
	}

	public void download(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		HabilitationBean habilitationBean;
		InputFileData fileData;
		String fileName = "";
		String filePath = "";
		if (add || isModif) {
			fileData = (InputFileData) table.getRowData();
			fileName = fileData.getFile().getName();
			filePath = fileData.getPath();
		} else {
			habilitationBean = (HabilitationBean) table.getRowData();
			fileName = habilitationBean.getJustif().getName();
			filePath = habilitationBean.getJustificatif();
		}
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		filePath = Utils.getSessionFileUploadPath(session, getIdSalarie(),
				"habilitation", 0, false, false, salarieFormBB.getNomGroupe())
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

	private HtmlDataTable getParentDatatable(UIComponent compo) {
		if (compo == null) {
			return null;
		}
		if (compo instanceof HtmlDataTable) {
			return (HtmlDataTable) compo;
		}
		return getParentDatatable(compo.getParent());
	}

	public List<InputFileData> getFileList() {
		if (!fileList.isEmpty()
				&& (!fileList.get(0).getFile().exists()
						|| !fileList.get(0).getFile().isFile() || !fileList
						.get(0).getFile().canRead()))
			fileError = true;
		else
			fileError = false;
		return fileList;
	}

	public int getFileProgress() {
		return fileProgress;
	}

	// 1.8
	public void uploadFile(ActionEvent event) throws Exception {
		InputFile inputFile = (InputFile) event.getSource();

		FileInfo fileInfo = inputFile.getFileInfo();

		if (fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.",
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:inputFileName", message);
			return;
		}

		if (fileInfo.getStatus() == FileInfo.SAVED) {

			fileList.clear();
			fileList.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("habilitation")));
			if (isModif)
				newFile = true;
		}
	}

	public void fileUploadProgress(EventObject event) {

		InputFile ifile = (InputFile) event.getSource();

		fileProgress = ifile.getFileInfo().getPercent();
	}

	public void remove(ActionEvent evt) {
		modalRenderedDelFile = true;
	}

	public void cancelRemove(ActionEvent evt) {
		modalRenderedDelFile = false;
	}

	public void removeUploadedFile(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		HabilitationBean habilitationBean = (HabilitationBean) table
				.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		fileList.clear();

		habilitationBean.setJustificatif(null);
		HabilitationServiceImpl serv = new HabilitationServiceImpl();
		serv.saveOrUppdate(habilitationBean);

		init = true;
	}

	public void removeUploadedFileTemp(ActionEvent event) {
		modalRenderedDelFile = false;
		fileList.clear();
		this.fileProgress = 0;
		newFile = false;
	}

	public boolean isDureeValiditePositif() {
		return dureeValiditePositif;
	}

	public void setDureeValiditePositif(boolean dureeValiditePositif) {
		this.dureeValiditePositif = dureeValiditePositif;
	}

	private int getNombreJourRupture(List<ParcoursBean> listParcoursSalarie,
			Date debAbsence, Date finAbsence) {
		int nbrJour = 0;
		int ParcoursDeb = -1;
		int ParcoursFin = -1;
		int j = 0;
		int k = 0;
		Collections.sort(listParcoursSalarie);
		GregorianCalendar debAbs = new GregorianCalendar();
		debAbs.setTime(debAbsence);
		GregorianCalendar finAbs = new GregorianCalendar();
		finAbs.setTime(finAbsence);
		// On détermine dans un premier temps dans quel postes se situent la
		// date de début et la date de fin
		for (ParcoursBean parcoursBean : listParcoursSalarie) {
			GregorianCalendar debParcours = new GregorianCalendar();
			debParcours.setTime(parcoursBean.getDebutFonction());
			if (parcoursBean.getFinFonction() != null) {
				GregorianCalendar finParcours = new GregorianCalendar();
				finParcours.setTime(parcoursBean.getFinFonction());
				if (Utils.before(debAbs, finParcours)
						&& Utils.before(debParcours, debAbs)) {
					if (Utils.before(finAbs, finParcours)
							&& Utils.before(debParcours, finAbs)) {
						// Absence dans un meme poste
						return 0;
					} else {
						ParcoursDeb = j;
					}
				}
				if (Utils.before(finAbs, finParcours)
						&& Utils.before(debParcours, finAbs)) {
					ParcoursFin = k;
				}
			} else {
				if (ParcoursDeb >= 0)
					ParcoursFin = k;
				else
					return 0;
			}
			j++;
			k++;
		}

		// On récupère ensuite le nombre de jour de rupture entre ces postes
		for (int i = ParcoursDeb; i < ParcoursFin; i++) {
			{
				ParcoursBean parcoursBeanPrecedent = listParcoursSalarie.get(i);
				ParcoursBean parcoursBeanActuel = listParcoursSalarie
						.get(i + 1);
				if (parcoursBeanPrecedent.getFinFonction() != null)
					if (parcoursBeanActuel.getDebutFonction().compareTo(
							parcoursBeanPrecedent.getFinFonction()) > 0) {
						GregorianCalendar debParcoursActuel = new GregorianCalendar();
						debParcoursActuel.setTime(parcoursBeanActuel
								.getDebutFonction());
						GregorianCalendar finParcoursPrecedent = new GregorianCalendar();
						finParcoursPrecedent.setTime(parcoursBeanPrecedent
								.getFinFonction());
						long nbjJourMiliiSec = debParcoursActuel
								.getTimeInMillis()
								- finParcoursPrecedent.getTimeInMillis();
						nbrJour += nbjJourMiliiSec / 86400000;
					}
			}
		}
		return nbrJour;
	}

	public void getRupture() throws Exception {

		modalRenderedAbs = !modalRenderedAbs;
		saveOrUpdateSalarieHabilitation();
		if (!fail) {
			add = false;
			isModif = false;
			init = true;
		} else
			fail = false;
	}

	public boolean isModalRenderedAbs() {
		return modalRenderedAbs;
	}

	public void setModalRenderedAbs(boolean modalRenderedAbs) {
		this.modalRenderedAbs = modalRenderedAbs;
	}

	public void toggleModalRupture(ActionEvent event) {
		modalRenderedAbs = !modalRenderedAbs;
	}

	public int getIndexSelectedRow() {
		return indexSelectedRow;
	}

	public void setIndexSelectedRow(int indexSelectedRow) {
		this.indexSelectedRow = indexSelectedRow;
	}

	public boolean isFileError() {
		return fileError;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public void setTypeHabilitationList(
			ArrayList<SelectItem> typeHabilitationList) {
		this.typeHabilitationList = typeHabilitationList;
	}

	public void setFileProgress(int fileProgress) {
		this.fileProgress = fileProgress;
	}

	public String getUrl() throws Exception {
		return salarieFormBB.getUrl("habilitation");
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

	public boolean isModif() {
		return isModif;
	}

	public void setModif(boolean isModif) {
		this.isModif = isModif;
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

	public void setIdSalarie(int idSalarie) {
		this.idSalarie = idSalarie;
	}

}
