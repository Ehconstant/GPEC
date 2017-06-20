package com.cci.gpec.web.backingBean.negociationSalariale;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.NegociationSalarialeBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.RemunerationBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.ParcoursServiceImpl;
import com.cci.gpec.metier.implementation.RemunerationServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.icesoft.faces.context.effects.JavascriptContext;

public class NegociationSalarialeBB {

	private ArrayList<SelectItem> entreprisesListItem;
	private ArrayList<SelectItem> servicesListItem;
	private ArrayList<SelectItem> cspListItem;
	private ArrayList<SelectItem> metiersListItem;
	private ArrayList<SelectItem> anneesListItem;

	private List<NegociationSalarialeBean> rowList = new ArrayList<NegociationSalarialeBean>();

	private Integer sommeEffectif = 0;
	private Integer sommeEffectifH = 0;
	private Integer sommeEffectifF = 0;

	private Double sommeSalairesDeBaseBrutsAnnuels;
	private Double sommeSalairesDeBaseBrutsAnnuelsMoy;
	private Double sommeSalairesDeBaseBrutsAnnuelsH;
	private Double sommeSalairesDeBaseBrutsAnnuelsF;

	private Double sommeRemuGlobalesBrutesAnnuelles;
	private Double sommeRemuGlobalesBrutesAnnuellesMoy;
	private Double sommeRemuGlobalesBrutesAnnuellesH;
	private Double sommeRemuGlobalesBrutesAnnuellesF;

	private Double sommeHeuresSupAnnuelles;
	private Double sommeHeuresSupAnnuellesMoy;
	private Double sommeHeuresSupAnnuellesH;
	private Double sommeHeuresSupAnnuellesF;

	private Double sommeAvantagesNonAssujettisAnnuels;
	private Double sommeAvantagesNonAssujettisAnnuelsMoy;
	private Double sommeAvantagesNonAssujettisAnnuelsH;
	private Double sommeAvantagesNonAssujettisAnnuelsF;

	private int idService;
	private int idEntreprise;
	private int idCSP;

	private Integer twoYearAgo;
	private Integer oneYearAgo;
	private Integer curYear;

	private Integer annee;

	private boolean moyenne;

	DecimalFormat df = new DecimalFormat();

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public NegociationSalarialeBB() throws Exception {
		init();
	}

	public void init() throws Exception {

		initSomme();

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		this.moyenne = false;
		this.idCSP = 1;
		this.annee = getCurYear();

		rowList = new ArrayList<NegociationSalarialeBean>();

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

		anneesListItem = new ArrayList<SelectItem>();
		SelectItem item = new SelectItem();
		item.setLabel(getCurYear() + "");
		item.setValue(getCurYear());
		anneesListItem.add(item);
		item = new SelectItem();
		item.setLabel(getOneYearAgo() + "");
		item.setValue(getOneYearAgo());
		anneesListItem.add(item);
		item = new SelectItem();
		item.setLabel(getTwoYearAgo() + "");
		item.setValue(getTwoYearAgo());
		anneesListItem.add(item);

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

		getTable(this.annee, this.idEntreprise, this.idService, this.idCSP);
	}

	public void initSomme() {
		sommeEffectif = 0;
		sommeEffectifH = 0;
		sommeEffectifF = 0;

		sommeSalairesDeBaseBrutsAnnuels = 0.0;
		sommeSalairesDeBaseBrutsAnnuelsMoy = 0.0;
		sommeSalairesDeBaseBrutsAnnuelsH = 0.0;
		sommeSalairesDeBaseBrutsAnnuelsF = 0.0;

		sommeRemuGlobalesBrutesAnnuelles = 0.0;
		sommeRemuGlobalesBrutesAnnuellesMoy = 0.0;
		sommeRemuGlobalesBrutesAnnuellesH = 0.0;
		sommeRemuGlobalesBrutesAnnuellesF = 0.0;

		sommeHeuresSupAnnuelles = 0.0;
		sommeHeuresSupAnnuellesMoy = 0.0;
		sommeHeuresSupAnnuellesH = 0.0;
		sommeHeuresSupAnnuellesF = 0.0;

		sommeAvantagesNonAssujettisAnnuels = 0.0;
		sommeAvantagesNonAssujettisAnnuelsMoy = 0.0;
		sommeAvantagesNonAssujettisAnnuelsH = 0.0;
		sommeAvantagesNonAssujettisAnnuelsF = 0.0;
	}

