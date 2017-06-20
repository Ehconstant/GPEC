package com.cci.gpec.web.backingBean.filtre;

import java.text.SimpleDateFormat;
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

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.ContratTravailBean;
import com.cci.gpec.commons.DomaineFormationBean;
import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.FicheDePosteBean;
import com.cci.gpec.commons.FicheMetierBean;
import com.cci.gpec.commons.FicheMetierEntrepriseBean;
import com.cci.gpec.commons.Filtre;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.MotifRuptureContratBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.RemunerationBean;
import com.cci.gpec.commons.SalarieBeanExport;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.commons.StatutBean;
import com.cci.gpec.commons.TypeAbsenceBean;
import com.cci.gpec.commons.TypeAccidentBean;
import com.cci.gpec.commons.TypeCauseAccidentBean;
import com.cci.gpec.commons.TypeContratBean;
import com.cci.gpec.commons.TypeHabilitationBean;
import com.cci.gpec.commons.TypeLesionBean;
import com.cci.gpec.commons.TypeRecoursInterimBean;
import com.cci.gpec.db.Entretien;
import com.cci.gpec.db.FicheMetier;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.connection.HibernateUtil;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.AccidentServiceImpl;
import com.cci.gpec.metier.implementation.ContratTravailServiceImpl;
import com.cci.gpec.metier.implementation.DomaineFormationServiceImpl;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.EntretienServiceImpl;
import com.cci.gpec.metier.implementation.FicheDePosteServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierEntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierServiceImpl;
import com.cci.gpec.metier.implementation.FormationServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.HabilitationServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.MotifRuptureContratServiceImpl;
import com.cci.gpec.metier.implementation.ParcoursServiceImpl;
import com.cci.gpec.metier.implementation.PersonneAChargeServiceImpl;
import com.cci.gpec.metier.implementation.RemunerationServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.cci.gpec.metier.implementation.TypeAbsenceServiceImpl;
import com.cci.gpec.metier.implementation.TypeAccidentServiceImpl;
import com.cci.gpec.metier.implementation.TypeCauseAccidentServiceImpl;
import com.cci.gpec.metier.implementation.TypeContratServiceImpl;
import com.cci.gpec.metier.implementation.TypeHabilitationServiceImpl;
import com.cci.gpec.metier.implementation.TypeLesionServiceImpl;
import com.cci.gpec.metier.implementation.TypeRecoursInterimServiceImpl;
import com.cci.gpec.web.backingBean.Admin.AdminFormBB;
import com.cci.gpec.web.backingBean.excel.ExcelBB;
import com.icesoft.faces.context.ByteArrayResource;
import com.icesoft.faces.context.effects.JavascriptContext;

public class FiltreMultiBB {
	
	private static final Logger	LOGGER = LoggerFactory.getLogger(FiltreMultiBB.class);

	private String[] selectedEnfant;
	private String[] selectedPermisConduire;
	private String[] selectedSexe;
	private String[] selectedNature;
	private String[] selectedTypeFinancement;
	private String typeAbsET;
	private String typeAccET;
	private String typeCauseAccET;
	private String typeLesionET;
	private String typeFormationET;
	private String typeHabET;
	private String formationET;
	private String financementET;
	private String metierET;
	private Integer debutAge;
	private Integer finAge;
	private Integer debutAnciennete;
	private Integer finAnciennete;
	private Date datePeremption;

	private Date debutPeriode;
	private Date finPeriode;

	private GregorianCalendar debutExtraction = new GregorianCalendar();
	private GregorianCalendar finExtraction = new GregorianCalendar();

	private String motCleCompetences;
	private String motCleCompetencesAAmeliorer;
	private String motCleFormationSouhaitee;
	private String motCleEvolutionSouhaitee;

	private Integer idEntrepriseSelected;
	private Integer[] idServicesSelected;
	private Integer[] idMetiersSelected;
	private Integer[] idMetiersSelectedFDP;
	private Integer[] idStatutsSelected;
	private Integer[] idContratsSelected;
	private String[] idSituationFamilleSelected;
	private Integer[] idTypeAccidentSelected;
	private Integer[] idCauseAccidentSelected;
	private Integer[] idTypeAbsenceSelected;
	private Integer[] idTypeLesionSelected;
	private Integer[] idTypeHabSelected;
	private Integer[] idFormationSelected;
	private Integer[] idDomaineFormationSelected;
	private String[] idCoefsSelected;
	private String[] idNiveauxSelected;
	private String[] idEchelonsSelected;
	private Integer[] idMotifsRuptureSelected;
	private Integer[] idMotifsRecoursSelected;

	private String[] idNiveauFormSelected;
	private List<SalarieBeanExport> salarieList;
	private ArrayList<SelectItem> entreprisesList;
	private ArrayList<SelectItem> servicesList;
	private ArrayList<SelectItem> metiersList;
	private ArrayList<SelectItem> metiersListFDP;
	private ArrayList<SelectItem> statutsList;
	private ArrayList<SelectItem> contratsList;
	private ArrayList<SelectItem> situationFamilleList;
	private ArrayList<SelectItem> typeAccidentList;
	private ArrayList<SelectItem> typeCauseAccidentList;
	private ArrayList<SelectItem> typeAbsenceList;
	private ArrayList<SelectItem> typeFormationList;
	private ArrayList<SelectItem> typeLesionList;
	private ArrayList<SelectItem> typeHabList;
	private ArrayList<SelectItem> formationList;
	private ArrayList<SelectItem> domaineFormationList;
	private ArrayList<SelectItem> coefsList;
	private ArrayList<SelectItem> niveauxList;
	private ArrayList<SelectItem> echelonsList;
	private ArrayList<SelectItem> motifsRuptureList;
	private ArrayList<SelectItem> motifsRecoursList;
	private ArrayList<Filtre> filtres;
	private ByteArrayResource imagen = new ByteArrayResource(null);
	private boolean habValide;
	private boolean useDIF;
	private ArrayList<SelectItem> monthList;
	private int idMonthSelected;
	private int idDureeSelected;
	private boolean retourSession;

	private boolean displayRecap = false;

	List<SalarieBeanExport> salarieBeanList = new ArrayList<SalarieBeanExport>();
	List<ParcoursBean> parcoursBeanList = new ArrayList<ParcoursBean>();
	List<ContratTravailBean> contratBeanList = new ArrayList<ContratTravailBean>();
	List<AbsenceBean> absenceBeanList = new ArrayList<AbsenceBean>();
	List<AccidentBean> accidentBeanList = new ArrayList<AccidentBean>();
	List<HabilitationBean> habilitationBeanList = new ArrayList<HabilitationBean>();
	List<FormationBean> formationBeanList = new ArrayList<FormationBean>();
	List<EntretienBean> entretienBeanList = new ArrayList<EntretienBean>();
	List<RemunerationBean> remunerationBeanList = new ArrayList<RemunerationBean>();

	private int nbSalaries = 0;
	private int nbParcours = 0;
	private int nbContratTravail = 0;
	private int nbAbsences = 0;
	private int nbAccidents = 0;
	private int nbHabilitations = 0;
	private int nbFormations = 0;
	private int nbEntretiens = 0;
	private int nbRemunerations = 0;

	// construction de la requete
	private List<String> joinTables;
	private List<String> whereAnd;
	private List<String> whereOr;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public static final String[] FRENCH_STOP_WORDS = { "a", "afin", "ai",
			"ainsi", "après", "attendu", "au", "aujourd", "auquel", "aussi",
			"autre", "autres", "aux", "auxquelles", "auxquels", "avait",
			"avant", "avec", "avoir", "c", "car", "ce", "ceci", "cela",
			"celle", "celles", "celui", "cependant", "certain", "certaine",
			"certaines", "certains", "ces", "cet", "cette", "ceux", "chez",
			"ci", "combien", "comme", "comment", "concernant", "contre", "d",
			"dans", "de", "debout", "dedans", "dehors", "delà", "depuis",
			"derrière", "des", "désormais", "desquelles", "desquels",
			"dessous", "dessus", "devant", "devers", "devra", "divers",
			"diverse", "diverses", "doit", "donc", "dont", "du", "duquel",
			"durant", "dès", "elle", "elles", "en", "entre", "environ", "est",
			"et", "etc", "etre", "eu", "eux", "excepté", "hormis", "hors",
			"hélas", "hui", "il", "ils", "j", "je", "jusqu", "jusque", "l",
			"la", "laquelle", "le", "lequel", "les", "lesquelles", "lesquels",
			"leur", "leurs", "lorsque", "lui", "là", "ma", "mais", "malgré",
			"me", "merci", "mes", "mien", "mienne", "miennes", "miens", "moi",
			"moins", "mon", "moyennant", "même", "mêmes", "n", "ne", "ni",
			"non", "nos", "notre", "nous", "néanmoins", "nôtre", "nôtres",
			"on", "ont", "ou", "outre", "où", "par", "parmi", "partant", "pas",
			"passé", "pendant", "plein", "plus", "plusieurs", "pour",
			"pourquoi", "proche", "près", "puisque", "qu", "quand", "que",
			"quel", "quelle", "quelles", "quels", "qui", "quoi", "quoique",
			"revoici", "revoilà", "s", "sa", "sans", "sauf", "se", "selon",
			"seront", "ses", "si", "sien", "sienne", "siennes", "siens",
			"sinon", "soi", "soit", "son", "sont", "sous", "suivant", "sur",
			"ta", "te", "tes", "tien", "tienne", "tiennes", "tiens", "toi",
			"ton", "tous", "tout", "toute", "toutes", "tu", "un", "une", "va",
			"vers", "voici", "voilà", "vos", "votre", "vous", "vu", "vôtre",
			"vôtres", "y", "à", "ça", "ès", "été", "être", "ô" };

	public FiltreMultiBB() throws Exception {
		initList();
	}

	public void displayRecap() throws Exception {
		rechercher();
		displayRecap = true;
	}

	public void closeRecap() {
		displayRecap = false;
	}

