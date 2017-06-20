package com.cci.gpec.web.backingBean.remuneration;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.LienRemunerationRevenuBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.RemunerationBean;
import com.cci.gpec.commons.RevenuComplementaireBean;
import com.cci.gpec.commons.StatutBean;
import com.cci.gpec.metier.implementation.LienRemunerationRevenuServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.RemunerationServiceImpl;
import com.cci.gpec.metier.implementation.RevenuComplementaireServiceImpl;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;
import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.context.effects.JavascriptContext;

public class SalarieRemunerationsFormBB {
	// primary key
	private int id;

	private String montantNPrecString = "Montant<br/>Annee N-1";
	private String montantNString = "Montant<br/>Annee N";
	private String actuString = "Actualisation<br/>en numéraire";
	private String evolString = "Evolution<br/>en %";

	private boolean anneeOk;

	private boolean changeCouleur;

	private int idSalarie;

	private boolean isFirst;
	private boolean maj;

	private boolean print = false;

	private String firstValuec;
	private String firstValuepf;
	private String firstValuepv;
	private String firstValueaa;
	private String firstValueana;
	private String firstValuefp;

	private boolean modif = false;

	// Atributs de type string pour avoir des champs vides à la place d'avoir
	// des champs à 0

	DecimalFormat df = new DecimalFormat();
	DecimalFormat df2 = new DecimalFormat();

	private int annee;
	private String coefficient;
	private String echelon;
	private String niveau;
	private String horaire;
	private String taux;
	private String salaireBrut;
	private String salaireConvBrut;
	private String csp;
	private String metier;

	private String salaireConventionelMinimumAnneePrecedente;
	private String salaireConventionelMinimum;
	private String salaireConventionelMinimumReact;
	private String salaireConventionelMinimumEvol;
	private String salaireConventionelMinimumComm;

	private String salaireDeBaseAnneePrecedente;
	private String salaireDeBase;
	private String salaireDeBaseReact;
	private String salaireDeBaseEvol;
	private String salaireDeBaseComm;

	private String totalBrutAnnuelAnneePrecedente;
	private String totalBrutAnnuel;
	private String totalBrutAnnuelReact;
	private String totalBrutAnnuelEvol;
	private String totalBrutAnnuelComm;

	private String augmentationCollectiveAnneePrecedente;
	private String augmentationCollective;
	private String augmentationCollectiveReact;
	private String augmentationCollectiveEvol;
	private String augmentationCollectiveComm;

	private String augmentationIndividuelleAnneePrecedente;
	private String augmentationIndividuelle;
	private String augmentationIndividuelleReact;
	private String augmentationIndividuelleEvol;
	private String augmentationIndividuelleComm;

	private String heuresSup25AnneePrecedente;
	private String heuresSup25;
	private String heuresSup25React;
	private String heuresSup25Evol;
	private String heuresSup25Comm;

	private String heuresSup50AnneePrecedente;
	private String heuresSup50;
	private String heuresSup50React;
	private String heuresSup50Evol;
	private String heuresSup50Comm;

	private List<RevenuComplementaireBean> revenusComplementairesBeanList;
	private List<RevenuComplementaireBean> commissionBeanList;
	private List<RevenuComplementaireBean> primeVariableBeanList;
	private List<RevenuComplementaireBean> primeFixeBeanList;
	private List<RevenuComplementaireBean> avantageAssujettiBeanList;
	private List<RevenuComplementaireBean> avantageNonAssujettiBeanList;

	private LienRemunerationRevenuBean modifC = null;
	private LienRemunerationRevenuBean modifPf = null;
	private LienRemunerationRevenuBean modifPv = null;
	private LienRemunerationRevenuBean modifAa = null;
	private LienRemunerationRevenuBean modifAna = null;
	private LienRemunerationRevenuBean modifFp = null;

	private int idLienRevComp;
	private int idRevCompLien;
	private List<RevenuComplementaireBean> revenuComplementaireInventory;

	private List<LienRemunerationRevenuBean> commissionInventoryLienEditable;
	private List<LienRemunerationRevenuBean> commissionInventoryLien;

	private List<LienRemunerationRevenuBean> primeVariableInventoryLienEditable;
	private List<LienRemunerationRevenuBean> primeVariableInventoryLien;

	private List<LienRemunerationRevenuBean> primeFixeInventoryLienEditable;
	private List<LienRemunerationRevenuBean> primeFixeInventoryLien;

	private List<LienRemunerationRevenuBean> avantageAssujettiInventoryLienEditable;
	private List<LienRemunerationRevenuBean> avantageAssujettiInventoryLien;

	private List<LienRemunerationRevenuBean> avantageNonAssujettiInventoryLienEditable;
	private List<LienRemunerationRevenuBean> avantageNonAssujettiInventoryLien;

	private List<LienRemunerationRevenuBean> fraisProfInventoryLienEditable;
	private List<LienRemunerationRevenuBean> fraisProfInventoryLien;

	private List<Double> commissionNPrecInventory;
	private List<Double> primeVariableNPrecInventory;
	private List<Double> primeFixeNPrecInventory;
	private List<Double> avantageAssujettiNPrecInventory;
	private List<Double> avantageNonAssujettiNPrecInventory;
	private List<Double> fraisProfNPrecInventory;

	private List<LienRemunerationRevenuBean> lienRemuRevListDeleted;

	private List<SelectItem> revenusComplementairesList;
	private List<SelectItem> commissionList;
	private List<SelectItem> primeVariableList;
	private List<SelectItem> primeFixeList;
	private List<SelectItem> avantageAssujettiList;
	private List<SelectItem> avantageNonAssujettiList;
	private List<SelectItem> fraisProfessionnelList;

	private int idRevenuComplementaireSelected;
	private String idCommissionSelected;
	private String idPrimeVariableSelected;
	private String idPrimeFixeSelected;
	private String idAvantageAssujettiSelected;
	private String idAvantageNonAssujettiSelected;
	private String idFraisProfSelected;
	private int idLienSelected;

	private int metierSelected;

	private String montantNPrecCommission;
	private String montantNCommission;
	private String actuCommission;
	private String comCommission;
	private String totalNPrecCommission;
	private String totalNCommission;
	private String totalActuCommission;

	private String montantNPrecPrimeVariable;
	private String montantNPrimeVariable;
	private String actuPrimeVariable;
	private String comPrimeVariable;
	private String totalNPrecPrimeVariable;
	private String totalNPrimeVariable;
	private String totalActuPrimeVariable;

	private String montantNPrecPrimeFixe;
	private String montantNPrimeFixe;
	private String actuPrimeFixe;
	private String comPrimeFixe;
	private String totalNPrecPrimeFixe;
	private String totalNPrimeFixe;
	private String totalActuPrimeFixe;

	private String montantNPrecAvantageAssujetti;
	private String montantNAvantageAssujetti;
	private String actuAvantageAssujetti;
	private String comAvantageAssujetti;
	private String totalNPrecAvantageAssujetti;
	private String totalNAvantageAssujetti;
	private String totalActuAvantageAssujetti;

	private String montantNPrecAvantageNonAssujetti;
	private String montantNAvantageNonAssujetti;
	private String actuAvantageNonAssujetti;
	private String comAvantageNonAssujetti;
	private String totalNPrecAvantageNonAssujetti;
	private String totalNAvantageNonAssujetti;
	private String totalActuAvantageNonAssujetti;

	private String montantNPrecFraisProf;
	private String montantNFraisProf;
	private String comFraisProf;
	private String totalNPrecFraisProf;
	private String totalNFraisProf;

	private String commentaire;

	private boolean modalRendered = false;

	private String remunerationBruteAnnuelle;
	private String remunerationBruteAnnuelleNPrec;
	private String remunerationBruteAnnuelleActu;
	private String remunerationBruteAnnuelleEvol;

