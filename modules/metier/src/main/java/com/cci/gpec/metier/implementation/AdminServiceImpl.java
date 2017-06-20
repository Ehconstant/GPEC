package com.cci.gpec.metier.implementation;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.AdminBean;
import com.cci.gpec.db.Admin;
import com.cci.gpec.db.Groupe;
import com.cci.gpec.db.connection.HibernateUtil;

public class AdminServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

	public void saveOrUppdate(AdminBean adminBean, int idGroupe) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Admin adminPersistant = new Admin();
			transaction = session.beginTransaction();

			if (adminBean.getId() == 0) {
				adminPersistant.setId(null);
			} else {
				adminPersistant.setId(adminBean.getId());
			}

			Groupe groupePersistant = new Groupe();
			groupePersistant = (Groupe) session.load(Groupe.class, idGroupe);

			adminPersistant.setGroupe(groupePersistant);

			if (adminBean.getLogo() != null) {
				adminPersistant.setLogo(adminBean.getLogo());
			}
			if (adminBean.isMigration()) {
				adminPersistant.setMigration(1);
			} else {
				adminPersistant.setMigration(0);
			}

			session.saveOrUpdate(adminPersistant);
			transaction.commit();

		} catch (HibernateException e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void save(AdminBean adminBean, int idGroupe) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Admin adminPersistant = new Admin();

			adminPersistant.setId(adminBean.getId());

			Groupe groupePersistant = new Groupe();
			groupePersistant = (Groupe) session.load(Groupe.class, idGroupe);

			adminPersistant.setGroupe(groupePersistant);

			adminPersistant.setLogo(adminBean.getLogo());
			if (adminBean.isMigration()) {
				adminPersistant.setMigration(1);
			} else {
				adminPersistant.setMigration(0);
			}

			session.save(adminPersistant);
			adminBean.setId(adminPersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public AdminBean getAdminBean(int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		AdminBean adminBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Admin adminPersistant = null;
			transaction = session.beginTransaction();

			adminPersistant = (Admin) session.createQuery(
					"from Admin where Groupe=" + idGroupe).uniqueResult();

			if (adminPersistant != null && adminPersistant.getId() != null) {
				adminBean = adminPersistantToAdminBean(adminPersistant);
			}

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
		return adminBean;
	}

	public AdminBean adminPersistantToAdminBean(Admin adminPersistant)
			throws Exception {

		AdminBean adminBean = new AdminBean();
		adminBean.setId(adminPersistant.getId());
		adminBean.setLogo(adminPersistant.getLogo());
		if (adminPersistant.getMigration() == 1) {
			adminBean.setMigration(true);
		} else {
			adminBean.setMigration(false);
		}
		return adminBean;
	}

	public void deleteAdminWithoutTransaction(AdminBean adminBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Admin adminPersistant = new Admin();
			if (adminBean.getId() != 0) {
				adminPersistant = (Admin) session.load(Admin.class,
						adminBean.getId());

				adminPersistant.setId(adminBean.getId());
			}
			session.delete(adminPersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

}
