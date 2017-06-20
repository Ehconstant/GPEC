package com.cci.gpec.web.backingBean.salarie;

import java.io.File;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.ContratTravailBean;
import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.EvenementBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.LienRemunerationRevenuBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.PersonneAChargeBean;
import com.cci.gpec.commons.PiecesJustificativesBean;
import com.cci.gpec.commons.RemunerationBean;
import com.cci.gpec.commons.RevenuComplementaireBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.AccidentServiceImpl;
import com.cci.gpec.metier.implementation.ContratTravailServiceImpl;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.EntretienServiceImpl;
import com.cci.gpec.metier.implementation.EvenementServiceImpl;
import com.cci.gpec.metier.implementation.FormationServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.HabilitationServiceImpl;
import com.cci.gpec.metier.implementation.LienRemunerationRevenuServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.ParcoursServiceImpl;
import com.cci.gpec.metier.implementation.PersonneAChargeServiceImpl;
import com.cci.gpec.metier.implementation.PiecesJustificativesServiceImpl;
import com.cci.gpec.metier.implementation.RemunerationServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.navigation.NavigationBB;
import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.component.paneltabset.TabChangeEvent;
import com.icesoft.faces.context.effects.JavascriptContext;

public class SalarieFormBB {

	// primary key
	private int id;

	// fields
	private String civilite;
	private String nom;
	private String prenom;
	private String adresse;
	private Date dateNaissance;
	private String lieuNaissance;
	private String telephone;
	private Double creditDif;
	private Double creditDifTmp;
	private String creditDifTmpDisplay;
	private Integer idLienSubordination;
	private String nivFormationInit;
	private String nivFormationAtteint;
	private String cv;
	private boolean present = true;

	private String telephonePortable;
	private String situationFamiliale;
	private String personneAPrevenirNom;
	private String personneAPrevenirPrenom;
	private String personneAPrevenirAdresse;
	private String personneAPrevenirTelephone;
	private String personneAPrevenirLienParente;
	private boolean impression;
	private boolean possedePermisConduire;
	private String commentaire;
	private boolean hasEvenement = false;

	private Integer pjn = null;

	private boolean displayButton = false;

	private Date dateLastSaisieDif;
	private Double creditDifAnnuel;
	private Double creditDifAnnuelTmp;

	private ArrayList<SelectItem> entrepriseList;
	private List<HabilitationBean> habilitationBeanList;
	private List<AbsenceBean> absenceBeanList;
	private List<AbsenceBean> absenceBeanListRefresh;

	private List<AccidentBean> accidentBeanList;
	private List<EntretienBean> entretienBeanList;
	private List<FormationBean> formationBeanList;
	private List<ParcoursBean> parcoursBeanList;
	private ArrayList<SelectItem> entreprisesList;
	private List<RemunerationBean> remunerationBeanList;
	private List<EvenementBean> evenementBeanList;
	private List<ContratTravailBean> contratTravailBeanList;
	private int idRem = 0;
	private List<RevenuComplementaireBean> commissionBeanList;
	private List<RevenuComplementaireBean> primeFixeBeanList;
	private List<RevenuComplementaireBean> primeVariableBeanList;
	private List<RevenuComplementaireBean> avantageAssujettiBeanList;
	private List<RevenuComplementaireBean> avantageNonAssujettiBeanList;

	private List<PersonneAChargeBean> personneAChargeBeanList;
	private List<PersonneAChargeBean> personneAChargeBeanListTemp;
	private List<PersonneAChargeBean> personneAChargeBeanListDeleted;
	private PersonneAChargeBean modifPersonneACharge;

	private PiecesJustificativesBean piecesJustificativesBean;

	private boolean carteIdentite = false;
	private boolean attestationSecu = false;
	private boolean permisConduire = false;
	private boolean rib = false;
	private boolean diplomes = false;
	private boolean certificatTravail = false;
	private boolean attestationVisiteMedicale = false;
	private boolean habilitations = false;

	private boolean modalRenderedTest = false;
	private boolean modalFormationAide = false;
	private boolean modalTransmissionAide = false;

	private boolean newSalarieTab = false;
	private Integer idEntrepriseSelected;
	private String nomEntreprise;
	private String entreprise;

	private Integer idServiceSelected;
	private ArrayList<SelectItem> servicesList;
	private String libelleService;

	private boolean modalLoginRendered = false;
	private boolean modalLoginRenderedRemu = false;
	private boolean loggedRendered = false;
	private String password;
	private String passwordRemu;

	private String username = "";

	private Date debutExtraction;
	private Date finExtraction;

	private Integer debutPo;
	private Integer finPo;
	private List<SelectItem> anneesPo;

	private Integer debutCt;
	private Integer finCt;
	private List<SelectItem> anneesCt;

	private Integer debutRe;
	private Integer finRe;
	private List<SelectItem> anneesRe;

	private Integer debutHa;
	private Integer finHa;
	private List<SelectItem> anneesHa;

	private Integer debutFo;
	private Integer finFo;
	private List<SelectItem> anneesFo;

	private Integer debutAb;
	private Integer finAb;
	private List<SelectItem> anneesAb;

	private Integer debutAc;
	private Integer finAc;
	private List<SelectItem> anneesAc;

	private Integer debutEn;
	private Integer finEn;
	private List<SelectItem> anneesEn;

	private Integer debutEv;
	private Integer finEv;
	private List<SelectItem> anneesEv;

	// files associated with the current user
	private List<InputFileData> fileList = new ArrayList<InputFileData>();

	private boolean fileError = false;

	// latest file uploaded by client
	private InputFileData currentFile;
	// file upload completed percent (Progress)
	private int fileProgress = 0;

	private ArrayList<SelectItem> salariesList;

	private ArrayList<SelectItem> nivFormationInitList;
	private ArrayList<SelectItem> nivFormationAtteintList;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	private String nomGroupe;
	private String nomGroupeForUpload;

	public SalarieFormBB() throws Exception {
		init();

	}

	public String getUrl(String type) throws Exception {
		return Utils.getFileUrl(this.id, type, false, false, false, false,
				nomGroupe);
	}

	public String init() throws Exception {
		if (session.getAttribute("groupe") != null
				&& (Integer) session.getAttribute("groupe") != 0) {
			this.newSalarieTab = true;
			servicesList = new ArrayList<SelectItem>();

			GroupeServiceImpl grpServ = new GroupeServiceImpl();
			nomGroupe = grpServ
					.getGroupeBeanById(
							Integer.parseInt(session.getAttribute("groupe")
									.toString())).getNom();

			SalarieServiceImpl salarieService = new SalarieServiceImpl();

			List<SalarieBean> salarieBeanList = salarieService
					.getSalariesList(Integer.parseInt(session.getAttribute(
							"groupe").toString()));
			salariesList = new ArrayList<SelectItem>();
			SelectItem selectItemSalarie;

			for (SalarieBean salarieBean : salarieBeanList) {
				if (salarieBean.isPresent()) {
					ParcoursBean actu = new ParcoursBean();
					Calendar dateActu = new GregorianCalendar();
					dateActu.setTime(new Date());
					for (ParcoursBean p : salarieBean.getParcoursBeanList()) {
						if (p.getDebutFonction().before(dateActu.getTime())) {
							if (p.getFinFonction() != null) {
								if (p.getFinFonction()
										.after(dateActu.getTime())) {
									actu = p;
									break;
								}
							} else {
								actu = p;
								break;
							}
						}
					}

					selectItemSalarie = new SelectItem();
					selectItemSalarie.setValue(salarieBean.getId());
					if (actu.getNomMetier() != null) {
						selectItemSalarie.setLabel(salarieBean.getNom() + " "
								+ salarieBean.getPrenom() + " - "
								+ actu.getNomMetier());
					} else {
						selectItemSalarie.setLabel(salarieBean.getNom() + " "
								+ salarieBean.getPrenom() + " - ");
					}
					salariesList.add(selectItemSalarie);
				}
			}

			EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();

			List<EntrepriseBean> entrepriseBeanList = entrepriseService
					.getEntreprisesList(Integer.parseInt(session.getAttribute(
							"groupe").toString()));
			entreprisesList = new ArrayList<SelectItem>();
			SelectItem selectItem;
			for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
				selectItem = new SelectItem();
				selectItem.setValue(entrepriseBean.getId());
				selectItem.setLabel(entrepriseBean.getNom());
				entreprisesList.add(selectItem);
			}

			this.absenceBeanList = new ArrayList<AbsenceBean>();
			this.absenceBeanListRefresh = new ArrayList<AbsenceBean>();

			this.accidentBeanList = new ArrayList<AccidentBean>();
			this.adresse = new String();
			this.civilite = new String();
			this.commentaire = new String();
			this.creditDif = 0.0;
			this.dateNaissance = null;
			this.entretienBeanList = new ArrayList<EntretienBean>();
			this.formationBeanList = new ArrayList<FormationBean>();
			this.habilitationBeanList = new ArrayList<HabilitationBean>();
			this.remunerationBeanList = new ArrayList<RemunerationBean>();
			this.evenementBeanList = new ArrayList<EvenementBean>();
			this.contratTravailBeanList = new ArrayList<ContratTravailBean>();
			this.commissionBeanList = new ArrayList<RevenuComplementaireBean>();
			this.primeFixeBeanList = new ArrayList<RevenuComplementaireBean>();
			this.primeVariableBeanList = new ArrayList<RevenuComplementaireBean>();
			this.avantageAssujettiBeanList = new ArrayList<RevenuComplementaireBean>();
			this.avantageNonAssujettiBeanList = new ArrayList<RevenuComplementaireBean>();
			this.personneAChargeBeanList = new ArrayList<PersonneAChargeBean>();
			this.personneAChargeBeanListTemp = new ArrayList<PersonneAChargeBean>();
			this.personneAChargeBeanListTemp.add(new PersonneAChargeBean());
			this.personneAChargeBeanListDeleted = new ArrayList<PersonneAChargeBean>();

			this.piecesJustificativesBean = new PiecesJustificativesBean();
			this.id = 0;
			this.lieuNaissance = new String();
			this.nom = new String();
			this.parcoursBeanList = new ArrayList<ParcoursBean>();
			this.prenom = new String();
			this.telephone = new String();
			this.present = new Boolean(true);

			this.idLienSubordination = -1;
			this.nivFormationAtteint = new String();
			this.nivFormationInit = new String();
			this.cv = new String();
			this.fileProgress = 0;
			this.fileList.clear();

			telephonePortable = null;
			situationFamiliale = null;
			personneAPrevenirNom = null;
			personneAPrevenirPrenom = null;
			personneAPrevenirAdresse = null;
			personneAPrevenirTelephone = null;
			personneAPrevenirLienParente = null;
			impression = true;
			possedePermisConduire = false;

			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);

			if (session.getAttribute("entreprise") != null) {
				this.idEntrepriseSelected = Integer.parseInt(session
						.getAttribute("entreprise").toString());
			} else {
				this.idEntrepriseSelected = 0;
			}

			this.idServiceSelected = -1;

			this.modalLoginRendered = false;
			this.modalLoginRenderedRemu = false;
			this.loggedRendered = false;
			this.password = new String();
			this.passwordRemu = new String();

			this.dateLastSaisieDif = null;
			this.creditDifAnnuel = null;
			this.creditDifAnnuelTmp = null;

			this.carteIdentite = false;
			this.attestationSecu = false;
			this.permisConduire = false;
			this.rib = false;
			this.diplomes = false;
			this.certificatTravail = false;
			this.attestationVisiteMedicale = false;
			this.habilitations = false;

			debutPo = null;
			finPo = null;
			anneesPo = new ArrayList<SelectItem>();

			debutCt = null;
			finCt = null;
			anneesCt = new ArrayList<SelectItem>();

