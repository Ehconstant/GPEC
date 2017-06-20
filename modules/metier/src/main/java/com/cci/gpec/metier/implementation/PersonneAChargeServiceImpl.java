package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.PersonneAChargeBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.db.PersonneACharge;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.connection.HibernateUtil;

public class PersonneAChargeServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonneAChargeServiceImpl.class);

	/**
	 * Retourne la liste des Personnes a charge.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<PersonneAChargeBean> getPersonneAChargeBeanList(int idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<PersonneAChargeBean> personneAChargesInventoryBean;
			transaction = session.beginTransaction();

			List<PersonneACharge> personneAChargesInventory = session
					.createQuery(
							"from PersonneACharge as p left join fetch p.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " order by p.LienParente, p.Nom, p.Prenom")
					.list();

			personneAChargesInventoryBean = personneAChargePersistantListToPersonneAChargeBeanList(personneAChargesInventory);
			transaction.commit();

			return personneAChargesInventoryBean;

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

	public List<PersonneAChargeBean> getPersonneAChargeBeanListByIdSalarie(
			int salarie) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<PersonneAChargeBean> personneAChargesInventoryBean;
			transaction = session.beginTransaction();

			List<PersonneACharge> personneAChargesInventory = session
					.createQuery(
							"from PersonneACharge where Salarie=" + salarie
									+ " order by LienParente, Nom, Prenom")
					.list();

			personneAChargesInventoryBean = personneAChargePersistantListToPersonneAChargeBeanList(personneAChargesInventory);
			transaction.commit();

			return personneAChargesInventoryBean;

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

	public void saveOrUppdate(PersonneAChargeBean personneAChargeBean)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(personneAChargeBeanToPersonneAChargePersistant(
					personneAChargeBean, personneAChargeBean.getIdSalarie(), false));
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

	public void save(PersonneAChargeBean personneAChargeBean) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.save(personneAChargeBeanToPersonneAChargePersistant(
					personneAChargeBean, personneAChargeBean.getIdSalarie(), false));
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public PersonneAChargeBean getPersonneAChargeBeanById(
			Integer idPersonneACharge) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			PersonneACharge personneAChargePersistant = new PersonneACharge();
			transaction = session.beginTransaction();

			if (idPersonneACharge == -1) {
				return null;
			}

			personneAChargePersistant = (PersonneACharge) session.load(
					PersonneACharge.class, idPersonneACharge);

			PersonneAChargeBean personneAChargeBean = personneAChargePersistantToPersonneAChargeBean(personneAChargePersistant);

			transaction.commit();

			return personneAChargeBean;

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

	public List<PersonneAChargeBean> personneAChargePersistantListToPersonneAChargeBeanList(
			List<PersonneACharge> personneAChargePersistantList)
			throws Exception {

		List<PersonneAChargeBean> personneAChargeBeanList = new ArrayList<PersonneAChargeBean>();

		for (PersonneACharge personneAChargePersistant : personneAChargePersistantList) {

			personneAChargeBeanList
					.add(personneAChargePersistantToPersonneAChargeBean(personneAChargePersistant));
		}

		return personneAChargeBeanList;

	}

	public PersonneAChargeBean personneAChargePersistantToPersonneAChargeBean(
			PersonneACharge personneAChargePersistant) throws Exception {

		PersonneAChargeBean personneAChargeBean = new PersonneAChargeBean();

		personneAChargeBean.setId(personneAChargePersistant.getId());
		personneAChargeBean.setIdSalarie(personneAChargePersistant.getSalarie()
				.getId());
		personneAChargeBean.setNom(personneAChargePersistant.getNom());
		personneAChargeBean.setPrenom(personneAChargePersistant.getPrenom());
		personneAChargeBean.setDateNaissance(personneAChargePersistant
				.getDateNaissance());
		personneAChargeBean.setLienParente(personneAChargePersistant
				.getLienParente());

		return personneAChargeBean;
	}

	public List<PersonneACharge> personneAChargeBeanListToPersonneAChargePersistantList(
			SalarieBean salarieBean) throws Exception {

		List<PersonneACharge> personneAChargeList = new ArrayList<PersonneACharge>();

		for (PersonneAChargeBean personneAChargeBean : getPersonneAChargeBeanListByIdSalarie(salarieBean
				.getId())) {

			personneAChargeList
					.add(personneAChargeBeanToPersonneAChargePersistant(
							personneAChargeBean, salarieBean.getId(), true));
		}

		return personneAChargeList;

	}

	public PersonneACharge personneAChargeBeanToPersonneAChargePersistant(
			PersonneAChargeBean personneAChargeBean, int idSalarie, boolean closeSession)
			throws Exception {

		Session session = null;
		PersonneACharge personneAChargePersistant = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			personneAChargePersistant = new PersonneACharge();
	
			if (personneAChargeBean.getId() == -1
					|| personneAChargeBean.getId() == 0) {
				personneAChargePersistant.setId(null);
			} else {
				personneAChargePersistant.setId(personneAChargeBean.getId());
			}
			Salarie salarie = (Salarie) session.load(Salarie.class, idSalarie);
			personneAChargePersistant.setSalarie(salarie);
			personneAChargePersistant.setNom(personneAChargeBean.getNom());
			personneAChargePersistant.setPrenom(personneAChargeBean.getPrenom());
			personneAChargePersistant.setDateNaissance(personneAChargeBean
					.getDateNaissance());
			personneAChargePersistant.setLienParente(personneAChargeBean
					.getLienParente());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} finally {
			if (closeSession && session != null && session.isOpen()) {
				session.close();
			}
		}
		return personneAChargePersistant;
	}

	public void delete(PersonneAChargeBean personneAChargeBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			PersonneACharge personneAChargePersistant = new PersonneACharge();
			transaction = session.beginTransaction();
			if (personneAChargeBean.getId() != 0) {
				personneAChargePersistant = (PersonneACharge) session.load(
						PersonneACharge.class, personneAChargeBean.getId());

				personneAChargePersistant.setId(personneAChargeBean.getId());
			}
			session.delete(personneAChargePersistant);
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

	public void deleteersonneAChargeWithoutTransaction(
			PersonneAChargeBean personneAChargeBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			PersonneACharge personneAChargePersistant = new PersonneACharge();
			if (personneAChargeBean.getId() != 0) {
				personneAChargePersistant = (PersonneACharge) session.load(
						PersonneACharge.class, personneAChargeBean.getId());

				personneAChargePersistant.setId(personneAChargeBean.getId());
			}
			session.delete(personneAChargePersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}
}
