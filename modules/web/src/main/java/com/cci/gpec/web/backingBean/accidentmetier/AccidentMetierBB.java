package com.cci.gpec.web.backingBean.accidentmetier;

import java.text.DecimalFormat;
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

import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.AccidentMetierBean;
import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.ParamsGenerauxBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.metier.implementation.AccidentServiceImpl;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.ParamsGenerauxServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.icesoft.faces.context.effects.JavascriptContext;

public class AccidentMetierBB {

	private ArrayList<AccidentMetierBean> listBean;
	private ArrayList<AccidentMetierBean> listBean2;
	private ArrayList<String> listFooter;
	private int twoYearAgo;
	private int oneYearAgo;
	private int curYear;
	private int nbAccidentTwoYearAgo;
	private int nbAccidentOneYearAgo;
	private int nbAccidentCurYear;

	private int nbAccidentTwoYearAgoWithArret;
	private int nbAccidentOneYearAgoWithArret;
	private int nbAccidentCurYearWithArret;

	private int nbAccidentWithArretTmpCurrent = 0;
	private int nbAccidentWithArretTmpOne = 0;
	private int nbAccidentWithArretTmpTwo = 0;

	private int nbAccidentTmpCurrent = 0;
	private int nbAccidentTmpOne = 0;
	private int nbAccidentTmpTwo = 0;

	private String tauxFreqTwoYearAgo;
	private String tauxFreqOneYearAgo;
	private String tauxFreqCurYear;
	private boolean afficheErreur = false;
	private boolean init = false;
	private int idEntreprise = -1;
	private int idService = -1;
	private ArrayList<SelectItem> entreprisesListItem;
	private ArrayList<SelectItem> servicesListItem;
	private static String INDEFINI = "Ind\u00E9fini*";

	private List<MetierBean> metierBeanListFact = new ArrayList<MetierBean>();
	private boolean initList = false;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public int getCurYear() {
		Calendar cal = new GregorianCalendar();
		Date d = new Date();
		cal.setTime(d);
		curYear = cal.get(Calendar.YEAR);
		return curYear;
	}

	public int getTwoYearAgo() {
		this.twoYearAgo = (getCurYear() - 2);
		return twoYearAgo;
	}

