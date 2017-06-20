package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.UserBean;
import com.cci.gpec.db.Groupe;
import com.cci.gpec.db.User;
import com.cci.gpec.db.connection.HibernateUtil;

public class UserServiceImpl {
	
	private static final Logger				LOGGER					= LoggerFactory.getLogger(UserServiceImpl.class);

	public List<UserBean> getUserList(int idGroupe, boolean superAdmin,
			boolean all, boolean admin) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			if (idGroupe == -1) {
				return new ArrayList<UserBean>();
			}
			List<UserBean> UsersInventoryBean;
			transaction = session.beginTransaction();
			List<User> UsersInventory;
			if (all) {
				if (!admin) {
					UsersInventory = session.createQuery(
							"from User as u where u.Groupe=" + idGroupe
									+ " and u.Admin='0'").list();
				} else {
					UsersInventory = session.createQuery(
							"from User as u where u.Groupe=" + idGroupe).list();
				}
			} else {
				if (superAdmin) {
					UsersInventory = session.createQuery(
							"from User as u where u.SuperAdmin='1'").list();
				} else {
					UsersInventory = session.createQuery(
							"from User as u where u.Groupe=" + idGroupe
									+ " and u.SuperAdmin='0'").list();
				}
			}

			UsersInventoryBean = UserPersistantListToUserBeanList(UsersInventory);
			transaction.commit();

			return UsersInventoryBean;

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

	public List<UserBean> getDemandesVersionEssaiList() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<UserBean> UsersInventoryBean;
			transaction = session.beginTransaction();
			List<User> UsersInventory;
			UsersInventory = session.createQuery(
					"from User as u where u.DateDemandeVersionEssai <> null")
					.list();

			UsersInventoryBean = UserPersistantListToUserBeanList(UsersInventory);
			transaction.commit();

			return UsersInventoryBean;

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

	public List<UserBean> getVersionEssaiList() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<UserBean> UsersInventoryBean;
			transaction = session.beginTransaction();
			List<User> UsersInventory;
			UsersInventory = session
					.createQuery(
							"from User as u left join fetch u.Groupe as g where g.FinPeriodeEssai <> null and g.Deleted=false")
					.list();

			UsersInventoryBean = UserPersistantListToUserBeanList(UsersInventory);
			transaction.commit();

			return UsersInventoryBean;

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

	public UserBean getUserByLogin(String login) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			List<User> usersInventory;

			Query query = session.createQuery("from User where Login=:login")
					.setParameter("login", login);
			usersInventory = query.list();
			transaction.commit();

