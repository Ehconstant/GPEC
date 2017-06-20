package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.TypeCauseAccidentBean;
import com.cci.gpec.db.Typecauseaccident;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class TypeCauseAccidentServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TypeCauseAccidentServiceImpl.class);
	
	/**
	 * Retourne la liste des Entreprises.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<TypeCauseAccidentBean> getTypeCauseAccidentList()
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<TypeCauseAccidentBean> TypeCauseAccidentsInventoryBean;
			transaction = session.beginTransaction();

			List<Typecauseaccident> TypeCauseAccidentsInventory = session
					.createQuery("from Typecauseaccident").list();

			TypeCauseAccidentsInventoryBean = TypeCauseAccidentPersistantListToTypeCauseAccidentBeanList(TypeCauseAccidentsInventory);
			transaction.commit();

			return TypeCauseAccidentsInventoryBean;

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

	private void suppression(TypeCauseAccidentBean typeCauseAccidentBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typecauseaccident typeCauseAccidentPersistant = new Typecauseaccident();
			transaction = session.beginTransaction();
			if (typeCauseAccidentBean.getId() != 0) {
				typeCauseAccidentPersistant = (Typecauseaccident) session.load(
						Typecauseaccident.class, typeCauseAccidentBean.getId());

				typeCauseAccidentPersistant
						.setId(typeCauseAccidentBean.getId());
			}
			session.delete(typeCauseAccidentPersistant);
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

	public void saveOrUppdate(TypeCauseAccidentBean TypeCauseAccidentBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typecauseaccident TypeCauseAccidentPersistant = new Typecauseaccident();
			transaction = session.beginTransaction();
			if (TypeCauseAccidentBean.getId() != 0) {
				TypeCauseAccidentPersistant = (Typecauseaccident) session.load(
						Typecauseaccident.class, TypeCauseAccidentBean.getId());

				TypeCauseAccidentPersistant
						.setId(TypeCauseAccidentBean.getId());
			}

			TypeCauseAccidentPersistant
					.setNomTypeCauseAccident(TypeCauseAccidentBean.getNom());

			session.saveOrUpdate(TypeCauseAccidentPersistant);
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

	public void save(TypeCauseAccidentBean TypeCauseAccidentBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typecauseaccident TypeCauseAccidentPersistant = new Typecauseaccident();
			transaction = session.beginTransaction();

			TypeCauseAccidentPersistant.setId(TypeCauseAccidentBean.getId());

			TypeCauseAccidentPersistant
					.setNomTypeCauseAccident(TypeCauseAccidentBean.getNom());

			session.save(TypeCauseAccidentPersistant);
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

	public TypeCauseAccidentBean getTypeCauseAccidentBeanById(
			Integer idTypeCauseAccident) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typecauseaccident TypeCauseAccidentPersistant = new Typecauseaccident();
			transaction = session.beginTransaction();

			TypeCauseAccidentPersistant = (Typecauseaccident) session.load(
					Typecauseaccident.class, idTypeCauseAccident);

			TypeCauseAccidentBean TypeCauseAccidentBean = TypeCauseAccidentPersistantToTypeCauseAccidentBean(TypeCauseAccidentPersistant);

			transaction.commit();

			return TypeCauseAccidentBean;

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

	private List<TypeCauseAccidentBean> TypeCauseAccidentPersistantListToTypeCauseAccidentBeanList(
			List<Typecauseaccident> TypeCauseAccidentPersistantList)
			throws Exception {

		List<TypeCauseAccidentBean> TypeCauseAccidentBeanList = new ArrayList<TypeCauseAccidentBean>();

		for (Typecauseaccident TypeCauseAccidentPersistant : TypeCauseAccidentPersistantList) {

			TypeCauseAccidentBeanList
					.add(TypeCauseAccidentPersistantToTypeCauseAccidentBean(TypeCauseAccidentPersistant));
		}

		return TypeCauseAccidentBeanList;

	}

	public TypeCauseAccidentBean TypeCauseAccidentPersistantToTypeCauseAccidentBean(
			Typecauseaccident TypeCauseAccidentPersistant) throws Exception {

		TypeCauseAccidentBean TypeCauseAccidentBean = new TypeCauseAccidentBean();
		TypeCauseAccidentBean.setId(TypeCauseAccidentPersistant.getId());
		TypeCauseAccidentBean.setNom(TypeCauseAccidentPersistant
				.getNomTypeCauseAccident());

		return TypeCauseAccidentBean;
	}
}
