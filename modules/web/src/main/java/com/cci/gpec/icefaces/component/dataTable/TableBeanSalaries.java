package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.EvenementBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.PiecesJustificativesBean;
import com.cci.gpec.commons.RemunerationBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.SalarieBeanExport;
import com.cci.gpec.commons.TransmissionBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.EvenementServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.HabilitationServiceImpl;
import com.cci.gpec.metier.implementation.PiecesJustificativesServiceImpl;
import com.cci.gpec.metier.implementation.RemunerationServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.metier.implementation.TransmissionServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.jdom.ExportXml;
import com.cci.gpec.web.backingBean.login.LoginBB;
import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.paneltabset.TabChangeEvent;
import com.icesoft.faces.context.effects.JavascriptContext;

public class TableBeanSalaries {

	private List<SalarieBeanExport> salariesInventory = new ArrayList<SalarieBeanExport>();
	private List<SalarieBeanExport> salariesInventoryAlpha = new ArrayList<SalarieBeanExport>();
	private int id;
	private ArrayList<SelectItem> entrepriseList;
	private ArrayList<SelectItem> alphabetList;
	private int idEntrepriseSelected;
	private String alphabetSelected;
	private String filtreChoisi = "Sélection en cours : Tous les salariés";
	private String url = "";

	private boolean echec = false;
	private boolean reussite = false;

	private boolean modalLoginRenderedComp = false;
	private boolean modalLoginRenderedEnt = false;
	private boolean modalLoginRenderedRemu = false;
	private boolean loggedRendered = false;
	private int afficheSortie = 0;
	private String password;
	private String passwordEnt;
	private String passwordComp;
	private String passwordRemu;

	private Integer nbSalarie = 0;
	private String libelleNbSalarie = "";

	// Pour les tableaux de suivis
	private Date debutExtraction;
	private Date finExtraction;
	private GregorianCalendar debutExtractionGC;
	private GregorianCalendar finExtractionGC;
	private boolean modalImportExportRendered = false;
	private boolean modalImportExportPreviewRendered = true;

	private boolean colorButtonDisplay = false;

	private String lastTransmission;

	private boolean rattacheAutreCCI;

	private String cciRattachement;

	public static final String AUTRES_CCI = "Autres CCI";

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	LoginBB loginBB = (LoginBB) FacesContext.getCurrentInstance()
			.getCurrentInstance().getExternalContext().getSessionMap()
			.get("loginBB");

