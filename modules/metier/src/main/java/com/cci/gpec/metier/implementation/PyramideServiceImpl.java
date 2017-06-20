package com.cci.gpec.metier.implementation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.general.KeyedValues2DDataset;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.PyramideDataBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.metier.interfaces.PyramideService;
import com.cci.gpec.metier.interfaces.SalarieService;

public class PyramideServiceImpl implements PyramideService {

	private int nbMax = 0;

	private ParcoursBean getLastParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		ParcoursBean pb = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			if (pb == null) {
				pb = parcour;
			}
			if (parcour.getDebutFonction().after(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}

	private int getAnciennete(SalarieBean salarie) {
		ParcoursBean parcourDeb = getFirstParcours(salarie);
		Calendar dateDebut = new GregorianCalendar();
		Calendar dateFin = new GregorianCalendar();
		if (parcourDeb != null) {
			dateDebut.setTime(parcourDeb.getDebutFonction());
		}
		ParcoursBean parcourFin = getLastParcours(salarie);
		if (parcourFin != null) {
			if (parcourFin.getFinFonction() != null
					&& (parcourFin.getFinFonction().before(new Date()) || parcourFin
							.getFinFonction().equals(new Date()))) {
				dateFin.setTime(parcourFin.getFinFonction());
			} else {
				dateFin.setTime(new Date());
			}
		}
		return dateFin.get(Calendar.YEAR) - dateDebut.get(Calendar.YEAR);
	}

	private ParcoursBean getFirstParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		Collections.sort(parcourList);
		Collections.reverse(parcourList);
		ParcoursBean pb = null;
		Long diffMillis;
		Long diffenjours = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			if (pb == null) {
				pb = parcour;
			}
			int nbJours = 0;
			Calendar d1 = new GregorianCalendar();
			Calendar d2 = new GregorianCalendar();
			if (parcour.getFinFonction() != null) {
				d1.setTime(parcour.getFinFonction());
			} else {
				d1.setTime(new Date());
			}
			d2.setTime(pb.getDebutFonction());
			diffMillis = d2.getTimeInMillis() - d1.getTimeInMillis();

			diffenjours = diffMillis / (24 * 60 * 60 * 1000);
			if (parcour.getDebutFonction().before(pb.getDebutFonction())
					&& diffenjours <= 1) {
				pb = parcour;
			}
		}
		return pb;
	}

	public static double getDaysBetweenDates(Date theEarlierDate,
			Date theLaterDate) {
		double result = Double.POSITIVE_INFINITY;
		if (theEarlierDate != null && theLaterDate != null) {
			final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
			Calendar aCal = Calendar.getInstance();
			aCal.setTime(theEarlierDate);
			long aFromOffset = aCal.get(Calendar.DST_OFFSET);
			aCal.setTime(theLaterDate);
			long aToOffset = aCal.get(Calendar.DST_OFFSET);
			long aDayDiffInMili = (theLaterDate.getTime() + aToOffset)
					- (theEarlierDate.getTime() + aFromOffset);
			result = ((double) aDayDiffInMili / MILLISECONDS_PER_DAY);
		}
		return result;
	}

	public byte[] creerPyramide(String type, Integer entreprise,
			Integer service, Integer metier, Integer projection, int idGroupe)
			throws Exception {
		KeyedValues2DDataset dataset;
		JFreeChart pyramide;
		String abscisse = "Nombre de salari\u00E9s";
		EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
		ServiceImpl servServ = new ServiceImpl();
		MetierServiceImpl metServ = new MetierServiceImpl();

		String nomEntreprise = "";
		String nomService = "";
		String nomMetier = "";

		if (entreprise != null && entreprise > 0) {
			EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
			EntrepriseBean e = serv.getEntrepriseBeanById(entreprise);
			nomEntreprise = entServ.getEntrepriseBeanById(entreprise).getNom();
			if (service != null && service > 0) {
				nomService = " - "
						+ servServ.getServiceBeanById(service).getNom();
				if (metier != null && metier > 0) {
					nomMetier = " - "
							+ metServ.getMetierBeanById(metier).getNom();
				}
			}
		} else {
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean groupe = new GroupeBean();
			groupe = grServ.getGroupeBeanById(idGroupe);
			nomEntreprise = groupe.getNom();
		}
		String filtre = "\n" + nomEntreprise + nomService + nomMetier
				+ "                 ";
		if (projection > 0) {
			filtre += " \n Projection : " + projection
					+ "ans                     ";
		}
		GregorianCalendar anneeActu = new GregorianCalendar();
		anneeActu.setTime(new Date());
		if (projection > 0) {
			anneeActu.add(Calendar.YEAR, projection);
		}
		if (type.equals("Age")) {
			dataset = createDataset(type, entreprise, service, metier,
					projection, idGroupe);

			pyramide = ChartFactory.createStackedBarChart(
					"Pyramide des âges " + anneeActu.get(Calendar.YEAR)
							+ "               " + filtre, type
							+ " - % par tranche", // domain
					// axis
					// label
					abscisse, // range axis label
					dataset, // data
					PlotOrientation.HORIZONTAL, true, // include legend
					true, // tooltips
					false // urls
					);
		} else {
			dataset = createDatasetAnciennete(type, entreprise, service,
					metier, idGroupe);
			pyramide = ChartFactory.createStackedBarChart(
					"Pyramide de l'ancienneté " + anneeActu.get(Calendar.YEAR)
							+ "           " + filtre,
					type + " - % par tranche", // domain
					// axis
					// label
					abscisse, // range axis label
					dataset, // data
					PlotOrientation.HORIZONTAL, true, // include legend
					true, // tooltips
					false // urls
					);
		}

		CategoryPlot plot = (CategoryPlot) pyramide.getPlot();
		CategoryItemRenderer categoryitemrenderer = plot.getRenderer();

		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
		plot.mapDatasetToRangeAxis(1, 1);

		categoryitemrenderer.setSeriesPaint(0, Color.getHSBColor(
				new Float(0.6), new Float(0.6), new Float(1.0)));
		categoryitemrenderer.setSeriesPaint(1, Color.getHSBColor(
				new Float(1.0), new Float(0.3), new Float(1.0)));

		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();

		// modifie le max et le min du graphique en fonction du nombre max de
		// personne
		// pour avoir un graphique symétrique
		numberaxis.setRange(-nbMax, nbMax);
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberaxis.setVisible(false);

		ValueMarker valuemarker = new ValueMarker(0);
		valuemarker.setLabelOffsetType(LengthAdjustmentType.EXPAND);
		valuemarker.setPaint(Color.red);
		valuemarker.setStroke(new BasicStroke(2.0F));
		valuemarker.setLabelPaint(Color.red);
		valuemarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
		valuemarker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
		plot.addRangeMarker(valuemarker);

		// permet d'afficher le nombre de salarié sans le signe négatif devant
		DecimalFormat decimalFormat = new DecimalFormat("#");
		decimalFormat.setNegativePrefix("");
		categoryitemrenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
						"{2}", decimalFormat));
		categoryitemrenderer.setSeriesItemLabelsVisible(0, Boolean.TRUE);
		categoryitemrenderer.setSeriesItemLabelsVisible(1, Boolean.TRUE);
		categoryitemrenderer.setSeriesItemLabelsVisible(2, Boolean.TRUE);

		byte[] tabByte = new byte[1];
		tabByte = ChartUtilities.encodeAsPNG(pyramide.createBufferedImage(900,
				420));
		return tabByte;
	}

	private int anciennete(Date debut, Date fin) {
		long val = fin.getTime() - debut.getTime();
		return (int) (val / (1000 * 3600 * 24 * 365.25));
	}

	public KeyedValues2DDataset createDatasetAnciennete(String type,
			Integer entreprise, Integer service, Integer metier, int idGroupe)
			throws Exception {

		DefaultKeyedValues2DDataset data = new DefaultKeyedValues2DDataset();

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		List<SalarieBean> salarieList;
		List<PyramideDataBean> pyramideDataListHomme = new ArrayList<PyramideDataBean>();
		List<PyramideDataBean> pyramideDataListFemme = new ArrayList<PyramideDataBean>();

		try {
			// on retrouve le bon jeu de données
			SalarieService salarieService = new SalarieServiceImpl();

			salarieList = salarieService.getPyramideDataOrderedByAnciennete(
					entreprise, service, idGroupe);

			// fait un tri sur la liste et garde seulement les salarié qui ont
			// le metier séléctionné.
			if (metier != null && metier != 0 && metier != -1) {
				boolean flag = false;
				ArrayList<SalarieBean> salarieListTmp = new ArrayList<SalarieBean>();
				for (SalarieBean salarieBean : salarieList) {
					if (getLastParcours(salarieBean) != null
							&& getLastParcours(salarieBean)
									.getIdTypeContratSelected() != 3
							&& getLastParcours(salarieBean)
									.getIdTypeContratSelected() != 10) {
						for (ParcoursBean parcoursBean : salarieBean
								.getParcoursBeanList()) {
							if (metier.intValue() == parcoursBean
									.getIdMetierSelected().intValue()) {
								flag = true;
							}
						}
						if (flag) {
							salarieListTmp.add(salarieBean);
							flag = false;
						}
					}
				}
				salarieList = salarieListTmp;
			}

			// initialisation des variables
			int anciennete = 0;
			int nbFemme;
			int nbHomme;
			GregorianCalendar datePremierPoste = new GregorianCalendar();
			GregorianCalendar dateDernierPoste = new GregorianCalendar();
			PyramideDataBean pyramideDataHomme;
			PyramideDataBean pyramideDataFemme;
			// on compte le nombre de personne par ancienneté et sexe
			boolean exit;

			int i = 0;
			while (i <= 45) {
				nbFemme = 0;
				nbHomme = 0;

				for (SalarieBean salarie : salarieList) {
					if (getLastParcours(salarie) != null
							&& getLastParcours(salarie)
									.getIdTypeContratSelected() != 3
							&& getLastParcours(salarie)
									.getIdTypeContratSelected() != 10
							&& salarie.isPresent()) {
						exit = false;
						// détermination de l'ancienneté
						ArrayList<ParcoursBean> parcoursBeanList = (ArrayList<ParcoursBean>) salarie
								.getParcoursBeanList();
						if (parcoursBeanList.size() > 0
								&& parcoursBeanList != null) {
							Collections.sort(parcoursBeanList);
							// Collections.reverse(parcoursBeanList);

							ParcoursBean parcoursBeanTmp = parcoursBeanList
									.get(parcoursBeanList.size() - 1);
							if (parcoursBeanTmp.getFinFonction() != null
							/*
							 * && !parcoursBeanTmp.getFinFonction().equals(
							 * UtilsDate.FIN_NULL)
							 */) {
								Calendar finFonction = new GregorianCalendar();
								finFonction.setTime(parcoursBeanTmp
										.getFinFonction());
								Calendar today = GregorianCalendar
										.getInstance();
								if (finFonction.before(today)) {
									exit = true;
								}
							} else {
								// Salarie en CDI
								exit = false;
							}
							if (!exit) {
								if (parcoursBeanList.size() > 1) {
									// récupération du premier porte occupé
									dateDernierPoste.setTime(parcoursBeanList
											.get(parcoursBeanList.size() - 1)
											.getDebutFonction());

								} else {
									dateDernierPoste.setTime(new Date());

								}
								// récupération du premier poste occupé
								datePremierPoste.setTime(parcoursBeanList
										.get(0).getDebutFonction());

								anciennete = this.getAnciennete(salarie);
								if (i < 5) {
									if (i == 0) {
										if (salarie.getCivilite().equals(
												"Monsieur")) {
											if ((0 <= anciennete)
													&& (anciennete <= 1)) {
												nbHomme++;
											}
										} else {
											if ((0 <= anciennete)
													&& (anciennete <= 1)) {
												nbFemme++;
											}
										}
									} else {
										if (salarie.getCivilite().equals(
												"Monsieur")) {
											if ((i < anciennete)
													&& (anciennete <= (i + 1))) {
												nbHomme++;
											}
										} else {
											if ((i < anciennete)
													&& (anciennete <= (i + 1))) {
												nbFemme++;
											}
										}
									}

								} else {
									if (salarie.getCivilite()
											.equals("Monsieur")) {
										if ((i < anciennete)
												&& (anciennete <= (i + 5))) {
											nbHomme++;
										}
									} else {
										if ((i < anciennete)
												&& (anciennete <= (i + 5))) {
											nbFemme++;
										}
									}

								}
							}
						} // Fin if possede metier
					} // Fin isPresent
				} // Fin salarie

				pyramideDataHomme = new PyramideDataBean(i, "Homme", nbHomme);
				pyramideDataListHomme.add(pyramideDataHomme);

				pyramideDataFemme = new PyramideDataBean(i, "Femme", nbFemme);
				pyramideDataListFemme.add(pyramideDataFemme);

				if (i < 5) {
					i++;
				} else {
					i += 5;
				}

				// récupère le nombre max de personne afin de créer un graphique
				// ayant la bonne échelle et le zéro centré
				if (nbMax < nbHomme) {
					nbMax = nbHomme;
				}
				if (nbMax < nbFemme) {
					nbMax = nbFemme;
				}
			}// fin while

			Collections.sort(pyramideDataListFemme);
			Collections.sort(pyramideDataListHomme);
			Collections.reverse(pyramideDataListFemme);
			Collections.reverse(pyramideDataListHomme);
			String libelle;

			List<Integer> nbHommeCat = new ArrayList<Integer>();
			List<Integer> nbFemmeCat = new ArrayList<Integer>();

			int nbSalarie = 0;
			int c = 0;
			for (PyramideDataBean pyramideDataHomme2 : pyramideDataListHomme) {
				nbHommeCat.add(c, pyramideDataHomme2.getNb());
				nbSalarie += pyramideDataHomme2.getNb();
				c++;
			}
			c = 0;
			for (PyramideDataBean pyramideDataFemme2 : pyramideDataListFemme) {
				nbFemmeCat.add(c, pyramideDataFemme2.getNb());
				nbSalarie += pyramideDataFemme2.getNb();
				c++;
			}

			// ajout au dataset des données concernant les hommes
			c = 0;
			for (PyramideDataBean pyramideDataHomme2 : pyramideDataListHomme) {
				if (pyramideDataHomme2.getDonnee() == 0) {
					libelle = ((Integer) pyramideDataHomme2.getDonnee())
							.toString()
							+ "-"
							+ ((Integer) (pyramideDataHomme2.getDonnee() + 1))
									.toString()
							+ "        "
							+ (nbSalarie == 0 ? df.format(0) : df
									.format((((nbHommeCat.get(c) + nbFemmeCat
											.get(c)) * 100) / Double
											.valueOf(nbSalarie)))) + "%";

				} else if (pyramideDataHomme2.getDonnee() >= 1
						&& pyramideDataHomme2.getDonnee() < 5) {
					libelle = ((Integer) pyramideDataHomme2.getDonnee())
							.toString()
							+ "-"
							+ ((Integer) (pyramideDataHomme2.getDonnee() + 1))
									.toString()
							+ "        "
							+ (nbSalarie == 0 ? df.format(0) : df
									.format((((nbHommeCat.get(c) + nbFemmeCat
											.get(c)) * 100) / Double
											.valueOf(nbSalarie)))) + "%";
				} else {
					libelle = ((Integer) (pyramideDataHomme2.getDonnee()))
							.toString()
							+ "-"
							+ ((Integer) (pyramideDataHomme2.getDonnee() + 5))
									.toString()
							+ ((pyramideDataHomme2.getDonnee() == 5) ? "      "
									: "    ")
							+ (nbSalarie == 0 ? df.format(0) : df
									.format((((nbHommeCat.get(c) + nbFemmeCat
											.get(c)) * 100) / Double
											.valueOf(nbSalarie)))) + "%";
				}
				c++;
				data.addValue((pyramideDataHomme2.getNb() * -1), "Homme",
						libelle);
			}
			c = 0;
			// ajout au dataset des données concernant les femmes
			for (PyramideDataBean pyramideDataFemme2 : pyramideDataListFemme) {

				if (pyramideDataFemme2.getDonnee() == 0) {
					libelle = ((Integer) pyramideDataFemme2.getDonnee())
							.toString()
							+ "-"
							+ ((Integer) (pyramideDataFemme2.getDonnee() + 1))
									.toString()
							+ "        "
							+ (nbSalarie == 0 ? df.format(0) : df
									.format((((nbHommeCat.get(c) + nbFemmeCat
											.get(c)) * 100) / Double
											.valueOf(nbSalarie)))) + "%";

				} else if (pyramideDataFemme2.getDonnee() >= 1
						&& pyramideDataFemme2.getDonnee() < 5) {
					libelle = ((Integer) pyramideDataFemme2.getDonnee())
							.toString()
							+ "-"
							+ ((Integer) (pyramideDataFemme2.getDonnee() + 1))
									.toString()
							+ "        "
							+ (nbSalarie == 0 ? df.format(0) : df
									.format((((nbHommeCat.get(c) + nbFemmeCat
											.get(c)) * 100) / Double
											.valueOf(nbSalarie)))) + "%";
				} else {
					libelle = ((Integer) (pyramideDataFemme2.getDonnee()))
							.toString()
							+ "-"
							+ ((Integer) (pyramideDataFemme2.getDonnee() + 5))
									.toString()
							+ ((pyramideDataFemme2.getDonnee() == 5) ? "      "
									: "    ")
							+ (nbSalarie == 0 ? df.format(0) : df
									.format((((nbHommeCat.get(c) + nbFemmeCat
											.get(c)) * 100) / Double
											.valueOf(nbSalarie)))) + "%";
				}
				c++;
				data.addValue(pyramideDataFemme2.getNb(), "Femme", libelle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return data;
		}
	}

	public KeyedValues2DDataset createDataset(String type, Integer entreprise,
			Integer service, Integer metier, Integer projection, int idGroupe)
			throws Exception {
		DefaultKeyedValues2DDataset data = new DefaultKeyedValues2DDataset();

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		List<SalarieBean> salarieList;
		List<PyramideDataBean> pyramideDataListHomme = new ArrayList<PyramideDataBean>();
		List<PyramideDataBean> pyramideDataListFemme = new ArrayList<PyramideDataBean>();

		try {
			// on retrouve le bon jeu de données
			SalarieService salarieService = new SalarieServiceImpl();
			salarieList = salarieService.getPyramideDataOrderedOderedByAge(
					entreprise, service, idGroupe);

			// fait un tri sur la liste et garde seulement les salarié qui sont
			// tjs dans l'entreprise.
			ArrayList<SalarieBean> salarieListTmp1 = new ArrayList<SalarieBean>();
			for (SalarieBean salarieBean : salarieList) {
				if (getLastParcours(salarieBean) != null
						&& getLastParcours(salarieBean)
								.getIdTypeContratSelected() != 3
						&& getLastParcours(salarieBean)
								.getIdTypeContratSelected() != 10) {
					ArrayList<ParcoursBean> parcoursBeanList = (ArrayList<ParcoursBean>) salarieBean
							.getParcoursBeanList();
					if (parcoursBeanList != null && parcoursBeanList.size() > 0) {
						Collections.sort(parcoursBeanList);
						Collections.reverse(parcoursBeanList);
						// récupération de le dernier porte occupé
						if (isExit(parcoursBeanList.get(0)) == false
								&& salarieBean.isPresent()) {
							salarieListTmp1.add(salarieBean);
						}
					}
				}
			}
			salarieList = salarieListTmp1;

			// fait un tri sur la liste et garde seulement les salarié qui ont
			// le metier séléctionné.
			if (metier != null && metier != 0 && metier != -1) {
				ArrayList<SalarieBean> salarieListTmp = new ArrayList<SalarieBean>();
				for (SalarieBean salarieBean : salarieList) {
					if (getLastParcours(salarieBean) != null
							&& getLastParcours(salarieBean)
									.getIdTypeContratSelected() != 3
							&& getLastParcours(salarieBean)
									.getIdTypeContratSelected() != 10) {
						ArrayList<ParcoursBean> parcoursBeanList = (ArrayList<ParcoursBean>) salarieBean
								.getParcoursBeanList();
						if (parcoursBeanList != null
								&& parcoursBeanList.size() > 0) {
							Collections.sort(parcoursBeanList);
							Collections.reverse(parcoursBeanList);
							// récupération de le dernier porte occupé
							if (metier.intValue() == parcoursBeanList.get(0)
									.getIdMetierSelected().intValue()) {
								salarieListTmp.add(salarieBean);
							}
						}
					}
				}
				salarieList = salarieListTmp;

			}

			// initialisation des variables
			int agePersonne = 0;
			int nbFemme;
			int nbHomme;
			GregorianCalendar dateDuJour = new GregorianCalendar();
			GregorianCalendar date = new GregorianCalendar();
			PyramideDataBean pyramideDataHomme;
			PyramideDataBean pyramideDataFemme;
			// on compte le nombre de personne par age ou ancienneté et sexe

			int i = 15;
			while (i <= 65) {
				nbFemme = 0;
				nbHomme = 0;
				for (SalarieBean salarie : salarieList) {
					if (getLastParcours(salarie) != null
							&& getLastParcours(salarie)
									.getIdTypeContratSelected() != 3
							&& getLastParcours(salarie)
									.getIdTypeContratSelected() != 10
							&& salarie.isPresent()) {
						// détermination de l'âge du salarié ou de l'ancienneté
						date.setTime(salarie.getDateNaissance());

						int diff = (dateDuJour.get(GregorianCalendar.YEAR))
								- date.get(GregorianCalendar.YEAR);
						if (dateDuJour.get(GregorianCalendar.MONTH) < date
								.get(GregorianCalendar.MONTH)) {
							agePersonne = diff - 1;
						} else {
							if (dateDuJour.get(GregorianCalendar.MONTH) == date
									.get(GregorianCalendar.MONTH)) {
								if (dateDuJour
										.get(GregorianCalendar.DAY_OF_MONTH) < date
										.get(GregorianCalendar.DAY_OF_MONTH)) {
									agePersonne = diff - 1;
								} else {
									agePersonne = diff;
								}
							} else {
								agePersonne = diff;
							}
						}

						agePersonne = agePersonne + projection;

						if (salarie.getCivilite().equals("Monsieur")) {
							if ((i < agePersonne) && (agePersonne <= (i + 5))) {

								nbHomme++;
							}
						} else {
							if ((i < agePersonne) && (agePersonne <= (i + 5))) {
								nbFemme++;
							}
						}
					}
				}

				pyramideDataHomme = new PyramideDataBean(i, "Homme", nbHomme);
				pyramideDataListHomme.add(pyramideDataHomme);

				pyramideDataFemme = new PyramideDataBean(i, "Femme", nbFemme);
				pyramideDataListFemme.add(pyramideDataFemme);
				i = i + 5;

				// récupère le nombre max de personne afin de créer un graphique
				// ayant la bonne échelle et le zéro centré
				if (nbMax < nbHomme) {
					nbMax = nbHomme;
				}
				if (nbMax < nbFemme) {
					nbMax = nbFemme;
				}
			}

			Collections.sort(pyramideDataListFemme);
			Collections.sort(pyramideDataListHomme);
			Collections.reverse(pyramideDataListFemme);
			Collections.reverse(pyramideDataListHomme);

			List<Integer> nbHommeCat = new ArrayList<Integer>();
			List<Integer> nbFemmeCat = new ArrayList<Integer>();

			int nbSalarie = 0;
			int c = 0;
			for (PyramideDataBean pyramideDataHomme2 : pyramideDataListHomme) {
				nbHommeCat.add(c, pyramideDataHomme2.getNb());
				nbSalarie += pyramideDataHomme2.getNb();
				c++;
			}
			c = 0;
			for (PyramideDataBean pyramideDataFemme2 : pyramideDataListFemme) {
				nbFemmeCat.add(c, pyramideDataFemme2.getNb());
				nbSalarie += pyramideDataFemme2.getNb();
				c++;
			}
			c = 0;
			// ajout au dataset des données concernant les hommes
			for (PyramideDataBean pyramideDataHomme2 : pyramideDataListHomme) {
				data.addValue(
						(pyramideDataHomme2.getNb() * -1),
						"Homme",
						((c == pyramideDataListHomme.size() - 1) ? ((Integer) pyramideDataHomme2
								.getDonnee()).toString()
								: ((Integer) (pyramideDataHomme2.getDonnee()))
										.toString())
								+ "-"
								+ ((Integer) (pyramideDataHomme2.getDonnee() + 5))
										.toString()
								+ "    "
								+ ((nbSalarie == 0) ? df.format(0.0)
										: df.format(((nbHommeCat.get(c) + nbFemmeCat
												.get(c)) * 100)
												/ Double.valueOf(nbSalarie)))
								+ "%");
				c++;
			}
			c = 0;
			// ajout au dataset des données concernant les femmes
			for (PyramideDataBean pyramideDataFemme2 : pyramideDataListFemme) {
				data.addValue(
						pyramideDataFemme2.getNb(),
						"Femme",
						((c == pyramideDataListFemme.size() - 1) ? ((Integer) pyramideDataFemme2
								.getDonnee()).toString()
								: ((Integer) (pyramideDataFemme2.getDonnee()))
										.toString())
								+ "-"
								+ ((Integer) (pyramideDataFemme2.getDonnee() + 5))
										.toString()
								+ "    "
								+ ((nbSalarie == 0) ? df.format(0.0)
										: df.format((((nbHommeCat.get(c) + nbFemmeCat
												.get(c)) * 100) / Double
												.valueOf(nbSalarie)))) + "%");
				c++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return data;
		}
	}

	public boolean isExit(ParcoursBean parcoursBeanTmp) {
		boolean exit = false;
		if (parcoursBeanTmp.getFinFonction() != null) {
			Calendar finFonction = new GregorianCalendar();
			finFonction.setTime(parcoursBeanTmp.getFinFonction());
			Calendar today = GregorianCalendar.getInstance();
			if (finFonction.before(today)) {
				exit = true;
			}
		}
		return exit;
	}
}
