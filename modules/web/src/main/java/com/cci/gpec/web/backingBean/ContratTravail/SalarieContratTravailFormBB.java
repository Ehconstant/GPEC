package com.cci.gpec.web.backingBean.ContratTravail;

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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.ContratTravailBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.MotifRuptureContratBean;
import com.cci.gpec.commons.TypeContratBean;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.ContratTravailServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.MotifRuptureContratServiceImpl;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.cci.gpec.metier.implementation.TypeContratServiceImpl;
import com.cci.gpec.metier.implementation.TypeRecoursInterimServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.effects.JavascriptContext;

public class SalarieContratTravailFormBB {
	// primary key
	private int id;
	private int idSalarie;

	// fields
	private Date debutContrat;
	private Date finContrat;

	private boolean modalRendered = false;
	private boolean modalRenderedDelFile = false;

	private ArrayList<SelectItem> metierList;
	private int idMetierSelected;

	private ArrayList<SelectItem> typeContratList;
	private int idTypeContratSelected;

	private ArrayList<SelectItem> motifRuptureList;
	private int idMotifRuptureSelected;

	private boolean motifRuptureRequired = false;

	private boolean renouvellementCDD = false;

	private boolean displayRenouvellementCDD = false;

	private int indexSelectedRow = -1;
	private boolean isModif = false;
	private boolean add = false;
	private boolean init = true;
	private boolean newFile = false;

	private List<File> deletedFiles = new ArrayList<File>();

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");
	private List<InputFileData> fileListContratTravailTemp = new ArrayList<InputFileData>();

	private int fileProgressContratTravail = 0;

	private boolean fileError = false;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public SalarieContratTravailFormBB() {
		// init();
	}

	public void init() throws Exception {

		this.fileProgressContratTravail = 0;
		this.fileListContratTravailTemp.clear();

		this.idSalarie = salarieFormBB.getId();

		MetierServiceImpl metierService = new MetierServiceImpl();
		TypeContratServiceImpl typeContratService = new TypeContratServiceImpl();
		MotifRuptureContratServiceImpl motifRuptureService = new MotifRuptureContratServiceImpl();

		List<MetierBean> metierBeanList = metierService.getMetiersList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		List<TypeContratBean> typeContratBeanList = typeContratService
				.getTypeContratList();
		List<MotifRuptureContratBean> motifRuptureContratBeanList = motifRuptureService
				.getMotifRuptureContratList();

		metierList = new ArrayList<SelectItem>();
		typeContratList = new ArrayList<SelectItem>();
		motifRuptureList = new ArrayList<SelectItem>();
		renouvellementCDD = false;

		SelectItem selectItem;

		Collections.sort(metierBeanList);
		for (MetierBean metierBean : metierBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(metierBean.getId());
			selectItem.setLabel(metierBean.getNom());
			metierList.add(selectItem);
		}

		for (TypeContratBean typeContratBean : typeContratBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(typeContratBean.getId());
			selectItem.setLabel(typeContratBean.getNom());
			typeContratList.add(selectItem);
		}

		for (MotifRuptureContratBean motifRuptureContratBean : motifRuptureContratBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(motifRuptureContratBean.getId());
			selectItem.setLabel(motifRuptureContratBean.getNom());
			motifRuptureList.add(selectItem);
		}

		this.id = 0;
		this.debutContrat = null;
		this.finContrat = null;

		this.idMetierSelected = 0;
		this.idTypeContratSelected = 0;
		this.idMotifRuptureSelected = 0;
		this.indexSelectedRow = -1;
		this.isModif = false;

		init = false;
	}

