package com.cci.gpec.web.backingBean.rattachementFichier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.ContratTravailBean;
import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.EvenementBean;
import com.cci.gpec.commons.FicheDePosteBean;
import com.cci.gpec.commons.FicheMetierBean;
import com.cci.gpec.commons.FicheMetierEntrepriseBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.SalarieBeanExport;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.AccidentServiceImpl;
import com.cci.gpec.metier.implementation.ContratTravailServiceImpl;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.EntretienServiceImpl;
import com.cci.gpec.metier.implementation.EvenementServiceImpl;
import com.cci.gpec.metier.implementation.FicheDePosteServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierEntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierServiceImpl;
import com.cci.gpec.metier.implementation.FormationServiceImpl;
import com.cci.gpec.metier.implementation.HabilitationServiceImpl;
import com.cci.gpec.metier.implementation.ParcoursServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.login.LoginBB;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.context.effects.JavascriptContext;

public class RepriseDonneesBB {

	private int idSalarie;
	private String nomPrenom;

	private ArrayList<SelectItem> entrepriseList;
	private Integer idEntrepriseSelected;

	private String intituleMetierFicheDePoste;
	private String intituleServiceFicheDePoste;
	private List<FicheMetierEntrepriseBean> ficheMetierEntreprisesInventory = new ArrayList<FicheMetierEntrepriseBean>();

	private List<File> fileListAbs = new ArrayList<File>();
	private List<File> fileListAcc = new ArrayList<File>();
	private List<File> fileListForm = new ArrayList<File>();
	private List<File> fileListEnt = new ArrayList<File>();
	private List<File> fileListPar = new ArrayList<File>();
	private List<File> fileListHab = new ArrayList<File>();
	private List<File> fileListCv = new ArrayList<File>();
	private List<File> fileListFdp = new ArrayList<File>();
	private List<File> fileListFm = new ArrayList<File>();
	private List<File> fileListCt = new ArrayList<File>();
	private List<File> fileListEv = new ArrayList<File>();

	private List<SelectItem> fileItemListAbs;
	private List<SelectItem> fileItemListAcc;
	private List<SelectItem> fileItemListForm;
	private List<SelectItem> fileItemListEnt;
	private List<SelectItem> fileItemListPar;
	private List<SelectItem> fileItemListHab;
	private List<SelectItem> fileItemListCv;
	private List<SelectItem> fileItemListFdp;
	private List<SelectItem> fileItemListFm;
	private List<SelectItem> fileItemListCt;
	private List<SelectItem> fileItemListEv;

	private String fileSelected = "";

	private List<AbsenceBean> absenceBeanList = new ArrayList<AbsenceBean>();
	private List<AccidentBean> accidentBeanList = new ArrayList<AccidentBean>();
	private List<FormationBean> formationBeanList = new ArrayList<FormationBean>();
	private List<EntretienBean> entretienBeanList = new ArrayList<EntretienBean>();
	private List<ParcoursBean> parcoursBeanList = new ArrayList<ParcoursBean>();
	private List<HabilitationBean> habilitationBeanList = new ArrayList<HabilitationBean>();
	private List<SalarieBeanExport> salarieBeanList = new ArrayList<SalarieBeanExport>();
	private List<FicheDePosteBean> ficheDePosteBeanList = new ArrayList<FicheDePosteBean>();
	private List<FicheMetierBean> ficheMetierBeanList = new ArrayList<FicheMetierBean>();
	private List<ContratTravailBean> contratTravailBeanList = new ArrayList<ContratTravailBean>();
	private List<EvenementBean> evenementBeanList = new ArrayList<EvenementBean>();

	private List<SalarieBean> salarieList = new ArrayList<SalarieBean>();
	private List<SalarieBean> salarieListTemp = new ArrayList<SalarieBean>();

	private int indexSelectedRow;

	private String initList;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
			.getCurrentInstance()
			.getApplication()
			.evaluateExpressionGet(FacesContext.getCurrentInstance(),
					"#{salarieFormBB}", SalarieFormBB.class);

	LoginBB loginBB = (LoginBB) FacesContext
			.getCurrentInstance()
			.getApplication()
			.evaluateExpressionGet(FacesContext.getCurrentInstance(),
					"#{loginBB}", LoginBB.class);

	public RepriseDonneesBB() throws Exception {
		init();
	}

	public void init() throws Exception {

		if (idEntrepriseSelected == null) {
			this.idEntrepriseSelected = -1;
		}

		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();

		List<EntrepriseBean> entrepriseBeanList = entrepriseService
				.getEntreprisesList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		entrepriseList = new ArrayList<SelectItem>();
		SelectItem selectItem;
		for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(entrepriseBean.getId());
			selectItem.setLabel(entrepriseBean.getNom());
			entrepriseList.add(selectItem);
		}

		this.fileListAbs.clear();
		this.fileListAcc.clear();
		this.fileListEnt.clear();
		this.fileListForm.clear();
		this.fileListHab.clear();
		this.fileListPar.clear();
		this.fileListCv.clear();
		this.fileListFdp.clear();
		this.fileListFm.clear();
		this.fileListCt.clear();
		this.fileListEv.clear();

		this.indexSelectedRow = -1;

		this.salarieList.clear();
		this.salarieListTemp.clear();

		SalarieServiceImpl sal = new SalarieServiceImpl();
		if (idEntrepriseSelected != -1) {
			salarieList = sal
					.getSalarieBeanListByIdEntreprise(idEntrepriseSelected);
		}

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		File[] files = null;

		for (SalarieBean s : salarieList) {
			File repertoire = new File(Utils.getSessionFileUploadPath(session)
					+ "/" + salarieFormBB.getNomGroupe().replace(" ", "_")
					+ "/" + s.getId() + "/");
			if (repertoire.exists()) {
				files = repertoire.listFiles();
				if (files.length != 0) {
					if (!salarieListTemp.contains(s)) {
						salarieListTemp.add(s);
					}
				}
			}

			repertoire = new File(Utils.getSessionFileUploadPath(session,
					s.getId(), "absence", 1, false, false,
					salarieFormBB.getNomGroupe()));
			if (repertoire.exists()) {
				files = repertoire.listFiles();
				if (files.length != 0) {
					if (!salarieListTemp.contains(s)) {
						salarieListTemp.add(s);
					}
				}
			}
			repertoire = new File(Utils.getSessionFileUploadPath(session,
					s.getId(), "accident", 1, false, false,
					salarieFormBB.getNomGroupe()));
			if (repertoire.exists()) {
				files = repertoire.listFiles();
				if (files.length != 0) {
					if (!salarieListTemp.contains(s)) {
						salarieListTemp.add(s);
					}
				}
			}
			repertoire = new File(Utils.getSessionFileUploadPath(session,
					s.getId(), "formation", 1, false, false,
					salarieFormBB.getNomGroupe()));
			if (repertoire.exists()) {
				files = repertoire.listFiles();
				if (files.length != 0) {
					if (!salarieListTemp.contains(s)) {
						salarieListTemp.add(s);
					}
				}
			}
			repertoire = new File(Utils.getSessionFileUploadPath(session,
					s.getId(), "entretien", 1, false, false,
					salarieFormBB.getNomGroupe()));
			if (repertoire.exists()) {
				files = repertoire.listFiles();
				if (files.length != 0) {
					if (!salarieListTemp.contains(s)) {
						salarieListTemp.add(s);
					}
				}
			}
			repertoire = new File(Utils.getSessionFileUploadPath(session,
					s.getId(), "parcours", 1, false, false,
					salarieFormBB.getNomGroupe()));
			if (repertoire.exists()) {
				files = repertoire.listFiles();
				if (files.length != 0) {
					if (!salarieListTemp.contains(s)) {
						salarieListTemp.add(s);
					}
				}
			}

			repertoire = new File(Utils.getSessionFileUploadPath(session,
					s.getId(), "ficheDePoste", 1, false, false,
					salarieFormBB.getNomGroupe()));
			if (repertoire.exists()) {
				files = repertoire.listFiles();
				if (files.length != 0) {
					if (!salarieListTemp.contains(s)) {
						salarieListTemp.add(s);
					}
				}
			}
			repertoire = new File(Utils.getSessionFileUploadPath(session,
					s.getId(), "evenement", 1, false, false,
					salarieFormBB.getNomGroupe()));
			if (repertoire.exists()) {
				files = repertoire.listFiles();
				if (files.length != 0) {
					if (!salarieListTemp.contains(s)) {
						salarieListTemp.add(s);
					}
				}
			}

			repertoire = new File(Utils.getSessionFileUploadPath(session,
					s.getId(), "contrat", 1, false, false,
					salarieFormBB.getNomGroupe()));
			if (repertoire.exists()) {
				files = repertoire.listFiles();
				if (files.length != 0) {
					if (!salarieListTemp.contains(s)) {
						salarieListTemp.add(s);
					}
				}
			}

			repertoire = new File(Utils.getSessionFileUploadPath(session) + "/"
					+ salarieFormBB.getNomGroupe().replace(" ", "_") + "/"
					+ s.getId() + "/");
			if (repertoire.exists()) {
				files = repertoire.listFiles();
				int length = 0;
				for (File f : files) {
					if (f.isFile()) {
						length++;
					}
				}
				if (length > 0) {
					if (!salarieListTemp.contains(s)) {
						salarieListTemp.add(s);
					}
				}
			}

			repertoire = new File(Utils.getSessionFileUploadPath(session,
					s.getId(), "habilitation", 1, false, false,
					salarieFormBB.getNomGroupe()));

			if (repertoire.exists()) {
				files = repertoire.listFiles();
				if (files.length != 0) {
					if (!salarieListTemp.contains(s)) {
						salarieListTemp.add(s);
					}
				}
			}

		}

