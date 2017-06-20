package com.cci.gpec.web.backingBean.suiviEffectif;

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
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.commons.SuiviEffectifBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.icesoft.faces.context.effects.JavascriptContext;

public class SuiviEffectifBB {

	// ANNEE N

	private Integer effectif;
	private Double moyenneAge;
	private Double moyenneAnciennete;
	private Integer effectifH;
	private Double moyenneAgeH;
	private Double moyenneAncienneteH;
	private Integer effectifF;
	private Double moyenneAgeF;
	private Double moyenneAncienneteF;

	private Integer totalEffectif;
	private Integer totalEffectifH;
	private Integer totalEffectifF;
	private Double totalMoyenneAge;
	private Double totalMoyenneAgeH;
	private Double totalMoyenneAgeF;
	private Double moyenneMoyenneAge;
	private Double moyenneMoyenneAgeH;
	private Double moyenneMoyenneAgeF;
	private Double totalMoyenneAnciennete;
	private Double totalMoyenneAncienneteH;
	private Double totalMoyenneAncienneteF;
	private Double moyenneMoyenneAnciennete;
	private Double moyenneMoyenneAncienneteH;
	private Double moyenneMoyenneAncienneteF;

	// ANNEE N-1

	private Integer effectifN1;
	private Double moyenneAgeN1;
	private Double moyenneAncienneteN1;
	private Integer effectifHN1;
	private Double moyenneAgeHN1;
	private Double moyenneAncienneteHN1;
	private Integer effectifFN1;
	private Double moyenneAgeFN1;
	private Double moyenneAncienneteFN1;

	private Double totalEffectifN1;
	private Double totalEffectifHN1;
	private Double totalEffectifFN1;
	private Double moyenneMoyenneAgeN1;
	private Double moyenneMoyenneAgeHN1;
	private Double moyenneMoyenneAgeFN1;
	private Double moyenneMoyenneAncienneteN1;
	private Double moyenneMoyenneAncienneteHN1;
	private Double moyenneMoyenneAncienneteFN1;

	// ANNEE N-2

	private Integer effectifN2;
	private Double moyenneAgeN2;
	private Double moyenneAncienneteN2;
	private Integer effectifHN2;
	private Double moyenneAgeHN2;
	private Double moyenneAncienneteHN2;
	private Integer effectifFN2;
	private Double moyenneAgeFN2;
	private Double moyenneAncienneteFN2;

	private Double totalEffectifN2;
	private Double totalEffectifHN2;
	private Double totalEffectifFN2;
	private Double moyenneMoyenneAgeN2;
	private Double moyenneMoyenneAgeHN2;
	private Double moyenneMoyenneAgeFN2;
	private Double moyenneMoyenneAncienneteN2;
	private Double moyenneMoyenneAncienneteHN2;
	private Double moyenneMoyenneAncienneteFN2;

	List<String> cspList;
	List<String> metierList;
	private ArrayList<SelectItem> entreprisesListItem;
	private ArrayList<SelectItem> servicesListItem;
	private ArrayList<SelectItem> cspListItem;
	private ArrayList<SelectItem> anneeListItem;

	private List<SuiviEffectifBean> rowList = new ArrayList<SuiviEffectifBean>();

	private int idService;
	private int idEntreprise;
	private int idCSP;
	private int annee;

	private Integer twoYearAgo;
	private Integer oneYearAgo;
	private Integer curYear;

	DecimalFormat df = new DecimalFormat();

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public SuiviEffectifBB() throws Exception {
		init();
	}

