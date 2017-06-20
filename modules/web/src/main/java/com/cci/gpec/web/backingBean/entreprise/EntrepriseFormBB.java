package com.cci.gpec.web.backingBean.entreprise;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.BackingBeanForm;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.effects.JavascriptContext;

public class EntrepriseFormBB extends BackingBeanForm {

	private String codeApe;
	private Long numSiret;
	private int idEntreprise = 0;
	private String cciRattachement;
	private Date dateCreation;
	private List<SelectItem> selectServiceItems;

	private boolean suiviFormations = false;
	private boolean suiviAccidents = false;
	private boolean suiviAbsences = false;
	private boolean suiviCompetences = false;
	private boolean suiviDIF = false;

	private Integer DIFMax = 126;

	private boolean modalRenderedDelFile = false;
	private int indexSelectedRow = -1;

	private List<File> deletedFiles = new ArrayList<File>();

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");

	private List<InputFileData> fileListEntrepriseTemp = new ArrayList<InputFileData>();

	private boolean fileError = false;

	private int fileProgressEntreprise = 0;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public boolean isModalRenderedDelFile() {
		return modalRenderedDelFile;
	}

	public void setModalRenderedDelFile(boolean modalRenderedDelFile) {
		this.modalRenderedDelFile = modalRenderedDelFile;
	}

	public int getIndexSelectedRow() {
		return indexSelectedRow;
	}

	public void setIndexSelectedRow(int indexSelectedRow) {
		this.indexSelectedRow = indexSelectedRow;
	}

	public List<File> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<File> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public boolean isFileError() {
		return fileError;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public int getFileProgressEntreprise() {
		return fileProgressEntreprise;
	}

	public void setFileProgressEntreprise(int fileProgressEntreprise) {
		this.fileProgressEntreprise = fileProgressEntreprise;
	}

	public void setFileListEntrepriseTemp(
			List<InputFileData> fileListEntrepriseTemp) {
		this.fileListEntrepriseTemp = fileListEntrepriseTemp;
	}

	public EntrepriseFormBB() {
		super();
	}

	public EntrepriseFormBB(int id, String nom) {
		super(id, nom);
	}

	public String init() throws IOException, Exception {
		idEntreprise = 0;
		super.nom = "";
		codeApe = "";
		numSiret = null;
		cciRattachement = "";
		dateCreation = null;
		DIFMax = 126;

		suiviAbsences = false;
		suiviAccidents = false;
		suiviCompetences = false;
		suiviDIF = false;
		suiviFormations = false;

		this.fileProgressEntreprise = 0;
		this.fileListEntrepriseTemp.clear();

		EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
		if (idEntreprise != 0 && serv.getJustificatif(idEntreprise) != null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);

			String justif = Utils.getSessionFileUploadPath(session,
					idEntreprise, "logo_entreprise", 0, false, false,
					salarieFormBB.getNomGroupe())
					+ serv.getJustificatif(idEntreprise);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(justif).getName());
			fileInfo.setFile(new File(justif));

			fileListEntrepriseTemp.clear();
			fileListEntrepriseTemp.add(new InputFileData(fileInfo, Utils
					.getFileUrl(this.idEntreprise, "logo_entreprise", false,
							false, true, false, salarieFormBB.getNomGroupe())));
		}