			debutRe = null;
			finRe = null;
			anneesRe = new ArrayList<SelectItem>();

			debutHa = null;
			finHa = null;
			anneesHa = new ArrayList<SelectItem>();

			debutFo = null;
			finFo = null;
			anneesFo = new ArrayList<SelectItem>();

			debutAb = null;
			finAb = null;
			anneesAb = new ArrayList<SelectItem>();

			debutAc = null;
			finAc = null;
			anneesAc = new ArrayList<SelectItem>();

			debutEn = null;
			finEn = null;
			anneesEn = new ArrayList<SelectItem>();

			debutEv = null;
			finEv = null;
			anneesEv = new ArrayList<SelectItem>();
			nivFormationInitList = new ArrayList<SelectItem>();

			nivFormationInitList.add(new SelectItem("Inconnu", "Inconnu"));
			nivFormationInitList.add(new SelectItem("VB", "V bis"));
			nivFormationInitList.add(new SelectItem("V", "V"));
			nivFormationInitList.add(new SelectItem("IV", "IV"));
			nivFormationInitList.add(new SelectItem("III", "III"));
			nivFormationInitList.add(new SelectItem("II", "II"));
			nivFormationInitList.add(new SelectItem("I", "I"));

			nivFormationAtteintList = nivFormationInitList;

			session.setAttribute("idSalarie", "-1");
			activateTestSaveBeforeExit();

			return "addSalarie";
		} else {
			return "";
		}
	}

	public void updateDate(ValueChangeEvent evt) {
		if (evt.getComponent().getId().equals("dateDebutExtractionCV")) {
			this.debutExtraction = (Date) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("dateFinExtractionCV")) {
			this.finExtraction = (Date) evt.getNewValue();
		}

	}
	
	public ArrayList<SelectItem> getEntrepriseList() throws Exception {
		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();

		List<EntrepriseBean> entrepriseBeanList = entrepriseService
				.getEntreprisesList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		entrepriseList = new ArrayList<SelectItem>();
		SelectItem selectItem;
//		GroupeServiceImpl grServ = new GroupeServiceImpl();
//		GroupeBean groupe = grServ.getGroupeBeanById(Integer.parseInt(session
//				.getAttribute("groupe").toString()));
//		selectItem = new SelectItem();
//		selectItem.setValue(-1);
//		selectItem.setLabel(groupe.getNom());
//		entrepriseList.add(selectItem);

		for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(entrepriseBean.getId());
			selectItem.setLabel(entrepriseBean.getNom());
			entrepriseList.add(selectItem);
		}
		return entrepriseList;
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

	public void addPersonneACharge(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		PersonneAChargeBean personneAChargeBean = (PersonneAChargeBean) table
				.getRowData();

		if (personneAChargeBean.getDateNaissance() != null
				|| !personneAChargeBean.getLienParente().equals("")
				|| !personneAChargeBean.getNom().equals("")
				|| !personneAChargeBean.getPrenom().equals("")) {
			modifPersonneACharge = null;

			this.personneAChargeBeanList.add(personneAChargeBean);

			Collections.sort(this.personneAChargeBeanList);

			this.personneAChargeBeanListTemp.remove(personneAChargeBean);
			this.personneAChargeBeanListTemp.add(new PersonneAChargeBean());

			displayButton = false;
		}

	}

	public void modifierPersonneACharge(ActionEvent evt) {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		PersonneAChargeBean personneAChargeBean = (PersonneAChargeBean) table
				.getRowData();

		modifPersonneACharge = new PersonneAChargeBean();
		modifPersonneACharge.setId(personneAChargeBean.getId());
		modifPersonneACharge.setIdSalarie(personneAChargeBean.getIdSalarie());
		modifPersonneACharge.setDateNaissance(personneAChargeBean
				.getDateNaissance());
		modifPersonneACharge.setLienParente(personneAChargeBean
				.getLienParente());
		modifPersonneACharge.setNom(personneAChargeBean.getNom());
		modifPersonneACharge.setPrenom(personneAChargeBean.getPrenom());

		this.personneAChargeBeanListTemp.clear();
		this.personneAChargeBeanList.remove(personneAChargeBean);
		this.personneAChargeBeanListTemp.add(personneAChargeBean);

		displayButton = true;

	}

	public void removePersonneACharge(ActionEvent evt) {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		PersonneAChargeBean personneAChargeBean = (PersonneAChargeBean) table
				.getRowData();

		this.personneAChargeBeanList.remove(personneAChargeBean);

		this.personneAChargeBeanListDeleted.add(personneAChargeBean);
	}

	public void cancelPersonneACharge(ActionEvent evt) {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		PersonneAChargeBean personneAChargeBean = (PersonneAChargeBean) table
				.getRowData();
		if (modifPersonneACharge != null) {
			this.personneAChargeBeanList.add(modifPersonneACharge);
		}
		this.personneAChargeBeanListTemp.remove(personneAChargeBean);
		this.personneAChargeBeanListTemp.clear();
		this.personneAChargeBeanListTemp.add(new PersonneAChargeBean());
		modifPersonneACharge = null;
		displayButton = false;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNomPrenom() {
		return nom + " " + prenom;
	}

	public String saveOrUpdateSalarie() throws Exception {

		if (this.idEntrepriseSelected != -1
				&& this.idServiceSelected != -1
				&& !this.nom.equals("")
				&& !this.prenom.equals("")
				&& this.dateNaissance != null
				&& ((!this.telephone.equals("") && (this.telephone.length() >= 1 && this.telephone
						.length() <= 14)) || this.telephone.equals(""))
				&& ((!this.telephonePortable.equals("") && (this.telephonePortable
						.length() >= 1 && this.telephonePortable.length() <= 14)) || this.telephonePortable
						.equals(""))
				&& ((!this.personneAPrevenirTelephone.equals("") && (this.personneAPrevenirTelephone
								.length() >= 1 && this.personneAPrevenirTelephone.length() <= 14)) || this.personneAPrevenirTelephone
								.equals(""))) {

			this.newSalarieTab = false;
			SalarieServiceImpl salarieService = new SalarieServiceImpl();
			SalarieBean salarieBean = new SalarieBean();

			salarieBean.setAdresse(this.adresse);
			salarieBean.setCivilite(this.civilite);
			salarieBean.setCreditDif(this.creditDif);
			salarieBean.setDateNaissance(this.dateNaissance);
			salarieBean.setId(this.id);
			salarieBean.setLieuNaissance(this.lieuNaissance);
			salarieBean.setNom(this.nom);
			salarieBean.setPrenom(this.prenom);
			salarieBean.setTelephone(this.telephone);
			salarieBean.setHabilitationBeanList(this.habilitationBeanList);
			salarieBean.setAbsenceBeanList(this.absenceBeanList);
			salarieBean.setAccidentBeanList(this.accidentBeanList);
			salarieBean.setEntretienBeanList(this.entretienBeanList);
			salarieBean.setFormationBeanList(this.formationBeanList);
			salarieBean.setParcoursBeanList(this.parcoursBeanList);
			salarieBean.setPresent(this.present);

			salarieBean.setTelephonePortable(this.telephonePortable);
			salarieBean.setSituationFamiliale(this.situationFamiliale);
			salarieBean.setPersonneAPrevenirNom(this.personneAPrevenirNom);
			salarieBean
					.setPersonneAPrevenirPrenom(this.personneAPrevenirPrenom);
			salarieBean
					.setPersonneAPrevenirAdresse(this.personneAPrevenirAdresse);
			salarieBean
					.setPersonneAPrevenirTelephone(this.personneAPrevenirTelephone);
			salarieBean
					.setPersonneAPrevenirLienParente(this.personneAPrevenirLienParente);
			salarieBean.setCommentaire(this.commentaire);
			salarieBean.setImpression(this.impression);
			salarieBean.setPossedePermisConduire(this.possedePermisConduire);

			if (this.idLienSubordination != null
					&& this.idLienSubordination > 0) {
				salarieBean.setIdLienSubordination(this.idLienSubordination);
			} else {
				if (this.idLienSubordination == null)
					this.idLienSubordination = -2;
				int nbChef = nbChef(idEntrepriseSelected);
				if (nbChef != 0 && (this.idLienSubordination == 0)) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Le poste de directeur est d\u00E9j\u00E0 s\u00E9lectionn\u00E9.",
							"Le poste de directeur est d\u00E9j\u00E0 s\u00E9lectionn\u00E9.");
					FacesContext.getCurrentInstance().addMessage(
							"idSalarieForm:idSalarieTabSet:0:sepCnty", message);
					return "";
				}
				if (this.idLienSubordination != null
						&& this.idLienSubordination == 0) {
					salarieBean
							.setIdLienSubordination(this.idLienSubordination);
				}
			}
			salarieBean.setNivFormationAtteint(this.nivFormationAtteint);
			salarieBean.setNivFormationInit(this.nivFormationInit);
			salarieBean.setCv(this.cv);

			salarieBean.setDateLastSaisieDif(this.dateLastSaisieDif);

			salarieBean.setIdEntrepriseSelected(this.idEntrepriseSelected);

			if (this.idServiceSelected != null && this.idServiceSelected > 0) {
				salarieBean.setIdServiceSelected(this.idServiceSelected);
			} else {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Veuillez s\u00E9lectionner une valeur.",
						"Veuillez s\u00E9lectionner une valeur.");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idServiceList",
						message);
				return "";
			}
			salarieService.saveOrUppdate(salarieBean);
			this.id = salarieBean.getId();

			if (salarieBean.getId() != 0) {

				PiecesJustificativesServiceImpl PJImpl = new PiecesJustificativesServiceImpl();
				if (this.piecesJustificativesBean == null) {
					this.piecesJustificativesBean = new PiecesJustificativesBean();
				}

				this.piecesJustificativesBean.setIdSalarie(salarieBean.getId());

				this.piecesJustificativesBean
						.setAttestaionVisiteMedicale(attestationVisiteMedicale);
				this.piecesJustificativesBean
						.setAttestationSecu(attestationSecu);
				this.piecesJustificativesBean.setCarteIdentite(carteIdentite);
				this.piecesJustificativesBean
						.setCertificatTravail(certificatTravail);
				this.piecesJustificativesBean.setDiplomes(diplomes);
				this.piecesJustificativesBean.setHabilitations(habilitations);
				this.piecesJustificativesBean.setPermisConduire(permisConduire);
				this.piecesJustificativesBean.setRib(rib);
				PJImpl.saveOrUppdate(this.piecesJustificativesBean);
			}

			PersonneAChargeServiceImpl persAC = new PersonneAChargeServiceImpl();
			for (PersonneAChargeBean p : this.personneAChargeBeanListDeleted) {
				persAC.delete(p);
			}
			this.personneAChargeBeanListDeleted.clear();
			for (PersonneAChargeBean p : this.personneAChargeBeanList) {
				p.setIdSalarie(salarieBean.getId());
				persAC.saveOrUppdate(p);
			}

			desactivateTestSaveBeforeExit();
			if (this.id == 0) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) facesContext
						.getExternalContext().getSession(false);

				session.putValue("entreprise", this.idEntrepriseSelected);
			}
			initSalarieForm();
			return "addSalarie";
		} else {
			if (!this.telephone.equals("") && this.telephone.length() > 14) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le num\u00E9ro de t\u00E9l\u00E9phone ne doit pas d\u00E9passer 14 caract\u00E8res",
						"Le num\u00E9ro de t\u00E9l\u00E9phone ne doit pas d\u00E9passer 14 caract\u00E8res");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idTel", message);
			}
			if (!this.telephonePortable.equals("")
					&& this.telephonePortable.length() > 14) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le num\u00E9ro de t\u00E9l\u00E9phone ne doit pas d\u00E9passer 14 caract\u00E8res",
						"Le num\u00E9ro de t\u00E9l\u00E9phone ne doit pas d\u00E9passer 14 caract\u00E8res");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idTelP", message);
			}
			if (!this.personneAPrevenirTelephone.equals("")
					&& this.personneAPrevenirTelephone.length() > 14) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le num\u00E9ro de t\u00E9l\u00E9phone ne doit pas d\u00E9passer 14 caract\u00E8res",
						"Le num\u00E9ro de t\u00E9l\u00E9phone ne doit pas d\u00E9passer 14 caract\u00E8res");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idPAPTel", message);
			}
			if (this.idEntrepriseSelected == -1) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire.",
						"Champ obligatoire.");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idEntrepriseList",
						message);
			}
			if (this.idServiceSelected == -1) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire.",
						"Champ obligatoire.");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idServiceListS",
						message);
			}
			if (this.nom.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire.",
						"Champ obligatoire.");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idNom", message);
			}
			if (this.prenom.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire.",
						"Champ obligatoire.");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idPrenom", message);
			}
			if (this.dateNaissance == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Champ obligatoire.",
						"Champ obligatoire.");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:date1", message);
			}
			return "";
		}
	}

