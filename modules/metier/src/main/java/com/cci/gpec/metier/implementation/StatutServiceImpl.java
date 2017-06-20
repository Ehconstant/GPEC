package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.StatutBean;
import com.cci.gpec.db.Statut;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class StatutServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StatutServiceImpl.class);
	
	/**
	 * Retourne la liste des Statuts.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<StatutBean> getStatutsList() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<StatutBean> statutsInventoryBean;
			transaction = session.beginTransaction();

			List<Statut> entreprisesInventory = session.createQuery(
					"from Statut order by NomStatut").list();

			statutsInventoryBean = statutPersistantListToStatutBeanList(entreprisesInventory);
			transaction.commit();

			return statutsInventoryBean;

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

	public void supprimer(StatutBean statutBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Statut statutPersistant = new Statut();
			transaction = session.beginTransaction();
			if (statutBean.getId() != 0) {
				statutPersistant = (Statut) session.load(Statut.class,
						statutBean.getId());

				statutPersistant.setId(statutBean.getId());
			}
			session.delete(statutPersistant);
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

	public void saveOrUppdate(StatutBean statutBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Statut statutPersistant = new Statut();
			transaction = session.beginTransaction();
			if (statutBean.getId() != 0) {
				statutPersistant = (Statut) session.load(Statut.class,
						statutBean.getId());

				// statutPersistant.setId(statutBean.getId());
			}

			statutPersistant.setNomStatut(statutBean.getNom());

			session.saveOrUpdate(statutPersistant);
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

	public void save(StatutBean statutBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Statut statutPersistant = new Statut();
			transaction = session.beginTransaction();

			statutPersistant.setId(statutBean.getId());

			statutPersistant.setNomStatut(statutBean.getNom());

			session.save(statutPersistant);
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

	public StatutBean getStatutBeanById(Integer idStatut) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Statut statutPersistant = new Statut();
			transaction = session.beginTransaction();

			statutPersistant = (Statut) session.load(Statut.class, idStatut);

			StatutBean statutBean = statutPersistantToStatutBean(statutPersistant);

			transaction.commit();

			return statutBean;

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

	private List<StatutBean> statutPersistantListToStatutBeanList(
			List<Statut> statutPersistantList) throws Exception {

		List<StatutBean> statutBeanList = new ArrayList<StatutBean>();

		for (Statut statutPersistant : statutPersistantList) {

			statutBeanList.add(statutPersistantToStatutBean(statutPersistant));
		}

		return statutBeanList;

	}

	public StatutBean statutPersistantToStatutBean(Statut statutPersistant)
			throws Exception {

		StatutBean statutBean = new StatutBean();
		if (statutPersistant.getId() != null) {
			statutBean.setId(statutPersistant.getId());
		}

		if (statutPersistant.getNomStatut() != null) {
			statutBean.setNom(statutPersistant.getNomStatut());
		}

		return statutBean;
	}
}
