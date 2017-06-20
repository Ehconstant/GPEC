package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.FicheMetierEntrepriseBean;
import com.cci.gpec.db.Entreprise;
import com.cci.gpec.db.FicheMetier;
import com.cci.gpec.db.FicheMetierEntreprise;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class FicheMetierEntrepriseServiceImpl {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FicheDePosteServiceImpl.class);

	public void suppression(FicheMetierEntrepriseBean ficheMetierEntrepriseBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheMetierEntreprise ficheMetierEntreprisePersistant = new FicheMetierEntreprise();
			transaction = session.beginTransaction();
			if (ficheMetierEntrepriseBean.getIdEntreprise() != 0
					&& ficheMetierEntrepriseBean.getIdFicheMetier() != 0) {
				Entreprise EntreprisePersistant = (Entreprise) session.load(
						Entreprise.class,
						ficheMetierEntrepriseBean.getIdEntreprise());
				ficheMetierEntreprisePersistant
						.setEntreprise(EntreprisePersistant);
				FicheMetier ficheMetierPersistant = (FicheMetier) session.load(
						FicheMetier.class,
						ficheMetierEntrepriseBean.getIdFicheMetier());
				ficheMetierEntreprisePersistant
						.setFicheMetier(ficheMetierPersistant);
			}

			session.delete(ficheMetierEntreprisePersistant);
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

	public void deleteFicheMetierEntrepriseWithoutTransaction(
			FicheMetierEntrepriseBean ficheMetierEntrepriseBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheMetierEntreprise ficheMetierEntreprisePersistant = new FicheMetierEntreprise();
			if (ficheMetierEntrepriseBean.getIdEntreprise() != 0
					&& ficheMetierEntrepriseBean.getIdFicheMetier() != 0) {
				Entreprise EntreprisePersistant = (Entreprise) session.load(
						Entreprise.class,
						ficheMetierEntrepriseBean.getIdEntreprise());
				ficheMetierEntreprisePersistant
						.setEntreprise(EntreprisePersistant);
				FicheMetier ficheMetierPersistant = (FicheMetier) session.load(
						FicheMetier.class,
						ficheMetierEntrepriseBean.getIdFicheMetier());
				ficheMetierEntreprisePersistant
						.setFicheMetier(ficheMetierPersistant);
			}

			session.delete(ficheMetierEntreprisePersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void saveOrUppdate(
			FicheMetierEntrepriseBean ficheMetierEntrepriseBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheMetierEntreprise ficheMetierEntreprisePersistant = new FicheMetierEntreprise();
			transaction = session.beginTransaction();
			Entreprise EntreprisePersistant = (Entreprise) session.load(
					Entreprise.class,
					ficheMetierEntrepriseBean.getIdEntreprise());
			ficheMetierEntreprisePersistant.setEntreprise(EntreprisePersistant);
			FicheMetier ficheMetierPersistant = (FicheMetier) session.load(
					FicheMetier.class,
					ficheMetierEntrepriseBean.getIdFicheMetier());
			ficheMetierEntreprisePersistant
					.setFicheMetier(ficheMetierPersistant);

			session.saveOrUpdate(ficheMetierEntreprisePersistant);
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

	public void save(FicheMetierEntrepriseBean ficheMetierEntrepriseBean) {
		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheMetierEntreprise ficheMetierEntreprisePersistant = new FicheMetierEntreprise();
			// transaction = session.beginTransaction();
			Entreprise EntreprisePersistant = (Entreprise) session.load(
					Entreprise.class,
					ficheMetierEntrepriseBean.getIdEntreprise());
			ficheMetierEntreprisePersistant.setEntreprise(EntreprisePersistant);
			FicheMetier ficheMetierPersistant = (FicheMetier) session.load(
					FicheMetier.class,
					ficheMetierEntrepriseBean.getIdFicheMetier());
			ficheMetierEntreprisePersistant
					.setFicheMetier(ficheMetierPersistant);

			session.save(ficheMetierEntreprisePersistant);
			// transaction.commit();
		} catch (HibernateException e) {
			// if (transaction != null && !transaction.wasRolledBack()) {
			// transaction.rollback();
			// }
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public List<FicheMetierEntrepriseBean> getFicheMetierEntrepriseBeanListByIdEntreprise(
			Integer idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<FicheMetierEntreprise> ficheMetierEntrepriseInventory = session
					.createQuery(
							"from FicheMetierEntreprise where Entreprise="
									+ idEntreprise).list();

			List<FicheMetierEntrepriseBean> ficheMetierEntrepriseBeanList = new ArrayList<FicheMetierEntrepriseBean>();
			ficheMetierEntrepriseBeanList = ficheMetierEntreprisePersistantListToSalarieBeanList(ficheMetierEntrepriseInventory);
			transaction.commit();

			return ficheMetierEntrepriseBeanList;

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

	public List<FicheMetierEntrepriseBean> getFicheMetierEntrepriseBeanListByIdFicheMetier(
			Integer idFicheMetier) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<FicheMetierEntreprise> ficheMetierEntrepriseInventory = session
					.createQuery(
							"from FicheMetierEntreprise where FicheMetier="
									+ idFicheMetier).list();

			List<FicheMetierEntrepriseBean> ficheMetierEntrepriseBeanList = new ArrayList<FicheMetierEntrepriseBean>();
			ficheMetierEntrepriseBeanList = ficheMetierEntreprisePersistantListToSalarieBeanList(ficheMetierEntrepriseInventory);
			transaction.commit();

			return ficheMetierEntrepriseBeanList;

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

	public List<FicheMetierEntrepriseBean> getFicheMetierEntrepriseList(
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<FicheMetierEntreprise> ficheMetierEntrepriseInventory = session
					.createQuery(
							"from FicheMetierEntreprise as f left join fetch f.Entreprise as e where e.Groupe="
									+ idGroupe).list();

			List<FicheMetierEntrepriseBean> ficheMetierEntrepriseBeanList = new ArrayList<FicheMetierEntrepriseBean>();
			ficheMetierEntrepriseBeanList = ficheMetierEntreprisePersistantListToSalarieBeanList(ficheMetierEntrepriseInventory);
			transaction.commit();

			return ficheMetierEntrepriseBeanList;

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

	private List<FicheMetierEntrepriseBean> ficheMetierEntreprisePersistantListToSalarieBeanList(
			List<FicheMetierEntreprise> ficheMetierEntreprisePersistantList)
			throws Exception {

		List<FicheMetierEntrepriseBean> ficheMetierEntrepriseBeanList = new ArrayList<FicheMetierEntrepriseBean>();

		for (FicheMetierEntreprise salariePersistant : ficheMetierEntreprisePersistantList) {

			ficheMetierEntrepriseBeanList
					.add(FicheMetierEntreprisePersistantToFicheMetierEntrepriseBean(salariePersistant));
		}

		return ficheMetierEntrepriseBeanList;

	}

	public FicheMetierEntrepriseBean FicheMetierEntreprisePersistantToFicheMetierEntrepriseBean(
			FicheMetierEntreprise FicheMetierEntreprisePersistant)
			throws Exception {

		FicheMetierEntrepriseBean FicheMetierEntrepriseBean = new FicheMetierEntrepriseBean();
		FicheMetierEntrepriseBean
				.setIdEntreprise(FicheMetierEntreprisePersistant
						.getEntreprise().getId());
		FicheMetierEntrepriseBean
				.setIdFicheMetier(FicheMetierEntreprisePersistant
						.getFicheMetier().getId());

		return FicheMetierEntrepriseBean;
	}
}
