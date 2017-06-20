package com.cci.gpec.web.backingBean.arrettravail;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.ArretTravailBean;
import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.ParamsGenerauxBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.commons.TypeAccidentBean;
import com.cci.gpec.metier.implementation.AccidentServiceImpl;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.ParamsGenerauxServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.metier.implementation.TypeAccidentServiceImpl;
import com.icesoft.faces.context.effects.JavascriptContext;

public class ArretTravailBB {

	private ArrayList<SelectItem> entrepriseList;
	private ArrayList<SelectItem> servicesList;
	private ArrayList<SelectItem> metiersList;
	private int idEntrepriseSelected;
	private int idServiceSelected;
	private int idMetierSelected;
	private ArrayList<ArretTravailBean> listBean;
	private int nbJourArretTwoYearAgo;
	private int nbJourArretOneYearAgo;
	private int nbJourArretCurYear;
	private String tauxGravTwoYearAgo;
	private String tauxGravOneYearAgo;
	private String tauxGravCurYear;
	private boolean afficheErreur = false;
	private static String INDEFINI = "Ind\u00E9fini*";

	private List<SalarieBean> salarieBeanListFact = new ArrayList<SalarieBean>();
	private boolean initList = false;

	private List<MetierBean> metierBeanListFact = new ArrayList<MetierBean>();
	private boolean initListMetier = false;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public ArretTravailBB() throws Exception {
		init();
	}

	public void change(ValueChangeEvent event) throws Exception {
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}

		if (event.getComponent().getId().equals("entrepriseListArret")) {
			idEntrepriseSelected = Integer.parseInt(event.getNewValue()
					.toString());
			idServiceSelected = -1;
			idMetierSelected = -1;
			metiersList = new ArrayList<SelectItem>();
			// On teste le cas ou il n'y aurait pas de service pour l'entreprise
			// selectionnée
			if ((idEntrepriseSelected != -1) && (servicesList.size() == 0)) {
				ResourceBundle bundle = ResourceBundle.getBundle("errors");
				FacesMessage message = new FacesMessage(
						bundle.getString("AfficheSansService"));
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage(
						"idForm:entrepriseListArret", message);
			}

		}
		if (event.getComponent().getId().equals("idServiceListArret")) {
			idServiceSelected = Integer
					.parseInt(event.getNewValue().toString());
			initMetiers();
			if ((idServiceSelected != -1) && (this.metiersList.size() == 0)) {
				FacesMessage message = new FacesMessage(
						"Il n'y a pas de m\u00E9tier correspondant au service.");
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage(
						"idForm:idServiceListArret", message);
			}
			idMetierSelected = -1;
		}
		if (event.getComponent().getId().equals("idMetierListArret")) {
			idMetierSelected = Integer.parseInt(event.getNewValue().toString());
		}

