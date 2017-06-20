package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.db.Absence;
import com.cci.gpec.db.Domaineformation;
import com.cci.gpec.db.Formation;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.connection.HibernateUtil;

public class FormationServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FormationServiceImpl.class);

	/**
	 * Retourne la liste des Formations.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */

	public List<FormationBean> getFormationsList(int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<FormationBean> formationsInventoryBean;
			transaction = session.beginTransaction();

			List<Formation> formationsInventory = session
					.createQuery(
							"from Formation f left join fetch f.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe).list();

			formationsInventoryBean = formationPersistantListToFormationBeanList(formationsInventory);
			transaction.commit();

			return formationsInventoryBean;

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

	public List<FormationBean> getFormationsListOrderByAnneeNomEntrepriseNomSalarie(
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<FormationBean> formationsInventoryBean;
			transaction = session.beginTransaction();

			List<Formation> formationsInventory = session
					.createQuery(
							"from Formation as f left join fetch f.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " order by year(f.DebutFormation) desc, e.NomEntreprise, s.Nom")
					.list();

			formationsInventoryBean = formationPersistantListToFormationBeanList(formationsInventory);
			transaction.commit();

			return formationsInventoryBean;

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

	public List<FormationBean> getFormationsListOrderByAnneeNomSalarie(
			int idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<FormationBean> formationsInventoryBean;
			transaction = session.beginTransaction();

			List<Formation> formationsInventory = session.createQuery(
					"from Formation as f left join fetch f.Salarie as s where s.Entreprise = "
							+ idEntreprise
							+ " order by year(f.DebutFormation) desc, s.Nom")
					.list();

			formationsInventoryBean = formationPersistantListToFormationBeanList(formationsInventory);
			transaction.commit();

			return formationsInventoryBean;

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

	public List<FormationBean> getFormationBeanListByIdSalarie(int salarie)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<FormationBean> formationInventoryBean;
			transaction = session.beginTransaction();

			List<Formation> formationInventory = session.createQuery(
					"from Formation where Salarie=" + salarie).list();

			formationInventoryBean = formationPersistantListToFormationBeanList(formationInventory);
			transaction.commit();

			return formationInventoryBean;

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

	public String getJustificatif(Integer idFormation) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Formation formationPersistant = new Formation();
			transaction = session.beginTransaction();
			formationPersistant = (Formation) session.load(Formation.class,
					idFormation);
			FormationBean formationBean = formationPersistantToFormationBean(formationPersistant);
			transaction.commit();
			return formationBean.getJustificatif();

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

	public List<FormationBean> getFormationsOfSalarieList(int idSalarie)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<FormationBean> formationsInventoryBean;
			transaction = session.beginTransaction();

			List<Formation> formationsInventory = session.createQuery(
					"from Formation where Salarie=" + idSalarie).list();

			formationsInventoryBean = formationPersistantListToFormationBeanList(formationsInventory);
			transaction.commit();

			return formationsInventoryBean;

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

	public List<FormationBean> getFormationBeanByIdType(int idDomaine,
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<FormationBean> formationsInventoryBean;
			transaction = session.beginTransaction();

			List<Formation> formationsInventory = session
					.createQuery(
							"from Formation as f left join fetch f.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " and DomaineFormation="
									+ idDomaine).list();

			formationsInventoryBean = formationPersistantListToFormationBeanList(formationsInventory);
			transaction.commit();

			return formationsInventoryBean;

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

	public Formation getFormationByIdAbsence(int idAbsence, boolean closeSession) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Formation formation;
			transaction = session.beginTransaction();

			formation = (Formation) session.createQuery(
					"from Formation where Absence=" + idAbsence).uniqueResult();

			return formation;

		} catch (HibernateException e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} finally {
			if (closeSession && session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public AbsenceBean genereAbsenceAutomatique(FormationBean formation)
			throws Exception {
		AbsenceBean abs = new AbsenceBean();
		AbsenceServiceImpl absServ = new AbsenceServiceImpl();

		if (formation.getIdAbsence() != 0 && formation.getIdAbsence() != -1) {
			abs.setId(formation.getIdAbsence());
		} else {
			abs.setId(-1);
		}
		abs.setDebutAbsence(formation.getDebutFormation());
		abs.setFinAbsence(formation.getFinFormation());
		abs.setNombreJourOuvre(formation.getNombreJourOuvre());
		abs.setAuto(true);
		abs.setIdSalarie(formation.getIdSalarie());

		abs.setIdTypeAbsenceSelected(formation.getIdTypeAbsenceGenere());

		TypeAbsenceServiceImpl typeAbsenceService = new TypeAbsenceServiceImpl();
		String nomTypeAbsence = new String();
		try {
			nomTypeAbsence = typeAbsenceService.getTypeAbsenceBeanById(
					formation.getIdTypeAbsenceGenere()).getNom();
		} catch (Exception e) {
			e.printStackTrace();
		}

		abs.setNomTypeAbsence(nomTypeAbsence);

		absServ.saveOrUppdate(abs);

		formation.setIdAbsence(abs.getId());

		return abs;
	}

	public void saveOrUppdate(FormationBean formationBean) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Formation formationPersistant = new Formation();
			transaction = session.beginTransaction();
			formationPersistant = formationBeanToFormationPersistant(
					formationBean, formationBean.getIdSalarie());
			session.saveOrUpdate(formationPersistant);
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

	public void save(FormationBean formationBean) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Formation formationPersistant = new Formation();
			formationPersistant = formationBeanToFormationPersistant(
					formationBean, formationBean.getIdSalarie());
			session.save(formationPersistant);
			formationBean.setId(formationPersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void deleteFormation(FormationBean formationBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Formation formationPersistant = new Formation();
			transaction = session.beginTransaction();
			if (formationBean.getId() != 0) {
				formationPersistant = (Formation) session.load(Formation.class,
						formationBean.getId());

				formationPersistant.setId(formationBean.getId());
			}

			session.delete(formationPersistant);
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

	public void deleteFormationWithoutTransaction(FormationBean formationBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Formation formationPersistant = new Formation();
			if (formationBean.getId() != 0) {
				formationPersistant = (Formation) session.load(Formation.class,
						formationBean.getId());

				formationPersistant.setId(formationBean.getId());
			}

			session.delete(formationPersistant);
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public FormationBean getFormationBeanById(Integer idFormation)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Formation formationPersistant = new Formation();
			transaction = session.beginTransaction();

			formationPersistant = (Formation) session.load(Formation.class,
					idFormation);

			FormationBean formationBean = formationPersistantToFormationBean(formationPersistant);

			transaction.commit();

			return formationBean;

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

	public List<FormationBean> formationPersistantListToFormationBeanList(
			List<Formation> formationPersistantList) throws Exception {

		List<FormationBean> formationBeanList = new ArrayList<FormationBean>();

		for (Formation formationPersistant : formationPersistantList) {

			formationBeanList
					.add(formationPersistantToFormationBean(formationPersistant));
		}

		return formationBeanList;

	}

	public FormationBean formationPersistantToFormationBean(
			Formation formationPersistant) throws Exception {

		FormationBean formationBean = new FormationBean();
		formationBean.setId(formationPersistant.getId());
		formationBean
				.setDebutFormation(formationPersistant.getDebutFormation());
		formationBean.setDif(formationPersistant.getDif());
		formationBean.setHorsDif(formationPersistant.getHorsDif());
		formationBean.setFinFormation(formationPersistant.getFinFormation());
		formationBean.setMode(formationPersistant.getMode());
		formationBean.setNomFormation(formationPersistant.getNomFormation());
		formationBean.setOrganismeFormation(formationPersistant
				.getOrganismeFormatino());
		formationBean.setIdDomaineFormationBeanSelected(formationPersistant
				.getDomaineFormation().getId());
		formationBean.setVolumeHoraire(formationPersistant.getVolumeHoraire());
		formationBean.setNombreJourOuvre(formationPersistant
				.getNombreJourOuvre());
		formationBean.setNomDomaineFormation(formationPersistant
				.getDomaineFormation().getNomDomaineFormation());
		formationBean.setIdSalarie(formationPersistant.getSalarie().getId());
		if (formationPersistant.getJustificatif() != null
				&& formationPersistant.getJustificatif().equals("")) {
			formationBean.setJustificatif(null);
		} else {
			formationBean
					.setJustificatif(formationPersistant.getJustificatif());
		}

		if (formationPersistant.getAbsence() != null
				&& formationPersistant.getId() != null) {
			formationBean
					.setIdAbsence(formationPersistant.getAbsence().getId());
		} else {
			formationBean.setIdAbsence(-1);
		}

		formationBean.setCoutOpca(formationPersistant.getCoutOpca());
		formationBean
				.setCoutEntreprise(formationPersistant.getCoutEntreprise());
		formationBean.setCoutAutre(formationPersistant.getCoutAutre());

		return formationBean;
	}

	public List<Formation> formationBeanListToFormationPersistantList(
			SalarieBean salarieBean,
			HashSet<Absence> listeAbsenceCompleteSalarie) throws Exception {

		List<Formation> formationList = new ArrayList<Formation>();

		for (FormationBean formationBean : salarieBean.getFormationBeanList()) {

			formationList.add(formationBeanToFormationPersistant(formationBean,
					salarieBean.getId(), listeAbsenceCompleteSalarie));
		}

		return formationList;
	}

	public Formation formationBeanToFormationPersistant(
			FormationBean formationBean, int idSalarie) throws Exception {
		Session session = null;
		Formation formationPersistant = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			formationPersistant = new Formation();
	
			Salarie salarie = (Salarie) session.load(Salarie.class, idSalarie);
			if (formationBean.getId() == -1) {
				formationPersistant.setId(null);
			} else {
				formationPersistant.setId(formationBean.getId());
			}
	
			if (formationBean.getIdAbsence() != -1
					&& formationBean.getIdAbsence() != 0) {
				Absence absencePersistant = new Absence();
				absencePersistant = (Absence) session.load(Absence.class,
						formationBean.getIdAbsence());
				formationPersistant.setAbsence(absencePersistant);
			}
			formationPersistant.setSalarie(salarie);
			formationPersistant
					.setDebutFormation(formationBean.getDebutFormation());
			formationPersistant.setFinFormation(formationBean.getFinFormation());
			formationPersistant.setDif(formationBean.getDif());
			formationPersistant.setHorsDif(formationBean.getHorsDif());
			formationPersistant.setMode(formationBean.getMode());
			formationPersistant.setNomFormation(formationBean.getNomFormation());
			formationPersistant.setOrganismeFormatino(formationBean
					.getOrganismeFormation());
			formationPersistant.setVolumeHoraire(formationBean.getVolumeHoraire());
			formationPersistant.setNombreJourOuvre(formationBean
					.getNombreJourOuvre());
			formationPersistant.setJustificatif(formationBean.getJustificatif());
			formationPersistant.setCoutOpca(formationBean.getCoutOpca());
			formationPersistant
					.setCoutEntreprise(formationBean.getCoutEntreprise());
			formationPersistant.setCoutAutre(formationBean.getCoutAutre());
	
			Domaineformation domaineformation = new Domaineformation();
	
			domaineformation = (Domaineformation) session.load(
					Domaineformation.class,
					formationBean.getIdDomaineFormationBeanSelected());
	
			formationPersistant.setDomaineFormation(domaineformation);
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
		return formationPersistant;
	}

	public Formation formationBeanToFormationPersistant(
			FormationBean formationBean, int idSalarie,
			HashSet<Absence> listeAbsenceCompleteSalarie) throws Exception {

		Session session = null;
		Formation formationPersistant = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			formationPersistant = new Formation();
	
			Salarie salarie = (Salarie) session.load(Salarie.class, idSalarie);
			if (formationBean.getId() == -1) {
				formationPersistant.setId(null);
			} else {
				formationPersistant.setId(formationBean.getId());
			}
	
			if (formationBean.getIdAbsence() != -1
					&& formationBean.getIdAbsence() != 0) {
				Absence absencePersistant = new Absence();
				absencePersistant = (Absence) session.load(Absence.class,
						formationBean.getIdAbsence());
				formationPersistant.setAbsence(absencePersistant);
			}
	
			formationPersistant.setSalarie(salarie);
	
			formationPersistant
					.setDebutFormation(formationBean.getDebutFormation());
			formationPersistant.setFinFormation(formationBean.getFinFormation());
			formationPersistant.setDif(formationBean.getDif());
			formationPersistant.setHorsDif(formationBean.getHorsDif());
			formationPersistant.setMode(formationBean.getMode());
			formationPersistant.setNomFormation(formationBean.getNomFormation());
			formationPersistant.setOrganismeFormatino(formationBean
					.getOrganismeFormation());
			formationPersistant.setVolumeHoraire(formationBean.getVolumeHoraire());
			formationPersistant.setNombreJourOuvre(formationBean
					.getNombreJourOuvre());
			formationPersistant.setJustificatif(formationBean.getJustificatif());
			formationPersistant.setCoutOpca(formationBean.getCoutOpca());
			formationPersistant
					.setCoutEntreprise(formationBean.getCoutEntreprise());
			formationPersistant.setCoutAutre(formationBean.getCoutAutre());
	
			Domaineformation domaineformation = new Domaineformation();
	
			domaineformation = (Domaineformation) session.load(
					Domaineformation.class,
					formationBean.getIdDomaineFormationBeanSelected());
	
			formationPersistant.setDomaineFormation(domaineformation);
		
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
		return formationPersistant;
	}

}
