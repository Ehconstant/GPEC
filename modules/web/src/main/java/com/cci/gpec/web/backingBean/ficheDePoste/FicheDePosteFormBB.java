package com.cci.gpec.web.backingBean.ficheDePoste;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.FicheDePosteBean;
import com.cci.gpec.commons.FicheMetierBean;
import com.cci.gpec.commons.FicheMetierEntrepriseBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.StatutBean;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.FicheDePosteServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierEntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.BackingBeanForm;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.effects.JavascriptContext;

public class FicheDePosteFormBB extends BackingBeanForm {

	private int id;

	private Date dateCreation;
	private Date dateModification;
	private String dateCreationAffiche;
	private String dateModificationAffiche;
	private String activitesSpecifiques;
	private String competencesSpecifiques;
	private String competencesSpecifiquesTexte;
	private String competencesNouvelles;
	private String competencesNouvelles2;
	private String competencesNouvelles3;
	private String competencesNouvelles4;
	private String competencesNouvelles5;
	private String commentaire;
	private int idSalarie;
	private int idSalarieChange = 0;
	private int idFicheMetierType = -1;
	private String intituleMetier;
	private List<ParcoursBean> parcoursList;
	private List<SelectItem> ficheMetierList;
	private String csp;
	private String savoir = "2";
	private String savoirFaire = "2";
	private String savoirEtre = "2";
	private String evaluationGlobale;
	private String activiteResponsabilite;
	private String ancienneteEntreprise;
	private String anciennetePoste;
	private String categorieCompetence;
	private String categorieCompetence2;
	private String categorieCompetence3;
	private String categorieCompetence4;
	private String categorieCompetence5;
	private String justificatif;
	private boolean modalRendered = false;
	private boolean modalRenderedImpression = false;
	private String libelleService;

	// Descendant de la fiche métier
	private String savoirMetier;
	private String savoirFaireMetier;
	private String savoirEtreMetier;
	private String particularitePoste;
	private String cspRef;
	// private String mobiliteEnvisagee;
	private String finalitePoste;
	private String niveauFormationRef;
	private String[] index;
	private String[] heading;
	private DataModel rowModel;
	private DataModel columnsModel;

	private int indexSelectedRow = -1;

	private boolean newSalarie = true;

	private boolean initFiche = false;

	private boolean modalRenderedAideComp = false;

	private boolean modalRenderedDelFile = false;

	private ArrayList<String> listeFicheDePoste = new ArrayList<String>();

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance().getCurrentInstance().getExternalContext()
			.getSessionMap().get("salarieFormBB");
	private List<InputFileData> fileListFicheDePosteTemp = new ArrayList<InputFileData>();
	private String currentFilePath;

	private int fileProgressFicheDePoste = 0;

	private boolean fileError = false;

	private boolean alreadyExist = false;

	private boolean isModif = false;
	private boolean add = false;
	private boolean init = true;
	private boolean newFile = false;

	public void setFileProgressFicheDePoste(int fileProgressFicheDePoste) {
		this.fileProgressFicheDePoste = fileProgressFicheDePoste;
	}

