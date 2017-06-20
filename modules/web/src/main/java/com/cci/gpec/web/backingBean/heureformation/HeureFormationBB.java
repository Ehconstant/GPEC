package com.cci.gpec.web.backingBean.heureformation;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.HeureBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.ParamsGenerauxBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FormationServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.ParamsGenerauxServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.icesoft.faces.context.effects.JavascriptContext;

public class HeureFormationBB {
	// Variable pour calculer les années dans les entetes
	private Integer twoYearAgo;
	private Integer oneYearAgo;
	private Integer curYear;

	private int totalTwoYearAgo;
	private int totalOneYearAgo;
	private int totalCurYear;

	private int totalTwoYearAgoH;
	private int totalOneYearAgoH;
	private int totalCurYearH;

	private int totalTwoYearAgoF;
	private int totalOneYearAgoF;
	private int totalCurYearF;

	private String totalCoutOPCATwoYearAgo;
	private String totalCoutOPCAOneYearAgo;
	private String totalCoutOPCACurYear;

	private String totalCoutENTTwoYearAgo;
	private String totalCoutENTOneYearAgo;
	private String totalCoutENTCurYear;

	private String totalCoutAUTRETwoYearAgo;
	private String totalCoutAUTREOneYearAgo;
	private String totalCoutAUTRECurYear;

	private String pourcentOPCATwoYearAgo;
	private String pourcentOPCAOneYearAgo;
	private String pourcentOPCACurYear;

	private String contributionFormationTwoYearAgo;
	private String contributionFormationOneYearAgo;
	private String contributionFormationCurYear;

	// Les id mise à jour des selections
	private int idService;
	private int idEntreprise;
	// Listes pour les selectonemenu
	private ArrayList<SelectItem> entreprisesListItem;
	private ArrayList<SelectItem> servicesListItem;
	private ArrayList<HeureBean> heureBeanList;
	private ArrayList<HeureBean> heureBeanListFooter;

	private boolean hasParams = true;

	private List<SalarieBean> salarieBeanListFact = new ArrayList<SalarieBean>();
	private boolean initList = false;

	DecimalFormat df = new DecimalFormat();

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public HeureFormationBB() throws Exception {
		init();
	}

	public void init() throws Exception {
		// init entreprises

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		heureBeanList = new ArrayList<HeureBean>();

		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();

		List<EntrepriseBean> entrepriseBeanList = entrepriseService
				.getEntreprisesList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		entreprisesListItem = new ArrayList<SelectItem>();
		SelectItem selectItem;
		GroupeServiceImpl grServ = new GroupeServiceImpl();
		GroupeBean groupe = grServ.getGroupeBeanById(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		selectItem = new SelectItem();
		selectItem.setValue(-1);
		selectItem.setLabel(groupe.getNom());
		entreprisesListItem.add(selectItem);
		for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(entrepriseBean.getId());
			selectItem.setLabel(entrepriseBean.getNom());
			entreprisesListItem.add(selectItem);
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("entreprise") != null) {
			this.idEntreprise = Integer.parseInt(session.getAttribute(
					"entreprise").toString());
		} else {
			this.idEntreprise = -1;
		}

		this.idService = -1;

		getTableHeureFormation(this.idEntreprise, this.idService);
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

	public void change(ValueChangeEvent event) throws Exception {
		if (event.getComponent().getId().equals("selectEntreprise")) {
			idEntreprise = Integer.parseInt(event.getNewValue().toString());
			idService = -1;
		} else {
			if (event.getComponent().getId().equals("selectService")) {
				idService = Integer.parseInt(event.getNewValue().toString());
			}
		}

		heureBeanList = new ArrayList<HeureBean>();
		heureBeanListFooter = new ArrayList<HeureBean>();
		getTableHeureFormation(this.idEntreprise, this.idService);
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	public Integer getTwoYearAgo() {
		this.twoYearAgo = (getCurYear() - 2);
		return twoYearAgo;
	}

	public Integer getOneYearAgo() {
		this.oneYearAgo = (getCurYear() - 1);
		return oneYearAgo;
	}

	public int getNbHeureFormationByYear(int year, int idEntrepriseSelected,
			int idServiceSelected) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		int nbHeureByMetierByYear = 0;
		boolean flag;
		for (FormationBean formationBean : formationBeanList) {
			flag = false;
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());

			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				Date debutFormation = formationBean.getDebutFormation();

				GregorianCalendar dateDebutFormation = new GregorianCalendar();

				dateDebutFormation.setTime(debutFormation);

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

				if (flag) {
					if (dateDebutFormation.get(Calendar.YEAR) >= year
							&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {
						nbHeureByMetierByYear = nbHeureByMetierByYear
								+ formationBean.getVolumeHoraire();

					}
				}
			}
		}
		return nbHeureByMetierByYear;
	}

