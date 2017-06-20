package com.cci.gpec.web.backingBean.tabCompetences;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeMap;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.EvaluationCompetencesBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.EntretienServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.icesoft.faces.context.effects.JavascriptContext;

public class TabCompetencesFormBB {

	/**
	 * Attributs
	 */
	private ArrayList<SelectItem> entrepriseList;
	private int idEntrepriseSelected;
	private int idServiceSelected;
	private ArrayList<SelectItem> servicesList;
	private boolean afficheErreur;
	private Integer twoYearAgo;
	private Integer oneYearAgo;
	private Integer curYear;
	private ArrayList<EvaluationCompetencesBean> evaluationCompetencesBeanList;

	private static String PLUS = "+";
	private static String EGALE = "=";
	private static String MOINS = "-";

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public TabCompetencesFormBB() throws Exception {
		init();
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		evaluationCompetencesBeanList = new ArrayList<EvaluationCompetencesBean>();

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
		} else
			this.idEntrepriseSelected = -1;
		this.idServiceSelected = -1;
		this.afficheErreur = false;

		getTableEvaluationComp(this.idEntrepriseSelected,
				this.idServiceSelected);

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

	public boolean isAfficheErreur() {
		return afficheErreur;
	}

	public void setAfficheErreur(boolean afficheErreur) {
		this.afficheErreur = afficheErreur;
	}