	public String rechercher() throws Exception {

		salarieList.clear();
		parcoursBeanList.clear();
		contratBeanList.clear();
		absenceBeanList.clear();
		accidentBeanList.clear();
		habilitationBeanList.clear();
		formationBeanList.clear();
		entretienBeanList.clear();
		remunerationBeanList.clear();

		filtres = new ArrayList<Filtre>();

		whereAnd = new ArrayList<String>();
		whereOr = new ArrayList<String>();
		joinTables = new ArrayList<String>();

		boolean joinEntretien = false;
		boolean joinFicheDePoste = false;
		boolean joinParcours = false;
		boolean needAnnee = false;

		if (idEntrepriseSelected != -1) {
			whereAnd.add("s.Entreprise=" + idEntrepriseSelected);
			Filtre filtre = new Filtre();
			filtre.setFiltre("Entreprise");
			EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
			String filtress = entServ.getEntrepriseBeanById(
					idEntrepriseSelected).getNom();
			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		if ((this.idServicesSelected != null)
				&& (idServicesSelected.length > 0)) {
			ServiceImpl serv = new ServiceImpl();
			Filtre filtre = new Filtre();
			filtre.setFiltre("Services");
			String filtress = "";
			String s = "s.IdService='" + idServicesSelected[0] + "'";
			filtress += serv.getServiceBeanById(idServicesSelected[0]).getNom()
					+ "; ";
			if (idServicesSelected.length > 1) {
				for (int i = 1; i < idServicesSelected.length; i++) {
					filtress += serv.getServiceBeanById(idServicesSelected[i])
							.getNom() + "; ";
					s += " or s.IdService='" + idServicesSelected[i] + "'";
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			filtre.setValue(filtress);
			filtres.add(filtre);
			whereAnd.add(s);
		}

		if (debutPeriode != null || finPeriode != null) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Période de recherche");
			String filtress = "";

			if (debutPeriode == null) {
				filtress += "Jusqu'au " + dateFormat.format(finPeriode);
			} else {
				if (finPeriode == null) {
					filtress += "Depuis le " + dateFormat.format(debutPeriode);
				} else {
					filtress += "Du " + dateFormat.format(debutPeriode)
							+ " au " + dateFormat.format(finPeriode);
				}
			}

			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		// salarie
		if (selectedSexe != null && selectedSexe.length > 0)
			if (selectedSexe.length == 1) {
				Filtre filtre = new Filtre();
				filtre.setFiltre("Sexe");
				String filtress = "";

				if (selectedSexe[0].equals("H")) {
					whereAnd.add("s.Civilite='Monsieur'");
					filtress = "Hommes";
				} else {
					whereAnd.add("(s.Civilite='Madame' or s.Civilite='Mademoiselle')");
					filtress = "Femmes";

				}
				filtre.setValue(filtress);
				filtres.add(filtre);
			}

		if ((this.idSituationFamilleSelected != null)
				&& (idSituationFamilleSelected.length > 0)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Situation familiale");
			String filtress = "";
			String s = "s.SituationFamiliale IN (:situFami)";
			filtress += idSituationFamilleSelected[0] + "; ";
			if (idSituationFamilleSelected.length > 1) {
				for (int i = 1; i < idSituationFamilleSelected.length; i++) {
					filtress += idSituationFamilleSelected[i] + "; ";
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			filtre.setValue(filtress);
			filtres.add(filtre);
			whereAnd.add(s);
		}

		if (selectedPermisConduire != null && selectedPermisConduire.length > 0)
			if (selectedPermisConduire.length == 1) {
				Filtre filtre = new Filtre();
				filtre.setFiltre("Permis de conduire");
				String filtress = "";
				if (selectedPermisConduire[0].equals("true")) {
					filtress = "Oui";
					whereAnd.add("s.PossedePermisConduire=1");
				} else {
					filtress = "Non";
					whereAnd.add("s.PossedePermisConduire=0");
				}
				filtre.setValue(filtress);
				filtres.add(filtre);
			}

		if ((this.idContratsSelected != null)
				&& (idContratsSelected.length > 0)) {
			joinParcours = true;
			Filtre filtre = new Filtre();
			filtre.setFiltre("Type de contrat");
			String filtress = "";

			String s = "";

			s = "p.TypeContrat.Id=" + idContratsSelected[0];
			TypeContratServiceImpl tcServ = new TypeContratServiceImpl();
			filtress = tcServ.getTypeContratBeanById(idContratsSelected[0])
					.getNom();
			for (int j = 1; j < idContratsSelected.length; j++) {
				s += " or p.TypeContrat.Id=" + idContratsSelected[j];
				filtress += "; "
						+ tcServ.getTypeContratBeanById(idContratsSelected[0])
								.getNom();
			}
			whereAnd.add(s);
			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		if ((this.idMetiersSelected != null) && (idMetiersSelected.length > 0)) {
			joinParcours = true;
			Filtre filtre = new Filtre();
			filtre.setFiltre("Métier");
			String filtress = "";

			String s = "";

			s = "p.Metier.Id=" + idMetiersSelected[0];
			MetierServiceImpl metierServ = new MetierServiceImpl();
			filtress = metierServ.getMetierBeanById(idMetiersSelected[0])
					.getNom();
			for (int j = 1; j < idMetiersSelected.length; j++) {
				s += " or p.Metier.Id=" + idMetiersSelected[j];
				filtress += "; "
						+ metierServ.getMetierBeanById(idMetiersSelected[0])
								.getNom();
			}
			whereAnd.add(s);
			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		if ((this.idStatutsSelected != null) && (idStatutsSelected.length > 0)) {
			joinParcours = true;
			Filtre filtre = new Filtre();
			filtre.setFiltre("CSP");
			String filtress = "";

			String s = "";

			s = "p.Statut.Id=" + idStatutsSelected[0];
			StatutServiceImpl cspServ = new StatutServiceImpl();
			filtress = cspServ.getStatutBeanById(idStatutsSelected[0]).getNom();
			for (int j = 1; j < idStatutsSelected.length; j++) {
				s += " or p.Statut.Id=" + idStatutsSelected[j];
				filtress += "; "
						+ cspServ.getStatutBeanById(idStatutsSelected[0])
								.getNom();
			}
			whereAnd.add(s);
			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		if ((this.idCoefsSelected != null) && (idCoefsSelected.length > 0)) {
			joinParcours = true;
			Filtre filtre = new Filtre();
			filtre.setFiltre("Coefficient");
			String filtress = "";

			String s = "";
			s = "p.Coefficient IN (:coefs)";
			filtress = idCoefsSelected[0];
			for (int j = 1; j < idCoefsSelected.length; j++) {
				filtress += "; " + idCoefsSelected[j];
			}
			whereAnd.add(s);
			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		if ((this.idNiveauxSelected != null) && (idNiveauxSelected.length > 0)) {
			joinParcours = true;
			Filtre filtre = new Filtre();
			filtre.setFiltre("Niveau");
			String filtress = "";

			String s = "";
			s = "p.Niveau IN (:niveaux)";
			filtress = idNiveauxSelected[0];
			for (int j = 1; j < idNiveauxSelected.length; j++) {
				filtress += "; " + idNiveauxSelected[j];
			}
			whereAnd.add(s);
			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		if ((this.idEchelonsSelected != null)
				&& (idEchelonsSelected.length > 0)) {
			joinParcours = true;
			Filtre filtre = new Filtre();
			filtre.setFiltre("Echelon");
			String filtress = "";

			String s = "";
			s = "p.Echelon IN (:echelons)";
			filtress = idEchelonsSelected[0];
			for (int j = 1; j < idEchelonsSelected.length; j++) {
				filtress += "; " + idEchelonsSelected[j];
			}
			whereAnd.add(s);
			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		if ((this.idMotifsRecoursSelected != null)
				&& (idMotifsRecoursSelected.length > 0)) {
			joinParcours = true;
			Filtre filtre = new Filtre();
			filtre.setFiltre("Motif de recours à l'intérim");
			String filtress = "";

			String s = "";
			TypeRecoursInterimServiceImpl triServ = new TypeRecoursInterimServiceImpl();
			filtress = triServ.getTypeRecoursInterimBeanById(
					idMotifsRecoursSelected[0]).getNom();
			s = "p.TypeRecours.Id=" + idMotifsRecoursSelected[0];
			for (int j = 1; j < idMotifsRecoursSelected.length; j++) {
				s += " or p.TypeRecours.Id=" + idMotifsRecoursSelected[j];
				filtress += "; " +  triServ.getTypeRecoursInterimBeanById(idMotifsRecoursSelected[j]).getNom();
			}
			whereAnd.add(s);
			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		// Competence - domaine formation
		if ((this.idDomaineFormationSelected != null)
				&& (idDomaineFormationSelected.length > 0)) {
			joinEntretien = true;
			Filtre filtre = new Filtre();
			filtre.setFiltre("Domaine de formation");
			String filtress = "";

			String s = "";
			DomaineFormationServiceImpl dfServ = new DomaineFormationServiceImpl();
			filtress = dfServ.getDomaineFormationBeanById(
					idDomaineFormationSelected[0]).getNom();
			s = " ( e.DomainesFormation=" + idDomaineFormationSelected[0]
					+ " or e.DomainesFormation2="
					+ idDomaineFormationSelected[0]
					+ " or e.DomainesFormation3="
					+ idDomaineFormationSelected[0]
					+ " or e.DomainesFormation4="
					+ idDomaineFormationSelected[0]
					+ " or e.DomainesFormation5="
					+ idDomaineFormationSelected[0];
			for (int j = 1; j < idDomaineFormationSelected.length; j++) {
				s += " or e.DomainesFormation=" + idDomaineFormationSelected[j]
						+ " or e.DomainesFormation2="
						+ idDomaineFormationSelected[j]
						+ " or e.DomainesFormation3="
						+ idDomaineFormationSelected[j]
						+ " or e.DomainesFormation4="
						+ idDomaineFormationSelected[j]
						+ " or e.DomainesFormation5="
						+ idDomaineFormationSelected[j];
				filtress += "; "
						+ dfServ.getDomaineFormationBeanById(
								idDomaineFormationSelected[j]).getNom();
			}
			s += " ) ";
			whereAnd.add(s);
			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		// recherche par mots clés
		if (motCleCompetencesAAmeliorer != null
				&& !motCleCompetencesAAmeliorer.equals("")) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Mots clés compétences à améliorer");
			String filtress = motCleCompetencesAAmeliorer;
			joinEntretien = true;
			joinFicheDePoste = true;
			if (motCleCompetencesAAmeliorer.contains("'")) {
				motCleCompetencesAAmeliorer = motCleCompetencesAAmeliorer
						.replaceAll("'", " ");
			}
			String[] mot = motCleCompetencesAAmeliorer.split(" ");
			List<String> mots = new ArrayList<String>();
			for (String s : mot) {
				boolean test = true;
				for (String s2 : FRENCH_STOP_WORDS) {
					if (s.equals(s2)) {
						test = false;
					}
				}
				if (test) {
					mots.add(s);
				}
			}

			if (mots.size() > 0) {
				String q = "";
				q = " ( e.Competence LIKE '%" + mots.get(0)
						+ "%' OR fdp.Commentaires LIKE '%" + mots.get(0) + "%'";
				for (int i = 1; i < mots.size(); i++) {
					q += " OR e.Competence LIKE '%" + mots.get(i)
							+ "%' OR fdp.Commentaires LIKE '%" + mots.get(i)
							+ "%' ";
				}
				q += ")";
				whereAnd.add(q);
			}
			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		if (motCleEvolutionSouhaitee != null
				&& !motCleEvolutionSouhaitee.equals("")) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Mots clés évolution souhaitée");
			String filtress = motCleEvolutionSouhaitee;
			joinEntretien = true;
			if (motCleEvolutionSouhaitee.contains("'")) {
				motCleEvolutionSouhaitee = motCleEvolutionSouhaitee.replaceAll(
						"'", " ");
			}
			String[] mot = motCleEvolutionSouhaitee.split(" ");
			List<String> mots = new ArrayList<String>();
			for (String s : mot) {
				boolean test = true;
				for (String s2 : FRENCH_STOP_WORDS) {
					if (s.equals(s2)) {
						test = false;
					}
				}
				if (test) {
					mots.add(s);
				}
			}

			if (mots.size() > 0) {
				String q = "";
				q = " ( e.PrincipaleConclusion LIKE '%" + mots.get(0) + "%' ";
				for (int i = 1; i < mots.size(); i++) {
					q += " OR e.PrincipaleConclusion LIKE '%" + mots.get(i)
							+ "%' ";
				}
				q += ")";
				whereAnd.add(q);
			}
			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		if (motCleFormationSouhaitee != null
				&& !motCleFormationSouhaitee.equals("")) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Mots clés formation souhaitée");
			String filtress = motCleFormationSouhaitee;
			joinEntretien = true;
			if (motCleFormationSouhaitee.contains("'")) {
				motCleFormationSouhaitee = motCleFormationSouhaitee.replaceAll(
						"'", " ");
			}
			String[] mot = motCleFormationSouhaitee.split(" ");
			List<String> mots = new ArrayList<String>();
			for (String s : mot) {
				boolean test = true;
				for (String s2 : FRENCH_STOP_WORDS) {
					if (s.equals(s2)) {
						test = false;
					}
				}
				if (test) {
					mots.add(s);
				}
			}

			if (mots.size() > 0) {
				String q = "";
				q = " ( e.Formations LIKE '%" + mots.get(0)
						+ "%' OR e.Formations2 LIKE '%" + mots.get(0)
						+ "%' OR e.Formations3 LIKE '%" + mots.get(0)
						+ "%' OR e.Formations4 LIKE '%" + mots.get(0)
						+ "%' OR e.Formations5 LIKE '%" + mots.get(0) + "%'";
				for (int i = 1; i < mots.size(); i++) {
					q += " OR e.Formations LIKE '%" + mots.get(i)
							+ "%' OR e.Formations2 LIKE '%" + mots.get(i)
							+ "%' OR e.Formations3 LIKE '%" + mots.get(i)
							+ "%' OR e.Formations4 LIKE '%" + mots.get(i)
							+ "%' OR e.Formations5 LIKE '%" + mots.get(i)
							+ "%'";
				}
				q += ")";
				whereAnd.add(q);
			}
			filtre.setValue(filtress);
			filtres.add(filtre);
		}

		String query = "from Salarie as s left join fetch s.Entreprise as e ";
		if (joinParcours) {
			query += "left join fetch s.PARCOURSs as p ";
		}

		if (joinEntretien) {
			query += "left join fetch s.ENTRETIENs as e ";
		}

		if (joinFicheDePoste) {
			query += "left join fetch s.FICHEDEPOSTEs as fdp ";
		}

		int i = 0;
		for (String s : joinTables) {
			query += joinTables.get(i) + " ";
			i++;
		}
		query += " where e.Groupe="
				+ Integer.parseInt(session.getAttribute("groupe").toString())
				+ " ";
		if (whereOr.size() > 0 || whereAnd.size() > 0) {
			query += "and ";
		}
		boolean first = true;
		boolean or = false;
		for (int j = 0; j < whereAnd.size(); j++) {
			if (j != 0 && j < whereAnd.size()) {
				query += " and ";
				first = false;
			}
			query += whereAnd.get(j);
		}
		for (int j = 0; j < whereOr.size(); j++) {
			if (j == 0 && !first) {
				query += " and (";
				or = true;
			}
			if (j != 0 && j < whereOr.size()) {
				query += " or ";
			}
			query += whereOr.get(j);
		}
		if (or) {
			query += ")";
		}

		if (joinParcours) {

			if (debutPeriode != null) {
				needAnnee = true;
				if (finPeriode != null) {
					query += " and ((p.DebutFonction between :debut and :fin) "
							+ "or (p.FinFonction between :debut and :fin) "
							+ "or (p.DebutFonction <= :debut and p.FinFonction >= :fin))";
				} else {
					query += " and (p.DebutFonction >= :debut or p.FinFonction >= :debut)";
				}
			} else {
				if (finPeriode != null) {
					needAnnee = true;
					query += " and (p.FinFonction <= :fin or p.DebutFonction <= :fin)";
				}
			}
		}
		if (joinEntretien) {
			if (debutPeriode != null) {
				needAnnee = true;
				if (finPeriode != null) {
					query += " and e.AnneeReference between :anneeDebut and :anneeFin";
				} else {
					query += " and e.AnneeReference >= :anneeDebut";
				}
			} else {
				if (finPeriode != null) {
					needAnnee = true;
					query += " and e.AnneeReference <= :anneeFin";
				}
			}
		}
		// query += " group by s.Id";

		SalarieServiceImpl salServ = new SalarieServiceImpl();

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			Query queryH = session.createQuery(query);
			if ((this.idSituationFamilleSelected != null)
					&& (idSituationFamilleSelected.length > 0)) {
				queryH.setParameterList("situFami", idSituationFamilleSelected);
			}
			if ((this.idCoefsSelected != null) && (idCoefsSelected.length > 0)) {
				queryH.setParameterList("coefs", idCoefsSelected);
			}
			if ((this.idNiveauxSelected != null)
					&& (idNiveauxSelected.length > 0)) {
				queryH.setParameterList("niveaux", idNiveauxSelected);
			}
			if ((this.idEchelonsSelected != null)
					&& (idEchelonsSelected.length > 0)) {
				queryH.setParameterList("echelons", idEchelonsSelected);
			}

			if (debutPeriode != null && queryH.toString().contains(":debut")) {
				queryH.setParameter("debut", debutPeriode);
			}
			if (finPeriode != null && queryH.toString().contains(":fin")) {
				queryH.setParameter("fin", finPeriode);
			}
			if (needAnnee) {
				if (debutPeriode != null
						&& queryH.toString().contains(":anneeDebut")) {
					debutExtraction.setTime(debutPeriode);
					int anneedebut = debutExtraction.get(Calendar.YEAR);
					queryH.setParameter("anneeDebut", anneedebut);
				}
				if (finPeriode != null
						&& queryH.toString().contains(":anneeFin")) {
					finExtraction.setTime(finPeriode);
					int anneeFin = debutExtraction.get(Calendar.YEAR);
					queryH.setParameter("anneeFin", anneeFin);
				}
			}

			List<Salarie> salarieInventory = queryH.list();

			salarieList = salServ
					.salariePersistantListToSalarieBeanExportList(salarieInventory);
			transaction.commit();

		} catch (HibernateException e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		keepActualEmploye();

		ContratTravailServiceImpl ctServ = new ContratTravailServiceImpl();

		List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
		RemunerationServiceImpl remuServ = new RemunerationServiceImpl();
		for (SalarieBeanExport s : salarieList) {
			if (!containSalarie(list, s)) {
				list.add(s);
				remunerationBeanList.addAll(remuServ
						.getRemunerationByIdSalarie(s.getId()));
				parcoursBeanList.addAll(s.getParcoursBeanList());
				contratBeanList.addAll(ctServ
						.getContratTravailBeanListByIdSalarie(s.getId()));
				absenceBeanList.addAll(s.getAbsenceBeanList());
				accidentBeanList.addAll(s.getAccidentBeanList());
				habilitationBeanList.addAll(s.getHabilitationBeanList());
				formationBeanList.addAll(s.getFormationBeanList());
				entretienBeanList.addAll(s.getEntretienBeanList());
			}
		}

		salarieList = list;

		// salarie
		initSalarieFromEnfant();
		initSalarieFromAge();
		initSalarieFromAnciennete();

		// CT + PP
		initSalarieFromMotifRupture();

		// absence
		initSalarieFromTypeAbsence();

		// accident
		initSalarieFromNatureAccident();
		initSalarieFromTypeAccident();
		initSalarieFromCauseAccident();
		initSalarieFromLesion();

		// habilitation + formation
		initSalarieFromHabilitation();

		initSalarieFromFormation();

		// compétence
		initSalarieFromMetiersFDP();
		rechercheParMotCleCompetences();

		// filtres les listes d'objets associés aux salaries
		if (motCleCompetencesAAmeliorer != null
				&& !motCleCompetencesAAmeliorer.equals("")) {
			verifEntretienContientMotCle();
		}
		filterListesObjets(salarieList);
		filtrerListesObjetsAvecPeriode();

		Collections.sort(salarieList);

		return "retourSalarie";
	}

	public void filtrerListesObjetsAvecPeriode() throws Exception {

		List<ParcoursBean> parcoursBeanListTemp = new ArrayList<ParcoursBean>();
		List<ContratTravailBean> contratBeanListTemp = new ArrayList<ContratTravailBean>();
		List<AbsenceBean> absenceBeanListTemp = new ArrayList<AbsenceBean>();
		List<AccidentBean> accidentBeanListTemp = new ArrayList<AccidentBean>();
		List<HabilitationBean> habilitationBeanListTemp = new ArrayList<HabilitationBean>();
		List<FormationBean> formationBeanListTemp = new ArrayList<FormationBean>();
		List<EntretienBean> entretienBeanListTemp = new ArrayList<EntretienBean>();
		List<RemunerationBean> remunerationBeanListTemp = new ArrayList<RemunerationBean>();

		if (debutPeriode != null || finPeriode != null) {

			if (debutPeriode != null) {
				debutExtraction.setTime(debutPeriode);
			} else {
				debutExtraction = null;
			}
			if (finPeriode != null) {
				finExtraction.setTime(finPeriode);
			} else {
				finExtraction = null;
			}

			for (ParcoursBean p : parcoursBeanList) {

				GregorianCalendar debD1 = new GregorianCalendar();
				debD1.setTime(p.getDebutFonction());
				GregorianCalendar finD1 = new GregorianCalendar();
				if (p.getFinFonction() != null) {
					finD1.setTime(p.getFinFonction());

					if (isInPeriode(debutExtraction, finExtraction, debD1,
							finD1)) {
						parcoursBeanListTemp.add(p);
					}
				} else {
					finD1.setTime(new Date());
					if (isInPeriode(debutExtraction, finExtraction, debD1,
							finD1)) {
						parcoursBeanListTemp.add(p);
					}
				}
			}
			for (ContratTravailBean ct : contratBeanList) {
				GregorianCalendar debD1 = new GregorianCalendar();
				debD1.setTime(ct.getDebutContrat());
				GregorianCalendar finD1 = new GregorianCalendar();
				if (ct.getFinContrat() != null) {
					finD1.setTime(ct.getFinContrat());

					if (isInPeriode(debutExtraction, finExtraction, debD1,
							finD1)) {
						contratBeanListTemp.add(ct);
					}
				} else {
					if (isInPeriode(debutExtraction, finExtraction, debD1,
							debD1)) {
						contratBeanListTemp.add(ct);
					}
				}
			}
			for (AbsenceBean abs : absenceBeanList) {

				if (abs.getDebutAbsence() != null
						&& abs.getFinAbsence() != null) {
					GregorianCalendar debD1 = new GregorianCalendar();
					debD1.setTime(abs.getDebutAbsence());
					GregorianCalendar finD1 = new GregorianCalendar();
					finD1.setTime(abs.getFinAbsence());

					if (isInPeriode(debutExtraction, finExtraction, debD1,
							finD1)) {
						absenceBeanListTemp.add(abs);
					}
				}
			}
			for (AccidentBean acc : accidentBeanList) {

				GregorianCalendar debD1 = new GregorianCalendar();
				debD1.setTime(acc.getDateAccident());
				GregorianCalendar finD1 = new GregorianCalendar();
				finD1.setTime(acc.getDateAccident());

				if (isInPeriode(debutExtraction, finExtraction, debD1, finD1)) {
					accidentBeanListTemp.add(acc);
				}

			}
			for (HabilitationBean h : habilitationBeanList) {

				GregorianCalendar debD1 = new GregorianCalendar();
				debD1.setTime(h.getDelivrance());
				GregorianCalendar finD1 = new GregorianCalendar();
				finD1.setTime(h.getExpiration());

				if (isInPeriode(debutExtraction, finExtraction, debD1, finD1)) {
					habilitationBeanListTemp.add(h);
				}
			}
			for (FormationBean f : formationBeanList) {

				GregorianCalendar debD1 = new GregorianCalendar();
				debD1.setTime(f.getDebutFormation());
				GregorianCalendar finD1 = new GregorianCalendar();
				finD1.setTime(f.getFinFormation());

				if (isInPeriode(debutExtraction, finExtraction, debD1, finD1)) {
					formationBeanListTemp.add(f);
				}

			}
			for (EntretienBean e : entretienBeanList) {
				if (e.getAnneeReference() != null) {
					if (debutExtraction != null) {
						if (finExtraction != null) {
							if (e.getAnneeReference() <= finExtraction
									.get(Calendar.YEAR)
									&& e.getAnneeReference() >= debutExtraction
											.get(Calendar.YEAR)) {
								entretienBeanListTemp.add(e);
							}
						} else {
							if (e.getAnneeReference() >= debutExtraction
									.get(Calendar.YEAR)) {
								entretienBeanListTemp.add(e);
							}
						}
					} else {
						if (finExtraction != null) {
							if (e.getAnneeReference() <= finExtraction
									.get(Calendar.YEAR)) {
								entretienBeanListTemp.add(e);
							}
						}
					}
				}
			}
			for (RemunerationBean r : remunerationBeanList) {

				if (debutExtraction != null) {
					if (finExtraction != null) {
						if (r.getAnnee() <= finExtraction.get(Calendar.YEAR)
								&& r.getAnnee() >= debutExtraction
										.get(Calendar.YEAR)) {
							remunerationBeanListTemp.add(r);
						}
					} else {
						if (r.getAnnee() >= debutExtraction.get(Calendar.YEAR)) {
							remunerationBeanListTemp.add(r);
						}
					}
				} else {
					if (finExtraction != null) {
						if (r.getAnnee() <= finExtraction.get(Calendar.YEAR)) {
							remunerationBeanListTemp.add(r);
						}
					}
				}
			}

			parcoursBeanList = parcoursBeanListTemp;
			contratBeanList = contratBeanListTemp;
			absenceBeanList = absenceBeanListTemp;
			accidentBeanList = accidentBeanListTemp;
			habilitationBeanList = habilitationBeanListTemp;
			formationBeanList = formationBeanListTemp;
			entretienBeanList = entretienBeanListTemp;
			remunerationBeanList = remunerationBeanListTemp;
		}
	}

	public void filterListesObjets(List<SalarieBeanExport> list)
			throws Exception {

		List<ParcoursBean> parcoursBeanListTemp = new ArrayList<ParcoursBean>();
		List<ContratTravailBean> contratBeanListTemp = new ArrayList<ContratTravailBean>();
		List<AbsenceBean> absenceBeanListTemp = new ArrayList<AbsenceBean>();
		List<AccidentBean> accidentBeanListTemp = new ArrayList<AccidentBean>();
		List<HabilitationBean> habilitationBeanListTemp = new ArrayList<HabilitationBean>();
		List<FormationBean> formationBeanListTemp = new ArrayList<FormationBean>();
		List<EntretienBean> entretienBeanListTemp = new ArrayList<EntretienBean>();
		List<RemunerationBean> remunerationBeanListTemp = new ArrayList<RemunerationBean>();

		SalarieServiceImpl salServ = new SalarieServiceImpl();

		for (ParcoursBean p : parcoursBeanList) {
			if (containSalarie(salarieList,
					salServ.getSalarieBeanExportById(p.getIdSalarie()))) {
				parcoursBeanListTemp.add(p);
			}
		}
		for (ContratTravailBean ct : contratBeanList) {
			if (containSalarie(salarieList,
					salServ.getSalarieBeanExportById(ct.getIdSalarie()))) {
				contratBeanListTemp.add(ct);
			}
		}
		for (AbsenceBean abs : absenceBeanList) {
			if (containSalarie(salarieList,
					salServ.getSalarieBeanExportById(abs.getIdSalarie()))) {
				absenceBeanListTemp.add(abs);
			}
		}
		for (AccidentBean acc : accidentBeanList) {
			if (containSalarie(salarieList,
					salServ.getSalarieBeanExportById(acc.getIdSalarie()))) {
				accidentBeanListTemp.add(acc);
			}
		}
		for (HabilitationBean h : habilitationBeanList) {
			if (containSalarie(salarieList,
					salServ.getSalarieBeanExportById(h.getIdSalarie()))) {
				habilitationBeanListTemp.add(h);
			}
		}
		for (FormationBean f : formationBeanList) {
			if (containSalarie(salarieList,
					salServ.getSalarieBeanExportById(f.getIdSalarie()))) {
				formationBeanListTemp.add(f);
			}
		}
		for (EntretienBean e : entretienBeanList) {
			if (containSalarie(salarieList,
					salServ.getSalarieBeanExportById(e.getIdSalarie()))) {
				entretienBeanListTemp.add(e);
			}
		}
		for (RemunerationBean r : remunerationBeanList) {
			if (containSalarie(salarieList,
					salServ.getSalarieBeanExportById(r.getIdSalarie()))) {
				remunerationBeanListTemp.add(r);
			}
		}

		parcoursBeanList = parcoursBeanListTemp;
		contratBeanList = contratBeanListTemp;
		absenceBeanList = absenceBeanListTemp;
		accidentBeanList = accidentBeanListTemp;
		habilitationBeanList = habilitationBeanListTemp;
		formationBeanList = formationBeanListTemp;
		entretienBeanList = entretienBeanListTemp;
		remunerationBeanList = remunerationBeanListTemp;

	}

	public void rechercheParMotCleCompetences() throws Exception {
		if (motCleCompetences != null && !motCleCompetences.equals("")) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Mots clés compétences");
			String filtress = motCleCompetences;

			if (motCleCompetences.contains("'")) {
				motCleCompetences = motCleCompetences.replaceAll("'", " ");
			}
			String[] mot = motCleCompetences.split(" ");
			List<String> mots = new ArrayList<String>();
			for (String s : mot) {
				boolean test = true;
				for (String s2 : FRENCH_STOP_WORDS) {
					if (s.equals(s2)) {
						test = false;
					}
				}
				if (test) {
					mots.add(s);
				}
			}

			if (mots.size() > 0) {
				List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
				for (SalarieBeanExport sal : salarieList) {
					FicheMetierServiceImpl fmServ = new FicheMetierServiceImpl();
					FicheDePosteServiceImpl fdpServ = new FicheDePosteServiceImpl();

					FicheMetierBean fm = new FicheMetierBean();
					FicheDePosteBean fdp = new FicheDePosteBean();

					if (fdpServ.getFicheDePosteBeanByIdSalarie(sal.getId()) != null) {
						fdp = fdpServ.getFicheDePosteBeanByIdSalarie(sal
								.getId());
						fm = fmServ.getFicheMetierBeanById(fdp
								.getIdFicheMetierType());
						for (String s : mots) {
							if (containMotCleCompetences(fm, s)) {
								if (!containSalarie(list, sal)) {
									list.add(sal);
								}
							}
						}
					}
				}
				salarieList = list;
			}
			filtre.setValue(filtress);
			filtres.add(filtre);
		}
	}

	public Boolean containMotCleCompetences(FicheMetierBean fm, String s) {
		Session session = null;
		Transaction transaction = null;
		List<FicheMetier> fmInventory = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			fmInventory = new ArrayList<FicheMetier>();
			Query queryH = session
					.createQuery("from FicheMetier as fm where fm.Id="
							+ fm.getId() + " AND (fm.Savoir LIKE '%" + s
							+ "%' OR fm.SavoirEtre LIKE '%" + s
							+ "%' OR fm.SavoirFaire LIKE '%" + s + "%')");

			fmInventory = queryH.list();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return fmInventory.size() > 0;
	}

	public void verifEntretienContientMotCle() {
		if (motCleCompetencesAAmeliorer != null
				&& !motCleCompetencesAAmeliorer.equals("")) {

			if (motCleCompetencesAAmeliorer.contains("'")) {
				motCleCompetencesAAmeliorer = motCleCompetencesAAmeliorer
						.replaceAll("'", " ");
			}
			String[] mot = motCleCompetencesAAmeliorer.split(" ");
			List<String> mots = new ArrayList<String>();
			for (String s : mot) {
				boolean test = true;
				for (String s2 : FRENCH_STOP_WORDS) {
					if (s.equals(s2)) {
						test = false;
					}
				}
				if (test) {
					mots.add(s);
				}
			}
			List<EntretienBean> listEntTemp = new ArrayList<EntretienBean>();
			if (mots.size() > 0) {
				for (EntretienBean e : entretienBeanList) {
					if (isInPeriodeEntretien(e)) {
						boolean contain = false;
						for (int i = 0; i < mots.size(); i++) {
							if (containMotCleEntretien(e.getId(), mots.get(i))) {
								contain = true;
								break;
							}
						}
						if (contain) {
							listEntTemp.add(e);
						}
					}
				}
			}
			entretienBeanList = listEntTemp;
		}
	}

	public boolean isInPeriodeEntretien(EntretienBean e) {

		if (debutPeriode != null) {
			debutExtraction.setTime(debutPeriode);
			if (finPeriode != null) {
				finExtraction.setTime(finPeriode);
				if (e.getAnneeReference() < finExtraction.get(Calendar.YEAR)
						&& e.getAnneeReference() > debutExtraction
								.get(Calendar.YEAR)) {
					return true;
				}
			} else {
				if (e.getAnneeReference() >= debutExtraction.get(Calendar.YEAR)) {
					return true;
				}
			}
		} else {
			if (finPeriode != null) {
				finExtraction.setTime(finPeriode);
				if (e.getAnneeReference() <= finExtraction.get(Calendar.YEAR)) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	public Boolean containMotCleEntretien(Integer id, String s) {

		Session sessionh = null;
		Transaction transaction = null;
		List<Entretien> entInventory = null;
		try {
			sessionh = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = sessionh.beginTransaction();
			entInventory = new ArrayList<Entretien>();
			Query queryH = sessionh
					.createQuery("from Entretien as e left join fetch e.IdSalarie as s left join fetch s.Entreprise as ent where e.Id="
							+ id
							+ ""
							+ " AND e.Competence LIKE '%"
							+ s
							+ "%' AND ent.Groupe="
							+ Integer.parseInt(session.getAttribute("groupe")
									.toString()));

			entInventory = queryH.list();

			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} finally {
			if (sessionh != null && sessionh.isOpen()) {
				sessionh.close();
			}
		}
		return entInventory.size() > 0;
	}

	public void rechercheParMotCleCompetencesAAmeliorer() throws Exception {
		if (motCleCompetencesAAmeliorer != null
				&& !motCleCompetencesAAmeliorer.equals("")) {

			Filtre filtre = new Filtre();
			filtre.setFiltre("Mots clés compétences à améliorer");
			String filtress = motCleCompetencesAAmeliorer;

			if (motCleCompetencesAAmeliorer.contains("'")) {
				motCleCompetencesAAmeliorer = motCleCompetencesAAmeliorer
						.replaceAll("'", " ");
			}
			String[] mot = motCleCompetencesAAmeliorer.split(" ");
			List<String> mots = new ArrayList<String>();
			for (String s : mot) {
				boolean test = true;
				for (String s2 : FRENCH_STOP_WORDS) {
					if (s.equals(s2)) {
						test = false;
					}
				}
				if (test) {
					mots.add(s);
				}
			}

			if (mots.size() > 0) {
				List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
				for (SalarieBeanExport sal : salarieList) {
					FicheDePosteServiceImpl fdpServ = new FicheDePosteServiceImpl();

					FicheDePosteBean fdp = new FicheDePosteBean();

					if (fdpServ.getFicheDePosteBeanByIdSalarie(sal.getId()) != null) {
						fdp = fdpServ.getFicheDePosteBeanByIdSalarie(sal
								.getId());
						for (String s : mots) {
							boolean entretienOk = false;
							for (EntretienBean e : sal.getEntretienBeanList()) {
								if (e.getCompetence().contains(s)) {
									entretienOk = true;
									break;
								}
							}
							if (fdp.getCommentaire().contains(s) || entretienOk) {
								if (!containSalarie(list, sal)) {
									list.add(sal);
								}
							}
						}
					}
				}
				salarieList = list;
			}
			filtre.setValue(filtress);
			filtres.add(filtre);
		}
	}

	private void keepActualEmploye() throws Exception {
		List<SalarieBeanExport> res = new ArrayList<SalarieBeanExport>();
		for (SalarieBeanExport s : salarieList) {
			if (debutPeriode != null || finPeriode != null) {

				if (debutPeriode != null) {
					debutExtraction.setTime(debutPeriode);
				} else {
					debutExtraction = null;
				}
				if (finPeriode != null) {
					finExtraction.setTime(finPeriode);
				} else {
					finExtraction = null;
				}

				boolean present = false;
				for (ParcoursBean p : s.getParcoursBeanList()) {
					if (p.getFinFonction() == null) {
						GregorianCalendar today = new GregorianCalendar();
						today.setTime(new Date());
						GregorianCalendar debut = new GregorianCalendar();
						debut.setTime(p.getDebutFonction());
						if (isInPeriode(debutExtraction, finExtraction, debut,
								today)) {
							present = true;
							break;
						}
					} else {
						GregorianCalendar fin = new GregorianCalendar();
						fin.setTime(p.getFinFonction());
						GregorianCalendar debut = new GregorianCalendar();
						debut.setTime(p.getDebutFonction());
						if (isInPeriode(debutExtraction, finExtraction, debut,
								fin)) {
							present = true;
							break;
						}
					}
				}
				if (present) {
					res.add(s);
				}
			} else {
				ParcoursBean pb = getLastParcours(s);
				if (pb != null) {
					if (pb.getFinFonction() == null) {
						res.add(s);
					} else {
						Calendar today = new GregorianCalendar();
						today.setTime(new Date());
						Calendar fin = new GregorianCalendar();
						fin.setTime(pb.getFinFonction());
						if (fin.after(today)) {
							res.add(s);
						}
					}
				}
			}
		}
		salarieList = res;
	}

	public String retour() {
		return "filtreMultiCriteres";
	}

	private void initSalarieFromEnfant() throws Exception {

		if (selectedEnfant != null && selectedEnfant.length > 0)
			if (selectedEnfant.length == 1) {
				Filtre filtre = new Filtre();
				List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
				PersonneAChargeServiceImpl pacServ = new PersonneAChargeServiceImpl();
				if (selectedEnfant[0].equals("true")) {
					filtre.setFiltre("Enfants");
					filtre.setValue("Oui");

					for (int i = 0; i < salarieList.size(); i++) {
						SalarieBeanExport b = salarieList.get(i);
						if (pacServ.getPersonneAChargeBeanListByIdSalarie(
								b.getId()).size() > 0) {
							list.add(b);
						}
					}
				} else {
					filtre.setFiltre("Enfant");
					filtre.setValue("Non");
					for (int i = 0; i < salarieList.size(); i++) {
						SalarieBeanExport b = salarieList.get(i);
						if (pacServ.getPersonneAChargeBeanListByIdSalarie(
								b.getId()).size() == 0) {
							list.add(b);
						}
					}
				}
				salarieList = list;
				filtres.add(filtre);

			}
	}

	private void initSalarieFromSexe() throws Exception {

		if ((selectedSexe != null) && (selectedSexe.length > 0)) {
			Filtre filtre = new Filtre();
			if (selectedSexe.length != 2) {
				filtre.setFiltre("Sexe");
				// Une mise a jour est nécessaire
				List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
				if (selectedSexe.length == 1) {
					if (selectedSexe[0].equals("H")) {
						filtre.setValue("H");
						for (int i = 0; i < salarieList.size(); i++) {
							SalarieBeanExport b = salarieList.get(i);
							if (b.getCivilite().equals("Monsieur")) {
								list.add(salarieList.get(i));
							}
						}
					} else {
						filtre.setValue("F");
						for (int i = 0; i < salarieList.size(); i++) {
							if (!salarieList.get(i).getCivilite()
									.equals("Monsieur")) {
								list.add(salarieList.get(i));
							}
						}
					}
				}
				salarieList = list;
			} else {
				filtre.setFiltre("Sexe");
				filtre.setValue("H,F");
			}
			filtres.add(filtre);

		}
	}

	private void initSalarieFromAge() throws Exception {

		if ((debutAge != null && debutAge != 0) || (finAge != null && finAge != 0)) {
			Filtre filtre = new Filtre();
			if ((debutAge != null && debutAge != 0) && (finAge != null && finAge != 0)) {
				filtre = new Filtre();
				filtre.setFiltre("Age");
				filtre.setValue("De " + debutAge + " \u00e0 " + finAge + " ans");
				if (salarieList.size() > 0) {
					// Une mise a jour est nécessaire

					List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
					for (int i = 0; i < salarieList.size(); i++) {
						SalarieBeanExport s = salarieList.get(i);
						int ageSalarie = ExcelBB.calculer(s.getDateNaissance());
						if ((debutAge <= ageSalarie) && (finAge >= ageSalarie)) {
							list.add(s);
						}
					}
					salarieList = list;
				}
			} else {
				if (debutAge != null && debutAge != 0) {
					filtre = new Filtre();
					filtre.setFiltre("Age");
					filtre.setValue("Plus de " + debutAge + " ans");
					if (salarieList.size() > 0) {
						// Une mise a jour est nécessaire
						List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
						for (int i = 0; i < salarieList.size(); i++) {
							SalarieBeanExport s = salarieList.get(i);
							int ageSalarie = ExcelBB.calculer(s
									.getDateNaissance());
							if (debutAge <= ageSalarie) {
								list.add(s);
							}
						}
						salarieList = list;
					}
				} else {
					filtre = new Filtre();
					filtre.setFiltre("Age");
					filtre.setValue("Moins de " + finAge + " ans");
					if (salarieList.size() > 0) {
						// Une mise a jour est nécessaire
						List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
						for (int i = 0; i < salarieList.size(); i++) {
							SalarieBeanExport s = salarieList.get(i);
							int ageSalarie = ExcelBB.calculer(s
									.getDateNaissance());
							if (finAge >= ageSalarie) {
								list.add(s);
							}
						}
						salarieList = list;
					}
				}
			}
			filtres.add(filtre);
		}
	}

	private int getAncienneteEntreprise(SalarieBeanExport salarie)
			throws Exception {

		if (getFirstParcours(salarie) != null
				&& getLastParcours(salarie) != null) {
			ParcoursBean parcourDeb = getFirstParcours(salarie);

			ParcoursBean parcourFin = getLastParcours(salarie);
			Calendar d1 = new GregorianCalendar();
			Calendar d2 = new GregorianCalendar();
			d1.setTime(parcourDeb.getDebutFonction());
			if (parcourFin.getFinFonction() != null
					&& (parcourFin.getFinFonction().before(
							d2.getInstance().getTime()) || parcourFin
							.getFinFonction().equals(new Date()))) {
				d2.setTime(parcourFin.getFinFonction());
			} else {
				d2 = d2.getInstance();
			}
			int nbMois = 0;

			while (d1.before(d2)) {
				d1.add(GregorianCalendar.MONTH, 1);
				nbMois++;
			}
			int nbAnnees = nbMois / 12;
			nbMois = (nbMois % 12) - 1;
			if (nbMois > 1) {
				return nbAnnees + 1;
			} else {
				return nbAnnees;
			}
		} else {
			return 0;
		}
	}

	private void initSalarieFromAnciennete() throws Exception {
		Filtre filtre = new Filtre();
		if ((debutAnciennete != null && debutAnciennete != 0) || (finAnciennete != null && finAnciennete != 0)) {
			if ((debutAnciennete != null && debutAnciennete != 0) && (finAnciennete != null && finAnciennete != 0)) {
				filtre = new Filtre();
				filtre.setFiltre("Anciennet\u00E9");
				filtre.setValue("De " + debutAnciennete + " \u00e0 "
						+ finAnciennete + " ans");
				if (salarieList.size() > 0) {
					// Une mise a jour est nécessaire

					List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
					for (int i = 0; i < salarieList.size(); i++) {
						SalarieBeanExport s = salarieList.get(i);
						int anciennete = getAncienneteEntreprise(s);
						if ((debutAnciennete <= anciennete)
								&& (finAnciennete >= anciennete)) {
							list.add(s);
						}
					}
					salarieList = list;
				}
				filtres.add(filtre);
			} else {
				if (debutAnciennete != null && debutAnciennete != 0) {
					filtre = new Filtre();
					filtre.setFiltre("Anciennet\u00E9");
					filtre.setValue("Plus de " + debutAnciennete + " ans");
					if (salarieList.size() > 0) {
						// Une mise a jour est nécessaire
						List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
						for (int i = 0; i < salarieList.size(); i++) {
							SalarieBeanExport s = salarieList.get(i);
							int anciennete = getAncienneteEntreprise(s);
							if (debutAnciennete <= anciennete) {
								list.add(s);
							}
						}
						salarieList = list;
					}
					filtres.add(filtre);
				} else {
					filtre = new Filtre();
					filtre.setFiltre("Anciennet\u00E9");
					filtre.setValue("Moins de " + finAnciennete + " ans");
					if (salarieList.size() > 0) {
						// Une mise a jour est nécessaire
						List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
						for (int i = 0; i < salarieList.size(); i++) {
							SalarieBeanExport s = salarieList.get(i);
							int anciennete = getAncienneteEntreprise(s);
							if (finAnciennete >= anciennete) {
								list.add(s);
							}
						}
						salarieList = list;
					}
					filtres.add(filtre);
				}
			}
		}
	}

	private void initSalarieFromEntreprise() throws Exception {

		if ((idEntrepriseSelected != null) && (idEntrepriseSelected > 0)) {
			EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
			EntrepriseBean ent = serv
					.getEntrepriseBeanById(idEntrepriseSelected);
			Filtre filtre = new Filtre();
			filtre.setFiltre("Entreprise");
			filtre.setValue(ent.getNom());
			if (salarieList.size() > 0) {
				// Une mise a jour est nécessaire
				List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
				for (int j = 0; j < salarieList.size(); j++) {
					SalarieBeanExport s = salarieList.get(j);
					if (s.getIdEntrepriseSelected() == idEntrepriseSelected) {
						list.add(s);
					}
				}
				salarieList = list;

			}
			filtres.add(filtre);
		}
	}

	private void initSalarieFromServices() throws Exception {
		if ((this.idServicesSelected != null)
				&& (idServicesSelected.length > 0)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Services");
			// Une mise a jour est nécessaire
			String filtress = "";
			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
			for (int i = 0; i < idServicesSelected.length; i++) {
				int idServ = idServicesSelected[i];
				ServiceImpl serv = new ServiceImpl();
				ServiceBean sBean = serv.getServiceBeanById(idServ);
				filtress += sBean.getNom() + "; ";

				for (int j = 0; j < salarieList.size(); j++) {
					SalarieBeanExport s = salarieList.get(j);
					if (s.getIdServiceSelected() == idServ) {
						list.add(s);
					}
				}
			}
			salarieList = list;
			filtress = filtress.substring(0, filtress.length() - 2);
			filtre.setValue(filtress);
			filtres.add(filtre);
		}
	}

	private void initSalarieFromMetiersFDP() throws Exception {
		if ((this.idMetiersSelectedFDP != null)
				&& (idMetiersSelectedFDP.length > 0)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Poste occupés - Fiche de poste");

			String filtress = "";
			// Une mise a jour est nécessaire

			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
			for (int i = 0; i < idMetiersSelectedFDP.length; i++) {
				int idMet = idMetiersSelectedFDP[i];
				FicheDePosteServiceImpl servFDP = new FicheDePosteServiceImpl();
				FicheMetierServiceImpl servFM = new FicheMetierServiceImpl();
				filtress += servFM.getFicheMetierBeanById(
						idMetiersSelectedFDP[i]).getIntituleFicheMetier()
						+ ", ";
				for (int j = 0; j < salarieList.size(); j++) {
					SalarieBeanExport s = salarieList.get(j);
					if (servFDP.getFicheDePosteBeanByIdSalarie(s.getId()) != null) {
						if (servFDP.getFicheDePosteBeanByIdSalarie(s.getId())
								.getIdFicheMetierType() == idMet) {
							list.add(s);
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			filtre.setValue(filtress);
			filtres.add(filtre);
			salarieList = list;
		}
	}

	private void initSalarieFromMotifRupture() throws Exception {
		if ((this.idMotifsRuptureSelected != null)
				&& (idMotifsRuptureSelected.length > 0)) {

			if (debutPeriode != null) {
				debutExtraction.setTime(debutPeriode);
			} else {
				debutExtraction = null;
			}
			if (finPeriode != null) {
				finExtraction.setTime(finPeriode);
			} else {
				finExtraction = null;
			}

			Filtre filtre = new Filtre();
			filtre.setFiltre("Motif de rupture de contrat");

			String filtress = "";

			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
			List<ContratTravailBean> listCT = new ArrayList<ContratTravailBean>();

			for (int i = 0; i < idMotifsRuptureSelected.length; i++) {
				int idMR = idMotifsRuptureSelected[i];
				MotifRuptureContratServiceImpl serv = new MotifRuptureContratServiceImpl();
				filtress += serv.getMotifRuptureContratBeanById(idMR).getNom()
						+ ", ";
				for (ContratTravailBean ct : contratBeanList) {
					if (ct.getIdMotifRuptureContrat() != null) {
						if (ct.getIdMotifRuptureContrat() == idMR) {
							GregorianCalendar debD1 = new GregorianCalendar();
							debD1.setTime(ct.getDebutContrat());
							GregorianCalendar finD1 = new GregorianCalendar();
							if (ct.getFinContrat() != null) {
								finD1.setTime(ct.getFinContrat());

								if (isInPeriode(debutExtraction, finExtraction,
										debD1, finD1)) {
									listCT.add(ct);
								}
							} else {
								if (isInPeriode(debutExtraction, finExtraction,
										debD1, debD1)) {
									listCT.add(ct);
								}
							}

						}
					}
				}
				for (int j = 0; j < salarieList.size(); j++) {
					SalarieBeanExport s = salarieList.get(j);
					ContratTravailBean ct = getLastContrat(s);
					if (ct != null) {
						if (ct.getIdMotifRuptureContrat() == idMR) {
							GregorianCalendar debD1 = new GregorianCalendar();
							debD1.setTime(ct.getDebutContrat());
							GregorianCalendar finD1 = new GregorianCalendar();
							if (ct.getFinContrat() != null) {
								finD1.setTime(ct.getFinContrat());

								if (isInPeriode(debutExtraction, finExtraction,
										debD1, finD1)) {
									list.add(s);
								}
							} else {
								if (isInPeriode(debutExtraction, finExtraction,
										debD1, debD1)) {
									list.add(s);
								}
							}
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			filtre.setValue(filtress);
			filtres.add(filtre);
			salarieList = list;

			List<ContratTravailBean> listCTTemp = new ArrayList<ContratTravailBean>();
			for (ContratTravailBean c : contratBeanList) {
				for (ContratTravailBean c2 : listCT) {
					if (c.getId() == c2.getId()) {
						listCTTemp.add(c);
					}
				}
			}
			contratBeanList = listCTTemp;
		}
	}

	private boolean after(GregorianCalendar d1, GregorianCalendar d2) {
		// On teste si les dates sont egales, car GregorianCalendar ne prend pas
		// en comtpe ce cas, sinon on renvoi
		// true si d1 est avant d2
		if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
			if (d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)) {
				if (d1.get(Calendar.DATE) == d2.get(Calendar.DATE)) {
					return true;
				}
			}
		}
		return (d1.after(d2));
	}

	private boolean before(GregorianCalendar d1, GregorianCalendar d2) {
		// On teste si les dates sont egales, car GregorianCalendar ne prend pas
		// en comtpe ce cas, sinon on renvoi
		// true si d1 est avant d2
		if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
			if (d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)) {
				if (d1.get(Calendar.DATE) == d2.get(Calendar.DATE)) {
					return true;
				}
			}
		}
		return (d1.before(d2));
	}

	private void initSalarieFromCategoriesSocioPro() throws Exception {

		if ((this.idStatutsSelected != null) && (idStatutsSelected.length > 0)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Cat\u00E9gories Socio-Professionnelles");
			String filtress = "";
			// Une mise a jour est nécessaire
			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
			for (int i = 0; i < idStatutsSelected.length; i++) {
				int idStatut = idStatutsSelected[i];
				StatutServiceImpl serv = new StatutServiceImpl();
				filtress += serv.getStatutBeanById(idStatut).getNom() + "; ";
				for (int j = 0; j < salarieList.size(); j++) {
					SalarieBeanExport s = salarieList.get(j);
					ParcoursBean p = getLastParcours(s);
					if (p != null) {
						if (p.getIdTypeStatutSelected() == idStatut) {
							list.add(s);
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			salarieList = list;
			filtre.setValue(filtress);
			filtres.add(filtre);
		}
	}

	private void initSalarieFromContrat() throws Exception {

		if ((this.idContratsSelected != null)
				&& (idContratsSelected.length > 0)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Contrats");
			String filtress = "";
			// Une mise a jour est nécessaire
			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
			TypeContratServiceImpl tc = new TypeContratServiceImpl();
			for (int i = 0; i < idContratsSelected.length; i++) {
				int idContrat = idContratsSelected[i];

				filtress += tc.getTypeContratBeanById(idContrat).getNom()
						+ "; ";
				for (int j = 0; j < salarieList.size(); j++) {
					SalarieBeanExport s = salarieList.get(j);
					ParcoursBean p = getLastParcours(s);
					if (p != null) {
						if (p.getIdTypeContratSelected() == idContrat) {
							list.add(s);
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			filtre.setValue(filtress);
			salarieList = list;
			filtres.add(filtre);
		}
	}

	private ParcoursBean getLastParcours(SalarieBeanExport salarie)
			throws Exception {
		ParcoursServiceImpl parServ = new ParcoursServiceImpl();
		List<ParcoursBean> parcoursBeanList = parServ
				.getParcoursBeanListByIdSalarie(salarie.getId());
		ParcoursBean pb = null;
		for (int i = 0; i < parcoursBeanList.size(); i++) {
			ParcoursBean parcour = parcoursBeanList.get(i);
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

	private ContratTravailBean getLastContrat(SalarieBeanExport salarie)
			throws Exception {
		ContratTravailServiceImpl ctServ = new ContratTravailServiceImpl();
		List<ContratTravailBean> ctList = ctServ
				.getContratTravailBeanListByIdSalarie(salarie.getId());

		ContratTravailBean ctBean = null;
		if (ctList != null && ctList.size() > 0) {
			Collections.sort(ctList);
			Collections.reverse(ctList);
			ctBean = ctList.get(0);
		}

		return ctBean;
	}

	private ParcoursBean getFirstParcours(SalarieBeanExport salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		ParcoursBean pb = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			if (pb == null) {
				pb = parcour;
			}
			if (parcour.getDebutFonction().before(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}

	private boolean hasTypeAccident(SalarieBeanExport s, int typeAc)
			throws Exception {
		AccidentServiceImpl serv = new AccidentServiceImpl();
		List<AccidentBean> listAccSalarie = serv.getAccidentsOfSalarie(s);
		for (int i = 0; i < listAccSalarie.size(); i++) {
			AccidentBean ac = listAccSalarie.get(i);
			if (ac.getIdTypeAccidentBeanSelected() == typeAc) {
				return true;
			}
		}
		return false;
	}

	private boolean hasAllTypeAccidentSelected(SalarieBeanExport s)
			throws Exception {
		for (int i = 0; i < this.idTypeAccidentSelected.length; i++) {
			if (!hasTypeAccident(s, idTypeAccidentSelected[i])) {
				return false;
			}
		}
		return true;
	}

	private void initSalarieFromAccident() throws Exception {
		if ((idTypeAccidentSelected != null)
				&& (idTypeAccidentSelected.length > 0)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Types d'accidents (s\u00e9lection en "
					+ this.getTypeAccET() + ")");
			String filtress = "";
			if (debutPeriode != null) {

				GregorianCalendar d1 = new GregorianCalendar();
				d1.setTime(debutPeriode);
				filtress += "du " + dateFormat.format(d1.getTime()) + ", ";
			}
			if (finPeriode != null) {
				GregorianCalendar d2 = new GregorianCalendar();
				d2.setTime(finPeriode);
				filtress += "au " + dateFormat.format(d2.getTime()) + ", ";
			}

			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
			for (int i = 0; i < idTypeAccidentSelected.length; i++) {
				// Pour chaque Type d'accident qui ont ete selectionné
				int idTypeAcc = idTypeAccidentSelected[i];
				TypeAccidentServiceImpl serv = new TypeAccidentServiceImpl();
				filtress += serv.getTypeAccidentBeanById(idTypeAcc).getNom()
						+ "; ";
				for (int j = 0; j < salarieList.size(); j++) {
					// Pour les salaries qu'il reste, on regarde s'ils ont eu
					// des accidents de ce type
					SalarieBeanExport s = salarieList.get(j);
					List<AccidentBean> listAcc = s.getAccidentBeanList();
					for (int k = 0; k < listAcc.size(); k++) {
						AccidentBean acc = listAcc.get(k);
						if (acc.getIdTypeAccidentBeanSelected() == idTypeAcc) {
							// Reste a savoir si l'accident a eu lieu dans la
							// periode donnée
							if ((debutPeriode == null) && (finPeriode == null)) {
								// Pas de période donnée
								if (!containSalarie(list, s)) {
									if (getTypeAccET().equals("Et")) {
										if (hasAllTypeAccidentSelected(s)) {
											list.add(s);
										}
									} else {
										list.add(s);
									}
								}
							} else {
								GregorianCalendar d1 = new GregorianCalendar();
								GregorianCalendar d2 = new GregorianCalendar();
								// Il faut regarder par rapport aux dates
								if (debutPeriode == null) {
									// On initialise a une date tres
									// ancienne, (l'an 1000...)
									d1.setTime(new Date(1));
								} else {
									d1.setTime(debutPeriode);
								}
								if (finPeriode == null) {
									// Une date tres loin
									d2.setTime(new Date(Long.MAX_VALUE));
								} else {
									d2.setTime(finPeriode);
								}
								GregorianCalendar CalAcc = new GregorianCalendar();
								CalAcc.setTime(acc.getDateAccident());
								// On Compare les dates
								if (before(d1, CalAcc)) {
									if (before(CalAcc, d2)) {
										if (!containSalarie(list, s)) {
											if (this.getTypeAccET()
													.equals("Et")) {
												if (this.hasAllTypeAccidentSelected(s)) {
													list.add(s);
												}
											} else {
												list.add(s);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			salarieList = list;
			filtre.setValue(filtress);
			filtres.add(filtre);
		}
	}

	private boolean isInPeriode(GregorianCalendar debutExt,
			GregorianCalendar finExt, GregorianCalendar debutObj,
			GregorianCalendar finObj) {

		if (debutExt != null) {
			if (finExt != null) {
				if (after(debutObj, debutExt) && before(debutObj, finExt)) {
					return true;
				}
				if (after(finObj, debutExt) && before(finObj, finExt)) {
					return true;
				}
				if (before(debutObj, debutExt) && after(finObj, finExt)) {
					return true;
				}
			} else {
				if (after(debutObj, debutExt)) {
					return true;
				}
				if (after(finObj, debutExt)) {
					return true;
				}
			}
		} else {
			if (finExt != null) {
				if (before(debutObj, finExt)) {
					return true;
				}
				if (before(finObj, finExt)) {
					return true;
				}
			} else {
				return true;
			}
		}

		return false;
	}

	private boolean hasTypeCauseAccident(SalarieBeanExport s, int typeCauseAcc)
			throws Exception {
		AccidentServiceImpl serv = new AccidentServiceImpl();
		List<AccidentBean> listAccSalarie = serv.getAccidentsOfSalarie(s);
		for (int i = 0; i < listAccSalarie.size(); i++) {
			AccidentBean ac = listAccSalarie.get(i);
			if (ac.getIdTypeCauseAccidentBeanSelected() == typeCauseAcc) {
				return true;
			}
		}
		return false;
	}

	private boolean hasAllTypeCauseAccidentSelected(SalarieBeanExport s)
			throws Exception {
		for (int i = 0; i < this.idCauseAccidentSelected.length; i++) {
			if (!hasTypeCauseAccident(s, idCauseAccidentSelected[i])) {
				return false;
			}
		}
		return true;
	}

	private void initSalarieFromCauseAccident() throws Exception {
		if ((this.idCauseAccidentSelected != null)
				&& (idCauseAccidentSelected.length > 0)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Causes d'accidents (s\u00e9lection en "
					+ this.getTypeCauseAccET() + ")");
			String filtress = "";
			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
			for (int i = 0; i < idCauseAccidentSelected.length; i++) {
				// Pour chaque Type de cause d'accident qui ont ete selectionné
				int idTypeCauseAcc = idCauseAccidentSelected[i];
				TypeCauseAccidentServiceImpl serv = new TypeCauseAccidentServiceImpl();
				filtress += serv.getTypeCauseAccidentBeanById(idTypeCauseAcc)
						.getNom() + "; ";
				for (int j = 0; j < salarieList.size(); j++) {
					// Pour les salaries qu'il reste, on regarde s'ils ont eu
					// des accidents de cette cause
					SalarieBeanExport s = salarieList.get(j);
					List<AccidentBean> listAcc = s.getAccidentBeanList();
					for (int k = 0; k < listAcc.size(); k++) {
						AccidentBean acc = listAcc.get(k);
						if (acc.getIdTypeCauseAccidentBeanSelected() == idTypeCauseAcc) {

							// Reste a savoir si l'accident a eu lieu dans la
							// periode donnée
							if ((this.debutPeriode == null)
									&& (finPeriode == null)) {
								// Pas de période donnée
								if (!containSalarie(list, s)) {
									if (this.getTypeCauseAccET().equals("Et")) {
										if (hasAllTypeCauseAccidentSelected(s)) {
											list.add(s);
										}
									} else {
										list.add(s);
									}
								}
							} else {
								GregorianCalendar d1 = new GregorianCalendar();
								GregorianCalendar d2 = new GregorianCalendar();
								// Il faut regarder par rapport aux dates
								if (debutPeriode == null) {
									// On initialise a une date tres ancienne,
									// (l'an 1000...)
									d1.setTime(new Date(1));
								} else {
									d1.setTime(debutPeriode);

								}
								if (finPeriode == null) {
									// Une date tres loin
									d2.setTime(new Date(Long.MAX_VALUE));
								} else {
									d2.setTime(finPeriode);
								}
								GregorianCalendar CalAcc = new GregorianCalendar();
								CalAcc.setTime(acc.getDateAccident());
								// On Compare les dates
								if (before(d1, CalAcc)) {
									if (before(CalAcc, d2)) {
										if (!containSalarie(list, s)) {
											if (this.getTypeCauseAccET()
													.equals("Et")) {
												if (hasAllTypeCauseAccidentSelected(s)) {
													list.add(s);
												}
											} else {
												list.add(s);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			salarieList = list;
			filtre.setValue(filtress);
			filtres.add(filtre);

			List<AccidentBean> listAcc = new ArrayList<AccidentBean>();

			for (SalarieBeanExport s : salarieList) {
				for (AccidentBean a : s.getAccidentBeanList()) {
					for (int i = 0; i < idCauseAccidentSelected.length; i++) {
						int idCauseAcc = idCauseAccidentSelected[i];
						if (a.getIdTypeCauseAccidentBeanSelected() == idCauseAcc) {
							listAcc.add(a);
						}
					}
				}
			}
			majAccidentList(listAcc);
		}
	}

	private boolean hasTypeAbsence(SalarieBeanExport s, int typeAbs)
			throws Exception {
		AbsenceServiceImpl serv = new AbsenceServiceImpl();
		List<AbsenceBean> listAbsSalarie = serv.getAbsenceBeanListByIdSalarie(s
				.getId().intValue());
		for (int i = 0; i < listAbsSalarie.size(); i++) {
			AbsenceBean ab = listAbsSalarie.get(i);
			if (ab.getIdTypeAbsenceSelected() == typeAbs) {
				return true;
			}
		}
		return false;
	}

	private boolean hasAllTypeAbsenceSelected(SalarieBeanExport s)
			throws Exception {
		for (int i = 0; i < this.idTypeAbsenceSelected.length; i++) {
			if (!hasTypeAbsence(s, idTypeAbsenceSelected[i])) {
				return false;
			}
		}
		return true;
	}

	private void initSalarieFromNatureAccident() throws Exception {
		String query = "";
		if (selectedNature != null && selectedNature.length > 0) {
			if (selectedNature.length == 1) {
				Filtre filtre = new Filtre();
				filtre.setFiltre("Natures d'accidents ");

				if (selectedNature[0].equals("I")) {
					filtre.setValue("Initial");
					query = "from Salarie as s left join fetch s.Entreprise as ent left join fetch s.ACCIDENTs as a where ent.Groupe="
							+ Integer.parseInt(session.getAttribute("groupe")
									.toString()) + " and a.Initial=1";
				} else {
					filtre.setValue("Rechute");
					query = "from Salarie as s left join fetch s.Entreprise as ent left join fetch s.ACCIDENTs as a where ent.Groupe="
							+ Integer.parseInt(session.getAttribute("groupe")
									.toString()) + " and a.Initial=0";
				}

				if (debutPeriode != null) {
					if (finPeriode != null) {
						query += " and (a.DateAccident between :debut and :fin)";
					} else {
						query += " and (a.DateAccident >= :debut)";
					}
				} else {
					if (finPeriode != null) {
						query += " and (a.DateAccident <= :fin)";
					}
				}

				SalarieServiceImpl salServ = new SalarieServiceImpl();

				Session session = null;
				Transaction transaction = null;
				List<SalarieBeanExport> listResult = new ArrayList<SalarieBeanExport>();
				try {
					session = HibernateUtil.getSessionFactory().getCurrentSession();
					transaction = session.beginTransaction();

					Query queryH = session.createQuery(query);
					if (debutPeriode != null) {
						queryH.setParameter("debut", debutPeriode);
					}
					if (finPeriode != null) {
						queryH.setParameter("fin", finPeriode);
					}

					List<Salarie> salarieInventory = queryH.list();

					listResult = salServ
							.salariePersistantListToSalarieBeanExportList(salarieInventory);
					transaction.commit();
				} catch (HibernateException e) {
					if (transaction != null && !transaction.wasRolledBack()) {
						transaction.rollback();
					}
					LOGGER.error("Hibernate Session Error", e);
					throw e;
				} finally {
					if (session != null && session.isOpen()) {
						session.close();
					}
				}

				List<AccidentBean> listAcc = new ArrayList<AccidentBean>();
				List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
				for (SalarieBeanExport s : salarieList) {
					for (SalarieBeanExport s2 : listResult) {
						if (s2.getId().intValue() == s.getId().intValue()) {
							list.add(s);
							listAcc.addAll(s2.getAccidentBeanList());
							break;
						}
					}
				}

				majAccidentList(listAcc);

				salarieList = list;
				filtres.add(filtre);
			}

		}

	}

	private void initSalarieFromTypeAccident() throws Exception {

		if ((this.idTypeAccidentSelected != null)
				&& (idTypeAccidentSelected.length > 0)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Natures d'accidents (s\u00e9lection en "
					+ this.getTypeAbsET() + ")");
			String filtress = "";
			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
			for (int i = 0; i < idTypeAccidentSelected.length; i++) {
				// Pour chaque Type d'accident qui ont ete selectionné
				int idTypeAcc = idTypeAccidentSelected[i];
				TypeAccidentServiceImpl serv = new TypeAccidentServiceImpl();
				filtress += serv.getTypeAccidentBeanById(idTypeAcc).getNom()
						+ "; ";
				for (int j = 0; j < salarieList.size(); j++) {
					// Pour les salaries qu'il reste, on regarde s'ils ont eu
					// des accidents de ce type
					SalarieBeanExport s = salarieList.get(j);
					List<AccidentBean> listAcc = s.getAccidentBeanList();
					for (int k = 0; k < listAcc.size(); k++) {
						AccidentBean abs = listAcc.get(k);
						if (abs.getDateAccident() != null) {
							if (abs.getIdTypeAccidentBeanSelected() == idTypeAcc) {

								// Reste a savoir si l'accident a eu lieu dans
								// la
								// periode donnée
								if ((this.debutPeriode == null)
										&& (finPeriode == null)) {
									// Pas de période donnée
									if (!containSalarie(list, s)) {
										if (this.getTypeAccET().equals("Et")) {
											if (this.hasAllTypeAccidentSelected(s)) {
												list.add(s);
											}
										} else {
											list.add(s);
										}
									}
								} else {
									GregorianCalendar d1 = new GregorianCalendar();
									GregorianCalendar d2 = new GregorianCalendar();
									// Il faut regarder par rapport aux dates
									if (debutPeriode == null) {
										// On initialise a une date tres
										// ancienne
										d1.setTime(new Date(1));
									} else {
										d1.setTime(debutPeriode);
									}
									if (finPeriode == null) {
										// Une date tres loin
										d2.setTime(new Date(Long.MAX_VALUE));
									} else {
										d2.setTime(finPeriode);
									}
									GregorianCalendar accDebut = new GregorianCalendar();
									accDebut.setTime(abs.getDateAccident());
									// On Compare les dates
									if (isInPeriode(d1, d2, accDebut, accDebut)) {
										if (!containSalarie(list, s)) {
											if (this.getTypeAccET()
													.equals("Et")) {
												if (this.hasAllTypeAccidentSelected(s)) {
													list.add(s);
												}
											} else {
												list.add(s);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			salarieList = list;
			filtre.setValue(filtress);
			filtres.add(filtre);

			List<AccidentBean> listAcc = new ArrayList<AccidentBean>();

			for (SalarieBeanExport s : salarieList) {
				for (AccidentBean a : s.getAccidentBeanList()) {
					for (int i = 0; i < idTypeAccidentSelected.length; i++) {
						int idTypeAcc = idTypeAccidentSelected[i];
						if (a.getIdTypeAccidentBeanSelected() == idTypeAcc) {
							listAcc.add(a);
						}
					}
				}
			}
			majAccidentList(listAcc);

		}
	}

	private void initSalarieFromTypeAbsence() throws Exception {

		if ((this.idTypeAbsenceSelected != null)
				&& (idTypeAbsenceSelected.length > 0)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Natures d'absences (s\u00e9lection en "
					+ this.getTypeAbsET() + ")");
			String filtress = "";
			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();

			for (int i = 0; i < idTypeAbsenceSelected.length; i++) {
				// Pour chaque Type d'accident qui ont ete selectionné
				int idTypeAbs = idTypeAbsenceSelected[i];
				TypeAbsenceServiceImpl serv = new TypeAbsenceServiceImpl();
				filtress += serv.getTypeAbsenceBeanById(idTypeAbs).getNom()
						+ "; ";
				for (int j = 0; j < salarieList.size(); j++) {
					// Pour les salaries qu'il reste, on regarde s'ils ont eu
					// des accidents de ce type
					SalarieBeanExport s = salarieList.get(j);
					List<AbsenceBean> listAbs = s.getAbsenceBeanList();
					for (int k = 0; k < listAbs.size(); k++) {
						AbsenceBean abs = listAbs.get(k);
						if (abs.getDebutAbsence() != null
								&& abs.getFinAbsence() != null) {
							if (abs.getIdTypeAbsenceSelected() == idTypeAbs) {

								// Reste a savoir si l'accident a eu lieu dans
								// la
								// periode donnée
								if ((this.debutPeriode == null)
										&& (finPeriode == null)) {
									// Pas de période donnée
									if (!containSalarie(list, s)) {
										if (this.getTypeAbsET().equals("Et")) {
											if (this.hasAllTypeAbsenceSelected(s)) {
												list.add(s);
											}
										} else {
											list.add(s);
										}
									}
								} else {
									GregorianCalendar d1 = new GregorianCalendar();
									GregorianCalendar d2 = new GregorianCalendar();
									// Il faut regarder par rapport aux dates
									if (debutPeriode == null) {
										// On initialise a une date tres
										// ancienne
										d1.setTime(new Date(1));
									} else {
										d1.setTime(debutPeriode);
									}
									if (finPeriode == null) {
										// Une date tres loin
										d2.setTime(new Date(Long.MAX_VALUE));
									} else {
										d2.setTime(finPeriode);
									}
									GregorianCalendar absDebut = new GregorianCalendar();
									absDebut.setTime(abs.getDebutAbsence());
									GregorianCalendar absFin = new GregorianCalendar();
									absFin.setTime(abs.getFinAbsence());
									// On Compare les dates
									if (isInPeriode(d1, d2, absDebut, absFin)) {
										if (!containSalarie(list, s)) {
											if (this.getTypeAbsET()
													.equals("Et")) {
												if (this.hasAllTypeAbsenceSelected(s)) {
													list.add(s);
												}
											} else {
												list.add(s);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			salarieList = list;
			filtre.setValue(filtress);
			filtres.add(filtre);

			List<AbsenceBean> listAb = new ArrayList<AbsenceBean>();

			for (SalarieBeanExport s : salarieList) {
				for (AbsenceBean a : s.getAbsenceBeanList()) {
					for (int i = 0; i < idTypeAbsenceSelected.length; i++) {
						int idTypeAbs = idTypeAbsenceSelected[i];
						if (a.getIdTypeAbsenceSelected() == idTypeAbs) {
							listAb.add(a);
						}
					}
				}
			}

			List<AbsenceBean> listAbsTemp = new ArrayList<AbsenceBean>();
			for (AbsenceBean a : absenceBeanList) {
				for (AbsenceBean a2 : listAb) {
					if (a.getId().intValue() == a2.getId().intValue()) {
						listAbsTemp.add(a);
					}
				}
			}
			// displayListID(absenceBeanList, "before :: ");
			absenceBeanList = listAbsTemp;
			// displayListID(absenceBeanList, "after :: ");
		}
	}

	private void majAccidentList(List<AccidentBean> acc) {
		List<AccidentBean> listAccTemp = new ArrayList<AccidentBean>();
		for (AccidentBean a : accidentBeanList) {
			for (AccidentBean a2 : acc) {
				if (a.getId() == a2.getId()) {
					listAccTemp.add(a);
				}
			}
		}
		// displayListID(accidentBeanList, "before ::");
		accidentBeanList = listAccTemp;
		// displayListID(accidentBeanList, "after ::");
	}

	private boolean hasLesion(SalarieBeanExport s, int lesion) throws Exception {
		AccidentServiceImpl serv = new AccidentServiceImpl();
		List<AccidentBean> listAccSalarie = serv.getAccidentsOfSalarie(s);
		for (int i = 0; i < listAccSalarie.size(); i++) {
			AccidentBean ac = listAccSalarie.get(i);
			if (ac.getIdTypeLesionBeanSelected() == lesion) {
				return true;
			}
		}
		return false;
	}

	private boolean hasAllLesionSelected(SalarieBeanExport s) throws Exception {
		for (int i = 0; i < this.idTypeLesionSelected.length; i++) {
			if (!hasLesion(s, idTypeLesionSelected[i])) {
				return false;
			}
		}
		return true;
	}

	private void initSalarieFromLesion() throws Exception {
		if ((this.idTypeLesionSelected != null)
				&& (idTypeLesionSelected.length > 0)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("L\u00E9sion (s\u00e9lection en "
					+ this.getTypeLesionET() + ")");
			String filtress = "";
			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
			for (int i = 0; i < idTypeLesionSelected.length; i++) {
				// Pour chaque Type de lesion qui ont ete selectionné
				int idTypeLesion = idTypeLesionSelected[i];
				TypeLesionServiceImpl serv = new TypeLesionServiceImpl();
				filtress += serv.getTypeLesionBeanById(idTypeLesion).getNom()
						+ "; ";
				for (int j = 0; j < salarieList.size(); j++) {
					// Pour les salaries qu'il reste, on regarde s'ils ont eu
					// des accidents de ce type avec la lesion
					SalarieBeanExport s = salarieList.get(j);
					List<AccidentBean> listAcc = s.getAccidentBeanList();
					for (int k = 0; k < listAcc.size(); k++) {
						AccidentBean acc = listAcc.get(k);
						if (acc.getIdTypeLesionBeanSelected() == idTypeLesion) {
							// Reste a savoir si l'accident a eu lieu dans la
							// periode donnée
							if ((this.debutPeriode == null)
									&& (finPeriode == null)) {
								// Pas de période donnée
								if (!containSalarie(list, s)) {
									if (this.getTypeLesionET().equals("Et")) {
										if (this.hasAllLesionSelected(s)) {
											list.add(s);
										}
									} else {
										list.add(s);
									}
								}
							} else {
								GregorianCalendar d1 = new GregorianCalendar();
								GregorianCalendar d2 = new GregorianCalendar();
								// Il faut regarder par rapport aux dates
								if (debutPeriode == null) {
									// On initialise a une date tres ancienne,
									// (l'an 1000...)
									d1.setTime(new Date(1));
								} else {
									d1.setTime(debutPeriode);
								}
								if (finPeriode == null) {
									// Une date tres loin
									d2.setTime(new Date(Long.MAX_VALUE));
								} else {
									d2.setTime(finPeriode);
								}
								GregorianCalendar CalAcc = new GregorianCalendar();
								CalAcc.setTime(acc.getDateAccident());
								// On Compare les dates
								if (before(d1, CalAcc)) {
									if (before(CalAcc, d2)) {
										if (!containSalarie(list, s)) {
											if (this.getTypeLesionET().equals(
													"Et")) {
												if (this.hasAllLesionSelected(s)) {
													list.add(s);
												}
											} else {
												list.add(s);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			salarieList = list;
			filtre.setValue(filtress);
			filtres.add(filtre);

			List<AccidentBean> listAcc = new ArrayList<AccidentBean>();

			for (SalarieBeanExport s : salarieList) {
				for (AccidentBean a : s.getAccidentBeanList()) {
					for (int i = 0; i < idTypeLesionSelected.length; i++) {
						int idLesionAcc = idTypeLesionSelected[i];
						if (a.getIdTypeLesionBeanSelected() == idLesionAcc) {
							listAcc.add(a);
						}
					}
				}
			}
			majAccidentList(listAcc);

		}
	}

	// private GregorianCalendar ajoutDate(int moisAjout) {
	// GregorianCalendar gc = new GregorianCalendar();
	// gc.setTime(new Date());
	// gc.add(Calendar.MONTH, moisAjout);
	// return gc;
	// }

	private boolean testHabilitationValide(HabilitationBean hab) {

		GregorianCalendar today = new GregorianCalendar();
		today.setTime(new Date());
		GregorianCalendar expir = new GregorianCalendar();
		expir.setTime(hab.getExpiration());
		Calendar delivr = new GregorianCalendar();
		delivr.setTime(hab.getDelivrance());
		if (debutPeriode == null) {
			if (finPeriode == null)
				if (expir.after(today)) {
					return true;
				} else if (expir.after(finPeriode)) {
					return true;
				}
		} else {
			if (finPeriode != null)
				if (delivr.before(debutPeriode) && expir.after(finPeriode)) {
					return true;
				} else if (delivr.before(debutPeriode) && expir.after(today)) {
					return true;
				}
		}

		return false;
	}

	private boolean testHabilitationPerimeAu(HabilitationBean hab) {

		GregorianCalendar expir = new GregorianCalendar();
		expir.setTime(hab.getExpiration());

		if (expir.getTime().before(datePeremption)) {
			return true;
		}

		return false;
	}

	private boolean hasTypeHabilitation(SalarieBeanExport s, int typeHab)
			throws Exception {
		HabilitationServiceImpl serv = new HabilitationServiceImpl();
		List<HabilitationBean> lisHabilitationSalarie = serv
				.getHabilitationsListFromIdSalarie(s.getId().intValue());
		for (int i = 0; i < lisHabilitationSalarie.size(); i++) {
			HabilitationBean hab = lisHabilitationSalarie.get(i);
			if (hab.getIdTypeHabilitationSelected() == typeHab) {
				return true;
			}
		}
		return false;
	}

	private boolean hasAllTypeHabilitationSelected(SalarieBeanExport s)
			throws Exception {
		for (int i = 0; i < this.idTypeHabSelected.length; i++) {
			if (!hasTypeHabilitation(s, idTypeHabSelected[i])) {
				return false;
			}
		}
		return true;
	}

	public void searchHabNonValide(List<SalarieBeanExport> list,
			HabilitationBean hab) throws Exception {
		for (int j = 0; j < salarieList.size(); j++) {
			SalarieBeanExport s = salarieList.get(j);
			if (s.getId() == hab.getIdSalarie()) {
				// On regarde s'il n'a pas ete ajouté
				// precedement
				if (!containSalarie(list, s)) {
					// Reste a savoir si l'accident a eu lieu
					// dans la periode donnée
					if ((this.debutPeriode == null) && (finPeriode == null)) {
						// Pas de période donnée
						if (this.getTypeHabET().equals("Et")) {
							if (this.hasAllTypeHabilitationSelected(s)) {
								list.add(s);
							}
						} else {
							list.add(s);
						}
					} else {
						GregorianCalendar d1 = new GregorianCalendar();
						GregorianCalendar d2 = new GregorianCalendar();
						// Il faut regarder par rapport aux
						// dates
						if (debutPeriode == null) {
							// On initialise a une date tres
							// ancienne, (l'an 1000...)
							d1.setTime(new Date(1));
						} else {
							d1.setTime(debutPeriode);
						}
						if (finPeriode == null) {
							// Une date tres loin
							d2.setTime(new Date(Long.MAX_VALUE));
						} else {
							d2.setTime(finPeriode);
						}
						GregorianCalendar habDebut = new GregorianCalendar();
						habDebut.setTime(hab.getDelivrance());
						GregorianCalendar habFin = new GregorianCalendar();
						habFin.setTime(hab.getExpiration());
						// On Compare les dates
						// hab recouvre entierement periode
						// ou commence avant , fini dedans
						// ou commence dedans, fini après
						// ou est inclu dedans
						if (((d1.compareTo(habDebut) == 0) || habDebut
								.before(d1))
								&& ((d2.compareTo(habFin) == 0) || habFin
										.after(d2))
								|| (habDebut.before(d1) && habFin.after(d1) && habFin
										.before(d2))
								|| (habFin.after(d2) && habDebut.after(d1) && habDebut
										.before(d2))
								|| (habDebut.after(d1) && habDebut.before(d2)
										&& habFin.after(d1) && habFin
											.before(d2))) {
							if (this.getTypeHabET().equals("Et")) {
								if (this.hasAllTypeHabilitationSelected(s)) {
									list.add(s);
								}
							} else {
								list.add(s);
							}
						}
					}
				}
			}
		}
	}

	public void searchHabValide(List<SalarieBeanExport> list,
			HabilitationBean hab) throws Exception {
		for (int j = 0; j < salarieList.size(); j++) {
			SalarieBeanExport s = salarieList.get(j);
			if (s.getId() == hab.getIdSalarie()) {
				// On regarde s'il n'a pas ete ajouté
				// precedement
				if (!containSalarie(list, s)) {
					// Reste a savoir si l'habilitation
					// a eu
					// lieu dans la periode donnée
					if ((this.debutPeriode == null) && (finPeriode == null)) {
						// Pas de période donnée
						if (this.getTypeHabET().equals("Et")) {
							if (this.hasAllTypeHabilitationSelected(s)) {
								list.add(s);
							}
						} else {
							list.add(s);
						}
					} else {
						GregorianCalendar d1 = new GregorianCalendar();
						GregorianCalendar d2 = new GregorianCalendar();
						// Il faut regarder par rapport
						// aux
						// dates
						if (debutPeriode == null) {
							// On initialise a une date
							// tres
							// ancienne, (l'an 1000...)
							d1.setTime(new Date(1));
						} else {
							d1.setTime(debutPeriode);
						}
						if (finPeriode == null) {
							// Une date tres loin
							d2.setTime(new Date(Long.MAX_VALUE));
						} else {
							d2.setTime(finPeriode);
						}
						GregorianCalendar habDebut = new GregorianCalendar();
						habDebut.setTime(hab.getDelivrance());
						GregorianCalendar habFin = new GregorianCalendar();
						habFin.setTime(hab.getExpiration());
						// On Compare les dates
						if (((d1.compareTo(habDebut) == 0) || habDebut
								.before(d1))
								&& ((d2.compareTo(habFin) == 0) || habFin
										.after(d2))) {
							if (this.getTypeHabET().equals("Et")) {
								if (this.hasAllTypeHabilitationSelected(s)) {
									list.add(s);
								}
							} else {
								list.add(s);
							}
						}
					}
				}
			}
		}
	}

	private void initSalarieFromHabilitation() throws Exception {

		if ((this.idTypeHabSelected != null) && (idTypeHabSelected.length > 0)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Natures d'habilitations (s\u00e9lection en "
					+ this.getTypeHabET() + ")");
			String filtress = "";
			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();

			for (int i = 0; i < idTypeHabSelected.length; i++) {
				// Pour chaque Type d'habilitaion qui ont ete selectionné
				int idTypeHab = idTypeHabSelected[i];
				TypeHabilitationServiceImpl serv = new TypeHabilitationServiceImpl();
				filtress += serv.getTypeHabilitationBeanById(idTypeHab)
						.getNom() + "; ";
				HabilitationServiceImpl servHab = new HabilitationServiceImpl();
				// On recupere la liste des habilitation qui ont ce type
				List<HabilitationBean> listHab = servHab
						.getHabilitationsListFromTypeHabilitation(idTypeHab,
								Integer.parseInt(session.getAttribute("groupe")
										.toString()));
				for (int val = 0; val < listHab.size(); val++) {
					// Pour chaque habilitation de ce type, on regarde si un
					// salarie la possede
					HabilitationBean hab = listHab.get(val);
					if (habValide) {
						if (testHabilitationValide(hab)) {
							if (datePeremption != null) {
								if (testHabilitationPerimeAu(hab)) {
									searchHabValide(list, hab);
								} else {
									searchHabValide(list, hab);
								}
							}
						}

					} else {
						if (datePeremption != null) {
							if (testHabilitationPerimeAu(hab)) {
								searchHabNonValide(list, hab);
							}
						} else {
							searchHabNonValide(list, hab);
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			filtre.setValue(filtress);
			filtres.add(filtre);
			salarieList = list;

			List<HabilitationBean> listHab = new ArrayList<HabilitationBean>();

			for (SalarieBeanExport s : salarieList) {
				for (HabilitationBean h : s.getHabilitationBeanList()) {
					for (int i = 0; i < idTypeHabSelected.length; i++) {
						int idTypeHab = idTypeHabSelected[i];
						if (h.getIdTypeHabilitationSelected() == idTypeHab) {
							listHab.add(h);
						}
					}
				}
			}

			List<HabilitationBean> listHabTemp = new ArrayList<HabilitationBean>();
			for (HabilitationBean a : habilitationBeanList) {
				for (HabilitationBean a2 : listHab) {
					if (a.getId() == a2.getId()) {
						listHabTemp.add(a);
					}
				}
			}
			habilitationBeanList = listHabTemp;

		} else {
			if (habValide || datePeremption != null) {
				List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
				for (SalarieBeanExport s : salarieList) {
					for (HabilitationBean hab : s.getHabilitationBeanList()) {
						if (habValide) {
							if (testHabilitationValide(hab)) {
								if (datePeremption != null) {
									if (testHabilitationPerimeAu(hab)) {
										list.add(s);
										break;
									}
								} else {
									list.add(s);
									break;
								}
							}

						} else {
							if (datePeremption != null) {
								if (testHabilitationPerimeAu(hab)) {
									list.add(s);
									break;
								}
							}
						}
					}
				}
				salarieList = list;

				List<HabilitationBean> listHab = new ArrayList<HabilitationBean>();

				for (SalarieBeanExport s : salarieList) {
					for (HabilitationBean h : s.getHabilitationBeanList()) {
						if (habValide) {
							if (testHabilitationValide(h)) {
								if (datePeremption != null) {
									if (testHabilitationPerimeAu(h)) {
										listHab.add(h);
									}
								} else {
									listHab.add(h);
								}
							}
						} else {
							if (datePeremption != null) {
								if (testHabilitationPerimeAu(h)) {
									listHab.add(h);
								}
							}
						}
					}
				}

				List<HabilitationBean> listHabTemp = new ArrayList<HabilitationBean>();
				for (HabilitationBean h : habilitationBeanList) {
					for (HabilitationBean h2 : listHab) {
						if (h.getId() == h2.getId()) {
							listHabTemp.add(h);
						}
					}
				}
				habilitationBeanList = listHabTemp;

			}
		}
		if (habValide) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Habilitations valides sur la période de recherche");
			filtre.setValue("Oui");
			filtres.add(filtre);
		}
		if (datePeremption != null) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Habilitations périmées au");
			filtre.setValue("" + dateFormat.format(datePeremption));
			filtres.add(filtre);
		}
	}

	private void initSalarieFromEntretien() throws Exception {

		if ((debutPeriode != null) || (finPeriode != null)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Entretiens");
			String filtress = "";
			if (debutPeriode != null) {
				GregorianCalendar d1 = new GregorianCalendar();
				d1.setTime(debutPeriode);
				filtress += "du " + dateFormat.format(d1.getTime()) + "; ";
			}
			if (finPeriode != null) {
				GregorianCalendar d2 = new GregorianCalendar();
				d2.setTime(finPeriode);
				filtress += "au " + dateFormat.format(d2.getTime()) + "; ";
			}
			// Une mise a jour des salaries est necessaire
			List<SalarieBeanExport> listSalar = new ArrayList<SalarieBeanExport>();
			EntretienServiceImpl serv = new EntretienServiceImpl();
			// List<EntretienBean> list = serv.getEntretiensList();
			List<EntretienBean> list = new ArrayList<EntretienBean>();
			for (SalarieBeanExport s : salarieList) {
				list.addAll(s.getEntretienBeanList());
			}
			GregorianCalendar d1 = new GregorianCalendar();
			GregorianCalendar d2 = new GregorianCalendar();
			// Il faut regarder par rapport aux dates
			if (debutPeriode == null) {
				// On initialise a une date tres ancienne
				d1.setTime(new Date(1));
			} else {
				d1.setTime(debutPeriode);
			}
			if (finPeriode == null) {
				// Une date tres loin
				d2.setTime(new Date(Long.MAX_VALUE));
			} else {
				d2.setTime(finPeriode);
			}
			for (int i = 0; i < list.size(); i++) {
				EntretienBean entretien = list.get(i);

				GregorianCalendar DateEntretien = new GregorianCalendar();
				DateEntretien.setTime(entretien.getDateEntretien());
				// On Compare les dates
				if (before(d1, DateEntretien)) {
					if (before(DateEntretien, d2)) {
						SalarieServiceImpl salarServ = new SalarieServiceImpl();
						SalarieBeanExport s = salarServ
								.getSalarieBeanExportById(entretien
										.getIdSalarie());
						if (!containSalarie(listSalar, s)) {
							listSalar.add(s);
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			salarieList = listSalar;
			filtre.setValue(filtress);
			filtres.add(filtre);
		}
	}

	private boolean useDIFuseFinancement(FormationBean form) {
		boolean typeFinancementOP = true;
		boolean typeFinancementEn = true;
		boolean typeFinancementAu = true;
		if (useDIF) {
			if (form.getDif() != null && form.getDif() > 0) {
				if (selectedTypeFinancement != null
						&& selectedTypeFinancement.length > 0) {
					for (int j = 0; j < selectedTypeFinancement.length; j++) {
						if (selectedTypeFinancement[j].equals("OPCA"))
							if (form.getCoutOpca() == null
									|| form.getCoutOpca() == 0) {
								typeFinancementOP = false;
								break;
							}
						if (selectedTypeFinancement[j].equals("Autre"))
							if (form.getCoutAutre() == null
									|| form.getCoutAutre() == 0) {
								typeFinancementEn = false;
								break;
							}
						if (selectedTypeFinancement[j].equals("Entreprise"))
							if (form.getCoutEntreprise() == null
									|| form.getCoutEntreprise() == 0) {
								typeFinancementAu = false;
								break;
							}
					}
					if (selectedTypeFinancement.length == 3) {
						return typeFinancementOP || typeFinancementEn
								|| typeFinancementAu;
					}
					if (selectedTypeFinancement.length == 2) {
						if ((selectedTypeFinancement[0].equals("OPCA") && selectedTypeFinancement[1]
								.equals("Entreprise"))
								|| (selectedTypeFinancement[0]
										.equals("Entreprise") && selectedTypeFinancement[1]
										.equals("OPCA"))) {
							return typeFinancementOP || typeFinancementEn;
						}
						if ((selectedTypeFinancement[0].equals("OPCA") && selectedTypeFinancement[1]
								.equals("Autre"))
								|| (selectedTypeFinancement[0].equals("Autre") && selectedTypeFinancement[1]
										.equals("OPCA"))) {
							return typeFinancementOP || typeFinancementAu;
						}
						if ((selectedTypeFinancement[0].equals("Autre") && selectedTypeFinancement[1]
								.equals("Entreprise"))
								|| (selectedTypeFinancement[0]
										.equals("Entreprise") && selectedTypeFinancement[1]
										.equals("Autre"))) {
							return typeFinancementAu || typeFinancementEn;
						}
					}

					if (selectedTypeFinancement.length == 1) {
						if (selectedTypeFinancement[0].equals("Autre")) {
							return typeFinancementAu;
						}
						if (selectedTypeFinancement[0].equals("OPCA")) {
							return typeFinancementOP;
						}
						if (selectedTypeFinancement[0].equals("Entreprise")) {
							return typeFinancementEn;
						}
					}
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			if (selectedTypeFinancement != null
					&& selectedTypeFinancement.length > 0) {
				for (int j = 0; j < selectedTypeFinancement.length; j++) {
					if (selectedTypeFinancement[j].equals("OPCA"))
						if (form.getCoutOpca() == null
								|| form.getCoutOpca() == 0) {
							typeFinancementOP = false;
						}
					if (selectedTypeFinancement[j].equals("Autre"))
						if (form.getCoutAutre() == null
								|| form.getCoutAutre() == 0) {
							typeFinancementAu = false;
						}
					if (selectedTypeFinancement[j].equals("Entreprise"))
						if (form.getCoutEntreprise() == null
								|| form.getCoutEntreprise() == 0) {
							typeFinancementEn = false;
						}
				}
				if (financementET.equals("Et")) {
					return typeFinancementOP && typeFinancementEn
							&& typeFinancementAu;
				} else {
					if (selectedTypeFinancement.length == 3) {
						return typeFinancementOP || typeFinancementEn
								|| typeFinancementAu;
					}
					if (selectedTypeFinancement.length == 2) {
						if ((selectedTypeFinancement[0].equals("OPCA") && selectedTypeFinancement[1]
								.equals("Entreprise"))
								|| (selectedTypeFinancement[0]
										.equals("Entreprise") && selectedTypeFinancement[1]
										.equals("OPCA"))) {
							return typeFinancementOP || typeFinancementEn;
						}
						if ((selectedTypeFinancement[0].equals("OPCA") && selectedTypeFinancement[1]
								.equals("Autre"))
								|| (selectedTypeFinancement[0].equals("Autre") && selectedTypeFinancement[1]
										.equals("OPCA"))) {
							return typeFinancementOP || typeFinancementAu;
						}
						if ((selectedTypeFinancement[0].equals("Autre") && selectedTypeFinancement[1]
								.equals("Entreprise"))
								|| (selectedTypeFinancement[0]
										.equals("Entreprise") && selectedTypeFinancement[1]
										.equals("Autre"))) {
							return typeFinancementAu || typeFinancementEn;
						}
					}

					if (selectedTypeFinancement.length == 1) {
						if (selectedTypeFinancement[0].equals("Autre")) {
							return typeFinancementAu;
						}
						if (selectedTypeFinancement[0].equals("OPCA")) {
							return typeFinancementOP;
						}
						if (selectedTypeFinancement[0].equals("Entreprise")) {
							return typeFinancementEn;
						}
					}
					return false;
				}
			} else {
				return true;
			}
		}
	}

	private boolean hasFormation(SalarieBeanExport s, int f) throws Exception {
		boolean typeFinancementOk = true;
		FormationServiceImpl serv = new FormationServiceImpl();
		List<FormationBean> listFormationSalarie = serv
				.getFormationsOfSalarieList(s.getId().intValue());
		for (int i = 0; i < listFormationSalarie.size(); i++) {
			FormationBean form = listFormationSalarie.get(i);
			if (form.getId() == f) {
				return useDIFuseFinancement(form);
			}
		}
		return false;
	}

	private boolean hasAllFormationSelected(SalarieBeanExport s)
			throws Exception {
		for (int i = 0; i < this.idFormationSelected.length; i++) {
			if (!hasFormation(s, idFormationSelected[i])) {
				return false;
			}
		}
		return true;
	}

	private void initSalarieFromFormation() throws Exception {

		Filtre filtre = new Filtre();
		String filtress = "";
		if ((this.idFormationSelected != null)
				&& (idFormationSelected.length > 0)) {
			filtre.setFiltre("Formations (s\u00e9lection en "
					+ this.getFormationET() + ")");

			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
			for (int i = 0; i < idFormationSelected.length; i++) {
				// Pour chaque Type de formation qui ont ete selectionné
				int idTypeForm = idFormationSelected[i];
				DomaineFormationServiceImpl serv = new DomaineFormationServiceImpl();
				filtress += serv.getDomaineFormationBeanById(idTypeForm)
						.getNom() + "; ";
				FormationServiceImpl servForm = new FormationServiceImpl();
				// On recupere la liste des formations qui ont ce type
				List<FormationBean> formationList = servForm
						.getFormationBeanByIdType(idTypeForm, Integer
								.parseInt(session.getAttribute("groupe")
										.toString()));
				for (int j = 0; j < salarieList.size(); j++) {
					SalarieBeanExport s = salarieList.get(j);
					for (FormationBean formation : formationList) {
						if (s.getId() == formation.getIdSalarie()) {
							// On regarde s'il n'a pas ete ajouté precedement
							if (!containSalarie(list, s)) {
								// Reste a savoir si l'accident a eu lieu dans
								// la
								// periode donnée
								if ((this.debutPeriode == null)
										&& (finPeriode == null)) {
									// Pas de période donnée
									if (this.getFormationET().equals("Et")) {
										if (this.hasAllFormationSelected(s)) {
											list.add(s);
										}
									} else {
										if (useDIFuseFinancement(formation)) {
											list.add(s);
										}
									}
								} else {
									GregorianCalendar d1 = new GregorianCalendar();
									GregorianCalendar d2 = new GregorianCalendar();
									// Il faut regarder par rapport aux dates
									if (debutPeriode == null) {
										// On initialise a une date tres
										// ancienne,
										// (l'an 1000...)
										d1.setTime(new Date(1));
									} else {
										d1.setTime(debutPeriode);
									}
									if (finPeriode == null) {
										// Une date tres loin
										d2.setTime(new Date(Long.MAX_VALUE));
									} else {
										d2.setTime(finPeriode);
									}
									GregorianCalendar formDebut = new GregorianCalendar();
									formDebut.setTime(formation
											.getDebutFormation());
									GregorianCalendar formFin = new GregorianCalendar();
									formFin.setTime(formation.getFinFormation());
									// On Compare les dates
									if (isInPeriode(d1, d2, formDebut, formFin)) {
										if (this.getFormationET().equals("Et")) {
											if (this.hasAllFormationSelected(s)) {
												list.add(s);
											}
										} else {
											if (useDIFuseFinancement(formation)) {
												list.add(s);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			salarieList = list;
			filtre.setValue(filtress);
			filtres.add(filtre);

			List<FormationBean> listForm = new ArrayList<FormationBean>();

			for (SalarieBeanExport s : salarieList) {
				for (FormationBean f : s.getFormationBeanList()) {
					for (int i = 0; i < idFormationSelected.length; i++) {
						int idTypeForm = idFormationSelected[i];
						if (f.getIdDomaineFormationBeanSelected() == idTypeForm) {
							listForm.add(f);
						}
					}
				}
			}

			List<FormationBean> listFormTemp = new ArrayList<FormationBean>();
			for (FormationBean f : formationBeanList) {
				for (FormationBean f2 : listForm) {
					if (f.getId() == f2.getId()) {
						listFormTemp.add(f);
					}
				}
			}
			formationBeanList = listFormTemp;

		} else {
			if (useDIF
					|| (selectedTypeFinancement != null && selectedTypeFinancement.length > 0)) {
				List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
				for (SalarieBeanExport s : salarieList) {
					for (FormationBean f : s.getFormationBeanList()) {
						if (useDIFuseFinancement(f)) {
							list.add(s);
							break;
						}
					}
				}
				salarieList = list;

				List<FormationBean> listForm = new ArrayList<FormationBean>();

				for (SalarieBeanExport s : salarieList) {
					for (FormationBean f : s.getFormationBeanList()) {
						if (useDIFuseFinancement(f)) {
							listForm.add(f);
						}
					}
				}

				List<FormationBean> listFormTemp = new ArrayList<FormationBean>();
				for (FormationBean f : formationBeanList) {
					for (FormationBean f2 : listForm) {
						if (f.getId() == f2.getId()) {
							listFormTemp.add(f);
						}
					}
				}
				formationBeanList = listFormTemp;
			}
		}
		if (useDIF) {
			filtre.setFiltre("Utilisation du DIF");
			filtre.setValue("Oui");
			filtres.add(filtre);
		}
		if (selectedTypeFinancement != null
				&& selectedTypeFinancement.length > 0) {
			filtre.setFiltre("Type de financement");
			filtress = "";
			for (String s : selectedTypeFinancement) {
				filtress += s + "; ";
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			filtre.setValue(filtress);
			filtres.add(filtre);
		}
	}

	public String exportExcelSalaries() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		int j = 0;

		String[] entete = new String[20];
		entete = new String[21];
		entete[j] = "Entreprise";
		j++;
		entete[j] = "Nom";
		entete[j + 1] = "Pr\u00E9nom";
		entete[j + 2] = "Sexe";
		entete[j + 3] = "Date de naissance";
		entete[j + 4] = "Age";
		entete[j + 5] = "Adresse";
		entete[j + 6] = "T\u00E9l fixe";
		entete[j + 7] = "T\u00E9l portable";
		entete[j + 8] = "Personne \u00e0 pr\u00E9venir";
		entete[j + 9] = "";
		entete[j + 10] = "";
		entete[j + 11] = "Entr\u00E9e dans l'entreprise";
		entete[j + 12] = "Sortie de l'entreprise";
		entete[j + 13] = "Anciennet\u00E9 dans l'entreprise";
		entete[j + 14] = "Motif de la rupture du contrat de travail";
		entete[j + 15] = "Type de contrat";
		entete[j + 16] = "Poste occup\u00E9";
		entete[j + 17] = "Service";
		// entete[8] = "Entr\u00E9e dans le poste";
		// entete[9] = "Nombre d'hrs de formation cumul\u00E9es";
		// entete[10] = "Solde DIF";
		entete[j + 18] = "Equivalent temps plein";
		entete[j + 19] = "Catégorie Socio-prof.";
		try {
			eContext.getSessionMap().put("datatable", salarieList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Filtre multi");
			eContext.getSessionMap().put("filtres", filtres);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelAbsences() {

		// displayListID(absenceBeanList, "");

		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[11];
		entete[0] = "Ann\u00E9e de r\u00E9f\u00E9rence";
		entete[1] = "Entreprise";
		entete[2] = "Nom";
		entete[3] = "Pr\u00E9nom";
		entete[4] = "Sexe";
		entete[5] = "Service";
		entete[6] = "Poste occup\u00E9";
		entete[7] = "Nature de l'absence ou du cong\u00E9";
		entete[8] = "Début de l'absence";
		entete[9] = "Fin de l'absence";
		entete[10] = "Durée (jours ouvr\u00E9s)";

		try {
			eContext.getSessionMap().put("datatable", salarieList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelAbsencesList");
			eContext.getSessionMap().put("list", absenceBeanList);
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", null);
			eContext.getSessionMap().put("present", 10);
			if (debutPeriode == null) {
				eContext.getSessionMap().put("debut", null);
			} else {
				debutExtraction.setTime(debutPeriode);
				eContext.getSessionMap().put("debut", debutExtraction);
			}
			if (finPeriode == null) {
				eContext.getSessionMap().put("fin", null);
			} else {
				finExtraction.setTime(finPeriode);
				eContext.getSessionMap().put("fin", finExtraction);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelAccidents() {

		// displayListID(accidentBeanList, "");

		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[15];
		entete[0] = "Ann\u00E9e de r\u00E9f\u00E9rence";
		entete[1] = "Entreprise";
		entete[2] = "Nom";
		entete[3] = "Pr\u00E9nom";
		entete[4] = "Service";
		entete[5] = "Poste occup\u00E9";
		entete[6] = "Type d'accident";
		entete[7] = "Date de l'accident";
		entete[8] = "";
		entete[9] = "P\u00E9riode d'absence";
		entete[10] = "";
		entete[11] = "Nbre de jrs d'arr\u00EAt de travail (jrs ouvr\u00E9s)";
		entete[12] = "Si\u00E8ge de la l\u00E9sion";
		entete[13] = "";
		entete[14] = "Cause de l'accident";

		try {
			eContext.getSessionMap().put("datatable", salarieList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelAccidentsList");
			eContext.getSessionMap().put("list", accidentBeanList);
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", null);
			eContext.getSessionMap().put("present", 10);
			if (debutPeriode == null) {
				eContext.getSessionMap().put("debut", null);
			} else {
				debutExtraction.setTime(debutPeriode);
				eContext.getSessionMap().put("debut", debutExtraction);
			}
			if (finPeriode == null) {
				eContext.getSessionMap().put("fin", null);
			} else {
				finExtraction.setTime(finPeriode);
				eContext.getSessionMap().put("fin", finExtraction);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelContratTravail() {

		// displayListID(accidentBeanList, "");

		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[11];
		entete[0] = "Entreprise";
		entete[1] = "Nom";
		entete[2] = "Pr\u00E9nom";
		entete[3] = "Service";
		entete[4] = "Date d'entr\u00E9e dans l'entreprise";
		entete[5] = "Date de sortie de l'entreprise";
		entete[6] = "Date de d\u00E9but de contrat";
		entete[7] = "Date de fin de contrat";
		entete[8] = "Type de contrat de travail";
		entete[9] = "Motif de rupture";
		entete[10] = "Anciennet\u00E9 dans l'entreprise";

		try {
			eContext.getSessionMap().put("datatable", salarieList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name",
					"exportExcelContratTravailList");
			eContext.getSessionMap().put("list", contratBeanList);
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", null);
			eContext.getSessionMap().put("present", 10);
			if (debutPeriode == null) {
				eContext.getSessionMap().put("debut", null);
			} else {
				debutExtraction.setTime(debutPeriode);
				eContext.getSessionMap().put("debut", debutExtraction);
			}
			if (finPeriode == null) {
				eContext.getSessionMap().put("fin", null);
			} else {
				finExtraction.setTime(finPeriode);
				eContext.getSessionMap().put("fin", finExtraction);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelFormations() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[19];
		entete[0] = "Ann\u00E9e de r\u00E9f\u00E9rence";
		entete[1] = "Entreprise";
		entete[2] = "Nom";
		entete[3] = "Pr\u00E9nom";
		entete[4] = "Service";
		entete[5] = "Poste occup\u00E9";
		// entete[3] = "Besoins en formation";
		entete[6] = "Formations réalisées";
		entete[7] = "";
		entete[8] = "";
		entete[9] = "";
		entete[10] = "Nature de la formation";
		entete[11] = "Niveau de formation";
		entete[12] = "";
		entete[13] = "Organisme de formation";
		entete[14] = "Coût de la formation en €";
		entete[15] = "";
		entete[16] = "";
		entete[17] = "Gestion du DIF";
		entete[18] = "";

		try {
			eContext.getSessionMap().put("datatable", salarieList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelFormationsList");
			eContext.getSessionMap().put("list", formationBeanList);
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", null);
			eContext.getSessionMap().put("present", 10);
			if (debutPeriode == null) {
				eContext.getSessionMap().put("debut", null);
			} else {
				debutExtraction.setTime(debutPeriode);
				eContext.getSessionMap().put("debut", debutExtraction);
			}
			if (finPeriode == null) {
				eContext.getSessionMap().put("fin", null);
			} else {
				finExtraction.setTime(finPeriode);
				eContext.getSessionMap().put("fin", finExtraction);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelHabilitations() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[13];
		entete[0] = "Ann\u00E9e de r\u00E9f\u00E9rence";
		entete[1] = "Entreprise";
		entete[2] = "Nom";
		entete[3] = "Pr\u00E9nom";
		entete[4] = "Date de naissance";
		entete[5] = "Lieu de naissance";
		entete[6] = "Service";
		entete[7] = "Poste occup\u00E9";
		entete[8] = "Type d'habilitation";
		entete[9] = "Période de Validit\u00E9";
		entete[10] = "";
		entete[11] = "D\u00E9lai de validit\u00E9 (en jours) ";
		entete[12] = "Commentaires";

		try {
			eContext.getSessionMap().put("datatable", salarieList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap()
					.put("name", "exportExcelHabilitationsList");
			eContext.getSessionMap().put("list", habilitationBeanList);
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", null);
			eContext.getSessionMap().put("present", 10);
			if (debutPeriode == null) {
				eContext.getSessionMap().put("debut", null);
			} else {
				debutExtraction.setTime(debutPeriode);
				eContext.getSessionMap().put("debut", debutExtraction);
			}
			if (finPeriode == null) {
				eContext.getSessionMap().put("fin", null);
			} else {
				finExtraction.setTime(finPeriode);
				eContext.getSessionMap().put("fin", finExtraction);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelRemunerations() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[24];
		entete[0] = "Annee";
		entete[1] = "Entreprise";
		entete[2] = "Nom";
		entete[3] = "Prenom";
		entete[4] = "Metier";
		entete[5] = "CSP";
		entete[6] = "Echelon";
		entete[7] = "Niveau";
		entete[8] = "Coef.";
		entete[9] = "Horaire mensuel";
		entete[10] = "Taux horaire brut €";
		entete[11] = "Salaire brut mensuel €";
		entete[12] = "Augmentation collective";
		entete[13] = "Augmentation individuelle";
		entete[14] = "Heures supp.";
		entete[15] = "SALAIRE BRUT ANNUEL";
		entete[16] = "Commissions";
		entete[17] = "Primes fixes";
		entete[18] = "Primes variables";
		entete[19] = "Avantages assujettis";
		entete[20] = "REMUNERATION BRUTE ANNUELLE";
		entete[21] = "Avantages non assujettis";
		entete[22] = "Frais professionnels";
		entete[23] = "TOTAL BRUT ANNUEL €";

		try {
			eContext.getSessionMap().put("datatable", salarieList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelRemunerations");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", null);
			eContext.getSessionMap().put("present", 10);
			if (debutPeriode == null) {
				eContext.getSessionMap().put("debut", null);
			} else {
				debutExtraction.setTime(debutPeriode);
				eContext.getSessionMap().put("debut", debutExtraction);
			}
			if (finPeriode == null) {
				eContext.getSessionMap().put("fin", null);
			} else {
				finExtraction.setTime(finPeriode);
				eContext.getSessionMap().put("fin", finExtraction);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelParcoursProfessionnels() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[17];
		entete[0] = "Entreprise";
		entete[1] = "Nom";
		entete[2] = "Pr\u00E9nom";
		entete[3] = "Service";
		entete[4] = "Date d'entr\u00E9e dans l'entreprise";
		entete[5] = "Date de sortie de l'entreprise";
		entete[6] = "Anciennet\u00E9 dans l'entreprise";
		entete[7] = "Poste occup\u00E9/m\u00E9tier";
		entete[8] = "D\u00E9but de prise de fonction";
		entete[9] = "Fin de prise de fonction";
		entete[10] = "Anciennet\u00E9 dans le m\u00E9tier";
		entete[11] = "Type de contrat de travail";
		entete[12] = "ETP";
		entete[13] = "CSP";
		entete[14] = "Coef.";
		entete[15] = "Niv.";
		entete[16] = "Ech.";

		try {
			eContext.getSessionMap().put("datatable", salarieList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name",
					"exportExcelParcoursProfessionnelsList");
			eContext.getSessionMap().put("list", parcoursBeanList);
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", null);
			eContext.getSessionMap().put("present", 10);
			if (debutPeriode == null) {
				eContext.getSessionMap().put("debut", null);
			} else {
				debutExtraction.setTime(debutPeriode);
				eContext.getSessionMap().put("debut", debutExtraction);
			}
			if (finPeriode == null) {
				eContext.getSessionMap().put("fin", null);
			} else {
				finExtraction.setTime(finPeriode);
				eContext.getSessionMap().put("fin", finExtraction);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public String exportExcelCompetences() {

		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[0];

		try {
			eContext.getSessionMap().put("datatable", salarieList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelCompetences");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("alphabet", null);
			eContext.getSessionMap().put("present", 10);
			if (debutPeriode == null) {
				eContext.getSessionMap().put("debut", null);
			} else {
				debutExtraction.setTime(debutPeriode);
				eContext.getSessionMap().put("debut", debutExtraction);
			}
			if (finPeriode == null) {
				eContext.getSessionMap().put("fin", null);
			} else {
				finExtraction.setTime(finPeriode);
				eContext.getSessionMap().put("fin", finExtraction);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return "";
	}

	public String exportExcelEntretiens() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportExcelForSuiviServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[14];
		entete[0] = "Ann\u00E9e de r\u00E9f\u00E9rence";
		entete[1] = "Entreprise";
		entete[2] = "Nom";
		entete[3] = "Pr\u00E9nom";
		entete[4] = "Service";
		entete[5] = "Poste occup\u00E9";
		entete[6] = "Objectifs de l'ann\u00E9e N";
		entete[7] = "Evaluation globale du niveau de comp\u00E9tences au poste occup\u00E9";
		entete[8] = "Comp\u00E9tences à am\u00E9liorer";
		entete[9] = "Comp\u00E9tences à acqu\u00E9rir";
		entete[10] = "D\u00E9cision d'\u00E9volution";
		entete[11] = "Besoins en formation";
		// entete[12] = "Formations programm\u00E9es";
		// entete[] = "Souhaits exprim\u00E9s";
		// entete[] = "Evolution de carri\u00E8re";
		entete[12] = "CR sign\u00E9";
		entete[13] = "Fiche de poste modifi\u00E9e";

		try {
			eContext.getSessionMap().put("datatable", salarieList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "exportExcelEntretiensList");
			eContext.getSessionMap().put("idEntreprise", idEntrepriseSelected);
			eContext.getSessionMap().put("list", entretienBeanList);
			eContext.getSessionMap().put("alphabet", null);
			eContext.getSessionMap().put("present", 10);
			if (debutPeriode == null) {
				eContext.getSessionMap().put("debut", null);
			} else {
				debutExtraction.setTime(debutPeriode);
				eContext.getSessionMap().put("debut", debutExtraction);
			}
			if (finPeriode == null) {
				eContext.getSessionMap().put("fin", null);
			} else {
				finExtraction.setTime(finPeriode);
				eContext.getSessionMap().put("fin", finExtraction);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public void updateDate(ValueChangeEvent evt) {
		if (evt.getComponent().getId().equals("debutPeriode")) {
			this.debutPeriode = (Date) evt.getNewValue();
		}
		if (evt.getComponent().getId().equals("finPeriode")) {
			this.finPeriode = (Date) evt.getNewValue();
		}

	}

	private void initSalarieFromNiveauFormation() throws Exception {
		if ((this.idNiveauFormSelected != null)
				&& (idNiveauFormSelected.length > 0)) {
			Filtre filtre = new Filtre();
			filtre.setFiltre("Niveaux de formation");
			String filtress = "";

			List<SalarieBeanExport> list = new ArrayList<SalarieBeanExport>();
			for (int i = 0; i < idNiveauFormSelected.length; i++) {
				// Pour chaque Type de niveau qui ont ete selectionné
				String nivForm = idNiveauFormSelected[i];
				filtress += nivForm + "; ";
				for (int j = 0; j < salarieList.size(); j++) {
					SalarieBeanExport s = salarieList.get(j);
					if (s.getNivFormationAtteint().equals(nivForm)) {
						// On regarde s'il n'a pas ete ajouté precedement
						if (!containSalarie(list, s)) {
							list.add(s);
						}
					}
				}
			}
			filtress = filtress.substring(0, filtress.length() - 2);
			salarieList = list;
			filtre.setValue(filtress);
			filtres.add(filtre);
		}
	}

	private boolean containSalarie(List<SalarieBeanExport> l,
			SalarieBeanExport s) {
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getId().intValue() == s.getId().intValue()) {
				return true;
			}
		}
		return false;
	}

	private boolean containIdSalarie(List<SalarieBeanExport> l, Integer id) {
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getId().intValue() == id.intValue()) {
				return true;
			}
		}
		return false;
	}

	public String retourRecherche() throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		if (session.getAttribute("retour") != null) {
			session.setAttribute("retour", null);
			return rechercher();
		} else {
			return "";
		}

	}

	public boolean isRetourSession() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		if (session.getAttribute("retour") != null
				&& (Boolean) session.getAttribute("retour") == true) {
			return true;
		} else {
			return false;
		}
	}

	public void setRetourSession(ActionEvent evt) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		session.setAttribute("retour", true);
	}

	public String initList() throws Exception {

		debutPeriode = null;
		finPeriode = null;

		debutExtraction = new GregorianCalendar();
		finExtraction = new GregorianCalendar();

		salarieList = new ArrayList<SalarieBeanExport>();
		parcoursBeanList = new ArrayList<ParcoursBean>();
		contratBeanList = new ArrayList<ContratTravailBean>();
		absenceBeanList = new ArrayList<AbsenceBean>();
		accidentBeanList = new ArrayList<AccidentBean>();
		habilitationBeanList = new ArrayList<HabilitationBean>();
		formationBeanList = new ArrayList<FormationBean>();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		if (session.getAttribute("retour") != null) {
			session.setAttribute("retour", null);
		}

		servicesList = new ArrayList<SelectItem>();

		EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();

		List<EntrepriseBean> entrepriseBeanList = entrepriseService
				.getEntreprisesList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		Collections.sort(entrepriseBeanList);
		entreprisesList = new ArrayList<SelectItem>();
		GroupeServiceImpl grServ = new GroupeServiceImpl();
		SelectItem selectItem = new SelectItem();
		GroupeBean groupe = grServ.getGroupeBeanById(Integer.parseInt(session
				.getAttribute("groupe").toString()));
		// if (list.size() > 0) {
		selectItem = new SelectItem();
		selectItem.setValue(-1);
		selectItem.setLabel(groupe.getNom());
		entreprisesList.add(selectItem);
		// } else {
		// selectItem = new SelectItem();
		// selectItem.setValue(-1);
		// selectItem.setLabel("Groupe");
		// entreprisesList.add(selectItem);
		// }
		for (EntrepriseBean entrepriseBean : entrepriseBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(entrepriseBean.getId());
			selectItem.setLabel(entrepriseBean.getNom());
			entreprisesList.add(selectItem);
		}

		// metiersList = new ArrayList<SelectItem>();

		StatutServiceImpl statutService = new StatutServiceImpl();
		List<StatutBean> statutBeanList = statutService.getStatutsList();
		statutsList = new ArrayList<SelectItem>();
		for (StatutBean stautBean : statutBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(stautBean.getId());
			selectItem.setLabel(stautBean.getNom());
			statutsList.add(selectItem);
		}

		TypeContratServiceImpl contratService = new TypeContratServiceImpl();
		List<TypeContratBean> contratBeanList = contratService
				.getTypeContratList();
		contratsList = new ArrayList<SelectItem>();
		for (TypeContratBean contratBean : contratBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(contratBean.getId());
			selectItem.setLabel(contratBean.getNom());
			contratsList.add(selectItem);
		}

		TypeAccidentServiceImpl accService = new TypeAccidentServiceImpl();
		List<TypeAccidentBean> accidentBeanList = accService
				.getTypeAccidentList();
		typeAccidentList = new ArrayList<SelectItem>();
		for (TypeAccidentBean accBean : accidentBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(accBean.getId());
			selectItem.setLabel(accBean.getNom());
			typeAccidentList.add(selectItem);
		}

		TypeCauseAccidentServiceImpl causeAccService = new TypeCauseAccidentServiceImpl();
		List<TypeCauseAccidentBean> causeAccidentBeanList = causeAccService
				.getTypeCauseAccidentList();
		typeCauseAccidentList = new ArrayList<SelectItem>();
		for (TypeCauseAccidentBean causeAccBean : causeAccidentBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(causeAccBean.getId());
			selectItem.setLabel(causeAccBean.getNom());
			typeCauseAccidentList.add(selectItem);
		}

		TypeAbsenceServiceImpl causeAbsService = new TypeAbsenceServiceImpl();
		List<TypeAbsenceBean> typeAbsenceBeanList = causeAbsService
				.getTypeAbsenceList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		typeAbsenceList = new ArrayList<SelectItem>();
		for (TypeAbsenceBean absBean : typeAbsenceBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(absBean.getId());
			selectItem.setLabel(absBean.getNom());
			typeAbsenceList.add(selectItem);
		}

		TypeLesionServiceImpl causeLesionService = new TypeLesionServiceImpl();
		List<TypeLesionBean> typeLesionBeanList = causeLesionService
				.getTypeLesionList();
		typeLesionList = new ArrayList<SelectItem>();
		for (TypeLesionBean lesionBean : typeLesionBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(lesionBean.getId());
			selectItem.setLabel(lesionBean.getNom());
			typeLesionList.add(selectItem);
		}

		TypeHabilitationServiceImpl causeHabService = new TypeHabilitationServiceImpl();
		List<TypeHabilitationBean> typeHabBeanList = causeHabService
				.getTypeHabilitationList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		typeHabList = new ArrayList<SelectItem>();
		for (TypeHabilitationBean habBean : typeHabBeanList) {
			selectItem = new SelectItem();
			selectItem.setValue(habBean.getId());
			selectItem.setLabel(habBean.getNom());
			typeHabList.add(selectItem);
		}

		FormationServiceImpl formationService = new FormationServiceImpl();
		List<FormationBean> formList = formationService
				.getFormationsList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		formationList = new ArrayList<SelectItem>();
		for (FormationBean formBean : formList) {
			selectItem = new SelectItem();
			selectItem.setValue(formBean.getId());
			selectItem.setLabel(formBean.getNomFormation() + "");
			formationList.add(selectItem);
		}

		DomaineFormationServiceImpl domaineFormationService = new DomaineFormationServiceImpl();
		List<DomaineFormationBean> domaineFormList = domaineFormationService
				.getDomaineFormationsList();
		domaineFormationList = new ArrayList<SelectItem>();
		for (DomaineFormationBean formBean : domaineFormList) {
			selectItem = new SelectItem();
			selectItem.setValue(formBean.getId());
			selectItem.setLabel(formBean.getNom() + "");
			domaineFormationList.add(selectItem);
		}

		situationFamilleList = new ArrayList<SelectItem>();
		situationFamilleList.add(new SelectItem("Célibataire", "Célibataire"));
		situationFamilleList.add(new SelectItem("Marié (e)", "Marié(e)"));
		situationFamilleList.add(new SelectItem("Divorcé (e)", "Divorcé(e)"));
		situationFamilleList.add(new SelectItem("Veuf (ve)", "Veuf(ve)"));
		situationFamilleList.add(new SelectItem("Pacsé (e)", "Pacsé(e)"));
		situationFamilleList.add(new SelectItem("Séparé (e)", "Séparé(e)"));

		MetierServiceImpl metierService = new MetierServiceImpl();
		List<MetierBean> metierList = metierService.getMetiersList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		metiersList = new ArrayList<SelectItem>();
		for (MetierBean metierBean : metierList) {
			selectItem = new SelectItem();
			selectItem.setValue(metierBean.getId());
			selectItem.setLabel(metierBean.getNom() + "");
			metiersList.add(selectItem);
		}

		FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
		FicheMetierEntrepriseServiceImpl ficheMetierEntrepriseService = new FicheMetierEntrepriseServiceImpl();

		List<FicheMetierBean> ficheMetierList = new ArrayList<FicheMetierBean>();

		List<FicheMetierEntrepriseBean> ficheMetierEntrepriseBeanList = ficheMetierEntrepriseService
				.getFicheMetierEntrepriseList(Integer.parseInt(session
						.getAttribute("groupe").toString()));

		for (FicheMetierEntrepriseBean fme : ficheMetierEntrepriseBeanList) {
			ficheMetierList.add(ficheMetierService.getFicheMetierBeanById(fme
					.getIdFicheMetier()));
		}

		metiersListFDP = new ArrayList<SelectItem>();
		for (FicheMetierBean ficheMetierBean : ficheMetierList) {
			selectItem = new SelectItem();
			selectItem.setValue(ficheMetierBean.getId());
			selectItem.setLabel(ficheMetierBean.getNom() + "");
			metiersListFDP.add(selectItem);
		}

		ParcoursServiceImpl parServ = new ParcoursServiceImpl();
		List<ParcoursBean> parcoursList = parServ.getParcourssList(Integer
				.parseInt(session.getAttribute("groupe").toString()));
		coefsList = new ArrayList<SelectItem>();
		niveauxList = new ArrayList<SelectItem>();
		echelonsList = new ArrayList<SelectItem>();
		for (ParcoursBean p : parcoursList) {
			SelectItem coef = new SelectItem(p.getCoefficient(),
					p.getCoefficient());
			SelectItem ech = new SelectItem(p.getEchelon(), p.getEchelon());
			SelectItem niv = new SelectItem(p.getNiveau(), p.getNiveau());
			if (!containsString(coefsList, coef.getLabel())
					&& coef.getLabel() != null) {
				if (!coef.getLabel().equals("")) {
					coefsList.add(coef);
				}
			}
			if (!containsString(echelonsList, ech.getLabel())
					&& ech.getLabel() != null) {
				if (!ech.getLabel().equals("")) {
					echelonsList.add(ech);
				}
			}
			if (!containsString(niveauxList, niv.getLabel())
					&& niv.getLabel() != null) {
				if (!niv.getLabel().equals("")) {
					niveauxList.add(niv);
				}
			}
		}

		MotifRuptureContratServiceImpl motifRuptureServ = new MotifRuptureContratServiceImpl();
		List<MotifRuptureContratBean> motifList = motifRuptureServ
				.getMotifRuptureContratList();
		motifsRuptureList = new ArrayList<SelectItem>();
		for (MotifRuptureContratBean motifBean : motifList) {
			selectItem = new SelectItem();
			selectItem.setValue(motifBean.getId());
			selectItem.setLabel(motifBean.getNom() + "");
			motifsRuptureList.add(selectItem);
		}

		TypeRecoursInterimServiceImpl recoursServ = new TypeRecoursInterimServiceImpl();
		List<TypeRecoursInterimBean> recoursList = recoursServ
				.getTypeRecoursInterimList();
		motifsRecoursList = new ArrayList<SelectItem>();
		for (TypeRecoursInterimBean recoursBean : recoursList) {
			selectItem = new SelectItem();
			selectItem.setValue(recoursBean.getId());
			selectItem.setLabel(recoursBean.getNom() + "");
			motifsRecoursList.add(selectItem);
		}

		this.idEntrepriseSelected = -1;

		return "";
	}

	public boolean containsString(List<SelectItem> list, String s) {
		for (SelectItem si : list) {
			if (si.getLabel().equals(s)) {
				return true;
			}
		}
		return false;
	}

	public List<SalarieBeanExport> getSalarieList() throws Exception {
		Collections.sort(salarieList);
		return salarieList;
	}

	public void setSalarieList(List<SalarieBeanExport> salarieList) {
		this.salarieList = salarieList;
	}

	public String[] getSelectedSexe() {
		return selectedSexe;
	}

	public void setSelectedSexe(String[] selectedSexe) {
		this.selectedSexe = selectedSexe;
	}

	public Integer getIdEntrepriseSelected() {
		return idEntrepriseSelected;
	}

	public void setIdEntrepriseSelected(Integer idEntrepriseSelected) {
		this.idEntrepriseSelected = idEntrepriseSelected;
	}

	public ArrayList<SelectItem> getEntreprisesList() {
		return entreprisesList;
	}

	public ArrayList<SelectItem> getServicesList() {
		servicesList = null;
		if (idEntrepriseSelected > 0) {
			ServiceImpl service = new ServiceImpl();

			ArrayList<ServiceBean> serviceBeanList;

			servicesList = new ArrayList<SelectItem>();
			try {
				serviceBeanList = (ArrayList<ServiceBean>) service
						.getServicesList(Integer.parseInt(session.getAttribute(
								"groupe").toString()));

				Collections.sort(serviceBeanList);
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

	public void entrepriseChanged(ValueChangeEvent event) {
		ArrayList<SelectItem> serviceItems = getServicesList();
	}

	public void serviceChanged(ValueChangeEvent event) throws Exception {
		metiersList = getMetiersList();
	}

	public void selectTypeAbsence(ValueChangeEvent event) {
		Integer[] idSelected = (Integer[]) event.getNewValue();
	}

	public void effectChangeListener(ValueChangeEvent event) {
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	public String download() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"window.open(\"servlet.exportDataTableToExcelServlet? \",\"_Reports\");");
		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		String[] entete = new String[15];
		entete[0] = "Nom";
		entete[1] = "Pr\u00E9nom";
		entete[2] = "Date de naissance";
		entete[3] = "Age";
		entete[4] = "Sexe";
		entete[5] = "Entr\u00E9e dans l'entreprise";
		entete[6] = "Sortie de l'entreprise";
		entete[7] = "M\u00E9tier";
		entete[8] = "Entr\u00E9e dans le poste";
		entete[9] = "Nombre d'hrs de formation cumul\u00E9es";
		entete[10] = "Solde DIF";
		entete[11] = "Anc.";
		entete[12] = "Type de Contrat";
		entete[13] = "Equiv temps plein";
		entete[14] = "CSP";
		try {
			eContext.getSessionMap().put("datatable", salarieList);
			eContext.getSessionMap().put("datatableEnTete", entete);
			eContext.getSessionMap().put("name", "Filtre multi");
			eContext.getSessionMap().put("filtres", filtres);
			eContext.getSessionMap()
					.put("groupe",
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	private boolean contain(int m, List<SelectItem> l) {
		for (int i = 0; i < l.size(); i++) {
			int val = (Integer) l.get(i).getValue();
			if (val == m) {
				return true;
			}
		}

		return false;
	}

	private void initMetiers() throws Exception {
		metiersList = new ArrayList<SelectItem>();
		SalarieServiceImpl salarieService = new SalarieServiceImpl();
		ArrayList<SalarieBeanExport> salarieBeanList;
		if (idServicesSelected != null) {
			try {
				salarieBeanList = (ArrayList<SalarieBeanExport>) salarieService
						.getSalariesExportList(Integer.parseInt(session
								.getAttribute("groupe").toString()));
				for (SalarieBeanExport salarieBean : salarieBeanList) {
					if (idEntrepriseSelected == salarieBean
							.getIdEntrepriseSelected()) {
						for (int i = 0; i < idServicesSelected.length; i++) {
							if (idServicesSelected[i] == salarieBean
									.getIdServiceSelected()) {
								List<ParcoursBean> l = salarieBean
										.getParcoursBeanList();
								for (int k = 0; k < l.size(); k++) {
									// On ajoute les metiers du salarie si cela
									// n'est pas encore fait
									MetierServiceImpl metierService = new MetierServiceImpl();
									int idMet = l.get(k).getIdMetierSelected();
									MetierBean metBean = metierService
											.getMetierBeanById(idMet);
									if (!this.contain(idMet, this.metiersList)) {
										metiersList.add(new SelectItem(metBean
												.getId(), metBean.getNom()));
									}
								}
							}
						}

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<SelectItem> getMetiersList() throws Exception {
		// this.initMetiers();
		return metiersList;
	}

	public ArrayList<SelectItem> getStatutsList() {
		return statutsList;
	}

	public Integer[] getIdStatutsSelected() {
		return idStatutsSelected;
	}

	public void setIdStatutsSelected(Integer[] idStatutsSelected) {
		this.idStatutsSelected = idStatutsSelected;
	}

	public Integer[] getIdMetiersSelected() {
		return idMetiersSelected;
	}

	public void setIdMetiersSelected(Integer[] idMetiersSelected) {
		this.idMetiersSelected = idMetiersSelected;
	}

	public Integer[] getIdServicesSelected() {
		return idServicesSelected;
	}

	public void setIdServicesSelected(Integer[] idServiceSelected) {

		this.idServicesSelected = idServiceSelected;
	}

	public ArrayList<SelectItem> getContratsList() {
		return contratsList;
	}

	public Integer[] getIdContratsSelected() {
		return idContratsSelected;
	}

	public void setIdContratsSelected(Integer[] idContratsSelected) {
		this.idContratsSelected = idContratsSelected;
	}

	public Integer[] getIdTypeAccidentSelected() {
		return idTypeAccidentSelected;
	}

	public void setIdTypeAccidentSelected(Integer[] idTypeAccidentSelected) {
		this.idTypeAccidentSelected = idTypeAccidentSelected;
	}

	public ArrayList<SelectItem> getTypeAccidentList() {
		return typeAccidentList;
	}

	public Integer[] getIdCauseAccidentSelected() {
		return idCauseAccidentSelected;
	}

	public void setIdCauseAccidentSelected(Integer[] idCauseAccidentSelected) {
		this.idCauseAccidentSelected = idCauseAccidentSelected;
	}

	public ArrayList<SelectItem> getTypeCauseAccidentList() {
		return typeCauseAccidentList;
	}

	public void setIdTypeAbsenceSelected(Integer[] idTypeAbsenceSelected) {
		this.idTypeAbsenceSelected = idTypeAbsenceSelected;
	}

	public ArrayList<SelectItem> getTypeAbsenceList() {
		return typeAbsenceList;
	}

	public Integer[] getIdTypeAbsenceSelected() {
		return idTypeAbsenceSelected;
	}

	public Integer[] getIdTypeLesionSelected() {
		return idTypeLesionSelected;
	}

	public void setIdTypeLesionSelected(Integer[] idTypeLesionSelected) {
		this.idTypeLesionSelected = idTypeLesionSelected;
	}

	public ArrayList<SelectItem> getTypeLesionList() {
		return typeLesionList;
	}

	public Integer[] getIdTypeHabSelected() {
		return idTypeHabSelected;
	}

	public void setIdTypeHabSelected(Integer[] idTypeHabSelected) {
		this.idTypeHabSelected = idTypeHabSelected;
	}

	public ArrayList<SelectItem> getTypeHabList() {
		return typeHabList;
	}

	public Integer[] getIdFormationSelected() {
		return idFormationSelected;
	}

	public void setIdFormationSelected(Integer[] idFormationSelected) {
		this.idFormationSelected = idFormationSelected;
	}

	public ArrayList<SelectItem> getFormationList() {
		return formationList;
	}

	public String[] getIdNiveauFormSelected() {
		return idNiveauFormSelected;
	}

	public void setIdNiveauFormSelected(String[] idNiveauFormSelected) {
		this.idNiveauFormSelected = idNiveauFormSelected;
	}

	public String reinitialiseValeur() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		if (session.getAttribute("retour") != null)
			session.setAttribute("retour", null);

		debutPeriode = null;
		finPeriode = null;

		habValide = false;
		useDIF = false;
		selectedSexe = null;
		selectedNature = null;
		selectedTypeFinancement = null;
		selectedPermisConduire = null;
		selectedEnfant = null;
		debutAge = null;
		finAge = null;
		debutAnciennete = null;
		finAnciennete = null;
		datePeremption = null;
		idEntrepriseSelected = 0;
		idServicesSelected = null;
		idSituationFamilleSelected = null;
		idMetiersSelected = null;
		idMetiersSelectedFDP = null;
		idStatutsSelected = null;
		idContratsSelected = null;
		idTypeAccidentSelected = null;
		idCauseAccidentSelected = null;
		idTypeAbsenceSelected = null;
		idTypeLesionSelected = null;
		idTypeHabSelected = null;
		idFormationSelected = null;
		idNiveauFormSelected = null;
		idMotifsRuptureSelected = null;
		idMotifsRecoursSelected = null;
		idDomaineFormationSelected = null;

		idCoefsSelected = null;
		idNiveauxSelected = null;
		idEchelonsSelected = null;

		motCleCompetences = null;
		motCleCompetencesAAmeliorer = null;
		motCleFormationSouhaitee = null;
		motCleEvolutionSouhaitee = null;

		typeAbsET = "Ou";
		typeAccET = "Ou";
		typeCauseAccET = "Ou";
		typeLesionET = "Ou";
		typeFormationET = "Ou";
		typeHabET = "Ou";
		formationET = "Ou";
		financementET = "Ou";
		metierET = "Ou";

		return "filtreMultiCriteres";
	}

	public ArrayList<Filtre> getFiltres() {
		return filtres;
	}

	public void setFiltres(ArrayList<Filtre> filtres) {
		this.filtres = filtres;
	}

	public ByteArrayResource getImagen() throws Exception {

		return imagen;
	}

	public void setImagen(ByteArrayResource imagen) {
		this.imagen = imagen;
	}

	private static Date date(final int day, final int month, final int year) {

		final Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		final Date result = calendar.getTime();
		return result;

	}

	public int getIdMonthSelected() {
		return idMonthSelected;
	}

	public void setIdMonthSelected(int idMonthSelected) {
		this.idMonthSelected = idMonthSelected;
	}

	public int getIdDureeSelected() {
		return idDureeSelected;
	}

	public void setIdDureeSelected(int idDureeSelected) {
		this.idDureeSelected = idDureeSelected;
	}

	public ArrayList<SelectItem> getMonthList() {
		return monthList;
	}

	public Integer getDebutAge() {
		return debutAge;
	}

	public void setDebutAge(Integer debutAge) {
		this.debutAge = debutAge;
	}

	public Integer getFinAge() {
		return finAge;
	}

	public void setFinAge(Integer finAge) {
		this.finAge = finAge;
	}

	public Integer getDebutAnciennete() {
		return debutAnciennete;
	}

	public void setDebutAnciennete(Integer debutAnciennete) {
		this.debutAnciennete = debutAnciennete;
	}

	public Integer getFinAnciennete() {
		return finAnciennete;
	}

	public void setFinAnciennete(Integer finAnciennete) {
		this.finAnciennete = finAnciennete;
	}

	public String getTypeAbsET() {
		if (typeAbsET == null) {
			return "Ou";
		}
		return typeAbsET;
	}

	public void setTypeAbsET(String typeAbsET) {
		this.typeAbsET = typeAbsET;
	}

	public String getTypeAccET() {
		if (typeAccET == null) {
			return "Ou";
		}
		return typeAccET;
	}

	public void setTypeAccET(String typeAccET) {
		this.typeAccET = typeAccET;
	}

	public String getTypeCauseAccET() {
		if (typeCauseAccET == null) {
			return "Ou";
		}
		return typeCauseAccET;
	}

	public void setTypeCauseAccET(String typeCauseAccET) {
		this.typeCauseAccET = typeCauseAccET;
	}

	public String getTypeLesionET() {
		if (typeLesionET == null) {
			return "Ou";
		}
		return typeLesionET;
	}

	public void setTypeLesionET(String typeLesionET) {
		this.typeLesionET = typeLesionET;
	}

	public String getTypeHabET() {
		if (typeHabET == null) {
			return "Ou";
		}
		return typeHabET;
	}

	public void setTypeHabET(String typeHabET) {
		this.typeHabET = typeHabET;
	}

	public String getFormationET() {
		if (formationET == null) {
			return "Ou";
		}
		return formationET;
	}

	public void setFormationET(String formationET) {
		this.formationET = formationET;
	}

	public String[] getIdCoefsSelected() {
		return idCoefsSelected;
	}

	public String[] getIdNiveauxSelected() {
		return idNiveauxSelected;
	}

	public String[] getIdEchelonsSelected() {
		return idEchelonsSelected;
	}

	public Integer[] getIdMotifsRuptureSelected() {
		return idMotifsRuptureSelected;
	}

	public Integer[] getIdMotifsRecoursSelected() {
		return idMotifsRecoursSelected;
	}

	public ArrayList<SelectItem> getCoefsList() {
		return coefsList;
	}

	public ArrayList<SelectItem> getNiveauxList() {
		return niveauxList;
	}

	public ArrayList<SelectItem> getEchelonsList() {
		return echelonsList;
	}

	public ArrayList<SelectItem> getMotifsRuptureList() {
		return motifsRuptureList;
	}

	public ArrayList<SelectItem> getMotifsRecoursList() {
		return motifsRecoursList;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public String[] getSelectedNature() {
		return selectedNature;
	}

	public void setSelectedNature(String[] selectedNature) {
		this.selectedNature = selectedNature;
	}

	public Date getDatePeremption() {
		return datePeremption;
	}

	public String getTypeFormationET() {
		if (typeFormationET == null) {
			return "Ou";
		}
		return typeFormationET;
	}

	public ArrayList<SelectItem> getTypeFormationList() {
		return typeFormationList;
	}

	public boolean isUseDIF() {
		return useDIF;
	}

	public void setUseDIF(boolean useDIF) {
		this.useDIF = useDIF;
	}

	public String getFinancementET() {
		if (financementET == null) {
			return "Ou";
		}
		return financementET;
	}

	public String[] getSelectedTypeFinancement() {
		return selectedTypeFinancement;
	}

	public String getMetierET() {
		if (metierET == null) {
			return "Ou";
		}
		return metierET;
	}

	public Integer[] getIdMetiersSelectedFDP() {
		return idMetiersSelectedFDP;
	}

	public ArrayList<SelectItem> getMetiersListFDP() {
		return metiersListFDP;
	}

	public String getMotCleCompetences() {
		return motCleCompetences;
	}

	public void setMotCleCompetences(String motCleCompetences) {
		this.motCleCompetences = motCleCompetences;
	}

	public String getMotCleCompetencesAAmeliorer() {
		return motCleCompetencesAAmeliorer;
	}

	public void setMotCleCompetencesAAmeliorer(
			String motCleCompetencesAAmeliorer) {
		this.motCleCompetencesAAmeliorer = motCleCompetencesAAmeliorer;
	}

	public String getMotCleFormationSouhaitee() {
		return motCleFormationSouhaitee;
	}

	public void setMotCleFormationSouhaitee(String motCleFormationSouhaitee) {
		this.motCleFormationSouhaitee = motCleFormationSouhaitee;
	}

	public String getMotCleEvolutionSouhaitee() {
		return motCleEvolutionSouhaitee;
	}

	public void setMotCleEvolutionSouhaitee(String motCleEvolutionSouhaitee) {
		this.motCleEvolutionSouhaitee = motCleEvolutionSouhaitee;
	}

	public Integer[] getIdDomaineFormationSelected() {
		return idDomaineFormationSelected;
	}

	public ArrayList<SelectItem> getDomaineFormationList() {
		return domaineFormationList;
	}

	public boolean isHabValide() {
		return habValide;
	}

	public void setHabValide(boolean habValide) {
		this.habValide = habValide;
	}

	public String[] getIdSituationFamilleSelected() {
		return idSituationFamilleSelected;
	}

	public ArrayList<SelectItem> getSituationFamilleList() {
		return situationFamilleList;
	}

	public List<String> getJoinTables() {
		return joinTables;
	}

	public void setSelectedTypeFinancement(String[] selectedTypeFinancement) {
		this.selectedTypeFinancement = selectedTypeFinancement;
	}

	public void setTypeFormationET(String typeFormationET) {
		this.typeFormationET = typeFormationET;
	}

	public void setFinancementET(String financementET) {
		this.financementET = financementET;
	}

	public void setMetierET(String metierET) {
		this.metierET = metierET;
	}

	public void setDatePeremption(Date datePeremption) {
		this.datePeremption = datePeremption;
	}

	public void setIdMetiersSelectedFDP(Integer[] idMetiersSelectedFDP) {
		this.idMetiersSelectedFDP = idMetiersSelectedFDP;
	}

	public void setIdSituationFamilleSelected(
			String[] idSituationFamilleSelected) {
		this.idSituationFamilleSelected = idSituationFamilleSelected;
	}

	public void setIdDomaineFormationSelected(
			Integer[] idDomaineFormationSelected) {
		this.idDomaineFormationSelected = idDomaineFormationSelected;
	}

	public void setIdCoefsSelected(String[] idCoefsSelected) {
		this.idCoefsSelected = idCoefsSelected;
	}

	public void setIdNiveauxSelected(String[] idNiveauxSelected) {
		this.idNiveauxSelected = idNiveauxSelected;
	}

	public void setIdEchelonsSelected(String[] idEchelonsSelected) {
		this.idEchelonsSelected = idEchelonsSelected;
	}

	public void setIdMotifsRuptureSelected(Integer[] idMotifsRuptureSelected) {
		this.idMotifsRuptureSelected = idMotifsRuptureSelected;
	}

	public void setIdMotifsRecoursSelected(Integer[] idMotifsRecoursSelected) {
		this.idMotifsRecoursSelected = idMotifsRecoursSelected;
	}

	public void setEntreprisesList(ArrayList<SelectItem> entreprisesList) {
		this.entreprisesList = entreprisesList;
	}

	public void setServicesList(ArrayList<SelectItem> servicesList) {
		this.servicesList = servicesList;
	}

	public void setMetiersList(ArrayList<SelectItem> metiersList) {
		this.metiersList = metiersList;
	}

	public void setMetiersListFDP(ArrayList<SelectItem> metiersListFDP) {
		this.metiersListFDP = metiersListFDP;
	}

	public void setStatutsList(ArrayList<SelectItem> statutsList) {
		this.statutsList = statutsList;
	}

	public void setContratsList(ArrayList<SelectItem> contratsList) {
		this.contratsList = contratsList;
	}

	public void setSituationFamilleList(
			ArrayList<SelectItem> situationFamilleList) {
		this.situationFamilleList = situationFamilleList;
	}

	public void setTypeAccidentList(ArrayList<SelectItem> typeAccidentList) {
		this.typeAccidentList = typeAccidentList;
	}

	public void setTypeCauseAccidentList(
			ArrayList<SelectItem> typeCauseAccidentList) {
		this.typeCauseAccidentList = typeCauseAccidentList;
	}

	public void setTypeAbsenceList(ArrayList<SelectItem> typeAbsenceList) {
		this.typeAbsenceList = typeAbsenceList;
	}

	public void setTypeFormationList(ArrayList<SelectItem> typeFormationList) {
		this.typeFormationList = typeFormationList;
	}

	public void setTypeLesionList(ArrayList<SelectItem> typeLesionList) {
		this.typeLesionList = typeLesionList;
	}

	public void setTypeHabList(ArrayList<SelectItem> typeHabList) {
		this.typeHabList = typeHabList;
	}

	public void setFormationList(ArrayList<SelectItem> formationList) {
		this.formationList = formationList;
	}

	public void setDomaineFormationList(
			ArrayList<SelectItem> domaineFormationList) {
		this.domaineFormationList = domaineFormationList;
	}

	public void setCoefsList(ArrayList<SelectItem> coefsList) {
		this.coefsList = coefsList;
	}

	public void setNiveauxList(ArrayList<SelectItem> niveauxList) {
		this.niveauxList = niveauxList;
	}

	public void setEchelonsList(ArrayList<SelectItem> echelonsList) {
		this.echelonsList = echelonsList;
	}

	public void setMotifsRuptureList(ArrayList<SelectItem> motifsRuptureList) {
		this.motifsRuptureList = motifsRuptureList;
	}

	public void setMotifsRecoursList(ArrayList<SelectItem> motifsRecoursList) {
		this.motifsRecoursList = motifsRecoursList;
	}

	public void setMonthList(ArrayList<SelectItem> monthList) {
		this.monthList = monthList;
	}

	public void setRetourSession(boolean retourSession) {
		this.retourSession = retourSession;
	}

	public void setJoinTables(List<String> joinTables) {
		this.joinTables = joinTables;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Date getDebutPeriode() {
		return debutPeriode;
	}

	public void setDebutPeriode(Date debutPeriode) {
		this.debutPeriode = debutPeriode;
	}

	public Date getFinPeriode() {
		return finPeriode;
	}

	public void setFinPeriode(Date finPeriode) {
		this.finPeriode = finPeriode;
	}

	public List<SalarieBeanExport> getSalarieBeanExportList() {
		return salarieBeanList;
	}

	public void setSalarieBeanExportList(List<SalarieBeanExport> salarieBeanList) {
		this.salarieBeanList = salarieBeanList;
	}

	public List<ParcoursBean> getParcoursBeanList() {
		return parcoursBeanList;
	}

	public void setParcoursBeanList(List<ParcoursBean> parcoursBeanList) {
		this.parcoursBeanList = parcoursBeanList;
	}

	public List<ContratTravailBean> getContratBeanList() {
		return contratBeanList;
	}

	public void setContratBeanList(List<ContratTravailBean> contratBeanList) {
		this.contratBeanList = contratBeanList;
	}

	public List<AbsenceBean> getAbsenceBeanList() {
		return absenceBeanList;
	}

	public void setAbsenceBeanList(List<AbsenceBean> absenceBeanList) {
		this.absenceBeanList = absenceBeanList;
	}

	public List<AccidentBean> getAccidentBeanList() {
		return accidentBeanList;
	}

	public void setAccidentBeanList(List<AccidentBean> accidentBeanList) {
		this.accidentBeanList = accidentBeanList;
	}

	public List<HabilitationBean> getHabilitationBeanList() {
		return habilitationBeanList;
	}

	public void setHabilitationBeanList(
			List<HabilitationBean> habilitationBeanList) {
		this.habilitationBeanList = habilitationBeanList;
	}

	public List<FormationBean> getFormationBeanList() {
		return formationBeanList;
	}

	public void setFormationBeanList(List<FormationBean> formationBeanList) {
		this.formationBeanList = formationBeanList;
	}

	public boolean isDisplayRecap() {
		return displayRecap;
	}

	public void setDisplayRecap(boolean displayRecap) {
		this.displayRecap = displayRecap;
	}

	public List<EntretienBean> getEntretienBeanList() {
		return entretienBeanList;
	}

	public void setEntretienBeanList(List<EntretienBean> entretienBeanList) {
		this.entretienBeanList = entretienBeanList;
	}

	public int getNbEntretiens() {
		return entretienBeanList.size();
	}

	public int getNbSalaries() {
		return salarieList.size();
	}

	public int getNbParcours() {
		return parcoursBeanList.size();
	}

	public int getNbContratTravail() {
		return contratBeanList.size();
	}

	public int getNbAbsences() {
		return absenceBeanList.size();
	}

	public int getNbAccidents() {
		return accidentBeanList.size();
	}

	public int getNbHabilitations() {
		return habilitationBeanList.size();
	}

	public int getNbFormations() {
		return formationBeanList.size();
	}

	public List<RemunerationBean> getRemunerationBeanList() {
		return remunerationBeanList;
	}

	public void setRemunerationBeanList(
			List<RemunerationBean> remunerationBeanList) {
		this.remunerationBeanList = remunerationBeanList;
	}

	public int getNbRemunerations() {
		return remunerationBeanList.size();
	}

	public String[] getSelectedEnfant() {
		return selectedEnfant;
	}

	public void setSelectedEnfant(String[] selectedEnfant) {
		this.selectedEnfant = selectedEnfant;
	}

	public String[] getSelectedPermisConduire() {
		return selectedPermisConduire;
	}

	public void setSelectedPermisConduire(String[] selectedPermisConduire) {
		this.selectedPermisConduire = selectedPermisConduire;
	}

}
