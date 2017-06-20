package com.cci.gpec.web.backingBean.parcours;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.StatutBean;
import com.cci.gpec.commons.TypeContratBean;
import com.cci.gpec.commons.TypeRecoursInterimBean;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.ParcoursServiceImpl;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.cci.gpec.metier.implementation.TypeContratServiceImpl;
import com.cci.gpec.metier.implementation.TypeRecoursInterimServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.effects.JavascriptContext;

public class SalarieParcoursFormBB {

	// primary key
	private int id;
	private int idSalarie;

	// fields
	private Date debutFonction;
	private Date finFonction;

	private Integer equivalenceTempsPlein;
	private String echelon;
	private String niveau;
	private String coefficient;
	private boolean modalRendered = false;
	private boolean modalRenderedDelFile = false;

	private ArrayList<SelectItem> metierList;
	private int idMetierSelected;

	private ArrayList<SelectItem> typeStatutList;
	private int idTypeStatutSelected;

	private ArrayList<SelectItem> typeContratList;
	private int idTypeContratSelected;

	private ArrayList<SelectItem> typeRecoursList;
	private Integer idRecoursSelected;

	private boolean displayRecoursList = false;

	private int indexSelectedRow = -1;
	private boolean isModif = false;
	private boolean add = false;
	private boolean init = true;
	private boolean newFile = false;

	private List<File> deletedFiles = new ArrayList<File>();

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");
	private List<InputFileData> fileListParcoursTemp = new ArrayList<InputFileData>();

	private int fileProgressParcours = 0;

	private boolean fileError = false;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public SalarieParcoursFormBB() {
	}

	public void init() throws Exception {

		this.fileProgressParcours = 0;
		this.fileListParcoursTemp.clear();

		MetierServiceImpl metierService = new MetierServiceImpl();
		StatutServiceImpl statutService = new StatutServiceImpl();
		TypeContratServiceImpl typeContratService = new TypeContratServiceImpl();
		TypeRecoursInterimServiceImpl typeRecoursService = new TypeRecoursInterimServiceImpl();

		List<MetierBean> metierBeanList = metierService.getMetiersList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		List<StatutBean> statutBeanList = statutService.getStatutsList();
		List<TypeContratBean> typeContratBeanList = typeContratService
				.getTypeContratList();
		List<TypeRecoursInterimBean> typeRecoursBeanList = typeRecoursService
				.getTypeRecoursInterimList();

		metierList = new ArrayList<SelectItem>();
		typeStatutList = new ArrayList<SelectItem>();
		typeContratList = new ArrayList<SelectItem>();
		typeRecoursList = new ArrayList<SelectItem>();
		SelectItem selectItem;

		Collections.sort(metierBeanList);
		for (MetierBean metierBean : metierBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(metierBean.getId());
			selectItem.setLabel(metierBean.getNom());
			metierList.add(selectItem);
		}

		for (StatutBean statutBean : statutBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(statutBean.getId());
			selectItem.setLabel(statutBean.getNom());
			typeStatutList.add(selectItem);
		}

		for (TypeContratBean typeContratBean : typeContratBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(typeContratBean.getId());
			selectItem.setLabel(typeContratBean.getNom());
			typeContratList.add(selectItem);
		}

		for (TypeRecoursInterimBean typeRecoursBean : typeRecoursBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(typeRecoursBean.getId());
			selectItem.setLabel(typeRecoursBean.getNom());
			typeRecoursList.add(selectItem);
		}

		this.id = 0;
		this.debutFonction = null;
		this.finFonction = null;
		this.equivalenceTempsPlein = null;
		this.coefficient = "";
		this.niveau = "";
		this.echelon = "";

		this.idMetierSelected = 0;
		this.idTypeStatutSelected = 0;
		this.idTypeContratSelected = 0;
		this.idRecoursSelected = 0;
		this.indexSelectedRow = -1;
		this.isModif = false;
	}