	public boolean isFileError() {
		return fileError;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public List<InputFileData> getFileListFicheDePosteTemp() {
		if (!fileListFicheDePosteTemp.isEmpty()
				&& (!fileListFicheDePosteTemp.get(0).getFile().exists()
						|| !fileListFicheDePosteTemp.get(0).getFile().isFile() || !fileListFicheDePosteTemp
						.get(0).getFile().canRead())) {
			fileError = true;
		} else {
			fileError = false;
		}
		return fileListFicheDePosteTemp;
	}

	public ArrayList<String> getListeFicheDePoste() {
		return listeFicheDePoste;
	}

	public void setListeFicheDePoste(ArrayList<String> listeFicheDePoste) {
		this.listeFicheDePoste = listeFicheDePoste;
	}

	public String getActiviteResponsabilite() {
		return activiteResponsabilite;
	}

	public void setActiviteResponsabilite(String activiteResponsabilite) {
		this.activiteResponsabilite = activiteResponsabilite;
	}

	public FicheDePosteFormBB() throws Exception {
		super();
	}

	public FicheDePosteFormBB(int id, String nom) throws Exception {
		super(id, nom);
	}

	public void initSalariePosteForm() throws Exception {
		initFiche = false;
		init();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		FicheDePosteServiceImpl serv = new FicheDePosteServiceImpl();

		fileListFicheDePosteTemp.clear();
		if (id != 0 && serv.getJustificatif(id) != null) {
			String justif = Utils.getSessionFileUploadPath(session,
					salarieFormBB.getId(), "ficheDePoste", 0, false, false,
					salarieFormBB.getNomGroupe())
					+ serv.getJustificatif(id);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(new File(justif).getName());
			fileInfo.setFile(new File(justif));

			fileListFicheDePosteTemp.add(new InputFileData(fileInfo,
					salarieFormBB.getUrl("ficheDePoste")));
		}
		modalRendered = !modalRendered;
		add = true;
	}

	public void imprimer() throws Exception {
		this.modalRenderedImpression = true;
	}

	public boolean isModalRendered() {
		return modalRendered;
	}

	public void setModalRendered(boolean modalRendered) {
		this.modalRendered = modalRendered;
	}

	public void toggleModal(ActionEvent event) throws Exception {
		if (newFile) {
			if (!fileListFicheDePosteTemp.isEmpty()
					&& fileListFicheDePosteTemp.get(0).getFile().exists()) {
				fileListFicheDePosteTemp.clear();
			}
			newFile = false;
		}
		initFiche = false;
		modalRendered = !modalRendered;
	}

	public void init() throws Exception {
		this.isModif = false;

		this.fileProgressFicheDePoste = 0;
		if (init)
			this.fileListFicheDePosteTemp.clear();

		init = false;
		alreadyExist = false;

		ServiceImpl service = new ServiceImpl();
		SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
				.getCurrentInstance().getCurrentInstance().getExternalContext()
				.getSessionMap().get("salarieFormBB");

		if (!initFiche) {
			this.idFicheMetierType = -1;
			this.cspRef = "";
			this.finalitePoste = "";
			this.savoirMetier = "";
			this.savoirFaireMetier = "";
			this.savoirEtreMetier = "";
			particularitePoste = "";
			this.activiteResponsabilite = "";
			this.niveauFormationRef = "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		FicheDePosteServiceImpl ficheDePosteService = new FicheDePosteServiceImpl();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("idSalarie") != null) {
			this.idSalarie = Integer.parseInt(session.getAttribute("idSalarie")
					.toString());
		} else {
			this.idSalarie = -1;
		}

		if (idSalarie != -1) {
			newSalarie = true;
			SalarieServiceImpl salarieService = new SalarieServiceImpl();

			// recupération du libellé service pour un salarié existant
			this.libelleService = service.getServiceBeanById(
					salarieFormBB.getIdServiceSelected()).getNom();

			SalarieBean salarieBean = salarieService
					.getSalarieBeanById(this.idSalarie);
			parcoursList = salarieBean.getParcoursBeanList();
			Collections.sort(parcoursList);
			Collections.reverse(parcoursList);

			if (parcoursList.size() == 0) {
				newSalarie = false;
				this.dateCreationAffiche = "-";
				this.dateModificationAffiche = "-";
				return;
			} else {
				this.intituleMetier = parcoursList.get(0).getNomMetier();
				this.csp = parcoursList.get(0).getNomTypeStatut();
			}

			List<FicheMetierEntrepriseBean> ficheMetierEntreprisesInventory = new ArrayList<FicheMetierEntrepriseBean>();
			FicheMetierEntrepriseServiceImpl ficheMetierEntreprise = new FicheMetierEntrepriseServiceImpl();
			ficheMetierEntreprisesInventory = ficheMetierEntreprise
					.getFicheMetierEntrepriseBeanListByIdEntreprise(salarieBean
							.getIdEntrepriseSelected());

			List<FicheMetierBean> ficheMetierInventory = new ArrayList<FicheMetierBean>();
			ficheMetierInventory = new ArrayList<FicheMetierBean>();
			for (FicheMetierEntrepriseBean ficheMetierEntrepriseBean : ficheMetierEntreprisesInventory) {
				FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
				FicheMetierBean ficheMetierBean = ficheMetierService
						.getFicheMetierBeanById(ficheMetierEntrepriseBean
								.getIdFicheMetier());
				ficheMetierInventory.add(ficheMetierBean);
			}

			Collections.sort(ficheMetierInventory);

			ficheMetierList = new ArrayList<SelectItem>();
			SelectItem selectItem;

			for (FicheMetierBean ficheMetierBean : ficheMetierInventory) {
				selectItem = new SelectItem();
				selectItem.setValue(ficheMetierBean.getId());
				selectItem.setLabel(ficheMetierBean.getNom());
				ficheMetierList.add(selectItem);
			}

			this.ancienneteEntreprise = getAncienneteEntreprise(salarieBean);
			this.anciennetePoste = getAnciennetePoste(salarieBean);

			if (!initFiche)
				try {
					FicheDePosteBean ficheDePosteBean = ficheDePosteService
							.getFicheDePosteBeanByIdSalarie(this.idSalarie);
					this.dateCreation = ficheDePosteBean.getDateCreation();
					this.dateModification = ficheDePosteBean
							.getDateModification();
					this.dateCreationAffiche = dateFormat.format(dateCreation);
					this.dateModificationAffiche = dateFormat
							.format(dateModification);
					this.activitesSpecifiques = ficheDePosteBean
							.getActivitesSpecifiques();
					this.competencesSpecifiques = ficheDePosteBean
							.getCompetencesSpecifiques();
					this.competencesSpecifiquesTexte = ficheDePosteBean
							.getCompetencesSpecifiquesTexte();
					this.competencesNouvelles = ficheDePosteBean
							.getCompetencesNouvelles();
					this.competencesNouvelles2 = ficheDePosteBean
							.getCompetencesNouvelles2();
					this.competencesNouvelles3 = ficheDePosteBean
							.getCompetencesNouvelles3();
					this.competencesNouvelles4 = ficheDePosteBean
							.getCompetencesNouvelles4();
					this.competencesNouvelles5 = ficheDePosteBean
							.getCompetencesNouvelles5();
					this.commentaire = ficheDePosteBean.getCommentaire();
					this.idFicheMetierType = ficheDePosteBean
							.getIdFicheMetierType();
					this.id = ficheDePosteBean.getId();
					this.savoir = ficheDePosteBean.getSavoir();
					this.savoirFaire = ficheDePosteBean.getSavoirFaire();
					this.savoirEtre = ficheDePosteBean.getSavoirEtre();
					this.evaluationGlobale = ficheDePosteBean
							.getEvaluationGlobale();
					this.categorieCompetence = ficheDePosteBean
							.getCategorieCompetence();
					this.categorieCompetence2 = ficheDePosteBean
							.getCategorieCompetence2();
					this.categorieCompetence3 = ficheDePosteBean
							.getCategorieCompetence3();
					this.categorieCompetence4 = ficheDePosteBean
							.getCategorieCompetence4();
					this.categorieCompetence5 = ficheDePosteBean
							.getCategorieCompetence5();
					this.justificatif = ficheDePosteBean.getJustificatif();
					this.fileError = ficheDePosteBean.isFileError();
					autoFill(this.idFicheMetierType);

				} catch (Exception e) {
					this.id = 0;
					this.dateCreation = new Date();
					this.dateModification = new Date();
					this.dateCreationAffiche = "-";
					this.dateModificationAffiche = "-";
					this.activitesSpecifiques = "";
					this.competencesSpecifiques = "2";
					this.competencesSpecifiquesTexte = "";
					this.competencesNouvelles = "";
					this.competencesNouvelles2 = "";
					this.competencesNouvelles3 = "";
					this.competencesNouvelles4 = "";
					this.competencesNouvelles5 = "";
					this.commentaire = "";
					this.savoir = "2";
					this.savoirFaire = "2";
					this.savoirEtre = "2";
					this.evaluationGlobale = "2";
					this.categorieCompetence = "";
					this.categorieCompetence2 = "";
					this.categorieCompetence3 = "";
					this.categorieCompetence4 = "";
					this.categorieCompetence5 = "";
					this.justificatif = "";
					this.fileError = false;
					this.intituleMetier = "Fiche non créée";
				}
		}
		initFiche = true;

		index = new String[] { "Intitulé du poste", "Service",
				"Date de création", "Date de modification", "Justificatif",
				"Action" };
		heading = new String[] { "test" };
		rowModel = new ArrayDataModel(index);
		columnsModel = new ArrayDataModel(heading);
	}

	public void uploadFileFicheDePoste(ActionEvent event) throws Exception {

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
							"idSalarieForm:idSalarieTabSet:0:inputFileNameFicheDePoste",
							message);
			return;
		}

