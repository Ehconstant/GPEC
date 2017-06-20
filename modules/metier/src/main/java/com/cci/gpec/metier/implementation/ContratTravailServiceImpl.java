package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.ContratTravailBean;
import com.cci.gpec.db.ContratTravail;
import com.cci.gpec.db.Metier;
import com.cci.gpec.db.Motifrupturecontrat;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.Typecontrat;
import com.cci.gpec.db.connection.HibernateUtil;

public class ContratTravailServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContratTravailServiceImpl.class);

	public List<ContratTravailBean> getContratTravailsList(int idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<ContratTravailBean> contratTravailsInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<ContratTravail> contratTravailsInventory = session
					.createQuery(
							"from ContratTravail as c left join fetch c.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe).list();

			contratTravailsInventoryBean = contratTravailPersistantListToContratTravailBeanList(contratTravailsInventory);
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
		return contratTravailsInventoryBean;
	}

	public List<ContratTravailBean> getContratTravailsListOrderByNomEntrepriseNomSalarie(
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<ContratTravailBean> contratTravailsInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<ContratTravail> contratTravailsInventory = session
					.createQuery(
							"from ContratTravail as c left join fetch c.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " order by e.NomEntreprise, s.Nom")
					.list();

			contratTravailsInventoryBean = contratTravailPersistantListToContratTravailBeanList(contratTravailsInventory);
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
		return contratTravailsInventoryBean;
	}

	public List<ContratTravailBean> getContratTravailsListOrderNomSalarie(
			int idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<ContratTravailBean> contratTravailsInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<ContratTravail> contratTravailsInventory = session
					.createQuery(
							"from ContratTravail as c left join fetch c.Salarie as s where s.Entreprise = "
									+ idEntreprise + " order by s.Nom").list();

			contratTravailsInventoryBean = contratTravailPersistantListToContratTravailBeanList(contratTravailsInventory);
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
		return contratTravailsInventoryBean;
	}

	public String getJustificatif(Integer idCT) throws Exception {
		Session session = null;
		Transaction transaction = null;
		String justificatif = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			ContratTravail contratTravailPersistant = new ContratTravail();
			transaction = session.beginTransaction();
			contratTravailPersistant = (ContratTravail) session.load(
					ContratTravail.class, idCT);
			ContratTravailBean contratTravailBean = contratTravailPersistantToContratTravailBean(contratTravailPersistant);
			transaction.commit();
			justificatif = contratTravailBean.getJustificatif();
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
		return justificatif;
	}

	public void saveOrUppdate(ContratTravailBean contratTravailBean)
			throws Exception {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			ContratTravail contratTravailPersistant = new ContratTravail();
			transaction = session.beginTransaction();

			contratTravailPersistant = contratTravailBeanToContratTravailPersistant(
					contratTravailBean, contratTravailBean.getIdSalarie());
			session.saveOrUpdate(contratTravailPersistant);

			contratTravailBean.setId(contratTravailPersistant.getId());
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

	public void save(ContratTravailBean contratTravailBean) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			ContratTravail contratTravailPersistant = new ContratTravail();
			session.save(contratTravailBeanToContratTravailPersistant(
					contratTravailBean, contratTravailBean.getIdSalarie()));
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void deleteContratTravail(ContratTravailBean contratTravailBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			ContratTravail contratTravailPersistant = new ContratTravail();
			transaction = session.beginTransaction();
			if (contratTravailBean.getId() != 0) {
				contratTravailPersistant = (ContratTravail) session.load(
						ContratTravail.class, contratTravailBean.getId());

				contratTravailPersistant.setId(contratTravailBean.getId());
			}
			session.delete(contratTravailPersistant);
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

	public void deleteContratTravailWithoutTransaction(
			ContratTravailBean contratTravailBean) {
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			ContratTravail contratTravailPersistant = new ContratTravail();
			if (contratTravailBean.getId() != 0) {
				contratTravailPersistant = (ContratTravail) session.load(
						ContratTravail.class, contratTravailBean.getId());

				contratTravailPersistant.setId(contratTravailBean.getId());
			}
			session.delete(contratTravailPersistant);
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public ContratTravailBean getContratTravailBeanById(Integer idContratTravail)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		ContratTravailBean contratTravailBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			ContratTravail contratTravailPersistant = new ContratTravail();
			transaction = session.beginTransaction();

			contratTravailPersistant = (ContratTravail) session.load(
					ContratTravail.class, idContratTravail);

			contratTravailBean = contratTravailPersistantToContratTravailBean(contratTravailPersistant);

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
		return contratTravailBean;
	}

	public List<ContratTravailBean> contratTravailPersistantListToContratTravailBeanList(
			List<ContratTravail> contratTravailPersistantList) throws Exception {

		List<ContratTravailBean> contratTravailBeanList = new ArrayList<ContratTravailBean>();

		for (ContratTravail contratTravailPersistant : contratTravailPersistantList) {

			contratTravailBeanList
					.add(contratTravailPersistantToContratTravailBean(contratTravailPersistant));
		}

		return contratTravailBeanList;

	}

	public ContratTravailBean contratTravailPersistantToContratTravailBean(
			ContratTravail contratTravailPersistant) throws Exception {

		ContratTravailBean contratTravailBean = new ContratTravailBean();
		contratTravailBean.setId(contratTravailPersistant.getId());
		contratTravailBean.setDebutContrat(contratTravailPersistant
				.getDebutContrat());
		contratTravailBean.setFinContrat(contratTravailPersistant
				.getFinContrat());

		contratTravailBean.setRenouvellementCDD(contratTravailPersistant
				.isRenouvellementCDD());

		contratTravailBean.setIdMetierSelected(contratTravailPersistant
				.getMetier().getId());
		contratTravailBean.setIdTypeContratSelected(contratTravailPersistant
				.getTypeContrat().getId());
		if (contratTravailPersistant.getMotifRuptureContrat() != null) {
			contratTravailBean
					.setIdMotifRuptureContrat(contratTravailPersistant
							.getMotifRuptureContrat().getId());
			contratTravailBean
					.setNomMotifRuptureContrat(contratTravailPersistant
							.getMotifRuptureContrat()
							.getNomMotifRuptureContrat());
		} else {
			contratTravailBean.setIdMotifRuptureContrat(null);
		}
		contratTravailBean.setIdSalarie(contratTravailPersistant.getSalarie()
				.getId());
		contratTravailBean.setNomMetier(contratTravailPersistant.getMetier()
				.getNomMetier());
		contratTravailBean.setNomTypeContrat(contratTravailPersistant
				.getTypeContrat().getNomTypeContrat());
		if (contratTravailPersistant.getJustificatif() != null
				&& contratTravailPersistant.getJustificatif().equals("")) {
			contratTravailBean.setJustificatif(null);
		} else {
			contratTravailBean.setJustificatif(contratTravailPersistant
					.getJustificatif());
		}

		return contratTravailBean;
	}

	public List<ContratTravailBean> getContratTravailBeanListByIdSalarie(
			int salarie) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<ContratTravailBean> contratTravailInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<ContratTravail> contratTravailInventory = session.createQuery(
					"from ContratTravail where Salarie=" + salarie).list();

			contratTravailInventoryBean = contratTravailPersistantListToContratTravailBeanList(contratTravailInventory);
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
		return contratTravailInventoryBean;
	}

	public List<ContratTravailBean> getContratTravailBeanListByIdMetier(
			int metier) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<ContratTravailBean> contratTravailInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<ContratTravail> contratTravailInventory = session.createQuery(
					"from ContratTravail where Metier=" + metier).list();

			contratTravailInventoryBean = contratTravailPersistantListToContratTravailBeanList(contratTravailInventory);
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
		return contratTravailInventoryBean;
	}

	public ContratTravail contratTravailBeanToContratTravailPersistant(
			ContratTravailBean contratTravailBean, int idSalarie)
			throws Exception {

		Session session = null;
		ContratTravail contratTravailPersistant = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			contratTravailPersistant = new ContratTravail();
	
			if (contratTravailBean.getId() == -1) {
				contratTravailPersistant.setId(null);
			} else {
				contratTravailPersistant.setId(contratTravailBean.getId());
			}
			Salarie salarie = (Salarie) session.load(Salarie.class, idSalarie);
			contratTravailPersistant.setSalarie(salarie);
			contratTravailPersistant.setDebutContrat(contratTravailBean
					.getDebutContrat());
			contratTravailPersistant.setFinContrat(contratTravailBean
					.getFinContrat());
			contratTravailPersistant.setJustificatif(contratTravailBean
					.getJustificatif());
			contratTravailPersistant.setRenouvellementCDD(contratTravailBean
					.isRenouvellementCDD());
	
			Metier metier = new Metier();
			metier = (Metier) session.load(Metier.class,
					contratTravailBean.getIdMetierSelected());
			contratTravailPersistant.setMetier(metier);
	
			Motifrupturecontrat motifRupture = new Motifrupturecontrat();
			if (contratTravailBean.getIdMotifRuptureContrat() != null) {
				motifRupture = (Motifrupturecontrat) session.load(
						Motifrupturecontrat.class,
						contratTravailBean.getIdMotifRuptureContrat());
				contratTravailPersistant.setMotifRuptureContrat(motifRupture);
			} else {
				contratTravailPersistant.setMotifRuptureContrat(null);
			}
	
			Typecontrat typeContrat = new Typecontrat();
			typeContrat = (Typecontrat) session.load(Typecontrat.class,
					contratTravailBean.getIdTypeContratSelected());
			contratTravailPersistant.setTypeContrat(typeContrat);
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
		return contratTravailPersistant;
	}
}
