package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.TypeRecoursInterimBean;
import com.cci.gpec.db.Typerecours;
import com.cci.gpec.db.connection.HibernateUtil;

public class TypeRecoursInterimServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TypeRecoursInterimServiceImpl.class);

	public List<TypeRecoursInterimBean> getTypeRecoursInterimList()
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<TypeRecoursInterimBean> typeRecoursInterimInventoryBean;
			transaction = session.beginTransaction();

			List<Typerecours> TypeRecoursInterimInventory = session
					.createQuery("from Typerecours").list();

			typeRecoursInterimInventoryBean = typeRecoursInterimPersistantListToTypeRecoursInterimBeanList(TypeRecoursInterimInventory);
			transaction.commit();

			return typeRecoursInterimInventoryBean;

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

	public void supprimer(TypeRecoursInterimBean typeRecoursInterimBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typerecours typeRecoursInterimPersistant = new Typerecours();
			transaction = session.beginTransaction();
			if (typeRecoursInterimBean.getId() != 0) {
				typeRecoursInterimPersistant = (Typerecours) session.load(
						Typerecours.class, typeRecoursInterimBean.getId());

				typeRecoursInterimPersistant.setId(typeRecoursInterimBean
						.getId());
			}
			session.delete(typeRecoursInterimPersistant);
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

	public void saveOrUppdate(TypeRecoursInterimBean typeRecoursInterimBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typerecours typeRecoursInterimPersistant = new Typerecours();
			transaction = session.beginTransaction();
			if (typeRecoursInterimBean.getId() != 0) {
				typeRecoursInterimPersistant = (Typerecours) session.load(
						Typerecours.class, typeRecoursInterimBean.getId());

				typeRecoursInterimPersistant.setId(typeRecoursInterimBean
						.getId());
			}

			typeRecoursInterimPersistant
					.setNomTypeRecours(typeRecoursInterimBean.getNom());

			session.saveOrUpdate(typeRecoursInterimPersistant);
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

	public void save(TypeRecoursInterimBean typeRecoursInterimBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typerecours typeRecoursInterimPersistant = new Typerecours();
			transaction = session.beginTransaction();

			typeRecoursInterimPersistant.setId(typeRecoursInterimBean.getId());

			typeRecoursInterimPersistant
					.setNomTypeRecours(typeRecoursInterimBean.getNom());

			session.save(typeRecoursInterimPersistant);
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

	public TypeRecoursInterimBean getTypeRecoursInterimBeanById(
			Integer idTypeRecoursInterim) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			if (idTypeRecoursInterim > 0) {
				Typerecours typeRecoursInterimPersistant = new Typerecours();
				transaction = session.beginTransaction();

				typeRecoursInterimPersistant = (Typerecours) session.load(
						Typerecours.class, idTypeRecoursInterim);

				TypeRecoursInterimBean typeRecoursInterimBean = typeRecoursInterimPersistantToTypeRecoursInterimBean(typeRecoursInterimPersistant);

				transaction.commit();

				return typeRecoursInterimBean;
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

	private List<TypeRecoursInterimBean> typeRecoursInterimPersistantListToTypeRecoursInterimBeanList(
			List<Typerecours> typeRecoursInterimPersistantList)
			throws Exception {

		List<TypeRecoursInterimBean> typeRecoursInterimBeanList = new ArrayList<TypeRecoursInterimBean>();

		for (Typerecours typeRecoursInterimPersistant : typeRecoursInterimPersistantList) {

			typeRecoursInterimBeanList
					.add(typeRecoursInterimPersistantToTypeRecoursInterimBean(typeRecoursInterimPersistant));
		}

		return typeRecoursInterimBeanList;

	}

	public TypeRecoursInterimBean typeRecoursInterimPersistantToTypeRecoursInterimBean(
			Typerecours typeRecoursInterimPersistant) throws Exception {

		TypeRecoursInterimBean typeRecoursInterimBean = new TypeRecoursInterimBean();
		typeRecoursInterimBean.setId(typeRecoursInterimPersistant.getId());
		typeRecoursInterimBean.setNom(typeRecoursInterimPersistant
				.getNomTypeRecours());

		return typeRecoursInterimBean;
	}

}