		if (fileInfo.getStatus() == FileInfo.SAVED) {

			fileListFicheDePosteTemp.clear();
			fileListFicheDePosteTemp.add(new InputFileData(fileInfo,
					salarieFormBB.getUrl("ficheDePoste")));
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
		for (int i = 0; i < (fileListFicheDePosteTemp.size() - 1); i++) {
			for (int j = (i + 1); j < fileListFicheDePosteTemp.size(); j++) {
				if (((InputFileData) fileListFicheDePosteTemp.get(i))
						.getFileInfo()
						.getFileName()
						.equals(((InputFileData) fileListFicheDePosteTemp
								.get(j)).getFileInfo().getFileName())) {
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

	public void download(ActionEvent evt) throws Exception {

		InputFileData fileData;
		String fileName = "";
		String filePath = "";
		if (add || isModif) {
			fileData = fileListFicheDePosteTemp.get(0);
			fileName = fileData.getFile().getName();
			filePath = fileData.getPath();
		} else {
			fileName = new File(this.getJustificatif()).getName();
			filePath = this.getJustificatif();
		}

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		filePath = Utils.getSessionFileUploadPath(session, idSalarie,
				"ficheDePoste", 0, false, false, salarieFormBB.getNomGroupe())
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

	public void fileUploadProgressFicheDePoste(EventObject event) {

		InputFile ifile = (InputFile) event.getSource();

		fileProgressFicheDePoste = ifile.getFileInfo().getPercent();
	}

	public void remove(ActionEvent evt) {
		modalRenderedDelFile = true;
	}

	public void cancelRemove(ActionEvent evt) {
		modalRenderedDelFile = false;
	}

	public void removeUploadedFileFicheDePoste(ActionEvent evt)
			throws Exception {

		FicheDePosteServiceImpl ficheDePosteService = new FicheDePosteServiceImpl();

		FicheDePosteBean ficheDePosteBean = ficheDePosteService
				.getFicheDePosteBeanByIdSalarie(this.idSalarie);

		if (ficheDePosteBean != null) {
			ficheDePosteBean.setJustificatif(null);
			ficheDePosteService.saveOrUppdate(ficheDePosteBean);
		}
		this.id = ficheDePosteBean.getId();

		this.fileListFicheDePosteTemp.clear();
		this.justificatif = "";

		init = true;
		init();
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

	public void removeUploadedFileFicheDePosteTemp(ActionEvent event) {
		modalRenderedDelFile = false;
		fileListFicheDePosteTemp.clear();
		this.fileProgressFicheDePoste = 0;
		newFile = false;
	}

	public int getFileProgressFicheDePoste() {
		return fileProgressFicheDePoste;
	}

	public void cancelPrintFicheDePoste(ActionEvent evt) {
		this.modalRenderedImpression = false;
	}

	public void printFicheDePoste(ActionEvent evt) throws Exception {
		init();
		this.modalRenderedImpression = false;
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.printFicheDePoste?contentType=pdf \",\"_Reports\");");

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("idFicheDePoste", this.id);
		eContext.getSessionMap().put("display", "false");
		eContext.getSessionMap().put("nomGroupe", salarieFormBB.getNomGroupe());

	}

	public void printFicheDePoste2(ActionEvent evt) throws Exception {
		init();
		this.modalRenderedImpression = false;
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.printFicheDePoste?contentType=pdf \",\"_Reports\");");

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("idFicheDePoste", this.id);
		eContext.getSessionMap().put("display", "true");
		eContext.getSessionMap().put("nomGroupe", salarieFormBB.getNomGroupe());

	}

	public void change(ValueChangeEvent event) throws Exception {
		this.idFicheMetierType = Integer.parseInt(event.getNewValue()
				.toString());
		if (this.idFicheMetierType != -1) {
			autoFill(this.idFicheMetierType);
		}
	}

	public void autoFill(int idFicheMetier) throws Exception {
		FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
		FicheMetierBean ficheMetierBean = ficheMetierService
				.getFicheMetierBeanById(idFicheMetier);
		if (ficheMetierBean.getCspReference() != -1) {
			StatutServiceImpl statutService = new StatutServiceImpl();
			StatutBean statutBean = statutService
					.getStatutBeanById(ficheMetierBean.getCspReference());
			this.cspRef = statutBean.getNom();
		} else {
			this.cspRef = "Non renseigné";
		}
		this.finalitePoste = ficheMetierBean.getFinaliteFicheMetier();
		this.niveauFormationRef = ficheMetierBean.getNiveauFormationRequis();
		this.savoirMetier = ficheMetierBean.getSavoir();
		this.savoirFaireMetier = ficheMetierBean.getSavoirFaire();
		this.savoirEtreMetier = ficheMetierBean.getSavoirEtre();
		this.particularitePoste = ficheMetierBean.getParticularite();
		this.activiteResponsabilite = ficheMetierBean
				.getActiviteResponsabilite();
		this.intituleMetier = ficheMetierBean.getNom();
		if (this.competencesSpecifiquesTexte == "") {
			this.competencesSpecifiquesTexte = ficheMetierBean
					.getParticularite();
		}
	}

	private String getAncienneteEntreprise(SalarieBean salarie) {
		ParcoursBean parcourDeb = getFirstParcours(salarie);
		Calendar dateDebut = new GregorianCalendar();
		Calendar dateFin = new GregorianCalendar();
		if (parcourDeb != null) {
			dateDebut.setTime(parcourDeb.getDebutFonction());
		}
		ParcoursBean parcourFin = getLastParcours(salarie);
		if (parcourFin != null) {
			if (parcourFin.getFinFonction() != null
					&& (parcourFin.getFinFonction().before(new Date()) || parcourFin
							.getFinFonction().equals(new Date()))) {
				dateFin.setTime(parcourFin.getFinFonction());
			} else {
				dateFin.setTime(new Date());
			}
		}
		return dateFin.get(Calendar.YEAR) - dateDebut.get(Calendar.YEAR)
				+ " an(s)";
	}

	private String getAnciennetePoste(SalarieBean salarie) {

		Calendar dateDebut = new GregorianCalendar();
		Calendar dateFin = new GregorianCalendar();

		ParcoursBean parcourFin = getLastParcours(salarie);
		dateDebut.setTime(parcourFin.getDebutFonction());
		if (parcourFin != null) {
			if (parcourFin.getFinFonction() != null
					&& parcourFin.getFinFonction().before(new Date())) {
				dateFin.setTime(parcourFin.getFinFonction());
			} else {
				dateFin.setTime(new Date());
			}
		}
		return dateFin.get(Calendar.YEAR) - dateDebut.get(Calendar.YEAR)
				+ " an(s)";
	}

	private ParcoursBean getLastParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();

		ParcoursBean parcoursBean = null;
		if (parcourList != null && parcourList.size() > 0) {
			Collections.sort(parcourList);
			Collections.reverse(parcourList);
			parcoursBean = parcourList.get(0);
		}
		return parcoursBean;
	}

	private ParcoursBean getFirstParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		Collections.sort(parcourList);
		Collections.reverse(parcourList);
		ParcoursBean pb = null;
		Long diffMillis;
		Long diffenjours = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			if (pb == null) {
				pb = parcour;
			}
			int nbJours = 0;
			Calendar d1 = new GregorianCalendar();
			Calendar d2 = new GregorianCalendar();
			if (parcour.getFinFonction() != null) {
				d1.setTime(parcour.getFinFonction());
			} else {
				d1.setTime(new Date());
			}
			d2.setTime(pb.getDebutFonction());
			diffMillis = d2.getTimeInMillis() - d1.getTimeInMillis();

			diffenjours = diffMillis / (24 * 60 * 60 * 1000);
			if (parcour.getDebutFonction().before(pb.getDebutFonction())
					&& diffenjours <= 1) {
				pb = parcour;
			}
		}
		return pb;
	}

	public void saveOrUpdateFicheDePoste(ActionEvent event) throws Exception {
		FicheDePosteServiceImpl ficheDePosteService = new FicheDePosteServiceImpl();
		FicheDePosteBean ficheDePosteBean = new FicheDePosteBean();
		ficheDePosteBean.setId(this.id);
		Date date = new Date();
		if (this.id != 0) {
			ficheDePosteBean.setDateCreation(this.dateCreation);
		} else {
			ficheDePosteBean.setDateCreation(date);
		}
		ficheDePosteBean.setDateModification(date);
		ficheDePosteBean.setCompetencesNouvelles(this.competencesNouvelles);
		ficheDePosteBean.setCompetencesNouvelles2(this.competencesNouvelles2);
		ficheDePosteBean.setCompetencesNouvelles3(this.competencesNouvelles3);
		ficheDePosteBean.setCompetencesNouvelles4(this.competencesNouvelles4);
		ficheDePosteBean.setCompetencesNouvelles5(this.competencesNouvelles5);
		ficheDePosteBean.setCompetencesSpecifiques(this.competencesSpecifiques);
		ficheDePosteBean
				.setCompetencesSpecifiquesTexte(this.competencesSpecifiquesTexte);
		ficheDePosteBean.setActivitesSpecifiques(this.activitesSpecifiques);
		ficheDePosteBean.setIdSalarie(this.idSalarie);
		ficheDePosteBean.setIdFicheMetierType(this.idFicheMetierType);
		ficheDePosteBean.setSavoir(this.savoir);
		ficheDePosteBean.setSavoirFaire(this.savoirFaire);
		ficheDePosteBean.setSavoirEtre(this.savoirEtre);
		ficheDePosteBean.setEvaluationGlobale(this.evaluationGlobale);
		ficheDePosteBean.setCommentaire(this.commentaire);
		ficheDePosteBean.setCategorieCompetence(this.categorieCompetence);
		ficheDePosteBean.setCategorieCompetence2(this.categorieCompetence2);
		ficheDePosteBean.setCategorieCompetence3(this.categorieCompetence3);
		ficheDePosteBean.setCategorieCompetence4(this.categorieCompetence4);
		ficheDePosteBean.setCategorieCompetence5(this.categorieCompetence5);
		if (!fileListFicheDePosteTemp.isEmpty()) {
			ficheDePosteBean.setJustificatif(fileListFicheDePosteTemp.get(0)
					.getFile().getName());
			this.justificatif = fileListFicheDePosteTemp.get(0).getFile()
					.getName();
		} else {
			ficheDePosteBean.setJustificatif(null);
			this.justificatif = null;
		}

		ficheDePosteService.saveOrUppdate(ficheDePosteBean);

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"La fiche de poste est mise à jour. ",
				"La fiche de poste est mise à jour. ");
		FacesContext.getCurrentInstance().addMessage(
				"idSalarieForm:idSalarieTabSet:0:idSauvegarde", message);

		initFiche = false;
		modalRendered = !modalRendered;

	}

