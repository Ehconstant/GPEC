package com.cci.gpec.web.backingBean.turnOver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.ParamsGenerauxBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.commons.TurnOverBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.ParamsGenerauxServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.icesoft.faces.context.effects.JavascriptContext;

public class TurnOverFormBB {

	private ArrayList<SelectItem> entrepriseList;
	private int idEntrepriseSelected;
	private int idServiceSelected;
	private ArrayList<SelectItem> servicesList;
	private ArrayList<TurnOverBean> turnOverBeanList;
	private Integer twoYearAgo;
	private Integer oneYearAgo;
	private Integer curYear;
	private int totalEntreeN2;
	private int totalEntreeN1;
	private int totalEntreeN;
	private int totalSortieN2;
	private int totalSortieN1;
	private int totalSortieN;
	private int maxWidth;
	private String tauxN;
	private String tauxN1;
	private String tauxN2;
	private boolean afficheErreur = false;
	private static String INDEFINI = "Ind\u00E9fini*";

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public TurnOverFormBB() throws Exception {
		init();
	}

	private void init() throws Exception {
		turnOverBeanList = new ArrayList<TurnOverBean>();

		// init entreprises
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
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("entreprise") != null) {
			this.idEntrepriseSelected = Integer.parseInt(session.getAttribute(
					"entreprise").toString());
		} else {
			this.idEntrepriseSelected = -1;
		}
		this.idServiceSelected = -1;
		this.afficheErreur = false;
		getTableTurnOver(this.idEntrepriseSelected, this.idServiceSelected);
	}

	public Integer getCurYear() {
		Calendar cal = new GregorianCalendar();
		Date d = new Date();
		cal.setTime(d);
		curYear = cal.get(Calendar.YEAR);
		return curYear;
	}

	public Integer getTwoYearAgo() {
		this.twoYearAgo = (getCurYear() - 2);
		return twoYearAgo;
	}

	public Integer getOneYearAgo() {
		this.oneYearAgo = (getCurYear() - 1);
		return oneYearAgo;
	}

	public int getIdEntrepriseSelected() {
		return idEntrepriseSelected;
	}

	public void setIdEntrepriseSelected(int idEntrepriseSelected) {
		this.idEntrepriseSelected = idEntrepriseSelected;
	}

	public int getIdServiceSelected() {
		return idServiceSelected;
	}

	public void setIdServiceSelected(int idServiceSelected) {
		this.idServiceSelected = idServiceSelected;
	}

	public ArrayList<SelectItem> getEntrepriseList() {
		return entrepriseList;
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
					if (idEntrepriseSelected == serviceBean.getIdEntreprise()) {

						servicesList.add(new SelectItem(serviceBean.getId(),
								serviceBean.getNom()));
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return servicesList;
	}

	public void recalculeDataTableTurnOver(ValueChangeEvent event)
			throws Exception {

		if (event.getComponent().getId().equals("entrepriseList")) {
			idEntrepriseSelected = Integer.parseInt(event.getNewValue()
					.toString());
			idServiceSelected = -1;
		} else {
			if (event.getComponent().getId().equals("ServiceList")) {
				idServiceSelected = Integer.parseInt(event.getNewValue()
						.toString());
			}
		}
		turnOverBeanList = new ArrayList<TurnOverBean>();
		getTableTurnOver(idEntrepriseSelected, idServiceSelected);

		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	public ArrayList<TurnOverBean> getTurnOverBeanList() {
		return turnOverBeanList;

	}

	private boolean contain(TurnOverBean to) {
		for (TurnOverBean turnO : turnOverBeanList) {
			if (turnO.getId() == to.getId()) {
				return true;
			}
		}
		return false;
	}

	private ParcoursBean getFirstParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		ParcoursBean pb = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			if (pb == null) {
				pb = parcour;
			}
			if (parcour.getDebutFonction().before(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}

	/**
	 * Retourne le decalage nécessaure pour centré pour le colspan
	 * 
	 * @param s
	 */
	private int getPosition(String s) {
		switch (s.length()) {
		case 9:
			return 27;

		case 8:
			return 28;

		case 7:
			return 29;

		case 6:
			return 30;

		case 5:
			return 31;

		case 4:
			return 34;

		case 3:
			return 37;

		case 2:
			return 41;

		case 1:
			return 45;

		default:
			return 1;
		}
	}

	private ParcoursBean getLastParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		ParcoursBean pb = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			if (pb == null) {
				pb = parcour;
			}
			if (parcour.getDebutFonction().after(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}

	public void getTableTurnOver(int idEntrep, int idServ) throws Exception {
		maxWidth = 0;
		if (this.idEntrepriseSelected <= 0) {
			// Il n'y a pas encore d'entreprise sélectionnée
			EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
			ArrayList<EntrepriseBean> entrepriseBeanList = (ArrayList<EntrepriseBean>) entrepriseService
					.getEntreprisesList(Integer.parseInt(session.getAttribute(
							"groupe").toString()));
			Collections.sort(entrepriseBeanList);
			totalEntreeN2 = 0;
			totalEntreeN1 = 0;
			totalEntreeN = 0;
			totalSortieN2 = 0;
			totalSortieN1 = 0;
			totalSortieN = 0;
			turnOverBeanList = new ArrayList<TurnOverBean>();
			for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
				TurnOverBean to = new TurnOverBean();
				to.setId(entrepriseBean.getId());
				if (!contain(to)) {
					to.setLibelle(entrepriseBean.getNom());
					if (entrepriseBean.getNom().length() > this.maxWidth)
						maxWidth = entrepriseBean.getNom().length();
					Integer[] entrees = new Integer[3];
					Integer[] sorties = new Integer[3];
					HashMap<String, Integer[]> hm = new HashMap<String, Integer[]>();
					hm = getEntreeSortieEntreprise(entrepriseBean.getId(),
							getCurYear());
					entrees = hm.get("entree");
					sorties = hm.get("sortie");

					int entreeN2 = entrees[2];
					int entreeN1 = entrees[1];
					int entreeN = entrees[0];
					int sortieN2 = sorties[2];
					int sortieN1 = sorties[1];
					int sortieN = sorties[0];

					totalEntreeN2 += entreeN2;
					totalEntreeN1 += entreeN1;
					totalEntreeN += entreeN;
					totalSortieN2 += sortieN2;
					totalSortieN1 += sortieN1;
					totalSortieN += sortieN;

					to.setEntreesN(entreeN);
					to.setEntreesN1(entreeN1);
					to.setEntreesN2(entreeN2);
					to.setSortiesN(sortieN);
					to.setSortiesN1(sortieN1);
					to.setSortiesN2(sortieN2);

					turnOverBeanList.add(to);
				}
			}
		} else {

			ServiceImpl service = new ServiceImpl();
			ArrayList<ServiceBean> serviceBeanList = (ArrayList<ServiceBean>) service
					.getServicesList(Integer.parseInt(session.getAttribute(
							"groupe").toString()));
			Collections.sort(serviceBeanList);
			totalEntreeN2 = 0;
			totalEntreeN1 = 0;
			totalEntreeN = 0;
			totalSortieN2 = 0;
			totalSortieN1 = 0;
			totalSortieN = 0;
			turnOverBeanList = new ArrayList<TurnOverBean>();
			if (this.idServiceSelected == -1) {
				// Initialise les entreprises
				for (ServiceBean serviceBean : serviceBeanList) {
					if (idEntrepriseSelected == serviceBean.getIdEntreprise()) {
						TurnOverBean to = new TurnOverBean();
						to.setId(serviceBean.getId());
						if (!contain(to)) {
							to.setLibelle(serviceBean.getNom());
							if (serviceBean.getNom().length() > this.maxWidth)
								maxWidth = serviceBean.getNom().length();

							Integer[] entrees = new Integer[3];
							Integer[] sorties = new Integer[3];
							HashMap<String, Integer[]> hm = new HashMap<String, Integer[]>();
							hm = getEntreeSortieService(serviceBean.getId(),
									getCurYear());
							entrees = hm.get("entree");
							sorties = hm.get("sortie");

							int entreeN2 = entrees[2];
							int entreeN1 = entrees[1];
							int entreeN = entrees[0];
							int sortieN2 = sorties[2];
							int sortieN1 = sorties[1];
							int sortieN = sorties[0];

							totalEntreeN2 += entreeN2;
							totalEntreeN1 += entreeN1;
							totalEntreeN += entreeN;
							totalSortieN2 += sortieN2;
							totalSortieN1 += sortieN1;
							totalSortieN += sortieN;

							to.setEntreesN(entreeN);
							to.setEntreesN1(entreeN1);
							to.setEntreesN2(entreeN2);
							to.setSortiesN(sortieN);
							to.setSortiesN1(sortieN1);
							to.setSortiesN2(sortieN2);

							turnOverBeanList.add(to);
						}
					}
				}
			} else {
				SalarieServiceImpl servSalar = new SalarieServiceImpl();
				List<SalarieBean> listSalar = servSalar.getSalariesList(Integer
						.parseInt(session.getAttribute("groupe").toString()));
				for (SalarieBean salarieBean : listSalar) {
					if ((salarieBean.getIdEntrepriseSelected() == idEntrep)
							&& (salarieBean.getIdServiceSelected() == idServ)) {
						for (ParcoursBean parcour : salarieBean
								.getParcoursBeanList()) {
							TurnOverBean t = new TurnOverBean();
							t.setId(parcour.getIdMetierSelected());
							if (!contain(t)) {
								t.setLibelle(parcour.getNomMetier());
								if (parcour.getNomMetier().length() > this.maxWidth)
									maxWidth = parcour.getNomMetier().length();

								Integer[] entrees = new Integer[3];
								Integer[] sorties = new Integer[3];
								HashMap<String, Integer[]> hm = new HashMap<String, Integer[]>();
								hm = getEntreeSortieMetier(
										parcour.getIdMetierSelected(),
										getCurYear());
								entrees = hm.get("entree");
								sorties = hm.get("sortie");

								int entreeN2 = entrees[2];
								int entreeN1 = entrees[1];
								int entreeN = entrees[0];
								int sortieN2 = sorties[2];
								int sortieN1 = sorties[1];
								int sortieN = sorties[0];

								totalEntreeN2 += entreeN2;
								totalEntreeN1 += entreeN1;
								totalEntreeN += entreeN;
								totalSortieN2 += sortieN2;
								totalSortieN1 += sortieN1;
								totalSortieN += sortieN;

								t.setEntreesN(entreeN);
								t.setEntreesN1(entreeN1);
								t.setEntreesN2(entreeN2);
								t.setSortiesN(sortieN);
								t.setSortiesN1(sortieN1);
								t.setSortiesN2(sortieN2);

								turnOverBeanList.add(t);
							}
							// }
						}
					}
				}
			}
		}
		if (idServiceSelected > 0) {
			Collections.sort(turnOverBeanList);
		}
		if (turnOverBeanList.size() > 0) {
			// Ajoute le total / année
			TurnOverBean to = new TurnOverBean();
			to.setFooter(true);
			to.setLibelle("Total / ann\u00E9e");
			to.setEntreesN(totalEntreeN);
			to.setEntreesN1(totalEntreeN1);
			to.setEntreesN2(totalEntreeN2);
			to.setSortiesN(totalSortieN);
			to.setSortiesN1(totalSortieN1);
			to.setSortiesN2(totalSortieN2);
			to.setStyle("font-weight:bold;font-size:9pt");
			turnOverBeanList.add(to);

			// Ajoute les taux

			to = new TurnOverBean();
			to.setFooter(true);

			to.setTaux(true);
			to.setLibelle("Taux de Turn over / ann\u00E9e");
			tauxN = calculerTaux(getCurYear(), totalEntreeN, totalSortieN);
			tauxN1 = calculerTaux(getOneYearAgo(), totalEntreeN1, totalSortieN1);
			tauxN2 = calculerTaux(getTwoYearAgo(), totalEntreeN2, totalSortieN2);

			String styleN1 = "font-weight:bold;font-size:9pt;position:relative;left:"
					+ getPosition(tauxN1) + "px;";
			String styleN = "font-weight:bold;font-size:9pt;position:relative;left:"
					+ getPosition(tauxN) + "px;";

			to.setStyleTauxN(styleN);
			to.setStyleTauxN1(styleN1);
			to.setTauxN(tauxN);
			to.setTauxN1(tauxN1);

			turnOverBeanList.add(to);
		}
	}

	private String calculerTaux(Integer year, int entree, int sortie)
			throws Exception {
		ParamsGenerauxServiceImpl serv = new ParamsGenerauxServiceImpl();
		ParamsGenerauxBean pg = null;
		List<ParamsGenerauxBean> listP = serv
				.getParamsGenerauxBeanListByIdEntreprise(idEntrepriseSelected);
		if (listP.size() == 0) {
			afficheErreur = true;
			return INDEFINI;
		} else {
			pg = listP.get(0);
		}
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2); // arrondi à 2 chiffres apres la
		// virgules
		df.setMinimumFractionDigits(0);
		df.setDecimalSeparatorAlwaysShown(false);
		String retour = "";
		Double effectif = null;

		if (year.intValue() == getTwoYearAgo()) {
			afficheErreur = true;
			retour = INDEFINI;
		}
		if (year.intValue() == getOneYearAgo()) {
			// On prend l'effectif moyen de l'année precedente
			effectif = pg.getEffectifMoyenAnN2();
			if (effectif != null) {
				if (effectif == 0) {
					afficheErreur = true;
					return INDEFINI;
				}
				double res = ((double) (entree + sortie) / 2)
						/ (double) effectif * 100;
				afficheErreur = false;
				retour = df.format(res);
			} else {
				afficheErreur = true;
				retour = INDEFINI;
			}
		}
		if (year.intValue() == getCurYear()) {
			// On prend l'effectif moyen de l'année precedente
			effectif = pg.getEffectifMoyenAnN1();
			if (effectif != null) {
				if (effectif == 0) {
					afficheErreur = true;
					return INDEFINI;
				}
				double res = ((double) (entree + sortie) / 2)
						/ (double) effectif * 100;
				afficheErreur = false;
				retour = df.format(res);
			} else {
				afficheErreur = true;
				retour = INDEFINI;
			}
		}

		return retour;
	}

	private HashMap<String, Integer[]> getEntreeSortieEntreprise(
			int entreprise, int year) throws Exception {
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBean> listSalarie = serv.getSalariesList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		int entree = 0;
		int sortie = 0;
		int entreeNMoins1 = 0;
		int sortieNMoins1 = 0;
		int entreeNMoins2 = 0;
		int sortieNMoins2 = 0;
		for (SalarieBean salarie : listSalarie) {
			ParcoursBean p = getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 7) {
				if (salarie.getIdEntrepriseSelected() == entreprise) {
					ParcoursBean firstP = getFirstParcours(salarie);
					ParcoursBean lastP = p;
					// Pour chaque salarié de l'entreprise
					if (firstP != null) {
						Calendar debut = new GregorianCalendar();
						debut.setTime(firstP.getDebutFonction());

						// On teste s'il est entré dans l'année recherchée
						if (debut.get(Calendar.YEAR) == year) {
							entree++;
						}
						if (debut.get(Calendar.YEAR) == year - 1) {
							entreeNMoins1++;
						}
						if (debut.get(Calendar.YEAR) == year - 2) {
							entreeNMoins2++;
						}
					}
					if (lastP != null) {
						// On teste s'il est sortie dans l'année recherchée
						if (lastP.getFinFonction() != null
						/*
						 * && !lastP.getFinFonction().equals(
						 * UtilsDate.FIN_NULL)
						 */) {
							Calendar fin = new GregorianCalendar();
							fin.setTime(lastP.getFinFonction());
							if (fin.get(Calendar.YEAR) == year) {
								sortie++;
							}
							if (fin.get(Calendar.YEAR) == year - 1) {
								sortieNMoins1++;
							}
							if (fin.get(Calendar.YEAR) == year - 2) {
								sortieNMoins2++;
							}
						}
					}
				}
			}
		}
		HashMap<String, Integer[]> hm = new HashMap<String, Integer[]>();
		Integer[] tabInt = new Integer[3];
		tabInt[0] = entree;
		tabInt[1] = entreeNMoins1;
		tabInt[2] = entreeNMoins2;
		hm.put("entree", tabInt);

		tabInt = new Integer[3];
		tabInt[0] = sortie;
		tabInt[1] = sortieNMoins1;
		tabInt[2] = sortieNMoins2;
		hm.put("sortie", tabInt);

		return hm;
	}

	private HashMap<String, Integer[]> getEntreeSortieService(int service,
			int year) throws Exception {
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBean> listSalarie = serv
				.getSalarieBeanListByIdEntreprise(idEntrepriseSelected);
		int entree = 0;
		int sortie = 0;
		int entreeNMoins1 = 0;
		int sortieNMoins1 = 0;
		int entreeNMoins2 = 0;
		int sortieNMoins2 = 0;
		for (SalarieBean salarie : listSalarie) {
			ParcoursBean p = getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 7) {
				if ((salarie.getIdEntrepriseSelected() == idEntrepriseSelected)
						&& (salarie.getIdServiceSelected() == service)) {
					ParcoursBean firstP = getFirstParcours(salarie);
					ParcoursBean lastP = p;
					// Pour chaque salarié de l'entreprise
					if (firstP != null) {
						Calendar debut = new GregorianCalendar();
						debut.setTime(firstP.getDebutFonction());

						// On teste s'il est entré dans l'année recherchée
						if (debut.get(Calendar.YEAR) == year) {
							entree++;
						}
						if (debut.get(Calendar.YEAR) == year - 1) {
							entreeNMoins1++;
						}
						if (debut.get(Calendar.YEAR) == year - 2) {
							entreeNMoins2++;
						}
					}
					if (lastP != null) {
						// On teste s'il est sortie dans l'année recherchée
						if (lastP.getFinFonction() != null
						/*
						 * && !lastP.getFinFonction().equals(
						 * UtilsDate.FIN_NULL)
						 */) {
							Calendar fin = new GregorianCalendar();
							fin.setTime(lastP.getFinFonction());
							if (fin.get(Calendar.YEAR) == year) {
								sortie++;
							}
							if (fin.get(Calendar.YEAR) == year - 1) {
								sortieNMoins1++;
							}
							if (fin.get(Calendar.YEAR) == year - 2) {
								sortieNMoins2++;
							}
						}
					}
				}
			}

		}
		HashMap<String, Integer[]> hm = new HashMap<String, Integer[]>();
		Integer[] tabInt = new Integer[3];
		tabInt[0] = entree;
		tabInt[1] = entreeNMoins1;
		tabInt[2] = entreeNMoins2;
		hm.put("entree", tabInt);
		tabInt = new Integer[3];
		tabInt[0] = sortie;
		tabInt[1] = sortieNMoins1;
		tabInt[2] = sortieNMoins2;
		hm.put("sortie", tabInt);

		return hm;
	}

	private HashMap<String, Integer[]> getEntreeSortieMetier(int metier,
			int year) throws Exception {
		SalarieServiceImpl serv = new SalarieServiceImpl();
		List<SalarieBean> listSalarie = serv
				.getSalarieBeanListByIdEntrepriseAndIdService(
						idEntrepriseSelected, idServiceSelected);
		int entree = 0;
		int sortie = 0;
		int entreeNMoins1 = 0;
		int sortieNMoins1 = 0;
		int entreeNMoins2 = 0;
		int sortieNMoins2 = 0;
		for (SalarieBean salarie : listSalarie) {
			ParcoursBean p = getLastParcours(salarie);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 7) {
				if ((salarie.getIdEntrepriseSelected() == this.idEntrepriseSelected)
						&& (this.idServiceSelected == salarie
								.getIdServiceSelected())) {

					ParcoursBean firstP = getFirstParcours(salarie);
					ParcoursBean lastP = p;
					// Pour chaque salarié de l'entreprise
					if ((firstP != null)
							&& (firstP.getIdMetierSelected() == metier)) {
						Calendar debut = new GregorianCalendar();
						debut.setTime(firstP.getDebutFonction());

						// On teste s'il est entré dans l'année recherchée
						if (debut.get(Calendar.YEAR) == year) {
							entree++;
						}
						if (debut.get(Calendar.YEAR) == year - 1) {
							entreeNMoins1++;
						}
						if (debut.get(Calendar.YEAR) == year - 2) {
							entreeNMoins2++;
						}
					}
					if ((lastP != null)
							&& (lastP.getIdMetierSelected() == metier)) {
						// On teste s'il est sortie dans l'année recherchée
						if (lastP.getFinFonction() != null
						/*
						 * && !lastP.getFinFonction().equals(
						 * UtilsDate.FIN_NULL)
						 */) {
							Calendar fin = new GregorianCalendar();
							fin.setTime(lastP.getFinFonction());
							if (fin.get(Calendar.YEAR) == year) {
								sortie++;
							}
							if (fin.get(Calendar.YEAR) == year - 1) {
								sortieNMoins1++;
							}
							if (fin.get(Calendar.YEAR) == year - 2) {
								sortieNMoins2++;
							}
						}
					}
				}
			}
		}
		HashMap<String, Integer[]> hm = new HashMap<String, Integer[]>();
		Integer[] tabInt = new Integer[3];
		tabInt[0] = entree;
		tabInt[1] = entreeNMoins1;
		tabInt[2] = entreeNMoins2;
		hm.put("entree", tabInt);
		tabInt = new Integer[3];
		tabInt[0] = sortie;
		tabInt[1] = sortieNMoins1;
		tabInt[2] = sortieNMoins2;
		hm.put("sortie", tabInt);

		return hm;
	}

	public boolean isAfficheErreur() {
		return afficheErreur;
	}

	public String download(ActionEvent e) {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[4];
		entete[0] = "Services / Metiers";
		entete[1] = getTwoYearAgo().toString();
		entete[2] = getOneYearAgo().toString();
		entete[3] = getCurYear().toString();

		ServiceImpl servServ = new ServiceImpl();

		try {
			eContext.getSessionMap().put("datatable", this.turnOverBeanList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Turn over");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
			if (idServiceSelected != -1) {
				eContext.getSessionMap()
						.put("nomService",
								servServ.getServiceBeanById(idServiceSelected)
										.getNom());
			} else {
				eContext.getSessionMap().put("nomService", null);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public boolean isAfficheMessage() {
		return (turnOverBeanList.size() == 0)
				&& (this.idEntrepriseSelected != -1);
	}

	public int getListSize() {
		return (turnOverBeanList == null) ? 0 : turnOverBeanList.size();
	}

	public int getMaxWidth() {
		int taillePx = (int) (maxWidth * 8);
		if (taillePx < 130) {
			return 130;
		}
		return taillePx;
	}

	public int getMaxWidthFooter() {
		return getMaxWidth();
	}
}
