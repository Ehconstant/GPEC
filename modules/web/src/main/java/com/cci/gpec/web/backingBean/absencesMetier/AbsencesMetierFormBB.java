package com.cci.gpec.web.backingBean.absencesMetier;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AbsencesMetierBean;
import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.ParcoursServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.icesoft.faces.context.effects.JavascriptContext;

public class AbsencesMetierFormBB {

	// Les id mise à jour lors des selections
	private int idEntreprise = -1;
	private int idService = -1;
	private Double nbJourAbsenceCurYear;
	private Double nbJourAbsenceOneYearAgo;
	private Double nbJourAbsenceTwoYearAgo;
	private String nbJourAbsenceCurYearDisplay;
	private String nbJourAbsenceOneYearAgoDisplay;
	private String nbJourAbsenceTwoYearAgoDisplay;
	// Listes pour les selectonemenu
	private ArrayList<SelectItem> entreprisesListItem;
	private ArrayList<SelectItem> servicesListItem;
	private ArrayList<AbsencesMetierBean> listBean;
	private boolean afficheErreur = false;
	private boolean init = false;

	private List<MetierBean> metierBeanList;
	private List<AbsenceBean> absenceBeanList;
	private List<SalarieBean> salarieBeanList;

	private List<SalarieBean> salarieBeanListFact = new ArrayList<SalarieBean>();
	private boolean initList = false;
	private Map<Integer, MetierBean> mapMetier = new HashMap<Integer, MetierBean>();

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	// @PostConstruct
	// public void postConstruct() throws Exception {
	//
	// if (idEntreprise == -1) {
	// initialiserAllEntreprises();
	// }
	// // listBean a déja été initialisé lors des selections sauf au 1er appel
	// }