			if (usersInventory != null && usersInventory.size() >= 1) {
				return UserPersistantToUserBean(usersInventory.get(0));
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

	public UserBean getUserByLoginExceptSuperAdmin(String login)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			List<User> usersInventory;

			Query query = session.createQuery(
					"from User where Login=:login and SuperAdmin='0'")
					.setParameter("login", login);
			usersInventory = query.list();
			transaction.commit();

			if (usersInventory != null && usersInventory.size() >= 1) {
				return UserPersistantToUserBean(usersInventory.get(0));
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

	public UserBean getUser(String login, String mdp) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			Query query = session
					.createQuery(
							"from User where Login=:login and Password=:password")
					.setParameter("login", login).setParameter("password", mdp);
			User user = (User) query.uniqueResult();
			transaction.commit();

			if (user != null) {
				return UserPersistantToUserBean(user);
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

	public boolean isSuperAdmin(String mdp) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			Query query = session
					.createQuery(
							"from User where Login='superAdmin' and Password=:password")
					.setParameter("password", mdp);
			User user = (User) query.uniqueResult();
			transaction.commit();

			if (user != null) {
				return true;
			} else {
				return false;
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

	public void suppression(UserBean UserBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			User UserPersistant = new User();
			transaction = session.beginTransaction();
			if (UserBean.getId() != 0) {
				UserPersistant = (User) session.load(User.class,
						UserBean.getId());

				UserPersistant.setId(UserBean.getId());
			}
			session.delete(UserPersistant);
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

	public void deleteUserWithoutTransaction(UserBean userBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			User userPersistant = new User();
			if (userBean.getId() != 0) {
				userPersistant = (User) session.load(User.class,
						userBean.getId());

				userPersistant.setId(userBean.getId());
			}
			session.delete(userPersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void saveOrUppdateWithoutTransaction(UserBean userBean, int idGroupe) {
		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			User userPersistant = new User();
			// transaction = session.beginTransaction();
			if (userBean.getId() != 0) {
				userPersistant = (User) session.load(User.class,
						userBean.getId());

				userPersistant.setId(userBean.getId());
			}

			if (idGroupe != 0) {
				Groupe groupePersistant = new Groupe();
				groupePersistant = (Groupe) session
						.load(Groupe.class, idGroupe);

				userPersistant.setGroupe(groupePersistant);
			} else {
				userPersistant.setGroupe(null);
			}

			userPersistant.setLogin(userBean.getLogin());
			if (userBean.getPassword() != null
					&& !userBean.getPassword().equals("")) {
				userPersistant.setPassword(userBean.getPassword());
			}
			userPersistant.setProfile(userBean.getProfile());
			userPersistant.setNom(userBean.getNom());
			userPersistant.setPrenom(userBean.getPrenom());
			userPersistant.setTelephone(userBean.getTelephone());
			userPersistant.setDateDemandeVersionEssai(userBean
					.getDateDemandeVersionEssai());
			userPersistant.setNomEntreprise(userBean.getNomEntreprise());
			userPersistant.setEvenement(userBean.isEvenement());
			userPersistant.setRemuneration(userBean.isRemuneration());
			userPersistant.setFicheDePoste(userBean.isFicheDePoste());
			userPersistant.setEntretien(userBean.isEntretien());
			userPersistant.setContratTravail(userBean.isContratTravail());
			userPersistant.setAdmin(userBean.isAdmin());
			userPersistant.setSuperAdmin(userBean.isSuperAdmin());

			session.saveOrUpdate(userPersistant);
			// transaction.commit();

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void saveOrUppdate(UserBean userBean, int idGroupe) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			User userPersistant = new User();
			transaction = session.beginTransaction();
			if (userBean.getId() != 0) {
				userPersistant = (User) session.load(User.class,
						userBean.getId());

				userPersistant.setId(userBean.getId());
			}

			if (idGroupe != 0) {
				Groupe groupePersistant = new Groupe();
				groupePersistant = (Groupe) session
						.load(Groupe.class, idGroupe);

				userPersistant.setGroupe(groupePersistant);
			} else {
				userPersistant.setGroupe(null);
			}

			userPersistant.setLogin(userBean.getLogin());
			if (userBean.getPassword() != null
					&& !userBean.getPassword().equals("")) {
				userPersistant.setPassword(userBean.getPassword());
			}
			userPersistant.setProfile(userBean.getProfile());
			userPersistant.setNom(userBean.getNom());
			userPersistant.setPrenom(userBean.getPrenom());
			userPersistant.setTelephone(userBean.getTelephone());
			userPersistant.setDateDemandeVersionEssai(userBean
					.getDateDemandeVersionEssai());
			userPersistant.setNomEntreprise(userBean.getNomEntreprise());
			userPersistant.setEvenement(userBean.isEvenement());
			userPersistant.setRemuneration(userBean.isRemuneration());
			userPersistant.setFicheDePoste(userBean.isFicheDePoste());
			userPersistant.setEntretien(userBean.isEntretien());
			userPersistant.setContratTravail(userBean.isContratTravail());
			userPersistant.setAdmin(userBean.isAdmin());
			userPersistant.setSuperAdmin(userBean.isSuperAdmin());

			session.saveOrUpdate(userPersistant);
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

	public void save(UserBean userBean) throws Exception {
		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			// transaction = session.beginTransaction();
			User user = new User();
			user.setId(userBean.getId());
			user.setGroupe((Groupe) session.load(Groupe.class,
					userBean.getIdGroupe()));
			user.setLogin(userBean.getLogin());
			user.setPassword(userBean.getPassword());
			user.setProfile(userBean.getProfile());
			user.setNom(userBean.getNom());
			user.setPrenom(userBean.getPrenom());
			user.setTelephone(userBean.getTelephone());
			user.setDateDemandeVersionEssai(userBean
					.getDateDemandeVersionEssai());
			user.setNomEntreprise(userBean.getNomEntreprise());
			user.setEvenement(userBean.isEvenement());
			user.setRemuneration(userBean.isRemuneration());
			user.setFicheDePoste(userBean.isFicheDePoste());
			user.setEntretien(userBean.isEntretien());
			user.setContratTravail(userBean.isContratTravail());
			user.setAdmin(userBean.isAdmin());
			user.setSuperAdmin(userBean.isSuperAdmin());

			session.save(user);
			// transaction.commit();
			userBean.setId(user.getId());

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	private List<UserBean> UserPersistantListToUserBeanList(
			List<User> UserPersistantList) throws Exception {

		List<UserBean> UserBeanList = new ArrayList<UserBean>();

		for (User UserPersistant : UserPersistantList) {

			UserBeanList.add(UserPersistantToUserBean(UserPersistant));
		}

		return UserBeanList;

	}

	public UserBean UserPersistantToUserBean(User userPersistant)
			throws Exception {

		UserBean userBean = new UserBean();
		userBean.setId(userPersistant.getId());
		if (userPersistant.getGroupe() != null) {
			userBean.setIdGroupe(userPersistant.getGroupe().getId());
		}
		userBean.setLogin(userPersistant.getLogin());
		userBean.setPassword(userPersistant.getPassword());
		userBean.setProfile(userPersistant.getProfile());
		userBean.setNom(userPersistant.getNom());
		userBean.setPrenom(userPersistant.getPrenom());
		userBean.setTelephone(userPersistant.getTelephone());
		userBean.setDateDemandeVersionEssai(userPersistant
				.getDateDemandeVersionEssai());
		userBean.setNomEntreprise(userPersistant.getNomEntreprise());
		userBean.setEvenement(userPersistant.isEvenement());
		userBean.setRemuneration(userPersistant.isRemuneration());
		userBean.setFicheDePoste(userPersistant.isFicheDePoste());
		userBean.setEntretien(userPersistant.isEntretien());
		userBean.setContratTravail(userPersistant.isContratTravail());
		userBean.setAdmin(userPersistant.isAdmin());
		userBean.setSuperAdmin(userPersistant.isSuperAdmin());

		return userBean;
	}

}
