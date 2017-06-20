package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.EvenementBean;
import com.cci.gpec.db.Evenement;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.connection.HibernateUtil;

public class EvenementServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EvenementServiceImpl.class);

	/**
	 * Retourne la liste des Evenements.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */

	public List<EvenementBean> getEvenementsList(int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<EvenementBean> evenementsInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Evenement> evenementsInventory = session
					.createQuery(
							"from Evenement as ev left join fetch ev.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe).list();

			evenementsInventoryBean = evenementPersistantListToEvenementBeanList(evenementsInventory);
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
		return evenementsInventoryBean;
	}

	public List<EvenementBean> getEvenementBeanListByIdSalarie(int salarie)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<EvenementBean> evenementInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Evenement> evenementInventory = session.createQuery(
					"from Evenement where Salarie=" + salarie).list();

			evenementInventoryBean = evenementPersistantListToEvenementBeanList(evenementInventory);
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
		return evenementInventoryBean;
	}

	public String getJustificatif(Integer idEvenement) throws Exception {
		Session session = null;
		Transaction transaction = null;
		String justificatif = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Evenement evenementPersistant = new Evenement();
			transaction = session.beginTransaction();
			evenementPersistant = (Evenement) session.load(Evenement.class,
					idEvenement);
			EvenementBean evenementBean = evenementPersistantToEvenementBean(evenementPersistant);
			transaction.commit();
			justificatif =  evenementBean.getJustificatif();
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

	public void saveOrUppdate(EvenementBean evenementBean) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			session.saveOrUpdate(evenementBeanToEvenementPersistant(
					evenementBean, evenementBean.getIdSalarie()));
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

	public void save(EvenementBean evenementBean) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.save(evenementBeanToEvenementPersistant(evenementBean,
					evenementBean.getIdSalarie()));
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void deleteEvenement(EvenementBean evenementBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Evenement evenementPersistant = new Evenement();
			transaction = session.beginTransaction();
			if (evenementBean.getId() != 0) {
				evenementPersistant = (Evenement) session.load(Evenement.class,
						evenementBean.getId());

				evenementPersistant.setId(evenementBean.getId());
			}

			session.delete(evenementPersistant);
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

	public void deleteEvenementWithoutTransaction(EvenementBean evenementBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Evenement evenementPersistant = new Evenement();
			if (evenementBean.getId() != 0) {
				evenementPersistant = (Evenement) session.load(Evenement.class,
						evenementBean.getId());

				evenementPersistant.setId(evenementBean.getId());
			}

			session.delete(evenementPersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public EvenementBean getEvenementBeanById(Integer idEvenement)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		EvenementBean evenementBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Evenement evenementPersistant = new Evenement();
			transaction = session.beginTransaction();

			evenementPersistant = (Evenement) session.load(Evenement.class,
					idEvenement);

			evenementBean = evenementPersistantToEvenementBean(evenementPersistant);

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
		return evenementBean;
	}

	public List<EvenementBean> evenementPersistantListToEvenementBeanList(
			List<Evenement> evenementPersistantList) throws Exception {

		List<EvenementBean> evenementBeanList = new ArrayList<EvenementBean>();

		for (Evenement evenementPersistant : evenementPersistantList) {

			evenementBeanList
					.add(evenementPersistantToEvenementBean(evenementPersistant));
		}

		return evenementBeanList;

	}

	public EvenementBean evenementPersistantToEvenementBean(
			Evenement evenementPersistant) throws Exception {

		EvenementBean evenementBean = new EvenementBean();
		evenementBean.setId(evenementPersistant.getId());
		evenementBean.setDateEvenement(evenementPersistant.getDateEvenement());
		evenementBean.setCommentaire(evenementPersistant.getCommentaire());
		evenementBean.setDecision(evenementPersistant.getDecision());
		evenementBean.setNature(evenementPersistant.getNature());
		evenementBean.setIdSalarie(evenementPersistant.getSalarie().getId());
		evenementBean.setInterlocuteur(evenementPersistant.getInterlocuteur());
		if (evenementPersistant.getJustificatif() != null
				&& evenementPersistant.getJustificatif().equals("")) {
			evenementBean.setJustificatif(null);
		} else {
			evenementBean
					.setJustificatif(evenementPersistant.getJustificatif());
		}

		return evenementBean;
	}

	public Evenement evenementBeanToEvenementPersistant(
			EvenementBean evenementBean, int idSalarie) throws Exception {

		Session session = null;
		Evenement evenementPersistant = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			evenementPersistant = new Evenement();
	
			Salarie salarie = (Salarie) session.load(Salarie.class, idSalarie);
			if (evenementBean.getId() == -1) {
				evenementPersistant.setId(null);
			} else {
				evenementPersistant.setId(evenementBean.getId());
			}
	
			evenementPersistant.setSalarie(salarie);
	
			evenementPersistant.setDateEvenement(evenementBean.getDateEvenement());
			evenementPersistant.setCommentaire(evenementBean.getCommentaire());
			evenementPersistant.setDecision(evenementBean.getDecision());
			evenementPersistant.setNature(evenementBean.getNature());
			evenementPersistant.setJustificatif(evenementBean.getJustificatif());
			evenementPersistant.setInterlocuteur(evenementBean.getInterlocuteur());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
		return evenementPersistant;
	}

}