		/**
		 * *****************************FICHE
		 * METIER*********************************
		 */

		File[] filesFm = null;
		File repertoireFm = new File(Utils.getSessionFileUploadPath(session,
				this.idSalarie, "ficheMetier", 1, true, false,
				salarieFormBB.getNomGroupe()));

		FicheMetierServiceImpl servFm = new FicheMetierServiceImpl();

		if (this.idEntrepriseSelected != -1) {
			FicheMetierEntrepriseServiceImpl ficheMetierEntreprise = new FicheMetierEntrepriseServiceImpl();
			ficheMetierEntreprisesInventory = ficheMetierEntreprise
					.getFicheMetierEntrepriseBeanListByIdEntreprise(idEntrepriseSelected);
			ficheMetierBeanList = new ArrayList<FicheMetierBean>();
			for (FicheMetierEntrepriseBean ficheMetierEntrepriseBean : ficheMetierEntreprisesInventory) {
				FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
				FicheMetierBean ficheMetierBean = ficheMetierService
						.getFicheMetierBeanById(ficheMetierEntrepriseBean
								.getIdFicheMetier());
				ficheMetierBeanList.add(ficheMetierBean);
			}
		}

		if (repertoireFm.exists())
			filesFm = repertoireFm.listFiles();
		if (filesFm != null) {
			for (File f : filesFm) {
				if (f.isFile()) {
					boolean contain = false;
					for (File ff : fileListFm) {
						if (ff.getName().equals(f.getName())) {
							contain = true;
						}
					}
					if (!contain) {
						fileListFm.add(f);
					}
				}
			}
		}
		fileItemListFm = new ArrayList<SelectItem>();
		SelectItem selectItemFm;
		for (File f : fileListFm) {
			selectItemFm = new SelectItem();
			selectItemFm.setValue(f.getAbsolutePath());
			selectItemFm.setLabel(f.getName());
			fileItemListFm.add(selectItemFm);

		}

		salarieList.clear();
		salarieList.addAll(salarieListTemp);

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

	public void initRepriseDonneesForm(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		SalarieBean salarieBean = (SalarieBean) table.getRowData();
		// On récupère aussi son index
		this.idSalarie = salarieBean.getId();
		this.nomPrenom = " " + salarieBean.getNom() + " "
				+ salarieBean.getPrenom();
		rattachement(salarieBean.getId());
		initRepriseDonneesForm();
	}

	public String initRepriseDonneesForm() {
		return "linkFile";
	}

	public String retour() {
		return "link";
	}