	public void change(ValueChangeEvent event) throws Exception {
		if (event.getComponent().getId().equals("selectEntreprise")) {
			idEntreprise = Integer.parseInt(event.getNewValue().toString());
			idService = -1;
			idCSP = 1;
		} else {
			if (event.getComponent().getId().equals("selectService")) {
				idService = Integer.parseInt(event.getNewValue().toString());
				idCSP = 1;
			} else {
				if (event.getComponent().getId().equals("selectCSP")) {
					idCSP = Integer.parseInt(event.getNewValue().toString());
				} else {
					if (event.getComponent().getId().equals("selectAnnee")) {
						annee = Integer
								.parseInt(event.getNewValue().toString());
					}
				}
			}
		}
		initSomme();
		rowList = new ArrayList<NegociationSalarialeBean>();
		getTable(this.annee, this.idEntreprise, this.idService, this.idCSP);
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	public void getTable(Integer annee, Integer idEntreprise,
			Integer idService, Integer idCSP) throws Exception {
		List<SalarieBean> s = new ArrayList<SalarieBean>();
		List<String> listCSP = new ArrayList<String>();
		List<String> listMetier = new ArrayList<String>();
		SalarieServiceImpl sal = new SalarieServiceImpl();
		ParcoursServiceImpl par = new ParcoursServiceImpl();
		RemunerationServiceImpl remu = new RemunerationServiceImpl();

		s = getSalariesList(idEntreprise, idService, getCurYear());

		for (SalarieBean salarie : s) {
			if (getLastParcours(salarie) != null) {
				if (idCSP == 1 || idCSP == 3) {
					if (!listCSP.contains(getLastParcours(salarie)
							.getNomTypeStatut())) {
						listCSP.add(getLastParcours(salarie).getNomTypeStatut());
					}
				} else {
					if (!listMetier.contains(getLastParcours(salarie)
							.getNomMetier())) {
						listMetier.add(getLastParcours(salarie).getNomMetier());
					}
				}
			}
		}

		s = getSalariesList(idEntreprise, idService, annee);

		int cpt = 0;
		int cptH = 0;
		int cptF = 0;
		int effectif = 0;
		int effectifH = 0;
		int effectifF = 0;
		int nbSal = 0;
		int nbSalH = 0;
		int nbSalF = 0;
		if (idCSP == 1 || idCSP == 3) {
			Collections.sort(listCSP);
			for (String csp : listCSP) {
				NegociationSalarialeBean nego = new NegociationSalarialeBean();
				nego.setLibelle(csp);
				Double salaire = 0.0;
				Double salaireH = 0.0;
				Double salaireF = 0.0;

				Double remuGlobale = 0.0;
				Double remuGlobaleH = 0.0;
				Double remuGlobaleF = 0.0;

				Double heuresSup = 0.0;
				Double heuresSupH = 0.0;
				Double heuresSupF = 0.0;

				Double avantageNonAssujetti = 0.0;
				Double avantageNonAssujettiH = 0.0;
				Double avantageNonAssujettiF = 0.0;

				cpt = 0;
				cptH = 0;
				cptF = 0;
				effectif = 0;
				effectifH = 0;
				effectifF = 0;
				for (SalarieBean salarie : s) {
					if (getLastParcours(salarie, annee) != null
							&& getLastParcours(salarie, annee)
									.getNomTypeStatut().equals(csp)) {
						effectif++;
						if (salarie.getCivilite().equals("Monsieur")) {
							effectifH++;
						} else {
							effectifF++;
						}
						RemunerationBean r = new RemunerationBean();
						if (remu.getRemuneration(annee, salarie.getId()).size() > 0) {
							nbSal++;
							r = remu.getRemuneration(annee, salarie.getId())
									.get(0);
							salaire += (r.getSalaireDeBase() != null) ? r
									.getSalaireDeBase() : 0;
							remuGlobale += (r.getRemuGlobale() != null) ? r
									.getRemuGlobale() : 0;
							heuresSup += (r.getHeuresSupAnnuelles() != null) ? r
									.getHeuresSupAnnuelles() : 0;
							avantageNonAssujetti += (r
									.getAvantagesNonAssujettisAnnuels() != null) ? r
									.getAvantagesNonAssujettisAnnuels() : 0;
							if (salarie.getCivilite().equals("Monsieur")) {
								salaireH += (r.getSalaireDeBase() != null) ? r
										.getSalaireDeBase() : 0;
								remuGlobaleH += (r.getRemuGlobale() != null) ? r
										.getRemuGlobale() : 0;
								heuresSupH += (r.getHeuresSupAnnuelles() != null) ? r
										.getHeuresSupAnnuelles() : 0;
								avantageNonAssujettiH += (r
										.getAvantagesNonAssujettisAnnuels() != null) ? r
										.getAvantagesNonAssujettisAnnuels() : 0;
								cptH++;
								nbSalH++;
							} else {
								salaireF += (r.getSalaireDeBase() != null) ? r
										.getSalaireDeBase() : 0;
								remuGlobaleF += (r.getRemuGlobale() != null) ? r
										.getRemuGlobale() : 0;
								heuresSupF += (r.getHeuresSupAnnuelles() != null) ? r
										.getHeuresSupAnnuelles() : 0;
								avantageNonAssujettiF += (r
										.getAvantagesNonAssujettisAnnuels() != null) ? r
										.getAvantagesNonAssujettisAnnuels() : 0;
								cptF++;
								nbSalF++;
							}
							cpt++;
						}

					}
				}
				nego.setEffectif(effectif);
				nego.setEffectifF(effectifF);
				nego.setEffectifH(effectifH);
				nego.setFooter(false);

				nego.setSalaireDeBaseBrutAnnuel(df.format(salaire));
				if (salaire == 0 || cpt == 0) {
					nego.setSalaireDeBaseBrutAnnuelMoy(df.format(0));
				} else {
					nego.setSalaireDeBaseBrutAnnuelMoy(df
							.format((salaire / cpt)));
				}

				nego.setRemuGlobaleBruteAnnuelle(df.format(remuGlobale));
				if (remuGlobale == 0 || cpt == 0) {
					nego.setRemuGlobaleBruteAnnuelleMoy(df.format(0));
				} else {
					nego.setRemuGlobaleBruteAnnuelleMoy(df
							.format((remuGlobale / cpt)));
				}

				nego.setHeureSupAnnuelles(df.format(heuresSup));
				if (heuresSup == 0 || cpt == 0) {
					nego.setHeureSupAnnuellesMoy(df.format(0));
				} else {
					nego.setHeureSupAnnuellesMoy(df.format((heuresSup / cpt)));
				}

				nego.setAvantagesNonAssujettisAnnuels(df
						.format(avantageNonAssujetti));
				if (avantageNonAssujetti == 0 || cpt == 0) {
					nego.setAvantagesNonAssujettisAnnuelsMoy(df.format(0));
				} else {
					nego.setAvantagesNonAssujettisAnnuelsMoy(df
							.format((avantageNonAssujetti / cpt)));
				}

				if (idCSP == 3) {
					// FEMMES
					if (salaireF == 0 || cptF == 0) {
						nego.setSalaireDeBaseBrutAnnuelMoyF(df.format(0));
						nego.setSalaireDeBaseBrutAnnuelF(df.format(0));
					} else {
						nego.setSalaireDeBaseBrutAnnuelMoyF(df
								.format((salaireF / cptF)));
						nego.setSalaireDeBaseBrutAnnuelF(df.format(salaireF));
					}

					if (remuGlobaleF == 0 || cptF == 0) {
						nego.setRemuGlobaleBruteAnnuelleMoyF(df.format(0));
						nego.setRemuGlobaleBruteAnnuelleF(df.format(0));
					} else {
						nego.setRemuGlobaleBruteAnnuelleMoyF(df
								.format((remuGlobaleF / cptF)));
						nego.setRemuGlobaleBruteAnnuelleF(df
								.format(remuGlobaleF));
					}

					if (heuresSupF == 0 || cptF == 0) {
						nego.setHeureSupAnnuellesMoyF(df.format(0));
						nego.setHeureSupAnnuellesF(df.format(0));
					} else {
						nego.setHeureSupAnnuellesMoyF(df
								.format((heuresSupF / cptF)));
						nego.setHeureSupAnnuellesF(df.format(heuresSupF));
					}

					if (avantageNonAssujettiF == 0 || cptF == 0) {
						nego.setAvantagesNonAssujettisAnnuelsMoyF(df.format(0));
						nego.setAvantagesNonAssujettisAnnuelsF(df.format(0));
					} else {
						nego.setAvantagesNonAssujettisAnnuelsMoyF(df
								.format((avantageNonAssujettiF / cptF)));
						nego.setAvantagesNonAssujettisAnnuelsF(df
								.format(avantageNonAssujettiF));
					}

					// HOMMES
					if (salaireH == 0 || cptH == 0) {
						nego.setSalaireDeBaseBrutAnnuelMoyH(df.format(0));
						nego.setSalaireDeBaseBrutAnnuelH(df.format(0));
					} else {
						nego.setSalaireDeBaseBrutAnnuelMoyH(df
								.format((salaireH / cptH)));
						nego.setSalaireDeBaseBrutAnnuelH(df.format(salaireH));
					}

					if (remuGlobaleH == 0 || cptH == 0) {
						nego.setRemuGlobaleBruteAnnuelleMoyH(df.format(0));
						nego.setRemuGlobaleBruteAnnuelleH(df.format(0));
					} else {
						nego.setRemuGlobaleBruteAnnuelleMoyH(df
								.format((remuGlobaleH / cptH)));
						nego.setRemuGlobaleBruteAnnuelleH(df
								.format(remuGlobaleH));
					}

					if (heuresSupH == 0 || cptH == 0) {
						nego.setHeureSupAnnuellesMoyH(df.format(0));
						nego.setHeureSupAnnuellesH(df.format(0));
					} else {
						nego.setHeureSupAnnuellesMoyH(df
								.format((heuresSupH / cptH)));
						nego.setHeureSupAnnuellesH(df.format(heuresSupH));
					}

					if (avantageNonAssujettiH == 0 || cptH == 0) {
						nego.setAvantagesNonAssujettisAnnuelsMoyH(df.format(0));
						nego.setAvantagesNonAssujettisAnnuelsH(df.format(0));
					} else {
						nego.setAvantagesNonAssujettisAnnuelsMoyH(df
								.format((avantageNonAssujettiH / cptH)));
						nego.setAvantagesNonAssujettisAnnuelsH(df
								.format(avantageNonAssujettiH));
					}
				}
				rowList.add(nego);
			}
		} else {
			Collections.sort(listMetier);
			for (String metier : listMetier) {
				NegociationSalarialeBean nego = new NegociationSalarialeBean();
				nego.setLibelle(metier);
				Double salaire = 0.0;
				Double salaireH = 0.0;
				Double salaireF = 0.0;

				Double remuGlobale = 0.0;
				Double remuGlobaleH = 0.0;
				Double remuGlobaleF = 0.0;

				Double heuresSup = 0.0;
				Double heuresSupH = 0.0;
				Double heuresSupF = 0.0;

				Double avantageNonAssujetti = 0.0;
				Double avantageNonAssujettiH = 0.0;
				Double avantageNonAssujettiF = 0.0;

				cpt = 0;
				cptH = 0;
				cptF = 0;
				effectif = 0;
				effectifH = 0;
				effectifF = 0;
				for (SalarieBean salarie : s) {
					if (getLastParcours(salarie, annee) != null
							&& getLastParcours(salarie, annee).getNomMetier()
									.equals(metier)) {
						effectif++;
						if (salarie.getCivilite().equals("Monsieur"))
							effectifH++;
						else
							effectifF++;
						RemunerationBean r = new RemunerationBean();
						if (remu.getRemuneration(annee, salarie.getId()).size() > 0) {
							nbSal++;
							r = remu.getRemuneration(annee, salarie.getId())
									.get(0);
							salaire += (r.getSalaireDeBase() != null) ? r
									.getSalaireDeBase() : 0;
							remuGlobale += (r.getRemuGlobale() != null) ? r
									.getRemuGlobale() : 0;
							heuresSup += (r.getHeuresSupAnnuelles() != null) ? r
									.getHeuresSupAnnuelles() : 0;
							avantageNonAssujetti += (r
									.getAvantagesNonAssujettisAnnuels() != null) ? r
									.getAvantagesNonAssujettisAnnuels() : 0;
							if (salarie.getCivilite().equals("Monsieur")) {
								salaireH += (r.getSalaireDeBase() != null) ? r
										.getSalaireDeBase() : 0;
								remuGlobaleH += (r.getRemuGlobale() != null) ? r
										.getRemuGlobale() : 0;
								heuresSupH += (r.getHeuresSupAnnuelles() != null) ? r
										.getHeuresSupAnnuelles() : 0;
								avantageNonAssujettiH += (r
										.getAvantagesNonAssujettisAnnuels() != null) ? r
										.getAvantagesNonAssujettisAnnuels() : 0;
								cptH++;
								nbSalH++;
							} else {
								salaireF += (r.getSalaireDeBase() != null) ? r
										.getSalaireDeBase() : 0;
								remuGlobaleF += (r.getRemuGlobale() != null) ? r
										.getRemuGlobale() : 0;
								heuresSupF += (r.getHeuresSupAnnuelles() != null) ? r
										.getHeuresSupAnnuelles() : 0;
								avantageNonAssujettiF += (r
										.getAvantagesNonAssujettisAnnuels() != null) ? r
										.getAvantagesNonAssujettisAnnuels() : 0;
								cptF++;
								nbSalF++;
							}
							cpt++;
						}

					}
				}
				nego.setEffectif(effectif);
				nego.setEffectifF(effectifF);
				nego.setEffectifH(effectifH);
				nego.setFooter(false);

				nego.setSalaireDeBaseBrutAnnuel(df.format(salaire));
				if (salaire == 0 || cpt == 0) {
					nego.setSalaireDeBaseBrutAnnuelMoy(df.format(0));
				} else {
					nego.setSalaireDeBaseBrutAnnuelMoy(df
							.format((salaire / cpt)));
				}

				nego.setRemuGlobaleBruteAnnuelle(df.format(remuGlobale));
				if (remuGlobale == 0 || cpt == 0) {
					nego.setRemuGlobaleBruteAnnuelleMoy(df.format(0));
				} else {
					nego.setRemuGlobaleBruteAnnuelleMoy(df
							.format((remuGlobale / cpt)));
				}

				nego.setHeureSupAnnuelles(df.format(heuresSup));
				if (heuresSup == 0 || cpt == 0) {
					nego.setHeureSupAnnuellesMoy(df.format(0));
				} else {
					nego.setHeureSupAnnuellesMoy(df.format((heuresSup / cpt)));
				}

				nego.setAvantagesNonAssujettisAnnuels(df
						.format(avantageNonAssujetti));
				if (avantageNonAssujetti == 0 || cpt == 0) {
					nego.setAvantagesNonAssujettisAnnuelsMoy(df.format(0));
				} else {
					nego.setAvantagesNonAssujettisAnnuelsMoy(df
							.format((avantageNonAssujetti / cpt)));
				}

				if (idCSP == 4) {
					// FEMMES
					if (salaireF == 0 || cptF == 0) {
						nego.setSalaireDeBaseBrutAnnuelMoyF(df.format(0));
						nego.setSalaireDeBaseBrutAnnuelF(df.format(0));
					} else {
						nego.setSalaireDeBaseBrutAnnuelMoyF(df
								.format((salaireF / cptF)));
						nego.setSalaireDeBaseBrutAnnuelF(df.format(salaireF));
					}

					if (remuGlobaleF == 0 || cptF == 0) {
						nego.setRemuGlobaleBruteAnnuelleMoyF(df.format(0));
						nego.setRemuGlobaleBruteAnnuelleF(df.format(0));
					} else {
						nego.setRemuGlobaleBruteAnnuelleMoyF(df
								.format((remuGlobaleF / cptF)));
						nego.setRemuGlobaleBruteAnnuelleF(df
								.format(remuGlobaleF));
					}

					if (heuresSupF == 0 || cptF == 0) {
						nego.setHeureSupAnnuellesMoyF(df.format(0));
						nego.setHeureSupAnnuellesF(df.format(0));
					} else {
						nego.setHeureSupAnnuellesMoyF(df
								.format((heuresSupF / cptF)));
						nego.setHeureSupAnnuellesF(df.format(heuresSupF));
					}

					if (avantageNonAssujettiF == 0 || cptF == 0) {
						nego.setAvantagesNonAssujettisAnnuelsMoyF(df.format(0));
						nego.setAvantagesNonAssujettisAnnuelsF(df.format(0));
					} else {
						nego.setAvantagesNonAssujettisAnnuelsMoyF(df
								.format((avantageNonAssujettiF / cptF)));
						nego.setAvantagesNonAssujettisAnnuelsF(df
								.format(avantageNonAssujettiF));
					}

					// HOMMES
					if (salaireH == 0 || cptH == 0) {
						nego.setSalaireDeBaseBrutAnnuelMoyH(df.format(0));
						nego.setSalaireDeBaseBrutAnnuelH(df.format(0));
					} else {
						nego.setSalaireDeBaseBrutAnnuelMoyH(df
								.format((salaireH / cptH)));
						nego.setSalaireDeBaseBrutAnnuelH(df.format(salaireH));
					}

					if (remuGlobaleH == 0 || cptH == 0) {
						nego.setRemuGlobaleBruteAnnuelleMoyH(df.format(0));
						nego.setRemuGlobaleBruteAnnuelleH(df.format(0));
					} else {
						nego.setRemuGlobaleBruteAnnuelleMoyH(df
								.format((remuGlobaleH / cptH)));
						nego.setRemuGlobaleBruteAnnuelleH(df
								.format(remuGlobaleH));
					}

					if (heuresSupH == 0 || cptH == 0) {
						nego.setHeureSupAnnuellesMoyH(df.format(0));
						nego.setHeureSupAnnuellesH(df.format(0));
					} else {
						nego.setHeureSupAnnuellesMoyH(df
								.format((heuresSupH / cptH)));
						nego.setHeureSupAnnuellesH(df.format(heuresSupH));
					}

					if (avantageNonAssujettiH == 0 || cptH == 0) {
						nego.setAvantagesNonAssujettisAnnuelsMoyH(df.format(0));
						nego.setAvantagesNonAssujettisAnnuelsH(df.format(0));
					} else {
						nego.setAvantagesNonAssujettisAnnuelsMoyH(df
								.format((avantageNonAssujettiH / cptH)));
						nego.setAvantagesNonAssujettisAnnuelsH(df
								.format(avantageNonAssujettiH));
					}
				}
				rowList.add(nego);
			}
		}

		for (NegociationSalarialeBean ns : rowList) {

			sommeEffectif += ns.getEffectif();

			if (idCSP == 3 || idCSP == 4) {
				sommeEffectifH += ns.getEffectifH();
				sommeEffectifF += ns.getEffectifF();
			}

			sommeSalairesDeBaseBrutsAnnuels += (ns.getSalaireDeBaseBrutAnnuel()
					.equals("-")) ? 0 : Double.valueOf(ns
					.getSalaireDeBaseBrutAnnuel());
			sommeSalairesDeBaseBrutsAnnuelsMoy += (ns
					.getSalaireDeBaseBrutAnnuelMoy().equals("-")) ? 0 : Double
					.valueOf(ns.getSalaireDeBaseBrutAnnuelMoy());
			if (idCSP == 3 || idCSP == 4) {
				sommeSalairesDeBaseBrutsAnnuelsH += (ns
						.getSalaireDeBaseBrutAnnuelH().equals("-")) ? 0
						: Double.valueOf(ns.getSalaireDeBaseBrutAnnuelH());
				sommeSalairesDeBaseBrutsAnnuelsF += (ns
						.getSalaireDeBaseBrutAnnuelF().equals("-")) ? 0
						: Double.valueOf(ns.getSalaireDeBaseBrutAnnuelF());
			}

			sommeRemuGlobalesBrutesAnnuelles += (ns
					.getRemuGlobaleBruteAnnuelle().equals("-")) ? 0 : Double
					.valueOf(ns.getRemuGlobaleBruteAnnuelle());
			sommeRemuGlobalesBrutesAnnuellesMoy += (ns
					.getRemuGlobaleBruteAnnuelleMoy().equals("-")) ? 0 : Double
					.valueOf(ns.getRemuGlobaleBruteAnnuelleMoy());
			if (idCSP == 3 || idCSP == 4) {
				sommeRemuGlobalesBrutesAnnuellesH += (ns
						.getRemuGlobaleBruteAnnuelleH().equals("-")) ? 0
						: Double.valueOf(ns.getRemuGlobaleBruteAnnuelleH());
				sommeRemuGlobalesBrutesAnnuellesF += (ns
						.getRemuGlobaleBruteAnnuelleF().equals("-")) ? 0
						: Double.valueOf(ns.getRemuGlobaleBruteAnnuelleF());
			}

			sommeHeuresSupAnnuelles += (ns.getHeureSupAnnuelles().equals("-")) ? 0
					: Double.valueOf(ns.getHeureSupAnnuelles());
			sommeHeuresSupAnnuellesMoy += (ns.getHeureSupAnnuellesMoy()
					.equals("-")) ? 0 : Double.valueOf(ns
					.getHeureSupAnnuellesMoy());
			if (idCSP == 3 || idCSP == 4) {
				sommeHeuresSupAnnuellesH += (ns.getHeureSupAnnuellesH()
						.equals("-")) ? 0 : Double.valueOf(ns
						.getHeureSupAnnuellesH());
				sommeHeuresSupAnnuellesF += (ns.getHeureSupAnnuellesF()
						.equals("-")) ? 0 : Double.valueOf(ns
						.getHeureSupAnnuellesF());
			}

			sommeAvantagesNonAssujettisAnnuels += (ns
					.getAvantagesNonAssujettisAnnuels().equals("-")) ? 0
					: Double.valueOf(ns.getAvantagesNonAssujettisAnnuels());
			sommeAvantagesNonAssujettisAnnuelsMoy += (ns
					.getAvantagesNonAssujettisAnnuelsMoy().equals("-")) ? 0
					: Double.valueOf(ns.getAvantagesNonAssujettisAnnuelsMoy());
			if (idCSP == 3 || idCSP == 4) {
				sommeAvantagesNonAssujettisAnnuelsH += (ns
						.getAvantagesNonAssujettisAnnuelsH().equals("-")) ? 0
						: Double.valueOf(ns.getAvantagesNonAssujettisAnnuelsH());
				sommeAvantagesNonAssujettisAnnuelsF += (ns
						.getAvantagesNonAssujettisAnnuelsF().equals("-")) ? 0
						: Double.valueOf(ns.getAvantagesNonAssujettisAnnuelsF());
			}
		}
		NegociationSalarialeBean nego = new NegociationSalarialeBean();
		if (idCSP == 3 || idCSP == 4) {
			nego.setLibelle("REPARTITION GLOBALE DES EFFECTIFS");
		} else {
			nego.setLibelle("Données globales / tous effectifs confondus");
		}

		nego.setEffectif(sommeEffectif);
		nego.setFooter(true);

		nego.setSalaireDeBaseBrutAnnuel(df
				.format(sommeSalairesDeBaseBrutsAnnuels));
		if (nbSal != 0) {
			nego.setSalaireDeBaseBrutAnnuelMoy(df
					.format((sommeSalairesDeBaseBrutsAnnuels / nbSal)));
		} else {
			nego.setSalaireDeBaseBrutAnnuelMoy(df.format(0));
		}

		nego.setRemuGlobaleBruteAnnuelle(df
				.format(sommeRemuGlobalesBrutesAnnuelles));
		if (nbSal != 0) {
			nego.setRemuGlobaleBruteAnnuelleMoy(df
					.format((sommeRemuGlobalesBrutesAnnuelles / nbSal)));
		} else {
			nego.setRemuGlobaleBruteAnnuelleMoy(df.format(0));
		}

		nego.setHeureSupAnnuelles(df.format(sommeHeuresSupAnnuelles));
		if (nbSal != 0) {
			nego.setHeureSupAnnuellesMoy(df
					.format((sommeHeuresSupAnnuelles / nbSal)));
		} else {
			nego.setHeureSupAnnuellesMoy(df.format(0));
		}

		nego.setAvantagesNonAssujettisAnnuels(df
				.format(sommeAvantagesNonAssujettisAnnuels));
		if (nbSal != 0) {
			nego.setAvantagesNonAssujettisAnnuelsMoy(df
					.format((sommeAvantagesNonAssujettisAnnuels / nbSal)));
		} else {
			nego.setAvantagesNonAssujettisAnnuelsMoy(df.format(0));
		}

		if (idCSP == 3 || idCSP == 4) {
			// HOMMES
			nego.setEffectifH(sommeEffectifH);
			nego.setFooter(true);

			if (sommeSalairesDeBaseBrutsAnnuelsH != 0 && nbSalH != 0) {
				nego.setSalaireDeBaseBrutAnnuelMoyH(df
						.format((sommeSalairesDeBaseBrutsAnnuelsH / nbSalH)));
			} else {
				nego.setSalaireDeBaseBrutAnnuelMoyH("0.00");
			}

			if (sommeRemuGlobalesBrutesAnnuellesH != 0 && nbSalH != 0) {
				nego.setRemuGlobaleBruteAnnuelleMoyH(df
						.format((sommeRemuGlobalesBrutesAnnuellesH / nbSalH)));
			} else {
				nego.setRemuGlobaleBruteAnnuelleMoyH("0.00");
			}

			if (sommeHeuresSupAnnuellesH != 0 && nbSalH != 0) {
				nego.setHeureSupAnnuellesMoyH(df
						.format((sommeHeuresSupAnnuellesH / nbSalH)));
			} else {
				nego.setHeureSupAnnuellesMoyH("0.00");
			}

			if (sommeAvantagesNonAssujettisAnnuelsH != 0 && nbSalH != 0) {
				nego.setAvantagesNonAssujettisAnnuelsMoyH(df
						.format((sommeAvantagesNonAssujettisAnnuelsH / nbSalH)));
			} else {
				nego.setAvantagesNonAssujettisAnnuelsMoyH("0.00");
			}

			// FEMMES
			nego.setEffectifF(sommeEffectifF);
			nego.setFooter(true);

			if (sommeSalairesDeBaseBrutsAnnuelsF != 0 && nbSalF != 0) {
				nego.setSalaireDeBaseBrutAnnuelMoyF(df
						.format((sommeSalairesDeBaseBrutsAnnuelsF / nbSalF)));
			} else {
				nego.setSalaireDeBaseBrutAnnuelMoyF("0.00");
			}

			if (sommeRemuGlobalesBrutesAnnuellesF != 0 && nbSalF != 0) {
				nego.setRemuGlobaleBruteAnnuelleMoyF(df
						.format((sommeRemuGlobalesBrutesAnnuellesF / nbSalF)));
			} else {
				nego.setRemuGlobaleBruteAnnuelleMoyF("0.00");
			}

			if (sommeHeuresSupAnnuellesF != 0 && nbSalF != 0) {
				nego.setHeureSupAnnuellesMoyF(df
						.format((sommeHeuresSupAnnuellesF / nbSalF)));
			} else {
				nego.setHeureSupAnnuellesMoyF("0.00");
			}

			if (sommeAvantagesNonAssujettisAnnuelsF != 0 && nbSalF != 0) {
				nego.setAvantagesNonAssujettisAnnuelsMoyF(df
						.format((sommeAvantagesNonAssujettisAnnuelsF / nbSalF)));
			} else {
				nego.setAvantagesNonAssujettisAnnuelsMoyF("0.00");
			}
		}

		rowList.add(nego);

	}

	public List<SalarieBean> getSalariesList(int idEntreprise, int idService,
			int annee) throws Exception {
		List<SalarieBean> list = new ArrayList<SalarieBean>();
		List<SalarieBean> listTemp = new ArrayList<SalarieBean>();

		SalarieServiceImpl sal = new SalarieServiceImpl();
		if (idEntreprise != -1) {
			listTemp = sal.getSalarieBeanListByIdEntreprise(idEntreprise);
		} else {
			listTemp = sal.getSalariesList(Integer.parseInt(session
					.getAttribute("groupe").toString()));
		}

		for (SalarieBean s : listTemp) {
			if (s.isPresent()) {
				Calendar embauche = new GregorianCalendar();

				ParcoursBean pb = getFirstParcours(s);
				if (pb != null) {
					embauche.setTime(pb.getDebutFonction());
				} else {
					embauche.setTime(new Date());
				}

				RemunerationServiceImpl remu = new RemunerationServiceImpl();

				if (embauche.get(Calendar.YEAR) <= annee
						&& getLastParcours(s) != null)
					if (idService != -1) {
						if (s.getIdServiceSelected() == idService) {
							list.add(s);
						}
					} else {
						list.add(s);
					}
			}
		}

		return list;
	}

	private ParcoursBean getFirstParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		Collections.sort(parcourList);
		Collections.reverse(parcourList);
		ParcoursBean pb = null;
		Long diffMillis;
		Long diffenjours = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			if (pb == null) {
				pb = parcour;
			}
			int nbJours = 0;
			Calendar d1 = new GregorianCalendar();
			Calendar d2 = new GregorianCalendar();
			if (parcour.getFinFonction() != null) {
				d1.setTime(parcour.getFinFonction());
			} else {
				d1.setTime(new Date());
			}
			d2.setTime(pb.getDebutFonction());
			diffMillis = d2.getTimeInMillis() - d1.getTimeInMillis();

			diffenjours = diffMillis / (24 * 60 * 60 * 1000);
			if (parcour.getDebutFonction().before(pb.getDebutFonction())
					&& diffenjours <= 1) {
				pb = parcour;
			}
		}
		return pb;
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

