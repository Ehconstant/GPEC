package com.cci.gpec.metier.implementation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.SalarieBeanExport;
import com.cci.gpec.db.Absence;
import com.cci.gpec.db.Accident;
import com.cci.gpec.db.Entreprise;
import com.cci.gpec.db.Entretien;
import com.cci.gpec.db.Formation;
import com.cci.gpec.db.Habilitation;
import com.cci.gpec.db.Parcours;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.Service;
import com.cci.gpec.db.connection.HibernateUtil;
import com.cci.gpec.metier.interfaces.SalarieService;

public class SalarieServiceImpl implements SalarieService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SalarieServiceImpl.class);

	/**
	 * Retourne la liste des salariés.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<SalarieBean> getSalariesList(int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Salarie> salarieInventory = session.createQuery(
					"from Salarie as s left join fetch s.Entreprise as e where e.Groupe="
							+ idGroupe + " order by s.Nom").list();

			List<SalarieBean> salarieBeanList = new ArrayList<SalarieBean>();
			salarieBeanList = salariePersistantListToSalarieBeanList(salarieInventory);
			transaction.commit();

			return salarieBeanList;

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
	}

	public List<SalarieBeanExport> getSalariesExportList(int idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Salarie> salarieInventory = session.createQuery(
					"from Salarie as s left join fetch s.Entreprise as e where e.Groupe="
							+ idGroupe + " order by Nom").list();
			// s left join fetch s.FORMATIONs f where f.VolumeHoraire > 30
			List<SalarieBeanExport> salarieBeanList = new ArrayList<SalarieBeanExport>();
			salarieBeanList = salariePersistantListToSalarieBeanExportList(salarieInventory);
			transaction.commit();

			return salarieBeanList;

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
	}

	public List<SalarieBeanExport> getSalariesExportListOrderByEntreprise(
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Salarie> salarieInventory = session.createQuery(
					"from Salarie as s left join fetch s.Entreprise as e where e.Groupe="
							+ idGroupe + " order by e.NomEntreprise,s.Nom")
					.list();
			List<SalarieBeanExport> salarieBeanList = new ArrayList<SalarieBeanExport>();
			salarieBeanList = salariePersistantListToSalarieBeanExportList(salarieInventory);
			transaction.commit();

			return salarieBeanList;

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
	}

	public List<SalarieBean> getSalarieBeanListByIdEntreprise(
			Integer idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Salarie> salarieInventory = session.createQuery(
					"from Salarie where Entreprise=" + idEntreprise
							+ " order by Nom").list();

			List<SalarieBean> salarieBeanList = new ArrayList<SalarieBean>();
			salarieBeanList = salariePersistantListToSalarieBeanList(salarieInventory);
			transaction.commit();

			return salarieBeanList;

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
	}

	public List<SalarieBean> getSalarieBeanListByIdEntrepriseAndIdService(
			Integer idEntreprise, Integer idService) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Salarie> salarieInventory = session.createQuery(
					"from Salarie where Entreprise=" + idEntreprise
							+ " and Service =" + idService).list();

			List<SalarieBean> salarieBeanList = new ArrayList<SalarieBean>();
			salarieBeanList = salariePersistantListToSalarieBeanList(salarieInventory);
			transaction.commit();

			return salarieBeanList;

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
	}

	public List<SalarieBeanExport> getSalarieBeanExportListByIdEntrepriseOrderByEntreprise(
			Integer idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Salarie> salarieInventory = session
					.createQuery(
							"from Salarie as s left join fetch s.Entreprise as e order by e.NomEntreprise, s.Nom")
					.list();

			List<SalarieBeanExport> salarieBeanList = new ArrayList<SalarieBeanExport>();
			salarieBeanList = salariePersistantListToSalarieBeanExportList(salarieInventory);
			transaction.commit();

			return salarieBeanList;

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
	}

	public List<SalarieBeanExport> getSalarieBeanExportListByIdEntreprise(
			Integer idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Salarie> salarieInventory = session.createQuery(
					"from Salarie where Entreprise=" + idEntreprise
							+ " order by Nom").list();

			List<SalarieBeanExport> salarieBeanExportList = new ArrayList<SalarieBeanExport>();
			salarieBeanExportList = salariePersistantListToSalarieBeanExportList(salarieInventory);
			transaction.commit();

			return salarieBeanExportList;

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
	}

	public List<SalarieBeanExport> getSalarieBeanListByIds(Integer[] idSelected)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		int idEntrepriseSelected = idSelected[0];
		int idServiceSelected = idSelected[1];
		Integer idMetierSelected = idSelected[2];

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			StringBuilder request = new StringBuilder();
			request.append("from Salarie where Entreprise="
					+ idEntrepriseSelected);

			if (idServiceSelected > 0) {
				request.append(" and Service=" + idServiceSelected);
			}

			request.append(" order by Nom");
			List<Salarie> salarieInventory = session.createQuery(
					request.toString()).list();

			List<SalarieBeanExport> salarieBeanExportList = new ArrayList<SalarieBeanExport>();

			salarieBeanExportList = salariePersistantListToSalarieBeanExportList(salarieInventory);
			transaction.commit();

			if (idMetierSelected > 0) {
				List<SalarieBeanExport> salarieBeanExportListTmp = new ArrayList<SalarieBeanExport>();
				for (SalarieBeanExport salarieBeanExport : salarieBeanExportList) {
					ParcoursBean parcoursBean = getLastParcours(salarieBeanExport);
					if (parcoursBean != null) {
						if (parcoursBean.getIdMetierSelected().intValue() == idMetierSelected
								.intValue()) {
							salarieBeanExportListTmp.add(salarieBeanExport);
						}
					}
				}

				salarieBeanExportList = salarieBeanExportListTmp;
			}

			return salarieBeanExportList;

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
	}

	public List<SalarieBeanExport> getSalariesListToExport(int idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Salarie> salarieInventory = session.createQuery(
					"from Salarie as s left join fetch s.Entreprise as e where e.Groupe="
							+ idGroupe + " order by Nom").list();

			List<SalarieBeanExport> salarieBeanExportList = new ArrayList<SalarieBeanExport>();
			salarieBeanExportList = salariePersistantListToSalarieBeanExportList(salarieInventory);
			transaction.commit();

			return salarieBeanExportList;

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
	}

	public SalarieBean getSalarieBeanById(Integer idSalarie) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Salarie salariePersistant = new Salarie();
			transaction = session.beginTransaction();

			salariePersistant = (Salarie) session
					.load(Salarie.class, idSalarie);

			SalarieBean salarieBean = salariePersistantToSalarieBean(salariePersistant);

			transaction.commit();

			return salarieBean;

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
	}

	public Boolean isSalariePresent(Integer idSalarie) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			Boolean present = (Boolean) session.createQuery(
					"select Present from Salarie where Id=" + idSalarie)
					.uniqueResult();

			transaction.commit();

			return present;

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
	}

	public SalarieBeanExport getSalarieBeanExportById(Integer idSalarie)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Salarie salariePersistant = new Salarie();
			transaction = session.beginTransaction();

			salariePersistant = (Salarie) session
					.load(Salarie.class, idSalarie);

			SalarieBeanExport salarieBeanExport = salariePersistantToSalarieBeanExport(salariePersistant);

			transaction.commit();

			return salarieBeanExport;

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
	}

	public void saveOrUppdate(SalarieBean salarieBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			Salarie salariePersistant = new Salarie();
			if ((salarieBean.getId() != null) && (salarieBean.getId() != 0)
					&& salarieBean.getId() != -1) {
				salariePersistant = (Salarie) session.load(Salarie.class,
						salarieBean.getId());
			}

			salariePersistant.setNom(salarieBean.getNom());
			salariePersistant.setPrenom(salarieBean.getPrenom());
			salariePersistant.setAdresse(salarieBean.getAdresse());
			salariePersistant.setCivilite(salarieBean.getCivilite());
			salariePersistant.setCreditDif(salarieBean.getCreditDif());
			salariePersistant.setDateNaissance(salarieBean.getDateNaissance());
			salariePersistant.setLieuNaissance(salarieBean.getLieuNaissance());
			salariePersistant.setTelephone(salarieBean.getTelephone());
			salariePersistant.setIdLienSubordination(salarieBean
					.getIdLienSubordination());
			salariePersistant.setNivFormationAtteint(salarieBean
					.getNivFormationAtteint());
			salariePersistant.setNivFormationInit(salarieBean
					.getNivFormationInit());
			salariePersistant.setCv(salarieBean.getCv());

			salariePersistant.setDateLastSaisieDif(salarieBean
					.getDateLastSaisieDif());
			salariePersistant.setPresent(salarieBean.isPresent());

			Entreprise entreprisePersistant;

			entreprisePersistant = (Entreprise) session.load(Entreprise.class,
					salarieBean.getIdEntrepriseSelected());
			salariePersistant.setEntreprise(entreprisePersistant);

			Service servicePersistant;

			if (salarieBean.getIdServiceSelected() > 0) {
				servicePersistant = (Service) session.load(Service.class,
						salarieBean.getIdServiceSelected());
				salariePersistant.setService(servicePersistant);
			}
			salariePersistant.setTelephonePortable(salarieBean
					.getTelephonePortable());
			salariePersistant.setSituationFamiliale(salarieBean
					.getSituationFamiliale());
			salariePersistant.setPersonneAPrevenirNom(salarieBean
					.getPersonneAPrevenirNom());
			salariePersistant.setPersonneAPrevenirPrenom(salarieBean
					.getPersonneAPrevenirPrenom());
			salariePersistant.setPersonneAPrevenirAdresse(salarieBean
					.getPersonneAPrevenirAdresse());
			salariePersistant.setPersonneAPrevenirTelephone(salarieBean
					.getPersonneAPrevenirTelephone());
			salariePersistant.setPersonneAPrevenirLienParente(salarieBean
					.getPersonneAPrevenirLienParente());
			salariePersistant.setImpression(salarieBean.isImpression());
			salariePersistant.setPossedePermisConduire(salarieBean
					.isPossedePermisConduire());

			salariePersistant.setCommentaire(salarieBean.getCommentaire());

			session.saveOrUpdate(salariePersistant);

			salarieBean.setId(salariePersistant.getId());

			try {
				HabilitationServiceImpl habilitationService = new HabilitationServiceImpl();
				ArrayList<Habilitation> habilitationList = new ArrayList<Habilitation>();
				habilitationList = (ArrayList<Habilitation>) habilitationService
						.habilitationBeanListToHabilitationPersistantList(salarieBean);

				HashSet<Habilitation> habilitationSet = new HashSet<Habilitation>();

				habilitationSet.addAll(habilitationList);

				salariePersistant.setHABILITATIONs(habilitationSet);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				AbsenceServiceImpl absenceService = new AbsenceServiceImpl();
				ArrayList<Absence> absenceList = new ArrayList<Absence>();
				absenceList = (ArrayList<Absence>) absenceService
						.absenceBeanListToAbsencePersistantList(salarieBean);
				HashSet<Absence> absenceSet = new HashSet<Absence>();

				absenceSet.addAll(absenceList);

				try {
					FormationServiceImpl formationService = new FormationServiceImpl();
					ArrayList<Formation> formationList = new ArrayList<Formation>();
					formationList = (ArrayList<Formation>) formationService
							.formationBeanListToFormationPersistantList(
									salarieBean, absenceSet);

					HashSet<Formation> formationSet = new HashSet<Formation>();

					formationSet.addAll(formationList);

					salariePersistant.setFORMATIONs(formationSet);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					AccidentServiceImpl accidentService = new AccidentServiceImpl();
					ArrayList<Accident> accidentList = new ArrayList<Accident>();
					accidentList = (ArrayList<Accident>) accidentService
							.accidentBeanListToAccidentPersistantList(
									salarieBean, absenceSet);

					HashSet<Accident> accidentSet = new HashSet<Accident>();

					accidentSet.addAll(accidentList);

					salariePersistant.setACCIDENTs(accidentSet);
				} catch (Exception e) {
					e.printStackTrace();
				}
				salariePersistant.setABSENCEs(absenceSet);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				EntretienServiceImpl entretienService = new EntretienServiceImpl();
				ArrayList<Entretien> entretienList = new ArrayList<Entretien>();
				entretienList = (ArrayList<Entretien>) entretienService
						.entretienBeanListToEntretienPersistantList(salarieBean);

				HashSet<Entretien> entretienSet = new HashSet<Entretien>();

				entretienSet.addAll(entretienList);

				salariePersistant.setENTRETIENs(entretienSet);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				ParcoursServiceImpl parcoursService = new ParcoursServiceImpl();
				ArrayList<Parcours> parcoursList = new ArrayList<Parcours>();
				parcoursList = (ArrayList<Parcours>) parcoursService
						.parcoursBeanListToParcoursPersistantList(salarieBean);

				HashSet<Parcours> parcoursSet = new HashSet<Parcours>();

				parcoursSet.addAll(parcoursList);

				salariePersistant.setPARCOURSs(parcoursSet);
			} catch (Exception e) {
				e.printStackTrace();
			}

			session.saveOrUpdate(salariePersistant);

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
	}

	public void delete(SalarieBean salarieBean) throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			Salarie s = (Salarie) session.load(Salarie.class,
					salarieBean.getId());

			session.delete(s);

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
	}

	public void deleteSalarieWithoutTransaction(SalarieBean salarieBean)
			throws Exception {

		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Salarie s = (Salarie) session.load(Salarie.class,
					salarieBean.getId());

			session.delete(s);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void save(SalarieBean salarieBean) {

		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			// transaction = session.beginTransaction();

			Salarie salariePersistant = new Salarie();

			salariePersistant.setId(salarieBean.getId());

			salariePersistant.setNom(salarieBean.getNom());
			salariePersistant.setPrenom(salarieBean.getPrenom());
			salariePersistant.setAdresse(salarieBean.getAdresse());
			salariePersistant.setCivilite(salarieBean.getCivilite());
			salariePersistant.setCreditDif(salarieBean.getCreditDif());
			salariePersistant.setDateNaissance(salarieBean.getDateNaissance());
			salariePersistant.setLieuNaissance(salarieBean.getLieuNaissance());
			salariePersistant.setTelephone(salarieBean.getTelephone());
			salariePersistant.setIdLienSubordination(salarieBean
					.getIdLienSubordination());
			salariePersistant.setNivFormationAtteint(salarieBean
					.getNivFormationAtteint());
			salariePersistant.setNivFormationInit(salarieBean
					.getNivFormationInit());
			salariePersistant.setCv(salarieBean.getCv());

			salariePersistant.setDateLastSaisieDif(salarieBean
					.getDateLastSaisieDif());
			salariePersistant.setPresent(salarieBean.isPresent());

			Entreprise entreprisePersistant;

			if (salarieBean.getIdServiceSelected() > 0) {
				entreprisePersistant = (Entreprise) session
						.load(Entreprise.class,
								salarieBean.getIdEntrepriseSelected());
				salariePersistant.setEntreprise(entreprisePersistant);
			}

			Service servicePersistant;

			if (salarieBean.getIdServiceSelected() > 0) {
				servicePersistant = (Service) session.load(Service.class,
						salarieBean.getIdServiceSelected());
				salariePersistant.setService(servicePersistant);
			}

			salariePersistant.setTelephonePortable(salarieBean
					.getTelephonePortable());
			salariePersistant.setSituationFamiliale(salarieBean
					.getSituationFamiliale());
			salariePersistant.setPersonneAPrevenirNom(salarieBean
					.getPersonneAPrevenirNom());
			salariePersistant.setPersonneAPrevenirPrenom(salarieBean
					.getPersonneAPrevenirPrenom());
			salariePersistant.setPersonneAPrevenirAdresse(salarieBean
					.getPersonneAPrevenirAdresse());
			salariePersistant.setPersonneAPrevenirTelephone(salarieBean
					.getPersonneAPrevenirTelephone());
			salariePersistant.setPersonneAPrevenirLienParente(salarieBean
					.getPersonneAPrevenirLienParente());
			salariePersistant.setImpression(salarieBean.isImpression());
			salariePersistant.setPossedePermisConduire(salarieBean
					.isPossedePermisConduire());
			salariePersistant.setCommentaire(salarieBean.getCommentaire());

			session.save(salariePersistant);

			salarieBean.setId(salariePersistant.getId());

			if (salarieBean.getHabilitationBeanList() != null) {
				try {
					HabilitationServiceImpl habilitationService = new HabilitationServiceImpl();
					ArrayList<Habilitation> habilitationList = new ArrayList<Habilitation>();
					habilitationList = (ArrayList<Habilitation>) habilitationService
							.habilitationBeanListToHabilitationPersistantList(salarieBean);

					HashSet<Habilitation> habilitationSet = new HashSet<Habilitation>();

					habilitationSet.addAll(habilitationList);

					salariePersistant.setHABILITATIONs(habilitationSet);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (salarieBean.getAbsenceBeanList() != null) {
				try {
					AbsenceServiceImpl absenceService = new AbsenceServiceImpl();
					ArrayList<Absence> absenceList = new ArrayList<Absence>();
					absenceList = (ArrayList<Absence>) absenceService
							.absenceBeanListToAbsencePersistantList(salarieBean);
					HashSet<Absence> absenceSet = new HashSet<Absence>();

					absenceSet.addAll(absenceList);

					if (salarieBean.getFormationBeanList() != null) {
						try {
							FormationServiceImpl formationService = new FormationServiceImpl();
							ArrayList<Formation> formationList = new ArrayList<Formation>();
							formationList = (ArrayList<Formation>) formationService
									.formationBeanListToFormationPersistantList(
											salarieBean, absenceSet);

							HashSet<Formation> formationSet = new HashSet<Formation>();

							formationSet.addAll(formationList);

							salariePersistant.setFORMATIONs(formationSet);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (salarieBean.getAccidentBeanList() != null) {
						try {
							AccidentServiceImpl accidentService = new AccidentServiceImpl();
							ArrayList<Accident> accidentList = new ArrayList<Accident>();
							accidentList = (ArrayList<Accident>) accidentService
									.accidentBeanListToAccidentPersistantList(
											salarieBean, absenceSet);

							HashSet<Accident> accidentSet = new HashSet<Accident>();

							accidentSet.addAll(accidentList);

							salariePersistant.setACCIDENTs(accidentSet);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					salariePersistant.setABSENCEs(absenceSet);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (salarieBean.getEntretienBeanList() != null) {
				try {
					EntretienServiceImpl entretienService = new EntretienServiceImpl();
					ArrayList<Entretien> entretienList = new ArrayList<Entretien>();
					entretienList = (ArrayList<Entretien>) entretienService
							.entretienBeanListToEntretienPersistantList(salarieBean);

					HashSet<Entretien> entretienSet = new HashSet<Entretien>();

					entretienSet.addAll(entretienList);

					salariePersistant.setENTRETIENs(entretienSet);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (salarieBean.getParcoursBeanList() != null) {
				try {
					ParcoursServiceImpl parcoursService = new ParcoursServiceImpl();
					ArrayList<Parcours> parcoursList = new ArrayList<Parcours>();
					parcoursList = (ArrayList<Parcours>) parcoursService
							.parcoursBeanListToParcoursPersistantList(salarieBean);

					HashSet<Parcours> parcoursSet = new HashSet<Parcours>();

					parcoursSet.addAll(parcoursList);

					salariePersistant.setPARCOURSs(parcoursSet);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			session.save(salariePersistant);

			// transaction.commit();
			salarieBean.setId(salariePersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public List<SalarieBean> salariePersistantListToSalarieBeanList(
			List<Salarie> salariePersistantList) throws Exception {

		List<SalarieBean> salarieBeanList = new ArrayList<SalarieBean>();

		for (Salarie salariePersistant : salariePersistantList) {

			salarieBeanList
					.add(salariePersistantToSalarieBean(salariePersistant));
		}

		return salarieBeanList;

	}

	public SalarieBean salariePersistantToSalarieBean(Salarie salariePersistant)
			throws Exception {

		SalarieBean salarieBean = new SalarieBean();
		salarieBean.setAdresse(salariePersistant.getAdresse());
		salarieBean.setCivilite(salariePersistant.getCivilite());
		salarieBean.setCreditDif(salariePersistant.getCreditDif());
		salarieBean.setDateNaissance(salariePersistant.getDateNaissance());
		salarieBean.setId(salariePersistant.getId());
		salarieBean.setLieuNaissance(salariePersistant.getLieuNaissance());
		salarieBean.setNom(salariePersistant.getNom());
		salarieBean.setPrenom(salariePersistant.getPrenom());
		salarieBean.setTelephone(salariePersistant.getTelephone());
		salarieBean.setCanDeleteSalarie(false);

		salarieBean.setNivFormationAtteint(salariePersistant
				.getNivFormationAtteint());
		salarieBean
				.setNivFormationInit(salariePersistant.getNivFormationInit());
		salarieBean.setIdLienSubordination(salariePersistant
				.getIdLienSubordination());
		salarieBean.setCv(salariePersistant.getCv());

		salarieBean.setDateLastSaisieDif(salariePersistant
				.getDateLastSaisieDif());
		salarieBean.setPresent(salariePersistant.getPresent());

		salarieBean.setTelephonePortable(salariePersistant
				.getTelephonePortable());
		salarieBean.setSituationFamiliale(salariePersistant
				.getSituationFamiliale());
		salarieBean.setPersonneAPrevenirNom(salariePersistant
				.getPersonneAPrevenirNom());
		salarieBean.setPersonneAPrevenirPrenom(salariePersistant
				.getPersonneAPrevenirPrenom());
		salarieBean.setPersonneAPrevenirAdresse(salariePersistant
				.getPersonneAPrevenirAdresse());
		salarieBean.setPersonneAPrevenirTelephone(salariePersistant
				.getPersonneAPrevenirTelephone());
		salarieBean.setPersonneAPrevenirLienParente(salariePersistant
				.getPersonneAPrevenirLienParente());

		if (salariePersistant.getImpression() == null) {
			salarieBean.setImpression(false);
		} else {
			salarieBean.setImpression(salariePersistant.getImpression());
		}

		if (salariePersistant.getPossedePermisConduire() == null) {
			salarieBean.setPossedePermisConduire(false);
		} else {
			salarieBean.setPossedePermisConduire(salariePersistant
					.getPossedePermisConduire());
		}

		salarieBean.setCommentaire(salariePersistant.getCommentaire());

		if (salariePersistant.getEntreprise() != null) {
			salarieBean.setIdEntrepriseSelected(salariePersistant
					.getEntreprise().getId());
		}
		if (salariePersistant.getService() != null) {
			salarieBean.setIdServiceSelected(salariePersistant.getService()
					.getId());
		}
		/*
		 * 
		 */
		HabilitationServiceImpl habilitationServiceImpl = new HabilitationServiceImpl();
		ArrayList<Habilitation> habilitationList = new ArrayList<Habilitation>(
				salariePersistant.getHABILITATIONs());
		List<HabilitationBean> habilitationBeanList = habilitationServiceImpl
				.habilitationPersistantListToHabilitationBeanList(habilitationList);

		salarieBean.setHabilitationBeanList(habilitationBeanList);

		/*
		 * 
		 */
		AbsenceServiceImpl absenceServiceImpl = new AbsenceServiceImpl();
		ArrayList<Absence> absencePersistantList = new ArrayList<Absence>(
				salariePersistant.getABSENCEs());
		List<AbsenceBean> absenceBeanList = absenceServiceImpl
				.absencePersistantListToAbsenceBeanList(absencePersistantList);
		salarieBean.setAbsenceBeanList(absenceBeanList);

		/*
		 * 
		 */
		AccidentServiceImpl accidentServiceImpl = new AccidentServiceImpl();
		ArrayList<Accident> accidentPersistantList = new ArrayList<Accident>(
				salariePersistant.getACCIDENTs());
		List<AccidentBean> accidentBeanList = accidentServiceImpl
				.accidentPersistantListToAccidentBeanList(accidentPersistantList);
		salarieBean.setAccidentBeanList(accidentBeanList);

		/*
		 * 
		 * 
		 */
		EntretienServiceImpl entretienServiceImpl = new EntretienServiceImpl();
		ArrayList<Entretien> entretienPersistantList = new ArrayList<Entretien>(
				salariePersistant.getENTRETIENs());
		List<EntretienBean> entretienBeanList = entretienServiceImpl
				.entretienPersistantListToEntretienBeanList(entretienPersistantList);
		salarieBean.setEntretienBeanList(entretienBeanList);

		/*
		 * 
		 */
		FormationServiceImpl formationServiceImpl = new FormationServiceImpl();
		ArrayList<Formation> formationPersistantList = new ArrayList<Formation>(
				salariePersistant.getFORMATIONs());
		List<FormationBean> formationBeanList = formationServiceImpl
				.formationPersistantListToFormationBeanList(formationPersistantList);
		salarieBean.setFormationBeanList(formationBeanList);

		/*
		 * 
		 */
		ParcoursServiceImpl parcoursServiceImpl = new ParcoursServiceImpl();
		ArrayList<Parcours> parcoursPersistantList = new ArrayList<Parcours>(
				salariePersistant.getPARCOURSs());
		List<ParcoursBean> parcoursBeanList = parcoursServiceImpl
				.parcoursPersistantListToParcoursBeanList(parcoursPersistantList);

		salarieBean.setParcoursBeanList(parcoursBeanList);

		return salarieBean;
	}

	public List<SalarieBeanExport> salariePersistantListToSalarieBeanExportList(
			List<Salarie> salariePersistantList) throws Exception {

		List<SalarieBeanExport> salarieBeanExportList = new ArrayList<SalarieBeanExport>();

		for (Salarie salariePersistant : salariePersistantList) {

			salarieBeanExportList
					.add(salariePersistantToSalarieBeanExport(salariePersistant));
		}

		return salarieBeanExportList;

	}

	public SalarieBeanExport salariePersistantToSalarieBeanExport(
			Salarie salariePersistant) throws Exception {

		SalarieBeanExport salarieBeanExport = new SalarieBeanExport();
		salarieBeanExport.setAdresse(salariePersistant.getAdresse());
		salarieBeanExport.setCivilite(salariePersistant.getCivilite());
		salarieBeanExport.setCreditDif(salariePersistant.getCreditDif());
		salarieBeanExport.setCanDeleteSalarie(false);

		// on crée l'objet en passant en paramétre une chaîne representant le
		// format
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		// on crée la chaîne à partir de la date
		salarieBeanExport
				.setDateNaissance(salariePersistant.getDateNaissance());
		if (salariePersistant.getDateNaissance() != null) {
			salarieBeanExport.setDateNaissanceExport(formatter
					.format(salariePersistant.getDateNaissance()));
		} else {
			salarieBeanExport.setDateNaissanceExport("");
		}
		salarieBeanExport.setId(salariePersistant.getId());
		salarieBeanExport
				.setLieuNaissance(salariePersistant.getLieuNaissance());
		salarieBeanExport.setNom(salariePersistant.getNom());
		salarieBeanExport.setPrenom(salariePersistant.getPrenom());
		salarieBeanExport.setTelephone(salariePersistant.getTelephone());

		salarieBeanExport.setNivFormationAtteint(salariePersistant
				.getNivFormationAtteint());
		salarieBeanExport.setNivFormationInit(salariePersistant
				.getNivFormationInit());
		salarieBeanExport.setIdLienSubordination(salariePersistant
				.getIdLienSubordination());
		salarieBeanExport.setCv(salariePersistant.getCv());

		salarieBeanExport.setDateLastSaisieDif(salariePersistant
				.getDateLastSaisieDif());

		salarieBeanExport.setTelephonePortable(salariePersistant
				.getTelephonePortable());
		salarieBeanExport.setSituationFamiliale(salariePersistant
				.getSituationFamiliale());
		salarieBeanExport.setPersonneAPrevenirNom(salariePersistant
				.getPersonneAPrevenirNom());
		salarieBeanExport.setPersonneAPrevenirPrenom(salariePersistant
				.getPersonneAPrevenirPrenom());
		salarieBeanExport.setPersonneAPrevenirAdresse(salariePersistant
				.getPersonneAPrevenirAdresse());
		salarieBeanExport.setPersonneAPrevenirTelephone(salariePersistant
				.getPersonneAPrevenirTelephone());
		salarieBeanExport.setPersonneAPrevenirLienParente(salariePersistant
				.getPersonneAPrevenirLienParente());
		if (salariePersistant.getImpression() == null) {
			salarieBeanExport.setImpression(false);
		} else {
			salarieBeanExport.setImpression(salariePersistant.getImpression());
		}

		if (salariePersistant.getPossedePermisConduire() == null) {
			salarieBeanExport.setPossedePermisConduire(false);
		} else {
			salarieBeanExport.setPossedePermisConduire(salariePersistant
					.getPossedePermisConduire());
		}

		salarieBeanExport.setCommentaire(salariePersistant.getCommentaire());

		salarieBeanExport.setPresent(salariePersistant.getPresent());

		salarieBeanExport.setIdLienSubordination(salariePersistant
				.getIdLienSubordination());

		if (salariePersistant.getEntreprise() != null) {
			salarieBeanExport.setIdEntrepriseSelected(salariePersistant
					.getEntreprise().getId());
			salarieBeanExport.setNomEntreprise(salariePersistant
					.getEntreprise().getNomEntreprise());
		}

		if (salariePersistant.getService() != null) {
			salarieBeanExport.setIdServiceSelected(salariePersistant
					.getService().getId());
			salarieBeanExport.setService(salariePersistant.getService()
					.getNomService());
		}
		/*
		 * 
		 */
		HabilitationServiceImpl habilitationServiceImpl = new HabilitationServiceImpl();
		ArrayList<Habilitation> habilitationList = new ArrayList<Habilitation>(
				salariePersistant.getHABILITATIONs());
		List<HabilitationBean> habilitationBeanList = habilitationServiceImpl
				.habilitationPersistantListToHabilitationBeanList(habilitationList);

		salarieBeanExport.setHabilitationBeanList(habilitationBeanList);

		/*
		 * 
		 */
		AbsenceServiceImpl absenceServiceImpl = new AbsenceServiceImpl();
		ArrayList<Absence> absencePersistantList = new ArrayList<Absence>(
				salariePersistant.getABSENCEs());
		List<AbsenceBean> absenceBeanList = absenceServiceImpl
				.absencePersistantListToAbsenceBeanList(absencePersistantList);

		salarieBeanExport.setAbsenceBeanList(absenceBeanList);

		/*
		 * 
		 */
		AccidentServiceImpl accidentServiceImpl = new AccidentServiceImpl();
		ArrayList<Accident> accidentPersistantList = new ArrayList<Accident>(
				salariePersistant.getACCIDENTs());
		List<AccidentBean> accidentBeanList = accidentServiceImpl
				.accidentPersistantListToAccidentBeanList(accidentPersistantList);
		salarieBeanExport.setAccidentBeanList(accidentBeanList);

		/*
		 * 
		 * 
		 */
		EntretienServiceImpl entretienServiceImpl = new EntretienServiceImpl();
		ArrayList<Entretien> entretienPersistantList = new ArrayList<Entretien>(
				salariePersistant.getENTRETIENs());
		List<EntretienBean> entretienBeanList = entretienServiceImpl
				.entretienPersistantListToEntretienBeanList(entretienPersistantList);
		salarieBeanExport.setEntretienBeanList(entretienBeanList);

		/*
		 * 
		 */
		FormationServiceImpl formationServiceImpl = new FormationServiceImpl();
		ArrayList<Formation> formationPersistantList = new ArrayList<Formation>(
				salariePersistant.getFORMATIONs());
		List<FormationBean> formationBeanList = formationServiceImpl
				.formationPersistantListToFormationBeanList(formationPersistantList);
		salarieBeanExport.setFormationBeanList(formationBeanList);

		/*
		 * 
		 */
		ParcoursServiceImpl parcoursServiceImpl = new ParcoursServiceImpl();
		ArrayList<Parcours> parcoursPersistantList = new ArrayList<Parcours>(
				salariePersistant.getPARCOURSs());
		List<ParcoursBean> parcoursBeanList = parcoursServiceImpl
				.parcoursPersistantListToParcoursBeanList(parcoursPersistantList);

		Collections.sort(parcoursBeanList);
		Collections.reverse(parcoursBeanList);
		salarieBeanExport.setParcoursBeanList(parcoursBeanList);

		if (parcoursBeanList != null && parcoursBeanList.size() > 0) {
			ParcoursBean parcourBean = parcoursBeanList.get(0);

			// on crée la chaîne à partir de la date
			if (parcourBean.getDebutFonction() != null) {
				salarieBeanExport.setDepuis(formatter.format(parcourBean
						.getDebutFonction()));
			} else {
				salarieBeanExport.setDepuis("");
			}
			salarieBeanExport.setPosteOccupe(parcourBean.getNomMetier());
			salarieBeanExport.setTypeContrat(parcourBean.getNomTypeContrat());
			salarieBeanExport.setCcp(parcourBean.getNomTypeStatut());
		}

		if (parcoursBeanList.size() > 0 && parcoursBeanList != null) {
			Collections.sort(parcoursBeanList);
			Collections.reverse(parcoursBeanList);

			Integer anciennete;
			GregorianCalendar datePremierPoste = new GregorianCalendar();
			GregorianCalendar dateDernierPoste = new GregorianCalendar();
			if (parcoursBeanList.size() > 1) {
				// récupération du premier porte occupé
				dateDernierPoste.setTime(parcoursBeanList.get(
						parcoursBeanList.size() - 1).getDebutFonction());

			} else {
				dateDernierPoste.setTime(new Date());

			}
			// récupération du premier poste occupé
			datePremierPoste
					.setTime(parcoursBeanList.get(0).getDebutFonction());

			anciennete = getAnciennete(salarieBeanExport);
			salarieBeanExport.setAnciennete(anciennete.toString());
		}
		return salarieBeanExport;
	}

	private ParcoursBean getFirstParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		ParcoursBean pb = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			if (pb == null) {
				pb = parcour;
			}
			if (parcour.getDebutFonction().before(pb.getDebutFonction())
					&& parcour.getFinFonction().equals(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}

	private int getAnciennete(SalarieBean salarie) {
		ParcoursBean parcourDeb = getFirstParcours(salarie);
		Calendar dateDebut = new GregorianCalendar();
		Calendar dateFin = new GregorianCalendar();
		if (parcourDeb != null) {
			dateDebut.setTime(parcourDeb.getDebutFonction());
		}
		ParcoursBean parcourFin = getLastParcours(salarie);
		if (parcourFin != null) {
			if (parcourFin.getFinFonction() != null) {
				dateFin.setTime(parcourFin.getFinFonction());
			} else {
				dateFin.setTime(new Date());
			}
		}
		Date d = new Date();
		Calendar actu = new GregorianCalendar();
		actu.setTime(d);
		if (dateFin.get(Calendar.YEAR) > actu.get(Calendar.YEAR)) {
			return actu.get(Calendar.YEAR) - dateDebut.get(Calendar.YEAR);
		} else {
			return dateFin.get(Calendar.YEAR) - dateDebut.get(Calendar.YEAR);
		}
	}

	public List<SalarieBean> getPyramideDataOrderedOderedByAge(
			Integer idEntreprise, Integer idService, int idGroupe) {
		Session session = null;
		Transaction transaction = null;
		List<SalarieBean> salarieBeanList = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			salarieBeanList = new ArrayList<SalarieBean>();
			String strWhere = "where ";
			boolean where = false;
			if (idEntreprise != null) {
				strWhere = strWhere + " s.Entreprise = " + idEntreprise + " ";
				where = true;
			}

			if (idService != null) {
				if (idEntreprise != null) {
					strWhere = strWhere + " AND ";
				}
				strWhere = strWhere + " s.Service = " + idService + " ";
				where = true;
			}

			String query = "from Salarie as s left join fetch s.Entreprise as e ";
			if (where) {
				query = query + strWhere + " and e.Groupe=" + idGroupe;
			} else {
				query = query + "where e.Groupe=" + idGroupe;
			}
			query = query + " order by s.DateNaissance";
			transaction = session.beginTransaction();
			List<Salarie> salarieInventory = session.createQuery(query).list();

			salarieBeanList = salariePersistantListToSalarieBeanList(salarieInventory);
			transaction.commit();

		} catch (HibernateException e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} catch (Exception e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return salarieBeanList;
	}

	public List<SalarieBean> getPyramideDataOrderedByAnciennete(
			Integer idEntreprise, Integer idService, int idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<SalarieBean> salarieBeanList = new ArrayList<SalarieBean>();
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			String strWhere = "where ";
			boolean where = false;
			if (idEntreprise != null) {
				strWhere = strWhere + " s.Entreprise = " + idEntreprise + " ";
				where = true;
			}

			if (idService != null) {
				if (idEntreprise != null) {
					strWhere = strWhere + " AND ";
				}
				strWhere = strWhere + " s.Service = " + idService + " ";
				where = true;
			}

			String query = "from Salarie as s left join fetch s.Entreprise as e ";
			if (where) {
				query = query + strWhere + " and e.Groupe=" + idGroupe;
			} else {
				query = query + "where e.Groupe=" + idGroupe;
			}
			query = query + " order by s.DateNaissance";
			transaction = session.beginTransaction();

			List<Salarie> salarieInventory = session.createQuery(query).list();

			salarieBeanList = salariePersistantListToSalarieBeanList(salarieInventory);
			transaction.commit();

		} catch (HibernateException e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} catch (Exception e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return salarieBeanList;
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
}
