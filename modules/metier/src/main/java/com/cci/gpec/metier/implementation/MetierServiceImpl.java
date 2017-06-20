package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.db.Famillemetier;
import com.cci.gpec.db.Groupe;
import com.cci.gpec.db.Metier;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class MetierServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MetierServiceImpl.class);
	
	/**
	 * Retourne la liste des Metiers.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<MetierBean> getMetiersList(int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<MetierBean> metierInventoryBean;
			transaction = session.beginTransaction();

			List<Metier> metiersInventory = session.createQuery(
					"from Metier where Groupe=" + idGroupe
							+ " order by NomMetier").list();

			metierInventoryBean = metierPersistantListToMetierBeanList(metiersInventory);
			transaction.commit();

			return metierInventoryBean;

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

	private List<MetierBean> metierPersistantListToMetierBeanList(
			List<Metier> metierPersistantList) throws Exception {

		List<MetierBean> metierBeanList = new ArrayList<MetierBean>();

		for (Metier metierPersistant : metierPersistantList) {

			metierBeanList.add(metierPersistantToMetierBean(metierPersistant));
		}

		return metierBeanList;

	}

	public void suppression(MetierBean metierBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Metier metierPersistant = new Metier();
			transaction = session.beginTransaction();
			if (metierBean.getId() != 0) {
				metierPersistant = (Metier) session.load(Metier.class,
						metierBean.getId());

				metierPersistant.setId(metierBean.getId());
			}
			session.delete(metierPersistant);
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

	public void deleteMetierWithoutTransaction(MetierBean metierBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Metier metierPersistant = new Metier();
			if (metierBean.getId() != 0) {
				metierPersistant = (Metier) session.load(Metier.class,
						metierBean.getId());

				metierPersistant.setId(metierBean.getId());
			}
			session.delete(metierPersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void saveOrUppdate(MetierBean metierBean, int idGroupe) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Metier metierPersistant = new Metier();
			transaction = session.beginTransaction();
			if (metierBean.getId() != 0) {
				metierPersistant = (Metier) session.load(Metier.class,
						metierBean.getId());

				metierPersistant.setId(metierBean.getId());
			}
			Groupe groupePersistant = new Groupe();
			groupePersistant = (Groupe) session.load(Groupe.class, idGroupe);
			metierPersistant.setGroupe(groupePersistant);
			metierPersistant.setDifficultes(metierBean.getDifficultes());
			metierPersistant.setNomMetier(metierBean.getNom());
			Famillemetier fm = new Famillemetier();
			fm.setId(metierBean.getIdFamilleMetier());
			metierPersistant.setFamilleMetier(fm);
			session.saveOrUpdate(metierPersistant);

			metierBean.setId(metierPersistant.getId());

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

	public void save(MetierBean metierBean, int idGroupe) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Metier metierPersistant = new Metier();

			metierPersistant.setId(metierBean.getId());

			Groupe groupePersistant = new Groupe();
			groupePersistant = (Groupe) session.load(Groupe.class, idGroupe);

			metierPersistant.setGroupe(groupePersistant);

			metierPersistant.setNomMetier(metierBean.getNom());

			Famillemetier fm = (Famillemetier) session.load(
					Famillemetier.class, metierBean.getIdFamilleMetier());
			metierPersistant.setFamilleMetier(fm);

			metierPersistant.setDifficultes(metierBean.getDifficultes());

			session.save(metierPersistant);
			metierBean.setId(metierPersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public MetierBean getMetierBeanById(Integer idMetier) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Metier metierPersistant = new Metier();
			transaction = session.beginTransaction();

			metierPersistant = (Metier) session.load(Metier.class, idMetier);

			MetierBean metierBean = metierPersistantToMetierBean(metierPersistant);

			transaction.commit();

			return metierBean;

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

	public MetierBean metierPersistantToMetierBean(Metier metierPersistant)
			throws Exception {

		MetierBean metierBean = new MetierBean();
		metierBean.setId(metierPersistant.getId());
		metierBean.setNom(metierPersistant.getNomMetier());
		metierBean.setDifficultes(metierPersistant.getDifficultes());
		metierBean.setIdFamilleMetier(metierPersistant.getFamilleMetier()
				.getId());
		metierBean.setNomFamilleMetier(metierPersistant.getFamilleMetier()
				.getNomFamilleMetier());

		return metierBean;
	}
}