//	public ArrayList<SelectItem> getEntreprisesList() {
//		return entreprisesList;
//	}

	private int nbChef(int entreprise) throws Exception {
		int res = 0;
		SalarieServiceImpl salarServ = new SalarieServiceImpl();
		List<SalarieBean> l = salarServ.getSalariesList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		for (int i = 0; i < l.size(); i++) {
			SalarieBean bean = l.get(i);
			if ((bean.getId() != id)
					&& (bean.getIdEntrepriseSelected() == entreprise)
					&& (bean.getIdLienSubordination() != null)
					&& (bean.getIdLienSubordination() == 0)
					&& (bean.isPresent()))
				res++;
		}
		return res;
	}

	public ArrayList<SelectItem> getServicesList() {

		servicesList = new ArrayList<SelectItem>();

		if (idEntrepriseSelected > 0) {
			ServiceImpl service = new ServiceImpl();

			ArrayList<ServiceBean> serviceBeanList;

			try {
				serviceBeanList = (ArrayList<ServiceBean>) service
						.getServicesList(Integer.parseInt(session.getAttribute(
								"groupe").toString()));

				for (ServiceBean serviceBean : serviceBeanList) {
					if (idEntrepriseSelected.equals(serviceBean
							.getIdEntreprise())) {

						servicesList.add(new SelectItem(new Integer(serviceBean
								.getId()), serviceBean.getNom()));
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return servicesList;
	}

	public void setServicesList(ArrayList<SelectItem> servicesList) {
		this.servicesList = servicesList;
	}

	public ArrayList<SelectItem> getSalariesList() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		salariesList = new ArrayList<SelectItem>();

		if (idEntrepriseSelected > 0) {
			SalarieServiceImpl salarieService = new SalarieServiceImpl();

			ArrayList<SalarieBean> salarieBeanList;
			try {
				salarieBeanList = (ArrayList<SalarieBean>) salarieService
						.getSalarieBeanListByIdEntreprise(idEntrepriseSelected);

				Collections.sort(salarieBeanList);
				for (SalarieBean salarieBean : salarieBeanList) {
					if (salarieBean.isPresent()) {
						ParcoursBean actu = new ParcoursBean();
						Calendar dateActu = new GregorianCalendar();
						dateActu.setTime(new Date());
						for (ParcoursBean p : salarieBean.getParcoursBeanList()) {
							if (p.getDebutFonction().before(dateActu.getTime())) {
								if (p.getFinFonction() != null) {
									if (p.getFinFonction().after(
											dateActu.getTime())) {
										actu = p;
										break;
									}
								} else {
									actu = p;
									break;
								}
							}
						}

						SelectItem selectItemSalarie = new SelectItem();
						selectItemSalarie.setValue(salarieBean.getId());
						if (actu.getNomMetier() != null) {
							selectItemSalarie.setLabel(salarieBean.getNom()
									+ " " + salarieBean.getPrenom() + " - "
									+ actu.getNomMetier());
						} else {
							selectItemSalarie.setLabel(salarieBean.getNom()
									+ " " + salarieBean.getPrenom() + " - ");
						}
						salariesList.add(selectItemSalarie);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return salariesList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCivilite() {
		return civilite;
	}

	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getLieuNaissance() {
		return lieuNaissance;
	}

	public void setLieuNaissance(String lieuNaissance) {
		this.lieuNaissance = lieuNaissance;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Double getCreditDif() {
		return creditDif;
	}

	public void setCreditDif(Double creditDif) {
		this.creditDif = creditDif;
	}

	public Double getCreditDifTmp() throws Exception {
		EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
		EntrepriseBean e = entServ
				.getEntrepriseBeanById(this.idEntrepriseSelected);
		Integer DIFMax = e.getDIFMax();
		if (creditDif < 0) {
			this.creditDifTmp = 0.0;
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Volume d'heures DIF d\u00E9pass\u00E9. ",
					"Volume d'heures DIF d\u00E9pass\u00E9. ");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:idCredit", message);
		} else {
			if (this.creditDif <= Double.valueOf(DIFMax)) {
				this.creditDifTmp = this.creditDif;
			} else {
				this.creditDifTmp = Double.valueOf(DIFMax);
			}
		}
		return creditDifTmp;
	}

	public void setCreditDifTmp(Double creditDifTmp) {
		this.creditDifTmp = creditDifTmp;
	}

	public List<HabilitationBean> getHabilitationBeanList() throws Exception {
		for (HabilitationBean h : habilitationBeanList) {
			if (h.getJustificatif() != null
					&& !h.getJustificatif()
							.contains(
									Utils.getSessionFileUploadPath(session, id,
											"habilitation", 0, false, false,
											nomGroupe))) {
				h.setJustificatif(Utils.getSessionFileUploadPath(session, id,
						"habilitation", 0, false, false, nomGroupe)
						+ h.getJustificatif());
			}
		}
		Collections.sort(habilitationBeanList);
		Collections.reverse(habilitationBeanList);
		return habilitationBeanList;
	}

	public void setHabilitationBeanList(
			List<HabilitationBean> habilitationBeanList) {
		this.habilitationBeanList = habilitationBeanList;
	}

	public List<AbsenceBean> getAbsenceBeanList() throws Exception {

		for (AbsenceBean a : absenceBeanList) {
			if (a.getJustificatif() != null
					&& !a.getJustificatif().contains(
							Utils.getSessionFileUploadPath(session, id,
									"absence", 0, false, false, nomGroupe))) {
				a.setJustificatif(Utils.getSessionFileUploadPath(session, id,
						"absence", 0, false, false, nomGroupe)
						+ a.getJustificatif());
			}
		}
		Collections.sort(absenceBeanList);
		Collections.reverse(absenceBeanList);
		return absenceBeanList;
	}

	public void setAbsenceBeanList(List<AbsenceBean> absenceBeanList) {
		this.absenceBeanList = absenceBeanList;
	}

	public List<AccidentBean> getAccidentBeanList() throws Exception {
		if ((debutAc == null || debutAc == -1)
				&& (finAc == null || finAc == -1)) {
			SalarieServiceImpl salarieServiceImpl = new SalarieServiceImpl();
			SalarieBean salarieBean = salarieServiceImpl
					.getSalarieBeanById(this.id);
			accidentBeanList.clear();
			accidentBeanList.addAll(salarieBean.getAccidentBeanList());
		}
		for (AccidentBean a : accidentBeanList) {
			if (a.getJustificatif() != null
					&& !a.getJustificatif().contains(
							Utils.getSessionFileUploadPath(session, id,
									"accident", 0, false, false, nomGroupe))) {
				a.setJustificatif(Utils.getSessionFileUploadPath(session, id,
						"accident", 0, false, false, nomGroupe)
						+ a.getJustificatif());
			}
		}
		Collections.sort(accidentBeanList);
		Collections.reverse(accidentBeanList);
		return accidentBeanList;
	}

	public void setAccidentBeanList(List<AccidentBean> accidentBeanList) {
		this.accidentBeanList = accidentBeanList;
	}

	public List<EntretienBean> getEntretienBeanList() throws Exception {
		for (EntretienBean e : entretienBeanList) {
			if (e.getJustificatif() != null
					&& !e.getJustificatif().contains(
							Utils.getSessionFileUploadPath(session, id,
									"entretien", 0, false, false, nomGroupe))) {
				e.setJustificatif(Utils.getSessionFileUploadPath(session, id,
						"entretien", 0, false, false, nomGroupe)
						+ e.getJustificatif());
			}
		}
		Collections.sort(entretienBeanList);
		Collections.reverse(entretienBeanList);
		return entretienBeanList;
	}

	public void setEntretienBeanList(List<EntretienBean> entretienBeanList) {
		this.entretienBeanList = entretienBeanList;
	}

	public List<FormationBean> getFormationBeanList() throws Exception {
		if ((debutFo == null || debutFo == -1)
				&& (finFo == null || finFo == -1)) {
			SalarieServiceImpl salarieServiceImpl = new SalarieServiceImpl();
			SalarieBean salarieBean = salarieServiceImpl
					.getSalarieBeanById(this.id);
			formationBeanList.clear();
			formationBeanList.addAll(salarieBean.getFormationBeanList());
		}
		for (FormationBean f : formationBeanList) {
			if (f.getJustificatif() != null
					&& !f.getJustificatif().contains(
							Utils.getSessionFileUploadPath(session, id,
									"formation", 0, false, false, nomGroupe))) {
				f.setJustificatif(Utils.getSessionFileUploadPath(session, id,
						"formation", 0, false, false, nomGroupe)
						+ f.getJustificatif());
			}
		}
		Collections.sort(formationBeanList);
		Collections.reverse(formationBeanList);
		return formationBeanList;
	}

	public void setFormationBeanList(List<FormationBean> formationBeanList) {
		this.formationBeanList = formationBeanList;
	}

	public List<ParcoursBean> getParcoursBeanList() throws Exception {
		for (ParcoursBean p : parcoursBeanList) {
			if (p.getJustificatif() != null
					&& !p.getJustificatif().contains(
							Utils.getSessionFileUploadPath(session, id,
									"parcours", 0, false, false, nomGroupe))) {
				p.setJustificatif(Utils.getSessionFileUploadPath(session, id,
						"parcours", 0, false, false, nomGroupe)
						+ p.getJustificatif());
			}
		}
		Collections.sort(parcoursBeanList);
		Collections.reverse(parcoursBeanList);
		for (ParcoursBean p : parcoursBeanList) {
			if (p.getIdTypeContratSelected() == 3) {
				p.setNomTypeContrat("CTT");
			}
		}
		return parcoursBeanList;
	}

	public void setParcoursBeanList(List<ParcoursBean> parcoursBeanList) {
		this.parcoursBeanList = parcoursBeanList;

	}

	public Integer getIdLienSubordination() {
		return idLienSubordination;
	}

	public void setIdLienSubordination(Integer idLienSubordination) {
		this.idLienSubordination = idLienSubordination;
	}

	public String getNivFormationInit() {
		return nivFormationInit;
	}

	public void setNivFormationInit(String nivFormationInit) {
		this.nivFormationInit = nivFormationInit;
	}

	public String getNivFormationAtteint() {

		return nivFormationAtteint;
	}

	public void setNivFormationAtteint(String nivFormationAtteint) {
		this.nivFormationAtteint = nivFormationAtteint;
	}

	public String getCv() {
		return cv;
	}

	public void setCv(String cv) {
		this.cv = cv;
	}

	public Integer getIdEntrepriseSelected() {
		return idEntrepriseSelected;
	}

	public void setIdEntrepriseSelected(Integer idEntrepriseSelected) {
		this.idEntrepriseSelected = idEntrepriseSelected;
	}

	public String initSalarieForm() {
		this.newSalarieTab = false;
		SalarieServiceImpl salarieServiceImpl = new SalarieServiceImpl();

		try {
			SalarieBean salarieBean = salarieServiceImpl
					.getSalarieBeanById(this.id);

			this.adresse = salarieBean.getAdresse();
			if (salarieBean.getCivilite().equals("Mademoiselle")) {
				this.civilite = "Madame";
			} else {
				this.civilite = salarieBean.getCivilite();
			}
			this.dateNaissance = salarieBean.getDateNaissance();
			this.habilitationBeanList = salarieBean.getHabilitationBeanList();
			Collections.sort(this.habilitationBeanList);
			Collections.reverse(this.habilitationBeanList);

			List<RemunerationBean> l = new ArrayList<RemunerationBean>();
			RemunerationServiceImpl servRemu = new RemunerationServiceImpl();
			for (RemunerationBean r : servRemu.getRemunerationList(Integer
					.parseInt(session.getAttribute("groupe").toString()))) {
				if (r.getIdSalarie() == this.id) {
					l.add(r);
				}
			}

			MetierServiceImpl servMetier = new MetierServiceImpl();
			StatutServiceImpl servStatut = new StatutServiceImpl();
			for (RemunerationBean rb : l) {
				rb.setNomMetier(servMetier.getMetierBeanById(rb.getIdMetier())
						.getNom());
				rb.setNomCsp(servStatut.getStatutBeanById(rb.getIdStatut())
						.getNom());
			}
			this.remunerationBeanList = l;

			List<AbsenceBean> list = new ArrayList<AbsenceBean>();
			list = salarieBean.getAbsenceBeanList();
			this.absenceBeanList = list;
			Collections.sort(this.absenceBeanList);
			Collections.reverse(this.absenceBeanList);

			this.accidentBeanList = salarieBean.getAccidentBeanList();
			Collections.sort(this.accidentBeanList);
			Collections.reverse(this.accidentBeanList);

			this.entretienBeanList = salarieBean.getEntretienBeanList();
			Collections.sort(this.entretienBeanList);
			Collections.reverse(this.entretienBeanList);

			this.formationBeanList = salarieBean.getFormationBeanList();
			Collections.sort(this.formationBeanList);
			Collections.reverse(this.formationBeanList);

			PersonneAChargeServiceImpl pac = new PersonneAChargeServiceImpl();
			this.personneAChargeBeanList = pac
					.getPersonneAChargeBeanListByIdSalarie(salarieBean.getId());

			PiecesJustificativesServiceImpl pj = new PiecesJustificativesServiceImpl();
			if (pj.getPiecesJustificativesBeanListByIdSalarie(
					salarieBean.getId()).size() > 0) {
				pjn = 0;
				this.piecesJustificativesBean = pj
						.getPiecesJustificativesBeanListByIdSalarie(
								salarieBean.getId()).get(0);
				this.attestationVisiteMedicale = this.piecesJustificativesBean
						.isAttestaionVisiteMedicale();
				this.attestationSecu = this.piecesJustificativesBean
						.isAttestationSecu();
				this.carteIdentite = this.piecesJustificativesBean
						.isCarteIdentite();
				this.certificatTravail = this.piecesJustificativesBean
						.isCertificatTravail();
				this.diplomes = this.piecesJustificativesBean.isDiplomes();
				this.habilitations = this.piecesJustificativesBean
						.isHabilitations();
				this.permisConduire = this.piecesJustificativesBean
						.isPermisConduire();
				this.rib = this.piecesJustificativesBean.isRib();
			} else {
				pjn = null;
				this.piecesJustificativesBean = null;
				this.attestationVisiteMedicale = false;
				this.attestationSecu = false;
				this.carteIdentite = false;
				this.certificatTravail = false;
				this.diplomes = false;
				this.habilitations = false;
				this.permisConduire = false;
				this.rib = false;
			}

			this.telephonePortable = salarieBean.getTelephonePortable();
			this.situationFamiliale = salarieBean.getSituationFamiliale();
			this.personneAPrevenirNom = salarieBean.getPersonneAPrevenirNom();
			this.personneAPrevenirPrenom = salarieBean
					.getPersonneAPrevenirPrenom();
			this.personneAPrevenirAdresse = salarieBean
					.getPersonneAPrevenirAdresse();
			this.personneAPrevenirTelephone = salarieBean
					.getPersonneAPrevenirTelephone();
			this.personneAPrevenirLienParente = salarieBean
					.getPersonneAPrevenirLienParente();
			this.impression = salarieBean.isImpression();
			this.commentaire = salarieBean.getCommentaire();
			this.possedePermisConduire = salarieBean.isPossedePermisConduire();

			List<ParcoursBean> listp = new ArrayList<ParcoursBean>();
			listp = salarieBean.getParcoursBeanList();
			this.parcoursBeanList = listp;
			Collections.sort(this.parcoursBeanList);
			Collections.reverse(this.parcoursBeanList);

			this.id = salarieBean.getId();
			this.lieuNaissance = salarieBean.getLieuNaissance();
			this.nom = salarieBean.getNom();
			this.prenom = salarieBean.getPrenom();
			this.telephone = salarieBean.getTelephone();
			this.present = salarieBean.isPresent();
			this.newSalarieTab = (!this.present) ? true : false;

			this.creditDif = salarieBean.getCreditDif();

			this.idLienSubordination = salarieBean.getIdLienSubordination();

			this.nivFormationAtteint = salarieBean.getNivFormationAtteint();
			this.nivFormationInit = salarieBean.getNivFormationInit();

			this.idEntrepriseSelected = salarieBean.getIdEntrepriseSelected();

			this.idServiceSelected = salarieBean.getIdServiceSelected();

			this.dateLastSaisieDif = salarieBean.getDateLastSaisieDif();
			this.creditDifAnnuel = null;
			this.creditDifAnnuelTmp = null;

			this.cv = salarieBean.getCv();
			this.fileProgress = 0;
			this.fileList.clear();

			debutPo = null;
			finPo = null;
			debutRe = null;
			finRe = null;
			debutHa = null;
			finHa = null;
			debutFo = null;
			finFo = null;
			debutAb = null;
			finAb = null;
			debutAc = null;
			finAc = null;
			debutEn = null;
			finEn = null;
			debutEv = null;
			finEv = null;

			this.debutExtraction = null;
			this.finExtraction = null;

			EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
			this.entreprise = serv.getEntrepriseBeanById(
					salarieBean.getIdEntrepriseSelected()).getNom();

			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);

			if ((salarieBean.getCv() != null)
					&& (!salarieBean.getCv().equals(""))) {

				if (!new File(Utils.getSessionFileUploadPath(session, this.id,
						null, 0, false, false, nomGroupe) + this.cv).exists()
						&& new File(Utils.getSessionFileUploadPath(session, 0,
								null, 0, false, false, nomGroupe) + this.cv)
								.exists()) {
					if (!new File(Utils.getSessionFileUploadPath(session,
							this.id, null, 0, false, false, nomGroupe))
							.exists()) {
						new File(Utils.getSessionFileUploadPath(session,
								this.id, null, 0, false, false, nomGroupe))
								.mkdirs();
					}
					Utils.copyFile(
							Utils.getSessionFileUploadPath(session, 0, null, 0,
									false, false, nomGroupe) + this.cv,
							Utils.getSessionFileUploadPath(session, this.id,
									null, 0, false, false, nomGroupe) + this.cv);
				}

				if (new File(Utils.getSessionFileUploadPath(session, this.id,
						null, 0, false, false, nomGroupe) + this.cv).exists()) {
					FileInfo fileInfo = new FileInfo();
					fileInfo.setFileName(new File(Utils
							.getSessionFileUploadPath(session, this.id, null,
									0, false, false, nomGroupe)
							+ this.cv).getName());
					fileInfo.setFile(new File(Utils.getSessionFileUploadPath(
							session, this.id, null, 0, false, false, nomGroupe)
							+ this.cv));

					currentFile = new InputFileData(fileInfo, getUrl(null));

					fileList.add(currentFile);
				}
			}

			session.setAttribute("idSalarie", id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		activateTestSaveBeforeExit();
		return "addSalarie";
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

	public void datesFiltre(ValueChangeEvent evt) {
		if (evt.getComponent().getId().equals("anneeDEv")) {
			this.debutEv = (Integer) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("anneeFEv")) {
			this.finEv = (Integer) evt.getNewValue();
		}

		if (evt.getComponent().getId().equals("anneeDRe")) {
			this.debutRe = (Integer) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("anneeFRe")) {
			this.finRe = (Integer) evt.getNewValue();
		}

		if (evt.getComponent().getId().equals("anneeDPo")) {
			this.debutPo = (Integer) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("anneeFPo")) {
			this.finPo = (Integer) evt.getNewValue();
		}

		if (evt.getComponent().getId().equals("anneeDCt")) {
			this.debutCt = (Integer) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("anneeFCt")) {
			this.finCt = (Integer) evt.getNewValue();
		}

		if (evt.getComponent().getId().equals("anneeDHa")) {
			this.debutHa = (Integer) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("anneeFHa")) {
			this.finHa = (Integer) evt.getNewValue();
		}

		if (evt.getComponent().getId().equals("anneeDFo")) {
			this.debutFo = (Integer) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("anneeFFo")) {
			this.finFo = (Integer) evt.getNewValue();
		}

		if (evt.getComponent().getId().equals("anneeDAb")) {
			this.debutAb = (Integer) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("anneeFAb")) {
			this.finAb = (Integer) evt.getNewValue();
		}

		if (evt.getComponent().getId().equals("anneeDAc")) {
			this.debutAc = (Integer) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("anneeFAc")) {
			this.finAc = (Integer) evt.getNewValue();
		}

		if (evt.getComponent().getId().equals("anneeDEn")) {
			this.debutEn = (Integer) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("anneeFEn")) {
			this.finEn = (Integer) evt.getNewValue();
		}

	}

	public void filtrerPO() throws Exception {
		List<ParcoursBean> l = new ArrayList<ParcoursBean>();
		SalarieServiceImpl sal = new SalarieServiceImpl();
		for (ParcoursBean p : sal.getSalarieBeanById(this.id)
				.getParcoursBeanList()) {
			GregorianCalendar debutP = new GregorianCalendar();
			debutP.setTime(p.getDebutFonction());
			GregorianCalendar finP = new GregorianCalendar();
			if (p.getFinFonction() != null) {
				finP.setTime(p.getFinFonction());
			} else {
				finP = null;
			}
			if (debutPo != null && debutPo == -1) {
				debutPo = null;
			}
			if (finPo != null && finPo == -1) {
				finPo = null;
			}
			if (isInPeriode(debutPo, finPo, debutP, finP)) {
				l.add(p);
			}
		}
		this.parcoursBeanList.clear();
		this.parcoursBeanList = l;
	}

	public void filtrerRE() throws Exception {
		List<RemunerationBean> l = new ArrayList<RemunerationBean>();
		RemunerationServiceImpl remu = new RemunerationServiceImpl();
		for (RemunerationBean r : remu.getRemunerationByIdSalarie(this.id)) {
			if (debutRe != null && debutRe == -1) {
				debutRe = null;
			}
			if (finRe != null && finRe == -1) {
				finRe = null;
			}
			if ((debutRe == null && finRe == null)
					|| (debutRe == null && finRe != null && r.getAnnee() <= finRe)
					|| (debutRe != null && finRe == null && debutRe <= r
							.getAnnee())
					|| (debutRe != null && finRe != null
							&& debutRe <= r.getAnnee() && finRe >= r.getAnnee())) {
				l.add(r);
			}
		}
		MetierServiceImpl servMetier = new MetierServiceImpl();
		StatutServiceImpl servStatut = new StatutServiceImpl();
		for (RemunerationBean rb : l) {
			rb.setNomMetier(servMetier.getMetierBeanById(rb.getIdMetier())
					.getNom());
			rb.setNomCsp(servStatut.getStatutBeanById(rb.getIdStatut())
					.getNom());
		}
		this.remunerationBeanList.clear();
		this.remunerationBeanList = l;
	}

	public void filtrerHA() throws Exception {
		List<HabilitationBean> l = new ArrayList<HabilitationBean>();
		SalarieServiceImpl sal = new SalarieServiceImpl();
		for (HabilitationBean h : sal.getSalarieBeanById(this.id)
				.getHabilitationBeanList()) {
			GregorianCalendar debutP = new GregorianCalendar();
			debutP.setTime(h.getDelivrance());
			GregorianCalendar finP = new GregorianCalendar();
			if (h.getExpiration() != null) {
				finP.setTime(h.getExpiration());
			} else {
				finP = null;
			}
			if (debutHa != null && debutHa == -1) {
				debutHa = null;
			}
			if (finHa != null && finHa == -1) {
				finHa = null;
			}
			if (isInPeriode(debutHa, finHa, debutP, finP)) {
				l.add(h);
			}
		}
		this.habilitationBeanList.clear();
		this.habilitationBeanList = l;
	}

	public void filtrerFO() throws Exception {
		List<FormationBean> l = new ArrayList<FormationBean>();
		SalarieServiceImpl sal = new SalarieServiceImpl();
		for (FormationBean f : sal.getSalarieBeanById(this.id)
				.getFormationBeanList()) {
			GregorianCalendar debutP = new GregorianCalendar();
			debutP.setTime(f.getDebutFormation());
			GregorianCalendar finP = new GregorianCalendar();
			if (f.getFinFormation() != null) {
				finP.setTime(f.getFinFormation());
			} else {
				finP = null;
			}
			if (debutFo != null && debutFo == -1) {
				debutFo = null;
			}
			if (finFo != null && finFo == -1) {
				finFo = null;
			}
			if (isInPeriode(debutFo, finFo, debutP, finP)) {
				l.add(f);
			}
		}
		this.formationBeanList.clear();
		this.formationBeanList = l;
	}

	public void filtrerAB() throws Exception {
		List<AbsenceBean> l = new ArrayList<AbsenceBean>();
		SalarieServiceImpl sal = new SalarieServiceImpl();
		for (AbsenceBean a : sal.getSalarieBeanById(this.id)
				.getAbsenceBeanList()) {
			GregorianCalendar debutP = new GregorianCalendar();
			debutP.setTime(a.getDebutAbsence());
			GregorianCalendar finP = new GregorianCalendar();
			if (a.getFinAbsence() != null) {
				finP.setTime(a.getFinAbsence());
			} else {
				finP = null;
			}
			if (debutAb != null && debutAb == -1) {
				debutAb = null;
			}
			if (finAb != null && finAb == -1) {
				finAb = null;
			}
			if (isInPeriode(debutAb, finAb, debutP, finP)) {
				l.add(a);
			}
		}
		this.absenceBeanListRefresh.clear();
		this.absenceBeanListRefresh = l;
	}

	public void filtrerAC() throws Exception {
		List<AccidentBean> l = new ArrayList<AccidentBean>();
		SalarieServiceImpl sal = new SalarieServiceImpl();
		for (AccidentBean a : sal.getSalarieBeanById(this.id)
				.getAccidentBeanList()) {
			GregorianCalendar debutP = new GregorianCalendar();
			debutP.setTime(a.getDateAccident());
			if (debutAc != null && debutAc == -1) {
				debutAc = null;
			}
			if (finAc != null && finAc == -1) {
				finAc = null;
			}
			if (isInPeriode(debutAc, finAc, debutP, null)) {
				l.add(a);
			}
		}
		this.accidentBeanList.clear();
		this.accidentBeanList = l;
	}

	public void filtrerEN() throws Exception {
		List<EntretienBean> l = new ArrayList<EntretienBean>();
		SalarieServiceImpl sal = new SalarieServiceImpl();
		for (EntretienBean e : sal.getSalarieBeanById(this.id)
				.getEntretienBeanList()) {
			GregorianCalendar debutP = new GregorianCalendar();
			debutP.setTime(e.getDateEntretien());
			if (debutEn != null && debutEn == -1) {
				debutEn = null;
			}
			if (finEn != null && finEn == -1) {
				finEn = null;
			}
			if (isInPeriode(debutEn, finEn, debutP, null)) {
				l.add(e);
			}
		}
		this.entretienBeanList.clear();
		this.entretienBeanList = l;
	}

	public void filtrerEV() throws Exception {
		List<EvenementBean> l = new ArrayList<EvenementBean>();
		EvenementServiceImpl ev = new EvenementServiceImpl();
		for (EvenementBean e : ev.getEvenementBeanListByIdSalarie(this.id)) {
			GregorianCalendar debutP = new GregorianCalendar();
			debutP.setTime(e.getDateEvenement());
			if (debutEv != null && debutEv == -1) {
				debutEv = null;
			}
			if (finEv != null && finEv == -1) {
				finEv = null;
			}
			if (debutEv != null && finEv != null) {
				if (finEv < debutEv) {
					Integer finEvTemp = finEv;
					finEv = debutEv;
					debutEv = finEvTemp;
				}
			}
			if (isInPeriode(debutEv, finEv, debutP, null)) {
				l.add(e);
			}
		}
		this.evenementBeanList.clear();
		this.evenementBeanList = l;
	}

	public void filtrerCT() throws Exception {
		List<ContratTravailBean> l = new ArrayList<ContratTravailBean>();
		ContratTravailServiceImpl s = new ContratTravailServiceImpl();
		for (ContratTravailBean p : s
				.getContratTravailBeanListByIdSalarie(this.id)) {
			GregorianCalendar debutP = new GregorianCalendar();
			debutP.setTime(p.getDebutContrat());
			GregorianCalendar finP = new GregorianCalendar();
			if (p.getFinContrat() != null) {
				finP.setTime(p.getFinContrat());
			} else {
				finP = null;
			}
			if (debutCt != null && debutCt == -1) {
				debutCt = null;
			}
			if (finCt != null && finCt == -1) {
				finCt = null;
			}
			if (debutCt != null && finCt != null) {
				if (finCt < debutCt) {
					Integer finCtTemp = finCt;
					finCt = debutCt;
					debutCt = finCtTemp;
				}
			}
			if (isInPeriode(debutCt, finCt, debutP, finP)) {
				l.add(p);
			}
		}
		this.contratTravailBeanList.clear();
		this.contratTravailBeanList = l;
	}

	public String retour() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		if (session.getAttribute("retour") != null) {
			session.setAttribute("retour", null);
		}

		desactivateTestSaveBeforeExit();
		return "salaries";
	}

	public void annuler() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		if (session.getAttribute("retour") != null) {
			session.setAttribute("retour", null);
		}

		desactivateTestSaveBeforeExit();
		initSalarieForm();
	}

	public InputFileData getCurrentFile() {
		return currentFile;
	}

	public List getFileList() {
		return fileList;
	}

	public int getFileProgress() {
		return fileProgress;
	}

	/**
	 * <p>
	 * Action event method which is triggered when a user clicks on the upload
	 * file button. Uploaded files are added to a list so that user have the
	 * option to delete them programatically. Any errors that occurs during the
	 * file uploaded are added the messages output.
	 * </p>
	 * 
	 * @param event
	 *            jsf action event.
	 */
	public void uploadFile(ActionEvent event) throws Exception {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		InputFile inputFile = (InputFile) event.getSource();

		FileInfo fileInfo = inputFile.getFileInfo();

		if (fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.",
					"Le fichier est trop gros. Sa taille ne doit pas dépasser 1Mo.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:inputFileNameCV", message);
			return;
		}

		if (fileInfo.getStatus() == FileInfo.SAVED) {

			currentFile = new InputFileData(fileInfo, getUrl(null));
			this.cv = currentFile.getFile().getName();
			synchronized (fileList) {
				fileList.add(currentFile);
				this.fileProgress = 0;

			}

		}

	}

	public void download(ActionEvent evt) throws Exception {

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier",
				this.fileList.get(0).getFile().getAbsolutePath());

		JavascriptContext.addJavascriptCall(
				FacesContext.getCurrentInstance(),
				"window.open(\"servlet.download?contentType="
						+ Files.probeContentType(new File(this.fileList.get(0)
								.getFile().getName()).toPath())
						+ " \",\"_Download\");");

	}

	/**
	 * <p>
	 * This method is bound to the inputFile component and is executed multiple
	 * times during the file upload process. Every call allows the user to finds
	 * out what percentage of the file has been uploaded. This progress
	 * information can then be used with a progressBar component for user
	 * feedback on the file upload progress.
	 * </p>
	 * 
	 * @param event
	 *            holds a InputFile object in its source which can be probed for
	 *            the file upload percentage complete.
	 */
	public void fileUploadProgress(EventObject event) {

		InputFile ifile = (InputFile) event.getSource();

		fileProgress = ifile.getFileInfo().getPercent();
	}

	/**
	 * <p>
	 * Allows a user to remove a file from a list of uploaded files. This
	 * methods assumes that a request param "fileName" has been set to a valid
	 * file name that the user wishes to remove or delete
	 * </p>
	 * 
	 * @param event
	 *            jsf action event
	 */
	public void removeUploadedFile(ActionEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String fileName = (String) map.get("fileName");
		if (new File(fileList.get(0).getPath()).exists()) {
			new File(fileList.get(0).getPath()).delete();
		}
		this.cv = "";
		fileList.clear();
	}

	public void supprRemuneration() throws Exception {

		if (this.idRem != 0) {

			RemunerationBean rb = new RemunerationBean();
			rb.setIdRemuneration(this.idRem);
			LienRemunerationRevenuServiceImpl servlrr = new LienRemunerationRevenuServiceImpl();
			for (LienRemunerationRevenuBean lrr : servlrr
					.getLienRemunerationRevenuFromIdRemu(this.idRem)) {
				servlrr.supprimer(lrr);
			}
			RemunerationServiceImpl serv = new RemunerationServiceImpl();
			serv.deleteRemuneration(rb);

			this.remunerationBeanList.clear();

			List<RemunerationBean> l = new ArrayList<RemunerationBean>();
			RemunerationServiceImpl servRemu = new RemunerationServiceImpl();
			for (RemunerationBean r : servRemu.getRemunerationList(Integer
					.parseInt(session.getAttribute("groupe").toString()))) {
				if (r.getIdSalarie() == this.id) {
					l.add(r);
				}
			}

			MetierServiceImpl servMetier = new MetierServiceImpl();
			StatutServiceImpl servStatut = new StatutServiceImpl();
			for (RemunerationBean r : l) {
				r.setNomMetier(servMetier.getMetierBeanById(r.getIdMetier())
						.getNom());
				r.setNomCsp(servStatut.getStatutBeanById(r.getIdStatut())
						.getNom());
			}
			this.remunerationBeanList = l;
			idRem = 0;
		}

	}

	public boolean isModalRenderedTest() {
		return modalRenderedTest;
	}

	public void setModalRenderedTest(boolean modalRenderedTest) {
		this.modalRenderedTest = modalRenderedTest;
	}

	public void toggleModalTest(ActionEvent event) {
		modalRenderedTest = !modalRenderedTest;
	}

	public boolean isModalFormationAide() {
		return modalFormationAide;
	}

	public void setModalFormationAide(boolean modalFormationAide) {
		this.modalFormationAide = modalFormationAide;
	}

	public void toggleModalFormationAide(ActionEvent event) {
		modalFormationAide = !modalFormationAide;
	}

	/**
	 * 
	 * @param event
	 *            value change event
	 */
	public void entrepriseChanged(ValueChangeEvent event) {
		if (event.getComponent().getId().equals("idEntrepriseList")) {
			idEntrepriseSelected = Integer.parseInt(event.getNewValue()
					.toString());
			idServiceSelected = -1;

		}

	}

	public void niveauInitChanged(ValueChangeEvent event) {

		ArrayList<SelectItem> cityItems = getNivFormationAtteintList();

		for (SelectItem selectItem : cityItems) {
			if (selectItem.getValue().equals(event.getNewValue())) {
				nivFormationAtteint = selectItem.getValue().toString();
			}
		}
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),
				"location.reload(true);");
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	public Integer getIdServiceSelected() {
		return idServiceSelected;
	}

	public void setIdServiceSelected(Integer idServiceSelected) {
		this.idServiceSelected = idServiceSelected;
	}

	public String getLibelleService() {

		ServiceImpl serviceImpl = new ServiceImpl();
		try {
			ServiceBean serviceBean = serviceImpl
					.getServiceBeanById(this.idServiceSelected);
			this.libelleService = serviceBean.getNom();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return libelleService;
	}

	public void setLibelleService(String libelleService) {
		this.libelleService = libelleService;
	}

	public void printFicheIndivSalarie(ActionEvent e) throws Exception {

		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.downloadReport.rep?contentType=pdf \",\"_Reports\");");

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		eContext.getSessionMap().put("idSalarie", this.id);
		eContext.getSessionMap().put("debutExtractionCV", this.debutExtraction);
		eContext.getSessionMap().put("finExtractionCV", this.finExtraction);
		eContext.getSessionMap().put("idEntreprise", this.idEntrepriseSelected);
		eContext.getSessionMap().put("session", session);
		eContext.getSessionMap().put("nomGroupe", this.getNomGroupe());

	}

	public void effectChangeListener(ValueChangeEvent event) {

		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	public void processTabChange(TabChangeEvent tabChangeEvent)
			throws Exception {
	}

	public boolean isModalLoginRendered() {
		return modalLoginRendered;
	}

	public void setModalLoginRendered(boolean modalLoginRendered) {
		this.modalLoginRendered = modalLoginRendered;
	}

	public void toggleModalLoginRendered(ActionEvent event) {
		modalLoginRendered = !modalLoginRendered;
	}

	public void toggleModalLoginRenderedRemu(ActionEvent event) {
		modalLoginRenderedRemu = !modalLoginRenderedRemu;
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

	public void log(ActionEvent event) {
		ResourceBundle bundle = ResourceBundle.getBundle("CCI");

		String mdp = bundle.getString("cci");

		if (mdp.equals(this.password)) {
			modalLoginRendered = false;
			loggedRendered = true;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);
			session.setAttribute("identifie", true);
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Mot de passe non valide",
					"Mot de passe non valide");
			FacesContext.getCurrentInstance().addMessage("idSalarieForm:TxtPw",
					message);
		}
	}

	public void logRemu(ActionEvent event) {
		ResourceBundle bundle = ResourceBundle.getBundle("CCI");

		String mdp = bundle.getString("cci2");

		if (mdp.equals(this.passwordRemu)) {
			modalLoginRenderedRemu = false;
			loggedRendered = true;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);
			session.setAttribute("identifie", true);
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Mot de passe non valide",
					"Mot de passe non valide");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:TxtPwRemu", message);
		}
	}

	public ArrayList<SelectItem> getNivFormationInitList() {
		return nivFormationInitList;
	}

	public ArrayList<SelectItem> getNivFormationAtteintList() {
		return nivFormationAtteintList;
	}

	public Date getDateLastSaisieDif() {
		return dateLastSaisieDif;
	}

	public void setDateLastSaisieDif(Date dateLastSaisieDif) {
		this.dateLastSaisieDif = dateLastSaisieDif;
	}

	public Double getCreditDifAnnuel() {
		return creditDifAnnuel;
	}

	public void setCreditDifAnnuel(Double creditDifAnnuel) {
		this.creditDifAnnuel = creditDifAnnuel;
	}

	public void addDIF() throws Exception {
		if (creditDifAnnuel != null) {
			EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
			Double difMax = Double.valueOf(entServ
					.getEntrepriseBeanById(this.idEntrepriseSelected)
					.getDIFMax().toString());
			if (this.creditDif + this.creditDifAnnuel <= difMax) {
				this.creditDif = this.creditDif + this.creditDifAnnuel;
				this.dateLastSaisieDif = new Date();
				this.creditDifAnnuelTmp = this.creditDifAnnuel;
				this.creditDifAnnuel = null;
				saveDIF();
			} else {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Cumul heures de DIF sup\u00E9rieur au plafond ("
								+ difMax.intValue() + " heures)",
						"Cumul heures de DIF sup\u00E9rieur au plafond ("
								+ difMax.intValue() + " heures)");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:idCreditAnnuel",
						message);
			}

		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Vous devez saisir une valeur pour le cr\u00E9dit DIF annuel. ",
					"Vous devez saisir une valeur pour le cr\u00E9dit DIF annuel. ");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:idCreditAnnuel", message);
		}
	}

	public void saveDIF() throws Exception {
		saveOrUpdateSalarie();
	}

	public void undoLastDIF() {
		if (this.creditDifAnnuelTmp != null) {
			this.creditDif = this.creditDif - this.creditDifAnnuelTmp;
			this.creditDifAnnuelTmp = null;
		}
	}

	/**
	 * Active le test d'affichage de la pop up d'information de sauvegarde de la
	 * fiche salarié
	 */
	public void activateTestSaveBeforeExit() {

		NavigationBB navigationBB = (NavigationBB) FacesContext
				.getCurrentInstance().getCurrentInstance().getExternalContext()
				.getSessionMap().get("navigationBB");

	}

	/**
	 * Désactive le test d'affichage de la pop up d'information de sauvegarde de
	 * la fiche salarié
	 */
	public void desactivateTestSaveBeforeExit() {

		NavigationBB navigationBB = (NavigationBB) FacesContext
				.getCurrentInstance().getCurrentInstance().getExternalContext()
				.getSessionMap().get("navigationBB");

		navigationBB.setExitWithoutSaveSalarie(false);
	}

	public boolean isPresent() {
		return present;
	}

	public void setPresent(boolean present) {
		this.present = present;
	}

	public void changePresent(ValueChangeEvent event) {
		this.present = (Boolean) event.getNewValue();
	}

	public void checkPresenceDebutAbsence(ValueChangeEvent event)
			throws Exception {
		checkPeriode((Date) event.getNewValue(), "dateDebutAbsence");
	}

	public void checkPresenceEvenement(ValueChangeEvent event) throws Exception {
		checkPeriode((Date) event.getNewValue(), "dateEvenement");
	}

	public void checkPresenceFinAbsence(ValueChangeEvent event)
			throws Exception {
		checkPeriode((Date) event.getNewValue(), "dateFinAbsence");
	}

	public void checkPresenceDebutAccident(ValueChangeEvent event)
			throws Exception {
		checkPeriode((Date) event.getNewValue(), "dateAccident");
	}

	public void checkPresenceDebutEntretien(ValueChangeEvent event)
			throws Exception {
		checkPeriode((Date) event.getNewValue(), "dateEntretien");
	}

	public void checkPresenceDebutFormation(ValueChangeEvent event)
			throws Exception {
		checkPeriode((Date) event.getNewValue(), "dateDebutFormation");
	}

	public void checkPresenceFinFormation(ValueChangeEvent event)
			throws Exception {
		checkPeriode((Date) event.getNewValue(), "dateFinFormation");
	}

	public void checkPresenceDebutHabilitation(ValueChangeEvent event)
			throws Exception {
		checkPeriode((Date) event.getNewValue(), "dateDelivranceHabilitation");
	}

	public void checkPresenceFinHabilitation(ValueChangeEvent event)
			throws Exception {
		checkPeriode((Date) event.getNewValue(), "dateExpirationHabilitation");
	}

	public void checkPeriode(Date dateDebut, String id) throws Exception {
		List<ParcoursBean> listParcoursSalarie = getParcoursBeanList();
		boolean horsEntreprise = true;
		for (ParcoursBean parcoursBean : listParcoursSalarie) {
			if (parcoursBean.getFinFonction() != null) {

				GregorianCalendar debD1 = new GregorianCalendar();
				debD1.setTime(parcoursBean.getDebutFonction());
				GregorianCalendar finD1 = new GregorianCalendar();
				finD1.setTime(parcoursBean.getFinFonction());
				GregorianCalendar debD2 = new GregorianCalendar();
				debD2.setTime(dateDebut);
				GregorianCalendar finD2 = new GregorianCalendar();
				finD2.setTime(dateDebut);
				if (isInEntrepriseWithBorne(debD1, finD1, debD2, finD2)) {
					horsEntreprise = false;
				}
			} else {
				GregorianCalendar debD1 = new GregorianCalendar();
				debD1.setTime(parcoursBean.getDebutFonction());
				GregorianCalendar debD2 = new GregorianCalendar();
				debD2.setTime(dateDebut);
				GregorianCalendar finD2 = new GregorianCalendar();
				finD2.setTime(dateDebut);
				if (isInEntreprise(debD1, debD2, finD2)) {
					horsEntreprise = false;
				}
			}
		}

		if (horsEntreprise) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Aucune pr\u00E9sence \u00e0 cette date.",
					"Aucune pr\u00E9sence \u00e0 cette date.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:" + id, message);
			return;
		}
	}

	private boolean isInEntrepriseWithBorne(GregorianCalendar debutC,
			GregorianCalendar finC, GregorianCalendar debutDate,
			GregorianCalendar finDate) {

		if (before(debutDate, debutC)) {
			return false;
		}
		if (before(finDate, debutC)) {
			return false;
		}
		if (after(finDate, finC)) {
			return false;
		}
		if (after(debutDate, finC)) {
			return false;
		}

		return true;
	}

	private boolean isInEntreprise(GregorianCalendar debutC,
			GregorianCalendar debutDate, GregorianCalendar finDate) {

		if (before(debutDate, debutC)) {
			return false;
		}
		if (before(finDate, debutC)) {
			return false;
		}

		return true;
	}

	private boolean after(GregorianCalendar d1, GregorianCalendar d2) {
		// On teste si les dates sont egales, car GregorianCalendar ne prend pas
		// en comtpe ce cas, sinon on renvoi
		// true si d1 est avant d2
		if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
			if (d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)) {
				if (d1.get(Calendar.DATE) == d2.get(Calendar.DATE)) {
					return false;
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
					return false;
				}
			}
		}
		return (d1.before(d2));
	}

	public boolean isNewSalarieTab() {
		return newSalarieTab;
	}

	public void setNewSalarieTab(boolean newSalarieTab) {
		this.newSalarieTab = newSalarieTab;
	}

	public boolean isModalTransmissionAide() {
		return modalTransmissionAide;
	}

	public void setModalTransmissionAide(boolean modalTransmissionAide) {
		this.modalTransmissionAide = modalTransmissionAide;
	}

	public void toggleModalTransmissionAide(ActionEvent event) {
		modalTransmissionAide = !modalTransmissionAide;
	}

	public Double getCreditDifAnnuelTmp() {
		return creditDifAnnuelTmp;
	}

	public void setCreditDifAnnuelTmp(Double creditDifAnnuelTmp) {
		this.creditDifAnnuelTmp = creditDifAnnuelTmp;
	}

	public List<RemunerationBean> getRemunerationBeanList() throws Exception {
		Collections.sort(remunerationBeanList);
		Collections.reverse(remunerationBeanList);

		return remunerationBeanList;
	}

	public void setRemunerationBeanList(List<RemunerationBean> remunerationBean) {
		this.remunerationBeanList = remunerationBean;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEntreprisesList(ArrayList<SelectItem> entreprisesList) {
		this.entreprisesList = entreprisesList;
	}

	public void setCurrentFile(InputFileData currentFile) {
		this.currentFile = currentFile;
	}

	public void setFileProgress(int fileProgress) {
		this.fileProgress = fileProgress;
	}

	public void setSalariesList(ArrayList<SelectItem> salariesList) {
		this.salariesList = salariesList;
	}

	public void setNivFormationInitList(
			ArrayList<SelectItem> nivFormationInitList) {
		this.nivFormationInitList = nivFormationInitList;
	}

	public void setNivFormationAtteintList(
			ArrayList<SelectItem> nivFormationAtteintList) {
		this.nivFormationAtteintList = nivFormationAtteintList;
	}

	public List<RevenuComplementaireBean> getCommissionBeanList() {
		Collections.sort(commissionBeanList);
		Collections.reverse(commissionBeanList);
		return commissionBeanList;
	}

	public void setCommissionBeanList(
			List<RevenuComplementaireBean> commissionBeanList) {
		this.commissionBeanList = commissionBeanList;
	}

	public List<RevenuComplementaireBean> getPrimeFixeBeanList() {
		Collections.sort(primeFixeBeanList);
		Collections.reverse(primeFixeBeanList);
		return primeFixeBeanList;
	}

	public void setPrimeFixeBeanList(
			List<RevenuComplementaireBean> primeFixeBeanList) {
		this.primeFixeBeanList = primeFixeBeanList;
	}

	public List<RevenuComplementaireBean> getPrimeVariableBeanList() {
		Collections.sort(primeVariableBeanList);
		Collections.reverse(primeVariableBeanList);
		return primeVariableBeanList;
	}

	public void setPrimeVariableBeanList(
			List<RevenuComplementaireBean> primeVariableBeanList) {
		this.primeVariableBeanList = primeVariableBeanList;
	}

	public List<RevenuComplementaireBean> getAvantageAssujettiBeanList() {
		Collections.sort(avantageAssujettiBeanList);
		Collections.reverse(avantageAssujettiBeanList);
		return avantageAssujettiBeanList;
	}

	public void setAvantageAssujettiBeanList(
			List<RevenuComplementaireBean> avantageAssujettiBeanList) {
		this.avantageAssujettiBeanList = avantageAssujettiBeanList;
	}

	public List<RevenuComplementaireBean> getAvantageNonAssujettiBeanList() {
		Collections.sort(avantageNonAssujettiBeanList);
		Collections.reverse(avantageNonAssujettiBeanList);
		return avantageNonAssujettiBeanList;
	}

	public void setAvantageNonAssujettiBeanList(
			List<RevenuComplementaireBean> avantageNonAssujettiBeanList) {
		this.avantageNonAssujettiBeanList = avantageNonAssujettiBeanList;
	}

	public boolean isFileError() {
		return fileError;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public int getIdRem() {
		return idRem;
	}

	public void setIdRem(int idRem) {
		this.idRem = idRem;
	}

	public String getUrl() throws Exception {
		return getUrl(null);
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

	public String getTelephonePortable() {
		return telephonePortable;
	}

	public void setTelephonePortable(String telephonePortable) {
		this.telephonePortable = telephonePortable;
	}

	public String getSituationFamiliale() {
		return situationFamiliale;
	}

	public void setSituationFamiliale(String situationFamiliale) {
		this.situationFamiliale = situationFamiliale;
	}

	public String getPersonneAPrevenirNom() {
		return personneAPrevenirNom;
	}

	public void setPersonneAPrevenirNom(String personneAPrevenirNom) {
		this.personneAPrevenirNom = personneAPrevenirNom;
	}

	public String getPersonneAPrevenirPrenom() {
		return personneAPrevenirPrenom;
	}

	public void setPersonneAPrevenirPrenom(String personneAPrevenirPrenom) {
		this.personneAPrevenirPrenom = personneAPrevenirPrenom;
	}

	public String getPersonneAPrevenirAdresse() {
		return personneAPrevenirAdresse;
	}

	public void setPersonneAPrevenirAdresse(String personneAPrevenirAdresse) {
		this.personneAPrevenirAdresse = personneAPrevenirAdresse;
	}

	public String getPersonneAPrevenirTelephone() {
		return personneAPrevenirTelephone;
	}

	public void setPersonneAPrevenirTelephone(String personneAPrevenirTelephone) {
		this.personneAPrevenirTelephone = personneAPrevenirTelephone;
	}

	public String getPersonneAPrevenirLienParente() {
		return personneAPrevenirLienParente;
	}

	public void setPersonneAPrevenirLienParente(
			String personneAPrevenirLienParente) {
		this.personneAPrevenirLienParente = personneAPrevenirLienParente;
	}

	public boolean isImpression() {
		return impression;
	}

	public void setImpression(boolean impression) {
		this.impression = impression;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public List<PersonneAChargeBean> getPersonneAChargeBeanList() {
		return personneAChargeBeanList;
	}

	public void setPersonneAChargeBeanList(
			List<PersonneAChargeBean> personneAChargeBeanList) {
		this.personneAChargeBeanList = personneAChargeBeanList;
	}

	public PiecesJustificativesBean getPiecesJustificativesBean() {
		return piecesJustificativesBean;
	}

	public void setPiecesJustificativesBean(
			PiecesJustificativesBean piecesJustificativesBean) {
		this.piecesJustificativesBean = piecesJustificativesBean;
	}

	public boolean getCarteIdentite() {
		return carteIdentite;
	}

	public void setCarteIdentite(boolean carteIdentite) {
		this.carteIdentite = carteIdentite;
	}

	public boolean getAttestationSecu() {
		return attestationSecu;
	}

	public void setAttestationSecu(boolean attestationSecu) {
		this.attestationSecu = attestationSecu;
	}

	public boolean getPermisConduire() {
		return permisConduire;
	}

	public void setPermisConduire(boolean permisConduire) {
		this.permisConduire = permisConduire;
	}

	public boolean getRib() {
		return rib;
	}

	public void setRib(boolean rib) {
		this.rib = rib;
	}

	public boolean getDiplomes() {
		return diplomes;
	}

	public void setDiplomes(boolean diplomes) {
		this.diplomes = diplomes;
	}

	public boolean getCertificatTravail() {
		return certificatTravail;
	}

	public void setCertificatTravail(boolean certificatTravail) {
		this.certificatTravail = certificatTravail;
	}

	public boolean getAttestationVisiteMedicale() {
		return attestationVisiteMedicale;
	}

	public boolean getHabilitations() {
		return habilitations;
	}

	public void setHabilitations(boolean habilitations) {
		this.habilitations = habilitations;
	}

	public void setAttestationVisiteMedicale(boolean attestationVisiteMedicale) {
		this.attestationVisiteMedicale = attestationVisiteMedicale;
	}

	public List<PersonneAChargeBean> getPersonneAChargeBeanListTemp() {
		return personneAChargeBeanListTemp;
	}

	public void setPersonneAChargeBeanListTemp(
			List<PersonneAChargeBean> personneAChargeBeanListTemp) {
		this.personneAChargeBeanListTemp = personneAChargeBeanListTemp;
	}

	public boolean isPossedePermisConduire() {
		return possedePermisConduire;
	}

	public void setPossedePermisConduire(boolean possedePermisConduire) {
		this.possedePermisConduire = possedePermisConduire;
	}

	public Date getDebutExtraction() {
		return debutExtraction;
	}

	public void setDebutExtraction(Date debutExtraction) {
		this.debutExtraction = debutExtraction;
	}

	public Date getFinExtraction() {
		return finExtraction;
	}

	public void setFinExtraction(Date finExtraction) {
		this.finExtraction = finExtraction;
	}

	public String getUploadDir() {
		return Utils.upload;
	}

	public List<AbsenceBean> getAbsenceBeanListRefresh() throws Exception {
		if ((debutAb == null || debutAb == -1)
				&& (finAb == null || finAb == -1)) {
			AbsenceServiceImpl absenceServiceImpl = new AbsenceServiceImpl();
			List<AbsenceBean> absenceBeanList = absenceServiceImpl
					.getAbsenceBeanListByIdSalarie(this.id);
			absenceBeanListRefresh.clear();
			absenceBeanListRefresh.addAll(absenceBeanList);
		}
		for (AbsenceBean a : absenceBeanListRefresh) {
			if (a.getJustificatif() != null
					&& !a.getJustificatif().contains(
							Utils.getSessionFileUploadPath(session, id,
									"absence", 0, false, false, nomGroupe)))
				a.setJustificatif(Utils.getSessionFileUploadPath(session, id,
						"absence", 0, false, false, nomGroupe)
						+ a.getJustificatif());
			AccidentServiceImpl accServ = new AccidentServiceImpl();
			FormationServiceImpl formServ = new FormationServiceImpl();
			if (accServ.getAccidentByIdAbsence(a.getId(), true) != null
					|| formServ.getFormationByIdAbsence(a.getId(), true) != null)
				a.setWarning(true);
		}
		Collections.sort(absenceBeanListRefresh);
		Collections.reverse(absenceBeanListRefresh);
		return absenceBeanListRefresh;
	}

	public void setAbsenceBeanListRefresh(
			List<AbsenceBean> absenceBeanListRefresh) {
		this.absenceBeanListRefresh = absenceBeanListRefresh;
	}

	public String getNomEntreprise() throws Exception {
		EntrepriseServiceImpl s = new EntrepriseServiceImpl();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		if (session.getAttribute("entreprise") != null
				&& Integer.parseInt(session.getAttribute("entreprise")
						.toString()) > 0) {
			return s.getEntrepriseBeanById(
					Integer.parseInt(session.getAttribute("entreprise")
							.toString())).getNom();
		} else {
			return "";
		}
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public boolean isDisplayButton() {
		return displayButton;
	}

	public void setDisplayButton(boolean displayButton) {
		this.displayButton = displayButton;
	}

	public List<EvenementBean> getEvenementBeanList() throws Exception {
		if ((debutEv == null || debutEv == -1)
				&& (finEv == null || finEv == -1)) {
			EvenementServiceImpl evserv = new EvenementServiceImpl();
			evenementBeanList = evserv.getEvenementBeanListByIdSalarie(this.id);
		}
		for (EvenementBean h : evenementBeanList) {
			if (h.getJustificatif() != null
					&& !h.getJustificatif().contains(
							Utils.getSessionFileUploadPath(session, id,
									"evenement", 0, false, false, nomGroupe))) {
				h.setJustificatif(Utils.getSessionFileUploadPath(session, id,
						"evenement", 0, false, false, nomGroupe)
						+ h.getJustificatif());
			}
		}
		Collections.sort(evenementBeanList);
		Collections.reverse(evenementBeanList);
		return evenementBeanList;
	}

	public void setEvenementBeanList(List<EvenementBean> evenementBeanList) {
		this.evenementBeanList = evenementBeanList;
	}

	public String getUsername() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		String userName = authentication.getName();
		return userName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(String entreprise) {
		this.entreprise = entreprise;
	}

	public boolean isHasEvenement() throws Exception {
		EvenementServiceImpl ev = new EvenementServiceImpl();
		if (ev.getEvenementBeanListByIdSalarie(this.id).isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public void setHasEvenement(boolean hasEvenement) {
		this.hasEvenement = hasEvenement;
	}

	public Integer getDebutPo() {
		return debutPo;
	}

	public void setDebutPo(Integer debutPo) {
		this.debutPo = debutPo;
	}

	public Integer getFinPo() {
		return finPo;
	}

	public void setFinPo(Integer finPo) {
		this.finPo = finPo;
	}

	public Integer getDebutRe() {
		return debutRe;
	}

	public void setDebutRe(Integer debutRe) {
		this.debutRe = debutRe;
	}

	public Integer getFinRe() {
		return finRe;
	}

	public void setFinRe(Integer finRe) {
		this.finRe = finRe;
	}

	public Integer getDebutHa() {
		return debutHa;
	}

	public void setDebutHa(Integer debutHa) {
		this.debutHa = debutHa;
	}

	public Integer getFinHa() {
		return finHa;
	}

	public void setFinHa(Integer finHa) {
		this.finHa = finHa;
	}

	public Integer getDebutFo() {
		return debutFo;
	}

	public void setDebutFo(Integer debutFo) {
		this.debutFo = debutFo;
	}

	public Integer getFinFo() {
		return finFo;
	}

	public void setFinFo(Integer finFo) {
		this.finFo = finFo;
	}

	public Integer getDebutAb() {
		return debutAb;
	}

	public void setDebutAb(Integer debutAb) {
		this.debutAb = debutAb;
	}

	public Integer getFinAb() {
		return finAb;
	}

	public void setFinAb(Integer finAb) {
		this.finAb = finAb;
	}

	public Integer getDebutAc() {
		return debutAc;
	}

	public void setDebutAc(Integer debutAc) {
		this.debutAc = debutAc;
	}

	public Integer getFinAc() {
		return finAc;
	}

	public void setFinAc(Integer finAc) {
		this.finAc = finAc;
	}

	public Integer getDebutEn() {
		return debutEn;
	}

	public void setDebutEn(Integer debutEn) {
		this.debutEn = debutEn;
	}

	public Integer getFinEn() {
		return finEn;
	}

	public void setFinEn(Integer finEn) {
		this.finEn = finEn;
	}

	public Integer getDebutEv() {
		return debutEv;
	}

	public void setDebutEv(Integer debutEv) {
		this.debutEv = debutEv;
	}

	public Integer getFinEv() {
		return finEv;
	}

	public void setFinEv(Integer finEv) {
		this.finEv = finEv;
	}

	public List<SelectItem> getAnneesPo() throws Exception {
		anneesPo.clear();
		Integer annee = new GregorianCalendar().get(Calendar.YEAR);
		ParcoursServiceImpl par = new ParcoursServiceImpl();
		for (ParcoursBean p : par.getParcoursBeanListByIdSalarie(this.id)) {
			Calendar c = new GregorianCalendar();
			c.setTime(p.getDebutFonction());
			if (c.get(Calendar.YEAR) < annee) {
				annee = c.get(Calendar.YEAR);
			}
		}
		for (int i = annee; i <= new GregorianCalendar().get(Calendar.YEAR); i++) {
			anneesPo.add(new SelectItem(i, i + ""));
		}
		return anneesPo;
	}

	public void setAnneesPo(List<SelectItem> anneesPo) {
		this.anneesPo = anneesPo;
	}

	public List<SelectItem> getAnneesRe() throws Exception {
		anneesRe.clear();
		Integer annee = new GregorianCalendar().get(Calendar.YEAR);
		RemunerationServiceImpl rem = new RemunerationServiceImpl();
		for (RemunerationBean r : rem.getRemunerationByIdSalarie(this.id)) {
			if (r.getAnnee() < annee) {
				annee = r.getAnnee();
			}
		}
		for (int i = annee; i <= new GregorianCalendar().get(Calendar.YEAR); i++) {
			anneesRe.add(new SelectItem(i, i + ""));
		}
		return anneesRe;
	}

	public void setAnneesRe(List<SelectItem> anneesRe) {
		this.anneesRe = anneesRe;
	}

	public List<SelectItem> getAnneesHa() throws Exception {
		anneesHa.clear();
		Integer annee = new GregorianCalendar().get(Calendar.YEAR);
		HabilitationServiceImpl hab = new HabilitationServiceImpl();
		for (HabilitationBean h : hab
				.getHabilitationBeanListByIdSalarie(this.id)) {
			Calendar c = new GregorianCalendar();
			c.setTime(h.getDelivrance());
			if (c.get(Calendar.YEAR) < annee) {
				annee = c.get(Calendar.YEAR);
			}
		}
		for (int i = annee; i <= new GregorianCalendar().get(Calendar.YEAR); i++) {
			anneesHa.add(new SelectItem(i, i + ""));
		}
		return anneesHa;
	}

	public void setAnneesHa(List<SelectItem> anneesHa) {
		this.anneesHa = anneesHa;
	}

	public List<SelectItem> getAnneesFo() throws Exception {
		anneesFo.clear();
		Integer annee = new GregorianCalendar().get(Calendar.YEAR);
		FormationServiceImpl form = new FormationServiceImpl();
		for (FormationBean f : form.getFormationBeanListByIdSalarie(this.id)) {
			Calendar c = new GregorianCalendar();
			c.setTime(f.getDebutFormation());
			if (c.get(Calendar.YEAR) < annee) {
				annee = c.get(Calendar.YEAR);
			}
		}
		for (int i = annee; i <= new GregorianCalendar().get(Calendar.YEAR); i++) {
			anneesFo.add(new SelectItem(i, i + ""));
		}
		return anneesFo;
	}

	public void setAnneesFo(List<SelectItem> anneesFo) {
		this.anneesFo = anneesFo;
	}

	public List<SelectItem> getAnneesAb() throws Exception {
		anneesAb.clear();
		Integer annee = new GregorianCalendar().get(Calendar.YEAR);
		AbsenceServiceImpl abs = new AbsenceServiceImpl();
		for (AbsenceBean a : abs.getAbsenceBeanListByIdSalarie(this.id)) {
			Calendar c = new GregorianCalendar();
			c.setTime(a.getDebutAbsence());
			if (c.get(Calendar.YEAR) < annee) {
				annee = c.get(Calendar.YEAR);
			}
		}
		for (int i = annee; i <= new GregorianCalendar().get(Calendar.YEAR); i++) {
			anneesAb.add(new SelectItem(i, i + ""));
		}
		return anneesAb;
	}

	public void setAnneesAb(List<SelectItem> anneesAb) {
		this.anneesAb = anneesAb;
	}

	public List<SelectItem> getAnneesAc() throws Exception {
		anneesAc.clear();
		Integer annee = new GregorianCalendar().get(Calendar.YEAR);
		AccidentServiceImpl acc = new AccidentServiceImpl();
		for (AccidentBean a : acc.getAccidentBeanListByIdSalarie(this.id)) {
			Calendar c = new GregorianCalendar();
			c.setTime(a.getDateAccident());
			if (c.get(Calendar.YEAR) < annee) {
				annee = c.get(Calendar.YEAR);
			}
		}
		for (int i = annee; i <= new GregorianCalendar().get(Calendar.YEAR); i++) {
			anneesAc.add(new SelectItem(i, i + ""));
		}
		return anneesAc;
	}

	public void setAnneesAc(List<SelectItem> anneesAc) {
		this.anneesAc = anneesAc;
	}

	public List<SelectItem> getAnneesEn() throws Exception {
		anneesEn.clear();
		Integer annee = new GregorianCalendar().get(Calendar.YEAR);
		EntretienServiceImpl ev = new EntretienServiceImpl();
		for (EntretienBean e : ev.getEntretienBeanListByIdSalarie(this.id)) {
			Calendar c = new GregorianCalendar();
			c.setTime(e.getDateEntretien());
			if (c.get(Calendar.YEAR) < annee) {
				annee = c.get(Calendar.YEAR);
			}
		}
		for (int i = annee; i <= new GregorianCalendar().get(Calendar.YEAR); i++) {
			anneesEn.add(new SelectItem(i, i + ""));
		}
		return anneesEn;
	}

	public void setAnneesEn(List<SelectItem> anneesEn) {
		this.anneesEn = anneesEn;
	}

	public List<SelectItem> getAnneesEv() throws Exception {
		anneesEv.clear();
		Integer annee = new GregorianCalendar().get(Calendar.YEAR);
		EvenementServiceImpl ev = new EvenementServiceImpl();
		for (EvenementBean e : ev.getEvenementBeanListByIdSalarie(this.id)) {
			Calendar c = new GregorianCalendar();
			c.setTime(e.getDateEvenement());
			if (c.get(Calendar.YEAR) < annee) {
				annee = c.get(Calendar.YEAR);
			}
		}
		for (int i = annee; i <= new GregorianCalendar().get(Calendar.YEAR); i++) {
			anneesEv.add(new SelectItem(i, i + ""));
		}
		return anneesEv;
	}

	public void setAnneesEv(List<SelectItem> anneesEv) {
		this.anneesEv = anneesEv;
	}

	public Integer getDebutCt() {
		return debutCt;
	}

	public void setDebutCt(Integer debutCt) {
		this.debutCt = debutCt;
	}

	public Integer getFinCt() {
		return finCt;
	}

	public void setFinCt(Integer finCt) {
		this.finCt = finCt;
	}

	public List<SelectItem> getAnneesCt() throws Exception {
		anneesCt.clear();
		Integer annee = new GregorianCalendar().get(Calendar.YEAR);
		ContratTravailServiceImpl serv = new ContratTravailServiceImpl();
		for (ContratTravailBean ct : serv
				.getContratTravailBeanListByIdSalarie(this.id)) {
			Calendar c = new GregorianCalendar();
			c.setTime(ct.getDebutContrat());
			if (c.get(Calendar.YEAR) < annee) {
				annee = c.get(Calendar.YEAR);
			}
		}
		for (int i = annee; i <= new GregorianCalendar().get(Calendar.YEAR); i++) {
			anneesCt.add(new SelectItem(i, i + ""));
		}
		return anneesCt;
	}

	public void setAnneesCt(List<SelectItem> anneesCt) {
		this.anneesCt = anneesCt;
	}

	public List<ContratTravailBean> getContratTravailBeanList()
			throws Exception {
		if ((debutCt == null || debutCt == -1)
				&& (finCt == null || finCt == -1)) {
			ContratTravailServiceImpl ctserv = new ContratTravailServiceImpl();
			contratTravailBeanList = ctserv
					.getContratTravailBeanListByIdSalarie(this.id);
		}
		for (ContratTravailBean h : contratTravailBeanList) {
			if (h.getJustificatif() != null
					&& !h.getJustificatif().contains(
							Utils.getSessionFileUploadPath(session, id,
									"contrat", 0, false, false, nomGroupe))) {
				h.setJustificatif(Utils.getSessionFileUploadPath(session, id,
						"contrat", 0, false, false, nomGroupe)
						+ h.getJustificatif());
			}
		}
		Collections.sort(contratTravailBeanList);
		Collections.reverse(contratTravailBeanList);
		return contratTravailBeanList;
	}

	public void setContratTravailBeanList(
			List<ContratTravailBean> contratTravailBeanList) {
		this.contratTravailBeanList = contratTravailBeanList;
	}

	public String getCreditDifTmpDisplay() throws Exception {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");
		if (getCreditDifTmp() != null) {
			return df.format(getCreditDifTmp());
		} else {
			return "0.00";
		}
	}

	public void setCreditDifTmpDisplay(String creditDifTmpDisplay) {
		this.creditDifTmpDisplay = creditDifTmpDisplay;
	}

	public String getNomGroupe() {
		return nomGroupe;
	}

	public void setNomGroupe(String nomGroupe) {
		this.nomGroupe = nomGroupe;
	}

	public String getNomGroupeForUpload() {
		nomGroupeForUpload = nomGroupe.replace(" ", "_");
		return nomGroupeForUpload;
	}

	public void setNomGroupeForUpload(String nomGroupeForUpload) {
		this.nomGroupeForUpload = nomGroupeForUpload;
	}

}