		initialiserListBean();

	}

	private void init() throws Exception {
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
		this.idMetierSelected = -1;
		initialiserListBean();
	}

	/**
	 * Initialise la liste d'items des metiers quand un service est selectionné
	 * 
	 * @throws Exception
	 */
	private void initMetiers() throws Exception {
		metiersList = new ArrayList<SelectItem>();
		SalarieServiceImpl service = new SalarieServiceImpl();
		ArrayList<SalarieBean> salarieBeanList;
		MetierServiceImpl serv = new MetierServiceImpl();

		if (initListMetier == false) {
			initListMetier = true;
			List<MetierBean> l = new ArrayList<MetierBean>();
			l = serv.getMetiersList(Integer.parseInt(session.getAttribute(
					"groupe").toString()));
			for (MetierBean m : l) {
				metierBeanListFact.add(m);
			}
		}

		try {
			salarieBeanList = (ArrayList<SalarieBean>) service
					.getSalariesList(Integer.parseInt(session.getAttribute(
							"groupe").toString()));
			for (SalarieBean salarieBean : salarieBeanList) {
				if ((idEntrepriseSelected == salarieBean
						.getIdEntrepriseSelected())
						&& (idServiceSelected == salarieBean
								.getIdServiceSelected())) {
					List<ParcoursBean> l = salarieBean.getParcoursBeanList();
					for (int k = 0; k < l.size(); k++) {
						// On ajoute les metiers du salarie si cela n'est pas
						// encore fait
						int idMet = l.get(k).getIdMetierSelected();
						MetierBean metBean = getMetierById(idMet);
						// serv.getMetierBeanById(idMet);
						if (!this.contain(idMet, this.metiersList)) {
							metiersList.add(new SelectItem(metBean.getId(),
									metBean.getNom()));
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getCurYear() {
		Calendar cal = new GregorianCalendar();
		Date d = new Date();
		cal.setTime(d);
		return cal.get(Calendar.YEAR);
	}

	public int getTwoYearAgo() {
		return (getCurYear() - 2);
	}

	public int getOneYearAgo() {
		return (getCurYear() - 1);
	}

	/**
	 * Initialise la liste de bean
	 * 
	 * @throws Exception
	 */
	private void initialiserListBean() throws Exception {
		TypeAccidentServiceImpl serv = new TypeAccidentServiceImpl();
		List<TypeAccidentBean> listTypeAccidentBean = serv
				.getTypeAccidentList();
		listBean = new ArrayList<ArretTravailBean>();
		nbJourArretCurYear = 0;
		nbJourArretOneYearAgo = 0;
		nbJourArretTwoYearAgo = 0;
		for (TypeAccidentBean typeAccidentBean : listTypeAccidentBean) {
			ArretTravailBean atb = new ArretTravailBean();

			atb.setId(typeAccidentBean.getId());
			atb.setNom(typeAccidentBean.getNom());
			int val3 = calculNbJourArret(getCurYear() - 2, typeAccidentBean);
			int val2 = calculNbJourArret(getCurYear() - 1, typeAccidentBean);
			int val1 = calculNbJourArret(getCurYear(), typeAccidentBean);

			nbJourArretCurYear += val1;
			nbJourArretOneYearAgo += val2;
			nbJourArretTwoYearAgo += val3;

			atb.setNbJourCurYear(val1);
			atb.setNbJourOneYearAgo(val2);
			atb.setNbJourTwoYearAgo(val3);

			listBean.add(atb);
		}

		ArretTravailBean atb = new ArretTravailBean();
		atb.setStyle("font-weight: bold ;");
		atb.setNom("Nombre de jours d'arr\u00eat du travail / ann\u00e9e");
		atb.setNbJourTwoYearAgo(this.nbJourArretTwoYearAgo);
		atb.setNbJourOneYearAgo(this.nbJourArretOneYearAgo);
		atb.setNbJourCurYear(this.nbJourArretCurYear);
		listBean.add(atb);

		ArretTravailBean atbean = new ArretTravailBean();
		atbean.setEspace(true);
		listBean.add(atbean);

		atb = new ArretTravailBean();
		atb.setStyle("font-weight: bold ;");
		atb.setNom("Taux de gravité des AT / année");
		atb.setBoolTitre(true);
		listBean.add(atb);

		// Initialisation des taux de gravités avec les nouvelles valeur des
		// nbJour
		tauxGravCurYear = getTauxGrav(getCurYear());
		tauxGravOneYearAgo = getTauxGrav(getOneYearAgo());
		tauxGravTwoYearAgo = getTauxGrav(getTwoYearAgo());

		atb = new ArretTravailBean();
		atb.setStyle("font-weight: bold ;");
		atb.setNom("Taux de gravit\u00e9 des AT / ann\u00e9e");
		atb.setBoolTaux(true);
		atb.setTauxN2(this.tauxGravTwoYearAgo);
		atb.setTauxN1(this.tauxGravOneYearAgo);
		atb.setTauxN(this.tauxGravCurYear);
		listBean.add(atb);

	}

	public MetierBean getMetierById(int id) {
		MetierBean result = new MetierBean();
		for (MetierBean s : metierBeanListFact) {
			if (s.getId() == id)
				result = s;
		}
		return result;
	}

	private MetierBean getMetierBySalarieAndDate(SalarieBean salarie, Date d)
			throws Exception {
		List<ParcoursBean> listParcour = salarie.getParcoursBeanList();
		MetierServiceImpl servMetier = new MetierServiceImpl();

		if (initListMetier == false) {
			initListMetier = true;
			List<MetierBean> l = new ArrayList<MetierBean>();
			l = servMetier.getMetiersList(Integer.parseInt(session
					.getAttribute("groupe").toString()));
			for (MetierBean m : l) {
				metierBeanListFact.add(m);
			}
		}

		for (int j = 0; j < listParcour.size(); j++) {
			ParcoursBean pb = listParcour.get(j);
			Calendar deb = new GregorianCalendar();
			deb.setTime(pb.getDebutFonction());
			Calendar fin = new GregorianCalendar();
			if (pb.getFinFonction() == null) {
				fin.setTime(new Date());
			} else {
				fin.setTime(pb.getFinFonction());
			}
			Calendar dateG = new GregorianCalendar();
			dateG.setTime(d);
			if (dateG.after(deb) && dateG.before(fin)) {
				return getMetierById(pb.getIdMetierSelected());
			}
		}
		return null;
	}

	public SalarieBean getSalarieById(int id) {
		SalarieBean result = new SalarieBean();
		for (SalarieBean s : salarieBeanListFact) {
			if (s.getId() == id) {
				result = s;
			}
		}
		return result;
	}

	private ParcoursBean getLastParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		ParcoursBean pb = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			// Test le 1er appel
			if (pb == null)
				pb = parcour;
			if (parcour.getDebutFonction().after(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}

	/**
	 * Calcule le nombre de jour d'arret pour un type d'accident dans une année
	 * 
	 * @param year
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	private int calculNbJourArret(int year, TypeAccidentBean bean)
			throws Exception {
		AccidentServiceImpl accidentServ = new AccidentServiceImpl();
		List<AccidentBean> listAccident = accidentServ.getAccidentsList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		SalarieServiceImpl servImpl = new SalarieServiceImpl();

		if (initList == false) {
			initList = true;
			List<SalarieBean> l = new ArrayList<SalarieBean>();
			l = servImpl.getSalariesList(Integer.parseInt(session.getAttribute(
					"groupe").toString()));
			for (SalarieBean s : l) {
				salarieBeanListFact.add(s);
			}
		}

		int res = 0;
		int nbJour = 0;
		GregorianCalendar acc = new GregorianCalendar();
		if (idEntrepriseSelected != -1) {
			if (idServiceSelected != -1) {
				if (idMetierSelected != -1) {
					for (int i = 0; i < listAccident.size(); i++) {
						// UN ACCIDENT A EU LIEU
						AccidentBean accident = listAccident.get(i);
						SalarieBean s = getSalarieById(accident.getIdSalarie());
						if (getLastParcours(s) != null
								&& getLastParcours(s)
										.getIdTypeContratSelected() != 3
								&& getLastParcours(s)
										.getIdTypeContratSelected() != 7) {
							if ((s.getIdEntrepriseSelected() == idEntrepriseSelected)
									&& (s.getIdServiceSelected() == idServiceSelected)) {
								// LE SALARIE APPARTIENT AU SERVICE ET A
								// L'ENTREPRISE, ON REGARDE SI SON METIER
								// CORRESPOND
								// A CELUI SELECTIONNE
								MetierBean met;
								if (accident.isInitial()) {
									met = getMetierBySalarieAndDate(s,
											accident.getDateAccident());
								} else {
									met = getMetierBySalarieAndDate(s,
											accident.getDateRechute());
								}
								if ((met != null)
										&& (met.getId() == idMetierSelected)) {
									if (accident.isInitial()) {
										acc.setTime(accident.getDateAccident());
									} else {
										acc.setTime(accident.getDateRechute());
									}
									// ON VERFIIE L ANNEE DE L ACCIDENT
									// RESTE A VERIFIER SI C'EST LE TYPE D
									// ACCIDENT
									if (accident
											.getIdTypeAccidentBeanSelected() == bean
											.getId()) {
										if (accident.isInitial()) {
											res = accident.getNombreJourArret();
										} else {
											res = accident
													.getNombreJourArretRechute();
										}
										nbJour += ventilation(res, acc, year);
									}

								}
							}
						}
						// }
					}
				} else {
					for (int i = 0; i < listAccident.size(); i++) {
						// UN ACCIDENT A EU LIEU
						AccidentBean accident = listAccident.get(i);
						// if (accident.isInitial()) {
						SalarieBean s = getSalarieById(accident.getIdSalarie());
						// servImpl.getSalarieBeanById(accident
						// .getIdSalarie());
						if (getLastParcours(s) != null
								&& getLastParcours(s)
										.getIdTypeContratSelected() != 3
								&& getLastParcours(s)
										.getIdTypeContratSelected() != 7) {
							if ((s.getIdEntrepriseSelected() == idEntrepriseSelected)
									&& (s.getIdServiceSelected() == idServiceSelected)) {
								if (accident.isInitial()) {
									acc.setTime(accident.getDateAccident());
								} else {
									acc.setTime(accident.getDateRechute());
								}

								// UN ACCIDENT A EU LIEU CETTE ANNEE, RESTE
								// A
								// VERIFIER SI C'EST LE TYPE D ACCIDENT
								if (accident.getIdTypeAccidentBeanSelected() == bean
										.getId()) {
									if (accident.isInitial()) {
										res = accident.getNombreJourArret();
									} else {
										res = accident
												.getNombreJourArretRechute();
									}
									nbJour += ventilation(res, acc, year);
								}
							}
						}
					}
				}
			} else {
				for (int i = 0; i < listAccident.size(); i++) {
					// UN ACCIDENT A EU LIEU
					AccidentBean accident = listAccident.get(i);
					// if (accident.isInitial()) {
					SalarieBean s = getSalarieById(accident.getIdSalarie());
					if (getLastParcours(s) != null
							&& getLastParcours(s).getIdTypeContratSelected() != 3
							&& getLastParcours(s).getIdTypeContratSelected() != 7) {
						if (s.getIdEntrepriseSelected() == idEntrepriseSelected) {
							if (accident.isInitial()) {
								acc.setTime(accident.getDateAccident());
							} else {
								acc.setTime(accident.getDateRechute());
							}

							// UN ACCIDENT A EU LIEU CETTE ANNEE, RESTE A
							// VERIFIER
							// SI C'EST LE TYPE D ACCIDENT
							if (accident.getIdTypeAccidentBeanSelected() == bean
									.getId()) {
								if (accident.isInitial())
									res = accident.getNombreJourArret();
								else
									res = accident.getNombreJourArretRechute();
								nbJour += ventilation(res, acc, year);
							}
						}
					}
				}
			}
		} else {
			for (int i = 0; i < listAccident.size(); i++) {
				// UN ACCIDENT A EU LIEU
				AccidentBean accident = listAccident.get(i);
				if (accident.isInitial()) {
					acc.setTime(accident.getDateAccident());
				} else {
					acc.setTime(accident.getDateRechute());
				}

				// UN ACCIDENT A EU LIEU CETTE ANNEE, RESTE A VERIFIER SI
				// C'EST
				// LE TYPE D ACCIDENT
				if (accident.getIdTypeAccidentBeanSelected() == bean.getId()) {
					if (accident.isInitial()) {
						res = accident.getNombreJourArret();
					} else {
						res = accident.getNombreJourArretRechute();
					}
					nbJour += ventilation(res, acc, year);
				}
			}
		}

		return nbJour;
	}

	private static int getNbrJourOuvre(Date d1, Date d2) {
		GregorianCalendar debut = new GregorianCalendar();
		GregorianCalendar fin = new GregorianCalendar();
		debut.setTime(d1);
		fin.setTime(d2);
		int nbrJour = 0;
		while (debut.compareTo(fin) != 0) {
			if (debut.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
					&& debut.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				nbrJour++;
			}
			debut.add(Calendar.DAY_OF_MONTH, 1);
		}
		if (nbrJour == 0) {
			return 1;
		}
		return nbrJour;
	}

	private int ventilation(int res, GregorianCalendar acc, int year) {
		int nbJour = 0;
		// Ventilation sur plusieurs années
		GregorianCalendar dateFinAccident = new GregorianCalendar();

		dateFinAccident.setTime(getDateFinCalendaire(acc.getTime(), res));

		int nbrJourOuvreYearAgo = 0;
		int nbrJourOuvreYearNext = 0;
		if (dateFinAccident.get(Calendar.YEAR) == year) {
			if (acc.get(Calendar.YEAR) == year) {
				nbJour += res;
			} else {
				Calendar calTmpDebut = new GregorianCalendar(year, 0, 1);
				Calendar calTmpFin = new GregorianCalendar(year - 1, 11, 31);
				nbrJourOuvreYearAgo = getNbrJourOuvre(acc.getTime(),
						calTmpFin.getTime());
				nbrJourOuvreYearNext = getNbrJourOuvre(calTmpDebut.getTime(),
						dateFinAccident.getTime());
				nbJour += getDuree(res, nbrJourOuvreYearAgo,
						nbrJourOuvreYearNext, 1);
			}

		} else {
			if ((acc.get(Calendar.YEAR) <= year)
					&& (year < dateFinAccident.get(Calendar.YEAR))) {
				GregorianCalendar calTmp = new GregorianCalendar(year, 11, 31);

				if (acc.get(Calendar.YEAR) == year) {
					Calendar calTmpDebut = new GregorianCalendar(year + 1, 0, 1);
					Calendar calTmpFin = new GregorianCalendar(year, 11, 31);
					nbrJourOuvreYearAgo = getNbrJourOuvre(acc.getTime(),
							calTmpFin.getTime());
					nbrJourOuvreYearNext = getNbrJourOuvre(
							calTmpDebut.getTime(), dateFinAccident.getTime());
					nbJour += getDuree(res, nbrJourOuvreYearAgo,
							nbrJourOuvreYearNext, 0);
				} else {
					GregorianCalendar calTmp2 = new GregorianCalendar(year, 0,
							1);
					nbJour += getNbrJourOuvre(calTmp2.getTime(),
							calTmp.getTime());
				}
			}
		}
		return nbJour;
	}

	private int getDuree(Integer nbrJourOuvre, int nbrJourOuvreYearAgo,
			int nbrJourOuvreYearNext, int periode) {
		if (periode == 0)
			return (nbrJourOuvre * nbrJourOuvreYearAgo)
					/ (nbrJourOuvreYearAgo + nbrJourOuvreYearNext);
		else
			return (nbrJourOuvre * nbrJourOuvreYearNext)
					/ (nbrJourOuvreYearAgo + nbrJourOuvreYearNext);
	}

	/**
	 * Détermine une fin calendaire en fonction du nombre de jours ouvrés
	 * 
	 * @param d1
	 *            date de début de l'accident
	 * @param nbrJourOuvre
	 *            Nombre de jours ouvrés
	 * @return Retourne une date de fin
	 */
	private Date getDateFinCalendaire(Date d1, int nbrJourOuvre) {
		GregorianCalendar debut = new GregorianCalendar();
		GregorianCalendar fin = new GregorianCalendar();
		debut.setTime(d1);
		fin.setTime(d1);
		while (nbrJourOuvre != 0) {
			if (fin.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
					&& fin.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				nbrJourOuvre--;
			}
			fin.add(Calendar.DAY_OF_MONTH, 1);
		}
		return fin.getTime();
	}

	/**
	 * Retourne le total de nombre de jour d'arret il y a 2 ans
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getNbJourArretTwoYearAgo() throws Exception {
		return nbJourArretTwoYearAgo;
	}

	/**
	 * Retourne le total de nombre de jour d'arret il y a 1 ans
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getNbJourArretOneYearAgo() throws Exception {
		return nbJourArretOneYearAgo;
	}

	/**
	 * Retourne le total de nombre de jour d'arret durant l'année courante
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getNbJourArretCurYear() throws Exception {
		return nbJourArretCurYear;
	}

	public ArrayList<ArretTravailBean> getListBean() throws Exception {
		return listBean;
	}

	/**
	 * Calcule le taux de gravité pour une année
	 * 
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public String getTauxGrav(int year) throws Exception {
		int nbJourArretWForArretWCurYear = 0;
		int nbJourArretWForArretWOneYearAgo = 0;
		int nbJourArretWForArretWTwoYearAgo = 0;
		for (ArretTravailBean arretTravailBean : listBean) {
			if (arretTravailBean.getId() == 1) {
				nbJourArretWForArretWCurYear = arretTravailBean
						.getNbJourCurYear();
				nbJourArretWForArretWOneYearAgo = arretTravailBean
						.getNbJourOneYearAgo();
				nbJourArretWForArretWTwoYearAgo = arretTravailBean
						.getNbJourTwoYearAgo();
			}
		}

		ParamsGenerauxServiceImpl serv = new ParamsGenerauxServiceImpl();
		ParamsGenerauxBean pg = null;
		if (idEntrepriseSelected != -1) {
			List<ParamsGenerauxBean> listP = serv
					.getParamsGenerauxBeanListByIdEntreprise(idEntrepriseSelected);
			if (listP.size() == 0) {
				afficheErreur = true;
				return INDEFINI;
			} else {
				pg = listP.get(0);
			}
		} else {
			afficheErreur = true;
			return INDEFINI;
		}
		double res = 0;
		double nbHeureArret = 0;
		if (year == this.getCurYear()) {
			Double val = pg.getDureeTravailAnNHeuresRealiseesEffectifTot();
			if (val != null) {
				nbHeureArret = val;
				if (nbHeureArret == 0) {
					afficheErreur = true;
					return INDEFINI;
				}
				res = (nbJourArretWForArretWCurYear / nbHeureArret) * 1000;
			}
		}
		if (year == this.getOneYearAgo()) {
			Double val = pg.getDureeTravailAnN1HeuresRealiseesEffectifTot();
			if (val != null) {
				nbHeureArret = val;
				if (nbHeureArret == 0) {
					afficheErreur = true;
					return INDEFINI;
				}
				res = (nbJourArretWForArretWOneYearAgo / nbHeureArret) * 1000;
			}
		}
		if (year == this.getTwoYearAgo()) {
			Double val = pg.getDureeTravailAnN2HeuresRealiseesEffectifTot();
			if (val != null) {
				nbHeureArret = val;
				if (nbHeureArret == 0) {
					afficheErreur = true;
					return INDEFINI;
				}
				res = (nbJourArretWForArretWTwoYearAgo / nbHeureArret) * 1000;
			}
		}

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2); // arrondi à 2 chiffres apres la
		// virgules
		df.setMinimumFractionDigits(0);
		df.setDecimalSeparatorAlwaysShown(false);
		afficheErreur = false;
		return df.format(res);
	}

	public String getTauxGravTwoYearAgo() throws Exception {
		return tauxGravTwoYearAgo;
	}

	public String getTauxGravOneYearAgo() throws Exception {
		return tauxGravOneYearAgo;
	}

	public String getTauxGravCurYear() throws Exception {
		return tauxGravCurYear;
	}

	/**
	 * Ce boolean permet ou non d'afficher l'erreur dans la vue si dans les
	 * parametres généraux, certaines valeurs ne seraient pas entrées
	 * 
	 * @return
	 */
	public boolean isAfficheErreur() {
		return afficheErreur;
	}

	/**
	 * Export Excel vers la servlet
	 * 
	 * @param e
	 * @return
	 */
	public String download(ActionEvent e) {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[4];
		entete[0] = "Nature";
		entete[1] = "" + getTwoYearAgo();
		entete[2] = "" + getOneYearAgo();
		entete[3] = "" + getCurYear();

		ServiceImpl servServ = new ServiceImpl();
		MetierServiceImpl metServ = new MetierServiceImpl();

		try {
			eContext.getSessionMap().put("datatable", this.listBean);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Arret de travail");
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
			if (idMetierSelected != -1) {
				eContext.getSessionMap().put("nomMetier",
						metServ.getMetierBeanById(idMetierSelected).getNom());
			} else {
				eContext.getSessionMap().put("nomMetier", null);
			}

			HashMap<String, String> mapTaux = new HashMap<String, String>();
			HashMap<String, Integer> mapTotaux = new HashMap<String, Integer>();

			mapTotaux.put("nbArretCurYear", nbJourArretCurYear);
			mapTotaux.put("nbArretOneYearAgo", nbJourArretOneYearAgo);
			mapTotaux.put("nbArretTwoYearAgo", nbJourArretTwoYearAgo);

			mapTaux.put("tauxGraviteTwoYearAgo", tauxGravTwoYearAgo);
			mapTaux.put("tauxGraviteOneYearAgo", tauxGravOneYearAgo);
			mapTaux.put("tauxGraviteCurYear", tauxGravCurYear);

			eContext.getSessionMap().put("mapTaux", mapTaux);
			eContext.getSessionMap().put("mapTotaux", mapTotaux);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public ArrayList<SelectItem> getEntrepriseList() {
		return entrepriseList;
	}

	public void setEntrepriseList(ArrayList<SelectItem> entrepriseList) {
		this.entrepriseList = entrepriseList;
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

	public void setServicesList(ArrayList<SelectItem> servicesList) {
		this.servicesList = servicesList;
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

	private boolean contain(int m, List<SelectItem> l) {
		for (int i = 0; i < l.size(); i++) {
			int val = (Integer) l.get(i).getValue();
			if (val == m) {
				return true;
			}
		}

		return false;
	}

	public ArrayList<SelectItem> getMetiersList() {

		if (metiersList != null) {
			Comparator<SelectItem> comparator = new Comparator<SelectItem>() {
				public int compare(SelectItem s1, SelectItem s2) {
					return s1.getLabel().compareTo(s2.getLabel());
				}
			};

			Collections.sort(metiersList, comparator);
		}

		return metiersList;
	}

	public void setMetiersList(ArrayList<SelectItem> metiersList) {
		this.metiersList = metiersList;
	}

	public int getIdMetierSelected() {
		return idMetierSelected;
	}

	public void setIdMetierSelected(int idMetierSelected) {
		this.idMetierSelected = idMetierSelected;
	}

}