	public int getOneYearAgo() {
		this.oneYearAgo = (getCurYear() - 1);
		return oneYearAgo;
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

			// On teste le cas ou il n'y aurait pas de service pour l'entreprise
			// selectionnée
			if ((idEntreprise != -1)
					&& (servicesListItem == null || servicesListItem.size() == 0)) {
				ResourceBundle bundle = ResourceBundle.getBundle("errors");
				FacesMessage message = new FacesMessage(
						bundle.getString("AfficheSansService"));
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage("idForm:panel",
						message);
			}

		} else {
			idService = Integer.parseInt(event.getNewValue().toString());
		}
		initialiserEntreprise2();
	}

	/**
	 * Initialise les métiers pour toutes les entreprises
	 * 
	 * @throws Exception
	 */
	public void initialiserAllEntreprises() throws Exception {
		// Initialisation des items
		EntrepriseServiceImpl servEntreprise = new EntrepriseServiceImpl();
		entreprisesListItem = new ArrayList<SelectItem>();
		GroupeServiceImpl grServ = new GroupeServiceImpl();
		GroupeBean groupe = grServ.getGroupeBeanById(Integer.parseInt(session
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
		init = true;

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("entreprise") != null) {
			this.idEntreprise = Integer.parseInt(session.getAttribute(
					"entreprise").toString());
			// On teste le cas ou il n'y aurait pas de service pour l'entreprise
			// selectionnée
			if ((idEntreprise != -1)
					&& (servicesListItem == null || servicesListItem.size() == 0)) {
				ResourceBundle bundle = ResourceBundle.getBundle("errors");
				FacesMessage message = new FacesMessage(
						bundle.getString("AfficheSansService"));
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage("idForm:panel",
						message);
			}
		}
		initialiserEntreprise2();

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

	public MetierBean getMetierById(int id) {
		MetierBean result = new MetierBean();
		for (MetierBean s : metierBeanListFact) {
			if (s.getId() == id) {
				result = s;
			}
		}
		return result;
	}

	public void initialiserEntreprise2() throws Exception {
		MetierServiceImpl metServ = new MetierServiceImpl();
		List<MetierBean> listMetierBean = metServ.getMetiersList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		Collections.sort(listMetierBean);
		listBean = new ArrayList<AccidentMetierBean>();
		nbAccidentCurYear = 0;
		nbAccidentOneYearAgo = 0;
		nbAccidentTwoYearAgo = 0;

		nbAccidentCurYearWithArret = 0;
		nbAccidentOneYearAgoWithArret = 0;
		nbAccidentTwoYearAgoWithArret = 0;

		Map<String, Integer[]> map = new HashMap<String, Integer[]>();
		Integer[] tab = new Integer[6];
		tab[0] = 0;
		tab[1] = 0;
		tab[2] = 0;
		tab[3] = 0;
		tab[4] = 0;
		tab[5] = 0;

		for (MetierBean metierBean : listMetierBean) {
			map.put(metierBean.getNom(), tab);
		}

		SalarieServiceImpl salServ = new SalarieServiceImpl();
		List<SalarieBean> listSal = new ArrayList<SalarieBean>();
		if (idEntreprise != -1) {
			listSal = salServ.getSalarieBeanListByIdEntreprise(idEntreprise);
		} else {
			listSal = salServ.getSalariesList(Integer.parseInt(session
					.getAttribute("groupe").toString()));
		}

		for (SalarieBean s : listSal) {
			if (getLastParcours(s) != null
					&& getLastParcours(s).getIdTypeContratSelected() != 3
					&& getLastParcours(s).getIdTypeContratSelected() != 7) {
				for (AccidentBean accidentBean : s.getAccidentBeanList()) {
					// UN ACCIDENT A EU LIEU
					if (accidentBean.isInitial()) {
						// on test si cet accident est de type
						// "accident de travail"
						if (accidentBean.getIdTypeAccidentBeanSelected() == 1) {
							Calendar acc = new GregorianCalendar();
							acc.setTime(accidentBean.getDateAccident());
							if (acc.get(Calendar.YEAR) == getCurYear()
									|| acc.get(Calendar.YEAR) == getOneYearAgo()
									|| acc.get(Calendar.YEAR) == getTwoYearAgo()) {
								// UN ACCIDENT A EU LIEU CETTE ANNEE, RESTE A
								// VERIFIER
								// SI
								// C'EST AVEC CE METIER
								if (idService != -1) {
									if (idService == s.getIdServiceSelected()) {
										// IL S'AGIT DU SALAIRE QUI A EU
										// L
										// ACCIDENT, RESTE A SAVOIR QUEL
										// ETAIT
										// SON POSTE A CE MOMENT
										int idMet = getIdMetier(s,
												accidentBean.getDateAccident());
										if (idMet != 0) {
											int nbN = 0;
											int nbNMoins1 = 0;
											int nbNMoins2 = 0;
											int nbNwa = 0;
											int nbNMoins1wa = 0;
											int nbNMoins2wa = 0;
											nbN = map.get(metServ
													.getMetierBeanById(idMet)
													.getNom())[0];
											nbNMoins1 = map.get(metServ
													.getMetierBeanById(idMet)
													.getNom())[1];
											nbNMoins2 = map.get(metServ
													.getMetierBeanById(idMet)
													.getNom())[2];
											nbNwa = map.get(metServ
													.getMetierBeanById(idMet)
													.getNom())[3];
											nbNMoins1wa = map.get(metServ
													.getMetierBeanById(idMet)
													.getNom())[4];
											nbNMoins2wa = map.get(metServ
													.getMetierBeanById(idMet)
													.getNom())[5];

											if (acc.get(Calendar.YEAR) == getCurYear()) {
												nbN++;
												if (accidentBean
														.getNombreJourArret() > 0) {
													nbNwa++;
												}
											}
											if (acc.get(Calendar.YEAR) == getOneYearAgo()) {
												nbNMoins1++;
												if (accidentBean
														.getNombreJourArret() > 0) {
													nbNMoins1wa++;
												}
											}
											if (acc.get(Calendar.YEAR) == getTwoYearAgo()) {
												nbNMoins2++;
												if (accidentBean
														.getNombreJourArret() > 0) {
													nbNMoins2wa++;
												}
											}

											Integer[] tabI = new Integer[6];

											tabI[0] = nbN;
											tabI[1] = nbNMoins1;
											tabI[2] = nbNMoins2;
											tabI[3] = nbNwa;
											tabI[4] = nbNMoins1wa;
											tabI[5] = nbNMoins2wa;

											map.put(metServ.getMetierBeanById(
													idMet).getNom(), tabI);
										}
									}
								} else {
									// IL S'AGIT DU SALAIRE QUI A EU L
									// ACCIDENT,
									// RESTE A SAVOIR QUEL ETAIT SON POSTE A
									// CE
									// MOMENT
									int idMet = getIdMetier(s,
											accidentBean.getDateAccident());
									if (idMet != 0) {
										int nbN = 0;
										int nbNMoins1 = 0;
										int nbNMoins2 = 0;
										int nbNwa = 0;
										int nbNMoins1wa = 0;
										int nbNMoins2wa = 0;
										nbN = map.get(metServ
												.getMetierBeanById(idMet)
												.getNom())[0];
										nbNMoins1 = map.get(metServ
												.getMetierBeanById(idMet)
												.getNom())[1];
										nbNMoins2 = map.get(metServ
												.getMetierBeanById(idMet)
												.getNom())[2];
										nbNwa = map.get(metServ
												.getMetierBeanById(idMet)
												.getNom())[3];
										nbNMoins1wa = map.get(metServ
												.getMetierBeanById(idMet)
												.getNom())[4];
										nbNMoins2wa = map.get(metServ
												.getMetierBeanById(idMet)
												.getNom())[5];

										if (acc.get(Calendar.YEAR) == getCurYear()) {
											nbN++;
											if (accidentBean
													.getNombreJourArret() > 0) {
												nbNwa++;
											}
										}
										if (acc.get(Calendar.YEAR) == getOneYearAgo()) {
											nbNMoins1++;
											if (accidentBean
													.getNombreJourArret() > 0) {
												nbNMoins1wa++;
											}
										}
										if (acc.get(Calendar.YEAR) == getTwoYearAgo()) {
											nbNMoins2++;
											if (accidentBean
													.getNombreJourArret() > 0) {
												nbNMoins2wa++;
											}
										}

										Integer[] tabI = new Integer[6];

										tabI[0] = nbN;
										tabI[1] = nbNMoins1;
										tabI[2] = nbNMoins2;
										tabI[3] = nbNwa;
										tabI[4] = nbNMoins1wa;
										tabI[5] = nbNMoins2wa;

										map.put(metServ
												.getMetierBeanById(idMet)
												.getNom(), tabI);
									}
								}
							}
						}
					}
				}
			}
		}
		List<String> metierListForEntreprise = new ArrayList<String>();

		if (idEntreprise != -1) {
			if (idService != -1) {
				for (SalarieBean sal : salServ
						.getSalarieBeanListByIdEntreprise(idEntreprise)) {
					if (sal.getIdServiceSelected() == idService) {
						ParcoursBean p = new ParcoursBean();
						p = getLastParcours(sal);
						if (p != null) {
							metierListForEntreprise.add(metServ
									.getMetierBeanById(p.getIdMetierSelected())
									.getNom());
						}
					}
				}
			} else {
				for (SalarieBean sal : salServ
						.getSalarieBeanListByIdEntreprise(idEntreprise)) {
					ParcoursBean p = new ParcoursBean();
					p = getLastParcours(sal);
					if (p != null) {
						metierListForEntreprise.add(metServ.getMetierBeanById(
								p.getIdMetierSelected()).getNom());
					}
				}
			}
		}
		for (MetierBean metierBean : listMetierBean) {

			if (idEntreprise != -1) {
				if (metierListForEntreprise.contains(metierBean.getNom())) {
					AccidentMetierBean amb = new AccidentMetierBean();

					Integer[] tabNb = new Integer[6];
					tabNb = map.get(metierBean.getNom());

					amb.setId(metierBean.getId());
					amb.setNom(metierBean.getNom());
					amb.setNbAccidentCurYear(tabNb[0]);
					amb.setNbAccidentCurYearWithArret(tabNb[3]);

					amb.setNbAccidentOneYearAgo(tabNb[1]);
					amb.setNbAccidentOneYearAgoWithArret(tabNb[4]);

					amb.setNbAccidentTwoYearAgo(tabNb[2]);
					amb.setNbAccidentTwoYearAgoWithArret(tabNb[5]);

					listBean.add(amb);

					nbAccidentCurYear += amb.getNbAccidentCurYear();
					nbAccidentOneYearAgo += amb.getNbAccidentOneYearAgo();
					nbAccidentTwoYearAgo += amb.getNbAccidentTwoYearAgo();

					nbAccidentCurYearWithArret += amb
							.getNbAccidentCurYearWithArret();
					nbAccidentOneYearAgoWithArret += amb
							.getNbAccidentOneYearAgoWithArret();
					nbAccidentTwoYearAgoWithArret += amb
							.getNbAccidentTwoYearAgoWithArret();

					tauxFreqCurYear = getTauxFreqYear(getCurYear());
					tauxFreqOneYearAgo = getTauxFreqYear(getOneYearAgo());
					tauxFreqTwoYearAgo = getTauxFreqYear(getTwoYearAgo());
				}

			} else {
				AccidentMetierBean amb = new AccidentMetierBean();

				Integer[] tabNb = new Integer[6];
				tabNb = map.get(metierBean.getNom());

				amb.setId(metierBean.getId());
				amb.setNom(metierBean.getNom());
				amb.setNbAccidentCurYear(tabNb[0]);
				amb.setNbAccidentCurYearWithArret(tabNb[3]);

				amb.setNbAccidentOneYearAgo(tabNb[1]);
				amb.setNbAccidentOneYearAgoWithArret(tabNb[4]);

				amb.setNbAccidentTwoYearAgo(tabNb[2]);
				amb.setNbAccidentTwoYearAgoWithArret(tabNb[5]);

				listBean.add(amb);

				nbAccidentCurYear += amb.getNbAccidentCurYear();
				nbAccidentOneYearAgo += amb.getNbAccidentOneYearAgo();
				nbAccidentTwoYearAgo += amb.getNbAccidentTwoYearAgo();

				nbAccidentCurYearWithArret += amb
						.getNbAccidentCurYearWithArret();
				nbAccidentOneYearAgoWithArret += amb
						.getNbAccidentOneYearAgoWithArret();
				nbAccidentTwoYearAgoWithArret += amb
						.getNbAccidentTwoYearAgoWithArret();

				tauxFreqCurYear = getTauxFreqYear(getCurYear());
				tauxFreqOneYearAgo = getTauxFreqYear(getOneYearAgo());
				tauxFreqTwoYearAgo = getTauxFreqYear(getTwoYearAgo());
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

	private int getIdMetier(SalarieBean salarie, Date dateAccident) {
		for (ParcoursBean parcour : salarie.getParcoursBeanList()) {

			Calendar deb = new GregorianCalendar();
			Calendar acc = new GregorianCalendar();
			Calendar fin = new GregorianCalendar();
			acc.setTime(dateAccident);
			deb.setTime(parcour.getDebutFonction());
			if (parcour.getFinFonction() == null) {
				fin.setTime(new Date());
			} else {
				fin.setTime(parcour.getFinFonction());
			}
			if ((deb.before(acc) && fin.after(acc)) || deb.equals(acc)
					|| fin.equals(acc)) {
				return parcour.getIdMetierSelected();
			}
		}
		return 0;

	}

	public int getNbAccidentTwoYearAgoWithArret() throws Exception {
		return nbAccidentTwoYearAgoWithArret;
	}

	public int getNbAccidentOneYearAgoWithArret() throws Exception {
		return nbAccidentOneYearAgoWithArret;
	}

	public int getNbAccidentCurYearWithArret() throws Exception {
		return nbAccidentCurYearWithArret;
	}

	public ArrayList<AccidentMetierBean> getListBean() throws Exception {
		if (idEntreprise == -1 && !init)
			initialiserAllEntreprises();
		// listBean a déja été initialisé lors des selections sauf au 1er appel
		return listBean;
	}

	public ArrayList<String> getListFooter() {
		listFooter = new ArrayList<String>();
		listFooter.add("Total 1");
		listFooter.add("Total 2");
		return listFooter;
	}

	public String getTauxFreqYear(int year) throws Exception {
		ParamsGenerauxServiceImpl serv = new ParamsGenerauxServiceImpl();
		ParamsGenerauxBean paramGBean = null;

		if (idEntreprise != -1) {
			List<ParamsGenerauxBean> listParamG = serv
					.getParamsGenerauxBeanListByIdEntreprise(idEntreprise);
			if (listParamG.size() == 0) {
				afficheErreur = true;
				return INDEFINI;
			} else {
				paramGBean = listParamG.get(0);
			}
		} else {
			afficheErreur = true;
			return INDEFINI;
		}

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2); // arrondi à 2 chiffres apres la
		// virgules
		df.setMinimumFractionDigits(0);
		df.setDecimalSeparatorAlwaysShown(false);
		double res = 0;

		Double val = null;
		if (year == getCurYear()) {
			val = paramGBean.getDureeTravailAnNHeuresRealiseesEffectifTot();
			if (val != null) {
				if (val == 0) {
					afficheErreur = true;
					return INDEFINI;
				}
				res = ((double) nbAccidentCurYearWithArret / (double) val) * 1000000;
				afficheErreur = false;
				return df.format(res);
			} else {
				afficheErreur = true;
				return INDEFINI;
			}
		}
		if (year == getOneYearAgo()) {
			val = paramGBean.getDureeTravailAnN1HeuresRealiseesEffectifTot();
			if (val != null) {
				if (val == 0) {
					afficheErreur = true;
					return INDEFINI;
				}
				res = ((double) nbAccidentOneYearAgoWithArret / (double) val) * 1000000;
				afficheErreur = false;
				return df.format(res);
			} else {
				afficheErreur = true;
				return INDEFINI;
			}
		}
		if (year == getTwoYearAgo()) {
			val = paramGBean.getDureeTravailAnN2HeuresRealiseesEffectifTot();
			if (val != null) {
				if (val == 0) {
					afficheErreur = true;
					return INDEFINI;
				}
				res = ((double) nbAccidentTwoYearAgoWithArret / (double) val) * 1000000;
				afficheErreur = false;
				return df.format(res);
			} else {
				afficheErreur = true;
				return INDEFINI;
			}
		}
		return "";
	}

	public String getTauxFreqTwoYearAgo() throws Exception {
		return tauxFreqTwoYearAgo;
	}

	public String getTauxFreqOneYearAgo() throws Exception {
		return tauxFreqOneYearAgo;
	}

	public String getTauxFreqCurYear() throws Exception {
		return tauxFreqCurYear;
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
		ResourceBundle rb = ResourceBundle.getBundle("accents");
		entete[0] = rb.getString("Metiers");
		entete[1] = "" + getTwoYearAgo();
		entete[2] = "" + getOneYearAgo();
		entete[3] = "" + getCurYear();

		ServiceImpl servServ = new ServiceImpl();

		try {
			eContext.getSessionMap().put("datatable", this.listBean);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Accident par metier");
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

			HashMap<String, String> mapTaux = new HashMap<String, String>();
			HashMap<String, Integer> mapTotaux = new HashMap<String, Integer>();

			mapTotaux.put("nbAccidentCurYear", nbAccidentCurYear);
			mapTotaux.put("nbAccidentOneYearAgo", nbAccidentOneYearAgo);
			mapTotaux.put("nbAccidentTwoYearAgo", nbAccidentTwoYearAgo);

			mapTaux.put("tauxAccidentTwoYearAgo", this.tauxFreqTwoYearAgo);
			mapTaux.put("tauxAccidentOneYearAgo", tauxFreqOneYearAgo);
			mapTaux.put("tauxAccidentCurYear", tauxFreqCurYear);

			eContext.getSessionMap().put("mapTaux", mapTaux);
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

	public ArrayList<SelectItem> getEntreprisesListItem() throws Exception {
		if (!init)
			initialiserAllEntreprises();
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

	public ArrayList<AccidentMetierBean> getListBean2() {
		return listBean;
	}

}
