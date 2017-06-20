package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.FamilleMetierBean;
import com.cci.gpec.db.Famillemetier;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class FamilleMetierServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FamilleMetierServiceImpl.class);
	
	/**
	 * Retourne la liste des familleMetiers.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<FamilleMetierBean> getFamilleMetiersList() throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<FamilleMetierBean> familleMetierInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Famillemetier> familleMetiersInventory = session.createQuery(
					"from Famillemetier").list();
			familleMetierInventoryBean = familleMetierPersistantListTofamilleMetierBeanList(familleMetiersInventory);
			transaction.commit();

			Collections.sort(familleMetierInventoryBean);
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
		return familleMetierInventoryBean;
	}

	public void supprimer(FamilleMetierBean familleMetierBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Famillemetier familleMetierPersistant = new Famillemetier();
			transaction = session.beginTransaction();
			if (familleMetierBean.getId() != 0) {
				familleMetierPersistant = (Famillemetier) session.load(
						Famillemetier.class, familleMetierBean.getId());

				familleMetierPersistant.setId(familleMetierBean.getId());
			}
			session.delete(familleMetierPersistant);
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

	public void saveOrUppdate(FamilleMetierBean familleMetierBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Famillemetier familleMetierPersistant = new Famillemetier();
			transaction = session.beginTransaction();
			if (familleMetierBean.getId() != 0) {
				familleMetierPersistant = (Famillemetier) session.load(
						Famillemetier.class, familleMetierBean.getId());

				familleMetierPersistant.setId(familleMetierBean.getId());
			}

			familleMetierPersistant.setNomFamilleMetier(familleMetierBean
					.getNom());

			session.saveOrUpdate(familleMetierPersistant);
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

	public void save(FamilleMetierBean familleMetierBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Famillemetier familleMetierPersistant = new Famillemetier();
			transaction = session.beginTransaction();

			familleMetierPersistant.setId(familleMetierBean.getId());

			familleMetierPersistant.setNomFamilleMetier(familleMetierBean
					.getNom());

			session.save(familleMetierPersistant);
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

	public FamilleMetierBean getFamilleMetierBeanById(Integer idfamilleMetier)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		FamilleMetierBean familleMetierBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Famillemetier familleMetierPersistant = new Famillemetier();
			transaction = session.beginTransaction();
			familleMetierPersistant = (Famillemetier) session.load(
					Famillemetier.class, idfamilleMetier);
			familleMetierBean = familleMetierPersistantTofamilleMetierBean(familleMetierPersistant);
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
		return familleMetierBean;
	}

	private List<FamilleMetierBean> familleMetierPersistantListTofamilleMetierBeanList(
			List<Famillemetier> familleMetierPersistantList) throws Exception {

		List<FamilleMetierBean> familleMetierBeanList = new ArrayList<FamilleMetierBean>();

		for (Famillemetier familleMetierPersistant : familleMetierPersistantList) {

			familleMetierBeanList
					.add(familleMetierPersistantTofamilleMetierBean(familleMetierPersistant));
		}

		return familleMetierBeanList;

	}

	public FamilleMetierBean familleMetierPersistantTofamilleMetierBean(
			Famillemetier familleMetierPersistant) throws Exception {
		FamilleMetierBean familleMetierBean = new FamilleMetierBean();
		familleMetierBean.setId(familleMetierPersistant.getId());
		familleMetierBean.setNom(familleMetierPersistant.getNomFamilleMetier());
		return familleMetierBean;
	}
}
