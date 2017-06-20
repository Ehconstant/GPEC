package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.TypeLesionBean;
import com.cci.gpec.db.Typelesion;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class TypeLesionServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TypeLesionServiceImpl.class);
	
	/**
	 * Retourne la liste des Entreprises.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<TypeLesionBean> getTypeLesionList() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<TypeLesionBean> TypeLesionsInventoryBean;
			transaction = session.beginTransaction();

			List<Typelesion> TypeLesionsInventory = session.createQuery(
					"from Typelesion").list();

			TypeLesionsInventoryBean = TypeLesionPersistantListToTypeLesionBeanList(TypeLesionsInventory);
			transaction.commit();

			return TypeLesionsInventoryBean;

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

	private void suppression(TypeLesionBean typeLesionBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typelesion typeLesionPersistant = new Typelesion();
			transaction = session.beginTransaction();
			if (typeLesionBean.getId() != 0) {
				typeLesionPersistant = (Typelesion) session.load(
						Typelesion.class, typeLesionBean.getId());

				typeLesionPersistant.setId(typeLesionBean.getId());
			}
			session.delete(typeLesionPersistant);
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

	public void saveOrUppdate(TypeLesionBean TypeLesionBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typelesion TypeLesionPersistant = new Typelesion();
			transaction = session.beginTransaction();
			if (TypeLesionBean.getId() != 0) {
				TypeLesionPersistant = (Typelesion) session.load(
						Typelesion.class, TypeLesionBean.getId());

				TypeLesionPersistant.setId(TypeLesionBean.getId());
			}

			TypeLesionPersistant.setNomTypeLesion(TypeLesionBean.getNom());

			session.saveOrUpdate(TypeLesionPersistant);
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

	public void save(TypeLesionBean TypeLesionBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typelesion TypeLesionPersistant = new Typelesion();
			transaction = session.beginTransaction();

			TypeLesionPersistant.setId(TypeLesionBean.getId());

			TypeLesionPersistant.setNomTypeLesion(TypeLesionBean.getNom());

			session.save(TypeLesionPersistant);
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

	public TypeLesionBean getTypeLesionBeanById(Integer idTypeLesion)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typelesion TypeLesionPersistant = new Typelesion();
			transaction = session.beginTransaction();

			TypeLesionPersistant = (Typelesion) session.load(Typelesion.class,
					idTypeLesion);

			TypeLesionBean TypeLesionBean = TypeLesionPersistantToTypeLesionBean(TypeLesionPersistant);

			transaction.commit();

			return TypeLesionBean;

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

	private List<TypeLesionBean> TypeLesionPersistantListToTypeLesionBeanList(
			List<Typelesion> TypeLesionPersistantList) throws Exception {

		List<TypeLesionBean> TypeLesionBeanList = new ArrayList<TypeLesionBean>();

		for (Typelesion TypeLesionPersistant : TypeLesionPersistantList) {

			TypeLesionBeanList
					.add(TypeLesionPersistantToTypeLesionBean(TypeLesionPersistant));
		}

		return TypeLesionBeanList;

	}

	public TypeLesionBean TypeLesionPersistantToTypeLesionBean(
			Typelesion TypeLesionPersistant) throws Exception {

		TypeLesionBean TypeLesionBean = new TypeLesionBean();
		TypeLesionBean.setId(TypeLesionPersistant.getId());
		TypeLesionBean.setNom(TypeLesionPersistant.getNomTypeLesion());

		return TypeLesionBean;
	}
}