	public void initSalarieParcoursForm() throws Exception {
		init();

		ParcoursServiceImpl serv = new ParcoursServiceImpl();
		if (id != 0 && serv.getJustificatif(id) != null) {

			String justif = Utils.getSessionFileUploadPath(session,
					salarieFormBB.getId(), "parcours", 0, false, false,
					salarieFormBB.getNomGroupe())
					+ serv.getJustificatif(id);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(justif).getName());
			fileInfo.setFile(new File(justif));

			fileListParcoursTemp.clear();
			fileListParcoursTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("parcours")));
		}
		add = true;
		modalRendered = !modalRendered;
	}

	public void uploadFileParcours(ActionEvent event) throws Exception {

		InputFile inputFile = (InputFile) event.getSource();

		FileInfo fileInfo = inputFile.getFileInfo();

		if (fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.",
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:inputFileNameParcours",
					message);
			return;
		}

		if (fileInfo.getStatus() == FileInfo.SAVED) {

			fileListParcoursTemp.clear();
			fileListParcoursTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("parcours")));
			if (isModif) {
				newFile = true;
			}
		}
	}

	public void download(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ParcoursBean parcoursBean;
		InputFileData fileData;
		String fileName = "";
		String filePath = "";
		if (add || isModif) {
			fileData = (InputFileData) table.getRowData();
			fileName = fileData.getFile().getName();
			filePath = fileData.getPath();
		} else {
			parcoursBean = (ParcoursBean) table.getRowData();
			fileName = parcoursBean.getJustif().getName();
			filePath = parcoursBean.getJustificatif();
		}
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		filePath = Utils.getSessionFileUploadPath(session, getIdSalarie(),
				"parcours", 0, false, false, salarieFormBB.getNomGroupe())
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

	public static void copyFile(final String currentFile, final String newFile)
			throws FileNotFoundException, IOException {
		FileInputStream in = new FileInputStream(currentFile);
		try {
			FileOutputStream out = new FileOutputStream(newFile);
			byte[] buffer = new byte[1024 * 4];
			// ca peut être bien de rendre la taille du buffer paramétrable
			int nbRead;
			try {
				while ((nbRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, nbRead);
				}
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}

	public void fileUploadProgressParcours(EventObject event) {

		InputFile ifile = (InputFile) event.getSource();

		fileProgressParcours = ifile.getFileInfo().getPercent();
	}

	public void remove(ActionEvent evt) {
		modalRenderedDelFile = true;
	}

	public void cancelRemove(ActionEvent evt) {
		modalRenderedDelFile = false;
	}

	public void removeUploadedFileParcours(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ParcoursBean parcoursBean = (ParcoursBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();

		parcoursBean.setJustificatif(null);
		ParcoursServiceImpl serv = new ParcoursServiceImpl();
		serv.saveOrUppdate(parcoursBean);
		this.id = parcoursBean.getId();

		init = true;
	}

	public void removeUploadedFileParcoursTemp(ActionEvent event) {
		modalRenderedDelFile = false;
		if (fileListParcoursTemp.get(0).getFile().exists()) {
			deletedFiles.add(fileListParcoursTemp.get(0).getFile());
		}
		fileListParcoursTemp.clear();
		this.fileProgressParcours = 0;
		newFile = false;
	}

	public int getFileProgressParcours() {
		return fileProgressParcours;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDebutFonction() {
		return debutFonction;
	}

	public void setDebutFonction(Date debutFonction) {
		this.debutFonction = debutFonction;
	}

	public Date getFinFonction() {
		return finFonction;
	}

	public void setFinFonction(Date finFonction) {
		this.finFonction = finFonction;
	}

	public Integer getEquivalenceTempsPlein() {
		return equivalenceTempsPlein;
	}

	public void setEquivalenceTempsPlein(Integer equivalenceTempsPlein) {
		this.equivalenceTempsPlein = equivalenceTempsPlein;
	}

	public boolean isModalRendered() {
		return modalRendered;
	}

	public void setModalRendered(boolean modalRendered) {
		this.modalRendered = modalRendered;
	}

	public void toggleModal(ActionEvent event) {
		if (newFile) {
			if (!fileListParcoursTemp.isEmpty()
					&& fileListParcoursTemp.get(0).getFile().exists()) {
				fileListParcoursTemp.clear();
			}
			newFile = false;
		}
		deletedFiles.clear();
		modalRendered = !modalRendered;
	}

	public int getIdMetierSelected() {
		return idMetierSelected;
	}

	public void setIdMetierSelected(int idMetierSelected) {
		this.idMetierSelected = idMetierSelected;
	}

	public int getIdTypeStatutSelected() {
		return idTypeStatutSelected;
	}

	public void setIdTypeStatutSelected(int idTypeStatutSelected) {
		this.idTypeStatutSelected = idTypeStatutSelected;
	}

	public int getIdTypeContratSelected() {
		return idTypeContratSelected;
	}

	public void setIdTypeContratSelected(int idTypeContratSelected) {
		this.idTypeContratSelected = idTypeContratSelected;
	}

	public ArrayList<SelectItem> getMetierList() {
		return metierList;
	}

	public ArrayList<SelectItem> getTypeStatutList() {
		return typeStatutList;
	}

	public ArrayList<SelectItem> getTypeContratList() {
		return typeContratList;
	}

	public void saveOrUpdateSalarieParcours(ActionEvent event) throws Exception {

		if (this.idMetierSelected != -1
				&& this.idTypeStatutSelected != -1
				&& this.idTypeContratSelected != -1
				&& this.equivalenceTempsPlein != null
				&& this.debutFonction != null
				&& ((this.displayRecoursList && this.idRecoursSelected != -1) || !this.displayRecoursList)) {

			newFile = false;
			boolean isAnterieur = false;
			if (this.finFonction != null) {
				// Teste si date fin > date debut
				Calendar debut = new GregorianCalendar();
				Calendar fin = new GregorianCalendar();
				debut.setTime(this.debutFonction);
				fin.setTime(this.finFonction);

				isAnterieur = debut.after(fin);

			}
			boolean isPosteOuvert = false;
			ParcoursServiceImpl ps = new ParcoursServiceImpl();
			for (ParcoursBean p : ps
					.getParcoursBeanListByIdSalarie(salarieFormBB.getId())) {
				Calendar debut = new GregorianCalendar();
				debut.setTime(this.debutFonction);

				Calendar debutP = new GregorianCalendar();
				debutP.setTime(p.getDebutFonction());

				Calendar finP = new GregorianCalendar();
				if (p.getFinFonction() != null) {
					finP.setTime(p.getFinFonction());
				} else {
					finP = null;
				}

				if (this.finFonction != null) {

					Calendar fin = new GregorianCalendar();
					fin.setTime(this.finFonction);

					if (finP == null) {
						if (debut.after(debutP) || fin.after(debutP)) {
							isPosteOuvert = true;
						}

					} else {
						if ((fin.after(debutP) && fin
								.before(p.getFinFonction()))
								|| (debut.after(debutP) && debut.before(finP))
								|| (debut.after(debutP) && fin.before(finP))
								|| (debut.before(debutP) && fin.after(finP))
								|| (debut.equals(debutP) && fin.equals(finP))) {
							isPosteOuvert = true;
						}
					}
				} else {

					if (finP == null) {
						isPosteOuvert = true;
					} else {
						if (debut.before(finP) || debut.before(debutP))
							isPosteOuvert = true;
					}
				}

			}
			if (isAnterieur || (isPosteOuvert && isModif == false)) {
				// Erreur
				if (isAnterieur) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"La date de fin de prise de fonction est ant\u00E9rieure \u00E0 celle de d\u00E9but.",
							"La date de fin de fonction est ant\u00E9rieure \u00E0 celle de d\u00E9but.");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:finFonction",
							message);
				}

				if (isPosteOuvert && isModif == false) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Le salari\u00E9 est d\u00E9ja en poste pour cette p\u00E9riode.",
							"Le salari\u00E9 est d\u00E9ja en poste pour cette p\u00E9riode.");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:finFonction",
							message);
				}
			} else {
				ParcoursBean parcoursBean = new ParcoursBean();

				if (this.id == 0) {
					this.id = -1;
				}
				parcoursBean.setId(this.id);
				parcoursBean.setDebutFonction(this.debutFonction);
				parcoursBean
						.setEquivalenceTempsPlein(this.equivalenceTempsPlein);
				parcoursBean.setFinFonction(this.finFonction);
				parcoursBean.setIdMetierSelected(this.idMetierSelected);
				parcoursBean
						.setIdTypeContratSelected(this.idTypeContratSelected);
				parcoursBean.setIdTypeStatutSelected(this.idTypeStatutSelected);
				parcoursBean
						.setIdTypeRecoursSelected((this.idRecoursSelected != 0) ? this.idRecoursSelected
								: null);
				parcoursBean.setEchelon(this.echelon);
				parcoursBean.setNiveau(this.niveau);
				parcoursBean.setCoefficient(this.coefficient);

				if (!fileListParcoursTemp.isEmpty()) {
					parcoursBean.setJustificatif(fileListParcoursTemp.get(0)
							.getFile().getName());
				}

				MetierServiceImpl metierService = new MetierServiceImpl();
				StatutServiceImpl statutService = new StatutServiceImpl();
				TypeContratServiceImpl typeContratService = new TypeContratServiceImpl();
				TypeRecoursInterimServiceImpl typeRecoursService = new TypeRecoursInterimServiceImpl();

				String nomMetier = new String();
				String nomTypeContrat = new String();
				String nomTypeStatut = new String();
				String nomTypeRecours = new String();

				try {
					nomMetier = metierService.getMetierBeanById(
							this.idMetierSelected).getNom();
					if (this.idTypeContratSelected != 3) {
						nomTypeContrat = typeContratService
								.getTypeContratBeanById(
										this.idTypeContratSelected).getNom();
					} else {
						nomTypeContrat = "CTT";

						nomTypeRecours = typeRecoursService
								.getTypeRecoursInterimBeanById(
										this.idRecoursSelected).getNom();

						parcoursBean.setNomTypeRecours(nomTypeRecours);
					}

					nomTypeStatut = statutService.getStatutBeanById(
							this.idTypeStatutSelected).getNom();
				} catch (Exception e) {
					e.printStackTrace();
				}

				parcoursBean.setNomMetier(nomMetier);
				parcoursBean.setNomTypeContrat(nomTypeContrat);
				parcoursBean.setNomTypeStatut(nomTypeStatut);
				parcoursBean.setIdSalarie(salarieFormBB.getId());

				if (isModif == true) {
					salarieFormBB.getParcoursBeanList().set(indexSelectedRow,
							parcoursBean);
					isModif = false;
				} else {
					ParcoursServiceImpl parServ = new ParcoursServiceImpl();
					parServ.saveOrUppdate(parcoursBean);
					salarieFormBB.getParcoursBeanList().add(parcoursBean);
					Collections.sort(salarieFormBB.getParcoursBeanList());
					Collections.reverse(salarieFormBB.getParcoursBeanList());
				}
				modalRendered = !modalRendered;
				salarieFormBB.saveOrUpdateSalarie();
				add = false;
				isModif = false;
				init = true;
			}
		} else {

			if (this.idMetierSelected == -1) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:metierParcoursList",
						message);
			}
			if (this.idTypeStatutSelected == -1) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:statutParcoursList",
						message);
			}
			if (this.idTypeContratSelected == -1) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:contratParcoursList",
						message);
			}
			if (this.equivalenceTempsPlein == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idEqui", message);
			}
			if (this.debutFonction == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:debutFonction",
						message);
			}
			if (this.displayRecoursList) {
				if (this.idRecoursSelected == -1) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
							"Champ obligatoire");
					FacesContext
							.getCurrentInstance()
							.addMessage(
									"idSalarieForm:idSalarieTabSet:0:recoursParcoursList",
									message);
				}
			}

		}
	}

	public void modifParcours(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ParcoursBean parcoursBean = (ParcoursBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		this.fileProgressParcours = 0;

		this.debutFonction = parcoursBean.getDebutFonction();
		this.equivalenceTempsPlein = parcoursBean.getEquivalenceTempsPlein();
		this.finFonction = parcoursBean.getFinFonction();
		this.id = parcoursBean.getId();
		this.idMetierSelected = parcoursBean.getIdMetierSelected();
		this.idTypeContratSelected = parcoursBean.getIdTypeContratSelected();
		this.idTypeStatutSelected = parcoursBean.getIdTypeStatutSelected();
		this.idRecoursSelected = (parcoursBean.getIdTypeRecoursSelected() != null) ? parcoursBean
				.getIdTypeRecoursSelected() : 0;
		this.echelon = parcoursBean.getEchelon();
		this.niveau = parcoursBean.getNiveau();
		this.coefficient = parcoursBean.getCoefficient();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		fileListParcoursTemp.clear();
		if (parcoursBean.getJustificatif() != null) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(parcoursBean.getJustificatif())
					.getName());
			fileInfo.setFile(new File(parcoursBean.getJustificatif()));

			fileListParcoursTemp.clear();
			fileListParcoursTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("parcours")));
		}

		isModif = true;

		modalRendered = !modalRendered;
	}

	public void deleteParcours(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ParcoursBean parcoursBean = (ParcoursBean) table.getRowData();
		// On récupère aussi son index
		int index = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		salarieFormBB.getParcoursBeanList().remove(index);

		if (parcoursBean.getId() > 0) {
			ParcoursServiceImpl parcoursService = new ParcoursServiceImpl();
			parcoursService.deleteParcours(parcoursBean);
		}
		this.fileListParcoursTemp.clear();
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

	public boolean isFileError() {
		return fileError;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public List getFileListParcoursTemp() {
		if (!fileListParcoursTemp.isEmpty()
				&& (!fileListParcoursTemp.get(0).getFile().exists()
						|| !fileListParcoursTemp.get(0).getFile().isFile() || !fileListParcoursTemp
						.get(0).getFile().canRead())) {
			fileError = true;
		} else {
			fileError = false;
		}
		return fileListParcoursTemp;
	}

	public void setMetierList(ArrayList<SelectItem> metierList) {
		this.metierList = metierList;
	}

	public void setTypeStatutList(ArrayList<SelectItem> typeStatutList) {
		this.typeStatutList = typeStatutList;
	}

	public void setTypeContratList(ArrayList<SelectItem> typeContratList) {
		this.typeContratList = typeContratList;
	}

	public void setFileProgressParcours(int fileProgressParcours) {
		this.fileProgressParcours = fileProgressParcours;
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

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public void setFileListParcoursTemp(List<InputFileData> fileListParcoursTemp) {
		this.fileListParcoursTemp = fileListParcoursTemp;
	}

	public String getUrl() throws Exception {
		return salarieFormBB.getUrl("parcours");
	}

	public int getIdSalarie() {
		return salarieFormBB.getId();
	}

	public String getEchelon() {
		return echelon;
	}

	public boolean isModalRenderedDelFile() {
		return modalRenderedDelFile;
	}

	public void setModalRenderedDelFile(boolean modalRenderedDelFile) {
		this.modalRenderedDelFile = modalRenderedDelFile;
	}

	public void setEchelon(String echelon) {
		this.echelon = echelon;
	}

	public String getNiveau() {
		return niveau;
	}

	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}

	public String getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(String coefficient) {
		this.coefficient = coefficient;
	}

	public ArrayList<SelectItem> getTypeRecoursList() {
		return typeRecoursList;
	}

	public void setTypeRecoursList(ArrayList<SelectItem> typeRecoursList) {
		this.typeRecoursList = typeRecoursList;
	}

	public int getIdRecoursSelected() {
		return (idRecoursSelected != null) ? idRecoursSelected : 0;
	}

	public void setIdRecoursSelected(int idRecoursSelected) {
		this.idRecoursSelected = idRecoursSelected;
	}

	public void displayRecoursInterim(ValueChangeEvent evt) {
		this.idTypeContratSelected = (Integer) evt.getNewValue();
		if (this.idTypeContratSelected == 3) {
			displayRecoursList = true;
		} else {
			displayRecoursList = false;
		}
	}

	public void refreshDate(ValueChangeEvent evt) {
		if (evt.getComponent().getId().equals("debutFonction")) {
			this.debutFonction = (Date) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("finFonction")) {
			this.finFonction = (Date) evt.getNewValue();
		}
	}

	public boolean isDisplayRecoursList() {
		if (this.idTypeContratSelected == 3) {
			displayRecoursList = true;
		} else {
			displayRecoursList = false;
		}
		return displayRecoursList;
	}

	public void setDisplayRecoursList(boolean displayRecoursList) {
		this.displayRecoursList = displayRecoursList;
	}

	public void setIdSalarie(int idSalarie) {
		this.idSalarie = idSalarie;
	}

}
