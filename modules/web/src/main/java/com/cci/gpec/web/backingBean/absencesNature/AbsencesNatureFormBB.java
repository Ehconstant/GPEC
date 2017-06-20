package com.cci.gpec.web.backingBean.absencesNature;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AbsencesNatureBean;
import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.ParamsGenerauxBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.commons.TypeAbsenceBean;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.ParamsGenerauxServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.metier.implementation.TypeAbsenceServiceImpl;
import com.icesoft.faces.context.effects.JavascriptContext;

public class AbsencesNatureFormBB {

	private ArrayList<SelectItem> entrepriseList;
	private int idEntrepriseSelected;
	private int idServiceSelected;
	private ArrayList<SelectItem> servicesList;
	private ArrayList<AbsencesNatureBean> absencesNatureBeanList;
	private Integer twoYearAgo;
	private Integer oneYearAgo;
	private Integer curYear;
	private boolean afficheErreur;
	private static String INDEFINI = "Ind\u00E9fini*";

	private List<SalarieBean> salarieBeanListFact = new ArrayList<SalarieBean>();
	private boolean initList = false;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public AbsencesNatureFormBB() throws Exception {
		init();
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		absencesNatureBeanList = new ArrayList<AbsencesNatureBean>();

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
		// if (list.size() > 0) {
		selectItem = new SelectItem();
		selectItem.setValue(-1);
		selectItem.setLabel(groupe.getNom());
		entrepriseList.add(selectItem);
		// } else {
		// selectItem = new SelectItem();
		// selectItem.setValue(-1);
		// selectItem.setLabel("Groupe");
		// entrepriseList.add(selectItem);
		// }
		for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(entrepriseBean.getId());
			selectItem.setLabel(entrepriseBean.getNom());
			entrepriseList.add(selectItem);
		}