		return "addEntrepriseForm";
	}

	public void download(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EntrepriseBean entrepriseBean = (EntrepriseBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier",
				entrepriseBean.getJustif().getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(entrepriseBean
								.getJustificatif()).toPath())
						+ " \",\"_Download\");");

	}

	public void downloadFromForm(ActionEvent evt) throws Exception {

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier",
				fileListEntrepriseTemp.get(0).getFile().getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(fileListEntrepriseTemp.get(0)
								.getFile().toPath()) + " \",\"_Download\");");

	}

	public void uploadFileEntreprise(ActionEvent event) throws Exception {

		InputFile inputFile = (InputFile) event.getSource();

		if (inputFile.getFileInfo().getFileName().toUpperCase().endsWith("JPG")
				|| inputFile.getFileInfo().getFileName().toUpperCase()
						.endsWith("JPEG")
				|| inputFile.getFileInfo().getFileName().toUpperCase()
						.endsWith("PNG")) {
			FileInfo fileInfo = inputFile.getFileInfo();

			if (fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.",
						"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.");
				FacesContext.getCurrentInstance().addMessage(
						"formulaire:inputFileNameEntreprise", message);
				return;
			}

			if (fileInfo.getStatus() == FileInfo.SAVED) {

				FacesContext facesContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) facesContext
						.getExternalContext().getSession(false);

				String filePath = Utils.getSessionFileUploadPath(session,
						this.idEntreprise, "logo_entreprise", 1, false, true,
						salarieFormBB.getNomGroupe())
						+ fileInfo.getFileName();
				String newName = this.getNom().replace(" ", "_");
				String newPath = Utils.getSessionFileUploadPath(session,
						this.idEntreprise, "logo_entreprise", 1, false, true,
						salarieFormBB.getNomGroupe())
						+ "logo_"
						+ newName
						+ fileInfo
								.getFile()
								.getAbsolutePath()
								.substring(
										fileInfo.getFile().getAbsolutePath()
												.lastIndexOf("."))
								.toLowerCase();

				Utils.copyFile(filePath, newPath);

				fileInfo.setFile(new File(newPath));
				fileInfo.setFileName(new File(newPath).getName());

				fileListEntrepriseTemp.clear();
				fileListEntrepriseTemp.add(new InputFileData(fileInfo, Utils
						.getFileUrl(this.idEntreprise, "logo_entreprise",
								false, false, true, false,
								salarieFormBB.getNomGroupe())));

			}
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le fichier doit etre une image !",
					"Le fichier doit etre une image !");
			FacesContext.getCurrentInstance().addMessage(
					"formulaire:inputFileNameEntreprise", message);
		}
	}

	public void fileUploadProgressEntreprise(EventObject event) {

		InputFile ifile = (InputFile) event.getSource();

		fileProgressEntreprise = ifile.getFileInfo().getPercent();
	}

	public void removeUploadedFileEntreprise(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EntrepriseBean entrepriseBean = (EntrepriseBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		// if (new File(entrepriseBean.getJustificatif()).exists())
		// new File(entrepriseBean.getJustificatif()).delete();

		entrepriseBean.setJustificatif(null);
		EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
		serv.saveOrUppdate(entrepriseBean,
				Integer.parseInt(session.getAttribute("groupe").toString()));
		this.id = entrepriseBean.getId();

	}

	public void remove(ActionEvent event) {
		modalRenderedDelFile = true;
	}

	public void removeUploadedFileEntrepriseTemp(ActionEvent event) {
		modalRenderedDelFile = false;
		if (fileListEntrepriseTemp.get(0).getFile().exists())
			deletedFiles.add(fileListEntrepriseTemp.get(0).getFile());

		fileListEntrepriseTemp.clear();
		this.fileProgressEntreprise = 0;
	}

	public void cancelRemove(ActionEvent evt) {
		modalRenderedDelFile = false;
	}

	public List<InputFileData> getFileListEntrepriseTemp() {
		if (!fileListEntrepriseTemp.isEmpty()
				&& (!fileListEntrepriseTemp.get(0).getFile().exists()
						|| !fileListEntrepriseTemp.get(0).getFile().isFile() || !fileListEntrepriseTemp
						.get(0).getFile().canRead()))
			fileError = true;
		else
			fileError = false;
		return fileListEntrepriseTemp;
	}

	public String modifierAction() {
		return "addEntrepriseForm";
	}

	public void modifierActionListener(ActionEvent evt) throws IOException,
			Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		EntrepriseBean entrepriseBean = (EntrepriseBean) table.getRowData();

		this.idEntreprise = entrepriseBean.getId();
		this.nom = entrepriseBean.getNom();
		this.cciRattachement = entrepriseBean.getCciRattachement();
		this.codeApe = entrepriseBean.getCodeApe();
		this.dateCreation = entrepriseBean.getDateCreation();
		this.numSiret = entrepriseBean.getNumSiret();
		this.suiviAbsences = entrepriseBean.isSuiviAbsences();
		this.suiviAccidents = entrepriseBean.isSuiviAccidents();
		this.suiviCompetences = entrepriseBean.isSuiviCompetences();
		this.suiviDIF = entrepriseBean.isSuiviDIF();
		this.suiviFormations = entrepriseBean.isSuiviFormations();
		if (entrepriseBean.getDIFMax() == null)
			this.DIFMax = 126;
		else
			this.DIFMax = entrepriseBean.getDIFMax();
		fileListEntrepriseTemp.clear();
		if (entrepriseBean.getJustificatif() != null) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(entrepriseBean.getJustificatif())
					.getName());
			fileInfo.setFile(new File(entrepriseBean.getJustificatif()));

			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);

			fileListEntrepriseTemp.clear();
			fileListEntrepriseTemp.add(new InputFileData(fileInfo, Utils
					.getFileUrl(this.idEntreprise, "logo_entreprise", false,
							false, true, false, salarieFormBB.getNomGroupe())));
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

	public String saveOrUpdateEntreprise() {

		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
		EntrepriseBean entrepriseBean = new EntrepriseBean();
		entrepriseBean.setId(idEntreprise);
		entrepriseBean.setNom(super.nom);
		entrepriseBean.setNumSiret(numSiret);
		entrepriseBean.setCodeApe(codeApe);
		entrepriseBean.setCciRattachement(cciRattachement);
		entrepriseBean.setDateCreation(dateCreation);
		entrepriseBean.setSuiviFormations(suiviFormations);
		entrepriseBean.setSuiviAccidents(suiviAccidents);
		entrepriseBean.setSuiviAbsences(suiviAbsences);
		entrepriseBean.setSuiviCompetences(suiviCompetences);
		entrepriseBean.setSuiviDIF(suiviDIF);
		entrepriseBean.setDIFMax(DIFMax);
		if (!fileListEntrepriseTemp.isEmpty())
			entrepriseBean.setJustificatif(fileListEntrepriseTemp.get(0)
					.getFile().getName());
		else
			entrepriseBean.setJustificatif(null);
		entrepriseService.saveOrUppdate(entrepriseBean,
				Integer.parseInt(session.getAttribute("groupe").toString()));

		return "addEntrepriseForm";
	}

	public void supprimerEntreprise() throws Exception {

		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
		EntrepriseBean entrepriseBean = new EntrepriseBean();
		entrepriseBean.setId(super.id);
		boolean b = entrepriseService.supprimer(entrepriseBean);
		if (!b) {
			ResourceBundle rb = ResourceBundle.getBundle("errors");
			FacesMessage message = new FacesMessage(
					rb.getString("suppressionEntreprise"));
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"idForm:dataTable:idSupprimer", message);

		}
		this.fileListEntrepriseTemp.clear();
	}

	public String saveOrUpdateEntrepriseFin() {

		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
		EntrepriseBean entrepriseBean = new EntrepriseBean();
		entrepriseBean.setId(idEntreprise);
		entrepriseBean.setNom(super.nom);
		entrepriseBean.setNumSiret(numSiret);
		entrepriseBean.setCodeApe(codeApe);
		entrepriseBean.setCciRattachement(cciRattachement);
		entrepriseBean.setDateCreation(dateCreation);
		entrepriseBean.setSuiviFormations(suiviFormations);
		entrepriseBean.setSuiviAccidents(suiviAccidents);
		entrepriseBean.setSuiviAbsences(suiviAbsences);
		entrepriseBean.setSuiviCompetences(suiviCompetences);
		entrepriseBean.setSuiviDIF(suiviDIF);
		entrepriseBean.setDIFMax(DIFMax);

		if (!fileListEntrepriseTemp.isEmpty())
			entrepriseBean.setJustificatif(fileListEntrepriseTemp.get(0)
					.getFile().getName());
		else
			entrepriseBean.setJustificatif(null);

		entrepriseService.saveOrUppdate(entrepriseBean,
				Integer.parseInt(session.getAttribute("groupe").toString()));

		return "saveOrUpdateEntreprise";
	}

	public String annuler() {
		this.fileProgressEntreprise = 0;
		return "saveOrUpdateEntreprise";
	}

	public String getUrl() throws Exception {
		return Utils.getFileUrl(this.idEntreprise, "logo_entreprise", false,
				false, true, false, salarieFormBB.getNomGroupe());
	}

	public String getCodeApe() {
		return codeApe;
	}

	public void setCodeApe(String codeApe) {
		this.codeApe = codeApe;
	}

	public Long getNumSiret() {
		return numSiret;
	}

	public void setNumSiret(Long numSiret) {
		this.numSiret = numSiret;
	}

	public List<SelectItem> getSelectServiceItems() throws Exception {
		ServiceImpl service = new ServiceImpl();
		List<ServiceBean> serviceList = service.getServicesList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		selectServiceItems = new ArrayList<SelectItem>();
		for (ServiceBean sb : serviceList) {
			selectServiceItems.add(new SelectItem(sb.getId(), sb.getNom()));
		}
		return selectServiceItems;
	}

	public void setSelectServiceItems(List<SelectItem> selectItems) {
		this.selectServiceItems = selectItems;
	}

	public int getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(int idEntreprise) {
		this.idEntreprise = idEntreprise;
	}

	public String getCciRattachement() {
		return cciRattachement;
	}

	public void setCciRattachement(String cciRattachement) {
		this.cciRattachement = cciRattachement;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public boolean isSuiviFormations() {
		return suiviFormations;
	}

	public void setSuiviFormations(boolean suiviFormations) {
		this.suiviFormations = suiviFormations;
	}

	public boolean isSuiviAccidents() {
		return suiviAccidents;
	}

	public void setSuiviAccidents(boolean suiviAccidents) {
		this.suiviAccidents = suiviAccidents;
	}

	public boolean isSuiviAbsences() {
		return suiviAbsences;
	}

	public void setSuiviAbsences(boolean suiviAbsences) {
		this.suiviAbsences = suiviAbsences;
	}

	public boolean isSuiviCompetences() {
		return suiviCompetences;
	}

	public void setSuiviCompetences(boolean suiviCompetences) {
		this.suiviCompetences = suiviCompetences;
	}

	public boolean isSuiviDIF() {
		return suiviDIF;
	}

	public void setSuiviDIF(boolean suiviDIF) {
		this.suiviDIF = suiviDIF;
	}

	public Integer getDIFMax() {
		return DIFMax;
	}

	public void setDIFMax(Integer max) {
		DIFMax = max;
	}

}
