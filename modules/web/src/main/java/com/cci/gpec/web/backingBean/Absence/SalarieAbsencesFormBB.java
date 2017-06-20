package com.cci.gpec.web.backingBean.Absence;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.TypeAbsenceBean;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.AccidentServiceImpl;
import com.cci.gpec.metier.implementation.FormationServiceImpl;
import com.cci.gpec.metier.implementation.TypeAbsenceServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.effects.JavascriptContext;

public class SalarieAbsencesFormBB {

	// primary key
	private int id;
	private int idSalarie;

	// fields
	private Date debutAbsence;
	private Date finAbsence;
	private boolean modalRendered = false;
	private boolean modalRenderedAbs = false;
	private boolean modalRenderedDelFile = false;
	private boolean generationAuto = false;
	private int idTypeAbsenceSelected;
	private String libelleTypeAbsence = new String();
	private String nombreJourOuvre;
	private ArrayList<SelectItem> typeAbsenceList;

	private int indexSelectedRow = -1;

	private List<InputFileData> fileListAbsenceTemp = new ArrayList<InputFileData>();

	private boolean fileError = false;

	private int fileProgressAbsence = 0;

	private boolean init = true;
	private boolean isModif = false;
	private boolean add = false;
	private boolean newFile = false;
	private boolean fail = false;

	private List<File> deletedFiles = new ArrayList<File>();

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public boolean isNewFile() {
		return newFile;
	}

	public void setNewFile(boolean newFile) {
		this.newFile = newFile;
	}

	public String getUrl() throws Exception {
		return salarieFormBB.getUrl("absence");
	}

	public SalarieAbsencesFormBB() throws Exception {
		// init();
	}

	public void init() throws Exception {

		this.libelleTypeAbsence = "";
		this.fileProgressAbsence = 0;
		this.fileListAbsenceTemp.clear();

		TypeAbsenceServiceImpl typeAbsenceService = new TypeAbsenceServiceImpl();

		List<TypeAbsenceBean> typeAbsenceBeanList = typeAbsenceService
				.getTypeAbsenceList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		Collections.sort(typeAbsenceBeanList);

		int i = 0;
		List<TypeAbsenceBean> typeAbsenceInventoryTmp = new ArrayList<TypeAbsenceBean>();
		for (TypeAbsenceBean typeAbsenceBean : typeAbsenceBeanList) {
			typeAbsenceInventoryTmp.add(typeAbsenceBean);
		}
		for (TypeAbsenceBean typeAbsenceBean : typeAbsenceBeanList) {
			if (typeAbsenceBean.getNom().startsWith("Autre")) {
				typeAbsenceInventoryTmp.remove(typeAbsenceBean);
				typeAbsenceInventoryTmp.add(typeAbsenceBean);
				i++;
				if (i == 2) {
					break;
				}
			}
		}
		typeAbsenceBeanList = typeAbsenceInventoryTmp;

		typeAbsenceList = new ArrayList<SelectItem>();
		SelectItem selectItem;
		for (TypeAbsenceBean typeAbsenceBean : typeAbsenceBeanList) {
			if (typeAbsenceBean.getId() != 1 && typeAbsenceBean.getId() != 2
					&& typeAbsenceBean.getId() != 4) {
				selectItem = new SelectItem();
				selectItem.setValue(typeAbsenceBean.getId());
				selectItem.setLabel(typeAbsenceBean.getNom());
				typeAbsenceList.add(selectItem);
			}
		}

		this.id = 0;
		this.debutAbsence = null;
		this.finAbsence = null;
		this.nombreJourOuvre = null;
		this.idTypeAbsenceSelected = -1;
		this.indexSelectedRow = -1;
		this.generationAuto = false;
		this.isModif = false;

	}

