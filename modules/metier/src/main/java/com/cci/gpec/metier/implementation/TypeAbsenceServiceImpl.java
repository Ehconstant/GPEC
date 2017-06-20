package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.TypeAbsenceBean;
import com.cci.gpec.db.Groupe;
import com.cci.gpec.db.Typeabsence;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class TypeAbsenceServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TypeAbsenceServiceImpl.class);
	
	/**
	 * Retourne la liste des Entreprises.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<TypeAbsenceBean> getTypeAbsenceList(int idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<TypeAbsenceBean> typeAbsencesInventoryBean;
			transaction = session.beginTransaction();

			List<Typeabsence> typeAbsencesInventory = session.createQuery(
					"from Typeabsence where Groupe IS NULL OR Groupe="
							+ idGroupe).list();

			typeAbsencesInventoryBean = typeAbsencePersistantListToTypeAbsenceBeanList(typeAbsencesInventory);
			transaction.commit();

			return typeAbsencesInventoryBean;

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

	public List<TypeAbsenceBean> getTypeAbsenceListForGroupWithoutCommon(
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<TypeAbsenceBean> typeAbsencesInventoryBean;
			transaction = session.beginTransaction();

			List<Typeabsence> typeAbsencesInventory = session.createQuery(
					"from Typeabsence where Groupe=" + idGroupe).list();

			typeAbsencesInventoryBean = typeAbsencePersistantListToTypeAbsenceBeanList(typeAbsencesInventory);
			transaction.commit();

			return typeAbsencesInventoryBean;

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

	public List<TypeAbsenceBean> getOnlyTypeAbsenceList() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<TypeAbsenceBean> typeAbsencesInventoryBean = new ArrayList<TypeAbsenceBean>();
			transaction = session.beginTransaction();

			List<Typeabsence> typeAbsencesInventory = session.createQuery(
					"from Typeabsence").list();

			// de 0 à 6 car dans la table fixe des type d'absence il y a 7
			// éléments de type absence
			// et le reste correspond aux congés.
			for (int i = 0; i < 7; i++) {
				typeAbsencesInventoryBean
						.add(typeAbsencePersistantToTypeAbsenceBean(typeAbsencesInventory
								.get(i)));
			}
			transaction.commit();

			return typeAbsencesInventoryBean;

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

	public void suppression(TypeAbsenceBean typeAbsenceBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typeabsence typeAbsencePersistant = new Typeabsence();
			transaction = session.beginTransaction();
			if (typeAbsenceBean.getId() != 0) {
				typeAbsencePersistant = (Typeabsence) session.load(
						Typeabsence.class, typeAbsenceBean.getId());

				typeAbsencePersistant.setId(typeAbsenceBean.getId());
			}
			session.delete(typeAbsencePersistant);
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

	public void deleteTypeAbsenceWithoutTransaction(
			TypeAbsenceBean typeAbsenceBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typeabsence typeAbsencePersistant = new Typeabsence();
			if (typeAbsenceBean.getId() != 0) {
				typeAbsencePersistant = (Typeabsence) session.load(
						Typeabsence.class, typeAbsenceBean.getId());

				typeAbsencePersistant.setId(typeAbsenceBean.getId());
			}
			session.delete(typeAbsencePersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void saveOrUppdate(TypeAbsenceBean typeAbsenceBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typeabsence typeAbsencePersistant = new Typeabsence();
			transaction = session.beginTransaction();
			if (typeAbsenceBean.getId() != 0) {
				typeAbsencePersistant = (Typeabsence) session.load(
						Typeabsence.class, typeAbsenceBean.getId());

				typeAbsencePersistant.setId(typeAbsenceBean.getId());
			}
			typeAbsencePersistant.setGroupe((Groupe) session.load(Groupe.class,
					typeAbsenceBean.getIdGroupe()));
			typeAbsencePersistant.setNomTypeAbsence(typeAbsenceBean.getNom());

			session.saveOrUpdate(typeAbsencePersistant);

			typeAbsenceBean.setId(typeAbsencePersistant.getId());

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

	public void save(TypeAbsenceBean typeAbsenceBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typeabsence typeAbsencePersistant = new Typeabsence();

			typeAbsencePersistant.setId(typeAbsenceBean.getId());
			typeAbsencePersistant.setGroupe((Groupe) session.load(Groupe.class,
					typeAbsenceBean.getIdGroupe()));
			typeAbsencePersistant.setNomTypeAbsence(typeAbsenceBean.getNom());

			session.save(typeAbsencePersistant);
			typeAbsenceBean.setId(typeAbsencePersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public TypeAbsenceBean getTypeAbsenceBeanById(Integer idTypeAbsence)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typeabsence typeAbsencePersistant = new Typeabsence();
			transaction = session.beginTransaction();

			typeAbsencePersistant = (Typeabsence) session.load(
					Typeabsence.class, idTypeAbsence);

			TypeAbsenceBean typeAbsenceBean = typeAbsencePersistantToTypeAbsenceBean(typeAbsencePersistant);

			transaction.commit();

			return typeAbsenceBean;

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

	private List<TypeAbsenceBean> typeAbsencePersistantListToTypeAbsenceBeanList(
			List<Typeabsence> typeAbsencePersistantList) throws Exception {

		List<TypeAbsenceBean> typeAbsenceBeanList = new ArrayList<TypeAbsenceBean>();

		for (Typeabsence typeAbsencePersistant : typeAbsencePersistantList) {

			typeAbsenceBeanList
					.add(typeAbsencePersistantToTypeAbsenceBean(typeAbsencePersistant));
		}

		return typeAbsenceBeanList;

	}

	public TypeAbsenceBean typeAbsencePersistantToTypeAbsenceBean(
			Typeabsence typeAbsencePersistant) throws Exception {

		TypeAbsenceBean typeAbsenceBean = new TypeAbsenceBean();
		typeAbsenceBean.setId(typeAbsencePersistant.getId());
		typeAbsenceBean.setNom(typeAbsencePersistant.getNomTypeAbsence());

		return typeAbsenceBean;
	}
}