	public boolean isRattacheAutreCCI() {

		this.rattacheAutreCCI = false;
		if (this.idEntrepriseSelected > 0) {
			EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
			EntrepriseBean entrepriseBean;

			try {
				entrepriseBean = entrepriseService
						.getEntrepriseBeanById(this.idEntrepriseSelected);
				if (entrepriseBean.getCciRattachement() == null
						|| entrepriseBean.getCciRattachement().equals(
								AUTRES_CCI)) {
					this.rattacheAutreCCI = true;
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return rattacheAutreCCI;
	}

	public void setRattacheAutreCCI(boolean rattacheAutreCCI) {
		this.rattacheAutreCCI = rattacheAutreCCI;
	}

	public void setLastTransmission(String lastTransmission) {
		this.lastTransmission = lastTransmission;
	}

	public TableBeanSalaries() throws Exception {
		init();
	}

	public String refresh() {
		return "refreshMigration";
	}

	public void transmission(ActionEvent event) throws Exception {
		if (loginBB.isModalTransmissionRendered()) {
			loginBB.setModalTransmissionRendered(false);
		}

		try {
			ExportXml exportXml = new ExportXml();

			echec = true;
			reussite = true;
			EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
			SalarieServiceImpl sal = new SalarieServiceImpl();
			List<SalarieBean> l = new ArrayList<SalarieBean>();
			for (EntrepriseBean e : entServ.getEntreprisesList(Integer
					.parseInt(session.getAttribute("groupe").toString()))) {
				// if (e.getCciRattachement().equals(this.cciRattachement)) {
				l.clear();
				l = sal.getSalarieBeanListByIdEntreprise(e.getId());

				int c = 0;
				for (SalarieBean s : l) {
					if (getLastParcours(s) != null) {
						if (s.isPresent()
								&& getLastParcours(s)
										.getIdTypeContratSelected() != 3
								&& getLastParcours(s)
										.getIdTypeContratSelected() != 7) {
							c++;
						}
					}
				}
				nbSalarie = c;

				exportXml.setIdEntrepriseSelected(e.getId());

				exportXml.setNbSalarie(nbSalarie);

				exportXml.send();

				echec = echec && exportXml.isEchec();
				reussite = reussite && exportXml.isReussite();

				Integer idGroupe = Integer.parseInt(session.getAttribute(
						"groupe").toString());
				TransmissionServiceImpl transmissionServiceImpl = new TransmissionServiceImpl();
				TransmissionBean transmissionBean = transmissionServiceImpl
						.getTransmissionBean(idGroupe);
				transmissionBean.setDateTransmission(new Date());
				transmissionBean.setDateDerniereDemande(new Date());
				TransmissionServiceImpl transmissionService = new TransmissionServiceImpl();
				transmissionService.saveOrUppdate(transmissionBean, idGroupe);
			}
			// }

		} catch (Exception e) {
			e.printStackTrace();
			echec = true;
			reussite = false;
		}

	}

	public List<SalarieBeanExport> getSalariesInventory() throws Exception {
		SalarieServiceImpl salarieService = new SalarieServiceImpl();

		if (idEntrepriseSelected != -1) {
			salariesInventory = salarieService
					.getSalarieBeanExportListByIdEntreprise(idEntrepriseSelected);
		} else {
			salariesInventory = salarieService.getSalariesExportList(Integer
					.parseInt(session.getAttribute("groupe").toString()));
		}

		EntrepriseServiceImpl servE = new EntrepriseServiceImpl();
		EvenementServiceImpl servEv = new EvenementServiceImpl();
		RemunerationServiceImpl servRe = new RemunerationServiceImpl();
		List<RemunerationBean> remuList = new ArrayList<RemunerationBean>();
		List<EvenementBean> evList = new ArrayList<EvenementBean>();
		remuList = servRe.getRemunerationList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		evList = servEv.getEvenementsList(Integer.parseInt(session
				.getAttribute("groupe").toString()));

		ServiceImpl servS = new ServiceImpl();
		for (SalarieBeanExport s : salariesInventory) {
			boolean hasRemu = false;
			for (RemunerationBean r : remuList) {
				if (r.getIdSalarie().intValue() == s.getId().intValue()) {
					hasRemu = true;
					break;
				}
			}
			boolean hasEv = false;
			for (EvenementBean e : evList) {
				if (e.getIdSalarie() == s.getId().intValue()) {
					hasEv = true;
					break;
				}
			}
			if (s.getAbsenceBeanList().size() > 0
					|| s.getAccidentBeanList().size() > 0
					|| s.getFormationBeanList().size() > 0
					|| s.getHabilitationBeanList().size() > 0 || hasRemu
					|| s.getEntretienBeanList().size() > 0 || hasEv) {
				s.setCanDeleteSalarie(false);
			} else {
				s.setCanDeleteSalarie(true);
			}
			if (!s.isPresent()) {
				s.setChangeCouleur(true);
			}
		}

		if (alphabetSelected != null) {
			salariesInventoryAlpha = new ArrayList<SalarieBeanExport>();
			int k = salariesInventory.size();
			for (int i = 0; i < k; i++) {
				SalarieBeanExport bean = salariesInventory.get(i);

				if (bean.getNom().toLowerCase().substring(0, 1)
						.equals(alphabetSelected)
						|| bean.getNom().substring(0, 1).toUpperCase()
								.equals(alphabetSelected)) {
					if (afficheSortie == 1) {
						if (!bean.isPresent()) {
							salariesInventoryAlpha.add(bean);
						}
					}
					if (afficheSortie == 2) {
						if (bean.isPresent()) {
							salariesInventoryAlpha.add(bean);
						}
					}
					if (afficheSortie == 0) {
						salariesInventoryAlpha.add(bean);
					}
				}

			}
			salariesInventory = salariesInventoryAlpha;
		} else {
			salariesInventoryAlpha = new ArrayList<SalarieBeanExport>();
			int k = salariesInventory.size();
			for (int i = 0; i < k; i++) {
				SalarieBeanExport bean = salariesInventory.get(i);

				if (afficheSortie == 1) {
					if (!bean.isPresent()) {
						salariesInventoryAlpha.add(bean);
					}
				}
				if (afficheSortie == 2) {
					if (bean.isPresent()) {
						salariesInventoryAlpha.add(bean);
					}
				}
				if (afficheSortie == 0) {
					salariesInventoryAlpha.add(bean);
				}

			}
			salariesInventory = salariesInventoryAlpha;
		}

		return salariesInventory;
	}

	public List<SalarieBeanExport> getSalariesInventory2() throws Exception {

		return salariesInventory;
	}

	private ParcoursBean getLastParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		ParcoursBean pb = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			// Test le 1er appel
			if (pb == null) {
				pb = parcour;
			}
			if (parcour.getDebutFonction().after(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}

	public void setSalariesInventory(List<SalarieBeanExport> salariesInventory) {
		Collections.sort(salariesInventory);
		this.salariesInventory = salariesInventory;
	}

	public void init() throws Exception {

		this.nbSalarie = 0;

		this.cciRattachement = "";

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		if (session.getAttribute("afficheSortie") == null) {
			session.putValue("afficheSortie", 0);
		}
		if (session.getAttribute("filtre") == null) {
			session.putValue("filtre", "");
		}

		// init entreprises
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
		if (session.getAttribute("entreprise") != null) {
			this.idEntrepriseSelected = Integer.parseInt(session.getAttribute(
					"entreprise").toString());
		} else {
			this.idEntrepriseSelected = -1;
		}

		getSalarieListByIdEntreprise(this.idEntrepriseSelected);

		SalarieServiceImpl sal = new SalarieServiceImpl();
		List<SalarieBean> l = new ArrayList<SalarieBean>();
		if (idEntrepriseSelected != -1) {
			l = sal.getSalarieBeanListByIdEntreprise(idEntrepriseSelected);
		} else {
			l = sal.getSalariesList(Integer.parseInt(session.getAttribute(
					"groupe").toString()));
		}
		int c = 0;
		for (SalarieBean s : l) {
			if (getLastParcours(s) != null) {
				if (s.isPresent()
						&& getLastParcours(s).getIdTypeContratSelected() != 3
						&& getLastParcours(s).getIdTypeContratSelected() != 7) {
					c++;
				}
			}
		}
		nbSalarie = c;
		this.alphabetSelected = null;
		this.debutExtraction = null;
		this.finExtraction = null;
		this.debutExtractionGC = null;
		this.finExtractionGC = null;
	}

	public void initialiserAlphabet() throws Exception {
		// init alphabet
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		alphabetList = new ArrayList<SelectItem>();
		int index = 0;
		SelectItem selectItem2;
		while (index < alphabet.length()) {
			String letter = alphabet.substring(index, index + 1);
			selectItem2 = new SelectItem();
			selectItem2.setValue(letter);
			selectItem2.setLabel(letter);
			alphabetList.add(selectItem2);
			index = index + 1;
		}
	}

	private void getSalarieListByIdEntreprise(int idEntreprise)
			throws Exception {
		SalarieServiceImpl salarieService = new SalarieServiceImpl();
		if (idEntreprise != -1) {
			salariesInventory = salarieService
					.getSalarieBeanExportListByIdEntreprise(idEntreprise);
		} else {
			salariesInventory = salarieService.getSalariesExportList(Integer
					.parseInt(session.getAttribute("groupe").toString()));
		}

		RemunerationServiceImpl remu = new RemunerationServiceImpl();
		for (SalarieBeanExport s : salariesInventory) {
			List<RemunerationBean> remunerationList = remu
					.getRemunerationByIdSalarie(s.getId());

			if (remunerationList.isEmpty()) {
				s.setHasRemu(false);
			} else {
				s.setHasRemu(true);
			}
		}

		EvenementServiceImpl ev = new EvenementServiceImpl();
		for (SalarieBeanExport s : salariesInventory) {
			List<EvenementBean> list = ev.getEvenementBeanListByIdSalarie(s
					.getId());

			if (list.isEmpty()) {
				s.setHasEvenement(false);
			} else {
				s.setHasEvenement(true);
			}
		}

		List<SalarieBeanExport> salariesInventorytri = new ArrayList<SalarieBeanExport>();

		int j = salariesInventory.size() - 1;
		for (int i = j; i >= 0; i--) {
			SalarieBeanExport bean = salariesInventory.get(i);

			if (afficheSortie == 1) {
				if (!bean.isPresent()) {
					salariesInventorytri.add(bean);
				}
			}
			if (afficheSortie == 2) {
				if (bean.isPresent()) {
					salariesInventorytri.add(bean);
				}
			}
			if (afficheSortie == 0) {
				salariesInventorytri.add(bean);
			}
		}
		salariesInventory = salariesInventorytri;
		Collections.sort(salariesInventory);
		ServiceImpl ss = new ServiceImpl();
		for (SalarieBeanExport s : salariesInventory) {
			s.setService(ss.getServiceBeanById(s.getIdServiceSelected())
					.getNom());
		}
	}

	private void getSalarieListByAlphabet(int idEntreprise, String lettre)
			throws Exception {
		SalarieServiceImpl salarieService = new SalarieServiceImpl();
		salariesInventoryAlpha = new ArrayList<SalarieBeanExport>();
		if (idEntreprise != -1) {
			salariesInventory = salarieService
					.getSalarieBeanExportListByIdEntreprise(idEntreprise);
		} else {
			salariesInventory = salarieService.getSalariesExportList(Integer
					.parseInt(session.getAttribute("groupe").toString()));
		}
		RemunerationServiceImpl remu = new RemunerationServiceImpl();
		for (SalarieBeanExport s : salariesInventory) {
			List<RemunerationBean> remunerationList = remu
					.getRemunerationByIdSalarie(s.getId());

			if (remunerationList.isEmpty()) {
				s.setHasRemu(false);
			} else {
				s.setHasRemu(true);
			}
		}

		EvenementServiceImpl ev = new EvenementServiceImpl();
		for (SalarieBeanExport s : salariesInventory) {
			List<EvenementBean> list = ev.getEvenementBeanListByIdSalarie(s
					.getId());

			if (list.isEmpty()) {
				s.setHasEvenement(false);
			} else {
				s.setHasEvenement(true);
			}
		}

		int k = salariesInventory.size() - 1;
		for (int i = k; i >= 0; i--) {
			SalarieBeanExport bean = salariesInventory.get(i);
			if (!bean.isPresent()) {
				bean.setChangeCouleur(true);
			}

			String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

			alphabetList = new ArrayList<SelectItem>();
			int index = 0;
			int indexStop = 0;
			while (index < alphabet.length()) {

				if (lettre.toString().equals(
						alphabet.substring(index, index + 1))) {
					indexStop = index;
				}
				index++;
			}
			for (int j = indexStop; j < alphabet.length(); j++) {
				if (bean.getNom().substring(0, 1)
						.equals(alphabet.substring(j, j + 1))) {
					if (afficheSortie == 1) {
						if (!bean.isPresent()) {
							salariesInventoryAlpha.add(bean);
						}
					}
					if (afficheSortie == 2) {
						if (bean.isPresent()) {
							salariesInventoryAlpha.add(bean);
						}
					}
					if (afficheSortie == 0) {
						salariesInventoryAlpha.add(bean);
					}
				}
			}
		}
		salariesInventory = salariesInventoryAlpha;
		Collections.sort(salariesInventory);

		ServiceImpl ss = new ServiceImpl();
		for (SalarieBeanExport s : salariesInventory) {
			s.setService(ss.getServiceBeanById(s.getIdServiceSelected())
					.getNom());
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdEntrepriseSelected() {
		return idEntrepriseSelected;
	}

	public void setIdEntrepriseSelected(int idEntrepriseSelected) {
		this.idEntrepriseSelected = idEntrepriseSelected;
	}

	public ArrayList<SelectItem> getEntrepriseList() throws Exception {
		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();

		List<EntrepriseBean> entrepriseBeanList = entrepriseService
				.getEntreprisesList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		entrepriseList = new ArrayList<SelectItem>();
		SelectItem selectItem;
		GroupeServiceImpl grServ = new GroupeServiceImpl();
		GroupeBean groupe = grServ.getGroupeBeanById(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		selectItem = new SelectItem();
		selectItem.setValue(-1);
		selectItem.setLabel(groupe.getNom());
		entrepriseList.add(selectItem);

		for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(entrepriseBean.getId());
			selectItem.setLabel(entrepriseBean.getNom());
			entrepriseList.add(selectItem);
		}
		return entrepriseList;
	}

	public ArrayList<SelectItem> getEntrepriseList2() throws Exception {
		return entrepriseList;
	}

	public String getAlphabetSelected() {
		return alphabetSelected;
	}

	public void setAlphabetSelected(String alphabetSelected) {
		this.alphabetSelected = alphabetSelected;
	}

	public ArrayList<SelectItem> getAlphabetList() {
		return alphabetList;
	}

	public void deleteSalarie(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());

		SalarieBean salarieBean = (SalarieBean) table.getRowData();

		SalarieServiceImpl sal = new SalarieServiceImpl();
		PiecesJustificativesServiceImpl pj = new PiecesJustificativesServiceImpl();
		PiecesJustificativesBean p = new PiecesJustificativesBean();
		if (pj.getPiecesJustificativesBeanListByIdSalarie(salarieBean.getId())
				.size() > 0) {
			p = pj.getPiecesJustificativesBeanListByIdSalarie(
					salarieBean.getId()).get(0);

			pj.delete(p);
		}

		sal.delete(salarieBean);

		SalarieServiceImpl salarieService = new SalarieServiceImpl();
		if (idEntrepriseSelected != -1) {
			salariesInventory = salarieService
					.getSalarieBeanExportListByIdEntreprise(idEntrepriseSelected);
		} else {
			salariesInventory = salarieService
					.getSalariesExportListOrderByEntreprise(Integer
							.parseInt(session.getAttribute("groupe").toString()));
		}

		RemunerationServiceImpl remu = new RemunerationServiceImpl();
		for (SalarieBeanExport s : salariesInventory) {
			List<RemunerationBean> remunerationList = remu
					.getRemunerationByIdSalarie(s.getId());

			if (remunerationList.isEmpty()) {
				s.setHasRemu(false);
			} else {
				s.setHasRemu(true);
			}
		}

		EvenementServiceImpl ev = new EvenementServiceImpl();
		for (SalarieBeanExport s : salariesInventory) {
			List<EvenementBean> list = ev.getEvenementBeanListByIdSalarie(s
					.getId());

			if (list.isEmpty()) {
				s.setHasEvenement(false);
			} else {
				s.setHasEvenement(true);
			}
		}

		int j = salariesInventory.size() - 1;
		for (int i = j; i >= 0; i--) {
			SalarieBean bean = salariesInventory.get(i);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);

			if (!bean.isPresent()) {
				bean.setChangeCouleur(true);
			}

			if (Integer.valueOf(session.getAttribute("afficheSortie")
					.toString()) == 1) {
				if (bean.isPresent()) {
					salariesInventory.remove(salariesInventory.get(i));
				}
			}
			if (Integer.valueOf(session.getAttribute("afficheSortie")
					.toString()) == 2) {
				if (!bean.isPresent()) {
					salariesInventory.remove(salariesInventory.get(i));
				}
			}
		}
		Collections.sort(salariesInventory);
		ServiceImpl ss = new ServiceImpl();
		for (SalarieBeanExport s : salariesInventory) {
			s.setService(ss.getServiceBeanById(s.getIdServiceSelected())
					.getNom());
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

	public void filtre(ValueChangeEvent event) throws Exception {
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
		if (event.getComponent().getId().equals("entrepriseListSalarie")) {
			idEntrepriseSelected = Integer.parseInt(event.getNewValue()
					.toString());
			SalarieServiceImpl sal = new SalarieServiceImpl();
			List<SalarieBean> l = new ArrayList<SalarieBean>();

			if (idEntrepriseSelected != -1) {
				l = sal.getSalarieBeanListByIdEntreprise(idEntrepriseSelected);
			} else {
				l = sal.getSalariesList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
			}

			int c = 0;
			for (SalarieBean s : l) {
				if (getLastParcours(s) != null) {
					if (s.isPresent()
							&& getLastParcours(s).getIdTypeContratSelected() != 3
							&& getLastParcours(s).getIdTypeContratSelected() != 7) {
						c++;
					}
				}
			}
			nbSalarie = c;

			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);
			session.setAttribute("entreprise", idEntrepriseSelected);

			getSalarieListByIdEntreprise(idEntrepriseSelected);
			alphabetList = new ArrayList<SelectItem>();
			if (idEntrepriseSelected != -1)
				initialiserAlphabet();
		}

	}

	public String download() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		int j = 0;

		String[] entete = new String[19];
		if (idEntrepriseSelected == 0 || idEntrepriseSelected == -1) {
			entete = new String[20];
			entete[j] = "Entreprise";
			j++;
		}
		entete[j] = "Nom";
		entete[j + 1] = "Pr\u00E9nom";
		entete[j + 2] = "Sexe";
		entete[j + 3] = "Date de naissance";
		entete[j + 4] = "Age";
		entete[j + 5] = "Adresse";
		entete[j + 6] = "T\u00E9l fixe";
		entete[j + 7] = "T\u00E9l portable";
		entete[j + 8] = "Personne \u00e0 pr\u00E9venir";
		entete[j + 9] = "";
		entete[j + 10] = "";
		entete[j + 11] = "Entr\u00E9e dans l'entreprise";
		entete[j + 12] = "Sortie de l'entreprise";
		entete[j + 13] = "Anciennet\u00E9 dans l'entreprise";
		entete[j + 14] = "Type de contrat";
		entete[j + 15] = "Poste occup\u00E9";
		entete[j + 16] = "Service";
		entete[j + 17] = "Equivalent temps plein";
		entete[j + 18] = "Catégorie Socio-prof.";
		try {
			eContext.getSessionMap().put("datatable", salariesInventory);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Liste Salaries");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", alphabetSelected);
			eContext.getSessionMap().put("present", afficheSortie);
			eContext.getSessionMap().put("debut", debutExtractionGC);
			eContext.getSessionMap().put("fin", finExtractionGC);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelHabilitations() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[13];
		entete[0] = "Ann\u00E9e de r\u00E9f\u00E9rence";
		entete[1] = "Entreprise";
		entete[2] = "Nom";
		entete[3] = "Pr\u00E9nom";
		entete[4] = "Date de naissance";
		entete[5] = "Lieu de naissance";
		entete[6] = "Service";
		entete[7] = "Poste occup\u00E9";
		entete[8] = "Type d'habilitation";
		entete[9] = "Période de Validit\u00E9";
		entete[10] = "";
		entete[11] = "D\u00E9lai de validit\u00E9 (en jours) ";
		entete[12] = "Commentaires";

		try {
			eContext.getSessionMap().put("datatable", salariesInventory);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelHabilitations");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", alphabetSelected);
			eContext.getSessionMap().put("present", afficheSortie);
			eContext.getSessionMap().put("debut", debutExtractionGC);
			eContext.getSessionMap().put("fin", finExtractionGC);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelRemunerations() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[24];
		entete[0] = "Annee";
		entete[1] = "Entreprise";
		entete[2] = "Nom";
		entete[3] = "Prenom";
		entete[4] = "Metier";
		entete[5] = "CSP";
		entete[6] = "Echelon";
		entete[7] = "Niveau";
		entete[8] = "Coef.";
		entete[9] = "Horaire mensuel";
		entete[10] = "Taux horaire brut €";
		entete[11] = "Salaire brut mensuel €";
		entete[12] = "Augmentation collective";
		entete[13] = "Augmentation individuelle";
		entete[14] = "Heures supp.";
		entete[15] = "SALAIRE BRUT ANNUEL";
		entete[16] = "Commissions";
		entete[17] = "Primes fixes";
		entete[18] = "Primes variables";
		entete[19] = "Avantages assujettis";
		entete[20] = "REMUNERATION BRUTE ANNUELLE";
		entete[21] = "Avantages non assujettis";
		entete[22] = "Frais professionnels";
		entete[23] = "TOTAL BRUT ANNUEL €";

		try {
			eContext.getSessionMap().put("datatable", salariesInventory);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelRemunerations");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", alphabetSelected);
			eContext.getSessionMap().put("present", afficheSortie);
			eContext.getSessionMap().put("debut", debutExtractionGC);
			eContext.getSessionMap().put("fin", finExtractionGC);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelFormations() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[19];
		entete[0] = "Ann\u00E9e de r\u00E9f\u00E9rence";
		entete[1] = "Entreprise";
		entete[2] = "Nom";
		entete[3] = "Pr\u00E9nom";
		entete[4] = "Service";
		entete[5] = "Poste occup\u00E9";
		entete[6] = "Formations réalisées";
		entete[7] = "";
		entete[8] = "";
		entete[9] = "";
		entete[10] = "Nature de la formation";
		entete[11] = "Niveau de formation";
		entete[12] = "";
		entete[13] = "Organisme de formation";
		entete[14] = "Coût de la formation en €";
		entete[15] = "";
		entete[16] = "";
		entete[17] = "Gestion du DIF";
		entete[18] = "";

		try {
			eContext.getSessionMap().put("datatable", salariesInventory);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelFormations");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", alphabetSelected);
			eContext.getSessionMap().put("present", afficheSortie);
			eContext.getSessionMap().put("debut", debutExtractionGC);
			eContext.getSessionMap().put("fin", finExtractionGC);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelAbsences() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[11];
		entete[0] = "Ann\u00E9e de r\u00E9f\u00E9rence";
		entete[1] = "Entreprise";
		entete[2] = "Nom";
		entete[3] = "Pr\u00E9nom";
		entete[4] = "Sexe";
		entete[5] = "Service";
		entete[6] = "Poste occup\u00E9";
		entete[7] = "Nature de l'absence ou du cong\u00E9";
		entete[8] = "Début de l'absence";
		entete[9] = "Fin de l'absence";
		entete[10] = "Durée (jours ouvr\u00E9s)";

		try {
			eContext.getSessionMap().put("datatable", salariesInventory);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelAbsences");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", alphabetSelected);
			eContext.getSessionMap().put("present", afficheSortie);
			eContext.getSessionMap().put("debut", debutExtractionGC);
			eContext.getSessionMap().put("fin", finExtractionGC);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelAccidents() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[15];
		entete[0] = "Ann\u00E9e de r\u00E9f\u00E9rence";
		entete[1] = "Entreprise";
		entete[2] = "Nom";
		entete[3] = "Pr\u00E9nom";
		entete[4] = "Service";
		entete[5] = "Poste occup\u00E9";
		entete[6] = "Type d'accident";
		entete[7] = "Date de l'accident";
		entete[8] = "";
		entete[9] = "P\u00E9riode d'absence";
		entete[10] = "";
		entete[11] = "Nbre de jrs d'arr\u00EAt de travail (jrs ouvr\u00E9s)";
		entete[12] = "Si\u00E8ge de la l\u00E9sion";
		entete[13] = "";
		entete[14] = "Cause de l'accident";

		try {
			eContext.getSessionMap().put("datatable", salariesInventory);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelAccidents");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", alphabetSelected);
			eContext.getSessionMap().put("present", afficheSortie);
			eContext.getSessionMap().put("debut", debutExtractionGC);
			eContext.getSessionMap().put("fin", finExtractionGC);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelParcoursProfessionnels() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[17];
		entete[0] = "Entreprise";
		entete[1] = "Nom";
		entete[2] = "Pr\u00E9nom";
		entete[3] = "Service";
		entete[4] = "Date d'entr\u00E9e dans l'entreprise";
		entete[5] = "Date de sortie de l'entreprise";
		entete[6] = "Anciennet\u00E9 dans l'entreprise";
		entete[7] = "Poste occup\u00E9/m\u00E9tier";
		entete[8] = "D\u00E9but de prise de fonction";
		entete[9] = "Fin de prise de fonction";
		entete[10] = "Anciennet\u00E9 dans le m\u00E9tier";
		entete[11] = "Type de contrat de travail";
		entete[12] = "ETP";
		entete[13] = "CSP";
		entete[14] = "Coef.";
		entete[15] = "Niv.";
		entete[16] = "Ech.";

		try {
			eContext.getSessionMap().put("datatable", salariesInventory);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name",
					"exportExcelParcoursProfessionnels");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", alphabetSelected);
			eContext.getSessionMap().put("present", afficheSortie);
			eContext.getSessionMap().put("debut", debutExtractionGC);
			eContext.getSessionMap().put("fin", finExtractionGC);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelContratTravail() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[11];
		entete[0] = "Entreprise";
		entete[1] = "Nom";
		entete[2] = "Pr\u00E9nom";
		entete[3] = "Service";
		entete[4] = "Date d'entr\u00E9e dans l'entreprise";
		entete[5] = "Date de sortie de l'entreprise";
		entete[6] = "Date de d\u00E9but de contrat";
		entete[7] = "Date de fin de contrat";
		entete[8] = "Type de contrat de travail";
		entete[9] = "Motif de rupture";
		entete[10] = "Anciennet\u00E9 dans l'entreprise";

		try {
			eContext.getSessionMap().put("datatable", salariesInventory);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelContratTravail");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", alphabetSelected);
			eContext.getSessionMap().put("present", afficheSortie);
			eContext.getSessionMap().put("debut", debutExtractionGC);
			eContext.getSessionMap().put("fin", finExtractionGC);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelEntretiens() throws Exception {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[14];
		entete[0] = "Ann\u00E9e de r\u00E9f\u00E9rence";
		entete[1] = "Entreprise";
		entete[2] = "Nom";
		entete[3] = "Pr\u00E9nom";
		entete[4] = "Service";
		entete[5] = "Poste occup\u00E9";
		entete[6] = "Objectifs de l'ann\u00E9e N";
		entete[7] = "Evaluation globale du niveau de comp\u00E9tences au poste occup\u00E9";
		entete[8] = "Comp\u00E9tences à am\u00E9liorer";
		entete[9] = "Comp\u00E9tences à acqu\u00E9rir";
		entete[10] = "D\u00E9cision d'\u00E9volution";
		entete[11] = "Besoins en formation";
		entete[12] = "CR sign\u00E9";
		entete[13] = "Fiche de poste modifi\u00E9e";

		SalarieServiceImpl salarieService = new SalarieServiceImpl();
		List<SalarieBeanExport> salalrieList = new ArrayList<SalarieBeanExport>();

		if (idEntrepriseSelected != -1)
			salalrieList = salarieService
					.getSalarieBeanExportListByIdEntrepriseOrderByEntreprise(idEntrepriseSelected);
		else
			salalrieList = salarieService
					.getSalariesExportListOrderByEntreprise(Integer
							.parseInt(session.getAttribute("groupe").toString()));

		try {
			eContext.getSessionMap().put("datatable", salalrieList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelEntretiens");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", alphabetSelected);
			eContext.getSessionMap().put("present", afficheSortie);
			eContext.getSessionMap().put("debut", debutExtractionGC);
			eContext.getSessionMap().put("fin", finExtractionGC);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public boolean isModalLoginRenderedComp() {
		return modalLoginRenderedComp;
	}

	public void setModalLoginRenderedComp(boolean modalLoginRenderedComp) {
		this.modalLoginRenderedComp = modalLoginRenderedComp;
	}

	public boolean isModalLoginRenderedEnt() {
		return modalLoginRenderedEnt;
	}

	public void setModalLoginRenderedEnt(boolean modalLoginRenderedEnt) {
		this.modalLoginRenderedEnt = modalLoginRenderedEnt;
	}

	public boolean isLoggedRendered() {
		return loggedRendered;
	}

	public void setLoggedRendered(boolean loggedRendered) {
		this.loggedRendered = loggedRendered;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void toggleModalLoginRenderedComp(ActionEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("identifie") != null) {
			this.loggedRendered = true;
		} else {
			this.loggedRendered = false;
		}
		modalLoginRenderedComp = !modalLoginRenderedComp;
	}

	public void toggleModalLoginRenderedEnt(ActionEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("identifie") != null) {
			this.loggedRendered = true;
		} else {
			this.loggedRendered = false;
		}
		modalLoginRenderedEnt = !modalLoginRenderedEnt;
	}

	public void toggleModalLoginRenderedRemu(ActionEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		this.loggedRendered = false;
		modalLoginRenderedRemu = !modalLoginRenderedRemu;
	}

	public String logCompetences() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("identifie") != null) {
			loggedRendered = true;
			return exportExcelCompetences();
		} else {
			ResourceBundle bundle = ResourceBundle.getBundle("CCI");

			String mdp = bundle.getString("cci");

			if (mdp.equals(this.passwordComp)) {
				modalLoginRenderedComp = false;
				loggedRendered = true;
				facesContext = FacesContext.getCurrentInstance();
				session = (HttpSession) facesContext.getExternalContext()
						.getSession(false);
				session.setAttribute("identifie", true);

				return exportExcelCompetences();
			} else {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Mot de passe non valide",
						"Mot de passe non valide");
				FacesContext.getCurrentInstance().addMessage(
						"idForm:TxtPwSuiviTabComp", message);

				return "";
			}
		}

	}

	public String logEntretiens() throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("identifie") != null) {
			loggedRendered = true;
			return exportExcelEntretiens();
		} else {
			ResourceBundle bundle = ResourceBundle.getBundle("CCI");

			String mdp = bundle.getString("cci");

			if (mdp.equals(this.passwordEnt)) {
				modalLoginRenderedEnt = false;
				loggedRendered = true;
				facesContext = FacesContext.getCurrentInstance();
				session = (HttpSession) facesContext.getExternalContext()
						.getSession(false);
				session.setAttribute("identifie", true);

				return exportExcelEntretiens();
			} else {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Mot de passe non valide",
						"Mot de passe non valide");
				FacesContext.getCurrentInstance().addMessage(
						"idForm:TxtPwSuiviTabEnt", message);

				return "";
			}
		}

	}