	public void initSalarieAbsenceForm() throws Exception {
		init();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		AbsenceServiceImpl serv = new AbsenceServiceImpl();

		if (id != 0 && serv.getJustificatif(id) != null) {
			String justif = Utils.getSessionFileUploadPath(session,
					salarieFormBB.getId(), "absence", 0, false, false,
					salarieFormBB.getNomGroupe())
					+ serv.getJustificatif(id);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(justif).getName());
			fileInfo.setFile(new File(justif));

			fileListAbsenceTemp.clear();
			fileListAbsenceTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("absence")));
		}
		modalRendered = !modalRendered;
		add = true;
	}

	public void uploadFileAbsence(ActionEvent event) throws Exception {

		InputFile inputFile = (InputFile) event.getSource();

		FileInfo fileInfo = inputFile.getFileInfo();

		if (fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.",
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:inputFileNameAbsence",
					message);
			return;
		}

		if (fileInfo.getStatus() == FileInfo.SAVED) {

			fileListAbsenceTemp.clear();
			fileListAbsenceTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("absence")));
			if (isModif) {
				newFile = true;
			}
		}
	}

	public void fileUploadProgressAbsence(EventObject event) {

		InputFile ifile = (InputFile) event.getSource();

		fileProgressAbsence = ifile.getFileInfo().getPercent();
	}

	public void remove(ActionEvent evt) {
		// modalRenderedDelFile = true;
	}

	public void cancelRemove(ActionEvent evt) {
		modalRenderedDelFile = false;
	}

	public void removeUploadedFileAbsence(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AbsenceBean absenceBean = (AbsenceBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		absenceBean.setJustificatif(null);
		AbsenceServiceImpl serv = new AbsenceServiceImpl();
		serv.saveOrUppdate(absenceBean);
		this.id = absenceBean.getId();

		fileListAbsenceTemp.clear();

		init = true;
	}

	public void download(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AbsenceBean absenceBean;
		InputFileData fileData;
		String fileName = "";
		String filePath = "";
		if (add || isModif) {
			fileData = (InputFileData) table.getRowData();
			fileName = fileData.getFile().getName();
			filePath = fileData.getPath();
		} else {
			absenceBean = (AbsenceBean) table.getRowData();
			fileName = absenceBean.getJustif().getName();
			filePath = absenceBean.getJustificatif();
		}
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		filePath = Utils.getSessionFileUploadPath(session, getIdSalarie(),
				"absence", 0, false, false, salarieFormBB.getNomGroupe())
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

	public void removeUploadedFileAbsenceTemp(ActionEvent event) {
		modalRenderedDelFile = false;

		if (fileListAbsenceTemp.get(0).getFile().exists()) {
			deletedFiles.add(fileListAbsenceTemp.get(0).getFile());
		}
		fileListAbsenceTemp.clear();
		this.fileProgressAbsence = 0;
		newFile = false;
	}

	public int getFileProgressAbsence() {
		return fileProgressAbsence;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDebutAbsence() {
		return debutAbsence;
	}

	public void setDebutAbsence(Date debutAbsence) {
		this.debutAbsence = debutAbsence;
	}

	public Date getFinAbsence() {
		return finAbsence;
	}

	public void setFinAbsence(Date finAbsence) {
		this.finAbsence = finAbsence;
	}

	public boolean isModalRendered() {
		return modalRendered;
	}

	public void setModalRendered(boolean modalRendered) {
		this.modalRendered = modalRendered;
	}

	public void toggleModal(ActionEvent event) {

		if (newFile) {
			if (!fileListAbsenceTemp.isEmpty()
					&& fileListAbsenceTemp.get(0).getFile().exists()) {
				fileListAbsenceTemp.clear();
			}
			newFile = false;
		}
		deletedFiles.clear();
		modalRendered = !modalRendered;
	}

	public boolean isModalRenderedAbs() {
		return modalRenderedAbs;
	}

	public void setModalRenderedAbs(boolean modalRenderedAbs) {
		this.modalRenderedAbs = modalRenderedAbs;
	}

	public void toggleModalRupture(ActionEvent event) {

		if (!fileListAbsenceTemp.isEmpty()) {
			fileListAbsenceTemp.clear();
		}

		modalRenderedAbs = !modalRenderedAbs;
	}

	public ArrayList<SelectItem> getTypeAbsenceList() {
		return typeAbsenceList;
	}

	public int getIdTypeAbsenceSelected() {
		return this.idTypeAbsenceSelected;
	}

	public void setIdTypeAbsenceSelected(int idTypeAbsenceSelected) {
		this.idTypeAbsenceSelected = idTypeAbsenceSelected;
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

	private boolean afterStrict(GregorianCalendar d1, GregorianCalendar d2) {
		// On teste si les dates sont egales, car GregorianCalendar ne prend pas
		// en comtpe ce cas, sinon on renvoi
		// true si d1 est avant d2
		if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
			if (d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)) {
				if (d1.get(Calendar.DATE) > d2.get(Calendar.DATE)) {
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

	private boolean beforeStrict(GregorianCalendar d1, GregorianCalendar d2) {
		// On teste si les dates sont egales, car GregorianCalendar ne prend pas
		// en comtpe ce cas, sinon on renvoi
		// true si d1 est avant d2
		if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
			if (d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)) {
				if (d1.get(Calendar.DATE) < d2.get(Calendar.DATE)) {
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

	private AbsenceBean isChevaucheDateAbsence(SalarieFormBB salarieFormBB,
			Date d1, Date d2, Double d) throws Exception {

		AbsenceServiceImpl absServ = new AbsenceServiceImpl();
		List<AbsenceBean> absList = absServ.getAbsenceBeanListByIdSalarie(this
				.getIdSalarie());

		for (AbsenceBean absBean : absList) {
			if (absBean.getDebutAbsence() != null
					&& absBean.getFinAbsence() != null) {
				if (absBean.getId() != this.id) {
					GregorianCalendar debD1 = new GregorianCalendar();
					debD1.setTime(absBean.getDebutAbsence());
					GregorianCalendar finD1 = new GregorianCalendar();
					finD1.setTime(absBean.getFinAbsence());
					GregorianCalendar debD2 = new GregorianCalendar();
					debD2.setTime(debutAbsence);
					GregorianCalendar finD2 = new GregorianCalendar();
					finD2.setTime(finAbsence);
					if (isInPeriode(debD1, finD1, debD2, finD2)) {
						if (chevaucheDemiJournee(debD1, finD1, debD2, finD2)
								&& debD2.equals(finD2)) {
							if ((d.toString().equals("0.5") || d.toString()
									.equals("0.50"))
									&& (absBean.getNombreJourOuvre().toString()
											.endsWith(".5") || absBean
											.getNombreJourOuvre().toString()
											.endsWith(".50"))) {
								return null;
							} else {
								return absBean;
							}
						} else {
							if (chevaucheDemiJournee(debD1, finD1, debD2, finD2)
									&& (d.toString().endsWith(".5") || d
											.toString().endsWith(".50"))
									&& (absBean.getNombreJourOuvre().toString()
											.endsWith(".5") || absBean
											.getNombreJourOuvre().toString()
											.endsWith(".50"))) {
								return null;
							} else {
								return absBean;
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

	public void checkPeriode(ValueChangeEvent evt) throws Exception {
		if (evt != null) {
			if (evt.getComponent().getId().equals("dateFinAbsence")) {
				finAbsence = (Date) evt.getNewValue();
			}
			if (evt.getComponent().getId().equals("dateDebutAbsence")) {
				debutAbsence = (Date) evt.getNewValue();
			}
		}
		if (debutAbsence != null && finAbsence != null && !isGenerationAuto()) {
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
				AbsenceBean absenceBean = new AbsenceBean();

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
				if (this.id == -1) {
					this.id = 0;
					if (isModif == true) {
						AbsenceBean absenceBeanTmp = salarieFormBB
								.getAbsenceBeanList().get(indexSelectedRow);

						absenceBeanTmp.setId(0);
						salarieFormBB.getAbsenceBeanList().set(
								indexSelectedRow, absenceBeanTmp);
					}
				}

				Double d = (nombreJourOuvre.equals("")) ? 0.0 : Double
						.valueOf(nombreJourOuvre);
				if (isChevaucheDateAbsence(salarieFormBB, debutAbsence,
						finAbsence, d) != null) {

					AbsenceBean f = isChevaucheDateAbsence(salarieFormBB,
							debutAbsence, finAbsence, d);
					if (((f.getDebutAbsence().equals(finAbsence) && (debutAbsence
							.before(f.getDebutAbsence()) || debutAbsence
							.equals(finAbsence))))
							|| (f.getFinAbsence().equals(debutAbsence) && (finAbsence
									.after(f.getFinAbsence()) || debutAbsence
									.equals(finAbsence)))) {
						if (debutAbsence.equals(finAbsence)
								&& !nombreJourOuvre.equals("0.5")
								&& !nombreJourOuvre.equals("0.50")) {

							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Une absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e ce jour, vous devez saisir 0.5 jour pour ajouter une demi-journ\u00E9e d'absence.",
									"Une absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e ce jour, vous devez saisir 0.5 jour pour ajouter une demi-journ\u00E9e d'absence.");
							FacesContext
									.getCurrentInstance()
									.addMessage(
											"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
											message);
							return;

						}
					} else {
						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Une autre absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.",
								"Une autre absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e pendant ces dates.");
						FacesContext
								.getCurrentInstance()
								.addMessage(
										"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
										message);
						return;
					}
				}
			}
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

	public void saveOrUpdateSalarieAbsence() throws Exception {
		modalRenderedAbs = false;
		newFile = false;
		// Teste si date fin > date debut

		if (this.idTypeAbsenceSelected != -1 && this.debutAbsence != null
				&& this.finAbsence != null && this.nombreJourOuvre != null
				&& !this.nombreJourOuvre.equals("")) {

			Calendar debut = new GregorianCalendar();
			Calendar fin = new GregorianCalendar();
			debut.setTime(debutAbsence);
			fin.setTime(finAbsence);

			if (debut.after(fin) && !isGenerationAuto()) {
				// Erreur

				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"La date de fin est ant\u00E9rieure \u00E0 celle du d\u00E9but",
						"La date de fin d'absence est ant\u00E9rieure \u00E0 celle du d\u00E9but");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
						message);
				fail = true;
				return;

			} else {
				AbsenceBean absenceBean = new AbsenceBean();

				List<ParcoursBean> listParcoursSalarie = salarieFormBB
						.getParcoursBeanList();

				if (!Utils.isInEntreprise(salarieFormBB.getParcoursBeanList(),
						debutAbsence, finAbsence) && !isGenerationAuto()) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Aucune pr\u00E9sence \u00e0 cette date.",
							"Aucune pr\u00E9sence \u00e0 cette date.");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
							message);
					fail = true;
					return;
				}
				Double d = (nombreJourOuvre.equals("")) ? 0.0 : Double
						.valueOf(nombreJourOuvre);
				if (isChevaucheDateAbsence(salarieFormBB, debutAbsence,
						finAbsence, d) != null && !isGenerationAuto()) {

					if (debutAbsence.equals(finAbsence)
							&& !nombreJourOuvre.equals("0.5")
							&& !nombreJourOuvre.equals("0.50")) {
						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Une absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e ce jour, vous devez saisir 0.5 jour pour ajouter une demi-journ\u00E9e d'absence.",
								"Une absence est d\u00E9j\u00E0 d\u00E9clar\u00E9e ce jour, vous devez saisir 0.5 jour pour ajouter une demi-journ\u00E9e d'absence.");
						FacesContext
								.getCurrentInstance()
								.addMessage(
										"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
										message);
						fail = true;
						return;
					} else {
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

				absenceBean.setId(this.id);
				absenceBean.setIdSalarie(this.getIdSalarie());
				absenceBean.setFinAbsence(this.finAbsence);
				absenceBean.setDebutAbsence(this.debutAbsence);
				absenceBean
						.setIdTypeAbsenceSelected(this.idTypeAbsenceSelected);
				absenceBean.setNombreJourOuvre(Double
						.valueOf(this.nombreJourOuvre));
				if (!fileListAbsenceTemp.isEmpty()) {

					absenceBean.setJustificatif(fileListAbsenceTemp.get(0)
							.getFile().getName());

				}

				TypeAbsenceServiceImpl typeAbsenceService = new TypeAbsenceServiceImpl();

				String nomTypeAbsence = new String();

				try {
					nomTypeAbsence = typeAbsenceService.getTypeAbsenceBeanById(
							this.idTypeAbsenceSelected).getNom();
				} catch (Exception e) {
					e.printStackTrace();
				}

				absenceBean.setNomTypeAbsence(nomTypeAbsence);

				if (isModif == true) {
					salarieFormBB.getAbsenceBeanList().set(indexSelectedRow,
							absenceBean);
					isModif = false;
				} else {
					AbsenceServiceImpl absServ = new AbsenceServiceImpl();
					absServ.saveOrUppdate(absenceBean);
					salarieFormBB.getAbsenceBeanList().add(absenceBean);

				}

				salarieFormBB.saveOrUpdateSalarie();
				modalRendered = !modalRendered;
			}
		} else {
			if (this.idTypeAbsenceSelected == -1) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idtypeAbsenceList",
						message);
				fail = true;
			}
			if (this.debutAbsence == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:dateDebutAbsence",
						message);
				fail = true;
			}
			if (this.finAbsence == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:dateFinAbsence",
						message);
				fail = true;
			}
			if (this.nombreJourOuvre == null || this.nombreJourOuvre.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire",
						"Champ obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idJourOuvre", message);
				fail = true;
			}
		}

	}

	public void modifAbsence(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AbsenceBean absenceBean = (AbsenceBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		this.fileProgressAbsence = 0;

		this.id = absenceBean.getId();
		this.finAbsence = absenceBean.getFinAbsence();
		this.debutAbsence = absenceBean.getDebutAbsence();

		AccidentServiceImpl accServ = new AccidentServiceImpl();
		FormationServiceImpl formServ = new FormationServiceImpl();
		if (accServ.getAccidentByIdAbsence(absenceBean.getId(), true) != null
				|| formServ.getFormationByIdAbsence(absenceBean.getId(), true) != null) {
			this.generationAuto = true;
			this.idTypeAbsenceSelected = absenceBean.getIdTypeAbsenceSelected();
			this.libelleTypeAbsence = absenceBean.getNomTypeAbsence();
		} else {
			this.generationAuto = false;
			this.idTypeAbsenceSelected = absenceBean.getIdTypeAbsenceSelected();
			this.libelleTypeAbsence = "";
		}

		this.nombreJourOuvre = absenceBean.getNombreJourOuvre().toString();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		fileListAbsenceTemp.clear();
		if (absenceBean.getJustificatif() != null) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(absenceBean.getJustificatif())
					.getName());
			fileInfo.setFile(new File(absenceBean.getJustificatif()));

			fileListAbsenceTemp.clear();
			fileListAbsenceTemp.add(new InputFileData(fileInfo, salarieFormBB
					.getUrl("absence")));
		}

		isModif = true;

		modalRendered = !modalRendered;

		checkPeriode(null);

	}

	public String deleteAbsence(ActionEvent evt) throws Exception {
		// On récupère la datatable.
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AbsenceBean absenceBean = (AbsenceBean) table.getRowData();
		// On récupère aussi son index
		int index = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		salarieFormBB.getAbsenceBeanList().remove(index);

		if (absenceBean.getId() > 0) {
			AbsenceServiceImpl absenceService = new AbsenceServiceImpl();
			absenceService.deleteAbsence(absenceBean);
			salarieFormBB.initSalarieForm();

		}
		return "salaries";
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

	public int getIdSalarie() {
		return salarieFormBB.getId();
	}

	public void getRupture() throws Exception {

		List<ParcoursBean> listParcoursSalarie = salarieFormBB
				.getParcoursBeanList();
		int nbrJour = 0;
		if (debutAbsence != null && finAbsence != null) {
			nbrJour = getNombreJourRupture(listParcoursSalarie, debutAbsence,
					finAbsence);
		}
		if (nbrJour > 1) {
			modalRenderedAbs = !modalRenderedAbs;
		} else {
			modalRenderedAbs = !modalRenderedAbs;
			saveOrUpdateSalarieAbsence();
		}
		if (!fail) {
			init = true;
			deletedFiles.clear();
			add = false;
			isModif = false;
		} else
			fail = false;

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

	public void warnGeneration(ValueChangeEvent event) {
		if (event.getNewValue().toString().equals("1")
				|| event.getNewValue().toString().equals("2")
				|| event.getNewValue().toString().equals("16")
				|| event.getNewValue().toString().equals("17")) {
			generationAuto = true;
		} else
			generationAuto = false;
	}

	public boolean isGenerationAuto() {
		AccidentServiceImpl accServ = new AccidentServiceImpl();
		FormationServiceImpl formServ = new FormationServiceImpl();
		if (accServ.getAccidentByIdAbsence(this.id, true) != null
				|| formServ.getFormationByIdAbsence(this.id, true) != null) {
			return true;
		} else {
			return false;
		}
	}

	public void setGenerationAuto(boolean generationAuto) {
		this.generationAuto = generationAuto;
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

	public void setId(int id) {
		this.id = id;
	}

	public void setTypeAbsenceList(ArrayList<SelectItem> typeAbsenceList) {
		this.typeAbsenceList = typeAbsenceList;
	}

	public void setFileProgressAbsence(int fileProgressAbsence) {
		this.fileProgressAbsence = fileProgressAbsence;
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

	public List<InputFileData> getFileListAbsenceTemp() {
		if (!fileListAbsenceTemp.isEmpty()
				&& (!fileListAbsenceTemp.get(0).getFile().exists()
						|| !fileListAbsenceTemp.get(0).getFile().isFile() || !fileListAbsenceTemp
						.get(0).getFile().canRead())) {
			fileError = true;
		} else {
			fileError = false;
		}
		return fileListAbsenceTemp;
	}

	public void setFileListAbsenceTemp(List<InputFileData> fileListAbsenceTemp) {
		this.fileListAbsenceTemp = fileListAbsenceTemp;
	}

	public String getLibelleTypeAbsence() {
		return libelleTypeAbsence;
	}

	public void setLibelleTypeAbsence(String libelleTypeAbsence) {
		this.libelleTypeAbsence = libelleTypeAbsence;
	}

	public boolean isModalRenderedDelFile() {
		return modalRenderedDelFile;
	}

	public void setModalRenderedDelFile(boolean modalRenderedDelFile) {
		this.modalRenderedDelFile = modalRenderedDelFile;
	}

	public void setIdSalarie(int idSalarie) {
		this.idSalarie = idSalarie;
	}

}