	private String remunerationGlobale;
	private String remunerationGlobaleNPrec;
	private String remunerationGlobaleActu;
	private String remunerationGlobaleEvol;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public void init() throws Exception {

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		df2.setDecimalFormatSymbols(symbols);
		df2.applyPattern("0");

		isFirst = true;
		maj = false;

		firstValuec = "";
		firstValuepf = "";
		firstValuepv = "";
		firstValueaa = "";
		firstValueana = "";
		firstValuefp = "";

		idLienRevComp = 0;
		idRevCompLien = 0;

		if (modif == false) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);
			if (session.getAttribute("idSalarie") != null) {
				this.idSalarie = Integer.parseInt(session.getAttribute(
						"idSalarie").toString());
			} else {
				this.idSalarie = -1;
			}
		}
		if (modif == false && anneeOk == false) {
			Date dateToday = new Date();
			GregorianCalendar calendarToday = new GregorianCalendar();
			calendarToday.setTime(dateToday);
			int year = calendarToday.get(Calendar.YEAR);
			this.annee = year;
		}

		this.id = 0;

		this.coefficient = "";
		this.echelon = "";
		this.niveau = "";
		this.horaire = "";
		this.taux = "";
		this.salaireBrut = "";
		this.salaireConvBrut = "";
		this.commentaire = "";

		this.salaireConventionelMinimumAnneePrecedente = "0";
		this.salaireConventionelMinimum = "";
		this.salaireConventionelMinimumReact = "";
		this.salaireConventionelMinimumEvol = "0";
		this.salaireConventionelMinimumComm = "";

		this.salaireDeBaseAnneePrecedente = "0";
		this.salaireDeBase = "";
		this.salaireDeBaseReact = "";
		this.salaireDeBaseEvol = "0";
		this.salaireDeBaseComm = "";

		this.totalBrutAnnuelAnneePrecedente = "";
		this.totalBrutAnnuel = "";
		this.totalBrutAnnuelReact = "";
		this.totalBrutAnnuelEvol = "0";
		this.totalBrutAnnuelComm = "";

		this.augmentationCollectiveAnneePrecedente = "0";
		this.augmentationCollective = "";
		this.augmentationCollectiveReact = "";
		this.augmentationCollectiveEvol = "0";
		this.augmentationCollectiveComm = "";

		this.augmentationIndividuelleAnneePrecedente = "0";
		this.augmentationIndividuelle = "";
		this.augmentationIndividuelleReact = "";
		this.augmentationIndividuelleEvol = "0";
		this.augmentationIndividuelleComm = "";

		this.heuresSup25AnneePrecedente = "0";
		this.heuresSup25 = "";
		this.heuresSup25React = "";
		this.heuresSup25Evol = "0";
		this.heuresSup25Comm = "";

		this.heuresSup50AnneePrecedente = "0";
		this.heuresSup50 = "";
		this.heuresSup50React = "";
		this.heuresSup50Evol = "0";
		this.heuresSup50Comm = "";

		this.idRevenuComplementaireSelected = 0;
		this.idCommissionSelected = "-1";
		this.idPrimeVariableSelected = "-1";
		this.idPrimeFixeSelected = "-1";
		this.idAvantageAssujettiSelected = "-1";
		this.idAvantageNonAssujettiSelected = "-1";
		this.idFraisProfSelected = "-1";
		this.idLienSelected = 0;

		this.montantNPrecCommission = "0";
		this.montantNCommission = "0";
		this.actuCommission = "0";
		this.comCommission = "";

		this.montantNPrecPrimeVariable = "0";
		this.montantNPrimeVariable = "0";
		this.actuPrimeVariable = "0";
		this.comPrimeVariable = "";

		this.montantNPrecPrimeFixe = "0";
		this.montantNPrimeFixe = "0";
		this.actuPrimeFixe = "0";
		this.comPrimeFixe = "";

		this.montantNPrecAvantageAssujetti = "0";
		this.montantNAvantageAssujetti = "0";
		this.actuAvantageAssujetti = "0";
		this.comAvantageAssujetti = "";

		this.montantNPrecAvantageNonAssujetti = "0";
		this.montantNAvantageNonAssujetti = "0";
		this.actuAvantageNonAssujetti = "0";
		this.comAvantageNonAssujetti = "";

		this.montantNPrecFraisProf = "0";
		this.montantNFraisProf = "0";
		this.comFraisProf = "";

		this.totalNCommission = "0";
		this.totalActuCommission = "0";
		this.totalNPrecCommission = "0";

		this.totalNPrimeFixe = "0";
		this.totalActuPrimeFixe = "0";
		this.totalNPrecPrimeFixe = "0";

		this.totalNPrimeVariable = "0";
		this.totalActuPrimeVariable = "0";
		this.totalNPrecPrimeVariable = "0";

		this.totalNAvantageAssujetti = "0";
		this.totalActuAvantageAssujetti = "0";
		this.totalNPrecAvantageAssujetti = "0";

		this.totalNAvantageNonAssujetti = "0";
		this.totalActuAvantageNonAssujetti = "0";
		this.totalNPrecAvantageNonAssujetti = "0";

		this.totalNFraisProf = "0";
		this.totalNPrecFraisProf = "0";

		this.remunerationBruteAnnuelle = "0";
		this.remunerationBruteAnnuelleNPrec = "0";
		this.remunerationBruteAnnuelleActu = "0";
		this.remunerationBruteAnnuelleEvol = "0";
		this.remunerationGlobale = "0";
		this.remunerationGlobaleNPrec = "0";
		this.remunerationGlobaleActu = "0";
		this.remunerationGlobaleEvol = "0";

		commissionBeanList = new ArrayList<RevenuComplementaireBean>();
		RevenuComplementaireServiceImpl revenuComplementaireService = new RevenuComplementaireServiceImpl();
		commissionBeanList = revenuComplementaireService
				.getCommissionList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		List<RevenuComplementaireBean> revenuComplementaireBeanList = revenuComplementaireService
				.getRevenuComplementaireList(Integer.parseInt(session
						.getAttribute("groupe").toString()));
		revenusComplementairesList = new ArrayList<SelectItem>();
		SelectItem selectItem;

		for (RevenuComplementaireBean revenuComplementaireBean : revenuComplementaireBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(revenuComplementaireBean.getId());
			selectItem.setLabel(revenuComplementaireBean.getLibelle());
			revenusComplementairesList.add(selectItem);
		}

		List<RevenuComplementaireBean> commissionBeanList = revenuComplementaireService
				.getCommissionList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		commissionList = new ArrayList<SelectItem>();
		SelectItem selectItem2;

		for (RevenuComplementaireBean revenuComplementaireBean : commissionBeanList) {
			selectItem2 = new SelectItem();
			selectItem2.setValue(revenuComplementaireBean.getLibelle());
			selectItem2.setLabel(revenuComplementaireBean.getLibelle());
			commissionList.add(selectItem2);
		}

		List<RevenuComplementaireBean> primeFixeBeanList = revenuComplementaireService
				.getPrimeFixeList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		primeFixeList = new ArrayList<SelectItem>();
		SelectItem selectItem3;

		for (RevenuComplementaireBean revenuComplementaireBean : primeFixeBeanList) {
			selectItem3 = new SelectItem();
			selectItem3.setValue(revenuComplementaireBean.getLibelle());
			selectItem3.setLabel(revenuComplementaireBean.getLibelle());
			primeFixeList.add(selectItem3);
		}

		List<RevenuComplementaireBean> primeVariableBeanList = revenuComplementaireService
				.getPrimeVariableList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		primeVariableList = new ArrayList<SelectItem>();
		SelectItem selectItem4;

		for (RevenuComplementaireBean revenuComplementaireBean : primeVariableBeanList) {
			selectItem4 = new SelectItem();
			selectItem4.setValue(revenuComplementaireBean.getLibelle());
			selectItem4.setLabel(revenuComplementaireBean.getLibelle());
			primeVariableList.add(selectItem4);
		}

		List<RevenuComplementaireBean> avantageAssujettiBeanList = revenuComplementaireService
				.getAvantageAssujettiList(Integer.parseInt(session
						.getAttribute("groupe").toString()));
		avantageAssujettiList = new ArrayList<SelectItem>();
		SelectItem selectItem5;

		for (RevenuComplementaireBean revenuComplementaireBean : avantageAssujettiBeanList) {
			selectItem5 = new SelectItem();
			selectItem5.setValue(revenuComplementaireBean.getLibelle());
			selectItem5.setLabel(revenuComplementaireBean.getLibelle());
			avantageAssujettiList.add(selectItem5);
		}

		List<RevenuComplementaireBean> avantageNonAssujettiBeanList = revenuComplementaireService
				.getAvantageNonAssujettiList(Integer.parseInt(session
						.getAttribute("groupe").toString()));
		avantageNonAssujettiList = new ArrayList<SelectItem>();
		SelectItem selectItem6;

		for (RevenuComplementaireBean revenuComplementaireBean : avantageNonAssujettiBeanList) {
			selectItem6 = new SelectItem();
			selectItem6.setValue(revenuComplementaireBean.getLibelle());
			selectItem6.setLabel(revenuComplementaireBean.getLibelle());
			avantageNonAssujettiList.add(selectItem6);
		}

		List<RevenuComplementaireBean> fraisProfessionnelBeanList = revenuComplementaireService
				.getFraisProfList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		fraisProfessionnelList = new ArrayList<SelectItem>();
		SelectItem selectItem7;

		for (RevenuComplementaireBean revenuComplementaireBean : fraisProfessionnelBeanList) {
			selectItem7 = new SelectItem();
			selectItem7.setValue(revenuComplementaireBean.getLibelle());
			selectItem7.setLabel(revenuComplementaireBean.getLibelle());
			fraisProfessionnelList.add(selectItem7);
		}

	}

	public void modifRemuneration() throws Exception {
		this.modif = true;
		this.anneeOk = true;
		validerAnnee();
	}

	public void validerAnnee() throws Exception {

		if (modif) {
			init();
			if (!print) {
				modalRendered = !modalRendered;
			}
		}
		this.lienRemuRevListDeleted = new ArrayList<LienRemunerationRevenuBean>();

		this.commissionInventoryLien = new ArrayList<LienRemunerationRevenuBean>();
		this.commissionInventoryLienEditable = new ArrayList<LienRemunerationRevenuBean>();

		this.primeFixeInventoryLien = new ArrayList<LienRemunerationRevenuBean>();
		this.primeFixeInventoryLienEditable = new ArrayList<LienRemunerationRevenuBean>();

		this.primeVariableInventoryLien = new ArrayList<LienRemunerationRevenuBean>();
		this.primeVariableInventoryLienEditable = new ArrayList<LienRemunerationRevenuBean>();

		this.avantageAssujettiInventoryLien = new ArrayList<LienRemunerationRevenuBean>();
		this.avantageAssujettiInventoryLienEditable = new ArrayList<LienRemunerationRevenuBean>();

		this.avantageNonAssujettiInventoryLien = new ArrayList<LienRemunerationRevenuBean>();
		this.avantageNonAssujettiInventoryLienEditable = new ArrayList<LienRemunerationRevenuBean>();

		this.fraisProfInventoryLien = new ArrayList<LienRemunerationRevenuBean>();
		this.fraisProfInventoryLienEditable = new ArrayList<LienRemunerationRevenuBean>();

		RemunerationServiceImpl service = new RemunerationServiceImpl();

		this.echelon = "";
		this.niveau = "";
		this.coefficient = "";
		this.lienRemuRevListDeleted = new ArrayList<LienRemunerationRevenuBean>();

		SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
				.getCurrentInstance().getCurrentInstance().getExternalContext()
				.getSessionMap().get("salarieFormBB");

		List<ParcoursBean> list = new ArrayList<ParcoursBean>();
		list = salarieFormBB.getParcoursBeanList();
		ParcoursBean p = new ParcoursBean();
		boolean parcoursFound = false;

		int isIn = 0;
		for (ParcoursBean pb : list) {
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(pb.getDebutFonction());
			int y1 = c.get(Calendar.YEAR);
			int y2 = 0;
			if (pb.getFinFonction() != null) {
				c.setTime(pb.getFinFonction());
				y2 = c.get(Calendar.YEAR);
			}
			if (pb.getDebutFonction() != null) {
				if ((pb.getFinFonction() != null && y1 <= this.annee && y2 >= this.annee)
						|| (pb.getFinFonction() == null && y1 <= this.annee)) {
					isIn = 1;
					if (!parcoursFound) {
						p = pb;
						parcoursFound = true;
					} else {
						if (pb.getDebutFonction().before(p.getDebutFonction())) {
							p = pb;
							parcoursFound = true;
						}
					}
				}
			}
		}
		if (modif == false && isIn == 0) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Aucun poste occup\u00E9 \u00E0 cette p\u00E9riode.",
					"Aucun poste occup\u00E9 \u00E0 cette p\u00E9riode.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:annee", message);
			anneeOk = false;
		} else {
			RemunerationServiceImpl serv = new RemunerationServiceImpl();
			if (modif == false
					&& !serv.getRemuneration(this.annee, salarieFormBB.getId())
							.isEmpty()) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Une fiche de r\u00E9mun\u00E9ration existe d\u00E9j\u00E0 pour ce salari\u00E9 \u00E0 l'ann\u00E9e s\u00E9lectionn\u00E9e.",
						"Une fiche de r\u00E9mun\u00E9ration existe d\u00E9j\u00E0 pour ce salari\u00E9 \u00E0 l'ann\u00E9e s\u00E9lectionn\u00E9e.");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:annee", message);
				anneeOk = false;
			} else {

				this.metier = p.getNomMetier();
				this.csp = p.getNomTypeStatut();

				anneeOk = true;

				RemunerationBean remuActuelle = new RemunerationBean();

				if (modif == true) {
					for (RemunerationBean rb : service
							.getRemunerationList(Integer.parseInt(session
									.getAttribute("groupe").toString()))) {
						if (rb.getAnnee() == this.annee
								&& rb.getIdSalarie().equals(this.idSalarie)) {
							remuActuelle = rb;
						}
					}
				}
				List<RemunerationBean> rb = service.getRemuneration(
						this.annee - 1, this.idSalarie);
				if (!rb.isEmpty()) {
					RemunerationBean actu = rb.get(0);

					this.echelon = actu.getEchelon();
					this.niveau = actu.getNiveau();
					this.coefficient = (actu.getCoefficient() != null && !actu
							.getCoefficient().equals("")) ? actu
							.getCoefficient() : "";
					if (actu.getSalaireMinimumConventionnelActualisation() != 0) {
						this.salaireConventionelMinimumAnneePrecedente = df
								.format(actu
										.getSalaireMinimumConventionnelActualisation());

					} else {
						this.salaireConventionelMinimumAnneePrecedente = df
								.format(actu.getSalaireMinimumConventionnel());
					}

					if (actu.getSalaireDeBaseActualisation() != 0) {
						this.salaireDeBaseAnneePrecedente = df.format(actu
								.getSalaireDeBaseActualisation());
					} else {
						this.salaireDeBaseAnneePrecedente = df.format(actu
								.getSalaireDeBase());
					}

					if (actu.getAugmentationCollectiveActualisation() != 0) {
						this.augmentationCollectiveAnneePrecedente = df
								.format(actu
										.getAugmentationCollectiveActualisation());
					} else {
						this.augmentationCollectiveAnneePrecedente = df
								.format(actu.getAugmentationCollective());
					}

					if (actu.getAugmentationIndividuelleActualisation() != 0) {
						this.augmentationIndividuelleAnneePrecedente = df
								.format(actu
										.getAugmentationIndividuelleActualisation());
					} else {
						this.augmentationIndividuelleAnneePrecedente = df
								.format(actu.getAugmentationIndividuelle());
					}

					if (actu.getHeuresSup25Actualisation() != 0) {
						this.heuresSup25AnneePrecedente = df.format(actu
								.getHeuresSup25Actualisation());
					} else {
						this.heuresSup25AnneePrecedente = df.format(actu
								.getHeuresSup25());
					}

					if (actu.getHeuresSup50Actualisation() != 0) {
						this.heuresSup50AnneePrecedente = df.format(actu
								.getHeuresSup50Actualisation());

					} else {
						this.heuresSup50AnneePrecedente = df.format(actu
								.getHeuresSup50());
					}

				} else {
					this.salaireConventionelMinimumAnneePrecedente = "0";
					this.salaireDeBaseAnneePrecedente = "0";
					this.augmentationCollectiveAnneePrecedente = "0";
					this.augmentationIndividuelleAnneePrecedente = "0";
					this.heuresSup25AnneePrecedente = "0";
					this.heuresSup50AnneePrecedente = "0";
				}

				changeNullToZero();

				double sommeAnneePrecedente = Double
						.valueOf(this.salaireDeBaseAnneePrecedente)
						+ Double.valueOf(this.augmentationCollectiveAnneePrecedente)
						+ Double.valueOf(this.augmentationIndividuelleAnneePrecedente)
						+ Double.valueOf(this.heuresSup25AnneePrecedente)
						+ Double.valueOf(this.heuresSup50AnneePrecedente);
				this.totalBrutAnnuelAnneePrecedente = df
						.format(sommeAnneePrecedente);

				if (modif == true) {
					this.id = remuActuelle.getIdRemuneration();

					this.coefficient = remuActuelle.getCoefficient();
					this.echelon = remuActuelle.getEchelon();
					this.niveau = remuActuelle.getNiveau();

					List<RemunerationBean> listRemu = service.getRemuneration(
							this.annee - 1, this.idSalarie);
					if (!listRemu.isEmpty()) {
						RemunerationBean actu = listRemu.get(0);

						if (this.echelon.equals("")) {
							this.echelon = p.getEchelon();
						}
						if (this.niveau.equals("")) {
							this.niveau = p.getNiveau();
						}
						if (this.coefficient.equals("")) {
							this.coefficient = (p.getCoefficient() != null && !p
									.getCoefficient().equals("")) ? p
									.getCoefficient() : "";
						}
					}
					this.horaire = remuActuelle.getHoraireMensuelTravaille();
					this.taux = df.format(remuActuelle.getTauxHoraireBrut());
					this.salaireBrut = df.format(remuActuelle
							.getSalaireMensuelBrut());
					this.salaireConvBrut = df.format(remuActuelle
							.getSalaireMensuelConventionnelBrut());

					this.salaireConventionelMinimumComm = remuActuelle
							.getSalaireMinimumConventionnelCommentaire();
					this.salaireDeBaseComm = remuActuelle
							.getSalaireDeBaseCommentaire();
					this.augmentationCollectiveComm = remuActuelle
							.getAugmentationCollectiveCommentaire();
					this.augmentationIndividuelleComm = remuActuelle
							.getAugmentationIndividuelleCommentaire();
					this.heuresSup25Comm = remuActuelle
							.getHeuresSup25Commentaire();
					this.heuresSup50Comm = remuActuelle
							.getHeuresSup50Commentaire();

					this.salaireConventionelMinimum = df.format(remuActuelle
							.getSalaireMinimumConventionnel());
					this.salaireConventionelMinimumReact = df
							.format(remuActuelle
									.getSalaireMinimumConventionnelActualisation());

					if (Double
							.valueOf(this.salaireConventionelMinimumAnneePrecedente) != 0
							&& (Double.valueOf(this.salaireConventionelMinimum) != 0 || Double
									.valueOf(this.salaireConventionelMinimumReact) != 0)) {
						if (Double
								.valueOf(this.salaireConventionelMinimumReact) == 0) {
							this.salaireConventionelMinimumEvol = df
									.format(Double.valueOf(((((Double
											.valueOf(this.salaireConventionelMinimum) - Double
											.valueOf(this.salaireConventionelMinimumAnneePrecedente)) / Double
											.valueOf(this.salaireConventionelMinimumAnneePrecedente)) * 100) * 100) / 100));
						} else {
							this.salaireConventionelMinimumEvol = df
									.format(Double.valueOf(((((Double
											.valueOf(this.salaireConventionelMinimumReact) - Double
											.valueOf(this.salaireConventionelMinimumAnneePrecedente)) / Double
											.valueOf(this.salaireConventionelMinimumAnneePrecedente)) * 100) * 100) / 100));
						}
					}
					this.salaireConventionelMinimumComm = remuActuelle
							.getSalaireMinimumConventionnelCommentaire();

					this.salaireDeBase = df.format(remuActuelle
							.getSalaireDeBase());
					this.salaireDeBaseReact = df.format(remuActuelle
							.getSalaireDeBaseActualisation());

					if (Double.valueOf(this.salaireDeBaseAnneePrecedente) != 0
							&& (Double.valueOf(this.salaireDeBase) != 0 || Double
									.valueOf(this.salaireDeBaseReact) != 0)) {
						if (Double.valueOf(this.salaireDeBaseReact) == 0) {
							this.salaireDeBaseEvol = df
									.format(((((Double
											.valueOf(this.salaireDeBase) - Double
											.valueOf(this.salaireDeBaseAnneePrecedente)) / Double
											.valueOf(this.salaireDeBaseAnneePrecedente)) * 100) * 100) / 100);
						} else {
							this.salaireDeBaseEvol = df
									.format(((((Double
											.valueOf(this.salaireDeBaseReact) - Double
											.valueOf(this.salaireDeBaseAnneePrecedente)) / Double
											.valueOf(this.salaireDeBaseAnneePrecedente)) * 100) * 100) / 100);
						}
					}
					this.salaireDeBaseComm = remuActuelle
							.getSalaireDeBaseCommentaire();

					this.augmentationCollective = df.format(remuActuelle
							.getAugmentationCollective());
					this.augmentationCollectiveReact = df.format(remuActuelle
							.getAugmentationCollectiveActualisation());
					if (Double
							.valueOf(this.augmentationCollectiveAnneePrecedente) != 0
							&& (Double.valueOf(this.augmentationCollective) != 0 || Double
									.valueOf(this.augmentationCollectiveReact) != 0)) {
						if (Double.valueOf(this.augmentationCollectiveReact) == 0) {
							this.augmentationCollectiveEvol = df
									.format(((((Double
											.valueOf(this.augmentationCollective) - Double
											.valueOf(this.augmentationCollectiveAnneePrecedente)) / Double
											.valueOf(this.augmentationCollectiveAnneePrecedente)) * 100) * 100) / 100);
						} else {
							this.augmentationCollectiveEvol = df
									.format(((((Double
											.valueOf(this.augmentationCollectiveReact) - Double
											.valueOf(this.augmentationCollectiveAnneePrecedente)) / Double
											.valueOf(this.augmentationCollectiveAnneePrecedente)) * 100) * 100) / 100);
						}
					}
					this.augmentationCollectiveComm = remuActuelle
							.getAugmentationCollectiveCommentaire();

					this.augmentationIndividuelle = df.format(remuActuelle
							.getAugmentationIndividuelle());
					this.augmentationIndividuelleReact = df.format(remuActuelle
							.getAugmentationIndividuelleActualisation());
					if (Double
							.valueOf(this.augmentationIndividuelleAnneePrecedente) != 0
							&& (Double.valueOf(this.augmentationIndividuelle) != 0 || Double
									.valueOf(this.augmentationIndividuelleReact) != 0)) {
						if (Double.valueOf(this.augmentationIndividuelleReact) == 0) {
							this.augmentationIndividuelleEvol = df
									.format(((((Double
											.valueOf(this.augmentationIndividuelle) - Double
											.valueOf(this.augmentationIndividuelleAnneePrecedente)) / Double
											.valueOf(this.augmentationIndividuelleAnneePrecedente)) * 100) * 100) / 100);
						} else {
							this.augmentationIndividuelleEvol = df
									.format(((((Double
											.valueOf(this.augmentationIndividuelleReact) - Double
											.valueOf(this.augmentationIndividuelleAnneePrecedente)) / Double
											.valueOf(this.augmentationIndividuelleAnneePrecedente)) * 100) * 100) / 100);
						}
					}

					this.augmentationIndividuelleComm = remuActuelle
							.getAugmentationIndividuelleCommentaire();

					this.heuresSup25 = df.format(remuActuelle.getHeuresSup25());
					this.heuresSup25React = df.format(remuActuelle
							.getHeuresSup25Actualisation());
					if (Double.valueOf(this.heuresSup25AnneePrecedente) != 0
							&& (Double.valueOf(this.heuresSup25) != 0 || Double
									.valueOf(this.heuresSup25React) != 0)) {
						if (Double.valueOf(this.heuresSup25React) == 0) {
							this.heuresSup25Evol = df
									.format(((((Double
											.valueOf(this.heuresSup25) - Double
											.valueOf(this.heuresSup25AnneePrecedente)) / Double
											.valueOf(this.heuresSup25AnneePrecedente)) * 100) * 100) / 100);
						} else {
							this.heuresSup25Evol = df
									.format(((((Double
											.valueOf(this.heuresSup25React) - Double
											.valueOf(this.heuresSup25AnneePrecedente)) / Double
											.valueOf(this.heuresSup25AnneePrecedente)) * 100) * 100) / 100);
						}
					}
					this.heuresSup25Comm = remuActuelle
							.getHeuresSup25Commentaire();

					this.heuresSup50 = df.format(remuActuelle.getHeuresSup50());
					this.heuresSup50React = df.format(remuActuelle
							.getHeuresSup50Actualisation());
					if (Double.valueOf(this.heuresSup50AnneePrecedente) != 0
							&& (Double.valueOf(this.heuresSup50) != 0 || Double
									.valueOf(this.heuresSup50React) != 0)) {
						if (Double.valueOf(this.heuresSup50React) == 0) {
							this.heuresSup50Evol = df
									.format(((((Double
											.valueOf(this.heuresSup50) - Double
											.valueOf(this.heuresSup50AnneePrecedente)) / Double
											.valueOf(this.heuresSup50AnneePrecedente)) * 100) * 100) / 100);
						} else {
							this.heuresSup50Evol = df
									.format(((((Double
											.valueOf(this.heuresSup50React) - Double
											.valueOf(this.heuresSup50AnneePrecedente)) / Double
											.valueOf(this.heuresSup50AnneePrecedente)) * 100) * 100) / 100);
						}
					}

					this.heuresSup50Comm = remuActuelle
							.getHeuresSup50Commentaire();

					double somme = Double.valueOf(this.salaireDeBase)
							+ Double.valueOf(this.augmentationCollective)
							+ Double.valueOf(this.augmentationIndividuelle)
							+ Double.valueOf(this.heuresSup25)
							+ Double.valueOf(this.heuresSup50);
					this.totalBrutAnnuel = df.format(somme);

					double s1 = 0.0;
					double s2 = 0.0;
					double s3 = 0.0;
					double s4 = 0.0;
					double s5 = 0.0;
					if (Double.valueOf(this.salaireDeBaseReact) == 0.0) {
						s1 = Double.valueOf(this.salaireDeBase);
					} else {
						s1 = Double.valueOf(this.salaireDeBaseReact);
					}

					if (Double.valueOf(this.augmentationCollectiveReact) == 0.0) {
						s2 = Double.valueOf(this.augmentationCollective);
					} else {
						s2 = Double.valueOf(this.augmentationCollectiveReact);
					}

					if (Double.valueOf(this.augmentationIndividuelleReact) == 0.0) {
						s3 = Double.valueOf(this.augmentationIndividuelle);
					} else {
						s3 = Double.valueOf(this.augmentationIndividuelleReact);
					}

					if (Double.valueOf(this.heuresSup25React) == 0.0) {
						s4 = Double.valueOf(this.heuresSup25);
					} else {
						s4 = Double.valueOf(this.heuresSup25React);
					}

					if (Double.valueOf(this.heuresSup50React) == 0.0) {
						s5 = Double.valueOf(this.heuresSup50);
					} else {
						s5 = Double.valueOf(this.heuresSup50React);
					}

					double sommeReact = s1 + s2 + s3 + s4 + s5;
					this.totalBrutAnnuelReact = df.format(sommeReact);

					if (Double.valueOf(this.totalBrutAnnuelAnneePrecedente) != 0
							&& (Double.valueOf(this.totalBrutAnnuel) != 0 || Double
									.valueOf(this.totalBrutAnnuelReact) != 0)) {
						if (Double.valueOf(this.totalBrutAnnuelReact) == 0) {
							double totalBrutEvol = ((Double
									.valueOf(this.totalBrutAnnuel) - Double
									.valueOf(this.totalBrutAnnuelAnneePrecedente)) / Double
									.valueOf(this.totalBrutAnnuelAnneePrecedente)) * 100;
							double total = (double) ((totalBrutEvol * 100)) / 100;
							this.totalBrutAnnuelEvol = df.format(Double
									.valueOf(total));
						} else {
							double totalBrutEvol = ((Double
									.valueOf(this.totalBrutAnnuelReact) - Double
									.valueOf(this.totalBrutAnnuelAnneePrecedente)) / Double
									.valueOf(this.totalBrutAnnuelAnneePrecedente)) * 100;
							double total = (double) ((totalBrutEvol * 100)) / 100;
							this.totalBrutAnnuelEvol = df.format(Double
									.valueOf(total));
						}
					}

					LienRemunerationRevenuServiceImpl serviceLien = new LienRemunerationRevenuServiceImpl();
					List<LienRemunerationRevenuBean> l = new ArrayList<LienRemunerationRevenuBean>();
					l = serviceLien
							.getLienRemunerationRevenuFromIdRemu(remuActuelle
									.getIdRemuneration());

					RevenuComplementaireServiceImpl serviceRevCom = new RevenuComplementaireServiceImpl();

					LienRemunerationRevenuServiceImpl lien = new LienRemunerationRevenuServiceImpl();
					RemunerationServiceImpl remu = new RemunerationServiceImpl();
					RevenuComplementaireServiceImpl servrc = new RevenuComplementaireServiceImpl();

					for (LienRemunerationRevenuBean lrr : l) {

						RevenuComplementaireBean rev = new RevenuComplementaireBean();

						rev = servrc.getRevenuComplementaireBeanById(lrr
								.getIdRevenuComplementaire());

						lrr.setLibelle(rev.getLibelle());

						RemunerationBean rr = new RemunerationBean();
						for (RemunerationBean r : remu
								.getRemunerationList(Integer.parseInt(session
										.getAttribute("groupe").toString()))) {
							if (r.getIdRemuneration().equals(
									lrr.getIdRemuneration())) {
								rr = r;
							}
						}

						int sal = rr.getIdSalarie();

						int idRemu = 0;

						for (RemunerationBean r : remu
								.getRemunerationList(Integer.parseInt(session
										.getAttribute("groupe").toString()))) {
							if (r.getAnnee() == (rr.getAnnee() - 1)
									&& r.getIdSalarie().equals(sal)) {
								idRemu = r.getIdRemuneration();
							}
						}

						if (idRemu == 0) {
							lrr.setMontantNPrec("0.00");
						} else {
							String montant = df.format(Double.valueOf(lien
									.getMontantNPrec(idRemu,
											lrr.getIdRevenuComplementaire())));
							lrr.setMontantNPrec(montant);
						}
						String montant = lrr.getMontant();
						lrr.setMontant(df.format(Double.valueOf(montant)));

						String actu = lrr.getActualisation();
						lrr.setActualisation(df.format(Double.valueOf(actu)));

						if (serviceRevCom.getType(
								lrr.getIdRevenuComplementaire(),
								Integer.parseInt(session.getAttribute("groupe")
										.toString())).equals("commission")) {
							lrr.setMontantNPrec(calculNPrec("commission",
									lrr.getLibelle()));
							if (Double.valueOf(lrr.getActualisation()) == 0.0) {
								lrr.setActualisation(lrr.getMontant());
							}
							this.commissionInventoryLien.add(lrr);
							for (SelectItem s : this.commissionList) {
								if (s.getLabel().equals(lrr.getLibelle())) {
									this.commissionList.remove(s);
									break;
								}
							}
							if (Double.valueOf(lrr.getActualisation()) == 0.0) {
								this.totalActuCommission = df.format(Double
										.valueOf(this.totalActuCommission)
										+ Double.valueOf(lrr.getMontant()));
							} else {
								this.totalActuCommission = df
										.format(Double
												.valueOf(this.totalActuCommission)
												+ Double.valueOf(lrr
														.getActualisation()));
							}
							this.totalNCommission = df.format(Double
									.valueOf(this.totalNCommission)
									+ Double.valueOf(lrr.getMontant()));
						}
						if (serviceRevCom.getType(
								lrr.getIdRevenuComplementaire(),
								Integer.parseInt(session.getAttribute("groupe")
										.toString())).equals("prime_fixe")) {
							lrr.setMontantNPrec(calculNPrec("prime_fixe",
									lrr.getLibelle()));
							this.primeFixeInventoryLien.add(lrr);
							for (SelectItem s : this.primeFixeList) {
								if (s.getLabel().equals(lrr.getLibelle())) {
									this.primeFixeList.remove(s);
									break;
								}
							}
							if (Double.valueOf(lrr.getActualisation()) != 0.0) {
								this.totalActuPrimeFixe = df
										.format(Double
												.valueOf(this.totalActuPrimeFixe)
												+ Double.valueOf(lrr
														.getActualisation()));
							} else {
								this.totalActuPrimeFixe = df.format(Double
										.valueOf(this.totalActuPrimeFixe)
										+ Double.valueOf(lrr.getMontant()));
							}
							this.totalNPrimeFixe = df.format(Double
									.valueOf(this.totalNPrimeFixe)
									+ Double.valueOf(lrr.getMontant()));
						}
						if (serviceRevCom.getType(
								lrr.getIdRevenuComplementaire(),
								Integer.parseInt(session.getAttribute("groupe")
										.toString())).equals("prime_variable")) {
							lrr.setMontantNPrec(calculNPrec("prime_variable",
									lrr.getLibelle()));
							this.primeVariableInventoryLien.add(lrr);
							for (SelectItem s : this.primeVariableList) {
								if (s.getLabel().equals(lrr.getLibelle())) {
									this.primeVariableList.remove(s);
									break;
								}
							}
							if (Double.valueOf(lrr.getActualisation()) != 0.0) {
								this.totalActuPrimeVariable = df
										.format(Double
												.valueOf(this.totalActuPrimeVariable)
												+ Double.valueOf(lrr
														.getActualisation()));
							} else {
								this.totalActuPrimeVariable = df.format(Double
										.valueOf(this.totalActuPrimeVariable)
										+ Double.valueOf(lrr.getMontant()));
							}
							this.totalNPrimeVariable = df.format(Double
									.valueOf(this.totalNPrimeVariable)
									+ Double.valueOf(lrr.getMontant()));
						}
						if (serviceRevCom.getType(
								lrr.getIdRevenuComplementaire(),
								Integer.parseInt(session.getAttribute("groupe")
										.toString())).equals(
								"avantage_assujetti")) {
							lrr.setMontantNPrec(calculNPrec(
									"avantage_assujetti", lrr.getLibelle()));
							this.avantageAssujettiInventoryLien.add(lrr);
							for (SelectItem s : this.avantageAssujettiList) {
								if (s.getLabel().equals(lrr.getLibelle())) {
									this.avantageAssujettiList.remove(s);
									break;
								}
							}
							if (Double.valueOf(lrr.getActualisation()) != 0.0) {
								this.totalActuAvantageAssujetti = df
										.format(Double
												.valueOf(this.totalActuAvantageAssujetti)
												+ Double.valueOf(lrr
														.getActualisation()));
							} else {
								this.totalActuAvantageAssujetti = df
										.format(Double
												.valueOf(this.totalActuAvantageAssujetti)
												+ Double.valueOf(lrr
														.getMontant()));
							}
							this.totalNAvantageAssujetti = df.format(Double
									.valueOf(this.totalNAvantageAssujetti)
									+ Double.valueOf(lrr.getMontant()));
						}
						if (serviceRevCom.getType(
								lrr.getIdRevenuComplementaire(),
								Integer.parseInt(session.getAttribute("groupe")
										.toString())).equals(
								"avantage_non_assujetti")) {
							lrr.setMontantNPrec(calculNPrec(
									"avantage_non_assujetti", lrr.getLibelle()));
							this.avantageNonAssujettiInventoryLien.add(lrr);
							for (SelectItem s : this.avantageNonAssujettiList) {
								if (s.getLabel().equals(lrr.getLibelle())) {
									this.avantageNonAssujettiList.remove(s);
									break;
								}
							}
							if (Double.valueOf(lrr.getActualisation()) != 0.0) {
								this.totalActuAvantageNonAssujetti = df
										.format(Double
												.valueOf(this.totalActuAvantageNonAssujetti)
												+ Double.valueOf(lrr
														.getActualisation()));
							} else {
								this.totalActuAvantageNonAssujetti = df
										.format(Double
												.valueOf(this.totalActuAvantageNonAssujetti)
												+ Double.valueOf(lrr
														.getMontant()));
							}
							this.totalNAvantageNonAssujetti = df.format(Double
									.valueOf(this.totalNAvantageNonAssujetti)
									+ Double.valueOf(lrr.getMontant()));
						}
						if (serviceRevCom.getType(
								lrr.getIdRevenuComplementaire(),
								Integer.parseInt(session.getAttribute("groupe")
										.toString())).equals(
								"frais_professionnel")) {
							lrr.setMontantNPrec(calculNPrec(
									"frais_professionnel", lrr.getLibelle()));
							this.fraisProfInventoryLien.add(lrr);
							for (SelectItem s : this.fraisProfessionnelList) {
								if (s.getLabel().equals(lrr.getLibelle())) {
									this.fraisProfessionnelList.remove(s);
									break;
								}
							}
							this.totalNFraisProf = df.format(Double
									.valueOf(this.totalNFraisProf)
									+ Double.valueOf(lrr.getMontant()));
						}
					}

					if (this.totalBrutAnnuel.contains(",")) {
						this.totalBrutAnnuel = this.totalBrutAnnuel.replace(
								",", ".");
					}
					this.remunerationBruteAnnuelle = df.format(Double
							.valueOf(this.totalBrutAnnuel)
							+ Double.valueOf(this.totalNCommission)
							+ Double.valueOf(this.totalNPrimeFixe)
							+ Double.valueOf(this.totalNPrimeVariable)
							+ Double.valueOf(this.totalNAvantageAssujetti));

					this.remunerationBruteAnnuelleNPrec = df.format(Double
							.valueOf(this.totalBrutAnnuelAnneePrecedente)
							+ Double.valueOf(this.totalNPrecCommission)
							+ Double.valueOf(this.totalNPrecPrimeFixe)
							+ Double.valueOf(this.totalNPrecPrimeVariable)
							+ Double.valueOf(this.totalNPrecAvantageAssujetti));

					this.remunerationBruteAnnuelleActu = df.format(Double
							.valueOf(this.totalBrutAnnuelReact)
							+ Double.valueOf(this.totalActuCommission)
							+ Double.valueOf(this.totalActuPrimeFixe)
							+ Double.valueOf(this.totalActuPrimeVariable)
							+ Double.valueOf(this.totalActuAvantageAssujetti));

					if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
							&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
									.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
						if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
							double totalBrutEvol = ((Double
									.valueOf(this.remunerationBruteAnnuelle) - Double
									.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
									.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
							double total = (double) ((totalBrutEvol * 100)) / 100;
							this.remunerationBruteAnnuelleEvol = df
									.format(Double.valueOf(total));
						} else {
							double totalBrutEvol = ((Double
									.valueOf(this.remunerationBruteAnnuelleActu) - Double
									.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
									.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
							double total = (double) ((totalBrutEvol * 100)) / 100;
							this.remunerationBruteAnnuelleEvol = df
									.format(Double.valueOf(total));
						}
					}

					this.remunerationGlobale = df.format(Double
							.valueOf(this.remunerationBruteAnnuelle)
							+ Double.valueOf(this.totalNAvantageNonAssujetti));

					this.remunerationGlobaleNPrec = df
							.format(Double
									.valueOf(this.remunerationBruteAnnuelleNPrec)
									+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

					this.remunerationGlobaleActu = df
							.format(Double
									.valueOf(this.remunerationBruteAnnuelleActu)
									+ Double.valueOf(this.totalActuAvantageNonAssujetti));

					if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
							&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
									.valueOf(this.remunerationGlobaleActu) != 0)) {
						if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
							double totalBrutEvol = ((Double
									.valueOf(this.remunerationGlobale) - Double
									.valueOf(this.remunerationGlobaleNPrec)) / Double
									.valueOf(this.remunerationGlobaleNPrec)) * 100;
							double total = (double) ((totalBrutEvol * 100)) / 100;
							this.remunerationGlobaleEvol = df.format(Double
									.valueOf(total));
						} else {
							double totalBrutEvol = ((Double
									.valueOf(this.remunerationGlobaleActu) - Double
									.valueOf(this.remunerationGlobaleNPrec)) / Double
									.valueOf(this.remunerationGlobaleNPrec)) * 100;
							double total = (double) ((totalBrutEvol * 100)) / 100;
							this.remunerationGlobaleEvol = df.format(Double
									.valueOf(total));
						}
					}

				} else {
					this.totalNCommission = "0";
					this.totalActuCommission = "0";

					this.totalNPrimeFixe = "0";
					this.totalActuPrimeFixe = "0";

					this.totalNPrimeVariable = "0";
					this.totalActuPrimeVariable = "0";

					this.totalNAvantageAssujetti = "0";
					this.totalActuAvantageAssujetti = "0";

					this.totalNAvantageNonAssujetti = "0";
					this.totalActuAvantageNonAssujetti = "0";

					this.totalNFraisProf = "0";

				}

				List<RemunerationBean> rbi = service.getRemuneration(
						this.annee - 1, this.idSalarie);
				if (!rbi.isEmpty()) {
					RemunerationBean actu = rbi.get(0);

					List<LienRemunerationRevenuBean> l = new ArrayList<LienRemunerationRevenuBean>();
					LienRemunerationRevenuServiceImpl ls = new LienRemunerationRevenuServiceImpl();
					RevenuComplementaireServiceImpl rcs = new RevenuComplementaireServiceImpl();
					l = ls.getLienRemunerationRevenuFromIdRemu(actu
							.getIdRemuneration());
					for (LienRemunerationRevenuBean lrr : l) {
						if (rcs.getType(
								lrr.getIdRevenuComplementaire(),
								Integer.parseInt(session.getAttribute("groupe")
										.toString())).equals("commission")) {
							if (Double.valueOf(lrr.getMontant()) != 0
									|| Double.valueOf(lrr.getActualisation()) != 0) {
								LienRemunerationRevenuBean lien = new LienRemunerationRevenuBean();
								lien.setIdRevenuComplementaire(lrr
										.getIdRevenuComplementaire());
								lien.setIdLienRemunerationRevenuComplementaire(0);
								lien.setCommentaire("");
								lien.setLibelle(rcs
										.getRevenuComplementaireBeanById(
												lrr.getIdRevenuComplementaire())
										.getLibelle());
								if (Double.valueOf(lrr.getActualisation()) != 0) {
									lien.setMontantNPrec(df.format(Double
											.valueOf(lrr.getActualisation())));
								} else {
									lien.setMontantNPrec(df.format(Double
											.valueOf(lrr.getMontant())));
								}
								lien.setMontant("0.00");
								lien.setActualisation("0.00");
								lien.setRemonteNPrec(true);
								boolean contain = false;
								for (LienRemunerationRevenuBean lrb : this.commissionInventoryLien) {
									if (lien.getLibelle()
											.equals(rcs
													.getRevenuComplementaireBeanById(
															lrb.getIdRevenuComplementaire())
													.getLibelle())) {
										contain = true;
										break;
									}
								}
								if (!contain) {
									this.commissionInventoryLien.add(lien);
									for (SelectItem s : this.commissionList) {
										if (s.getLabel()
												.equals(rcs
														.getRevenuComplementaireBeanById(
																lrr.getIdRevenuComplementaire())
														.getLibelle())) {
											this.commissionList.remove(s);
											break;
										}
									}
								}
							}
							if (Double.valueOf(lrr.getActualisation()) == 0.0) {
								this.totalNPrecCommission = df.format(Double
										.valueOf(this.totalNPrecCommission)
										+ Double.valueOf(lrr.getMontant()));
							} else {
								this.totalNPrecCommission = df
										.format(Double
												.valueOf(this.totalNPrecCommission)
												+ Double.valueOf(lrr
														.getActualisation()));
							}
						}
						if (rcs.getType(
								lrr.getIdRevenuComplementaire(),
								Integer.parseInt(session.getAttribute("groupe")
										.toString())).equals("prime_fixe")) {
							if (Double.valueOf(lrr.getMontant()) != 0
									|| Double.valueOf(lrr.getActualisation()) != 0) {
								LienRemunerationRevenuBean lien = new LienRemunerationRevenuBean();
								lien.setIdRevenuComplementaire(lrr
										.getIdRevenuComplementaire());
								lien.setIdLienRemunerationRevenuComplementaire(0);
								lien.setLibelle(rcs
										.getRevenuComplementaireBeanById(
												lrr.getIdRevenuComplementaire())
										.getLibelle());
								if (Double.valueOf(lrr.getActualisation()) != 0) {
									lien.setMontantNPrec(df.format(Double
											.valueOf(lrr.getActualisation())));
								} else {
									lien.setMontantNPrec(df.format(Double
											.valueOf(lrr.getMontant())));
								}
								lien.setMontant("0.00");
								lien.setActualisation("0.00");
								lien.setCommentaire("");
								lien.setRemonteNPrec(true);
								boolean contain = false;
								for (LienRemunerationRevenuBean lrb : this.primeFixeInventoryLien) {
									if (lien.getLibelle()
											.equals(rcs
													.getRevenuComplementaireBeanById(
															lrb.getIdRevenuComplementaire())
													.getLibelle())) {
										contain = true;
										break;
									}
								}
								if (!contain) {
									this.primeFixeInventoryLien.add(lien);
									for (SelectItem s : this.primeFixeList) {
										if (s.getLabel()
												.equals(rcs
														.getRevenuComplementaireBeanById(
																lrr.getIdRevenuComplementaire())
														.getLibelle())) {
											this.primeFixeList.remove(s);
											break;
										}
									}
								}
							}
							if (Double.valueOf(lrr.getActualisation()) == 0.0) {
								this.totalNPrecPrimeFixe = df.format(Double
										.valueOf(this.totalNPrecPrimeFixe)
										+ Double.valueOf(lrr.getMontant()));
							} else {
								this.totalNPrecPrimeFixe = df
										.format(Double
												.valueOf(this.totalNPrecPrimeFixe)
												+ Double.valueOf(lrr
														.getActualisation()));
							}
						}
						if (rcs.getType(
								lrr.getIdRevenuComplementaire(),
								Integer.parseInt(session.getAttribute("groupe")
										.toString())).equals("prime_variable")) {
							if (Double.valueOf(lrr.getMontant()) != 0
									|| Double.valueOf(lrr.getActualisation()) != 0) {
								LienRemunerationRevenuBean lien = new LienRemunerationRevenuBean();
								lien.setIdRevenuComplementaire(lrr
										.getIdRevenuComplementaire());
								lien.setIdLienRemunerationRevenuComplementaire(0);
								lien.setCommentaire("");
								lien.setLibelle(rcs
										.getRevenuComplementaireBeanById(
												lrr.getIdRevenuComplementaire())
										.getLibelle());
								if (Double.valueOf(lrr.getActualisation()) != 0) {
									lien.setMontantNPrec(df.format(Double
											.valueOf(lrr.getActualisation())));
								} else {
									lien.setMontantNPrec(df.format(Double
											.valueOf(lrr.getMontant())));
								}
								lien.setMontant("0.00");
								lien.setActualisation("0.00");
								lien.setRemonteNPrec(true);
								boolean contain = false;
								for (LienRemunerationRevenuBean lrb : this.primeVariableInventoryLien) {
									if (lien.getLibelle()
											.equals(rcs
													.getRevenuComplementaireBeanById(
															lrb.getIdRevenuComplementaire())
													.getLibelle())) {
										contain = true;
										break;
									}
								}
								if (!contain) {
									this.primeVariableInventoryLien.add(lien);
									for (SelectItem s : this.primeVariableList) {
										if (s.getLabel()
												.equals(rcs
														.getRevenuComplementaireBeanById(
																lrr.getIdRevenuComplementaire())
														.getLibelle())) {
											this.primeVariableList.remove(s);
											break;
										}
									}
								}
							}
							if (Double.valueOf(lrr.getActualisation()) == 0.0) {
								this.totalNPrecPrimeVariable = df.format(Double
										.valueOf(this.totalNPrecPrimeVariable)
										+ Double.valueOf(lrr.getMontant()));
							} else {
								this.totalNPrecPrimeVariable = df
										.format(Double
												.valueOf(this.totalNPrecPrimeVariable)
												+ Double.valueOf(lrr
														.getActualisation()));
							}
						}
						if (rcs.getType(
								lrr.getIdRevenuComplementaire(),
								Integer.parseInt(session.getAttribute("groupe")
										.toString())).equals(
								"avantage_assujetti")) {
							if (Double.valueOf(lrr.getMontant()) != 0
									|| Double.valueOf(lrr.getActualisation()) != 0) {
								LienRemunerationRevenuBean lien = new LienRemunerationRevenuBean();
								lien.setIdRevenuComplementaire(lrr
										.getIdRevenuComplementaire());
								lien.setIdLienRemunerationRevenuComplementaire(0);
								lien.setCommentaire("");
								lien.setLibelle(rcs
										.getRevenuComplementaireBeanById(
												lrr.getIdRevenuComplementaire())
										.getLibelle());
								if (Double.valueOf(lrr.getActualisation()) != 0) {
									lien.setMontantNPrec(df.format(Double
											.valueOf(lrr.getActualisation())));
								} else {
									lien.setMontantNPrec(df.format(Double
											.valueOf(lrr.getMontant())));
								}
								lien.setMontant("0.00");
								lien.setActualisation("0.00");
								lien.setRemonteNPrec(true);
								boolean contain = false;
								for (LienRemunerationRevenuBean lrb : this.avantageAssujettiInventoryLien) {
									if (lien.getLibelle()
											.equals(rcs
													.getRevenuComplementaireBeanById(
															lrb.getIdRevenuComplementaire())
													.getLibelle())) {
										contain = true;
										break;
									}
								}
								if (!contain) {
									this.avantageAssujettiInventoryLien
											.add(lien);
									for (SelectItem s : this.avantageAssujettiList) {
										if (s.getLabel()
												.equals(rcs
														.getRevenuComplementaireBeanById(
																lrr.getIdRevenuComplementaire())
														.getLibelle())) {
											this.avantageAssujettiList
													.remove(s);
											break;
										}
									}
								}
							}
							if (Double.valueOf(lrr.getActualisation()) == 0.0) {
								this.totalNPrecAvantageAssujetti = df
										.format(Double
												.valueOf(this.totalNPrecAvantageAssujetti)
												+ Double.valueOf(lrr
														.getMontant()));
							} else {
								this.totalNPrecAvantageAssujetti = df
										.format(Double
												.valueOf(this.totalNPrecAvantageAssujetti)
												+ Double.valueOf(lrr
														.getActualisation()));
							}
						}
						if (rcs.getType(
								lrr.getIdRevenuComplementaire(),
								Integer.parseInt(session.getAttribute("groupe")
										.toString())).equals(
								"avantage_non_assujetti")) {
							if (Double.valueOf(lrr.getMontant()) != 0
									|| Double.valueOf(lrr.getActualisation()) != 0) {
								LienRemunerationRevenuBean lien = new LienRemunerationRevenuBean();
								lien.setIdRevenuComplementaire(lrr
										.getIdRevenuComplementaire());
								lien.setIdLienRemunerationRevenuComplementaire(0);
								lien.setCommentaire("");
								lien.setLibelle(rcs
										.getRevenuComplementaireBeanById(
												lrr.getIdRevenuComplementaire())
										.getLibelle());
								if (Double.valueOf(lrr.getActualisation()) != 0) {
									lien.setMontantNPrec(df.format(Double
											.valueOf(lrr.getActualisation())));
								} else {
									lien.setMontantNPrec(df.format(Double
											.valueOf(lrr.getMontant())));
								}
								lien.setMontant("0.00");
								lien.setActualisation("0.00");
								lien.setRemonteNPrec(true);
								boolean contain = false;
								for (LienRemunerationRevenuBean lrb : this.avantageNonAssujettiInventoryLien) {
									if (lien.getLibelle()
											.equals(rcs
													.getRevenuComplementaireBeanById(
															lrb.getIdRevenuComplementaire())
													.getLibelle())) {
										contain = true;
										break;
									}
								}
								if (!contain) {
									this.avantageNonAssujettiInventoryLien
											.add(lien);
									for (SelectItem s : this.avantageNonAssujettiList) {
										if (s.getLabel()
												.equals(rcs
														.getRevenuComplementaireBeanById(
																lrr.getIdRevenuComplementaire())
														.getLibelle())) {
											this.avantageNonAssujettiList
													.remove(s);
											break;
										}
									}
								}
							}
							if (Double.valueOf(lrr.getActualisation()) == 0.0) {
								this.totalNPrecAvantageNonAssujetti = df
										.format(Double
												.valueOf(this.totalNPrecAvantageNonAssujetti)
												+ Double.valueOf(lrr
														.getMontant()));
							} else {
								this.totalNPrecAvantageNonAssujetti = df
										.format(Double
												.valueOf(this.totalNPrecAvantageNonAssujetti)
												+ Double.valueOf(lrr
														.getActualisation()));
							}
						}
						if (rcs.getType(
								lrr.getIdRevenuComplementaire(),
								Integer.parseInt(session.getAttribute("groupe")
										.toString())).equals(
								"frais_professionnel")) {
							if (Double.valueOf(lrr.getMontant()) != 0) {
								LienRemunerationRevenuBean lien = new LienRemunerationRevenuBean();
								lien.setIdRevenuComplementaire(lrr
										.getIdRevenuComplementaire());
								lien.setIdLienRemunerationRevenuComplementaire(0);
								lien.setCommentaire("");
								lien.setLibelle(rcs
										.getRevenuComplementaireBeanById(
												lrr.getIdRevenuComplementaire())
										.getLibelle());
								lien.setMontantNPrec(df.format(Double
										.valueOf(lrr.getMontant())));
								lien.setMontant("0.00");
								lien.setRemonteNPrec(true);
								boolean contain = false;
								for (LienRemunerationRevenuBean lrb : this.fraisProfInventoryLien) {
									if (lien.getLibelle()
											.equals(rcs
													.getRevenuComplementaireBeanById(
															lrb.getIdRevenuComplementaire())
													.getLibelle())) {
										contain = true;
										break;
									}
								}
								if (!contain) {
									this.fraisProfInventoryLien.add(lien);
									for (SelectItem s : this.fraisProfessionnelList) {
										if (s.getLabel()
												.equals(rcs
														.getRevenuComplementaireBeanById(
																lrr.getIdRevenuComplementaire())
														.getLibelle())) {
											this.fraisProfessionnelList
													.remove(s);
											break;
										}
									}
								}
							}
							this.totalNPrecFraisProf = df.format(Double
									.valueOf(this.totalNPrecFraisProf)
									+ Double.valueOf(lrr.getMontant()));
						}
					}
				}
				this.remunerationBruteAnnuelleNPrec = df.format(Double
						.valueOf(this.totalBrutAnnuelAnneePrecedente)
						+ Double.valueOf(this.totalNPrecCommission)
						+ Double.valueOf(this.totalNPrecPrimeFixe)
						+ Double.valueOf(this.totalNPrecPrimeVariable)
						+ Double.valueOf(this.totalNPrecAvantageAssujetti));

				this.remunerationBruteAnnuelleActu = df.format(Double
						.valueOf(this.totalBrutAnnuelReact)
						+ Double.valueOf(this.totalActuCommission)
						+ Double.valueOf(this.totalActuPrimeFixe)
						+ Double.valueOf(this.totalActuPrimeVariable)
						+ Double.valueOf(this.totalActuAvantageAssujetti));

				this.remunerationGlobaleNPrec = df.format(Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)
						+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

				this.remunerationGlobaleActu = df.format(Double
						.valueOf(this.remunerationBruteAnnuelleActu)
						+ Double.valueOf(this.totalActuAvantageNonAssujetti));

				changeZeroToNull();
			}
		}
	}

	public String checkStringDouble(String s) {
		Double d = 0.0;
		if (!s.equals("")) {
			try {
				d = Double.valueOf(s);
				if (d == 0.0) {
					return "";
				}
			} catch (Exception e) {
				return "";
			}
		}
		return s;
	}

	public String checkStringDoubleTiret(String s) {
		Double d = 0.0;
		if (!s.equals("")) {
			try {
				d = Double.valueOf(s);
				if (d == 0.0) {
					return "-";
				}
			} catch (Exception e) {
				return "-";
			}
		}
		return s;
	}

	public void changeZeroToNull() {
		this.taux = checkStringDouble(taux);
		this.salaireBrut = checkStringDouble(salaireBrut);
		this.salaireConvBrut = checkStringDouble(salaireConvBrut);
		this.salaireConventionelMinimum = checkStringDouble(salaireConventionelMinimum);
		this.salaireConventionelMinimumReact = checkStringDouble(salaireConventionelMinimumReact);

		this.salaireConventionelMinimumEvol = checkStringDouble(salaireConventionelMinimumEvol);

		this.totalBrutAnnuelEvol = checkStringDouble(totalBrutAnnuelEvol);

		this.salaireConventionelMinimumAnneePrecedente = checkStringDoubleTiret(salaireConventionelMinimumAnneePrecedente);
		this.salaireDeBase = checkStringDouble(salaireDeBase);
		this.salaireDeBaseReact = checkStringDouble(salaireDeBaseReact);

		this.salaireDeBaseEvol = checkStringDouble(salaireDeBaseEvol);

		this.salaireDeBaseAnneePrecedente = checkStringDoubleTiret(salaireDeBaseAnneePrecedente);
		this.totalBrutAnnuelAnneePrecedente = checkStringDoubleTiret(totalBrutAnnuelAnneePrecedente);
		this.totalBrutAnnuel = checkStringDouble(totalBrutAnnuel);
		this.totalBrutAnnuelReact = checkStringDouble(totalBrutAnnuelReact);
		this.augmentationCollective = checkStringDouble(augmentationCollective);
		this.augmentationCollectiveReact = checkStringDouble(augmentationCollectiveReact);

		this.augmentationCollectiveEvol = checkStringDouble(augmentationCollectiveEvol);

		this.augmentationCollectiveAnneePrecedente = checkStringDoubleTiret(augmentationCollectiveAnneePrecedente);
		this.augmentationIndividuelle = checkStringDouble(augmentationIndividuelle);
		this.augmentationIndividuelleReact = checkStringDouble(augmentationIndividuelleReact);

		this.augmentationIndividuelleEvol = checkStringDouble(augmentationIndividuelleEvol);

		this.augmentationIndividuelleAnneePrecedente = checkStringDoubleTiret(augmentationIndividuelleAnneePrecedente);
		this.heuresSup25 = checkStringDouble(heuresSup25);
		this.heuresSup25React = checkStringDouble(heuresSup25React);

		this.heuresSup25Evol = checkStringDouble(heuresSup25Evol);

		this.heuresSup25AnneePrecedente = checkStringDoubleTiret(heuresSup25AnneePrecedente);
		this.heuresSup50 = checkStringDouble(heuresSup50);
		this.heuresSup50React = checkStringDouble(heuresSup50React);

		this.heuresSup50Evol = checkStringDouble(heuresSup50Evol);

		this.heuresSup50AnneePrecedente = checkStringDoubleTiret(heuresSup50AnneePrecedente);

		this.montantNCommission = checkStringDouble(montantNCommission);
		this.actuCommission = checkStringDouble(actuCommission);

		this.montantNPrimeFixe = checkStringDouble(montantNPrimeFixe);
		this.actuPrimeFixe = checkStringDouble(actuPrimeFixe);

		this.montantNPrimeVariable = checkStringDouble(montantNPrimeVariable);
		this.actuPrimeVariable = checkStringDouble(actuPrimeVariable);

		this.montantNAvantageAssujetti = checkStringDouble(montantNAvantageAssujetti);
		this.actuAvantageAssujetti = checkStringDouble(actuAvantageAssujetti);

		this.montantNAvantageNonAssujetti = checkStringDouble(montantNAvantageNonAssujetti);
		this.actuAvantageNonAssujetti = checkStringDouble(actuAvantageNonAssujetti);

		this.montantNFraisProf = checkStringDouble(montantNFraisProf);
	}

	public void changeNullToZero() {
		if (this.taux.equals("")) {
			this.taux = "0";
		}
		if (this.salaireBrut.equals("")) {
			this.salaireBrut = "0";
		}
		if (this.salaireConvBrut.equals("")) {
			this.salaireConvBrut = "0";
		}
		if (this.salaireConventionelMinimum.equals("")) {
			this.salaireConventionelMinimum = "0";
		}
		if (this.salaireConventionelMinimumReact.equals("")) {
			this.salaireConventionelMinimumReact = "0";
		}

		if (this.salaireConventionelMinimumEvol.equals("")) {
			this.salaireConventionelMinimumEvol = "0";
		}

		if (this.salaireConventionelMinimumAnneePrecedente.equals("-")
				|| this.salaireConventionelMinimumAnneePrecedente.equals("")) {
			this.salaireConventionelMinimumAnneePrecedente = "0";
		}
		if (this.salaireDeBase.equals("")) {
			this.salaireDeBase = "0";
		}
		if (this.salaireDeBaseReact.equals("")) {
			this.salaireDeBaseReact = "0";
		}

		if (this.salaireDeBaseEvol.equals("")) {
			this.salaireDeBaseEvol = "0";
		}
		if (this.totalBrutAnnuelEvol.equals("")) {
			this.totalBrutAnnuelEvol = "0";
		}

		if (this.salaireDeBaseAnneePrecedente.equals("-")
				|| this.salaireDeBaseAnneePrecedente.equals("")) {
			this.salaireDeBaseAnneePrecedente = "0";
		}
		if (this.totalBrutAnnuelAnneePrecedente.equals("-")
				|| this.totalBrutAnnuelAnneePrecedente.equals("")) {
			this.totalBrutAnnuelAnneePrecedente = "0";
		}
		if (this.totalBrutAnnuel.equals("")) {
			this.totalBrutAnnuel = "0";
		}
		if (this.totalBrutAnnuelReact.equals("")) {
			this.totalBrutAnnuelReact = "0";
		}
		if (this.augmentationCollective.equals("")) {
			this.augmentationCollective = "0";
		}
		if (this.augmentationCollectiveReact.equals("")) {
			this.augmentationCollectiveReact = "0";
		}

		if (this.augmentationCollectiveEvol.equals("")) {
			this.augmentationCollectiveEvol = "0";
		}

		if (this.augmentationCollectiveAnneePrecedente.equals("-")
				|| this.augmentationCollectiveAnneePrecedente.equals("")) {
			this.augmentationCollectiveAnneePrecedente = "0";
		}
		if (this.augmentationIndividuelle.equals("")) {
			this.augmentationIndividuelle = "0";
		}
		if (this.augmentationIndividuelleReact.equals("")) {
			this.augmentationIndividuelleReact = "0";
		}

		if (this.augmentationIndividuelleEvol.equals("")) {
			this.augmentationIndividuelleEvol = "0";
		}

		if (this.augmentationIndividuelleAnneePrecedente.equals("-")
				|| this.augmentationIndividuelleAnneePrecedente.equals("")) {
			this.augmentationIndividuelleAnneePrecedente = "0";
		}
		if (this.heuresSup25.equals("")) {
			this.heuresSup25 = "0";
		}
		if (this.heuresSup25React.equals("")) {
			this.heuresSup25React = "0";
		}

		if (this.heuresSup25Evol.equals("")) {
			this.heuresSup25Evol = "0";
		}

		if (this.heuresSup25AnneePrecedente.equals("-")
				|| this.heuresSup25AnneePrecedente.equals("")) {
			this.heuresSup25AnneePrecedente = "0";
		}
		if (this.heuresSup50.equals("")) {
			this.heuresSup50 = "0";
		}
		if (this.heuresSup50React.equals("")) {
			this.heuresSup50React = "0";
		}

		if (this.heuresSup50Evol.equals("")) {
			this.heuresSup50Evol = "0";
		}

		if (this.heuresSup50AnneePrecedente.equals("-")
				|| this.heuresSup50AnneePrecedente.equals("")) {
			this.heuresSup50AnneePrecedente = "0";
		}

		if (this.montantNCommission.equals("")) {
			this.montantNCommission = "0";
		}
		if (this.actuCommission.equals("")) {
			this.actuCommission = "0";
		}

		if (this.montantNPrimeFixe.equals("")) {
			this.montantNPrimeFixe = "0";
		}
		if (this.actuPrimeFixe.equals("")) {
			this.actuPrimeFixe = "0";
		}

		if (this.montantNPrimeVariable.equals("")) {
			this.montantNPrimeVariable = "0";
		}
		if (this.actuPrimeVariable.equals("")) {
			this.actuPrimeVariable = "0";
		}

		if (this.montantNAvantageAssujetti.equals("")) {
			this.montantNAvantageAssujetti = "0";
		}
		if (this.actuAvantageAssujetti.equals("")) {
			this.actuAvantageAssujetti = "0";
		}

		if (this.montantNAvantageNonAssujetti.equals("")) {
			this.montantNAvantageNonAssujetti = "0";
		}
		if (this.actuAvantageNonAssujetti.equals("")) {
			this.actuAvantageNonAssujetti = "0";
		}

		if (this.montantNFraisProf.equals("")) {
			this.montantNFraisProf = "0";
		}

	}

	public String saveOrUpdateSalarieRemuneration(ActionEvent e)
			throws Exception {

		changeNullToZero();

		if (this.taux.contains(",")) {
			this.taux = this.taux.replace(",", ".");
		}

		if (this.coefficient.contains(",")) {
			this.coefficient = this.coefficient.replace(",", ".");
		}

		if (this.salaireBrut.contains(",")) {
			this.salaireBrut = this.salaireBrut.replace(",", ".");
		}

		if (this.salaireConvBrut.contains(",")) {
			this.salaireConvBrut = this.salaireConvBrut.replace(",", ".");
		}

		if (this.salaireConventionelMinimum.contains(",")) {
			this.salaireConventionelMinimum = this.salaireConventionelMinimum
					.replace(",", ".");
		}

		if (this.salaireConventionelMinimumReact.contains(",")) {
			this.salaireConventionelMinimumReact = this.salaireConventionelMinimumReact
					.replace(",", ".");
		}

		if (this.salaireDeBase.contains(",")) {
			this.salaireDeBase = this.salaireDeBase.replace(",", ".");
		}

		if (this.salaireDeBaseReact.contains(",")) {
			this.salaireDeBaseReact = this.salaireDeBaseReact.replace(",", ".");
		}

		if (this.augmentationCollective.contains(",")) {
			this.augmentationCollective = this.augmentationCollective.replace(
					",", ".");
		}

		if (this.augmentationCollectiveReact.contains(",")) {
			this.augmentationCollectiveReact = this.augmentationCollectiveReact
					.replace(",", ".");
		}

		if (this.augmentationIndividuelle.contains(",")) {
			this.augmentationIndividuelle = this.augmentationIndividuelle
					.replace(",", ".");
		}

		if (this.augmentationIndividuelleReact.contains(",")) {
			this.augmentationIndividuelleReact = this.augmentationIndividuelleReact
					.replace(",", ".");
		}

		if (this.heuresSup25.contains(",")) {
			this.heuresSup25 = this.heuresSup25.replace(",", ".");
		}

		if (this.heuresSup25React.contains(",")) {
			this.heuresSup25React = this.heuresSup25React.replace(",", ".");
		}

		if (this.heuresSup50.contains(",")) {
			this.heuresSup50 = this.heuresSup50.replace(",", ".");
		}

		if (this.heuresSup50React.contains(",")) {
			this.heuresSup50React = this.heuresSup50React.replace(",", ".");
		}

		Pattern pattern = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
		Matcher matcher = pattern.matcher(this.salaireConventionelMinimum);
		boolean m1 = matcher.matches();
		matcher = pattern.matcher(this.salaireConventionelMinimumReact);
		boolean m2 = matcher.matches();
		matcher = pattern.matcher(this.augmentationCollective);
		boolean m3 = matcher.matches();
		matcher = pattern.matcher(this.augmentationCollectiveReact);
		boolean m4 = matcher.matches();
		matcher = pattern.matcher(this.augmentationIndividuelle);
		boolean m5 = matcher.matches();
		matcher = pattern.matcher(this.augmentationIndividuelleReact);
		boolean m6 = matcher.matches();
		matcher = pattern.matcher(this.heuresSup25);
		boolean m7 = matcher.matches();
		matcher = pattern.matcher(this.heuresSup25React);
		boolean m8 = matcher.matches();
		matcher = pattern.matcher(this.heuresSup50);
		boolean m9 = matcher.matches();
		matcher = pattern.matcher(this.heuresSup50React);
		boolean m10 = matcher.matches();
		matcher = pattern.matcher(this.salaireDeBase);
		boolean m15 = matcher.matches();
		matcher = pattern.matcher(this.salaireDeBaseReact);
		boolean m16 = matcher.matches();

		matcher = pattern.matcher(this.taux);
		boolean m12 = matcher.matches();
		matcher = pattern.matcher(this.salaireBrut);
		boolean m13 = matcher.matches();
		matcher = pattern.matcher(this.salaireConvBrut);
		boolean m14 = matcher.matches();
		matcher = pattern.matcher(this.coefficient);
		boolean m17 = matcher.matches();

		if ((m12 && m13 && m14 && m17) == false) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Les montants / taux / coefficient doivent \u00eatre des donn\u00E9es num\u00E9riques d\u00E9cimales.",
					"Les montants / taux / coefficient doivent \u00eatre des donn\u00E9es num\u00E9riques d\u00E9cimales.");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:horaire", message);

			changeZeroToNull();
			return "";

		}

		if ((m1 && m2 && m3 && m4 && m5 && m6 && m7 && m8 && m9 && m10 && m15 && m16) == false) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Les montants et r\u00E9actualisations doivent \u00eatre des donn\u00E9es num\u00E9riques d\u00E9cimales.",
					"Les montants et r\u00E9actualisations doivent \u00eatre des donn\u00E9es num\u00E9riques d\u00E9cimales.");
			FacesContext.getCurrentInstance().addMessage(null, message);

			changeZeroToNull();
			return "";
		}

		LienRemunerationRevenuServiceImpl serviceLien = new LienRemunerationRevenuServiceImpl();
		for (LienRemunerationRevenuBean lrr : this.lienRemuRevListDeleted) {
			serviceLien.supprimer(lrr);
		}

		RemunerationServiceImpl serviceRemu = new RemunerationServiceImpl();

		RemunerationBean remu = new RemunerationBean();

		remu.setIdRemuneration(this.id);

		remu.setAnnee(this.annee);
		remu.setAugmentationCollective(Double.valueOf(this
				.getAugmentationCollective()));
		remu.setAugmentationCollectiveActualisation(Double.valueOf(this
				.getAugmentationCollectiveReact()));
		remu.setAugmentationCollectiveCommentaire(this
				.getAugmentationCollectiveComm());
		remu.setAugmentationIndividuelle(Double.valueOf(this
				.getAugmentationIndividuelle()));
		remu.setAugmentationIndividuelleActualisation(Double.valueOf(this
				.getAugmentationIndividuelleReact()));
		remu.setAugmentationIndividuelleCommentaire(this
				.getAugmentationIndividuelleComm());
		remu.setCoefficient((this.coefficient != null && !this.coefficient
				.equals("")) ? this.coefficient : "");
		remu.setNiveau(this.niveau);
		remu.setEchelon(this.echelon);
		remu.setCommentaire(this.getCommentaire());
		remu.setHeuresSup25(Double.valueOf(this.getHeuresSup25()));
		remu.setHeuresSup25Actualisation(Double.valueOf(this
				.getHeuresSup25React()));
		remu.setHeuresSup25Commentaire(this.getHeuresSup25Comm());
		remu.setHeuresSup50(Double.valueOf(this.getHeuresSup50()));
		remu.setHeuresSup50Actualisation(Double.valueOf(this
				.getHeuresSup50React()));
		remu.setHeuresSup50Commentaire(this.getHeuresSup50Comm());
		remu.setHoraireMensuelTravaille(this.getHoraire());

		int idMetier = 0;
		MetierServiceImpl ms = new MetierServiceImpl();
		for (MetierBean m : ms.getMetiersList(Integer.parseInt(session
				.getAttribute("groupe").toString()))) {
			if (m.getNom().equals(this.metier)) {
				idMetier = m.getId();
			}
		}
		remu.setIdMetier(idMetier);
		remu.setIdSalarie(this.getIdSalarie());

		StatutServiceImpl ss = new StatutServiceImpl();
		List<StatutBean> list = new ArrayList<StatutBean>();
		list = ss.getStatutsList();
		StatutBean m = new StatutBean();
		for (StatutBean p : list) {
			if (p.getNom().equals(this.csp)) {
				m = p;
			}
		}
		remu.setNomCsp(this.csp);
		remu.setNomMetier(this.metier);
		remu.setIdStatut(m.getId());
		remu.setEchelon(this.getEchelon());
		remu.setNiveau(this.getNiveau());
		remu.setSalaireDeBase(Double.valueOf(this.getSalaireDeBase()));
		remu.setSalaireDeBaseActualisation(Double.valueOf(this
				.getSalaireDeBaseReact()));
		remu.setSalaireDeBaseCommentaire(this.getSalaireDeBaseComm());
		remu.setSalaireMensuelBrut(Double.valueOf(this.getSalaireBrut()));
		remu.setSalaireMensuelCenventionnelBrut(Double.valueOf(this
				.getSalaireConvBrut()));
		remu.setSalaireMinimumConventionnel(Double.valueOf(this
				.getSalaireConventionelMinimum()));
		remu.setSalaireMinimumConventionnelActualisation(Double.valueOf(this
				.getSalaireConventionelMinimumReact()));
		remu.setSalaireMinimumConventionnelCommentaire(this
				.getSalaireConventionelMinimumComm());
		remu.setTauxHoraireBrut(Double.valueOf(this.getTaux()));

		if (Double.valueOf(this.remunerationBruteAnnuelleActu) != 0.0) {
			remu.setBaseBruteAnnuelle(Double
					.valueOf(this.remunerationBruteAnnuelleActu));
		} else {
			remu.setBaseBruteAnnuelle(Double
					.valueOf(this.remunerationBruteAnnuelle));
		}

		if (Double.valueOf(this.remunerationGlobaleActu) != 0.0) {
			remu.setRemuGlobale(Double.valueOf(this.remunerationGlobaleActu));
		} else {
			remu.setRemuGlobale(Double.valueOf(this.remunerationGlobale));
		}

		if (Double.valueOf(this.heuresSup25React) != 0.0) {
			if (Double.valueOf(this.heuresSup50React) != 0.0) {
				remu.setHeuresSupAnnuelles(Double
						.valueOf(this.heuresSup25React)
						+ Double.valueOf(this.heuresSup50React));
			} else {
				remu.setHeuresSupAnnuelles(Double
						.valueOf(this.heuresSup25React)
						+ Double.valueOf(this.heuresSup50));
			}
		} else {
			if (Double.valueOf(this.heuresSup50React) != 0.0) {
				remu.setHeuresSupAnnuelles(Double.valueOf(this.heuresSup25)
						+ Double.valueOf(this.heuresSup50React));
			} else {
				remu.setHeuresSupAnnuelles(Double.valueOf(this.heuresSup25)
						+ Double.valueOf(this.heuresSup50));
			}
		}

		if (Double.valueOf(this.totalActuAvantageNonAssujetti) != 0.0) {
			remu.setAvantagesNonAssujettisAnnuels(Double
					.valueOf(this.totalActuAvantageNonAssujetti));
		} else {
			remu.setAvantagesNonAssujettisAnnuels(Double
					.valueOf(this.totalNAvantageNonAssujetti));
		}

		serviceRemu.saveOrUpdate(remu);

		SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
				.getCurrentInstance().getCurrentInstance().getExternalContext()
				.getSessionMap().get("salarieFormBB");

		List<RemunerationBean> l = new ArrayList<RemunerationBean>();
		l.addAll(salarieFormBB.getRemunerationBeanList());
		l.add(remu);
		salarieFormBB.setRemunerationBeanList(l);

		salarieFormBB.saveOrUpdateSalarie();

		RemunerationServiceImpl rs = new RemunerationServiceImpl();
		int id = 0;
		for (RemunerationBean rb : rs.getRemunerationList(Integer
				.parseInt(session.getAttribute("groupe").toString()))) {
			if (rb.getAnnee() == this.annee
					&& rb.getIdSalarie() == this.getIdSalarie()) {
				id = rb.getIdRemuneration();
			}
			List<RemunerationBean> lt = new ArrayList<RemunerationBean>();
			lt.addAll(salarieFormBB.getRemunerationBeanList());
			lt.add(remu);
			salarieFormBB.setRemunerationBeanList(lt);

			salarieFormBB.saveOrUpdateSalarie();
		}

		int idRemu = 0;
		for (RemunerationBean r : rs
				.getRemuneration(this.annee, this.idSalarie)) {
			if (r.getAnnee() == this.annee) {
				idRemu = r.getIdRemuneration();
			}
		}

		RevenuComplementaireServiceImpl rev = new RevenuComplementaireServiceImpl();
		saveOrUpdateLienRemuRev(idRemu);

		modalRendered = !modalRendered;
		anneeOk = false;
		modif = false;
		return "saveOrUpdateRemuneration";
	}

	public void saveOrUpdateLienRemuRev(int idRemu) {
		LienRemunerationRevenuServiceImpl serv = new LienRemunerationRevenuServiceImpl();
		for (LienRemunerationRevenuBean lrb : this.commissionInventoryLien) {
			lrb.setIdRemuneration(idRemu);
			serv.saveOrUpdate(lrb);
		}
		for (LienRemunerationRevenuBean lrb : this.primeFixeInventoryLien) {
			lrb.setIdRemuneration(idRemu);
			serv.saveOrUpdate(lrb);
		}
		for (LienRemunerationRevenuBean lrb : this.primeVariableInventoryLien) {
			lrb.setIdRemuneration(idRemu);
			serv.saveOrUpdate(lrb);
		}
		for (LienRemunerationRevenuBean lrb : this.avantageAssujettiInventoryLien) {
			lrb.setIdRemuneration(idRemu);
			serv.saveOrUpdate(lrb);
		}
		for (LienRemunerationRevenuBean lrb : this.avantageNonAssujettiInventoryLien) {
			lrb.setIdRemuneration(idRemu);
			serv.saveOrUpdate(lrb);
		}
		for (LienRemunerationRevenuBean lrb : this.fraisProfInventoryLien) {
			lrb.setIdRemuneration(idRemu);
			serv.saveOrUpdate(lrb);
		}
	}

	public String calculNPrec(String type, String idSelected) throws Exception {

		if (idSelected.equals("-1")) {
			return df.format(Double.valueOf("0"));
		}

		int anneePrec = this.annee - 1;

		RemunerationServiceImpl remu = new RemunerationServiceImpl();
		List<RemunerationBean> list = new ArrayList<RemunerationBean>();
		list = remu.getRemunerationList(Integer.parseInt(session.getAttribute(
				"groupe").toString()));
		RemunerationBean r = new RemunerationBean();
		for (RemunerationBean rb : list) {
			if (rb.getAnnee() == anneePrec
					&& rb.getIdSalarie() == this.idSalarie) {
				r = rb;
			}
		}

		RevenuComplementaireServiceImpl service = new RevenuComplementaireServiceImpl();
		int id;
		if (!type.equals("frais_professionnel")) {
			id = service
					.getIdFromRevenuComplementaire(idSelected, type,
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} else {
			id = service
					.getIdFromRevenuComplementaireFp(idSelected, type,
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		}

		LienRemunerationRevenuServiceImpl lien = new LienRemunerationRevenuServiceImpl();
		List<LienRemunerationRevenuBean> listLien = new ArrayList<LienRemunerationRevenuBean>();
		listLien = lien.getLienRemunerationRevenuList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		LienRemunerationRevenuBean l = new LienRemunerationRevenuBean();
		for (LienRemunerationRevenuBean lrr : listLien) {
			if (lrr.getIdRemuneration().equals(r.getIdRemuneration())
					&& lrr.getIdRevenuComplementaire().equals(id)) {
				l = lrr;
			}
		}
		if (l.getActualisation() == null
				|| Double.valueOf(l.getActualisation()) == 0.0) {
			if (l.getMontant() == null || Double.valueOf(l.getMontant()) == 0.0) {
				return df.format(Double.valueOf("0"));
			} else {
				return df.format(Double.valueOf(l.getMontant()));
			}
		} else {
			return df.format(Double.valueOf(l.getActualisation()));
		}
	}

	public void printFicheIndivRemuneration() throws Exception {

		modif = true;
		print = true;
		validerAnnee();
		modif = false;
		print = false;

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
				.getCurrentInstance().getCurrentInstance().getExternalContext()
				.getSessionMap().get("salarieFormBB");

		eContext.getSessionMap().put("idSalarie", this.idSalarie);
		eContext.getSessionMap().put("idRemu", this.id);
		eContext.getSessionMap().put("annee", this.annee);
		List<LienRemunerationRevenuBean> list = new ArrayList<LienRemunerationRevenuBean>();
		list.addAll(commissionInventoryLien);
		list.addAll(primeFixeInventoryLien);
		list.addAll(primeVariableInventoryLienEditable);
		list.addAll(avantageAssujettiInventoryLien);
		list.addAll(avantageNonAssujettiInventoryLien);
		eContext.getSessionMap().put("list", list);
		eContext.getSessionMap().put("idEntreprise",
				salarieFormBB.getIdEntrepriseSelected());
		eContext.getSessionMap().put("groupe",
				Integer.parseInt(session.getAttribute("groupe").toString()));
		eContext.getSessionMap().put("nomGroupe", salarieFormBB.getNomGroupe());

		JavascriptContext
				.addJavascriptCall(
						FacesContext.getCurrentInstance(),
						"window.open(\"servlet.printFicheIndivRemuneration?contentType=pdf \",\"_Reports\");");
	}

	public void maj(ValueChangeEvent e) throws Exception {

		this.commissionInventoryLienEditable.clear();
		this.primeFixeInventoryLienEditable.clear();
		this.primeVariableInventoryLienEditable.clear();
		this.avantageAssujettiInventoryLienEditable.clear();
		this.avantageNonAssujettiInventoryLienEditable.clear();
		this.fraisProfInventoryLienEditable.clear();

		maj = true;
		if (!this.idCommissionSelected.equals("-1")) {
			majCom(e);
		}
		if (!this.idPrimeFixeSelected.equals("-1")) {
			majPf(e);
		}
		if (!this.idPrimeVariableSelected.equals("-1")) {
			majPv(e);
		}
		if (!this.idAvantageAssujettiSelected.equals("-1")) {
			majAa(e);
		}
		if (!this.idAvantageNonAssujettiSelected.equals("-1")) {
			majAna(e);
		}
		if (!this.idFraisProfSelected.equals("-1")) {
			majFp(e);
		}

	}

	public void majCom(ValueChangeEvent e) throws Exception {

		if (this.montantNCommission.contains(",")) {
			this.montantNCommission = this.montantNCommission.replace(",", ".");
		}
		if (this.actuCommission.contains(",")) {
			this.actuCommission = this.actuCommission.replace(",", ".");
		}

		this.idCommissionSelected = (String) e.getNewValue();
		if (isFirst == true && maj == false) {
			firstValuec = this.idCommissionSelected;
		}

		if (isFirst == true && maj == true && !firstValuec.equals("-1")
				|| isFirst == true && maj == false && !firstValuec.equals("-1")
				|| isFirst == false && maj == false
				&& !this.idCommissionSelected.equals("-1")) {

			changeNullToZero();

			RevenuComplementaireServiceImpl s = new RevenuComplementaireServiceImpl();
			LienRemunerationRevenuBean l = new LienRemunerationRevenuBean();
			if (isFirst == true && maj == true) {

				l.setLibelle(firstValuec);
				l.setIdRevenuComplementaire(s.getIdFromRevenuComplementaire(
						firstValuec, "commission", Integer.parseInt(session
								.getAttribute("groupe").toString())));
				this.montantNPrecCommission = calculNPrec("commission",
						firstValuec);
				l.setMontantNPrec(calculNPrec("commission", firstValuec));

				firstValuec = "";
				maj = false;
				isFirst = false;

			} else {
				this.montantNCommission = "0";
				this.actuCommission = "0";
				this.comCommission = "";
				l.setLibelle(this.idCommissionSelected);
				l.setIdRevenuComplementaire(s.getIdFromRevenuComplementaire(
						this.idCommissionSelected, "commission", Integer
								.parseInt(session.getAttribute("groupe")
										.toString())));
				this.montantNPrecCommission = calculNPrec("commission",
						this.idCommissionSelected);
				l.setMontantNPrec(calculNPrec("commission",
						this.idCommissionSelected));
			}
			l.setRemonteNPrec(false);
			l.setMontant(df.format(Double.valueOf(this.montantNCommission)));
			l.setActualisation(df.format(Double.valueOf(this.actuCommission)));
			l.setCommentaire(this.comCommission);
			l.setIdRemuneration(this.id);
			l.setIdLienRemunerationRevenuComplementaire(0);

			this.commissionInventoryLienEditable.clear();
			this.commissionInventoryLienEditable.add(l);

			changeZeroToNull();
		}
	}

	public void majPf(ValueChangeEvent e) throws Exception {

		if (this.montantNPrimeFixe.contains(",")) {
			this.montantNPrimeFixe = this.montantNPrimeFixe.replace(",", ".");
		}
		if (this.actuPrimeFixe.contains(",")) {
			this.actuPrimeFixe = this.actuPrimeFixe.replace(",", ".");
		}

		this.idPrimeFixeSelected = (String) e.getNewValue();
		if (isFirst == true && maj == false) {
			firstValuepf = this.idPrimeFixeSelected;
		}

		if (isFirst == true && maj == true && !firstValuepf.equals("-1")
				|| isFirst == true && maj == false
				&& !firstValuepf.equals("-1") || isFirst == false
				&& maj == false && !this.idPrimeFixeSelected.equals("-1")) {

			changeNullToZero();

			RevenuComplementaireServiceImpl s = new RevenuComplementaireServiceImpl();
			LienRemunerationRevenuBean l = new LienRemunerationRevenuBean();
			if (isFirst == true && maj == true) {

				l.setLibelle(firstValuepf);
				l.setIdRevenuComplementaire(s.getIdFromRevenuComplementaire(
						firstValuepf, "prime_fixe", Integer.parseInt(session
								.getAttribute("groupe").toString())));
				if (Double.valueOf(calculNPrec("prime_fixe", firstValuepf)) != 0.0) {
					this.montantNPrecPrimeFixe = calculNPrec("prime_fixe",
							firstValuepf);
					l.setMontantNPrec(calculNPrec("prime_fixe", firstValuepf));
				} else {
					this.montantNPrecPrimeFixe = "0";
					l.setMontantNPrec("0.00");
				}
				firstValuepf = "";
				maj = false;
				isFirst = false;

			} else {
				this.montantNPrimeFixe = "0";
				this.actuPrimeFixe = "0";
				this.comPrimeFixe = "";
				l.setLibelle(this.idPrimeFixeSelected);
				l.setIdRevenuComplementaire(s.getIdFromRevenuComplementaire(
						this.idPrimeFixeSelected, "prime_fixe", Integer
								.parseInt(session.getAttribute("groupe")
										.toString())));
				if (Double.valueOf(calculNPrec("prime_fixe",
						this.idPrimeFixeSelected)) != 0.0) {
					this.montantNPrecPrimeFixe = calculNPrec("prime_fixe",
							this.idPrimeFixeSelected);
					l.setMontantNPrec(calculNPrec("prime_fixe",
							this.idPrimeFixeSelected));
				} else {
					this.montantNPrecPrimeFixe = "0";
					l.setMontantNPrec("0.00");
				}
			}
			l.setRemonteNPrec(false);
			l.setMontant(df.format(Double.valueOf(this.montantNPrimeFixe)));
			l.setActualisation(df.format(Double.valueOf(this.actuPrimeFixe)));
			l.setCommentaire(this.comPrimeFixe);

			this.primeFixeInventoryLienEditable.clear();
			this.primeFixeInventoryLienEditable.add(l);

			changeZeroToNull();
		}
	}

	public void majPv(ValueChangeEvent e) throws Exception {

		this.idPrimeVariableSelected = (String) e.getNewValue();
		if (isFirst == true && maj == false) {
			firstValuepv = this.idPrimeVariableSelected;
		}

		if (isFirst == true && maj == true && !firstValuepv.equals("-1")
				|| isFirst == true && maj == false
				&& !firstValuepv.equals("-1") || isFirst == false
				&& maj == false && !this.idPrimeVariableSelected.equals("-1")) {

			changeNullToZero();
			RevenuComplementaireServiceImpl s = new RevenuComplementaireServiceImpl();
			LienRemunerationRevenuBean l = new LienRemunerationRevenuBean();
			if (isFirst == true && maj == true) {

				l.setLibelle(firstValuepv);
				l.setIdRevenuComplementaire(s.getIdFromRevenuComplementaire(
						firstValuepv, "prime_variable", Integer
								.parseInt(session.getAttribute("groupe")
										.toString())));
				if (Double.valueOf(calculNPrec("prime_variable", firstValuepv)) != 0.0) {
					this.montantNPrecPrimeVariable = calculNPrec(
							"prime_variable", firstValuepv);
					l.setMontantNPrec(calculNPrec("prime_variable",
							firstValuepv));
				} else {
					this.montantNPrecPrimeVariable = "0";
					l.setMontantNPrec("0.00");
				}
				firstValuepv = "";
				maj = false;
				isFirst = false;

			} else {
				this.montantNPrimeVariable = "0";
				this.actuPrimeVariable = "0";
				this.comPrimeVariable = "";
				l.setLibelle(this.idPrimeVariableSelected);
				l.setIdRevenuComplementaire(s.getIdFromRevenuComplementaire(
						this.idPrimeVariableSelected, "prime_variable", Integer
								.parseInt(session.getAttribute("groupe")
										.toString())));
				if (Double.valueOf(calculNPrec("prime_variable",
						this.idPrimeVariableSelected)) != 0.0) {
					this.montantNPrecPrimeVariable = calculNPrec(
							"prime_variable", this.idPrimeVariableSelected);
					l.setMontantNPrec(calculNPrec("prime_variable",
							this.idPrimeVariableSelected));
				} else {
					this.montantNPrecPrimeVariable = "0";
					l.setMontantNPrec("0.00");
				}
			}
			l.setRemonteNPrec(false);
			l.setMontant(df.format(Double.valueOf(this.montantNPrimeVariable)));
			l.setActualisation(df.format(Double.valueOf(this.actuPrimeVariable)));
			l.setCommentaire(this.comPrimeVariable);

			this.primeVariableInventoryLienEditable.clear();
			this.primeVariableInventoryLienEditable.add(l);

			changeZeroToNull();
		}
	}

	public void majAa(ValueChangeEvent e) throws Exception {

		if (this.montantNAvantageAssujetti.contains(",")) {
			this.montantNAvantageAssujetti = this.montantNAvantageAssujetti
					.replace(",", ".");
		}
		if (this.actuAvantageAssujetti.contains(",")) {
			this.actuAvantageAssujetti = this.actuAvantageAssujetti.replace(
					",", ".");
		}

		this.idAvantageAssujettiSelected = (String) e.getNewValue();
		if (isFirst == true && maj == false) {
			firstValueaa = this.idAvantageAssujettiSelected;
		}

		if (isFirst == true && maj == true && !firstValueaa.equals("-1")
				|| isFirst == true && maj == false
				&& !firstValueaa.equals("-1") || isFirst == false
				&& maj == false
				&& !this.idAvantageAssujettiSelected.equals("-1")) {

			changeNullToZero();
			RevenuComplementaireServiceImpl s = new RevenuComplementaireServiceImpl();
			LienRemunerationRevenuBean l = new LienRemunerationRevenuBean();
			if (isFirst == true && maj == true) {

				l.setLibelle(firstValueaa);
				l.setIdRevenuComplementaire(s.getIdFromRevenuComplementaire(
						firstValueaa, "avantage_assujetti", Integer
								.parseInt(session.getAttribute("groupe")
										.toString())));
				if (Double.valueOf(calculNPrec("avantage_assujetti",
						firstValueaa)) != 0.0) {
					this.montantNPrecAvantageAssujetti = calculNPrec(
							"avantage_assujetti", firstValueaa);
					l.setMontantNPrec(calculNPrec("avantage_assujetti",
							firstValueaa));
				} else {
					this.montantNPrecAvantageAssujetti = "0";
					l.setMontantNPrec("0.00");
				}
				firstValueaa = "";
				maj = false;
				isFirst = false;

			} else {
				this.montantNAvantageAssujetti = "0";
				this.actuAvantageAssujetti = "0";
				this.comAvantageAssujetti = "";
				l.setLibelle(this.idAvantageAssujettiSelected);
				l.setIdRevenuComplementaire(s.getIdFromRevenuComplementaire(
						this.idAvantageAssujettiSelected, "avantage_assujetti",
						Integer.parseInt(session.getAttribute("groupe")
								.toString())));
				if (Double.valueOf(calculNPrec("avantage_assujetti",
						this.idAvantageAssujettiSelected)) != 0.0) {
					this.montantNPrecAvantageAssujetti = calculNPrec(
							"avantage_assujetti",
							this.idAvantageAssujettiSelected);
					l.setMontantNPrec(calculNPrec("avantage_assujetti",
							this.idAvantageAssujettiSelected));
				} else {
					this.montantNPrecAvantageAssujetti = "0";
					l.setMontantNPrec("0.00");
				}
			}
			l.setRemonteNPrec(false);
			l.setMontant(df.format(Double
					.valueOf(this.montantNAvantageAssujetti)));
			l.setActualisation(df.format(Double
					.valueOf(this.actuAvantageAssujetti)));
			l.setCommentaire(this.comAvantageAssujetti);

			this.avantageAssujettiInventoryLienEditable.clear();
			this.avantageAssujettiInventoryLienEditable.add(l);

			changeZeroToNull();
		}
	}

	public void majAna(ValueChangeEvent e) throws Exception {

		if (this.montantNAvantageNonAssujetti.contains(",")) {
			this.montantNAvantageNonAssujetti = this.montantNAvantageNonAssujetti
					.replace(",", ".");
		}
		if (this.actuAvantageNonAssujetti.contains(",")) {
			this.actuAvantageNonAssujetti = this.actuAvantageNonAssujetti
					.replace(",", ".");
		}

		this.idAvantageNonAssujettiSelected = (String) e.getNewValue();
		if (isFirst == true && maj == false) {
			firstValueana = this.idAvantageNonAssujettiSelected;
		}

		if (isFirst == true && maj == true && !firstValueana.equals("-1")
				|| isFirst == true && maj == false
				&& !firstValueana.equals("-1") || isFirst == false
				&& maj == false
				&& !this.idAvantageNonAssujettiSelected.equals("-1")) {

			changeNullToZero();
			RevenuComplementaireServiceImpl s = new RevenuComplementaireServiceImpl();
			LienRemunerationRevenuBean l = new LienRemunerationRevenuBean();
			if (isFirst == true && maj == true) {

				l.setLibelle(firstValueana);
				l.setIdRevenuComplementaire(s.getIdFromRevenuComplementaire(
						firstValueana, "avantage_non_assujetti", Integer
								.parseInt(session.getAttribute("groupe")
										.toString())));
				if (Double.valueOf(calculNPrec("avantage_non_assujetti",
						firstValueana)) != 0.0) {
					this.montantNPrecAvantageNonAssujetti = calculNPrec(
							"avantage_non_assujetti", firstValueana);
					l.setMontantNPrec(calculNPrec("avantage_non_assujetti",
							firstValueana));
				} else {
					this.montantNPrecAvantageNonAssujetti = "0";
					l.setMontantNPrec("0.00");
				}
				firstValueana = "";
				maj = false;
				isFirst = false;

			} else {
				this.montantNAvantageNonAssujetti = "0";
				this.actuAvantageNonAssujetti = "0";
				this.comAvantageNonAssujetti = "";
				l.setLibelle(this.idAvantageNonAssujettiSelected);
				l.setIdRevenuComplementaire(s.getIdFromRevenuComplementaire(
						this.idAvantageNonAssujettiSelected,
						"avantage_non_assujetti", Integer.parseInt(session
								.getAttribute("groupe").toString())));
				if (Double.valueOf(calculNPrec("avantage_non_assujetti",
						this.idAvantageNonAssujettiSelected)) != 0.0) {
					this.montantNPrecAvantageNonAssujetti = calculNPrec(
							"avantage_non_assujetti",
							this.idAvantageNonAssujettiSelected);
					l.setMontantNPrec(calculNPrec("avantage_non_assujetti",
							this.idAvantageNonAssujettiSelected));
				} else {
					this.montantNPrecAvantageNonAssujetti = "0";
					l.setMontantNPrec("0.00");
				}
			}
			l.setRemonteNPrec(false);
			l.setMontant(df.format(Double
					.valueOf(this.montantNAvantageNonAssujetti)));
			l.setActualisation(df.format(Double
					.valueOf(this.actuAvantageNonAssujetti)));
			l.setCommentaire(this.comAvantageNonAssujetti);

			this.avantageNonAssujettiInventoryLienEditable.clear();
			this.avantageNonAssujettiInventoryLienEditable.add(l);

			changeZeroToNull();
		}
	}

	public void majFp(ValueChangeEvent e) throws Exception {

		if (this.montantNFraisProf.contains(",")) {
			this.montantNFraisProf = this.montantNFraisProf.replace(",", ".");
		}

		this.idFraisProfSelected = (String) e.getNewValue();
		if (isFirst == true && maj == false) {
			firstValuefp = this.idFraisProfSelected;
		}

		if (isFirst == true && maj == true && !firstValuefp.equals("-1")
				|| isFirst == true && maj == false
				&& !firstValuefp.equals("-1") || isFirst == false
				&& maj == false && !this.idFraisProfSelected.equals("-1")) {

			changeNullToZero();
			RevenuComplementaireServiceImpl s = new RevenuComplementaireServiceImpl();
			LienRemunerationRevenuBean l = new LienRemunerationRevenuBean();
			if (isFirst == true && maj == true) {

				l.setLibelle(firstValuefp);
				l.setIdRevenuComplementaire(s.getIdFromRevenuComplementaireFp(
						firstValuefp, "frais_professionnel", Integer
								.parseInt(session.getAttribute("groupe")
										.toString())));
				if (Double.valueOf(calculNPrec("frais_professionnel",
						firstValuefp)) != 0.0) {
					this.montantNPrecFraisProf = calculNPrec(
							"frais_professionnel", firstValuefp);
					l.setMontantNPrec(calculNPrec("frais_professionnel",
							firstValuefp));
				} else {
					this.montantNPrecFraisProf = "0";
					l.setMontantNPrec("0.00");
				}
				firstValuefp = "";
				maj = false;
				isFirst = false;

			} else {
				this.montantNFraisProf = "0";
				this.comFraisProf = "";
				l.setLibelle(this.idFraisProfSelected);
				l.setIdRevenuComplementaire(s.getIdFromRevenuComplementaireFp(
						this.idFraisProfSelected, "frais_professionnel",
						Integer.parseInt(session.getAttribute("groupe")
								.toString())));
				if (Double.valueOf(calculNPrec("frais_professionnel",
						this.idFraisProfSelected)) != 0.0) {
					this.montantNPrecFraisProf = calculNPrec(
							"frais_professionnel", this.idFraisProfSelected);
					l.setMontantNPrec(calculNPrec("frais_professionnel",
							this.idFraisProfSelected));
				} else {
					this.montantNPrecFraisProf = "0";
					l.setMontantNPrec("0.00");
				}
			}
			l.setRemonteNPrec(false);
			l.setMontant(df.format(Double.valueOf(this.montantNFraisProf)));
			l.setCommentaire(this.comFraisProf);

			this.fraisProfInventoryLienEditable.clear();
			this.fraisProfInventoryLienEditable.add(l);

			if (isFirst)
				maj(e);
			changeNullToZero();
			changeZeroToNull();
		}
	}

	public void toggleModal(ActionEvent event) {
		modalRendered = !modalRendered;
		anneeOk = false;
		modif = false;
	}

	public void initSalarieRemunerationForm() throws Exception {
		anneeOk = false;
		init();
		modalRendered = !modalRendered;
	}

	public void majtotal(ValueChangeEvent event) {
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {

			changeNullToZero();

			if (this.taux.contains(",")) {
				this.taux = this.taux.replace(",", ".");
			}

			if (this.coefficient.contains(",")) {
				this.coefficient = this.coefficient.replace(",", ".");
			}

			if (this.salaireBrut.contains(",")) {
				this.salaireBrut = this.salaireBrut.replace(",", ".");
			}

			if (this.salaireConvBrut.contains(",")) {
				this.salaireConvBrut = this.salaireConvBrut.replace(",", ".");
			}

			if (this.salaireConventionelMinimum.contains(",")) {
				this.salaireConventionelMinimum = this.salaireConventionelMinimum
						.replace(",", ".");
			}

			if (this.salaireConventionelMinimumReact.contains(",")) {
				this.salaireConventionelMinimumReact = this.salaireConventionelMinimumReact
						.replace(",", ".");
			}

			if (this.salaireDeBase.contains(",")) {
				this.salaireDeBase = this.salaireDeBase.replace(",", ".");
			}

			if (this.salaireDeBaseReact.contains(",")) {
				this.salaireDeBaseReact = this.salaireDeBaseReact.replace(",",
						".");
			}

			if (this.augmentationCollective.contains(",")) {
				this.augmentationCollective = this.augmentationCollective
						.replace(",", ".");
			}

			if (this.augmentationCollectiveReact.contains(",")) {
				this.augmentationCollectiveReact = this.augmentationCollectiveReact
						.replace(",", ".");
			}

			if (this.augmentationIndividuelle.contains(",")) {
				this.augmentationIndividuelle = this.augmentationIndividuelle
						.replace(",", ".");
			}

			if (this.augmentationIndividuelleReact.contains(",")) {
				this.augmentationIndividuelleReact = this.augmentationIndividuelleReact
						.replace(",", ".");
			}

			if (this.heuresSup25.contains(",")) {
				this.heuresSup25 = this.heuresSup25.replace(",", ".");
			}

			if (this.heuresSup25React.contains(",")) {
				this.heuresSup25React = this.heuresSup25React.replace(",", ".");
			}

			if (this.heuresSup50.contains(",")) {
				this.heuresSup50 = this.heuresSup50.replace(",", ".");
			}

			if (this.heuresSup50React.contains(",")) {
				this.heuresSup50React = this.heuresSup50React.replace(",", ".");
			}

			Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
			Matcher m = p.matcher(this.salaireConventionelMinimum);
			boolean m1 = m.matches();
			m = p.matcher(this.salaireConventionelMinimumReact);
			boolean m2 = m.matches();
			m = p.matcher(this.augmentationCollective);
			boolean m3 = m.matches();
			m = p.matcher(this.augmentationCollectiveReact);
			boolean m4 = m.matches();
			m = p.matcher(this.augmentationIndividuelle);
			boolean m5 = m.matches();
			m = p.matcher(this.augmentationIndividuelleReact);
			boolean m6 = m.matches();
			m = p.matcher(this.heuresSup25);
			boolean m7 = m.matches();
			m = p.matcher(this.heuresSup25React);
			boolean m8 = m.matches();
			m = p.matcher(this.heuresSup50);
			boolean m9 = m.matches();
			m = p.matcher(this.heuresSup50React);
			boolean m10 = m.matches();
			m = p.matcher(this.salaireDeBase);
			boolean m15 = m.matches();
			m = p.matcher(this.salaireDeBaseReact);
			boolean m16 = m.matches();

			m = p.matcher(this.taux);
			boolean m12 = m.matches();
			m = p.matcher(this.salaireBrut);
			boolean m13 = m.matches();
			m = p.matcher(this.salaireConvBrut);
			boolean m14 = m.matches();
			m = p.matcher(this.coefficient);
			boolean m17 = m.matches();

			if ((m12 && m13 && m14 && m17) == false) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Les montants / taux / coefficient doivent \u00eatre des donn\u00E9es num\u00E9riques d\u00E9cimales.",
						"Les montants / taux / coefficient doivent \u00eatre des donn\u00E9es num\u00E9riques d\u00E9cimales.");
				FacesContext.getCurrentInstance().addMessage(
						"idSalarieForm:idSalarieTabSet:0:horaire", message);

				changeZeroToNull();
				return;

			}

			if ((m1 && m2 && m3 && m4 && m5 && m6 && m7 && m8 && m9 && m10
					&& m15 && m16) == false) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Les montants et r\u00E9actualisations doivent \u00eatre des donn\u00E9es num\u00E9riques d\u00E9cimales.",
						"Les montants et r\u00E9actualisations doivent \u00eatre des donn\u00E9es num\u00E9riques d\u00E9cimales.");
				FacesContext.getCurrentInstance().addMessage(null, message);

				changeZeroToNull();
				return;

			} else {

				if (event.getComponent().getId()
						.equals("salaireConventionelMinimum")) {
					if (!this.modif
							&& Double.valueOf(this.salaireConventionelMinimum) != 0.0) {
						this.salaireConventionelMinimumReact = this.salaireConventionelMinimum;
					}
				}
				if (event.getComponent().getId().equals("salaireDeBase")) {
					if (!this.modif
							&& Double.valueOf(this.salaireDeBase) != 0.0) {
						this.salaireDeBaseReact = this.salaireDeBase;
					}
				}
				if (event.getComponent().getId()
						.equals("augmentationCollective")) {
					if (!this.modif
							&& Double.valueOf(this.augmentationCollective) != 0.0) {
						this.augmentationCollectiveReact = this.augmentationCollective;
					}
				}
				if (event.getComponent().getId()
						.equals("augmentationIndividuelle")) {
					if (!this.modif
							&& Double.valueOf(this.augmentationIndividuelle) != 0.0) {
						this.augmentationIndividuelleReact = this.augmentationIndividuelle;
					}
				}
				if (event.getComponent().getId().equals("heuresSup25")) {
					if (!this.modif && Double.valueOf(this.heuresSup25) != 0.0) {
						this.heuresSup25React = this.heuresSup25;
					}
				}
				if (event.getComponent().getId().equals("heuresSup50")) {
					if (!this.modif && Double.valueOf(this.heuresSup50) != 0.0) {
						this.heuresSup50React = this.heuresSup50;
					}
				}

				double sommeAnneePrecedente = Double
						.valueOf(this.salaireDeBaseAnneePrecedente)
						+ Double.valueOf(this.augmentationCollectiveAnneePrecedente)
						+ Double.valueOf(this.augmentationIndividuelleAnneePrecedente)
						+ Double.valueOf(this.heuresSup25AnneePrecedente)
						+ Double.valueOf(this.heuresSup50AnneePrecedente);
				this.totalBrutAnnuelAnneePrecedente = String
						.valueOf(sommeAnneePrecedente);

				double somme = Double.valueOf(this.salaireDeBase)
						+ Double.valueOf(this.augmentationCollective)
						+ Double.valueOf(this.augmentationIndividuelle)
						+ Double.valueOf(this.heuresSup25)
						+ Double.valueOf(this.heuresSup50);
				this.totalBrutAnnuel = String.valueOf(somme);

				double s1 = 0.0;
				double s2 = 0.0;
				double s3 = 0.0;
				double s4 = 0.0;
				double s5 = 0.0;
				if (Double.valueOf(this.salaireDeBaseReact) == 0.0) {
					s1 = Double.valueOf(this.salaireDeBase);
				} else {
					s1 = Double.valueOf(this.salaireDeBaseReact);
				}

				if (Double.valueOf(this.augmentationCollectiveReact) == 0.0) {
					s2 = Double.valueOf(this.augmentationCollective);
				} else {
					s2 = Double.valueOf(this.augmentationCollectiveReact);
				}

				if (Double.valueOf(this.augmentationIndividuelleReact) == 0.0) {
					s3 = Double.valueOf(this.augmentationIndividuelle);
				} else {
					s3 = Double.valueOf(this.augmentationIndividuelleReact);
				}

				if (Double.valueOf(this.heuresSup25React) == 0.0) {
					s4 = Double.valueOf(this.heuresSup25);
				} else {
					s4 = Double.valueOf(this.heuresSup25React);
				}

				if (Double.valueOf(this.heuresSup50React) == 0.0) {
					s5 = Double.valueOf(this.heuresSup50);
				} else {
					s5 = Double.valueOf(this.heuresSup50React);
				}

				double sommeReact = s1 + s2 + s3 + s4 + s5;
				this.totalBrutAnnuelReact = df.format(sommeReact);

				if ((Double.valueOf(this.totalBrutAnnuel) != 0.0 || Double
						.valueOf(this.totalBrutAnnuelReact) != 0.0)
						&& Double.valueOf(this.totalBrutAnnuelAnneePrecedente) != 0.0) {
					if (Double.valueOf(this.totalBrutAnnuelReact) == 0.0) {
						double totalBrutEvol = ((Double
								.valueOf(this.totalBrutAnnuel) - Double
								.valueOf(this.totalBrutAnnuelAnneePrecedente)) / Double
								.valueOf(this.totalBrutAnnuelAnneePrecedente)) * 100;
						double total = (((totalBrutEvol * 100)) / 100);
						this.totalBrutAnnuelEvol = df.format(total);
					} else {
						double totalBrutEvol = ((Double
								.valueOf(this.totalBrutAnnuelReact) - Double
								.valueOf(this.totalBrutAnnuelAnneePrecedente)) / Double
								.valueOf(this.totalBrutAnnuelAnneePrecedente)) * 100;
						double total = (((totalBrutEvol * 100)) / 100);
						this.totalBrutAnnuelEvol = df.format(total);
					}
				} else {
					this.totalBrutAnnuelEvol = df.format(0.0);
				}

				if ((Double.valueOf(this.salaireConventionelMinimum) != 0.0 || Double
						.valueOf(this.salaireConventionelMinimumReact) != 0.0)
						&& Double
								.valueOf(this.salaireConventionelMinimumAnneePrecedente) != 0.0) {
					if (Double.valueOf(this.salaireConventionelMinimumReact) == 0.0) {
						double totalSalaireConvMin = ((Double
								.valueOf(this.salaireConventionelMinimum) - Double
								.valueOf(this.salaireConventionelMinimumAnneePrecedente)) / Double
								.valueOf(this.salaireConventionelMinimumAnneePrecedente)) * 100;
						double total1 = ((totalSalaireConvMin * 100)) / 100;
						this.salaireConventionelMinimumEvol = df.format(total1);
					} else {
						double totalSalaireConvMin = ((Double
								.valueOf(this.salaireConventionelMinimumReact) - Double
								.valueOf(this.salaireConventionelMinimumAnneePrecedente)) / Double
								.valueOf(this.salaireConventionelMinimumAnneePrecedente)) * 100;
						double total1 = ((totalSalaireConvMin * 100)) / 100;
						this.salaireConventionelMinimumEvol = df.format(total1);
					}
				} else {
					this.salaireConventionelMinimumEvol = df.format(0.0);
				}

				if ((Double.valueOf(this.salaireDeBase) != 0.0 || Double
						.valueOf(this.salaireDeBaseReact) != 0.0)
						&& Double.valueOf(this.salaireDeBaseAnneePrecedente) != 0.0) {
					if (Double.valueOf(this.salaireDeBaseReact) == 0.0) {
						double totalSalaireBase = ((Double
								.valueOf(this.salaireDeBase) - Double
								.valueOf(this.salaireDeBaseAnneePrecedente)) / Double
								.valueOf(this.salaireDeBaseAnneePrecedente)) * 100;
						double total2 = (totalSalaireBase * 100) / 100;
						this.salaireDeBaseEvol = df.format(total2);
					} else {
						double totalSalaireBase = ((Double
								.valueOf(this.salaireDeBaseReact) - Double
								.valueOf(this.salaireDeBaseAnneePrecedente)) / Double
								.valueOf(this.salaireDeBaseAnneePrecedente)) * 100;
						double total2 = (totalSalaireBase * 100) / 100;
						this.salaireDeBaseEvol = df.format(total2);
					}
				} else {
					this.salaireDeBaseEvol = df.format(0.0);
				}

				if ((Double.valueOf(this.augmentationCollective) != 0.0 || Double
						.valueOf(this.augmentationCollectiveReact) != 0.0)
						&& Double
								.valueOf(this.augmentationCollectiveAnneePrecedente) != 0.0) {
					if (Double.valueOf(this.augmentationCollectiveReact) == 0.0) {
						double totalAugmColl = ((Double
								.valueOf(this.augmentationCollective) - Double
								.valueOf(this.augmentationCollectiveAnneePrecedente)) / Double
								.valueOf(this.augmentationCollectiveAnneePrecedente)) * 100;
						double total3 = (totalAugmColl * 100) / 100;
						this.augmentationCollectiveEvol = df.format(total3);
					} else {
						double totalAugmColl = ((Double
								.valueOf(this.augmentationCollectiveReact) - Double
								.valueOf(this.augmentationCollectiveAnneePrecedente)) / Double
								.valueOf(this.augmentationCollectiveAnneePrecedente)) * 100;
						double total3 = (totalAugmColl * 100) / 100;
						this.augmentationCollectiveEvol = df.format(total3);
					}
				} else {
					this.augmentationCollectiveEvol = df.format(0.0);
				}

				if ((Double.valueOf(this.augmentationIndividuelle) != 0.0 || Double
						.valueOf(this.augmentationIndividuelleReact) != 0.0)
						&& Double
								.valueOf(this.augmentationIndividuelleAnneePrecedente) != 0.0) {
					if (Double.valueOf(this.augmentationIndividuelleReact) == 0.0) {
						double totalAugmInd = ((Double
								.valueOf(this.augmentationIndividuelle) - Double
								.valueOf(this.augmentationIndividuelleAnneePrecedente)) / Double
								.valueOf(this.augmentationIndividuelleAnneePrecedente)) * 100;
						double total4 = (totalAugmInd * 100) / 100;
						this.augmentationIndividuelleEvol = df.format(total4);
					} else {
						double totalAugmInd = ((Double
								.valueOf(this.augmentationIndividuelleReact) - Double
								.valueOf(this.augmentationIndividuelleAnneePrecedente)) / Double
								.valueOf(this.augmentationIndividuelleAnneePrecedente)) * 100;
						double total4 = (totalAugmInd * 100) / 100;
						this.augmentationIndividuelleEvol = df.format(total4);
					}
				} else {
					this.augmentationIndividuelleEvol = df.format(0.0);
				}

				if ((Double.valueOf(this.heuresSup25) != 0.0 || Double
						.valueOf(this.heuresSup25React) != 0.0)
						&& Double.valueOf(this.heuresSup25AnneePrecedente) != 0.0) {
					if (Double.valueOf(this.heuresSup25React) == 0.0) {
						double totalHeureSup25 = ((Double
								.valueOf(this.heuresSup25) - Double
								.valueOf(this.heuresSup25AnneePrecedente)) / Double
								.valueOf(this.heuresSup25AnneePrecedente)) * 100;
						double total5 = (totalHeureSup25 * 100) / 100;
						this.heuresSup25Evol = df.format(total5);
					} else {
						double totalHeureSup25 = ((Double
								.valueOf(this.heuresSup25React) - Double
								.valueOf(this.heuresSup25AnneePrecedente)) / Double
								.valueOf(this.heuresSup25AnneePrecedente)) * 100;
						double total5 = (totalHeureSup25 * 100) / 100;
						this.heuresSup25Evol = df.format(total5);
					}
				} else {
					this.heuresSup25Evol = df.format(0.0);
				}

				if ((Double.valueOf(this.heuresSup50) != 0.0 || Double
						.valueOf(this.heuresSup50React) != 0.0)
						&& Double.valueOf(this.heuresSup50AnneePrecedente) != 0.0) {
					if (Double.valueOf(this.heuresSup50React) == 0.0) {
						double totalHeureSup50 = ((Double
								.valueOf(this.heuresSup50) - Double
								.valueOf(this.heuresSup50AnneePrecedente)) / Double
								.valueOf(this.heuresSup50AnneePrecedente)) * 100;
						double total6 = (totalHeureSup50 * 100) / 100;
						this.heuresSup50Evol = df.format(total6);
					} else {
						double totalHeureSup50 = ((Double
								.valueOf(this.heuresSup50React) - Double
								.valueOf(this.heuresSup50AnneePrecedente)) / Double
								.valueOf(this.heuresSup50AnneePrecedente)) * 100;
						double total6 = (totalHeureSup50 * 100) / 100;
						this.heuresSup50Evol = df.format(total6);
					}
				} else {
					this.heuresSup50Evol = df.format(0.0);
				}

				this.remunerationBruteAnnuelle = df.format(Double
						.valueOf(this.totalBrutAnnuel)
						+ Double.valueOf(this.totalNCommission)
						+ Double.valueOf(this.totalNPrimeFixe)
						+ Double.valueOf(this.totalNPrimeVariable)
						+ Double.valueOf(this.totalNAvantageAssujetti));

				this.remunerationBruteAnnuelleNPrec = df.format(Double
						.valueOf(this.totalBrutAnnuelAnneePrecedente)
						+ Double.valueOf(this.totalNPrecCommission)
						+ Double.valueOf(this.totalNPrecPrimeFixe)
						+ Double.valueOf(this.totalNPrecPrimeVariable)
						+ Double.valueOf(this.totalNPrecAvantageAssujetti));

				this.remunerationBruteAnnuelleActu = df.format(Double
						.valueOf(this.totalBrutAnnuelReact)
						+ Double.valueOf(this.totalActuCommission)
						+ Double.valueOf(this.totalActuPrimeFixe)
						+ Double.valueOf(this.totalActuPrimeVariable)
						+ Double.valueOf(this.totalActuAvantageAssujetti));

				if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
						&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
								.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
					if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
						double totalBrutEvol = ((Double
								.valueOf(this.remunerationBruteAnnuelle) - Double
								.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
								.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
						double total = (double) ((totalBrutEvol * 100)) / 100;
						this.remunerationBruteAnnuelleEvol = df.format(Double
								.valueOf(total));
					} else {
						double totalBrutEvol = ((Double
								.valueOf(this.remunerationBruteAnnuelleActu) - Double
								.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
								.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
						double total = (double) ((totalBrutEvol * 100)) / 100;
						this.remunerationBruteAnnuelleEvol = df.format(Double
								.valueOf(total));
					}
				}

				this.remunerationGlobale = df.format(Double
						.valueOf(this.remunerationBruteAnnuelle)
						+ Double.valueOf(this.totalNAvantageNonAssujetti));

				this.remunerationGlobaleNPrec = df.format(Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)
						+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

				this.remunerationGlobaleActu = df.format(Double
						.valueOf(this.remunerationBruteAnnuelleActu)
						+ Double.valueOf(this.totalActuAvantageNonAssujetti));

				if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
						&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
								.valueOf(this.remunerationGlobaleActu) != 0)) {
					if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
						double totalBrutEvol = ((Double
								.valueOf(this.remunerationGlobale) - Double
								.valueOf(this.remunerationGlobaleNPrec)) / Double
								.valueOf(this.remunerationGlobaleNPrec)) * 100;
						double total = (double) ((totalBrutEvol * 100)) / 100;
						this.remunerationGlobaleEvol = df.format(Double
								.valueOf(total));
					} else {
						double totalBrutEvol = ((Double
								.valueOf(this.remunerationGlobaleActu) - Double
								.valueOf(this.remunerationGlobaleNPrec)) / Double
								.valueOf(this.remunerationGlobaleNPrec)) * 100;
						double total = (double) ((totalBrutEvol * 100)) / 100;
						this.remunerationGlobaleEvol = df.format(Double
								.valueOf(total));
					}
				}
			}
			changeZeroToNull();
		}
	}

	public void cancelCommission() {

		if (modifC != null) {
			this.commissionInventoryLien.add(modifC);

			this.totalNCommission = df.format(Double
					.valueOf(this.totalNCommission)
					+ Double.valueOf(modifC.getMontant()));
			if (Double.valueOf(modifC.getActualisation()) != 0) {
				this.totalActuCommission = df.format(Double
						.valueOf(this.totalActuCommission)
						+ Double.valueOf(modifC.getActualisation()));
			} else {
				this.totalActuCommission = df.format(Double
						.valueOf(this.totalActuCommission)
						+ Double.valueOf(modifC.getMontant()));
			}

			for (SelectItem s : this.commissionList) {
				if (s.getLabel().equals(modifC.getLibelle())) {
					this.commissionList.remove(s);
					break;
				}
			}
		}

		this.commissionInventoryLienEditable.clear();
		this.idCommissionSelected = "-1";
		modifC = null;
	}

	public void refreshCom() {
		this.idCommissionSelected = "-1";
	}

	public void cancelPrimeFixe() {

		if (modifPf != null) {
			this.primeFixeInventoryLien.add(modifPf);

			this.totalNPrimeFixe = df.format(Double
					.valueOf(this.totalNPrimeFixe)
					+ Double.valueOf(modifPf.getMontant()));
			if (Double.valueOf(modifPf.getActualisation()) != 0) {
				this.totalActuPrimeFixe = df.format(Double
						.valueOf(this.totalActuPrimeFixe)
						+ Double.valueOf(modifPf.getActualisation()));
			} else {
				this.totalActuPrimeFixe = df.format(Double
						.valueOf(this.totalActuPrimeFixe)
						+ Double.valueOf(modifPf.getMontant()));
			}

			for (SelectItem s : this.primeFixeList) {
				if (s.getLabel().equals(modifPf.getLibelle())) {
					this.primeFixeList.remove(s);
					break;
				}
			}
		}

		this.primeFixeInventoryLienEditable.clear();
		this.idPrimeFixeSelected = "-1";
		modifPf = null;
	}

	public void refreshPf() {
		this.idPrimeFixeSelected = "-1";
	}

	public void cancelPrimeVariable() {

		if (modifPv != null) {
			this.primeVariableInventoryLien.add(modifPv);

			this.totalNPrimeVariable = df.format(Double
					.valueOf(this.totalNPrimeVariable)
					+ Double.valueOf(modifPv.getMontant()));
			if (Double.valueOf(modifPv.getActualisation()) != 0) {
				this.totalActuPrimeVariable = df.format(Double
						.valueOf(this.totalActuPrimeVariable)
						+ Double.valueOf(modifPv.getActualisation()));
			} else {
				this.totalActuPrimeVariable = df.format(Double
						.valueOf(this.totalActuPrimeVariable)
						+ Double.valueOf(modifPv.getMontant()));
			}

			for (SelectItem s : this.primeVariableList) {
				if (s.getLabel().equals(modifPv.getLibelle())) {
					this.primeVariableList.remove(s);
					break;
				}
			}
		}

		this.primeVariableInventoryLienEditable.clear();
		this.idPrimeVariableSelected = "-1";
		modifPv = null;
	}

	public void refreshPv() {
		this.idPrimeVariableSelected = "-1";
	}

	public void cancelAvantageAssujetti() {

		if (modifAa != null) {
			this.avantageAssujettiInventoryLien.add(modifAa);

			this.totalNAvantageAssujetti = df.format(Double
					.valueOf(this.totalNAvantageAssujetti)
					+ Double.valueOf(modifAa.getMontant()));
			if (Double.valueOf(modifAa.getActualisation()) != 0) {
				this.totalActuAvantageAssujetti = df.format(Double
						.valueOf(this.totalActuAvantageAssujetti)
						+ Double.valueOf(modifAa.getActualisation()));
			} else {
				this.totalActuAvantageAssujetti = df.format(Double
						.valueOf(this.totalActuAvantageAssujetti)
						+ Double.valueOf(modifAa.getMontant()));
			}

			for (SelectItem s : this.avantageAssujettiList) {
				if (s.getLabel().equals(modifAa.getLibelle())) {
					this.avantageAssujettiList.remove(s);
					break;
				}
			}
		}

		this.avantageAssujettiInventoryLienEditable.clear();
		this.idAvantageAssujettiSelected = "-1";
		modifAa = null;
	}

	public void refreshAa() {
		this.idAvantageAssujettiSelected = "-1";
	}

	public void cancelAvantageNonAssujetti() {

		if (modifAna != null) {
			this.avantageNonAssujettiInventoryLien.add(modifAna);

			this.totalNAvantageNonAssujetti = df.format(Double
					.valueOf(this.totalNAvantageNonAssujetti)
					+ Double.valueOf(modifAna.getMontant()));
			if (Double.valueOf(modifAna.getActualisation()) != 0) {
				this.totalActuAvantageNonAssujetti = df.format(Double
						.valueOf(this.totalActuAvantageNonAssujetti)
						+ Double.valueOf(modifAna.getActualisation()));
			} else {
				this.totalActuAvantageNonAssujetti = df.format(Double
						.valueOf(this.totalActuAvantageNonAssujetti)
						+ Double.valueOf(modifAna.getMontant()));
			}

			for (SelectItem s : this.avantageNonAssujettiList) {
				if (s.getLabel().equals(modifAna.getLibelle())) {
					this.avantageNonAssujettiList.remove(s);
					break;
				}
			}
		}

		this.avantageNonAssujettiInventoryLienEditable.clear();
		this.idAvantageNonAssujettiSelected = "-1";
		modifAna = null;
	}

	public void refreshAna() {
		this.idAvantageNonAssujettiSelected = "-1";
	}

	public void cancelFraisProf() {

		if (modifFp != null) {
			this.fraisProfInventoryLien.add(modifFp);

			this.totalNFraisProf = df.format(Double
					.valueOf(this.totalNFraisProf)
					+ Double.valueOf(modifFp.getMontant()));

			for (SelectItem s : this.fraisProfessionnelList) {
				if (s.getLabel().equals(modifFp.getLibelle())) {
					this.fraisProfessionnelList.remove(s);
					break;
				}
			}
		}

		this.fraisProfInventoryLienEditable.clear();
		this.idFraisProfSelected = "-1";
		modifFp = null;
	}

	public void refreshFp() {
		this.idFraisProfSelected = "-1";
	}

	public void supprimerCommission(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean l = (LienRemunerationRevenuBean) table
				.getRowData();

		SelectItem s = new SelectItem();
		s.setLabel(l.getLibelle());
		s.setValue(l.getLibelle());

		boolean contain = false;
		for (SelectItem pf : this.commissionList) {
			if (pf.getLabel().equals(s.getLabel())) {
				contain = true;
				break;
			}
		}
		if (!contain) {
			this.commissionList.add(s);
		}

		changeNullToZero();
		modifC = null;
		this.lienRemuRevListDeleted.add(l);
		this.commissionInventoryLien.remove(l);
		this.totalNCommission = df.format(Double.valueOf(this.totalNCommission)
				- Double.valueOf(l.getMontant()));
		if (Double.valueOf(l.getActualisation()) != 0.0) {
			this.totalActuCommission = df.format(Double
					.valueOf(this.totalActuCommission)
					- Double.valueOf(l.getActualisation()));
		} else {
			this.totalActuCommission = df.format(Double
					.valueOf(this.totalActuCommission)
					- Double.valueOf(l.getMontant()));
		}

		this.remunerationBruteAnnuelle = df.format(Double
				.valueOf(this.totalBrutAnnuel)
				+ Double.valueOf(this.totalNCommission)
				+ Double.valueOf(this.totalNPrimeFixe)
				+ Double.valueOf(this.totalNPrimeVariable)
				+ Double.valueOf(this.totalNAvantageAssujetti));

		this.remunerationBruteAnnuelleNPrec = df.format(Double
				.valueOf(this.totalBrutAnnuelAnneePrecedente)
				+ Double.valueOf(this.totalNPrecCommission)
				+ Double.valueOf(this.totalNPrecPrimeFixe)
				+ Double.valueOf(this.totalNPrecPrimeVariable)
				+ Double.valueOf(this.totalNPrecAvantageAssujetti));

		this.remunerationBruteAnnuelleActu = df.format(Double
				.valueOf(this.totalBrutAnnuelReact)
				+ Double.valueOf(this.totalActuCommission)
				+ Double.valueOf(this.totalActuPrimeFixe)
				+ Double.valueOf(this.totalActuPrimeVariable)
				+ Double.valueOf(this.totalActuAvantageAssujetti));

		this.remunerationGlobale = df.format(Double
				.valueOf(this.remunerationBruteAnnuelle)
				+ Double.valueOf(this.totalNAvantageNonAssujetti));

		this.remunerationGlobaleNPrec = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleNPrec)
				+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

		this.remunerationGlobaleActu = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleActu)
				+ Double.valueOf(this.totalActuAvantageNonAssujetti));

		if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
				&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
						.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
			if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelle) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelleActu) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			}
		}
		if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
				&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
						.valueOf(this.remunerationGlobaleActu) != 0)) {
			if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobale) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobaleActu) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			}
		}

		this.idCommissionSelected = "-1";
		changeZeroToNull();
	}

	public void supprimerPrimeFixe(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean l = (LienRemunerationRevenuBean) table
				.getRowData();

		SelectItem s = new SelectItem();
		s.setLabel(l.getLibelle());
		s.setValue(l.getLibelle());

		boolean contain = false;
		for (SelectItem pf : this.primeFixeList) {
			if (pf.getLabel().equals(s.getLabel())) {
				contain = true;
				break;
			}
		}
		if (!contain) {
			this.primeFixeList.add(s);
		}

		changeNullToZero();
		modifPf = null;
		this.lienRemuRevListDeleted.add(l);
		this.primeFixeInventoryLien.remove(l);
		this.totalNPrimeFixe = df.format(Double.valueOf(this.totalNPrimeFixe)
				- Double.valueOf(l.getMontant()));
		if (Double.valueOf(l.getActualisation()) != 0.0) {
			this.totalActuPrimeFixe = df.format(Double
					.valueOf(this.totalActuPrimeFixe)
					- Double.valueOf(l.getActualisation()));
		} else {
			this.totalActuPrimeFixe = df.format(Double
					.valueOf(this.totalActuPrimeFixe)
					- Double.valueOf(l.getMontant()));
		}

		this.remunerationBruteAnnuelle = df.format(Double
				.valueOf(this.totalBrutAnnuel)
				+ Double.valueOf(this.totalNCommission)
				+ Double.valueOf(this.totalNPrimeFixe)
				+ Double.valueOf(this.totalNPrimeVariable)
				+ Double.valueOf(this.totalNAvantageAssujetti));

		this.remunerationBruteAnnuelleNPrec = df.format(Double
				.valueOf(this.totalBrutAnnuelAnneePrecedente)
				+ Double.valueOf(this.totalNPrecCommission)
				+ Double.valueOf(this.totalNPrecPrimeFixe)
				+ Double.valueOf(this.totalNPrecPrimeVariable)
				+ Double.valueOf(this.totalNPrecAvantageAssujetti));

		this.remunerationBruteAnnuelleActu = df.format(Double
				.valueOf(this.totalBrutAnnuelReact)
				+ Double.valueOf(this.totalActuCommission)
				+ Double.valueOf(this.totalActuPrimeFixe)
				+ Double.valueOf(this.totalActuPrimeVariable)
				+ Double.valueOf(this.totalActuAvantageAssujetti));

		this.remunerationGlobale = df.format(Double
				.valueOf(this.remunerationBruteAnnuelle)
				+ Double.valueOf(this.totalNAvantageNonAssujetti));

		this.remunerationGlobaleNPrec = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleNPrec)
				+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

		this.remunerationGlobaleActu = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleActu)
				+ Double.valueOf(this.totalActuAvantageNonAssujetti));

		if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
				&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
						.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
			if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelle) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelleActu) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			}
		}
		if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
				&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
						.valueOf(this.remunerationGlobaleActu) != 0)) {
			if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobale) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobaleActu) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			}
		}

		this.idPrimeFixeSelected = "-1";
		changeZeroToNull();
	}

	public void supprimerPrimeVariable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean l = (LienRemunerationRevenuBean) table
				.getRowData();

		SelectItem s = new SelectItem();
		s.setLabel(l.getLibelle());
		s.setValue(l.getLibelle());

		boolean contain = false;
		for (SelectItem pf : this.primeVariableList) {
			if (pf.getLabel().equals(s.getLabel())) {
				contain = true;
				break;
			}
		}
		if (!contain) {
			this.primeVariableList.add(s);
		}

		changeNullToZero();
		modifPv = null;
		this.lienRemuRevListDeleted.add(l);
		this.primeVariableInventoryLien.remove(l);
		this.totalNPrimeVariable = df.format(Double
				.valueOf(this.totalActuPrimeVariable)
				- Double.valueOf(l.getMontant()));
		if (Double.valueOf(l.getActualisation()) != 0.0) {
			this.totalActuPrimeVariable = df.format(Double
					.valueOf(this.totalActuPrimeVariable)
					- Double.valueOf(l.getActualisation()));
		} else {
			this.totalActuPrimeVariable = df.format(Double
					.valueOf(this.totalActuPrimeVariable)
					- Double.valueOf(l.getMontant()));
		}

		this.remunerationBruteAnnuelle = df.format(Double
				.valueOf(this.totalBrutAnnuel)
				+ Double.valueOf(this.totalNCommission)
				+ Double.valueOf(this.totalNPrimeFixe)
				+ Double.valueOf(this.totalNPrimeVariable)
				+ Double.valueOf(this.totalNAvantageAssujetti));

		this.remunerationBruteAnnuelleNPrec = df.format(Double
				.valueOf(this.totalBrutAnnuelAnneePrecedente)
				+ Double.valueOf(this.totalNPrecCommission)
				+ Double.valueOf(this.totalNPrecPrimeFixe)
				+ Double.valueOf(this.totalNPrecPrimeVariable)
				+ Double.valueOf(this.totalNPrecAvantageAssujetti));

		this.remunerationBruteAnnuelleActu = df.format(Double
				.valueOf(this.totalBrutAnnuelReact)
				+ Double.valueOf(this.totalActuCommission)
				+ Double.valueOf(this.totalActuPrimeFixe)
				+ Double.valueOf(this.totalActuPrimeVariable)
				+ Double.valueOf(this.totalActuAvantageAssujetti));

		this.remunerationGlobale = df.format(Double
				.valueOf(this.remunerationBruteAnnuelle)
				+ Double.valueOf(this.totalNAvantageNonAssujetti));

		this.remunerationGlobaleNPrec = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleNPrec)
				+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

		this.remunerationGlobaleActu = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleActu)
				+ Double.valueOf(this.totalActuAvantageNonAssujetti));

		if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
				&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
						.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
			if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelle) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelleActu) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			}
		}
		if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
				&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
						.valueOf(this.remunerationGlobaleActu) != 0)) {
			if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobale) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobaleActu) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			}
		}

		this.idPrimeVariableSelected = "-1";
		changeZeroToNull();
	}

	public void supprimerAvantageAssujetti(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean l = (LienRemunerationRevenuBean) table
				.getRowData();

		SelectItem s = new SelectItem();
		s.setLabel(l.getLibelle());
		s.setValue(l.getLibelle());

		boolean contain = false;
		for (SelectItem pf : this.avantageAssujettiList) {
			if (pf.getLabel().equals(s.getLabel())) {
				contain = true;
				break;
			}
		}
		if (!contain) {
			this.avantageAssujettiList.add(s);
		}

		changeNullToZero();
		modifAa = null;
		this.lienRemuRevListDeleted.add(l);
		this.avantageAssujettiInventoryLien.remove(l);
		this.totalNAvantageAssujetti = df.format(Double
				.valueOf(this.totalNAvantageAssujetti)
				- Double.valueOf(l.getMontant()));
		if (Double.valueOf(l.getActualisation()) != 0.0) {
			this.totalActuAvantageAssujetti = df.format(Double
					.valueOf(this.totalActuAvantageAssujetti)
					- Double.valueOf(l.getActualisation()));
		} else {
			this.totalActuAvantageAssujetti = df.format(Double
					.valueOf(this.totalActuAvantageAssujetti)
					- Double.valueOf(l.getMontant()));
		}

		this.remunerationBruteAnnuelle = df.format(Double
				.valueOf(this.totalBrutAnnuel)
				+ Double.valueOf(this.totalNCommission)
				+ Double.valueOf(this.totalNPrimeFixe)
				+ Double.valueOf(this.totalNPrimeVariable)
				+ Double.valueOf(this.totalNAvantageAssujetti));

		this.remunerationBruteAnnuelleNPrec = df.format(Double
				.valueOf(this.totalBrutAnnuelAnneePrecedente)
				+ Double.valueOf(this.totalNPrecCommission)
				+ Double.valueOf(this.totalNPrecPrimeFixe)
				+ Double.valueOf(this.totalNPrecPrimeVariable)
				+ Double.valueOf(this.totalNPrecAvantageAssujetti));

		this.remunerationBruteAnnuelleActu = df.format(Double
				.valueOf(this.totalBrutAnnuelReact)
				+ Double.valueOf(this.totalActuCommission)
				+ Double.valueOf(this.totalActuPrimeFixe)
				+ Double.valueOf(this.totalActuPrimeVariable)
				+ Double.valueOf(this.totalActuAvantageAssujetti));

		this.remunerationGlobale = df.format(Double
				.valueOf(this.remunerationBruteAnnuelle)
				+ Double.valueOf(this.totalNAvantageNonAssujetti));

		this.remunerationGlobaleNPrec = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleNPrec)
				+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

		this.remunerationGlobaleActu = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleActu)
				+ Double.valueOf(this.totalActuAvantageNonAssujetti));

		if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
				&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
						.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
			if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelle) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelleActu) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			}
		}
		if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
				&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
						.valueOf(this.remunerationGlobaleActu) != 0)) {
			if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobale) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobaleActu) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			}
		}

		this.idAvantageAssujettiSelected = "-1";
		changeZeroToNull();
	}

	public void supprimerAvantageNonAssujetti(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean l = (LienRemunerationRevenuBean) table
				.getRowData();

		SelectItem s = new SelectItem();
		s.setLabel(l.getLibelle());
		s.setValue(l.getLibelle());

		boolean contain = false;
		for (SelectItem pf : this.avantageNonAssujettiList) {
			if (pf.getLabel().equals(s.getLabel())) {
				contain = true;
				break;
			}
		}
		if (!contain) {
			this.avantageNonAssujettiList.add(s);
		}

		changeNullToZero();
		modifAna = null;
		this.lienRemuRevListDeleted.add(l);
		this.avantageNonAssujettiInventoryLien.remove(l);
		this.totalNAvantageNonAssujetti = df.format(Double
				.valueOf(this.totalNAvantageNonAssujetti)
				- Double.valueOf(l.getMontant()));
		if (Double.valueOf(l.getActualisation()) != 0.0) {
			this.totalActuAvantageNonAssujetti = df.format(Double
					.valueOf(this.totalActuAvantageNonAssujetti)
					- Double.valueOf(l.getActualisation()));
		} else {
			this.totalActuAvantageNonAssujetti = df.format(Double
					.valueOf(this.totalActuAvantageNonAssujetti)
					- Double.valueOf(l.getActualisation()));
		}

		this.remunerationBruteAnnuelle = df.format(Double
				.valueOf(this.totalBrutAnnuel)
				+ Double.valueOf(this.totalNCommission)
				+ Double.valueOf(this.totalNPrimeFixe)
				+ Double.valueOf(this.totalNPrimeVariable)
				+ Double.valueOf(this.totalNAvantageAssujetti));

		this.remunerationBruteAnnuelleNPrec = df.format(Double
				.valueOf(this.totalBrutAnnuelAnneePrecedente)
				+ Double.valueOf(this.totalNPrecCommission)
				+ Double.valueOf(this.totalNPrecPrimeFixe)
				+ Double.valueOf(this.totalNPrecPrimeVariable)
				+ Double.valueOf(this.totalNPrecAvantageAssujetti));

		this.remunerationBruteAnnuelleActu = df.format(Double
				.valueOf(this.totalBrutAnnuelReact)
				+ Double.valueOf(this.totalActuCommission)
				+ Double.valueOf(this.totalActuPrimeFixe)
				+ Double.valueOf(this.totalActuPrimeVariable)
				+ Double.valueOf(this.totalActuAvantageAssujetti));

		this.remunerationGlobale = df.format(Double
				.valueOf(this.remunerationBruteAnnuelle)
				+ Double.valueOf(this.totalNAvantageNonAssujetti));

		this.remunerationGlobaleNPrec = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleNPrec)
				+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

		this.remunerationGlobaleActu = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleActu)
				+ Double.valueOf(this.totalActuAvantageNonAssujetti));

		if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
				&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
						.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
			if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelle) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelleActu) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			}
		}
		if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
				&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
						.valueOf(this.remunerationGlobaleActu) != 0)) {
			if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobale) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobaleActu) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			}
		}

		this.idAvantageNonAssujettiSelected = "-1";
		changeZeroToNull();
	}

	public void supprimerFraisProf(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean l = (LienRemunerationRevenuBean) table
				.getRowData();

		SelectItem s = new SelectItem();
		s.setLabel(l.getLibelle());
		s.setValue(l.getLibelle());

		boolean contain = false;
		for (SelectItem pf : this.fraisProfessionnelList) {
			if (pf.getLabel().equals(s.getLabel())) {
				contain = true;
				break;
			}
		}
		if (!contain) {
			this.fraisProfessionnelList.add(s);
		}

		changeNullToZero();
		modifFp = null;
		this.lienRemuRevListDeleted.add(l);
		this.fraisProfInventoryLien.remove(l);
		this.totalNFraisProf = df.format(Double.valueOf(this.totalNFraisProf)
				- Double.valueOf(l.getMontant()));

		this.remunerationBruteAnnuelle = df.format(Double
				.valueOf(this.totalBrutAnnuel)
				+ Double.valueOf(this.totalNCommission)
				+ Double.valueOf(this.totalNPrimeFixe)
				+ Double.valueOf(this.totalNPrimeVariable)
				+ Double.valueOf(this.totalNAvantageAssujetti));

		this.remunerationBruteAnnuelleNPrec = df.format(Double
				.valueOf(this.totalBrutAnnuelAnneePrecedente)
				+ Double.valueOf(this.totalNPrecCommission)
				+ Double.valueOf(this.totalNPrecPrimeFixe)
				+ Double.valueOf(this.totalNPrecPrimeVariable)
				+ Double.valueOf(this.totalNPrecAvantageAssujetti));

		this.remunerationBruteAnnuelleActu = df.format(Double
				.valueOf(this.totalBrutAnnuelReact)
				+ Double.valueOf(this.totalActuCommission)
				+ Double.valueOf(this.totalActuPrimeFixe)
				+ Double.valueOf(this.totalActuPrimeVariable)
				+ Double.valueOf(this.totalActuAvantageAssujetti));

		this.remunerationGlobale = df.format(Double
				.valueOf(this.remunerationBruteAnnuelle)
				+ Double.valueOf(this.totalNAvantageNonAssujetti));

		this.remunerationGlobaleNPrec = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleNPrec)
				+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

		this.remunerationGlobaleActu = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleActu)
				+ Double.valueOf(this.totalActuAvantageNonAssujetti));

		if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
				&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
						.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
			if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelle) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelleActu) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			}
		}
		if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
				&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
						.valueOf(this.remunerationGlobaleActu) != 0)) {
			if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobale) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobaleActu) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			}
		}

		this.idFraisProfSelected = "-1";
		changeZeroToNull();
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

	public void modifierCommission(ActionEvent evt) {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean lienRemunerationRevenuBean = (LienRemunerationRevenuBean) table
				.getRowData();

		SelectItem s = new SelectItem();
		s.setLabel(lienRemunerationRevenuBean.getLibelle());
		s.setValue(lienRemunerationRevenuBean.getLibelle());
		this.commissionList.add(s);

		this.idCommissionSelected = lienRemunerationRevenuBean.getLibelle();
		changeNullToZero();

		if (modifC != null) {
			cancelCommission();
		}

		modifC = new LienRemunerationRevenuBean();
		modifC.setActualisation(lienRemunerationRevenuBean.getActualisation());
		modifC.setCommentaire(lienRemunerationRevenuBean.getCommentaire());
		if (lienRemunerationRevenuBean
				.getIdLienRemunerationRevenuComplementaire() != null
				&& lienRemunerationRevenuBean
						.getIdLienRemunerationRevenuComplementaire() != 0) {
			modifC.setIdLienRemunerationRevenuComplementaire(lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire());
		} else {
			modifC.setIdLienRemunerationRevenuComplementaire(0);
		}
		modifC.setIdRemuneration(lienRemunerationRevenuBean.getIdRemuneration());
		modifC.setIdRevenuComplementaire(lienRemunerationRevenuBean
				.getIdRevenuComplementaire());
		modifC.setLibelle(lienRemunerationRevenuBean.getLibelle());
		modifC.setMontant(lienRemunerationRevenuBean.getMontant());
		modifC.setMontantNPrec(lienRemunerationRevenuBean.getMontantNPrec());

		this.commissionInventoryLienEditable.add(lienRemunerationRevenuBean);

		this.commissionInventoryLien.remove(lienRemunerationRevenuBean);

		this.totalNCommission = df.format(Double.valueOf(this.totalNCommission)
				- Double.valueOf(lienRemunerationRevenuBean.getMontant()));
		if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) != 0) {
			this.totalActuCommission = df.format(Double
					.valueOf(this.totalActuCommission)
					- Double.valueOf(lienRemunerationRevenuBean
							.getActualisation()));
		} else {
			this.totalActuCommission = df.format(Double
					.valueOf(this.totalActuCommission)
					- Double.valueOf(lienRemunerationRevenuBean.getMontant()));
		}

		changeZeroToNull();
	}

	public void modifierPrimeFixe(ActionEvent evt) {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean lienRemunerationRevenuBean = (LienRemunerationRevenuBean) table
				.getRowData();

		SelectItem s = new SelectItem();
		s.setLabel(lienRemunerationRevenuBean.getLibelle());
		s.setValue(lienRemunerationRevenuBean.getLibelle());
		this.primeFixeList.add(s);

		this.idPrimeFixeSelected = lienRemunerationRevenuBean.getLibelle();
		changeNullToZero();

		if (modifPf != null) {
			cancelPrimeFixe();
		}

		modifPf = new LienRemunerationRevenuBean();
		modifPf.setActualisation(lienRemunerationRevenuBean.getActualisation());
		modifPf.setCommentaire(lienRemunerationRevenuBean.getCommentaire());
		if (lienRemunerationRevenuBean
				.getIdLienRemunerationRevenuComplementaire() != null
				&& lienRemunerationRevenuBean
						.getIdLienRemunerationRevenuComplementaire() != 0) {
			modifPf.setIdLienRemunerationRevenuComplementaire(lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire());
		} else {
			modifPf.setIdLienRemunerationRevenuComplementaire(0);
		}
		modifPf.setIdRevenuComplementaire(lienRemunerationRevenuBean
				.getIdRevenuComplementaire());
		modifPf.setLibelle(lienRemunerationRevenuBean.getLibelle());
		modifPf.setMontant(lienRemunerationRevenuBean.getMontant());
		modifPf.setMontantNPrec(lienRemunerationRevenuBean.getMontantNPrec());
		modifPf.setIdRemuneration(lienRemunerationRevenuBean
				.getIdRemuneration());

		this.primeFixeInventoryLienEditable.add(lienRemunerationRevenuBean);
		this.primeFixeInventoryLien.remove(lienRemunerationRevenuBean);

		this.totalNPrimeFixe = df.format(Double.valueOf(this.totalNPrimeFixe)
				- Double.valueOf(lienRemunerationRevenuBean.getMontant()));
		if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) != 0.0) {
			this.totalActuPrimeFixe = df.format(Double
					.valueOf(this.totalActuPrimeFixe)
					- Double.valueOf(lienRemunerationRevenuBean
							.getActualisation()));
		} else {
			this.totalActuPrimeFixe = df.format(Double
					.valueOf(this.totalActuPrimeFixe)
					- Double.valueOf(lienRemunerationRevenuBean.getMontant()));
		}

		changeZeroToNull();
	}

	public void modifierPrimeVariable(ActionEvent evt) {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean lienRemunerationRevenuBean = (LienRemunerationRevenuBean) table
				.getRowData();

		SelectItem s = new SelectItem();
		s.setLabel(lienRemunerationRevenuBean.getLibelle());
		s.setValue(lienRemunerationRevenuBean.getLibelle());
		this.primeVariableList.add(s);

		this.idPrimeVariableSelected = lienRemunerationRevenuBean.getLibelle();
		changeNullToZero();

		if (modifPv != null) {
			cancelPrimeVariable();
		}

		modifPv = new LienRemunerationRevenuBean();
		modifPv.setActualisation(lienRemunerationRevenuBean.getActualisation());
		modifPv.setCommentaire(lienRemunerationRevenuBean.getCommentaire());
		if (lienRemunerationRevenuBean
				.getIdLienRemunerationRevenuComplementaire() != null
				&& lienRemunerationRevenuBean
						.getIdLienRemunerationRevenuComplementaire() != 0) {
			modifPv.setIdLienRemunerationRevenuComplementaire(lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire());
		} else {
			modifPv.setIdLienRemunerationRevenuComplementaire(0);
		}
		modifPv.setIdRevenuComplementaire(lienRemunerationRevenuBean
				.getIdRevenuComplementaire());
		modifPv.setLibelle(lienRemunerationRevenuBean.getLibelle());
		modifPv.setMontant(lienRemunerationRevenuBean.getMontant());
		modifPv.setMontantNPrec(lienRemunerationRevenuBean.getMontantNPrec());
		modifPv.setIdRemuneration(lienRemunerationRevenuBean
				.getIdRemuneration());

		this.primeVariableInventoryLienEditable.add(lienRemunerationRevenuBean);
		this.primeVariableInventoryLien.remove(lienRemunerationRevenuBean);

		this.totalNPrimeVariable = df.format(Double
				.valueOf(this.totalNPrimeVariable)
				- Double.valueOf(lienRemunerationRevenuBean.getMontant()));
		if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) != 0.0) {
			this.totalActuPrimeVariable = df.format(Double
					.valueOf(this.totalActuPrimeVariable)
					- Double.valueOf(lienRemunerationRevenuBean
							.getActualisation()));
		} else {
			this.totalActuPrimeVariable = df.format(Double
					.valueOf(this.totalActuPrimeVariable)
					- Double.valueOf(lienRemunerationRevenuBean.getMontant()));
		}

		changeZeroToNull();
	}

	public void modifierAvantageAssujetti(ActionEvent evt) {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean lienRemunerationRevenuBean = (LienRemunerationRevenuBean) table
				.getRowData();

		SelectItem s = new SelectItem();
		s.setLabel(lienRemunerationRevenuBean.getLibelle());
		s.setValue(lienRemunerationRevenuBean.getLibelle());
		this.avantageAssujettiList.add(s);

		this.idAvantageAssujettiSelected = lienRemunerationRevenuBean
				.getLibelle();
		changeNullToZero();

		if (modifAa != null) {
			cancelAvantageAssujetti();
		}

		modifAa = new LienRemunerationRevenuBean();
		modifAa.setActualisation(lienRemunerationRevenuBean.getActualisation());
		modifAa.setCommentaire(lienRemunerationRevenuBean.getCommentaire());
		if (lienRemunerationRevenuBean
				.getIdLienRemunerationRevenuComplementaire() != null
				&& lienRemunerationRevenuBean
						.getIdLienRemunerationRevenuComplementaire() != 0) {
			modifAa.setIdLienRemunerationRevenuComplementaire(lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire());
		} else {
			modifAa.setIdLienRemunerationRevenuComplementaire(0);
		}
		modifAa.setIdRevenuComplementaire(lienRemunerationRevenuBean
				.getIdRevenuComplementaire());
		modifAa.setLibelle(lienRemunerationRevenuBean.getLibelle());
		modifAa.setMontant(lienRemunerationRevenuBean.getMontant());
		modifAa.setMontantNPrec(lienRemunerationRevenuBean.getMontantNPrec());
		modifAa.setIdRemuneration(lienRemunerationRevenuBean
				.getIdRemuneration());

		this.avantageAssujettiInventoryLienEditable
				.add(lienRemunerationRevenuBean);
		this.avantageAssujettiInventoryLien.remove(lienRemunerationRevenuBean);

		this.totalNAvantageAssujetti = df.format(Double
				.valueOf(this.totalNAvantageAssujetti)
				- Double.valueOf(lienRemunerationRevenuBean.getMontant()));
		if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) != 0.0) {
			this.totalActuAvantageAssujetti = df.format(Double
					.valueOf(this.totalActuAvantageAssujetti)
					- Double.valueOf(lienRemunerationRevenuBean
							.getActualisation()));
		} else {
			this.totalActuAvantageAssujetti = df.format(Double
					.valueOf(this.totalActuAvantageAssujetti)
					- Double.valueOf(lienRemunerationRevenuBean.getMontant()));
		}

		changeZeroToNull();
	}

	public void modifierAvantageNonAssujetti(ActionEvent evt) {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean lienRemunerationRevenuBean = (LienRemunerationRevenuBean) table
				.getRowData();

		SelectItem s = new SelectItem();
		s.setLabel(lienRemunerationRevenuBean.getLibelle());
		s.setValue(lienRemunerationRevenuBean.getLibelle());
		this.avantageNonAssujettiList.add(s);

		this.idAvantageNonAssujettiSelected = lienRemunerationRevenuBean
				.getLibelle();
		changeNullToZero();

		if (modifAna != null) {
			cancelAvantageNonAssujetti();
		}

		modifAna = new LienRemunerationRevenuBean();
		modifAna.setActualisation(lienRemunerationRevenuBean.getActualisation());
		modifAna.setCommentaire(lienRemunerationRevenuBean.getCommentaire());
		if (lienRemunerationRevenuBean
				.getIdLienRemunerationRevenuComplementaire() != null
				&& lienRemunerationRevenuBean
						.getIdLienRemunerationRevenuComplementaire() != 0) {
			modifAna.setIdLienRemunerationRevenuComplementaire(lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire());
		} else {
			modifAna.setIdLienRemunerationRevenuComplementaire(0);
		}
		modifAna.setIdRevenuComplementaire(lienRemunerationRevenuBean
				.getIdRevenuComplementaire());
		modifAna.setLibelle(lienRemunerationRevenuBean.getLibelle());
		modifAna.setMontant(lienRemunerationRevenuBean.getMontant());
		modifAna.setMontantNPrec(lienRemunerationRevenuBean.getMontantNPrec());
		modifAna.setIdRemuneration(lienRemunerationRevenuBean
				.getIdRemuneration());

		this.avantageNonAssujettiInventoryLienEditable
				.add(lienRemunerationRevenuBean);
		this.avantageNonAssujettiInventoryLien
				.remove(lienRemunerationRevenuBean);

		this.totalNAvantageNonAssujetti = df.format(Double
				.valueOf(this.totalNAvantageNonAssujetti)
				- Double.valueOf(lienRemunerationRevenuBean.getMontant()));
		if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) != 0.0) {
			this.totalActuAvantageNonAssujetti = df.format(Double
					.valueOf(this.totalActuAvantageNonAssujetti)
					- Double.valueOf(lienRemunerationRevenuBean
							.getActualisation()));
		} else {
			this.totalActuAvantageNonAssujetti = df.format(Double
					.valueOf(this.totalActuAvantageNonAssujetti)
					- Double.valueOf(lienRemunerationRevenuBean.getMontant()));
		}

		changeZeroToNull();
	}

	public void modifierFraisProf(ActionEvent evt) {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean lienRemunerationRevenuBean = (LienRemunerationRevenuBean) table
				.getRowData();

		SelectItem s = new SelectItem();
		s.setLabel(lienRemunerationRevenuBean.getLibelle());
		s.setValue(lienRemunerationRevenuBean.getLibelle());
		this.fraisProfessionnelList.add(s);

		this.idFraisProfSelected = lienRemunerationRevenuBean.getLibelle();
		changeNullToZero();

		if (modifFp != null) {
			cancelFraisProf();
		}

		modifFp = new LienRemunerationRevenuBean();
		modifFp.setActualisation(lienRemunerationRevenuBean.getActualisation());
		modifFp.setCommentaire(lienRemunerationRevenuBean.getCommentaire());
		if (lienRemunerationRevenuBean
				.getIdLienRemunerationRevenuComplementaire() != null
				&& lienRemunerationRevenuBean
						.getIdLienRemunerationRevenuComplementaire() != 0) {
			modifFp.setIdLienRemunerationRevenuComplementaire(lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire());
		} else {
			modifFp.setIdLienRemunerationRevenuComplementaire(0);
		}
		modifFp.setIdRevenuComplementaire(lienRemunerationRevenuBean
				.getIdRevenuComplementaire());
		modifFp.setLibelle(lienRemunerationRevenuBean.getLibelle());
		modifFp.setMontant(lienRemunerationRevenuBean.getMontant());
		modifFp.setMontantNPrec(lienRemunerationRevenuBean.getMontantNPrec());
		modifFp.setIdRemuneration(lienRemunerationRevenuBean
				.getIdRemuneration());

		this.fraisProfInventoryLienEditable.add(lienRemunerationRevenuBean);
		this.fraisProfInventoryLien.remove(lienRemunerationRevenuBean);

		this.totalNFraisProf = df.format(Double.valueOf(this.totalNFraisProf)
				- Double.valueOf(lienRemunerationRevenuBean.getMontant()));

		changeZeroToNull();
	}

	public void addCommission2(ActionEvent evt) {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean lienRemunerationRevenuBean = (LienRemunerationRevenuBean) table
				.getRowData();

		changeNullToZero();

		if (Double.valueOf(lienRemunerationRevenuBean.getMontant()) == 0
				&& Double
						.valueOf(lienRemunerationRevenuBean.getActualisation()) == 0) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le montant ou l'actualisation doit \u00eatre renseign\u00E9 et diff\u00E9rent de 0 pour \u00eatre enregistr\u00E9",
					"Le montant ou l'actualisation doit \u00eatre renseign\u00E9 et diff\u00E9rent de 0 pour \u00eatre enregistr\u00E9");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:dataTableEditableC",
					message);
			return;
		}

		RevenuComplementaireServiceImpl service = new RevenuComplementaireServiceImpl();
		int id = service.getIdFromRevenuComplementaire(
				lienRemunerationRevenuBean.getLibelle(), "commission",
				Integer.parseInt(session.getAttribute("groupe").toString()));
		if (id != 0) {
			if (this.actuCommission.contains(",")) {
				this.actuCommission = this.actuCommission.replace(",", ".");
			}
			if (this.montantNCommission.contains(",")) {
				this.montantNCommission = this.montantNCommission.replace(",",
						".");
			}
			this.actuCommission = df
					.format(Double.valueOf(this.actuCommission));
			this.montantNCommission = df.format(Double
					.valueOf(this.montantNCommission));
			lienRemunerationRevenuBean.setMontant(df.format(Double
					.valueOf(lienRemunerationRevenuBean.getMontant())));
			if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) == 0
					&& Double.valueOf(lienRemunerationRevenuBean.getMontant()) != 0) {
				lienRemunerationRevenuBean.setActualisation(df.format(Double
						.valueOf(lienRemunerationRevenuBean.getMontant())));
			} else {
				lienRemunerationRevenuBean
						.setActualisation(df.format(Double
								.valueOf(lienRemunerationRevenuBean
										.getActualisation())));
			}
			if (lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire() == null) {
				lienRemunerationRevenuBean
						.setIdLienRemunerationRevenuComplementaire(0);
			}

			this.commissionInventoryLienEditable.clear();
			this.commissionInventoryLien.add(lienRemunerationRevenuBean);
			this.totalNCommission = df.format(Double
					.valueOf(this.totalNCommission)
					+ Double.valueOf(lienRemunerationRevenuBean.getMontant()));
			if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) != 0) {
				this.totalActuCommission = df.format(Double
						.valueOf(this.totalActuCommission)
						+ Double.valueOf(lienRemunerationRevenuBean
								.getActualisation()));
			} else {
				this.totalActuCommission = df.format(Double
						.valueOf(this.totalActuCommission)
						+ Double.valueOf(lienRemunerationRevenuBean
								.getMontant()));
			}

			this.remunerationBruteAnnuelle = df.format(Double
					.valueOf(this.totalBrutAnnuel)
					+ Double.valueOf(this.totalNCommission)
					+ Double.valueOf(this.totalNPrimeFixe)
					+ Double.valueOf(this.totalNPrimeVariable)
					+ Double.valueOf(this.totalNAvantageAssujetti));

			this.remunerationBruteAnnuelleNPrec = df.format(Double
					.valueOf(this.totalBrutAnnuelAnneePrecedente)
					+ Double.valueOf(this.totalNPrecCommission)
					+ Double.valueOf(this.totalNPrecPrimeFixe)
					+ Double.valueOf(this.totalNPrecPrimeVariable)
					+ Double.valueOf(this.totalNPrecAvantageAssujetti));

			this.remunerationBruteAnnuelleActu = df.format(Double
					.valueOf(this.totalBrutAnnuelReact)
					+ Double.valueOf(this.totalActuCommission)
					+ Double.valueOf(this.totalActuPrimeFixe)
					+ Double.valueOf(this.totalActuPrimeVariable)
					+ Double.valueOf(this.totalActuAvantageAssujetti));

			this.remunerationGlobale = df.format(Double
					.valueOf(this.remunerationBruteAnnuelle)
					+ Double.valueOf(this.totalNAvantageNonAssujetti));

			this.remunerationGlobaleNPrec = df.format(Double
					.valueOf(this.remunerationBruteAnnuelleNPrec)
					+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

			this.remunerationGlobaleActu = df.format(Double
					.valueOf(this.remunerationBruteAnnuelleActu)
					+ Double.valueOf(this.totalActuAvantageNonAssujetti));

			if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
					&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
							.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
				if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationBruteAnnuelle) - Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationBruteAnnuelleEvol = df.format(Double
							.valueOf(total));
				} else {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationBruteAnnuelleActu) - Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationBruteAnnuelleEvol = df.format(Double
							.valueOf(total));
				}
			}
			if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
					&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
							.valueOf(this.remunerationGlobaleActu) != 0)) {
				if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationGlobale) - Double
							.valueOf(this.remunerationGlobaleNPrec)) / Double
							.valueOf(this.remunerationGlobaleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationGlobaleEvol = df.format(Double
							.valueOf(total));
				} else {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationGlobaleActu) - Double
							.valueOf(this.remunerationGlobaleNPrec)) / Double
							.valueOf(this.remunerationGlobaleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationGlobaleEvol = df.format(Double
							.valueOf(total));
				}
			}
		}
		modifC = null;
		changeZeroToNull();
		this.idCommissionSelected = "-1";

		for (SelectItem s : this.commissionList) {
			if (s.getLabel().equals(lienRemunerationRevenuBean.getLibelle())) {
				this.commissionList.remove(s);
				break;
			}
		}
	}

	public void addPrimeFixe(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean lienRemunerationRevenuBean = (LienRemunerationRevenuBean) table
				.getRowData();

		changeNullToZero();

		if (Double.valueOf(lienRemunerationRevenuBean.getMontant()) == 0
				&& Double
						.valueOf(lienRemunerationRevenuBean.getActualisation()) == 0) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le montant ou l'actualisation doit \u00eatre renseign\u00E9 et diff\u00E9rent de 0 pour \u00eatre enregistr\u00E9",
					"Le montant ou l'actualisation doit \u00eatre renseign\u00E9 et diff\u00E9rent de 0 pour \u00eatre enregistr\u00E9");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:dataTableEditablePf",
					message);
			return;
		}

		RevenuComplementaireServiceImpl service = new RevenuComplementaireServiceImpl();
		int id = service.getIdFromRevenuComplementaire(
				this.idPrimeFixeSelected, "prime_fixe",
				Integer.parseInt(session.getAttribute("groupe").toString()));

		if (id != 0) {
			if (this.actuPrimeFixe.contains(",")) {
				this.actuPrimeFixe = this.actuPrimeFixe.replace(",", ".");
			}
			if (this.montantNPrimeFixe.contains(",")) {
				this.montantNPrimeFixe = this.montantNPrimeFixe.replace(",",
						".");
			}
			this.actuPrimeFixe = df.format(Double.valueOf(this.actuPrimeFixe));
			this.montantNPrimeFixe = df.format(Double
					.valueOf(this.montantNPrimeFixe));

			lienRemunerationRevenuBean.setMontant(df.format(Double
					.valueOf(lienRemunerationRevenuBean.getMontant())));
			if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) == 0
					&& Double.valueOf(lienRemunerationRevenuBean.getMontant()) != 0) {
				lienRemunerationRevenuBean.setActualisation(df.format(Double
						.valueOf(lienRemunerationRevenuBean.getMontant())));
			} else {
				lienRemunerationRevenuBean
						.setActualisation(df.format(Double
								.valueOf(lienRemunerationRevenuBean
										.getActualisation())));
			}
			if (lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire() == null) {
				lienRemunerationRevenuBean
						.setIdLienRemunerationRevenuComplementaire(0);
			}
			this.primeFixeInventoryLienEditable.clear();

			this.primeFixeInventoryLien.add(lienRemunerationRevenuBean);

			this.totalNPrimeFixe = df.format(Double
					.valueOf(this.totalNPrimeFixe)
					+ Double.valueOf(lienRemunerationRevenuBean.getMontant()));
			if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) != 0.0) {
				this.totalActuPrimeFixe = df.format(Double
						.valueOf(this.totalActuPrimeFixe)
						+ Double.valueOf(lienRemunerationRevenuBean
								.getActualisation()));
			} else {
				this.totalActuPrimeFixe = df.format(Double
						.valueOf(this.totalActuPrimeFixe)
						+ Double.valueOf(lienRemunerationRevenuBean
								.getMontant()));
			}
		}

		this.remunerationBruteAnnuelle = df.format(Double
				.valueOf(this.totalBrutAnnuel)
				+ Double.valueOf(this.totalNCommission)
				+ Double.valueOf(this.totalNPrimeFixe)
				+ Double.valueOf(this.totalNPrimeVariable)
				+ Double.valueOf(this.totalNAvantageAssujetti));

		this.remunerationBruteAnnuelleNPrec = df.format(Double
				.valueOf(this.totalBrutAnnuelAnneePrecedente)
				+ Double.valueOf(this.totalNPrecCommission)
				+ Double.valueOf(this.totalNPrecPrimeFixe)
				+ Double.valueOf(this.totalNPrecPrimeVariable)
				+ Double.valueOf(this.totalNPrecAvantageAssujetti));

		this.remunerationBruteAnnuelleActu = df.format(Double
				.valueOf(this.totalBrutAnnuelReact)
				+ Double.valueOf(this.totalActuCommission)
				+ Double.valueOf(this.totalActuPrimeFixe)
				+ Double.valueOf(this.totalActuPrimeVariable)
				+ Double.valueOf(this.totalActuAvantageAssujetti));

		this.remunerationGlobale = df.format(Double
				.valueOf(this.remunerationBruteAnnuelle)
				+ Double.valueOf(this.totalNAvantageNonAssujetti));

		this.remunerationGlobaleNPrec = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleNPrec)
				+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

		this.remunerationGlobaleActu = df.format(Double
				.valueOf(this.remunerationBruteAnnuelleActu)
				+ Double.valueOf(this.totalActuAvantageNonAssujetti));

		if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
				&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
						.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
			if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelle) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationBruteAnnuelleActu) - Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
						.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationBruteAnnuelleEvol = df.format(Double
						.valueOf(total));
			}
		}
		if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
				&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
						.valueOf(this.remunerationGlobaleActu) != 0)) {
			if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobale) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			} else {
				double totalBrutEvol = ((Double
						.valueOf(this.remunerationGlobaleActu) - Double
						.valueOf(this.remunerationGlobaleNPrec)) / Double
						.valueOf(this.remunerationGlobaleNPrec)) * 100;
				double total = (double) ((totalBrutEvol * 100)) / 100;
				this.remunerationGlobaleEvol = df.format(Double.valueOf(total));
			}
		}

		modifPf = null;
		changeZeroToNull();
		this.idPrimeFixeSelected = "-1";

		for (SelectItem s : this.primeFixeList) {
			if (s.getLabel().equals(lienRemunerationRevenuBean.getLibelle())) {
				this.primeFixeList.remove(s);
				break;
			}
		}
	}

	public void addPrimeVariable(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean lienRemunerationRevenuBean = (LienRemunerationRevenuBean) table
				.getRowData();

		changeNullToZero();

		if (Double.valueOf(lienRemunerationRevenuBean.getMontant()) == 0
				&& Double
						.valueOf(lienRemunerationRevenuBean.getActualisation()) == 0) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le montant ou l'actualisation doit \u00eatre renseign\u00E9 et diff\u00E9rent de 0 pour \u00eatre enregistr\u00E9",
					"Le montant ou l'actualisation doit \u00eatre renseign\u00E9 et diff\u00E9rent de 0 pour \u00eatre enregistr\u00E9");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:dataTableEditablePv",
					message);
			return;
		}

		RevenuComplementaireServiceImpl service = new RevenuComplementaireServiceImpl();
		int id = service.getIdFromRevenuComplementaire(
				this.idPrimeVariableSelected, "prime_variable",
				Integer.parseInt(session.getAttribute("groupe").toString()));
		if (id != 0) {
			if (this.actuPrimeVariable.contains(",")) {
				this.actuPrimeVariable = this.actuPrimeVariable.replace(",",
						".");
			}
			if (this.montantNPrimeVariable.contains(",")) {
				this.montantNPrimeVariable = this.montantNPrimeVariable
						.replace(",", ".");
			}
			this.actuPrimeVariable = df.format(Double
					.valueOf(this.actuPrimeVariable));
			this.montantNPrimeVariable = df.format(Double
					.valueOf(this.montantNPrimeVariable));

			lienRemunerationRevenuBean.setMontant(df.format(Double
					.valueOf(lienRemunerationRevenuBean.getMontant())));
			if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) == 0
					&& Double.valueOf(lienRemunerationRevenuBean.getMontant()) != 0) {
				lienRemunerationRevenuBean.setActualisation(df.format(Double
						.valueOf(lienRemunerationRevenuBean.getMontant())));
			} else {
				lienRemunerationRevenuBean
						.setActualisation(df.format(Double
								.valueOf(lienRemunerationRevenuBean
										.getActualisation())));
			}
			if (lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire() == null) {
				lienRemunerationRevenuBean
						.setIdLienRemunerationRevenuComplementaire(0);
			}
			this.primeVariableInventoryLienEditable.clear();

			this.primeVariableInventoryLien.add(lienRemunerationRevenuBean);
			this.totalNPrimeVariable = df.format(Double
					.valueOf(this.totalNPrimeVariable)
					+ Double.valueOf(lienRemunerationRevenuBean.getMontant()));
			if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) != 0.0) {
				this.totalActuPrimeVariable = df.format(Double
						.valueOf(this.totalActuPrimeVariable)
						+ Double.valueOf(lienRemunerationRevenuBean
								.getActualisation()));
			} else {
				this.totalActuPrimeVariable = df.format(Double
						.valueOf(this.totalActuPrimeVariable)
						+ Double.valueOf(lienRemunerationRevenuBean
								.getMontant()));
			}

			this.remunerationBruteAnnuelle = df.format(Double
					.valueOf(this.totalBrutAnnuel)
					+ Double.valueOf(this.totalNCommission)
					+ Double.valueOf(this.totalNPrimeFixe)
					+ Double.valueOf(this.totalNPrimeVariable)
					+ Double.valueOf(this.totalNAvantageAssujetti));

			this.remunerationBruteAnnuelleNPrec = df.format(Double
					.valueOf(this.totalBrutAnnuelAnneePrecedente)
					+ Double.valueOf(this.totalNPrecCommission)
					+ Double.valueOf(this.totalNPrecPrimeFixe)
					+ Double.valueOf(this.totalNPrecPrimeVariable)
					+ Double.valueOf(this.totalNPrecAvantageAssujetti));

			this.remunerationBruteAnnuelleActu = df.format(Double
					.valueOf(this.totalBrutAnnuelReact)
					+ Double.valueOf(this.totalActuCommission)
					+ Double.valueOf(this.totalActuPrimeFixe)
					+ Double.valueOf(this.totalActuPrimeVariable)
					+ Double.valueOf(this.totalActuAvantageAssujetti));

			this.remunerationGlobale = df.format(Double
					.valueOf(this.remunerationBruteAnnuelle)
					+ Double.valueOf(this.totalNAvantageNonAssujetti));

			this.remunerationGlobaleNPrec = df.format(Double
					.valueOf(this.remunerationBruteAnnuelleNPrec)
					+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

			this.remunerationGlobaleActu = df.format(Double
					.valueOf(this.remunerationBruteAnnuelleActu)
					+ Double.valueOf(this.totalActuAvantageNonAssujetti));

			if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
					&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
							.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
				if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationBruteAnnuelle) - Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationBruteAnnuelleEvol = df.format(Double
							.valueOf(total));
				} else {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationBruteAnnuelleActu) - Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationBruteAnnuelleEvol = df.format(Double
							.valueOf(total));
				}
			}
			if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
					&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
							.valueOf(this.remunerationGlobaleActu) != 0)) {
				if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationGlobale) - Double
							.valueOf(this.remunerationGlobaleNPrec)) / Double
							.valueOf(this.remunerationGlobaleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationGlobaleEvol = df.format(Double
							.valueOf(total));
				} else {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationGlobaleActu) - Double
							.valueOf(this.remunerationGlobaleNPrec)) / Double
							.valueOf(this.remunerationGlobaleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationGlobaleEvol = df.format(Double
							.valueOf(total));
				}
			}
		}
		modifPv = null;
		changeZeroToNull();
		this.idPrimeVariableSelected = "-1";

		for (SelectItem s : this.primeVariableList) {
			if (s.getLabel().equals(lienRemunerationRevenuBean.getLibelle())) {
				this.primeVariableList.remove(s);
				break;
			}
		}
	}

	public void addAvantageAssujetti(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean lienRemunerationRevenuBean = (LienRemunerationRevenuBean) table
				.getRowData();

		changeNullToZero();

		if (Double.valueOf(lienRemunerationRevenuBean.getMontant()) == 0
				&& Double
						.valueOf(lienRemunerationRevenuBean.getActualisation()) == 0) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le montant ou l'actualisation doit \u00eatre renseign\u00E9 et diff\u00E9rent de 0 pour \u00eatre enregistr\u00E9",
					"Le montant ou l'actualisation doit \u00eatre renseign\u00E9 et diff\u00E9rent de 0 pour \u00eatre enregistr\u00E9");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:dataTableEditableAa",
					message);
			return;
		}

		RevenuComplementaireServiceImpl service = new RevenuComplementaireServiceImpl();
		int id = service.getIdFromRevenuComplementaire(
				this.idAvantageAssujettiSelected, "avantage_assujetti",
				Integer.parseInt(session.getAttribute("groupe").toString()));
		if (id != 0) {
			if (this.actuAvantageAssujetti.contains(",")) {
				this.actuAvantageAssujetti = this.actuAvantageAssujetti
						.replace(",", ".");
			}
			if (this.montantNAvantageAssujetti.contains(",")) {
				this.montantNAvantageAssujetti = this.montantNAvantageAssujetti
						.replace(",", ".");
			}
			this.actuAvantageAssujetti = df.format(Double
					.valueOf(this.actuAvantageAssujetti));
			this.montantNAvantageAssujetti = df.format(Double
					.valueOf(this.montantNAvantageAssujetti));

			lienRemunerationRevenuBean.setMontant(df.format(Double
					.valueOf(lienRemunerationRevenuBean.getMontant())));
			if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) == 0
					&& Double.valueOf(lienRemunerationRevenuBean.getMontant()) != 0) {
				lienRemunerationRevenuBean.setActualisation(df.format(Double
						.valueOf(lienRemunerationRevenuBean.getMontant())));
			} else {
				lienRemunerationRevenuBean
						.setActualisation(df.format(Double
								.valueOf(lienRemunerationRevenuBean
										.getActualisation())));
			}
			if (lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire() == null) {
				lienRemunerationRevenuBean
						.setIdLienRemunerationRevenuComplementaire(0);
			}
			this.avantageAssujettiInventoryLienEditable.clear();

			this.avantageAssujettiInventoryLien.add(lienRemunerationRevenuBean);
			this.totalNAvantageAssujetti = df.format(Double
					.valueOf(this.totalNAvantageAssujetti)
					+ Double.valueOf(lienRemunerationRevenuBean.getMontant()));
			if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) != 0.0) {
				this.totalActuAvantageAssujetti = df.format(Double
						.valueOf(this.totalActuAvantageAssujetti)
						+ Double.valueOf(lienRemunerationRevenuBean
								.getActualisation()));
			} else {
				this.totalActuAvantageAssujetti = df.format(Double
						.valueOf(this.totalActuAvantageAssujetti)
						+ Double.valueOf(lienRemunerationRevenuBean
								.getMontant()));
			}

			this.remunerationBruteAnnuelle = df.format(Double
					.valueOf(this.totalBrutAnnuel)
					+ Double.valueOf(this.totalNCommission)
					+ Double.valueOf(this.totalNPrimeFixe)
					+ Double.valueOf(this.totalNPrimeVariable)
					+ Double.valueOf(this.totalNAvantageAssujetti));

			this.remunerationBruteAnnuelleNPrec = df.format(Double
					.valueOf(this.totalBrutAnnuelAnneePrecedente)
					+ Double.valueOf(this.totalNPrecCommission)
					+ Double.valueOf(this.totalNPrecPrimeFixe)
					+ Double.valueOf(this.totalNPrecPrimeVariable)
					+ Double.valueOf(this.totalNPrecAvantageAssujetti));

			this.remunerationBruteAnnuelleActu = df.format(Double
					.valueOf(this.totalBrutAnnuelReact)
					+ Double.valueOf(this.totalActuCommission)
					+ Double.valueOf(this.totalActuPrimeFixe)
					+ Double.valueOf(this.totalActuPrimeVariable)
					+ Double.valueOf(this.totalActuAvantageAssujetti));

			this.remunerationGlobale = df.format(Double
					.valueOf(this.remunerationBruteAnnuelle)
					+ Double.valueOf(this.totalNAvantageNonAssujetti));

			this.remunerationGlobaleNPrec = df.format(Double
					.valueOf(this.remunerationBruteAnnuelleNPrec)
					+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

			this.remunerationGlobaleActu = df.format(Double
					.valueOf(this.remunerationBruteAnnuelleActu)
					+ Double.valueOf(this.totalActuAvantageNonAssujetti));

			if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
					&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
							.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
				if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationBruteAnnuelle) - Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationBruteAnnuelleEvol = df.format(Double
							.valueOf(total));
				} else {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationBruteAnnuelleActu) - Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationBruteAnnuelleEvol = df.format(Double
							.valueOf(total));
				}
			}
			if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
					&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
							.valueOf(this.remunerationGlobaleActu) != 0)) {
				if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationGlobale) - Double
							.valueOf(this.remunerationGlobaleNPrec)) / Double
							.valueOf(this.remunerationGlobaleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationGlobaleEvol = df.format(Double
							.valueOf(total));
				} else {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationGlobaleActu) - Double
							.valueOf(this.remunerationGlobaleNPrec)) / Double
							.valueOf(this.remunerationGlobaleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationGlobaleEvol = df.format(Double
							.valueOf(total));
				}
			}
		}
		modifAa = null;
		changeZeroToNull();
		this.idAvantageAssujettiSelected = "-1";

		for (SelectItem s : this.avantageAssujettiList) {
			if (s.getLabel().equals(lienRemunerationRevenuBean.getLibelle())) {
				this.avantageAssujettiList.remove(s);
				break;
			}
		}
	}

	public void addAvantageNonAssujetti(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean lienRemunerationRevenuBean = (LienRemunerationRevenuBean) table
				.getRowData();

		changeNullToZero();

		if (Double.valueOf(lienRemunerationRevenuBean.getMontant()) == 0
				&& Double
						.valueOf(lienRemunerationRevenuBean.getActualisation()) == 0) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le montant ou l'actualisation doit \u00eatre renseign\u00E9 et diff\u00E9rent de 0 pour \u00eatre enregistr\u00E9",
					"Le montant ou l'actualisation doit \u00eatre renseign\u00E9 et diff\u00E9rent de 0 pour \u00eatre enregistr\u00E9");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:dataTableEditableAna",
					message);
			return;
		}

		RevenuComplementaireServiceImpl service = new RevenuComplementaireServiceImpl();
		int id = service.getIdFromRevenuComplementaire(
				this.idAvantageNonAssujettiSelected, "avantage_non_assujetti",
				Integer.parseInt(session.getAttribute("groupe").toString()));
		if (id != 0) {
			if (this.actuAvantageNonAssujetti.contains(",")) {
				this.actuAvantageNonAssujetti = this.actuAvantageNonAssujetti
						.replace(",", ".");
			}
			if (this.montantNAvantageNonAssujetti.contains(",")) {
				this.montantNAvantageNonAssujetti = this.montantNAvantageNonAssujetti
						.replace(",", ".");
			}
			this.actuAvantageNonAssujetti = df.format(Double
					.valueOf(this.actuAvantageNonAssujetti));
			this.montantNAvantageNonAssujetti = df.format(Double
					.valueOf(this.montantNAvantageNonAssujetti));

			lienRemunerationRevenuBean.setMontant(df.format(Double
					.valueOf(lienRemunerationRevenuBean.getMontant())));
			if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) == 0
					&& Double.valueOf(lienRemunerationRevenuBean.getMontant()) != 0) {
				lienRemunerationRevenuBean.setActualisation(df.format(Double
						.valueOf(lienRemunerationRevenuBean.getMontant())));
			} else {
				lienRemunerationRevenuBean
						.setActualisation(df.format(Double
								.valueOf(lienRemunerationRevenuBean
										.getActualisation())));
			}
			if (lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire() == null) {
				lienRemunerationRevenuBean
						.setIdLienRemunerationRevenuComplementaire(0);
			}
			this.avantageNonAssujettiInventoryLienEditable.clear();

			this.avantageNonAssujettiInventoryLien
					.add(lienRemunerationRevenuBean);
			this.totalNAvantageNonAssujetti = df.format(Double
					.valueOf(this.totalNAvantageNonAssujetti)
					+ Double.valueOf(lienRemunerationRevenuBean.getMontant()));
			if (Double.valueOf(lienRemunerationRevenuBean.getActualisation()) != 0.0) {
				this.totalActuAvantageNonAssujetti = df.format(Double
						.valueOf(this.totalActuAvantageNonAssujetti)
						+ Double.valueOf(lienRemunerationRevenuBean
								.getActualisation()));
			} else {
				this.totalActuAvantageNonAssujetti = df.format(Double
						.valueOf(this.totalActuAvantageNonAssujetti)
						+ Double.valueOf(lienRemunerationRevenuBean
								.getMontant()));
			}

			this.remunerationBruteAnnuelle = df.format(Double
					.valueOf(this.totalBrutAnnuel)
					+ Double.valueOf(this.totalNCommission)
					+ Double.valueOf(this.totalNPrimeFixe)
					+ Double.valueOf(this.totalNPrimeVariable)
					+ Double.valueOf(this.totalNAvantageAssujetti));

			this.remunerationBruteAnnuelleNPrec = df.format(Double
					.valueOf(this.totalBrutAnnuelAnneePrecedente)
					+ Double.valueOf(this.totalNPrecCommission)
					+ Double.valueOf(this.totalNPrecPrimeFixe)
					+ Double.valueOf(this.totalNPrecPrimeVariable)
					+ Double.valueOf(this.totalNPrecAvantageAssujetti));

			this.remunerationBruteAnnuelleActu = df.format(Double
					.valueOf(this.totalBrutAnnuelReact)
					+ Double.valueOf(this.totalActuCommission)
					+ Double.valueOf(this.totalActuPrimeFixe)
					+ Double.valueOf(this.totalActuPrimeVariable)
					+ Double.valueOf(this.totalActuAvantageAssujetti));

			this.remunerationGlobale = df.format(Double
					.valueOf(this.remunerationBruteAnnuelle)
					+ Double.valueOf(this.totalNAvantageNonAssujetti));

			this.remunerationGlobaleNPrec = df.format(Double
					.valueOf(this.remunerationBruteAnnuelleNPrec)
					+ Double.valueOf(this.totalNPrecAvantageNonAssujetti));

			this.remunerationGlobaleActu = df.format(Double
					.valueOf(this.remunerationBruteAnnuelleActu)
					+ Double.valueOf(this.totalActuAvantageNonAssujetti));

			if (Double.valueOf(this.remunerationBruteAnnuelleNPrec) != 0
					&& (Double.valueOf(this.remunerationBruteAnnuelle) != 0 || Double
							.valueOf(this.remunerationBruteAnnuelleActu) != 0)) {
				if (Double.valueOf(this.remunerationBruteAnnuelleActu) == 0) {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationBruteAnnuelle) - Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationBruteAnnuelleEvol = df.format(Double
							.valueOf(total));
				} else {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationBruteAnnuelleActu) - Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) / Double
							.valueOf(this.remunerationBruteAnnuelleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationBruteAnnuelleEvol = df.format(Double
							.valueOf(total));
				}
			}
			if (Double.valueOf(this.remunerationGlobaleNPrec) != 0
					&& (Double.valueOf(this.remunerationGlobale) != 0 || Double
							.valueOf(this.remunerationGlobaleActu) != 0)) {
				if (Double.valueOf(this.remunerationGlobaleActu) == 0) {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationGlobale) - Double
							.valueOf(this.remunerationGlobaleNPrec)) / Double
							.valueOf(this.remunerationGlobaleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationGlobaleEvol = df.format(Double
							.valueOf(total));
				} else {
					double totalBrutEvol = ((Double
							.valueOf(this.remunerationGlobaleActu) - Double
							.valueOf(this.remunerationGlobaleNPrec)) / Double
							.valueOf(this.remunerationGlobaleNPrec)) * 100;
					double total = (double) ((totalBrutEvol * 100)) / 100;
					this.remunerationGlobaleEvol = df.format(Double
							.valueOf(total));
				}
			}
		}
		modifAna = null;
		changeZeroToNull();
		this.idAvantageNonAssujettiSelected = "-1";

		for (SelectItem s : this.avantageNonAssujettiList) {
			if (s.getLabel().equals(lienRemunerationRevenuBean.getLibelle())) {
				this.avantageNonAssujettiList.remove(s);
				break;
			}
		}
	}

	public void addFraisProf(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		LienRemunerationRevenuBean lienRemunerationRevenuBean = (LienRemunerationRevenuBean) table
				.getRowData();

		changeNullToZero();

		if (Double.valueOf(lienRemunerationRevenuBean.getMontant()) == 0) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le montant doit \u00eatre renseign\u00E9 et diff\u00E9rent de 0 pour \u00eatre enregistr\u00E9",
					"Le montant doit \u00eatre renseign\u00E9 et diff\u00E9rent de 0 pour \u00eatre enregistr\u00E9");
			FacesContext.getCurrentInstance().addMessage(
					"idSalarieForm:idSalarieTabSet:0:dataTableEditableFp",
					message);
			return;
		}

		RevenuComplementaireServiceImpl service = new RevenuComplementaireServiceImpl();
		int id = service.getIdFromFraisProf(
				lienRemunerationRevenuBean.getLibelle(), "frais_professionnel",
				Integer.parseInt(session.getAttribute("groupe").toString()));
		if (id != 0) {
			if (this.montantNFraisProf.contains(",")) {
				this.montantNFraisProf = this.montantNFraisProf.replace(",",
						".");
			}
			this.montantNFraisProf = df.format(Double
					.valueOf(this.montantNFraisProf));

			lienRemunerationRevenuBean.setMontant(df.format(Double
					.valueOf(lienRemunerationRevenuBean.getMontant())));
			if (lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire() == null) {
				lienRemunerationRevenuBean
						.setIdLienRemunerationRevenuComplementaire(0);
			}
			this.fraisProfInventoryLienEditable.clear();

			this.fraisProfInventoryLien.add(lienRemunerationRevenuBean);
			this.totalNFraisProf = df.format(Double
					.valueOf(this.totalNFraisProf)
					+ Double.valueOf(lienRemunerationRevenuBean.getMontant()));
		}
		modifFp = null;
		changeZeroToNull();
		this.idFraisProfSelected = "-1";

		for (SelectItem s : this.fraisProfessionnelList) {
			if (s.getLabel().equals(lienRemunerationRevenuBean.getLibelle())) {
				this.fraisProfessionnelList.remove(s);
				break;
			}
		}
	}

	public String download(ActionEvent e) {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[23];
		entete[0] = "ANNEE";
		entete[1] = "NOM";
		entete[2] = "PRENOM";
		entete[3] = "METIER";
		entete[4] = "CSP";
		entete[5] = "Echelon";
		entete[6] = "Niveau";
		entete[7] = "Coef.";
		entete[8] = "Horaire mensuel";
		entete[9] = "Taux horaire brut €";
		entete[10] = "Salaire brut mensuel €";
		entete[11] = "Augmentation collective";
		entete[12] = "Augmentation individuelle";
		entete[13] = "Heures supp.";
		entete[14] = "SALAIRE BRUT ANNUEL";
		entete[15] = "Commissions";
		entete[16] = "Primes fixes";
		entete[17] = "Primes variables";
		entete[18] = "Avantages assujettis";
		entete[19] = "REMUNERATION BRUTE ANNUELLE";
		entete[20] = "Avantages non assujettis";
		entete[21] = "TOTAL BRUT ANNUEL €";
		entete[22] = "Frais professionnels";

		SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
				.getCurrentInstance().getCurrentInstance().getExternalContext()
				.getSessionMap().get("salarieFormBB");

		try {
			eContext.getSessionMap().put("datatable",
					salarieFormBB.getRemunerationBeanList());
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Remuneration");

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public String getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(String coefficient) {
		this.coefficient = (coefficient != null && !coefficient.equals("")) ? coefficient
				: "";
	}

	public String getEchelon() {
		return echelon;
	}

	public void setEchelon(String echelon) {
		this.echelon = echelon;
	}

	public String getHoraire() {
		return horaire;
	}

	public void setHoraire(String horaire) {
		this.horaire = horaire;
	}

	public String getSalaireConventionelMinimum() {
		if (!salaireConventionelMinimum.equals("")) {
			String s = "";
			try {
				s = df.format(Double.valueOf(salaireConventionelMinimum));
			} catch (Exception e) {
				salaireConventionelMinimum = "0.00";
				s = "0.00";
			}
			return s;
		} else
			return salaireConventionelMinimum;
	}

	public void setSalaireConventionelMinimum(String salaireConventionelMinimum) {
		this.salaireConventionelMinimum = salaireConventionelMinimum;
	}

	public String getSalaireConventionelMinimumReact() {
		if (!salaireConventionelMinimumReact.equals("")) {
			try {
				return df.format(Double
						.valueOf(salaireConventionelMinimumReact));
			} catch (Exception e) {
				salaireConventionelMinimumReact = "0.00";
				return "0.00";
			}
		} else {
			return salaireConventionelMinimumReact;
		}
	}

	public void setSalaireConventionelMinimumReact(
			String salaireConventionelMinimumReact) {
		this.salaireConventionelMinimumReact = salaireConventionelMinimumReact;
	}

	public String getSalaireConventionelMinimumComm() {
		return salaireConventionelMinimumComm;
	}

	public void setSalaireConventionelMinimumComm(
			String salaireConventionelMinimumComm) {
		this.salaireConventionelMinimumComm = salaireConventionelMinimumComm;
	}

	public String getSalaireDeBase() {
		if (!salaireDeBase.equals("")) {
			try {
				return df.format(Double.valueOf(salaireDeBase));
			} catch (Exception e) {
				salaireDeBase = "0.00";
				return "0.00";
			}
		} else {
			return salaireDeBase;
		}
	}

	public void setSalaireDeBase(String salaireDeBase) {
		this.salaireDeBase = salaireDeBase;
	}

	public String getSalaireDeBaseReact() {
		if (!salaireDeBaseReact.equals("")) {
			try {
				return df.format(Double.valueOf(salaireDeBaseReact));
			} catch (Exception e) {
				salaireDeBaseReact = "0.00";
				return "0.00";
			}
		} else {
			return salaireDeBaseReact;
		}
	}

	public void setSalaireDeBaseReact(String salaireDeBaseReact) {
		this.salaireDeBaseReact = salaireDeBaseReact;
	}

	public String getSalaireDeBaseComm() {
		return salaireDeBaseComm;
	}

	public void setSalaireDeBaseComm(String salaireDeBaseComm) {
		this.salaireDeBaseComm = salaireDeBaseComm;
	}

	public String getAugmentationCollective() {
		if (!augmentationCollective.equals("")) {
			try {
				return df.format(Double.valueOf(augmentationCollective));
			} catch (Exception e) {
				augmentationCollective = "0.00";
				return "0.00";
			}
		} else {
			return augmentationCollective;
		}
	}

	public void setAugmentationCollective(String augmentationCollective) {
		this.augmentationCollective = augmentationCollective;
	}

	public String getAugmentationCollectiveReact() {
		if (!augmentationCollectiveReact.equals("")) {
			try {
				return df.format(Double.valueOf(augmentationCollectiveReact));
			} catch (Exception e) {
				augmentationCollectiveReact = "0.00";
				return "0.00";
			}
		} else {
			return augmentationCollectiveReact;
		}
	}

	public void setAugmentationCollectiveReact(
			String augmentationCollectiveReact) {
		this.augmentationCollectiveReact = augmentationCollectiveReact;
	}

	public String getAugmentationCollectiveComm() {
		return augmentationCollectiveComm;
	}

	public void setAugmentationCollectiveComm(String augmentationCollectiveComm) {
		this.augmentationCollectiveComm = augmentationCollectiveComm;
	}

	public String getAugmentationIndividuelle() {
		if (!augmentationIndividuelle.equals("")) {
			try {
				return df.format(Double.valueOf(augmentationIndividuelle));
			} catch (Exception e) {
				augmentationIndividuelle = "0.00";
				return "0.00";
			}
		} else {
			return augmentationIndividuelle;
		}
	}

	public void setAugmentationIndividuelle(String augmentationIndividuelle) {
		this.augmentationIndividuelle = augmentationIndividuelle;
	}

	public String getAugmentationIndividuelleReact() {
		if (!augmentationIndividuelleReact.equals("")) {
			try {
				return df.format(Double.valueOf(augmentationIndividuelleReact));
			} catch (Exception e) {
				augmentationIndividuelleReact = "0.00";
				return "0.00";
			}
		} else {
			return augmentationIndividuelleReact;
		}
	}

	public void setAugmentationIndividuelleReact(
			String augmentationIndividuelleReact) {
		this.augmentationIndividuelleReact = augmentationIndividuelleReact;
	}

	public String getAugmentationIndividuelleComm() {
		return augmentationIndividuelleComm;
	}

	public void setAugmentationIndividuelleComm(
			String augmentationIndividuelleComm) {
		this.augmentationIndividuelleComm = augmentationIndividuelleComm;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public boolean isModalRendered() {
		return modalRendered;
	}

	public void setModalRendered(boolean modalRendered) {
		this.modalRendered = modalRendered;
	}

	public String getMetier() {
		return metier;
	}

	public void setMetier(String metier) {
		this.metier = metier;
	}

	public int getMetierSelected() {
		return metierSelected;
	}

	public void setMetierSelected(int metierSelected) {
		this.metierSelected = metierSelected;
	}

	public String getTotalBrutAnnuel() {
		if (!totalBrutAnnuel.equals("")) {
			return df.format(Double.valueOf(totalBrutAnnuel));
		} else {
			return totalBrutAnnuel;
		}
	}

	public void setTotalBrutAnnuel(String totalBrutAnnuel) {
		this.totalBrutAnnuel = totalBrutAnnuel;
	}

	public String getTotalBrutAnnuelReact() {
		if (!totalBrutAnnuelReact.equals("")) {
			return df.format(Double.valueOf(totalBrutAnnuelReact));
		} else {
			return totalBrutAnnuelReact;
		}
	}

	public void setTotalBrutAnnuelReact(String totalBrutAnnuelReact) {
		this.totalBrutAnnuelReact = totalBrutAnnuelReact;
	}

	public String getTotalBrutAnnuelComm() {
		return totalBrutAnnuelComm;
	}

	public void setTotalBrutAnnuelComm(String totalBrutAnnuelComm) {
		this.totalBrutAnnuelComm = totalBrutAnnuelComm;
	}

	public String getSalaireConventionelMinimumAnneePrecedente() {
		if (!salaireConventionelMinimumAnneePrecedente.equals("")
				&& !salaireConventionelMinimumAnneePrecedente.equals("-")) {
			return salaireConventionelMinimumAnneePrecedente;
		} else {
			return salaireConventionelMinimumAnneePrecedente;
		}
	}

	public void setSalaireConventionelMinimumAnneePrecedente(
			String salaireConventionelMinimumAnneePrecedente) {
		this.salaireConventionelMinimumAnneePrecedente = salaireConventionelMinimumAnneePrecedente;
	}

	public String getSalaireDeBaseAnneePrecedente() {
		if (!salaireDeBaseAnneePrecedente.equals("")
				&& !salaireDeBaseAnneePrecedente.equals("-")) {
			return salaireDeBaseAnneePrecedente;
		} else {
			return salaireDeBaseAnneePrecedente;
		}
	}

	public void setSalaireDeBaseAnneePrecedente(
			String salaireDeBaseAnneePrecedente) {
		this.salaireDeBaseAnneePrecedente = salaireDeBaseAnneePrecedente;
	}

	public String getTotalBrutAnnuelAnneePrecedente() {
		if (!totalBrutAnnuelAnneePrecedente.equals("")
				&& !totalBrutAnnuelAnneePrecedente.equals("-")) {
			return df.format(Double.valueOf(totalBrutAnnuelAnneePrecedente));
		} else {
			return totalBrutAnnuelAnneePrecedente;
		}
	}

	public void setTotalBrutAnnuelAnneePrecedente(
			String totalBrutAnnuelAnneePrecedente) {
		this.totalBrutAnnuelAnneePrecedente = totalBrutAnnuelAnneePrecedente;
	}

	public String getAugmentationCollectiveAnneePrecedente() {
		if (!augmentationCollectiveAnneePrecedente.equals("")
				&& !augmentationCollectiveAnneePrecedente.equals("-")) {
			return augmentationCollectiveAnneePrecedente;
		} else {
			return augmentationCollectiveAnneePrecedente;
		}
	}

	public void setAugmentationCollectiveAnneePrecedente(
			String augmentationCollectiveAnneePrecedente) {
		this.augmentationCollectiveAnneePrecedente = augmentationCollectiveAnneePrecedente;
	}

	public String getAugmentationIndividuelleAnneePrecedente() {
		if (!augmentationIndividuelleAnneePrecedente.equals("")
				&& !augmentationIndividuelleAnneePrecedente.equals("-")) {
			return augmentationIndividuelleAnneePrecedente;
		} else {
			return augmentationIndividuelleAnneePrecedente;
		}
	}

	public void setAugmentationIndividuelleAnneePrecedente(
			String augmentationIndividuelleAnneePrecedente) {
		this.augmentationIndividuelleAnneePrecedente = augmentationIndividuelleAnneePrecedente;
	}

	public String getHeuresSup25AnneePrecedente() {
		if (!heuresSup25AnneePrecedente.equals("")
				&& !heuresSup25AnneePrecedente.equals("-")) {
			return heuresSup25AnneePrecedente;
		} else {
			return heuresSup25AnneePrecedente;
		}
	}

	public void setHeuresSup25AnneePrecedente(String heuresSup25AnneePrecedente) {
		this.heuresSup25AnneePrecedente = heuresSup25AnneePrecedente;
	}

	public String getHeuresSup25() {
		if (!heuresSup25.equals("")) {
			try {
				return df.format(Double.valueOf(heuresSup25));
			} catch (Exception e) {
				heuresSup25 = "0.00";
				return "0.00";
			}
		} else {
			return heuresSup25;
		}
	}

	public void setHeuresSup25(String heuresSup25) {
		this.heuresSup25 = heuresSup25;
	}

	public String getHeuresSup25React() {
		if (!heuresSup25React.equals("")) {
			try {
				return df.format(Double.valueOf(heuresSup25React));
			} catch (Exception e) {
				heuresSup25React = "0.00";
				return "0.00";
			}
		} else {
			return heuresSup25React;
		}
	}

	public void setHeuresSup25React(String heuresSup25React) {
		this.heuresSup25React = heuresSup25React;
	}

	public String getHeuresSup50AnneePrecedente() {
		if (!heuresSup50AnneePrecedente.equals("")
				&& !heuresSup50AnneePrecedente.equals("-")) {
			return heuresSup50AnneePrecedente;
		} else {
			return heuresSup50AnneePrecedente;
		}
	}

	public void setHeuresSup50AnneePrecedente(String heuresSup50AnneePrecedente) {
		this.heuresSup50AnneePrecedente = heuresSup50AnneePrecedente;
	}

	public String getHeuresSup50() {
		if (!heuresSup50.equals("")) {
			try {
				return df.format(Double.valueOf(heuresSup50));
			} catch (Exception e) {
				heuresSup50 = "0.00";
				return "0.00";
			}
		} else {
			return heuresSup50;
		}
	}

	public void setHeuresSup50(String heuresSup50) {
		this.heuresSup50 = heuresSup50;
	}

	public String getHeuresSup50React() {
		if (!heuresSup50React.equals("")) {
			try {
				return df.format(Double.valueOf(heuresSup50React));
			} catch (Exception e) {
				heuresSup50React = "0.00";
				return "0.00";
			}
		} else {
			return heuresSup50React;
		}
	}

	public void setHeuresSup50React(String heuresSup50React) {
		this.heuresSup50React = heuresSup50React;
	}

	public String getHeuresSup25Comm() {
		return heuresSup25Comm;
	}

	public void setHeuresSup25Comm(String heuresSup25Comm) {
		this.heuresSup25Comm = heuresSup25Comm;
	}

	public String getHeuresSup50Comm() {
		return heuresSup50Comm;
	}

	public void setHeuresSup50Comm(String heuresSup50Comm) {
		this.heuresSup50Comm = heuresSup50Comm;
	}

	public String getTaux() {
		if (!taux.equals("")) {
			try {
				return df.format(Double.valueOf(taux));
			} catch (Exception e) {
				taux = "0.00";
				return "0.00";
			}
		} else {
			return taux;
		}
	}

	public void setTaux(String taux) {
		this.taux = taux;
	}

	public String getSalaireBrut() {
		if (!salaireBrut.equals("")) {
			try {
				return df.format(Double.valueOf(salaireBrut));
			} catch (Exception e) {
				salaireBrut = "0.00";
				return "0.00";
			}
		} else {
			return salaireBrut;
		}
	}

	public void setSalaireBrut(String salaireBrut) {
		this.salaireBrut = salaireBrut;
	}

	public String getSalaireConvBrut() {
		if (!salaireConvBrut.equals("")) {
			try {
				return df.format(Double.valueOf(salaireConvBrut));
			} catch (Exception e) {
				salaireConvBrut = "0.00";
				return "0.00";
			}
		} else {
			return salaireConvBrut;
		}
	}

	public void setSalaireConvBrut(String salaireConvBrut) {
		this.salaireConvBrut = salaireConvBrut;
	}

	public String getSalaireConventionelMinimumEvol() {
		if (!salaireConventionelMinimumEvol.equals("")) {
			return df.format(Double.valueOf(salaireConventionelMinimumEvol))
					+ "%";
		} else {
			return "";
		}
	}

	public void setSalaireConventionelMinimumEvol(
			String salaireConventionelMinimumEvol) {
		this.salaireConventionelMinimumEvol = salaireConventionelMinimumEvol;
	}

	public String getSalaireDeBaseEvol() {
		if (!this.salaireDeBaseEvol.equals("")) {
			return df.format(Double.valueOf(salaireDeBaseEvol)) + "%";
		} else {
			return "";
		}
	}

	public void setSalaireDeBaseEvol(String salaireDeBaseEvol) {
		this.salaireDeBaseEvol = salaireDeBaseEvol;
	}

	public String getTotalBrutAnnuelEvol() {
		if (!this.totalBrutAnnuelEvol.equals("")) {
			return df.format(Double.valueOf(totalBrutAnnuelEvol)) + "%";
		} else {
			return "";
		}
	}

	public void setTotalBrutAnnuelEvol(String totalBrutAnnuelEvol) {
		this.totalBrutAnnuelEvol = totalBrutAnnuelEvol;
	}

	public String getAugmentationCollectiveEvol() {
		if (!this.augmentationCollectiveEvol.equals("")) {
			return df.format(Double.valueOf(augmentationCollectiveEvol)) + "%";
		} else {
			return "";
		}
	}

	public void setAugmentationCollectiveEvol(String augmentationCollectiveEvol) {
		this.augmentationCollectiveEvol = augmentationCollectiveEvol;
	}

	public String getAugmentationIndividuelleEvol() {
		if (!this.augmentationIndividuelleEvol.equals("")) {
			return df.format(Double.valueOf(augmentationIndividuelleEvol))
					+ "%";
		} else {
			return "";
		}
	}

	public void setAugmentationIndividuelleEvol(
			String augmentationIndividuelleEvol) {
		this.augmentationIndividuelleEvol = augmentationIndividuelleEvol;
	}

	public String getHeuresSup25Evol() {
		if (!this.heuresSup25Evol.equals("")) {
			return df.format(Double.valueOf(heuresSup25Evol)) + "%";
		} else {
			return "";
		}
	}

	public void setHeuresSup25Evol(String heuresSup25Evol) {
		this.heuresSup25Evol = heuresSup25Evol;
	}

	public String getHeuresSup50Evol() {
		if (!this.heuresSup50Evol.equals("")) {
			return df.format(Double.valueOf(heuresSup50Evol)) + "%";
		} else {
			return "";
		}
	}

	public void setHeuresSup50Evol(String heuresSup50Evol) {
		this.heuresSup50Evol = heuresSup50Evol;
	}

	public List<RevenuComplementaireBean> getRevenusComplementairesBeanList() {
		Collections.sort(revenusComplementairesBeanList);
		Collections.reverse(revenusComplementairesBeanList);
		return revenusComplementairesBeanList;
	}

	public void setRevenusComplementairesBeanList(
			List<RevenuComplementaireBean> revenusComplementairesBeanList) {
		this.revenusComplementairesBeanList = revenusComplementairesBeanList;
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

	public List<RevenuComplementaireBean> getPrimeVariableBeanList() {
		Collections.sort(primeVariableBeanList);
		Collections.reverse(primeVariableBeanList);
		return primeVariableBeanList;
	}

	public void setPrimeVariabelBeanList(
			List<RevenuComplementaireBean> primeVariableBeanList) {
		this.primeVariableBeanList = primeVariableBeanList;
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

	public List<SelectItem> getRevenusComplementairesList() {
		return revenusComplementairesList;
	}

	public void setRevenusComplementairesList(
			List<SelectItem> revenusComplementairesList) {
		this.revenusComplementairesList = revenusComplementairesList;
	}

	public List<SelectItem> getCommissionList() {
		return commissionList;
	}

	public void setCommissionList(List<SelectItem> commissionList) {
		this.commissionList = commissionList;
	}

	public List<SelectItem> getPrimeVariableList() {
		return primeVariableList;
	}

	public void setPrimeVariableList(List<SelectItem> primeVariableList) {
		this.primeVariableList = primeVariableList;
	}

	public List<SelectItem> getPrimeFixeList() {
		return primeFixeList;
	}

	public void setPrimeFixeList(List<SelectItem> primeFixeList) {
		this.primeFixeList = primeFixeList;
	}

	public List<SelectItem> getAvantageAssujettiList() {
		return avantageAssujettiList;
	}

	public void setAvantageAssujettiList(List<SelectItem> avantageAssujettiList) {
		this.avantageAssujettiList = avantageAssujettiList;
	}

	public List<SelectItem> getAvantageNonAssujettiList() {
		return avantageNonAssujettiList;
	}

	public void setAvantageNonAssujettiList(
			List<SelectItem> avantageNonAssujettiList) {
		this.avantageNonAssujettiList = avantageNonAssujettiList;
	}

	public String getIdCommissionSelected() {
		return idCommissionSelected;
	}

	public void setIdCommissionSelected(String idCommissionSelected) {
		this.idCommissionSelected = idCommissionSelected;
	}

	public String getIdPrimeVariableSelected() {
		return idPrimeVariableSelected;
	}

	public void setIdPrimeVariableSelected(String idPrimeVariableSelected) {
		this.idPrimeVariableSelected = idPrimeVariableSelected;
	}

	public String getIdPrimeFixeSelected() {
		return idPrimeFixeSelected;
	}

	public void setIdPrimeFixeSelected(String idPrimeFixeSelected) {
		this.idPrimeFixeSelected = idPrimeFixeSelected;
	}

	public String getIdAvantageAssujettiSelected() {
		return idAvantageAssujettiSelected;
	}

	public void setIdAvantageAssujettiSelected(
			String idAvantageAssujettiSelected) {
		this.idAvantageAssujettiSelected = idAvantageAssujettiSelected;
	}

	public String getIdAvantageNonAssujettiSelected() {
		return idAvantageNonAssujettiSelected;
	}

	public void setIdAvantageNonAssujettiSelected(
			String idAvantageNonAssujettiSelected) {
		this.idAvantageNonAssujettiSelected = idAvantageNonAssujettiSelected;
	}

	public int getIdRevenuComplementaireSelected() {
		return idRevenuComplementaireSelected;
	}

	public void setIdRevenuComplementaireSelected(
			int idRevenuComplementaireSelected) {
		this.idRevenuComplementaireSelected = idRevenuComplementaireSelected;
	}

	public List<SelectItem> getFraisProfessionnelList() {
		return fraisProfessionnelList;
	}

	public void setFraisProfessionnelList(
			List<SelectItem> fraisProfessionnelList) {
		this.fraisProfessionnelList = fraisProfessionnelList;
	}

	public String getIdFraisProfessionnelSelected() {
		return idFraisProfSelected;
	}

	public void setIdFraisProfessionnelSelected(
			String idFraisProfessionnelSelected) {
		this.idFraisProfSelected = idFraisProfessionnelSelected;
	}

	public List<RevenuComplementaireBean> getRevenuComplementaireInventory() {
		return revenuComplementaireInventory;
	}

	public void setRevenuComplementaireInventory(
			List<RevenuComplementaireBean> revenuComplementaireInventory) {
		this.revenuComplementaireInventory = revenuComplementaireInventory;
	}

	public void setPrimeVariableBeanList(
			List<RevenuComplementaireBean> primeVariableBeanList) {
		this.primeVariableBeanList = primeVariableBeanList;
	}

	public int getIdLienSelected() {
		return idLienSelected;
	}

	public void setIdLienSelected(int idLienSelected) {
		this.idLienSelected = idLienSelected;
	}

	public String getMontantNPrecCommission() {
		return montantNPrecCommission;
	}

	public void setMontantNPrecCommission(String montantNPrecCommission) {
		this.montantNPrecCommission = montantNPrecCommission;
	}

	public String getMontantNCommission() {
		if (!montantNCommission.equals("")) {
			try {
				return df.format(Double.valueOf(montantNCommission));
			} catch (Exception e) {
				montantNCommission = "0.00";
				return "0.00";
			}
		} else {
			return montantNCommission;
		}
	}

	public void setMontantNCommission(String montantNCommission) {
		this.montantNCommission = montantNCommission;
	}

	public String getActuCommission() {
		if (!actuCommission.equals("")) {
			try {
				return df.format(Double.valueOf(actuCommission));
			} catch (Exception e) {
				actuCommission = "0.00";
				return "0.00";
			}
		} else {
			return actuCommission;
		}
	}

	public void setActuCommission(String actuCommission) {
		this.actuCommission = actuCommission;
	}

	public String getComCommission() {
		return comCommission;
	}

	public void setComCommission(String comCommission) {
		this.comCommission = comCommission;
	}

	public String getMontantNPrecPrimeVariable() {
		return montantNPrecPrimeVariable;
	}

	public void setMontantNPrecPrimeVariable(String montantNPrecPrimeVariable) {
		this.montantNPrecPrimeVariable = montantNPrecPrimeVariable;
	}

	public String getMontantNPrimeVariable() {
		if (!montantNPrimeVariable.equals("")) {
			try {
				return df.format(Double.valueOf(montantNPrimeVariable));
			} catch (Exception e) {
				montantNPrimeVariable = "0.00";
				return "0.00";
			}
		} else {
			return montantNPrimeVariable;
		}
	}

	public void setMontantNPrimeVariable(String montantNPrimeVariable) {
		this.montantNPrimeVariable = montantNPrimeVariable;
	}

	public String getActuPrimeVariable() {
		if (!actuPrimeVariable.equals("")) {
			try {
				return df.format(Double.valueOf(actuPrimeVariable));
			} catch (Exception e) {
				actuPrimeVariable = "0.00";
				return "0.00";
			}
		} else {
			return actuPrimeVariable;
		}
	}

	public void setActuPrimeVariable(String actuPrimeVariable) {
		this.actuPrimeVariable = actuPrimeVariable;
	}

	public String getComPrimeVariable() {
		return comPrimeVariable;
	}

	public void setComPrimeVariable(String comPrimeVariable) {
		this.comPrimeVariable = comPrimeVariable;
	}

	public String getMontantNPrecPrimeFixe() {
		return montantNPrecPrimeFixe;
	}

	public void setMontantNPrecPrimeFixe(String montantNPrecPrimeFixe) {
		this.montantNPrecPrimeFixe = montantNPrecPrimeFixe;
	}

	public String getMontantNPrimeFixe() {
		if (!montantNPrimeFixe.equals("")) {
			try {
				return df.format(Double.valueOf(montantNPrimeFixe));
			} catch (Exception e) {
				montantNPrimeFixe = "0.00";
				return "0.00";
			}
		} else {
			return montantNPrimeFixe;
		}
	}

	public void setMontantNPrimeFixe(String montantNPrimeFixe) {
		this.montantNPrimeFixe = montantNPrimeFixe;
	}

	public String getActuPrimeFixe() {
		if (!actuPrimeFixe.equals("")) {
			try {
				return df.format(Double.valueOf(actuPrimeFixe));
			} catch (Exception e) {
				actuPrimeFixe = "0.00";
				return "0.00";
			}
		} else {
			return actuPrimeFixe;
		}
	}

	public void setActuPrimeFixe(String actuPrimeFixe) {
		this.actuPrimeFixe = actuPrimeFixe;
	}

	public String getComPrimeFixe() {
		return comPrimeFixe;
	}

	public void setComPrimeFixe(String comPrimeFixe) {
		this.comPrimeFixe = comPrimeFixe;
	}

	public String getMontantNPrecAvantageAssujetti() {
		return montantNPrecAvantageAssujetti;
	}

	public void setMontantNPrecAvantageAssujetti(
			String montantNPrecAvantageAssujetti) {
		this.montantNPrecAvantageAssujetti = montantNPrecAvantageAssujetti;
	}

	public String getMontantNAvantageAssujetti() {
		if (!montantNAvantageAssujetti.equals("")) {
			try {
				return df.format(Double.valueOf(montantNAvantageAssujetti));
			} catch (Exception e) {
				montantNAvantageAssujetti = "0.00";
				return "0.00";
			}
		} else {
			return montantNAvantageAssujetti;
		}
	}

	public void setMontantNAvantageAssujetti(String montantNAvantageAssujetti) {
		this.montantNAvantageAssujetti = montantNAvantageAssujetti;
	}

	public String getActuAvantageAssujetti() {
		if (!actuAvantageAssujetti.equals("")) {
			try {
				return df.format(Double.valueOf(actuAvantageAssujetti));
			} catch (Exception e) {
				actuAvantageAssujetti = "0.00";
				return "0.00";
			}
		} else {
			return actuAvantageAssujetti;
		}
	}

	public void setActuAvantageAssujetti(String actuAvantageAssujetti) {
		this.actuAvantageAssujetti = actuAvantageAssujetti;
	}

	public String getComAvantageAssujetti() {
		return comAvantageAssujetti;
	}

	public void setComAvantageAssujetti(String comAvantageAssujetti) {
		this.comAvantageAssujetti = comAvantageAssujetti;
	}

	public String getMontantNPrecAvantageNonAssujetti() {
		return montantNPrecAvantageNonAssujetti;
	}

	public void setMontantNPrecAvantageNonAssujetti(
			String montantNPrecAvantageNonAssujetti) {
		this.montantNPrecAvantageNonAssujetti = montantNPrecAvantageNonAssujetti;
	}

	public String getMontantNAvantageNonAssujetti() {
		if (!montantNAvantageNonAssujetti.equals("")) {
			try {
				return df.format(Double.valueOf(montantNAvantageNonAssujetti));
			} catch (Exception e) {
				montantNAvantageNonAssujetti = "0.00";
				return "0.00";
			}
		} else {
			return montantNAvantageNonAssujetti;
		}
	}

	public void setMontantNAvantageNonAssujetti(
			String montantNAvantageNonAssujetti) {
		this.montantNAvantageNonAssujetti = montantNAvantageNonAssujetti;
	}

	public String getActuAvantageNonAssujetti() {
		if (!actuAvantageNonAssujetti.equals("")) {
			try {
				return df.format(Double.valueOf(actuAvantageNonAssujetti));
			} catch (Exception e) {
				actuAvantageNonAssujetti = "0.00";
				return "0.00";
			}
		} else {
			return actuAvantageNonAssujetti;
		}
	}

	public void setActuAvantageNonAssujetti(String actuAvantageNonAssujetti) {
		this.actuAvantageNonAssujetti = actuAvantageNonAssujetti;
	}

	public String getComAvantageNonAssujetti() {
		return comAvantageNonAssujetti;
	}

	public void setComAvantageNonAssujetti(String comAvantageNonAssujetti) {
		this.comAvantageNonAssujetti = comAvantageNonAssujetti;
	}

	public String getMontantNPrecFraisProf() {
		return montantNPrecFraisProf;
	}

	public void setMontantNPrecFraisProf(String montantNPrecFraisProf) {
		this.montantNPrecFraisProf = montantNPrecFraisProf;
	}

	public String getMontantNFraisProf() {
		if (!montantNFraisProf.equals("")) {
			try {
				return df.format(Double.valueOf(montantNFraisProf));
			} catch (Exception e) {
				montantNFraisProf = "0.00";
				return "0.00";
			}
		} else {
			return montantNFraisProf;
		}
	}

	public void setMontantNFraisProf(String montantNFraisProf) {
		this.montantNFraisProf = montantNFraisProf;
	}

	public String getComFraisProf() {
		return comFraisProf;
	}

	public void setComFraisProf(String comFraisProf) {
		this.comFraisProf = comFraisProf;
	}

	public int getIdSalarie() {
		return idSalarie;
	}

	public void setIdSalarie(int idSalarie) {
		this.idSalarie = idSalarie;
	}

	public String getIdFraisProfSelected() {
		return idFraisProfSelected;
	}

	public void setIdFraisProfSelected(String idFraisProfSelected) {
		this.idFraisProfSelected = idFraisProfSelected;
	}

	public String getTotalNPrecCommission() {
		return df.format(Double.valueOf(totalNPrecCommission));
	}

	public void setTotalNPrecCommission(String totalNPrecCommission) {
		this.totalNPrecCommission = totalNPrecCommission;
	}

	public String getTotalNCommission() {
		return df.format(Double.valueOf(totalNCommission));
	}

	public void setTotalNCommission(String totalNCommission) {
		this.totalNCommission = totalNCommission;
	}

	public String getTotalActuCommission() {
		return df.format(Double.valueOf(totalActuCommission));
	}

	public void setTotalActuCommission(String totalActuCommission) {
		this.totalActuCommission = totalActuCommission;
	}

	public String getTotalNPrecPrimeVariable() {
		return df.format(Double.valueOf(totalNPrecPrimeVariable));
	}

	public void setTotalNPrecPrimeVariable(String totalNPrecPrimeVariable) {
		this.totalNPrecPrimeVariable = totalNPrecPrimeVariable;
	}

	public String getTotalNPrimeVariable() {
		return df.format(Double.valueOf(totalNPrimeVariable));
	}

	public void setTotalNPrimeVariable(String totalNPrimeVariable) {
		this.totalNPrimeVariable = totalNPrimeVariable;
	}

	public String getTotalActuPrimeVariable() {
		return df.format(Double.valueOf(totalActuPrimeVariable));
	}

	public void setTotalActuPrimeVariable(String totalActuPrimeVariable) {
		this.totalActuPrimeVariable = totalActuPrimeVariable;
	}

	public String getTotalNPrecPrimeFixe() {
		return df.format(Double.valueOf(totalNPrecPrimeFixe));
	}

	public void setTotalNPrecPrimeFixe(String totalNPrecPrimeFixe) {
		this.totalNPrecPrimeFixe = totalNPrecPrimeFixe;
	}

	public String getTotalNPrimeFixe() {
		return df.format(Double.valueOf(totalNPrimeFixe));
	}

	public void setTotalNPrimeFixe(String totalNPrimeFixe) {
		this.totalNPrimeFixe = totalNPrimeFixe;
	}

	public String getTotalActuPrimeFixe() {
		return df.format(Double.valueOf(totalActuPrimeFixe));
	}

	public void setTotalActuPrimeFixe(String totalActuPrimeFixe) {
		this.totalActuPrimeFixe = totalActuPrimeFixe;
	}

	public String getTotalNPrecAvantageAssujetti() {
		return df.format(Double.valueOf(totalNPrecAvantageAssujetti));
	}

	public void setTotalNPrecAvantageAssujetti(
			String totalNPrecAvantageAssujetti) {
		this.totalNPrecAvantageAssujetti = totalNPrecAvantageAssujetti;
	}

	public String getTotalNAvantageAssujetti() {
		return df.format(Double.valueOf(totalNAvantageAssujetti));
	}

	public void setTotalNAvantageAssujetti(String totalNAvantageAssujetti) {
		this.totalNAvantageAssujetti = totalNAvantageAssujetti;
	}

	public String getTotalActuAvantageAssujetti() {
		return df.format(Double.valueOf(totalActuAvantageAssujetti));
	}

	public void setTotalActuAvantageAssujetti(String totalActuAvantageAssujetti) {
		this.totalActuAvantageAssujetti = totalActuAvantageAssujetti;
	}

	public String getTotalNPrecAvantageNonAssujetti() {
		return df.format(Double.valueOf(totalNPrecAvantageNonAssujetti));
	}

	public void setTotalNPrecAvantageNonAssujetti(
			String totalNPrecAvantageNonAssujetti) {
		this.totalNPrecAvantageNonAssujetti = totalNPrecAvantageNonAssujetti;
	}

	public String getTotalNAvantageNonAssujetti() {
		return df.format(Double.valueOf(totalNAvantageNonAssujetti));
	}

	public void setTotalNAvantageNonAssujetti(String totalNAvantageNonAssujetti) {
		this.totalNAvantageNonAssujetti = totalNAvantageNonAssujetti;
	}

	public String getTotalActuAvantageNonAssujetti() {
		return df.format(Double.valueOf(totalActuAvantageNonAssujetti));
	}

	public void setTotalActuAvantageNonAssujetti(
			String totalActuAvantageNonAssujetti) {
		this.totalActuAvantageNonAssujetti = totalActuAvantageNonAssujetti;
	}

	public String getTotalNPrecFraisProf() {
		return df.format(Double.valueOf(totalNPrecFraisProf));
	}

	public void setTotalNPrecFraisProf(String totalNPrecFraisProf) {
		this.totalNPrecFraisProf = totalNPrecFraisProf;
	}

	public String getTotalNFraisProf() {
		return df.format(Double.valueOf(totalNFraisProf));
	}

	public void setTotalNFraisProf(String totalNFraisProf) {
		this.totalNFraisProf = totalNFraisProf;
	}

	public String getRemunerationBruteAnnuelle() {
		return df.format(Double.valueOf(remunerationBruteAnnuelle));
	}

	public void setRemunerationBruteAnnuelle(String remunerationBruteAnnuelle) {
		this.remunerationBruteAnnuelle = remunerationBruteAnnuelle;
	}

	public String getRemunerationGlobale() {
		return df.format(Double.valueOf(remunerationGlobale));
	}

	public void setRemunerationGlobale(String remunerationGlobale) {
		this.remunerationGlobale = remunerationGlobale;
	}

	public List<Double> getCommissionNPrecInventory() {
		return commissionNPrecInventory;
	}

	public void setCommissionNPrecInventory(
			List<Double> commissionNPrecInventory) {
		this.commissionNPrecInventory = commissionNPrecInventory;
	}

	public List<Double> getPrimeVariableNPrecInventory() {
		return primeVariableNPrecInventory;
	}

	public void setPrimeVariableNPrecInventory(
			List<Double> primeVariableNPrecInventory) {
		this.primeVariableNPrecInventory = primeVariableNPrecInventory;
	}

	public List<Double> getPrimeFixeNPrecInventory() {
		return primeFixeNPrecInventory;
	}

	public void setPrimeFixeNPrecInventory(List<Double> primeFixeNPrecInventory) {
		this.primeFixeNPrecInventory = primeFixeNPrecInventory;
	}

	public List<Double> getAvantageAssujettiNPrecInventory() {
		return avantageAssujettiNPrecInventory;
	}

	public void setAvantageAssujettiNPrecInventory(
			List<Double> avantageAssujettiNPrecInventory) {
		this.avantageAssujettiNPrecInventory = avantageAssujettiNPrecInventory;
	}

	public List<Double> getAvantageNonAssujettiNPrecInventory() {
		return avantageNonAssujettiNPrecInventory;
	}

	public void setAvantageNonAssujettiNPrecInventory(
			List<Double> avantageNonAssujettiNPrecInventory) {
		this.avantageNonAssujettiNPrecInventory = avantageNonAssujettiNPrecInventory;
	}

	public List<Double> getFraisProfNPrecInventory() {
		return fraisProfNPrecInventory;
	}

	public void setFraisProfNPrecInventory(List<Double> fraisProfNPrecInventory) {
		this.fraisProfNPrecInventory = fraisProfNPrecInventory;
	}

	public List<LienRemunerationRevenuBean> getCommissionInventoryLien() {
		Collections.sort(commissionInventoryLien,
				new Comparator<LienRemunerationRevenuBean>() {
					public int compare(LienRemunerationRevenuBean l1,
							LienRemunerationRevenuBean l2) {
						int eq = 0;
						if (Double.valueOf(l1.getMontant()) > Double.valueOf(l2
								.getMontant())) {
							eq = 1;
						}
						if (Double.valueOf(l1.getMontant()) < Double.valueOf(l2
								.getMontant())) {
							eq = -1;
						}
						return eq;
					}
				});
		Collections.reverse(commissionInventoryLien);

		return commissionInventoryLien;
	}

	public void setCommissionInventoryLien(
			List<LienRemunerationRevenuBean> commissionInventoryLien) {
		this.commissionInventoryLien = commissionInventoryLien;
	}

	public List<LienRemunerationRevenuBean> getPrimeVariableInventoryLien() {
		Collections.sort(primeVariableInventoryLien,
				new Comparator<LienRemunerationRevenuBean>() {
					public int compare(LienRemunerationRevenuBean l1,
							LienRemunerationRevenuBean l2) {
						int eq = 0;
						if (Double.valueOf(l1.getMontant()) > Double.valueOf(l2
								.getMontant())) {
							eq = 1;
						}
						if (Double.valueOf(l1.getMontant()) < Double.valueOf(l2
								.getMontant())) {
							eq = -1;
						}
						return eq;
					}
				});
		Collections.reverse(primeVariableInventoryLien);

		return primeVariableInventoryLien;
	}

	public void setPrimeVariableInventoryLien(
			List<LienRemunerationRevenuBean> primeVariableInventoryLien) {
		this.primeVariableInventoryLien = primeVariableInventoryLien;
	}

	public List<LienRemunerationRevenuBean> getPrimeFixeInventoryLien() {
		Collections.sort(primeFixeInventoryLien,
				new Comparator<LienRemunerationRevenuBean>() {
					public int compare(LienRemunerationRevenuBean l1,
							LienRemunerationRevenuBean l2) {
						int eq = 0;
						if (Double.valueOf(l1.getMontant()) > Double.valueOf(l2
								.getMontant())) {
							eq = 1;
						}
						if (Double.valueOf(l1.getMontant()) < Double.valueOf(l2
								.getMontant())) {
							eq = -1;
						}
						return eq;
					}
				});
		Collections.reverse(primeFixeInventoryLien);

		return primeFixeInventoryLien;
	}

	public void setPrimeFixeInventoryLien(
			List<LienRemunerationRevenuBean> primeFixeInventoryLien) {
		this.primeFixeInventoryLien = primeFixeInventoryLien;
	}

	public List<LienRemunerationRevenuBean> getAvantageAssujettiInventoryLien() {
		Collections.sort(avantageAssujettiInventoryLien,
				new Comparator<LienRemunerationRevenuBean>() {
					public int compare(LienRemunerationRevenuBean l1,
							LienRemunerationRevenuBean l2) {
						int eq = 0;
						if (Double.valueOf(l1.getMontant()) > Double.valueOf(l2
								.getMontant())) {
							eq = 1;
						}
						if (Double.valueOf(l1.getMontant()) < Double.valueOf(l2
								.getMontant())) {
							eq = -1;
						}
						return eq;
					}
				});
		Collections.reverse(avantageAssujettiInventoryLien);

		return avantageAssujettiInventoryLien;
	}

	public void setAvantageAssujettiInventoryLien(
			List<LienRemunerationRevenuBean> avantageAssujettiInventoryLien) {
		this.avantageAssujettiInventoryLien = avantageAssujettiInventoryLien;
	}

	public List<LienRemunerationRevenuBean> getAvantageNonAssujettiInventoryLien() {
		Collections.sort(avantageNonAssujettiInventoryLien,
				new Comparator<LienRemunerationRevenuBean>() {
					public int compare(LienRemunerationRevenuBean l1,
							LienRemunerationRevenuBean l2) {
						int eq = 0;
						if (Double.valueOf(l1.getMontant()) > Double.valueOf(l2
								.getMontant())) {
							eq = 1;
						}
						if (Double.valueOf(l1.getMontant()) < Double.valueOf(l2
								.getMontant())) {
							eq = -1;
						}
						return eq;
					}
				});
		Collections.reverse(avantageNonAssujettiInventoryLien);

		return avantageNonAssujettiInventoryLien;
	}

	public void setAvantageNonAssujettiInventoryLien(
			List<LienRemunerationRevenuBean> avantageNonAssujettiInventoryLien) {
		this.avantageNonAssujettiInventoryLien = avantageNonAssujettiInventoryLien;
	}

	public List<LienRemunerationRevenuBean> getFraisProfInventoryLien() {
		Collections.sort(fraisProfInventoryLien,
				new Comparator<LienRemunerationRevenuBean>() {
					public int compare(LienRemunerationRevenuBean l1,
							LienRemunerationRevenuBean l2) {
						int eq = 0;
						if (Double.valueOf(l1.getMontant()) > Double.valueOf(l2
								.getMontant())) {
							eq = 1;
						}
						if (Double.valueOf(l1.getMontant()) < Double.valueOf(l2
								.getMontant())) {
							eq = -1;
						}
						return eq;
					}
				});
		Collections.reverse(fraisProfInventoryLien);

		return fraisProfInventoryLien;
	}

	public void setFraisProfInventoryLien(
			List<LienRemunerationRevenuBean> fraisProfInventoryLien) {
		this.fraisProfInventoryLien = fraisProfInventoryLien;
	}

	public String getCsp() {
		return csp;
	}

	public void setCsp(String csp) {
		this.csp = csp;
	}

	public String getMontantNPrecString() {
		return montantNPrecString;
	}

	public void setMontantNPrecString(String montantNPrecString) {
		this.montantNPrecString = montantNPrecString;
	}

	public String getMontantNString() {
		return montantNString;
	}

	public void setMontantNString(String montantNString) {
		this.montantNString = montantNString;
	}

	public String getActuString() {
		return actuString;
	}

	public void setActuString(String actuString) {
		this.actuString = actuString;
	}

	public String getEvolString() {
		return evolString;
	}

	public void setEvolString(String evolString) {
		this.evolString = evolString;
	}

	public boolean isModif() {
		return modif;
	}

	public void setModif(boolean modif) {
		this.modif = modif;
	}

	public int getIdLienRevComp() {
		return idLienRevComp;
	}

	public void setIdLienRevComp(int idLienRevComp) {
		this.idLienRevComp = idLienRevComp;
	}

	public List<LienRemunerationRevenuBean> getCommissionInventoryLienEditable() {
		return commissionInventoryLienEditable;
	}

	public void setCommissionInventoryLienEditable(
			List<LienRemunerationRevenuBean> commissionInventoryLienEditable) {
		this.commissionInventoryLienEditable = commissionInventoryLienEditable;
	}

	public int getIdRevCompLien() {
		return idRevCompLien;
	}

	public void setIdRevCompLien(int idRevCompLien) {
		this.idRevCompLien = idRevCompLien;
	}

	public String getNiveau() {
		return niveau;
	}

	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}

	public boolean isAnneeOk() {
		return anneeOk;
	}

	public void setAnneeOk(boolean anneeOk) {
		this.anneeOk = anneeOk;
	}

	public List<LienRemunerationRevenuBean> getPrimeVariableInventoryLienEditable() {
		return primeVariableInventoryLienEditable;
	}

	public void setPrimeVariableInventoryLienEditable(
			List<LienRemunerationRevenuBean> primeVariableInventoryLienEditable) {
		this.primeVariableInventoryLienEditable = primeVariableInventoryLienEditable;
	}

	public List<LienRemunerationRevenuBean> getPrimeFixeInventoryLienEditable() {
		return primeFixeInventoryLienEditable;
	}

	public void setPrimeFixeInventoryLienEditable(
			List<LienRemunerationRevenuBean> primeFixeInventoryLienEditable) {
		this.primeFixeInventoryLienEditable = primeFixeInventoryLienEditable;
	}

	public List<LienRemunerationRevenuBean> getAvantageAssujettiInventoryLienEditable() {
		return avantageAssujettiInventoryLienEditable;
	}

	public void setAvantageAssujettiInventoryLienEditable(
			List<LienRemunerationRevenuBean> avantageAssujettiInventoryLienEditable) {
		this.avantageAssujettiInventoryLienEditable = avantageAssujettiInventoryLienEditable;
	}

	public List<LienRemunerationRevenuBean> getAvantageNonAssujettiInventoryLienEditable() {
		return avantageNonAssujettiInventoryLienEditable;
	}

	public void setAvantageNonAssujettiInventoryLienEditable(
			List<LienRemunerationRevenuBean> avantageNonAssujettiInventoryLienEditable) {
		this.avantageNonAssujettiInventoryLienEditable = avantageNonAssujettiInventoryLienEditable;
	}

	public List<LienRemunerationRevenuBean> getFraisProfInventoryLienEditable() {
		return fraisProfInventoryLienEditable;
	}

	public void setFraisProfInventoryLienEditable(
			List<LienRemunerationRevenuBean> fraisProfInventoryLienEditable) {
		this.fraisProfInventoryLienEditable = fraisProfInventoryLienEditable;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public String getFirstValuec() {
		return firstValuec;
	}

	public void setFirstValuec(String firstValuec) {
		this.firstValuec = firstValuec;
	}

	public String getFirstValuepf() {
		return firstValuepf;
	}

	public void setFirstValuepf(String firstValuepf) {
		this.firstValuepf = firstValuepf;
	}

	public String getFirstValuepv() {
		return firstValuepv;
	}

	public void setFirstValuepv(String firstValuepv) {
		this.firstValuepv = firstValuepv;
	}

	public String getFirstValueaa() {
		return firstValueaa;
	}

	public void setFirstValueaa(String firstValueaa) {
		this.firstValueaa = firstValueaa;
	}

	public String getFirstValueana() {
		return firstValueana;
	}

	public void setFirstValueana(String firstValueana) {
		this.firstValueana = firstValueana;
	}

	public String getFirstValuefp() {
		return firstValuefp;
	}

	public void setFirstValuefp(String firstValuefp) {
		this.firstValuefp = firstValuefp;
	}

	public boolean isChangeCouleur() {
		return changeCouleur;
	}

	public void setChangeCouleur(boolean changeCouleur) {
		this.changeCouleur = changeCouleur;
	}

	public boolean isMaj() {
		return maj;
	}

	public void setMaj(boolean maj) {
		this.maj = maj;
	}

	public LienRemunerationRevenuBean getModifC() {
		return modifC;
	}

	public void setModifC(LienRemunerationRevenuBean modifC) {
		this.modifC = modifC;
	}

	public LienRemunerationRevenuBean getModifPf() {
		return modifPf;
	}

	public void setModifPf(LienRemunerationRevenuBean modifPf) {
		this.modifPf = modifPf;
	}

	public LienRemunerationRevenuBean getModifPv() {
		return modifPv;
	}

	public void setModifPv(LienRemunerationRevenuBean modifPv) {
		this.modifPv = modifPv;
	}

	public LienRemunerationRevenuBean getModifAna() {
		return modifAna;
	}

	public void setModifAna(LienRemunerationRevenuBean modifAna) {
		this.modifAna = modifAna;
	}

	public LienRemunerationRevenuBean getModifFp() {
		return modifFp;
	}

	public void setModifFp(LienRemunerationRevenuBean modifFp) {
		this.modifFp = modifFp;
	}

	public LienRemunerationRevenuBean getModifAa() {
		return modifAa;
	}

	public void setModifAa(LienRemunerationRevenuBean modifAa) {
		this.modifAa = modifAa;
	}

	public String getRemunerationBruteAnnuelleNPrec() {
		return remunerationBruteAnnuelleNPrec;
	}

	public void setRemunerationBruteAnnuelleNPrec(
			String remunerationBruteAnnuelleNPrec) {
		this.remunerationBruteAnnuelleNPrec = remunerationBruteAnnuelleNPrec;
	}

	public String getRemunerationBruteAnnuelleActu() {
		return df.format(Double.valueOf(remunerationBruteAnnuelleActu));
	}

	public void setRemunerationBruteAnnuelleActu(
			String remunerationBruteAnnuelleActu) {
		this.remunerationBruteAnnuelleActu = remunerationBruteAnnuelleActu;
	}

	public String getRemunerationBruteAnnuelleEvol() {
		return df.format(Double.valueOf(remunerationBruteAnnuelleEvol)) + "%";
	}

	public void setRemunerationBruteAnnuelleEvol(
			String remunerationBruteAnnuelleEvol) {
		this.remunerationBruteAnnuelleEvol = remunerationBruteAnnuelleEvol;
	}

	public String getRemunerationGlobaleNPrec() {
		return remunerationGlobaleNPrec;
	}

	public void setRemunerationGlobaleNPrec(String remunerationGlobaleNPrec) {
		this.remunerationGlobaleNPrec = remunerationGlobaleNPrec;
	}

	public String getRemunerationGlobaleActu() {
		return df.format(Double.valueOf(remunerationGlobaleActu));
	}

	public void setRemunerationGlobaleActu(String remunerationGlobaleActu) {
		this.remunerationGlobaleActu = remunerationGlobaleActu;
	}

	public String getRemunerationGlobaleEvol() {
		return df.format(Double.valueOf(remunerationGlobaleEvol)) + "%";
	}

	public void setRemunerationGlobaleEvol(String remunerationGlobaleEvol) {
		this.remunerationGlobaleEvol = remunerationGlobaleEvol;
	}

	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}

}
