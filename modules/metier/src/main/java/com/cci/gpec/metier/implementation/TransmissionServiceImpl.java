package com.cci.gpec.metier.implementation;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.TransmissionBean;
import com.cci.gpec.db.Groupe;
import com.cci.gpec.db.Transmission;
import com.cci.gpec.db.connection.HibernateUtil;

public class TransmissionServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransmissionServiceImpl.class);

	public void saveOrUppdate(TransmissionBean transmissionBean, int idGroupe) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transmission transmissionPersistant = new Transmission();
			transaction = session.beginTransaction();

			if (transmissionBean.getId() == -1) {
				transmissionPersistant.setId(null);
			} else {
				transmissionPersistant.setId(transmissionBean.getId());
			}

			Groupe groupe = (Groupe) session.load(Groupe.class, idGroupe);
			transmissionPersistant.setGroupe(groupe);
			transmissionPersistant.setDateTransmission(transmissionBean
					.getDateTransmission());
			transmissionPersistant.setDateDerniereDemande(transmissionBean
					.getDateDerniereDemande());

			session.saveOrUpdate(transmissionPersistant);
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

	public void save(TransmissionBean transmissionBean, int idGroupe) {
		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transmission transmissionPersistant = new Transmission();
			// transaction = session.beginTransaction();

			if (transmissionBean.getId() == -1) {
				transmissionPersistant.setId(null);
			} else {
				transmissionPersistant.setId(transmissionBean.getId());
			}

			Groupe groupe = (Groupe) session.load(Groupe.class, idGroupe);
			transmissionPersistant.setGroupe(groupe);
			transmissionPersistant.setDateTransmission(transmissionBean
					.getDateTransmission());
			transmissionPersistant.setDateDerniereDemande(transmissionBean
					.getDateDerniereDemande());

			session.save(transmissionPersistant);
			// transaction.commit();
			transmissionBean.setId(transmissionPersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public TransmissionBean getTransmissionBean(int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			Transmission transmission = (Transmission) session.createQuery(
					"from Transmission where Groupe=" + idGroupe)
					.uniqueResult();

			transaction.commit();

			return transmissionPersistantToTransmissionBean(transmission);

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

	public TransmissionBean transmissionPersistantToTransmissionBean(
			Transmission transmissionPersistant) throws Exception {

		TransmissionBean transmissionBean = new TransmissionBean();
		transmissionBean.setId(transmissionPersistant.getId());
		transmissionBean
				.setIdGroupe(transmissionPersistant.getGroupe().getId());
		transmissionBean.setDateDerniereDemande(transmissionPersistant
				.getDateDerniereDemande());
		transmissionBean.setDateTransmission(transmissionPersistant
				.getDateTransmission());

		return transmissionBean;
	}

	public void deleteTransmissionWithoutTransaction(
			TransmissionBean transmissionBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transmission transmissionPersistant = new Transmission();
			if (transmissionBean.getId() != 0) {
				transmissionPersistant = (Transmission) session.load(
						Transmission.class, transmissionBean.getId());

				transmissionPersistant.setId(transmissionBean.getId());
			}
			session.delete(transmissionPersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}
}
