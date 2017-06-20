package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.db.Habilitation;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.Typehabilitation;
import com.cci.gpec.db.connection.HibernateUtil;

public class HabilitationServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HabilitationServiceImpl.class);
	
	/**
	 * Retourne la liste des Habilitations.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<HabilitationBean> getHabilitationsList(int idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<HabilitationBean> habilitationsInventoryBean;
			transaction = session.beginTransaction();

			List<Habilitation> habilitationsInventory = session
					.createQuery(
							"from Habilitation as h left join fetch h.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe).list();

			habilitationsInventoryBean = habilitationPersistantListToHabilitationBeanList(habilitationsInventory);
			transaction.commit();

			return habilitationsInventoryBean;

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

	public List<HabilitationBean> getHabilitationsListOrderByAnneeNomEntrepriseNomSalarie(
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<HabilitationBean> habilitationsInventoryBean;
			transaction = session.beginTransaction();

			List<Habilitation> habilitationsInventory = session
					.createQuery(
							"from Habilitation as h left join fetch h.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " order by year(h.Delivrance) desc, e.NomEntreprise, s.Nom")
					.list();

			habilitationsInventoryBean = habilitationPersistantListToHabilitationBeanList(habilitationsInventory);
			transaction.commit();

			return habilitationsInventoryBean;

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

	public List<HabilitationBean> getHabilitationsListOrderByAnneeNomSalarie(
			int idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<HabilitationBean> habilitationsInventoryBean;
			transaction = session.beginTransaction();

			List<Habilitation> habilitationsInventory = session.createQuery(
					"from Habilitation as h left join fetch h.Salarie as s where s.Entreprise = "
							+ idEntreprise
							+ " order by year(h.Delivrance) desc, s.Nom")
					.list();

			habilitationsInventoryBean = habilitationPersistantListToHabilitationBeanList(habilitationsInventory);
			transaction.commit();

			return habilitationsInventoryBean;

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

	public List<HabilitationBean> getHabilitationsListFromIdSalarie(Integer id)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<HabilitationBean> habilitationsInventoryBean;
			transaction = session.beginTransaction();

			List<Habilitation> habilitationsInventory = session.createQuery(
					"from Habilitation where Salarie=" + id).list();

			habilitationsInventoryBean = habilitationPersistantListToHabilitationBeanList(habilitationsInventory);
			transaction.commit();

			return habilitationsInventoryBean;

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

	public List<HabilitationBean> getHabilitationsListFromIdSalarieOrderByAnnee(
			Integer id) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<HabilitationBean> habilitationsInventoryBean;
			transaction = session.beginTransaction();

			List<Habilitation> habilitationsInventory = session.createQuery(
					"from Habilitation where Salarie=" + id
							+ " order by Delivrance desc").list();

			habilitationsInventoryBean = habilitationPersistantListToHabilitationBeanList(habilitationsInventory);
			transaction.commit();

			return habilitationsInventoryBean;

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

	public List<HabilitationBean> getHabilitationBeanListByIdSalarie(int salarie)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<HabilitationBean> habilitationInventoryBean;
			transaction = session.beginTransaction();

			List<Habilitation> habilitationInventory = session.createQuery(
					"from Habilitation where Salarie=" + salarie).list();

			habilitationInventoryBean = habilitationPersistantListToHabilitationBeanList(habilitationInventory);
			transaction.commit();

			return habilitationInventoryBean;

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

	public List<HabilitationBean> getHabilitationsListFromTypeHabilitation(
			Integer idTypeHabilitation, int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<HabilitationBean> habilitationsInventoryBean;
			transaction = session.beginTransaction();

			List<Habilitation> habilitationsInventory = session
					.createQuery(
							"from Habilitation as h left join fetch h.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " and h.TypeHabilitation="
									+ idTypeHabilitation).list();

			habilitationsInventoryBean = habilitationPersistantListToHabilitationBeanList(habilitationsInventory);
			transaction.commit();

			return habilitationsInventoryBean;

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

	public void delete(HabilitationBean habilitationBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Habilitation habilitationPersistant = new Habilitation();
			transaction = session.beginTransaction();
			if (habilitationBean.getId() != 0) {
				habilitationPersistant = (Habilitation) session.load(
						Habilitation.class, habilitationBean.getId());

				habilitationPersistant.setId(habilitationBean.getId());
			}

			session.delete(habilitationPersistant);
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

	public void deleteHabilitationWithoutTransaction(
			HabilitationBean habilitationBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Habilitation habilitationPersistant = new Habilitation();
			if (habilitationBean.getId() != 0) {
				habilitationPersistant = (Habilitation) session.load(
						Habilitation.class, habilitationBean.getId());

				habilitationPersistant.setId(habilitationBean.getId());
			}

			session.delete(habilitationPersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void saveOrUppdate(HabilitationBean habilitationBean)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Habilitation habilitationPersistant = new Habilitation();
			transaction = session.beginTransaction();
			habilitationPersistant = habilitationBeanToHabilitationPersistant(
					habilitationBean, habilitationBean.getIdSalarie());
			session.saveOrUpdate(habilitationPersistant);

			habilitationBean.setId(habilitationPersistant.getId());

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

	public void save(HabilitationBean habilitationBean) throws Exception {
		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Habilitation habilitationPersistant = new Habilitation();
			// transaction = session.beginTransaction();
			habilitationPersistant = habilitationBeanToHabilitationPersistant(
					habilitationBean, habilitationBean.getIdSalarie());
			session.save(habilitationPersistant);
			// transaction.commit();
			habilitationBean.setId(habilitationPersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public String getJustificatif(Integer idHabilitation) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Habilitation habilitationPersistant = new Habilitation();
			transaction = session.beginTransaction();
			habilitationPersistant = (Habilitation) session.load(
					Habilitation.class, idHabilitation);
			HabilitationBean habilitationBean = habilitationPersistantToHabilitationBean(habilitationPersistant);
			transaction.commit();
			return habilitationBean.getJustificatif();

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

	public HabilitationBean getHabilitationBeanById(Integer idHabilitation)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Habilitation habilitationPersistant = new Habilitation();
			transaction = session.beginTransaction();

			habilitationPersistant = (Habilitation) session.load(
					Habilitation.class, idHabilitation);

			HabilitationBean habilitationBean = habilitationPersistantToHabilitationBean(habilitationPersistant);

			transaction.commit();

			return habilitationBean;

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

	public List<HabilitationBean> habilitationPersistantListToHabilitationBeanList(
			List<Habilitation> habilitationPersistantList) throws Exception {

		List<HabilitationBean> habilitationBeanList = new ArrayList<HabilitationBean>();

		for (Habilitation habilitationPersistant : habilitationPersistantList) {

			habilitationBeanList
					.add(habilitationPersistantToHabilitationBean(habilitationPersistant));
		}

		return habilitationBeanList;

	}

	public HabilitationBean habilitationPersistantToHabilitationBean(
			Habilitation habilitationPersistant) throws Exception {

		HabilitationBean habilitationBean = new HabilitationBean();
		habilitationBean.setId(habilitationPersistant.getId());
		habilitationBean.setDelivrance(habilitationPersistant.getDelivrance());
		habilitationBean.setDureeValidite(habilitationPersistant
				.getDureeValidite());
		habilitationBean.setExpiration(habilitationPersistant.getExpiration());
		if (habilitationPersistant.getJustificatif() != null
				&& habilitationPersistant.getJustificatif().equals("")) {
			habilitationBean.setJustificatif(null);
		} else {
			habilitationBean.setJustificatif(habilitationPersistant
					.getJustificatif());
		}
		habilitationBean.setIdTypeHabilitationSelected(habilitationPersistant
				.getTypeHabilitation().getId());
		habilitationBean.setIdSalarie(habilitationPersistant.getSalarie()
				.getId());
		habilitationBean.setNomTypeHabilitation(habilitationPersistant
				.getTypeHabilitation().getNomTypeHabilitation());
		habilitationBean
				.setCommentaire(habilitationPersistant.getCommentaire());

		return habilitationBean;
	}

	public List<Habilitation> habilitationBeanListToHabilitationPersistantList(
			SalarieBean salarieBean) throws Exception {

		List<Habilitation> habilitationList = new ArrayList<Habilitation>();

		for (HabilitationBean habilitationBean : salarieBean
				.getHabilitationBeanList()) {

			habilitationList.add(habilitationBeanToHabilitationPersistant(
					habilitationBean, salarieBean.getId()));
		}

		return habilitationList;

	}

	public Habilitation habilitationBeanToHabilitationPersistant(
			HabilitationBean habilitationBean, int idSalarie) throws Exception {

		Session session = null;
		Habilitation habilitationPersistant = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			habilitationPersistant = new Habilitation();
	
			if (habilitationBean.getId() == -1) {
				habilitationPersistant.setId(null);
			} else {
				habilitationPersistant.setId(habilitationBean.getId());
			}
			Salarie salarie = (Salarie) session.load(Salarie.class, idSalarie);
			habilitationPersistant.setSalarie(salarie);
			habilitationPersistant.setDelivrance(habilitationBean.getDelivrance());
			habilitationPersistant.setDureeValidite(habilitationBean
					.getDureeValidite());
			habilitationPersistant.setExpiration(habilitationBean.getExpiration());
			habilitationPersistant.setJustificatif(habilitationBean
					.getJustificatif());
			habilitationPersistant
					.setCommentaire(habilitationBean.getCommentaire());
	
			Typehabilitation typeHabilitation = new Typehabilitation();
	
			typeHabilitation = (Typehabilitation) session.load(
					Typehabilitation.class,
					habilitationBean.getIdTypeHabilitationSelected());
	
			habilitationPersistant.setTypeHabilitation(typeHabilitation);
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
		return habilitationPersistant;
	}
}
