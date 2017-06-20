package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.ObjectifsEntretienBean;
import com.cci.gpec.db.Entretien;
import com.cci.gpec.db.Objectifsentretien;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class ObjectifsEntretienServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectifsEntretienServiceImpl.class);

	public List<ObjectifsEntretienBean> getObjectifsEntretienList(int idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<ObjectifsEntretienBean> objectifsEntretienInventoryBean;
			transaction = session.beginTransaction();

			List<Objectifsentretien> objectifsEntretienBeanInventory = session
					.createQuery(
							"from Objectifsentretien as o left join fetch o.Entretien as e left join fetch e.Salarie as s left join fetch s.Entreprise as ent where ent.Groupe="
									+ idGroupe).list();

			objectifsEntretienInventoryBean = objectifsEntretienPersistantListToObjectifsEntretienBeanList(objectifsEntretienBeanInventory);
			transaction.commit();

			return objectifsEntretienInventoryBean;

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

	public List<ObjectifsEntretienBean> getObjectifsEntretienListByIdEntretien(
			Integer idEntretien) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<ObjectifsEntretienBean> objectifsEntretienInventoryBean;
			transaction = session.beginTransaction();

			List<Objectifsentretien> objectifsEntretienBeanInventory = session
					.createQuery(
							"from Objectifsentretien where Entretien="
									+ idEntretien).list();

			objectifsEntretienInventoryBean = objectifsEntretienPersistantListToObjectifsEntretienBeanList(objectifsEntretienBeanInventory);
			transaction.commit();

			return objectifsEntretienInventoryBean;

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

	public void suppression(ObjectifsEntretienBean objectifsEntretienBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Objectifsentretien objectifsEntretienPersistant = new Objectifsentretien();
			transaction = session.beginTransaction();
			if (objectifsEntretienBean.getIdObjectif() != 0) {
				objectifsEntretienPersistant = (Objectifsentretien) session
						.load(Objectifsentretien.class,
								objectifsEntretienBean.getIdObjectif());

				objectifsEntretienPersistant
						.setIdObjectif(objectifsEntretienBean.getIdObjectif());
			}
			session.delete(objectifsEntretienPersistant);
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

	public void deleteObjectifWithoutTransaction(
			ObjectifsEntretienBean objectifsEntretienBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Objectifsentretien objectifsEntretienPersistant = new Objectifsentretien();
			if (objectifsEntretienBean.getIdObjectif() != 0) {
				objectifsEntretienPersistant = (Objectifsentretien) session
						.load(Objectifsentretien.class,
								objectifsEntretienBean.getIdObjectif());

				objectifsEntretienPersistant
						.setIdObjectif(objectifsEntretienBean.getIdObjectif());
			}
			session.delete(objectifsEntretienPersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void saveOrUppdate(ObjectifsEntretienBean objectifsEntretienBean)
			throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Objectifsentretien objectifsEntretienPersistant = new Objectifsentretien();
			transaction = session.beginTransaction();

			objectifsEntretienPersistant = objectifsEntretienBeanToObjectifsEntretienPersistant(
					objectifsEntretienBean,
					objectifsEntretienBean.getIdEntretien());
			session.saveOrUpdate(objectifsEntretienPersistant);

			objectifsEntretienBean.setIdObjectif(objectifsEntretienPersistant
					.getIdObjectif());

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

	public void save(ObjectifsEntretienBean objectifsEntretienBean)
			throws Exception {
		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			if (objectifsEntretienBean.getIdEntretien() != null) {
				Objectifsentretien objectifsEntretienPersistant = new Objectifsentretien();
				// transaction = session.beginTransaction();

				objectifsEntretienPersistant = objectifsEntretienBeanToObjectifsEntretienPersistant(
						objectifsEntretienBean,
						objectifsEntretienBean.getIdEntretien());
				session.save(objectifsEntretienPersistant);

				objectifsEntretienBean
						.setIdObjectif(objectifsEntretienPersistant
								.getIdObjectif());
			}
			// transaction.commit();
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public ObjectifsEntretienBean getobjectifsEntretienBeanById(
			Integer idObjectifsEntretien) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Objectifsentretien objectifsEntretienPersistant = new Objectifsentretien();
			transaction = session.beginTransaction();

			objectifsEntretienPersistant = (Objectifsentretien) session.load(
					Objectifsentretien.class, idObjectifsEntretien);

			ObjectifsEntretienBean objectifsEntretienBean = objectifsEntretienPersistantToObjectifsEntretienBean(objectifsEntretienPersistant);

			transaction.commit();

			return objectifsEntretienBean;

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

	private List<ObjectifsEntretienBean> objectifsEntretienPersistantListToObjectifsEntretienBeanList(
			List<Objectifsentretien> objectifsEntretienPersistantList)
			throws Exception {

		List<ObjectifsEntretienBean> objectifsEntretienBeanList = new ArrayList<ObjectifsEntretienBean>();

		for (Objectifsentretien objectifsEntretienPersistant : objectifsEntretienPersistantList) {

			objectifsEntretienBeanList
					.add(objectifsEntretienPersistantToObjectifsEntretienBean(objectifsEntretienPersistant));
		}

		return objectifsEntretienBeanList;

	}

	public ObjectifsEntretienBean objectifsEntretienPersistantToObjectifsEntretienBean(
			Objectifsentretien objectifsEntretienPersistant) throws Exception {

		ObjectifsEntretienBean objectifsEntretienBean = new ObjectifsEntretienBean();
		objectifsEntretienBean.setIdObjectif(objectifsEntretienPersistant
				.getIdObjectif());
		objectifsEntretienBean.setIdEntretien(objectifsEntretienPersistant
				.getEntretien().getId());
		objectifsEntretienBean.setIntitule(objectifsEntretienPersistant
				.getIntitule());
		objectifsEntretienBean
				.setDelai(objectifsEntretienPersistant.getDelai());
		objectifsEntretienBean
				.setMoyen(objectifsEntretienPersistant.getMoyen());
		objectifsEntretienBean.setResultat(objectifsEntretienPersistant
				.getResultat());
		objectifsEntretienBean.setCommentaire(objectifsEntretienPersistant
				.getCommentaire());

		return objectifsEntretienBean;
	}

	public Objectifsentretien objectifsEntretienBeanToObjectifsEntretienPersistant(
			ObjectifsEntretienBean objectifsEntretienBean, Integer idEntretien)
			throws Exception {

		Session session = null;
		Objectifsentretien objectifsEntretienPersistant = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			objectifsEntretienPersistant = new Objectifsentretien();
	
			Entretien entretien = (Entretien) session.load(Entretien.class,
					idEntretien);
	
			objectifsEntretienPersistant.setIdObjectif(objectifsEntretienBean
					.getIdObjectif());
			objectifsEntretienPersistant.setEntretien(entretien);
			objectifsEntretienPersistant.setIntitule(objectifsEntretienBean
					.getIntitule());
			objectifsEntretienPersistant
					.setDelai(objectifsEntretienBean.getDelai());
			objectifsEntretienPersistant
					.setMoyen(objectifsEntretienBean.getMoyen());
			objectifsEntretienPersistant.setResultat(objectifsEntretienBean
					.getResultat());
			objectifsEntretienPersistant.setCommentaire(objectifsEntretienBean
					.getCommentaire());

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
		return objectifsEntretienPersistant;
	}
}
