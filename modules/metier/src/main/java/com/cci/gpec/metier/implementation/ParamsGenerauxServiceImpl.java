package com.cci.gpec.metier.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.ParamsGenerauxBean;
import com.cci.gpec.commons.ParamsGenerauxBeanExport;
import com.cci.gpec.db.Entreprise;
import com.cci.gpec.db.Paramsgeneraux;
import com.cci.gpec.db.connection.HibernateUtil;

public class ParamsGenerauxServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParamsGenerauxServiceImpl.class);

	public List<ParamsGenerauxBean> getParamsGenerauxBeanList(int idGroupe)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<ParamsGenerauxBean> paramsGenerauxBeanList;
			transaction = session.beginTransaction();

			List<Paramsgeneraux> paramsGeneraux = session.createQuery(
					"from Paramsgeneraux as p left join fetch p.Entreprise as e where e.Groupe="
							+ idGroupe).list();

			paramsGenerauxBeanList = paramsGenerauxPersistantListToParamsGenerauxBeanList(paramsGeneraux);
			transaction.commit();

			return paramsGenerauxBeanList;

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

	public ParamsGenerauxBean getParamsGenerauxBeanByIdEntreprise(
			Integer idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			ParamsGenerauxBean paramsGenerauxBean;
			transaction = session.beginTransaction();

			Paramsgeneraux paramsGeneraux = (Paramsgeneraux) session
					.createQuery(
							"from Paramsgeneraux where Entreprise="
									+ idEntreprise).uniqueResult();

			if (paramsGeneraux != null) {
				paramsGenerauxBean = paramsGenerauxToParamsGenerauxBean(paramsGeneraux);
			} else {
				paramsGenerauxBean = null;
			}
			transaction.commit();

			return paramsGenerauxBean;

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

	public List<ParamsGenerauxBean> getParamsGenerauxBeanListByIdEntreprise(
			Integer idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<ParamsGenerauxBean> paramsGenerauxBeanList;
			transaction = session.beginTransaction();

			List<Paramsgeneraux> paramsGenerauxList = session.createQuery(
					"from Paramsgeneraux where Entreprise=" + idEntreprise)
					.list();

			paramsGenerauxBeanList = paramsGenerauxPersistantListToParamsGenerauxBeanList(paramsGenerauxList);
			transaction.commit();

			return paramsGenerauxBeanList;

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

	public List<ParamsGenerauxBeanExport> getParamsGenerauxBeanExportListByIdEntreprise(
			Integer idEntreprise) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			List<ParamsGenerauxBeanExport> paramsGenerauxBeanList;
			transaction = session.beginTransaction();

			List<Paramsgeneraux> paramsGenerauxList = session.createQuery(
					"from Paramsgeneraux where Entreprise=" + idEntreprise)
					.list();

			paramsGenerauxBeanList = paramsGenerauxPersistantListToParamsGenerauxBeanExportList(paramsGenerauxList);
			transaction.commit();

			return paramsGenerauxBeanList;

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

	public void saveOrUppdate(ParamsGenerauxBean paramsGenerauxBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Paramsgeneraux paramsGenerauxPersistant = new Paramsgeneraux();
			transaction = session.beginTransaction();
			if (paramsGenerauxBean.getId() != 0) {
				paramsGenerauxPersistant = (Paramsgeneraux) session.load(
						Paramsgeneraux.class, paramsGenerauxBean.getId());

			}

			paramsGenerauxPersistant.setAgeLegalRetraiteAnN(paramsGenerauxBean
					.getAgeLegalRetraiteAnN());
			paramsGenerauxPersistant.setAgeLegalRetraiteAnN1(paramsGenerauxBean
					.getAgeLegalRetraiteAnN1());
			paramsGenerauxPersistant.setAgeLegalRetraiteAnN2(paramsGenerauxBean
					.getAgeLegalRetraiteAnN2());

			paramsGenerauxPersistant
					.setDureeTravailAnNJoursEffectifTot(paramsGenerauxBean
							.getDureeTravailAnNJoursEffectifTot());
			paramsGenerauxPersistant
					.setDureeTravailAnN1JoursEffectifTot(paramsGenerauxBean
							.getDureeTravailAnN1JoursEffectifTot());
			paramsGenerauxPersistant
					.setDureeTravailAnN2JoursEffectifTot(paramsGenerauxBean
							.getDureeTravailAnN2JoursEffectifTot());

			paramsGenerauxPersistant
					.setDureeTravailAnNHeuresSal(paramsGenerauxBean
							.getDureeTravailAnNHeuresSal());
			paramsGenerauxPersistant
					.setDureeTravailAnN1HeuresSal(paramsGenerauxBean
							.getDureeTravailAnN1HeuresSal());
			paramsGenerauxPersistant
					.setDureeTravailAnN2HeuresSal(paramsGenerauxBean
							.getDureeTravailAnN2HeuresSal());

			paramsGenerauxPersistant
					.setDureeTravailAnNHeuresRealiseesEffectifTot(paramsGenerauxBean
							.getDureeTravailAnNHeuresRealiseesEffectifTot());
			paramsGenerauxPersistant
					.setDureeTravailAnN1HeuresRealiseesEffectifTot(paramsGenerauxBean
							.getDureeTravailAnN1HeuresRealiseesEffectifTot());
			paramsGenerauxPersistant
					.setDureeTravailAnN2HeuresRealiseesEffectifTot(paramsGenerauxBean
							.getDureeTravailAnN2HeuresRealiseesEffectifTot());

			paramsGenerauxPersistant
					.setDureeTravailAnNJoursSal(paramsGenerauxBean
							.getDureeTravailAnNJoursSal());
			paramsGenerauxPersistant
					.setDureeTravailAnN1JoursSal(paramsGenerauxBean
							.getDureeTravailAnN1JoursSal());
			paramsGenerauxPersistant
					.setDureeTravailAnN2JoursSal(paramsGenerauxBean
							.getDureeTravailAnN2JoursSal());

			paramsGenerauxPersistant.setEffectifMoyenAnN(paramsGenerauxBean
					.getEffectifMoyenAnN());
			paramsGenerauxPersistant.setEffectifMoyenAnN1(paramsGenerauxBean
					.getEffectifMoyenAnN1());
			paramsGenerauxPersistant.setEffectifMoyenAnN2(paramsGenerauxBean
					.getEffectifMoyenAnN2());

			paramsGenerauxPersistant.setMasseSalarialeAnN(paramsGenerauxBean
					.getMasseSalarialeAnN());
			paramsGenerauxPersistant.setMasseSalarialeAnN1(paramsGenerauxBean
					.getMasseSalarialeAnN1());
			paramsGenerauxPersistant.setMasseSalarialeAnN2(paramsGenerauxBean
					.getMasseSalarialeAnN2());

			paramsGenerauxPersistant
					.setPourcentageFormationAnN(paramsGenerauxBean
							.getPourcentageFormationAnN());
			paramsGenerauxPersistant
					.setPourcentageFormationAnN1(paramsGenerauxBean
							.getPourcentageFormationAnN1());
			paramsGenerauxPersistant
					.setPourcentageFormationAnN2(paramsGenerauxBean
							.getPourcentageFormationAnN2());

			paramsGenerauxPersistant.setEffectifPhysiqueAnN(paramsGenerauxBean
					.getEffectifPhysiqueAnN());
			paramsGenerauxPersistant.setEffectifPhysiqueAnN1(paramsGenerauxBean
					.getEffectifPhysiqueAnN1());
			paramsGenerauxPersistant.setEffectifPhysiqueAnN2(paramsGenerauxBean
					.getEffectifPhysiqueAnN2());

			paramsGenerauxPersistant.setEffectifEtpAnN(paramsGenerauxBean
					.getEffectifEtpAnN());
			paramsGenerauxPersistant.setEffectifEtpAnN1(paramsGenerauxBean
					.getEffectifEtpAnN1());
			paramsGenerauxPersistant.setEffectifEtpAnN2(paramsGenerauxBean
					.getEffectifEtpAnN2());

			Entreprise entreprisePersistant = (Entreprise) session.load(
					Entreprise.class, paramsGenerauxBean.getIdEntreprise());
			paramsGenerauxPersistant.setEntreprise(entreprisePersistant);

			paramsGenerauxPersistant.setAnnee(paramsGenerauxBean.getAnnee());

			session.saveOrUpdate(paramsGenerauxPersistant);
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

	public void save(ParamsGenerauxBean paramsGenerauxBean) {

		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Paramsgeneraux paramsGenerauxPersistant = new Paramsgeneraux();
			paramsGenerauxPersistant.setId(paramsGenerauxBean.getId());

			paramsGenerauxPersistant.setAgeLegalRetraiteAnN(paramsGenerauxBean
					.getAgeLegalRetraiteAnN());
			paramsGenerauxPersistant.setAgeLegalRetraiteAnN1(paramsGenerauxBean
					.getAgeLegalRetraiteAnN1());
			paramsGenerauxPersistant.setAgeLegalRetraiteAnN2(paramsGenerauxBean
					.getAgeLegalRetraiteAnN2());

			paramsGenerauxPersistant
					.setDureeTravailAnNJoursEffectifTot(paramsGenerauxBean
							.getDureeTravailAnNJoursEffectifTot());
			paramsGenerauxPersistant
					.setDureeTravailAnN1JoursEffectifTot(paramsGenerauxBean
							.getDureeTravailAnN1JoursEffectifTot());
			paramsGenerauxPersistant
					.setDureeTravailAnN2JoursEffectifTot(paramsGenerauxBean
							.getDureeTravailAnN2JoursEffectifTot());

			paramsGenerauxPersistant
					.setDureeTravailAnNHeuresSal(paramsGenerauxBean
							.getDureeTravailAnNHeuresSal());
			paramsGenerauxPersistant
					.setDureeTravailAnN1HeuresSal(paramsGenerauxBean
							.getDureeTravailAnN1HeuresSal());
			paramsGenerauxPersistant
					.setDureeTravailAnN2HeuresSal(paramsGenerauxBean
							.getDureeTravailAnN2HeuresSal());

			paramsGenerauxPersistant
					.setDureeTravailAnNHeuresRealiseesEffectifTot(paramsGenerauxBean
							.getDureeTravailAnNHeuresRealiseesEffectifTot());
			paramsGenerauxPersistant
					.setDureeTravailAnN1HeuresRealiseesEffectifTot(paramsGenerauxBean
							.getDureeTravailAnN1HeuresRealiseesEffectifTot());
			paramsGenerauxPersistant
					.setDureeTravailAnN2HeuresRealiseesEffectifTot(paramsGenerauxBean
							.getDureeTravailAnN2HeuresRealiseesEffectifTot());

			paramsGenerauxPersistant
					.setDureeTravailAnNJoursSal(paramsGenerauxBean
							.getDureeTravailAnNJoursSal());
			paramsGenerauxPersistant
					.setDureeTravailAnN1JoursSal(paramsGenerauxBean
							.getDureeTravailAnN1JoursSal());
			paramsGenerauxPersistant
					.setDureeTravailAnN2JoursSal(paramsGenerauxBean
							.getDureeTravailAnN2JoursSal());

			paramsGenerauxPersistant.setEffectifMoyenAnN(paramsGenerauxBean
					.getEffectifMoyenAnN());
			paramsGenerauxPersistant.setEffectifMoyenAnN1(paramsGenerauxBean
					.getEffectifMoyenAnN1());
			paramsGenerauxPersistant.setEffectifMoyenAnN2(paramsGenerauxBean
					.getEffectifMoyenAnN2());

			paramsGenerauxPersistant.setMasseSalarialeAnN(paramsGenerauxBean
					.getMasseSalarialeAnN());
			paramsGenerauxPersistant.setMasseSalarialeAnN1(paramsGenerauxBean
					.getMasseSalarialeAnN1());
			paramsGenerauxPersistant.setMasseSalarialeAnN2(paramsGenerauxBean
					.getMasseSalarialeAnN2());

			paramsGenerauxPersistant
					.setPourcentageFormationAnN(paramsGenerauxBean
							.getPourcentageFormationAnN());
			paramsGenerauxPersistant
					.setPourcentageFormationAnN1(paramsGenerauxBean
							.getPourcentageFormationAnN1());
			paramsGenerauxPersistant
					.setPourcentageFormationAnN2(paramsGenerauxBean
							.getPourcentageFormationAnN2());

			paramsGenerauxPersistant.setEffectifPhysiqueAnN(paramsGenerauxBean
					.getEffectifPhysiqueAnN());
			paramsGenerauxPersistant.setEffectifPhysiqueAnN1(paramsGenerauxBean
					.getEffectifPhysiqueAnN1());
			paramsGenerauxPersistant.setEffectifPhysiqueAnN2(paramsGenerauxBean
					.getEffectifPhysiqueAnN2());

			paramsGenerauxPersistant.setEffectifEtpAnN(paramsGenerauxBean
					.getEffectifEtpAnN());
			paramsGenerauxPersistant.setEffectifEtpAnN1(paramsGenerauxBean
					.getEffectifEtpAnN1());
			paramsGenerauxPersistant.setEffectifEtpAnN2(paramsGenerauxBean
					.getEffectifEtpAnN2());

			paramsGenerauxPersistant.setAnnee(paramsGenerauxBean.getAnnee());

			Entreprise entreprisePersistant = (Entreprise) session.load(
					Entreprise.class, paramsGenerauxBean.getIdEntreprise());
			paramsGenerauxPersistant.setEntreprise(entreprisePersistant);

			session.save(paramsGenerauxPersistant);
			paramsGenerauxBean.setId(paramsGenerauxPersistant.getId());
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

	public List<ParamsGenerauxBean> paramsGenerauxPersistantListToParamsGenerauxBeanList(
			List<Paramsgeneraux> paramsGenerauxPersistantList) throws Exception {

		List<ParamsGenerauxBean> paramsGenerauxBeanList = new ArrayList<ParamsGenerauxBean>();

		for (Paramsgeneraux paramsGenerauxPersistant : paramsGenerauxPersistantList) {

			paramsGenerauxBeanList
					.add(paramsGenerauxToParamsGenerauxBean(paramsGenerauxPersistant));
		}

		return paramsGenerauxBeanList;

	}

	public List<ParamsGenerauxBeanExport> paramsGenerauxPersistantListToParamsGenerauxBeanExportList(
			List<Paramsgeneraux> paramsGenerauxPersistantList) throws Exception {

		List<ParamsGenerauxBeanExport> paramsGenerauxBeanExportList = new ArrayList<ParamsGenerauxBeanExport>();

		for (Paramsgeneraux paramsGenerauxPersistant : paramsGenerauxPersistantList) {

			paramsGenerauxBeanExportList
					.add(paramsGenerauxToParamsGenerauxBeanExport(paramsGenerauxPersistant));
		}

		return paramsGenerauxBeanExportList;

	}

	public ParamsGenerauxBean paramsGenerauxToParamsGenerauxBean(
			Paramsgeneraux paramsGenerauxPersistant) throws Exception {

		ParamsGenerauxBean paramsGenerauxBean = new ParamsGenerauxBean();

		paramsGenerauxBean.setId(paramsGenerauxPersistant.getId());
		paramsGenerauxBean.setAnnee(paramsGenerauxPersistant.getAnnee());
		paramsGenerauxBean.setAgeLegalRetraiteAnN(paramsGenerauxPersistant
				.getAgeLegalRetraiteAnN());
		paramsGenerauxBean.setAgeLegalRetraiteAnN1(paramsGenerauxPersistant
				.getAgeLegalRetraiteAnN1());
		paramsGenerauxBean.setAgeLegalRetraiteAnN2(paramsGenerauxPersistant
				.getAgeLegalRetraiteAnN2());

		paramsGenerauxBean
				.setDureeTravailAnNJoursEffectifTot(paramsGenerauxPersistant
						.getDureeTravailAnNJoursEffectifTot());
		paramsGenerauxBean
				.setDureeTravailAnN1JoursEffectifTot(paramsGenerauxPersistant
						.getDureeTravailAnN1JoursEffectifTot());
		paramsGenerauxBean
				.setDureeTravailAnN2JoursEffectifTot(paramsGenerauxPersistant
						.getDureeTravailAnN2JoursEffectifTot());

		paramsGenerauxBean.setDureeTravailAnNHeuresSal(paramsGenerauxPersistant
				.getDureeTravailAnNHeuresSal());
		paramsGenerauxBean
				.setDureeTravailAnN1HeuresSal(paramsGenerauxPersistant
						.getDureeTravailAnN1HeuresSal());
		paramsGenerauxBean
				.setDureeTravailAnN2HeuresSal(paramsGenerauxPersistant
						.getDureeTravailAnN2HeuresSal());

		paramsGenerauxBean
				.setDureeTravailAnNHeuresRealiseesEffectifTot(paramsGenerauxPersistant
						.getDureeTravailAnNHeuresRealiseesEffectifTot());
		paramsGenerauxBean
				.setDureeTravailAnN1HeuresRealiseesEffectifTot(paramsGenerauxPersistant
						.getDureeTravailAnN1HeuresRealiseesEffectifTot());
		paramsGenerauxBean
				.setDureeTravailAnN2HeuresRealiseesEffectifTot(paramsGenerauxPersistant
						.getDureeTravailAnN2HeuresRealiseesEffectifTot());

		paramsGenerauxBean.setDureeTravailAnNJoursSal(paramsGenerauxPersistant
				.getDureeTravailAnNJoursSal());
		paramsGenerauxBean.setDureeTravailAnN1JoursSal(paramsGenerauxPersistant
				.getDureeTravailAnN1JoursSal());
		paramsGenerauxBean.setDureeTravailAnN2JoursSal(paramsGenerauxPersistant
				.getDureeTravailAnN2JoursSal());

		paramsGenerauxBean.setEffectifMoyenAnN(paramsGenerauxPersistant
				.getEffectifMoyenAnN());
		paramsGenerauxBean.setEffectifMoyenAnN1(paramsGenerauxPersistant
				.getEffectifMoyenAnN1());
		paramsGenerauxBean.setEffectifMoyenAnN2(paramsGenerauxPersistant
				.getEffectifMoyenAnN2());

		paramsGenerauxBean.setMasseSalarialeAnN(paramsGenerauxPersistant
				.getMasseSalarialeAnN());
		paramsGenerauxBean.setMasseSalarialeAnN1(paramsGenerauxPersistant
				.getMasseSalarialeAnN1());
		paramsGenerauxBean.setMasseSalarialeAnN2(paramsGenerauxPersistant
				.getMasseSalarialeAnN2());

		paramsGenerauxBean.setPourcentageFormationAnN(paramsGenerauxPersistant
				.getPourcentageFormationAnN());
		paramsGenerauxBean.setPourcentageFormationAnN1(paramsGenerauxPersistant
				.getPourcentageFormationAnN1());
		paramsGenerauxBean.setPourcentageFormationAnN2(paramsGenerauxPersistant
				.getPourcentageFormationAnN2());

		paramsGenerauxBean.setEffectifPhysiqueAnN(paramsGenerauxPersistant
				.getEffectifPhysiqueAnN());
		paramsGenerauxBean.setEffectifPhysiqueAnN1(paramsGenerauxPersistant
				.getEffectifPhysiqueAnN1());
		paramsGenerauxBean.setEffectifPhysiqueAnN2(paramsGenerauxPersistant
				.getEffectifPhysiqueAnN2());

		paramsGenerauxBean.setEffectifEtpAnN(paramsGenerauxPersistant
				.getEffectifEtpAnN());
		paramsGenerauxBean.setEffectifEtpAnN1(paramsGenerauxPersistant
				.getEffectifEtpAnN1());
		paramsGenerauxBean.setEffectifEtpAnN2(paramsGenerauxPersistant
				.getEffectifEtpAnN2());

		if (paramsGenerauxPersistant.getEntreprise() != null) {
			paramsGenerauxBean.setIdEntreprise(paramsGenerauxPersistant
					.getEntreprise().getId());
		}

		return paramsGenerauxBean;
	}

	public ParamsGenerauxBeanExport paramsGenerauxToParamsGenerauxBeanExport(
			Paramsgeneraux paramsGenerauxPersistant) throws Exception {

		ParamsGenerauxBeanExport paramsGenerauxBeanExport = new ParamsGenerauxBeanExport();

		paramsGenerauxBeanExport.setId(paramsGenerauxPersistant.getId());
		paramsGenerauxBeanExport
				.setAgeLegalRetraiteAnN(paramsGenerauxPersistant
						.getAgeLegalRetraiteAnN());
		paramsGenerauxBeanExport
				.setAgeLegalRetraiteAnN1(paramsGenerauxPersistant
						.getAgeLegalRetraiteAnN1());
		paramsGenerauxBeanExport
				.setAgeLegalRetraiteAnN2(paramsGenerauxPersistant
						.getAgeLegalRetraiteAnN2());

		if (paramsGenerauxPersistant.getDureeTravailAnNJoursEffectifTot() != null) {

			paramsGenerauxBeanExport
					.setDureeTravailAnNJoursEffectifTot(paramsGenerauxPersistant
							.getDureeTravailAnNJoursEffectifTot().toString());
		} else {
			paramsGenerauxBeanExport.setDureeTravailAnNJoursEffectifTot("");
		}

		if (paramsGenerauxPersistant.getDureeTravailAnN1JoursEffectifTot() != null) {
			paramsGenerauxBeanExport
					.setDureeTravailAnN1JoursEffectifTot(paramsGenerauxPersistant
							.getDureeTravailAnN1JoursEffectifTot().toString());
		} else {
			paramsGenerauxBeanExport.setDureeTravailAnN1JoursEffectifTot("");
		}

		if (paramsGenerauxPersistant.getDureeTravailAnN2JoursEffectifTot() != null) {
			paramsGenerauxBeanExport
					.setDureeTravailAnN2JoursEffectifTot(paramsGenerauxPersistant
							.getDureeTravailAnN2JoursEffectifTot().toString());
		} else {
			paramsGenerauxBeanExport.setDureeTravailAnN2JoursEffectifTot("");
		}

		paramsGenerauxBeanExport
				.setDureeTravailAnNHeuresSal(paramsGenerauxPersistant
						.getDureeTravailAnNHeuresSal());
		paramsGenerauxBeanExport
				.setDureeTravailAnN1HeuresSal(paramsGenerauxPersistant
						.getDureeTravailAnN1HeuresSal());
		paramsGenerauxBeanExport
				.setDureeTravailAnN2HeuresSal(paramsGenerauxPersistant
						.getDureeTravailAnN2HeuresSal());

		if (paramsGenerauxPersistant
				.getDureeTravailAnNHeuresRealiseesEffectifTot() != null) {
			paramsGenerauxBeanExport
					.setDureeTravailAnNHeuresRealiseesEffectifTot(paramsGenerauxPersistant
							.getDureeTravailAnNHeuresRealiseesEffectifTot()
							.toString());
		} else {
			paramsGenerauxBeanExport
					.setDureeTravailAnNHeuresRealiseesEffectifTot("");
		}

		if (paramsGenerauxPersistant
				.getDureeTravailAnN1HeuresRealiseesEffectifTot() != null) {
			paramsGenerauxBeanExport
					.setDureeTravailAnN1HeuresRealiseesEffectifTot(paramsGenerauxPersistant
							.getDureeTravailAnN1HeuresRealiseesEffectifTot()
							.toString());
		} else {
			paramsGenerauxBeanExport
					.setDureeTravailAnN1HeuresRealiseesEffectifTot("");
		}

		if (paramsGenerauxPersistant
				.getDureeTravailAnN2HeuresRealiseesEffectifTot() != null) {
			paramsGenerauxBeanExport
					.setDureeTravailAnN2HeuresRealiseesEffectifTot(paramsGenerauxPersistant
							.getDureeTravailAnN2HeuresRealiseesEffectifTot()
							.toString());
		} else {
			paramsGenerauxBeanExport
					.setDureeTravailAnN2HeuresRealiseesEffectifTot("");
		}

		paramsGenerauxBeanExport
				.setDureeTravailAnNJoursSal(paramsGenerauxPersistant
						.getDureeTravailAnNJoursSal());
		paramsGenerauxBeanExport
				.setDureeTravailAnN1JoursSal(paramsGenerauxPersistant
						.getDureeTravailAnN1JoursSal());
		paramsGenerauxBeanExport
				.setDureeTravailAnN2JoursSal(paramsGenerauxPersistant
						.getDureeTravailAnN2JoursSal());

		if (paramsGenerauxPersistant.getEffectifMoyenAnN() != null) {
			paramsGenerauxBeanExport
					.setEffectifMoyenAnN(paramsGenerauxPersistant
							.getEffectifMoyenAnN().toString());
		} else {
			paramsGenerauxBeanExport.setEffectifMoyenAnN("");
		}

		if (paramsGenerauxPersistant.getEffectifMoyenAnN1() != null) {
			paramsGenerauxBeanExport
					.setEffectifMoyenAnN1(paramsGenerauxPersistant
							.getEffectifMoyenAnN1().toString());
		} else {
			paramsGenerauxBeanExport.setEffectifMoyenAnN1("");
		}

		if (paramsGenerauxPersistant.getEffectifMoyenAnN2() != null) {
			paramsGenerauxBeanExport
					.setEffectifMoyenAnN2(paramsGenerauxPersistant
							.getEffectifMoyenAnN2().toString());
		} else {
			paramsGenerauxBeanExport.setEffectifMoyenAnN2("");
		}

		if (paramsGenerauxPersistant.getMasseSalarialeAnN() != null) {
			paramsGenerauxBeanExport
					.setMasseSalarialeAnN(paramsGenerauxPersistant
							.getMasseSalarialeAnN().toString());
		} else {
			paramsGenerauxBeanExport.setMasseSalarialeAnN("");
		}

		if (paramsGenerauxPersistant.getMasseSalarialeAnN1() != null) {
			paramsGenerauxBeanExport
					.setMasseSalarialeAnN1(paramsGenerauxPersistant
							.getMasseSalarialeAnN1().toString());
		} else {
			paramsGenerauxBeanExport.setMasseSalarialeAnN1("");
		}

		if (paramsGenerauxPersistant.getMasseSalarialeAnN2() != null) {
			paramsGenerauxBeanExport
					.setMasseSalarialeAnN2(paramsGenerauxPersistant
							.getMasseSalarialeAnN2().toString());
		} else {
			paramsGenerauxBeanExport.setMasseSalarialeAnN2("");
		}

		if (paramsGenerauxPersistant.getPourcentageFormationAnN() != null) {
			paramsGenerauxBeanExport
					.setPourcentageFormationAnN(paramsGenerauxPersistant
							.getPourcentageFormationAnN().toString());
		} else {
			paramsGenerauxBeanExport.setPourcentageFormationAnN("");
		}

		if (paramsGenerauxPersistant.getPourcentageFormationAnN1() != null) {
			paramsGenerauxBeanExport
					.setPourcentageFormationAnN1(paramsGenerauxPersistant
							.getPourcentageFormationAnN1().toString());
		} else {
			paramsGenerauxBeanExport.setPourcentageFormationAnN1("");
		}

		if (paramsGenerauxPersistant.getPourcentageFormationAnN2() != null) {
			paramsGenerauxBeanExport
					.setPourcentageFormationAnN2(paramsGenerauxPersistant
							.getPourcentageFormationAnN2().toString());
		} else {
			paramsGenerauxBeanExport.setPourcentageFormationAnN2("");
		}

		paramsGenerauxBeanExport
				.setEffectifPhysiqueAnN(paramsGenerauxPersistant
						.getEffectifPhysiqueAnN());
		paramsGenerauxBeanExport
				.setEffectifPhysiqueAnN1(paramsGenerauxPersistant
						.getEffectifPhysiqueAnN1());
		paramsGenerauxBeanExport
				.setEffectifPhysiqueAnN2(paramsGenerauxPersistant
						.getEffectifPhysiqueAnN2());

		if (paramsGenerauxPersistant.getEffectifEtpAnN() != null) {
			paramsGenerauxBeanExport.setEffectifEtpAnN(paramsGenerauxPersistant
					.getEffectifEtpAnN().toString());
		} else {
			paramsGenerauxBeanExport.setEffectifEtpAnN("");
		}

		if (paramsGenerauxPersistant.getEffectifEtpAnN1() != null) {
			paramsGenerauxBeanExport
					.setEffectifEtpAnN1(paramsGenerauxPersistant
							.getEffectifEtpAnN1().toString());
		} else {
			paramsGenerauxBeanExport.setEffectifEtpAnN1("");
		}

		if (paramsGenerauxPersistant.getEffectifEtpAnN2() != null) {
			paramsGenerauxBeanExport
					.setEffectifEtpAnN2(paramsGenerauxPersistant
							.getEffectifEtpAnN2().toString());
		} else {
			paramsGenerauxBeanExport.setEffectifEtpAnN2("");
		}

		if (paramsGenerauxPersistant.getEntreprise() != null) {
			paramsGenerauxBeanExport.setIdEntreprise(paramsGenerauxPersistant
					.getEntreprise().getId());
		}

		return paramsGenerauxBeanExport;
	}

	public void suppression(int idEntreprise) throws Exception {
		Session session = null;
		// Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			String hql = "delete from Paramsgeneraux where Entreprise="
					+ idEntreprise;
			Query query = session.createQuery(hql);
			query.executeUpdate();

		} catch (HibernateException e) {
			// if (transaction != null && !transaction.wasRolledBack()) {
			// 	transaction.rollback();
			// }
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}

	}

	public void deleteParamsGenerauxWithoutTransaction(
			ParamsGenerauxBean paramsGenerauxBean) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Paramsgeneraux paramsGenerauxPersistant = new Paramsgeneraux();
			if (paramsGenerauxBean.getId() != 0) {
				paramsGenerauxPersistant = (Paramsgeneraux) session.load(
						Paramsgeneraux.class, paramsGenerauxBean.getId());

				paramsGenerauxPersistant.setId(paramsGenerauxBean.getId());
			}

			session.delete(paramsGenerauxPersistant);

		} catch (HibernateException e) {
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		}
	}

}