	public String annuler() throws Exception {
		return "saveOrUpdateFicheDePoste";
	}

	public int getId() {
		return id;
	}

	public void setId(int idFicheDePoste) {
		this.id = idFicheDePoste;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateModification() throws Exception {
		return dateModification;
	}

	public void setDateModification(Date dateModification) {
		this.dateModification = dateModification;
	}

	public String getActivitesSpecifiques() {
		return activitesSpecifiques;
	}

	public void setActivitesSpecifiques(String activitesSpecifiques) {
		this.activitesSpecifiques = activitesSpecifiques;
	}

	public String getCompetencesSpecifiques() {
		return competencesSpecifiques;
	}

	public void setCompetencesSpecifiques(String competencesSpecifiques) {
		this.competencesSpecifiques = competencesSpecifiques;
	}

	public String getCompetencesNouvelles() {
		return competencesNouvelles;
	}

	public void setCompetencesNouvelles(String competencesNouvelles) {
		this.competencesNouvelles = competencesNouvelles;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public int getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(int idSalarie) {
		this.idSalarie = idSalarie;
	}

	public int getIdFicheMetierType() {
		return idFicheMetierType;
	}

	public void setIdFicheMetierType(int idFicheMetierType) {
		this.idFicheMetierType = idFicheMetierType;
	}

	public String getIntituleMetier() {
		return intituleMetier;
	}

	public void setIntituleMetier(String intituleMetier) {
		this.intituleMetier = intituleMetier;
	}

	public List<ParcoursBean> getParcoursList() {
		return parcoursList;
	}

	public void setParcoursList(List<ParcoursBean> parcoursList) {
		this.parcoursList = parcoursList;
	}

	public String getCsp() {
		return csp;
	}

	public void setCsp(String csp) {
		this.csp = csp;
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

	public List<SelectItem> getFicheMetierList() {
		return ficheMetierList;
	}

	public void setFicheMetierList(List<SelectItem> ficheMetierList) {
		this.ficheMetierList = ficheMetierList;
	}

	public String getAncienneteEntreprise() {
		return ancienneteEntreprise;
	}

	public void setAncienneteEntreprise(String ancienneteEntreprise) {
		this.ancienneteEntreprise = ancienneteEntreprise;
	}

	public String getAnciennetePoste() {
		return anciennetePoste;
	}

	public void setAnciennetePoste(String anciennetePoste) {
		this.anciennetePoste = anciennetePoste;
	}

	public String getSavoirMetier() {
		return savoirMetier;
	}

	public void setSavoirMetier(String savoirMetier) {
		this.savoirMetier = savoirMetier;
	}

	public String getSavoirFaireMetier() {
		return savoirFaireMetier;
	}

	public void setSavoirFaireMetier(String savoirFaireMetier) {
		this.savoirFaireMetier = savoirFaireMetier;
	}

	public String getSavoirEtreMetier() {
		return savoirEtreMetier;
	}

	public void setSavoirEtreMetier(String savoirEtreMetier) {
		this.savoirEtreMetier = savoirEtreMetier;
	}

	public String getCspRef() {
		return cspRef;
	}

	public void setCspRef(String cspRef) {
		this.cspRef = cspRef;
	}

	public String getFinalitePoste() {
		return finalitePoste;
	}

	public void setFinalitePoste(String finalitePoste) {
		this.finalitePoste = finalitePoste;
	}

	public String getDateCreationAffiche() {
		return dateCreationAffiche;
	}

	public void setDateCreationAffiche(String dateCreationAffiche) {
		this.dateCreationAffiche = dateCreationAffiche;
	}

	public String getDateModificationAffiche() {
		return dateModificationAffiche;
	}

	public void setDateModificationAffiche(String dateModificationAffiche) {
		this.dateModificationAffiche = dateModificationAffiche;
	}

	public String getCompetencesSpecifiquesTexte() {
		return competencesSpecifiquesTexte;
	}

	public void setCompetencesSpecifiquesTexte(
			String competencesSpecifiquesTexte) {
		this.competencesSpecifiquesTexte = competencesSpecifiquesTexte;
	}

	public boolean isInitFiche() throws Exception {
		SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
				.getCurrentInstance().getCurrentInstance().getExternalContext()
				.getSessionMap().get("salarieFormBB");
		if (idSalarieChange == 0) {
			idSalarieChange = salarieFormBB.getId();
		}
		if (idSalarieChange != salarieFormBB.getId()) {
			initFiche = false;
			idSalarieChange = salarieFormBB.getId();
		}
		init();
		return initFiche;
	}

	public void setInitFiche(boolean initFiche) {
		this.initFiche = initFiche;
	}

	public String getEvaluationGlobale() {
		return evaluationGlobale;
	}

	public void setEvaluationGlobale(String evaluationGlobale) {
		this.evaluationGlobale = evaluationGlobale;
	}

	public boolean isNewSalarie() {
		return newSalarie;
	}

	public void setNewSalarie(boolean newSalarie) {
		this.newSalarie = newSalarie;
	}

	public String getCategorieCompetence() {
		return categorieCompetence;
	}

	public void setCategorieCompetence(String categorieCompetence) {
		this.categorieCompetence = categorieCompetence;
	}

	public String getNiveauFormationRef() {
		return niveauFormationRef;
	}

	public void setNiveauFormationRef(String niveauFormationRef) {
		this.niveauFormationRef = niveauFormationRef;
	}

	public String getLibelleService() {
		return libelleService;
	}

	public void setLibelleService(String libelleService) {
		this.libelleService = libelleService;
	}

	public String[] getIndex() {
		return index;
	}

	public void setIndex(String[] index) {
		this.index = index;
	}

	public String[] getHeading() {
		return heading;
	}

	public void setHeading(String[] heading) {
		this.heading = heading;
	}

	public DataModel getRowModel() {
		return rowModel;
	}

	public void setRowModel(DataModel rowModel) {
		this.rowModel = rowModel;
	}

	public DataModel getColumnsModel() {
		return columnsModel;
	}

	public void setColumnsModel(DataModel columnsModel) {
		this.columnsModel = columnsModel;
	}

	public String getCompetencesNouvelles2() {
		return competencesNouvelles2;
	}

	public void setCompetencesNouvelles2(String competencesNouvelles2) {
		this.competencesNouvelles2 = competencesNouvelles2;
	}

	public String getCompetencesNouvelles3() {
		return competencesNouvelles3;
	}

	public void setCompetencesNouvelles3(String competencesNouvelles3) {
		this.competencesNouvelles3 = competencesNouvelles3;
	}

	public String getCompetencesNouvelles4() {
		return competencesNouvelles4;
	}

	public void setCompetencesNouvelles4(String competencesNouvelles4) {
		this.competencesNouvelles4 = competencesNouvelles4;
	}

	public String getCompetencesNouvelles5() {
		return competencesNouvelles5;
	}

	public void setCompetencesNouvelles5(String competencesNouvelles5) {
		this.competencesNouvelles5 = competencesNouvelles5;
	}

	public String getCategorieCompetence2() {
		return categorieCompetence2;
	}

	public void setCategorieCompetence2(String categorieCompetence2) {
		this.categorieCompetence2 = categorieCompetence2;
	}

	public String getCategorieCompetence3() {
		return categorieCompetence3;
	}

	public void setCategorieCompetence3(String categorieCompetence3) {
		this.categorieCompetence3 = categorieCompetence3;
	}

	public String getCategorieCompetence4() {
		return categorieCompetence4;
	}

	public void setCategorieCompetence4(String categorieCompetence4) {
		this.categorieCompetence4 = categorieCompetence4;
	}

	public String getCategorieCompetence5() {
		return categorieCompetence5;
	}

	public void setCategorieCompetence5(String categorieCompetence5) {
		this.categorieCompetence5 = categorieCompetence5;
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

	public boolean isAlreadyExist() {
		return alreadyExist;
	}

	public void setAlreadyExist(boolean alreadyExist) {
		this.alreadyExist = alreadyExist;
	}

	public String getUrl() throws Exception {
		return salarieFormBB.getUrl("ficheDePoste");
	}

	public String getCurrentFilePath() {
		return currentFilePath;
	}

	public void setCurrentFilePath(String currentFilePath) {
		this.currentFilePath = currentFilePath;
	}

	public void setFileListFicheDePosteTemp(
			List<InputFileData> fileListFicheDePosteTemp) {
		this.fileListFicheDePosteTemp = fileListFicheDePosteTemp;
	}

	public String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(String justificatif) {
		this.justificatif = justificatif;
	}

	public boolean isModalRenderedDelFile() {
		return modalRenderedDelFile;
	}

	public void setModalRenderedDelFile(boolean modalRenderedDelFile) {
		this.modalRenderedDelFile = modalRenderedDelFile;
	}

	public String getParticularitePoste() {
		return particularitePoste;
	}

	public void setParticularitePoste(String particularitePoste) {
		this.particularitePoste = particularitePoste;
	}

	public boolean isModalRenderedImpression() {
		return modalRenderedImpression;
	}

	public void setModalRenderedImpression(boolean modalRenderedImpression) {
		this.modalRenderedImpression = modalRenderedImpression;
	}

}
