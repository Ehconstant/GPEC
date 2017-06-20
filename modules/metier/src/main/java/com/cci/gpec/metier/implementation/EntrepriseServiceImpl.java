package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.db.Entreprise;
import com.cci.gpec.db.Groupe;
import com.cci.gpec.db.connection.HibernateUtil;
import com.cci.gpec.metier.interfaces.EntrepriseService;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class EntrepriseServiceImpl implements EntrepriseService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EntrepriseServiceImpl.class);
	
	/**
	 * Retourne la liste des Entreprises.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public List<EntrepriseBean> getEntreprisesList(int idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		List<EntrepriseBean> entreprisesInventoryBean = null;
		try {
			session =  HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = HibernateUtil.getSessionFactory().getCurrentSession()
					.beginTransaction();

			List<Entreprise> entreprisesInventory = session.createQuery(
							"from Entreprise where Groupe=" + idGroupe
									+ " order by NomEntreprise").list();
			entreprisesInventoryBean = entreprisePersistantListToEntrepriseBeanList(entreprisesInventory);
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
		return entreprisesInventoryBean;
	}

	public void saveOrUppdate(EntrepriseBean entrepriseBean, int idGroupe) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entreprise entreprisePersistant = new Entreprise();
			transaction = session.beginTransaction();
			if (entrepriseBean.getId() != 0) {
				entreprisePersistant = (Entreprise) session.load(
						Entreprise.class, entrepriseBean.getId());

				entreprisePersistant.setId(entrepriseBean.getId());
			}

			Groupe groupePersistant = new Groupe();
			groupePersistant = (Groupe) session.load(Groupe.class, idGroupe);

			entreprisePersistant.setGroupe(groupePersistant);
			entreprisePersistant.setNomEntreprise(entrepriseBean.getNom());
			entreprisePersistant.setCodeape(entrepriseBean.getCodeApe());
			entreprisePersistant.setNumsiret(entrepriseBean.getNumSiret());
			entreprisePersistant.setCciRattachement(entrepriseBean
					.getCciRattachement());
			entreprisePersistant.setDateCreation(entrepriseBean
					.getDateCreation());
			entreprisePersistant.setSuiviFormations(entrepriseBean
					.isSuiviFormations());
			entreprisePersistant.setSuiviAccidents(entrepriseBean
					.isSuiviAccidents());
			entreprisePersistant.setSuiviAbsences(entrepriseBean
					.isSuiviAbsences());
			entreprisePersistant.setSuiviCompetences(entrepriseBean
					.isSuiviCompetences());
			entreprisePersistant.setSuiviDIF(entrepriseBean.isSuiviDIF());
			entreprisePersistant.setDIFMax(entrepriseBean.getDIFMax());

			entreprisePersistant.setJustificatif(entrepriseBean
					.getJustificatif());

			session.saveOrUpdate(entreprisePersistant);
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

	public String getJustificatif(Integer idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		String justificatif = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entreprise entreprisePersistant = new Entreprise();
			transaction = session.beginTransaction();
			entreprisePersistant = (Entreprise) session.load(Entreprise.class,
					idEntreprise);
			EntrepriseBean entrepriseBean = entreprisePersistantToEntrepriseBean(entreprisePersistant);
			transaction.commit();
			justificatif =  entrepriseBean.getJustificatif();
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

	public void save(EntrepriseBean entrepriseBean, int idGroupe) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entreprise entreprisePersistant = new Entreprise();
			entreprisePersistant.setId(entrepriseBean.getId());
			Groupe groupePersistant = new Groupe();
			groupePersistant = (Groupe) session.load(Groupe.class, idGroupe);
			entreprisePersistant.setGroupe(groupePersistant);
			entreprisePersistant.setNomEntreprise(entrepriseBean.getNom());
			entreprisePersistant.setCodeape(entrepriseBean.getCodeApe());
			entreprisePersistant.setNumsiret(entrepriseBean.getNumSiret());
			entreprisePersistant.setCciRattachement(entrepriseBean
					.getCciRattachement());
			entreprisePersistant.setDateCreation(entrepriseBean
					.getDateCreation());
			entreprisePersistant.setSuiviFormations(entrepriseBean
					.isSuiviFormations());
			entreprisePersistant.setSuiviAccidents(entrepriseBean
					.isSuiviAccidents());
			entreprisePersistant.setSuiviAbsences(entrepriseBean
					.isSuiviAbsences());
			entreprisePersistant.setSuiviCompetences(entrepriseBean
					.isSuiviCompetences());
			entreprisePersistant.setSuiviDIF(entrepriseBean.isSuiviDIF());
			entreprisePersistant.setDIFMax(entrepriseBean.getDIFMax());
			entreprisePersistant.setJustificatif(entrepriseBean
					.getJustificatif());
			session.save(entreprisePersistant);
			entrepriseBean.setId(entreprisePersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public boolean supprimer(EntrepriseBean entrepriseBean) throws Exception {
		// On vérifie qu'un service n'est pas associé à une entreprise
		ServiceImpl serv = new ServiceImpl();
		if (serv.containEntrepriseBean(entrepriseBean)) {
			return false;
		} else {
			suppression(entrepriseBean);
		}
		return true;
	}

	private void suppression(EntrepriseBean entrepriseBean) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entreprise entreprisePersistant = new Entreprise();
			transaction = session.beginTransaction();
			if (entrepriseBean.getId() != 0) {
				// On supprime les parametres generaux de l'entreprise
				ParamsGenerauxServiceImpl servParam = new ParamsGenerauxServiceImpl();
				servParam.suppression(entrepriseBean.getId());
				entreprisePersistant = (Entreprise) session.load(
						Entreprise.class, entrepriseBean.getId());

				entreprisePersistant.setId(entrepriseBean.getId());
			}
			session.delete(entreprisePersistant);
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

	public void deleteEntrepriseWithoutTransaction(EntrepriseBean entrepriseBean)
			throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entreprise entreprisePersistant = new Entreprise();
			if (entrepriseBean.getId() != 0) {
				entreprisePersistant = (Entreprise) session.load(
						Entreprise.class, entrepriseBean.getId());

				entreprisePersistant.setId(entrepriseBean.getId());
			}
			session.delete(entreprisePersistant);
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public EntrepriseBean getEntrepriseBeanById(Integer idEntreprise)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		EntrepriseBean entrepriseBean = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Entreprise entreprisePersistant = new Entreprise();
			transaction = session.beginTransaction();
			entreprisePersistant = (Entreprise) session.load(Entreprise.class,
					idEntreprise);
			entrepriseBean = entreprisePersistantToEntrepriseBean(entreprisePersistant);
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
		return entrepriseBean;
	}

	private List<EntrepriseBean> entreprisePersistantListToEntrepriseBeanList(
			List<Entreprise> entreprisePersistantList) throws Exception {

		List<EntrepriseBean> entrepriseBeanList = new ArrayList<EntrepriseBean>();

		for (Entreprise entreprisePersistant : entreprisePersistantList) {

			entrepriseBeanList
					.add(entreprisePersistantToEntrepriseBean(entreprisePersistant));
		}

		return entrepriseBeanList;

	}

	public EntrepriseBean entreprisePersistantToEntrepriseBean(
			Entreprise entreprisePersistant) throws Exception {
		EntrepriseBean entrepriseBean = new EntrepriseBean();
		entrepriseBean.setId(entreprisePersistant.getId());
		entrepriseBean.setIdGroupe(entreprisePersistant.getGroupe().getId());
		entrepriseBean.setNom(entreprisePersistant.getNomEntreprise());
		entrepriseBean.setCodeApe(entreprisePersistant.getCodeape());
		entrepriseBean.setNumSiret(entreprisePersistant.getNumsiret());
		entrepriseBean.setDateCreation(entreprisePersistant.getDateCreation());
		entrepriseBean.setCciRattachement(entreprisePersistant
				.getCciRattachement());
		entrepriseBean.setSuiviFormations(entreprisePersistant
				.isSuiviFormations());
		entrepriseBean.setSuiviAccidents(entreprisePersistant
				.isSuiviAccidents());
		entrepriseBean.setSuiviAbsences(entreprisePersistant.isSuiviAbsences());
		entrepriseBean.setSuiviCompetences(entreprisePersistant
				.isSuiviCompetences());
		entrepriseBean.setSuiviDIF(entreprisePersistant.isSuiviDIF());
		entrepriseBean.setDIFMax(entreprisePersistant.getDIFMax());

		if (entreprisePersistant.getJustificatif() != null
				&& !entreprisePersistant.getJustificatif().equals("")) {
			entrepriseBean.setJustificatif(entreprisePersistant
					.getJustificatif());
		} else {
			entrepriseBean.setJustificatif(null);
		}

		return entrepriseBean;
	}
}
