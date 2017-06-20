package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.RevenuComplementaireBean;
import com.cci.gpec.db.Groupe;
import com.cci.gpec.db.RevenuComplementaire;
import com.cci.gpec.db.connection.HibernateUtil;

public class RevenuComplementaireServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RevenuComplementaireServiceImpl.class);

	public String getType(int idRev, int idGroupe) {
		String type = "";
		List<RevenuComplementaireBean> list = getRevenuComplementaireList(idGroupe);
		list.addAll(getFraisProfList(idGroupe));
		for (RevenuComplementaireBean b : list) {
			if (b.getId() == idRev) {
				type = b.getType();
			}
		}
		return type;
	}

	public int getIdFromRevenuComplementaire(String libelle, String type,
			int idGroupe) {
		int id = 0;
		List<RevenuComplementaireBean> list = getRevenuComplementaireList(idGroupe);
		for (RevenuComplementaireBean b : list) {
			if (b.getLibelle().equals(libelle) && b.getType().equals(type)) {
				id = b.getId();
			}
		}
		return id;
	}

	public int getIdFromRevenuComplementaireFp(String libelle, String type,
			int idGroupe) {
		int id = 0;
		List<RevenuComplementaireBean> list = getRevenuComplementaireListFp(idGroupe);
		for (RevenuComplementaireBean b : list) {
			if (b.getLibelle().equals(libelle) && b.getType().equals(type)) {
				id = b.getId();
			}
		}
		return id;
	}

	public int getIdFromFraisProf(String libelle, String type, int idGroupe) {
		int id = 0;
		List<RevenuComplementaireBean> list = getFraisProfList(idGroupe);
		for (RevenuComplementaireBean b : list) {
			if (b.getLibelle().equals(libelle) && b.getType().equals(type)) {
				id = b.getId();
			}
		}
		return id;
	}

	/**
	 * Retourne la liste des revenus complémentaires.
	 */
	public List<RevenuComplementaireBean> getRevenuComplementaireList(
			int idGroupe) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<RevenuComplementaire> revenuComplementaireInventory = session
					.createQuery(
							"from RevenuComplementaire as r where r.Groupe="
									+ idGroupe
									+ " and r.Type <> 'frais_professionnel' order by Type,Libelle")
					.list();

			List<RevenuComplementaireBean> revenuComplementaireInventoryBean = new ArrayList<RevenuComplementaireBean>();
			revenuComplementaireInventoryBean = revenuComplementairePersistantListToRevenuComplementaireBeanList(revenuComplementaireInventory);
			transaction.commit();

			return revenuComplementaireInventoryBean;

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

	public List<RevenuComplementaireBean> getRevenuComplementaireListFp(
			int idGroupe) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<RevenuComplementaire> revenuComplementaireInventory = session
					.createQuery(
							"from RevenuComplementaire r where r.Groupe="
									+ idGroupe
									+ " and r.Type = 'frais_professionnel' order by Libelle")
					.list();

			List<RevenuComplementaireBean> revenuComplementaireInventoryBean = new ArrayList<RevenuComplementaireBean>();
			revenuComplementaireInventoryBean = revenuComplementairePersistantListToRevenuComplementaireBeanList(revenuComplementaireInventory);
			transaction.commit();

			return revenuComplementaireInventoryBean;

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

	public List<RevenuComplementaireBean> getRevenuComplementaireByType(
			String type, int idGroupe) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			List<RevenuComplementaire> commissionInventory = new ArrayList<RevenuComplementaire>();
			if (type.equals("tous")) {
				commissionInventory = session
						.createQuery(
								"from RevenuComplementaire r where r.Groupe="
										+ idGroupe
										+ " and r.Type <> 'frais_professionnel' order by Type,Libelle")
						.list();
			} else {
				commissionInventory = session.createQuery(
						"from RevenuComplementaire r where r.Groupe="
								+ idGroupe + " and r.Type='" + type
								+ "' order by Type,Libelle").list();
			}

			List<RevenuComplementaireBean> commissionInventoryBean = new ArrayList<RevenuComplementaireBean>();
			commissionInventoryBean = revenuComplementairePersistantListToRevenuComplementaireBeanList(commissionInventory);
			transaction.commit();

			return commissionInventoryBean;

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

	public List<RevenuComplementaireBean> getCommissionList(int idGroupe) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<RevenuComplementaire> commissionInventory = session
					.createQuery(
							"from RevenuComplementaire r where r.Groupe="
									+ idGroupe
									+ " and r.Type='commission' order by Libelle")
					.list();

			List<RevenuComplementaireBean> commissionInventoryBean = new ArrayList<RevenuComplementaireBean>();
			commissionInventoryBean = revenuComplementairePersistantListToRevenuComplementaireBeanList(commissionInventory);
			transaction.commit();

			return commissionInventoryBean;

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

	public List<RevenuComplementaireBean> getFraisProfList(int idGroupe) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<RevenuComplementaire> fraisProfInventory = session
					.createQuery(
							"from RevenuComplementaire r where r.Groupe="
									+ idGroupe
									+ " and r.Type='frais_professionnel' order by Libelle")
					.list();

			List<RevenuComplementaireBean> fraisProfInventoryBean = new ArrayList<RevenuComplementaireBean>();
			fraisProfInventoryBean = revenuComplementairePersistantListToRevenuComplementaireBeanList(fraisProfInventory);
			transaction.commit();

			return fraisProfInventoryBean;

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

	public List<RevenuComplementaireBean> getPrimeFixeList(int idGroupe) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<RevenuComplementaire> primeFixeInventory = session
					.createQuery(
							"from RevenuComplementaire r where r.Groupe="
									+ idGroupe
									+ " and r.Type='prime_fixe' order by Libelle")
					.list();

			List<RevenuComplementaireBean> primeFixeInventoryBean = new ArrayList<RevenuComplementaireBean>();
			primeFixeInventoryBean = revenuComplementairePersistantListToRevenuComplementaireBeanList(primeFixeInventory);
			transaction.commit();

			return primeFixeInventoryBean;

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

	public List<RevenuComplementaireBean> getPrimeVariableList(int idGroupe) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<RevenuComplementaire> primeVariableInventory = session
					.createQuery(
							"from RevenuComplementaire r where r.Groupe="
									+ idGroupe
									+ " and r.Type='prime_variable' order by Libelle")
					.list();

			List<RevenuComplementaireBean> primeVariableInventoryBean = new ArrayList<RevenuComplementaireBean>();
			primeVariableInventoryBean = revenuComplementairePersistantListToRevenuComplementaireBeanList(primeVariableInventory);
			transaction.commit();

			return primeVariableInventoryBean;

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

	public List<RevenuComplementaireBean> getAvantageAssujettiList(int idGroupe) {

		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<RevenuComplementaire> avantageAssujettiInventory = session
					.createQuery(
							"from RevenuComplementaire r where r.Groupe="
									+ idGroupe
									+ " and r.Type='avantage_assujetti' order by Libelle")
					.list();

			List<RevenuComplementaireBean> avantageAssujettiInventoryBean = new ArrayList<RevenuComplementaireBean>();
			avantageAssujettiInventoryBean = revenuComplementairePersistantListToRevenuComplementaireBeanList(avantageAssujettiInventory);
			transaction.commit();

			return avantageAssujettiInventoryBean;

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

	public List<RevenuComplementaireBean> getAvantageNonAssujettiList(
			int idGroupe) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<RevenuComplementaire> avantageNonAssujettiInventory = session
					.createQuery(
							"from RevenuComplementaire r where r.Groupe="
									+ idGroupe
									+ " and r.Type='avantage_non_assujetti' order by Libelle")
					.list();

			List<RevenuComplementaireBean> avantageNonAssujettiInventoryBean = new ArrayList<RevenuComplementaireBean>();
			avantageNonAssujettiInventoryBean = revenuComplementairePersistantListToRevenuComplementaireBeanList(avantageNonAssujettiInventory);
			transaction.commit();

			return avantageNonAssujettiInventoryBean;

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

	public RevenuComplementaireBean getRevenuComplementaireBeanById(
			Integer idRevenuComplementaire) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			RevenuComplementaire revenuComplementairePersistant = new RevenuComplementaire();
			transaction = session.beginTransaction();
			revenuComplementairePersistant = (RevenuComplementaire) session
					.load(RevenuComplementaire.class, idRevenuComplementaire);
			RevenuComplementaireBean revenuComplementaireBean = revenuComplementairePersistantToRevenuComplementaireBean(revenuComplementairePersistant);
			transaction.commit();
			return revenuComplementaireBean;

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

	public List<RevenuComplementaireBean> revenuComplementairePersistantListToRevenuComplementaireBeanList(
			List<RevenuComplementaire> RevenuComplementairePersistantList) {

		List<RevenuComplementaireBean> RevenuComplementaireBeanList = new ArrayList<RevenuComplementaireBean>();

		for (RevenuComplementaire RevenuComplementairePersistant : RevenuComplementairePersistantList) {

			RevenuComplementaireBeanList
					.add(revenuComplementairePersistantToRevenuComplementaireBean(RevenuComplementairePersistant));
		}

		return RevenuComplementaireBeanList;

	}

	public RevenuComplementaireBean revenuComplementairePersistantToRevenuComplementaireBean(
			RevenuComplementaire revenuComplementairePersistant) {

		RevenuComplementaireBean revenuComplementaireBean = new RevenuComplementaireBean();
		revenuComplementaireBean.setLibelle(revenuComplementairePersistant
				.getLibelle());
		revenuComplementaireBean.setId(revenuComplementairePersistant.getId());
		revenuComplementaireBean.setType(revenuComplementairePersistant
				.getType());

		return revenuComplementaireBean;
	}

	public void saveOrUpdate(RevenuComplementaireBean revCompBean, int idGroupe) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			RevenuComplementaire revComplPersistant = new RevenuComplementaire();
			transaction = session.beginTransaction();
			if (revCompBean.getId() != 0) {
				revComplPersistant = (RevenuComplementaire) session.load(
						RevenuComplementaire.class, revCompBean.getId());

				revComplPersistant.setId(revCompBean.getId());

			}

			Groupe groupePersistant = new Groupe();
			groupePersistant = (Groupe) session.load(Groupe.class, idGroupe);

			revComplPersistant.setGroupe(groupePersistant);

			revComplPersistant.setLibelle(revCompBean.getLibelle());
			revComplPersistant.setType(revCompBean.getType());
			session.saveOrUpdate(revComplPersistant);

			revCompBean.setId(revComplPersistant.getId());

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

	public void save(RevenuComplementaireBean revCompBean, int idGroupe) {
		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			RevenuComplementaire revComplPersistant = new RevenuComplementaire();
			// transaction = session.beginTransaction();

			revComplPersistant.setId(revCompBean.getId());

			Groupe groupePersistant = new Groupe();
			groupePersistant = (Groupe) session.load(Groupe.class, idGroupe);

			revComplPersistant.setGroupe(groupePersistant);

			revComplPersistant.setLibelle(revCompBean.getLibelle());
			revComplPersistant.setType(revCompBean.getType());
			session.save(revComplPersistant);
			// transaction.commit();
			revCompBean.setId(revComplPersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public boolean supprimer(RevenuComplementaireBean revenuComplementaireBean)
			throws Exception {
		suppression(revenuComplementaireBean);
		return true;
	}

	public void suppression(RevenuComplementaireBean revenuComplementaireBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			RevenuComplementaire revenuComplementairePersistant = new RevenuComplementaire();
			transaction = session.beginTransaction();
			if (revenuComplementaireBean.getId() != 0) {
				revenuComplementairePersistant = (RevenuComplementaire) session
						.load(RevenuComplementaire.class,
								revenuComplementaireBean.getId());

				revenuComplementairePersistant.setId(revenuComplementaireBean
						.getId());
			}
			session.delete(revenuComplementairePersistant);
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

	public void deleteRevenuComplementaireWithoutTransaction(
			RevenuComplementaireBean revenuComplementaireBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			RevenuComplementaire revenuComplementairePersistant = new RevenuComplementaire();
			if (revenuComplementaireBean.getId() != 0) {
				revenuComplementairePersistant = (RevenuComplementaire) session
						.load(RevenuComplementaire.class,
								revenuComplementaireBean.getId());

				revenuComplementairePersistant.setId(revenuComplementaireBean
						.getId());
			}
			session.delete(revenuComplementairePersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

}
