package com.cci.gpec.metier.implementation;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.RemunerationBean;
import com.cci.gpec.commons.RemunerationBeanExport;
import com.cci.gpec.db.Remuneration;
import com.cci.gpec.db.Salarie;
import com.cci.gpec.db.connection.HibernateUtil;

public class RemunerationServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RemunerationServiceImpl.class);

	/**
	 * Retourne la liste des remunerations
	 */

	public List<RemunerationBean> getRemunerationList(int idGroupe)
			throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Remuneration> remunerationInventory = session
					.createQuery(
							"from Remuneration as r left join fetch r.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe + " order by Annee desc").list();

			List<RemunerationBean> remunerationInventoryBean = new ArrayList<RemunerationBean>();
			remunerationInventoryBean = RemunerationPersistantListToRemunerationBeanList(remunerationInventory);
			transaction.commit();

			return remunerationInventoryBean;

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

	public List<RemunerationBean> getRemunerationListOrderByAnneAndSalarie(
			int idGroupe) throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Remuneration> remunerationInventory = session
					.createQuery(
							"from Remuneration as r left join fetch r.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " order by r.Annee desc, s.Nom").list();

			List<RemunerationBean> remunerationInventoryBean = new ArrayList<RemunerationBean>();
			remunerationInventoryBean = RemunerationPersistantListToRemunerationBeanList(remunerationInventory);
			transaction.commit();

			return remunerationInventoryBean;

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

	public List<RemunerationBean> getRemunerationListOrderByAnneeNomEntrepriseNomSalarie(
			int idGroupe) throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Remuneration> remunerationInventory = session
					.createQuery(
							"from Remuneration as r left join fetch r.Salarie as s left join fetch s.Entreprise as e where e.Groupe="
									+ idGroupe
									+ " order by r.Annee desc, e.NomEntreprise, s.Nom")
					.list();

			List<RemunerationBean> remunerationInventoryBean = new ArrayList<RemunerationBean>();
			remunerationInventoryBean = RemunerationPersistantListToRemunerationBeanList(remunerationInventory);
			transaction.commit();

			return remunerationInventoryBean;

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

	public List<RemunerationBean> getRemunerationListOrderByAnneeNomSalarie(
			int idEntreprise) throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Remuneration> remunerationInventory = session.createQuery(
					"from Remuneration as r left join fetch r.Salarie as s where s.Entreprise = "
							+ idEntreprise + " order by r.Annee desc, s.Nom")
					.list();

			List<RemunerationBean> remunerationInventoryBean = new ArrayList<RemunerationBean>();
			remunerationInventoryBean = RemunerationPersistantListToRemunerationBeanList(remunerationInventory);
			transaction.commit();

			return remunerationInventoryBean;

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

	public List<RemunerationBean> getRemuneration(int year, int idSalarie)
			throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Remuneration> remunerationInventory = session.createQuery(
					"from Remuneration r where r.Annee = " + year
							+ " and r.Salarie = " + idSalarie).list();

			List<RemunerationBean> remunerationInventoryBean = new ArrayList<RemunerationBean>();
			remunerationInventoryBean = RemunerationPersistantListToRemunerationBeanList(remunerationInventory);
			transaction.commit();

			return remunerationInventoryBean;

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

	public List<RemunerationBeanExport> getRemunerationExport(int year,
			int idSalarie) throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Remuneration> remunerationInventory = session.createQuery(
					"from Remuneration r where r.Annee = " + year
							+ " and r.Salarie = " + idSalarie).list();

			List<RemunerationBeanExport> remunerationInventoryBean = new ArrayList<RemunerationBeanExport>();
			remunerationInventoryBean = RemunerationPersistantListToRemunerationBeanExportList(remunerationInventory);
			transaction.commit();

			return remunerationInventoryBean;

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

	public List<RemunerationBean> getRemunerationByIdSalarie(int idSalarie)
			throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			List<Remuneration> remunerationInventory = session.createQuery(
					"from Remuneration r where r.Salarie = " + idSalarie)
					.list();

			List<RemunerationBean> remunerationInventoryBean = new ArrayList<RemunerationBean>();
			remunerationInventoryBean = RemunerationPersistantListToRemunerationBeanList(remunerationInventory);
			transaction.commit();

			return remunerationInventoryBean;

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

	public List<RemunerationBean> RemunerationPersistantListToRemunerationBeanList(
			List<Remuneration> RemunerationPersistantList) throws Exception {

		List<RemunerationBean> remunerationBeanList = new ArrayList<RemunerationBean>();

		for (Remuneration remunerationPersistant : RemunerationPersistantList) {

			remunerationBeanList
					.add(RemunerationPersistantToRemunerationBean(remunerationPersistant));
		}

		return remunerationBeanList;

	}

	public List<RemunerationBeanExport> RemunerationPersistantListToRemunerationBeanExportList(
			List<Remuneration> RemunerationPersistantList) throws Exception {

		List<RemunerationBeanExport> remunerationBeanList = new ArrayList<RemunerationBeanExport>();

		for (Remuneration remunerationPersistant : RemunerationPersistantList) {

			remunerationBeanList
					.add(RemunerationPersistantToRemunerationBeanExport(remunerationPersistant));
		}

		return remunerationBeanList;

	}

	public RemunerationBean RemunerationPersistantToRemunerationBean(
			Remuneration remunerationPersistant) throws Exception {

		RemunerationBean remunerationBean = new RemunerationBean();
		remunerationBean.setIdRemuneration(remunerationPersistant
				.getIdRemuneration());
		remunerationBean.setAnnee(remunerationPersistant.getAnnee());
		remunerationBean.setAugmentationCollective(remunerationPersistant
				.getAugmentationCollective());
		remunerationBean
				.setAugmentationCollectiveActualisation(remunerationPersistant
						.getAugmentationCollectiveActualisation());
		remunerationBean
				.setAugmentationCollectiveCommentaire(remunerationPersistant
						.getAugmentationCollectiveCommentaire());
		remunerationBean.setAugmentationIndividuelle(remunerationPersistant
				.getAugmentationIndividuelle());
		remunerationBean
				.setAugmentationIndividuelleActualisation(remunerationPersistant
						.getAugmentationIndividuelleActualisation());
		remunerationBean
				.setAugmentationIndividuelleCommentaire(remunerationPersistant
						.getAugmentationIndividuelleCommentaire());

		remunerationBean
				.setCoefficient(remunerationPersistant.getCoefficient());
		remunerationBean.setNiveau(remunerationPersistant.getNiveau());
		remunerationBean.setEchelon(remunerationPersistant.getEchelon());
		remunerationBean
				.setCommentaire(remunerationPersistant.getCommentaire());
		remunerationBean
				.setHeuresSup25(remunerationPersistant.getHeuresSup25());
		remunerationBean.setHeuresSup25Actualisation(remunerationPersistant
				.getHeuresSup25Actualisation());
		remunerationBean.setHeuresSup25Commentaire(remunerationPersistant
				.getHeuresSup25Commentaire());
		remunerationBean
				.setHeuresSup50(remunerationPersistant.getHeuresSup50());
		remunerationBean.setHeuresSup50Actualisation(remunerationPersistant
				.getHeuresSup50Actualisation());
		remunerationBean.setHeuresSup50Commentaire(remunerationPersistant
				.getHeuresSup50Commentaire());
		remunerationBean.setHoraireMensuelTravaille(remunerationPersistant
				.getHoraireMensuelTravaille());
		remunerationBean.setIdMetier(remunerationPersistant.getIdMetier());
		remunerationBean.setIdRevenuComplementaire(remunerationPersistant
				.getIdRevenuComplementaire());
		remunerationBean.setIdSalarie(remunerationPersistant.getSalarie()
				.getId());
		remunerationBean.setIdStatut(remunerationPersistant.getIdStatut());

		remunerationBean.setSalaireDeBase(remunerationPersistant
				.getSalaireDeBase());
		remunerationBean.setSalaireDeBaseActualisation(remunerationPersistant
				.getSalaireDeBaseActualisation());
		remunerationBean.setSalaireDeBaseCommentaire(remunerationPersistant
				.getSalaireDeBaseCommentaire());
		remunerationBean.setSalaireMensuelBrut(remunerationPersistant
				.getSalaireMensuelBrut());
		remunerationBean
				.setSalaireMensuelCenventionnelBrut(remunerationPersistant
						.getSalaireMensuelCenventionnelBrut());
		remunerationBean.setSalaireMinimumConventionnel(remunerationPersistant
				.getSalaireMinimumConventionnel());
		remunerationBean
				.setSalaireMinimumConventionnelActualisation(remunerationPersistant
						.getSalaireMinimumConventionnelActualisation());
		remunerationBean
				.setSalaireMinimumConventionnelCommentaire(remunerationPersistant
						.getSalaireMinimumConventionnelCommentaire());
		remunerationBean.setTauxHoraireBrut(remunerationPersistant
				.getTauxHoraireBrut());

		remunerationBean
				.setRemuGlobale(remunerationPersistant.getRemuGlobale());
		remunerationBean.setHeuresSupAnnuelles(remunerationPersistant
				.getHeuresSupAnnuelles());
		remunerationBean
				.setAvantagesNonAssujettisAnnuels(remunerationPersistant
						.getAvantagesNonAssujettisAnnuels());
		remunerationBean.setBaseBruteAnnuelle(remunerationPersistant
				.getBaseBruteAnnuelle());

		return remunerationBean;
	}

	public RemunerationBeanExport RemunerationPersistantToRemunerationBeanExport(
			Remuneration remunerationPersistant) throws Exception {

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		RemunerationBeanExport remunerationBean = new RemunerationBeanExport();
		remunerationBean.setIdRemuneration(remunerationPersistant
				.getIdRemuneration());
		remunerationBean.setAnnee(remunerationPersistant.getAnnee());
		remunerationBean.setAugmentationCollective(df
				.format(remunerationPersistant.getAugmentationCollective()));
		remunerationBean.setAugmentationCollectiveActualisation(df
				.format(remunerationPersistant
						.getAugmentationCollectiveActualisation()));
		remunerationBean
				.setAugmentationCollectiveCommentaire(remunerationPersistant
						.getAugmentationCollectiveCommentaire());
		remunerationBean.setAugmentationIndividuelle(df
				.format(remunerationPersistant.getAugmentationIndividuelle()));
		remunerationBean.setAugmentationIndividuelleActualisation(df
				.format(remunerationPersistant
						.getAugmentationIndividuelleActualisation()));
		remunerationBean
				.setAugmentationIndividuelleCommentaire(remunerationPersistant
						.getAugmentationIndividuelleCommentaire());

		remunerationBean
				.setCoefficient(remunerationPersistant.getCoefficient());
		remunerationBean.setNiveau(remunerationPersistant.getNiveau());
		remunerationBean.setEchelon(remunerationPersistant.getEchelon());
		remunerationBean
				.setCommentaire(remunerationPersistant.getCommentaire());
		remunerationBean.setHeuresSup25(df.format(remunerationPersistant
				.getHeuresSup25()));
		remunerationBean.setHeuresSup25Actualisation(df
				.format(remunerationPersistant.getHeuresSup25Actualisation()));
		remunerationBean.setHeuresSup25Commentaire(remunerationPersistant
				.getHeuresSup25Commentaire());
		remunerationBean.setHeuresSup50(df.format(remunerationPersistant
				.getHeuresSup50()));
		remunerationBean.setHeuresSup50Actualisation(df
				.format(remunerationPersistant.getHeuresSup50Actualisation()));
		remunerationBean.setHeuresSup50Commentaire(remunerationPersistant
				.getHeuresSup50Commentaire());
		remunerationBean.setHoraireMensuelTravaille(remunerationPersistant
				.getHoraireMensuelTravaille());
		remunerationBean.setIdMetier(remunerationPersistant.getIdMetier());
		remunerationBean.setIdRevenuComplementaire(remunerationPersistant
				.getIdRevenuComplementaire());
		remunerationBean.setIdSalarie(remunerationPersistant.getSalarie()
				.getId());
		remunerationBean.setIdStatut(remunerationPersistant.getIdStatut());

		remunerationBean.setSalaireDeBase(df.format(remunerationPersistant
				.getSalaireDeBase()));
		remunerationBean
				.setSalaireDeBaseActualisation(df.format(remunerationPersistant
						.getSalaireDeBaseActualisation()));
		remunerationBean.setSalaireDeBaseCommentaire(remunerationPersistant
				.getSalaireDeBaseCommentaire());
		remunerationBean.setSalaireMensuelBrut(df.format(remunerationPersistant
				.getSalaireMensuelBrut()));
		remunerationBean.setSalaireMensuelConventionnelBrut(df
				.format(remunerationPersistant
						.getSalaireMensuelCenventionnelBrut()));
		remunerationBean
				.setSalaireMinimumConventionnel(df
						.format(remunerationPersistant
								.getSalaireMinimumConventionnel()));
		remunerationBean.setSalaireMinimumConventionnelActualisation(df
				.format(remunerationPersistant
						.getSalaireMinimumConventionnelActualisation()));
		remunerationBean
				.setSalaireMinimumConventionnelCommentaire(remunerationPersistant
						.getSalaireMinimumConventionnelCommentaire());
		remunerationBean.setTauxHoraireBrut(df.format(remunerationPersistant
				.getTauxHoraireBrut()));

		remunerationBean.setRemuGlobale(df.format(remunerationPersistant
				.getRemuGlobale()));
		remunerationBean.setHeuresSupAnnuelles(df.format(remunerationPersistant
				.getHeuresSupAnnuelles()));
		remunerationBean.setAvantagesNonAssujettisAnnuels(df
				.format(remunerationPersistant
						.getAvantagesNonAssujettisAnnuels()));
		remunerationBean.setBaseBruteAnnuelle(df.format(remunerationPersistant
				.getBaseBruteAnnuelle()));

		return remunerationBean;
	}

	public void saveOrUpdate(RemunerationBean remunerationBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Remuneration remunerationPersistant = new Remuneration();
			transaction = session.beginTransaction();
			if (remunerationBean.getIdRemuneration() != 0) {
				remunerationPersistant = (Remuneration) session.load(
						Remuneration.class,
						remunerationBean.getIdRemuneration());

				remunerationPersistant.setIdRemuneration(remunerationBean
						.getIdRemuneration());

			}

			remunerationPersistant.setAnnee(remunerationBean.getAnnee());
			remunerationPersistant.setIdMetier(remunerationBean.getIdMetier());
			remunerationPersistant.setAugmentationCollective(remunerationBean
					.getAugmentationCollective());
			remunerationPersistant
					.setAugmentationCollectiveActualisation(remunerationBean
							.getAugmentationCollectiveActualisation());
			remunerationPersistant
					.setAugmentationCollectiveCommentaire(remunerationBean
							.getAugmentationCollectiveCommentaire());
			remunerationPersistant.setAugmentationIndividuelle(remunerationBean
					.getAugmentationIndividuelle());
			remunerationPersistant
					.setAugmentationIndividuelleActualisation(remunerationBean
							.getAugmentationIndividuelleActualisation());
			remunerationPersistant
					.setAugmentationIndividuelleCommentaire(remunerationBean
							.getAugmentationIndividuelleCommentaire());
			remunerationPersistant.setCoefficient(remunerationBean
					.getCoefficient());
			remunerationPersistant.setCommentaire(remunerationBean
					.getCommentaire());
			remunerationPersistant.setHeuresSup25(remunerationBean
					.getHeuresSup25());
			remunerationPersistant.setHeuresSup25Actualisation(remunerationBean
					.getHeuresSup25Actualisation());
			remunerationPersistant.setHeuresSup25Commentaire(remunerationBean
					.getHeuresSup25Commentaire());
			remunerationPersistant.setHeuresSup50(remunerationBean
					.getHeuresSup50());
			remunerationPersistant.setHeuresSup50Actualisation(remunerationBean
					.getHeuresSup50Actualisation());
			remunerationPersistant.setHeuresSup50Commentaire(remunerationBean
					.getHeuresSup50Commentaire());
			remunerationPersistant.setHoraireMensuelTravaille(remunerationBean
					.getHoraireMensuelTravaille());
			remunerationPersistant.setIdMetier(remunerationBean.getIdMetier());
			remunerationPersistant.setIdRevenuComplementaire(remunerationBean
					.getIdRevenuComplementaire());

			Salarie s = new Salarie();
			s.setId(remunerationBean.getIdSalarie());
			remunerationPersistant.setSalarie(s);

			remunerationPersistant.setIdStatut(remunerationBean.getIdStatut());
			remunerationPersistant.setNiveau(remunerationBean.getNiveau());
			remunerationPersistant.setEchelon(remunerationBean.getEchelon());
			remunerationPersistant.setSalaireDeBase(remunerationBean
					.getSalaireDeBase());
			remunerationPersistant
					.setSalaireDeBaseActualisation(remunerationBean
							.getSalaireDeBaseActualisation());
			remunerationPersistant.setSalaireDeBaseCommentaire(remunerationBean
					.getSalaireDeBaseCommentaire());
			remunerationPersistant.setSalaireMensuelBrut(remunerationBean
					.getSalaireMensuelBrut());
			remunerationPersistant
					.setSalaireMensuelCenventionnelBrut(remunerationBean
							.getSalaireMensuelConventionnelBrut());
			remunerationPersistant
					.setSalaireMinimumConventionnel(remunerationBean
							.getSalaireMinimumConventionnel());
			remunerationPersistant
					.setSalaireMinimumConventionnelActualisation(remunerationBean
							.getSalaireMinimumConventionnelActualisation());
			remunerationPersistant
					.setSalaireMinimumConventionnelCommentaire(remunerationBean
							.getSalaireMinimumConventionnelCommentaire());
			remunerationPersistant.setTauxHoraireBrut(remunerationBean
					.getTauxHoraireBrut());

			remunerationPersistant.setRemuGlobale(remunerationBean
					.getRemuGlobale());
			remunerationPersistant.setHeuresSupAnnuelles(remunerationBean
					.getHeuresSupAnnuelles());
			remunerationPersistant
					.setAvantagesNonAssujettisAnnuels(remunerationBean
							.getAvantagesNonAssujettisAnnuels());
			remunerationPersistant.setBaseBruteAnnuelle(remunerationBean
					.getBaseBruteAnnuelle());

			session.saveOrUpdate(remunerationPersistant);
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

	public void save(RemunerationBean remunerationBean) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Remuneration remunerationPersistant = new Remuneration();
			// transaction = session.beginTransaction();

			remunerationPersistant.setIdRemuneration(remunerationBean
					.getIdRemuneration());

			remunerationPersistant.setAnnee(remunerationBean.getAnnee());
			remunerationPersistant.setIdMetier(remunerationBean.getIdMetier());
			remunerationPersistant.setAugmentationCollective(remunerationBean
					.getAugmentationCollective());
			remunerationPersistant
					.setAugmentationCollectiveActualisation(remunerationBean
							.getAugmentationCollectiveActualisation());
			remunerationPersistant
					.setAugmentationCollectiveCommentaire(remunerationBean
							.getAugmentationCollectiveCommentaire());
			remunerationPersistant.setAugmentationIndividuelle(remunerationBean
					.getAugmentationIndividuelle());
			remunerationPersistant
					.setAugmentationIndividuelleActualisation(remunerationBean
							.getAugmentationIndividuelleActualisation());
			remunerationPersistant
					.setAugmentationIndividuelleCommentaire(remunerationBean
							.getAugmentationIndividuelleCommentaire());
			remunerationPersistant.setCoefficient(remunerationBean
					.getCoefficient());
			remunerationPersistant.setCommentaire(remunerationBean
					.getCommentaire());
			remunerationPersistant.setHeuresSup25(remunerationBean
					.getHeuresSup25());
			remunerationPersistant.setHeuresSup25Actualisation(remunerationBean
					.getHeuresSup25Actualisation());
			remunerationPersistant.setHeuresSup25Commentaire(remunerationBean
					.getHeuresSup25Commentaire());
			remunerationPersistant.setHeuresSup50(remunerationBean
					.getHeuresSup50());
			remunerationPersistant.setHeuresSup50Actualisation(remunerationBean
					.getHeuresSup50Actualisation());
			remunerationPersistant.setHeuresSup50Commentaire(remunerationBean
					.getHeuresSup50Commentaire());
			remunerationPersistant.setHoraireMensuelTravaille(remunerationBean
					.getHoraireMensuelTravaille());
			remunerationPersistant.setIdMetier(remunerationBean.getIdMetier());
			remunerationPersistant.setIdRevenuComplementaire(remunerationBean
					.getIdRevenuComplementaire());

			Salarie s = new Salarie();
			s.setId(remunerationBean.getIdSalarie());
			remunerationPersistant.setSalarie(s);

			remunerationPersistant.setIdStatut(remunerationBean.getIdStatut());
			remunerationPersistant.setNiveau(remunerationBean.getNiveau());
			remunerationPersistant.setEchelon(remunerationBean.getEchelon());
			remunerationPersistant.setSalaireDeBase(remunerationBean
					.getSalaireDeBase());
			remunerationPersistant
					.setSalaireDeBaseActualisation(remunerationBean
							.getSalaireDeBaseActualisation());
			remunerationPersistant.setSalaireDeBaseCommentaire(remunerationBean
					.getSalaireDeBaseCommentaire());
			remunerationPersistant.setSalaireMensuelBrut(remunerationBean
					.getSalaireMensuelBrut());
			remunerationPersistant
					.setSalaireMensuelCenventionnelBrut(remunerationBean
							.getSalaireMensuelConventionnelBrut());
			remunerationPersistant
					.setSalaireMinimumConventionnel(remunerationBean
							.getSalaireMinimumConventionnel());
			remunerationPersistant
					.setSalaireMinimumConventionnelActualisation(remunerationBean
							.getSalaireMinimumConventionnelActualisation());
			remunerationPersistant
					.setSalaireMinimumConventionnelCommentaire(remunerationBean
							.getSalaireMinimumConventionnelCommentaire());
			remunerationPersistant.setTauxHoraireBrut(remunerationBean
					.getTauxHoraireBrut());

			remunerationPersistant.setRemuGlobale(remunerationBean
					.getRemuGlobale());
			remunerationPersistant.setHeuresSupAnnuelles(remunerationBean
					.getHeuresSupAnnuelles());
			remunerationPersistant
					.setAvantagesNonAssujettisAnnuels(remunerationBean
							.getAvantagesNonAssujettisAnnuels());
			remunerationPersistant.setBaseBruteAnnuelle(remunerationBean
					.getBaseBruteAnnuelle());

			session.save(remunerationPersistant);
			// transaction.commit();
			remunerationBean.setIdRemuneration(remunerationPersistant
					.getIdRemuneration());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public void deleteRemuneration(RemunerationBean remunerationBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Remuneration remunerationPersistant = new Remuneration();
			transaction = session.beginTransaction();
			if (remunerationBean.getIdRemuneration() != 0) {
				remunerationPersistant = (Remuneration) session.load(
						Remuneration.class,
						remunerationBean.getIdRemuneration());

				remunerationPersistant.setIdRemuneration(remunerationBean
						.getIdRemuneration());
			}

			session.delete(remunerationPersistant);
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

	public void deleteRemunerationWithoutTransaction(
			RemunerationBean remunerationBean) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Remuneration remunerationPersistant = new Remuneration();
			if (remunerationBean.getIdRemuneration() != 0) {
				remunerationPersistant = (Remuneration) session.load(
						Remuneration.class,
						remunerationBean.getIdRemuneration());

				remunerationPersistant.setIdRemuneration(remunerationBean
						.getIdRemuneration());
			}

			session.delete(remunerationPersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

}
