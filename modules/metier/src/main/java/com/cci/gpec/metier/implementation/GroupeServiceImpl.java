package com.cci.gpec.metier.implementation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.db.Groupe;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class GroupeServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupeServiceImpl.class);

	public void suppression(GroupeBean groupeBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Groupe groupePersistant = new Groupe();
			transaction = session.beginTransaction();
			if (groupeBean.getId() != 0) {
				groupePersistant = (Groupe) session.load(Groupe.class,
						groupeBean.getId());

				groupePersistant.setId(groupeBean.getId());
			}
			session.delete(groupePersistant);
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

	public String deleteGroupeWithoutTransaction(GroupeBean groupeBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Groupe groupePersistant = new Groupe();
			if (groupeBean.getId() != 0) {
				groupePersistant = (Groupe) session.load(Groupe.class,
						groupeBean.getId());

				groupePersistant.setId(groupeBean.getId());
			}
			groupePersistant.setDeleted(true);
			DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
			groupePersistant.setNomGroupe(groupeBean.getNom() + "_deleted_"
					+ dateFormat.format(new Date()));
			session.saveOrUpdate(groupePersistant);

			return groupeBean.getNom();

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void saveOrUppdate(GroupeBean groupeBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Groupe groupePersistant = new Groupe();
			transaction = session.beginTransaction();
			if (groupeBean.getId() != 0) {
				groupePersistant = (Groupe) session.load(Groupe.class,
						groupeBean.getId());

				groupePersistant.setId(groupeBean.getId());
			}

			groupePersistant.setNomGroupe(groupeBean.getNom());
			groupePersistant.setDeleted(groupeBean.isDeleted());
			groupePersistant
					.setFinPeriodeEssai(groupeBean.getFinPeriodeEssai());

			session.saveOrUpdate(groupePersistant);
			groupeBean.setId(groupePersistant.getId());
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

	public void save(GroupeBean groupeBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Groupe groupePersistant = new Groupe();

			groupePersistant.setId(groupeBean.getId());
			groupePersistant.setNomGroupe(groupeBean.getNom());
			groupePersistant.setDeleted(groupeBean.isDeleted());
			groupePersistant
					.setFinPeriodeEssai(groupeBean.getFinPeriodeEssai());

			session.save(groupePersistant);
			groupeBean.setId(groupePersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public GroupeBean getGroupeBeanById(Integer idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Groupe> groupes = session.createQuery(
					"from Groupe where Id=" + idGroupe + " and Deleted=false")
					.list();

			transaction.commit();

			if (groupes.size() > 0) {
				return groupePersistantToGroupeBean(groupes.get(0));
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

	public GroupeBean getGroupeBeanDeletedById(Integer idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Groupe> groupes = session.createQuery(
					"from Groupe where Id=" + idGroupe + " and Deleted=true")
					.list();

			transaction.commit();

			if (groupes.size() > 0) {
				return groupePersistantToGroupeBean(groupes.get(0));
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

	public GroupeBean getGroupeBeanByName(String nomGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			Query query = session.createQuery(
					"from Groupe where NomGroupe=:nomGroupe and Deleted=false")
					.setParameter("nomGroupe", nomGroupe);
			List<Groupe> groupes = query.list();

			transaction.commit();

			if (groupes.size() > 0) {
				return groupePersistantToGroupeBean(groupes.get(0));
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

	public List<GroupeBean> getGroupeBeanList(boolean closeSession) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<GroupeBean> groupeInventoryBean;
			transaction = session.beginTransaction();

			List<Groupe> groupeInventory = session.createQuery(
					"from Groupe where Deleted=false order by NomGroupe")
					.list();

			groupeInventoryBean = groupePersistantListToGroupeBeanList(groupeInventory);
			transaction.commit();

			return groupeInventoryBean;
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

	public Integer getMaxId() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List max = session.createQuery("select max(Id) from Groupe").list();

			transaction.commit();

			return (Integer) max.get(0);

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

	private List<GroupeBean> groupePersistantListToGroupeBeanList(
			List<Groupe> groupePersistantList) throws Exception {

		List<GroupeBean> groupeBeanList = new ArrayList<GroupeBean>();

		for (Groupe groupePersistant : groupePersistantList) {

			groupeBeanList.add(groupePersistantToGroupeBean(groupePersistant));
		}

		return groupeBeanList;

	}

	public GroupeBean groupePersistantToGroupeBean(Groupe groupePersistant)
			throws Exception {

		GroupeBean groupeBean = new GroupeBean();
		groupeBean.setId(groupePersistant.getId());
		groupeBean.setNom(groupePersistant.getNomGroupe());
		groupeBean.setDeleted(groupePersistant.getDeleted());
		groupeBean.setFinPeriodeEssai(groupePersistant.getFinPeriodeEssai());

		return groupeBean;
	}
}
