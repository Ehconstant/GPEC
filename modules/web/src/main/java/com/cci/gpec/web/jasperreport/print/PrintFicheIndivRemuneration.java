package com.cci.gpec.web.jasperreport.print;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.cci.gpec.commons.LienRemunerationRevenuBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.RemunerationBean;
import com.cci.gpec.commons.RemunerationBeanExport;
import com.cci.gpec.commons.RevenuComplementaireBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.LienRemunerationRevenuServiceImpl;
import com.cci.gpec.metier.implementation.RemunerationServiceImpl;
import com.cci.gpec.metier.implementation.RevenuComplementaireServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.web.Utils;

public class PrintFicheIndivRemuneration extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			printFicheIndivRemuneration(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 704992979558623612L;

	/*
	 */
	public String printFicheIndivRemuneration(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {

			Integer idEntreprise = (Integer) request.getSession().getAttribute(
					"idEntreprise");

			Integer idSalarie = (Integer) request.getSession().getAttribute(
					"idSalarie");

			Integer idGroupe = (Integer) request.getSession().getAttribute(
					"groupe");

			String nomGroupe = request.getSession().getAttribute("nomGroupe")
					.toString();

			Integer annee = (Integer) request.getSession()
					.getAttribute("annee");

			DecimalFormat df = new DecimalFormat();
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setDecimalSeparator('.');
			df.setDecimalFormatSymbols(symbols);
			df.applyPattern("0.00");

			List<LienRemunerationRevenuBean> commissionInventoryLien = new ArrayList<LienRemunerationRevenuBean>();
			List<LienRemunerationRevenuBean> primeFixeInventoryLien = new ArrayList<LienRemunerationRevenuBean>();
			List<LienRemunerationRevenuBean> primeVariableInventoryLien = new ArrayList<LienRemunerationRevenuBean>();
			List<LienRemunerationRevenuBean> avantageAssujettiInventoryLien = new ArrayList<LienRemunerationRevenuBean>();
			List<LienRemunerationRevenuBean> avantageNonAssujettiInventoryLien = new ArrayList<LienRemunerationRevenuBean>();
			List<LienRemunerationRevenuBean> fraisProfInventoryLien = new ArrayList<LienRemunerationRevenuBean>();

			RemunerationServiceImpl remuService = new RemunerationServiceImpl();

			ArrayList<RemunerationBeanExport> remunerationBeanExportList = new ArrayList<RemunerationBeanExport>();

			RemunerationBeanExport remu = remuService.getRemunerationExport(
					annee, idSalarie).get(0);

			List<RemunerationBean> remuNPrec = remuService.getRemuneration(
					(annee - 1), idSalarie);

			if (!remuNPrec.isEmpty()) {
				RemunerationBean actu = remuNPrec.get(0);

				if (actu.getSalaireMinimumConventionnelActualisation() != 0) {
					remu.setSalaireMinimumConventionnelNPrec(df.format(actu
							.getSalaireMinimumConventionnelActualisation()));

				} else {
					remu.setSalaireMinimumConventionnelNPrec(df.format(actu
							.getSalaireMinimumConventionnel()));
				}

				if (actu.getSalaireDeBaseActualisation() != 0) {
					remu.setSalaireDeBaseNPrec(df.format(actu
							.getSalaireDeBaseActualisation()));
				} else {
					remu.setSalaireDeBaseNPrec(df.format(actu
							.getSalaireDeBase()));
				}

				if (actu.getAugmentationCollectiveActualisation() != 0) {
					remu.setAugmentationCollectiveNPrec(df.format(actu
							.getAugmentationCollectiveActualisation()));
				} else {
					remu.setAugmentationCollectiveNPrec(df.format(actu
							.getAugmentationCollective()));
				}

				if (actu.getAugmentationIndividuelleActualisation() != 0) {
					remu.setAugmentationIndividuelleNPrec(df.format(actu
							.getAugmentationIndividuelleActualisation()));
				} else {
					remu.setAugmentationIndividuelleNPrec(df.format(actu
							.getAugmentationIndividuelle()));
				}

				if (actu.getHeuresSup25Actualisation() != 0) {
					remu.setHeuresSup25NPrec(df.format(actu
							.getHeuresSup25Actualisation()));
				} else {
					remu.setHeuresSup25NPrec(df.format(actu.getHeuresSup25()));
				}

				if (actu.getHeuresSup50Actualisation() != 0) {
					remu.setHeuresSup50NPrec(df.format(actu
							.getHeuresSup50Actualisation()));

				} else {
					remu.setHeuresSup50NPrec(df.format(actu.getHeuresSup50()));
				}

				double sommeNPrec = Double
						.valueOf(remu.getSalaireDeBaseNPrec())
						+ Double.valueOf(remu.getAugmentationCollectiveNPrec())
						+ Double.valueOf(remu
								.getAugmentationIndividuelleNPrec())
						+ Double.valueOf(remu.getHeuresSup25NPrec())
						+ Double.valueOf(remu.getHeuresSup50NPrec());
				remu.setBaseBruteAnnuelleNPrec(df.format(sommeNPrec));

				if (Double.valueOf(remu.getSalaireMinimumConventionnelNPrec()) != 0
						&& (Double.valueOf(remu
								.getSalaireMinimumConventionnel()) != 0 || Double
								.valueOf(remu
										.getSalaireMinimumConventionnelActualisation()) != 0)) {
					if (Double.valueOf(remu
							.getSalaireMinimumConventionnelActualisation()) == 0) {
						remu.setSalaireMinimumConventionnelEvol(df.format(Double.valueOf(((((Double
								.valueOf(remu.getSalaireMinimumConventionnel()) - Double
								.valueOf(remu
										.getSalaireMinimumConventionnelNPrec())) / Double
								.valueOf(remu
										.getSalaireMinimumConventionnelNPrec())) * 100) * 100) / 100)));
					} else {
						remu.setSalaireMinimumConventionnelEvol(df.format(Double.valueOf(((((Double.valueOf(remu
								.getSalaireMinimumConventionnelActualisation()) - Double
								.valueOf(remu
										.getSalaireMinimumConventionnelNPrec())) / Double
								.valueOf(remu
										.getSalaireMinimumConventionnelNPrec())) * 100) * 100) / 100)));
					}
				}

				if (Double.valueOf(remu.getSalaireDeBaseNPrec()) != 0
						&& (Double.valueOf(remu.getSalaireDeBase()) != 0 || Double
								.valueOf(remu.getSalaireDeBaseActualisation()) != 0)) {
					if (Double.valueOf(remu.getSalaireDeBaseActualisation()) == 0) {
						remu.setSalaireDeBaseEvol(df.format(((((Double
								.valueOf(remu.getSalaireDeBase()) - Double
								.valueOf(remu.getSalaireDeBaseNPrec())) / Double
								.valueOf(remu.getSalaireDeBaseNPrec())) * 100) * 100) / 100));
					} else {
						remu.setSalaireDeBaseEvol(df.format(((((Double
								.valueOf(remu.getSalaireDeBaseActualisation()) - Double
								.valueOf(remu.getSalaireDeBaseNPrec())) / Double
								.valueOf(remu.getSalaireDeBaseNPrec())) * 100) * 100) / 100));
					}
				}

				if (Double.valueOf(remu.getAugmentationCollectiveNPrec()) != 0
						&& (Double.valueOf(remu.getAugmentationCollective()) != 0 || Double
								.valueOf(remu
										.getAugmentationCollectiveActualisation()) != 0)) {
					if (Double.valueOf(remu
							.getAugmentationCollectiveActualisation()) == 0) {
						remu.setAugmentationCollectiveEvol(df.format(((((Double
								.valueOf(remu.getAugmentationCollective()) - Double
								.valueOf(remu.getAugmentationCollectiveNPrec())) / Double
								.valueOf(remu.getAugmentationCollectiveNPrec())) * 100) * 100) / 100));
					} else {
						remu.setAugmentationCollectiveEvol(df.format(((((Double.valueOf(remu
								.getAugmentationCollectiveActualisation()) - Double
								.valueOf(remu.getAugmentationCollectiveNPrec())) / Double
								.valueOf(remu.getAugmentationCollectiveNPrec())) * 100) * 100) / 100));
					}
				}

				if (Double.valueOf(remu.getAugmentationIndividuelleNPrec()) != 0
						&& (Double.valueOf(remu.getAugmentationIndividuelle()) != 0 || Double
								.valueOf(remu
										.getAugmentationIndividuelleActualisation()) != 0)) {
					if (Double.valueOf(remu
							.getAugmentationIndividuelleActualisation()) == 0) {
						remu.setAugmentationIndividuelleEvol(df.format(((((Double
								.valueOf(remu.getAugmentationIndividuelle()) - Double
								.valueOf(remu
										.getAugmentationIndividuelleNPrec())) / Double
								.valueOf(remu
										.getAugmentationIndividuelleNPrec())) * 100) * 100) / 100));
					} else {
						remu.setAugmentationIndividuelleEvol(df.format(((((Double.valueOf(remu
								.getAugmentationIndividuelleActualisation()) - Double
								.valueOf(remu
										.getAugmentationIndividuelleNPrec())) / Double
								.valueOf(remu
										.getAugmentationIndividuelleNPrec())) * 100) * 100) / 100));
					}
				}

				if (Double.valueOf(remu.getHeuresSup25NPrec()) != 0
						&& (Double.valueOf(remu.getHeuresSup25()) != 0 || Double
								.valueOf(remu.getHeuresSup25Actualisation()) != 0)) {
					if (Double.valueOf(remu.getHeuresSup25Actualisation()) == 0) {
						remu.setHeuresSup25Evol(df.format(((((Double
								.valueOf(remu.getHeuresSup25()) - Double
								.valueOf(remu.getHeuresSup25NPrec())) / Double
								.valueOf(remu.getHeuresSup25NPrec())) * 100) * 100) / 100));
					} else {
						remu.setHeuresSup25Evol(df.format(((((Double
								.valueOf(remu.getHeuresSup25Actualisation()) - Double
								.valueOf(remu.getHeuresSup25NPrec())) / Double
								.valueOf(remu.getHeuresSup25NPrec())) * 100) * 100) / 100));
					}
				}

				if (Double.valueOf(remu.getHeuresSup50NPrec()) != 0
						&& (Double.valueOf(remu.getHeuresSup50()) != 0 || Double
								.valueOf(remu.getHeuresSup50Actualisation()) != 0)) {
					if (Double.valueOf(remu.getHeuresSup50Actualisation()) == 0) {
						remu.setHeuresSup50Evol(df.format(((((Double
								.valueOf(remu.getHeuresSup50()) - Double
								.valueOf(remu.getHeuresSup50NPrec())) / Double
								.valueOf(remu.getHeuresSup50NPrec())) * 100) * 100) / 100));
					} else {
						remu.setHeuresSup50Evol(df.format(((((Double
								.valueOf(remu.getHeuresSup50Actualisation()) - Double
								.valueOf(remu.getHeuresSup50NPrec())) / Double
								.valueOf(remu.getHeuresSup50NPrec())) * 100) * 100) / 100));
					}
				}
			}
			double somme = Double.valueOf(remu.getSalaireDeBase())
					+ Double.valueOf(remu.getAugmentationCollective())
					+ Double.valueOf(remu.getAugmentationIndividuelle())
					+ Double.valueOf(remu.getHeuresSup25())
					+ Double.valueOf(remu.getHeuresSup50());
			remu.setBaseBruteAnnuelle(df.format(somme));

			double s1 = 0.0;
			double s2 = 0.0;
			double s3 = 0.0;
			double s4 = 0.0;
			double s5 = 0.0;
			if (Double.valueOf(remu.getSalaireDeBaseActualisation()) == 0.0) {
				s1 = Double.valueOf(remu.getSalaireDeBase());
			} else {
				s1 = Double.valueOf(remu.getSalaireDeBaseActualisation());
			}

			if (Double.valueOf(remu.getAugmentationCollectiveActualisation()) == 0.0) {
				s2 = Double.valueOf(remu.getAugmentationCollective());
			} else {
				s2 = Double.valueOf(remu
						.getAugmentationCollectiveActualisation());
			}

			if (Double.valueOf(remu.getAugmentationIndividuelleActualisation()) == 0.0) {
				s3 = Double.valueOf(remu.getAugmentationIndividuelle());
			} else {
				s3 = Double.valueOf(remu
						.getAugmentationIndividuelleActualisation());
			}

			if (Double.valueOf(remu.getHeuresSup25Actualisation()) == 0.0) {
				s4 = Double.valueOf(remu.getHeuresSup25());
			} else {
				s4 = Double.valueOf(remu.getHeuresSup25Actualisation());
			}

			if (Double.valueOf(remu.getHeuresSup50Actualisation()) == 0.0) {
				s5 = Double.valueOf(remu.getHeuresSup50());
			} else {
				s5 = Double.valueOf(remu.getHeuresSup50Actualisation());
			}

			double sommeActualisation = s1 + s2 + s3 + s4 + s5;
			remu.setBaseBruteAnnuelleActu(df.format(sommeActualisation));
			if (!remuNPrec.isEmpty()) {
				if (Double.valueOf(remu.getBaseBruteAnnuelleNPrec()) != 0
						&& (Double.valueOf(remu.getBaseBruteAnnuelle()) != 0 || Double
								.valueOf(remu.getBaseBruteAnnuelleActu()) != 0)) {
					if (Double.valueOf(remu.getBaseBruteAnnuelleActu()) == 0) {
						double totalBrutEvol = ((Double.valueOf(remu
								.getBaseBruteAnnuelle()) - Double.valueOf(remu
								.getBaseBruteAnnuelleNPrec())) / Double
								.valueOf(remu.getBaseBruteAnnuelleNPrec())) * 100;
						double total = (double) ((totalBrutEvol * 100)) / 100;
						remu.setBaseBruteAnnuelleEvol(df.format(Double
								.valueOf(total)));
					} else {
						double totalBrutEvol = ((Double.valueOf(remu
								.getBaseBruteAnnuelleActu()) - Double
								.valueOf(remu.getBaseBruteAnnuelleNPrec())) / Double
								.valueOf(remu.getBaseBruteAnnuelleNPrec())) * 100;
						double total = (double) ((totalBrutEvol * 100)) / 100;
						remu.setBaseBruteAnnuelleEvol(df.format(Double
								.valueOf(total)));
					}
				}
			}

			remu.setTotalActuCommission("0.00");
			remu.setTotalNCommission("0.00");
			remu.setTotalNPrecCommission("0.00");

			remu.setTotalActuPrimeFixe("0.00");
			remu.setTotalNPrimeFixe("0.00");
			remu.setTotalNPrecPrimeFixe("0.00");

			remu.setTotalActuPrimeVariable("0.00");
			remu.setTotalNPrimeVariable("0.00");
			remu.setTotalNPrecPrimeVariable("0.00");

			remu.setTotalActuAvantageAssujetti("0.00");
			remu.setTotalNAvantageAssujetti("0.00");
			remu.setTotalNPrecAvantageAssujetti("0.00");

			remu.setTotalActuAvantageNonAssujetti("0.00");
			remu.setTotalNAvantageNonAssujetti("0.00");
			remu.setTotalNPrecAvantageNonAssujetti("0.00");

			remu.setTotalActuFraisProf("0.00");
			remu.setTotalNFraisProf("0.00");
			remu.setTotalNPrecFraisProf("0.00");

			// //Ajout des éléments qui remontent de l'année N-1
			List<RemunerationBean> rbi = remuService.getRemuneration(annee - 1,
					idSalarie);

			LienRemunerationRevenuServiceImpl serviceLien = new LienRemunerationRevenuServiceImpl();
			List<LienRemunerationRevenuBean> l = new ArrayList<LienRemunerationRevenuBean>();
			l = serviceLien.getLienRemunerationRevenuFromIdRemu(remu
					.getIdRemuneration());

			RevenuComplementaireServiceImpl serviceRevCom = new RevenuComplementaireServiceImpl();

			LienRemunerationRevenuServiceImpl lienServ = new LienRemunerationRevenuServiceImpl();
			RemunerationServiceImpl remuServ = new RemunerationServiceImpl();
			RevenuComplementaireServiceImpl servrc = new RevenuComplementaireServiceImpl();

			for (LienRemunerationRevenuBean lrr : l) {

				RevenuComplementaireBean rev = new RevenuComplementaireBean();

				rev = servrc.getRevenuComplementaireBeanById(lrr
						.getIdRevenuComplementaire());

				lrr.setLibelle(rev.getLibelle());

				RemunerationBean rr = new RemunerationBean();
				for (RemunerationBean r : remuServ
						.getRemunerationList(idGroupe)) {
					if (r.getIdRemuneration().equals(lrr.getIdRemuneration())) {
						rr = r;
					}
				}

				int sal = rr.getIdSalarie();

				int idRemu = 0;

				for (RemunerationBean r : remuServ
						.getRemunerationList(idGroupe)) {
					if (r.getAnnee() == (rr.getAnnee() - 1)
							&& r.getIdSalarie().equals(sal)) {
						idRemu = r.getIdRemuneration();
					}
				}

				if (idRemu == 0) {
					lrr.setMontantNPrec("0.00");
				} else {
					String montant = df.format(Double.valueOf(lienServ
							.getMontantNPrec(idRemu,
									lrr.getIdRevenuComplementaire())));
					lrr.setMontantNPrec(montant);
				}
				String montant = lrr.getMontant();
				lrr.setMontant(df.format(Double.valueOf(montant)));

				String actua = lrr.getActualisation();
				lrr.setActualisation(df.format(Double.valueOf(actua)));

				if (serviceRevCom.getType(lrr.getIdRevenuComplementaire(),
						idGroupe).equals("commission")) {
					lrr.setMontantNPrec(calculNPrec("commission",
							lrr.getLibelle(), annee, idSalarie, idGroupe));
					if (Double.valueOf(lrr.getActualisation()) == 0.0) {
						lrr.setActualisation(lrr.getMontant());
					}
					commissionInventoryLien.add(lrr);
					if (Double.valueOf(lrr.getActualisation()) == 0.0) {
						remu.setTotalActuCommission(df.format(Double
								.valueOf(remu.getTotalActuCommission())
								+ Double.valueOf(lrr.getMontant())));
					} else {
						remu.setTotalActuCommission(df.format(Double
								.valueOf(remu.getTotalActuCommission())
								+ Double.valueOf(lrr.getActualisation())));
					}

					if (Double.valueOf(lrr.getMontantNPrec()) != 0.0) {
						remu.setTotalNPrecCommission(df.format(Double
								.valueOf(remu.getTotalNPrecCommission())
								+ Double.valueOf(lrr.getMontantNPrec())));
					}

					remu.setTotalNCommission(df.format(Double.valueOf(remu
							.getTotalNCommission())
							+ Double.valueOf(lrr.getMontant())));
				}
				if (serviceRevCom.getType(lrr.getIdRevenuComplementaire(),
						idGroupe).equals("prime_fixe")) {
					lrr.setMontantNPrec(calculNPrec("prime_fixe",
							lrr.getLibelle(), annee, idSalarie, idGroupe));
					primeFixeInventoryLien.add(lrr);

					if (Double.valueOf(lrr.getActualisation()) != 0.0) {
						remu.setTotalActuPrimeFixe(df.format(Double
								.valueOf(remu.getTotalActuPrimeFixe())
								+ Double.valueOf(lrr.getActualisation())));
					} else {
						remu.setTotalActuPrimeFixe(df.format(Double
								.valueOf(remu.getTotalActuPrimeFixe())
								+ Double.valueOf(lrr.getMontant())));
					}
					if (Double.valueOf(lrr.getMontantNPrec()) != 0.0) {
						remu.setTotalNPrecPrimeFixe(df.format(Double
								.valueOf(remu.getTotalNPrecPrimeFixe())
								+ Double.valueOf(lrr.getMontantNPrec())));
					}
					remu.setTotalNPrimeFixe(df.format(Double.valueOf(remu
							.getTotalNPrimeFixe())
							+ Double.valueOf(lrr.getMontant())));
				}
				if (serviceRevCom.getType(lrr.getIdRevenuComplementaire(),
						idGroupe).equals("prime_variable")) {
					lrr.setMontantNPrec(calculNPrec("prime_variable",
							lrr.getLibelle(), annee, idSalarie, idGroupe));
					primeVariableInventoryLien.add(lrr);

					if (Double.valueOf(lrr.getActualisation()) != 0.0) {
						remu.setTotalActuPrimeVariable(df.format(Double
								.valueOf(remu.getTotalActuPrimeVariable())
								+ Double.valueOf(lrr.getActualisation())));
					} else {
						remu.setTotalActuPrimeVariable(df.format(Double
								.valueOf(remu.getTotalActuPrimeVariable())
								+ Double.valueOf(lrr.getMontant())));
					}
					if (Double.valueOf(lrr.getMontantNPrec()) != 0.0) {
						remu.setTotalNPrecPrimeVariable(df.format(Double
								.valueOf(remu.getTotalNPrecPrimeVariable())
								+ Double.valueOf(lrr.getMontantNPrec())));
					}
					remu.setTotalNPrimeVariable(df.format(Double.valueOf(remu
							.getTotalNPrimeVariable())
							+ Double.valueOf(lrr.getMontant())));
				}
				if (serviceRevCom.getType(lrr.getIdRevenuComplementaire(),
						idGroupe).equals("avantage_assujetti")) {
					lrr.setMontantNPrec(calculNPrec("avantage_assujetti",
							lrr.getLibelle(), annee, idSalarie, idGroupe));
					avantageAssujettiInventoryLien.add(lrr);

					if (Double.valueOf(lrr.getActualisation()) != 0.0) {
						remu.setTotalActuAvantageAssujetti(df.format(Double
								.valueOf(remu.getTotalActuAvantageAssujetti())
								+ Double.valueOf(lrr.getActualisation())));
					} else {
						remu.setTotalActuAvantageAssujetti(df.format(Double
								.valueOf(remu.getTotalActuAvantageAssujetti())
								+ Double.valueOf(lrr.getMontant())));
					}
					if (Double.valueOf(lrr.getMontantNPrec()) != 0.0) {
						remu.setTotalNPrecAvantageAssujetti(df.format(Double
								.valueOf(remu.getTotalNPrecAvantageAssujetti())
								+ Double.valueOf(lrr.getMontantNPrec())));
					}
					remu.setTotalNAvantageAssujetti(df.format(Double
							.valueOf(remu.getTotalNAvantageAssujetti())
							+ Double.valueOf(lrr.getMontant())));
				}
				if (serviceRevCom.getType(lrr.getIdRevenuComplementaire(),
						idGroupe).equals("avantage_non_assujetti")) {
					lrr.setMontantNPrec(calculNPrec("avantage_non_assujetti",
							lrr.getLibelle(), annee, idSalarie, idGroupe));
					avantageNonAssujettiInventoryLien.add(lrr);

					if (Double.valueOf(lrr.getActualisation()) != 0.0) {
						remu.setTotalActuAvantageNonAssujetti(df.format(Double
								.valueOf(remu
										.getTotalActuAvantageNonAssujetti())
								+ Double.valueOf(lrr.getActualisation())));
					} else {
						remu.setTotalActuAvantageNonAssujetti(df.format(Double
								.valueOf(remu
										.getTotalActuAvantageNonAssujetti())
								+ Double.valueOf(lrr.getMontant())));
					}
					if (Double.valueOf(lrr.getMontantNPrec()) != 0.0) {
						remu.setTotalNPrecAvantageNonAssujetti(df.format(Double
								.valueOf(remu
										.getTotalNPrecAvantageNonAssujetti())
								+ Double.valueOf(lrr.getMontantNPrec())));
					}
					remu.setTotalNAvantageNonAssujetti(df.format(Double
							.valueOf(remu.getTotalNAvantageNonAssujetti())
							+ Double.valueOf(lrr.getMontant())));
				}
				if (serviceRevCom.getType(lrr.getIdRevenuComplementaire(),
						idGroupe).equals("frais_professionnel")) {
					lrr.setMontantNPrec(calculNPrec("frais_professionnel",
							lrr.getLibelle(), annee, idSalarie, idGroupe));
					fraisProfInventoryLien.add(lrr);
					if (Double.valueOf(lrr.getMontantNPrec()) != 0.0) {
						remu.setTotalNPrecFraisProf(df.format(Double
								.valueOf(remu.getTotalNPrecFraisProf())
								+ Double.valueOf(lrr.getMontantNPrec())));
					}
					remu.setTotalNFraisProf(df.format(Double.valueOf(remu
							.getTotalNFraisProf())
							+ Double.valueOf(lrr.getMontant())));
				}
			}

			if (remu.getBaseBruteAnnuelle().contains(",")) {
				remu.setBaseBruteAnnuelle(remu.getBaseBruteAnnuelle().replace(
						",", "."));
			}
			remu.setRemunerationBruteAnnuelle(df.format(Double.valueOf(remu
					.getBaseBruteAnnuelle())
					+ Double.valueOf(remu.getTotalNCommission())
					+ Double.valueOf(remu.getTotalNPrimeFixe())
					+ Double.valueOf(remu.getTotalNPrimeVariable())
					+ Double.valueOf(remu.getTotalNAvantageAssujetti())));
			if (!remuNPrec.isEmpty()) {
				remu.setRemunerationBruteAnnuelleNPrec(df.format(Double
						.valueOf(remu.getBaseBruteAnnuelleNPrec())
						+ Double.valueOf(remu.getTotalNPrecCommission())
						+ Double.valueOf(remu.getTotalNPrecPrimeFixe())
						+ Double.valueOf(remu.getTotalNPrecPrimeVariable())
						+ Double.valueOf(remu.getTotalNPrecAvantageAssujetti())));
			}
			remu.setRemunerationBruteAnnuelleActu(df.format(Double.valueOf(remu
					.getBaseBruteAnnuelleActu())
					+ Double.valueOf(remu.getTotalActuCommission())
					+ Double.valueOf(remu.getTotalActuPrimeFixe())
					+ Double.valueOf(remu.getTotalActuPrimeVariable())
					+ Double.valueOf(remu.getTotalActuAvantageAssujetti())));
			if (!remuNPrec.isEmpty()) {
				if (Double.valueOf(remu.getRemunerationBruteAnnuelleNPrec()) != 0
						&& (Double.valueOf(remu.getRemunerationBruteAnnuelle()) != 0 || Double
								.valueOf(remu
										.getRemunerationBruteAnnuelleActu()) != 0)) {
					if (Double.valueOf(remu.getRemunerationBruteAnnuelleActu()) == 0) {
						double totalBrutEvol = ((Double.valueOf(remu
								.getRemunerationBruteAnnuelle()) - Double
								.valueOf(remu
										.getRemunerationBruteAnnuelleNPrec())) / Double
								.valueOf(remu
										.getRemunerationBruteAnnuelleNPrec())) * 100;
						double total = (double) ((totalBrutEvol * 100)) / 100;
						remu.setRemunerationBruteAnnuelleEvol(df.format(Double
								.valueOf(total)));
					} else {
						double totalBrutEvol = ((Double.valueOf(remu
								.getRemunerationBruteAnnuelleActu()) - Double
								.valueOf(remu
										.getRemunerationBruteAnnuelleNPrec())) / Double
								.valueOf(remu
										.getRemunerationBruteAnnuelleNPrec())) * 100;
						double total = (double) ((totalBrutEvol * 100)) / 100;
						remu.setRemunerationBruteAnnuelleEvol(df.format(Double
								.valueOf(total)));
					}
				}
			}
			remu.setRemuGlobale(df.format(Double.valueOf(remu
					.getRemunerationBruteAnnuelle())
					+ Double.valueOf(remu.getTotalNAvantageNonAssujetti())));
			if (!remuNPrec.isEmpty()) {
				remu.setRemuGlobaleNPrec(df.format(Double.valueOf(remu
						.getRemunerationBruteAnnuelleNPrec())
						+ Double.valueOf(remu
								.getTotalNPrecAvantageNonAssujetti())));
			}
			remu.setRemuGlobaleActu(df.format(Double.valueOf(remu
					.getRemunerationBruteAnnuelleActu())
					+ Double.valueOf(remu.getTotalActuAvantageNonAssujetti())));
			if (!remuNPrec.isEmpty()) {
				if (Double.valueOf(remu.getRemuGlobaleNPrec()) != 0
						&& (Double.valueOf(remu.getRemuGlobale()) != 0 || Double
								.valueOf(remu.getRemuGlobaleActu()) != 0)) {
					if (Double.valueOf(remu.getRemuGlobaleActu()) == 0) {
						double totalBrutEvol = ((Double.valueOf(remu
								.getRemuGlobale()) - Double.valueOf(remu
								.getRemuGlobaleNPrec())) / Double.valueOf(remu
								.getRemuGlobaleNPrec())) * 100;
						double total = (double) ((totalBrutEvol * 100)) / 100;
						remu.setRemuGlobaleEvol(df.format(Double.valueOf(total)));
					} else {
						double totalBrutEvol = ((Double.valueOf(remu
								.getRemuGlobaleActu()) - Double.valueOf(remu
								.getRemuGlobaleNPrec())) / Double.valueOf(remu
								.getRemuGlobaleNPrec())) * 100;
						double total = (double) ((totalBrutEvol * 100)) / 100;
						remu.setRemuGlobaleEvol(df.format(Double.valueOf(total)));
					}
				}
			}

			if (!rbi.isEmpty()) {
				RemunerationBean actu = rbi.get(0);

				l = new ArrayList<LienRemunerationRevenuBean>();
				LienRemunerationRevenuServiceImpl ls = new LienRemunerationRevenuServiceImpl();
				RevenuComplementaireServiceImpl rcs = new RevenuComplementaireServiceImpl();
				l = ls.getLienRemunerationRevenuFromIdRemu(actu
						.getIdRemuneration());
				for (LienRemunerationRevenuBean lrr : l) {
					if (rcs.getType(lrr.getIdRevenuComplementaire(), idGroupe)
							.equals("commission")) {
						if (Double.valueOf(lrr.getMontant()) != 0
								|| Double.valueOf(lrr.getActualisation()) != 0) {
							LienRemunerationRevenuBean lien = new LienRemunerationRevenuBean();
							lien.setIdRevenuComplementaire(lrr
									.getIdRevenuComplementaire());
							lien.setIdLienRemunerationRevenuComplementaire(0);
							lien.setCommentaire(lrr.getCommentaire());
							lien.setLibelle(rcs
									.getRevenuComplementaireBeanById(
											lrr.getIdRevenuComplementaire())
									.getLibelle());
							if (Double.valueOf(lrr.getActualisation()) != 0) {
								lien.setMontantNPrec(df.format(Double
										.valueOf(lrr.getActualisation())));
							} else {
								lien.setMontantNPrec(df.format(Double
										.valueOf(lrr.getMontant())));
							}
							lien.setMontant("0.00");
							lien.setActualisation("0.00");
							lien.setRemonteNPrec(true);
							boolean contain = false;
							for (LienRemunerationRevenuBean lrb : commissionInventoryLien) {
								if (lien.getLibelle()
										.equals(rcs
												.getRevenuComplementaireBeanById(
														lrb.getIdRevenuComplementaire())
												.getLibelle())) {
									contain = true;
									break;
								}
							}
							if (!contain) {
								commissionInventoryLien.add(lien);
								if (Double.valueOf(lrr.getActualisation()) == 0.0) {
									remu.setTotalNPrecCommission(df
											.format(Double.valueOf(remu
													.getTotalNPrecCommission())
													+ Double.valueOf(lrr
															.getMontant())));
								} else {
									remu.setTotalNPrecCommission(df.format(Double
											.valueOf(remu
													.getTotalNPrecCommission())
											+ Double.valueOf(lrr
													.getActualisation())));
								}
							}
						}
					}
					if (rcs.getType(lrr.getIdRevenuComplementaire(), idGroupe)
							.equals("prime_fixe")) {
						if (Double.valueOf(lrr.getMontant()) != 0
								|| Double.valueOf(lrr.getActualisation()) != 0) {
							LienRemunerationRevenuBean lien = new LienRemunerationRevenuBean();
							lien.setIdRevenuComplementaire(lrr
									.getIdRevenuComplementaire());
							lien.setIdLienRemunerationRevenuComplementaire(0);
							lien.setLibelle(rcs
									.getRevenuComplementaireBeanById(
											lrr.getIdRevenuComplementaire())
									.getLibelle());
							if (Double.valueOf(lrr.getActualisation()) != 0) {
								lien.setMontantNPrec(df.format(Double
										.valueOf(lrr.getActualisation())));
							} else {
								lien.setMontantNPrec(df.format(Double
										.valueOf(lrr.getMontant())));
							}
							lien.setMontant("0.00");
							lien.setActualisation("0.00");
							lien.setCommentaire(lrr.getCommentaire());
							lien.setRemonteNPrec(true);
							boolean contain = false;
							for (LienRemunerationRevenuBean lrb : primeFixeInventoryLien) {
								if (lien.getLibelle()
										.equals(rcs
												.getRevenuComplementaireBeanById(
														lrb.getIdRevenuComplementaire())
												.getLibelle())) {
									contain = true;
									break;
								}
							}
							if (!contain) {
								primeFixeInventoryLien.add(lien);

								if (Double.valueOf(lrr.getActualisation()) == 0.0) {
									remu.setTotalNPrecPrimeFixe(df
											.format(Double.valueOf(remu
													.getTotalNPrecPrimeFixe())
													+ Double.valueOf(lrr
															.getMontant())));
								} else {
									remu.setTotalNPrecPrimeFixe(df.format(Double
											.valueOf(remu
													.getTotalNPrecPrimeFixe())
											+ Double.valueOf(lrr
													.getActualisation())));
								}
							}
						}
					}
					if (rcs.getType(lrr.getIdRevenuComplementaire(), idGroupe)
							.equals("prime_variable")) {
						if (Double.valueOf(lrr.getMontant()) != 0
								|| Double.valueOf(lrr.getActualisation()) != 0) {
							LienRemunerationRevenuBean lien = new LienRemunerationRevenuBean();
							lien.setIdRevenuComplementaire(lrr
									.getIdRevenuComplementaire());
							lien.setIdLienRemunerationRevenuComplementaire(0);
							lien.setCommentaire(lrr.getCommentaire());
							lien.setLibelle(rcs
									.getRevenuComplementaireBeanById(
											lrr.getIdRevenuComplementaire())
									.getLibelle());
							if (Double.valueOf(lrr.getActualisation()) != 0) {
								lien.setMontantNPrec(df.format(Double
										.valueOf(lrr.getActualisation())));
							} else {
								lien.setMontantNPrec(df.format(Double
										.valueOf(lrr.getMontant())));
							}
							lien.setMontant("0.00");
							lien.setActualisation("0.00");
							lien.setRemonteNPrec(true);
							boolean contain = false;
							for (LienRemunerationRevenuBean lrb : primeVariableInventoryLien) {
								if (lien.getLibelle()
										.equals(rcs
												.getRevenuComplementaireBeanById(
														lrb.getIdRevenuComplementaire())
												.getLibelle())) {
									contain = true;
									break;
								}
							}
							if (!contain) {
								primeVariableInventoryLien.add(lien);

								if (Double.valueOf(lrr.getActualisation()) == 0.0) {
									remu.setTotalNPrecPrimeVariable(df.format(Double.valueOf(remu
											.getTotalNPrecPrimeVariable())
											+ Double.valueOf(lrr.getMontant())));
								} else {
									remu.setTotalNPrecPrimeVariable(df.format(Double.valueOf(remu
											.getTotalNPrecPrimeVariable())
											+ Double.valueOf(lrr
													.getActualisation())));
								}
							}
						}
					}
					if (rcs.getType(lrr.getIdRevenuComplementaire(), idGroupe)
							.equals("avantage_assujetti")) {
						if (Double.valueOf(lrr.getMontant()) != 0
								|| Double.valueOf(lrr.getActualisation()) != 0) {
							LienRemunerationRevenuBean lien = new LienRemunerationRevenuBean();
							lien.setIdRevenuComplementaire(lrr
									.getIdRevenuComplementaire());
							lien.setIdLienRemunerationRevenuComplementaire(0);
							lien.setCommentaire(lrr.getCommentaire());
							lien.setLibelle(rcs
									.getRevenuComplementaireBeanById(
											lrr.getIdRevenuComplementaire())
									.getLibelle());
							if (Double.valueOf(lrr.getActualisation()) != 0) {
								lien.setMontantNPrec(df.format(Double
										.valueOf(lrr.getActualisation())));
							} else {
								lien.setMontantNPrec(df.format(Double
										.valueOf(lrr.getMontant())));
							}
							lien.setMontant("0.00");
							lien.setActualisation("0.00");
							lien.setRemonteNPrec(true);
							boolean contain = false;
							for (LienRemunerationRevenuBean lrb : avantageAssujettiInventoryLien) {
								if (lien.getLibelle()
										.equals(rcs
												.getRevenuComplementaireBeanById(
														lrb.getIdRevenuComplementaire())
												.getLibelle())) {
									contain = true;
									break;
								}
							}
							if (!contain) {
								avantageAssujettiInventoryLien.add(lien);

								if (Double.valueOf(lrr.getActualisation()) == 0.0) {
									remu.setTotalNPrecAvantageAssujetti(df.format(Double.valueOf(remu
											.getTotalNPrecAvantageAssujetti())
											+ Double.valueOf(lrr.getMontant())));
								} else {
									remu.setTotalNPrecAvantageAssujetti(df.format(Double.valueOf(remu
											.getTotalNPrecAvantageAssujetti())
											+ Double.valueOf(lrr
													.getActualisation())));
								}
							}
						}
					}
					if (rcs.getType(lrr.getIdRevenuComplementaire(), idGroupe)
							.equals("avantage_non_assujetti")) {
						if (Double.valueOf(lrr.getMontant()) != 0
								|| Double.valueOf(lrr.getActualisation()) != 0) {
							LienRemunerationRevenuBean lien = new LienRemunerationRevenuBean();
							lien.setIdRevenuComplementaire(lrr
									.getIdRevenuComplementaire());
							lien.setIdLienRemunerationRevenuComplementaire(0);
							lien.setCommentaire(lrr.getCommentaire());
							lien.setLibelle(rcs
									.getRevenuComplementaireBeanById(
											lrr.getIdRevenuComplementaire())
									.getLibelle());
							if (Double.valueOf(lrr.getActualisation()) != 0) {
								lien.setMontantNPrec(df.format(Double
										.valueOf(lrr.getActualisation())));
							} else {
								lien.setMontantNPrec(df.format(Double
										.valueOf(lrr.getMontant())));
							}
							lien.setMontant("0.00");
							lien.setActualisation("0.00");
							lien.setRemonteNPrec(true);
							boolean contain = false;
							for (LienRemunerationRevenuBean lrb : avantageNonAssujettiInventoryLien) {
								if (lien.getLibelle()
										.equals(rcs
												.getRevenuComplementaireBeanById(
														lrb.getIdRevenuComplementaire())
												.getLibelle())) {
									contain = true;
									break;
								}
							}
							if (!contain) {
								avantageNonAssujettiInventoryLien.add(lien);

								if (Double.valueOf(lrr.getActualisation()) == 0.0) {
									remu.setTotalNPrecAvantageNonAssujetti(df.format(Double.valueOf(remu
											.getTotalNPrecAvantageNonAssujetti())
											+ Double.valueOf(lrr.getMontant())));
								} else {
									remu.setTotalNPrecAvantageNonAssujetti(df.format(Double.valueOf(remu
											.getTotalNPrecAvantageNonAssujetti())
											+ Double.valueOf(lrr
													.getActualisation())));
								}
							}
						}
					}
					if (rcs.getType(lrr.getIdRevenuComplementaire(), idGroupe)
							.equals("frais_professionnel")) {
						if (Double.valueOf(lrr.getMontant()) != 0) {
							LienRemunerationRevenuBean lien = new LienRemunerationRevenuBean();
							lien.setIdRevenuComplementaire(lrr
									.getIdRevenuComplementaire());
							lien.setIdLienRemunerationRevenuComplementaire(0);
							lien.setCommentaire(lrr.getCommentaire());
							lien.setLibelle(rcs
									.getRevenuComplementaireBeanById(
											lrr.getIdRevenuComplementaire())
									.getLibelle());
							lien.setMontantNPrec(df.format(Double.valueOf(lrr
									.getMontant())));
							lien.setMontant("0.00");
							lien.setRemonteNPrec(true);
							boolean contain = false;
							for (LienRemunerationRevenuBean lrb : fraisProfInventoryLien) {
								if (lien.getLibelle()
										.equals(rcs
												.getRevenuComplementaireBeanById(
														lrb.getIdRevenuComplementaire())
												.getLibelle())) {
									contain = true;
									break;
								}
							}
							if (!contain) {
								fraisProfInventoryLien.add(lien);

								remu.setTotalNPrecFraisProf(df.format(Double
										.valueOf(remu.getTotalNPrecFraisProf())
										+ Double.valueOf(lrr.getMontant())));
							}
						}
					}
				}
			}

			EntrepriseServiceImpl ent = new EntrepriseServiceImpl();

			remu.setNomEntreprise(ent.getEntrepriseBeanById(idEntreprise)
					.getNom());

			remu.setJustificatif(ent.getEntrepriseBeanById(idEntreprise)
					.getJustificatif());
			remu.setUrl(Utils.getFileUrl(idEntreprise, "logo_entreprise",
					false, false, true, true, nomGroupe));

			remu.setComList(commissionInventoryLien);
			remu.setPfList(primeFixeInventoryLien);
			remu.setPvList(primeVariableInventoryLien);
			remu.setAaList(avantageAssujettiInventoryLien);
			remu.setAnaList(avantageNonAssujettiInventoryLien);
			remu.setFpList(fraisProfInventoryLien);

			SalarieServiceImpl sal = new SalarieServiceImpl();
			SalarieBean s = sal.getSalarieBeanById(idSalarie);

			remu.setNom(s.getNom());
			remu.setPrenom(s.getPrenom());

			List<ParcoursBean> parcoursBeanList = new ArrayList<ParcoursBean>();
			parcoursBeanList = s.getParcoursBeanList();
			Collections.sort(parcoursBeanList);
			Collections.reverse(parcoursBeanList);

			if (parcoursBeanList != null && parcoursBeanList.size() > 0) {
				ParcoursBean parcourBean = parcoursBeanList.get(0);

				remu.setNomMetier(parcourBean.getNomMetier());
				remu.setNomCsp(parcourBean.getNomTypeStatut());
			} else {
				remu.setNomCsp("");
				remu.setNomMetier("");
			}

			remunerationBeanExportList.add(remu);

			JRDataSource jRDataSource = new JRBeanCollectionDataSource(
					remunerationBeanExportList);

			String realPath = getServletConfig()
					.getServletContext()
					.getRealPath(
							"/reportTemplates/RemunerationIndividuelleSalarie.jrxml");

			JasperDesign jasperDesign = JRXmlLoader.load(realPath);

			HashMap<String, Object> jasperMap = new HashMap<String, Object>();

			// COMPILATION des template
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);

			// création du virtualizer
			JRFileVirtualizer virtualizer = new JRFileVirtualizer(100);

			jasperMap.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

			// Fill the report using an empty data source
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, jasperMap, jRDataSource);

			virtualizer.setReadOnly(true);

			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

			/*******************************************************************
			 * Pour afficher une boîte de dialogue pour enregistrer le fichier
			 * sous le nom rapport.pdf
			 ******************************************************************/
			response.addHeader("Content-disposition",
					"attachment;filename=FicheRemuneration.pdf");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
			response.setContentType("application/pdf");

			// vidage du virtualizer
			virtualizer.cleanup();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String calculNPrec(String type, String idSelected, Integer annee,
			Integer idSalarie, int idGroupe) throws Exception {

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		if (idSelected.equals("-1")) {
			return df.format(Double.valueOf("0"));
		}

		int anneePrec = annee - 1;

		RemunerationServiceImpl remu = new RemunerationServiceImpl();
		List<RemunerationBean> list = new ArrayList<RemunerationBean>();
		list = remu.getRemunerationList(idGroupe);
		RemunerationBean r = new RemunerationBean();
		for (RemunerationBean rb : list) {
			if (rb.getAnnee() == anneePrec
					&& rb.getIdSalarie().intValue() == idSalarie) {
				r = rb;
			}
		}

		RevenuComplementaireServiceImpl service = new RevenuComplementaireServiceImpl();
		int id;
		if (!type.equals("frais_professionnel")) {
			id = service.getIdFromRevenuComplementaire(idSelected, type,
					idGroupe);
		} else {
			id = service.getIdFromRevenuComplementaireFp(idSelected, type,
					idGroupe);
		}

		LienRemunerationRevenuServiceImpl lien = new LienRemunerationRevenuServiceImpl();
		List<LienRemunerationRevenuBean> listLien = new ArrayList<LienRemunerationRevenuBean>();
		listLien = lien.getLienRemunerationRevenuList(idGroupe);
		LienRemunerationRevenuBean l = new LienRemunerationRevenuBean();
		for (LienRemunerationRevenuBean lrr : listLien) {
			if (lrr.getIdRemuneration().equals(r.getIdRemuneration())
					&& lrr.getIdRevenuComplementaire().equals(id)) {
				l = lrr;
			}
		}
		if (l.getActualisation() == null
				|| Double.valueOf(l.getActualisation()) == 0.0) {
			if (l.getMontant() == null || Double.valueOf(l.getMontant()) == 0.0) {
				return df.format(Double.valueOf("0"));
			} else {
				return df.format(Double.valueOf(l.getMontant()));
			}
		} else {
			return df.format(Double.valueOf(l.getActualisation()));
		}
	}

}