	public ArrayList<SelectItem> getEntrepriseList() {
		return entrepriseList;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getTwoYearAgo() {
		this.twoYearAgo = (getCurYear() - 2);
		return twoYearAgo;
	}

	public void setTwoYearAgo(Integer twoYearAgo) {
		this.twoYearAgo = twoYearAgo;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getOneYearAgo() {
		this.oneYearAgo = (getCurYear() - 1);
		return oneYearAgo;
	}

	public void setOneYearAgo(Integer oneYearAgo) {
		this.oneYearAgo = oneYearAgo;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getCurYear() {
		Calendar cal = new GregorianCalendar();
		Date d = new Date();
		cal.setTime(d);
		curYear = cal.get(Calendar.YEAR);
		return curYear;
	}

	public void setCurYear(Integer curYear) {
		this.curYear = curYear;
	}

	/**
	 * 
	 * @return
	 */
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

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void recalculeDataTableCompetence(ValueChangeEvent event)
			throws Exception {

		if (event.getComponent().getId().equals("entrepriseListTabComp")) {
			idEntrepriseSelected = Integer.parseInt(event.getNewValue()
					.toString());
			idServiceSelected = -1;
		} else {
			if (event.getComponent().getId().equals("idServiceListTabComp")) {
				idServiceSelected = Integer.parseInt(event.getNewValue()
						.toString());
			}
		}
		evaluationCompetencesBeanList = new ArrayList<EvaluationCompetencesBean>();
		getTableEvaluationComp(idEntrepriseSelected, idServiceSelected);
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public String download(ActionEvent event) {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[4];
		entete[0] = "Intitul\u00E9";
		entete[1] = getTwoYearAgo().toString();
		entete[2] = getOneYearAgo().toString();
		entete[3] = getCurYear().toString();

		ServiceImpl servServ = new ServiceImpl();

		try {
			eContext.getSessionMap().put("datatable",
					this.evaluationCompetencesBeanList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "EvalComp");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
			if (idServiceSelected != -1)
				eContext.getSessionMap()
						.put("nomService",
								servServ.getServiceBeanById(idServiceSelected)
										.getNom());
			else
				eContext.getSessionMap().put("nomService", null);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public ArrayList<EvaluationCompetencesBean> getEvaluationCompetencesBeanList() {
		return evaluationCompetencesBeanList;
	}

	public void setEvaluationCompetencesBeanList(
			ArrayList<EvaluationCompetencesBean> evaluationCompetencesBeanList) {
		this.evaluationCompetencesBeanList = evaluationCompetencesBeanList;
	}

	public void getTableEvaluationComp(int idEntrepriseSelected,
			int idServiceSelected) throws Exception {

		SalarieServiceImpl salarieService = new SalarieServiceImpl();

		EvaluationCompetencesBean evaluationCompetencesBean;

		int nbCompPlusCurrentYearTaux = 0;
		int nbCompEgaleCurrentYearTaux = 0;
		int nbCompMoinsCurrentYearTaux = 0;

		int nbAbsenceForTauxCurYear = 0;

		evaluationCompetencesBean = new EvaluationCompetencesBean();

		if (idEntrepriseSelected == -1 && idServiceSelected == -1) {
			EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
			ArrayList<EntrepriseBean> entrepriseBeanList = (ArrayList<EntrepriseBean>) entrepriseService
					.getEntreprisesList(Integer.parseInt(session.getAttribute(
							"groupe").toString()));
			Collections.sort(entrepriseBeanList);
			for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
				evaluationCompetencesBean = getEvaluationCompetencesBean(
						entrepriseBean.getNom(), entrepriseBean.getId(),
						idServiceSelected, -1);
				evaluationCompetencesBeanList.add(evaluationCompetencesBean);

				nbCompPlusCurrentYearTaux = nbCompPlusCurrentYearTaux
						+ evaluationCompetencesBean.getNbCompPlusCurrentYear();
				nbCompEgaleCurrentYearTaux = nbCompEgaleCurrentYearTaux
						+ evaluationCompetencesBean.getNbCompEgaleCurrentYear();
				nbCompMoinsCurrentYearTaux = nbCompMoinsCurrentYearTaux
						+ evaluationCompetencesBean.getNbCompMoinsCurrentYear();
			}

		} else {
			if (idEntrepriseSelected != -1) {

				if (idServiceSelected == -1) {
					ServiceImpl service = new ServiceImpl();
					ArrayList<ServiceBean> serviceBeanList = (ArrayList<ServiceBean>) service
							.getServiceBeanListByIdEntreprise(idEntrepriseSelected);

					Collections.sort(serviceBeanList);
					for (ServiceBean serviceBean : serviceBeanList) {

						evaluationCompetencesBean = getEvaluationCompetencesBean(
								serviceBean.getNom(), idEntrepriseSelected,
								serviceBean.getId(), -1);
						evaluationCompetencesBeanList
								.add(evaluationCompetencesBean);

						nbCompPlusCurrentYearTaux = nbCompPlusCurrentYearTaux
								+ evaluationCompetencesBean
										.getNbCompPlusCurrentYear();
						nbCompEgaleCurrentYearTaux = nbCompEgaleCurrentYearTaux
								+ evaluationCompetencesBean
										.getNbCompEgaleCurrentYear();
						nbCompMoinsCurrentYearTaux = nbCompMoinsCurrentYearTaux
								+ evaluationCompetencesBean
										.getNbCompMoinsCurrentYear();
					}
				} else {
					TreeMap<String, Integer> nomMetierList = new TreeMap<String, Integer>();
					for (SalarieBean salarieBean : salarieService
							.getSalariesList(Integer.parseInt(session
									.getAttribute("groupe").toString()))) {
						ParcoursBean p = getLastParcours(salarieBean);
						if (p != null && p.getIdTypeContratSelected() != 3
								&& p.getIdTypeContratSelected() != 10) {
							if (salarieBean.getIdEntrepriseSelected() == this.idEntrepriseSelected
									&& salarieBean.getIdServiceSelected() == this.idServiceSelected) {
								ParcoursBean parcourBean = p;
								if (parcourBean != null) {
									if (!nomMetierList
											.containsValue(parcourBean
													.getNomMetier())) {
										nomMetierList.put(parcourBean
												.getNomMetier(), parcourBean
												.getIdMetierSelected());
									}
								}
							}
						}
					}

					for (String nomMetier : nomMetierList.keySet()) {

						Integer idMetier = nomMetierList.get(nomMetier);
						evaluationCompetencesBean = getEvaluationCompetencesBean(
								nomMetier, idEntrepriseSelected,
								idServiceSelected, idMetier);
						evaluationCompetencesBeanList
								.add(evaluationCompetencesBean);

						nbCompPlusCurrentYearTaux = nbCompPlusCurrentYearTaux
								+ evaluationCompetencesBean
										.getNbCompPlusCurrentYear();
						nbCompEgaleCurrentYearTaux = nbCompEgaleCurrentYearTaux
								+ evaluationCompetencesBean
										.getNbCompEgaleCurrentYear();
						nbCompMoinsCurrentYearTaux = nbCompMoinsCurrentYearTaux
								+ evaluationCompetencesBean
										.getNbCompMoinsCurrentYear();
					}
				}
			}
		}

		evaluationCompetencesBean = new EvaluationCompetencesBean();
		evaluationCompetencesBean.setIntituleCol("Total");

		evaluationCompetencesBean.setNbCompMoinsCurrentYearTaux(""
				+ nbCompMoinsCurrentYearTaux);
		evaluationCompetencesBean.setNbCompEgaleCurrentYearTaux(""
				+ nbCompEgaleCurrentYearTaux);
		evaluationCompetencesBean.setNbCompPlusCurrentYearTaux(""
				+ nbCompPlusCurrentYearTaux);
		evaluationCompetencesBean.setFooter(true);
		evaluationCompetencesBeanList.add(evaluationCompetencesBean);

		evaluationCompetencesBean = new EvaluationCompetencesBean();
		evaluationCompetencesBean.setIntituleCol("%");

		int totalCurrentYear = getTotal(nbCompPlusCurrentYearTaux,
				nbCompEgaleCurrentYearTaux, nbCompMoinsCurrentYearTaux);
		evaluationCompetencesBean.setNbCompMoinsCurrentYearTaux(getPourcentage(
				totalCurrentYear, nbCompMoinsCurrentYearTaux));
		evaluationCompetencesBean.setNbCompEgaleCurrentYearTaux(getPourcentage(
				totalCurrentYear, nbCompEgaleCurrentYearTaux));
		evaluationCompetencesBean.setNbCompPlusCurrentYearTaux(getPourcentage(
				totalCurrentYear, nbCompPlusCurrentYearTaux));

		evaluationCompetencesBean.setFooter(true);
		evaluationCompetencesBeanList.add(evaluationCompetencesBean);

	}

	public int getTotal(int valuePlus, int valueEgale, int valueMoins) {
		int result = 0;

		result = valuePlus + valueEgale + valueMoins;

		return result;
	}

	public String getPourcentage(int total, int value) {
		double totalD = new Double(total);
		double valueD = new Double(value);

		double resultat = new Double(0);
		if (total != 0) {
			resultat = (valueD * 100) / totalD;
		}

		DecimalFormat df = new DecimalFormat("###.##");
		return df.format(resultat);

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

	public EvaluationCompetencesBean getEvaluationCompetencesBean(
			String intituleCol, int idEntrepriseSelected,
			int idServiceSelected, int idMetierSelected) throws Exception {
		EvaluationCompetencesBean evaluationCompetencesBean = new EvaluationCompetencesBean();
		evaluationCompetencesBean.setIntituleCol(intituleCol);

		int[] tabInt = getNbCompetencesByTypeByYear(getCurYear(),
				idEntrepriseSelected, idServiceSelected, idMetierSelected);

		int nbCompPlusCurrentYear = tabInt[0];
		int nbCompEgaleCurrentYear = tabInt[1];
		int nbCompMoinsCurrentYear = tabInt[2];

		evaluationCompetencesBean
				.setNbCompEgaleCurrentYear(nbCompEgaleCurrentYear);

		evaluationCompetencesBean
				.setNbCompPlusCurrentYear(nbCompPlusCurrentYear);

		evaluationCompetencesBean
				.setNbCompMoinsCurrentYear(nbCompMoinsCurrentYear);

		return evaluationCompetencesBean;
	}

	public int[] getNbCompetencesByTypeByYear(int year,
			int idEntrepriseSelected, int idServiceSelected,
			int idMetierSelected) throws Exception {
		SalarieServiceImpl salarieService = new SalarieServiceImpl();
		List<SalarieBean> salarieBeanList = new ArrayList<SalarieBean>();
		if (idEntrepriseSelected != -1) {
			salarieBeanList = salarieService
					.getSalarieBeanListByIdEntreprise(idEntrepriseSelected);
		} else {
			salarieBeanList = salarieService.getSalariesList(Integer
					.parseInt(session.getAttribute("groupe").toString()));
		}
		int nbCompetenceByTypeByYearEgal = 0;
		int nbCompetenceByTypeByYearPlus = 0;
		int nbCompetenceByTypeByYearMoins = 0;
		boolean flag;
		for (SalarieBean salarieBean : salarieBeanList) {
			ParcoursBean p = getLastParcours(salarieBean);
			if (p != null && p.getIdTypeContratSelected() != 3
					&& p.getIdTypeContratSelected() != 10) {
				flag = false;
				List<ParcoursBean> parcoursBeanList = salarieBean
						.getParcoursBeanList();
				Collections.sort(parcoursBeanList);
				Collections.reverse(parcoursBeanList);
				if (parcoursBeanList.size() > 0) {
					ParcoursBean parcoursBean = parcoursBeanList.get(0);

					EntretienServiceImpl entServ = new EntretienServiceImpl();
					EntretienBean entretienBean;
					if (entServ.getEntretienBeanListByIdSalarieAndAnnee(
							salarieBean.getId(), year).size() > 0)
						entretienBean = entServ
								.getEntretienBeanListByIdSalarieAndAnnee(
										salarieBean.getId(), year).get(0);
					else
						entretienBean = null;

					if (salarieBean.getIdEntrepriseSelected() == idEntrepriseSelected) {

						if (idServiceSelected == -1) {
							flag = true;
						} else if (salarieBean.getIdServiceSelected() == idServiceSelected) {
							if (idMetierSelected == -1) {
								flag = true;
							} else if (parcoursBean.getIdMetierSelected() == idMetierSelected) {
								flag = true;
							}
						}
					}
					if (flag) {
						if (entretienBean != null) {
							if (entretienBean.getEvaluationGlobale().equals(
									PLUS))
								nbCompetenceByTypeByYearPlus++;
							if (entretienBean.getEvaluationGlobale().equals(
									EGALE))
								nbCompetenceByTypeByYearEgal++;
							if (entretienBean.getEvaluationGlobale().equals(
									MOINS))
								nbCompetenceByTypeByYearMoins++;
						}

					}
				}
			}
		}
		int[] tabInt = new int[3];
		tabInt[0] = nbCompetenceByTypeByYearPlus;
		tabInt[1] = nbCompetenceByTypeByYearEgal;
		tabInt[2] = nbCompetenceByTypeByYearMoins;
		return tabInt;
	}
}
