package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.db.Metier;
import com.cci.gpec.db.Parcours;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.Statut;
import com.cci.gpec.db.Typecontrat;
import com.cci.gpec.db.Typerecours;
import com.cci.gpec.db.connection.HibernateUtil;

public class ParcoursServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParcoursServiceImpl.class);

	/**
	 * Retourne la liste des Parcours.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<ParcoursBean> getParcourssList(int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<ParcoursBean> parcourssInventoryBean;
			transaction = session.beginTransaction();

			List<Parcours> parcourssInventory = session
					.createQuery(
							"from Parcours as p left join fetch p.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe).list();

			parcourssInventoryBean = parcoursPersistantListToParcoursBeanList(parcourssInventory);
			transaction.commit();

			return parcourssInventoryBean;

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

	public List<ParcoursBean> getParcoursListOrderByNomEntrepriseNomSalarie(
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<ParcoursBean> parcourssInventoryBean;
			transaction = session.beginTransaction();

			List<Parcours> parcourssInventory = session
					.createQuery(
							"from Parcours as p left join fetch p.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " order by e.NomEntreprise, s.Nom")
					.list();

			parcourssInventoryBean = parcoursPersistantListToParcoursBeanList(parcourssInventory);
			transaction.commit();

			return parcourssInventoryBean;

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

	public List<ParcoursBean> getParcoursListOrderByNomSalarie(int idEntreprise)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<ParcoursBean> parcourssInventoryBean;
			transaction = session.beginTransaction();

			List<Parcours> parcourssInventory = session.createQuery(
					"from Parcours as p left join fetch p.Salarie as s where s.Entreprise = "
							+ idEntreprise + " order by s.Nom").list();

			parcourssInventoryBean = parcoursPersistantListToParcoursBeanList(parcourssInventory);
			transaction.commit();

			return parcourssInventoryBean;

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

	public String getJustificatif(Integer idParcours) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Parcours parcoursPersistant = new Parcours();
			transaction = session.beginTransaction();
			parcoursPersistant = (Parcours) session.load(Parcours.class,
					idParcours);
			ParcoursBean parcoursBean = parcoursPersistantToParcoursBean(parcoursPersistant);
			transaction.commit();
			return parcoursBean.getJustificatif();

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

	public void saveOrUppdate(ParcoursBean parcoursBean) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Parcours parcoursPersistant = new Parcours();
			transaction = session.beginTransaction();
			parcoursPersistant = parcoursBeanToParcoursPersistant(parcoursBean,
					parcoursBean.getIdSalarie());
			session.saveOrUpdate(parcoursPersistant);
			parcoursBean.setId(parcoursPersistant.getId());
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

	public void save(ParcoursBean parcoursBean) throws Exception {
		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Parcours parcoursPersistant = new Parcours();
			// transaction = session.beginTransaction();
			parcoursPersistant = parcoursBeanToParcoursPersistant(parcoursBean,
					parcoursBean.getIdSalarie());
			session.save(parcoursPersistant);
			// transaction.commit();
			parcoursBean.setId(parcoursPersistant.getId());
		} catch (HibernateException e) {
			// if (transaction != null && !transaction.wasRolledBack()) {
			//	transaction.rollback();
			// }
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void deleteParcours(ParcoursBean parcoursBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Parcours parcoursPersistant = new Parcours();
			transaction = session.beginTransaction();
			if (parcoursBean.getId() != 0) {
				parcoursPersistant = (Parcours) session.load(Parcours.class,
						parcoursBean.getId());

				parcoursPersistant.setId(parcoursBean.getId());
			}

			session.delete(parcoursPersistant);
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

	public void deleteParcoursWithoutTransaction(ParcoursBean parcoursBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Parcours parcoursPersistant = new Parcours();
			if (parcoursBean.getId() != 0) {
				parcoursPersistant = (Parcours) session.load(Parcours.class,
						parcoursBean.getId());

				parcoursPersistant.setId(parcoursBean.getId());
			}

			session.delete(parcoursPersistant);
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public ParcoursBean getParcoursBeanById(Integer idParcours)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Parcours parcoursPersistant = new Parcours();
			transaction = session.beginTransaction();

			parcoursPersistant = (Parcours) session.load(Parcours.class,
					idParcours);

			ParcoursBean parcoursBean = parcoursPersistantToParcoursBean(parcoursPersistant);

			transaction.commit();

			return parcoursBean;

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

	public List<ParcoursBean> parcoursPersistantListToParcoursBeanList(
			List<Parcours> parcoursPersistantList) throws Exception {

		List<ParcoursBean> parcoursBeanList = new ArrayList<ParcoursBean>();

		for (Parcours parcoursPersistant : parcoursPersistantList) {

			parcoursBeanList
					.add(parcoursPersistantToParcoursBean(parcoursPersistant));
		}

		return parcoursBeanList;

	}

	public ParcoursBean parcoursPersistantToParcoursBean(
			Parcours parcoursPersistant) throws Exception {

		ParcoursBean parcoursBean = new ParcoursBean();
		parcoursBean.setId(parcoursPersistant.getId());
		parcoursBean.setDebutFonction(parcoursPersistant.getDebutFonction());

		parcoursBean.setEquivalenceTempsPlein(parcoursPersistant
				.getEquivalenceTempsPlein());
		/** reprise données entreprise 1.8 défaillantes niveau dates de fin * */
		Calendar c = new GregorianCalendar();
		c.set(1923, 2, 5);
		Calendar c2 = new GregorianCalendar();
		c2.set(3800, 1, 1);
		Calendar c3 = new GregorianCalendar();
		c3.set(1922, 2, 5);
		if (parcoursPersistant.getFinFonction() == null
				|| compare(parcoursPersistant.getFinFonction(), c.getTime())
				|| compare(parcoursPersistant.getFinFonction(), c2.getTime())
				|| compare(parcoursPersistant.getFinFonction(), c3.getTime())) {
			parcoursBean.setFinFonction(null);
		} else {
			parcoursBean.setFinFonction(parcoursPersistant.getFinFonction());
		}
		parcoursBean
				.setIdMetierSelected(parcoursPersistant.getMetier().getId());
		parcoursBean.setIdTypeContratSelected(parcoursPersistant
				.getTypeContrat().getId());
		parcoursBean.setIdTypeStatutSelected(parcoursPersistant.getStatut()
				.getId());
		if (parcoursPersistant.getTypeRecours() != null) {
			parcoursBean.setIdTypeRecoursSelected(parcoursPersistant
					.getTypeRecours().getId());
		}
		parcoursBean.setIdSalarie(parcoursPersistant.getSalarie().getId());
		parcoursBean
				.setNomMetier(parcoursPersistant.getMetier().getNomMetier());
		parcoursBean.setNomTypeContrat(parcoursPersistant.getTypeContrat()
				.getNomTypeContrat());
		parcoursBean.setNomTypeStatut(parcoursPersistant.getStatut()
				.getNomStatut());
		if (parcoursPersistant.getJustificatif() != null
				&& parcoursPersistant.getJustificatif().equals("")) {
			parcoursBean.setJustificatif(null);
		} else {
			parcoursBean.setJustificatif(parcoursPersistant.getJustificatif());
		}
		parcoursBean.setEchelon(parcoursPersistant.getEchelon());
		parcoursBean.setNiveau(parcoursPersistant.getNiveau());
		parcoursBean.setCoefficient(parcoursPersistant.getCoefficient());

		return parcoursBean;
	}

	public boolean compare(Date d1, Date d2) {
		Calendar c1 = new GregorianCalendar();
		c1.setTime(d1);
		Calendar c2 = new GregorianCalendar();
		c2.setTime(d2);
		if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
				&& c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
				&& c1.getTime().equals(c2.getTime())) {
			return true;
		} else {
			return false;
		}
	}

	public List<Parcours> parcoursBeanListToParcoursPersistantList(
			SalarieBean salarieBean) throws Exception {

		List<Parcours> parcoursList = new ArrayList<Parcours>();

		for (ParcoursBean parcoursBean : salarieBean.getParcoursBeanList()) {

			parcoursList.add(parcoursBeanToParcoursPersistant(parcoursBean,
					salarieBean.getId()));
		}

		return parcoursList;

	}

	public List<ParcoursBean> getParcoursBeanListByIdSalarie(int salarie)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<ParcoursBean> parcoursInventoryBean;
			transaction = session.beginTransaction();

			List<Parcours> parcoursInventory = session.createQuery(
					"from Parcours where Salarie=" + salarie).list();

			parcoursInventoryBean = parcoursPersistantListToParcoursBeanList(parcoursInventory);
			transaction.commit();

			return parcoursInventoryBean;

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

	public Parcours parcoursBeanToParcoursPersistant(ParcoursBean parcoursBean,
			int idSalarie) throws Exception {

		Session session = null;
		Parcours parcoursPersistant = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			
			parcoursPersistant = new Parcours();
	
			if (parcoursBean.getId() == -1) {
				parcoursPersistant.setId(null);
			} else {
				parcoursPersistant.setId(parcoursBean.getId());
			}
			Salarie salarie = (Salarie) session.load(Salarie.class, idSalarie);
			parcoursPersistant.setSalarie(salarie);
			parcoursPersistant.setDebutFonction(parcoursBean.getDebutFonction());
			parcoursPersistant.setEquivalenceTempsPlein(parcoursBean
					.getEquivalenceTempsPlein());
			parcoursPersistant.setFinFonction(parcoursBean.getFinFonction());
			parcoursPersistant.setJustificatif(parcoursBean.getJustificatif());
			parcoursPersistant.setCoefficient(parcoursBean.getCoefficient());
			parcoursPersistant.setEchelon(parcoursBean.getEchelon());
			parcoursPersistant.setNiveau(parcoursBean.getNiveau());
	
			Metier metier = new Metier();
			metier = (Metier) session.load(Metier.class,
					parcoursBean.getIdMetierSelected());
			parcoursPersistant.setMetier(metier);
	
			Statut statut = new Statut();
			statut = (Statut) session.load(Statut.class,
					parcoursBean.getIdTypeStatutSelected());
			parcoursPersistant.setStatut(statut);
	
			if (parcoursBean.getIdTypeRecoursSelected() != null) {
				Typerecours typeRecours = new Typerecours();
				typeRecours = (Typerecours) session.load(Typerecours.class,
						parcoursBean.getIdTypeRecoursSelected());
				parcoursPersistant.setTypeRecours(typeRecours);
			}
	
			Typecontrat typeContrat = new Typecontrat();
			typeContrat = (Typecontrat) session.load(Typecontrat.class,
					parcoursBean.getIdTypeContratSelected());
			parcoursPersistant.setTypeContrat(typeContrat);
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}

		return parcoursPersistant;
	}
}
