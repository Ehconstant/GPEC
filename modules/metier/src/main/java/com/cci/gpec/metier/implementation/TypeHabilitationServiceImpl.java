package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.TypeHabilitationBean;
import com.cci.gpec.db.Groupe;
import com.cci.gpec.db.Typehabilitation;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class TypeHabilitationServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TypeHabilitationServiceImpl.class);
	
	/**
	 * Retourne la liste des Entreprises.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<TypeHabilitationBean> getTypeHabilitationList(int idGroupe)
			throws Exception {
		
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<TypeHabilitationBean> TypeHabilitationsInventoryBean;
			transaction = session.beginTransaction();

			List<Typehabilitation> TypeHabilitationsInventory = session
					.createQuery(
							"from Typehabilitation where Groupe IS NULL OR Groupe="
									+ idGroupe).list();

			TypeHabilitationsInventoryBean = TypeHabilitationPersistantListToTypeHabilitationBeanList(TypeHabilitationsInventory);
			transaction.commit();

			return TypeHabilitationsInventoryBean;

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

	public List<TypeHabilitationBean> getTypeHabilitationListForGroupWithoutCommon(
			int idGroupe) throws Exception {
		
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<TypeHabilitationBean> TypeHabilitationsInventoryBean;
			transaction = session.beginTransaction();

			List<Typehabilitation> TypeHabilitationsInventory = session
					.createQuery(
							"from Typehabilitation where Groupe=" + idGroupe)
					.list();

			TypeHabilitationsInventoryBean = TypeHabilitationPersistantListToTypeHabilitationBeanList(TypeHabilitationsInventory);
			transaction.commit();

			return TypeHabilitationsInventoryBean;

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

	public boolean supprimer(TypeHabilitationBean typeHabilitationBean,
			int idGroupe) throws Exception {
		HabilitationServiceImpl hab = new HabilitationServiceImpl();
		List<HabilitationBean> l = hab.getHabilitationsList(idGroupe);
		for (int i = 0; i < l.size(); i++) {
			HabilitationBean hb = l.get(i);
			if (hb.getIdTypeHabilitationSelected() == typeHabilitationBean
					.getId()) {
				return false;
			}
		}
		suppression(typeHabilitationBean);
		return true;
	}

	private void suppression(TypeHabilitationBean typeHabilitationBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typehabilitation typeHabilitationPersistant = new Typehabilitation();
			transaction = session.beginTransaction();
			if (typeHabilitationBean.getId() != 0) {
				typeHabilitationPersistant = (Typehabilitation) session.load(
						Typehabilitation.class, typeHabilitationBean.getId());

				typeHabilitationPersistant.setId(typeHabilitationBean.getId());
			}
			session.delete(typeHabilitationPersistant);
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

	public void deleteTypeHabilitationWithoutTransaction(
			TypeHabilitationBean typeHabilitationBean) {
		
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typehabilitation typeHabilitationPersistant = new Typehabilitation();
			if (typeHabilitationBean.getId() != 0) {
				typeHabilitationPersistant = (Typehabilitation) session.load(
						Typehabilitation.class, typeHabilitationBean.getId());

				typeHabilitationPersistant.setId(typeHabilitationBean.getId());
			}
			session.delete(typeHabilitationPersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void saveOrUppdate(TypeHabilitationBean typeHabilitationBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typehabilitation typeHabilitationPersistant = new Typehabilitation();
			transaction = session.beginTransaction();
			if (typeHabilitationBean.getId() != 0) {
				typeHabilitationPersistant = (Typehabilitation) session.load(
						Typehabilitation.class, typeHabilitationBean.getId());

				typeHabilitationPersistant.setId(typeHabilitationBean.getId());
			}
			typeHabilitationPersistant.setGroupe((Groupe) session.load(
					Groupe.class, typeHabilitationBean.getIdGroupe()));
			typeHabilitationPersistant
					.setNomTypeHabilitation(typeHabilitationBean.getNom());

			session.saveOrUpdate(typeHabilitationPersistant);

			typeHabilitationBean.setId(typeHabilitationPersistant.getId());

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

	public void save(TypeHabilitationBean typeHabilitationBean) {
		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typehabilitation typeHabilitationPersistant = new Typehabilitation();
			// transaction = session.beginTransaction();

			typeHabilitationPersistant.setId(typeHabilitationBean.getId());
			typeHabilitationPersistant.setGroupe((Groupe) session.load(
					Groupe.class, typeHabilitationBean.getIdGroupe()));
			typeHabilitationPersistant
					.setNomTypeHabilitation(typeHabilitationBean.getNom());

			session.save(typeHabilitationPersistant);
			// transaction.commit();
			typeHabilitationBean.setId(typeHabilitationPersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public TypeHabilitationBean getTypeHabilitationBeanById(
			Integer idTypeHabilitation) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Typehabilitation TypeHabilitationPersistant = new Typehabilitation();
			transaction = session.beginTransaction();

			TypeHabilitationPersistant = (Typehabilitation) session.load(
					Typehabilitation.class, idTypeHabilitation);

			TypeHabilitationBean TypeHabilitationBean = TypeHabilitationPersistantToTypeHabilitationBean(TypeHabilitationPersistant);

			transaction.commit();

			return TypeHabilitationBean;

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

	private List<TypeHabilitationBean> TypeHabilitationPersistantListToTypeHabilitationBeanList(
			List<Typehabilitation> TypeHabilitationPersistantList)
			throws Exception {

		List<TypeHabilitationBean> TypeHabilitationBeanList = new ArrayList<TypeHabilitationBean>();

		for (Typehabilitation TypeHabilitationPersistant : TypeHabilitationPersistantList) {

			TypeHabilitationBeanList
					.add(TypeHabilitationPersistantToTypeHabilitationBean(TypeHabilitationPersistant));
		}

		return TypeHabilitationBeanList;

	}

	public TypeHabilitationBean TypeHabilitationPersistantToTypeHabilitationBean(
			Typehabilitation typeHabilitationPersistant) throws Exception {

		TypeHabilitationBean typeHabilitationBean = new TypeHabilitationBean();
		typeHabilitationBean.setId(typeHabilitationPersistant.getId());
		typeHabilitationBean.setNom(typeHabilitationPersistant
				.getNomTypeHabilitation());

		return typeHabilitationBean;
	}
}
