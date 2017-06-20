package com.cci.gpec.web.backingBean.Accident;

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
import java.util.Iterator;
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

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.TypeAccidentBean;
import com.cci.gpec.commons.TypeCauseAccidentBean;
import com.cci.gpec.commons.TypeLesionBean;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.AccidentServiceImpl;
import com.cci.gpec.metier.implementation.TypeAccidentServiceImpl;
import com.cci.gpec.metier.implementation.TypeCauseAccidentServiceImpl;
import com.cci.gpec.metier.implementation.TypeLesionServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.effects.JavascriptContext;

public class SalarieAccidentsFormBB {

	// primary key
	private int id;
	private int idSalarie;

	// fields
	private Date dateAccident;
	private Date dateRechute;
	private Integer nombreJourArret;
	private Integer nombreJourArretRechute;
	private boolean modalRendered = false;
	private Date debutAbsence;
	private Date finAbsence;
	private boolean disabledDates = true;
	private int idAbsence;
	private boolean modalRenderedAbs = false;
	private boolean modalRenderedDelFile = false;
	private String commentaire;

	private boolean canDelete;
	/*
	 * afficher la popup d'avertissement de suppression d'un accident initial
	 * avec rechute
	 */
	private boolean delete;

	private boolean aggravation = true;
	private boolean initial = true;

	private int idOldAbsence;
	private Date oldDebutAbs;
	private Date oldFinAbs;

	private ArrayList<SelectItem> typeAccidentList;
	private int idTypeAccidentBeanSelected;

	private ArrayList<SelectItem> typeLesionList;
	private String nomTypeLesion;
	private int idTypeLesionBeanSelected;
	private int idTypeLesionRechuteBeanSelected;

	private ArrayList<SelectItem> typeCauseAccidentList;
	private String nomTypeCause;
	private int idTypeCauseAccidentBeanSelected;

	private String nomTypeAccident;

	private int indexSelectedRow = -1;
	private boolean isModif = false;
	private boolean init = true;
	private boolean add = false;
	private boolean newFile = false;
	private boolean fail = false;

	private boolean required;

	private List<File> deletedFiles = new ArrayList<File>();

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");

	private List<InputFileData> fileListAccidentTemp = new ArrayList<InputFileData>();

	private boolean fileError = false;

	private int fileProgressAccident = 0;

	public SalarieAccidentsFormBB() throws Exception {
		// init();
	}

	public void init() throws Exception {

		this.fileProgressAccident = 0;
		this.fileListAccidentTemp.clear();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		TypeAccidentServiceImpl typeAccidentService = new TypeAccidentServiceImpl();

		TypeLesionServiceImpl typeLesionService = new TypeLesionServiceImpl();

		TypeCauseAccidentServiceImpl typeCauseAccidentService = new TypeCauseAccidentServiceImpl();

		List<TypeAccidentBean> typeAccidentBeanList = typeAccidentService
				.getTypeAccidentList();
		typeAccidentList = new ArrayList<SelectItem>();
		SelectItem selectItem;
		for (TypeAccidentBean typeAccidentBean : typeAccidentBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(typeAccidentBean.getId());
			selectItem.setLabel(typeAccidentBean.getNom());
			typeAccidentList.add(selectItem);
		}

		List<TypeLesionBean> typeLesionBeanList = typeLesionService
				.getTypeLesionList();
		typeLesionList = new ArrayList<SelectItem>();
		SelectItem selectItemLesion;
		for (TypeLesionBean typeLesionBean : typeLesionBeanList) {
			selectItemLesion = new SelectItem();
			selectItemLesion.setValue(typeLesionBean.getId());
			selectItemLesion.setLabel(typeLesionBean.getNom());
			typeLesionList.add(selectItemLesion);
		}

		List<TypeCauseAccidentBean> typeCauseAccidentBeanList = typeCauseAccidentService
				.getTypeCauseAccidentList();
		typeCauseAccidentList = new ArrayList<SelectItem>();
		SelectItem selectItemCauseAccident;
		for (TypeCauseAccidentBean typeCauseAccidentBean : typeCauseAccidentBeanList) {
			selectItemCauseAccident = new SelectItem();
			selectItemCauseAccident.setValue(typeCauseAccidentBean.getId());
			selectItemCauseAccident.setLabel(typeCauseAccidentBean.getNom());
			typeCauseAccidentList.add(selectItemCauseAccident);
		}

		this.id = 0;
		this.dateAccident = null;
		this.dateRechute = null;
		this.nombreJourArret = null;
		this.nombreJourArretRechute = null;
		this.debutAbsence = null;
		this.finAbsence = null;
		this.idAbsence = -1;
		this.idSalarie = salarieFormBB.getId();

		this.initial = true;
		this.aggravation = true;

		this.idTypeAccidentBeanSelected = 0;

		this.idTypeLesionBeanSelected = -1;
		this.idTypeLesionRechuteBeanSelected = -1;
		this.idTypeCauseAccidentBeanSelected = -1;
		this.indexSelectedRow = -1;
		this.isModif = false;

	}

	public void initSalarieAccidentForm() throws Exception {
		init();
		AccidentServiceImpl serv = new AccidentServiceImpl();
		if (id != 0 && serv.getJustificatif(id) != null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);

			String justif = Utils.getSessionFileUploadPath(session,
					salarieFormBB.getId(), "accident", 0, false, false,
					salarieFormBB.getNomGroupe())
					+ serv.getJustificatif(id);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(justif).getName());
			fileInfo.setFile(new File(justif));

