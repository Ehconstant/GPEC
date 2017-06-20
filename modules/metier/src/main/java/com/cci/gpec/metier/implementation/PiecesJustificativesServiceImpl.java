package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.PiecesJustificativesBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.db.PiecesJustificatives;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.connection.HibernateUtil;

public class PiecesJustificativesServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PiecesJustificativesServiceImpl.class);

	/**
	 * Retourne la liste des Personnes a charge.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<PiecesJustificativesBean> getPiecesJustificativesBeanList(
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<PiecesJustificativesBean> piecesJustificativessInventoryBean;
			transaction = session.beginTransaction();

			List<PiecesJustificatives> piecesJustificativessInventory = session
					.createQuery(
							"from PiecesJustificatives as p left join fetch p.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe).list();

			piecesJustificativessInventoryBean = piecesJustificativesPersistantListToPiecesJustificativesBeanList(piecesJustificativessInventory);
			transaction.commit();

			return piecesJustificativessInventoryBean;

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

	public List<PiecesJustificativesBean> getPiecesJustificativesBeanListByIdSalarie(
			int salarie) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<PiecesJustificativesBean> piecesJustificativessInventoryBean;
			transaction = session.beginTransaction();

			List<PiecesJustificatives> piecesJustificativessInventory = session
					.createQuery(
							"from PiecesJustificatives where Salarie="
									+ salarie).list();

			piecesJustificativessInventoryBean = piecesJustificativesPersistantListToPiecesJustificativesBeanList(piecesJustificativessInventory);
			transaction.commit();

			return piecesJustificativessInventoryBean;

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

	public void saveOrUppdate(PiecesJustificativesBean piecesJustificativesBean)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			PiecesJustificatives piecesJustificativesPersistant = new PiecesJustificatives();
			transaction = session.beginTransaction();
			session.saveOrUpdate(piecesJustificativesBeanToPiecesJustificativesPersistant(
					piecesJustificativesBean,
					piecesJustificativesBean.getIdSalarie()));
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

	public void save(PiecesJustificativesBean piecesJustificativesBean)
			throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.save(piecesJustificativesBeanToPiecesJustificativesPersistant(
					piecesJustificativesBean,
					piecesJustificativesBean.getIdSalarie()));

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public PiecesJustificativesBean getPiecesJustificativesBeanById(
			Integer idPiecesJustificatives) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			PiecesJustificatives piecesJustificativesPersistant = new PiecesJustificatives();
			transaction = session.beginTransaction();

			if (idPiecesJustificatives == -1) {
				return null;
			}

			piecesJustificativesPersistant = (PiecesJustificatives) session
					.load(PiecesJustificatives.class, idPiecesJustificatives);

			PiecesJustificativesBean piecesJustificativesBean = piecesJustificativesPersistantToPiecesJustificativesBean(piecesJustificativesPersistant);

			transaction.commit();

			return piecesJustificativesBean;

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

	public List<PiecesJustificativesBean> piecesJustificativesPersistantListToPiecesJustificativesBeanList(
			List<PiecesJustificatives> piecesJustificativesPersistantList)
			throws Exception {

		List<PiecesJustificativesBean> piecesJustificativesBeanList = new ArrayList<PiecesJustificativesBean>();

		for (PiecesJustificatives piecesJustificativesPersistant : piecesJustificativesPersistantList) {

			piecesJustificativesBeanList
					.add(piecesJustificativesPersistantToPiecesJustificativesBean(piecesJustificativesPersistant));
		}

		return piecesJustificativesBeanList;

	}

	public PiecesJustificativesBean piecesJustificativesPersistantToPiecesJustificativesBean(
			PiecesJustificatives piecesJustificativesPersistant)
			throws Exception {

		PiecesJustificativesBean piecesJustificativesBean = new PiecesJustificativesBean();

		piecesJustificativesBean.setId(piecesJustificativesPersistant.getId());
		piecesJustificativesBean.setIdSalarie(piecesJustificativesPersistant
				.getSalarie().getId());

		piecesJustificativesBean
				.setPermisConduire(piecesJustificativesPersistant
						.isPermisConduire());
		piecesJustificativesBean.setRib(piecesJustificativesPersistant.isRib());
		piecesJustificativesBean
				.setAttestaionVisiteMedicale(piecesJustificativesPersistant
						.isAttestaionVisiteMedicale());
		piecesJustificativesBean
				.setAttestationSecu(piecesJustificativesPersistant
						.isAttestationSecu());
		piecesJustificativesBean
				.setCarteIdentite(piecesJustificativesPersistant
						.isCarteIdentite());
		piecesJustificativesBean
				.setCertificatTravail(piecesJustificativesPersistant
						.isCertificatTravail());
		piecesJustificativesBean.setDiplomes(piecesJustificativesPersistant
				.isDiplomes());
		piecesJustificativesBean
				.setHabilitations(piecesJustificativesPersistant
						.isHabilitations());

		return piecesJustificativesBean;
	}

	public List<PiecesJustificatives> piecesJustificativesBeanListToPiecesJustificativesPersistantList(
			SalarieBean salarieBean) throws Exception {

		List<PiecesJustificatives> piecesJustificativesList = new ArrayList<PiecesJustificatives>();

		for (PiecesJustificativesBean piecesJustificativesBean : getPiecesJustificativesBeanListByIdSalarie(salarieBean
				.getId())) {

			piecesJustificativesList
					.add(piecesJustificativesBeanToPiecesJustificativesPersistant(
							piecesJustificativesBean, salarieBean.getId()));
		}

		return piecesJustificativesList;

	}

	public PiecesJustificatives piecesJustificativesBeanToPiecesJustificativesPersistant(
			PiecesJustificativesBean piecesJustificativesBean, int idSalarie)
			throws Exception {

		Session session = null;
		PiecesJustificatives piecesJustificativesPersistant = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			piecesJustificativesPersistant = new PiecesJustificatives();
	
			if (piecesJustificativesBean.getId() == -1
					|| piecesJustificativesBean.getId() == 0) {
				piecesJustificativesPersistant.setId(null);
			} else {
				piecesJustificativesPersistant.setId(piecesJustificativesBean
						.getId());
			}
			Salarie salarie = (Salarie) session.load(Salarie.class, idSalarie);
			piecesJustificativesPersistant.setSalarie(salarie);
			piecesJustificativesPersistant
					.setPermisConduire(piecesJustificativesBean.isPermisConduire());
			piecesJustificativesPersistant.setRib(piecesJustificativesBean.isRib());
			piecesJustificativesPersistant
					.setAttestaionVisiteMedicale(piecesJustificativesBean
							.isAttestaionVisiteMedicale());
			piecesJustificativesPersistant
					.setAttestationSecu(piecesJustificativesBean
							.isAttestationSecu());
			piecesJustificativesPersistant
					.setCarteIdentite(piecesJustificativesBean.isCarteIdentite());
			piecesJustificativesPersistant
					.setCertificatTravail(piecesJustificativesBean
							.isCertificatTravail());
			piecesJustificativesPersistant.setDiplomes(piecesJustificativesBean
					.isDiplomes());
			piecesJustificativesPersistant
					.setHabilitations(piecesJustificativesBean.isHabilitations());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}

		return piecesJustificativesPersistant;
	}

	public void delete(PiecesJustificativesBean pjBean) throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			PiecesJustificatives p = (PiecesJustificatives) session.load(
					PiecesJustificatives.class, pjBean.getId());

			session.delete(p);

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

	public void deletePieceJustificativeWithoutTransaction(
			PiecesJustificativesBean pjBean) throws Exception {

		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			PiecesJustificatives p = (PiecesJustificatives) session.load(
					PiecesJustificatives.class, pjBean.getId());

			session.delete(p);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}
}
