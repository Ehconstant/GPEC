package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.MappingRepriseBean;
import com.cci.gpec.db.Groupe;
import com.cci.gpec.db.MappingReprise;
import com.cci.gpec.db.connection.HibernateUtil;

public class MappingRepriseServiceImpl {
	
	private static final Logger				LOGGER					= LoggerFactory.getLogger(MappingRepriseServiceImpl.class);

	public void saveOrUppdate(MappingRepriseBean mappingRepriseBean) {
		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			MappingReprise mappingReprisePersistant = new MappingReprise();
			// transaction = session.beginTransaction();

			Groupe groupePersistant = new Groupe();
			groupePersistant = (Groupe) session.load(Groupe.class,
					mappingRepriseBean.getIdGroupe());

			mappingReprisePersistant.setGroupe(groupePersistant);
			mappingReprisePersistant.setEntite(mappingRepriseBean.getEntite());
			mappingReprisePersistant.setOldId(mappingRepriseBean.getOldId());
			mappingReprisePersistant.setNewId(mappingRepriseBean.getNewId());

			session.save(mappingReprisePersistant);
			// transaction.commit();

		} catch (HibernateException e) {
			// if (transaction != null && !transaction.wasRolledBack()) {
			// 	transaction.rollback();
			// }
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void saveOrUppdateWithTransaction(
			MappingRepriseBean mappingRepriseBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			MappingReprise mappingReprisePersistant = new MappingReprise();
			transaction = session.beginTransaction();

			Groupe groupePersistant = new Groupe();
			groupePersistant = (Groupe) session.load(Groupe.class,
					mappingRepriseBean.getIdGroupe());

			mappingReprisePersistant.setGroupe(groupePersistant);
			mappingReprisePersistant.setEntite(mappingRepriseBean.getEntite());
			mappingReprisePersistant.setOldId(mappingRepriseBean.getOldId());
			mappingReprisePersistant.setNewId(mappingRepriseBean.getNewId());

			session.save(mappingReprisePersistant);

			mappingRepriseBean.setId(mappingReprisePersistant.getId());

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

	public MappingRepriseBean getMappingRepriseByOldIdSalarie(int oldIdSalarie,
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			MappingReprise mappingReprise = (MappingReprise) session
					.createQuery(
							"from MappingReprise as m where m.Groupe="
									+ idGroupe
									+ " and Entite='salarie' and OldId="
									+ oldIdSalarie).uniqueResult();

			MappingRepriseBean mappingRepriseBean = mappingReprisePersistantToMappingRepriseBean(mappingReprise);
			transaction.commit();

			return mappingRepriseBean;

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

	public List<MappingRepriseBean> getMappingRepriseEntrepriseByGroupe(
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<MappingReprise> mappingRepriseInventory = (List<MappingReprise>) session
					.createQuery(
							"from MappingReprise as m where m.Groupe="
									+ idGroupe + " and Entite='entreprise'")
					.list();

			List<MappingRepriseBean> mappingRepriseBeanList = mappingReprisePersistantListToMappingRepriseBeanList(mappingRepriseInventory);
			transaction.commit();

			return mappingRepriseBeanList;

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

	public MappingRepriseBean getMappingRepriseByOldIdEntreprise(
			int oldIdEntreprise, int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			MappingReprise mappingReprise = (MappingReprise) session
					.createQuery(
							"from MappingReprise as m where m.Groupe="
									+ idGroupe
									+ " and Entite='entreprise' and OldId="
									+ oldIdEntreprise).uniqueResult();

			MappingRepriseBean mappingRepriseBean = mappingReprisePersistantToMappingRepriseBean(mappingReprise);
			transaction.commit();

			return mappingRepriseBean;

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

	private List<MappingRepriseBean> mappingReprisePersistantListToMappingRepriseBeanList(
			List<MappingReprise> mappingReprisePersistantList) throws Exception {

		List<MappingRepriseBean> mappingRepriseBeanList = new ArrayList<MappingRepriseBean>();

		for (MappingReprise mappingReprisePersistant : mappingReprisePersistantList) {

			mappingRepriseBeanList
					.add(mappingReprisePersistantToMappingRepriseBean(mappingReprisePersistant));
		}

		return mappingRepriseBeanList;

	}

	public MappingRepriseBean mappingReprisePersistantToMappingRepriseBean(
			MappingReprise mappingPersistant) throws Exception {

		if (mappingPersistant == null)
			return null;
		MappingRepriseBean m = new MappingRepriseBean(
				mappingPersistant.getEntite(), mappingPersistant.getOldId(),
				mappingPersistant.getNewId(), mappingPersistant.getGroupe()
						.getId());

		return m;

	}

}