	public int getCurYear() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		return cal.get(Calendar.YEAR);
	}

	public int getTwoYearAgo() {
		return (getCurYear() - 2);
	}

	public int getOneYearAgo() {
		return (getCurYear() - 1);
	}

	public void change(ValueChangeEvent event) throws Exception {
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}

		if (event.getComponent().getId().equals("selectEntreprise")) {
			idEntreprise = Integer.parseInt(event.getNewValue().toString());
			idService = -1;

			initialiserEntreprise();
			// On teste le cas ou il n'y aurait pas de service pour l'entreprise
			// selectionnée
			if ((idEntreprise != -1) && (servicesListItem.size() == 0)) {
				ResourceBundle bundle = ResourceBundle.getBundle("errors");
				FacesMessage message = new FacesMessage(
						bundle.getString("AfficheSansService"));
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage(
						"idForm:idDataEntreprise1", message);
			} else {
				if (idEntreprise == -1) {
					initialiserAllEntreprises();
				}
			}

		} else {
			idService = Integer.parseInt(event.getNewValue().toString());
			if (idService != -1) {
				initialiserServices();
			} else {
				initialiserEntreprise();
			}
		}

	}

	/**
	 * Retourne le poste d'un salarie à une date donnée
	 * 
	 * @param salarie
	 * @param d
	 * @return
	 * @throws Exception
	 */
	private MetierBean getMetierBySalarieAndDate(SalarieBean salarie, Date d)
			throws Exception {
		MetierServiceImpl servMetier = new MetierServiceImpl();
		Calendar deb = new GregorianCalendar();
		Calendar fin = new GregorianCalendar();
		Calendar dateG = new GregorianCalendar();
		for (ParcoursBean parcoursBean : salarie.getParcoursBeanList()) {
			deb.setTime(parcoursBean.getDebutFonction());
			if (parcoursBean.getFinFonction() == null) {
				fin.setTime(new Date());
			} else {
				fin.setTime(parcoursBean.getFinFonction());
			}
			dateG.setTime(d);
			if (dateG.after(deb) && dateG.before(fin)) {
				return mapMetier.get(parcoursBean.getIdMetierSelected());
			}
		}
		return null;
	}

	/**
	 * Retourne le dernier parcours d'un salarie, null s'il ny en a pas
	 * 
	 * @param salarie
	 * @return
	 */
	private ParcoursBean getLastParcours(List<ParcoursBean> parcours) {

		ParcoursBean pb = null;
		for (ParcoursBean parcour : parcours) {
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

	private static Double getNbrJourOuvre(Date d1, Date d2) {
		GregorianCalendar debut = new GregorianCalendar();
		GregorianCalendar fin = new GregorianCalendar();
		debut.setTime(d1);
		fin.setTime(d2);
		Double nbrJour = 0.0;
		while (debut.compareTo(fin) < 0) {
			if (debut.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
					&& debut.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				nbrJour++;
			}
			debut.add(Calendar.DAY_OF_MONTH, 1);
		}
		if (nbrJour == 0) {
			return 1.0;
		}
		return nbrJour;
	}

	private Double getDuree(Double nbrJourOuvre, Double nbrJourOuvreYearAgo,
			Double nbrJourOuvreYearNext, int periode) {
		if (periode == 0) {
			return (nbrJourOuvre * nbrJourOuvreYearAgo)
					/ (nbrJourOuvreYearAgo + nbrJourOuvreYearNext);
		} else {
			return (nbrJourOuvre * nbrJourOuvreYearNext)
					/ (nbrJourOuvreYearAgo + nbrJourOuvreYearNext);
		}
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

	/**
	 * Retourne le nombre d'absence dans une année pour un metier
	 * 
	 * @param year
	 * @param metierP
	 * @return
	 * @throws Exception
	 */
	private Double nbJourAbsenceByMetier(int year, int metierP)
			throws Exception {
		SalarieServiceImpl servSalarie = new SalarieServiceImpl();

		if (initList == false) {
			initList = true;
			List<SalarieBean> l = new ArrayList<SalarieBean>();
			l = servSalarie.getSalariesList(Integer.parseInt(session
					.getAttribute("groupe").toString()));
			for (SalarieBean s : l) {
				salarieBeanListFact.add(s);
			}
		}

		Double res = 0.0;
		Calendar deb = new GregorianCalendar();
		Calendar fin = new GregorianCalendar();
		for (AbsenceBean ab : absenceBeanList) {
			if (ab.getIdTypeAbsenceSelected() < 8) {
				SalarieBean salarie = getSalarieById(ab.getIdSalarie());
				List<ParcoursBean> parcours = salarie.getParcoursBeanList();
				if (getLastParcours(parcours) != null
						&& getLastParcours(parcours).getIdTypeContratSelected() != 3
						&& getLastParcours(parcours).getIdTypeContratSelected() != 7) {
					if (idEntreprise != -1) {
						if (idService != -1) {
							if ((salarie.getIdEntrepriseSelected() == idEntreprise)
									&& (salarie.getIdServiceSelected() == idService)) {
								res = addResultat(year, metierP, res, deb, fin,
										ab, salarie);
							}
						} else {
							if (salarie.getIdEntrepriseSelected() == idEntreprise) {
								res = addResultat(year, metierP, res, deb, fin,
										ab, salarie);
							}
						}
					} else {
						res = addResultat(year, metierP, res, deb, fin, ab,
								salarie);
					}
				}
			}
		}
		return res;
	}

	/**
	 * @param year
	 * @param metierP
	 * @param res
	 * @param deb
	 * @param fin
	 * @param ab
	 * @param salarie
	 * @return
	 * @throws Exception
	 */
	private Double addResultat(int year, int metierP, Double res, Calendar deb,
			Calendar fin, AbsenceBean ab, SalarieBean salarie) throws Exception {
		if (ab.getDebutAbsence() != null && ab.getFinAbsence() != null) {
			Double nbrJourOuvreYearAgo = 0.0;
			Double nbrJourOuvreYearNext = 0.0;
			deb.setTime(ab.getDebutAbsence());
			if (ab.getFinAbsence() != null)
				fin.setTime(ab.getFinAbsence());

			MetierBean metierSalar;
			// la fin d'absence est bien dans cette année
			if (fin.get(Calendar.YEAR) == year) {
				// Année de début et de fin dans l'année courante, cas classique
				if (deb.get(Calendar.YEAR) == year) {
					// Absence durant l'année recherchée, reste a savoir si le
					// salarie absent travail pour ce metier
					metierSalar = getMetierBySalarieAndDate(salarie,
							ab.getDebutAbsence());
					if ((metierSalar != null)
							&& (metierSalar.getId() == metierP)) {
						res += ab.getNombreJourOuvre();
					}
					// année de début est l'année précédente à l'année actuelle
					// et
					// fin cette année
				} else {
					// Absence durant l'année recherchée, reste a savoir si le
					// salarie absent travail pour ce metier
					Calendar calTmpDebut = new GregorianCalendar(year, 0, 1);
					Calendar calTmpFin = new GregorianCalendar(year - 1, 11, 31);
					metierSalar = getMetierBySalarieAndDate(salarie,
							calTmpDebut.getTime());
					if ((metierSalar != null)
							&& (metierSalar.getId() == metierP)) {
						nbrJourOuvreYearAgo = getNbrJourOuvre(
								ab.getDebutAbsence(), calTmpFin.getTime());
						nbrJourOuvreYearNext = getNbrJourOuvre(
								calTmpDebut.getTime(), ab.getFinAbsence());
						res += getDuree(ab.getNombreJourOuvre(),
								nbrJourOuvreYearAgo, nbrJourOuvreYearNext, 1);
					}
				}
				// L'année de fin est l'année suivante
			} else {
				// Si l'année de fin est bien l'année suivante et celle de début
				// cette année ou l'année prédédente
				if ((deb.get(Calendar.YEAR) <= year)
						&& (year < fin.get(Calendar.YEAR))) {
					Calendar calTmp = new GregorianCalendar(year, 11, 31);

					// L'année de début est bien l'année courante, fin suivante
					if (deb.get(Calendar.YEAR) == year) {
						Calendar calTmpDebut = new GregorianCalendar(year + 1,
								0, 1);
						Calendar calTmpFin = new GregorianCalendar(year, 11, 31);
						metierSalar = getMetierBySalarieAndDate(salarie,
								calTmp.getTime());
						if ((metierSalar != null)
								&& (metierSalar.getId() == metierP)) {
							nbrJourOuvreYearAgo = getNbrJourOuvre(
									ab.getDebutAbsence(), calTmpFin.getTime());
							nbrJourOuvreYearNext = getNbrJourOuvre(
									calTmpDebut.getTime(), ab.getFinAbsence());
							res += getDuree(ab.getNombreJourOuvre(),
									nbrJourOuvreYearAgo, nbrJourOuvreYearNext,
									0);
						}
						// Cas particulier, on prend toute l'année en compte,
						// l'absence s'étend sur plus d'un an
					} else {
						Calendar calTmp2 = new GregorianCalendar(year, 0, 1);
						metierSalar = getMetierBySalarieAndDate(salarie,
								calTmp.getTime());
						if ((metierSalar != null)
								&& (metierSalar.getId() == metierP)) {
							res += getNbrJourOuvre(calTmp2.getTime(),
									calTmp.getTime());
						}
					}
				}
			}
		}
		return res;
	}

	/**
	 * Initialise les métiers pour toutes les entreprises
	 * 
	 * @throws Exception
	 */
	public void initialiserAllEntreprises() throws Exception {

		// initialisation de la liste des absences;
		AbsenceServiceImpl serv = new AbsenceServiceImpl();

		absenceBeanList = serv.getAbsencesList(Integer.parseInt(session
				.getAttribute("groupe").toString()));

		// initialise la liste des services
		SalarieServiceImpl serviceSalarie = new SalarieServiceImpl();
		salarieBeanList = serviceSalarie.getSalariesList(Integer
				.parseInt(session.getAttribute("groupe").toString()));

		EntrepriseServiceImpl servEntreprise = new EntrepriseServiceImpl();
		entreprisesListItem = new ArrayList<SelectItem>();
		GroupeServiceImpl grServ = new GroupeServiceImpl();
		SelectItem selectItem = new SelectItem();
		GroupeBean groupe = grServ.getGroupeBeanById(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		selectItem = new SelectItem();
		selectItem.setValue(-1);
		selectItem.setLabel(groupe.getNom());
		entreprisesListItem.add(selectItem);
		for (EntrepriseBean eb : servEntreprise.getEntreprisesList(Integer
				.parseInt(session.getAttribute("groupe").toString()))) {
			entreprisesListItem.add(new SelectItem(eb.getId(), eb.getNom()));
		}

		// Initialisation de la liste des metiers
		MetierServiceImpl entrepServ = new MetierServiceImpl();
		metierBeanList = entrepServ.getMetiersList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		Collections.sort(metierBeanList);

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("entreprise") != null) {
			idEntreprise = Integer.parseInt(session.getAttribute("entreprise")
					.toString());
			initialiserEntreprise();
			init = true;
		}
		{
			getAbsenceMetierBean();

			// Initialisation des items

			init = true;
		}

	}

	/**
	 * @throws Exception
	 */
	private void getAbsenceMetierBean() throws Exception {

		listBean = new ArrayList<AbsencesMetierBean>();
		nbJourAbsenceCurYear = 0.0;
		nbJourAbsenceOneYearAgo = 0.0;
		nbJourAbsenceTwoYearAgo = 0.0;

		Map<String, Double[]> tab = new HashMap<String, Double[]>();

		for (MetierBean metierBean : metierBeanList) {
			mapMetier.put(metierBean.getId(), metierBean);
			Double[] tabInt = new Double[3];
			tabInt[0] = nbJourAbsenceByMetier(getCurYear(), metierBean.getId());
			tabInt[1] = nbJourAbsenceByMetier(getOneYearAgo(),
					metierBean.getId());
			tabInt[2] = nbJourAbsenceByMetier(getTwoYearAgo(),
					metierBean.getId());
			tab.put(metierBean.getNom(), tabInt);
		}

		for (MetierBean metierbean : metierBeanList) {
			Double val1 = tab.get(metierbean.getNom())[0];
			Double val2 = tab.get(metierbean.getNom())[1];
			Double val3 = tab.get(metierbean.getNom())[2];
			nbJourAbsenceCurYear += val1;
			nbJourAbsenceOneYearAgo += val2;
			nbJourAbsenceTwoYearAgo += val3;
			listBean.add(getAbsencesMetierBean(metierbean, val1, val2, val3));
		}
	}

	/**
	 * @param metbean
	 * @param val1
	 * @param val2
	 * @param val3
	 * @param ab
	 */
	private AbsencesMetierBean getAbsencesMetierBean(MetierBean metbean,
			Double val1, Double val2, Double val3) {

		AbsencesMetierBean ab = new AbsencesMetierBean();
		ab.setNbAbsenceByMetierCurYear(val1);
		ab.setNbAbsenceByMetierOneYearAgo(val2);
		ab.setNbAbsenceByMetierTwoYearAgo(val3);
		ab.setId(metbean.getId());
		ab.setNom(metbean.getNom());

		return ab;
	}

	/**
	 * Initialise les métiers pour l'entreprise selectionnée
	 * 
	 * @throws Exception
	 */
	public void initialiserEntreprise() throws Exception {
		listBean = new ArrayList<AbsencesMetierBean>();
		nbJourAbsenceCurYear = 0.0;
		nbJourAbsenceOneYearAgo = 0.0;
		nbJourAbsenceTwoYearAgo = 0.0;

		Map<String, Double[]> tab = new HashMap<String, Double[]>();

		for (MetierBean metierBean : metierBeanList) {
			Double[] tabInt = new Double[3];
			tabInt[0] = nbJourAbsenceByMetier(getCurYear(), metierBean.getId());
			tabInt[1] = nbJourAbsenceByMetier(getOneYearAgo(),
					metierBean.getId());
			tabInt[2] = nbJourAbsenceByMetier(getTwoYearAgo(),
					metierBean.getId());
			tab.put(metierBean.getNom(), tabInt);
		}

		for (MetierBean metierbean : metierBeanList) {
			if (isInEntreprise(metierbean.getId(), idEntreprise)) {
				Double val1 = tab.get(metierbean.getNom())[0];
				Double val2 = tab.get(metierbean.getNom())[1];
				Double val3 = tab.get(metierbean.getNom())[2];
				nbJourAbsenceCurYear += val1;
				nbJourAbsenceOneYearAgo += val2;
				nbJourAbsenceTwoYearAgo += val3;
				listBean.add(getAbsencesMetierBean(metierbean, val1, val2, val3));
			}
		}
		servicesListItem = new ArrayList<SelectItem>();
		ServiceImpl servImpl = new ServiceImpl();
		List<ServiceBean> liste = servImpl.getServicesList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		for (ServiceBean service : liste) {
			if (service.getIdEntreprise() == idEntreprise) {
				servicesListItem.add(new SelectItem(service.getId(), service
						.getNom()));
			}
		}

	}

	/**
	 * Teste si un employé ayant pour poste le metier "idmetier" travaille pour
	 * l'entreprise "idEnt"
	 * 
	 * @param idmetier
	 * @param idEnt
	 * @return
	 * @throws Exception
	 */
	private boolean isInEntreprise(int idMetier, int idEntreprise)
			throws Exception {
		for (SalarieBean s : salarieBeanList) {
			if (s.getIdEntrepriseSelected() == idEntreprise) {

				MetierBean metSalar = getMetierBeanByIdSalarie(s.getId());
				if ((metSalar != null) && (metSalar.getId() == idMetier)) {
					for (ParcoursBean pb : s.getParcoursBeanList()) {
						if (pb.getIdMetierSelected() == idMetier) {
							return true;
						}
					}
				}

			}
		}
		return false;
	}

	/**
	 * Teste si un employé ayant pour poste le metier "met" travail dans le
	 * service et l'entreprise
	 * 
	 * @param met
	 * @param idSer
	 * @param idEnt
	 * @return
	 * @throws Exception
	 */
	private boolean isInService(int idMetier, int idEntreprise, int idService)
			throws Exception {
		for (SalarieBean s : salarieBeanList) {
			if ((s.getIdServiceSelected() == idService)
					&& (s.getIdEntrepriseSelected() == idEntreprise)) {

				MetierBean metSalar = getMetierBeanByIdSalarie(s.getId());
				if ((metSalar != null) && (metSalar.getId() == idMetier)) {
					for (ParcoursBean pb : s.getParcoursBeanList()) {
						if (pb.getIdMetierSelected() == idMetier) {
							return true;
						}
					}
				}

			}
		}
		return false;
	}

	/**
	 * Initialise les métiers pour l'entreprise et le service selectionnée
	 * 
	 * @throws Exception
	 */
	public void initialiserServices() throws Exception {
		listBean = new ArrayList<AbsencesMetierBean>();
		nbJourAbsenceCurYear = 0.0;
		nbJourAbsenceOneYearAgo = 0.0;
		nbJourAbsenceTwoYearAgo = 0.0;

		Map<String, Double[]> tab = new HashMap<String, Double[]>();

		for (MetierBean metierBean : metierBeanList) {
			Double[] tabInt = new Double[3];
			tabInt[0] = nbJourAbsenceByMetier(getCurYear(), metierBean.getId());
			tabInt[1] = nbJourAbsenceByMetier(getOneYearAgo(),
					metierBean.getId());
			tabInt[2] = nbJourAbsenceByMetier(getTwoYearAgo(),
					metierBean.getId());
			tab.put(metierBean.getNom(), tabInt);
		}

		for (MetierBean metierBean : metierBeanList) {
			if (isInService(metierBean.getId(), idEntreprise, idService)) {
				Double val1 = tab.get(metierBean.getNom())[0];
				Double val2 = tab.get(metierBean.getNom())[1];
				Double val3 = tab.get(metierBean.getNom())[2];
				nbJourAbsenceCurYear += val1;
				nbJourAbsenceOneYearAgo += val2;
				nbJourAbsenceTwoYearAgo += val3;
				listBean.add(getAbsencesMetierBean(metierBean, val1, val2, val3));
			}
		}
	}

	private MetierBean getMetierBeanByIdSalarie(int idSalarie) throws Exception {
		ParcoursServiceImpl parcoursServiceImpl = new ParcoursServiceImpl();
		List<ParcoursBean> parcours = parcoursServiceImpl
				.getParcoursBeanListByIdSalarie(idSalarie);
		ParcoursBean parcour = getLastParcours(parcours);
		if (parcour != null) {
			int idMet = parcour.getIdMetierSelected();
			return mapMetier.get(idMet);
		}
		return null;
	}

	public ArrayList<SelectItem> getEntreprisesListItem() throws Exception {
		if (!init)
			initialiserAllEntreprises();
		return entreprisesListItem;
	}

	public String download(ActionEvent e) throws Exception {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[4];
		ResourceBundle rb = ResourceBundle.getBundle("accents");
		entete[0] = rb.getString("Metiers");
		entete[1] = "" + getTwoYearAgo();
		entete[2] = "" + getOneYearAgo();
		entete[3] = "" + getCurYear();

		ServiceImpl servServ = new ServiceImpl();

		try {
			eContext.getSessionMap().put("datatable", this.listBean);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Absence par metier");
			eContext.getSessionMap().put("idEntreprise", idEntreprise);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
			if (idService != -1) {
				eContext.getSessionMap().put("nomService",
						servServ.getServiceBeanById(idService).getNom());
			} else {
				eContext.getSessionMap().put("nomService", null);
			}
			HashMap<String, Double> mapTotaux = new HashMap<String, Double>();
			// Redmine 2532 - Correction affichage des totaux à virgule
			mapTotaux.put("nbJourAbsenceCurYear", Double.valueOf(getNbJourAbsenceCurYearDisplay()));
			mapTotaux.put("nbJourAbsenceOneYearAgo", Double.valueOf(getNbJourAbsenceOneYearAgoDisplay()));
			mapTotaux.put("nbJourAbsenceTwoYearAgo", Double.valueOf(getNbJourAbsenceTwoYearAgoDisplay()));
			eContext.getSessionMap().put("mapTotaux", mapTotaux);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public ArrayList<SelectItem> getServicesListItem() throws Exception {
		return servicesListItem;
	}

	public ArrayList<AbsencesMetierBean> getListBean() throws Exception {
		return listBean;
	}

	public Double getNbJourAbsenceCurYear() throws Exception {
		return nbJourAbsenceCurYear;
	}

	public Double getNbJourAbsenceOneYearAgo() throws Exception {
		return nbJourAbsenceOneYearAgo;
	}

	public Double getNbJourAbsenceTwoYearAgo() throws Exception {
		return nbJourAbsenceTwoYearAgo;
	}

	public boolean isAfficheErreur() {
		return afficheErreur;
	}

	public int getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(int idEntreprise) {
		this.idEntreprise = idEntreprise;
	}

	public int getIdService() {
		return idService;
	}

	public void setIdService(int idService) {
		this.idService = idService;
	}

	/**
	 * @return the metierBeanList
	 */
	public List<MetierBean> getMetierBeanList() {
		return metierBeanList;
	}

	/**
	 * @param metierBeanList
	 *            the metierBeanList to set
	 */
	public void setMetierBeanList(List<MetierBean> metierBeanList) {
		this.metierBeanList = metierBeanList;
	}

	public String getNbJourAbsenceCurYearDisplay() {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.0");
		return df.format(nbJourAbsenceCurYear);
	}

	public void setNbJourAbsenceCurYearDisplay(
			String nbJourAbsenceCurYearDisplay) {
		this.nbJourAbsenceCurYearDisplay = nbJourAbsenceCurYearDisplay;
	}

	public String getNbJourAbsenceOneYearAgoDisplay() {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.0");
		return df.format(nbJourAbsenceOneYearAgo);
	}

	public void setNbJourAbsenceOneYearAgoDisplay(
			String nbJourAbsenceOneYearAgoDisplay) {
		this.nbJourAbsenceOneYearAgoDisplay = nbJourAbsenceOneYearAgoDisplay;
	}

	public String getNbJourAbsenceTwoYearAgoDisplay() {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.0");
		return df.format(nbJourAbsenceTwoYearAgo);
	}

	public void setNbJourAbsenceTwoYearAgoDisplay(
			String nbJourAbsenceTwoYearAgoDisplay) {
		this.nbJourAbsenceTwoYearAgoDisplay = nbJourAbsenceTwoYearAgoDisplay;
	}

}
