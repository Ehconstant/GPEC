package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.db.Entreprise;
import com.cci.gpec.db.Service;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class ServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceImpl.class);
	
	/**
	 * Retourne la liste des Entreprises.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws Exception
	 * @throws
	 */

	public boolean containEntrepriseBean(EntrepriseBean entreprise)
			throws Exception {
		List<ServiceBean> l = getServicesList(entreprise.getIdGroupe());
		for (int i = 0; i < l.size(); i++) {
			ServiceBean service = l.get(i);
			if (service.getIdEntreprise() == entreprise.getId()) {
				return true;
			}
		}
		return false;
	}

	public List<ServiceBean> getServicesList(int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<ServiceBean> servicesInventoryBean;
			transaction = session.beginTransaction();

			List<Service> servicesInventory = session.createQuery(
					"from Service as s left join fetch s.Entreprise as e where e.Groupe="
							+ idGroupe + " order by s.NomService").list();

			servicesInventoryBean = servicePersistantListToServiceBeanList(servicesInventory);
			transaction.commit();

			return servicesInventoryBean;

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

	public List<ServiceBean> getServiceBeanListByIdEntreprise(int idEntreprise)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<ServiceBean> servicesInventoryBean;
			transaction = session.beginTransaction();

			List<Service> servicesInventory = session.createQuery(
					"from Service where Entreprise=" + idEntreprise
							+ "order by NomService").list();

			servicesInventoryBean = servicePersistantListToServiceBeanList(servicesInventory);
			transaction.commit();

			return servicesInventoryBean;

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

	private boolean isSalarieContainService(ServiceBean serviceBean)
			throws Exception {
		EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
		EntrepriseBean ent = entServ.getEntrepriseBeanById(serviceBean
				.getIdEntreprise());

		SalarieServiceImpl salarie = new SalarieServiceImpl();
		List<SalarieBean> l = salarie.getSalariesList(ent.getIdGroupe());
		for (int i = 0; i < l.size(); i++) {
			SalarieBean sal = l.get(i);
			if (sal.getIdServiceSelected() == serviceBean.getId()) {
				return true;
			}
		}
		return false;
	}

	public boolean supprimer(ServiceBean serviceBean) throws Exception {
		// On vérifie qu'un service n'est pas associé à une entreprise
		if (isSalarieContainService(serviceBean)) {
			return false;
		} else {
			suppression(serviceBean);
		}
		return true;
	}

	public void suppression(ServiceBean serviceBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Service servicePersistant = new Service();
			transaction = session.beginTransaction();
			if (serviceBean.getId() != 0) {
				servicePersistant = (Service) session.load(Service.class,
						serviceBean.getId());

				servicePersistant.setId(serviceBean.getId());
			}
			session.delete(servicePersistant);
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

	public void deleteServiceWithoutTransaction(ServiceBean serviceBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Service servicePersistant = new Service();
			if (serviceBean.getId() != 0) {
				servicePersistant = (Service) session.load(Service.class,
						serviceBean.getId());

				servicePersistant.setId(serviceBean.getId());
			}
			session.delete(servicePersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void saveOrUppdate(ServiceBean serviceBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Service servicePersistant = new Service();
			transaction = session.beginTransaction();
			if (serviceBean.getId() != 0) {
				servicePersistant = (Service) session.load(Service.class,
						serviceBean.getId());

				servicePersistant.setId(serviceBean.getId());

			}

			Entreprise entreprise = new Entreprise();
			entreprise.setId(serviceBean.getIdEntreprise());
			entreprise.setNomEntreprise(serviceBean.getNomEntreprise());

			servicePersistant.setNomService(serviceBean.getNom());
			servicePersistant.setEntreprise(entreprise);
			session.saveOrUpdate(servicePersistant);
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

	public void save(ServiceBean serviceBean) {
		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Service servicePersistant = new Service();
			// transaction = session.beginTransaction();

			servicePersistant.setId(serviceBean.getId());

			Entreprise entreprise = new Entreprise();
			entreprise.setId(serviceBean.getIdEntreprise());
			entreprise.setNomEntreprise(serviceBean.getNomEntreprise());

			servicePersistant.setNomService(serviceBean.getNom());
			servicePersistant.setEntreprise(entreprise);
			session.save(servicePersistant);
			// transaction.commit();
			serviceBean.setId(servicePersistant.getId());
		} catch (HibernateException e) {
			// if (transaction != null && !transaction.wasRolledBack()) {
			// 	transaction.rollback();
			// }
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public ServiceBean getServiceBeanById(Integer idService) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Service servicePersistant = new Service();
			transaction = session.beginTransaction();

			servicePersistant = (Service) session
					.load(Service.class, idService);

			ServiceBean serviceBean = servicePersistantToServiceBean(servicePersistant);

			transaction.commit();

			return serviceBean;

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

	private List<ServiceBean> servicePersistantListToServiceBeanList(
			List<Service> servicePersistantList) throws Exception {

		List<ServiceBean> serviceBeanList = new ArrayList<ServiceBean>();

		for (Service servicePersistant : servicePersistantList) {

			serviceBeanList
					.add(servicePersistantToServiceBean(servicePersistant));
		}

		return serviceBeanList;

	}

	public ServiceBean servicePersistantToServiceBean(Service servicePersistant)
			throws Exception {

		ServiceBean serviceBean = new ServiceBean();
		serviceBean.setId(servicePersistant.getId());
		serviceBean.setNom(servicePersistant.getNomService());
		serviceBean.setIdEntreprise(servicePersistant.getEntreprise().getId());
		serviceBean.setNomEntreprise(servicePersistant.getEntreprise()
				.getNomEntreprise());

		return serviceBean;
	}
}
