package com.cci.gpec.web.backingBean.jdom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.DomaineFormationBean;
import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.FamilleMetierBean;
import com.cci.gpec.commons.FicheDePosteBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.ParamsGenerauxBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.SalarieBeanExport;
import com.cci.gpec.commons.StatutBean;
import com.cci.gpec.commons.TypeAbsenceBean;
import com.cci.gpec.commons.TypeAccidentBean;
import com.cci.gpec.commons.TypeContratBean;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.AccidentServiceImpl;
import com.cci.gpec.metier.implementation.DomaineFormationServiceImpl;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FamilleMetierServiceImpl;
import com.cci.gpec.metier.implementation.FicheDePosteServiceImpl;
import com.cci.gpec.metier.implementation.FormationServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.ParamsGenerauxServiceImpl;
import com.cci.gpec.metier.implementation.ParcoursServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.cci.gpec.metier.implementation.TypeAbsenceServiceImpl;
import com.cci.gpec.metier.implementation.TypeAccidentServiceImpl;
import com.cci.gpec.metier.implementation.TypeContratServiceImpl;
import com.cci.gpec.metier.implementation.TypeRecoursInterimServiceImpl;
import com.cci.gpec.web.ftp.Ftp;

public class ExportXml {
	
	private static final Logger LOGGER = Logger.getLogger(ExportXml.class);

	private Element racine;
	private Document document;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private File xml;
	private int idEntrepriseSelected;
	private int nbSalarie;
	private boolean echec = false;
	private boolean reussite = false;
	private FacesContext facesContext = FacesContext.getCurrentInstance();
	private HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

	public boolean isReussite() {
		return reussite;
	}

	public void setReussite(boolean reussite) {
		this.reussite = reussite;
	}

	public File getXml() {
		return xml;
	}

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

	public String getDateEntree(SalarieBean s) {
		ParcoursBean pb = getFirstParcours(s);
		if (pb != null) {
			return dateFormat.format(pb.getDebutFonction());
		} else {
			return "";
		}
	}

