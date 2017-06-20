package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.FicheDePosteBean;
import com.cci.gpec.commons.FicheMetierBean;
import com.cci.gpec.commons.FicheMetierEntrepriseBean;
import com.cci.gpec.db.FicheMetier;
import com.cci.gpec.db.Groupe;
import com.cci.gpec.db.connection.HibernateUtil;

/**
 * Implementation de l'interface EntrepriseService.
 */
public class FicheMetierServiceImpl {
	
	private static final Logger	LOGGER = LoggerFactory.getLogger(FicheMetierServiceImpl.class);

	private List<FicheMetierBean> ficheMetierPersistantListToFicheMetierBeanList(
			List<FicheMetier> ficheMetierPersistantList) throws Exception {

		List<FicheMetierBean> ficheMetierBeanList = new ArrayList<FicheMetierBean>();

		for (FicheMetier ficheMetierPersistant : ficheMetierPersistantList) {

			ficheMetierBeanList
					.add(ficheMetierPersistantToFicheMetierBean(ficheMetierPersistant));
		}

		return ficheMetierBeanList;

	}

	private boolean isEntrepriseContainFicheMetier(
			FicheMetierBean ficheMetierBean) throws Exception {
		FicheMetierEntrepriseServiceImpl ficheMetierEntreprise = new FicheMetierEntrepriseServiceImpl();
		List<FicheMetierEntrepriseBean> fme = ficheMetierEntreprise
				.getFicheMetierEntrepriseBeanListByIdFicheMetier(ficheMetierBean
						.getId());
		if (fme.size() > 0) {
			return true;
		}
		return false;
	}

	private boolean isFicheDePosteContainFicheMetier(
			FicheMetierBean ficheMetierBean) throws Exception {
		FicheDePosteServiceImpl ficheDePoste = new FicheDePosteServiceImpl();
		List<FicheDePosteBean> fdp = ficheDePoste
				.getFicheDePosteBeanListByIdFicheMetier(ficheMetierBean.getId());
		if (fdp.size() > 0) {
			return true;
		}
		return false;
	}

	public boolean supprimer(FicheMetierBean ficheMetierBean) throws Exception {
		// On vérifie qu'un service n'est pas associé à une entreprise
		if (isEntrepriseContainFicheMetier(ficheMetierBean)
				|| isFicheDePosteContainFicheMetier(ficheMetierBean)) {
			return false;
		} else {
			suppression(ficheMetierBean);
		}
		return true;
	}

