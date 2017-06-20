package com.cci.gpec.metier.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.AdminBean;
import com.cci.gpec.commons.ContratTravailBean;
import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.EvenementBean;
import com.cci.gpec.commons.FicheDePosteBean;
import com.cci.gpec.commons.FicheMetierBean;
import com.cci.gpec.commons.FicheMetierEntrepriseBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.LienRemunerationRevenuBean;
import com.cci.gpec.commons.MappingRepriseBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.ObjectifsEntretienBean;
import com.cci.gpec.commons.ParamsGenerauxBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.PersonneAChargeBean;
import com.cci.gpec.commons.PiecesJustificativesBean;
import com.cci.gpec.commons.RemunerationBean;
import com.cci.gpec.commons.RevenuComplementaireBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.commons.TransmissionBean;
import com.cci.gpec.commons.TypeAbsenceBean;
import com.cci.gpec.commons.TypeHabilitationBean;
import com.cci.gpec.commons.UserBean;
import com.cci.gpec.db.connection.HibernateUtil;

public class ImportExportBDD {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportExportBDD.class);

	private static Integer idGroupe = 0;

	public static int importe(String nomGroupe, String filePath, boolean essai)
			throws Exception {
		System.out.println("Lancement de l'import de la base de données ...");

		Map<Integer, Integer> entretien = new HashMap<Integer, Integer>();
		Map<Integer, Integer> salarie = new HashMap<Integer, Integer>();
		Map<Integer, Integer> absence = new HashMap<Integer, Integer>();
		Map<Integer, Integer> ficheMetier = new HashMap<Integer, Integer>();
		Map<Integer, Integer> entreprise = new HashMap<Integer, Integer>();
		Map<Integer, Integer> remuneration = new HashMap<Integer, Integer>();
		Map<Integer, Integer> revenuComplementaire = new HashMap<Integer, Integer>();
		Map<Integer, Integer> metier = new HashMap<Integer, Integer>();
		Map<Integer, Integer> service = new HashMap<Integer, Integer>();
		Map<Integer, Integer> typeAbsence = new HashMap<Integer, Integer>();
		Map<Integer, Integer> typeHabilitation = new HashMap<Integer, Integer>();
		Map<Integer, Integer> lienHierarchique = new HashMap<Integer, Integer>();

		GroupeServiceImpl gr = new GroupeServiceImpl();
		if (gr.getMaxId() != null) {
			idGroupe = gr.getMaxId() + 1;
		} else {
			idGroupe = 1;
		}

		File file = new File(filePath);

		if (file == null || file.getAbsolutePath().equals("") || !file.exists()
				|| !file.canRead() || !file.isFile()) {
			System.out.println("***************************");
			System.out.println("Erreur : fichier introuvable ou illisible : "
					+ file.getAbsolutePath());
			System.out.println("***************************");

		}

		Session sessionf = null;
		Transaction tx = null;
		try {
			sessionf = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = sessionf.beginTransaction();

			/** **Creation des tables "fixes" **** */

			GroupeBean g = new GroupeBean();
			g.setId(idGroupe);
			g.setNom(nomGroupe);
			g.setDeleted(false);
			if (essai) {
				GregorianCalendar finPeriodeEssai = new GregorianCalendar();
				finPeriodeEssai.setTime(new Date());
				finPeriodeEssai.add(2, Calendar.MONTH);
				g.setFinPeriodeEssai(finPeriodeEssai.getTime());
			}
			gr.save(g);
			idGroupe = g.getId();

			MappingRepriseServiceImpl mappingService = new MappingRepriseServiceImpl();

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			InputStream ips = new FileInputStream(file.getAbsolutePath());
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			String ligneTemp = "";
			//liste des entreprises pour lesquelles les parametres generaux ont ete importes -> evite les doublons
			List<Integer> paramsGenerauxEnt = new ArrayList<Integer>();
			while ((ligne = br.readLine()) != null) {
				if (!ligne.endsWith("/eol/ ")) {
					if (ligneTemp.equals("")) {
						ligneTemp = ligne;
					} else {
						ligneTemp = ligneTemp + "\n" + ligne;
					}
				} else {
					if (ligneTemp.equals("")) {
						ligneTemp = ligne;
					} else {
						ligneTemp = ligneTemp + "\n" + ligne;
					}
					ligneTemp = ligneTemp.replace("/eol/ ", "");
					ligneTemp = ligneTemp.replace("|", "/s/");
					ligneTemp = ligneTemp.replace("\\/", "/");
					ligneTemp = ligneTemp.replace("\\/rc\\/", "\n");
					ligneTemp = ligneTemp.replace("/rc/", "\n");

					// System.out.println("lign : " + ligneTemp);
					String[] tab = ligneTemp.split("/s/", -1);

					ligneTemp = "";

					AdminServiceImpl admServ = new AdminServiceImpl();
					if (tab[0].equals("admin")) {
						AdminBean adm = new AdminBean();
						adm.setId(0);
						adm.setMigration(Integer.valueOf(tab[2]) == 0 ? false
								: true);
						adm.setLogo(tab[3]);
						admServ.save(adm, idGroupe);
					}

					AbsenceServiceImpl abs = new AbsenceServiceImpl();
					if (tab[0].equals("absence")) {
						AbsenceBean a = new AbsenceBean();
						a.setId(0);
						a.setDebutAbsence(formatter.parse(tab[2]));
						a.setFinAbsence(formatter.parse(tab[3]));
						if (!tab[4].equals("null")) {
							a.setNombreJourOuvre(Double.valueOf(tab[4]));
						} else {
							a.setNombreJourOuvre(null);
						}
						if (!tab[5].equals("null")) {
							a.setJustificatif(tab[5]);
						} else {
							a.setJustificatif(null);
						}
						if (!tab[6].equals("null")) {
							if (Integer.valueOf(tab[6]) > 18) {
								a.setIdTypeAbsenceSelected(typeAbsence
										.get(Integer.valueOf(tab[6])));
							} else {
								a.setIdTypeAbsenceSelected(Integer
										.valueOf(tab[6]));
							}
						} else {
							a.setIdTypeAbsenceSelected(null);
						}
						if (!tab[7].equals("null")) {
							a.setIdSalarie(salarie.get(Integer.valueOf(tab[7])));
						} else {
							a.setIdSalarie(null);
						}
						abs.save(a);
						absence.put(Integer.valueOf(tab[1]), a.getId());
						mappingService.saveOrUppdate(new MappingRepriseBean(
								"absence", Integer.valueOf(tab[1]), a.getId(),
								idGroupe));
					}

					AccidentServiceImpl acc = new AccidentServiceImpl();
					if (tab[0].equals("accident")) {
						AccidentBean a = new AccidentBean();
						a.setId(0);
						if (!tab[2].equals("null")) {
							a.setInitial(Integer.valueOf(tab[2]) == 0 ? false
									: true);
						}
						if (!tab[3].equals("null")) {
							a.setAggravation(Integer.valueOf(tab[3]) == 0 ? false
									: true);
						}
						if (!tab[4].equals("null")) {
							a.setDateAccident(formatter.parse(tab[4]));
						} else {
							a.setDateAccident(null);
						}
						if (!tab[5].equals("null")) {
							a.setDateRechute(formatter.parse(tab[5]));
						} else {
							a.setDateRechute(null);
						}
						if (!tab[6].equals("null")) {
							a.setNombreJourArret(Integer.valueOf(tab[6]));
						} else {
							a.setNombreJourArret(null);
						}
						if (!tab[7].equals("null")) {
							a.setNombreJourArretRechute(Integer.valueOf(tab[7]));
						} else {
							a.setNombreJourArretRechute(null);
						}
						if (!tab[8].equals("null")) {
							a.setJustificatif(tab[8]);
						} else {
							a.setJustificatif(null);
						}
						a.setCommentaire(tab[9]);
						if (!tab[10].equals("null")) {
							a.setIdTypeLesionBeanSelected(Integer
									.valueOf(tab[10]));
						} else {
							a.setIdTypeLesionBeanSelected(null);
						}
						if (!tab[11].equals("null")) {
							a.setIdTypeLesionRechuteBeanSelected(Integer
									.valueOf(tab[11]));
						} else {
							a.setIdTypeLesionRechuteBeanSelected(null);
						}
						if (!tab[12].equals("null")) {
							a.setIdTypeCauseAccidentBeanSelected(Integer
									.valueOf(tab[12]));
						} else {
							a.setIdTypeCauseAccidentBeanSelected(null);
						}
						if (!tab[13].equals("null")) {
							a.setIdTypeAccidentBeanSelected(Integer
									.valueOf(tab[13]));
						} else {
							a.setIdTypeAccidentBeanSelected(null);
						}
						if (!tab[14].equals("null")) {
							a.setIdSalarie(salarie.get(Integer.valueOf(tab[14])));
						} else {
							a.setIdSalarie(null);
						}
						if (!tab[15].equals("null")) {
							a.setIdAbsence(absence.get(Integer.valueOf(tab[15])));
						} else {
							a.setIdAbsence(-1);
						}
						acc.save(a);
					}

					ContratTravailServiceImpl ct = new ContratTravailServiceImpl();
					if (tab[0].equals("contrat_travail")) {
						if (!tab[9].equals("null") && !tab[9].equals("0")) {
							ContratTravailBean c = new ContratTravailBean();
							c.setId(-1);
							if (!tab[2].equals("null")) {
								c.setDebutContrat(formatter.parse(tab[2]));
							} else {
								c.setDebutContrat(null);
							}
							if (!tab[3].equals("null")) {
								c.setFinContrat(formatter.parse(tab[3]));
							} else {
								c.setFinContrat(null);
							}
							c.setRenouvellementCDD(Integer.valueOf(tab[4]) == 0 ? false
									: true);
							if (!tab[5].equals("null")) {
								c.setJustificatif(tab[5]);
							} else {
								c.setJustificatif(null);
							}
							c.setNomTypeContrat(tab[6]);
							if (!tab[7].equals("null")) {
								c.setIdMetierSelected(metier.get(Integer
										.valueOf(tab[7])));
							} else {
								c.setIdMetierSelected(null);
							}
							if (!tab[8].equals("null")) {
								c.setIdTypeContratSelected(Integer.valueOf(tab[8]));
							} else {
								c.setIdTypeContratSelected(null);
							}
							if (!tab[9].equals("null")) {
								c.setIdSalarie(salarie.get(Integer.valueOf(tab[9])));
							} else {
								c.setIdSalarie(null);
							}
							if (!tab[10].equals("null")) {
								c.setIdMotifRuptureContrat(Integer.valueOf(tab[10]));
							} else {
								c.setIdMotifRuptureContrat(null);
							}
							ct.save(c);
						}

					}

					EntrepriseServiceImpl ent = new EntrepriseServiceImpl();
					if (tab[0].equals("entreprise")) {
						EntrepriseBean e = new EntrepriseBean();
						e.setId(0);
						e.setNom(tab[2]);
						e.setNumSiret(Long.valueOf(tab[3]));
						e.setCodeApe(tab[4]);
						if (!tab[5].equals("null")) {
							e.setDateCreation(formatter.parse(tab[5]));
						} else {
							e.setDateCreation(null);
						}
						if (!tab[6].equals("null")) {
							e.setCciRattachement(tab[6]);
						}
						e.setIdGroupe(idGroupe);
						if (!tab[7].equals("null")) {
							e.setJustificatif(tab[7]);
						} else {
							e.setJustificatif(null);
						}
						if (!tab[8].equals("null")) {
							e.setSuiviFormations(Integer.valueOf(tab[8]) == 0 ? false
									: true);
						} else {
							e.setSuiviFormations(false);
						}
						if (!tab[9].equals("null")) {
							e.setSuiviAccidents(Integer.valueOf(tab[9]) == 0 ? false
									: true);
						} else {
							e.setSuiviAccidents(false);
						}
						if (!tab[10].equals("null")) {
							e.setSuiviAbsences(Integer.valueOf(tab[10]) == 0 ? false
									: true);
						} else {
							e.setSuiviAbsences(false);
						}
						if (!tab[11].equals("null")) {
							e.setSuiviCompetences(Integer.valueOf(tab[11]) == 0 ? false
									: true);
						} else {
							e.setSuiviCompetences(false);
						}
						if (!tab[12].equals("null")) {
							e.setSuiviDIF(Integer.valueOf(tab[12]) == 0 ? false
									: true);
						} else {
							e.setSuiviDIF(false);
						}
						if (!tab[13].equals("null")) {
							e.setDIFMax(Integer.valueOf(tab[13]));
						} else {
							e.setDIFMax(null);
						}
						ent.save(e, idGroupe);
						entreprise.put(Integer.valueOf(tab[1]), e.getId());
						mappingService.saveOrUppdate(new MappingRepriseBean(
								"entreprise", Integer.valueOf(tab[1]), e
										.getId(), idGroupe));
					}

					EntretienServiceImpl entr = new EntretienServiceImpl();
					if (tab[0].equals("entretien")) {
						EntretienBean e = new EntretienBean();
						e.setId(-1);
						e.setPrincipaleConclusion(tab[2]);
						e.setSouhait(tab[3]);
						e.setCompetence(tab[4]);
						if (!tab[5].equals("null")) {
							e.setDateEntretien(formatter.parse(tab[5]));
						} else {
							e.setDateEntretien(null);
						}
						e.setCrSigne(tab[6]);
						e.setObjMoyens(!tab[7].equals("null") ? tab[7] : "");
						e.setServiceManager(!tab[8].equals("null") ? tab[8]
								: "");
						e.setFormationNMoinsUn(!tab[9].equals("null") ? tab[9]
								: "");
						e.setCommentaireFormation(!tab[10].equals("null") ? tab[10]
								: "");
						e.setModifProfil(!tab[11].equals("null") ? tab[11] : "");
						e.setBilanDessous(!tab[12].equals("null") ? tab[12]
								: "");
						e.setBilanEgal(!tab[13].equals("null") ? tab[13] : "");
						e.setObservations(!tab[14].equals("null") ? tab[14]
								: "");
						e.setBilanDessus(!tab[15].equals("null") ? tab[15] : "");
						e.setFormations(!tab[16].equals("null") ? tab[16] : "");
						e.setFormations2(!tab[17].equals("null") ? tab[17] : "");
						e.setFormations3(!tab[18].equals("null") ? tab[18] : "");
						e.setFormations4(!tab[19].equals("null") ? tab[19] : "");
						e.setFormations5(!tab[20].equals("null") ? tab[20] : "");
						e.setCommentaireBilan(!tab[21].equals("null") ? tab[21]
								: "");
						e.setEvolutionPerso(!tab[23].equals("null") ? tab[23]
								: "");
						e.setSynthese(!tab[24].equals("null") ? tab[24] : "");
						e.setNomManager(!tab[25].equals("null") ? tab[25] : "");

						if (!tab[28].equals("null")) {
							e.setDomainesFormation(Integer.valueOf(tab[28]));
						} else {
							e.setDomainesFormation(-1);
						}
						if (!tab[29].equals("null")) {
							e.setDomainesFormation2(Integer.valueOf(tab[29]));
						} else {
							e.setDomainesFormation2(-1);
						}
						if (!tab[30].equals("null")) {
							e.setDomainesFormation3(Integer.valueOf(tab[30]));
						} else {
							e.setDomainesFormation3(-1);
						}
						if (!tab[31].equals("null")) {
							e.setDomainesFormation4(Integer.valueOf(tab[31]));
						} else {
							e.setDomainesFormation4(-1);
						}
						if (!tab[32].equals("null")) {
							e.setDomainesFormation5(Integer.valueOf(tab[32]));
						} else {
							e.setDomainesFormation5(-1);
						}
						if (!tab[33].equals("null")) {
							e.setJustificatif(tab[33]);
						} else {
							e.setJustificatif(null);
						}
						if (!tab[34].equals("null")) {
							e.setIdSalarie(salarie.get(Integer.valueOf(tab[34])));
						} else {
							e.setIdSalarie(null);
						}
						e.setEvaluationGlobale(!tab[35].equals("null") ? tab[35]
								: "");
						if (!tab[36].equals("null")) {
							e.setAnneeReference(Integer.valueOf(tab[36]));
						} else {
							e.setAnneeReference(null);
						}
						entr.save(e);
						entretien.put(Integer.valueOf(tab[1]), e.getId());
						mappingService.saveOrUppdate(new MappingRepriseBean(
								"entretien", Integer.valueOf(tab[1]),
								e.getId(), idGroupe));
					}

					EvenementServiceImpl ev = new EvenementServiceImpl();
					if (tab[0].equals("evenement")) {
						EvenementBean e = new EvenementBean();
						e.setId(-1);
						if (!tab[2].equals("null")) {
							e.setDateEvenement(formatter.parse(tab[2]));
						} else {
							e.setDateEvenement(null);
						}
						e.setCommentaire(!tab[3].equals("null") ? tab[3] : "");
						e.setInterlocuteur(!tab[4].equals("null") ? tab[4] : "");
						e.setNature(!tab[5].equals("null") ? tab[5] : "");
						e.setDecision(!tab[6].equals("null") ? tab[6] : "");
						if (!tab[7].equals("null")) {
							e.setJustificatif(tab[7]);
						} else {
							e.setJustificatif(null);
						}
						if (!tab[8].equals("null")) {
							e.setIdSalarie(salarie.get(Integer.valueOf(tab[8])));
						} else {
							e.setIdSalarie(null);
						}
						ev.save(e);
					}

					ObjectifsEntretienServiceImpl obj = new ObjectifsEntretienServiceImpl();
					if (tab[0].equals("objectifsentretien")) {
						ObjectifsEntretienBean o = new ObjectifsEntretienBean();
						o.setIdObjectif(0);
						o.setIntitule(!tab[2].equals("null") ? tab[2] : "");
						o.setDelai(!tab[3].equals("null") ? tab[3] : "");
						o.setMoyen(!tab[4].equals("null") ? tab[4] : "");
						o.setResultat(!tab[5].equals("null") ? tab[5] : "");
						o.setCommentaire(!tab[6].equals("null") ? tab[6] : "");
						if (!tab[7].equals("null")) {
							o.setIdEntretien(entretien.get(Integer
									.valueOf(tab[7])) != null ? entretien
									.get(Integer.valueOf(tab[7])) : null);
						} else {
							o.setIdEntretien(null);
						}
						obj.save(o);
					}

					FicheDePosteServiceImpl fichp = new FicheDePosteServiceImpl();
					if (tab[0].equals("fichedeposte")) {
						FicheDePosteBean fp = new FicheDePosteBean();
						fp.setId(0);
						if (!tab[2].equals("null")) {
							fp.setIdSalarie(salarie.get(Integer.valueOf(tab[2])));
						} else {
							fp.setIdSalarie(null);
						}
						if (!tab[3].equals("null")) {
							fp.setIdFicheMetierType(ficheMetier.get(Integer
									.valueOf(tab[3])));
						} else {
							fp.setIdFicheMetierType(null);
						}
						if (!tab[4].equals("null")) {
							fp.setDateModification(formatter.parse(tab[4]));
						} else {
							fp.setDateModification(null);
						}
						fp.setCompetencesSpecifiquesTexte(!tab[5]
								.equals("null") ? tab[5] : "");
						fp.setCompetencesSpecifiques(!tab[6].equals("null") ? tab[6]
								: "");
						fp.setSavoir(!tab[7].equals("null") ? tab[7] : "");
						fp.setSavoirFaire(!tab[8].equals("null") ? tab[8] : "");
						fp.setSavoirEtre(!tab[9].equals("null") ? tab[9] : "");
						fp.setEvaluationGlobale(!tab[10].equals("null") ? tab[10]
								: "");
						fp.setCategorieCompetence(!tab[11].equals("null") ? tab[11]
								: "");
						fp.setCategorieCompetence2(!tab[12].equals("null") ? tab[12]
								: "");
						fp.setCategorieCompetence3(!tab[13].equals("null") ? tab[13]
								: "");
						fp.setCategorieCompetence4(!tab[14].equals("null") ? tab[14]
								: "");
						fp.setCategorieCompetence5(!tab[15].equals("null") ? tab[15]
								: "");
						fp.setMobiliteEnvisagee(!tab[16].equals("null") ? tab[16]
								: "");
						fp.setCommentaire(!tab[17].equals("null") ? tab[17]
								: "");
						if (!tab[18].equals("null")) {
							fp.setDateCreation(formatter.parse(tab[18]));
						} else {
							fp.setDateCreation(null);
						}
						fp.setCompetencesNouvelles(!tab[19].equals("null") ? tab[19]
								: "");
						fp.setCompetencesNouvelles2(!tab[20].equals("null") ? tab[20]
								: "");
						fp.setCompetencesNouvelles3(!tab[21].equals("null") ? tab[21]
								: "");
						fp.setCompetencesNouvelles4(!tab[22].equals("null") ? tab[22]
								: "");
						fp.setCompetencesNouvelles5(!tab[23].equals("null") ? tab[23]
								: "");
						fp.setActivitesSpecifiques(!tab[24].equals("null") ? tab[24]
								: "");
						if (!tab[25].equals("null")) {
							fp.setJustificatif(tab[25]);
						} else {
							fp.setJustificatif(null);
						}
						fichp.save(fp);
					}

					FicheMetierEntrepriseServiceImpl fichme = new FicheMetierEntrepriseServiceImpl();
					if (tab[0].equals("fichemetierentreprise")) {
						FicheMetierEntrepriseBean fm = new FicheMetierEntrepriseBean();
						if (!tab[1].equals("null")) {
							fm.setIdFicheMetier(ficheMetier.get(Integer
									.valueOf(tab[1])));
						} else {
							fm.setIdFicheMetier(null);
						}
						if (!tab[2].equals("null")) {
							fm.setIdEntreprise(entreprise.get(Integer
									.valueOf(tab[2])));
						} else {
							fm.setIdEntreprise(null);
						}
						fichme.save(fm);
					}

					FicheMetierServiceImpl fichm = new FicheMetierServiceImpl();
					if (tab[0].equals("fichemetier")) {
						FicheMetierBean fm = new FicheMetierBean();
						fm.setId(0);
						fm.setFinaliteFicheMetier(!tab[2].equals("null") ? tab[2]
								: "");
						fm.setSavoirFaire(!tab[3].equals("null") ? tab[3] : "");
						fm.setActiviteResponsabilite(!tab[4].equals("null") ? tab[4]
								: "");
						fm.setIntituleFicheMetier(!tab[5].equals("null") ? tab[5]
								: "");
						if (!tab[6].equals("null")) {
							fm.setCspReference(Integer.valueOf(tab[6]));
						} else {
							fm.setCspReference(null);
						}
						fm.setNiveauFormationRequis(!tab[7].equals("null") ? tab[7]
								: "");
						fm.setSavoirEtre(!tab[8].equals("null") ? tab[8] : "");
						fm.setSavoir(!tab[9].equals("null") ? tab[9] : "");
						fm.setParticularite(!tab[10].equals("null") ? tab[10]
								: "");
						if (!tab[11].equals("null")) {
							fm.setJustificatif(tab[11]);
						} else {
							fm.setJustificatif(null);
						}
						fm.setIdGroupe(idGroupe);
						fichm.save(fm);
						ficheMetier.put(Integer.valueOf(tab[1]), fm.getId());
						mappingService.saveOrUppdate(new MappingRepriseBean(
								"ficheMetier", Integer.valueOf(tab[1]), fm
										.getId(), idGroupe));
					}

					FormationServiceImpl form = new FormationServiceImpl();
					if (tab[0].equals("formation")) {
						FormationBean fo = new FormationBean();
						fo.setId(0);
						if (!tab[2].equals("null")) {
							fo.setDebutFormation(formatter.parse(tab[2]));
						} else {
							fo.setFinFormation(null);
						}
						if (!tab[3].equals("null")) {
							fo.setFinFormation(formatter.parse(tab[3]));
						} else {
							fo.setFinFormation(null);
						}
						fo.setNomFormation(!tab[4].equals("null") ? tab[4] : "");
						fo.setOrganismeFormation(!tab[5].equals("null") ? tab[5]
								: "");
						fo.setMode(!tab[6].equals("null") ? tab[6] : "");
						if (!tab[7].equals("null")) {
							fo.setVolumeHoraire(Integer.valueOf(tab[7]));
						} else {
							fo.setVolumeHoraire(null);
						}
						if (!tab[8].equals("null")) {
							fo.setDif(Integer.valueOf(tab[8]));
						} else {
							fo.setDif(null);
						}
						if (!tab[9].equals("null")) {
							fo.setHorsDif(Integer.valueOf(tab[9]));
						} else {
							fo.setHorsDif(null);
						}
						if (!tab[10].equals("null")) {
							fo.setNombreJourOuvre(Double.valueOf(tab[10]));
						} else {
							fo.setNombreJourOuvre(null);
						}
						if (!tab[11].equals("null")) {
							fo.setJustificatif(tab[11]);
						} else {
							fo.setJustificatif(null);
						}
						if (!tab[12].equals("null")) {
							fo.setCoutOpca(Double.valueOf(tab[12]));
						} else {
							fo.setCoutOpca(null);
						}
						if (!tab[13].equals("null")) {
							fo.setCoutEntreprise(Double.valueOf(tab[13]));
						} else {
							fo.setCoutEntreprise(null);
						}
						if (!tab[14].equals("null")) {
							fo.setCoutAutre(Double.valueOf(tab[14]));
						} else {
							fo.setCoutAutre(null);
						}
						if (!tab[15].equals("null")) {
							fo.setIdDomaineFormationBeanSelected(Integer
									.valueOf(tab[15]));
						} else {
							fo.setIdDomaineFormationBeanSelected(null);
						}
						if (!tab[16].equals("null")) {
							fo.setIdSalarie(salarie.get(Integer
									.valueOf(tab[16])));
						} else {
							fo.setIdSalarie(null);
						}
						if (!tab[17].equals("null")) {
							fo.setIdAbsence(absence.get(Integer
									.valueOf(tab[17])));
						} else {
							fo.setIdAbsence(-1);
						}
						form.save(fo);
					}

					/*
					 * UserServiceImpl user = new UserServiceImpl(); if
					 * (tab[0].equals("gpec_user")) { UserBean u = new
					 * UserBean(); u.setId(0); u.setLogin(tab[2]);
					 * u.setPassword(tab[3]); if (!tab[4].equals("null")) {
					 * u.setEvenement(Integer.valueOf(tab[4]) == 0 ? false :
					 * true); } else { u.setEvenement(false); } if
					 * (!tab[5].equals("null")) {
					 * u.setRemuneration(Integer.valueOf(tab[5]) == 0 ? false :
					 * true); } else { u.setRemuneration(false); } if
					 * (!tab[6].equals("null")) {
					 * u.setFicheDePoste(Integer.valueOf(tab[6]) == 0 ? false :
					 * true); } else { u.setFicheDePoste(false); } if
					 * (!tab[7].equals("null")) {
					 * u.setEntretien(Integer.valueOf(tab[7]) == 0 ? false :
					 * true); } else { u.setEntretien(false); } if
					 * (!tab[8].equals("null")) {
					 * u.setContratTravail(Integer.valueOf(tab[8]) == 0 ? false
					 * : true); } else { u.setContratTravail(false); } if
					 * (!tab[9].equals("null")) {
					 * u.setAdmin(Integer.valueOf(tab[9]) == 0 ? false : true);
					 * } else { u.setAdmin(false); } u.setIdGroupe(idGroupe);
					 * user.save(u); }
					 */

					HabilitationServiceImpl hab = new HabilitationServiceImpl();
					if (tab[0].equals("habilitation")) {
						HabilitationBean h = new HabilitationBean();
						h.setId(-1);
						if (!tab[2].equals("null")) {
							h.setDelivrance(formatter.parse(tab[2]));
						} else {
							h.setDelivrance(null);
						}
						if (!tab[3].equals("null")) {
							h.setExpiration(formatter.parse(tab[3]));
						} else {
							h.setExpiration(null);
						}
						h.setDureeValidite(Integer.valueOf(tab[4]));
						if (!tab[5].equals("null")) {
							h.setJustificatif(tab[5]);
						} else {
							h.setJustificatif(null);
						}
						h.setCommentaire(!tab[6].equals("null") ? tab[6] : "");
						if (!tab[7].equals("null")) {
							h.setIdSalarie(salarie.get(Integer.valueOf(tab[7])));
						} else {
							h.setIdSalarie(null);
						}
						if (!tab[8].equals("null")) {
							if (Integer.valueOf(tab[8]) > 12) {
								h.setIdTypeHabilitationSelected(typeHabilitation
										.get(Integer.valueOf(tab[8])));
							} else {
								h.setIdTypeHabilitationSelected(Integer
										.valueOf(tab[8]));
							}
						} else {
							h.setIdTypeHabilitationSelected(null);
						}
						hab.save(h);
					}

					LienRemunerationRevenuServiceImpl lien = new LienRemunerationRevenuServiceImpl();
					if (tab[0]
							.equals("lien_remuneration_revenu_complementaire")) {
						LienRemunerationRevenuBean l = new LienRemunerationRevenuBean();
						l.setIdLienRemunerationRevenuComplementaire(0);
						l.setMontant(tab[2]);
						l.setCommentaire(tab[3]);
						l.setActualisation(tab[4]);
						if (!tab[5].equals("null")) {
							l.setRemonteNPrec(Integer.valueOf(tab[5]) == 0 ? false
									: true);
						} else {
							l.setRemonteNPrec(false);
						}
						if (!tab[6].equals("null")) {
							l.setIdRemuneration(remuneration.get(Integer
									.valueOf(tab[6])));
						} else {
							l.setIdRemuneration(null);
						}
						if (!tab[7].equals("null")) {
							l.setIdRevenuComplementaire(revenuComplementaire
									.get(Integer.valueOf(tab[7])));
						} else {
							l.setIdRevenuComplementaire(null);
						}
						lien.save(l);
					}

					MetierServiceImpl met = new MetierServiceImpl();
					if (tab[0].equals("metier")) {
						MetierBean m = new MetierBean();
						m.setId(0);
						m.setNom(tab[2]);
						m.setDifficultes(tab[3].equals("null") ? null : tab[3]);
						if (!tab[4].equals("null")) {
							m.setIdFamilleMetier(Integer.valueOf(tab[4]));
						} else {
							m.setIdFamilleMetier(null);
						}
						m.setIdGroupe(idGroupe);
						met.save(m, idGroupe);
						metier.put(Integer.valueOf(tab[1]), m.getId());
						mappingService.saveOrUppdate(new MappingRepriseBean(
								"metier", Integer.valueOf(tab[1]), m.getId(),
								idGroupe));
					}

					ParamsGenerauxServiceImpl param = new ParamsGenerauxServiceImpl();
					if (tab[0].equals("paramsgeneraux")) {
						
						if (!tab[17].equals(null) && !paramsGenerauxEnt.contains(Integer
									.valueOf(tab[17]))) {
							paramsGenerauxEnt.add(Integer
										.valueOf(tab[17]));
							ParamsGenerauxBean p = new ParamsGenerauxBean();
							p.setId(0);
							if (!tab[2].equals("null")) {
								p.setAgeLegalRetraiteAnN(Integer.valueOf(tab[2]));
							} else {
								p.setAgeLegalRetraiteAnN(null);
							}
							if (!tab[3].equals("null")) {
								p.setAgeLegalRetraiteAnN1(Integer.valueOf(tab[3]));
							} else {
								p.setAgeLegalRetraiteAnN1(null);
							}
							if (!tab[4].equals("null")) {
								p.setAgeLegalRetraiteAnN2(Integer.valueOf(tab[4]));
							} else {
								p.setAgeLegalRetraiteAnN2(null);
							}
	
							if (!tab[5].equals("null")) {
								p.setDureeTravailAnNHeuresRealiseesEffectifTot(Double
										.valueOf(tab[5]));
							} else {
								p.setDureeTravailAnNHeuresRealiseesEffectifTot(null);
							}
							if (!tab[6].equals("null")) {
								p.setDureeTravailAnN1HeuresRealiseesEffectifTot(Double
										.valueOf(tab[6]));
							} else {
								p.setDureeTravailAnN1HeuresRealiseesEffectifTot(null);
							}
							if (!tab[7].equals("null")) {
								p.setDureeTravailAnN2HeuresRealiseesEffectifTot(Double
										.valueOf(tab[7]));
							} else {
								p.setDureeTravailAnN2HeuresRealiseesEffectifTot(null);
							}
	
							if (!tab[8].equals("null")) {
								p.setDureeTravailAnNJoursEffectifTot(Double
										.valueOf(tab[8]));
							} else {
								p.setDureeTravailAnNJoursEffectifTot(null);
							}
							if (!tab[9].equals("null")) {
								p.setDureeTravailAnN1JoursEffectifTot(Double
										.valueOf(tab[9]));
							} else {
								p.setDureeTravailAnN1JoursEffectifTot(null);
							}
							if (!tab[10].equals("null")) {
								p.setDureeTravailAnN2JoursEffectifTot(Double
										.valueOf(tab[10]));
							} else {
								p.setDureeTravailAnN2JoursEffectifTot(null);
							}
	
							if (!tab[11].equals("null")) {
								p.setDureeTravailAnNJoursSal(Integer
										.valueOf(tab[11]));
							} else {
								p.setDureeTravailAnNJoursSal(null);
							}
							if (!tab[12].equals("null")) {
								p.setDureeTravailAnN1JoursSal(Integer
										.valueOf(tab[12]));
							} else {
								p.setDureeTravailAnN1JoursSal(null);
							}
							if (!tab[13].equals("null")) {
								p.setDureeTravailAnN2JoursSal(Integer
										.valueOf(tab[13]));
							} else {
								p.setDureeTravailAnN2JoursSal(null);
							}
	
							if (!tab[14].equals("null")) {
								p.setDureeTravailAnNHeuresSal(Integer
										.valueOf(tab[14]));
							} else {
								p.setDureeTravailAnNHeuresSal(null);
							}
							if (!tab[15].equals("null")) {
								p.setDureeTravailAnN1HeuresSal(Integer
										.valueOf(tab[15]));
							} else {
								p.setDureeTravailAnN1HeuresSal(null);
							}
							if (!tab[16].equals("null")) {
								p.setDureeTravailAnN2HeuresSal(Integer
										.valueOf(tab[16]));
							} else {
								p.setDureeTravailAnN2HeuresSal(null);
							}
	
							if (!tab[17].equals("null")) {
								p.setIdEntreprise(entreprise.get(Integer
										.valueOf(tab[17])));
							} else {
								p.setIdEntreprise(null);
							}
	
							if (!tab[18].equals("null")) {
								p.setMasseSalarialeAnN(Double.valueOf(tab[18]));
							} else {
								p.setMasseSalarialeAnN(null);
							}
							if (!tab[19].equals("null")) {
								p.setMasseSalarialeAnN1(Double.valueOf(tab[19]));
							} else {
								p.setMasseSalarialeAnN1(null);
							}
							if (!tab[20].equals("null")) {
								p.setMasseSalarialeAnN2(Double.valueOf(tab[20]));
							} else {
								p.setMasseSalarialeAnN2(null);
							}
	
							if (!tab[21].equals("null")) {
								p.setEffectifMoyenAnN(Double.valueOf(tab[21]));
							} else {
								p.setEffectifMoyenAnN(null);
							}
							if (!tab[22].equals("null")) {
								p.setEffectifMoyenAnN1(Double.valueOf(tab[22]));
							} else {
								p.setEffectifMoyenAnN1(null);
							}
							if (!tab[23].equals("null")) {
								p.setEffectifMoyenAnN2(Double.valueOf(tab[23]));
							} else {
								p.setEffectifMoyenAnN2(null);
							}
	
							if (!tab[24].equals("null")) {
								p.setPourcentageFormationAnN(Double
										.valueOf(tab[24]));
							} else {
								p.setPourcentageFormationAnN(null);
							}
							if (!tab[25].equals("null")) {
								p.setPourcentageFormationAnN1(Double
										.valueOf(tab[25]));
							} else {
								p.setPourcentageFormationAnN1(null);
							}
							if (!tab[26].equals("null")) {
								p.setPourcentageFormationAnN2(Double
										.valueOf(tab[26]));
							} else {
								p.setPourcentageFormationAnN2(null);
							}
	
							if (!tab[27].equals("null")) {
								p.setEffectifPhysiqueAnN(Integer.valueOf(tab[27]));
							} else {
								p.setEffectifPhysiqueAnN(null);
							}
							if (!tab[28].equals("null")) {
								p.setEffectifPhysiqueAnN1(Integer.valueOf(tab[28]));
							} else {
								p.setEffectifPhysiqueAnN1(null);
							}
							if (!tab[29].equals("null")) {
								p.setEffectifPhysiqueAnN2(Integer.valueOf(tab[29]));
							} else {
								p.setEffectifPhysiqueAnN2(null);
							}
	
							if (!tab[30].equals("null")) {
								p.setEffectifEtpAnN(Double.valueOf(tab[30]));
							} else {
								p.setEffectifEtpAnN(null);
							}
							if (!tab[31].equals("null")) {
								p.setEffectifEtpAnN1(Double.valueOf(tab[31]));
							} else {
								p.setEffectifEtpAnN1(null);
							}
							if (!tab[32].equals("null")) {
								p.setEffectifEtpAnN2(Double.valueOf(tab[32]));
							} else {
								p.setEffectifEtpAnN2(null);
							}
							if (!tab[33].equals("null")) {
								p.setAnnee(Integer.valueOf(tab[33]));
							} else {
								p.setAnnee(null);
							}
							param.save(p);
						}
					}

					ParcoursServiceImpl par = new ParcoursServiceImpl();
					if (tab[0].equals("parcours")) {
						if (!tab[11].equals("null") && !tab[11].equals("0")) {
							ParcoursBean p = new ParcoursBean();
							p.setId(-1);
							if (!tab[2].equals("null")) {
								p.setDebutFonction(formatter.parse(tab[2]));
							} else {
								p.setDebutFonction(null);
							}
							if (!tab[3].equals("null")) {
								p.setFinFonction(formatter.parse(tab[3]));
							} else {
								p.setFinFonction(null);
							}
							p.setEquivalenceTempsPlein(Integer.valueOf(tab[4]));
							if (!tab[5].equals("null")) {
								p.setJustificatif(tab[5]);
							} else {
								p.setJustificatif(null);
							}
							p.setCoefficient(!tab[6].equals("null") ? tab[6] : "");
							p.setNiveau(!tab[7].equals("null") ? tab[7] : "");
							p.setEchelon(!tab[8].equals("null") ? tab[8] : "");
							if (!tab[9].equals("null")) {
								p.setIdMetierSelected(metier.get(Integer
										.valueOf(tab[9])));
							} else {
								p.setIdMetierSelected(null);
							}
							if (!tab[10].equals("null")) {
								p.setIdTypeContratSelected(Integer.valueOf(tab[10]));
							} else {
								p.setIdTypeContratSelected(null);
							}
							if (!tab[11].equals("null")) {
								p.setIdSalarie(salarie.get(Integer.valueOf(tab[11])));
							} else {
								p.setIdSalarie(null);
							}
							if (!tab[12].equals("null")) {
								p.setIdTypeStatutSelected(Integer.valueOf(tab[12]));
							} else {
								p.setIdTypeStatutSelected(null);
							}
							if (!tab[13].equals("null")) {
								p.setIdTypeRecoursSelected(Integer.valueOf(tab[13]));
							} else {
								p.setIdTypeRecoursSelected(null);
							}
							par.save(p);
						}
					}

					PersonneAChargeServiceImpl pers = new PersonneAChargeServiceImpl();
					if (tab[0].equals("personne_a_charge")) {
						PersonneAChargeBean p = new PersonneAChargeBean();
						p.setId(0);
						p.setNom(!tab[2].equals("null") ? tab[2] : "");
						p.setPrenom(!tab[3].equals("null") ? tab[3] : "");
						p.setLienParente(!tab[4].equals("null") ? tab[4] : "");
						if (!tab[5].equals("null")) {
							p.setDateNaissance(formatter.parse(tab[5]));
						} else {
							p.setDateNaissance(null);
						}
						if (!tab[6].equals("null")) {
							p.setIdSalarie(salarie.get(Integer.valueOf(tab[6])));
						} else {
							p.setIdSalarie(null);
						}
						pers.save(p);
					}

					PiecesJustificativesServiceImpl pj = new PiecesJustificativesServiceImpl();
					if (tab[0].equals("pieces_justificatives")) {
						PiecesJustificativesBean p = new PiecesJustificativesBean();
						p.setId(0);
						p.setCarteIdentite(Integer.valueOf(tab[2]) == 0 ? false
								: true);
						p.setAttestationSecu(Integer.valueOf(tab[3]) == 0 ? false
								: true);
						p.setPermisConduire(Integer.valueOf(tab[4]) == 0 ? false
								: true);
						p.setRib(Integer.valueOf(tab[5]) == 0 ? false : true);
						p.setDiplomes(Integer.valueOf(tab[6]) == 0 ? false
								: true);
						p.setCertificatTravail(Integer.valueOf(tab[7]) == 0 ? false
								: true);
						p.setAttestaionVisiteMedicale(Integer.valueOf(tab[8]) == 0 ? false
								: true);
						p.setHabilitations(Integer.valueOf(tab[9]) == 0 ? false
								: true);
						if (!tab[10].equals("null")) {
							p.setIdSalarie(salarie.get(Integer.valueOf(tab[10])));
						} else {
							p.setIdSalarie(null); 
						}
						pj.save(p);
					}

					RevenuComplementaireServiceImpl rev = new RevenuComplementaireServiceImpl();
					if (tab[0].equals("ref_revenu_complementaire")) {
						RevenuComplementaireBean r = new RevenuComplementaireBean();
						r.setId(0);
						r.setLibelle(tab[2]);
						r.setType(tab[3]);
						r.setIdGroupe(idGroupe);
						rev.save(r, idGroupe);
						revenuComplementaire.put(Integer.valueOf(tab[1]),
								r.getId());
						mappingService.saveOrUppdate(new MappingRepriseBean(
								"revenuComplementaire",
								Integer.valueOf(tab[1]), r.getId(), idGroupe));
					}

					RemunerationServiceImpl remu = new RemunerationServiceImpl();
					if (tab[0].equals("remuneration")) {
						RemunerationBean r = new RemunerationBean();
						r.setIdRemuneration(0);
						r.setHeuresSup50Commentaire(tab[2]);
						r.setAugmentationCollective(Double.valueOf(tab[3]));
						r.setHeuresSup50(Double.valueOf(tab[4]));
						r.setSalaireMensuelConventionnelBrut(Double
								.valueOf(tab[5]));
						if (!tab[6].equals("null")) {
							r.setAnnee(Integer.valueOf(tab[6]));
						} else {
							r.setAnnee(null);
						}
						r.setNiveau(tab[7]);
						r.setSalaireMinimumConventionnelActualisation(Double
								.valueOf(tab[8]));
						r.setAugmentationCollectiveActualisation(Double
								.valueOf(tab[9]));
						r.setSalaireDeBaseActualisation(Double.valueOf(tab[10]));
						if (!tab[11].equals("null")) {
							r.setHoraireMensuelTravaille(tab[11]);
						} else {
							r.setHoraireMensuelTravaille("");
						}
						if (!tab[12].equals("null")) {
							r.setIdMetier(metier.get(Integer.valueOf(tab[12])));
						} else {
							r.setIdMetier(null);
						}
						r.setSalaireMinimumConventionnelCommentaire(tab[13]);
						r.setEchelon(tab[14]);
						r.setSalaireDeBase(Double.valueOf(tab[15]));
						r.setSalaireDeBaseCommentaire(tab[16]);
						r.setHeuresSup25Actualisation(Double.valueOf(tab[17]));
						r.setCommentaire(tab[18]);
						r.setAugmentationIndividuelle(Double.valueOf(tab[19]));
						r.setHeuresSup25(Double.valueOf(tab[20]));
						r.setHeuresSup50Actualisation(Double.valueOf(tab[21]));
						if (!tab[22].equals("null")) {
							r.setIdStatut(Integer.valueOf(tab[22]));
						} else {
							r.setIdStatut(null);
						}
						r.setTauxHoraireBrut(Double.valueOf(tab[23]));
						r.setAugmentationIndividuelleActualisation(Double
								.valueOf(tab[24]));
						r.setAugmentationIndividuelleCommentaire(tab[25]);
						r.setCoefficient(tab[26]);
						if (!tab[27].equals("null")) {
							r.setIdRevenuComplementaire(revenuComplementaire
									.get(Integer.valueOf(tab[27])));
						} else {
							r.setIdRevenuComplementaire(null);
						}
						r.setSalaireMinimumConventionnel(Double
								.valueOf(tab[28]));
						r.setHeuresSup25Commentaire(tab[29]);
						r.setAugmentationCollectiveCommentaire(tab[30]);
						r.setSalaireMensuelBrut(Double.valueOf(tab[31]));
						r.setBaseBruteAnnuelle(Double.valueOf(tab[32]));
						r.setRemuGlobale(Double.valueOf(tab[33]));
						r.setHeuresSupAnnuelles(Double.valueOf(tab[34]));
						r.setAvantagesNonAssujettisAnnuels(Double
								.valueOf(tab[35]));
						if (!tab[36].equals("null")) {
							r.setIdSalarie(salarie.get(Integer.valueOf(tab[36])));
						} else {
							r.setIdSalarie(null);
						}

						remu.save(r);
						remuneration.put(Integer.valueOf(tab[1]),
								r.getIdRemuneration());
						mappingService.saveOrUppdate(new MappingRepriseBean(
								"remuneration", Integer.valueOf(tab[1]), r
										.getIdRemuneration(), idGroupe));
					}

					SalarieServiceImpl sal = new SalarieServiceImpl();
					if (tab[0].equals("salarie")) {
						SalarieBean s = new SalarieBean();
						s.setId(0);
						s.setCivilite(tab[2]);
						s.setNom(!tab[3].equals("null") ? tab[3] : "");
						s.setPrenom(!tab[4].equals("null") ? tab[4] : "");
						s.setAdresse(!tab[5].equals("null") ? tab[5] : "");
						if (!tab[6].equals("null")) {
							s.setDateNaissance(formatter.parse(tab[6]));
						} else {
							s.setDateNaissance(null);
						}
						s.setLieuNaissance(!tab[7].equals("null") ? tab[7] : "");
						s.setTelephone(!tab[8].equals("null") ? tab[8] : "");
						if (!tab[9].equals("null")) {
							s.setCreditDif(Double.valueOf(tab[9]));
						} else {
							s.setCreditDif(null);
						}
						s.setNivFormationInit(tab[11]);
						s.setNivFormationAtteint(tab[12]);
						if (!tab[13].equals("null")) {
							s.setCv(tab[13]);
						} else {
							s.setCv(null);
						}
						s.setPresent(Integer.valueOf(tab[14]) == 0 ? false
								: true);
						s.setImpression(Integer.valueOf(tab[15]) == 0 ? false
								: true);
						s.setPossedePermisConduire(Integer.valueOf(tab[16]) == 0 ? false
								: true);
						if (!tab[17].equals("null")) {
							s.setDateLastSaisieDif(formatter.parse(tab[17]));
						} else {
							s.setDateLastSaisieDif(null);
						}
						s.setTelephonePortable(!tab[18].equals("null") ? tab[18]
								: "");
						s.setSituationFamiliale(!tab[19].equals("null") ? tab[19]
								: "");
						s.setPersonneAPrevenirNom(!tab[20].equals("null") ? tab[20]
								: "");
						s.setPersonneAPrevenirPrenom(!tab[21].equals("null") ? tab[21]
								: "");
						s.setPersonneAPrevenirAdresse(!tab[22].equals("null") ? tab[22]
								: "");
						s.setPersonneAPrevenirTelephone(!tab[23].equals("null") ? tab[23]
								: "");
						s.setPersonneAPrevenirLienParente(!tab[24]
								.equals("null") ? tab[24] : "");
						s.setCommentaire(!tab[25].equals("null") ? tab[25] : "");
						if (!tab[26].equals("null")) {
							s.setIdEntrepriseSelected(entreprise.get(Integer
									.valueOf(tab[26])));
						} else {
							s.setIdEntrepriseSelected(-1);
						}
						if (!tab[27].equals("null")) {
							s.setIdServiceSelected(service.get(Integer
									.valueOf(tab[27])));
						} else {
							s.setIdServiceSelected(-1);
						}
						sal.save(s);
						lienHierarchique.put(
								s.getId(),
								tab[10].equals("null") ? null : Integer
										.valueOf(tab[10]));
						salarie.put(Integer.valueOf(tab[1]), s.getId());
						mappingService.saveOrUppdate(new MappingRepriseBean(
								"salarie", Integer.valueOf(tab[1]), s.getId(),
								idGroupe));
					}

					ServiceImpl serv = new ServiceImpl();
					if (tab[0].equals("service")) {
						ServiceBean s = new ServiceBean();
						s.setId(0);
						s.setNom(tab[2]);
						if (!tab[3].equals("null")) {
							s.setIdEntreprise(entreprise.get(Integer
									.valueOf(tab[3])));
						} else {
							s.setIdEntreprise(null);
						}
						serv.save(s);
						service.put(Integer.valueOf(tab[1]), s.getId());
						mappingService.saveOrUppdate(new MappingRepriseBean(
								"service", Integer.valueOf(tab[1]), s.getId(),
								idGroupe));
					}

					TransmissionServiceImpl tr = new TransmissionServiceImpl();
					if (tab[0].equals("transmission")) {
						TransmissionBean t = new TransmissionBean();
						t.setId(-1);
						t.setDateDerniereDemande(new Date());
						t.setDateTransmission(new Date());
						t.setIdGroupe(idGroupe);
						tr.save(t, idGroupe);
					}

					TypeAbsenceServiceImpl ta = new TypeAbsenceServiceImpl();
					if (tab[0].equals("typeabsence")) {
						if (Integer.valueOf(tab[1]) > 18) {
							TypeAbsenceBean t = new TypeAbsenceBean();
							t.setId(0);
							t.setNom(tab[2]);
							t.setIdGroupe(idGroupe);
							ta.save(t);
							typeAbsence.put(Integer.valueOf(tab[1]), t.getId());
							mappingService
									.saveOrUppdate(new MappingRepriseBean(
											"typeAbsence", Integer
													.valueOf(tab[1]),
											t.getId(), idGroupe));
						}
					}

					TypeHabilitationServiceImpl th = new TypeHabilitationServiceImpl();
					if (tab[0].equals("typehabilitation")) {
						if (Integer.valueOf(tab[1]) > 12) {
							TypeHabilitationBean t = new TypeHabilitationBean();
							t.setId(0);
							t.setNom(tab[2]);
							t.setIdGroupe(idGroupe);
							th.save(t);
							typeHabilitation.put(Integer.valueOf(tab[1]),
									t.getId());
							mappingService
									.saveOrUppdate(new MappingRepriseBean(
											"typeHabilitation", Integer
													.valueOf(tab[1]),
											t.getId(), idGroupe));
						}
					}
				}
			}
			br.close();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} finally {
			if (sessionf != null && sessionf.isOpen()) {
				sessionf.close();
			}
		}

		SalarieServiceImpl salServ = new SalarieServiceImpl();
		for (Integer idSal : lienHierarchique.keySet()) {
			SalarieBean s = salServ.getSalarieBeanById(idSal);
			//pas de hierarchique si == 0 ou s'il n'existe plus (cas SOMAC 3.0)
			s.setIdLienSubordination(lienHierarchique.get(idSal) == null ? null
					: ((lienHierarchique.get(idSal) == 0) || (salarie
							.get(lienHierarchique.get(idSal))==null)) ? 0 : salarie
							.get(lienHierarchique.get(idSal)));
			salServ.saveOrUppdate(s);
		}

		System.out.println("Import terminé");
		return idGroupe;

	}

	public static List<Integer> splitByEntreprise(int idGroupe)
			throws Exception {

		EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
		GroupeServiceImpl grpServ = new GroupeServiceImpl();
		MetierServiceImpl metServ = new MetierServiceImpl();
		AdminServiceImpl admServ = new AdminServiceImpl();
		TypeAbsenceServiceImpl typAbs = new TypeAbsenceServiceImpl();
		TransmissionServiceImpl transServ = new TransmissionServiceImpl();
		MappingRepriseServiceImpl mrServ = new MappingRepriseServiceImpl();
		TypeHabilitationServiceImpl typHab = new TypeHabilitationServiceImpl();
		RevenuComplementaireServiceImpl rcServ = new RevenuComplementaireServiceImpl();
		UserServiceImpl usrServ = new UserServiceImpl();
		ContratTravailServiceImpl ctServ = new ContratTravailServiceImpl();
		AbsenceServiceImpl absServ = new AbsenceServiceImpl();
		HabilitationServiceImpl habServ = new HabilitationServiceImpl();
		LienRemunerationRevenuServiceImpl lienRevCompServ = new LienRemunerationRevenuServiceImpl();
		FicheMetierServiceImpl ficheMetServ = new FicheMetierServiceImpl();

		List<Integer> newGroupes = new ArrayList<Integer>();

		for (EntrepriseBean e : entServ.getEntreprisesList(idGroupe)) {
			GroupeBean g = new GroupeBean();
			g.setId(0);
			g.setDeleted(false);
			g.setFinPeriodeEssai(null);
			g.setNom(e.getNom());
			grpServ.saveOrUppdate(g);

			int newGroupeId = g.getId();
			newGroupes.add(newGroupeId);

			for (FicheMetierBean fm : ficheMetServ
					.getFichesMetierList(idGroupe)) {
				ficheMetServ.saveOrUppdate(fm, newGroupeId);
			}

			for (MetierBean m : metServ.getMetiersList(idGroupe)) {
				List<ContratTravailBean> ctList = ctServ
						.getContratTravailBeanListByIdMetier(m.getId());
				m.setId(0);
				metServ.saveOrUppdate(m, newGroupeId);
				for (ContratTravailBean ct : ctList) {
					ct.setIdMetierSelected(m.getId());
					ctServ.saveOrUppdate(ct);
				}
			}

			AdminBean adm = admServ.getAdminBean(idGroupe);
			if (adm == null) {
				System.err
						.println("Reprise de données spécifique : Erreur sur la récupération de l'objet admin");
				return null;
			} else {
				adm.setId(0);
				admServ.saveOrUppdate(adm, newGroupeId);
			}

			for (TypeAbsenceBean t : typAbs.getTypeAbsenceList(idGroupe)) {
				List<AbsenceBean> absList = absServ
						.getAbsenceBeanListByIdTypeAbsence(t.getId(), idGroupe);
				t.setId(0);
				t.setIdGroupe(newGroupeId);
				typAbs.saveOrUppdate(t);
				for (AbsenceBean abs : absList) {
					abs.setIdTypeAbsenceSelected(t.getId());
					absServ.saveOrUppdate(abs);
				}
			}

			TransmissionBean t = transServ.getTransmissionBean(idGroupe);
			if (t == null) {
				System.err
						.println("Reprise de données spécifique : Erreur sur la récupération de l'objet transmission");
				return null;
			} else {
				t.setId(-1);
				transServ.saveOrUppdate(t, newGroupeId);
			}

			for (MappingRepriseBean mr : mrServ
					.getMappingRepriseEntrepriseByGroupe(idGroupe)) {
				mr.setIdGroupe(newGroupeId);
				mr.setEntite(mr.getEntite() + "_" + idGroupe + "_"
						+ newGroupeId);
				mrServ.saveOrUppdateWithTransaction(mr);
			}

			for (TypeHabilitationBean th : typHab
					.getTypeHabilitationList(idGroupe)) {
				List<HabilitationBean> habList = habServ
						.getHabilitationsListFromTypeHabilitation(th.getId(),
								idGroupe);
				th.setId(0);
				th.setIdGroupe(newGroupeId);
				typHab.saveOrUppdate(th);
				for (HabilitationBean hab : habList) {
					hab.setIdTypeHabilitationSelected(th.getId());
					habServ.saveOrUppdate(hab);
				}
			}

			for (RevenuComplementaireBean r : rcServ
					.getRevenuComplementaireList(idGroupe)) {
				List<LienRemunerationRevenuBean> lienRevCompList = lienRevCompServ
						.getLienRemunerationRevenuFromIdRevComp(r.getId());
				r.setId(0);
				rcServ.saveOrUpdate(r, newGroupeId);
				for (LienRemunerationRevenuBean lr : lienRevCompList) {
					lr.setIdRevenuComplementaire(r.getId());
					lienRevCompServ.saveOrUpdate(lr);
				}
			}

			UserBean usr = usrServ.getUserList(idGroupe, false, false, true)
					.get(0);
			if (usr == null) {
				System.err
						.println("Reprise de données spécifique : Erreur sur la récupération de l'admin groupe");
				return null;
			} else {
				usr.setId(0);
				usrServ.saveOrUppdate(usr, newGroupeId);
			}

			entServ.saveOrUppdate(e, newGroupeId);
		}

		return newGroupes;
	}

	public static int getMaxId(Session session, String id_column, String table) {
		SQLQuery q2 = session.createSQLQuery("select max( " + id_column
				+ " ) as m_a_x from " + table);
		q2.addScalar("m_a_x", Hibernate.INTEGER);

		List result2 = q2.list();
		if (result2.get(0) != null) {
			return (Integer.valueOf(result2.get(0).toString()) + 1);
		} else {
			return 1;
		}
	}

}