	private ParcoursBean getLastParcours(SalarieBean salarie, int annee) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();

		ParcoursBean parcoursBean = null;
		if (parcourList != null && parcourList.size() > 0) {
			Collections.sort(parcourList);
			Collections.reverse(parcourList);
			for (ParcoursBean p : parcourList) {
				if (p.getFinFonction() != null) {
					GregorianCalendar fin = new GregorianCalendar();
					fin.setTime(p.getFinFonction());
					GregorianCalendar debut = new GregorianCalendar();
					debut.setTime(p.getDebutFonction());
					if (debut.get(Calendar.YEAR) <= annee
							&& fin.get(Calendar.YEAR) >= annee) {
						return p;
					}
				} else {
					GregorianCalendar debut = new GregorianCalendar();
					debut.setTime(p.getDebutFonction());
					if (debut.get(Calendar.YEAR) <= annee) {
						return p;
					}
				}
			}
		}

		return parcoursBean;
	}

	public String download(ActionEvent e) {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[14];
		entete[0] = "Intitul\u00E9";
		entete[1] = "";
		entete[2] = "";
		entete[3] = annee + "";
		entete[4] = "CSP";
		entete[5] = "Métier";
		entete[6] = "Effectif";
		entete[7] = "Salaires bruts annuels";
		entete[8] = "Rémunération globale brute annuelle";
		entete[9] = "Heures supplémentaires annuelles ";
		entete[10] = "Avantages non assujettis annuels ";
		entete[11] = "Moyenne";
		entete[12] = "H";
		entete[13] = "F";

		ServiceImpl servServ = new ServiceImpl();

		try {
			eContext.getSessionMap().put("datatable", this.rowList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Negociation salariale");
			eContext.getSessionMap().put("idEntreprise", idEntreprise);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
			eContext.getSessionMap().put("idCSP", idCSP);
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

	public boolean isAfficheTable1() {
		return (((rowList.size() - 1) >= 1) && (idCSP == 1 || idCSP == 2));
	}

	public boolean isAfficheTable2() {
		return (((rowList.size() - 1) >= 1) && (idCSP == 3 || idCSP == 4));
	}

	public Integer getTwoYearAgo() {
		this.twoYearAgo = (getCurYear() - 2);
		return twoYearAgo;
	}

	public Integer getOneYearAgo() {
		this.oneYearAgo = (getCurYear() - 1);
		return oneYearAgo;
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

	public ArrayList<SelectItem> getEntreprisesListItem() {
		return entreprisesListItem;
	}

	public void setEntreprisesListItem(ArrayList<SelectItem> entreprisesListItem) {
		this.entreprisesListItem = entreprisesListItem;
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

	public void setServicesListItem(ArrayList<SelectItem> servicesListItem) {
		this.servicesListItem = servicesListItem;
	}

	public ArrayList<SelectItem> getCspListItem() {

		cspListItem = new ArrayList<SelectItem>();
		SelectItem s = new SelectItem(1, "CSP");
		cspListItem.add(s);
		s = new SelectItem(2, "Métier");
		cspListItem.add(s);
		s = new SelectItem(3, "CSP - moyenne ventilée par sexe");
		cspListItem.add(s);
		s = new SelectItem(4, "Métier - moyenne ventilée par sexe");
		cspListItem.add(s);

		return cspListItem;
	}

	public void setCspListItem(ArrayList<SelectItem> cspListItem) {
		this.cspListItem = cspListItem;
	}

	public ArrayList<SelectItem> getMetiersListItem() {
		return metiersListItem;
	}

	public void setMetiersListItem(ArrayList<SelectItem> metiersListItem) {
		this.metiersListItem = metiersListItem;
	}

	public Integer getSommeEffectif() {
		return sommeEffectif;
	}

	public void setSommeEffectif(Integer sommeEffectif) {
		this.sommeEffectif = sommeEffectif;
	}

	public Integer getSommeEffectifH() {
		return sommeEffectifH;
	}

	public void setSommeEffectifH(Integer sommeEffectifH) {
		this.sommeEffectifH = sommeEffectifH;
	}

	public Integer getSommeEffectifF() {
		return sommeEffectifF;
	}

	public void setSommeEffectifF(Integer sommeEffectifF) {
		this.sommeEffectifF = sommeEffectifF;
	}

	public Double getSommeSalairesDeBaseBrutsAnnuels() {
		return sommeSalairesDeBaseBrutsAnnuels;
	}

	public void setSommeSalairesDeBaseBrutsAnnuels(
			Double sommeSalairesDeBaseBrutsAnnuels) {
		this.sommeSalairesDeBaseBrutsAnnuels = sommeSalairesDeBaseBrutsAnnuels;
	}

	public Double getSommeSalairesDeBaseBrutsAnnuelsH() {
		return sommeSalairesDeBaseBrutsAnnuelsH;
	}

	public void setSommeSalairesDeBaseBrutsAnnuelsH(
			Double sommeSalairesDeBaseBrutsAnnuelsH) {
		this.sommeSalairesDeBaseBrutsAnnuelsH = sommeSalairesDeBaseBrutsAnnuelsH;
	}

	public Double getSommeSalairesDeBaseBrutsAnnuelsF() {
		return sommeSalairesDeBaseBrutsAnnuelsF;
	}

	public void setSommeSalairesDeBaseBrutsAnnuelsF(
			Double sommeSalairesDeBaseBrutsAnnuelsF) {
		this.sommeSalairesDeBaseBrutsAnnuelsF = sommeSalairesDeBaseBrutsAnnuelsF;
	}

	public Double getSommeRemuGlobalesBrutesAnnuelles() {
		return sommeRemuGlobalesBrutesAnnuelles;
	}

	public void setSommeRemuGlobalesBrutesAnnuelles(
			Double sommeRemuGlobalesBrutesAnnuelles) {
		this.sommeRemuGlobalesBrutesAnnuelles = sommeRemuGlobalesBrutesAnnuelles;
	}

	public Double getSommeRemuGlobalesBrutesAnnuellesH() {
		return sommeRemuGlobalesBrutesAnnuellesH;
	}

	public void setSommeRemuGlobalesBrutesAnnuellesH(
			Double sommeRemuGlobalesBrutesAnnuellesH) {
		this.sommeRemuGlobalesBrutesAnnuellesH = sommeRemuGlobalesBrutesAnnuellesH;
	}

	public Double getSommeRemuGlobalesBrutesAnnuellesF() {
		return sommeRemuGlobalesBrutesAnnuellesF;
	}

	public void setSommeRemuGlobalesBrutesAnnuellesF(
			Double sommeRemuGlobalesBrutesAnnuellesF) {
		this.sommeRemuGlobalesBrutesAnnuellesF = sommeRemuGlobalesBrutesAnnuellesF;
	}

	public Double getSommeHeuresSupAnnuelles() {
		return sommeHeuresSupAnnuelles;
	}

	public void setSommeHeuresSupAnnuelles(Double sommeHeuresSupAnnuelles) {
		this.sommeHeuresSupAnnuelles = sommeHeuresSupAnnuelles;
	}

	public Double getSommeHeuresSupAnnuellesH() {
		return sommeHeuresSupAnnuellesH;
	}

	public void setSommeHeuresSupAnnuellesH(Double sommeHeuresSupAnnuellesH) {
		this.sommeHeuresSupAnnuellesH = sommeHeuresSupAnnuellesH;
	}

	public Double getSommeHeuresSupAnnuellesF() {
		return sommeHeuresSupAnnuellesF;
	}

	public void setSommeHeuresSupAnnuellesF(Double sommeHeuresSupAnnuellesF) {
		this.sommeHeuresSupAnnuellesF = sommeHeuresSupAnnuellesF;
	}

	public Double getSommeAvantagesNonAssujettisAnnuels() {
		return sommeAvantagesNonAssujettisAnnuels;
	}

	public void setSommeAvantagesNonAssujettisAnnuels(
			Double sommeAvantagesNonAssujettisAnnuels) {
		this.sommeAvantagesNonAssujettisAnnuels = sommeAvantagesNonAssujettisAnnuels;
	}

	public Double getSommeAvantagesNonAssujettisAnnuelsH() {
		return sommeAvantagesNonAssujettisAnnuelsH;
	}

	public void setSommeAvantagesNonAssujettisAnnuelsH(
			Double sommeAvantagesNonAssujettisAnnuelsH) {
		this.sommeAvantagesNonAssujettisAnnuelsH = sommeAvantagesNonAssujettisAnnuelsH;
	}

	public Double getSommeAvantagesNonAssujettisAnnuelsF() {
		return sommeAvantagesNonAssujettisAnnuelsF;
	}

	public void setSommeAvantagesNonAssujettisAnnuelsF(
			Double sommeAvantagesNonAssujettisAnnuelsF) {
		this.sommeAvantagesNonAssujettisAnnuelsF = sommeAvantagesNonAssujettisAnnuelsF;
	}

	public List<NegociationSalarialeBean> getRowList() {
		return rowList;
	}

	public void setRowList(List<NegociationSalarialeBean> rowList) {
		this.rowList = rowList;
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

	public int getIdCSP() {
		return idCSP;
	}

	public void setIdCSP(int idCSP) {
		this.idCSP = idCSP;
	}

	public boolean isMoyenne() {
		return moyenne;
	}

	public void setMoyenne(boolean moyenne) {
		this.moyenne = moyenne;
	}

	public void setTwoYearAgo(Integer twoYearAgo) {
		this.twoYearAgo = twoYearAgo;
	}

	public void setOneYearAgo(Integer oneYearAgo) {
		this.oneYearAgo = oneYearAgo;
	}

	public Double getSommeSalairesDeBaseBrutsAnnuelsMoy() {
		return sommeSalairesDeBaseBrutsAnnuelsMoy;
	}

	public void setSommeSalairesDeBaseBrutsAnnuelsMoy(
			Double sommeSalairesDeBaseBrutsAnnuelsMoy) {
		this.sommeSalairesDeBaseBrutsAnnuelsMoy = sommeSalairesDeBaseBrutsAnnuelsMoy;
	}

	public Double getSommeRemuGlobalesBrutesAnnuellesMoy() {
		return sommeRemuGlobalesBrutesAnnuellesMoy;
	}

	public void setSommeRemuGlobalesBrutesAnnuellesMoy(
			Double sommeRemuGlobalesBrutesAnnuellesMoy) {
		this.sommeRemuGlobalesBrutesAnnuellesMoy = sommeRemuGlobalesBrutesAnnuellesMoy;
	}

	public Double getSommeHeuresSupAnnuellesMoy() {
		return sommeHeuresSupAnnuellesMoy;
	}

	public void setSommeHeuresSupAnnuellesMoy(Double sommeHeuresSupAnnuellesMoy) {
		this.sommeHeuresSupAnnuellesMoy = sommeHeuresSupAnnuellesMoy;
	}

	public Double getSommeAvantagesNonAssujettisAnnuelsMoy() {
		return sommeAvantagesNonAssujettisAnnuelsMoy;
	}

	public void setSommeAvantagesNonAssujettisAnnuelsMoy(
			Double sommeAvantagesNonAssujettisAnnuelsMoy) {
		this.sommeAvantagesNonAssujettisAnnuelsMoy = sommeAvantagesNonAssujettisAnnuelsMoy;
	}

	public ArrayList<SelectItem> getAnneesListItem() {
		return anneesListItem;
	}

	public void setAnneesListItem(ArrayList<SelectItem> anneesListItem) {
		this.anneesListItem = anneesListItem;
	}

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
	}

}
