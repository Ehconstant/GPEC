package com.cci.gpec.web.backingBean.formation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.DomaineFormationBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.DomaineFormationServiceImpl;
import com.cci.gpec.metier.implementation.FormationServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.effects.JavascriptContext;

public class SalarieFormationsFormBB {

	// primary key
	private int id;
	private int idSalarie;

	// fields
	private Date debutFormation;
	private Date finFormation;
	private String nomFormation;
	private String organismeFormation;
	private String mode;
	private Integer volumeHoraire;
	private Integer dif;
	private Integer horsDif;
	private String difDisplay;
	private String horsDifDisplay;
	private boolean modalRendered = false;
	private boolean modalRenderedDelFile = false;
	private ArrayList<SelectItem> domaineFormationList;
	private int idDomaineFormationBeanSelected;
	private int idTypeAbsenceSelected;
	private boolean genereAbsence = true;
	private int idAbsence;
	private String nombreJourOuvre;
	private boolean modalRenderedAbs = false;

	private String coutOpca;
	private String coutEntreprise;
	private String coutAutre;

	DecimalFormat df = new DecimalFormat();

	private boolean natureAbsRequired;

	private int indexSelectedRow = -1;
	private boolean isModif = false;
	private boolean add = false;
	private boolean init = true;
	private boolean newFile = false;
	private boolean fail = false;

	private boolean majAbsence = true;

	private List<File> deletedFiles = new ArrayList<File>();

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");
	private List<InputFileData> fileListFormationTemp = new ArrayList<InputFileData>();
	private String currentFilePath;

	private int fileProgressFormation = 0;

	// latest file uploaded by client
	private InputFileData currentFile;

	private boolean fileError = false;

	private boolean alreadyExist = false;

