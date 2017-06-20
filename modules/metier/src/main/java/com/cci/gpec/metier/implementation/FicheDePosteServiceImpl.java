package com.cci.gpec.metier.implementation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.FicheDePosteBean;
import com.cci.gpec.commons.FicheDePosteBeanExport;
import com.cci.gpec.db.FicheDePoste;
import com.cci.gpec.db.FicheMetier;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class FicheDePosteServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FicheDePosteServiceImpl.class);
	
	/**
	 * Retourne la liste des Fiche de postes.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<FicheDePosteBean> getFicheDePostesList(int idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<FicheDePosteBean> ficheDePosteInventoryBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<FicheDePoste> ficheDePosteInventory = session
					.createQuery(
							"from FicheDePoste as f left join fetch f.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe).list();

			ficheDePosteInventoryBean = ficheDePostePersistantListToFicheDePosteBeanList(ficheDePosteInventory);
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
		return ficheDePosteInventoryBean;
	}

	private List<FicheDePosteBean> ficheDePostePersistantListToFicheDePosteBeanList(
			List<FicheDePoste> ficheDePostePersistantList) throws Exception {

		List<FicheDePosteBean> ficheDePosteBeanList = new ArrayList<FicheDePosteBean>();

		for (FicheDePoste ficheDePostePersistant : ficheDePostePersistantList) {

			ficheDePosteBeanList
					.add(ficheDePostePersistantToFicheDePosteBean(ficheDePostePersistant));
		}

		return ficheDePosteBeanList;

	}

	private void suppression(FicheDePosteBean ficheDePosteBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheDePoste ficheDePostePersistant = new FicheDePoste();
			transaction = session.beginTransaction();
			if (ficheDePosteBean.getId() != 0) {
				ficheDePostePersistant = (FicheDePoste) session.load(
						FicheDePoste.class, ficheDePosteBean.getId());

				ficheDePostePersistant.setId(ficheDePosteBean.getId());
			}
			session.delete(ficheDePostePersistant);
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

	public void deleteFicheDePosteWithoutTransaction(
			FicheDePosteBean ficheDePosteBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheDePoste ficheDePostePersistant = new FicheDePoste();
			if (ficheDePosteBean.getId() != 0) {
				ficheDePostePersistant = (FicheDePoste) session.load(
						FicheDePoste.class, ficheDePosteBean.getId());

				ficheDePostePersistant.setId(ficheDePosteBean.getId());
			}
			session.delete(ficheDePostePersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void saveOrUppdate(FicheDePosteBean ficheDePosteBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheDePoste ficheDePostePersistant = new FicheDePoste();
			transaction = session.beginTransaction();
			if (ficheDePosteBean.getId() != 0) {
				ficheDePostePersistant = (FicheDePoste) session.load(
						FicheDePoste.class, ficheDePosteBean.getId());

				ficheDePostePersistant.setId(ficheDePosteBean.getId());
			}

			ficheDePostePersistant.setDateCreation(ficheDePosteBean
					.getDateCreation());
			ficheDePostePersistant.setDateModification(ficheDePosteBean
					.getDateModification());
			ficheDePostePersistant.setActivitesSpecifiques(ficheDePosteBean
					.getActivitesSpecifiques());
			ficheDePostePersistant.setCompetencesNouvelles(ficheDePosteBean
					.getCompetencesNouvelles());
			ficheDePostePersistant.setCompetencesNouvelles2(ficheDePosteBean
					.getCompetencesNouvelles2());
			ficheDePostePersistant.setCompetencesNouvelles3(ficheDePosteBean
					.getCompetencesNouvelles3());
			ficheDePostePersistant.setCompetencesNouvelles4(ficheDePosteBean
					.getCompetencesNouvelles4());
			ficheDePostePersistant.setCompetencesNouvelles5(ficheDePosteBean
					.getCompetencesNouvelles5());
			ficheDePostePersistant.setCompetencesSpecifiques(ficheDePosteBean
					.getCompetencesSpecifiques());
			ficheDePostePersistant
					.setCompetencesSpecifiquesTexte(ficheDePosteBean
							.getCompetencesSpecifiquesTexte());
			ficheDePostePersistant.setCommentaires(ficheDePosteBean
					.getCommentaire());
			ficheDePostePersistant.setSavoir(ficheDePosteBean.getSavoir());
			ficheDePostePersistant.setSavoirEtre(ficheDePosteBean
					.getSavoirEtre());
			ficheDePostePersistant.setSavoirFaire(ficheDePosteBean
					.getSavoirFaire());
			ficheDePostePersistant.setEvaluationGlobale(ficheDePosteBean
					.getEvaluationGlobale());
			ficheDePostePersistant.setMobiliteEnvisagee(ficheDePosteBean
					.getMobiliteEnvisagee());
			ficheDePostePersistant.setCategorieCompetence(ficheDePosteBean
					.getCategorieCompetence());
			ficheDePostePersistant.setCategorieCompetence2(ficheDePosteBean
					.getCategorieCompetence2());
			ficheDePostePersistant.setCategorieCompetence3(ficheDePosteBean
					.getCategorieCompetence3());
			ficheDePostePersistant.setCategorieCompetence4(ficheDePosteBean
					.getCategorieCompetence4());
			ficheDePostePersistant.setCategorieCompetence5(ficheDePosteBean
					.getCategorieCompetence5());

			FicheMetier ficheMetierPersistant = (FicheMetier) session.load(
					FicheMetier.class, ficheDePosteBean.getIdFicheMetierType());
			ficheDePostePersistant.setMetierType(ficheMetierPersistant);

			Salarie salariePersistant = (Salarie) session.load(Salarie.class,
					ficheDePosteBean.getIdSalarie());
			ficheDePostePersistant.setSalarie(salariePersistant);
			if (ficheDePosteBean.getJustificatif() != null) {
				ficheDePostePersistant.setJustificatif(ficheDePosteBean
						.getJustificatif());
			} else {
				ficheDePostePersistant.setJustificatif(null);
			}

			session.saveOrUpdate(ficheDePostePersistant);
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

	public void save(FicheDePosteBean ficheDePosteBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheDePoste ficheDePostePersistant = new FicheDePoste();
			ficheDePostePersistant.setId(ficheDePosteBean.getId());
			ficheDePostePersistant.setDateCreation(ficheDePosteBean
					.getDateCreation());
			ficheDePostePersistant.setDateModification(ficheDePosteBean
					.getDateModification());
			ficheDePostePersistant.setActivitesSpecifiques(ficheDePosteBean
					.getActivitesSpecifiques());
			ficheDePostePersistant.setCompetencesNouvelles(ficheDePosteBean
					.getCompetencesNouvelles());
			ficheDePostePersistant.setCompetencesNouvelles2(ficheDePosteBean
					.getCompetencesNouvelles2());
			ficheDePostePersistant.setCompetencesNouvelles3(ficheDePosteBean
					.getCompetencesNouvelles3());
			ficheDePostePersistant.setCompetencesNouvelles4(ficheDePosteBean
					.getCompetencesNouvelles4());
			ficheDePostePersistant.setCompetencesNouvelles5(ficheDePosteBean
					.getCompetencesNouvelles5());
			ficheDePostePersistant.setCompetencesSpecifiques(ficheDePosteBean
					.getCompetencesSpecifiques());
			ficheDePostePersistant
					.setCompetencesSpecifiquesTexte(ficheDePosteBean
							.getCompetencesSpecifiquesTexte());
			ficheDePostePersistant.setCommentaires(ficheDePosteBean
					.getCommentaire());
			ficheDePostePersistant.setSavoir(ficheDePosteBean.getSavoir());
			ficheDePostePersistant.setSavoirEtre(ficheDePosteBean
					.getSavoirEtre());
			ficheDePostePersistant.setSavoirFaire(ficheDePosteBean
					.getSavoirFaire());
			ficheDePostePersistant.setEvaluationGlobale(ficheDePosteBean
					.getEvaluationGlobale());
			ficheDePostePersistant.setMobiliteEnvisagee(ficheDePosteBean
					.getMobiliteEnvisagee());
			ficheDePostePersistant.setCategorieCompetence(ficheDePosteBean
					.getCategorieCompetence());
			ficheDePostePersistant.setCategorieCompetence2(ficheDePosteBean
					.getCategorieCompetence2());
			ficheDePostePersistant.setCategorieCompetence3(ficheDePosteBean
					.getCategorieCompetence3());
			ficheDePostePersistant.setCategorieCompetence4(ficheDePosteBean
					.getCategorieCompetence4());
			ficheDePostePersistant.setCategorieCompetence5(ficheDePosteBean
					.getCategorieCompetence5());

			FicheMetier ficheMetierPersistant = (FicheMetier) session.load(
					FicheMetier.class, ficheDePosteBean.getIdFicheMetierType());
			ficheDePostePersistant.setMetierType(ficheMetierPersistant);

			Salarie salariePersistant = (Salarie) session.load(Salarie.class,
					ficheDePosteBean.getIdSalarie());
			ficheDePostePersistant.setSalarie(salariePersistant);
			if (ficheDePosteBean.getJustificatif() != null)
				ficheDePostePersistant.setJustificatif(ficheDePosteBean
						.getJustificatif());
			else
				ficheDePostePersistant.setJustificatif(null);

			session.save(ficheDePostePersistant);
			ficheDePosteBean.setId(ficheDePostePersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public String getJustificatif(Integer idFicheDePoste) throws Exception {
		Session session = null;
		Transaction transaction = null;
		String justificatif = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheDePoste ficheDePostePersistant = new FicheDePoste();
			transaction = session.beginTransaction();
			ficheDePostePersistant = (FicheDePoste) session.load(
					FicheDePoste.class, idFicheDePoste);
			FicheDePosteBean ficheDePosteBean = ficheDePostePersistantToFicheDePosteBean(ficheDePostePersistant);
			transaction.commit();
			justificatif = ficheDePosteBean.getJustificatif();
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

	public FicheDePosteBeanExport getFicheDePosteBeanExportById(
			Integer idFicheDePoste) throws Exception {
		Session session = null;
		Transaction transaction = null;
		FicheDePosteBeanExport ficheDePosteBeanExport = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheDePoste ficheDePostePersistant = new FicheDePoste();
			transaction = session.beginTransaction();

			ficheDePostePersistant = (FicheDePoste) session.load(
					FicheDePoste.class, idFicheDePoste);

			ficheDePosteBeanExport = ficheDePostePersistantToFicheDePosteBeanExport(ficheDePostePersistant);

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
		return ficheDePosteBeanExport;
	}

	public List<FicheDePosteBean> getFicheDePosteBeanListByIdFicheMetier(
			Integer idFicheMetier) throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<FicheDePosteBean> ficheDePosteBeanList = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<FicheDePoste> ficheDePosteInventory = session.createQuery(
					"from FicheDePoste where MetierType=" + idFicheMetier)
					.list();

			ficheDePosteBeanList = new ArrayList<FicheDePosteBean>();
			ficheDePosteBeanList = ficheDePostePersistantListToFicheDePosteBeanList(ficheDePosteInventory);
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
		return ficheDePosteBeanList;
	}

	public FicheDePosteBeanExport ficheDePostePersistantToFicheDePosteBeanExport(
			FicheDePoste ficheDePostePersistant) throws Exception {

		FicheDePosteBeanExport ficheDePosteBeanExport = new FicheDePosteBeanExport();

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		ficheDePosteBeanExport.setIdSalarie(ficheDePostePersistant.getSalarie()
				.getId());
		ficheDePosteBeanExport.setDateCreation(formatter
				.format(ficheDePostePersistant.getDateCreation()));
		ficheDePosteBeanExport.setDateModification(formatter
				.format(ficheDePostePersistant.getDateModification()));
		ficheDePosteBeanExport.setActivitesSpecifiques(ficheDePostePersistant
				.getActivitesSpecifiques());
		ficheDePosteBeanExport.setCompetencesNouvelles(ficheDePostePersistant
				.getCompetencesNouvelles());
		ficheDePosteBeanExport.setCompetencesNouvelles2(ficheDePostePersistant
				.getCompetencesNouvelles2());
		ficheDePosteBeanExport.setCompetencesNouvelles3(ficheDePostePersistant
				.getCompetencesNouvelles3());
		ficheDePosteBeanExport.setCompetencesNouvelles4(ficheDePostePersistant
				.getCompetencesNouvelles4());
		ficheDePosteBeanExport.setCompetencesNouvelles5(ficheDePostePersistant
				.getCompetencesNouvelles5());
		ficheDePosteBeanExport.setCompetencesSpecifiques(ficheDePostePersistant
				.getCompetencesSpecifiques());
		ficheDePosteBeanExport
				.setCompetencesSpecifiquesTexte(ficheDePostePersistant
						.getCompetencesSpecifiquesTexte());
		ficheDePosteBeanExport.setCommentaire(ficheDePostePersistant
				.getCommentaires());
		ficheDePosteBeanExport.setSavoir(ficheDePostePersistant.getSavoir());
		ficheDePosteBeanExport.setSavoirEtre(ficheDePostePersistant
				.getSavoirEtre());
		ficheDePosteBeanExport.setSavoirFaire(ficheDePostePersistant
				.getSavoirFaire());
		ficheDePosteBeanExport.setEvaluationGlobale(ficheDePostePersistant
				.getEvaluationGlobale());
		ficheDePosteBeanExport.setCategorieCompetence(ficheDePostePersistant
				.getCategorieCompetence());
		ficheDePosteBeanExport.setCategorieCompetence2(ficheDePostePersistant
				.getCategorieCompetence2());
		ficheDePosteBeanExport.setCategorieCompetence3(ficheDePostePersistant
				.getCategorieCompetence3());
		ficheDePosteBeanExport.setCategorieCompetence4(ficheDePostePersistant
				.getCategorieCompetence4());
		ficheDePosteBeanExport.setCategorieCompetence5(ficheDePostePersistant
				.getCategorieCompetence5());
		if (ficheDePostePersistant.getJustificatif() != null) {
			ficheDePosteBeanExport.setJustificatif(ficheDePostePersistant
					.getJustificatif());
		} else {
			ficheDePosteBeanExport.setJustificatif(null);
		}

		return ficheDePosteBeanExport;
	}

	public FicheDePosteBean getFicheDePosteBeanById(Integer idFicheDePoste)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheDePoste ficheDePostePersistant = new FicheDePoste();
			transaction = session.beginTransaction();

			ficheDePostePersistant = (FicheDePoste) session.load(
					FicheDePoste.class, idFicheDePoste);

			FicheDePosteBean ficheDePosteBean = ficheDePostePersistantToFicheDePosteBean(ficheDePostePersistant);

			transaction.commit();

			return ficheDePosteBean;

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

	public FicheDePosteBean getFicheDePosteBeanByIdSalarie(Integer idSalarie)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheDePoste ficheDePostePersistant = new FicheDePoste();
			FicheDePosteBean ficheDePosteBean;
			transaction = session.beginTransaction();

			ficheDePostePersistant = (FicheDePoste) session.createQuery(
					"from FicheDePoste where Salarie=" + idSalarie)
					.uniqueResult();
			if (ficheDePostePersistant != null) {
				ficheDePosteBean = ficheDePostePersistantToFicheDePosteBean(ficheDePostePersistant);
			} else
				ficheDePosteBean = null;

			transaction.commit();

			return ficheDePosteBean;

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

	public FicheDePosteBean getFicheDePosteBeanByIdMetierType(
			Integer idMetierType, int idGroupe) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheDePoste ficheDePostePersistant = new FicheDePoste();
			FicheDePosteBean ficheDePosteBean;
			transaction = session.beginTransaction();

			ficheDePostePersistant = (FicheDePoste) session
					.createQuery(
							"from FicheDePoste as f left join fetch f.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " and f.MetierType="
									+ idMetierType).uniqueResult();
			if (ficheDePostePersistant != null) {
				ficheDePosteBean = ficheDePostePersistantToFicheDePosteBean(ficheDePostePersistant);
			} else {
				ficheDePosteBean = null;
			}

			transaction.commit();

			return ficheDePosteBean;

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

	public FicheDePosteBeanExport getFicheDePosteBeanExportByIdSalarie(
			Integer idSalarie) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheDePoste ficheDePostePersistant = new FicheDePoste();
			FicheDePosteBeanExport ficheDePosteBeanExport;
			transaction = session.beginTransaction();

			ficheDePostePersistant = (FicheDePoste) session.createQuery(
					"from FicheDePoste where Salarie=" + idSalarie)
					.uniqueResult();
			if (ficheDePostePersistant != null) {
				ficheDePosteBeanExport = ficheDePostePersistantToFicheDePosteBeanExport(ficheDePostePersistant);
			} else {
				ficheDePosteBeanExport = null;
			}

			transaction.commit();

			return ficheDePosteBeanExport;

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

	public FicheDePosteBean ficheDePostePersistantToFicheDePosteBean(
			FicheDePoste ficheDePostePersistant) throws Exception {

		FicheDePosteBean ficheDePosteBean = new FicheDePosteBean();
		ficheDePosteBean.setId(ficheDePostePersistant.getId());
		ficheDePosteBean.setDateCreation(ficheDePostePersistant
				.getDateCreation());
		ficheDePosteBean.setDateModification(ficheDePostePersistant
				.getDateModification());
		ficheDePosteBean.setActivitesSpecifiques(ficheDePostePersistant
				.getActivitesSpecifiques());
		ficheDePosteBean.setCompetencesNouvelles(ficheDePostePersistant
				.getCompetencesNouvelles());
		ficheDePosteBean.setCompetencesNouvelles2(ficheDePostePersistant
				.getCompetencesNouvelles2());
		ficheDePosteBean.setCompetencesNouvelles3(ficheDePostePersistant
				.getCompetencesNouvelles3());
		ficheDePosteBean.setCompetencesNouvelles4(ficheDePostePersistant
				.getCompetencesNouvelles4());
		ficheDePosteBean.setCompetencesNouvelles5(ficheDePostePersistant
				.getCompetencesNouvelles5());
		ficheDePosteBean.setCompetencesSpecifiques(ficheDePostePersistant
				.getCompetencesSpecifiques());
		ficheDePosteBean.setCompetencesSpecifiquesTexte(ficheDePostePersistant
				.getCompetencesSpecifiquesTexte());
		ficheDePosteBean.setCommentaire(ficheDePostePersistant
				.getCommentaires());
		ficheDePosteBean.setSavoir(ficheDePostePersistant.getSavoir());
		ficheDePosteBean.setSavoirEtre(ficheDePostePersistant.getSavoirEtre());
		ficheDePosteBean
				.setSavoirFaire(ficheDePostePersistant.getSavoirFaire());
		ficheDePosteBean.setEvaluationGlobale(ficheDePostePersistant
				.getEvaluationGlobale());
		ficheDePosteBean.setMobiliteEnvisagee(ficheDePostePersistant
				.getMobiliteEnvisagee());
		ficheDePosteBean.setIdFicheMetierType(ficheDePostePersistant
				.getMetierType().getId());
		ficheDePosteBean.setIdSalarie(ficheDePostePersistant.getSalarie()
				.getId());
		ficheDePosteBean.setCategorieCompetence(ficheDePostePersistant
				.getCategorieCompetence());
		ficheDePosteBean.setCategorieCompetence2(ficheDePostePersistant
				.getCategorieCompetence2());
		ficheDePosteBean.setCategorieCompetence3(ficheDePostePersistant
				.getCategorieCompetence3());
		ficheDePosteBean.setCategorieCompetence4(ficheDePostePersistant
				.getCategorieCompetence4());
		ficheDePosteBean.setCategorieCompetence5(ficheDePostePersistant
				.getCategorieCompetence5());
		if (ficheDePostePersistant.getJustificatif() != null
				&& ficheDePostePersistant.getJustificatif().equals("")) {
			ficheDePosteBean.setJustificatif(null);
		} else {
			ficheDePosteBean.setJustificatif(ficheDePostePersistant
					.getJustificatif());
		}

		return ficheDePosteBean;
	}
}