	public void initSalarieContratTravailForm() throws Exception {
		init();

		ContratTravailServiceImpl serv = new ContratTravailServiceImpl();
		if (id != 0 && serv.getJustificatif(id) != null) {
			String justif = Utils.getSessionFileUploadPath(session,
					salarieFormBB.getId(), "contratTravail", 0, false, false,
					salarieFormBB.getNomGroupe())
					+ serv.getJustificatif(id);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(justif).getName());
			fileInfo.setFile(new File(justif));

			fileListContratTravailTemp.clear();
			fileListContratTravailTemp.add(new InputFileData(fileInfo,
					salarieFormBB.getUrl("contratTravail")));
		}
		add = true;
		modalRendered = !modalRendered;
	}

	public boolean isInPeriode(Integer yearD, Integer yearF,
			GregorianCalendar debut, GregorianCalendar fin) {
		if (yearD != null) {
			if (yearF != null) {
				if (debut != null) {
					int y = debut.get(Calendar.YEAR);
					if (y == yearD || y == yearF || (y > yearD && y < yearF)) {
						return true;
					}
				}
				if (fin != null) {
					int y = fin.get(Calendar.YEAR);
					if (y == yearD || y == yearF || (y > yearD && y < yearF)) {
						return true;
					}
				}
				return false;
			} else {
				if (debut != null) {
					int y = debut.get(Calendar.YEAR);
					if (y >= yearD) {
						return true;
					}
				}
				if (fin != null) {
					int y = fin.get(Calendar.YEAR);
					if (y >= yearD) {
						return true;
					}
				}
				return false;
			}
		} else {
			if (yearF != null) {
				if (debut != null) {
					int y = debut.get(Calendar.YEAR);
					if (y <= yearF) {
						return true;
					}
				}
				if (fin != null) {
					int y = fin.get(Calendar.YEAR);
					if (y <= yearF) {
						return true;
					}
				}
				return false;
			} else {
				return true;
			}
		}
	}

	public void uploadFileContratTravail(ActionEvent event) throws Exception {

		InputFile inputFile = (InputFile) event.getSource();

		FileInfo fileInfo = inputFile.getFileInfo();

		if (fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.",
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.");
			FacesContext
					.getCurrentInstance()
					.addMessage(
							"idSalarieForm:idSalarieTabSet:0:inputFileNameContratTravail",
							message);
			return;
		}

		if (fileInfo.getStatus() == FileInfo.SAVED) {

			fileListContratTravailTemp.clear();
			fileListContratTravailTemp.add(new InputFileData(fileInfo,
					salarieFormBB.getUrl("contratTravail")));
			if (isModif) {
				newFile = true;
			}
		}
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

	public void fileUploadProgressContratTravail(EventObject event) {

		InputFile ifile = (InputFile) event.getSource();

		fileProgressContratTravail = ifile.getFileInfo().getPercent();
	}

	public void remove(ActionEvent evt) {
		modalRenderedDelFile = true;
	}

	public void cancelRemove(ActionEvent evt) {
		modalRenderedDelFile = false;
	}

	public void removeUploadedFileContratTravail(ActionEvent evt)
			throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ContratTravailBean ctBean = (ContratTravailBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();

		ctBean.setJustificatif(null);
		ContratTravailServiceImpl serv = new ContratTravailServiceImpl();
		serv.saveOrUppdate(ctBean);
		this.id = ctBean.getId();

		init = true;
	}

	public void removeUploadedFileContratTravailTemp(ActionEvent event) {
		modalRenderedDelFile = false;
		if (fileListContratTravailTemp.get(0).getFile().exists()) {
			deletedFiles.add(fileListContratTravailTemp.get(0).getFile());
		}
		fileListContratTravailTemp.clear();
		this.fileProgressContratTravail = 0;
		newFile = false;
	}

	public int getFileProgressContratTravail() {
		return fileProgressContratTravail;
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
			if (!fileListContratTravailTemp.isEmpty()
					&& fileListContratTravailTemp.get(0).getFile().exists()) {
				fileListContratTravailTemp.clear();
			}
			newFile = false;
		}
		deletedFiles.clear();
		add = false;
		isModif = false;
		modalRendered = !modalRendered;
	}

	public int getIdMetierSelected() {
		return idMetierSelected;
	}

	public void setIdMetierSelected(int idMetierSelected) {
		this.idMetierSelected = idMetierSelected;
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

	public ArrayList<SelectItem> getTypeContratList() {
		return typeContratList;
	}

	public void saveOrUpdateSalarieContratTravail(ActionEvent event)
			throws Exception {

		if (this.idTypeContratSelected != -1
				&& this.idMetierSelected != -1
				&& this.debutContrat != null
				&& ((this.finContrat != null && this.idMotifRuptureSelected != -1) || this.finContrat == null)) {

			newFile = false;
			boolean isAnterieur = false;
			if (this.finContrat != null) {
				// Teste si date fin > date debut
				Calendar debut = new GregorianCalendar();
				Calendar fin = new GregorianCalendar();
				debut.setTime(this.debutContrat);
				fin.setTime(this.finContrat);

				isAnterieur = debut.after(fin);

			}
			boolean isPosteOuvert = false;
			ContratTravailServiceImpl ps = new ContratTravailServiceImpl();
			for (ContratTravailBean p : ps
					.getContratTravailBeanListByIdSalarie(salarieFormBB.getId())) {
				Calendar debut = new GregorianCalendar();
				debut.setTime(this.debutContrat);

				Calendar debutP = new GregorianCalendar();
				debutP.setTime(p.getDebutContrat());

				Calendar finP = new GregorianCalendar();
				if (p.getFinContrat() != null) {
					finP.setTime(p.getFinContrat());
				} else {
					finP = null;
				}

				if (this.finContrat != null) {

					Calendar fin = new GregorianCalendar();
					fin.setTime(this.finContrat);

					if (finP == null) {
						if (debut.after(debutP) || fin.after(debutP)) {
							isPosteOuvert = true;
						}

					} else {
						if ((fin.after(debutP) && fin.before(p.getFinContrat()))
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
						if (debut.before(finP) || debut.before(debutP)) {
							isPosteOuvert = true;
						}
					}
				}

			}
			if (isAnterieur || (isPosteOuvert && isModif == false)) {
				// Erreur
				if (isAnterieur) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"La date de fin de contrat est ant\u00E9rieure \u00E0 celle de d\u00E9but.",
							"La date de fin de contrat est ant\u00E9rieure \u00E0 celle de d\u00E9but.");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:finContrat",
							message);
				}

				if (isPosteOuvert && isModif == false) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Le salari\u00E9 est d\u00E9ja en poste pour cette p\u00E9riode.",
							"Le salari\u00E9 est d\u00E9ja en poste pour cette p\u00E9riode.");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:finContrat",
							message);
				}
			} else {
				ContratTravailBean contratTravailBean = new ContratTravailBean();

				if (this.id == 0) {
					this.id = -1;
				}
				contratTravailBean.setId(this.id);
				contratTravailBean.setDebutContrat(this.debutContrat);
				contratTravailBean.setFinContrat(this.finContrat);
				contratTravailBean.setIdMetierSelected(this.idMetierSelected);
				contratTravailBean
						.setIdTypeContratSelected(this.idTypeContratSelected);
				if (this.idMotifRuptureSelected != 0
						&& this.idMotifRuptureSelected != -1) {
					contratTravailBean
							.setIdMotifRuptureContrat(this.idMotifRuptureSelected);
				} else {
					contratTravailBean.setIdMotifRuptureContrat(null);
				}
				contratTravailBean.setRenouvellementCDD(this.renouvellementCDD);

				if (!fileListContratTravailTemp.isEmpty()) {
					contratTravailBean
							.setJustificatif(fileListContratTravailTemp.get(0)
									.getFile().getName());
				}

				MetierServiceImpl metierService = new MetierServiceImpl();
				StatutServiceImpl statutService = new StatutServiceImpl();
				TypeContratServiceImpl typeContratService = new TypeContratServiceImpl();
				TypeRecoursInterimServiceImpl typeRecoursService = new TypeRecoursInterimServiceImpl();

				String nomMetier = new String();
				String nomTypeContrat = new String();

				try {
					nomMetier = metierService.getMetierBeanById(
							this.idMetierSelected).getNom();
					if (this.idTypeContratSelected != 3) {
						nomTypeContrat = typeContratService
								.getTypeContratBeanById(
										this.idTypeContratSelected).getNom();
					} else {
						nomTypeContrat = "CTT";

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				contratTravailBean.setNomMetier(nomMetier);
				contratTravailBean.setNomTypeContrat(nomTypeContrat);
				contratTravailBean.setIdSalarie(salarieFormBB.getId());

				ContratTravailServiceImpl parServ = new ContratTravailServiceImpl();

				parServ.saveOrUppdate(contratTravailBean);

				modalRendered = !modalRendered;
				add = false;
				isModif = false;
				init = true;
			}
		} else {
			if (this.idTypeContratSelected == -1) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext
						.getCurrentInstance()
						.addMessage(
								"idSalarieForm:idSalarieTabSet:0:contratContratTravailList",
								message);
			}
			if (this.idMetierSelected == -1) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext
						.getCurrentInstance()
						.addMessage(
								"idSalarieForm:idSalarieTabSet:0:metierContratTravailList",
								message);
			}
			if (this.debutContrat == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance()
						.addMessage(
								"idSalarieForm:idSalarieTabSet:0:debutContrat",
								message);
			}
			if (this.finContrat != null) {
				if (this.idMotifRuptureSelected == -1) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
							"Champ obligatoire");
					FacesContext
							.getCurrentInstance()
							.addMessage(
									"idSalarieForm:idSalarieTabSet:0:motifRuptureContratTravailList",
									message);
				}
			}
		}
	}

	public void modifContratTravail(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ContratTravailBean contratTravailBean = (ContratTravailBean) table
				.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		this.fileProgressContratTravail = 0;

		this.debutContrat = contratTravailBean.getDebutContrat();
		this.finContrat = contratTravailBean.getFinContrat();
		this.id = contratTravailBean.getId();
		this.idMetierSelected = contratTravailBean.getIdMetierSelected();
		this.idTypeContratSelected = contratTravailBean
				.getIdTypeContratSelected();
		this.renouvellementCDD = contratTravailBean.isRenouvellementCDD();
		if (contratTravailBean.getIdMotifRuptureContrat() != null) {
			this.idMotifRuptureSelected = contratTravailBean
					.getIdMotifRuptureContrat();
		} else {
			this.idMotifRuptureSelected = 0;
		}

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		fileListContratTravailTemp.clear();
		if (contratTravailBean.getJustificatif() != null) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(contratTravailBean.getJustificatif())
					.getName());
			fileInfo.setFile(new File(contratTravailBean.getJustificatif()));

			fileListContratTravailTemp.clear();
			fileListContratTravailTemp.add(new InputFileData(fileInfo,
					salarieFormBB.getUrl("contratTravail")));
		}

		isModif = true;

		modalRendered = !modalRendered;
	}

	public void deleteContratTravail(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ContratTravailBean contratTravailBean = (ContratTravailBean) table
				.getRowData();
		// On récupère aussi son index
		int index = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		if (contratTravailBean.getId() > 0) {
			ContratTravailServiceImpl contratTravailService = new ContratTravailServiceImpl();
			contratTravailService.deleteContratTravail(contratTravailBean);
		}
		this.fileListContratTravailTemp.clear();
	}

	public void download(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ContratTravailBean contratTravailBean;
		InputFileData fileData;
		String fileName = "";
		String filePath = "";
		if (add || isModif) {
			fileData = (InputFileData) table.getRowData();
			fileName = fileData.getFile().getName();
			filePath = fileData.getPath();
		} else {
			contratTravailBean = (ContratTravailBean) table.getRowData();
			fileName = contratTravailBean.getJustif().getName();
			filePath = contratTravailBean.getJustificatif();
		}
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		filePath = Utils.getSessionFileUploadPath(session, getIdSalarie(),
				"contrat", 0, false, false, salarieFormBB.getNomGroupe())
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

	public List getFileListContratTravailTemp() {
		if (!fileListContratTravailTemp.isEmpty()
				&& (!fileListContratTravailTemp.get(0).getFile().exists()
						|| !fileListContratTravailTemp.get(0).getFile()
								.isFile() || !fileListContratTravailTemp.get(0)
						.getFile().canRead())) {
			fileError = true;
		} else {
			fileError = false;
		}
		return fileListContratTravailTemp;
	}

	public void setMetierList(ArrayList<SelectItem> metierList) {
		this.metierList = metierList;
	}

	public void setTypeContratList(ArrayList<SelectItem> typeContratList) {
		this.typeContratList = typeContratList;
	}

	public void setFileProgressContratTravail(int fileProgressContratTravail) {
		this.fileProgressContratTravail = fileProgressContratTravail;
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

	public void setFileListContratTravailTemp(
			List<InputFileData> fileListContratTravailTemp) {
		this.fileListContratTravailTemp = fileListContratTravailTemp;
	}

	public String getUrl() throws Exception {
		return salarieFormBB.getUrl("contrat");
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

	public void displayRenouvellementCDD(ValueChangeEvent evt) {
		this.idTypeContratSelected = (Integer) evt.getNewValue();
		if (this.idTypeContratSelected == 2) {
			displayRenouvellementCDD = true;
		} else {
			displayRenouvellementCDD = false;
		}
	}

	public boolean isdisplayRenouvellementCDD() {
		if (this.idTypeContratSelected == 2) {
			displayRenouvellementCDD = true;
		} else {
			displayRenouvellementCDD = false;
		}
		return displayRenouvellementCDD;
	}

	public void setDisplayRenouvellementCDD(boolean displayRenouvellementCDDList) {
		this.displayRenouvellementCDD = displayRenouvellementCDDList;
	}

	public Date getDebutContrat() {
		return debutContrat;
	}

	public void setDebutContrat(Date debutContrat) {
		this.debutContrat = debutContrat;
	}

	public Date getFinContrat() {
		return finContrat;
	}

	public void setFinContrat(Date finContrat) {
		this.finContrat = finContrat;
	}

	public ArrayList<SelectItem> getMotifRuptureList() {
		return motifRuptureList;
	}

	public void setMotifRuptureList(ArrayList<SelectItem> motifRuptureList) {
		this.motifRuptureList = motifRuptureList;
	}

	public int getIdMotifRuptureSelected() {
		return idMotifRuptureSelected;
	}

	public void setIdMotifRuptureSelected(int idMotifRuptureSelected) {
		this.idMotifRuptureSelected = idMotifRuptureSelected;
	}

	public boolean isRenouvellementCDD() {
		return renouvellementCDD;
	}

	public void setRenouvellementCDD(boolean renouvellementCDD) {
		this.renouvellementCDD = renouvellementCDD;
	}

	public boolean isDisplayRenouvellementCDD() {
		return displayRenouvellementCDD;
	}

	public boolean isMotifRuptureRequired() {
		if (this.finContrat == null) {
			return true;
		} else {
			return false;
		}
	}

	public void setMotifRuptureRequired(boolean motifRuptureRequired) {
		this.motifRuptureRequired = motifRuptureRequired;
	}

	public void setIdSalarie(int idSalarie) {
		this.idSalarie = idSalarie;
	}

}