		this.idServiceSelected = -1;

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("entreprise") != null) {
			this.idEntrepriseSelected = Integer.parseInt(session.getAttribute(
					"entreprise").toString());
		} else
			this.idEntrepriseSelected = -1;

		this.afficheErreur = false;

		getTableAbsencesNature(this.idEntrepriseSelected,
				this.idServiceSelected);

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

	/**
	 * 
	 * @return
	 */
	public Integer getTwoYearAgo() {
		this.twoYearAgo = (getCurYear() - 2);
		return twoYearAgo;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getOneYearAgo() {
		this.oneYearAgo = (getCurYear() - 1);
		return oneYearAgo;
	}

	/**
	 * 
	 * @return
	 */
	public int getIdEntrepriseSelected() {
		return idEntrepriseSelected;
	}

	/**
	 * 
	 * @param idEntrepriseSelected
	 */
	public void setIdEntrepriseSelected(int idEntrepriseSelected) {
		this.idEntrepriseSelected = idEntrepriseSelected;
	}

	/**
	 * 
	 * @return
	 */
	public int getIdServiceSelected() {
		return idServiceSelected;
	}

	/**
	 * 
	 * @param idServiceSelected
	 */
	public void setIdServiceSelected(int idServiceSelected) {
		this.idServiceSelected = idServiceSelected;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<SelectItem> getEntrepriseList() {
		return entrepriseList;
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
	public void recalculeDataTableAbsencesNature(ValueChangeEvent event)
			throws Exception {
		if (event.getComponent().getId().equals("entrepriseListAbsencesNat")) {
			idEntrepriseSelected = Integer.parseInt(event.getNewValue()
					.toString());
			idServiceSelected = -1;
		} else {
			if (event.getComponent().getId().equals("idServiceListAbsencesNat")) {
				idServiceSelected = Integer.parseInt(event.getNewValue()
						.toString());
			}
		}
		absencesNatureBeanList = new ArrayList<AbsencesNatureBean>();
		getTableAbsencesNature(idEntrepriseSelected, idServiceSelected);
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<AbsencesNatureBean> getAbsencesNatureBeanList() {
		return absencesNatureBeanList;
	}

	public void setTwoYearAgo(int twoYearAgo) {
		this.twoYearAgo = twoYearAgo;
	}

	public void setOneYearAgo(int oneYearAgo) {
		this.oneYearAgo = oneYearAgo;
	}

	public void setCurYear(int curYear) {
		this.curYear = curYear;
	}

	/**
	 * 
	 * @param idEntrepriseSelected
	 * @param idServiceSelected
	 * @throws Exception
	 */
	public void getTableAbsencesNature(int idEntrepriseSelected,
			int idServiceSelected) throws Exception {
		TypeAbsenceServiceImpl typeAbsenceService = new TypeAbsenceServiceImpl();
		List<TypeAbsenceBean> typeAbsenceBeanList = typeAbsenceService
				.getOnlyTypeAbsenceList();
		AbsencesNatureBean absencesNatureBean;
		Double nbAbsenceByNatureCurrentYearTotal = 0.0;
		Double nbAbsenceByNatureOneYearAgoTotal = 0.0;
		Double nbAbsenceByNatureTwoYearAgoTotal = 0.0;
		Double nbAbsenceForTauxCurYear = 0.0;
		Double nbAbsenceForTauxOneYearAgo = 0.0;
		Double nbAbsenceForTauxTwoYearAgo = 0.0;
		for (TypeAbsenceBean typeAbsenceBean : typeAbsenceBeanList) {

			absencesNatureBean = new AbsencesNatureBean();
			absencesNatureBean.setNomTypeAbsence(typeAbsenceBean.getNom());

			Double nbAbsenceByNatureCurrentYear = getNbAbsenceByNatureByYear(
					typeAbsenceBean.getId(), getCurYear());
			Double nbAbsenceByNatureOneYearAgo = getNbAbsenceByNatureByYear(
					typeAbsenceBean.getId(), getOneYearAgo());
			Double nbAbsenceByNatureTwoYearAgo = getNbAbsenceByNatureByYear(
					typeAbsenceBean.getId(), getTwoYearAgo());

			absencesNatureBean
					.setNbAbsenceByNatureCurrentYear(nbAbsenceByNatureCurrentYear);
			absencesNatureBean
					.setNbAbsenceByNatureOneYearAgo(nbAbsenceByNatureOneYearAgo);
			absencesNatureBean
					.setNbAbsenceByNatureTwoYearAgo(nbAbsenceByNatureTwoYearAgo);
			absencesNatureBeanList.add(absencesNatureBean);

			nbAbsenceByNatureCurrentYearTotal = nbAbsenceByNatureCurrentYearTotal
					+ nbAbsenceByNatureCurrentYear;

			nbAbsenceByNatureOneYearAgoTotal = nbAbsenceByNatureOneYearAgoTotal
					+ nbAbsenceByNatureOneYearAgo;

			nbAbsenceByNatureTwoYearAgoTotal = nbAbsenceByNatureTwoYearAgoTotal
					+ nbAbsenceByNatureTwoYearAgo;

			if ((typeAbsenceBean.getId() == 1)
					|| (typeAbsenceBean.getId() == 2)
					|| (typeAbsenceBean.getId() == 3)
					|| (typeAbsenceBean.getId() == 4)
					|| (typeAbsenceBean.getId() == 6)) {
				nbAbsenceForTauxCurYear += getNbAbsenceByNatureByYear(
						typeAbsenceBean.getId(), getCurYear());
				nbAbsenceForTauxOneYearAgo += getNbAbsenceByNatureByYear(
						typeAbsenceBean.getId(), getOneYearAgo());
				nbAbsenceForTauxTwoYearAgo += getNbAbsenceByNatureByYear(
						typeAbsenceBean.getId(), getTwoYearAgo());
			}
		}
		absencesNatureBean = new AbsencesNatureBean();
		absencesNatureBean
				.setNomTypeAbsence("Nombre de jours d'absence / ann\u00E9e");

		absencesNatureBean
				.setNbAbsenceByNatureCurrentYear(nbAbsenceByNatureCurrentYearTotal);
		absencesNatureBean
				.setNbAbsenceByNatureOneYearAgo(nbAbsenceByNatureOneYearAgoTotal);
		absencesNatureBean
				.setNbAbsenceByNatureTwoYearAgo(nbAbsenceByNatureTwoYearAgoTotal);
		absencesNatureBean.setFooter(true);
		absencesNatureBeanList.add(absencesNatureBean);

		absencesNatureBean = new AbsencesNatureBean();
		absencesNatureBean
				.setNomTypeAbsence("Taux d'absent\u00E9isme / ann\u00E9e");

		absencesNatureBean
				.setNbAbsenceByNatureCurrentYearTaux(getTauxAbsenteismeByYear(
						nbAbsenceForTauxCurYear, 0));
		absencesNatureBean
				.setNbAbsenceByNatureOneYearAgoTaux(getTauxAbsenteismeByYear(
						nbAbsenceForTauxOneYearAgo, 1));
		absencesNatureBean
				.setNbAbsenceByNatureTwoYearAgoTaux(getTauxAbsenteismeByYear(
						nbAbsenceForTauxTwoYearAgo, 2));
		absencesNatureBean.setFooter(true);
		absencesNatureBeanList.add(absencesNatureBean);
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
		if (nbrJour == 0)
			return 1.0;
		return nbrJour;
	}

	public SalarieBean getSalarieById(int id) {
		SalarieBean result = new SalarieBean();
		for (SalarieBean s : salarieBeanListFact) {
			if (s.getId() == id)
				result = s;
		}
		return result;
	}

	/**
	 * Retourne le dernier parcours d'un salarie, null s'il ny en a pas
	 * 
	 * @param salarie
	 * @return
	 */
	private ParcoursBean getLastParcours(SalarieBean salarie) {
		/*
		 * ParcoursBean pb = null; for (ParcoursBean parcour :
		 * salarie.getParcoursBeanList()) { if (pb == null ||
		 * parcour.getDebutFonction().after(pb.getDebutFonction())) { pb =
		 * parcour; break; } } return pb;
		 */

		ParcoursBean pb = null;
		for (ParcoursBean parcour : salarie.getParcoursBeanList()) {
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
	 * 
	 * @param idTypeAbsence
	 * @param year
	 * @param idEntrepriseSelected
	 * @param idServiceSelected
	 * @return
	 * @throws Exception
	 */
	public Double getNbAbsenceByNatureByYear(int idTypeAbsence, int year)
			throws Exception {
		AbsenceServiceImpl absenceService = new AbsenceServiceImpl();
		SalarieServiceImpl salarieService = new SalarieServiceImpl();

		if (initList == false) {
			initList = true;
			List<SalarieBean> l = new ArrayList<SalarieBean>();
			l = salarieService.getSalariesList(Integer.parseInt(session
					.getAttribute("groupe").toString()));
			for (SalarieBean s : l) {
				salarieBeanListFact.add(s);
			}
		}

		ArrayList<AbsenceBean> absenceBeanList = (ArrayList<AbsenceBean>) absenceService
				.getAbsenceBeanListByIdTypeAbsence(idTypeAbsence, Integer
						.parseInt(session.getAttribute("groupe").toString()));

		Double nbAbsenceByNatureByYear = 0.0;
		boolean flag;
		for (AbsenceBean absenceBean : absenceBeanList) {
			if (absenceBean.getDebutAbsence() != null
					&& absenceBean.getFinAbsence() != null) {
				flag = false;
				SalarieBean salarieBean = getSalarieById(absenceBean
						.getIdSalarie());
				// salarieService
				// .getSalarieBeanById(absenceBean.getIdSalarie());

				if (getLastParcours(salarieBean) != null
						&& getLastParcours(salarieBean)
								.getIdTypeContratSelected() != 3
						&& getLastParcours(salarieBean)
								.getIdTypeContratSelected() != 7) {
					Date debutAbsence = absenceBean.getDebutAbsence();
					Date finAbsence = absenceBean.getFinAbsence();

					GregorianCalendar dateFinAbsence = new GregorianCalendar();
					GregorianCalendar dateDebutAbsence = new GregorianCalendar();

					dateFinAbsence.setTime(finAbsence);
					dateDebutAbsence.setTime(debutAbsence);

					if (idEntrepriseSelected == -1 && idServiceSelected == -1) {
						flag = true;
					} else {
						if (salarieBean.getIdEntrepriseSelected() == idEntrepriseSelected) {

							if (idServiceSelected == -1) {
								flag = true;
							} else if (salarieBean.getIdServiceSelected() == idServiceSelected) {
								flag = true;
							}
						}
					}

					Double nbrJourOuvreYearAgo = 0.0;
					Double nbrJourOuvreYearNext = 0.0;
					// On vérifie si la période ne va pas sur deux années, sinon
					// on
					// prend en compte chaque année
					if (flag) {
						if (dateFinAbsence.get(Calendar.YEAR) == year) {
							if (dateDebutAbsence.get(Calendar.YEAR) == year) {
								nbAbsenceByNatureByYear += absenceBean
										.getNombreJourOuvre();
							} else {
								Calendar calTmpDebut = new GregorianCalendar(
										year, 0, 1);
								Calendar calTmpFin = new GregorianCalendar(
										year - 1, 11, 31);
								nbrJourOuvreYearAgo = getNbrJourOuvre(
										absenceBean.getDebutAbsence(),
										calTmpFin.getTime());
								nbrJourOuvreYearNext = getNbrJourOuvre(
										calTmpDebut.getTime(),
										absenceBean.getFinAbsence());
								nbAbsenceByNatureByYear += getDuree(
										absenceBean.getNombreJourOuvre(),
										nbrJourOuvreYearAgo,
										nbrJourOuvreYearNext, 1);
							}

						} else {
							if ((dateDebutAbsence.get(Calendar.YEAR) <= year)
									&& (year < dateFinAbsence
											.get(Calendar.YEAR))) {
								GregorianCalendar calTmp = new GregorianCalendar(
										year, 11, 31);

								if (dateDebutAbsence.get(Calendar.YEAR) == year) {
									Calendar calTmpDebut = new GregorianCalendar(
											year + 1, 0, 1);
									Calendar calTmpFin = new GregorianCalendar(
											year, 11, 31);
									nbrJourOuvreYearAgo = getNbrJourOuvre(
											absenceBean.getDebutAbsence(),
											calTmpFin.getTime());
									nbrJourOuvreYearNext = getNbrJourOuvre(
											calTmpDebut.getTime(),
											absenceBean.getFinAbsence());
									nbAbsenceByNatureByYear += getDuree(
											absenceBean.getNombreJourOuvre(),
											nbrJourOuvreYearAgo,
											nbrJourOuvreYearNext, 0);
									;
								} else {
									GregorianCalendar calTmp2 = new GregorianCalendar(
											year, 0, 1);
									nbAbsenceByNatureByYear += getNbrJourOuvre(
											calTmp2.getTime(), calTmp.getTime());
									;
								}
							}
						}
					}
				}
			}
		}
		return nbAbsenceByNatureByYear;
	}

	/**
	 * 
	 * @param dateDebutAbsence
	 * @param dateFinAbsence
	 * @return
	 */
	private Double getDuree(Double nbrJourOuvre, Double nbrJourOuvreYearAgo,
			Double nbrJourOuvreYearNext, int periode) {
		if (periode == 0)
			return (nbrJourOuvre * nbrJourOuvreYearAgo)
					/ (nbrJourOuvreYearAgo + nbrJourOuvreYearNext);
		else
			return (nbrJourOuvre * nbrJourOuvreYearNext)
					/ (nbrJourOuvreYearAgo + nbrJourOuvreYearNext);
	}

	private String getTauxAbsenteismeByYear(Double nbJoursAbsence, int year)
			throws Exception {
		ParamsGenerauxServiceImpl paramsGenerauxService = new ParamsGenerauxServiceImpl();
		List<ParamsGenerauxBean> paramsGenerauxBeanList = paramsGenerauxService
				.getParamsGenerauxBeanListByIdEntreprise(this.idEntrepriseSelected);

		Double tauxAbsenteisme = null;
		String tauxAbsenteismeAffichage = new String();

		if (paramsGenerauxBeanList != null && paramsGenerauxBeanList.size() > 0) {

			ParamsGenerauxBean paramsGenerauxBean = paramsGenerauxBeanList
					.get(0);

			Double dureeTravailJoursEffectifTot = null;

			if (year == 0) {
				if (paramsGenerauxBean.getDureeTravailAnNJoursEffectifTot() != null) {
					dureeTravailJoursEffectifTot = paramsGenerauxBean
							.getDureeTravailAnNJoursEffectifTot().doubleValue();
				}
			} else if (year == 1) {
				if (paramsGenerauxBean.getDureeTravailAnN1JoursEffectifTot() != null) {
					dureeTravailJoursEffectifTot = paramsGenerauxBean
							.getDureeTravailAnN1JoursEffectifTot()
							.doubleValue();
				}
			} else {
				if (paramsGenerauxBean.getDureeTravailAnN2JoursEffectifTot() != null) {
					dureeTravailJoursEffectifTot = paramsGenerauxBean
							.getDureeTravailAnN2JoursEffectifTot()
							.doubleValue();
				}
			}

			if (dureeTravailJoursEffectifTot != null) {

				tauxAbsenteisme = ((double) nbJoursAbsence / dureeTravailJoursEffectifTot) * 100;

				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2); // arrondi à 2 chiffres apres la
				// virgules
				df.setMinimumFractionDigits(2);
				df.setDecimalSeparatorAlwaysShown(true);

				this.afficheErreur = false;
				tauxAbsenteismeAffichage = df.format(tauxAbsenteisme);
			} else {
				this.afficheErreur = true;
				tauxAbsenteismeAffichage = INDEFINI;
			}
		} else {
			this.afficheErreur = true;
			tauxAbsenteismeAffichage = INDEFINI;
		}

		return tauxAbsenteismeAffichage;

		// return " /!\\ ";
	}

	/**
	 * 
	 * @return
	 */
	public boolean isAfficheErreur() {
		return afficheErreur;
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
		entete[0] = "Nature des absences";
		entete[1] = getTwoYearAgo().toString();
		entete[2] = getOneYearAgo().toString();
		entete[3] = getCurYear().toString();

		try {
			eContext.getSessionMap().put("datatable",
					this.absencesNatureBeanList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Absence par nature");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}
}