	public void rattachement(int id) throws Exception {
		this.idSalarie = id;

		init();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		/** *****************************CV********************************* */

		File[] filesCv = null;
		File repertoireCv = new File(Utils.getSessionFileUploadPath(session)
				+ "/" + salarieFormBB.getNomGroupe().replace(" ", "_") + "/"
				+ this.idSalarie + "/");

		SalarieServiceImpl serv = new SalarieServiceImpl();

		salarieBeanList.clear();
		salarieBeanList.add(serv.getSalarieBeanExportById(this.idSalarie));

		File justificatif;
		SalarieServiceImpl salServ = new SalarieServiceImpl();
		for (SalarieBean s : salarieBeanList) {
			if (s.getCv() != null && !s.getCv().isEmpty()) {
				justificatif = new File(repertoireCv + Utils.FILE_SEPARATOR
						+ s.getCv());
				if (!justificatif.exists()) {
					s.setCv(null);
					salServ.saveOrUppdate(s);
				}
			}
		}
		SalarieBean sal;
		if (repertoireCv.exists()) {
			filesCv = repertoireCv.listFiles();
			if (filesCv != null) {
				if (filesCv.length != 0) {
					int i = 0;
					for (File f : filesCv) {
						if (f.isFile()) {

							sal = serv.getSalarieBeanById(this.idSalarie);
							sal.setCv(f.getName());
							serv.saveOrUppdate(sal);
							if (!fileListCv.contains(f)) {
								fileListCv.add(f);
							}

						}
						i++;
					}
				}
			}
			fileItemListCv = new ArrayList<SelectItem>();
			SelectItem selectItem;
			for (File f : fileListCv) {
				selectItem = new SelectItem();
				selectItem.setValue(f.getAbsolutePath());
				selectItem.setLabel(f.getName());
				fileItemListCv.add(selectItem);

			}
		}

		/**
		 * *****************************ABSENCES********************************
		 * *
		 */

		File[] filesAbsence = null;
		File repertoireAbsence = new File(Utils.getSessionFileUploadPath(
				session, this.idSalarie, "absence", 1, false, false,
				salarieFormBB.getNomGroupe()));

		AbsenceServiceImpl servAbsence = new AbsenceServiceImpl();

		absenceBeanList = servAbsence
				.getAbsenceBeanListByIdSalarie(this.idSalarie);

		AbsenceServiceImpl absServ = new AbsenceServiceImpl();
		for (AbsenceBean a : absenceBeanList) {
			if (a.getJustificatif() != null && !a.getJustificatif().isEmpty()) {
				justificatif = new File(repertoireAbsence
						+ Utils.FILE_SEPARATOR + a.getJustificatif());
				if (!justificatif.exists()) {
					a.setJustificatif(null);
					absServ.saveOrUppdate(a);
				}
			}
		}

		if (repertoireAbsence.exists())
			filesAbsence = repertoireAbsence.listFiles();
		if (filesAbsence != null) {
			for (File f : filesAbsence) {
				boolean contain = false;
				for (File ff : fileListAbs) {
					if (ff.getName().equals(f.getName())) {
						contain = true;
					}
				}
				if (!contain) {
					fileListAbs.add(f);
				}
			}
		}
		fileItemListAbs = new ArrayList<SelectItem>();
		SelectItem selectItem;
		for (File f : fileListAbs) {
			selectItem = new SelectItem();
			selectItem.setValue(f.getAbsolutePath());
			selectItem.setLabel(f.getName());
			fileItemListAbs.add(selectItem);

		}

		/**
		 * *****************************EVENEMENTS******************************
		 * ** *
		 */

		File[] filesEvenement = null;
		File repertoireEvenement = new File(Utils.getSessionFileUploadPath(
				session, this.idSalarie, "evenement", 1, false, false,
				salarieFormBB.getNomGroupe()));

		EvenementServiceImpl servEv = new EvenementServiceImpl();

		evenementBeanList = servEv
				.getEvenementBeanListByIdSalarie(this.idSalarie);

		for (EvenementBean e : evenementBeanList) {
			if (e.getJustificatif() != null && !e.getJustificatif().isEmpty()) {
				justificatif = new File(repertoireEvenement
						+ Utils.FILE_SEPARATOR + e.getJustificatif());
				if (!justificatif.exists()) {
					e.setJustificatif(null);
					servEv.saveOrUppdate(e);
				}
			}
		}

		if (repertoireEvenement.exists())
			filesEvenement = repertoireEvenement.listFiles();
		if (filesEvenement != null) {
			for (File f : filesEvenement) {
				boolean contain = false;
				for (File ff : fileListEv) {
					if (ff.getName().equals(f.getName())) {
						contain = true;
					}
				}
				if (!contain) {
					fileListEv.add(f);
				}
			}
		}
		fileItemListEv = new ArrayList<SelectItem>();
		SelectItem selectItemEv;
		for (File f : fileListEv) {
			selectItemEv = new SelectItem();
			selectItemEv.setValue(f.getAbsolutePath());
			selectItemEv.setLabel(f.getName());
			fileItemListEv.add(selectItemEv);

		}

		/**
		 * ************************FICHE DE POSTE****************************
		 */

		File[] filesFdp = null;
		File repertoireFdp = new File(Utils.getSessionFileUploadPath(session,
				this.idSalarie, "ficheDePoste", 1, false, false,
				salarieFormBB.getNomGroupe()));

		FicheDePosteServiceImpl servFdp = new FicheDePosteServiceImpl();

		ficheDePosteBeanList.add(servFdp
				.getFicheDePosteBeanByIdSalarie(this.idSalarie));

		FicheDePosteServiceImpl fdpServ = new FicheDePosteServiceImpl();
		for (FicheDePosteBean fdp : ficheDePosteBeanList) {
			if (fdp != null)
				if (fdp.getJustificatif() != null
						&& !fdp.getJustificatif().isEmpty()) {
					justificatif = new File(repertoireFdp
							+ Utils.FILE_SEPARATOR + fdp.getJustificatif());
					if (!justificatif.exists()) {
						fdp.setJustificatif(null);
						fdpServ.saveOrUppdate(fdp);
					}
				}
		}

		FicheMetierServiceImpl fm = new FicheMetierServiceImpl();
		if (servFdp.getFicheDePosteBeanByIdSalarie(this.idSalarie) != null)
			this.intituleMetierFicheDePoste = fm.getFicheMetierBeanById(
					servFdp.getFicheDePosteBeanByIdSalarie(this.idSalarie)
							.getIdFicheMetierType()).getIntituleFicheMetier();

		ServiceImpl service = new ServiceImpl();

		// recupération du libellé service pour un salarié existant
		this.intituleServiceFicheDePoste = service.getServiceBeanById(
				salServ.getSalarieBeanById(this.idSalarie)
						.getIdServiceSelected()).getNom();

		if (repertoireFdp.exists()) {
			filesFdp = repertoireFdp.listFiles();
		}
		if (filesFdp != null) {
			for (File f : filesFdp) {
				boolean contain = false;
				for (File ff : fileListFdp) {
					if (ff.getName().equals(f.getName())) {
						contain = true;
					}
				}
				if (!contain) {
					fileListFdp.add(f);
				}
			}
		}
		fileItemListFdp = new ArrayList<SelectItem>();
		SelectItem selectItemFdp;
		for (File f : fileListFdp) {
			selectItemFdp = new SelectItem();
			selectItemFdp.setValue(f.getAbsolutePath());
			selectItemFdp.setLabel(f.getName());
			fileItemListFdp.add(selectItemFdp);

		}
		/**
		 * *****************************ACCIDENTS*******************************
		 * **
		 */

		File[] filesAccident = null;
		File repertoireAccident = new File(Utils.getSessionFileUploadPath(
				session, this.idSalarie, "accident", 1, false, false,
				salarieFormBB.getNomGroupe()));

		AccidentServiceImpl servAccident = new AccidentServiceImpl();

		accidentBeanList = servAccident
				.getAccidentBeanListByIdSalarie(this.idSalarie);

		AccidentServiceImpl accServ = new AccidentServiceImpl();
		for (AccidentBean acc : accidentBeanList) {
			if (acc.getJustificatif() != null
					&& !acc.getJustificatif().isEmpty()) {
				justificatif = new File(repertoireAccident
						+ Utils.FILE_SEPARATOR + acc.getJustificatif());
				if (!justificatif.exists()) {
					acc.setJustificatif(null);
					accServ.saveOrUppdate(acc);
				}
			}
		}

		if (repertoireAccident.exists()) {
			filesAccident = repertoireAccident.listFiles();
		}
		if (filesAccident != null) {
			for (File f : filesAccident) {
				boolean contain = false;
				for (File ff : fileListAcc) {
					if (ff.getName().equals(f.getName())) {
						contain = true;
					}
				}
				if (!contain) {
					fileListAcc.add(f);
				}
			}
		}
		fileItemListAcc = new ArrayList<SelectItem>();
		SelectItem selectItemAccident;
		for (File f : fileListAcc) {
			selectItemAccident = new SelectItem();
			selectItemAccident.setValue(f.getAbsolutePath());
			selectItemAccident.setLabel(f.getName());
			fileItemListAcc.add(selectItemAccident);

		}
		/**
		 * *****************************FORMATIONS******************************
		 * ***
		 */

		File[] filesFormation = null;
		File repertoireFormation = new File(Utils.getSessionFileUploadPath(
				session, this.idSalarie, "formation", 1, false, false,
				salarieFormBB.getNomGroupe()));

		FormationServiceImpl servFormation = new FormationServiceImpl();

		formationBeanList = servFormation
				.getFormationBeanListByIdSalarie(this.idSalarie);

		FormationServiceImpl formServ = new FormationServiceImpl();
		for (FormationBean form : formationBeanList) {
			if (form.getJustificatif() != null
					&& !form.getJustificatif().isEmpty()) {
				justificatif = new File(repertoireFormation
						+ Utils.FILE_SEPARATOR + form.getJustificatif());
				if (!justificatif.exists()) {
					form.setJustificatif(null);
					formServ.saveOrUppdate(form);
				}
			}
		}

		if (repertoireFormation.exists()) {
			filesFormation = repertoireFormation.listFiles();
		}
		if (filesFormation != null) {
			for (File f : filesFormation) {
				boolean contain = false;
				for (File ff : fileListForm) {
					if (ff.getName().equals(f.getName())) {
						contain = true;
					}
				}
				if (!contain)
					fileListForm.add(f);
			}
		}
		fileItemListForm = new ArrayList<SelectItem>();
		SelectItem selectItemFormation;
		for (File f : fileListForm) {
			selectItemFormation = new SelectItem();
			selectItemFormation.setValue(f.getAbsolutePath());
			selectItemFormation.setLabel(f.getName());
			fileItemListForm.add(selectItemFormation);

		}

		/**
		 * *****************************CONTRAT
		 * TRAVAIL*********************************
		 */

		File[] filesContrat = null;
		File repertoireContrat = new File(Utils.getSessionFileUploadPath(
				session, this.idSalarie, "contrat", 1, false, false,
				salarieFormBB.getNomGroupe()));

		ContratTravailServiceImpl servContrat = new ContratTravailServiceImpl();

		contratTravailBeanList = servContrat
				.getContratTravailBeanListByIdSalarie(this.idSalarie);

		ContratTravailServiceImpl ctServ = new ContratTravailServiceImpl();
		for (ContratTravailBean ct : contratTravailBeanList) {
			if (ct.getJustificatif() != null && !ct.getJustificatif().isEmpty()) {
				justificatif = new File(repertoireContrat
						+ Utils.FILE_SEPARATOR + ct.getJustificatif());
				if (!justificatif.exists()) {
					ct.setJustificatif(null);
					ctServ.saveOrUppdate(ct);
				}
			}
		}

		if (repertoireContrat.exists()) {
			filesContrat = repertoireContrat.listFiles();
		}
		if (filesContrat != null) {
			for (File f : filesContrat) {
				boolean contain = false;
				for (File ff : fileListCt) {
					if (ff.getName().equals(f.getName())) {
						contain = true;
					}
				}
				if (!contain) {
					fileListCt.add(f);
				}
			}
		}
		fileItemListCt = new ArrayList<SelectItem>();
		SelectItem selectItemContrat;
		for (File f : fileListCt) {
			selectItemContrat = new SelectItem();
			selectItemContrat.setValue(f.getAbsolutePath());
			selectItemContrat.setLabel(f.getName());
			fileItemListCt.add(selectItemContrat);

		}

		/**
		 * *****************************ENTRETIENS******************************
		 * ***
		 */

		File[] filesEntretien = null;
		File repertoireEntretien = new File(Utils.getSessionFileUploadPath(
				session, this.idSalarie, "entretien", 1, false, false,
				salarieFormBB.getNomGroupe()));

		EntretienServiceImpl servEntretien = new EntretienServiceImpl();

		entretienBeanList = servEntretien
				.getEntretienBeanListByIdSalarie(this.idSalarie);

		EntretienServiceImpl entServ = new EntretienServiceImpl();
		for (EntretienBean ent : entretienBeanList) {
			if (ent.getJustificatif() != null
					&& !ent.getJustificatif().isEmpty()) {
				justificatif = new File(repertoireEntretien
						+ Utils.FILE_SEPARATOR + ent.getJustificatif());
				if (!justificatif.exists()) {
					ent.setJustificatif(null);
					entServ.saveOrUppdate(ent);
				}
			}
		}

		if (repertoireEntretien.exists()) {
			filesEntretien = repertoireEntretien.listFiles();
		}
		if (filesEntretien != null) {
			for (File f : filesEntretien) {
				boolean contain = false;
				for (File ff : fileListEnt) {
					if (ff.getName().equals(f.getName())) {
						contain = true;
					}
				}
				if (!contain) {
					fileListEnt.add(f);
				}
			}
		}
		fileItemListEnt = new ArrayList<SelectItem>();
		SelectItem selectItemEntretien;
		for (File f : fileListEnt) {
			selectItemEntretien = new SelectItem();
			selectItemEntretien.setValue(f.getAbsolutePath());
			selectItemEntretien.setLabel(f.getName());
			fileItemListEnt.add(selectItemEntretien);

		}
		/**
		 * *****************************PARCOURS********************************
		 * *
		 */

		File[] filesParcours = null;
		File repertoireParcours = new File(Utils.getSessionFileUploadPath(
				session, this.idSalarie, "parcours", 1, false, false,
				salarieFormBB.getNomGroupe()));

		ParcoursServiceImpl servParcours = new ParcoursServiceImpl();

		parcoursBeanList = servParcours
				.getParcoursBeanListByIdSalarie(this.idSalarie);

		ParcoursServiceImpl parServ = new ParcoursServiceImpl();
		for (ParcoursBean par : parcoursBeanList) {
			if (par.getJustificatif() != null
					&& !par.getJustificatif().isEmpty()) {
				justificatif = new File(repertoireParcours
						+ Utils.FILE_SEPARATOR + par.getJustificatif());
				if (!justificatif.exists()) {
					par.setJustificatif(null);
					parServ.saveOrUppdate(par);
				}
			}
		}

		if (repertoireParcours.exists()) {
			filesParcours = repertoireParcours.listFiles();
		}
		if (filesParcours != null) {
			for (File f : filesParcours) {
				boolean contain = false;
				for (File ff : fileListPar) {
					if (ff.getName().equals(f.getName())) {
						contain = true;
					}
				}
				if (!contain) {
					fileListPar.add(f);
				}
			}
		}
		fileItemListPar = new ArrayList<SelectItem>();
		SelectItem selectItemParcours;
		for (File f : fileListPar) {
			selectItemParcours = new SelectItem();
			selectItemParcours.setValue(f.getAbsolutePath());
			selectItemParcours.setLabel(f.getName());
			fileItemListPar.add(selectItemParcours);

		}

		/**
		 * *****************************FICHE
		 * METIER*********************************
		 */

		File[] filesFm = null;
		File repertoireFm = new File(Utils.getSessionFileUploadPath(session,
				this.idSalarie, "ficheMetier", 1, true, false,
				salarieFormBB.getNomGroupe()));

		FicheMetierServiceImpl servFm = new FicheMetierServiceImpl();

		if (this.idEntrepriseSelected != -1) {
			FicheMetierEntrepriseServiceImpl ficheMetierEntreprise = new FicheMetierEntrepriseServiceImpl();
			ficheMetierEntreprisesInventory = ficheMetierEntreprise
					.getFicheMetierEntrepriseBeanListByIdEntreprise(idEntrepriseSelected);
			ficheMetierBeanList = new ArrayList<FicheMetierBean>();
			for (FicheMetierEntrepriseBean ficheMetierEntrepriseBean : ficheMetierEntreprisesInventory) {
				FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
				FicheMetierBean ficheMetierBean = ficheMetierService
						.getFicheMetierBeanById(ficheMetierEntrepriseBean
								.getIdFicheMetier());
				ficheMetierBeanList.add(ficheMetierBean);
			}
		}

		FicheMetierServiceImpl fmServ = new FicheMetierServiceImpl();
		for (FicheMetierBean fmet : ficheMetierBeanList) {
			if (fmet.getJustificatif() != null
					&& !fmet.getJustificatif().isEmpty()) {
				justificatif = new File(repertoireFm + Utils.FILE_SEPARATOR
						+ fmet.getJustificatif());
				if (!justificatif.exists()) {
					fmet.setJustificatif(null);
					fmServ.saveOrUppdate(fmet, Integer.parseInt(session
							.getAttribute("groupe").toString()));
				}
			}
		}

		if (repertoireFm.exists()) {
			filesFm = repertoireFm.listFiles();
		}
		if (filesFm != null) {
			for (File f : filesFm) {
				boolean contain = false;
				for (File ff : fileListFm) {
					if (ff.getName().equals(f.getName())) {
						contain = true;
					}
				}
				if (!contain) {
					fileListFm.add(f);
				}
			}
		}
		fileItemListFm = new ArrayList<SelectItem>();
		SelectItem selectItemFm;
		for (File f : fileListFm) {
			selectItemFm = new SelectItem();
			selectItemFm.setValue(f.getAbsolutePath());
			selectItemFm.setLabel(f.getName());
			fileItemListFm.add(selectItemFm);

		}

		/**
		 * *****************************HABILITATIONS***************************
		 * ******
		 */

		File[] filesHabilitation = null;
		File repertoireHabilitation = new File(Utils.getSessionFileUploadPath(
				session, this.idSalarie, "habilitation", 1, false, false,
				salarieFormBB.getNomGroupe()));

		HabilitationServiceImpl servHabilitation = new HabilitationServiceImpl();

		habilitationBeanList = servHabilitation
				.getHabilitationBeanListByIdSalarie(this.idSalarie);

		HabilitationServiceImpl habServ = new HabilitationServiceImpl();
		for (HabilitationBean hab : habilitationBeanList) {
			if (hab.getJustificatif() != null
					&& !hab.getJustificatif().isEmpty()) {
				justificatif = new File(repertoireHabilitation
						+ Utils.FILE_SEPARATOR + hab.getJustificatif());
				if (!justificatif.exists()) {
					hab.setJustificatif(null);
					habServ.saveOrUppdate(hab);
				}
			}
		}

		if (repertoireHabilitation.exists()) {
			filesHabilitation = repertoireHabilitation.listFiles();
		}
		if (filesHabilitation != null) {
			for (File f : filesHabilitation) {
				boolean contain = false;
				for (File ff : fileListHab) {
					if (ff.getName().equals(f.getName())) {
						contain = true;
					}
				}
				if (!contain)
					fileListHab.add(f);
			}
			fileItemListHab = new ArrayList<SelectItem>();
			SelectItem selectItemHabilitation;
			for (File f : fileListHab) {
				selectItemHabilitation = new SelectItem();
				selectItemHabilitation.setValue(f.getAbsolutePath());
				selectItemHabilitation.setLabel(f.getName());
				fileItemListHab.add(selectItemHabilitation);

			}
		}

	}