	public void init() throws Exception {

		effectif = 0;
		effectifH = 0;
		effectifF = 0;
		moyenneAge = 0.0;
		moyenneAgeH = 0.0;
		moyenneAgeF = 0.0;
		moyenneAnciennete = 0.0;
		moyenneAncienneteH = 0.0;
		moyenneAncienneteF = 0.0;

		moyenneMoyenneAge = 0.0;
		moyenneMoyenneAgeH = 0.0;
		moyenneMoyenneAgeF = 0.0;
		moyenneMoyenneAnciennete = 0.0;
		moyenneMoyenneAncienneteH = 0.0;
		moyenneMoyenneAncienneteF = 0.0;

		annee = getCurYear();

		initTotal();

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		this.idCSP = 1;

		rowList = new ArrayList<SuiviEffectifBean>();
		cspList = new ArrayList<String>();
		metierList = new ArrayList<String>();

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

		anneeListItem = new ArrayList<SelectItem>();
		selectItem = new SelectItem();
		selectItem.setValue(getCurYear());
		selectItem.setLabel(getCurYear() + "");
		anneeListItem.add(selectItem);
		selectItem = new SelectItem();
		selectItem.setValue(getOneYearAgo());
		selectItem.setLabel(getOneYearAgo() + "");
		anneeListItem.add(selectItem);
		selectItem = new SelectItem();
		selectItem.setValue(getTwoYearAgo());
		selectItem.setLabel(getTwoYearAgo() + "");
		anneeListItem.add(selectItem);

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

		getTable(this.idEntreprise, this.idService, this.idCSP, getCurYear());
	}

	public void initTotal() {
		totalEffectif = 0;
		totalEffectifH = 0;
		totalEffectifF = 0;
		totalMoyenneAge = 0.0;
		totalMoyenneAgeH = 0.0;
		totalMoyenneAgeF = 0.0;
		totalMoyenneAnciennete = 0.0;
		totalMoyenneAncienneteH = 0.0;
		totalMoyenneAncienneteF = 0.0;
	}