	public SalarieFormationsFormBB() throws Exception {
		// init();
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

	public void setFileProgressFormation(int fileProgressFormation) {
		this.fileProgressFormation = fileProgressFormation;
	}

	public InputFileData getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFile(InputFileData currentFile) {
		this.currentFile = currentFile;
	}

	public boolean isFileError() {
		return fileError;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public List<InputFileData> getFileListFormationTemp() {
		if (!fileListFormationTemp.isEmpty()
				&& (!fileListFormationTemp.get(0).getFile().exists()
						|| !fileListFormationTemp.get(0).getFile().isFile() || !fileListFormationTemp
						.get(0).getFile().canRead())) {
			fileError = true;
		} else {
			fileError = false;
		}
		return fileListFormationTemp;
	}

	public void setDomaineFormationList(
			ArrayList<SelectItem> domaineFormationList) {
		this.domaineFormationList = domaineFormationList;
	}

	public void init() throws Exception {

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		this.fileProgressFormation = 0;
		this.fileListFormationTemp.clear();

		alreadyExist = false;

		DomaineFormationServiceImpl domaineFormationService = new DomaineFormationServiceImpl();

		List<DomaineFormationBean> domaineFormationBeanList = domaineFormationService
				.getDomaineFormationsList();
		domaineFormationList = new ArrayList<SelectItem>();
		SelectItem selectItem;
		for (DomaineFormationBean domaineFormationBean : domaineFormationBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(domaineFormationBean.getId());
			if (domaineFormationBean.getId() == 22) {
				selectItem.setLabel("Qualité-Hygiène-Sécurité-Environnement");
			} else {
				selectItem.setLabel(domaineFormationBean.getNom());
			}
			domaineFormationList.add(selectItem);
		}

		this.debutFormation = null;
		this.dif = null;
		this.horsDif = null;
		this.finFormation = null;
		this.id = 0;
		this.idSalarie = salarieFormBB.getId();
		this.mode = "Attestation formation";
		this.nomFormation = new String();
		this.organismeFormation = new String();
		this.volumeHoraire = null;
		this.idDomaineFormationBeanSelected = 0;
		this.idTypeAbsenceSelected = -1;
		this.indexSelectedRow = -1;
		this.isModif = false;
		this.genereAbsence = true;
		this.majAbsence = true;
		this.idAbsence = -1;
		this.nombreJourOuvre = null;
		this.coutOpca = null;
		this.coutEntreprise = null;
		this.coutAutre = null;

		init = false;
	}

	public void initSalarieFormationForm() throws Exception {
		init();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		FormationServiceImpl serv = new FormationServiceImpl();
		if (id != 0 && serv.getJustificatif(id) != null) {

			String justif = Utils.getSessionFileUploadPath(session,
					salarieFormBB.getId(), "formation", 0, false, false,
					salarieFormBB.getNomGroupe())
					+ serv.getJustificatif(id);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(justif).getName());
			fileInfo.setFile(new File(justif));

			fileListFormationTemp.clear();
			fileListFormationTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("formation")));
		}
		modalRendered = !modalRendered;
		add = true;
	}

	public void uploadFileFormation(ActionEvent event) throws Exception {

		InputFile inputFile = (InputFile) event.getSource();

		FileInfo fileInfo = inputFile.getFileInfo();

		if (fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.",
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:inputFileNameFormation",
					message);
			return;
		}

		if (fileInfo.getStatus() == FileInfo.SAVED) {

			fileListFormationTemp.clear();
			fileListFormationTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("formation")));
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

	public boolean alreadyExist() {
		boolean b = false;
		for (int i = 0; i < (fileListFormationTemp.size() - 1); i++) {
			for (int j = (i + 1); j < fileListFormationTemp.size(); j++) {
				if (((InputFileData) fileListFormationTemp.get(i))
						.getFileInfo()
						.getFileName()
						.equals(((InputFileData) fileListFormationTemp.get(j))
								.getFileInfo().getFileName())) {
					b = true;
					break;
				}
			}
			if (b == true) {
				break;
			}
		}
		return b;
	}

	public void fileUploadProgressFormation(EventObject event) {

		InputFile ifile = (InputFile) event.getSource();

		fileProgressFormation = ifile.getFileInfo().getPercent();
	}

	public void remove(ActionEvent evt) {
		modalRenderedDelFile = true;
	}

	public void cancelRemove(ActionEvent evt) {
		modalRenderedDelFile = false;
	}

	public void removeUploadedFileFormation(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FormationBean formationBean = (FormationBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		AbsenceServiceImpl absServ = new AbsenceServiceImpl();
		AbsenceBean abs = new AbsenceBean();
		abs = absServ.getAbsenceBeanById(formationBean.getIdAbsence());
		abs.setJustificatif(null);
		absServ.saveOrUppdate(abs);

		formationBean.setJustificatif(null);
		FormationServiceImpl serv = new FormationServiceImpl();
		serv.saveOrUppdate(formationBean);
		this.id = formationBean.getId();

		init = true;
	}

	public void removeUploadedFileFormationTemp(ActionEvent event) {
		modalRenderedDelFile = false;
		if (fileListFormationTemp.get(0).getFile().exists()) {
			deletedFiles.add(fileListFormationTemp.get(0).getFile());
		}
		fileListFormationTemp.clear();
		this.fileProgressFormation = 0;
		newFile = false;
	}

	public int getFileProgressFormation() {
		return fileProgressFormation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDebutFormation() {
		return debutFormation;
	}

	public void setDebutFormation(Date debutFormation) {
		this.debutFormation = debutFormation;
	}

	public Date getFinFormation() {
		return finFormation;
	}

	public void setFinFormation(Date finFormation) {
		this.finFormation = finFormation;
	}

	public String getNomFormation() {
		return nomFormation;
	}

	public void setNomFormation(String nomFormation) {
		this.nomFormation = nomFormation;
	}

	public String getOrganismeFormation() {
		return organismeFormation;
	}

	public void setOrganismeFormation(String organismeFormation) {
		this.organismeFormation = organismeFormation;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getVolumeHoraire() {
		return volumeHoraire;
	}

	public void setVolumeHoraire(Integer volumeHoraire) {
		this.volumeHoraire = volumeHoraire;
	}

	public Integer getDif() {
		return (dif != null) ? dif : 0;
	}

	public Integer getDif2() {
		if (dif != null) {
			return dif;
		} else {
			return 0;
		}
	}

	public void setDif(Integer dif) {
		this.dif = dif;
	}

	public boolean isModalRendered() {
		return modalRendered;
	}

	public void setModalRendered(boolean modalRendered) {
		this.modalRendered = modalRendered;
	}

	public void toggleModal(ActionEvent event) {
		if (newFile) {
			if (!fileListFormationTemp.isEmpty()
					&& fileListFormationTemp.get(0).getFile().exists()) {
				fileListFormationTemp.clear();
			}
			newFile = false;
		}
		deletedFiles.clear();
		modalRendered = !modalRendered;
	}

	public int getIdDomaineFormationBeanSelected() {
		return idDomaineFormationBeanSelected;
	}

	public void setIdDomaineFormationBeanSelected(
			int idDomaineFormationBeanSelected) {
		this.idDomaineFormationBeanSelected = idDomaineFormationBeanSelected;
	}

	public ArrayList<SelectItem> getDomaineFormationList() {
		return domaineFormationList;
	}

	private boolean isInPeriode(GregorianCalendar debutA,
			GregorianCalendar finA, GregorianCalendar debutF,
			GregorianCalendar finF) {
		// Teste si les dates de début et de fin appartiennent au début et fin
		// de période
		if (Utils.before(debutA, debutF) && Utils.before(debutF, finA)) {
			return true;
		}

		if (Utils.before(debutF, debutA) && Utils.after(finF, finA)) {
			return true;
		}

		if (Utils.before(debutF, debutA) && Utils.before(finF, finA)
				&& Utils.before(debutA, finF)) {
			return true;
		}

		if (chevaucheDemiJournee(debutA, finA, debutF, finF)) {
			return true;
		}

		return false;
	}

	public void checkPeriode(ValueChangeEvent evt) throws Exception {
		if (evt.getComponent().getId().equals("dateFinFormation")) {
			finFormation = (Date) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("dateDebutFormation")) {
			debutFormation = (Date) evt.getNewValue();
		}
		if (debutFormation != null && finFormation != null) {
			Calendar debut = new GregorianCalendar();
			Calendar fin = new GregorianCalendar();
			debut.setTime(this.debutFormation);
			fin.setTime(this.finFormation);

			FormationBean formationBean = new FormationBean();

			List<ParcoursBean> listParcoursSalarie = salarieFormBB
					.getParcoursBeanList();
			if (debut.after(fin)) {
				// Erreur

				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"La date de fin de formation est ant\u00E9rieure \u00E0 celle de d\u00E9but.",
						"La date de fin de formation est ant\u00E9rieure \u00E0 celle de d\u00E9but.");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:dateFinFormation",
						message);
				return;
			} else {

				if (!Utils.isInEntreprise(listParcoursSalarie, debutFormation,
						finFormation)) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Aucune pr\u00E9sence \u00e0 cette date.",
							"Aucune pr\u00E9sence \u00e0 cette date.");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:dateFinFormation",
							message);
					return;
				}

				Double d = (nombreJourOuvre.equals("")) ? 0.0 : Double
						.valueOf(nombreJourOuvre);
				if (isChevaucheDateFormation(salarieFormBB, debutFormation,
						finFormation, d) != null) {

					FormationBean f = isChevaucheDateFormation(salarieFormBB,
							debutFormation, finFormation, d);
					if (((f.getDebutFormation().equals(finFormation) && (debutFormation
							.before(f.getDebutFormation()) || debutFormation
							.equals(finFormation))))
							|| (f.getFinFormation().equals(debutFormation) && (finFormation
									.after(f.getFinFormation()) || debutFormation
									.equals(finFormation)))) {
						if (debutFormation.equals(finFormation)
								&& !nombreJourOuvre.equals("0.5")
								&& !nombreJourOuvre.equals("0.50")) {
							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Une formation est d\u00E9j\u00E0 d\u00E9clar\u00E9e ce jour, vous devez rentrer 0.5 jour pour ajouter une demi-journ\u00E9e de formation.",
									"Une formation est d\u00E9j\u00E0 d\u00E9clar\u00E9e ce jour, vous devez rentrer 0.5 jour pour ajouter une demi-journ\u00E9e de formation.");
							FacesContext
									.getCurrentInstance()
									.addMessage(
											"idSalarieForm:idSalarieTabSet:0:dateFinFormation",
											message);
							return;
						}
					} else {
						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Une autre formation est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.",
								"Une autre formation est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.");
						FacesContext
								.getCurrentInstance()
								.addMessage(
										"idSalarieForm:idSalarieTabSet:0:dateFinFormation",
										message);
						return;
					}

				}

				if (isChevaucheDateAbsence(salarieFormBB, debutFormation,
						finFormation) && this.genereAbsence) {

					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Une autre absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.",
							"Une autre absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:dateFinFormation",
							message);
					return;
				}
			}
		}
	}

	private boolean isChevaucheDateAbsence(SalarieFormBB salarieFormBB,
			Date d1, Date d2) throws Exception {

		AbsenceServiceImpl absServ = new AbsenceServiceImpl();
		List<AbsenceBean> absList = absServ.getAbsenceBeanListByIdSalarie(this
				.getIdSalarie());

		for (AbsenceBean absBean : absList) {
			if (absBean.getDebutAbsence() != null
					&& absBean.getFinAbsence() != null) {
				if (absBean.getId().intValue() != this.idAbsence) {
					GregorianCalendar debD1 = new GregorianCalendar();
					debD1.setTime(absBean.getDebutAbsence());
					GregorianCalendar finD1 = new GregorianCalendar();
					finD1.setTime(absBean.getFinAbsence());
					GregorianCalendar debD2 = new GregorianCalendar();
					debD2.setTime(debutFormation);
					GregorianCalendar finD2 = new GregorianCalendar();
					finD2.setTime(finFormation);
					if (isInPeriode(debD1, finD1, debD2, finD2)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private FormationBean isChevaucheDateFormation(SalarieFormBB salarieFormBB,
			Date d1, Date d2, Double d) throws Exception {

		FormationServiceImpl formServ = new FormationServiceImpl();
		List<FormationBean> formList = formServ
				.getFormationBeanListByIdSalarie(this.getIdSalarie());

		for (FormationBean formBean : formList) {
			if (formBean.getDebutFormation() != null
					&& formBean.getFinFormation() != null) {
				if (formBean.getId() != this.id) {
					GregorianCalendar debD1 = new GregorianCalendar();
					debD1.setTime(formBean.getDebutFormation());
					GregorianCalendar finD1 = new GregorianCalendar();
					finD1.setTime(formBean.getFinFormation());
					GregorianCalendar debD2 = new GregorianCalendar();
					debD2.setTime(d1);
					GregorianCalendar finD2 = new GregorianCalendar();
					finD2.setTime(d2);
					if (isInPeriode(debD1, finD1, debD2, finD2)) {
						if (chevaucheDemiJournee(debD1, finD1, debD2, finD2)
								&& debD2.equals(finD2)) {
							if ((d.toString().equals("0.5") || d.toString()
									.equals("0.50"))
									&& (formBean.getNombreJourOuvre()
											.toString().endsWith(".5") || formBean
											.getNombreJourOuvre().toString()
											.endsWith(".50"))) {
								return null;
							} else {
								return formBean;
							}
						} else {
							if (chevaucheDemiJournee(debD1, finD1, debD2, finD2)
									&& (d.toString().endsWith(".5") || d
											.toString().endsWith(".50"))
									&& (formBean.getNombreJourOuvre()
											.toString().endsWith(".5") || formBean
											.getNombreJourOuvre().toString()
											.endsWith(".50"))) {
								return null;
							} else {
								return formBean;
							}
						}
					}
				}
			}
		}
		return null;
	}

	private boolean chevaucheDemiJournee(GregorianCalendar deb1,
			GregorianCalendar fin1, GregorianCalendar deb2,
			GregorianCalendar fin2) {
		return ((deb1.equals(fin2) && (fin1.after(fin2) || deb1.equals(fin1)))
				|| (deb2.equals(fin1) && (fin2.after(fin1) || deb2.equals(fin2))) || (deb1
				.equals(deb2) && fin1.equals(fin2)));
	}

	public void saveOrUpdateSalarieFormation() throws Exception {
		newFile = false;
		modalRenderedAbs = false;

		SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
				.getCurrentInstance().getCurrentInstance().getExternalContext()
				.getSessionMap().get("salarieFormBB");

		Double creditDIFCumule = salarieFormBB.getCreditDifTmp();

		FormationServiceImpl formServ = new FormationServiceImpl();
		double oldDif = 0;
		if (isModif) {
			oldDif = formServ.getFormationBeanById(this.id).getDif();
		}

		if (this.nomFormation != null
				&& this.volumeHoraire != null
				&& (((this.dif != null && !this.dif.equals("")
						&& this.horsDif != null && !this.horsDif.equals("")) && ((this.dif + this.horsDif) == this.volumeHoraire))
						|| ((this.dif == null || this.dif.equals(""))
								&& this.horsDif != null
								&& !this.horsDif.equals("") && this.horsDif == this.volumeHoraire)
						|| ((this.horsDif == null || !this.horsDif.equals(""))
								&& this.dif != null && !this.dif.equals("") && this.dif == this.volumeHoraire) || (this.dif == null || this.dif
						.equals(""))
						&& (this.horsDif == null || !this.horsDif.equals(""))
						&& this.volumeHoraire == 0)
				&& !this.nomFormation.equals("")
				&& this.idDomaineFormationBeanSelected != -1
				&& this.organismeFormation != null
				&& !this.organismeFormation.equals("")
				&& this.debutFormation != null
				&& this.finFormation != null
				&& this.nombreJourOuvre != null
				&& !this.nombreJourOuvre.equals("")
				&& ((this.dif != null && this.dif <= creditDIFCumule && !isModif)
						|| (this.dif == null)
						|| (this.dif != null && isModif
								&& this.dif - oldDif > 0 && this.dif - oldDif <= creditDIFCumule) || (this.dif != null
						&& isModif && this.dif - oldDif <= 0))
				&& ((this.genereAbsence && this.idTypeAbsenceSelected != -1) || !this.genereAbsence)) {

			Calendar debut = new GregorianCalendar();
			Calendar fin = new GregorianCalendar();
			debut.setTime(this.debutFormation);
			fin.setTime(this.finFormation);

			FormationBean formationBean = new FormationBean();

			List<ParcoursBean> listParcoursSalarie = salarieFormBB
					.getParcoursBeanList();
			if (debut.after(fin)) {
				// Erreur

				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"La date de fin de formation est ant\u00E9rieure \u00E0 celle de d\u00E9but.",
						"La date de fin de formation est ant\u00E9rieure \u00E0 celle de d\u00E9but.");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:dateFinFormation",
						message);
				fail = true;
				return;

			} else if ((salarieFormBB.getCreditDif() - this.getVolumeHoraire()) < 0
					&& Double.valueOf(this.getDif2()) == 1 && !isModif) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le salari\u00E9 n'a plus assez de cr\u00E9dit DIF.",
						"Le salari\u00E9 n'a plus assez de cr\u00E9dit DIF.");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idVolHeure", message);
				fail = true;
				return;
			} else {
				if ((isModif && this.idTypeAbsenceSelected == -1
						&& this.genereAbsence && this.idAbsence != -1)
						|| (!isModif && this.idTypeAbsenceSelected == -1 && this.genereAbsence)) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Ce champ est obligatoire.",
							"Ce champ est obligatoire.");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:typeAbsenceList",
							message);
					fail = true;
					return;

				} else {

					if (!Utils.isInEntreprise(listParcoursSalarie,
							debutFormation, finFormation)) {
						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Aucune pr\u00E9sence \u00e0 cette date.",
								"Aucune pr\u00E9sence \u00e0 cette date.");
						FacesContext
								.getCurrentInstance()
								.addMessage(
										"idSalarieForm:idSalarieTabSet:0:dateDebutFormation",
										message);
						fail = true;
						return;
					}

					Double nb = (nombreJourOuvre.equals("")) ? 0.0 : Double
							.valueOf(nombreJourOuvre);
					if (isChevaucheDateFormation(salarieFormBB, debutFormation,
							finFormation, nb) != null) {

						if (debutFormation.equals(finFormation)
								&& !nombreJourOuvre.equals("0.5")
								&& !nombreJourOuvre.equals("0.50")) {
							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Une formation est d\u00E9j\u00E0 d\u00E9clar\u00E9e ce jour, vous devez saisir 0.5 jour pour ajouter une demi-journ\u00E9e de formation.",
									"Une formation est d\u00E9j\u00E0 d\u00E9clar\u00E9e ce jour, vous devez saisir 0.5 jour pour ajouter une demi-journ\u00E9e de formation.");
							FacesContext
									.getCurrentInstance()
									.addMessage(
											"idSalarieForm:idSalarieTabSet:0:dateFinFormation",
											message);
							fail = true;
							return;
						} else {
							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Une autre formation est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.",
									"Une autre formation est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.");
							FacesContext
									.getCurrentInstance()
									.addMessage(
											"idSalarieForm:idSalarieTabSet:0:dateFinFormation",
											message);
							fail = true;
							return;
						}
					}

					Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
					Matcher m = p.matcher(this.coutOpca);
					boolean m1 = m.matches();
					m = p.matcher(this.coutEntreprise);
					boolean m2 = m.matches();
					m = p.matcher(this.coutAutre);
					boolean m3 = m.matches();

					if ((!m1 && !this.coutOpca.equals(""))
							|| (!m2 && !this.coutEntreprise.equals(""))
							|| (!m3 && !this.coutAutre.equals(""))) {
						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Les couts doivent \u00eatre des nombres d\u00E9cimaux.",
								"Les couts doivent \u00eatre des nombres d\u00E9cimaux.");
						FacesContext.getCurrentInstance().addMessage(
								"idSalarieForm:idSalarieTabSet:0:idEntreprise",
								message);
						fail = true;
						return;
					}

					Integer difTemp = (dif == null) ? 0 : dif;
					Integer horsDifTemp = (horsDif == null) ? 0 : horsDif;
					if (((difTemp + horsDifTemp) != this.volumeHoraire)) {
						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Attention la somme DIF + HORS DIF est diff\u00E9rente du volume horaire.",
								"Attention la somme DIF + HORS DIF est diff\u00E9rente du volume horaire.");
						FacesContext.getCurrentInstance().addMessage(
								"idSalarieForm:idSalarieTabSet:0:idDif",
								message);
						fail = true;
						return;
					}

					if (!this.nombreJourOuvre.equals("")
							&& this.nombreJourOuvre.contains(",")) {
						this.nombreJourOuvre = this.nombreJourOuvre.replace(
								",", ".");
					}

					Double d = (this.nombreJourOuvre.equals("")) ? 0.0 : Double
							.valueOf(this.nombreJourOuvre) * 2;
					if (!d.toString().endsWith(".0")
							&& !d.toString().endsWith(",0")) {
						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"La précision du nombre de jour ne descend pas sous la demi-journée (0.5 jour).",
								"La précision du nombre de jour ne descend pas sous la demi-journée (0.5 jour).");
						FacesContext.getCurrentInstance().addMessage(
								"idSalarieForm:idSalarieTabSet:0:idJourOuvre",
								message);
						fail = true;
						return;
					}

					if (isChevaucheDateAbsence(salarieFormBB, debutFormation,
							finFormation) && this.genereAbsence) {

						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Une autre absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.",
								"Une autre absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.");
						FacesContext
								.getCurrentInstance()
								.addMessage(
										"idSalarieForm:idSalarieTabSet:0:dateFinFormation",
										message);
					}

					if (this.id == 0) {
						this.id = -1;
					}

					formationBean.setId(this.id);
					formationBean.setIdSalarie(this.getIdSalarie());
					formationBean.setDebutFormation(this.debutFormation);
					if (this.dif != null) {
						formationBean.setDif(this.dif);
					} else {
						formationBean.setDif(0);
					}
					if (this.horsDif != null) {
						formationBean.setHorsDif(this.horsDif);
					} else {
						formationBean.setHorsDif(0);
					}
					formationBean.setFinFormation(this.finFormation);
					formationBean.setMode(this.mode);
					formationBean.setNomFormation(this.nomFormation);
					formationBean
							.setOrganismeFormation(this.organismeFormation);
					formationBean
							.setIdDomaineFormationBeanSelected(idDomaineFormationBeanSelected);
					formationBean.setVolumeHoraire(this.volumeHoraire);

					if (!this.nombreJourOuvre.equals("")) {
						if (this.nombreJourOuvre.contains(",")) {
							this.nombreJourOuvre = this.nombreJourOuvre
									.replace(",", ".");
						}
						formationBean.setNombreJourOuvre(Double
								.valueOf(this.nombreJourOuvre));
					} else {
						formationBean.setNombreJourOuvre(0.0);
					}

					if (!this.coutOpca.equals("")) {
						formationBean.setCoutOpca(Double.valueOf(coutOpca));
					} else {
						formationBean.setCoutOpca(null);
					}

					if (!this.coutEntreprise.equals("")) {
						formationBean.setCoutEntreprise(Double
								.valueOf(coutEntreprise));
					} else {
						formationBean.setCoutEntreprise(null);
					}

					if (!this.coutAutre.equals("")) {
						formationBean.setCoutAutre(Double.valueOf(coutAutre));
					} else {
						formationBean.setCoutAutre(null);
					}

					if (!fileListFormationTemp.isEmpty()) {
						formationBean.setJustificatif(fileListFormationTemp
								.get(0).getFile().getName());
					}

					DomaineFormationServiceImpl domaineFormationService = new DomaineFormationServiceImpl();
					String nomDomaineFormation = new String();

					try {
						nomDomaineFormation = domaineFormationService
								.getDomaineFormationBeanById(
										this.idDomaineFormationBeanSelected)
								.getNom();
					} catch (Exception e) {
						e.printStackTrace();
					}

					formationBean.setNomDomaineFormation(nomDomaineFormation);

					formationBean
							.setIdTypeAbsenceGenere(this.idTypeAbsenceSelected);
					formationBean.setGenerationAbs(false);
					if (((this.idAbsence == -1 || this.idAbsence == 0) && genereAbsence)
							|| ((this.idAbsence != -1 && this.idAbsence != 0) && majAbsence)) {
						formationBean.setIdAbsence(this.idAbsence);
						formationBean.setGenerationAbs(true);
						AbsenceBean absenceBean = new AbsenceBean();
						absenceBean = formServ
								.genereAbsenceAutomatique(formationBean);

						salarieFormBB.getAbsenceBeanList().remove(absenceBean);
						salarieFormBB.getAbsenceBeanList().add(absenceBean);
					} else {
						if (this.idAbsence != -1 && this.idAbsence != 0) {
							AbsenceServiceImpl absServ = new AbsenceServiceImpl();
							AbsenceBean a = new AbsenceBean();
							a = absServ.getAbsenceBeanById(this.idAbsence);
							salarieFormBB.getAbsenceBeanList().remove(a);
							absServ.deleteAbsence(a);
							formationBean.setGenerationAbs(false);
							formationBean.setIdAbsence(-1);
						}
					}

					Double difGlobalSalarie = salarieFormBB.getCreditDif();
					Double difNewDouble = Double.valueOf(this.getDif2()
							.toString());
					Double difNew = (this.dif != null) ? Double
							.valueOf(this.dif) : 0.0;

					if (isModif == true) {
						FormationBean formationBeanSalarie = salarieFormBB
								.getFormationBeanList().get(indexSelectedRow);
						Double difOldDouble = (formationBeanSalarie.getDif() != null) ? formationBeanSalarie
								.getDif() : 0.0;
						Double difOld = (formationBeanSalarie.getDif() != null) ? formationBeanSalarie
								.getDif() : 0.0;

						if ((salarieFormBB.getCreditDif() - (difNewDouble - difOldDouble)) < 0) {
							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Le salari\u00E9 n'a plus assez de cr\u00E9dit DIF.",
									"Le salari\u00E9 n'a plus assez de cr\u00E9dit DIF.");
							FacesContext
									.getCurrentInstance()
									.addMessage(
											"idSalarieForm:idSalarieTabSet:0:idVolHeure",
											message);
							fail = true;
						}
						if (difNew != difOld) {
							if (difNew < difOld) {
								difGlobalSalarie += (difOld - difNew);
							}
							if (difNew > difOld) {
								difGlobalSalarie -= (difNew - difOld);
								if (difGlobalSalarie < 0) {
									FacesMessage message = new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Le salari\u00E9 n'a plus assez de cr\u00E9dit DIF.",
											"Le salari\u00E9 n'a plus assez de cr\u00E9dit DIF.");
									FacesContext
											.getCurrentInstance()
											.addMessage(
													"idSalarieForm:idSalarieTabSet:0:idVolHeure",
													message);
									fail = true;
								}
							}
							salarieFormBB.setCreditDif(difGlobalSalarie);
						}
						salarieFormBB.getFormationBeanList().set(
								indexSelectedRow, formationBean);
					} else {
						if (Double.valueOf(this.getDif2()) > 0) {
							// retire au compteur dif du salarié la quantité
							// horaire
							// de la formation ajoutée
							salarieFormBB.setCreditDif(difGlobalSalarie
									- difNewDouble);
						}
						salarieFormBB.getFormationBeanList().add(formationBean);
					}

					modalRendered = !modalRendered;
				}
			}
			salarieFormBB.saveOrUpdateSalarie();
		} else {
			fail = true;

			if (this.volumeHoraire == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idVolHeure", message);
			} else {
				if (this.dif != null && !this.dif.equals("")) {
					if (this.horsDif != null && !this.horsDif.equals("")) {
						if ((this.dif + this.horsDif) != this.volumeHoraire) {
							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"La somme DIF + HORS DIF doit \u00EAtre \u00E9gale au volume horaire.",
									"La somme DIF + HORS DIF doit \u00EAtre \u00E9gale au volume horaire.");
							FacesContext.getCurrentInstance().addMessage(
									"idSalarieForm:idSalarieTabSet:0:idDif",
									message);
						}
					} else {
						if (this.dif == this.volumeHoraire) {
							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"La somme DIF + HORS DIF doit \u00EAtre \u00E9gale au volume horaire.",
									"La somme DIF + HORS DIF doit \u00EAtre \u00E9gale au volume horaire.");
							FacesContext.getCurrentInstance().addMessage(
									"idSalarieForm:idSalarieTabSet:0:idDif",
									message);
						}
					}
				} else {
					if (this.horsDif != null && !this.horsDif.equals("")) {
						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"La somme DIF + HORS DIF doit \u00EAtre \u00E9gale au volume horaire.",
								"La somme DIF + HORS DIF doit \u00EAtre \u00E9gale au volume horaire.");
						FacesContext.getCurrentInstance().addMessage(
								"idSalarieForm:idSalarieTabSet:0:idDif",
								message);
					} else {
						if (this.volumeHoraire != 0) {
							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"La somme DIF + HORS DIF doit \u00EAtre \u00E9gale au volume horaire.",
									"La somme DIF + HORS DIF doit \u00EAtre \u00E9gale au volume horaire.");
							FacesContext.getCurrentInstance().addMessage(
									"idSalarieForm:idSalarieTabSet:0:idDif",
									message);
						}

					}
				}
			}

			if (this.nomFormation == null || this.nomFormation.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idFormation", message);
			}
			if (this.idDomaineFormationBeanSelected == -1) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:domaineFormationList",
						message);
			}
			if (this.organismeFormation == null
					|| this.organismeFormation.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idOrgForm", message);
			}
			if (this.debutFormation == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:dateDebutFormation",
						message);
			}
			if (this.finFormation == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:dateFinFormation",
						message);
			}
			if (this.nombreJourOuvre == null || this.nombreJourOuvre.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idJourOuvre", message);
			}
			if (this.genereAbsence) {
				if (this.idTypeAbsenceSelected == -1) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
							"Champ obligatoire");
					FacesContext
							.getCurrentInstance()
							.addMessage(
									"idSalarieForm:idSalarieTabSet:0:typeAbsenceListRequired",
									message);
				}
			}
			if (this.dif != null && !isModif) {
				if (this.dif > creditDIFCumule) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Le volume d'heures au titre du DIF dépasse le nombre d'heures du DIF cumulé",
							"Le volume d'heures au titre du DIF dépasse le nombre d'heures du DIF cumulé");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:idDif", message);
				}
			}
			if (this.dif != null && isModif) {
				if (this.dif - oldDif > 0
						&& this.dif - oldDif > creditDIFCumule) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Le volume d'heures au titre du DIF dépasse le nombre d'heures du DIF cumulé",
							"Le volume d'heures au titre du DIF dépasse le nombre d'heures du DIF cumulé");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:idDif", message);
				}
			}
		}
	}

	public void modifFormation(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FormationBean formationBean = (FormationBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		this.fileProgressFormation = 0;

		this.id = formationBean.getId();
		this.idDomaineFormationBeanSelected = formationBean
				.getIdDomaineFormationBeanSelected();
		this.debutFormation = formationBean.getDebutFormation();
		this.finFormation = formationBean.getFinFormation();
		this.idAbsence = formationBean.getIdAbsence();
		this.mode = formationBean.getMode();
		this.dif = (formationBean.getDif() != null) ? formationBean.getDif()
				: 0;
		this.horsDif = (formationBean.getHorsDif() != null) ? formationBean
				.getHorsDif() : 0;
		this.nomFormation = formationBean.getNomFormation();
		this.organismeFormation = formationBean.getOrganismeFormation();
		this.volumeHoraire = formationBean.getVolumeHoraire();
		this.nombreJourOuvre = df.format(formationBean.getNombreJourOuvre());
		this.majAbsence = (formationBean.getIdAbsence() == -1 || formationBean
				.getIdAbsence() == 0) ? false : true;
		this.coutOpca = (formationBean.getCoutOpca() != null) ? df
				.format(formationBean.getCoutOpca()) : "";
		this.coutEntreprise = (formationBean.getCoutEntreprise() != null) ? df
				.format(formationBean.getCoutEntreprise()) : "";
		this.coutAutre = (formationBean.getCoutAutre() != null) ? df
				.format(formationBean.getCoutAutre()) : "";

		if (this.idAbsence == 0 || this.idAbsence == -1) {
			this.majAbsence = false;
			this.genereAbsence = false;
		}

		// 1.8
		AbsenceServiceImpl abs = new AbsenceServiceImpl();
		AbsenceBean a = new AbsenceBean();
		if (this.idAbsence != 0 && this.idAbsence != -1
				&& abs.getAbsenceBeanById(this.idAbsence) != null) {
			a = abs.getAbsenceBeanById(this.idAbsence);
			this.idTypeAbsenceSelected = a.getIdTypeAbsenceSelected();
		} else {
			this.idTypeAbsenceSelected = -1;
		}
		//

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		fileListFormationTemp.clear();
		if (formationBean.getJustificatif() != null) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(formationBean.getJustificatif())
					.getName());
			fileInfo.setFile(new File(formationBean.getJustificatif()));

			fileListFormationTemp.clear();
			fileListFormationTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("formation")));
		}

		isModif = true;

		modalRendered = !modalRendered;

	}

	public void deleteFormation(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FormationBean formationBean = (FormationBean) table.getRowData();
		// On récupère aussi son index
		int index = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		FormationBean formationBeanSalarie = salarieFormBB
				.getFormationBeanList().get(index);
		Double difGlobalSalarie = salarieFormBB.getCreditDif();
		Double difFormationDelete = Double.valueOf(formationBeanSalarie
				.getVolumeHoraire().toString());
		// recrédite le compteur dif du salarié de la quantité horaire de la
		// formation supprimée

		// la formation que l'on supprime est comprise dans le diff
		// il faut danc rajouter les heures au compteur
		// if (this.getDif2() == 1) {
		Double d = (formationBean.getDif() != null) ? formationBean.getDif()
				: 0.0;
		salarieFormBB.setCreditDif(difGlobalSalarie + d);

		salarieFormBB.getFormationBeanList().remove(index);

		if (formationBean.getId() > 0) {
			FormationServiceImpl formationService = new FormationServiceImpl();
			if (formationBean.getIdAbsence() != -1) {
				AbsenceServiceImpl absenceService = new AbsenceServiceImpl();
				AbsenceBean absenceBean = absenceService
						.getAbsenceBeanById(formationBean.getIdAbsence());
				Iterator<AbsenceBean> ite = salarieFormBB.getAbsenceBeanList()
						.iterator();

				while (ite.hasNext()) {
					AbsenceBean absenceBean2 = ite.next();
					if (absenceBean.getId().equals(absenceBean2.getId())) {
						ite.remove();
					}

				}
				formationService.deleteFormation(formationBean);
				absenceService.deleteAbsenceFin(absenceBean);
			} else
				formationService.deleteFormation(formationBean);
		}
		salarieFormBB.saveOrUpdateSalarie();
		this.fileListFormationTemp.clear();
	}

	public void download(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FormationBean formationBean;
		InputFileData fileData;
		String fileName = "";
		String filePath = "";
		if (add || isModif) {
			fileData = (InputFileData) table.getRowData();
			fileName = fileData.getFile().getName();
			filePath = fileData.getPath();
		} else {
			formationBean = (FormationBean) table.getRowData();
			fileName = formationBean.getJustif().getName();
			filePath = formationBean.getJustificatif();
		}
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		filePath = Utils.getSessionFileUploadPath(session, getIdSalarie(),
				"formation", 0, false, false, salarieFormBB.getNomGroupe())
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

	public void majCheckBox(ValueChangeEvent evt) throws Exception {
		if (evt.getComponent().getId().equals("majAbsence")) {
			this.majAbsence = (Boolean) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("genereAbsence")) {
			this.genereAbsence = (Boolean) evt.getNewValue();
		}
		checkPeriode(evt);
	}

	public void checkCoutOPCA(ValueChangeEvent event) {
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
		this.coutOpca = (String) event.getNewValue();

		if (!this.coutOpca.equals("") && this.coutOpca.contains(","))
			this.coutOpca = this.coutOpca.replace(",", ".");
		this.coutOpca = (this.coutOpca.equals("")) ? "" : df.format(Double
				.valueOf(this.coutOpca));

		Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
		Matcher m = p.matcher(this.coutOpca);
		boolean m1 = m.matches();

		if (!m1 && !this.coutOpca.equals("")) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Les couts doivent \u00eatre des nombres d\u00E9cimaux.",
					"Les couts doivent \u00eatre des nombres d\u00E9cimaux.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:idCoutOPCA", message);
			return;
		}
		FacesContext.getCurrentInstance().renderResponse();
	}

	public void checkCoutEntreprise(ValueChangeEvent event) {

		this.coutEntreprise = (String) event.getNewValue();

		if (!this.coutEntreprise.equals("")
				&& this.coutEntreprise.contains(",")) {
			this.coutEntreprise = this.coutEntreprise.replace(",", ".");
		}
		this.coutEntreprise = (this.coutEntreprise.equals("")) ? "" : df
				.format(Double.valueOf(this.coutEntreprise));

		Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
		Matcher m = p.matcher(this.coutEntreprise);
		boolean m1 = m.matches();

		if (!m1 && !this.coutEntreprise.equals("")) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Les couts doivent \u00eatre des nombres d\u00E9cimaux.",
					"Les couts doivent \u00eatre des nombres d\u00E9cimaux.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:idEntreprise", message);
			return;
		}

	}

	public void checkNbJoursOuvres(ValueChangeEvent event) throws Exception {

		this.nombreJourOuvre = (String) event.getNewValue();
		if (!this.nombreJourOuvre.toString().equals("")
				&& this.nombreJourOuvre.toString().contains(",")) {
			this.nombreJourOuvre = this.nombreJourOuvre.replace(",", ".");
		}

		if (!this.nombreJourOuvre.toString().equals("")) {
			Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
			Matcher m = p.matcher(this.nombreJourOuvre);
			boolean m1 = m.matches();

			if (!m1 && !this.nombreJourOuvre.toString().equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le nombre de jours ouvr\u00E9s doit \u00eatre un nombre entier ou d\u00E9cimal.",
						"Le nombre de jours ouvr\u00E9s doit \u00eatre un nombre entier ou d\u00E9cimal.");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idJourOuvre", message);
				return;
			}

			Double d = Double.valueOf(this.nombreJourOuvre) * 2;
			if (!d.toString().endsWith(".0") && !d.toString().endsWith(",0")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"La précision du nombre de jour ne descend pas sous la demi-journée (0.5 jour).",
						"La précision du nombre de jour ne descend pas sous la demi-journée (0.5 jour).");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idJourOuvre", message);
				return;
			}
		}
		checkPeriode(event);
	}

	public void checkCoutAutre(ValueChangeEvent event) {

		this.coutAutre = (String) event.getNewValue();
		if (!this.coutAutre.equals("") && this.coutAutre.contains(",")) {
			this.coutAutre = this.coutAutre.replace(",", ".");
		}
		this.coutAutre = (this.coutAutre.equals("")) ? "" : df.format(Double
				.valueOf(this.coutAutre));

		Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
		Matcher m = p.matcher(this.coutAutre);
		boolean m1 = m.matches();

		if (!m1 && !this.coutAutre.equals("")) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Les couts doivent \u00eatre des nombres d\u00E9cimaux.",
					"Les couts doivent \u00eatre des nombres d\u00E9cimaux.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:idCoutAutre", message);
			return;
		}

	}

	public void checkDif(ValueChangeEvent event) {

		if (event.getComponent().getId().equals("idVolHeure")
				&& !event.getNewValue().toString().equals("")) {
			this.volumeHoraire = (Integer) event.getNewValue();
		}
		if (event.getComponent().getId().equals("idDif")
				&& !event.getNewValue().toString().equals("")) {
			this.dif = Integer.valueOf(event.getNewValue().toString());
		}
		if (event.getComponent().getId().equals("idHorsDif")
				&& !event.getNewValue().toString().equals("")) {
			this.horsDif = Integer.valueOf(event.getNewValue().toString());
		}

	}

	public int getIdTypeAbsenceSelected() {
		return idTypeAbsenceSelected;
	}

	public void setIdTypeAbsenceSelected(int idTypeAbsenceSelected) {
		this.idTypeAbsenceSelected = idTypeAbsenceSelected;
	}

	public boolean isGenereAbsence() {
		return genereAbsence;
	}

	public void setGenereAbsence(boolean genereAbsence) {
		this.genereAbsence = genereAbsence;
	}

	public boolean isIsModif() {
		return isModif;
	}

	public void setIsModif(boolean isModif) {
		this.isModif = isModif;
	}

	public boolean isMajAbsence() {
		return majAbsence;
	}

	public void setMajAbsence(boolean majAbsence) {
		this.majAbsence = majAbsence;
	}

	public int getIdAbsence() {
		return idAbsence;
	}

	public void setIdAbsence(int idAbsence) {
		this.idAbsence = idAbsence;
	}

	public String getNombreJourOuvre() {
		return nombreJourOuvre;
	}

	public void setNombreJourOuvre(String nombreJourOuvre) {
		if (nombreJourOuvre.toString().contains(","))
			nombreJourOuvre = nombreJourOuvre.replace(",", ".");
		Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
		Matcher m = p.matcher(nombreJourOuvre);
		boolean m1 = m.matches();

		if (!m1 && !this.nombreJourOuvre.equals("")) {
			this.nombreJourOuvre = null;
		} else {
			this.nombreJourOuvre = nombreJourOuvre;
		}
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
				if (ParcoursDeb >= 0) {
					ParcoursFin = k;
				} else {
					return 0;
				}
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

		List<ParcoursBean> listParcoursSalarie = salarieFormBB
				.getParcoursBeanList();
		int nbrJour = 0;
		if (debutFormation != null && finFormation != null) {
			nbrJour = getNombreJourRupture(listParcoursSalarie, debutFormation,
					finFormation);
		}
		if (nbrJour > 1) {
			modalRenderedAbs = !modalRenderedAbs;
		} else {
			modalRenderedAbs = !modalRenderedAbs;
			saveOrUpdateSalarieFormation();
		}
		if (!fail) {
			deletedFiles.clear();
			add = false;
			isModif = false;
			init = true;
		} else {
			fail = false;
		}
	}

	public boolean isModalRenderedAbs() {
		return modalRenderedAbs;
	}

	public void setModalRenderedAbs(boolean modalRenderedAbs) {
		this.modalRenderedAbs = modalRenderedAbs;
	}

	public void toggleModalRupture(ActionEvent event) {
		deletedFiles.clear();
		modalRenderedAbs = !modalRenderedAbs;
	}

	public boolean isAlreadyExist() {
		return alreadyExist;
	}

	public void setAlreadyExist(boolean alreadyExist) {
		this.alreadyExist = alreadyExist;
	}

	public String getUrl() throws Exception {
		return salarieFormBB.getUrl("formation");
	}

	public String getCurrentFilePath() {
		return currentFilePath;
	}

	public void setCurrentFilePath(String currentFilePath) {
		this.currentFilePath = currentFilePath;
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

	public boolean isNatureAbsRequired() {
		return (((this.idAbsence != -1 && this.idAbsence != 0) && this.majAbsence) || ((this.idAbsence == -1 || this.idAbsence == 0) && this.genereAbsence));
	}

	public void setNatureAbsRequired(boolean natureAbsRequired) {
		this.natureAbsRequired = natureAbsRequired;
	}

	public String getCoutOpca() {
		if (coutOpca != null) {
			return (coutOpca.equals("")) ? "" : df.format(Double
					.valueOf(coutOpca));
		}
		return null;
	}

	public void setCoutOpca(String coutOpca) {
		this.coutOpca = coutOpca;
	}

	public String getCoutEntreprise() {
		if (coutEntreprise != null) {
			return (coutEntreprise.equals("")) ? "" : df.format(Double
					.valueOf(coutEntreprise));
		}
		return null;
	}

	public void setCoutEntreprise(String coutEntreprise) {
		this.coutEntreprise = coutEntreprise;
	}

	public boolean isModalRenderedDelFile() {
		return modalRenderedDelFile;
	}

	public void setModalRenderedDelFile(boolean modalRenderedDelFile) {
		this.modalRenderedDelFile = modalRenderedDelFile;
	}

	public String getCoutAutre() {
		if (coutAutre != null) {
			return (coutAutre.equals("")) ? "" : df.format(Double
					.valueOf(coutAutre));
		}
		return null;
	}

	public void setCoutAutre(String coutAutre) {
		this.coutAutre = coutAutre;
	}

	public Integer getHorsDif() {
		return (horsDif != null) ? horsDif : 0;
	}

	public void setHorsDif(Integer horsDif) {
		this.horsDif = horsDif;
	}

	public String getDifDisplay() {
		return (dif == null || dif == 0) ? "" : dif.toString();
	}

	public void setDifDisplay(String difDisplay) {
		this.difDisplay = difDisplay;
	}

	public String getHorsDifDisplay() {
		return (horsDif == null || horsDif == 0) ? "" : horsDif.toString();
	}

	public void setHorsDifDisplay(String horsDifDisplay) {
		this.horsDifDisplay = horsDifDisplay;
	}

	public void setIdSalarie(int idSalarie) {
		this.idSalarie = idSalarie;
	}

}
