package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.LienRemunerationRevenuBean;
import com.cci.gpec.db.LienRemunerationRevenuComplementaire;
import com.cci.gpec.db.Remuneration;
import com.cci.gpec.db.RevenuComplementaire;
import com.cci.gpec.db.connection.HibernateUtil;

public class LienRemunerationRevenuServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LienRemunerationRevenuServiceImpl.class);

	public LienRemunerationRevenuBean getLienRemunerationRevenuFromId(int idLien) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			LienRemunerationRevenuComplementaire lienRemunerationRevenuPersistant = new LienRemunerationRevenuComplementaire();
			transaction = session.beginTransaction();

			lienRemunerationRevenuPersistant = (LienRemunerationRevenuComplementaire) session
					.load(LienRemunerationRevenuBean.class, idLien);

			LienRemunerationRevenuBean lienRemunerationRevenuBean = lienRemunerationRevenuPersistantToLienRemunerationRevenuBean(lienRemunerationRevenuPersistant);

			transaction.commit();

			return lienRemunerationRevenuBean;

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

	public List<LienRemunerationRevenuBean> getLienRemunerationRevenuFromIdRemu(
			int idRemu) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<LienRemunerationRevenuComplementaire> liens = session
					.createQuery(
							"from LienRemunerationRevenuComplementaire l where l.Remuneration = "
									+ idRemu).list();

			List<LienRemunerationRevenuBean> liensBean = lienRemunerationRevenuPersistantListToLienRemunerationRevenuBeanList(liens);

			transaction.commit();

			return liensBean;

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

	public List<LienRemunerationRevenuBean> getLienRemunerationRevenuFromIdRevComp(
			int idRevComp) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<LienRemunerationRevenuComplementaire> liens = session
					.createQuery(
							"from LienRemunerationRevenuComplementaire l where l.RevenuComplementaire = "
									+ idRevComp).list();

			List<LienRemunerationRevenuBean> liensBean = lienRemunerationRevenuPersistantListToLienRemunerationRevenuBeanList(liens);

			transaction.commit();

			return liensBean;

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

	public String getMontantNPrec(int idRemu, int idPrime) {

		String montant = "0";
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<LienRemunerationRevenuComplementaire> lien = session
					.createQuery(
							"from LienRemunerationRevenuComplementaire l where l.Remuneration = "
									+ idRemu + " AND l.RevenuComplementaire = "
									+ idPrime).list();

			if (!lien.isEmpty()) {
				montant = String.valueOf(lien.get(0).getMontant());
			}
			transaction.commit();

			return montant;

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

	/**
	 * Retourne la liste des liens
	 */

	public List<LienRemunerationRevenuBean> getLienRemunerationRevenuList(
			int idGroupe) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<LienRemunerationRevenuComplementaire> lienRemunerationRevenuInventory = session
					.createQuery(
							"from LienRemunerationRevenuComplementaire as l left join fetch l.Remuneration as r left join fetch r.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe).list();

			transaction.commit();

			List<LienRemunerationRevenuBean> lienRemunerationRevenuInventoryBean = new ArrayList<LienRemunerationRevenuBean>();
			lienRemunerationRevenuInventoryBean = lienRemunerationRevenuPersistantListToLienRemunerationRevenuBeanList(lienRemunerationRevenuInventory);

			return lienRemunerationRevenuInventoryBean;

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

	public LienRemunerationRevenuBean getLienRemunerationRevenuBeanById(
			Integer idLienRemunerationRevenu) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			LienRemunerationRevenuComplementaire lienRemunerationRevenuPersistant = new LienRemunerationRevenuComplementaire();
			transaction = session.beginTransaction();
			lienRemunerationRevenuPersistant = (LienRemunerationRevenuComplementaire) session
					.load(LienRemunerationRevenuComplementaire.class,
							idLienRemunerationRevenu);
			LienRemunerationRevenuBean lienRemunerationRevenuBean = lienRemunerationRevenuPersistantToLienRemunerationRevenuBean(lienRemunerationRevenuPersistant);
			transaction.commit();

			return lienRemunerationRevenuBean;

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

	public List<LienRemunerationRevenuBean> lienRemunerationRevenuPersistantListToLienRemunerationRevenuBeanList(
			List<LienRemunerationRevenuComplementaire> LienRemunerationRevenuPersistantList) {

		List<LienRemunerationRevenuBean> lienRemunerationRevenuBeanList = new ArrayList<LienRemunerationRevenuBean>();

		for (LienRemunerationRevenuComplementaire lienRemunerationRevenuPersistant : LienRemunerationRevenuPersistantList) {

			lienRemunerationRevenuBeanList
					.add(lienRemunerationRevenuPersistantToLienRemunerationRevenuBean(lienRemunerationRevenuPersistant));
		}
		return lienRemunerationRevenuBeanList;

	}

	public LienRemunerationRevenuBean lienRemunerationRevenuPersistantToLienRemunerationRevenuBean(
			LienRemunerationRevenuComplementaire lienRemunerationRevenuPersistant) {

		LienRemunerationRevenuBean lienRemunerationRevenuBean = new LienRemunerationRevenuBean();

		if (lienRemunerationRevenuPersistant.getActualisation() != new Double(0)) {
			lienRemunerationRevenuBean.setActualisation(String
					.valueOf(lienRemunerationRevenuPersistant
							.getActualisation()));
		} else {
			lienRemunerationRevenuBean.setActualisation("0");
		}

		if (lienRemunerationRevenuPersistant.getRemonteNPrec() != null) {
			lienRemunerationRevenuBean
					.setRemonteNPrec(lienRemunerationRevenuPersistant
							.getRemonteNPrec());
		} else {
			lienRemunerationRevenuBean.setRemonteNPrec(false);
		}

		lienRemunerationRevenuBean
				.setCommentaire(lienRemunerationRevenuPersistant
						.getCommentaire());
		lienRemunerationRevenuBean
				.setIdRemuneration(lienRemunerationRevenuPersistant
						.getRemuneration().getIdRemuneration());
		lienRemunerationRevenuBean
				.setIdRevenuComplementaire(lienRemunerationRevenuPersistant
						.getRevenuComplementaire().getId());
		if (lienRemunerationRevenuPersistant.getMontant() != 0) {
			lienRemunerationRevenuBean.setMontant(String
					.valueOf(lienRemunerationRevenuPersistant.getMontant()));
		} else {
			lienRemunerationRevenuBean.setMontant("0");
		}
		lienRemunerationRevenuBean
				.setIdLienRemunerationRevenuComplementaire(lienRemunerationRevenuPersistant
						.getIdLienRemunerationRevenuComplementaire());

		return lienRemunerationRevenuBean;
	}

	public void saveOrUpdate(LienRemunerationRevenuBean lienBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			LienRemunerationRevenuComplementaire lienRemunerationRevenuPersistant = new LienRemunerationRevenuComplementaire();
			transaction = session.beginTransaction();
			if (lienBean.getIdLienRemunerationRevenuComplementaire() != 0) {
				lienRemunerationRevenuPersistant = (LienRemunerationRevenuComplementaire) session
						.load(LienRemunerationRevenuComplementaire.class,
								lienBean.getIdLienRemunerationRevenuComplementaire());
			}

			Remuneration remu = new Remuneration();
			remu.setIdRemuneration(lienBean.getIdRemuneration());
			lienRemunerationRevenuPersistant.setRemuneration(remu);

			RevenuComplementaire rc = new RevenuComplementaire();
			rc.setId(lienBean.getIdRevenuComplementaire());
			lienRemunerationRevenuPersistant.setRevenuComplementaire(rc);

			if (!lienBean.getMontant().equals("")) {
				lienRemunerationRevenuPersistant.setMontant(Double
						.valueOf(lienBean.getMontant()));
			} else {
				lienRemunerationRevenuPersistant.setMontant(new Double(0));
			}

			if (lienBean.getActualisation() != null
					&& !lienBean.getActualisation().equals("")) {
				lienRemunerationRevenuPersistant.setActualisation(Double
						.valueOf(lienBean.getActualisation()));
			} else {
				lienRemunerationRevenuPersistant
						.setActualisation(new Double(0));
			}

			lienRemunerationRevenuPersistant.setCommentaire(lienBean
					.getCommentaire());
			lienRemunerationRevenuPersistant.setRemonteNPrec(lienBean
					.isRemonteNPrec());
			session.saveOrUpdate(lienRemunerationRevenuPersistant);
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

	public void save(LienRemunerationRevenuBean lienBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			LienRemunerationRevenuComplementaire lienRemunerationRevenuPersistant = new LienRemunerationRevenuComplementaire();
			lienRemunerationRevenuPersistant
					.setIdLienRemunerationRevenuComplementaire(lienBean
							.getIdLienRemunerationRevenuComplementaire());

			RevenuComplementaire r = new RevenuComplementaire();
			r.setId(lienBean.getIdRevenuComplementaire());
			lienRemunerationRevenuPersistant.setRevenuComplementaire(r);

			Remuneration remu = new Remuneration();
			remu.setIdRemuneration(lienBean.getIdRemuneration());
			lienRemunerationRevenuPersistant.setRemuneration(remu);
			RevenuComplementaire rc = new RevenuComplementaire();
			rc.setId(lienBean.getIdRevenuComplementaire());
			lienRemunerationRevenuPersistant.setRevenuComplementaire(rc);
			if (!lienBean.getMontant().equals("")) {
				lienRemunerationRevenuPersistant.setMontant(Double
						.valueOf(lienBean.getMontant()));
			} else {
				lienRemunerationRevenuPersistant.setMontant(new Double(0));
			}

			if (lienBean.getActualisation() != null
					&& !lienBean.getActualisation().equals("")) {
				lienRemunerationRevenuPersistant.setActualisation(Double
						.valueOf(lienBean.getActualisation()));
			} else {
				lienRemunerationRevenuPersistant
						.setActualisation(new Double(0));
			}

			lienRemunerationRevenuPersistant.setCommentaire(lienBean
					.getCommentaire());
			lienRemunerationRevenuPersistant.setRemonteNPrec(lienBean
					.isRemonteNPrec());
			session.save(lienRemunerationRevenuPersistant);
			lienBean.setIdLienRemunerationRevenuComplementaire(lienRemunerationRevenuPersistant
					.getIdLienRemunerationRevenuComplementaire());

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public boolean supprimer(
			LienRemunerationRevenuBean lienRemunerationRevenuBean)
			throws Exception {
		suppression(lienRemunerationRevenuBean);
		return true;
	}

	public void suppression(
			LienRemunerationRevenuBean lienRemunerationRevenuBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			LienRemunerationRevenuComplementaire lienRemunerationRevenuPersistant = new LienRemunerationRevenuComplementaire();
			transaction = session.beginTransaction();
			if (lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire() != 0) {
				lienRemunerationRevenuPersistant = (LienRemunerationRevenuComplementaire) session
						.load(LienRemunerationRevenuComplementaire.class,
								lienRemunerationRevenuBean
										.getIdLienRemunerationRevenuComplementaire());

				lienRemunerationRevenuPersistant
						.setIdLienRemunerationRevenuComplementaire(lienRemunerationRevenuBean
								.getIdLienRemunerationRevenuComplementaire());
			}
			session.delete(lienRemunerationRevenuPersistant);
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

	public void deleteLienRemuRevCompWithoutTransaction(
			LienRemunerationRevenuBean lienRemunerationRevenuBean) {
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			LienRemunerationRevenuComplementaire lienRemunerationRevenuPersistant = new LienRemunerationRevenuComplementaire();
			if (lienRemunerationRevenuBean
					.getIdLienRemunerationRevenuComplementaire() != 0) {
				lienRemunerationRevenuPersistant = (LienRemunerationRevenuComplementaire) session
						.load(LienRemunerationRevenuComplementaire.class,
								lienRemunerationRevenuBean
										.getIdLienRemunerationRevenuComplementaire());

				lienRemunerationRevenuPersistant
						.setIdLienRemunerationRevenuComplementaire(lienRemunerationRevenuBean
								.getIdLienRemunerationRevenuComplementaire());
			}
			session.delete(lienRemunerationRevenuPersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

}
