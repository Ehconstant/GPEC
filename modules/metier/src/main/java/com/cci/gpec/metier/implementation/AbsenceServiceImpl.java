package com.cci.gpec.metier.implementation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.db.Absence;
import com.cci.gpec.db.Accident;
import com.cci.gpec.db.Formation;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.Typeabsence;
import com.cci.gpec.db.connection.HibernateUtil;

public class AbsenceServiceImpl {
	
	private static final Logger				LOGGER					= LoggerFactory.getLogger(AbsenceServiceImpl.class);

	/**
	 * Retourne la liste des Absences.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<AbsenceBean> getAbsencesList(int idGroupe) throws HibernateException {
		Session session = null;
		Transaction transaction = null;
		List<AbsenceBean> absencesInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Absence> absencesInventory = session
					.createQuery(
							"from Absence as a left join fetch a.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe).list();

			absencesInventoryBean = absencePersistantListToAbsenceBeanList(absencesInventory);
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
		return absencesInventoryBean;
	}

	public List<AbsenceBean> getAbsenceBeanListByIdTypeAbsence(
			int idTypeAbsence, int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<AbsenceBean> absencesInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Absence> absencesInventory = session
					.createQuery(
							"from Absence as a left join fetch a.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " and a.TypeAbsence="
									+ idTypeAbsence).list();

			absencesInventoryBean = absencePersistantListToAbsenceBeanList(absencesInventory);
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
		return absencesInventoryBean;
	}

	public List<AbsenceBean> getAbsenceBeanListByIdSalarie(int salarie)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<AbsenceBean> absencesInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Absence> absencesInventory = session.createQuery(
					"from Absence where Salarie=" + salarie).list();

			absencesInventoryBean = absencePersistantListToAbsenceBeanList(absencesInventory);
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
		return absencesInventoryBean;
	}

	public List<AbsenceBean> getAbsenceBeanListByIdSalarieAndYear(int salarie)
			throws Exception {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		List<AbsenceBean> absencesInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Absence> absencesInventory = session.createQuery(
					"from Absence where Salarie=" + salarie).list();

			absencesInventoryBean = absencePersistantListToAbsenceBeanList(absencesInventory);
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
		return absencesInventoryBean;
	}

	public List<AbsenceBean> getAbsenceBeanListOrderByYearNomEntrepriseNomSalarie(
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<AbsenceBean> absencesInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Absence> absencesInventory = session
					.createQuery(
							"from Absence as a left join fetch a.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " order by year(a.DebutAbsence) desc, e.NomEntreprise, s.Nom")
					.list();

			absencesInventoryBean = absencePersistantListToAbsenceBeanList(absencesInventory);
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
		return absencesInventoryBean;
	}

	public List<AbsenceBean> getAbsenceBeanListOrderByYearNomSalarie(
			int idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<AbsenceBean> absencesInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Absence> absencesInventory = session.createQuery(
					"from Absence as a left join fetch a.Salarie as s where s.Entreprise = "
							+ idEntreprise
							+ " order by year(a.DebutAbsence) desc, s.Nom")
					.list();

			absencesInventoryBean = absencePersistantListToAbsenceBeanList(absencesInventory);
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
		return absencesInventoryBean;
	}

	public List<AbsenceBean> getAbsenceBeanListByIdSalarieAndYear(int salarie,
			int yearD, int yearF) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<AbsenceBean> absencesInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Absence> absencesInventory = session.createQuery(
					"from Absence where Salarie=" + salarie
							+ "and ( YEAR(DebutAbsence)=" + yearD
							+ " OR YEAR(DebutAbsence)=" + yearF + ")").list();

			absencesInventoryBean = absencePersistantListToAbsenceBeanList(absencesInventory);
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
		return absencesInventoryBean;
	}

	public boolean deleteAbsence(AbsenceBean ab) {
		// On vérifie qu'une absence n'est pas associé à un salarié
		this.deleteAbsenceFin(ab);

		return true;
	}

	public void insert(String query) {
		Transaction transaction = null;
		Session session = null;
		Statement st;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			st = session.connection().createStatement();
			st.execute(query);
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} catch (SQLException e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public void saveOrUppdate(AbsenceBean absenceBean) throws Exception {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			Absence a = absenceBeanToAbsencePersistant(absenceBean,
					absenceBean.getIdSalarie());
			session.saveOrUpdate(a);

			absenceBean.setId(a.getId());

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

	public void save(AbsenceBean absenceBean) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();

			Absence a = absenceBeanToAbsencePersistant(absenceBean,
					absenceBean.getIdSalarie());
			session.save(a);

			absenceBean.setId(a.getId());

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public String getJustificatif(Integer idAbsence) throws Exception {
		Session session = null;
		Transaction transaction = null;
		String justificatif = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Absence absencePersistant = new Absence();
			transaction = session.beginTransaction();
			absencePersistant = (Absence) session
					.load(Absence.class, idAbsence);
			AbsenceBean absenceBean = absencePersistantToAbsenceBean(absencePersistant);
			justificatif = absenceBean.getJustificatif(); 
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
		return justificatif;
	}

	public int saveAccident(AbsenceBean absenceBean) {
		Session session = null;
		Transaction transaction = null;
		Object idAbsence = null;
		int n = -1;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Absence absencePersistant = new Absence();
			transaction = session.beginTransaction();

			if (absenceBean.getId() != 0) {
				absencePersistant = (Absence) session.load(Absence.class,
						absenceBean.getId());

				absencePersistant.setId(absenceBean.getId());
			}

			idAbsence = session.save(absencePersistant);
			n  = ((Integer) idAbsence).intValue();
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
		return n;
	}

	public void deleteAbsenceFin(AbsenceBean absenceBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Absence absencePersistant = new Absence();
			transaction = session.beginTransaction();

			AccidentServiceImpl accidentService = new AccidentServiceImpl();
			Accident accident = accidentService
					.getAccidentByIdAbsence(absenceBean.getId(), false);
			if (accident != null) {
				accident.setAbsence(null);
				session.saveOrUpdate(accident);
			}

			FormationServiceImpl formationService = new FormationServiceImpl();
			Formation formation = formationService
					.getFormationByIdAbsence(absenceBean.getId(), false);
			if (formation != null) {
				formation.setAbsence(null);
				session.saveOrUpdate(formation);
			}

			if (absenceBean.getId() != 0) {
				absencePersistant = (Absence) session.load(Absence.class,
						absenceBean.getId());

				absencePersistant.setId(absenceBean.getId());
			}

			session.delete(absencePersistant);
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

	public void deleteAbsenceWithoutTransaction(AbsenceBean absenceBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Absence absencePersistant = new Absence();

			AccidentServiceImpl accidentService = new AccidentServiceImpl();
			Accident accident = accidentService
					.getAccidentByIdAbsence(absenceBean.getId(), false);
			if (accident != null) {
				accident.setAbsence(null);
				session.saveOrUpdate(accident);
			}

			FormationServiceImpl formationService = new FormationServiceImpl();
			Formation formation = formationService
					.getFormationByIdAbsence(absenceBean.getId(), false);
			if (formation != null) {
				formation.setAbsence(null);
				session.saveOrUpdate(formation);
			}

			if (absenceBean.getId() != 0) {
				absencePersistant = (Absence) session.load(Absence.class,
						absenceBean.getId());

				absencePersistant.setId(absenceBean.getId());
			}

			session.delete(absencePersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public AbsenceBean getAbsenceBeanById(Integer idAbsence) throws Exception {
		Session session = null;
		Transaction transaction = null;
		AbsenceBean absenceBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Absence absencePersistant = new Absence();
			transaction = session.beginTransaction();

			if (idAbsence == -1)
				return null;

			absencePersistant = (Absence) session
					.load(Absence.class, idAbsence);

			absenceBean = absencePersistantToAbsenceBean(absencePersistant);

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
		return absenceBean;
	}

	public List<AbsenceBean> absencePersistantListToAbsenceBeanList(
			List<Absence> absencePersistantList) {

		List<AbsenceBean> absenceBeanList = new ArrayList<AbsenceBean>();

		for (Absence absencePersistant : absencePersistantList) {

			absenceBeanList
					.add(absencePersistantToAbsenceBean(absencePersistant));
		}

		return absenceBeanList;

	}

	public AbsenceBean absencePersistantToAbsenceBean(Absence absencePersistant) {

		AbsenceBean absenceBean = new AbsenceBean();
		absenceBean.setId(absencePersistant.getId());

		absenceBean.setDebutAbsence(absencePersistant.getDebutAbsence());

		absenceBean.setFinAbsence(absencePersistant.getFinAbsence());

		absenceBean.setIdTypeAbsenceSelected(absencePersistant.getTypeAbsence()
				.getId());
		absenceBean.setIdSalarie(absencePersistant.getSalarie().getId());
		absenceBean.setNomTypeAbsence(absencePersistant.getTypeAbsence()
				.getNomTypeAbsence());
		absenceBean.setNombreJourOuvre(absencePersistant.getNombreJourOuvre());
		absenceBean.setAuto(false);
		if (absencePersistant.getJustificatif() != null
				&& absencePersistant.getJustificatif().equals("")) {
			absenceBean.setJustificatif(null);
		} else {
			absenceBean.setJustificatif(absencePersistant.getJustificatif());
		}
		return absenceBean;
	}

	public List<Absence> absenceBeanListToAbsencePersistantList(
			SalarieBean salarieBean) throws Exception {

		List<Absence> absenceList = new ArrayList<Absence>();

		for (AbsenceBean absenceBean : salarieBean.getAbsenceBeanList()) {

			if (!absenceBean.isAuto()) {
				absenceList.add(absenceBeanToAbsencePersistant(absenceBean,
						salarieBean.getId()));
			}
		}

		return absenceList;

	}

	public Absence absenceBeanToAbsencePersistant(AbsenceBean absenceBean,
			int idSalarie) throws Exception {

		Session session = null;
		Absence absencePersistant = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		
			absencePersistant = new Absence();
	
			if (absenceBean.getId() == -1) {
				absencePersistant.setId(null);
			} else {
				absencePersistant.setId(absenceBean.getId());
			}
			Salarie salarie = (Salarie) session.load(Salarie.class, idSalarie);
			absencePersistant.setSalarie(salarie);
			absencePersistant.setDebutAbsence(absenceBean.getDebutAbsence());
			absencePersistant.setFinAbsence(absenceBean.getFinAbsence());
			absencePersistant.setNombreJourOuvre(absenceBean.getNombreJourOuvre());
			absencePersistant.setJustificatif(absenceBean.getJustificatif());
	
			Typeabsence typeAbsence = new Typeabsence();
	
			typeAbsence = (Typeabsence) session.load(Typeabsence.class,
					absenceBean.getIdTypeAbsenceSelected());
	
			absencePersistant.setTypeAbsence(typeAbsence);
		
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
		return absencePersistant;

	}
}