	public String logRemu() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		ResourceBundle bundle = ResourceBundle.getBundle("CCI");

		String mdp = bundle.getString("cci2");

		if (mdp.equals(this.passwordRemu)) {
			modalLoginRenderedRemu = false;
			loggedRendered = true;

			return exportExcelRemunerations();
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Mot de passe non valide",
					"Mot de passe non valide");
			FacesContext.getCurrentInstance().addMessage(
					"idForm:TxtPwSuiviRemu", message);

			return "";
		}

	}

	public void processTabChange(TabChangeEvent tabChangeEvent)
			throws Exception {
		switch (tabChangeEvent.getNewTabIndex()) {
		case 0:
			alphabetSelected = null;
			break;
		case 1:
			alphabetSelected = "A";
			break;
		case 2:
			alphabetSelected = "B";
			break;
		case 3:
			alphabetSelected = "C";
			break;
		case 4:
			alphabetSelected = "D";
			break;
		case 5:
			alphabetSelected = "E";
			break;
		case 6:
			alphabetSelected = "F";
			break;
		case 7:
			alphabetSelected = "G";
			break;
		case 8:
			alphabetSelected = "H";
			break;
		case 9:
			alphabetSelected = "I";
			break;
		case 10:
			alphabetSelected = "J";
			break;
		case 11:
			alphabetSelected = "K";
			break;
		case 12:
			alphabetSelected = "L";
			break;
		case 13:
			alphabetSelected = "M";
			break;
		case 14:
			alphabetSelected = "N";
			break;
		case 15:
			alphabetSelected = "O";
			break;
		case 16:
			alphabetSelected = "P";
			break;
		case 17:
			alphabetSelected = "Q";
			break;
		case 18:
			alphabetSelected = "R";
			break;
		case 19:
			alphabetSelected = "S";
			break;
		case 20:
			alphabetSelected = "T";
			break;
		case 21:
			alphabetSelected = "U";
			break;
		case 22:
			alphabetSelected = "V";
			break;
		case 23:
			alphabetSelected = "W";
			break;
		case 24:
			alphabetSelected = "X";
			break;
		case 25:
			alphabetSelected = "Y";
			break;
		case 26:
			alphabetSelected = "Z";
			break;
		default:
			alphabetSelected = null;
			break;
		}
		getSalariesInventory();
	}

	public String exportExcelCompetences() {

		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[0];

		try {
			eContext.getSessionMap().put("datatable", salariesInventory);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelCompetences");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", alphabetSelected);
			eContext.getSessionMap().put("present", afficheSortie);
			eContext.getSessionMap().put("debut", debutExtractionGC);
			eContext.getSessionMap().put("fin", finExtractionGC);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return "";
	}

	public int isAfficheSortie() {
		return afficheSortie;
	}

	public void setAfficheSortie(int afficheSortie) {
		this.afficheSortie = afficheSortie;
	}

	public void changeSortieSalarie(ActionEvent event) throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		session.setAttribute("afficheSortie", 1);
		session.setAttribute("filtre", "Sélection en cours : Salariés sortis");
		afficheSortie = 1;
		filtreChoisi = "Sélection en cours : Salariés sortis";
		if (alphabetSelected != null) {
			getSalarieListByAlphabet(idEntrepriseSelected, alphabetSelected);
		} else {
			getSalarieListByIdEntreprise(idEntrepriseSelected);
		}
	}

	public void changePresentSalarie(ActionEvent event) throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		session.setAttribute("afficheSortie", 2);
		session.setAttribute("filtre", "Sélection en cours : Salariés présents");
		afficheSortie = 2;
		filtreChoisi = "Sélection en cours : Salariés présents";
		if (alphabetSelected != null) {
			getSalarieListByAlphabet(idEntrepriseSelected, alphabetSelected);
		} else {
			getSalarieListByIdEntreprise(idEntrepriseSelected);
		}
	}

	public void changeTousSalarie(ActionEvent event) throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		session.setAttribute("afficheSortie", 0);
		session.setAttribute("filtre", "Sélection en cours : Tous les salariés");
		afficheSortie = 0;
		filtreChoisi = "Sélection en cours : Tous les salariés";
		if (alphabetSelected != null) {
			getSalarieListByAlphabet(idEntrepriseSelected, alphabetSelected);
		} else {
			getSalarieListByIdEntreprise(idEntrepriseSelected);
		}
	}

	public String getFiltreChoisi() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("filtre") == null) {
			return filtreChoisi;
		} else {
			return session.getAttribute("filtre").toString();
		}
	}

	public void setFiltreChoisi(String filtreChoisi) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		session.setAttribute("filtre", filtreChoisi);
		this.filtreChoisi = filtreChoisi;
	}

	public Date getDebutExtraction() {
		return debutExtraction;
	}

	public void setDebutExtraction(Date debutExtraction) {
		this.debutExtraction = debutExtraction;
		if (debutExtraction != null) {
			this.debutExtractionGC = new GregorianCalendar();
			this.debutExtractionGC.setTime(debutExtraction);
		} else {
			this.debutExtractionGC = null;
		}

	}

	public Date getFinExtraction() {
		return finExtraction;
	}

	public void setFinExtraction(Date finExtraction) {
		this.finExtraction = finExtraction;
		if (finExtraction != null) {
			this.finExtractionGC = new GregorianCalendar();
			this.finExtractionGC.setTime(finExtraction);
		} else
			this.finExtractionGC = null;
	}

	public boolean isColorButtonDisplay() throws Exception {
		HabilitationServiceImpl hs = new HabilitationServiceImpl();
		if (this.idEntrepriseSelected == -1) {
			for (HabilitationBean h : hs.getHabilitationsList(Integer
					.parseInt(session.getAttribute("groupe").toString()))) {
				if (getNbJoursValidite(h).intValue() < 90) {
					colorButtonDisplay = true;
					break;
				}
			}
		} else {
			SalarieServiceImpl ss = new SalarieServiceImpl();
			for (SalarieBean s : ss
					.getSalarieBeanListByIdEntreprise(this.idEntrepriseSelected)) {
				for (HabilitationBean h : s.getHabilitationBeanList()) {
					if (getNbJoursValidite(h).intValue() < 90) {
						colorButtonDisplay = true;
						break;
					}
				}
			}
		}
		return colorButtonDisplay;
	}

	private Long getNbJoursValidite(HabilitationBean h) {

		Long diffMillis;
		Long diffenjours = null;
		GregorianCalendar dateExpiration = new GregorianCalendar();
		GregorianCalendar dateDuJour = new GregorianCalendar();
		GregorianCalendar dateDelivrance = new GregorianCalendar();

		dateExpiration.setTime(h.getExpiration());
		dateDelivrance.setTime(h.getDelivrance());

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

	public void setColorButtonDisplay(boolean colorButtonDisplay) {
		this.colorButtonDisplay = colorButtonDisplay;
	}

	public boolean isModalLoginRenderedRemu() {
		return modalLoginRenderedRemu;
	}

	public void setModalLoginRenderedRemu(boolean modalLoginRenderedRemu) {
		this.modalLoginRenderedRemu = modalLoginRenderedRemu;
	}

	public String getPasswordRemu() {
		return passwordRemu;
	}

	public void setPasswordRemu(String passwordRemu) {
		this.passwordRemu = passwordRemu;
	}

	public Integer getNbSalarie() {
		return nbSalarie;
	}

	public void setNbSalarie(Integer nbSalarie) {
		this.nbSalarie = nbSalarie;
	}

	public String getLibelleNbSalarie() {
		if (this.nbSalarie == 0) {
			return "Aucun salarié";
		} else {
			if (this.nbSalarie == 1) {
				return "1 salarié";
			} else {
				return this.nbSalarie + " salariés";
			}
		}
	}

	public void setLibelleNbSalarie(String libelleNbSalarie) {
		this.libelleNbSalarie = libelleNbSalarie;
	}

	public boolean isEchec() {
		return echec;
	}

	public void setEchec(boolean echec) {
		this.echec = echec;
	}

	public void echecOk() {
		this.echec = false;
	}

	public void reussiteOk() {
		this.reussite = false;
	}

	public String getUrl() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		return Utils.getSessionFileUploadPathExport(session);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isReussite() {
		return reussite;
	}

	public void setReussite(boolean reussite) {
		this.reussite = reussite;
	}

	public String getPasswordEnt() {
		return passwordEnt;
	}

	public void setPasswordEnt(String passwordEnt) {
		this.passwordEnt = passwordEnt;
	}

	public String getPasswordComp() {
		return passwordComp;
	}

	public void setPasswordComp(String passwordComp) {
		this.passwordComp = passwordComp;
	}

	public String getCciRattachement() {
		return cciRattachement;
	}

	public void setCciRattachement(String cciRattachement) {
		this.cciRattachement = cciRattachement;
	}

}