	public int getNbHeureFormationByYearH(int year, int idEntrepriseSelected,
			int idServiceSelected) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		int nbHeureByMetierByYearH = 0;
		boolean flag;
		for (FormationBean formationBean : formationBeanList) {
			flag = false;
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());
			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				if (salarieBean.getCivilite().equals("Monsieur")) {

					Date debutFormation = formationBean.getDebutFormation();

					GregorianCalendar dateDebutFormation = new GregorianCalendar();

					dateDebutFormation.setTime(debutFormation);

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

					if (flag) {
						if (dateDebutFormation.get(Calendar.YEAR) >= year
								&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {
							nbHeureByMetierByYearH = nbHeureByMetierByYearH
									+ formationBean.getVolumeHoraire();

						}
					}
				}
			}
		}
		return nbHeureByMetierByYearH;
	}

	public int getNbHommeFormationByYear(int year, int idEntrepriseSelected,
			int idServiceSelected) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		List<SalarieBean> salarieList = new ArrayList<SalarieBean>();

		for (FormationBean formationBean : formationBeanList) {
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());
			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				if (salarieBean.getCivilite().equals("Monsieur")) {

					Date debutFormation = formationBean.getDebutFormation();

					GregorianCalendar dateDebutFormation = new GregorianCalendar();

					dateDebutFormation.setTime(debutFormation);

					if (dateDebutFormation.get(Calendar.YEAR) >= year
							&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {

						if (idEntrepriseSelected != -1) {
							if (idServiceSelected != -1) {
								if (salarieBean.getIdEntrepriseSelected() == idEntrepriseSelected
										&& salarieBean.getIdServiceSelected() == idServiceSelected) {
									if (!salarieList.contains(salarieBean)) {
										salarieList.add(salarieBean);
									}
								}
							} else {
								if (salarieBean.getIdEntrepriseSelected() == idEntrepriseSelected) {
									if (!salarieList.contains(salarieBean)) {
										salarieList.add(salarieBean);
									}
								}
							}
						} else {
							if (!salarieList.contains(salarieBean)) {
								salarieList.add(salarieBean);
							}
						}
					}
				}
			}
		}
		return salarieList.size();
	}

	public int getNbHommeFormationByYear(int year, int idEntrepriseSelected,
			int idServiceSelected, int idMetierSelected) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		List<SalarieBean> salarieList = new ArrayList<SalarieBean>();

		for (FormationBean formationBean : formationBeanList) {
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());
			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				if (salarieBean.getCivilite().equals("Monsieur")) {

					Date debutFormation = formationBean.getDebutFormation();

					GregorianCalendar dateDebutFormation = new GregorianCalendar();

					dateDebutFormation.setTime(debutFormation);

					if (dateDebutFormation.get(Calendar.YEAR) >= year
							&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {

						if (idEntrepriseSelected != -1) {
							if (idServiceSelected != -1) {
								if (salarieBean.getIdEntrepriseSelected() == idEntrepriseSelected
										&& salarieBean.getIdServiceSelected() == idServiceSelected) {

									if (idMetierSelected != -1) {
										MetierBean metierSalar = getMetierBySalarieAndDate(
												salarieBean, dateDebutFormation);
										if ((metierSalar != null)
												&& (metierSalar.getId() == idMetierSelected)) {
											if (!salarieList
													.contains(salarieBean)) {
												salarieList.add(salarieBean);
											}
										}
									} else {
										if (!salarieList.contains(salarieBean)) {
											salarieList.add(salarieBean);
										}
									}
								}
							} else {
								if (salarieBean.getIdEntrepriseSelected() == idEntrepriseSelected) {
									if (!salarieList.contains(salarieBean)) {
										salarieList.add(salarieBean);
									}
								}
							}
						} else {
							if (!salarieList.contains(salarieBean)) {
								salarieList.add(salarieBean);
							}
						}
					}
				}
			}
		}
		return salarieList.size();
	}

	public int getNbFemmeFormationByYear(int year, int idEntrepriseSelected,
			int idServiceSelected) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		List<SalarieBean> salarieList = new ArrayList<SalarieBean>();

		for (FormationBean formationBean : formationBeanList) {
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());
			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				if (!salarieBean.getCivilite().equals("Monsieur")) {

					Date debutFormation = formationBean.getDebutFormation();

					GregorianCalendar dateDebutFormation = new GregorianCalendar();

					dateDebutFormation.setTime(debutFormation);

					if (dateDebutFormation.get(Calendar.YEAR) >= year
							&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {

						if (idEntrepriseSelected != -1) {
							if (idServiceSelected != -1) {
								if (salarieBean.getIdEntrepriseSelected() == idEntrepriseSelected
										&& salarieBean.getIdServiceSelected() == idServiceSelected) {
									if (!salarieList.contains(salarieBean)) {
										salarieList.add(salarieBean);
									}
								}
							} else {
								if (salarieBean.getIdEntrepriseSelected() == idEntrepriseSelected) {
									if (!salarieList.contains(salarieBean)) {
										salarieList.add(salarieBean);
									}
								}
							}
						} else {
							if (!salarieList.contains(salarieBean)) {
								salarieList.add(salarieBean);
							}
						}
					}
				}
			}
		}
		return salarieList.size();
	}

	public int getNbFemmeFormationByYear(int year, int idEntrepriseSelected,
			int idServiceSelected, int idMetierSelected) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		List<SalarieBean> salarieList = new ArrayList<SalarieBean>();

		for (FormationBean formationBean : formationBeanList) {
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());
			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				if (!salarieBean.getCivilite().equals("Monsieur")) {

					Date debutFormation = formationBean.getDebutFormation();

					GregorianCalendar dateDebutFormation = new GregorianCalendar();

					dateDebutFormation.setTime(debutFormation);

					if (dateDebutFormation.get(Calendar.YEAR) >= year
							&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {

						if (idEntrepriseSelected != -1) {
							if (idServiceSelected != -1) {
								if (salarieBean.getIdEntrepriseSelected() == idEntrepriseSelected
										&& salarieBean.getIdServiceSelected() == idServiceSelected) {

									if (idMetierSelected != -1) {
										MetierBean metierSalar = getMetierBySalarieAndDate(
												salarieBean, dateDebutFormation);
										if ((metierSalar != null)
												&& (metierSalar.getId() == idMetierSelected)) {
											if (!salarieList
													.contains(salarieBean)) {
												salarieList.add(salarieBean);
											}
										}
									} else {
										if (!salarieList.contains(salarieBean)) {
											salarieList.add(salarieBean);
										}
									}
								}
							} else {
								if (salarieBean.getIdEntrepriseSelected() == idEntrepriseSelected) {
									if (!salarieList.contains(salarieBean)) {
										salarieList.add(salarieBean);
									}
								}
							}
						} else {
							if (!salarieList.contains(salarieBean)) {
								salarieList.add(salarieBean);
							}
						}
					}
				}
			}
		}
		return salarieList.size();
	}

	public int getNbHeureFormationByYearF(int year, int idEntrepriseSelected,
			int idServiceSelected) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		int nbHeureByMetierByYearF = 0;
		boolean flag;
		for (FormationBean formationBean : formationBeanList) {
			flag = false;
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());

			if (salarieBean.getId() != null) {
				if (!salarieBean.getCivilite().equals("Monsieur")) {
					if (getLastParcours(salarieBean) != null
							&& getLastParcours(salarieBean)
									.getIdTypeContratSelected() != 3
							&& getLastParcours(salarieBean)
									.getIdTypeContratSelected() != 7) {
						Date debutFormation = formationBean.getDebutFormation();

						GregorianCalendar dateDebutFormation = new GregorianCalendar();

						dateDebutFormation.setTime(debutFormation);

						if (idEntrepriseSelected == -1
								&& idServiceSelected == -1) {
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

						if (flag) {
							if (dateDebutFormation.get(Calendar.YEAR) >= year
									&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {
								nbHeureByMetierByYearF = nbHeureByMetierByYearF
										+ formationBean.getVolumeHoraire();

							}
						}
					}
				}
			}
		}
		return nbHeureByMetierByYearF;
	}

	public String getCoutOPCAFormationByYear(int year,
			int idEntrepriseSelected, int idServiceSelected) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		double coutOPCAByYear = 0;
		boolean flag;
		for (FormationBean formationBean : formationBeanList) {
			flag = false;
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());

			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				Date debutFormation = formationBean.getDebutFormation();

				GregorianCalendar dateDebutFormation = new GregorianCalendar();

				dateDebutFormation.setTime(debutFormation);

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

				if (flag) {
					if (dateDebutFormation.get(Calendar.YEAR) >= year
							&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {
						if (formationBean.getCoutOpca() != null) {
							coutOPCAByYear = coutOPCAByYear
									+ formationBean.getCoutOpca();
						}

					}
				}
			}
		}
		return df.format(coutOPCAByYear);
	}

	public String getCoutENTFormationByYear(int year, int idEntrepriseSelected,
			int idServiceSelected) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		double coutENTByYear = 0;
		boolean flag;
		for (FormationBean formationBean : formationBeanList) {
			flag = false;
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());

			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				Date debutFormation = formationBean.getDebutFormation();

				GregorianCalendar dateDebutFormation = new GregorianCalendar();

				dateDebutFormation.setTime(debutFormation);

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

				if (flag) {
					if (dateDebutFormation.get(Calendar.YEAR) >= year
							&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {
						if (formationBean.getCoutEntreprise() != null) {
							coutENTByYear = coutENTByYear
									+ formationBean.getCoutEntreprise();
						}

					}
				}
			}
		}
		return df.format(coutENTByYear);
	}

	public String getCoutAUTREFormationByYear(int year,
			int idEntrepriseSelected, int idServiceSelected) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		double coutAUTREByYear = 0;
		boolean flag;
		for (FormationBean formationBean : formationBeanList) {
			flag = false;
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());

			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				Date debutFormation = formationBean.getDebutFormation();

				GregorianCalendar dateDebutFormation = new GregorianCalendar();

				dateDebutFormation.setTime(debutFormation);

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

				if (flag) {
					if (dateDebutFormation.get(Calendar.YEAR) >= year
							&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {
						if (formationBean.getCoutAutre() != null) {
							coutAUTREByYear = coutAUTREByYear
									+ formationBean.getCoutAutre();
						}

					}
				}
			}
		}
		return df.format(coutAUTREByYear);
	}

	public String getCoutENTFormationByYear(int year, int idEntrepriseSelected,
			int idServiceSelected, int idMetier) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		double coutENTByYear = 0;
		boolean flag;
		for (FormationBean formationBean : formationBeanList) {
			flag = false;
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());

			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				Date debutFormation = formationBean.getDebutFormation();

				GregorianCalendar dateDebutFormation = new GregorianCalendar();

				dateDebutFormation.setTime(debutFormation);

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
				if (flag) {
					if (dateDebutFormation.get(Calendar.YEAR) >= year
							&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {
						MetierBean metierSalar = getMetierBySalarieAndDate(
								salarieBean, dateDebutFormation);
						if ((metierSalar != null)
								&& (metierSalar.getId() == idMetier)) {
							if (formationBean.getCoutEntreprise() != null) {
								coutENTByYear = coutENTByYear
										+ formationBean.getCoutEntreprise();
							}
						}
					}
				}
			}
		}
		return df.format(coutENTByYear);
	}

	public String getCoutOPCAFormationByYear(int year,
			int idEntrepriseSelected, int idServiceSelected, int idMetier)
			throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		double coutOPCAByYear = 0;
		boolean flag;
		for (FormationBean formationBean : formationBeanList) {
			flag = false;
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());

			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				Date debutFormation = formationBean.getDebutFormation();

				GregorianCalendar dateDebutFormation = new GregorianCalendar();

				dateDebutFormation.setTime(debutFormation);

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
				if (flag) {
					if (dateDebutFormation.get(Calendar.YEAR) >= year
							&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {
						MetierBean metierSalar = getMetierBySalarieAndDate(
								salarieBean, dateDebutFormation);
						if ((metierSalar != null)
								&& (metierSalar.getId() == idMetier)) {
							if (formationBean.getCoutOpca() != null) {
								coutOPCAByYear = coutOPCAByYear
										+ formationBean.getCoutOpca();
							}
						}
					}
				}
			}
		}
		return df.format(coutOPCAByYear);
	}

	public String getCoutAUTREFormationByYear(int year,
			int idEntrepriseSelected, int idServiceSelected, int idMetier)
			throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		double coutAUTREByYear = 0;
		boolean flag;
		for (FormationBean formationBean : formationBeanList) {
			flag = false;
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());

			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				Date debutFormation = formationBean.getDebutFormation();

				GregorianCalendar dateDebutFormation = new GregorianCalendar();

				dateDebutFormation.setTime(debutFormation);

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
				if (flag) {
					if (dateDebutFormation.get(Calendar.YEAR) >= year
							&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {
						MetierBean metierSalar = getMetierBySalarieAndDate(
								salarieBean, dateDebutFormation);
						if ((metierSalar != null)
								&& (metierSalar.getId() == idMetier)) {
							if (formationBean.getCoutAutre() != null) {
								coutAUTREByYear = coutAUTREByYear
										+ formationBean.getCoutAutre();
							}
						}
					}
				}
			}
		}
		return df.format(coutAUTREByYear);
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

	public int getNbHeureFormationByYear(int year, int idEntrepriseSelected,
			int idServiceSelected, int idMetier) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		boolean flag;
		int nbHeureByMetierByYear = 0;
		for (FormationBean formationBean : formationBeanList) {
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());
			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				flag = false;
				Date debutFormation = formationBean.getDebutFormation();

				GregorianCalendar dateDebutFormation = new GregorianCalendar();

				dateDebutFormation.setTime(debutFormation);

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

				if (flag) {
					if (dateDebutFormation.get(Calendar.YEAR) >= year
							&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {
						// Absence durant l'année recherchée, reste a savoir si
						// le
						// salarie absent travail pour ce metier
						MetierBean metierSalar = getMetierBySalarieAndDate(
								salarieBean, dateDebutFormation);
						if ((metierSalar != null)
								&& (metierSalar.getId() == idMetier)) {
							nbHeureByMetierByYear = nbHeureByMetierByYear
									+ formationBean.getVolumeHoraire();
						}
					}
				}
			}
		}
		return nbHeureByMetierByYear;
	}

	public int getNbHeureFormationByYearH(int year, int idEntrepriseSelected,
			int idServiceSelected, int idMetier) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		boolean flag;
		int nbHeureByMetierByYearH = 0;
		for (FormationBean formationBean : formationBeanList) {
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());
			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				if (salarieBean.getCivilite().equals("Monsieur")) {
					flag = false;
					Date debutFormation = formationBean.getDebutFormation();

					GregorianCalendar dateDebutFormation = new GregorianCalendar();

					dateDebutFormation.setTime(debutFormation);

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

					if (flag) {
						if (dateDebutFormation.get(Calendar.YEAR) >= year
								&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {
							// Absence durant l'année recherchée, reste a savoir
							// si
							// le
							// salarie absent travail pour ce metier
							MetierBean metierSalar = getMetierBySalarieAndDate(
									salarieBean, dateDebutFormation);
							if ((metierSalar != null)
									&& (metierSalar.getId() == idMetier)) {
								nbHeureByMetierByYearH = nbHeureByMetierByYearH
										+ formationBean.getVolumeHoraire();
							}
						}
					}
				}
			}
		}
		return nbHeureByMetierByYearH;
	}

	public int getNbHeureFormationByYearF(int year, int idEntrepriseSelected,
			int idServiceSelected, int idMetier) throws Exception {

		FormationServiceImpl formationService = new FormationServiceImpl();
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

		ArrayList<FormationBean> formationBeanList = (ArrayList<FormationBean>) formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		boolean flag;
		int nbHeureByMetierByYearF = 0;
		for (FormationBean formationBean : formationBeanList) {
			SalarieBean salarieBean = getSalarieById(formationBean
					.getIdSalarie());
			if (getLastParcours(salarieBean) != null
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 3
					&& getLastParcours(salarieBean).getIdTypeContratSelected() != 7) {
				if (!salarieBean.getCivilite().equals("Monsieur")) {
					flag = false;
					Date debutFormation = formationBean.getDebutFormation();

					GregorianCalendar dateDebutFormation = new GregorianCalendar();

					dateDebutFormation.setTime(debutFormation);

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

					if (flag) {
						if (dateDebutFormation.get(Calendar.YEAR) >= year
								&& dateDebutFormation.get(Calendar.YEAR) < (year + 1)) {
							// Absence durant l'année recherchée, reste a savoir
							// si le salarie absent travail pour ce metier
							MetierBean metierSalar = getMetierBySalarieAndDate(
									salarieBean, dateDebutFormation);
							if ((metierSalar != null)
									&& (metierSalar.getId() == idMetier)) {
								nbHeureByMetierByYearF = nbHeureByMetierByYearF
										+ formationBean.getVolumeHoraire();
							}
						}
					}
				}
			}
		}
		return nbHeureByMetierByYearF;
	}

	/**
	 * Retourne le poste d'un salarie à une date donnée
	 * 
	 * @param salarie
	 * @param d
	 * @return
	 * @throws Exception
	 */
	private MetierBean getMetierBySalarieAndDate(SalarieBean salarie,
			GregorianCalendar d) throws Exception {
		List<ParcoursBean> listParcour = salarie.getParcoursBeanList();
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
			if (d.after(deb) && d.before(fin)) {
				MetierServiceImpl servMetier = new MetierServiceImpl();
				return servMetier.getMetierBeanById(pb.getIdMetierSelected());
			}
		}
		return null;
	}

	public void getTableHeureFormation(int idEntrepriseSelected,
			int idServiceSelected) throws Exception {

		ParamsGenerauxServiceImpl pg = new ParamsGenerauxServiceImpl();

		// Initialisation des items
		this.totalCurYear = 0;
		this.totalOneYearAgo = 0;
		this.totalTwoYearAgo = 0;
		this.totalCurYearF = 0;
		this.totalOneYearAgoF = 0;
		this.totalTwoYearAgoF = 0;
		this.totalCurYearH = 0;
		this.totalOneYearAgoH = 0;
		this.totalTwoYearAgoH = 0;
		this.totalCoutENTCurYear = "0";
		this.totalCoutENTOneYearAgo = "0";
		this.totalCoutENTTwoYearAgo = "0";
		this.totalCoutOPCACurYear = "0";
		this.totalCoutOPCAOneYearAgo = "0";
		this.totalCoutOPCATwoYearAgo = "0";
		this.totalCoutAUTRECurYear = "0";
		this.totalCoutAUTREOneYearAgo = "0";
		this.totalCoutAUTRETwoYearAgo = "0";
		this.pourcentOPCACurYear = "0";
		this.pourcentOPCAOneYearAgo = "0";
		this.pourcentOPCATwoYearAgo = "0";
		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
		List<EntrepriseBean> entrepriseBeanList = entrepriseService
				.getEntreprisesList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		Collections.sort(entrepriseBeanList);
		heureBeanList = new ArrayList<HeureBean>();
		heureBeanListFooter = new ArrayList<HeureBean>();

		HeureBean heureBean;

		if (idEntrepriseSelected == -1) {
			for (EntrepriseBean entreprise : entrepriseBeanList) {

				hasParams = true;

				heureBean = new HeureBean();
				heureBean.setLibelle(entreprise.getNom());

				heureBean.setHeureCurYear(getNbHeureFormationByYear(
						getCurYear(), entreprise.getId(), this.idService));
				heureBean.setHeureOneYearAgo(getNbHeureFormationByYear(
						getOneYearAgo(), entreprise.getId(), this.idService));
				heureBean.setHeureTwoYearAgo(getNbHeureFormationByYear(
						getTwoYearAgo(), entreprise.getId(), this.idService));

				heureBean.setHeureCurYearH(getNbHommeFormationByYear(
						getCurYear(), entreprise.getId(), this.idService));
				heureBean.setHeureOneYearAgoH(getNbHommeFormationByYear(
						getOneYearAgo(), entreprise.getId(), this.idService));
				heureBean.setHeureTwoYearAgoH(getNbHommeFormationByYear(
						getTwoYearAgo(), entreprise.getId(), this.idService));

				heureBean.setHeureCurYearF(getNbFemmeFormationByYear(
						getCurYear(), entreprise.getId(), this.idService));
				heureBean.setHeureOneYearAgoF(getNbFemmeFormationByYear(
						getOneYearAgo(), entreprise.getId(), this.idService));
				heureBean.setHeureTwoYearAgoF(getNbFemmeFormationByYear(
						getTwoYearAgo(), entreprise.getId(), this.idService));

				heureBean.setCoutENTCurYear(getCoutENTFormationByYear(
						getCurYear(), entreprise.getId(), this.idService));
				heureBean.setCoutENTOneYearAgo(getCoutENTFormationByYear(
						getOneYearAgo(), entreprise.getId(), this.idService));
				heureBean.setCoutENTTwoYearAgo(getCoutENTFormationByYear(
						getTwoYearAgo(), entreprise.getId(), this.idService));

				heureBean.setCoutOPCACurYear(getCoutOPCAFormationByYear(
						getCurYear(), entreprise.getId(), this.idService));
				heureBean.setCoutOPCAOneYearAgo(getCoutOPCAFormationByYear(
						getOneYearAgo(), entreprise.getId(), this.idService));
				heureBean.setCoutOPCATwoYearAgo(getCoutOPCAFormationByYear(
						getTwoYearAgo(), entreprise.getId(), this.idService));

				heureBean.setCoutAUTRECurYear(getCoutAUTREFormationByYear(
						getCurYear(), entreprise.getId(), this.idService));
				heureBean.setCoutAUTREOneYearAgo(getCoutAUTREFormationByYear(
						getOneYearAgo(), entreprise.getId(), this.idService));
				heureBean.setCoutAUTRETwoYearAgo(getCoutAUTREFormationByYear(
						getTwoYearAgo(), entreprise.getId(), this.idService));

				this.totalCurYear = totalCurYear + heureBean.getHeureCurYear();
				this.totalOneYearAgo = totalOneYearAgo
						+ heureBean.getHeureOneYearAgo();
				this.totalTwoYearAgo = totalTwoYearAgo
						+ heureBean.getHeureTwoYearAgo();

				this.totalCurYearH = totalCurYearH
						+ heureBean.getHeureCurYearH();
				this.totalOneYearAgoH = totalOneYearAgoH
						+ heureBean.getHeureOneYearAgoH();
				this.totalTwoYearAgoH = totalTwoYearAgoH
						+ heureBean.getHeureTwoYearAgoH();

				this.totalCurYearF = totalCurYearF
						+ heureBean.getHeureCurYearF();
				this.totalOneYearAgoF = totalOneYearAgoF
						+ heureBean.getHeureOneYearAgoF();
				this.totalTwoYearAgoF = totalTwoYearAgoF
						+ heureBean.getHeureTwoYearAgoF();

				this.totalCoutOPCACurYear = df.format(Double
						.valueOf(totalCoutOPCACurYear)
						+ Double.valueOf(heureBean.getCoutOPCACurYear()));
				this.totalCoutOPCAOneYearAgo = df.format(Double
						.valueOf(totalCoutOPCAOneYearAgo)
						+ Double.valueOf(heureBean.getCoutOPCAOneYearAgo()));
				this.totalCoutOPCATwoYearAgo = df.format(Double
						.valueOf(totalCoutOPCATwoYearAgo)
						+ Double.valueOf(heureBean.getCoutOPCATwoYearAgo()));

				this.totalCoutENTCurYear = df.format(Double
						.valueOf(totalCoutENTCurYear)
						+ Double.valueOf(heureBean.getCoutENTCurYear()));
				this.totalCoutENTOneYearAgo = df.format(Double
						.valueOf(totalCoutENTOneYearAgo)
						+ Double.valueOf(heureBean.getCoutENTOneYearAgo()));
				this.totalCoutENTTwoYearAgo = df.format(Double
						.valueOf(totalCoutENTTwoYearAgo)
						+ Double.valueOf(heureBean.getCoutENTTwoYearAgo()));

				this.totalCoutAUTRECurYear = df.format(Double
						.valueOf(totalCoutAUTRECurYear)
						+ Double.valueOf(heureBean.getCoutAUTRECurYear()));
				this.totalCoutAUTREOneYearAgo = df.format(Double
						.valueOf(totalCoutAUTREOneYearAgo)
						+ Double.valueOf(heureBean.getCoutAUTREOneYearAgo()));
				this.totalCoutAUTRETwoYearAgo = df.format(Double
						.valueOf(totalCoutAUTRETwoYearAgo)
						+ Double.valueOf(heureBean.getCoutAUTRETwoYearAgo()));

				heureBean.setFooter(false);
				heureBeanList.add(heureBean);
			}

		} else if (idEntrepriseSelected > -1) {

			if (idServiceSelected == -1) {
				ServiceImpl service = new ServiceImpl();
				ArrayList<ServiceBean> serviceBeanList = (ArrayList<ServiceBean>) service
						.getServiceBeanListByIdEntreprise(idEntreprise);
				Collections.sort(serviceBeanList);

				ParamsGenerauxBean p = new ParamsGenerauxBean();
				p = pg.getParamsGenerauxBeanByIdEntreprise(idEntreprise);
				if (p != null) {
					hasParams = true;
					this.contributionFormationCurYear = (p
							.getPourcentageFormationAnN() != null) ? df
							.format(p.getPourcentageFormationAnN()) : null;
					this.contributionFormationOneYearAgo = (p
							.getPourcentageFormationAnN1() != null) ? df
							.format(p.getPourcentageFormationAnN1()) : null;
					this.contributionFormationTwoYearAgo = (p
							.getPourcentageFormationAnN2() != null) ? df
							.format(p.getPourcentageFormationAnN2()) : null;

					this.pourcentOPCACurYear = (p.getPourcentageFormationAnN() != null) ? df
							.format(((Double
									.valueOf(getCoutOPCAFormationByYear(
											getCurYear(), this.idEntreprise,
											this.idService)) / p
									.getPourcentageFormationAnN()) * 100))
							: null;
					this.pourcentOPCAOneYearAgo = (p
							.getPourcentageFormationAnN1() != null) ? df
							.format(((Double
									.valueOf(getCoutOPCAFormationByYear(
											getOneYearAgo(), this.idEntreprise,
											this.idService)) / p
									.getPourcentageFormationAnN1()) * 100))
							: null;
					this.pourcentOPCATwoYearAgo = (p
							.getPourcentageFormationAnN2() != null) ? df
							.format(((Double
									.valueOf(getCoutOPCAFormationByYear(
											getTwoYearAgo(), this.idEntreprise,
											this.idService)) / p
									.getPourcentageFormationAnN2()) * 100))
							: null;

					for (ServiceBean serviceBean : serviceBeanList) {
						heureBean = new HeureBean();
						heureBean.setLibelle(serviceBean.getNom());
						heureBean.setHeureCurYear(getNbHeureFormationByYear(
								getCurYear(), this.idEntreprise,
								serviceBean.getId()));
						heureBean.setHeureOneYearAgo(getNbHeureFormationByYear(
								getOneYearAgo(), this.idEntreprise,
								serviceBean.getId()));
						heureBean.setHeureTwoYearAgo(getNbHeureFormationByYear(
								getTwoYearAgo(), this.idEntreprise,
								serviceBean.getId()));

						heureBean.setHeureCurYearH(getNbHommeFormationByYear(
								getCurYear(), this.idEntreprise,
								serviceBean.getId()));
						heureBean
								.setHeureOneYearAgoH(getNbHommeFormationByYear(
										getOneYearAgo(), this.idEntreprise,
										serviceBean.getId()));
						heureBean
								.setHeureTwoYearAgoH(getNbHommeFormationByYear(
										getTwoYearAgo(), this.idEntreprise,
										serviceBean.getId()));

						heureBean.setHeureCurYearF(getNbFemmeFormationByYear(
								getCurYear(), this.idEntreprise,
								serviceBean.getId()));
						heureBean
								.setHeureOneYearAgoF(getNbFemmeFormationByYear(
										getOneYearAgo(), this.idEntreprise,
										serviceBean.getId()));
						heureBean
								.setHeureTwoYearAgoF(getNbFemmeFormationByYear(
										getTwoYearAgo(), this.idEntreprise,
										serviceBean.getId()));

						heureBean.setCoutENTCurYear(getCoutENTFormationByYear(
								getCurYear(), this.idEntreprise,
								serviceBean.getId()));
						heureBean
								.setCoutENTOneYearAgo(getCoutENTFormationByYear(
										getOneYearAgo(), this.idEntreprise,
										serviceBean.getId()));
						heureBean
								.setCoutENTTwoYearAgo(getCoutENTFormationByYear(
										getTwoYearAgo(), this.idEntreprise,
										serviceBean.getId()));

						heureBean
								.setCoutOPCACurYear(getCoutOPCAFormationByYear(
										getCurYear(), this.idEntreprise,
										serviceBean.getId()));
						heureBean
								.setCoutOPCAOneYearAgo(getCoutOPCAFormationByYear(
										getOneYearAgo(), this.idEntreprise,
										serviceBean.getId()));
						heureBean
								.setCoutOPCATwoYearAgo(getCoutOPCAFormationByYear(
										getTwoYearAgo(), this.idEntreprise,
										serviceBean.getId()));

						heureBean
								.setCoutAUTRECurYear(getCoutAUTREFormationByYear(
										getCurYear(), this.idEntreprise,
										serviceBean.getId()));
						heureBean
								.setCoutAUTREOneYearAgo(getCoutAUTREFormationByYear(
										getOneYearAgo(), this.idEntreprise,
										serviceBean.getId()));
						heureBean
								.setCoutAUTRETwoYearAgo(getCoutAUTREFormationByYear(
										getTwoYearAgo(), this.idEntreprise,
										serviceBean.getId()));

						this.totalCurYear = totalCurYear
								+ heureBean.getHeureCurYear();
						heureBean.setFooter(false);
						this.totalOneYearAgo = totalOneYearAgo
								+ heureBean.getHeureOneYearAgo();
						this.totalTwoYearAgo = totalTwoYearAgo
								+ heureBean.getHeureTwoYearAgo();

						this.totalCurYearH = totalCurYearH
								+ heureBean.getHeureCurYearH();
						this.totalOneYearAgoH = totalOneYearAgoH
								+ heureBean.getHeureOneYearAgoH();
						this.totalTwoYearAgoH = totalTwoYearAgoH
								+ heureBean.getHeureTwoYearAgoH();

						this.totalCurYearF = totalCurYearF
								+ heureBean.getHeureCurYearF();
						this.totalOneYearAgoF = totalOneYearAgoF
								+ heureBean.getHeureOneYearAgoF();
						this.totalTwoYearAgoF = totalTwoYearAgoF
								+ heureBean.getHeureTwoYearAgoF();

						this.totalCoutOPCACurYear = df
								.format(Double.valueOf(totalCoutOPCACurYear)
										+ Double.valueOf(heureBean
												.getCoutOPCACurYear()));
						this.totalCoutOPCAOneYearAgo = df.format(Double
								.valueOf(totalCoutOPCAOneYearAgo)
								+ Double.valueOf(heureBean
										.getCoutOPCAOneYearAgo()));
						this.totalCoutOPCATwoYearAgo = df.format(Double
								.valueOf(totalCoutOPCATwoYearAgo)
								+ Double.valueOf(heureBean
										.getCoutOPCATwoYearAgo()));

						this.totalCoutENTCurYear = df
								.format(Double.valueOf(totalCoutENTCurYear)
										+ Double.valueOf(heureBean
												.getCoutENTCurYear()));
						this.totalCoutENTOneYearAgo = df.format(Double
								.valueOf(totalCoutENTOneYearAgo)
								+ Double.valueOf(heureBean
										.getCoutENTOneYearAgo()));
						this.totalCoutENTTwoYearAgo = df.format(Double
								.valueOf(totalCoutENTTwoYearAgo)
								+ Double.valueOf(heureBean
										.getCoutENTTwoYearAgo()));

						this.totalCoutAUTRECurYear = df.format(Double
								.valueOf(totalCoutAUTRECurYear)
								+ Double.valueOf(heureBean
										.getCoutAUTRECurYear()));
						this.totalCoutAUTREOneYearAgo = df.format(Double
								.valueOf(totalCoutAUTREOneYearAgo)
								+ Double.valueOf(heureBean
										.getCoutAUTREOneYearAgo()));
						this.totalCoutAUTRETwoYearAgo = df.format(Double
								.valueOf(totalCoutAUTRETwoYearAgo)
								+ Double.valueOf(heureBean
										.getCoutAUTRETwoYearAgo()));

						heureBeanList.add(heureBean);
					}
				} else {
					hasParams = false;
				}
			} else {
				SalarieServiceImpl salarieService = new SalarieServiceImpl();
				TreeMap<String, Integer> nomMetierList = new TreeMap<String, Integer>();

				ParamsGenerauxBean p = new ParamsGenerauxBean();
				p = pg.getParamsGenerauxBeanByIdEntreprise(idEntreprise);
				if (p != null) {
					hasParams = true;
					this.contributionFormationCurYear = (p
							.getPourcentageFormationAnN() != null) ? df
							.format(p.getPourcentageFormationAnN()) : null;
					this.contributionFormationOneYearAgo = (p
							.getPourcentageFormationAnN1() != null) ? df
							.format(p.getPourcentageFormationAnN1()) : null;
					this.contributionFormationTwoYearAgo = (p
							.getPourcentageFormationAnN2() != null) ? df
							.format(p.getPourcentageFormationAnN2()) : null;

					this.pourcentOPCACurYear = (p.getPourcentageFormationAnN() != null) ? df
							.format(((Double
									.valueOf(getCoutOPCAFormationByYear(
											getCurYear(), this.idEntreprise,
											this.idService)) / p
									.getPourcentageFormationAnN()) * 100))
							: null;
					this.pourcentOPCAOneYearAgo = (p
							.getPourcentageFormationAnN1() != null) ? df
							.format(((Double
									.valueOf(getCoutOPCAFormationByYear(
											getOneYearAgo(), this.idEntreprise,
											this.idService)) / p
									.getPourcentageFormationAnN1()) * 100))
							: null;
					this.pourcentOPCATwoYearAgo = (p
							.getPourcentageFormationAnN2() != null) ? df
							.format(((Double
									.valueOf(getCoutOPCAFormationByYear(
											getTwoYearAgo(), this.idEntreprise,
											this.idService)) / p
									.getPourcentageFormationAnN2()) * 100))
							: null;

					for (SalarieBean salarieBean : salarieService
							.getSalariesList(Integer.parseInt(session
									.getAttribute("groupe").toString()))) {

						if (salarieBean.getIdEntrepriseSelected() == this.idEntreprise
								&& salarieBean.getIdServiceSelected() == this.idService
								&& getLastParcours(salarieBean) != null
								&& getLastParcours(salarieBean)
										.getIdTypeContratSelected() != 3
								&& getLastParcours(salarieBean)
										.getIdTypeContratSelected() != 7) {
							ParcoursBean parcourBean = getLastParcours(salarieBean);
							if (parcourBean != null) {
								if (!nomMetierList.containsValue(parcourBean
										.getNomMetier())) {
									nomMetierList.put(
											parcourBean.getNomMetier(),
											parcourBean.getIdMetierSelected());
								}
							}
						}
					}

					for (String nomMetier : nomMetierList.keySet()) {
						heureBean = new HeureBean();
						heureBean.setLibelle(nomMetier);
						Integer idMetier = nomMetierList.get(nomMetier);

						heureBean.setHeureCurYear(getNbHeureFormationByYear(
								getCurYear(), this.idEntreprise,
								this.idService, idMetier));
						heureBean.setHeureOneYearAgo(getNbHeureFormationByYear(
								getOneYearAgo(), this.idEntreprise,
								this.idService, idMetier));
						heureBean.setHeureTwoYearAgo(getNbHeureFormationByYear(
								getTwoYearAgo(), this.idEntreprise,
								this.idService, idMetier));

						heureBean.setHeureCurYearH(getNbHommeFormationByYear(
								getCurYear(), this.idEntreprise,
								this.idService, idMetier));
						heureBean
								.setHeureOneYearAgoH(getNbHommeFormationByYear(
										getOneYearAgo(), this.idEntreprise,
										this.idService, idMetier));
						heureBean
								.setHeureTwoYearAgoH(getNbHommeFormationByYear(
										getTwoYearAgo(), this.idEntreprise,
										this.idService, idMetier));

						heureBean.setHeureCurYearF(getNbFemmeFormationByYear(
								getCurYear(), this.idEntreprise,
								this.idService, idMetier));
						heureBean
								.setHeureOneYearAgoF(getNbFemmeFormationByYear(
										getOneYearAgo(), this.idEntreprise,
										this.idService, idMetier));
						heureBean
								.setHeureTwoYearAgoF(getNbFemmeFormationByYear(
										getTwoYearAgo(), this.idEntreprise,
										this.idService, idMetier));

						heureBean.setCoutENTCurYear(getCoutENTFormationByYear(
								getCurYear(), this.idEntreprise,
								this.idService, idMetier));
						heureBean
								.setCoutENTOneYearAgo(getCoutENTFormationByYear(
										getOneYearAgo(), this.idEntreprise,
										this.idService, idMetier));
						heureBean
								.setCoutENTTwoYearAgo(getCoutENTFormationByYear(
										getTwoYearAgo(), this.idEntreprise,
										this.idService, idMetier));

						heureBean
								.setCoutOPCACurYear(getCoutOPCAFormationByYear(
										getCurYear(), this.idEntreprise,
										this.idService, idMetier));
						heureBean
								.setCoutOPCAOneYearAgo(getCoutOPCAFormationByYear(
										getOneYearAgo(), this.idEntreprise,
										this.idService, idMetier));
						heureBean
								.setCoutOPCATwoYearAgo(getCoutOPCAFormationByYear(
										getTwoYearAgo(), this.idEntreprise,
										this.idService, idMetier));

						heureBean
								.setCoutAUTRECurYear(getCoutAUTREFormationByYear(
										getCurYear(), this.idEntreprise,
										this.idService, idMetier));
						heureBean
								.setCoutAUTREOneYearAgo(getCoutAUTREFormationByYear(
										getOneYearAgo(), this.idEntreprise,
										this.idService, idMetier));
						heureBean
								.setCoutAUTRETwoYearAgo(getCoutAUTREFormationByYear(
										getTwoYearAgo(), this.idEntreprise,
										this.idService, idMetier));

						heureBean.setFooter(false);
						this.totalCurYear = totalCurYear
								+ heureBean.getHeureCurYear();
						this.totalOneYearAgo = totalOneYearAgo
								+ heureBean.getHeureOneYearAgo();
						this.totalTwoYearAgo = totalTwoYearAgo
								+ heureBean.getHeureTwoYearAgo();

						this.totalCurYearH = totalCurYearH
								+ heureBean.getHeureCurYearH();
						this.totalOneYearAgoH = totalOneYearAgoH
								+ heureBean.getHeureOneYearAgoH();
						this.totalTwoYearAgoH = totalTwoYearAgoH
								+ heureBean.getHeureTwoYearAgoH();

						this.totalCurYearF = totalCurYearF
								+ heureBean.getHeureCurYearF();
						this.totalOneYearAgoF = totalOneYearAgoF
								+ heureBean.getHeureOneYearAgoF();
						this.totalTwoYearAgoF = totalTwoYearAgoF
								+ heureBean.getHeureTwoYearAgoF();

						this.totalCoutOPCACurYear = df
								.format(Double.valueOf(totalCoutOPCACurYear)
										+ Double.valueOf(heureBean
												.getCoutOPCACurYear()));
						this.totalCoutOPCAOneYearAgo = df.format(Double
								.valueOf(totalCoutOPCAOneYearAgo)
								+ Double.valueOf(heureBean
										.getCoutOPCAOneYearAgo()));
						this.totalCoutOPCATwoYearAgo = df.format(Double
								.valueOf(totalCoutOPCATwoYearAgo)
								+ Double.valueOf(heureBean
										.getCoutOPCATwoYearAgo()));

						this.totalCoutENTCurYear = df
								.format(Double.valueOf(totalCoutENTCurYear)
										+ Double.valueOf(heureBean
												.getCoutENTCurYear()));
						this.totalCoutENTOneYearAgo = df.format(Double
								.valueOf(totalCoutENTOneYearAgo)
								+ Double.valueOf(heureBean
										.getCoutENTOneYearAgo()));
						this.totalCoutENTTwoYearAgo = df.format(Double
								.valueOf(totalCoutENTTwoYearAgo)
								+ Double.valueOf(heureBean
										.getCoutENTTwoYearAgo()));

						this.totalCoutAUTRECurYear = df.format(Double
								.valueOf(totalCoutAUTRECurYear)
								+ Double.valueOf(heureBean
										.getCoutAUTRECurYear()));
						this.totalCoutAUTREOneYearAgo = df.format(Double
								.valueOf(totalCoutAUTREOneYearAgo)
								+ Double.valueOf(heureBean
										.getCoutAUTREOneYearAgo()));
						this.totalCoutAUTRETwoYearAgo = df.format(Double
								.valueOf(totalCoutAUTRETwoYearAgo)
								+ Double.valueOf(heureBean
										.getCoutAUTRETwoYearAgo()));

						heureBeanList.add(heureBean);
					}
				} else {
					hasParams = false;
				}
			}
		}

		heureBean = new HeureBean();
		heureBean.setFooter(true);
		heureBean.setTotalHeureFormation(true);
		heureBean.setHeureCurYear(totalCurYear);
		heureBean.setHeureCurYearH(totalCurYearH);
		heureBean.setHeureCurYearF(totalCurYearF);
		heureBean.setHeureOneYearAgo(totalOneYearAgo);
		heureBean.setHeureOneYearAgoH(totalOneYearAgoH);
		heureBean.setHeureOneYearAgoF(totalOneYearAgoF);
		heureBean.setHeureTwoYearAgo(totalTwoYearAgo);
		heureBean.setHeureTwoYearAgoH(totalTwoYearAgoH);
		heureBean.setHeureTwoYearAgoF(totalTwoYearAgoF);
		heureBean.setLibelle("Total heures de formation");
		heureBeanList.add(heureBean);

		heureBean = new HeureBean();
		heureBean.setFooter(true);
		heureBean.setTotalCoutFormation(true);
		heureBean.setCoutENTCurYear(totalCoutENTCurYear);
		heureBean.setCoutENTOneYearAgo(totalCoutENTOneYearAgo);
		heureBean.setCoutENTTwoYearAgo(totalCoutENTTwoYearAgo);
		heureBean.setCoutOPCACurYear(totalCoutOPCACurYear);
		heureBean.setCoutOPCAOneYearAgo(totalCoutOPCAOneYearAgo);
		heureBean.setCoutOPCATwoYearAgo(totalCoutOPCATwoYearAgo);
		heureBean.setCoutAUTRECurYear(totalCoutAUTRECurYear);
		heureBean.setCoutAUTREOneYearAgo(totalCoutAUTREOneYearAgo);
		heureBean.setCoutAUTRETwoYearAgo(totalCoutAUTRETwoYearAgo);
		heureBean.setLibelle("Total coût de formation");
		heureBeanList.add(heureBean);

		heureBean = new HeureBean();
		heureBean.setFooter(true);
		heureBean.setLibelle("Montant de la contribution formation");
		heureBean.setCoutOPCACurYear(this.contributionFormationCurYear);
		heureBean.setCoutOPCAOneYearAgo(this.contributionFormationOneYearAgo);
		heureBean.setCoutOPCATwoYearAgo(this.contributionFormationTwoYearAgo);
		heureBeanListFooter.add(heureBean);

		heureBean = new HeureBean();
		heureBean.setFooter(true);
		heureBean.setLibelle("% d'utilisation du budget formation OPCA");
		heureBean.setCoutOPCACurYear(this.pourcentOPCACurYear);
		heureBean.setCoutOPCAOneYearAgo(this.pourcentOPCAOneYearAgo);
		heureBean.setCoutOPCATwoYearAgo(this.pourcentOPCATwoYearAgo);
		heureBeanListFooter.add(heureBean);

	}

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

	public int getIdService() {
		return idService;
	}

	public void setIdService(int idService) {
		this.idService = idService;
	}

	public int getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(int idEntreprise) {
		this.idEntreprise = idEntreprise;
	}

	public ArrayList<SelectItem> getEntreprisesListItem() {
		return entreprisesListItem;
	}

	public ArrayList<SelectItem> getServicesListItem() {
		servicesListItem = new ArrayList<SelectItem>();
		if (idEntreprise > 0) {
			ServiceImpl service = new ServiceImpl();

			ArrayList<ServiceBean> serviceBeanList;
			try {
				serviceBeanList = (ArrayList<ServiceBean>) service
						.getServicesList(Integer.parseInt(session.getAttribute(
								"groupe").toString()));

				for (ServiceBean serviceBean : serviceBeanList) {
					if (idEntreprise == serviceBean.getIdEntreprise()) {

						servicesListItem.add(new SelectItem(
								serviceBean.getId(), serviceBean.getNom()));
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return servicesListItem;
	}

	public int getTotalTwoYearAgo() {
		return totalTwoYearAgo;
	}

	public int getTotalOneYearAgo() {
		return totalOneYearAgo;
	}

	public int getTotalCurYear() {
		return totalCurYear;
	}

	public ArrayList<HeureBean> getHeureBeanList() {
		return heureBeanList;
	}

	public void setHeureBeanList(ArrayList<HeureBean> heureBeanList) {
		this.heureBeanList = heureBeanList;
	}

	public String download(ActionEvent e) {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[11];
		entete[0] = "Intitul\u00E9";
		entete[1] = getTwoYearAgo().toString();
		entete[2] = getOneYearAgo().toString();
		entete[3] = getCurYear().toString();
		entete[4] = "Nombre d'heures";
		entete[5] = "Hommes";
		entete[6] = "Femmes";
		entete[7] = "R\u00E9partition du cout de la formation";
		entete[8] = "OPCA";
		entete[9] = "ENTREPRISE";
		entete[10] = "AUTRE";

		ServiceImpl servServ = new ServiceImpl();

		try {
			eContext.getSessionMap().put("datatable", this.heureBeanList);
			eContext.getSessionMap()
					.put("datatable2", this.heureBeanListFooter);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Heure formation");
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

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return "";
	}

	public boolean isAfficheTable() {
		return ((heureBeanList.size() - 2) >= 1);
	}

	public boolean isAfficheTable2() {
		return (((heureBeanList.size() - 2) >= 1) && idEntreprise != -1);
	}

	public int getTotalTwoYearAgoH() {
		return totalTwoYearAgoH;
	}

	public void setTotalTwoYearAgoH(int totalTwoYearAgoH) {
		this.totalTwoYearAgoH = totalTwoYearAgoH;
	}

	public int getTotalOneYearAgoH() {
		return totalOneYearAgoH;
	}

	public void setTotalOneYearAgoH(int totalOneYearAgoH) {
		this.totalOneYearAgoH = totalOneYearAgoH;
	}

	public int getTotalCurYearH() {
		return totalCurYearH;
	}

	public void setTotalCurYearH(int totalCurYearH) {
		this.totalCurYearH = totalCurYearH;
	}

	public int getTotalTwoYearAgoF() {
		return totalTwoYearAgoF;
	}

	public void setTotalTwoYearAgoF(int totalTwoYearAgoF) {
		this.totalTwoYearAgoF = totalTwoYearAgoF;
	}

	public int getTotalOneYearAgoF() {
		return totalOneYearAgoF;
	}

	public void setTotalOneYearAgoF(int totalOneYearAgoF) {
		this.totalOneYearAgoF = totalOneYearAgoF;
	}

	public int getTotalCurYearF() {
		return totalCurYearF;
	}

	public void setTotalCurYearF(int totalCurYearF) {
		this.totalCurYearF = totalCurYearF;
	}

	public String getPourcentOPCATwoYearAgo() {
		return pourcentOPCATwoYearAgo;
	}

	public void setPourcentOPCATwoYearAgo(String pourcentOPCATwoYearAgo) {
		this.pourcentOPCATwoYearAgo = pourcentOPCATwoYearAgo;
	}

	public String getPourcentOPCAOneYearAgo() {
		return pourcentOPCAOneYearAgo;
	}

	public void setPourcentOPCAOneYearAgo(String pourcentOPCAOneYearAgo) {
		this.pourcentOPCAOneYearAgo = pourcentOPCAOneYearAgo;
	}

	public String getPourcentOPCACurYear() {
		return pourcentOPCACurYear;
	}

	public void setPourcentOPCACurYear(String pourcentOPCACurYear) {
		this.pourcentOPCACurYear = pourcentOPCACurYear;
	}

	public void setTwoYearAgo(Integer twoYearAgo) {
		this.twoYearAgo = twoYearAgo;
	}

	public void setOneYearAgo(Integer oneYearAgo) {
		this.oneYearAgo = oneYearAgo;
	}

	public void setTotalTwoYearAgo(int totalTwoYearAgo) {
		this.totalTwoYearAgo = totalTwoYearAgo;
	}

	public void setTotalOneYearAgo(int totalOneYearAgo) {
		this.totalOneYearAgo = totalOneYearAgo;
	}

	public void setTotalCurYear(int totalCurYear) {
		this.totalCurYear = totalCurYear;
	}

	public String getContributionFormationTwoYearAgo() {
		return contributionFormationTwoYearAgo;
	}

	public void setContributionFormationTwoYearAgo(
			String contributionFormationTwoYearAgo) {
		this.contributionFormationTwoYearAgo = contributionFormationTwoYearAgo;
	}

	public String getContributionFormationOneYearAgo() {
		return contributionFormationOneYearAgo;
	}

	public void setContributionFormationOneYearAgo(
			String contributionFormationOneYearAgo) {
		this.contributionFormationOneYearAgo = contributionFormationOneYearAgo;
	}

	public String getContributionFormationCurYear() {
		return contributionFormationCurYear;
	}

	public void setContributionFormationCurYear(
			String contributionFormationCurYear) {
		this.contributionFormationCurYear = contributionFormationCurYear;
	}

	public String getTotalCoutOPCATwoYearAgo() {
		return totalCoutOPCATwoYearAgo;
	}

	public void setTotalCoutOPCATwoYearAgo(String totalCoutOPCATwoYearAgo) {
		this.totalCoutOPCATwoYearAgo = totalCoutOPCATwoYearAgo;
	}

	public String getTotalCoutOPCAOneYearAgoF() {
		return totalCoutOPCAOneYearAgo;
	}

	public void setTotalCoutOPCAOneYearAgoF(String totalCoutOPCAOneYearAgo) {
		this.totalCoutOPCAOneYearAgo = totalCoutOPCAOneYearAgo;
	}

	public String getTotalCoutOPCACurYear() {
		return totalCoutOPCACurYear;
	}

	public void setTotalCoutOPCACurYear(String totalCoutOPCACurYear) {
		this.totalCoutOPCACurYear = totalCoutOPCACurYear;
	}

	public String getTotalCoutENTTwoYearAgo() {
		return totalCoutENTTwoYearAgo;
	}

	public void setTotalCoutENTTwoYearAgo(String totalCoutENTTwoYearAgo) {
		this.totalCoutENTTwoYearAgo = totalCoutENTTwoYearAgo;
	}

	public String getTotalCoutENTOneYearAgo() {
		return totalCoutENTOneYearAgo;
	}

	public void setTotalCoutENTOneYearAgo(String totalCoutENTOneYearAgo) {
		this.totalCoutENTOneYearAgo = totalCoutENTOneYearAgo;
	}

	public String getTotalCoutENTCurYear() {
		return totalCoutENTCurYear;
	}

	public void setTotalCoutENTCurYear(String totalCoutENTCurYear) {
		this.totalCoutENTCurYear = totalCoutENTCurYear;
	}

	public String getTotalCoutOPCAOneYearAgo() {
		return totalCoutOPCAOneYearAgo;
	}

	public void setTotalCoutOPCAOneYearAgo(String totalCoutOPCAOneYearAgo) {
		this.totalCoutOPCAOneYearAgo = totalCoutOPCAOneYearAgo;
	}

	public ArrayList<HeureBean> getHeureBeanListFooter() {
		return heureBeanListFooter;
	}

	public void setHeureBeanListFooter(ArrayList<HeureBean> heureBeanListFooter) {
		this.heureBeanListFooter = heureBeanListFooter;
	}

	public boolean isHasParams() {
		return hasParams;
	}

	public void setHasParams(boolean hasParams) {
		this.hasParams = hasParams;
	}

	public String getTotalCoutAUTRETwoYearAgo() {
		return totalCoutAUTRETwoYearAgo;
	}

	public void setTotalCoutAUTRETwoYearAgo(String totalCoutAUTRETwoYearAgo) {
		this.totalCoutAUTRETwoYearAgo = totalCoutAUTRETwoYearAgo;
	}

	public String getTotalCoutAUTREOneYearAgo() {
		return totalCoutAUTREOneYearAgo;
	}

	public void setTotalCoutAUTREOneYearAgo(String totalCoutAUTREOneYearAgo) {
		this.totalCoutAUTREOneYearAgo = totalCoutAUTREOneYearAgo;
	}

	public String getTotalCoutAUTRECurYear() {
		return totalCoutAUTRECurYear;
	}

	public void setTotalCoutAUTRECurYear(String totalCoutAUTRECurYear) {
		this.totalCoutAUTRECurYear = totalCoutAUTRECurYear;
	}

}
