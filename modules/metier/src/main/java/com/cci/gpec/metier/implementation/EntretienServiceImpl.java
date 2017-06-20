package com.cci.gpec.metier.implementation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.EntretienBeanExport;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.db.Entretien;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.connection.HibernateUtil;

public class EntretienServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EntretienServiceImpl.class);

	/**
	 * Retourne la liste des Entretiens.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<EntretienBean> getEntretiensList(int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<EntretienBean> entretiensInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Entretien> entretiensInventory = session
					.createQuery(
							"from Entretien as e left join fetch e.Salarie as s left join fetch s.Entreprise as ent where ent.Groupe="
									+ idGroupe).list();

			entretiensInventoryBean = entretienPersistantListToEntretienBeanList(entretiensInventory);
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
		return entretiensInventoryBean;
	}

	public List<EntretienBean> getEntretiensListOrderByAnneeNomEntrepriseNomSalarie(
			int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<EntretienBean> entretiensInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Entretien> entretiensInventory = session
					.createQuery(
							"from Entretien as ent left join fetch ent.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " order by ent.AnneeReference desc, e.NomEntreprise, s.Nom")
					.list();

			entretiensInventoryBean = entretienPersistantListToEntretienBeanList(entretiensInventory);
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
		return entretiensInventoryBean;
	}

	public List<EntretienBean> getEntretiensListOrderByAnneeNomSalarie(
			int idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<EntretienBean> entretiensInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Entretien> entretiensInventory = session.createQuery(
					"from Entretien as ent left join fetch ent.Salarie as s where s.Entreprise= "
							+ idEntreprise
							+ " order by ent.AnneeReference desc, s.Nom")
					.list();

			entretiensInventoryBean = entretienPersistantListToEntretienBeanList(entretiensInventory);
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
		return entretiensInventoryBean;
	}

	public List<EntretienBean> getEntretiensListByIdSalarie(int id)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<EntretienBean> entretiensInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Entretien> entretiensInventory = session.createQuery(
					"from Entretien where Salarie=" + id).list();

			entretiensInventoryBean = entretienPersistantListToEntretienBeanList(entretiensInventory);
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
		return entretiensInventoryBean;
	}

	public EntretienBeanExport getEntretienBeanExportById(Integer idEntretien)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		EntretienBeanExport entretienBeanExport = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entretien entretienPersistant = new Entretien();
			transaction = session.beginTransaction();

			entretienPersistant = (Entretien) session.load(Entretien.class,
					idEntretien);

			entretienBeanExport = entretienPersistantToEntretienBeanExport(entretienPersistant);

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
		return entretienBeanExport;
	}

	public List<EntretienBean> getEntretienBeanListByIdSalarie(int salarie)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<EntretienBean> entretienInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Entretien> entretienInventory = session.createQuery(
					"from Entretien where Salarie=" + salarie).list();

			entretienInventoryBean = entretienPersistantListToEntretienBeanList(entretienInventory);
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
		return entretienInventoryBean;
	}

	public List<EntretienBean> getEntretienBeanListByIdSalarieOrderByAnnee(
			int salarie) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<EntretienBean> entretienInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Entretien> entretienInventory = session
					.createQuery(
							"from Entretien as e left join fetch e.IdSalarie as s left join fetch s.Entreprise as ent where e.Salarie="
									+ salarie
									+ " order by ent.NomEntreprise, s.Nom, e.AnneeReference desc")
					.list();

			entretienInventoryBean = entretienPersistantListToEntretienBeanList(entretienInventory);
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
		return entretienInventoryBean;
	}

	public List<EntretienBean> getEntretienBeanListByIdSalarieAndAnnee(
			int idSalarie, int annee) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<EntretienBean> entretienInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Entretien> entretienInventory = session.createQuery(
					"from Entretien where AnneeReference=" + annee
							+ "and Salarie=" + idSalarie).list();

			entretienInventoryBean = entretienPersistantListToEntretienBeanList(entretienInventory);
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
		return entretienInventoryBean;
	}

	public String getJustificatif(Integer idEntretien) throws Exception {
		Session session = null;
		Transaction transaction = null;
		String justificatif = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entretien entretienPersistant = new Entretien();
			transaction = session.beginTransaction();
			entretienPersistant = (Entretien) session.load(Entretien.class,
					idEntretien);
			EntretienBean entretienBean = entretienPersistantToEntretienBean(entretienPersistant);
			transaction.commit();
			justificatif = entretienBean.getJustificatif();
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

	public EntretienBeanExport entretienPersistantToEntretienBeanExport(
			Entretien entretienPersistant) throws Exception {

		EntretienBeanExport entretienBeanExport = new EntretienBeanExport();

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		entretienBeanExport.setAnneeReference(entretienPersistant
				.getAnneeReference());
		entretienBeanExport.setCompetence(entretienPersistant.getCompetence());
		entretienBeanExport.setEvaluationGlobale(entretienPersistant
				.getEvaluationGlobale());
		entretienBeanExport.setCrSigne(entretienPersistant.getCrSigne());
		entretienBeanExport.setDateEntretien(formatter
				.format(entretienPersistant.getDateEntretien()));
		entretienBeanExport.setPrincipaleConclusion(entretienPersistant
				.getPrincipaleConclusion());
		entretienBeanExport.setSouhait(entretienPersistant.getSouhait());
		entretienBeanExport.setIdSalarie(entretienPersistant.getSalarie()
				.getId());
		entretienBeanExport.setFormations(entretienPersistant.getFormations());
		entretienBeanExport
				.setFormations2(entretienPersistant.getFormations2());
		entretienBeanExport
				.setFormations3(entretienPersistant.getFormations3());
		entretienBeanExport
				.setFormations4(entretienPersistant.getFormations4());
		entretienBeanExport
				.setFormations5(entretienPersistant.getFormations5());
		entretienBeanExport.setBilanDessous(entretienPersistant
				.getBilanDessous());
		entretienBeanExport
				.setBilanDessus(entretienPersistant.getBilanDessus());
		entretienBeanExport.setBilanEgal(entretienPersistant.getBilanEgal());
		entretienBeanExport.setCommentaireBilan(entretienPersistant
				.getCommentaireBilan());
		entretienBeanExport.setCommentaireFormation(entretienPersistant
				.getCommentaireFormation());

		entretienBeanExport.setEvolutionPerso(entretienPersistant
				.getEvolutionPerso());
		entretienBeanExport.setFormationNMoinsUn(entretienPersistant
				.getFormationNMoinsUn());
		entretienBeanExport
				.setModifProfil(entretienPersistant.getModifProfil());
		entretienBeanExport.setNomManager(entretienPersistant.getNomManager());
		entretienBeanExport
				.setObjCriteres(entretienPersistant.getObjCriteres());
		entretienBeanExport.setObjDelais(entretienPersistant.getObjDelais());
		entretienBeanExport.setObjMoyens(entretienPersistant.getObjMoyens());
		entretienBeanExport
				.setObjIntitule(entretienPersistant.getObjIntitule());
		entretienBeanExport.setServiceManager(entretienPersistant
				.getServiceManager());
		entretienBeanExport.setSynthese(entretienPersistant.getSynthese());
		entretienBeanExport.setObservations(entretienPersistant
				.getObservations());

		return entretienBeanExport;
	}

	public void saveOrUppdate(EntretienBean entretienBean) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entretien entretienPersistant = new Entretien();
			transaction = session.beginTransaction();
			entretienPersistant = entretienBeanToEntretienPersistant(
					entretienBean, entretienBean.getIdSalarie());
			session.saveOrUpdate(entretienPersistant);

			entretienBean.setId(entretienPersistant.getId());

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

	public void save(EntretienBean entretienBean) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entretien entretienPersistant = new Entretien();
			entretienPersistant = entretienBeanToEntretienPersistant(
					entretienBean, entretienBean.getIdSalarie());
			session.save(entretienPersistant);
			entretienBean.setId(entretienPersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void deleteEntretien(EntretienBean entretienBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entretien entretienPersistant = new Entretien();
			transaction = session.beginTransaction();
			if (entretienBean.getId() != 0) {
				entretienPersistant = (Entretien) session.load(Entretien.class,
						entretienBean.getId());

				entretienPersistant.setId(entretienBean.getId());
			}
			session.delete(entretienPersistant);
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

	public void deleteEntretienWithoutTransaction(EntretienBean entretienBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entretien entretienPersistant = new Entretien();
			if (entretienBean.getId() != 0) {
				entretienPersistant = (Entretien) session.load(Entretien.class,
						entretienBean.getId());

				entretienPersistant.setId(entretienBean.getId());
			}

			session.delete(entretienPersistant);
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public EntretienBean getEntretienBeanById(Integer idEntretien)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		EntretienBean entretienBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entretien entretienPersistant = new Entretien();
			transaction = session.beginTransaction();

			entretienPersistant = (Entretien) session.load(Entretien.class,
					idEntretien);

			entretienBean = entretienPersistantToEntretienBean(entretienPersistant);

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
		return entretienBean;
	}

	public List<EntretienBean> entretienPersistantListToEntretienBeanList(
			List<Entretien> entretienPersistantList) throws Exception {

		List<EntretienBean> entretienBeanList = new ArrayList<EntretienBean>();

		for (Entretien entretienPersistant : entretienPersistantList) {

			entretienBeanList
					.add(entretienPersistantToEntretienBean(entretienPersistant));
		}

		return entretienBeanList;

	}

	public EntretienBean entretienPersistantToEntretienBean(
			Entretien entretienPersistant) throws Exception {

		EntretienBean entretienBean = new EntretienBean();
		entretienBean.setId(entretienPersistant.getId());
		entretienBean
				.setAnneeReference(entretienPersistant.getAnneeReference());
		entretienBean.setCompetence(entretienPersistant.getCompetence());
		entretienBean.setEvaluationGlobale(entretienPersistant
				.getEvaluationGlobale());
		entretienBean.setCrSigne(entretienPersistant.getCrSigne());
		entretienBean.setDateEntretien(entretienPersistant.getDateEntretien());
		entretienBean.setPrincipaleConclusion(entretienPersistant
				.getPrincipaleConclusion());
		entretienBean.setSouhait(entretienPersistant.getSouhait());
		entretienBean.setIdSalarie(entretienPersistant.getSalarie().getId());
		entretienBean.setFormations(entretienPersistant.getFormations());
		entretienBean.setFormations2(entretienPersistant.getFormations2());
		entretienBean.setFormations3(entretienPersistant.getFormations3());
		entretienBean.setFormations4(entretienPersistant.getFormations4());
		entretienBean.setFormations5(entretienPersistant.getFormations5());
		entretienBean.setBilanDessous(entretienPersistant.getBilanDessous());
		entretienBean.setBilanDessus(entretienPersistant.getBilanDessus());
		entretienBean.setBilanEgal(entretienPersistant.getBilanEgal());
		entretienBean.setCommentaireBilan(entretienPersistant
				.getCommentaireBilan());
		entretienBean.setCommentaireFormation(entretienPersistant
				.getCommentaireFormation());
		entretienBean.setDomainesFormation(entretienPersistant
				.getDomainesFormation());
		entretienBean.setDomainesFormation2(entretienPersistant
				.getDomainesFormation2());
		entretienBean.setDomainesFormation3(entretienPersistant
				.getDomainesFormation3());
		entretienBean.setDomainesFormation4(entretienPersistant
				.getDomainesFormation4());
		entretienBean.setDomainesFormation5(entretienPersistant
				.getDomainesFormation5());
		entretienBean
				.setEvolutionPerso(entretienPersistant.getEvolutionPerso());
		entretienBean.setFormationNMoinsUn(entretienPersistant
				.getFormationNMoinsUn());
		entretienBean.setModifProfil(entretienPersistant.getModifProfil());
		entretienBean.setNomManager(entretienPersistant.getNomManager());
		entretienBean.setObjCriteres(entretienPersistant.getObjCriteres());
		entretienBean.setObjDelais(entretienPersistant.getObjDelais());
		entretienBean.setObjMoyens(entretienPersistant.getObjMoyens());
		entretienBean.setObjIntitule(entretienPersistant.getObjIntitule());
		entretienBean
				.setServiceManager(entretienPersistant.getServiceManager());
		entretienBean.setSynthese(entretienPersistant.getSynthese());
		entretienBean.setObservations(entretienPersistant.getObservations());
		if (entretienPersistant.getJustificatif() != null
				&& entretienPersistant.getJustificatif().equals("")) {
			entretienBean.setJustificatif(null);
		} else {
			entretienBean
					.setJustificatif(entretienPersistant.getJustificatif());
		}

		return entretienBean;
	}

	public List<Entretien> entretienBeanListToEntretienPersistantList(
			SalarieBean salarieBean) throws Exception {

		List<Entretien> entretienList = new ArrayList<Entretien>();

		for (EntretienBean entretienBean : salarieBean.getEntretienBeanList()) {

			entretienList.add(entretienBeanToEntretienPersistant(entretienBean,
					salarieBean.getId()));
		}

		return entretienList;

	}

	public Entretien entretienBeanToEntretienPersistant(
			EntretienBean entretienBean, int idSalarie) throws Exception {
		Session session = null;
		Entretien entretien = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			entretien = new Entretien();
	
			if (entretienBean.getId() == -1) {
				entretien.setId(null);
			} else {
				entretien.setId(entretienBean.getId());
			}
	
			Salarie salarie = (Salarie) session.load(Salarie.class, idSalarie);
		
			entretien.setSalarie(salarie);
			entretien.setAnneeReference(entretienBean.getAnneeReference());
			entretien.setCompetence(entretienBean.getCompetence());
			entretien.setEvaluationGlobale(entretienBean.getEvaluationGlobale());
			entretien.setCrSigne(entretienBean.getCrSigne());
			entretien.setDateEntretien(entretienBean.getDateEntretien());
			entretien.setPrincipaleConclusion(entretienBean
					.getPrincipaleConclusion());
			entretien.setSouhait(entretienBean.getSouhait());
			entretien.setFormations(entretienBean.getFormations());
			entretien.setFormations2(entretienBean.getFormations2());
			entretien.setFormations3(entretienBean.getFormations3());
			entretien.setFormations4(entretienBean.getFormations4());
			entretien.setFormations5(entretienBean.getFormations5());
			entretien.setBilanDessous(entretienBean.getBilanDessous());
			entretien.setBilanDessus(entretienBean.getBilanDessus());
			entretien.setBilanEgal(entretienBean.getBilanEgal());
			entretien.setCommentaireBilan(entretienBean.getCommentaireBilan());
			entretien.setCommentaireFormation(entretienBean
					.getCommentaireFormation());
			entretien.setDomainesFormation(entretienBean.getDomainesFormation());
			entretien.setDomainesFormation2(entretienBean.getDomainesFormation2());
			entretien.setDomainesFormation3(entretienBean.getDomainesFormation3());
			entretien.setDomainesFormation4(entretienBean.getDomainesFormation4());
			entretien.setDomainesFormation5(entretienBean.getDomainesFormation5());
			entretien.setEvolutionPerso(entretienBean.getEvolutionPerso());
			entretien.setFormationNMoinsUn(entretienBean.getFormationNMoinsUn());
			entretien.setModifProfil(entretienBean.getModifProfil());
			entretien.setNomManager(entretienBean.getNomManager());
			entretien.setObjCriteres(entretienBean.getObjCriteres());
			entretien.setObjMoyens(entretienBean.getObjMoyens());
			entretien.setObjDelais(entretienBean.getObjDelais());
			entretien.setObjIntitule(entretienBean.getObjIntitule());
			entretien.setObservations(entretienBean.getObservations());
			entretien.setServiceManager(entretienBean.getServiceManager());
			entretien.setSynthese(entretienBean.getSynthese());
			entretien.setJustificatif(entretienBean.getJustificatif());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
		return entretien;
	}
}