	public void removeFileAbsence(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AbsenceBean absenceBean = (AbsenceBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		AbsenceServiceImpl serv = new AbsenceServiceImpl();
		absenceBean.setJustificatif("");
		AbsenceServiceImpl a = new AbsenceServiceImpl();
		a.saveOrUppdate(absenceBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie).getAbsenceBeanList()
				.set(indexSelectedRow, absenceBean);
		absenceBeanList.clear();
		absenceBeanList = serv.getAbsenceBeanListByIdSalarie(this.idSalarie);

	}

	public void removeFileEvenement(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EvenementBean evenementBean = (EvenementBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		EvenementServiceImpl serv = new EvenementServiceImpl();
		evenementBean.setJustificatif("");
		serv.saveOrUppdate(evenementBean);
		evenementBeanList.clear();
		evenementBeanList = serv
				.getEvenementBeanListByIdSalarie(this.idSalarie);

	}

	public void removeFileFdp(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FicheDePosteBean ficheDePosteBean = (FicheDePosteBean) table
				.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		FicheDePosteServiceImpl serv = new FicheDePosteServiceImpl();
		ficheDePosteBean.setJustificatif("");
		serv.saveOrUppdate(ficheDePosteBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie)
				.setFicheDePoste(ficheDePosteBean);
		ficheDePosteBeanList.clear();
		ficheDePosteBeanList.add(serv
				.getFicheDePosteBeanByIdSalarie(this.idSalarie));

	}

	public void removeFileFm(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FicheMetierBean ficheMetierBean = (FicheMetierBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		FicheMetierServiceImpl serv = new FicheMetierServiceImpl();
		ficheMetierBean.setJustificatif("");
		serv.saveOrUppdate(ficheMetierBean,
				Integer.parseInt(session.getAttribute("groupe").toString()));
		ficheMetierBeanList.clear();
		FicheMetierEntrepriseServiceImpl ficheMetierEntreprise = new FicheMetierEntrepriseServiceImpl();
		ficheMetierEntreprisesInventory = ficheMetierEntreprise
				.getFicheMetierEntrepriseBeanListByIdEntreprise(idEntrepriseSelected);
		ficheMetierBeanList = new ArrayList<FicheMetierBean>();
		for (FicheMetierEntrepriseBean ficheMetierEntrepriseBean : ficheMetierEntreprisesInventory) {
			FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
			FicheMetierBean ficheMetierBean2 = ficheMetierService
					.getFicheMetierBeanById(ficheMetierEntrepriseBean
							.getIdFicheMetier());
			ficheMetierBeanList.add(ficheMetierBean2);
		}

	}

	public void removeFileAccident(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AccidentBean accidentBean = (AccidentBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		AccidentServiceImpl serv = new AccidentServiceImpl();
		accidentBean.setJustificatif("");
		AccidentServiceImpl a = new AccidentServiceImpl();
		a.saveOrUppdate(accidentBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie).getAccidentBeanList()
				.set(indexSelectedRow, accidentBean);
		accidentBeanList.clear();
		accidentBeanList = serv.getAccidentBeanListByIdSalarie(this.idSalarie);

	}

	public void removeFileFormation(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FormationBean formationBean = (FormationBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		FormationServiceImpl serv = new FormationServiceImpl();
		formationBean.setJustificatif("");
		serv.saveOrUppdate(formationBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie).getFormationBeanList()
				.set(indexSelectedRow, formationBean);
		formationBeanList.clear();
		formationBeanList = serv
				.getFormationBeanListByIdSalarie(this.idSalarie);

	}

	public void removeFileEntretien(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EntretienBean entretienBean = (EntretienBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		EntretienServiceImpl serv = new EntretienServiceImpl();
		entretienBean.setJustificatif("");
		serv.saveOrUppdate(entretienBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie).getEntretienBeanList()
				.set(indexSelectedRow, entretienBean);
		entretienBeanList.clear();
		entretienBeanList = serv
				.getEntretienBeanListByIdSalarie(this.idSalarie);

	}

	public void removeFileParcours(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ParcoursBean parcoursBean = (ParcoursBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ParcoursServiceImpl serv = new ParcoursServiceImpl();
		parcoursBean.setJustificatif("");
		serv.saveOrUppdate(parcoursBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie).getParcoursBeanList()
				.set(indexSelectedRow, parcoursBean);
		parcoursBeanList.clear();
		parcoursBeanList = serv.getParcoursBeanListByIdSalarie(this.idSalarie);

	}

	public void removeFileContrat(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ContratTravailBean contratBean = (ContratTravailBean) table
				.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ContratTravailServiceImpl serv = new ContratTravailServiceImpl();
		contratBean.setJustificatif("");
		serv.saveOrUppdate(contratBean);
		contratTravailBeanList.clear();
		contratTravailBeanList = serv
				.getContratTravailBeanListByIdSalarie(this.idSalarie);

	}

	public void removeFileCv(ActionEvent evt) throws Exception {

		SalarieServiceImpl serv = new SalarieServiceImpl();
		SalarieBean sal = serv.getSalarieBeanById(this.idSalarie);
		sal.setCv("");
		serv.saveOrUppdate(sal);

		salarieBeanList.clear();
		salarieBeanList.add(serv.getSalarieBeanExportById(this.idSalarie));

	}

	public void removeFileHabilitation(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		HabilitationBean habilitationBean = (HabilitationBean) table
				.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		HabilitationServiceImpl serv = new HabilitationServiceImpl();
		habilitationBean.setJustificatif("");
		HabilitationServiceImpl a = new HabilitationServiceImpl();
		a.saveOrUppdate(habilitationBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie).getHabilitationBeanList()
				.set(indexSelectedRow, habilitationBean);
		habilitationBeanList.clear();
		habilitationBeanList = serv
				.getHabilitationBeanListByIdSalarie(this.idSalarie);

	}

	public void filtre(ValueChangeEvent event) throws Exception {
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
		if (event.getComponent().getId().equals("entrepriseListSalarie")) {
			idEntrepriseSelected = Integer.parseInt(event.getNewValue()
					.toString());
			init();
		}

	}

	public void linkAbsFile(ValueChangeEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AbsenceBean absenceBean = (AbsenceBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();

		AbsenceServiceImpl serv = new AbsenceServiceImpl();
		absenceBean.setJustificatif(new File(evt.getNewValue().toString())
				.getName());
		serv.saveOrUppdate(absenceBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie).getAbsenceBeanList()
				.set(indexSelectedRow, absenceBean);
		absenceBeanList.clear();
		absenceBeanList = serv.getAbsenceBeanListByIdSalarie(this.idSalarie);
	}

	public void linkEvFile(ValueChangeEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EvenementBean evenementBean = (EvenementBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();

		EvenementServiceImpl serv = new EvenementServiceImpl();
		evenementBean.setJustificatif(new File(evt.getNewValue().toString())
				.getName());
		serv.saveOrUppdate(evenementBean);
		evenementBeanList.clear();
		evenementBeanList = serv
				.getEvenementBeanListByIdSalarie(this.idSalarie);
	}

	public void linkFdpFile(ValueChangeEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FicheDePosteBean ficheDePosteBean = (FicheDePosteBean) table
				.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();

		FicheDePosteServiceImpl serv = new FicheDePosteServiceImpl();
		ficheDePosteBean.setJustificatif(new File(evt.getNewValue().toString())
				.getName());
		serv.saveOrUppdate(ficheDePosteBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie)
				.setFicheDePoste(ficheDePosteBean);
		ficheDePosteBeanList.clear();
		ficheDePosteBeanList.add(serv
				.getFicheDePosteBeanByIdSalarie(this.idSalarie));
	}

	public void linkFmFile(ValueChangeEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FicheMetierBean ficheMetierBean = (FicheMetierBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		FicheMetierServiceImpl serv = new FicheMetierServiceImpl();
		ficheMetierBean.setJustificatif(new File(evt.getNewValue().toString())
				.getName());
		serv.saveOrUppdate(ficheMetierBean,
				Integer.parseInt(session.getAttribute("groupe").toString()));
		ficheMetierBeanList.clear();
		FicheMetierEntrepriseServiceImpl ficheMetierEntreprise = new FicheMetierEntrepriseServiceImpl();
		ficheMetierEntreprisesInventory = ficheMetierEntreprise
				.getFicheMetierEntrepriseBeanListByIdEntreprise(idEntrepriseSelected);
		ficheMetierBeanList = new ArrayList<FicheMetierBean>();
		for (FicheMetierEntrepriseBean ficheMetierEntrepriseBean : ficheMetierEntreprisesInventory) {
			FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
			FicheMetierBean ficheMetierBean2 = ficheMetierService
					.getFicheMetierBeanById(ficheMetierEntrepriseBean
							.getIdFicheMetier());
			ficheMetierBeanList.add(ficheMetierBean2);
		}

	}

	public void linkAccFile(ValueChangeEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AccidentBean accidentBean = (AccidentBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();

		AccidentServiceImpl serv = new AccidentServiceImpl();
		accidentBean.setJustificatif(new File(evt.getNewValue().toString())
				.getName());
		serv.saveOrUppdate(accidentBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie).getAccidentBeanList()
				.set(indexSelectedRow, accidentBean);
		accidentBeanList.clear();
		accidentBeanList = serv.getAccidentBeanListByIdSalarie(this.idSalarie);
	}

	public void linkFormFile(ValueChangeEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FormationBean formationBean = (FormationBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();

		FormationServiceImpl serv = new FormationServiceImpl();
		formationBean.setJustificatif(new File(evt.getNewValue().toString())
				.getName());
		serv.saveOrUppdate(formationBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie).getFormationBeanList()
				.set(indexSelectedRow, formationBean);
		formationBeanList.clear();
		formationBeanList = serv
				.getFormationBeanListByIdSalarie(this.idSalarie);
	}

	public void linkEntFile(ValueChangeEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EntretienBean entretienBean = (EntretienBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();

		if (evt.getNewValue() != null) {
			EntretienServiceImpl serv = new EntretienServiceImpl();
			entretienBean
					.setJustificatif(new File(evt.getNewValue().toString())
							.getName());
			serv.saveOrUppdate(entretienBean);
			SalarieServiceImpl sal = new SalarieServiceImpl();
			sal.getSalarieBeanById(this.idSalarie).getEntretienBeanList()
					.set(indexSelectedRow, entretienBean);
			entretienBeanList.clear();
			entretienBeanList = serv
					.getEntretienBeanListByIdSalarie(this.idSalarie);
		}
	}

	public void linkParFile(ValueChangeEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ParcoursBean parcoursBean = (ParcoursBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();

		ParcoursServiceImpl serv = new ParcoursServiceImpl();
		parcoursBean.setJustificatif(new File(evt.getNewValue().toString())
				.getName());
		serv.saveOrUppdate(parcoursBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie).getParcoursBeanList()
				.set(indexSelectedRow, parcoursBean);
		parcoursBeanList.clear();
		parcoursBeanList = serv.getParcoursBeanListByIdSalarie(this.idSalarie);
	}

	public void linkCtFile(ValueChangeEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ContratTravailBean contratTravailBean = (ContratTravailBean) table
				.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();

		ContratTravailServiceImpl serv = new ContratTravailServiceImpl();
		contratTravailBean.setJustificatif(new File(evt.getNewValue()
				.toString()).getName());
		serv.saveOrUppdate(contratTravailBean);
		contratTravailBeanList.clear();
		contratTravailBeanList = serv
				.getContratTravailBeanListByIdSalarie(this.idSalarie);
	}

	public void linkCvFile(ValueChangeEvent evt) throws Exception {

		SalarieServiceImpl serv = new SalarieServiceImpl();
		SalarieBean sal = serv.getSalarieBeanById(this.idSalarie);
		sal.setCv(new File(evt.getNewValue().toString()).getName());
		serv.saveOrUppdate(sal);

		salarieBeanList.clear();
		salarieBeanList.add(serv.getSalarieBeanExportById(this.idSalarie));
	}

	public void linkHabFile(ValueChangeEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		HabilitationBean habilitationBean = (HabilitationBean) table
				.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();

		HabilitationServiceImpl serv = new HabilitationServiceImpl();
		habilitationBean.setJustificatif(new File(evt.getNewValue().toString())
				.getName());
		serv.saveOrUppdate(habilitationBean);
		SalarieServiceImpl sal = new SalarieServiceImpl();
		sal.getSalarieBeanById(this.idSalarie).getHabilitationBeanList()
				.set(indexSelectedRow, habilitationBean);
		habilitationBeanList.clear();
		habilitationBeanList = serv
				.getHabilitationBeanListByIdSalarie(this.idSalarie);
	}

	public void downloadCv(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		File file = (File) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", file.getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(file.toPath())
						+ " \",\"_Download\");");
	}

	public void downloadCvFromDatatable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		SalarieBean sal = (SalarieBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put(
				"fichier",
				Utils.getSessionFileUploadPath(session, idSalarie, null, 0,
						false, false, salarieFormBB.getNomGroupe())
						+ sal.getCv());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(Utils
								.getSessionFileUploadPath(session, idSalarie,
										null, 0, false, false,
										salarieFormBB.getNomGroupe())
								+ sal.getCv()).toPath())
						+ " \",\"_Download\");");

	}

	public void downloadCt(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		File file = (File) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", file.getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(file.toPath())
						+ " \",\"_Download\");");

	}

	public void downloadCtFromDatatable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ContratTravailBean ct = (ContratTravailBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put(
				"fichier",
				Utils.getSessionFileUploadPath(session, idSalarie, "contrat",
						0, false, false, salarieFormBB.getNomGroupe())
						+ ct.getJustificatif());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(Utils
								.getSessionFileUploadPath(session, idSalarie,
										"contrat", 0, false, false,
										salarieFormBB.getNomGroupe())
								+ ct.getJustificatif()).toPath())
						+ " \",\"_Download\");");

	}

	public void downloadPar(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		File file = (File) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", file.getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(file.toPath())
						+ " \",\"_Download\");");

	}

	public void downloadParFromDatatable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		ParcoursBean par = (ParcoursBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put(
				"fichier",
				Utils.getSessionFileUploadPath(session, idSalarie, "parcours",
						0, false, false, salarieFormBB.getNomGroupe())
						+ par.getJustif().getName());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(Utils
								.getSessionFileUploadPath(session, idSalarie,
										"parcours", 0, false, false,
										salarieFormBB.getNomGroupe())
								+ par.getJustif().getName()).toPath())
						+ " \",\"_Download\");");

	}

	public void downloadHab(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		File file = (File) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", file.getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(file.toPath())
						+ " \",\"_Download\");");

	}

	public void downloadHabFromDatatable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		HabilitationBean hab = (HabilitationBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put(
				"fichier",
				Utils.getSessionFileUploadPath(session, idSalarie,
						"habilitation", 0, false, false,
						salarieFormBB.getNomGroupe())
						+ hab.getJustif().getName());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(Utils
								.getSessionFileUploadPath(session, idSalarie,
										"habilitation", 0, false, false,
										salarieFormBB.getNomGroupe())
								+ hab.getJustif().getName()).toPath())
						+ " \",\"_Download\");");

	}

	public void downloadForm(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		File file = (File) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", file.getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(file.toPath())
						+ " \",\"_Download\");");

	}

	public void downloadFormFromDatatable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FormationBean form = (FormationBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put(
				"fichier",
				Utils.getSessionFileUploadPath(session, idSalarie, "formation",
						0, false, false, salarieFormBB.getNomGroupe())
						+ form.getJustif().getName());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(Utils
								.getSessionFileUploadPath(session, idSalarie,
										"formation", 0, false, false,
										salarieFormBB.getNomGroupe())
								+ form.getJustif().getName()).toPath())
						+ " \",\"_Download\");");

	}

	public void downloadAbs(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		File file = (File) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", file.getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(file.toPath())
						+ " \",\"_Download\");");

	}

	public void downloadFmFromDatatable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FicheMetierBean fm = (FicheMetierBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put(
				"fichier",
				Utils.getSessionFileUploadPath(session, idSalarie, "", 0, true,
						false, salarieFormBB.getNomGroupe())
						+ fm.getJustif().getName());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(Utils
								.getSessionFileUploadPath(session, idSalarie,
										"", 0, true, false,
										salarieFormBB.getNomGroupe())
								+ fm.getJustif().getName()).toPath())
						+ " \",\"_Download\");");

	}

	public void downloadFm(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		File file = (File) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", file.getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(file.toPath())
						+ " \",\"_Download\");");

	}

	public void downloadAbsFromDatatable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AbsenceBean abs = (AbsenceBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put(
				"fichier",
				Utils.getSessionFileUploadPath(session, idSalarie, "absence",
						0, false, false, salarieFormBB.getNomGroupe())
						+ abs.getJustif().getName());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(Utils
								.getSessionFileUploadPath(session, idSalarie,
										"absence", 0, false, false,
										salarieFormBB.getNomGroupe())
								+ abs.getJustif().getName()).toPath())
						+ " \",\"_Download\");");

	}

	public void downloadAcc(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		File file = (File) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", file.getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(file.toPath())
						+ " \",\"_Download\");");

	}

	public void downloadAccFromDatatable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		AccidentBean acc = (AccidentBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put(
				"fichier",
				Utils.getSessionFileUploadPath(session, idSalarie, "accident",
						0, false, false, salarieFormBB.getNomGroupe())
						+ acc.getJustif().getName());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(Utils
								.getSessionFileUploadPath(session, idSalarie,
										"accident", 0, false, false,
										salarieFormBB.getNomGroupe())
								+ acc.getJustif().getName()).toPath())
						+ " \",\"_Download\");");

	}

	public void downloadEv(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		File file = (File) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", file.getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(file.toPath())
						+ " \",\"_Download\");");

	}

	public void downloadEvFromDatatable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EvenementBean ev = (EvenementBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put(
				"fichier",
				Utils.getSessionFileUploadPath(session, idSalarie, "evenement",
						0, false, false, salarieFormBB.getNomGroupe())
						+ ev.getJustif().getName());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(Utils
								.getSessionFileUploadPath(session, idSalarie,
										"evenement", 0, false, false,
										salarieFormBB.getNomGroupe())
								+ ev.getJustif().getName()).toPath())
						+ " \",\"_Download\");");

	}

	public void downloadFdp(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		File file = (File) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", file.getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(file.toPath())
						+ " \",\"_Download\");");

	}

	public void downloadFdpFromDatatable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		FicheDePosteBean fdp = (FicheDePosteBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put(
				"fichier",
				Utils.getSessionFileUploadPath(session, idSalarie,
						"ficheDePoste", 0, false, false,
						salarieFormBB.getNomGroupe())
						+ fdp.getJustif().getName());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(Utils
								.getSessionFileUploadPath(session, idSalarie,
										"ficheDePoste", 0, false, false,
										salarieFormBB.getNomGroupe())
								+ fdp.getJustif().getName()).toPath())
						+ " \",\"_Download\");");

	}

	public void downloadEnt(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		File file = (File) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", file.getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(file.toPath())
						+ " \",\"_Download\");");

	}

	public void downloadEntFromDatatable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		EntretienBean ent = (EntretienBean) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put(
				"fichier",
				Utils.getSessionFileUploadPath(session, idSalarie, "entretien",
						0, false, false, salarieFormBB.getNomGroupe())
						+ ent.getJustif().getName());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(Utils
								.getSessionFileUploadPath(session, idSalarie,
										"entretien", 0, false, false,
										salarieFormBB.getNomGroupe())
								+ ent.getJustif().getName()).toPath())
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

	public List<File> getFileListAbs() {
		return fileListAbs;
	}

	public void setFileListAbs(List<File> fileListAbs) {
		this.fileListAbs = fileListAbs;
	}

	public List<File> getFileListForm() {
		return fileListForm;
	}

	public void setFileListForm(List<File> fileListForm) {
		this.fileListForm = fileListForm;
	}

	public List<File> getFileListEnt() {
		return fileListEnt;
	}

	public void setFileListEnt(List<File> fileListEnt) {
		this.fileListEnt = fileListEnt;
	}

	public List<File> getFileListAcc() {
		return fileListAcc;
	}

	public void setFileListAcc(List<File> fileListAcc) {
		this.fileListAcc = fileListAcc;
	}

	public List<File> getFileListPar() {
		return fileListPar;
	}

	public void setFileListPar(List<File> fileListPar) {
		this.fileListPar = fileListPar;
	}

	public List<AbsenceBean> getAbsenceBeanList() {
		return absenceBeanList;
	}

	public void setAbsenceBeanList(List<AbsenceBean> absenceBeanList) {
		this.absenceBeanList = absenceBeanList;
	}

	public List<SalarieBean> getSalarieList() {
		return salarieList;
	}

	public void setSalarieList(List<SalarieBean> salarieList) {
		this.salarieList = salarieList;
	}

	public List<SelectItem> getFileList() {
		return fileItemListAbs;
	}

	public void setFileList(List<SelectItem> fileList) {
		this.fileItemListAbs = fileList;
	}

	public List<SalarieBean> getSalarieListTemp() {
		return salarieListTemp;
	}

	public void setSalarieListTemp(List<SalarieBean> salarieListTemp) {
		this.salarieListTemp = salarieListTemp;
	}

	public int getIndexSelectedRow() {
		return indexSelectedRow;
	}

	public void setIndexSelectedRow(int indexSelectedRow) {
		this.indexSelectedRow = indexSelectedRow;
	}

	public int getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(int idSalarie) {
		this.idSalarie = idSalarie;
	}

	public List<SelectItem> getFileItemListAbs() {
		return fileItemListAbs;
	}

	public void setFileItemListAbs(List<SelectItem> fileItemListAbs) {
		this.fileItemListAbs = fileItemListAbs;
	}

	public List<SelectItem> getFileItemListAcc() {
		return fileItemListAcc;
	}

	public void setFileItemListAcc(List<SelectItem> fileItemListAcc) {
		this.fileItemListAcc = fileItemListAcc;
	}

	public List<SelectItem> getFileItemListForm() {
		return fileItemListForm;
	}

	public void setFileItemListForm(List<SelectItem> fileItemListForm) {
		this.fileItemListForm = fileItemListForm;
	}

	public List<SelectItem> getFileItemListEnt() {
		return fileItemListEnt;
	}

	public void setFileItemListEnt(List<SelectItem> fileItemListEnt) {
		this.fileItemListEnt = fileItemListEnt;
	}

	public List<SelectItem> getFileItemListPar() {
		return fileItemListPar;
	}

	public void setFileItemListPar(List<SelectItem> fileItemListPar) {
		this.fileItemListPar = fileItemListPar;
	}

	public List<AccidentBean> getAccidentBeanList() {
		return accidentBeanList;
	}

	public void setAccidentBeanList(List<AccidentBean> accidentBeanList) {
		this.accidentBeanList = accidentBeanList;
	}

	public List<FormationBean> getFormationBeanList() {
		return formationBeanList;
	}

	public void setFormationBeanList(List<FormationBean> formationBeanList) {
		this.formationBeanList = formationBeanList;
	}

	public List<EntretienBean> getEntretienBeanList() {
		return entretienBeanList;
	}

	public void setEntretienBeanList(List<EntretienBean> entretienBeanList) {
		this.entretienBeanList = entretienBeanList;
	}

	public List<ParcoursBean> getParcoursBeanList() {
		return parcoursBeanList;
	}

	public void setParcoursBeanList(List<ParcoursBean> parcoursBeanList) {
		this.parcoursBeanList = parcoursBeanList;
	}

	public String getFileSelected() {
		return fileSelected;
	}

	public void setFileSelected(String fileSelected) {
		this.fileSelected = fileSelected;
	}

	public List<File> getFileListHab() {
		return fileListHab;
	}

	public void setFileListHab(List<File> fileListHab) {
		this.fileListHab = fileListHab;
	}

	public List<SelectItem> getFileItemListHab() {
		return fileItemListHab;
	}

	public void setFileItemListHab(List<SelectItem> fileItemListHab) {
		this.fileItemListHab = fileItemListHab;
	}

	public List<HabilitationBean> getHabilitationBeanList() {
		return habilitationBeanList;
	}

	public void setHabilitationBeanList(
			List<HabilitationBean> habilitationBeanList) {
		this.habilitationBeanList = habilitationBeanList;
	}

	public List<File> getFileListCv() {
		return fileListCv;
	}

	public void setFileListCv(List<File> fileListCv) {
		this.fileListCv = fileListCv;
	}

	public List<SelectItem> getFileItemListCv() {
		return fileItemListCv;
	}

	public void setFileItemListCv(List<SelectItem> fileItemListCv) {
		this.fileItemListCv = fileItemListCv;
	}

	public List<SalarieBeanExport> getSalarieBeanList() {
		return salarieBeanList;
	}

	public void setSalarieBeanList(List<SalarieBeanExport> salarieBeanList) {
		this.salarieBeanList = salarieBeanList;
	}

	public String getNomPrenom() {
		return nomPrenom;
	}

	public void setNomPrenom(String nomPrenom) {
		this.nomPrenom = nomPrenom;
	}

	public List<File> getFileListFdp() {
		return fileListFdp;
	}

	public void setFileListFdp(List<File> fileListFdp) {
		this.fileListFdp = fileListFdp;
	}

	public List<SelectItem> getFileItemListFdp() {
		return fileItemListFdp;
	}

	public void setFileItemListFdp(List<SelectItem> fileItemListFdp) {
		this.fileItemListFdp = fileItemListFdp;
	}

	public List<FicheDePosteBean> getFicheDePosteBeanList() {
		return ficheDePosteBeanList;
	}

	public void setFicheDePosteBeanList(
			List<FicheDePosteBean> ficheDePosteBeanList) {
		this.ficheDePosteBeanList = ficheDePosteBeanList;
	}

	public String getIntituleMetierFicheDePoste() {
		return intituleMetierFicheDePoste;
	}

	public void setIntituleMetierFicheDePoste(String intituleMetierFicheDePoste) {
		this.intituleMetierFicheDePoste = intituleMetierFicheDePoste;
	}

	public String getIntituleServiceFicheDePoste() {
		return intituleServiceFicheDePoste;
	}

	public void setIntituleServiceFicheDePoste(
			String intituleServiceFicheDePoste) {
		this.intituleServiceFicheDePoste = intituleServiceFicheDePoste;
	}

	public ArrayList<SelectItem> getEntrepriseList() {
		return entrepriseList;
	}

	public void setEntrepriseList(ArrayList<SelectItem> entrepriseList) {
		this.entrepriseList = entrepriseList;
	}

	public int getIdEntrepriseSelected() {
		return idEntrepriseSelected;
	}

	public void setIdEntrepriseSelected(int idEntrepriseSelected) {
		this.idEntrepriseSelected = idEntrepriseSelected;
	}

	public List<File> getFileListFm() {
		return fileListFm;
	}

	public void setFileListFm(List<File> fileListFm) {
		this.fileListFm = fileListFm;
	}

	public List<SelectItem> getFileItemListFm() {
		return fileItemListFm;
	}

	public void setFileItemListFm(List<SelectItem> fileItemListFm) {
		this.fileItemListFm = fileItemListFm;
	}

	public List<FicheMetierBean> getFicheMetierBeanList() {
		return ficheMetierBeanList;
	}

	public void setFicheMetierBeanList(List<FicheMetierBean> ficheMetierBeanList) {
		this.ficheMetierBeanList = ficheMetierBeanList;
	}

	public List<File> getFileListCt() {
		return fileListCt;
	}

	public void setFileListCt(List<File> fileListCt) {
		this.fileListCt = fileListCt;
	}

	public List<SelectItem> getFileItemListCt() {
		return fileItemListCt;
	}

	public void setFileItemListCt(List<SelectItem> fileItemListCt) {
		this.fileItemListCt = fileItemListCt;
	}

	public List<ContratTravailBean> getContratTravailBeanList() {
		return contratTravailBeanList;
	}

	public void setContratTravailBeanList(
			List<ContratTravailBean> contratTravailBeanList) {
		this.contratTravailBeanList = contratTravailBeanList;
	}

	public String getInitList() throws Exception {
		init();
		return "";
	}

	public List<File> getFileListEv() {
		return fileListEv;
	}

	public void setFileListEv(List<File> fileListEv) {
		this.fileListEv = fileListEv;
	}

	public List<SelectItem> getFileItemListEv() {
		return fileItemListEv;
	}

	public void setFileItemListEv(List<SelectItem> fileItemListEv) {
		this.fileItemListEv = fileItemListEv;
	}

	public List<EvenementBean> getEvenementBeanList() {
		return evenementBeanList;
	}

	public void setEvenementBeanList(List<EvenementBean> evenementBeanList) {
		this.evenementBeanList = evenementBeanList;
	}

}
