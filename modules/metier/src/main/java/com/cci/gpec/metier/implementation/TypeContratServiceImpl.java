package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.TypeContratBean;
import com.cci.gpec.db.Typecontrat;
import com.cci.gpec.db.connection.HibernateUtil;
import com.cci.gpec.metier.interfaces.EntrepriseService;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class TypeContratServiceImpl implements EntrepriseService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TypeContratServiceImpl.class);
	
	/**
	 * Retourne la liste des Entreprises.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<TypeContratBean> getTypeContratList() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<TypeContratBean> TypeContratsInventoryBean;
			transaction = session.beginTransaction();

			List<Typecontrat> TypeContratsInventory = session.createQuery(
					"from Typecontrat order by NomTypeContrat desc").list();

			TypeContratsInventoryBean = TypeContratPersistantListToTypeContratBeanList(TypeContratsInventory);
			transaction.commit();

			return TypeContratsInventoryBean;

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

	public void supprimer(TypeContratBean typeContratBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typecontrat typeContratPersistant = new Typecontrat();
			transaction = session.beginTransaction();
			if (typeContratBean.getId() != 0) {
				typeContratPersistant = (Typecontrat) session.load(
						Typecontrat.class, typeContratBean.getId());

				typeContratPersistant.setId(typeContratBean.getId());
			}
			session.delete(typeContratPersistant);
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

	public void saveOrUppdate(TypeContratBean TypeContratBean) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typecontrat TypeContratPersistant = new Typecontrat();
			transaction = session.beginTransaction();
			if (TypeContratBean.getId() != 0) {
				TypeContratPersistant = (Typecontrat) session.load(
						Typecontrat.class, TypeContratBean.getId());

				TypeContratPersistant.setId(TypeContratBean.getId());
			}

			TypeContratPersistant.setNomTypeContrat(TypeContratBean.getNom());

			session.saveOrUpdate(TypeContratPersistant);
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

	public void save(TypeContratBean TypeContratBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typecontrat TypeContratPersistant = new Typecontrat();
			transaction = session.beginTransaction();

			TypeContratPersistant.setId(TypeContratBean.getId());

			TypeContratPersistant.setNomTypeContrat(TypeContratBean.getNom());

			session.save(TypeContratPersistant);
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

	public TypeContratBean getTypeContratBeanById(Integer idTypeContrat)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typecontrat TypeContratPersistant = new Typecontrat();
			transaction = session.beginTransaction();

			TypeContratPersistant = (Typecontrat) session.load(
					Typecontrat.class, idTypeContrat);

			TypeContratBean TypeContratBean = TypeContratPersistantToTypeContratBean(TypeContratPersistant);

			transaction.commit();

			return TypeContratBean;

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

	private List<TypeContratBean> TypeContratPersistantListToTypeContratBeanList(
			List<Typecontrat> TypeContratPersistantList) throws Exception {

		List<TypeContratBean> TypeContratBeanList = new ArrayList<TypeContratBean>();

		for (Typecontrat TypeContratPersistant : TypeContratPersistantList) {

			TypeContratBeanList
					.add(TypeContratPersistantToTypeContratBean(TypeContratPersistant));
		}

		return TypeContratBeanList;

	}

	public TypeContratBean TypeContratPersistantToTypeContratBean(
			Typecontrat TypeContratPersistant) throws Exception {

		TypeContratBean TypeContratBean = new TypeContratBean();
		TypeContratBean.setId(TypeContratPersistant.getId());
		TypeContratBean.setNom(TypeContratPersistant.getNomTypeContrat());

		return TypeContratBean;
	}
}
