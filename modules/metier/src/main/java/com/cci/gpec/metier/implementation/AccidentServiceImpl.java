package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.db.Absence;
import com.cci.gpec.db.Accident;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.Typeabsence;
import com.cci.gpec.db.Typeaccident;
import com.cci.gpec.db.Typecauseaccident;
import com.cci.gpec.db.Typelesion;
import com.cci.gpec.db.connection.HibernateUtil;

public class AccidentServiceImpl {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AccidentServiceImpl.class);

	/**
	 * Retourne la liste des Accidents.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<AccidentBean> getAccidentsList(int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<AccidentBean> accidentsInventoryBean = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Accident> accidentsInventory = session
					.createQuery(
							"from Accident as a left join fetch a.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe).list();

			accidentsInventoryBean = accidentPersistantListToAccidentBeanList(accidentsInventory);
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
		return accidentsInventoryBean;
	}

	public AbsenceBean genereAbsenceAutomatique(AccidentBean accident,
			String justificatif) throws Exception {
		AbsenceBean abs = new AbsenceBean();
		AbsenceServiceImpl absServ = new AbsenceServiceImpl();

		if (accident.getIdAbsence() != 0 && accident.getIdAbsence() != -1) {
			abs.setId(accident.getIdAbsence());
		} else {
			abs.setId(-1);
		}
		abs.setDebutAbsence(accident.getDateDebutAbsence());
		abs.setFinAbsence(accident.getDateFinAbsence());
		if (accident.isInitial()) {
			abs.setNombreJourOuvre(Double.valueOf(accident.getNombreJourArret()));
		} else {
			abs.setNombreJourOuvre(Double.valueOf(accident
					.getNombreJourArretRechute()));
		}
		abs.setAuto(true);
		abs.setIdSalarie(accident.getIdSalarie());

		if (!justificatif.equals("")) {
			abs.setJustificatif(justificatif);
		}

		int idTypeAbsenceBeanSelected = 0;
		if (accident.getIdTypeAccidentBeanSelected() == 1) {
			abs.setIdTypeAbsenceSelected(1);
			idTypeAbsenceBeanSelected = 1;
		} else if (accident.getIdTypeAccidentBeanSelected() == 2) {
			abs.setIdTypeAbsenceSelected(2);
			idTypeAbsenceBeanSelected = 2;
		} else if (accident.getIdTypeAccidentBeanSelected() == 3) {
			abs.setIdTypeAbsenceSelected(4);
			idTypeAbsenceBeanSelected = 4;
		}

		TypeAbsenceServiceImpl typeAbsenceService = new TypeAbsenceServiceImpl();
		String nomTypeAbsence = new String();
		try {
			nomTypeAbsence = typeAbsenceService.getTypeAbsenceBeanById(
					idTypeAbsenceBeanSelected).getNom();
		} catch (Exception e) {
			e.printStackTrace();
		}

		abs.setNomTypeAbsence(nomTypeAbsence);

		absServ.saveOrUppdate(abs);

		accident.setIdAbsence(abs.getId());

		return abs;
	}

	public List<AccidentBean> getAccidentBeanListByIdSalarie(int salarie)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<AccidentBean> accidentsInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			List<Accident> accidentsInventory = session.createQuery(
					"from Accident where id_salarie=" + salarie).list();

			accidentsInventoryBean = accidentPersistantListToAccidentBeanList(accidentsInventory);
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
		return accidentsInventoryBean;
	}

	public List<AccidentBean> getAccidentBeanListOrderByYearNomEntrepriseNomSalarie(
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<AccidentBean> accidentsInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Accident> accidentsInventory = session
					.createQuery(
							"from Accident as a left join fetch a.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " order by year(a.DateAccident) desc, e.NomEntreprise, s.Nom")
					.list();

			accidentsInventoryBean = accidentPersistantListToAccidentBeanList(accidentsInventory);
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
		return accidentsInventoryBean;
	}

	public List<AccidentBean> getAccidentBeanListOrderByYearNomSalarie(
			int idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<AccidentBean> accidentsInventoryBean = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Accident> accidentsInventory = session.createQuery(
					"from Accident as a left join fetch a.Salarie as s where s.Entreprise = "
							+ idEntreprise
							+ " order by year(a.DateAccident) desc, s.Nom")
					.list();

			accidentsInventoryBean = accidentPersistantListToAccidentBeanList(accidentsInventory);
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
		return accidentsInventoryBean;
	}

	public List<AccidentBean> getAccidentsOfSalarie(SalarieBean s)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<AccidentBean> accidentsInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Accident> accidentsInventory = session.createQuery(
					"from Accident where Salarie=" + s.getId()).list();

			accidentsInventoryBean = accidentPersistantListToAccidentBeanList(accidentsInventory);
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
		return accidentsInventoryBean;
	}

	public Accident getAccidentByIdAbsence(int idAbsence, boolean closeSession) {
		Session session = null;
		Transaction transaction = null;
		Accident accident = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			accident = (Accident) session.createQuery(
					"from Accident where Absence=" + idAbsence).uniqueResult();
		} catch (HibernateException e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} finally {
			if (closeSession && session != null && session.isOpen()) {
				session.close();
			}
		}
		return accident;
	}

	public List<AccidentBean> getAccidentsBetween(Date p1, Date p2, int idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<AccidentBean> accidentsInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			java.sql.Date d1 = new java.sql.Date(p1.getTime());
			java.sql.Date d2 = new java.sql.Date(p2.getTime());
			List<Accident> accidentsInventory = session
					.createQuery(
							"from Accident as a left join fetch a.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " and a.DateAccident>='"
									+ d1
									+ "' and a.DateAccident<='" + d2 + "'")
					.list();

			accidentsInventoryBean = accidentPersistantListToAccidentBeanList(accidentsInventory);
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
		return accidentsInventoryBean;
	}

	public void saveOrUppdate(AccidentBean accidentBean) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(accidentBeanToAccidentPersistant(accidentBean,
					accidentBean.getIdSalarie()));
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

	public void save(AccidentBean accidentBean) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Accident accidentPersistant = new Accident();
			accidentPersistant = accidentBeanToAccidentPersistant(accidentBean,
					accidentBean.getIdSalarie());
			session.save(accidentPersistant);
			accidentBean.setId(accidentPersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public String getJustificatif(Integer idAccident) throws Exception {
		Session session = null;
		Transaction transaction = null;
		String justificatif = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Accident accidentPersistant = new Accident();
			transaction = session.beginTransaction();
			accidentPersistant = (Accident) session.load(Accident.class,
					idAccident);
			AccidentBean accidentBean = accidentPersistantToAccidentBean(accidentPersistant);
			transaction.commit();
			justificatif =  accidentBean.getJustificatif();
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

	public void deleteAccident(AccidentBean accidentBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Accident accidentPersistant = new Accident();
			transaction = session.beginTransaction();
			if (accidentBean.getId() != 0) {
				accidentPersistant = (Accident) session.load(Accident.class,
						accidentBean.getId());

				accidentPersistant.setId(accidentBean.getId());
			}

			session.delete(accidentPersistant);
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

	public void deleteAccidentWithoutTransaction(AccidentBean accidentBean) {
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Accident accidentPersistant = new Accident();
			if (accidentBean.getId() != 0) {
				accidentPersistant = (Accident) session.load(Accident.class,
						accidentBean.getId());

				accidentPersistant.setId(accidentBean.getId());
			}

			session.delete(accidentPersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public AccidentBean getAccidentBeanById(Integer idAccident)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		AccidentBean accidentBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Accident accidentPersistant = new Accident();
			transaction = session.beginTransaction();

			accidentPersistant = (Accident) session.load(Accident.class,
					idAccident);

			accidentBean = accidentPersistantToAccidentBean(accidentPersistant);

			transaction.commit();
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return accidentBean;
	}

	public List<AccidentBean> accidentPersistantListToAccidentBeanList(
			List<Accident> accidentPersistantList) throws Exception {

		List<AccidentBean> accidentBeanList = new ArrayList<AccidentBean>();

		for (Accident accidentPersistant : accidentPersistantList) {

			accidentBeanList
					.add(accidentPersistantToAccidentBean(accidentPersistant));
		}

		return accidentBeanList;

	}

	public AccidentBean accidentPersistantToAccidentBean(
			Accident accidentPersistant) throws Exception {

		AccidentBean accidentBean = new AccidentBean();
		accidentBean.setId(accidentPersistant.getId());
		accidentBean.setDateAccident(accidentPersistant.getDateAccident());
		if (accidentPersistant.isInitial() == null) {
			accidentBean.setInitial(true);
		} else {
			accidentBean.setInitial(accidentPersistant.isInitial());
		}
		if (accidentPersistant.isInitial() != null
				&& accidentPersistant.isInitial() == false) {
			if (accidentPersistant.isAggravation() == null) {
				accidentBean.setAggravation(false);
			} else {
				accidentBean.setAggravation(accidentPersistant.isAggravation());
			}

			accidentBean.setDateRechute(accidentPersistant.getDateRechute());

			accidentBean.setNombreJourArretRechute(accidentPersistant
					.getNombreJourArretRechute());

			accidentBean.setIdTypeLesionRechuteBeanSelected(accidentPersistant
					.getTypeLesionRechute().getId());

			accidentBean.setNomTypeLesionRechute(accidentPersistant
					.getTypeLesionRechute().getNomTypeLesion());

		}
		accidentBean.setCommentaire(accidentPersistant.getCommentaire());
		accidentBean
				.setNombreJourArret(accidentPersistant.getNombreJourArret());
		accidentBean.setIdTypeAccidentBeanSelected(accidentPersistant
				.getTypeAccident().getId());
		accidentBean.setIdTypeCauseAccidentBeanSelected(accidentPersistant
				.getTypeCauseAccident().getId());
		accidentBean.setIdTypeLesionBeanSelected(accidentPersistant
				.getTypeLesion().getId());
		accidentBean.setIdSalarie(accidentPersistant.getSalarie().getId());
		accidentBean.setNomTypeAccident(accidentPersistant.getTypeAccident()
				.getNomTypeAccident());
		accidentBean.setNomTypeCauseAccident(accidentPersistant
				.getTypeCauseAccident().getNomTypeCauseAccident());
		accidentBean.setNomTypeLesion(accidentPersistant.getTypeLesion()
				.getNomTypeLesion());
		if (accidentPersistant.getJustificatif() != null
				&& accidentPersistant.getJustificatif().equals("")) {
			accidentBean.setJustificatif(null);
		} else {
			accidentBean.setJustificatif(accidentPersistant.getJustificatif());
		}
		if (accidentPersistant.getAbsence() != null
				&& accidentPersistant.getId() != null) {
			accidentBean.setIdAbsence(accidentPersistant.getAbsence().getId());
		} else {
			accidentBean.setIdAbsence(-1);
		}

		return accidentBean;
	}

	public List<Accident> accidentBeanListToAccidentPersistantList(
			SalarieBean salarieBean,
			HashSet<Absence> listeAbsenceCompleteSalarie) throws Exception {

		List<Accident> accidentList = new ArrayList<Accident>();

		for (AccidentBean accidentBean : salarieBean.getAccidentBeanList()) {

			accidentList.add(accidentBeanToAccidentPersistant(accidentBean,
					salarieBean.getId(), listeAbsenceCompleteSalarie));
		}

		return accidentList;

	}

	public Accident accidentBeanToAccidentPersistant(AccidentBean accidentBean,
			int idSalarie, HashSet<Absence> listeAbsenceCompleteSalarie)
			throws Exception {
		Session session = null;
		Accident accidentPersistant = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			accidentPersistant = new Accident();

			Salarie salarie = (Salarie) session.load(Salarie.class, idSalarie);
	
			if (accidentBean.getId() == -1) {
				accidentPersistant.setId(null);
				if (accidentBean.getNombreJourArret() > 0
						&& accidentBean.getDateDebutAbsence() != null
						&& accidentBean.getDateFinAbsence() != null) {
	
					Absence absencePersistant = new Absence();
					absencePersistant = (Absence) session.load(Absence.class,
							accidentBean.getIdAbsence());
	
					accidentPersistant.setAbsence(absencePersistant);
	
				}
			} else {
				accidentPersistant.setId(accidentBean.getId());
				Absence absencePersistant = new Absence();
				if (accidentBean.getIdAbsence() != -1
						&& accidentBean.getIdAbsence() != 0) {
					absencePersistant = (Absence) session.load(Absence.class,
							accidentBean.getIdAbsence());
					accidentPersistant.setAbsence(absencePersistant);
				}
			}
	
			accidentPersistant.setSalarie(salarie);
	
			accidentPersistant.setInitial(accidentBean.isInitial());
			Typeaccident typeAccident = new Typeaccident();
			Typelesion typeLesion = new Typelesion();
			Typecauseaccident typeCauseAccident = new Typecauseaccident();
			if (accidentBean.isInitial() == false) {
				Typelesion typeLesionRechute = new Typelesion();
				accidentPersistant.setAggravation(accidentBean.isAggravation());
				accidentPersistant.setDateRechute(accidentBean.getDateRechute());
				accidentPersistant.setNombreJourArretRechute(accidentBean
						.getNombreJourArretRechute());
				typeLesionRechute = (Typelesion) session.load(Typelesion.class,
						accidentBean.getIdTypeLesionRechuteBeanSelected());
				accidentPersistant.setTypeLesionRechute(typeLesionRechute);
			}
			accidentPersistant.setCommentaire(accidentBean.getCommentaire());
	
			accidentPersistant.setDateAccident(accidentBean.getDateAccident());
			accidentPersistant
					.setNombreJourArret(accidentBean.getNombreJourArret());
			accidentPersistant.setJustificatif(accidentBean.getJustificatif());
	
			typeAccident = (Typeaccident) session.load(Typeaccident.class,
					accidentBean.getIdTypeAccidentBeanSelected());
			typeLesion = (Typelesion) session.load(Typelesion.class,
					accidentBean.getIdTypeLesionBeanSelected());
			typeCauseAccident = (Typecauseaccident) session.load(
					Typecauseaccident.class,
					accidentBean.getIdTypeCauseAccidentBeanSelected());
	
			accidentPersistant.setTypeAccident(typeAccident);
			accidentPersistant.setTypeLesion(typeLesion);
			accidentPersistant.setTypeCauseAccident(typeCauseAccident);
		
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}

		return accidentPersistant;
	}

	public Accident accidentBeanToAccidentPersistant(AccidentBean accidentBean,
			int idSalarie) throws Exception {
		Session session = null;
		Accident accidentPersistant = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			accidentPersistant = new Accident();
	
			Salarie salarie = (Salarie) session.load(Salarie.class, idSalarie);
	
			if (accidentBean.getId() == -1) {
				accidentPersistant.setId(null);
				if (accidentBean.getNombreJourArret() > 0) {
					Absence absencePersistant = new Absence();
	
					absencePersistant.setId(null);
	
					absencePersistant.setSalarie(salarie);
					absencePersistant.setDebutAbsence(accidentBean
							.getDateDebutAbsence());
					absencePersistant.setFinAbsence(accidentBean
							.getDateFinAbsence());
					absencePersistant.setNombreJourOuvre(Double
							.valueOf(accidentBean.getNombreJourArret()));
	
					Typeabsence typeAbsence = new Typeabsence();
	
					int idTypeAbsence = 0;
					if (accidentBean.getIdTypeAccidentBeanSelected() == 1) {
						idTypeAbsence = 1;
					} else if (accidentBean.getIdTypeAccidentBeanSelected() == 2) {
						idTypeAbsence = 2;
					} else if (accidentBean.getIdTypeAccidentBeanSelected() == 3) {
						idTypeAbsence = 4;
					}
	
					typeAbsence = (Typeabsence) session.load(Typeabsence.class,
							idTypeAbsence);
	
					absencePersistant.setTypeAbsence(typeAbsence);
	
					accidentPersistant.setAbsence(absencePersistant);
				}
			} else {
				accidentPersistant.setId(accidentBean.getId());
				Absence absencePersistant = new Absence();
				if (accidentBean.getIdAbsence() != -1
						&& accidentBean.getIdAbsence() != 0) {
					absencePersistant = (Absence) session.load(Absence.class,
							accidentBean.getIdAbsence());
					//
					accidentPersistant.setAbsence(absencePersistant);
				}
			}
	
			accidentPersistant.setSalarie(salarie);
	
			accidentPersistant.setDateAccident(accidentBean.getDateAccident());
			accidentPersistant
					.setNombreJourArret(accidentBean.getNombreJourArret());
			accidentPersistant.setJustificatif(accidentBean.getJustificatif());
	
			accidentPersistant.setInitial(accidentBean.isInitial());
			Typeaccident typeAccident = new Typeaccident();
			Typelesion typeLesion = new Typelesion();
			Typecauseaccident typeCauseAccident = new Typecauseaccident();
			if (accidentBean.isInitial() != null) {
				if (accidentBean.isInitial() == false) {
					Typelesion typeLesionRechute = new Typelesion();
					accidentPersistant.setAggravation(accidentBean.isAggravation());
					accidentPersistant
							.setDateRechute(accidentBean.getDateRechute());
					accidentPersistant.setNombreJourArretRechute(accidentBean
							.getNombreJourArretRechute());
					typeLesionRechute = (Typelesion) session.load(Typelesion.class,
							accidentBean.getIdTypeLesionRechuteBeanSelected());
					accidentPersistant.setTypeLesionRechute(typeLesionRechute);
				}
			}
			accidentPersistant.setCommentaire(accidentBean.getCommentaire());
	
			typeAccident = (Typeaccident) session.load(Typeaccident.class,
					accidentBean.getIdTypeAccidentBeanSelected());
			typeLesion = (Typelesion) session.load(Typelesion.class,
					accidentBean.getIdTypeLesionBeanSelected());
			typeCauseAccident = (Typecauseaccident) session.load(
					Typecauseaccident.class,
					accidentBean.getIdTypeCauseAccidentBeanSelected());
	
			accidentPersistant.setTypeAccident(typeAccident);
			accidentPersistant.setTypeLesion(typeLesion);
			accidentPersistant.setTypeCauseAccident(typeCauseAccident);
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
		return accidentPersistant;
	}
}