	public String getDateSortie(SalarieBean s) {
		ParcoursBean pb = getLastParcours(s);
		if (pb != null) {
			if (pb.getFinFonction() != null) {
				return dateFormat.format(pb.getFinFonction());
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	public int calculer(Date d) {
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

	private int nbreHeureFormation(SalarieBean salarie) {
		List<FormationBean> l = salarie.getFormationBeanList();
		int res = 0;
		for (int i = 0; i < l.size(); i++) {
			FormationBean form = l.get(i);
			res += form.getVolumeHoraire().intValue();
		}
		return res;
	}

	private int nbreHeureFormationCurYear(SalarieBean salarie) {
		List<FormationBean> l = salarie.getFormationBeanList();
		int res = 0;
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date());
		int curYear = gc.get(Calendar.YEAR);
		for (int i = 0; i < l.size(); i++) {
			FormationBean form = l.get(i);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(form.getDebutFormation());
			if (cal.get(Calendar.YEAR) == curYear) {
				res += form.getVolumeHoraire().intValue();
			}
		}
		return res;
	}

	private int nbreHeureFormationLastYear(SalarieBean salarie) {
		List<FormationBean> l = salarie.getFormationBeanList();
		int res = 0;
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date());
		int lastYear = gc.get(Calendar.YEAR) - 1;
		for (int i = 0; i < l.size(); i++) {
			FormationBean form = l.get(i);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(form.getDebutFormation());
			if (cal.get(Calendar.YEAR) == lastYear) {
				res += form.getVolumeHoraire().intValue();
			}
		}
		return res;
	}

	private ParcoursBean getFirstParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		Collections.sort(parcourList);
		Collections.reverse(parcourList);
		int nbrJour = 0;
		ParcoursBean parcoursBeanActuel = null;
		for (int i = 0; i < parcourList.size(); i++) {
			// Parcours actuel

			parcoursBeanActuel = parcourList.get(i);
			ParcoursBean parcoursBeanPrecedent = null;
			if (parcourList.size() == 1) {
				return parcoursBeanActuel;
			}
			if (i > 0 && i + 1 < parcourList.size()) {
				parcoursBeanPrecedent = parcourList.get(i + 1);
			}
			if (i > 0)
				if (parcoursBeanPrecedent != null
						&& parcoursBeanPrecedent.getFinFonction() != null)
					if (parcoursBeanActuel.getDebutFonction().compareTo(
							parcoursBeanPrecedent.getFinFonction()) > 0) {
						GregorianCalendar debParcoursActuel = new GregorianCalendar();
						debParcoursActuel.setTime(parcoursBeanActuel
								.getDebutFonction());
						GregorianCalendar finParcoursPrecedent = new GregorianCalendar();
						finParcoursPrecedent.setTime(parcoursBeanPrecedent
								.getFinFonction());
						long nbjJourMiliiSec = debParcoursActuel
								.getTimeInMillis()
								- finParcoursPrecedent.getTimeInMillis();
						nbrJour += nbjJourMiliiSec / 86400000;
					}
			if (nbrJour > 1) {
				return parcoursBeanActuel;
			}

		}
		if (parcourList.size() > 0) {
			return parcoursBeanActuel;
		} else {
			return null;
		}
	}

	public Integer getAgeRetraiteOfParam() throws Exception {
		Integer res = 60;
		ParamsGenerauxServiceImpl serv = new ParamsGenerauxServiceImpl();
		List<ParamsGenerauxBean> l = serv
				.getParamsGenerauxBeanListByIdEntreprise(idEntrepriseSelected);
		if ((l != null) && (l.size() > 0)) {
			Integer retraite = l.get(0).getAgeLegalRetraiteAnN();
			if (retraite != null) {
				res = retraite;
			}
		}
		return res;
	}

	public GregorianCalendar getRetraiteOfSalarie(SalarieBean s)
			throws Exception {

		GregorianCalendar naissance = new GregorianCalendar();
		naissance.setTime(s.getDateNaissance());
		GregorianCalendar retraite = new GregorianCalendar();
		retraite.set(naissance.get(Calendar.YEAR)
				+ getAgeRetraiteOfParam().intValue(),
				naissance.get(Calendar.MONTH), naissance.get(Calendar.DATE));
		return retraite;
	}

	public void init() throws Exception {
		racine = new Element("entreprise");
		EntrepriseServiceImpl servEntre = new EntrepriseServiceImpl();
		EntrepriseBean entrepBean = servEntre
				.getEntrepriseBeanById(idEntrepriseSelected);

		Attribute nbSalarie = new Attribute("nombre_salaries", getNbSalarie()
				+ "");
		racine.setAttribute(nbSalarie);

		Attribute numSiret = new Attribute("num_siret",
				entrepBean.getNumSiret() + "");
		racine.setAttribute(numSiret);

		ParamsGenerauxServiceImpl paramsGenerauxService = new ParamsGenerauxServiceImpl();
		ParamsGenerauxBean paramsGenerauxBean = paramsGenerauxService
				.getParamsGenerauxBeanByIdEntreprise(idEntrepriseSelected);

		Attribute effectifMoyen;
		Attribute effectifPhysiqueTotal;
		Attribute effectifETP;
		Attribute masseSalariale;
		Attribute montantContributionFormation;
		if (paramsGenerauxBean != null) {
			effectifPhysiqueTotal = new Attribute("effectif_physique_total",
					paramsGenerauxBean.getEffectifPhysiqueAnN() + "");

			effectifMoyen = new Attribute(
					"effectif_moyen",
					(paramsGenerauxBean.getEffectifMoyenAnN() != null) ? paramsGenerauxBean
							.getEffectifMoyenAnN() + ""
							: 0 + "");

			effectifETP = new Attribute(
					"effectif_ETP",
					(paramsGenerauxBean.getEffectifEtpAnN() != null) ? paramsGenerauxBean
							.getEffectifEtpAnN() + ""
							: 0 + "");

			masseSalariale = new Attribute(
					"masse_salariale",
					(paramsGenerauxBean.getMasseSalarialeAnN() != null) ? paramsGenerauxBean
							.getMasseSalarialeAnN() + ""
							: 0 + "");

			montantContributionFormation = new Attribute(
					"montant_contribution_formation",
					(paramsGenerauxBean.getPourcentageFormationAnN() != null) ? paramsGenerauxBean
							.getPourcentageFormationAnN() + ""
							: 0 + "");
		} else {
			effectifPhysiqueTotal = new Attribute("effectif_physique_total",
					0 + "");

			effectifMoyen = new Attribute("effectif_moyen", 0 + "");

			effectifETP = new Attribute("effectif_ETP", 0 + "");

			masseSalariale = new Attribute("masse_salariale", 0 + "");

			montantContributionFormation = new Attribute(
					"montant_contribution_formation", 0 + "");
		}
		racine.setAttribute(effectifMoyen);
		racine.setAttribute(effectifPhysiqueTotal);
		racine.setAttribute(effectifETP);
		racine.setAttribute(masseSalariale);
		racine.setAttribute(montantContributionFormation);

		Attribute codeApe = new Attribute("code_ape", entrepBean.getCodeApe());
		racine.setAttribute(codeApe);

		Date dareCreateD = entrepBean.getDateCreation();
		if(dareCreateD != null) {
			Attribute dateCreation = new Attribute("date_creation",
				dateFormat.format(dareCreateD));
			racine.setAttribute(dateCreation);
		}

		Attribute cciRattachement = new Attribute("cci_rattachement",
				entrepBean.getCciRattachement() != null ? entrepBean
						.getCciRattachement() : "");
		racine.setAttribute(cciRattachement);

		Attribute suiviFormations = new Attribute("suivi_des_formations",
				entrepBean.isSuiviFormations() == true ? "oui" : "non");
		racine.setAttribute(suiviFormations);

		Attribute suiviAccidents = new Attribute("suivi_des_accidents",
				entrepBean.isSuiviAccidents() == true ? "oui" : "non");
		racine.setAttribute(suiviAccidents);

		Attribute suiviAbsences = new Attribute("suivi_des_absences",
				entrepBean.isSuiviAbsences() == true ? "oui" : "non");
		racine.setAttribute(suiviAbsences);

		Attribute suiviCompetences = new Attribute("suivi_des_competences",
				entrepBean.isSuiviCompetences() == true ? "oui" : "non");
		racine.setAttribute(suiviCompetences);

		Attribute suiviGestionDIF = new Attribute("suivi_gestion_DIF",
				entrepBean.isSuiviDIF() == true ? "oui" : "non");
		racine.setAttribute(suiviGestionDIF);

		// SALARIES
		SalarieServiceImpl service = new SalarieServiceImpl();
		List<SalarieBeanExport> list = service
				.getSalarieBeanExportListByIdEntreprise(idEntrepriseSelected);
		for (int i = 0; i < list.size(); i++) {
			SalarieBean s = list.get(i);
			SalarieBeanExport se = list.get(i);
			Element salarie = new Element("salarie");
			racine.addContent(salarie);
			// Date de naissance
			Attribute naissance = new Attribute("date_de_naissance",
					dateFormat.format(s.getDateNaissance()));
			salarie.setAttribute(naissance);

			// Catégorie de compétences à acquérir
			FicheDePosteServiceImpl ficheDePosteService = new FicheDePosteServiceImpl();
			FicheDePosteBean ficheDePosteBean = ficheDePosteService
					.getFicheDePosteBeanByIdSalarie(s.getId());
			if (ficheDePosteBean != null) {
				Attribute categorieCompetence = new Attribute(
						"categorie_competence_a_acquerir1",
						ficheDePosteBean.getCategorieCompetence() != null ? ficheDePosteBean
								.getCategorieCompetence() : "");
				Attribute categorieCompetence2 = new Attribute(
						"categorie_competence_a_acquerir2",
						ficheDePosteBean.getCategorieCompetence2() != null ? ficheDePosteBean
								.getCategorieCompetence2() : "");
				Attribute categorieCompetence3 = new Attribute(
						"categorie_competence_a_acquerir3",
						ficheDePosteBean.getCategorieCompetence3() != null ? ficheDePosteBean
								.getCategorieCompetence3() : "");
				Attribute categorieCompetence4 = new Attribute(
						"categorie_competence_a_acquerir4",
						ficheDePosteBean.getCategorieCompetence4() != null ? ficheDePosteBean
								.getCategorieCompetence4() : "");
				Attribute categorieCompetence5 = new Attribute(
						"categorie_competence_a_acquerir5",
						ficheDePosteBean.getCategorieCompetence5() != null ? ficheDePosteBean
								.getCategorieCompetence5() : "");
				salarie.setAttribute(categorieCompetence);
				salarie.setAttribute(categorieCompetence2);
				salarie.setAttribute(categorieCompetence3);
				salarie.setAttribute(categorieCompetence4);
				salarie.setAttribute(categorieCompetence5);

			} else {
				Attribute categorieCompetence = new Attribute(
						"categorie_competence_a_acquerir1", "");
				Attribute categorieCompetence2 = new Attribute(
						"categorie_competence_a_acquerir2", "");
				Attribute categorieCompetence3 = new Attribute(
						"categorie_competence_a_acquerir3", "");
				Attribute categorieCompetence4 = new Attribute(
						"categorie_competence_a_acquerir4", "");
				Attribute categorieCompetence5 = new Attribute(
						"categorie_competence_a_acquerir5", "");
				salarie.setAttribute(categorieCompetence);
				salarie.setAttribute(categorieCompetence2);
				salarie.setAttribute(categorieCompetence3);
				salarie.setAttribute(categorieCompetence4);
				salarie.setAttribute(categorieCompetence5);
			}

			// Age
			Attribute age = new Attribute("age", calculer(s.getDateNaissance())
					+ "");
			salarie.setAttribute(age);
			// Sexe
			Attribute sexe = new Attribute("sexe", se.getSexe());
			salarie.setAttribute(sexe);
			// Civilite
			Attribute civ = new Attribute("civilite", s.getCivilite());
			salarie.setAttribute(civ);
			// Entree dans l'entreprise
			Attribute entree = new Attribute("date_entree_entreprise",
					getDateEntree(s) + "");
			salarie.setAttribute(entree);
			// Sortie de l'entreprise
			Attribute sortie = new Attribute("date_sortie_entreprise",
					getDateSortie(s) + "");
			salarie.setAttribute(sortie);
			// Présent dans l'entreprise
			Attribute present = new Attribute("present_dans_entreprise",
					s.isPresent() ? "P" : "S" + "");
			salarie.setAttribute(present);
			// Nombre heure formation
			Attribute heureFormation = new Attribute("nombre_h_formation",
					nbreHeureFormation(s) + "");
			salarie.setAttribute(heureFormation);
			if (s.getCreditDif() == null) {
				// Solde DIF
				Attribute soldeDif = new Attribute("solde_DIF", "");
				salarie.setAttribute(soldeDif);
			} else {
				// Solde DIF
				Attribute soldeDif = new Attribute("solde_DIF",
						s.getCreditDif() + "");
				salarie.setAttribute(soldeDif);
			}

			// Niveau initial
			Attribute nivInit = new Attribute("niveau_initial_formation",
					s.getNivFormationInit());
			salarie.setAttribute(nivInit);

			// nbre heure formation annee N
			Attribute nbHeureN = new Attribute("nombre_heure_formation_anneeN",
					nbreHeureFormationCurYear(s) + "");
			salarie.setAttribute(nbHeureN);

			// nbre heure formation annee N -1
			Attribute nbHeureNMoinsUn = new Attribute(
					"nombre_heure_formation_anneeN_moins_un",
					nbreHeureFormationLastYear(s) + "");
			salarie.setAttribute(nbHeureNMoinsUn);

			if (ficheDePosteBean != null) {
				Attribute competenceNouvelle = new Attribute(
						"competence_a_acquerir1",
						ficheDePosteBean.getCompetencesNouvelles() != null ? ficheDePosteBean
								.getCompetencesNouvelles() : "");
				Attribute competenceNouvelle2 = new Attribute(
						"competence_a_acquerir2",
						ficheDePosteBean.getCompetencesNouvelles2() != null ? ficheDePosteBean
								.getCompetencesNouvelles2() : "");
				Attribute competenceNouvelle3 = new Attribute(
						"competence_a_acquerir3",
						ficheDePosteBean.getCompetencesNouvelles3() != null ? ficheDePosteBean
								.getCompetencesNouvelles3() : "");
				Attribute competenceNouvelle4 = new Attribute(
						"competence_a_acquerir4",
						ficheDePosteBean.getCompetencesNouvelles4() != null ? ficheDePosteBean
								.getCompetencesNouvelles4() : "");
				Attribute competenceNouvelle5 = new Attribute(
						"competence_a_acquerir5",
						ficheDePosteBean.getCompetencesNouvelles5() != null ? ficheDePosteBean
								.getCompetencesNouvelles5() : "");
				salarie.setAttribute(competenceNouvelle);
				salarie.setAttribute(competenceNouvelle2);
				salarie.setAttribute(competenceNouvelle3);
				salarie.setAttribute(competenceNouvelle4);
				salarie.setAttribute(competenceNouvelle5);

			} else {
				Attribute competenceNouvelle = new Attribute(
						"competence_a_acquerir1", "");
				Attribute competenceNouvelle2 = new Attribute(
						"competence_a_acquerir2", "");
				Attribute competenceNouvelle3 = new Attribute(
						"competence_a_acquerir3", "");
				Attribute competenceNouvelle4 = new Attribute(
						"competence_a_acquerir4", "");
				Attribute competenceNouvelle5 = new Attribute(
						"competence_a_acquerir5", "");
				salarie.setAttribute(competenceNouvelle);
				salarie.setAttribute(competenceNouvelle2);
				salarie.setAttribute(competenceNouvelle3);
				salarie.setAttribute(competenceNouvelle4);
				salarie.setAttribute(competenceNouvelle5);
			}

			// POSTES OCCUPES
			//  Métier
			//  Catégorie socio-professionnelle
			//  Type de contrat : Initial ou Avenant
			//  Equivalent temps-plein
			//  Coefficient
			//  Niveau
			//  Echelon
			//  Dates de début et de fin du contrat de travail
			//  Dates de début et de fin de prise de fonction
			//  Motif de rupture
			//  Raison du recours à l’intérim

			ParcoursServiceImpl par = new ParcoursServiceImpl();
			List<ParcoursBean> listParcours = par
					.getParcoursBeanListByIdSalarie(s.getId().intValue());
			for (ParcoursBean p : listParcours) {
				Element parcours = new Element("poste_occupe");
				Attribute atr = new Attribute("metier", p.getNomMetier());
				parcours.setAttribute(atr);

				atr = new Attribute("categorie_Sociaux_Professionnnelle",
						p.getNomTypeStatut());
				parcours.setAttribute(atr);

				atr = new Attribute("type_contrat", p.getNomTypeContrat());
				parcours.setAttribute(atr);

				atr = new Attribute("equivalent_temps_plein",
						p.getEquivalenceTempsPlein() + "");
				parcours.setAttribute(atr);

				atr = new Attribute("coefficient", p.getCoefficient() + "");
				parcours.setAttribute(atr);

				atr = new Attribute("niveau", p.getNiveau() + "");
				parcours.setAttribute(atr);

				atr = new Attribute("echelon", p.getEchelon() + "");
				parcours.setAttribute(atr);

				atr = new Attribute("debut_contrat", dateFormat.format(p
						.getDebutFonction()));
				parcours.setAttribute(atr);

				atr = new Attribute("fin_contrat",
						(p.getFinFonction() != null) ? dateFormat.format(p
								.getFinFonction()) : "");
				parcours.setAttribute(atr);

				TypeRecoursInterimServiceImpl tri = new TypeRecoursInterimServiceImpl();
				if (p.getIdTypeRecoursSelected() != null
						&& p.getIdTypeRecoursSelected() != 0) {
					atr = new Attribute("raison_recours_interim", tri
							.getTypeRecoursInterimBeanById(
									p.getIdTypeRecoursSelected()).getNom());
					parcours.setAttribute(atr);
				} else {
					atr = new Attribute("raison_recours_interim", "");
					parcours.setAttribute(atr);
				}

				salarie.addContent(parcours);
			}

			// FORMATIONS
			//  Intitulé
			//  Volume en heures
			//  Coût de la formation ventilé en :
			// • OPCA
			// • Entreprise
			// • Autre
			//  Volume en heures hors DIF
			//  Volume en heures hors DIF (on ne passe plus l’info actuelle
			// Oui/Non sur le DIF)
			//  Titre obtenu
			//  Domaine de formation
			//  Organisme de formation
			//  Date de début et de fin de formation
			//  Nombre de jours ouvrés

			FormationServiceImpl serv = new FormationServiceImpl();
			List<FormationBean> listFormation = serv
					.getFormationsOfSalarieList(s.getId().intValue());
			for (FormationBean fb : listFormation) {
				Element formation = new Element("formation");
				Attribute atr = new Attribute("nom", fb.getNomFormation());
				formation.setAttribute(atr);

				atr = new Attribute("volume_en_heure", fb.getVolumeHoraire()
						+ "");
				formation.setAttribute(atr);

				atr = new Attribute("cout_OPCA", fb.getCoutOpcaDisplay() + "");
				formation.setAttribute(atr);

				atr = new Attribute("cout_entreprise",
						fb.getCoutEntrepriseDisplay() + "");
				formation.setAttribute(atr);

				atr = new Attribute("cout_autre", fb.getCoutAutreDisplay() + "");
				formation.setAttribute(atr);

				atr = new Attribute("volume_horairer_hors_dif", fb.getHorsDif()
						+ "");
				formation.setAttribute(atr);

				atr = new Attribute("nature_titre_obtenu", fb.getMode());
				formation.setAttribute(atr);

				atr = new Attribute("domaine_formation",
						fb.getIdDomaineFormationBeanSelected() + "");
				formation.setAttribute(atr);

				atr = new Attribute("organisme", fb.getOrganismeFormation());
				formation.setAttribute(atr);

				atr = new Attribute("date_debut", dateFormat.format(fb
						.getDebutFormation()));
				formation.setAttribute(atr);

				atr = new Attribute("date_fin", dateFormat.format(fb
						.getFinFormation()));
				formation.setAttribute(atr);

				atr = new Attribute("nombre_jours_ouvres",
						fb.getNombreJourOuvre() + "");
				formation.setAttribute(atr);

				salarie.addContent(formation);
			}

			// ARRET DE TRAVAIL
			//  Type d’accident
			//  Nature de l’accident : Initial ou Rechute
			//  Date de l’accident initial
			//  Date de la rechute
			//  Cause de l’accident
			//  Siège de la lésion initiale
			//  Siège de la lésion en rechute
			//  Aggravation/Nouvelle lésion
			//  Début de l’absence
			//  Fin de l’absence
			//  Nombre de jours d’arrêt initial
			//  Nombre de jours d’arrêt en rechute

			AccidentServiceImpl accidentServ = new AccidentServiceImpl();
			List<AccidentBean> listAccident = accidentServ
					.getAccidentsOfSalarie(s);
			for (AccidentBean acc : listAccident) {
				Element absence = new Element("ArretTravail");

				Attribute atr = new Attribute("date_arret_initial",
						dateFormat.format(acc.getDateAccident()));
				absence.setAttribute(atr);

				atr = new Attribute("date_arret_rechute",
						(acc.isInitial()) ? "" : dateFormat.format(acc
								.getDateRechute()));
				absence.setAttribute(atr);

				atr = new Attribute("nbr_jour_arret", acc.getNombreJourArret()
						+ "");
				absence.setAttribute(atr);

				atr = new Attribute("nbr_jour_arret_rechute",
						(acc.isInitial()) ? "" : acc
								.getNombreJourArretRechute() + "");
				absence.setAttribute(atr);

				atr = new Attribute("type_arret",
						acc.getIdTypeAccidentBeanSelected() + "");
				absence.setAttribute(atr);

				atr = new Attribute("cause_arret",
						acc.getNomTypeCauseAccident() + "");
				absence.setAttribute(atr);

				atr = new Attribute("lesion_initiale_arret",
						acc.getNomTypeLesion() + "");
				absence.setAttribute(atr);

				atr = new Attribute("lesion_rechute_arret",
						(acc.isInitial()) ? "" : acc.getNomTypeLesionRechute()
								+ "");
				absence.setAttribute(atr);

				if (!acc.isInitial()) {
					atr = new Attribute("nature_lesion_arret",
							(acc.isAggravation()) ? "Aggravation"
									: "Nouvelle lesion" + "");
					absence.setAttribute(atr);
				} else {
					atr = new Attribute("nature_lesion_arret", "");
					absence.setAttribute(atr);
				}

				atr = new Attribute("nature_arret",
						(acc.isInitial()) ? "Initial" : "Rechute");
				absence.setAttribute(atr);

				if (acc.getIdAbsence() != -1 && acc.getIdAbsence() != 0) {
					AbsenceServiceImpl abs = new AbsenceServiceImpl();
					AbsenceBean a = abs.getAbsenceBeanById(acc.getIdAbsence());
					atr = new Attribute("date_debut_absence",
							(a.getDebutAbsence() != null) ? dateFormat.format(a
									.getDebutAbsence()) : "");
					absence.setAttribute(atr);

					atr = new Attribute("date_fin_absence",
							(a.getFinAbsence() != null) ? dateFormat.format(a
									.getFinAbsence()) : "");
					absence.setAttribute(atr);
				} else {
					atr = new Attribute("date_debut_absence", "");
					absence.setAttribute(atr);

					atr = new Attribute("date_fin_absence", "");
					absence.setAttribute(atr);
				}

				salarie.addContent(absence);
			}
			if (salarie.getContent().size() == 0) {
				salarie.addContent("Aucun");
			}

		}
		Element referenciel = new Element("Referentiel");

		// Domaine formation
		Element domaineFormation = new Element("DomaineFormation");
		DomaineFormationServiceImpl domaineServ = new DomaineFormationServiceImpl();
		List<DomaineFormationBean> listDomaine = domaineServ
				.getDomaineFormationsList();
		for (DomaineFormationBean domaine : listDomaine) {
			Element dom = new Element("Domaine");
			Attribute atr = new Attribute("id", domaine.getId() + "");
			dom.setAttribute(atr);
			atr = new Attribute("nom", domaine.getNom());
			dom.setAttribute(atr);
			domaineFormation.addContent(dom);
		}

		// Famille metier
		Element familleMetier = new Element("FamilleMetier");
		FamilleMetierServiceImpl familleMetierServ = new FamilleMetierServiceImpl();
		List<FamilleMetierBean> listFamilleMetier = familleMetierServ
				.getFamilleMetiersList();
		for (FamilleMetierBean familleMetierBean : listFamilleMetier) {
			Element fam = new Element("Famille");
			Attribute atr = new Attribute("id", familleMetierBean.getId() + "");
			fam.setAttribute(atr);
			atr = new Attribute("nom", familleMetierBean.getNom());
			fam.setAttribute(atr);
			familleMetier.addContent(fam);
		}

		// Type Absence
		Element typeAbsence = new Element("TypeAbsence");
		TypeAbsenceServiceImpl typeAbsenceServ = new TypeAbsenceServiceImpl();
		List<TypeAbsenceBean> listTypeAbsence = typeAbsenceServ
				.getTypeAbsenceList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		for (TypeAbsenceBean typeAbsenceBean : listTypeAbsence) {
			Element tyab = new Element("Type");
			Attribute atr = new Attribute("id", typeAbsenceBean.getId() + "");
			tyab.setAttribute(atr);
			atr = new Attribute("nom", typeAbsenceBean.getNom());
			tyab.setAttribute(atr);
			typeAbsence.addContent(tyab);
		}

		// Type Accident
		Element typeAccident = new Element("TypeAccident");
		TypeAccidentServiceImpl typeAccidentServ = new TypeAccidentServiceImpl();
		List<TypeAccidentBean> listTypeAccident = typeAccidentServ
				.getTypeAccidentList();
		for (TypeAccidentBean typeAccidentBean : listTypeAccident) {
			Element tyacc = new Element("Type");
			Attribute atr = new Attribute("id", typeAccidentBean.getId() + "");
			tyacc.setAttribute(atr);
			atr = new Attribute("nom", typeAccidentBean.getNom());
			tyacc.setAttribute(atr);
			typeAccident.addContent(tyacc);
		}

		// Type Contrat
		Element typeContrat = new Element("TypeContrat");
		TypeContratServiceImpl typeContratServ = new TypeContratServiceImpl();
		List<TypeContratBean> listTypeContrat = typeContratServ
				.getTypeContratList();
		for (TypeContratBean typeContratBean : listTypeContrat) {
			Element tyc = new Element("Type");
			Attribute atr = new Attribute("id", typeContratBean.getId() + "");
			tyc.setAttribute(atr);
			atr = new Attribute("nom", typeContratBean.getNom());
			tyc.setAttribute(atr);
			typeContrat.addContent(tyc);
		}

		// Catégorie sociaux pro
		Element statut = new Element("CategorieSociauxProfessionnel");
		StatutServiceImpl statutServ = new StatutServiceImpl();
		List<StatutBean> liststatut = statutServ.getStatutsList();
		for (StatutBean statutBean : liststatut) {
			Element stat = new Element("Categorie");
			Attribute atr = new Attribute("id", statutBean.getId() + "");
			stat.setAttribute(atr);
			atr = new Attribute("nom", statutBean.getNom());
			stat.setAttribute(atr);
			statut.addContent(stat);
		}

		// Metier
		Element metier = new Element("Metiers");
		MetierServiceImpl metierServ = new MetierServiceImpl();
		List<MetierBean> listmetier = metierServ.getMetiersList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		for (MetierBean metierBean : listmetier) {
			Element met = new Element("Metier");
			Attribute atr = new Attribute("id", metierBean.getId() + "");
			met.setAttribute(atr);
			atr = new Attribute("nom", metierBean.getNom());
			met.setAttribute(atr);
			atr = new Attribute("famille", metierBean.getNomFamilleMetier());
			met.setAttribute(atr);

			if (metierBean.getDifficultes() != null
					&& !metierBean.getDifficultes().isEmpty()) {
				String[] dif = metierBean.getDifficultes().split(";");
				if (dif.length > 0) {
					String[] difficulte = { "", "",
							"Expérience professionnelle insuffisante",
							"Niveau de formation insuffisant",
							"Niveau de qualification insuffisant",
							"Manque de motivation", "Pénurie de candidat",
							"Savoir-être comportemental", "",
							"Attractivité du métier", "Contraintes du métier",
							"Inadéquation emploi/formation",
							"Métier en évolution technologique",
							"Pénibilité du métier", "Spécificité du métier",
							"", "Conditions de travail",
							"Nature du contrat de travail",
							"Niveau de rémunération" };
					for (int i = 0; i < dif.length; i++) {
						atr = new Attribute(
								"Difficulte_recrutement_" + (i + 1),
								difficulte[Integer.valueOf(dif[i])]);
						met.setAttribute(atr);
					}
				}
			}

			metier.addContent(met);
		}

		referenciel.addContent(metier);
		referenciel.addContent(statut);
		referenciel.addContent(typeContrat);
		referenciel.addContent(typeAccident);
		referenciel.addContent(typeAbsence);
		referenciel.addContent(familleMetier);
		referenciel.addContent(domaineFormation);
		racine.addContent(referenciel);
	}

	public void send() throws Exception {
		try {
			init();
			enregistre();
			ResourceBundle rb = ResourceBundle.getBundle("ftp");
			String host = rb.getString("host");
			String id = rb.getString("id");
			String password = rb.getString("password");
			String port = rb.getString("port");
			String salariePath = rb.getString("salarieFile");
			Ftp ftp = new Ftp();
			ftp.connexion(host, id, password, Integer.parseInt(port));
			ftp.upload(xml, salariePath);
			ftp.deconnexion();
			reussite = true;
			xml.delete();
		} catch (Exception e) {
			echec = true;
			LOGGER.error("Error during send", e);
		}
	}

	public boolean isEchec() {
		return echec;
	}

	public void setEchec(boolean echec) {
		this.echec = echec;
	}

	public void copyFile(final String currentFile, final String newFile)
			throws FileNotFoundException, IOException {
		FileInputStream in = new FileInputStream(currentFile);
		try {
			FileOutputStream out = new FileOutputStream(newFile);
			byte[] buffer = new byte[1024 * 4];
			// ca peut être bien de rendre la taille du buffer paramétrable
			int nbRead;
			try {
				while ((nbRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, nbRead);
				}
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}

	public void enregistre() throws Exception {
		document = new Document(racine);
		try {
			Format f = Format.getPrettyFormat();
			f.setEncoding("UTF-8");
			XMLOutputter sortie = new XMLOutputter(f);
			EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
			EntrepriseBean bean = serv
					.getEntrepriseBeanById(idEntrepriseSelected);
			String nom = bean.getNumSiret() + ""; /*+ ".xml"*/
			xml = createTempFile(nom, ".xml");
			sortie.output(document, new FileOutputStream(xml));
		} catch (java.io.IOException e) {
		}
	}
	
	public File createTempFile(String prefix, String suffix){
	     String tempDir = System.getProperty("java.io.tmpdir");
	     String fileName = (prefix != null ? prefix : "" ) + /*System.nanoTime() +*/ (suffix != null ? suffix : "" ) ;
	     return new File(tempDir, fileName);
	}

	public int getIdEntrepriseSelected() {
		return idEntrepriseSelected;
	}

	public void setIdEntrepriseSelected(int idEntreprise) {
		idEntrepriseSelected = idEntreprise;
	}

	public int getNbSalarie() {
		return nbSalarie;
	}

	public void setNbSalarie(int nbSalarie) {
		this.nbSalarie = nbSalarie;
	}
}
