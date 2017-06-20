package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.DomaineFormationBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.db.Domaineformation;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class DomaineFormationServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DomaineFormationServiceImpl.class);
	
	/**
	 * Retourne la liste des Entreprises.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<DomaineFormationBean> getDomaineFormationsList()
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<DomaineFormationBean> domaineFormationsInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			List<Domaineformation> domaineFormationsInventory = session
					.createQuery("from Domaineformation").list();
			domaineFormationsInventoryBean = domaineFormationPersistantListToDomaineFormationBeanList(domaineFormationsInventory);
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
		return domaineFormationsInventoryBean;
	}

	public boolean supprimer(DomaineFormationBean domaineFormationBean,
			int idGroupe) throws Exception {
		FormationServiceImpl formation = new FormationServiceImpl();
		List<FormationBean> l = formation.getFormationsList(idGroupe);
		for (int i = 0; i < l.size(); i++) {
			FormationBean f = l.get(i);
			if (f.getIdDomaineFormationBeanSelected() == domaineFormationBean
					.getId()) {
				return false;
			}
		}
		suppression(domaineFormationBean);
		return true;
	}

	private void suppression(DomaineFormationBean domaineFormationBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Domaineformation domaineFormationPersistant = new Domaineformation();
			transaction = session.beginTransaction();
			if (domaineFormationBean.getId() != 0) {
				domaineFormationPersistant = (Domaineformation) session.load(
						Domaineformation.class, domaineFormationBean.getId());

				domaineFormationPersistant.setId(domaineFormationBean.getId());
			}
			session.delete(domaineFormationPersistant);
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

	public void saveOrUppdate(DomaineFormationBean domaineFormationBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Domaineformation domainePersistant = new Domaineformation();
			transaction = session.beginTransaction();
			if (domaineFormationBean.getId() != 0) {
				domainePersistant = (Domaineformation) session.load(
						Domaineformation.class, domaineFormationBean.getId());

				domainePersistant.setId(domaineFormationBean.getId());
			}
			domainePersistant.setNomDomaineFormation(domaineFormationBean
					.getNom());
			session.saveOrUpdate(domainePersistant);
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

	public void save(DomaineFormationBean domaineFormationBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Domaineformation domainePersistant = new Domaineformation();
			transaction = session.beginTransaction();

			domainePersistant.setId(domaineFormationBean.getId());
			domainePersistant.setNomDomaineFormation(domaineFormationBean
					.getNom());

			session.save(domainePersistant);
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

	public DomaineFormationBean getDomaineFormationBeanById(
			Integer idDomaineFormation) throws Exception {
		Session session = null;
		Transaction transaction = null;
		DomaineFormationBean domaineFormationBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Domaineformation domaineFormationPersistant = new Domaineformation();
			transaction = session.beginTransaction();

			domaineFormationPersistant = (Domaineformation) session.load(
					Domaineformation.class, idDomaineFormation);
			
			domaineFormationBean = domaineFormationPersistantToDomaineFormationBean(domaineFormationPersistant);

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
		return domaineFormationBean;
	}

	private List<DomaineFormationBean> domaineFormationPersistantListToDomaineFormationBeanList(
			List<Domaineformation> domaineFormationPersistantList)
			throws Exception {

		List<DomaineFormationBean> domaineFormationBeanList = new ArrayList<DomaineFormationBean>();

		for (Domaineformation domaineFormationPersistant : domaineFormationPersistantList) {

			domaineFormationBeanList
					.add(domaineFormationPersistantToDomaineFormationBean(domaineFormationPersistant));
		}

		return domaineFormationBeanList;

	}

	public DomaineFormationBean domaineFormationPersistantToDomaineFormationBean(
			Domaineformation domaineFormationPersistant) throws Exception {

		DomaineFormationBean domaineFormationBean = new DomaineFormationBean();
		domaineFormationBean.setId(domaineFormationPersistant.getId());
		domaineFormationBean.setNom(domaineFormationPersistant
				.getNomDomaineFormation());

		return domaineFormationBean;
	}
}
