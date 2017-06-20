package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.MotifRuptureContratBean;
import com.cci.gpec.db.Motifrupturecontrat;
import com.cci.gpec.db.connection.HibernateUtil;

public class MotifRuptureContratServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MotifRuptureContratServiceImpl.class);

	public List<MotifRuptureContratBean> getMotifRuptureContratList()
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<MotifRuptureContratBean> motifRuptureContratInventoryBean;
			transaction = session.beginTransaction();

			List<Motifrupturecontrat> MotifRuptureContratInventory = session
					.createQuery(
							"from Motifrupturecontrat order by OrdreAffichage")
					.list();

			motifRuptureContratInventoryBean = motifRuptureContratPersistantListToMotifRuptureContratBeanList(MotifRuptureContratInventory);
			transaction.commit();

			return motifRuptureContratInventoryBean;

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

	public void supprimer(MotifRuptureContratBean motifRuptureContratBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Motifrupturecontrat motifRuptureContratPersistant = new Motifrupturecontrat();
			transaction = session.beginTransaction();
			if (motifRuptureContratBean.getId() != 0) {
				motifRuptureContratPersistant = (Motifrupturecontrat) session
						.load(Motifrupturecontrat.class,
								motifRuptureContratBean.getId());

				motifRuptureContratPersistant.setId(motifRuptureContratBean
						.getId());
			}
			session.delete(motifRuptureContratPersistant);
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

	public void saveOrUppdate(MotifRuptureContratBean motifRuptureContratBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Motifrupturecontrat motifRuptureContratPersistant = new Motifrupturecontrat();
			transaction = session.beginTransaction();
			if (motifRuptureContratBean.getId() != 0) {
				motifRuptureContratPersistant = (Motifrupturecontrat) session
						.load(Motifrupturecontrat.class,
								motifRuptureContratBean.getId());

				motifRuptureContratPersistant.setId(motifRuptureContratBean
						.getId());
			}

			motifRuptureContratPersistant
					.setNomMotifRuptureContrat(motifRuptureContratBean.getNom());
			motifRuptureContratPersistant
					.setOrdreAffichage(motifRuptureContratBean
							.getOrdreAffichage());

			session.saveOrUpdate(motifRuptureContratPersistant);
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

	public void save(MotifRuptureContratBean motifRuptureContratBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Motifrupturecontrat motifRuptureContratPersistant = new Motifrupturecontrat();
			transaction = session.beginTransaction();

			motifRuptureContratPersistant
					.setId(motifRuptureContratBean.getId());

			motifRuptureContratPersistant
					.setNomMotifRuptureContrat(motifRuptureContratBean.getNom());

			motifRuptureContratPersistant
					.setOrdreAffichage(motifRuptureContratBean
							.getOrdreAffichage());

			session.save(motifRuptureContratPersistant);
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

	public MotifRuptureContratBean getMotifRuptureContratBeanById(
			Integer idMotifRuptureContrat) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			if (idMotifRuptureContrat > 0) {
				Motifrupturecontrat motifRuptureContratPersistant = new Motifrupturecontrat();
				transaction = session.beginTransaction();

				motifRuptureContratPersistant = (Motifrupturecontrat) session
						.load(Motifrupturecontrat.class, idMotifRuptureContrat);

				MotifRuptureContratBean motifRuptureContratBean = motifRuptureContratPersistantToMotifRuptureContratBean(motifRuptureContratPersistant);

				transaction.commit();

				return motifRuptureContratBean;
			} else {
				return null;
			}

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

	private List<MotifRuptureContratBean> motifRuptureContratPersistantListToMotifRuptureContratBeanList(
			List<Motifrupturecontrat> motifRuptureContratPersistantList)
			throws Exception {

		List<MotifRuptureContratBean> motifRuptureContratBeanList = new ArrayList<MotifRuptureContratBean>();

		for (Motifrupturecontrat motifRuptureContratPersistant : motifRuptureContratPersistantList) {

			motifRuptureContratBeanList
					.add(motifRuptureContratPersistantToMotifRuptureContratBean(motifRuptureContratPersistant));
		}

		return motifRuptureContratBeanList;

	}

	public MotifRuptureContratBean motifRuptureContratPersistantToMotifRuptureContratBean(
			Motifrupturecontrat motifRuptureContratPersistant) throws Exception {

		MotifRuptureContratBean motifRuptureContratBean = new MotifRuptureContratBean();
		motifRuptureContratBean.setId(motifRuptureContratPersistant.getId());
		motifRuptureContratBean.setNom(motifRuptureContratPersistant
				.getNomMotifRuptureContrat());

		return motifRuptureContratBean;
	}
}