	public boolean isAfficheTable() {
		return ((rowList.size() - 1) >= 1);
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
					if (event.getComponent().getId().equals("annee")) {
						annee = Integer
								.parseInt(event.getNewValue().toString());
					}
				}
			}
		}
		initTotal();
		rowList = new ArrayList<SuiviEffectifBean>();
		getTable(this.idEntreprise, this.idService, this.idCSP, annee);
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	public void getTable(Integer idEntreprise, Integer idService,
			Integer idCSP, Integer annee) throws Exception {
		List<SalarieBean> salariesList = new ArrayList<SalarieBean>();

		salariesList = getSalariesList(idEntreprise, idService, annee);

		cspList.clear();
		metierList.clear();

		StatutServiceImpl stat = new StatutServiceImpl();
		for (SalarieBean s : salariesList) {
			String csp = stat.getStatutBeanById(
					getLastParcours(s).getIdTypeStatutSelected()).getNom();
			if (!cspList.contains(csp)) {
				cspList.add(csp);
			}
		}

		MetierServiceImpl met = new MetierServiceImpl();
		for (SalarieBean s : salariesList) {
			String metier = met.getMetierBeanById(
					getLastParcours(s).getIdMetierSelected()).getNom();
			if (!metierList.contains(metier)) {
				metierList.add(metier);
			}
		}

		Collections.sort(cspList);
		Collections.sort(metierList);

		List<String> listTypeAffichage = new ArrayList<String>();
		if (idCSP == 1) {
			listTypeAffichage = cspList;
		} else {
			listTypeAffichage = metierList;
		}
		for (String libelle : listTypeAffichage) {
			effectif = 0;
			effectifH = 0;
			effectifF = 0;
			double sommeAge = 0;
			double sommeAgeH = 0;
			double sommeAgeF = 0;
			double sommeAnciennete = 0;
			double sommeAncienneteH = 0;
			double sommeAncienneteF = 0;
			for (SalarieBean s : salariesList) {
				if ((idCSP == 1 && stat
						.getStatutBeanById(
								getLastParcours(s).getIdTypeStatutSelected())
						.getNom().equals(libelle))
						|| (idCSP != 1 && met
								.getMetierBeanById(
										getLastParcours(s)
												.getIdMetierSelected())
								.getNom().equals((libelle)))) {
					int age = calculerAge(s.getDateNaissance());
					int anciennete = getAncienneteEntreprise(s);
					effectif++;
					sommeAge += age;
					sommeAnciennete += anciennete;
					if (s.getCivilite().equals("Monsieur")) {
						effectifH++;
						sommeAgeH += age;
						sommeAncienneteH += anciennete;
					} else {
						effectifF++;
						sommeAgeF += age;
						sommeAncienneteF += anciennete;
					}
				}
			}
			if (sommeAge != 0) {
				moyenneAge = sommeAge / effectif;
			} else {
				moyenneAge = 0.0;
			}
			if (sommeAgeH != 0) {
				moyenneAgeH = sommeAgeH / effectifH;
			} else {
				moyenneAgeH = 0.0;
			}
			if (sommeAgeF != 0) {
				moyenneAgeF = sommeAgeF / effectifF;
			} else {
				moyenneAgeF = 0.0;
			}
			if (sommeAnciennete != 0) {
				moyenneAnciennete = sommeAnciennete / effectif;
			} else {
				moyenneAnciennete = 0.0;
			}
			if (sommeAncienneteH != 0) {
				moyenneAncienneteH = sommeAncienneteH / effectifH;
			} else {
				moyenneAncienneteH = 0.0;
			}
			if (sommeAncienneteF != 0) {
				moyenneAncienneteF = sommeAncienneteF / effectifF;
			} else {
				moyenneAncienneteF = 0.0;
			}
			totalEffectif += effectif;
			totalEffectifH += effectifH;
			totalEffectifF += effectifF;
			totalMoyenneAge += moyenneAge;
			totalMoyenneAgeH += moyenneAgeH;
			totalMoyenneAgeF += moyenneAgeF;
			totalMoyenneAnciennete += moyenneAnciennete;
			totalMoyenneAncienneteH += moyenneAncienneteH;
			totalMoyenneAncienneteF += moyenneAncienneteF;

			SuiviEffectifBean suivi = new SuiviEffectifBean();
			suivi.setFooter(false);
			suivi.setLibelle(libelle);
			suivi.setEffectif(effectif);
			suivi.setEffectifH(effectifH);
			suivi.setEffectifF(effectifF);
			suivi.setMoyenneAge(df.format(moyenneAge));
			suivi.setMoyenneAgeH(df.format(moyenneAgeH));
			suivi.setMoyenneAgeF(df.format(moyenneAgeF));
			suivi.setMoyenneAnciennete(df.format(moyenneAnciennete));
			suivi.setMoyenneAncienneteH(df.format(moyenneAncienneteH));
			suivi.setMoyenneAncienneteF(df.format(moyenneAncienneteF));
			rowList.add(suivi);

		}

		int nbMoyenneAge = 0;
		int nbMoyenneAgeH = 0;
		int nbMoyenneAgeF = 0;
		int nbMoyenneAnciennete = 0;
		int nbMoyenneAncienneteH = 0;
		int nbMoyenneAncienneteF = 0;

		for (SuiviEffectifBean su : rowList) {
			if (!su.getMoyenneAge().equals("-")) {
				nbMoyenneAge++;
			}
			if (!su.getMoyenneAgeH().equals("-")) {
				nbMoyenneAgeH++;
			}
			if (!su.getMoyenneAgeF().equals("-")) {
				nbMoyenneAgeF++;
			}
			if (!su.getMoyenneAnciennete().equals("-")) {
				nbMoyenneAnciennete++;
			}
			if (!su.getMoyenneAncienneteH().equals("-")) {
				nbMoyenneAncienneteH++;
			}
			if (!su.getMoyenneAncienneteF().equals("-")) {
				nbMoyenneAncienneteF++;
			}
		}

		if (totalMoyenneAge != 0) {
			moyenneMoyenneAge = totalMoyenneAge / nbMoyenneAge;
		} else {
			moyenneMoyenneAge = 0.0;
		}
		if (totalMoyenneAgeH != 0) {
			moyenneMoyenneAgeH = totalMoyenneAgeH / nbMoyenneAgeH;
		} else {
			moyenneMoyenneAgeH = 0.0;
		}
		if (totalMoyenneAgeF != 0) {
			moyenneMoyenneAgeF = totalMoyenneAgeF / nbMoyenneAgeF;
		} else {
			moyenneMoyenneAgeF = 0.0;
		}
		if (totalMoyenneAnciennete != 0) {
			moyenneMoyenneAnciennete = totalMoyenneAnciennete
					/ nbMoyenneAnciennete;
		} else {
			moyenneMoyenneAnciennete = 0.0;
		}
		if (totalMoyenneAncienneteH != 0) {
			moyenneMoyenneAncienneteH = totalMoyenneAncienneteH
					/ nbMoyenneAncienneteH;
		} else {
			moyenneMoyenneAncienneteH = 0.0;
		}
		if (totalMoyenneAncienneteF != 0) {
			moyenneMoyenneAncienneteF = totalMoyenneAncienneteF
					/ nbMoyenneAncienneteF;
		} else {
			moyenneMoyenneAncienneteF = 0.0;
		}

		SuiviEffectifBean suivi = new SuiviEffectifBean();
		suivi.setFooter(true);
		suivi.setLibelle("TOTAL");
		suivi.setEffectif(totalEffectif);
		suivi.setEffectifH(totalEffectifH);
		suivi.setEffectifF(totalEffectifF);
		suivi.setMoyenneAge(df.format(moyenneMoyenneAge));
		suivi.setMoyenneAgeH(df.format(moyenneMoyenneAgeH));
		suivi.setMoyenneAgeF(df.format(moyenneMoyenneAgeF));
		suivi.setMoyenneAnciennete(df.format(moyenneMoyenneAnciennete));
		suivi.setMoyenneAncienneteH(df.format(moyenneMoyenneAncienneteH));
		suivi.setMoyenneAncienneteF(df.format(moyenneMoyenneAncienneteF));

		rowList.add(suivi);

	}

	public String download(ActionEvent e) {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[11];
		entete[0] = annee + "";
		if (idCSP == 1) {
			entete[1] = "Répartition par CSP";
		} else {
			entete[1] = "Répartition par métier";
		}
		entete[2] = "Effectif";
		entete[3] = "Moyenne d'âge";
		entete[4] = "Moyenne d'ancienneté";
		entete[5] = "Parité hommes/femmes";
		entete[6] = "Effectif";
		entete[7] = "Moyenne d'âge";
		entete[8] = "Moyenne d'ancienneté";
		entete[9] = "H";
		entete[10] = "F";

		ServiceImpl servServ = new ServiceImpl();

		try {
			eContext.getSessionMap().put("datatable", this.rowList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Gestion effectif");
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

	public List<SalarieBean> getSalariesList(int idEntreprise, int idService,
			int annee) throws Exception {
		List<SalarieBean> list = new ArrayList<SalarieBean>();
		List<SalarieBean> listTemp = new ArrayList<SalarieBean>();

		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

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

				if ((embauche.before(today) || embauche.equals(today))
						&& getLastParcours(s) != null) {

					if (getLastParcours(s).getFinFonction() != null) {
						Calendar fin = new GregorianCalendar();
						fin.setTime(getLastParcours(s).getFinFonction());
						if (fin.getTime().after(today.getTime())
								|| fin.getTime().equals(today.getTime())) {
							if (idService != -1) {
								if (s.getIdServiceSelected() == idService) {
									if (getLastParcours(s)
											.getIdTypeContratSelected() != 3
											&& getLastParcours(s)
													.getIdTypeContratSelected() != 7) {
										list.add(s);
									}
								}
							} else {
								if (getLastParcours(s)
										.getIdTypeContratSelected() != 3
										&& getLastParcours(s)
												.getIdTypeContratSelected() != 7) {
									list.add(s);
								}
							}
						}
					} else {
						if (idService != -1) {
							if (s.getIdServiceSelected() == idService) {
								if (getLastParcours(s)
										.getIdTypeContratSelected() != 3
										&& getLastParcours(s)
												.getIdTypeContratSelected() != 7) {
									list.add(s);
								}
							}
						} else {
							if (getLastParcours(s).getIdTypeContratSelected() != 3
									&& getLastParcours(s)
											.getIdTypeContratSelected() != 7) {
								list.add(s);
							}
						}
					}
				}
			}
		}

		return list;
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

	public int calculerAge(Date d) {
		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(d);
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
		dateOfBirth.add(Calendar.YEAR, age);
		if (today.before(dateOfBirth)) {
			age--;
		}
		return age;
	}

	private Integer getAncienneteEntreprise(SalarieBean salarie) {
		ParcoursBean parcourDeb = getFirstParcours(salarie);
		Calendar dateDebut = new GregorianCalendar();
		Calendar dateFin = new GregorianCalendar();
		if (parcourDeb != null) {
			dateDebut.setTime(parcourDeb.getDebutFonction());
		}
		ParcoursBean parcourFin = getLastParcours(salarie);
		if (parcourFin != null) {
			if (parcourFin.getFinFonction() != null
					&& (parcourFin.getFinFonction().before(new Date()) || parcourFin
							.getFinFonction().equals(new Date()))) {
				dateFin.setTime(parcourFin.getFinFonction());
			} else {
				dateFin.setTime(new Date());
			}
		}
		return dateFin.get(Calendar.YEAR) - dateDebut.get(Calendar.YEAR);
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
			if (pb == null)
				pb = parcour;
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

	public List<String> getCspList() {
		return cspList;
	}

	public void setCspList(List<String> cspList) {
		this.cspList = cspList;
	}

	public List<String> getMetierList() {
		return metierList;
	}

	public void setMetierList(List<String> metierList) {
		this.metierList = metierList;
	}

	public List<SelectItem> getEntrepriseListItem() {
		return entreprisesListItem;
	}

	public void setEntrepriseListItem(ArrayList<SelectItem> entreprisesListItem) {
		this.entreprisesListItem = entreprisesListItem;
	}

	public List<SuiviEffectifBean> getRowList() {
		return rowList;
	}

	public void setRowList(List<SuiviEffectifBean> rowList) {
		this.rowList = rowList;
	}

	public Integer getEffectif() {
		return effectif;
	}

	public void setEffectif(Integer effectif) {
		this.effectif = effectif;
	}

	public Double getMoyenneAge() {
		return moyenneAge;
	}

	public void setMoyenneAge(Double moyenneAge) {
		this.moyenneAge = moyenneAge;
	}

	public Double getMoyenneAnciennete() {
		return moyenneAnciennete;
	}

	public void setMoyenneAnciennete(Double moyenneAnciennete) {
		this.moyenneAnciennete = moyenneAnciennete;
	}

	public Integer getEffectifH() {
		return effectifH;
	}

	public void setEffectifH(Integer effectifH) {
		this.effectifH = effectifH;
	}

	public Double getMoyenneAgeH() {
		return moyenneAgeH;
	}

	public void setMoyenneAgeH(Double moyenneAgeH) {
		this.moyenneAgeH = moyenneAgeH;
	}

	public Double getMoyenneAncienneteH() {
		return moyenneAncienneteH;
	}

	public void setMoyenneAncienneteH(Double moyenneAncienneteH) {
		this.moyenneAncienneteH = moyenneAncienneteH;
	}

	public Integer getEffectifF() {
		return effectifF;
	}

	public void setEffectifF(Integer effectifF) {
		this.effectifF = effectifF;
	}

	public Double getMoyenneAgeF() {
		return moyenneAgeF;
	}

	public void setMoyenneAgeF(Double moyenneAgeF) {
		this.moyenneAgeF = moyenneAgeF;
	}

	public Double getMoyenneAncienneteF() {
		return moyenneAncienneteF;
	}

	public void setMoyenneAncienneteF(Double moyenneAncienneteF) {
		this.moyenneAncienneteF = moyenneAncienneteF;
	}

	public Integer getTotalEffectifH() {
		return totalEffectifH;
	}

	public void setTotalEffectifH(Integer totalEffectifH) {
		this.totalEffectifH = totalEffectifH;
	}

	public Integer getTotalEffectifF() {
		return totalEffectifF;
	}

	public void setTotalEffectifF(Integer totalEffectifF) {
		this.totalEffectifF = totalEffectifF;
	}

	public Double getMoyenneMoyenneAgeH() {
		return moyenneMoyenneAgeH;
	}

	public void setMoyenneMoyenneAgeH(Double moyenneMoyenneAgeH) {
		this.moyenneMoyenneAgeH = moyenneMoyenneAgeH;
	}

	public Double getMoyenneMoyenneAgeF() {
		return moyenneMoyenneAgeF;
	}

	public void setMoyenneMoyenneAgeF(Double moyenneMoyenneAgeF) {
		this.moyenneMoyenneAgeF = moyenneMoyenneAgeF;
	}

	public Double getMoyenneMoyenneAncienneteH() {
		return moyenneMoyenneAncienneteH;
	}

	public void setMoyenneMoyenneAncienneteH(Double moyenneMoyenneAncienneteH) {
		this.moyenneMoyenneAncienneteH = moyenneMoyenneAncienneteH;
	}

	public Double getMoyenneMoyenneAncienneteF() {
		return moyenneMoyenneAncienneteF;
	}

	public void setMoyenneMoyenneAncienneteF(Double moyenneMoyenneAncienneteF) {
		this.moyenneMoyenneAncienneteF = moyenneMoyenneAncienneteF;
	}

	public Integer getTotalEffectif() {
		return totalEffectif;
	}

	public void setTotalEffectif(Integer totalEffectif) {
		this.totalEffectif = totalEffectif;
	}

	public Double getMoyenneMoyenneAge() {
		return moyenneMoyenneAge;
	}

	public void setMoyenneMoyenneAge(Double moyenneMoyenneAge) {
		this.moyenneMoyenneAge = moyenneMoyenneAge;
	}

	public Double getMoyenneMoyenneAnciennete() {
		return moyenneMoyenneAnciennete;
	}

	public void setMoyenneMoyenneAnciennete(Double moyenneMoyenneAnciennete) {
		this.moyenneMoyenneAnciennete = moyenneMoyenneAnciennete;
	}

	public Integer getEffectifN1() {
		return effectifN1;
	}

	public void setEffectifN1(Integer effectifN1) {
		this.effectifN1 = effectifN1;
	}

	public Double getMoyenneAgeN1() {
		return moyenneAgeN1;
	}

	public void setMoyenneAgeN1(Double moyenneAgeN1) {
		this.moyenneAgeN1 = moyenneAgeN1;
	}

	public Double getMoyenneAncienneteN1() {
		return moyenneAncienneteN1;
	}

	public void setMoyenneAncienneteN1(Double moyenneAncienneteN1) {
		this.moyenneAncienneteN1 = moyenneAncienneteN1;
	}

	public Integer getEffectifHN1() {
		return effectifHN1;
	}

	public void setEffectifHN1(Integer effectifHN1) {
		this.effectifHN1 = effectifHN1;
	}

	public Double getMoyenneAgeHN1() {
		return moyenneAgeHN1;
	}

	public void setMoyenneAgeHN1(Double moyenneAgeHN1) {
		this.moyenneAgeHN1 = moyenneAgeHN1;
	}

	public Double getMoyenneAncienneteHN1() {
		return moyenneAncienneteHN1;
	}

	public void setMoyenneAncienneteHN1(Double moyenneAncienneteHN1) {
		this.moyenneAncienneteHN1 = moyenneAncienneteHN1;
	}

	public Integer getEffectifFN1() {
		return effectifFN1;
	}

	public void setEffectifFN1(Integer effectifFN1) {
		this.effectifFN1 = effectifFN1;
	}

	public Double getMoyenneAgeFN1() {
		return moyenneAgeFN1;
	}

	public void setMoyenneAgeFN1(Double moyenneAgeFN1) {
		this.moyenneAgeFN1 = moyenneAgeFN1;
	}

	public Double getMoyenneAncienneteFN1() {
		return moyenneAncienneteFN1;
	}

	public void setMoyenneAncienneteFN1(Double moyenneAncienneteFN1) {
		this.moyenneAncienneteFN1 = moyenneAncienneteFN1;
	}

	public Double getTotalEffectifHN1() {
		return totalEffectifHN1;
	}

	public void setTotalEffectifHN1(Double totalEffectifHN1) {
		this.totalEffectifHN1 = totalEffectifHN1;
	}

	public Double getTotalEffectifFN1() {
		return totalEffectifFN1;
	}

	public void setTotalEffectifFN1(Double totalEffectifFN1) {
		this.totalEffectifFN1 = totalEffectifFN1;
	}

	public Double getMoyenneMoyenneAgeHN1() {
		return moyenneMoyenneAgeHN1;
	}

	public void setMoyenneMoyenneAgeHN1(Double moyenneMoyenneAgeHN1) {
		this.moyenneMoyenneAgeHN1 = moyenneMoyenneAgeHN1;
	}

	public Double getMoyenneMoyenneAgeFN1() {
		return moyenneMoyenneAgeFN1;
	}

	public void setMoyenneMoyenneAgeFN1(Double moyenneMoyenneAgeFN1) {
		this.moyenneMoyenneAgeFN1 = moyenneMoyenneAgeFN1;
	}

	public Double getMoyenneMoyenneAncienneteHN1() {
		return moyenneMoyenneAncienneteHN1;
	}

	public void setMoyenneMoyenneAncienneteHN1(
			Double moyenneMoyenneAncienneteHN1) {
		this.moyenneMoyenneAncienneteHN1 = moyenneMoyenneAncienneteHN1;
	}

	public Double getMoyenneMoyenneAncienneteFN1() {
		return moyenneMoyenneAncienneteFN1;
	}

	public void setMoyenneMoyenneAncienneteFN1(
			Double moyenneMoyenneAncienneteFN1) {
		this.moyenneMoyenneAncienneteFN1 = moyenneMoyenneAncienneteFN1;
	}

	public Double getTotalEffectifN1() {
		return totalEffectifN1;
	}

	public void setTotalEffectifN1(Double totalEffectifN1) {
		this.totalEffectifN1 = totalEffectifN1;
	}

	public Double getMoyenneMoyenneAgeN1() {
		return moyenneMoyenneAgeN1;
	}

	public void setMoyenneMoyenneAgeN1(Double moyenneMoyenneAgeN1) {
		this.moyenneMoyenneAgeN1 = moyenneMoyenneAgeN1;
	}

	public Double getMoyenneMoyenneAncienneteN1() {
		return moyenneMoyenneAncienneteN1;
	}

	public void setMoyenneMoyenneAncienneteN1(Double moyenneMoyenneAncienneteN1) {
		this.moyenneMoyenneAncienneteN1 = moyenneMoyenneAncienneteN1;
	}

	public Integer getEffectifN2() {
		return effectifN2;
	}

	public void setEffectifN2(Integer effectifN2) {
		this.effectifN2 = effectifN2;
	}

	public Double getMoyenneAgeN2() {
		return moyenneAgeN2;
	}

	public void setMoyenneAgeN2(Double moyenneAgeN2) {
		this.moyenneAgeN2 = moyenneAgeN2;
	}

	public Double getMoyenneAncienneteN2() {
		return moyenneAncienneteN2;
	}

	public void setMoyenneAncienneteN2(Double moyenneAncienneteN2) {
		this.moyenneAncienneteN2 = moyenneAncienneteN2;
	}

	public Integer getEffectifHN2() {
		return effectifHN2;
	}

	public void setEffectifHN2(Integer effectifHN2) {
		this.effectifHN2 = effectifHN2;
	}

	public Double getMoyenneAgeHN2() {
		return moyenneAgeHN2;
	}

	public void setMoyenneAgeHN2(Double moyenneAgeHN2) {
		this.moyenneAgeHN2 = moyenneAgeHN2;
	}

	public Double getMoyenneAncienneteHN2() {
		return moyenneAncienneteHN2;
	}

	public void setMoyenneAncienneteHN2(Double moyenneAncienneteHN2) {
		this.moyenneAncienneteHN2 = moyenneAncienneteHN2;
	}

	public Integer getEffectifFN2() {
		return effectifFN2;
	}

	public void setEffectifFN2(Integer effectifFN2) {
		this.effectifFN2 = effectifFN2;
	}

	public Double getMoyenneAgeFN2() {
		return moyenneAgeFN2;
	}

	public void setMoyenneAgeFN2(Double moyenneAgeFN2) {
		this.moyenneAgeFN2 = moyenneAgeFN2;
	}

	public Double getMoyenneAncienneteFN2() {
		return moyenneAncienneteFN2;
	}

	public void setMoyenneAncienneteFN2(Double moyenneAncienneteFN2) {
		this.moyenneAncienneteFN2 = moyenneAncienneteFN2;
	}

	public Double getTotalEffectifHN2() {
		return totalEffectifHN2;
	}

	public void setTotalEffectifHN2(Double totalEffectifHN2) {
		this.totalEffectifHN2 = totalEffectifHN2;
	}

	public Double getTotalEffectifFN2() {
		return totalEffectifFN2;
	}

	public void setTotalEffectifFN2(Double totalEffectifFN2) {
		this.totalEffectifFN2 = totalEffectifFN2;
	}

	public Double getMoyenneMoyenneAgeHN2() {
		return moyenneMoyenneAgeHN2;
	}

	public void setMoyenneMoyenneAgeHN2(Double moyenneMoyenneAgeHN2) {
		this.moyenneMoyenneAgeHN2 = moyenneMoyenneAgeHN2;
	}

	public Double getMoyenneMoyenneAgeFN2() {
		return moyenneMoyenneAgeFN2;
	}

	public void setMoyenneMoyenneAgeFN2(Double moyenneMoyenneAgeFN2) {
		this.moyenneMoyenneAgeFN2 = moyenneMoyenneAgeFN2;
	}

	public Double getMoyenneMoyenneAncienneteHN2() {
		return moyenneMoyenneAncienneteHN2;
	}

	public void setMoyenneMoyenneAncienneteHN2(
			Double moyenneMoyenneAncienneteHN2) {
		this.moyenneMoyenneAncienneteHN2 = moyenneMoyenneAncienneteHN2;
	}

	public Double getMoyenneMoyenneAncienneteFN2() {
		return moyenneMoyenneAncienneteFN2;
	}

	public void setMoyenneMoyenneAncienneteFN2(
			Double moyenneMoyenneAncienneteFN2) {
		this.moyenneMoyenneAncienneteFN2 = moyenneMoyenneAncienneteFN2;
	}

	public Double getTotalEffectifN2() {
		return totalEffectifN2;
	}

	public void setTotalEffectifN2(Double totalEffectifN2) {
		this.totalEffectifN2 = totalEffectifN2;
	}

	public Double getMoyenneMoyenneAgeN2() {
		return moyenneMoyenneAgeN2;
	}

	public void setMoyenneMoyenneAgeN2(Double moyenneMoyenneAgeN2) {
		this.moyenneMoyenneAgeN2 = moyenneMoyenneAgeN2;
	}

	public Double getMoyenneMoyenneAncienneteN2() {
		return moyenneMoyenneAncienneteN2;
	}

	public void setMoyenneMoyenneAncienneteN2(Double moyenneMoyenneAncienneteN2) {
		this.moyenneMoyenneAncienneteN2 = moyenneMoyenneAncienneteN2;
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

	public void setTwoYearAgo(Integer twoYearAgo) {
		this.twoYearAgo = twoYearAgo;
	}

	public void setOneYearAgo(Integer oneYearAgo) {
		this.oneYearAgo = oneYearAgo;
	}

	public void setCurYear(Integer curYear) {
		this.curYear = curYear;
	}

	public DecimalFormat getDf() {
		return df;
	}

	public void setDf(DecimalFormat df) {
		this.df = df;
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
		return cspListItem;
	}

	public void setCspListItem(ArrayList<SelectItem> cspListItem) {
		this.cspListItem = cspListItem;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public ArrayList<SelectItem> getAnneeListItem() {
		return anneeListItem;
	}

	public void setAnneeListItem(ArrayList<SelectItem> anneeListItem) {
		this.anneeListItem = anneeListItem;
	}

}