			fileListAccidentTemp.clear();
			fileListAccidentTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("accident")));
		}
		modalRendered = !modalRendered;
		add = true;
	}

	public void uploadFileAccident(ActionEvent event) throws Exception {

		InputFile inputFile = (InputFile) event.getSource();

		FileInfo fileInfo = inputFile.getFileInfo();

		if (fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.",
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:inputFileNameAccident",
					message);
			return;
		}

		if (fileInfo.getStatus() == FileInfo.SAVED) {

			fileListAccidentTemp.clear();
			fileListAccidentTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("accident")));
			if (isModif) {
				newFile = true;
			}
		}
	}

	public void fileUploadProgressAccident(EventObject event) {

		InputFile ifile = (InputFile) event.getSource();

		fileProgressAccident = ifile.getFileInfo().getPercent();
	}

	public void removeUploadedFileAccident(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AccidentBean accidentBean = (AccidentBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		// if (new File(accidentBean.getJustificatif()).exists())
		// new File(accidentBean.getJustificatif()).delete();

		if (accidentBean.getIdAbsence() != -1
				&& accidentBean.getIdAbsence() != 0) {
			AbsenceServiceImpl absServ = new AbsenceServiceImpl();
			AbsenceBean abs = new AbsenceBean();
			abs = absServ.getAbsenceBeanById(accidentBean.getIdAbsence());
			abs.setJustificatif(null);
			absServ.saveOrUppdate(abs);
		}

		accidentBean.setJustificatif(null);
		AccidentServiceImpl serv = new AccidentServiceImpl();
		serv.saveOrUppdate(accidentBean);
		this.id = accidentBean.getId();

		init = true;
	}

	public void remove(ActionEvent event) {
		modalRenderedDelFile = true;
	}

	public void removeUploadedFileAccidentTemp(ActionEvent event) {
		modalRenderedDelFile = false;
		if (fileListAccidentTemp.get(0).getFile().exists()) {
			deletedFiles.add(fileListAccidentTemp.get(0).getFile());
		}

		fileListAccidentTemp.clear();
		this.fileProgressAccident = 0;
		newFile = false;
	}

	public void cancelRemove(ActionEvent evt) {
		modalRenderedDelFile = false;
	}

	public List<InputFileData> getFileListAccidentTemp() {
		if (!fileListAccidentTemp.isEmpty()
				&& (!fileListAccidentTemp.get(0).getFile().exists()
						|| !fileListAccidentTemp.get(0).getFile().isFile() || !fileListAccidentTemp
						.get(0).getFile().canRead())) {
			fileError = true;
		} else {
			fileError = false;
		}
		return fileListAccidentTemp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateAccident() {
		return dateAccident;
	}

	public void setDateAccident(Date dateAccident) {
		this.dateAccident = dateAccident;
	}

	public Integer getNombreJourArret() {
		return nombreJourArret;
	}

	public void setNombreJourArret(Integer nombreJourArret) {
		this.nombreJourArret = nombreJourArret;
	}

	public boolean isModalRendered() {
		return modalRendered;
	}

	public void setModalRendered(boolean modalRendered) {
		this.modalRendered = modalRendered;
	}

	public void toggleModal(ActionEvent event) {
		if (newFile) {
			if (!fileListAccidentTemp.isEmpty()
					&& fileListAccidentTemp.get(0).getFile().exists()) {
				fileListAccidentTemp.clear();
			}
			newFile = false;
		}
		deletedFiles.clear();
		modalRendered = !modalRendered;
	}

	public int getIdTypeAccidentBeanSelected() {
		return idTypeAccidentBeanSelected;
	}

	public void setIdTypeAccidentBeanSelected(int idTypeAccidentBeanSelected) {
		this.idTypeAccidentBeanSelected = idTypeAccidentBeanSelected;
	}

	public int getIdTypeLesionBeanSelected() {
		return idTypeLesionBeanSelected;
	}

	public void setIdTypeLesionBeanSelected(int idTypeLesionBeanSelected) {
		this.idTypeLesionBeanSelected = idTypeLesionBeanSelected;
	}

	public int getIdTypeCauseAccidentBeanSelected() {
		return idTypeCauseAccidentBeanSelected;
	}

	public void setIdTypeCauseAccidentBeanSelected(
			int idTypeCauseAccidentBeanSelected) {
		this.idTypeCauseAccidentBeanSelected = idTypeCauseAccidentBeanSelected;
	}

	public ArrayList<SelectItem> getTypeAccidentList() {
		return typeAccidentList;
	}

	public ArrayList<SelectItem> getTypeLesionList() {
		return typeLesionList;
	}

	public ArrayList<SelectItem> getTypeCauseAccidentList() {
		return typeCauseAccidentList;
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
			GregorianCalendar finA, GregorianCalendar debutAcc,
			GregorianCalendar finAcc) {
		// Teste si les dates de début et de fin appartiennent au début et fin
		// de période
		if (before(debutA, debutAcc) && before(debutAcc, finA)) {
			return true;
		}

		if (before(debutAcc, debutA) && after(finAcc, finA)) {
			return true;
		}

		if (before(debutAcc, debutA) && before(finAcc, finA)
				&& before(debutA, finAcc)) {
			return true;
		}

		if (debutAcc.equals(debutA) || finAcc.equals(finA)) {
			return true;
		}

		return false;
	}

	public void loadAccident(ValueChangeEvent evt) throws Exception {

		if (evt.getComponent().getId().equals("initial")) {
			initial = (Boolean) evt.getNewValue();
		}

		if (evt.getComponent().getId().equals("dateAccident")) {
			salarieFormBB.checkPresenceDebutAccident(evt);
			this.dateAccident = (Date) evt.getNewValue();
		}

		AccidentBean acc = new AccidentBean();

		boolean dateAlreadyExist = false;
		for (AccidentBean a : salarieFormBB.getAccidentBeanList()) {

			if (a.isInitial() && a.getDateAccident().equals(this.dateAccident)
					&& a.getId() != this.id) {
				dateAlreadyExist = true;
				acc = a;
				break;
			}
			if (!a.isInitial() && a.getDateRechute().equals(this.dateAccident)
					&& a.getId() != this.id) {
				dateAlreadyExist = true;
				acc = a;
				break;
			}

		}

		if (dateAlreadyExist && initial) {
			// Erreur

			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Un accident existe d\u00E9ja pour cette date",
					"Un accident existe d\u00E9ja pour cette date");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:dateAccident", message);
			return;
		}

		if (!initial) {
			if (!dateAlreadyExist) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Aucun accident enregistr\u00E9 pour cette date",
						"Aucun accident enregistr\u00E9 pour cette date");
				FacesContext.getCurrentInstance()
						.addMessage(
								"idSalarieForm:idSalarieTabSet:0:dateAccident",
								message);
				return;
			} else {

				this.idTypeAccidentBeanSelected = acc
						.getIdTypeAccidentBeanSelected();
				this.idTypeCauseAccidentBeanSelected = acc
						.getIdTypeCauseAccidentBeanSelected();
				this.idTypeLesionBeanSelected = acc
						.getIdTypeLesionBeanSelected();
				this.nombreJourArret = acc.getNombreJourArret();
				this.idTypeAccidentBeanSelected = acc
						.getIdTypeAccidentBeanSelected();

				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "", "");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:initial", message);
			}
		}
	}

	public void checkPresenceDebutAccident(ValueChangeEvent event)
			throws Exception {
		salarieFormBB.checkPresenceDebutAccident(event);
		if (!initial && dateAccident == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Rappel : la date de l'accident initial doit être saisie",
					"Rappel : la date de l'accident initial doit être saisie");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:initial", message);
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", "");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:initial", message);
		}
	}

	public void checkPeriode(ValueChangeEvent evt) throws Exception {
		if (evt != null) {
			if (evt.getComponent().getId().equals("dateFinAbsence")) {
				finAbsence = (Date) evt.getNewValue();
			}
			if (evt.getComponent().getId().equals("dateDebutAbsence")) {
				debutAbsence = (Date) evt.getNewValue();
			}
		}

		if (!initial && dateAccident == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Rappel : la date de l'accident initial doit être saisie",
					"Rappel : la date de l'accident initial doit être saisie");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:initial", message);
		}

		if (nombreJourArret > 0 && debutAbsence != null && finAbsence != null) {
			Calendar debut = new GregorianCalendar();
			Calendar fin = new GregorianCalendar();
			debut.setTime(debutAbsence);
			fin.setTime(finAbsence);
			if (debut.after(fin)) {
				// Erreur

				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"La date de fin est ant\u00E9rieure \u00E0 celle du d\u00E9but",
						"La date de fin d'absence est ant\u00E9rieure \u00E0 celle du d\u00E9but");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
						message);
				return;
			} else {
				List<ParcoursBean> listParcoursSalarie = salarieFormBB
						.getParcoursBeanList();

				if (!Utils.isInEntreprise(listParcoursSalarie, debutAbsence,
						finAbsence)) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Aucune pr\u00E9sence \u00e0 cette date.",
							"Aucune pr\u00E9sence \u00e0 cette date.");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
							message);
					return;
				}

				if (isChevaucheDateAbsenceType() && !isModif) {

					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Une autre absence pour accident est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.",
							"Une autre absence pour accident est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
							message);
					return;
				}

				if (isChevaucheDateAbsence() && !isModif) {

					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Une autre absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.",
							"Une autre absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
							message);
					return;
				}
			}
		}

	}

	private boolean isChevaucheDateAbsence() throws Exception {

		AbsenceServiceImpl absServ = new AbsenceServiceImpl();
		List<AbsenceBean> absList = absServ.getAbsenceBeanListByIdSalarie(this
				.getIdSalarie());

		for (AbsenceBean absBean : absList) {
			if (absBean.getDebutAbsence() != null
					&& absBean.getFinAbsence() != null) {
				if (absBean.getId() != this.idAbsence
						&& absBean.getIdTypeAbsenceSelected() != 1
						&& absBean.getIdTypeAbsenceSelected() != 2
						&& absBean.getIdTypeAbsenceSelected() != 4) {
					GregorianCalendar debD1 = new GregorianCalendar();
					debD1.setTime(absBean.getDebutAbsence());
					GregorianCalendar finD1 = new GregorianCalendar();
					finD1.setTime(absBean.getFinAbsence());
					GregorianCalendar debD2 = new GregorianCalendar();
					debD2.setTime(debutAbsence);
					GregorianCalendar finD2 = new GregorianCalendar();
					finD2.setTime(finAbsence);
					if (isInPeriode(debD1, finD1, debD2, finD2)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isChevaucheDateAbsenceType() throws Exception {

		AbsenceServiceImpl absServ = new AbsenceServiceImpl();
		List<AbsenceBean> absList = absServ.getAbsenceBeanListByIdSalarie(this
				.getIdSalarie());

		for (AbsenceBean absBean : absList) {
			if (absBean.getDebutAbsence() != null
					&& absBean.getFinAbsence() != null) {
				if (absBean.getId() != this.idAbsence
						&& (absBean.getIdTypeAbsenceSelected() == 1
								|| absBean.getIdTypeAbsenceSelected() == 2 || absBean
								.getIdTypeAbsenceSelected() == 4)) {
					GregorianCalendar debD1 = new GregorianCalendar();
					debD1.setTime(absBean.getDebutAbsence());
					GregorianCalendar finD1 = new GregorianCalendar();
					finD1.setTime(absBean.getFinAbsence());
					GregorianCalendar debD2 = new GregorianCalendar();
					debD2.setTime(debutAbsence);
					GregorianCalendar finD2 = new GregorianCalendar();
					finD2.setTime(finAbsence);
					if (isInPeriode(debD1, finD1, debD2, finD2)) {
						return true;
					}
				}
			}
		}
		return false;
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

	public void saveOrUpdateSalarieAccident() throws Exception {
		newFile = false;
		modalRenderedAbs = false;

		AccidentBean accidentBean = new AccidentBean();

		List<ParcoursBean> listParcoursSalarie = salarieFormBB
				.getParcoursBeanList();

		boolean dateAlreadyExist = false;
		for (AccidentBean a : salarieFormBB.getAccidentBeanList()) {
			if (a.getDateAccident().equals(this.dateAccident)) {
				dateAlreadyExist = true;
				break;
			}
		}
		if (!initial && !dateAlreadyExist) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Aucun accident n'existe à cette date",
					"Aucun accident n'existe à cette date");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:dateAccident", message);
			fail = true;

		} else {
			if (this.idTypeLesionBeanSelected == -1) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:typeLesionList",
						message);
				fail = true;
			} else {

				if (this.idTypeCauseAccidentBeanSelected == -1) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
							"Champ obligatoire");
					FacesContext
							.getCurrentInstance()
							.addMessage(
									"idSalarieForm:idSalarieTabSet:0:typeCauseAccidentList",
									message);
					fail = true;
				} else {

					if (nombreJourArret > 0 && debutAbsence != null
							&& finAbsence != null) {
						// Teste si date fin > date debut
						Calendar debut = new GregorianCalendar();
						Calendar fin = new GregorianCalendar();
						debut.setTime(debutAbsence);
						fin.setTime(finAbsence);
						if (debut.after(fin)) {
							// Erreur

							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"La date de fin est ant\u00E9rieure \u00E0 celle du d\u00E9but",
									"La date de fin d'absence est ant\u00E9rieure \u00E0 celle du d\u00E9but");
							FacesContext
									.getCurrentInstance()
									.addMessage(
											"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
											message);
							fail = true;
						}

						if ((isInitial() && !Utils
								.isInEntreprise(listParcoursSalarie,
										dateAccident, dateAccident))
								|| (!isInitial() && !Utils.isInEntreprise(
										listParcoursSalarie, dateRechute,
										dateRechute))) {
							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Aucune pr\u00E9sence \u00e0 cette date.",
									"Aucune pr\u00E9sence \u00e0 cette date.");
							FacesContext
									.getCurrentInstance()
									.addMessage(
											"idSalarieForm:idSalarieTabSet:0:dateDebutAbsence",
											message);
							fail = true;
							return;
						}
						if (isChevaucheDateAbsenceType()) {

							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Une autre absence pour accident est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.",
									"Une autre absence pour accident est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.");
							FacesContext
									.getCurrentInstance()
									.addMessage(
											"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
											message);
							fail = true;
							return;
						}
						if (isChevaucheDateAbsence()) {

							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Une autre absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.",
									"Une autre absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.");
							FacesContext
									.getCurrentInstance()
									.addMessage(
											"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
											message);
							fail = true;
							return;
						}
					}

					if (this.id == 0) {
						this.id = -1;
					}
					TypeLesionServiceImpl typeLesionService = new TypeLesionServiceImpl();

					TypeAccidentServiceImpl typeAccidentService = new TypeAccidentServiceImpl();

					TypeCauseAccidentServiceImpl typeCauseAccidentService = new TypeCauseAccidentServiceImpl();

					String nomTypeLesionRechute = new String();

					accidentBean.setId(this.id);
					accidentBean.setIdSalarie(this.getIdSalarie());
					accidentBean.setDateAccident(this.dateAccident);
					accidentBean.setNombreJourArret(this.nombreJourArret);

					accidentBean.setInitial(this.initial);
					if (!this.initial) {
						accidentBean.setDateRechute(this.dateRechute);
						accidentBean
								.setNombreJourArretRechute(this.nombreJourArretRechute);
						accidentBean.setAggravation(this.aggravation);
						accidentBean
								.setIdTypeLesionRechuteBeanSelected(this.idTypeLesionRechuteBeanSelected);
						try {
							nomTypeLesionRechute = typeLesionService
									.getTypeLesionBeanById(
											this.idTypeLesionRechuteBeanSelected)
									.getNom();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					accidentBean.setCommentaire(this.commentaire);
					accidentBean
							.setIdTypeAccidentBeanSelected(this.idTypeAccidentBeanSelected);
					accidentBean
							.setIdTypeCauseAccidentBeanSelected(this.idTypeCauseAccidentBeanSelected);
					accidentBean
							.setIdTypeLesionBeanSelected(this.idTypeLesionBeanSelected);

					String nomTypeAccident = new String();

					String nomTypeLesion = new String();

					String nomTypeCauseAccident = new String();

					try {
						nomTypeAccident = typeAccidentService
								.getTypeAccidentBeanById(
										this.idTypeAccidentBeanSelected)
								.getNom();
						nomTypeLesion = typeLesionService
								.getTypeLesionBeanById(
										this.idTypeLesionBeanSelected).getNom();
						nomTypeCauseAccident = typeCauseAccidentService
								.getTypeCauseAccidentBeanById(
										this.idTypeCauseAccidentBeanSelected)
								.getNom();

					} catch (Exception e) {
						e.printStackTrace();
					}

					accidentBean.setNomTypeAccident(nomTypeAccident);
					accidentBean.setNomTypeCauseAccident(nomTypeCauseAccident);
					accidentBean.setNomTypeLesion(nomTypeLesion);
					accidentBean.setNomTypeLesionRechute(nomTypeLesionRechute);
					accidentBean.setDateDebutAbsence(debutAbsence);
					accidentBean.setDateFinAbsence(finAbsence);
					if (!fileListAccidentTemp.isEmpty()) {
						accidentBean.setJustificatif(fileListAccidentTemp
								.get(0).getFile().getName());
					}

					if (nombreJourArret > 0 && debutAbsence != null
							&& finAbsence != null) {
						accidentBean.setIdAbsence(this.idAbsence);
						AccidentServiceImpl accServ = new AccidentServiceImpl();
						FacesContext facesContext = FacesContext
								.getCurrentInstance();
						HttpSession session = (HttpSession) facesContext
								.getExternalContext().getSession(false);

						if (accidentBean.getJustificatif() != null
								&& !accidentBean.getJustificatif().equals("")) {
							File f = new File(Utils.getSessionFileUploadPath(
									session, getIdSalarie(), "absence", 0,
									false, false, salarieFormBB.getNomGroupe()));
							if (!f.exists()) {
								f.mkdirs();
							}
							copyFile(
									Utils.getSessionFileUploadPath(session,
											getIdSalarie(), "accident", 0,
											false, false,
											salarieFormBB.getNomGroupe())
											+ accidentBean.getJustificatif(),
									Utils.getSessionFileUploadPath(session,
											getIdSalarie(), "absence", 0,
											false, false,
											salarieFormBB.getNomGroupe())
											+ accidentBean.getJustificatif());
						}
						AbsenceBean absenceBean = new AbsenceBean();
						if (accidentBean.getJustificatif() != null
								&& !accidentBean.getJustificatif().equals("")) {
							if (!fileListAccidentTemp.isEmpty()) {
								absenceBean = accServ.genereAbsenceAutomatique(
										accidentBean,
										fileListAccidentTemp.get(0).getFile()
												.getName());
							} else {
								absenceBean = accServ.genereAbsenceAutomatique(
										accidentBean, "");
							}
						} else {
							absenceBean = accServ.genereAbsenceAutomatique(
									accidentBean, "");
						}

						salarieFormBB.getAbsenceBeanList().remove(absenceBean);
						salarieFormBB.getAbsenceBeanList().add(absenceBean);
					} else {
						if (this.idAbsence != 0 && this.idAbsence != -1) {
							AbsenceServiceImpl absServ = new AbsenceServiceImpl();
							AbsenceBean a = new AbsenceBean();
							a = absServ.getAbsenceBeanById(this.idAbsence);
							salarieFormBB.getAbsenceBeanList().remove(a);
							absServ.deleteAbsence(a);
							accidentBean.setIdAbsence(-1);
						}
					}
					if (isModif == true) {
						salarieFormBB.getAccidentBeanList().set(
								indexSelectedRow, accidentBean);
						isModif = false;
					} else {
						salarieFormBB.getAccidentBeanList().add(accidentBean);

					}
					salarieFormBB.saveOrUpdateSalarie();
					modalRendered = !modalRendered;
				}
			}
		}
	}

	public void modifAccident(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AccidentBean accidentBean = (AccidentBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		this.fileProgressAccident = 0;

		this.initial = accidentBean.isInitial();
		if (accidentBean.isAggravation() != null) {
			this.aggravation = accidentBean.isAggravation();
		} else {
			this.aggravation = false;
		}
		this.id = accidentBean.getId();
		this.idAbsence = accidentBean.getIdAbsence();
		this.idOldAbsence = accidentBean.getIdAbsence();
		this.dateAccident = accidentBean.getDateAccident();
		this.nombreJourArret = accidentBean.getNombreJourArret();
		if (nombreJourArret > 0) {
			disabledDates = false;
		}
		if (accidentBean.getIdAbsence() != -1
				&& accidentBean.getIdAbsence() != 0) {
			AbsenceServiceImpl absenceService = new AbsenceServiceImpl();
			AbsenceBean absenceBean = absenceService
					.getAbsenceBeanById(accidentBean.getIdAbsence());

			this.debutAbsence = absenceBean.getDebutAbsence();
			this.finAbsence = absenceBean.getFinAbsence();
		} else {
			this.debutAbsence = null;
			this.finAbsence = null;
		}
		this.idTypeAccidentBeanSelected = accidentBean
				.getIdTypeAccidentBeanSelected();
		this.idTypeCauseAccidentBeanSelected = accidentBean
				.getIdTypeCauseAccidentBeanSelected();
		this.idTypeLesionBeanSelected = accidentBean
				.getIdTypeLesionBeanSelected();

		if (!this.initial) {
			this.idTypeLesionRechuteBeanSelected = accidentBean
					.getIdTypeLesionRechuteBeanSelected();
			this.dateRechute = accidentBean.getDateRechute();
			this.nombreJourArretRechute = accidentBean
					.getNombreJourArretRechute();
		}

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		fileListAccidentTemp.clear();
		if (accidentBean.getJustificatif() != null) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(accidentBean.getJustificatif())
					.getName());
			fileInfo.setFile(new File(accidentBean.getJustificatif()));

			fileListAccidentTemp.clear();
			fileListAccidentTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("accident")));
		}

		isModif = true;

		modalRendered = !modalRendered;

		checkPeriode(null);

	}

	public void closePopup() {
		delete = false;
	}

	public void displayPopup() {
		delete = true;
	}

	public void download(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AccidentBean accidentBean;
		InputFileData fileData;
		String fileName = "";
		String filePath = "";
		if (add || isModif) {
			fileData = (InputFileData) table.getRowData();
			fileName = fileData.getFile().getName();
			filePath = fileData.getPath();
		} else {
			accidentBean = (AccidentBean) table.getRowData();
			fileName = accidentBean.getJustif().getName();
			filePath = accidentBean.getJustificatif();
		}
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		filePath = Utils.getSessionFileUploadPath(session, getIdSalarie(),
				"accident", 0, false, false, salarieFormBB.getNomGroupe())
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

	public void deleteAccident(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AccidentBean accidentBean = (AccidentBean) table.getRowData();
		// On récupère aussi son index
		int index = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		canDelete = true;

		if (accidentBean.getInitial()) {
			for (AccidentBean a : salarieFormBB.getAccidentBeanList()) {
				if (!a.getInitial()
						&& a.getDateAccident().equals(
								accidentBean.getDateAccident())) {
					canDelete = false;
					break;
				}
			}
		}

		if (canDelete) {
			salarieFormBB.getAccidentBeanList().remove(index);

			if (accidentBean.getId() > 0) {
				AccidentServiceImpl accidentService = new AccidentServiceImpl();
				if (accidentBean.getIdAbsence() != -1) {
					AbsenceServiceImpl absenceService = new AbsenceServiceImpl();
					AbsenceBean absenceBean = absenceService
							.getAbsenceBeanById(accidentBean.getIdAbsence());
					Iterator<AbsenceBean> ite = salarieFormBB
							.getAbsenceBeanList().iterator();

					while (ite.hasNext()) {
						AbsenceBean absenceBean2 = ite.next();
						if (absenceBean.getId().equals(absenceBean2.getId())) {
							ite.remove();
						}

					}
					accidentService.deleteAccident(accidentBean);
					absenceService.deleteAbsenceFin(absenceBean);
				} else
					accidentService.deleteAccident(accidentBean);
			}
			this.fileListAccidentTemp.clear();
		} else {
			displayPopup();
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

	public Date getDebutAbsence() {
		return debutAbsence;
	}

	public void setDebutAbsence(Date debutAbsence) {
		this.debutAbsence = debutAbsence;
		if (!initial && dateAccident == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Rappel : la date de l'accident initial doit être saisie",
					"Rappel : la date de l'accident initial doit être saisie");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:initial", message);
		}
	}

	public Date getFinAbsence() {
		return finAbsence;
	}

	public void setFinAbsence(Date finAbsence) {
		this.finAbsence = finAbsence;
		if (!initial && dateAccident == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Rappel : la date de l'accident initial doit être saisie",
					"Rappel : la date de l'accident initial doit être saisie");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:initial", message);
		}
	}

	public void checkNombreJour(ValueChangeEvent event) throws Exception {
		int nbJour = Integer.parseInt(event.getNewValue().toString());
		disabledDates = (nbJour > 0) ? false : true;
		nombreJourArret = Integer.valueOf(event.getNewValue().toString());
		if (nombreJourArret > 0) {
			checkPeriode(event);
		}
	}

	public void checkNombreJourRechute(ValueChangeEvent event) throws Exception {
		int nbJour = Integer.parseInt(event.getNewValue().toString());
		disabledDates = (nbJour > 0) ? false : true;
		nombreJourArretRechute = Integer
				.valueOf(event.getNewValue().toString());
		if (nombreJourArretRechute > 0) {
			checkPeriode(event);
		}
	}

	public boolean isDisabledDates() {
		return disabledDates;
	}

	public void setDisabledDates(boolean disabledDates) {
		this.disabledDates = disabledDates;
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
				if (before(debAbs, finParcours) && before(debParcours, debAbs)) {
					if (before(finAbs, finParcours)
							&& before(debParcours, finAbs)) {
						// Absence dans un meme poste
						return 0;
					} else {
						ParcoursDeb = j;
					}
				}
				if (before(finAbs, finParcours) && before(debParcours, finAbs)) {
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

		int nbrJour = 0;
		if (dateAccident != null && nombreJourArret != null) {
			GregorianCalendar finaccident = new GregorianCalendar();
			finaccident.setTime(dateAccident);
			finaccident.add(Calendar.DAY_OF_MONTH, nombreJourArret);
			List<ParcoursBean> listParcoursSalarie = salarieFormBB
					.getParcoursBeanList();
			nbrJour = getNombreJourRupture(listParcoursSalarie, dateAccident,
					finaccident.getTime());
		}
		if (nbrJour > 1) {
			modalRenderedAbs = !modalRenderedAbs;
		} else {
			modalRenderedAbs = !modalRenderedAbs;

			if (this.initial) {
				if (this.dateAccident != null
						&& this.idTypeAccidentBeanSelected != -1
						&& this.idTypeCauseAccidentBeanSelected != -1
						&& this.idTypeLesionBeanSelected != -1) {
					if (this.nombreJourArret != null) {
						if (this.nombreJourArret != 0) {
							if (this.debutAbsence != null
									&& this.finAbsence != null) {
								saveOrUpdateSalarieAccident();
							} else {
								displayError();
							}
						} else {
							saveOrUpdateSalarieAccident();
						}
					} else {
						displayError();
					}
				} else {
					displayError();
				}
			} else {
				if (this.dateAccident != null
						&& this.idTypeAccidentBeanSelected != -1
						&& this.idTypeCauseAccidentBeanSelected != -1
						&& this.idTypeLesionBeanSelected != -1) {
					if (this.dateRechute != null
							&& this.idTypeLesionRechuteBeanSelected != -1) {
						if (this.nombreJourArretRechute != null) {
							if (this.nombreJourArretRechute != 0) {
								if (this.debutAbsence != null
										&& this.finAbsence != null) {
									saveOrUpdateSalarieAccident();
								} else {
									displayError();
								}
							} else {
								saveOrUpdateSalarieAccident();
							}
						} else {
							displayError();
						}
					} else {
						displayError();
					}
				} else {
					displayError();
				}
			}
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

	public void displayError() {
		fail = true;
		modalRenderedAbs = false;
		if (this.dateAccident == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
					"Champ obligatoire");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:dateAccident", message);
		}
		if (this.idTypeAccidentBeanSelected == -1) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
					"Champ obligatoire");
			FacesContext.getCurrentInstance()
					.addMessage(
							"idSalarieForm:idSalarieTabSet:0:typeAccidentList",
							message);
		}
		if (this.idTypeCauseAccidentBeanSelected == -1) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
					"Champ obligatoire");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:typeCauseAccidentList",
					message);
		}
		if (this.idTypeLesionBeanSelected == -1) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
					"Champ obligatoire");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:typeLesionList", message);
		}
		if (this.nombreJourArret == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
					"Champ obligatoire");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:idJourArret", message);
		} else {
			if (this.nombreJourArret != 0) {
				if (this.debutAbsence == null || this.finAbsence == null) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Saisir une date de début et de fin de l'absence, sinon saisir 0 dans le nombre de jours d'arrêt",
							"Saisir une date de début et de fin de l'absence, sinon saisir 0 dans le nombre de jours d'arrêt");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
							message);
				}
			}
		}
		if (!this.initial) {

			if (this.dateRechute == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:dateRechute", message);
			}
			if (this.idTypeLesionRechuteBeanSelected == -1) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:typeLesionList2",
						message);
			}
			if (this.nombreJourArretRechute == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idJourArretRechute",
						message);
			} else {
				if (this.nombreJourArretRechute != 0) {
					if (this.debutAbsence == null || this.finAbsence == null) {
						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Saisir une date de début et de fin de l'absence, sinon saisir 0 dans le nombre de jours d'arrêt",
								"Saisir une date de début et de fin de l'absence, sinon saisir 0 dans le nombre de jours d'arrêt");
						FacesContext
								.getCurrentInstance()
								.addMessage(
										"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
										message);
					}
				}
			}
		}
	}

	public boolean isModalRenderedAbs() {
		return modalRenderedAbs;
	}

	public void setModalRenderedAbs(boolean modalRenderedAbs) {
		this.modalRenderedAbs = modalRenderedAbs;
	}

	public void toggleModalRupture(ActionEvent event) {

		if (!fileListAccidentTemp.isEmpty()) {
			fileListAccidentTemp.clear();
		}

		modalRenderedAbs = !modalRenderedAbs;
	}

	public int getIdAbsence() {
		return idAbsence;
	}

	public void setIdAbsence(int idAbsence) {
		this.idAbsence = idAbsence;
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

	public int getFileProgressAccident() {
		return fileProgressAccident;
	}

	public void setFileProgressAccident(int fileProgressAccident) {
		this.fileProgressAccident = fileProgressAccident;
	}

	public void setTypeAccidentList(ArrayList<SelectItem> typeAccidentList) {
		this.typeAccidentList = typeAccidentList;
	}

	public void setTypeLesionList(ArrayList<SelectItem> typeLesionList) {
		this.typeLesionList = typeLesionList;
	}

	public void setTypeCauseAccidentList(
			ArrayList<SelectItem> typeCauseAccidentList) {
		this.typeCauseAccidentList = typeCauseAccidentList;
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

	public String getUrl() throws Exception {
		return salarieFormBB.getUrl("accident");
	}

	public void setFileListAccidentTemp(List<InputFileData> fileListAccidentTemp) {
		this.fileListAccidentTemp = fileListAccidentTemp;
	}

	public int getIdSalarie() {
		return salarieFormBB.getId();
	}

	public boolean isRequired() {
		if (nombreJourArret != null && nombreJourArret != 0) {
			required = true;
		} else {
			required = false;
		}
		return required;
	}

	public int getIdOldAbsence() {
		return idOldAbsence;
	}

	public void setIdOldAbsence(int idOldAbsence) {
		this.idOldAbsence = idOldAbsence;
	}

	public Date getOldDebutAbs() {
		return oldDebutAbs;
	}

	public void setOldDebutAbs(Date oldDebutAbs) {
		this.oldDebutAbs = oldDebutAbs;
	}

	public Date getOldFinAbs() {
		return oldFinAbs;
	}

	public void setOldFinAbs(Date oldFinAbs) {
		this.oldFinAbs = oldFinAbs;
	}

	public boolean isModalRenderedDelFile() {
		return modalRenderedDelFile;
	}

	public void setModalRenderedDelFile(boolean modalRenderedDelFile) {
		this.modalRenderedDelFile = modalRenderedDelFile;
	}

	public Date getDateRechute() {
		return dateRechute;
	}

	public void setDateRechute(Date dateRechute) {
		this.dateRechute = dateRechute;
		if (dateAccident == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Rappel : la date de l'accident initial doit être saisie",
					"Rappel : la date de l'accident initial doit être saisie");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:initial", message);
		}
	}

	public Integer getNombreJourArretRechute() {
		return nombreJourArretRechute;
	}

	public void setNombreJourArretRechute(Integer nombreJourArretRechute) {
		this.nombreJourArretRechute = nombreJourArretRechute;
		if (dateAccident == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Rappel : la date de l'accident initial doit être saisie",
					"Rappel : la date de l'accident initial doit être saisie");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:initial", message);
		}
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public boolean isAggravation() {
		return aggravation;
	}

	public void setAggravation(boolean aggravation) {
		this.aggravation = aggravation;
		if (dateAccident == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Rappel : la date de l'accident initial doit être saisie",
					"Rappel : la date de l'accident initial doit être saisie");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:initial", message);
		}
	}

	public boolean isInitial() {
		return initial;
	}

	public void setInitial(boolean initial) {
		this.initial = initial;
		if (!initial) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Rappel : la date de l'accident initial doit être saisie",
					"Rappel : la date de l'accident initial doit être saisie");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:initial", message);
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", "");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:initial", message);
		}
	}

	public int getIdTypeLesionRechuteBeanSelected() {
		return idTypeLesionRechuteBeanSelected;
	}

	public void setIdTypeLesionRechuteBeanSelected(
			int idTypeLesionRechuteBeanSelected) {
		this.idTypeLesionRechuteBeanSelected = idTypeLesionRechuteBeanSelected;
		if (!initial && dateAccident == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Rappel : la date de l'accident initial doit être saisie",
					"Rappel : la date de l'accident initial doit être saisie");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:initial", message);
		}
	}

	public String getNomTypeLesion() {
		if (this.idTypeLesionBeanSelected != -1) {
			for (SelectItem item : this.typeLesionList) {
				if (Integer.valueOf(item.getValue().toString()) == this.idTypeLesionBeanSelected)
					return item.getLabel();
			}
			return "";
		} else {
			return "";
		}
	}

	public void setNomTypeLesion(String nomTypeLesion) {
		this.nomTypeLesion = nomTypeLesion;
	}

	public String getNomTypeCause() {
		if (this.idTypeCauseAccidentBeanSelected != -1) {
			// rechercher le bon item avec son id
			for (SelectItem item : this.typeCauseAccidentList) {
				if (Integer.valueOf(item.getValue().toString()) == this.idTypeCauseAccidentBeanSelected)
					return item.getLabel();
			}
			return "";
		} else {
			return "";
		}
	}

	public void setNomTypeCause(String nomTypeCause) {
		this.nomTypeCause = nomTypeCause;
	}

	public String getNomTypeAccident() {
		if (this.idTypeAccidentBeanSelected != -1) {
			// rechercher le bon item avec son id
			for (SelectItem item : this.typeAccidentList) {
				if (Integer.valueOf(item.getValue().toString()) == this.idTypeAccidentBeanSelected)
					return item.getLabel();
			}
			return "";
		} else {
			return "";
		}
	}

	public void setNomTypeAccident(String nomTypeAccident) {
		this.nomTypeAccident = nomTypeAccident;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public void setIdSalarie(int idSalarie) {
		this.idSalarie = idSalarie;
	}

}
