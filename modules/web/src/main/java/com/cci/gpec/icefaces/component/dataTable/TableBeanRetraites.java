package com.cci.gpec.icefaces.component.dataTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.ParamsGenerauxBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.RetraiteBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.ParamsGenerauxServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.icesoft.faces.context.effects.JavascriptContext;

public class TableBeanRetraites {

	private List<RetraiteBean> metiersRetraiteInventory;
	private int year1;
	private int year2;
	private int year3;
	private int year4;
	private int year5;
	private int year6;
	private int totalYear1;
	private int totalYear2;
	private int totalYear3;
	private int totalYear4;
	private int totalYear5;
	private int totalYear6;
	private int idProjection;
	private int idEntreprise = -1;
	private int idService;
	private boolean is5year;
	private ArrayList<SelectItem> entreprisesListItem;
	private ArrayList<SelectItem> servicesListItem;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	/**
	 * 
	 * @throws Exception
	 */
	public TableBeanRetraites() throws Exception {
		init();
	}

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void change(ValueChangeEvent event) throws Exception {
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}

		if (event.getComponent().getId().equals("selectProjection")) {
			idProjection = Integer.parseInt(event.getNewValue().toString());
			initFromProjection();
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
			}

		}

		if (event.getComponent().getId().equals("selectService")) {
			idService = Integer.parseInt(event.getNewValue().toString());
			if (idService != -1) {
				initialiserServices();
			} else {
				initialiserEntreprise();
			}
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void initialiserEntreprise() throws Exception {
		MetierServiceImpl entrepServ = new MetierServiceImpl();
		List<MetierBean> l = entrepServ.getMetiersList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		Collections.sort(l);

		Map<String, Integer[]> tab = new HashMap<String, Integer[]>();

		int curYear = getCurYear();
		tab = nbDepartRetraite2(curYear);

		metiersRetraiteInventory = new ArrayList<RetraiteBean>();
		totalYear1 = 0;
		totalYear2 = 0;
		totalYear3 = 0;
		totalYear4 = 0;
		totalYear5 = 0;
		totalYear6 = 0;
		for (int i = 0; i < l.size(); i++) {
			MetierBean met = l.get(i);
			if (idEntreprise != -1) {
				if (tab.get(met.getNom())[6] == 1) {
					RetraiteBean rb = new RetraiteBean();
					rb.setId(met.getId());
					rb.setNom(met.getNom());
					// int curYear = getCurYear();
					int nbDepartYear1 = tab.get(met.getNom())[0];
					int nbDepartYear2 = tab.get(met.getNom())[1];
					int nbDepartYear3 = tab.get(met.getNom())[2];
					int nbDepartYear4 = tab.get(met.getNom())[3];
					int nbDepartYear5 = tab.get(met.getNom())[4];
					int nbDepartYear6 = tab.get(met.getNom())[5];
					totalYear1 += nbDepartYear1;
					totalYear2 += nbDepartYear2;
					totalYear3 += nbDepartYear3;
					totalYear4 += nbDepartYear4;
					totalYear5 += nbDepartYear5;
					totalYear6 += nbDepartYear6;
					int total = 0;
					if (idProjection > 0) {
						total = nbDepartYear1 + nbDepartYear2 + nbDepartYear3
								+ nbDepartYear4 + nbDepartYear5 + nbDepartYear6;
					} else {
						total = nbDepartYear1 + nbDepartYear2 + nbDepartYear3;
					}
					rb.setDepartYear1(nbDepartYear1);
					rb.setDepartYear2(nbDepartYear2);
					rb.setDepartYear3(nbDepartYear3);
					rb.setDepartYear4(nbDepartYear4);
					rb.setDepartYear5(nbDepartYear5);
					rb.setDepartYear6(nbDepartYear6);
					rb.setTotalDepart(total);
					metiersRetraiteInventory.add(rb);
				}
			} else {
				RetraiteBean rb = new RetraiteBean();
				rb.setId(met.getId());
				rb.setNom(met.getNom());
				int nbDepartYear1 = tab.get(met.getNom())[0];
				int nbDepartYear2 = tab.get(met.getNom())[1];
				int nbDepartYear3 = tab.get(met.getNom())[2];
				int nbDepartYear4 = tab.get(met.getNom())[3];
				int nbDepartYear5 = tab.get(met.getNom())[4];
				int nbDepartYear6 = tab.get(met.getNom())[5];
				totalYear1 += nbDepartYear1;
				totalYear2 += nbDepartYear2;
				totalYear3 += nbDepartYear3;
				totalYear4 += nbDepartYear4;
				totalYear5 += nbDepartYear5;
				totalYear6 += nbDepartYear6;
				int total = 0;
				if (idProjection > 0) {
					total = nbDepartYear1 + nbDepartYear2 + nbDepartYear3
							+ nbDepartYear4 + nbDepartYear5 + nbDepartYear6;
				} else {
					total = nbDepartYear1 + nbDepartYear2 + nbDepartYear3;
				}
				rb.setDepartYear1(nbDepartYear1);
				rb.setDepartYear2(nbDepartYear2);
				rb.setDepartYear3(nbDepartYear3);
				rb.setDepartYear4(nbDepartYear4);
				rb.setDepartYear5(nbDepartYear5);
				rb.setDepartYear6(nbDepartYear6);
				rb.setTotalDepart(total);
				metiersRetraiteInventory.add(rb);
			}
		}
		servicesListItem = new ArrayList<SelectItem>();
		ServiceImpl servImpl = new ServiceImpl();
		List<ServiceBean> liste = servImpl.getServicesList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		for (int j = 0; j < liste.size(); j++) {
			ServiceBean service = liste.get(j);
			if (service.getIdEntreprise() == idEntreprise) {
				servicesListItem.add(new SelectItem(service.getId(), service
						.getNom()));
			}
		}

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void initialiserServices() throws Exception {
		MetierServiceImpl entrepServ = new MetierServiceImpl();
		List<MetierBean> l = entrepServ.getMetiersList(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		Collections.sort(l);

		Map<String, Integer[]> tab = new HashMap<String, Integer[]>();

		int curYear = getCurYear();
		tab = nbDepartRetraite2(curYear);

		metiersRetraiteInventory = new ArrayList<RetraiteBean>();
		totalYear1 = 0;
		totalYear2 = 0;
		totalYear3 = 0;
		totalYear4 = 0;
		totalYear5 = 0;
		totalYear6 = 0;
		for (int i = 0; i < l.size(); i++) {
			MetierBean met = l.get(i);
			if (tab.get(met.getNom())[6] == 1) {
				RetraiteBean rb = new RetraiteBean();
				rb.setId(met.getId());
				rb.setNom(met.getNom());
				int nbDepartYear1 = tab.get(met.getNom())[0];
				int nbDepartYear2 = tab.get(met.getNom())[1];
				int nbDepartYear3 = tab.get(met.getNom())[2];
				int nbDepartYear4 = tab.get(met.getNom())[3];
				int nbDepartYear5 = tab.get(met.getNom())[4];
				int nbDepartYear6 = tab.get(met.getNom())[5];
				totalYear1 += nbDepartYear1;
				totalYear2 += nbDepartYear2;
				totalYear3 += nbDepartYear3;
				totalYear4 += nbDepartYear4;
				totalYear5 += nbDepartYear5;
				totalYear6 += nbDepartYear6;
				int total = 0;
				if (idProjection > 0) {
					total = nbDepartYear1 + nbDepartYear2 + nbDepartYear3
							+ nbDepartYear4 + nbDepartYear5 + nbDepartYear6;
				} else {
					total = nbDepartYear1 + nbDepartYear2 + nbDepartYear3;
				}
				rb.setDepartYear1(nbDepartYear1);
				rb.setDepartYear2(nbDepartYear2);
				rb.setDepartYear3(nbDepartYear3);
				rb.setDepartYear4(nbDepartYear4);
				rb.setDepartYear5(nbDepartYear5);
				rb.setDepartYear6(nbDepartYear6);
				rb.setTotalDepart(total);
				metiersRetraiteInventory.add(rb);
			}
		}

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		metiersRetraiteInventory = new ArrayList<RetraiteBean>();
		MetierServiceImpl metier = new MetierServiceImpl();
		List<MetierBean> metiersInventory = metier.getMetiersList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		Collections.sort(metiersInventory);

		Map<String, Integer[]> tab = new HashMap<String, Integer[]>();

		int curYear = getCurYear();
		tab = nbDepartRetraite2(curYear);

		totalYear1 = 0;
		totalYear2 = 0;
		totalYear3 = 0;
		totalYear4 = 0;
		totalYear5 = 0;
		totalYear6 = 0;
		for (int i = 0; i < metiersInventory.size(); i++) {
			MetierBean bean = metiersInventory.get(i);
			RetraiteBean rb = new RetraiteBean();
			rb.setId(bean.getId());
			rb.setNom(bean.getNom());
			int nbDepartYear1 = tab.get(bean.getNom())[0];
			int nbDepartYear2 = tab.get(bean.getNom())[1];
			int nbDepartYear3 = tab.get(bean.getNom())[2];
			int nbDepartYear4 = tab.get(bean.getNom())[3];
			int nbDepartYear5 = tab.get(bean.getNom())[4];
			int nbDepartYear6 = tab.get(bean.getNom())[5];
			totalYear1 += nbDepartYear1;
			totalYear2 += nbDepartYear2;
			totalYear3 += nbDepartYear3;
			totalYear4 += nbDepartYear4;
			totalYear5 += nbDepartYear5;
			totalYear6 += nbDepartYear6;
			int total = 0;
			if (idProjection > 0) {
				total = nbDepartYear1 + nbDepartYear2 + nbDepartYear3
						+ nbDepartYear4 + nbDepartYear5 + nbDepartYear6;
			} else {
				total = nbDepartYear1 + nbDepartYear2 + nbDepartYear3;
			}
			rb.setDepartYear1(nbDepartYear1);
			rb.setDepartYear2(nbDepartYear2);
			rb.setDepartYear3(nbDepartYear3);
			rb.setDepartYear4(nbDepartYear4);
			rb.setDepartYear5(nbDepartYear5);
			rb.setDepartYear6(nbDepartYear6);
			rb.setTotalDepart(total);
			metiersRetraiteInventory.add(i, rb);
		}

		EntrepriseServiceImpl servEntreprise = new EntrepriseServiceImpl();
		entreprisesListItem = new ArrayList<SelectItem>();
		GroupeServiceImpl grServ = new GroupeServiceImpl();
		GroupeBean groupe = new GroupeBean();
		groupe = grServ.getGroupeBeanById(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		SelectItem selectItem = new SelectItem();
		selectItem = new SelectItem();
		selectItem.setValue(-1);
		selectItem.setLabel(groupe.getNom());
		entreprisesListItem.add(selectItem);
		for (EntrepriseBean eb : servEntreprise.getEntreprisesList(Integer
				.parseInt(session.getAttribute("groupe").toString()))) {
			entreprisesListItem.add(new SelectItem(eb.getId(), eb.getNom()));
		}

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("entreprise") != null) {
			this.idEntreprise = Integer.parseInt(session.getAttribute(
					"entreprise").toString());
			initialiserEntreprise();
		} else
			this.idEntreprise = -1;

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void initFromProjection() throws Exception {

		Map<String, Integer[]> tab = new HashMap<String, Integer[]>();

		int curYear = getCurYear();
		tab = nbDepartRetraite2(curYear);

		totalYear1 = 0;
		totalYear2 = 0;
		totalYear3 = 0;
		totalYear4 = 0;
		totalYear5 = 0;
		totalYear6 = 0;
		for (int i = 0; i < metiersRetraiteInventory.size(); i++) {
			MetierBean bean = metiersRetraiteInventory.get(i);
			RetraiteBean rb = new RetraiteBean();
			rb.setId(bean.getId());
			rb.setNom(bean.getNom());
			int nbDepartYear1 = tab.get(bean.getNom())[0];
			int nbDepartYear2 = tab.get(bean.getNom())[1];
			int nbDepartYear3 = tab.get(bean.getNom())[2];
			int nbDepartYear4 = tab.get(bean.getNom())[3];
			int nbDepartYear5 = tab.get(bean.getNom())[4];
			int nbDepartYear6 = tab.get(bean.getNom())[5];
			totalYear1 += nbDepartYear1;
			totalYear2 += nbDepartYear2;
			totalYear3 += nbDepartYear3;
			totalYear4 += nbDepartYear4;
			totalYear5 += nbDepartYear5;
			totalYear6 += nbDepartYear6;
			int total = 0;
			if (idProjection > 0) {
				total = nbDepartYear1 + nbDepartYear2 + nbDepartYear3
						+ nbDepartYear4 + nbDepartYear5 + nbDepartYear6;
			} else {
				total = nbDepartYear1 + nbDepartYear2 + nbDepartYear3;
			}
			rb.setDepartYear1(nbDepartYear1);
			rb.setDepartYear2(nbDepartYear2);
			rb.setDepartYear3(nbDepartYear3);
			rb.setDepartYear4(nbDepartYear4);
			rb.setDepartYear5(nbDepartYear5);
			rb.setDepartYear6(nbDepartYear6);
			rb.setTotalDepart(total);
			metiersRetraiteInventory.set(i, rb);
		}

	}

	/**
	 * 
	 * @param idSalarie
	 * @return
	 * @throws Exception
	 */
	private MetierBean getMetierBeanByIdSalarie(int idSalarie) throws Exception {
		SalarieServiceImpl serviceSalarie = new SalarieServiceImpl();
		SalarieBean salarie = serviceSalarie.getSalarieBeanById(idSalarie);
		ParcoursBean parcour = getLastParcours(salarie);
		if (parcour != null) {
			int idMet = parcour.getIdMetierSelected();
			MetierServiceImpl metierService = new MetierServiceImpl();
			return metierService.getMetierBeanById(idMet);
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public int getCurYear() {
		Calendar today = Calendar.getInstance();
		return today.get(Calendar.YEAR);
	}

	/**
	 * 
	 * @param salarie
	 * @return
	 */
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

	/**
	 * Retourne l'année à laquelle le salarié peut prétendre à sa retraite
	 * 
	 * @param salarie
	 * @return
	 * @throws Exception
	 */
	public Integer getYearRetraiteTheorique(SalarieBean salarie)
			throws Exception {
		Integer ageRetraite = getAgeRetraiteOfParam(salarie);
		Calendar naissance = new GregorianCalendar();
		naissance.setTime(salarie.getDateNaissance());
		Integer year = naissance.get(Calendar.YEAR) + ageRetraite;
		return year;
	}

	/**
	 * Retourne l'âge légal de retraite(saisi dans params généraux) de
	 * l'entreprise séléctionnée
	 * 
	 * @return
	 * @throws Exception
	 */
	public Integer getAgeRetraiteOfParam(SalarieBean salarie) throws Exception {
		Integer res = 0;
		ParamsGenerauxServiceImpl serv = new ParamsGenerauxServiceImpl();
		List<ParamsGenerauxBean> l = serv
				.getParamsGenerauxBeanListByIdEntreprise(salarie
						.getIdEntrepriseSelected());
		if ((l != null) && (l.size() > 0)) {
			Integer retraite = l.get(0).getAgeLegalRetraiteAnN();
			if (retraite != null) {
				res = retraite;
			}
		}
		return res;
	}

	public Map<String, Integer[]> nbDepartRetraite2(int year) throws Exception {

		SalarieServiceImpl salarieService = new SalarieServiceImpl();
		List<SalarieBean> salarieBeanList = salarieService
				.getSalariesList(Integer.parseInt(session
						.getAttribute("groupe").toString()));

		MetierServiceImpl metier = new MetierServiceImpl();
		List<MetierBean> metiersInventory = metier.getMetiersList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		Collections.sort(metiersInventory);

		Map<String, Integer[]> tab = new HashMap<String, Integer[]>();

		Integer[] tabInt = new Integer[7];
		tabInt[0] = 0;
		tabInt[1] = 0;
		tabInt[2] = 0;
		tabInt[3] = 0;
		tabInt[4] = 0;
		tabInt[5] = 0;
		tabInt[6] = 0;

		for (MetierBean m : metiersInventory) {
			tab.put(m.getNom(), tabInt);
		}

		for (SalarieBean salarie : salarieBeanList) {
			ParcoursBean parcoursBean = getLastParcours(salarie);
			if (parcoursBean != null
					&& parcoursBean.getIdTypeContratSelected() != 3
					&& parcoursBean.getIdTypeContratSelected() != 10
					&& salarie.isPresent()) {
				int yearRetraite = this.getYearRetraiteTheorique(salarie)
						.intValue();
				if ((parcoursBean != null)) {
					MetierServiceImpl metServ = new MetierServiceImpl();
					String nomMetier = metServ.getMetierBeanById(
							parcoursBean.getIdMetierSelected()).getNom();

					tabInt = new Integer[7];
					tabInt[0] = tab.get(nomMetier)[0];
					tabInt[1] = tab.get(nomMetier)[1];
					tabInt[2] = tab.get(nomMetier)[2];
					tabInt[3] = tab.get(nomMetier)[3];
					tabInt[4] = tab.get(nomMetier)[4];
					tabInt[5] = tab.get(nomMetier)[5];
					tabInt[6] = tab.get(nomMetier)[6];

					if ((idEntreprise != -1
							&& idService != -1
							&& salarie.getIdEntrepriseSelected() == idEntreprise && salarie
							.getIdServiceSelected() == idService)
							|| (idEntreprise != -1 && idService == -1 && salarie
									.getIdEntrepriseSelected() == idEntreprise)
							|| idEntreprise == -1) {
						if (tabInt[6] == 0) {
							tabInt[6] = 1;
						}
						Calendar finFonction = new GregorianCalendar();
						if (parcoursBean.getFinFonction() == null) {

							if (yearRetraite == year
									|| (yearRetraite < year && year == getRealCurrentYear())) {
								tabInt[0] = tabInt[0] + 1;
							}

							if (yearRetraite == year + 1) {
								tabInt[1] = tabInt[1] + 1;
							}

							if (yearRetraite == year + 2) {
								tabInt[2] = tabInt[2] + 1;
							}

							if (yearRetraite == year + 3) {
								tabInt[3] = tabInt[3] + 1;
							}

							if (yearRetraite == year + 4) {
								tabInt[4] = tabInt[4] + 1;
							}

							if (yearRetraite == year + 5) {
								tabInt[5] = tabInt[5] + 1;
							}

						} else {
							finFonction.setTime(parcoursBean.getFinFonction());
							int yearFinFonction = finFonction
									.get(Calendar.YEAR);
							if ((yearFinFonction == year)
									&& (yearRetraite <= year)) {
								tabInt[0] = tabInt[0] + 1;
							}
							if ((yearFinFonction == year + 1)
									&& (yearRetraite <= year + 1)) {
								tabInt[1] = tabInt[1] + 1;
							}
							if ((yearFinFonction == year + 2)
									&& (yearRetraite <= year + 2)) {
								tabInt[2] = tabInt[2] + 1;
							}
							if ((yearFinFonction == year + 3)
									&& (yearRetraite <= year + 3)) {
								tabInt[3] = tabInt[3] + 1;
							}
							if ((yearFinFonction == year + 4)
									&& (yearRetraite <= year + 4)) {
								tabInt[4] = tabInt[4] + 1;
							}
							if ((yearFinFonction == year + 5)
									&& (yearRetraite <= year + 5)) {
								tabInt[5] = tabInt[5] + 1;
							}
						}
					}
					tab.put(nomMetier, tabInt);
				}
			}
		}
		return tab;
	}

	public int getTotalYear1() throws Exception {
		return totalYear1;
	}

	public int getTotalYear2() throws Exception {
		return totalYear2;
	}

	public int getTotalYear3() throws Exception {
		return totalYear3;
	}

	public int getYear1() {
		year1 = getCurYear();
		return year1;
	}

	public int getYear2() {
		year2 = getCurYear() + 1;
		return year2;
	}

	public int getYear3() {
		year3 = getCurYear() + 2;
		return year3;
	}

	public List<RetraiteBean> getMetiersRetraiteInventory() throws Exception {
		return metiersRetraiteInventory;
	}

	public void setMetiersRetraiteInventory(
			List<RetraiteBean> metiersRetraiteInventory) {
		this.metiersRetraiteInventory = metiersRetraiteInventory;
	}

	public int getIdProjection() {
		return idProjection;
	}

	public void setIdProjection(int idProjection) {
		this.idProjection = idProjection;
	}

	public String download(ActionEvent e) {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete;
		if (idProjection > 0) {
			entete = new String[7];
		} else {
			entete = new String[4];
		}
		ResourceBundle rb = ResourceBundle.getBundle("accents");
		entete[0] = rb.getString("Metiers");
		entete[1] = "" + year1;
		entete[2] = "" + year2;
		entete[3] = "" + year3;
		if (idProjection > 0) {
			entete[4] = "" + year4;
			entete[5] = "" + year5;
			entete[6] = "" + year6;
		}

		ServiceImpl servServ = new ServiceImpl();

		try {
			eContext.getSessionMap().put("datatable", metiersRetraiteInventory);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Depart retraite");
			eContext.getSessionMap().put("idEntreprise", idEntreprise);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
			if (idService > 0) {
				eContext.getSessionMap().put("nomService",
						servServ.getServiceBeanById(idService).getNom());
			} else {
				eContext.getSessionMap().put("nomService", null);
			}

			HashMap<String, Integer> mapTotaux = new HashMap<String, Integer>();
			mapTotaux.put("nbDepartCurYear", getTotalYear1());
			mapTotaux.put("nbDepartYear1", getTotalYear2());
			mapTotaux.put("nbDepartYear2", getTotalYear3());
			if (idProjection > 0) {
				mapTotaux.put("nbDepartYear3", getTotalYear4());
				mapTotaux.put("nbDepartYear4", getTotalYear5());
				mapTotaux.put("nbDepartYear5", getTotalYear6());
			}

			eContext.getSessionMap().put("mapTotaux", mapTotaux);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
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

	public ArrayList<SelectItem> getEntreprisesListItem() {
		return entreprisesListItem;
	}

	public void setEntreprisesListItem(ArrayList<SelectItem> entreprisesListItem) {
		this.entreprisesListItem = entreprisesListItem;
	}

	public ArrayList<SelectItem> getServicesListItem() {
		return servicesListItem;
	}

	public void setServicesListItem(ArrayList<SelectItem> servicesListItem) {
		this.servicesListItem = servicesListItem;
	}

	/**
	 * Renvoie l'année en cours.
	 * 
	 * @return
	 */
	private int getRealCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public boolean isIs5year() {
		return idProjection == 3;
	}

	public void setIs5year(boolean is5year) {
		this.is5year = is5year;
	}

	public int getYear4() {
		year4 = getCurYear() + 3;
		return year4;
	}

	public void setYear4(int year4) {
		this.year4 = year4;
	}

	public int getYear5() {
		year5 = getCurYear() + 4;
		return year5;
	}

	public void setYear5(int year5) {
		this.year5 = year5;
	}

	public int getYear6() {
		year6 = getCurYear() + 5;
		return year6;
	}

	public void setYear6(int year6) {
		this.year6 = year6;
	}

	public int getTotalYear4() {
		return totalYear4;
	}

	public void setTotalYear4(int totalYear4) {
		this.totalYear4 = totalYear4;
	}

	public int getTotalYear5() {
		return totalYear5;
	}

	public void setTotalYear5(int totalYear5) {
		this.totalYear5 = totalYear5;
	}

	public int getTotalYear6() {
		return totalYear6;
	}

	public void setTotalYear6(int totalYear6) {
		this.totalYear6 = totalYear6;
	}

	public void setYear1(int year1) {
		this.year1 = year1;
	}

	public void setYear2(int year2) {
		this.year2 = year2;
	}

	public void setYear3(int year3) {
		this.year3 = year3;
	}

	public void setTotalYear1(int totalYear1) {
		this.totalYear1 = totalYear1;
	}

	public void setTotalYear2(int totalYear2) {
		this.totalYear2 = totalYear2;
	}

	public void setTotalYear3(int totalYear3) {
		this.totalYear3 = totalYear3;
	}
}