	private void suppression(FicheMetierBean ficheMetierBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheMetier ficheMetierPersistant = new FicheMetier();
			transaction = session.beginTransaction();
			if (ficheMetierBean.getId() != 0) {
				ficheMetierPersistant = (FicheMetier) session.load(
						FicheMetier.class, ficheMetierBean.getId());

				ficheMetierPersistant.setId(ficheMetierBean.getId());
			}
			session.delete(ficheMetierPersistant);
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

	public void deleteFicheMetierWithoutTransaction(
			FicheMetierBean ficheMetierBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheMetier ficheMetierPersistant = new FicheMetier();
			if (ficheMetierBean.getId() != 0) {
				ficheMetierPersistant = (FicheMetier) session.load(
						FicheMetier.class, ficheMetierBean.getId());

				ficheMetierPersistant.setId(ficheMetierBean.getId());
			}
			session.delete(ficheMetierPersistant);
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public String getJustificatif(Integer idFicheMetier) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheMetier ficheMetierPersistant = new FicheMetier();
			transaction = session.beginTransaction();
			ficheMetierPersistant = (FicheMetier) session.load(
					FicheMetier.class, idFicheMetier);
			FicheMetierBean ficheMetierBean = ficheMetierPersistantToFicheMetierBean(ficheMetierPersistant);
			transaction.commit();

			return ficheMetierBean.getJustificatif();

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

	public void saveOrUppdate(FicheMetierBean ficheMetierBean, int idGroupe) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheMetier ficheMetierPersistant = new FicheMetier();
			transaction = session.beginTransaction();
			if (ficheMetierBean.getId() != 0) {
				ficheMetierPersistant = (FicheMetier) session.load(
						FicheMetier.class, ficheMetierBean.getId());

				ficheMetierPersistant.setId(ficheMetierBean.getId());
			}

			Groupe groupePersistant = new Groupe();
			groupePersistant = (Groupe) session.load(Groupe.class, idGroupe);

			ficheMetierPersistant.setGroupe(groupePersistant);

			ficheMetierPersistant.setIntituleMetierType(ficheMetierBean
					.getNom());
			ficheMetierPersistant.setFinaliteMetier(ficheMetierBean
					.getFinaliteFicheMetier());
			ficheMetierPersistant.setCspReference(ficheMetierBean
					.getCspReference());
			ficheMetierPersistant.setActivitesResponsabilites(ficheMetierBean
					.getActiviteResponsabilite());
			ficheMetierPersistant.setSavoir(ficheMetierBean.getSavoir());
			ficheMetierPersistant.setSavoirFaire(ficheMetierBean
					.getSavoirFaire());
			ficheMetierPersistant
					.setSavoirEtre(ficheMetierBean.getSavoirEtre());
			ficheMetierPersistant.setNiveauFormationRequis(ficheMetierBean
					.getNiveauFormationRequis());
			ficheMetierPersistant.setParticuliarite(ficheMetierBean
					.getParticularite());
			ficheMetierPersistant.setJustificatif(ficheMetierBean
					.getJustificatif());
			session.saveOrUpdate(ficheMetierPersistant);
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

	public void save(FicheMetierBean ficheMetierBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheMetier ficheMetierPersistant = new FicheMetier();

			ficheMetierPersistant.setId(ficheMetierBean.getId());
			ficheMetierPersistant.setIntituleMetierType(ficheMetierBean
					.getIntituleFicheMetier());
			ficheMetierPersistant.setFinaliteMetier(ficheMetierBean
					.getFinaliteFicheMetier());
			ficheMetierPersistant.setCspReference(ficheMetierBean
					.getCspReference());
			ficheMetierPersistant.setActivitesResponsabilites(ficheMetierBean
					.getActiviteResponsabilite());
			ficheMetierPersistant.setSavoir(ficheMetierBean.getSavoir());
			ficheMetierPersistant.setSavoirFaire(ficheMetierBean
					.getSavoirFaire());
			ficheMetierPersistant
					.setSavoirEtre(ficheMetierBean.getSavoirEtre());
			ficheMetierPersistant.setNiveauFormationRequis(ficheMetierBean
					.getNiveauFormationRequis());
			ficheMetierPersistant.setParticuliarite(ficheMetierBean
					.getParticularite());
			
			Groupe g = new Groupe();
			g.setId(ficheMetierBean.getIdGroupe());
			ficheMetierPersistant.setGroupe(g);
			
			session.save(ficheMetierPersistant);
			ficheMetierBean.setId(ficheMetierPersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public FicheMetierBean getFicheMetierBeanById(Integer idFicheMetier)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			FicheMetier ficheMetierPersistant = new FicheMetier();
			transaction = session.beginTransaction();

			ficheMetierPersistant = (FicheMetier) session.load(
					FicheMetier.class, idFicheMetier);

			FicheMetierBean ficheMetierBean = ficheMetierPersistantToFicheMetierBean(ficheMetierPersistant);

			transaction.commit();

			return ficheMetierBean;

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

	public List<FicheMetierBean> getFichesMetierList(int idGroupe)
			throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<FicheMetierBean> fichesMetierInventoryBean;
			transaction = session.beginTransaction();

			List<FicheMetier> fichesMetierInventory = HibernateUtil
					.getSessionFactory().getCurrentSession()
					.createQuery("from FicheMetier where Groupe=" + idGroupe)
					.list();
			fichesMetierInventoryBean = ficheMetierPersistantListToFicheMetierBeanList(fichesMetierInventory);
			transaction.commit();

			return fichesMetierInventoryBean;

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

	public FicheMetierBean ficheMetierPersistantToFicheMetierBean(
			FicheMetier ficheMetierPersistant) throws Exception {

		FicheMetierBean ficheMetierBean = new FicheMetierBean();
		ficheMetierBean.setId(ficheMetierPersistant.getId());
		ficheMetierBean.setNom(ficheMetierPersistant.getIntituleMetierType());
		ficheMetierBean.setFinaliteFicheMetier(ficheMetierPersistant
				.getFinaliteMetier());
		ficheMetierBean
				.setCspReference(ficheMetierPersistant.getCspReference());
		ficheMetierBean.setActiviteResponsabilite(ficheMetierPersistant
				.getActivitesResponsabilites());
		ficheMetierBean.setSavoir(ficheMetierPersistant.getSavoir());
		ficheMetierBean.setSavoirFaire(ficheMetierPersistant.getSavoirFaire());
		ficheMetierBean.setSavoirEtre(ficheMetierPersistant.getSavoirEtre());
		ficheMetierBean.setNiveauFormationRequis(ficheMetierPersistant
				.getNiveauFormationRequis());
		ficheMetierBean.setParticularite(ficheMetierPersistant
				.getParticuliarite());
		ficheMetierBean.setIntituleFicheMetier(ficheMetierPersistant
				.getIntituleMetierType());
		if (ficheMetierPersistant.getJustificatif() != null
				&& ficheMetierPersistant.getJustificatif().equals("")) {
			ficheMetierBean.setJustificatif(null);
		} else {
			ficheMetierBean.setJustificatif(ficheMetierPersistant
					.getJustificatif());
		}

		return ficheMetierBean;
	}
}
