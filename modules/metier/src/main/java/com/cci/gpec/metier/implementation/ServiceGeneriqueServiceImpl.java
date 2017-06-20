package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.ModelBean;
import com.cci.gpec.db.Servicegenerique;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class ServiceGeneriqueServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceGeneriqueServiceImpl.class);
	
	/**
	 * Retourne la liste des ServiceGeneriques.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<ModelBean> getServiceGeneriquesList() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<ModelBean> ServiceGeneriqueInventoryBean;
			transaction = session.beginTransaction();

			List<Servicegenerique> ServiceGeneriquesInventory = session
					.createQuery("from Servicegenerique").list();
			ServiceGeneriqueInventoryBean = ServiceGeneriquePersistantListToModelBeanList(ServiceGeneriquesInventory);
			transaction.commit();

			return ServiceGeneriqueInventoryBean;

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

	public void saveOrUppdate(ModelBean serviceGeneriqueBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			ModelBean serviceGeneriquePersistant = new ModelBean();
			transaction = session.beginTransaction();
			if (serviceGeneriqueBean.getId() != 0) {
				serviceGeneriquePersistant = (ModelBean) session.load(
						ModelBean.class, serviceGeneriqueBean.getId());

				serviceGeneriquePersistant.setId(serviceGeneriqueBean.getId());
			}

			serviceGeneriquePersistant.setNom(serviceGeneriqueBean.getNom());

			session.saveOrUpdate(serviceGeneriquePersistant);
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

	public void save(ModelBean serviceGeneriqueBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Servicegenerique serviceGeneriquePersistant = new Servicegenerique();
			transaction = session.beginTransaction();

			serviceGeneriquePersistant.setId(serviceGeneriqueBean.getId());

			serviceGeneriquePersistant
					.setNomServiceGenerique(serviceGeneriqueBean.getNom());

			session.save(serviceGeneriquePersistant);
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

	private List<ModelBean> ServiceGeneriquePersistantListToModelBeanList(
			List<Servicegenerique> ServiceGeneriquePersistantList)
			throws Exception {

		List<ModelBean> ModelBeanList = new ArrayList<ModelBean>();

		for (Servicegenerique ServiceGeneriquePersistant : ServiceGeneriquePersistantList) {

			ModelBeanList
					.add(metierPersistantToMetierBean(ServiceGeneriquePersistant));
		}

		return ModelBeanList;

	}

	public ModelBean metierPersistantToMetierBean(
			Servicegenerique servPersistant) throws Exception {

		ModelBean serv_genBean = new ModelBean();
		serv_genBean.setId(servPersistant.getId());
		serv_genBean.setNom(servPersistant.getNomServiceGenerique());

		return serv_genBean;
	}

}
