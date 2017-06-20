package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.TypeAccidentBean;
import com.cci.gpec.db.Typeaccident;
import com.cci.gpec.db.connection.HibernateUtil;
import com.cci.gpec.metier.interfaces.EntrepriseService;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class TypeAccidentServiceImpl implements EntrepriseService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TypeAccidentServiceImpl.class);
	
	/**
	 * Retourne la liste des Entreprises.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<TypeAccidentBean> getTypeAccidentList() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<TypeAccidentBean> TypeAccidentsInventoryBean;
			transaction = session.beginTransaction();

			List<Typeaccident> TypeAccidentsInventory = session.createQuery(
					"from Typeaccident").list();

			TypeAccidentsInventoryBean = TypeAccidentPersistantListToTypeAccidentBeanList(TypeAccidentsInventory);
			transaction.commit();

			return TypeAccidentsInventoryBean;

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

	private void suppression(TypeAccidentBean typeAccidentBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typeaccident TypeAccidentPersistant = new Typeaccident();
			transaction = session.beginTransaction();
			if (typeAccidentBean.getId() != 0) {
				TypeAccidentPersistant = (Typeaccident) session.load(
						Typeaccident.class, typeAccidentBean.getId());

				TypeAccidentPersistant.setId(typeAccidentBean.getId());
			}
			session.delete(TypeAccidentPersistant);
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

	public void saveOrUppdate(TypeAccidentBean TypeAccidentBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typeaccident TypeAccidentPersistant = new Typeaccident();
			transaction = session.beginTransaction();
			if (TypeAccidentBean.getId() != 0) {
				TypeAccidentPersistant = (Typeaccident) session.load(
						Typeaccident.class, TypeAccidentBean.getId());

				TypeAccidentPersistant.setId(TypeAccidentBean.getId());
			}

			TypeAccidentPersistant
					.setNomTypeAccident(TypeAccidentBean.getNom());

			session.saveOrUpdate(TypeAccidentPersistant);
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

	public void save(TypeAccidentBean TypeAccidentBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typeaccident TypeAccidentPersistant = new Typeaccident();
			transaction = session.beginTransaction();

			TypeAccidentPersistant.setId(TypeAccidentBean.getId());

			TypeAccidentPersistant
					.setNomTypeAccident(TypeAccidentBean.getNom());

			session.save(TypeAccidentPersistant);
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

	public TypeAccidentBean getTypeAccidentBeanById(Integer idTypeAccident)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typeaccident TypeAccidentPersistant = new Typeaccident();
			transaction = session.beginTransaction();

			TypeAccidentPersistant = (Typeaccident) session.load(
					Typeaccident.class, idTypeAccident);

			TypeAccidentBean TypeAccidentBean = TypeAccidentPersistantToTypeAccidentBean(TypeAccidentPersistant);

			transaction.commit();

			return TypeAccidentBean;

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

	private List<TypeAccidentBean> TypeAccidentPersistantListToTypeAccidentBeanList(
			List<Typeaccident> TypeAccidentPersistantList) throws Exception {

		List<TypeAccidentBean> TypeAccidentBeanList = new ArrayList<TypeAccidentBean>();

		for (Typeaccident TypeAccidentPersistant : TypeAccidentPersistantList) {

			TypeAccidentBeanList
					.add(TypeAccidentPersistantToTypeAccidentBean(TypeAccidentPersistant));
		}

		return TypeAccidentBeanList;

	}

	public TypeAccidentBean TypeAccidentPersistantToTypeAccidentBean(
			Typeaccident TypeAccidentPersistant) throws Exception {

		TypeAccidentBean TypeAccidentBean = new TypeAccidentBean();
		TypeAccidentBean.setId(TypeAccidentPersistant.getId());
		TypeAccidentBean.setNom(TypeAccidentPersistant.getNomTypeAccident());

		return TypeAccidentBean;
	}
}
