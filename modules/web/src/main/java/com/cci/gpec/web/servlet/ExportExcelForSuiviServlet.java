package com.cci.gpec.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.CompetencesExportBean;
import com.cci.gpec.commons.ContratTravailBean;
import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.FicheDePosteBean;
import com.cci.gpec.commons.FicheMetierBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.LienRemunerationRevenuBean;
import com.cci.gpec.commons.ObjectifsEntretienBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.RemunerationBean;
import com.cci.gpec.commons.RevenuComplementaireBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.SalarieBeanExport;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.AccidentServiceImpl;
import com.cci.gpec.metier.implementation.ContratTravailServiceImpl;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.EntretienServiceImpl;
import com.cci.gpec.metier.implementation.FicheDePosteServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierServiceImpl;
import com.cci.gpec.metier.implementation.FormationServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.HabilitationServiceImpl;
import com.cci.gpec.metier.implementation.LienRemunerationRevenuServiceImpl;
import com.cci.gpec.metier.implementation.ObjectifsEntretienServiceImpl;
import com.cci.gpec.metier.implementation.ParcoursServiceImpl;
import com.cci.gpec.metier.implementation.RemunerationServiceImpl;
import com.cci.gpec.metier.implementation.RevenuComplementaireServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;

public class ExportExcelForSuiviServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WritableWorkbook workbook;
	private WritableSheet sheet;
	private ArrayList<SalarieBeanExport> salarieListBean;
	private String[] entete;
	private ByteArrayOutputStream baos;
	private int maxSizeNom;
	private int maxSizePrenom;
	private int maxSizeNomHabilitation;

	private int maxSizeNomFormation;
	private int maxSizeOrganismeFormation;
	private int maxSizeTitre;
	private int maxSizeDomaineFormation;

	private int maxSizeNomMetier;

	private int maxSizeNatureAbsence;

	private int maxSizeTypeAccident;
	private int maxSizeNbJoursArret;
	private int maxSizeSiegeLesion;
	private int maxSizeCauseAccident;

	private int maxSizeBesoinFormation;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private static int DUREE_VALIDITE_RESTANTE = 90;
	private static String VALIDITE_DEPASSEE = "Validit\u00E9 d\u00E9pass\u00E9e";
	private static String NOM_FIC_EXCEL_HABILITATION = "suiviDesHabilitations.xls";
	private static String NOM_FIC_EXCEL_FORMATION = "suiviDesFormations.xls";
	private static String NOM_FIC_EXCEL_ABSENCE = "suiviDesAbsences.xls";
	private static String NOM_FIC_EXCEL_ACCIDENT = "suiviDesAccidents.xls";
	private static String NOM_FIC_EXCEL_COMPETENCE = "suiviDesCompetences.xls";
	private static String NOM_FIC_EXCEL_ENTRETIEN = "suiviDesEntretiens.xls";
	private static String NOM_FIC_EXCEL_REMUNERATION = "suiviDesRemunerations.xls";
	private static String NOM_FIC_EXCEL_PARCOURS = "suiviDesParcoursProfessionnel.xls";
	private static String NOM_FIC_EXCEL_CONTRAT = "suiviDesContratsDeTravail.xls";

	private EntrepriseBean entrepriseBean;

	private int idGroupe;

	private String lettre;
	private int present;
	private GregorianCalendar debutExtraction;
	private GregorianCalendar finExtraction;

	/**
	 * 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) {

		String nomFichier = "";

		entete = (String[]) req.getSession().getAttribute("datatableEnTete");
		salarieListBean = (ArrayList<SalarieBeanExport>) req.getSession()
				.getAttribute("datatable");

		idGroupe = (Integer) req.getSession().getAttribute("groupe");

		int idEntrepriseSelected = (Integer) req.getSession().getAttribute(
				"idEntreprise");

		lettre = (String) req.getSession().getAttribute("alphabet");
		present = (Integer) req.getSession().getAttribute("present");

		debutExtraction = (GregorianCalendar) req.getSession().getAttribute(
				"debut");
		finExtraction = (GregorianCalendar) req.getSession()
				.getAttribute("fin");

		entrepriseBean = new EntrepriseBean();
		if (idEntrepriseSelected != -1 && idEntrepriseSelected != 0) {
			EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
			try {
				entrepriseBean = entrepriseService
						.getEntrepriseBeanById(idEntrepriseSelected);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		baos = new ByteArrayOutputStream();

		try {

			workbook = Workbook.createWorkbook(baos);
			sheet = workbook.createSheet("First Sheet", 0);

			setEntete();
			maxSizeNom = 10;
			maxSizePrenom = 10;
			maxSizeNomHabilitation = 15;

			maxSizeNomFormation = 15;
			maxSizeOrganismeFormation = 15;
			maxSizeTitre = 15;
			maxSizeDomaineFormation = 15;

			maxSizeNomMetier = 10;

			maxSizeNatureAbsence = 14;

			maxSizeTypeAccident = 13;
			maxSizeNbJoursArret = 15;
			maxSizeSiegeLesion = 13;
			maxSizeCauseAccident = 13;

			maxSizeBesoinFormation = 14;

			String name = (String) req.getSession().getAttribute("name");

			if (name == "exportExcelHabilitations") {
				exportHabilitations(null);
				nomFichier = NOM_FIC_EXCEL_HABILITATION;
			}
			if (name == "exportExcelHabilitationsList") {
				List<HabilitationBean> hab = (ArrayList<HabilitationBean>) req
						.getSession().getAttribute("list");
				exportHabilitations(hab);
				nomFichier = NOM_FIC_EXCEL_HABILITATION;
			}
			if (name.equals("exportExcelFormations")) {
				exportFormations(null);
				nomFichier = NOM_FIC_EXCEL_FORMATION;
			}
			if (name.equals("exportExcelFormationsList")) {
				List<FormationBean> form = (ArrayList<FormationBean>) req
						.getSession().getAttribute("list");
				exportFormations(form);
				nomFichier = NOM_FIC_EXCEL_FORMATION;
			}
			if (name == "exportExcelAbsences") {
				exportAbsences(null);
				nomFichier = NOM_FIC_EXCEL_ABSENCE;
			}
			if (name == "exportExcelAbsencesList") {
				List<AbsenceBean> abs = (ArrayList<AbsenceBean>) req
						.getSession().getAttribute("list");
				exportAbsences(abs);
				nomFichier = NOM_FIC_EXCEL_ABSENCE;
			}
			if (name == "exportExcelAccidents") {
				exportAccidents(null);
				nomFichier = NOM_FIC_EXCEL_ACCIDENT;
			}
			if (name == "exportExcelAccidentsList") {
				List<AccidentBean> acc = (ArrayList<AccidentBean>) req
						.getSession().getAttribute("list");
				exportAccidents(acc);
				nomFichier = NOM_FIC_EXCEL_ACCIDENT;
			}
			if (name == "exportExcelParcoursProfessionnels") {
				exportParcoursProfessionnels(null);
				nomFichier = NOM_FIC_EXCEL_PARCOURS;
			}
			if (name == "exportExcelParcoursProfessionnelsList") {
				List<ParcoursBean> acc = (ArrayList<ParcoursBean>) req
						.getSession().getAttribute("list");
				exportParcoursProfessionnels(acc);
				nomFichier = NOM_FIC_EXCEL_PARCOURS;
			}
			if (name == "exportExcelContratTravail") {
				exportContratTravail(null);
				nomFichier = NOM_FIC_EXCEL_CONTRAT;
			}
			if (name == "exportExcelContratTravailList") {
				List<ContratTravailBean> ct = (ArrayList<ContratTravailBean>) req
						.getSession().getAttribute("list");
				exportContratTravail(ct);
				nomFichier = NOM_FIC_EXCEL_CONTRAT;
			}
			if (name == "exportExcelCompetences") {
				exportCompetences();
				nomFichier = NOM_FIC_EXCEL_COMPETENCE;
			}
			if (name == "exportExcelEntretiens") {
				exportEntretiens(null);
				nomFichier = NOM_FIC_EXCEL_ENTRETIEN;
			}
			if (name == "exportExcelEntretiensList") {
				List<EntretienBean> ent = (ArrayList<EntretienBean>) req
						.getSession().getAttribute("list");
				exportEntretiens(ent);
				nomFichier = NOM_FIC_EXCEL_ENTRETIEN;
			}
			if (name == "exportExcelRemunerations") {
				exportRemunerations();
				nomFichier = NOM_FIC_EXCEL_REMUNERATION;
			}

			end();
		} catch (Exception e) {
			e.printStackTrace();
		}
		res.addHeader("Content-disposition", "attachment;filename="
				+ nomFichier);
		res.setContentType("application/xls");
		res.setContentLength(baos.size());

		ServletOutputStream out;
		try {
			out = res.getOutputStream();
			baos.writeTo(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		doGet(req, res);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void end() throws Exception {
		workbook.write();
		workbook.close();
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void setEntete() throws Exception {
		sheet.setColumnView(0, 30);
		sheet.setRowView(0, 800);
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(jxl.format.Alignment.CENTRE);
		format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		format.setWrap(true);

		Label label;
		int j = 0;
		int length = entete.length;
		for (int i = 0; i < length; i++) {
			if (entete[j].equals("Entreprise")) {
				if (entrepriseBean.getId() == -1 || entrepriseBean.getId() == 0) {
					label = new Label(i, 4, entete[j]);
					label.setCellFormat(format);
					sheet.addCell(label);
				} else {
					j++;
					length--;
					label = new Label(i, 4, entete[j]);
					label.setCellFormat(format);
					sheet.addCell(label);
				}
			} else {
				label = new Label(i, 4, entete[j]);
				label.setCellFormat(format);
				sheet.addCell(label);
			}
			j++;
		}
	}

	/**
	 * 
	 * @param parcoursBeanList
	 * @return
	 * @throws Exception
	 */
	private String getNomMetier(List<ParcoursBean> parcoursBeanList,
			Date dateRecherche) throws Exception {

		Calendar date = new GregorianCalendar();
		date.setTime(dateRecherche);

		Calendar debut = new GregorianCalendar();
		Calendar fin = new GregorianCalendar();

		Collections.sort(parcoursBeanList);
		Collections.reverse(parcoursBeanList);

		for (ParcoursBean parcours : parcoursBeanList) {
			debut.setTime(parcours.getDebutFonction());
			if (parcours.getFinFonction() != null) {
				fin.setTime(parcours.getFinFonction());
			} else {
				fin.setTime(new Date());
				fin.add(Calendar.YEAR, 99);
			}
			if ((date.after(debut) && date.before(fin)) || date.equals(debut)
					|| date.equals(fin)) {
				return parcours.getNomMetier();
			}
		}

		return "";
	}

	private String getNomMetier(List<ParcoursBean> parcoursBeanList,
			Date dateDebutRecherche, Date dateFinRecherche) throws Exception {

		Calendar dateDebut = new GregorianCalendar();
		dateDebut.setTime(dateDebutRecherche);

		Calendar dateFin = new GregorianCalendar();
		dateFin.setTime(dateFinRecherche);

		Calendar debut = new GregorianCalendar();
		Calendar fin = new GregorianCalendar();

		Collections.sort(parcoursBeanList);
		Collections.reverse(parcoursBeanList);

		for (ParcoursBean parcours : parcoursBeanList) {
			debut.setTime(parcours.getDebutFonction());
			if (parcours.getFinFonction() != null) {
				fin.setTime(parcours.getFinFonction());
			} else {
				fin.setTime(new Date());
				fin.add(Calendar.YEAR, 99);
			}
			if ((dateDebut.after(debut) && dateDebut.before(fin))
					|| dateDebut.equals(debut) || dateDebut.equals(fin)) {
				return parcours.getNomMetier();
			}
			if ((dateFin.after(debut) && dateFin.before(fin))
					|| dateFin.equals(debut) || dateFin.equals(fin)) {
				return parcours.getNomMetier();
			}
		}

		return "";
	}

	private String getNomMetier(List<ParcoursBean> parcoursBeanList, int annee)
			throws Exception {

		Calendar debut = new GregorianCalendar();
		Calendar fin = new GregorianCalendar();

		Collections.sort(parcoursBeanList);
		Collections.reverse(parcoursBeanList);

		for (ParcoursBean parcours : parcoursBeanList) {
			debut.setTime(parcours.getDebutFonction());
			if (parcours.getFinFonction() != null) {
				fin.setTime(parcours.getFinFonction());
			} else {
				fin.setTime(new Date());
				fin.add(Calendar.YEAR, 99);
			}
			if (debut.get(Calendar.YEAR) == annee
					|| fin.get(Calendar.YEAR) == annee
					|| (debut.get(Calendar.YEAR) <= annee && fin
							.get(Calendar.YEAR) >= annee)) {
				return parcours.getNomMetier();
			}
		}

		return "";
	}

	private String getNomCSP(List<ParcoursBean> parcoursBeanList, int annee)
			throws Exception {

		Calendar debut = new GregorianCalendar();
		Calendar fin = new GregorianCalendar();

		for (ParcoursBean parcours : parcoursBeanList) {
			debut.setTime(parcours.getDebutFonction());
			if (parcours.getFinFonction() != null) {
				fin.setTime(parcours.getFinFonction());
			} else {
				fin.setTime(new Date());
				fin.add(Calendar.YEAR, 99);
			}
			if (debut.get(Calendar.YEAR) == annee
					|| fin.get(Calendar.YEAR) == annee
					|| (debut.get(Calendar.YEAR) <= annee && fin
							.get(Calendar.YEAR) >= annee)) {
				return parcours.getNomTypeStatut();
			}
		}

		return "";
	}

	/**
	 * Retourne le dernier parcours d'un salarie, string vide s'il n'y en a pas
	 * 
	 * @param parcoursBeanList
	 * @return
	 */
	private ParcoursBean getLastParcours(List<ParcoursBean> parcoursBeanList) {
		ParcoursBean pb = null;
		for (int i = 0; i < parcoursBeanList.size(); i++) {
			ParcoursBean parcour = parcoursBeanList.get(i);
			if (pb == null) {
				pb = parcour;
			}
			if (parcour.getDebutFonction().after(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void exportHabilitations(List<HabilitationBean> list)
			throws Exception {
		sheet.setColumnView(0, 20);
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 9, WritableFont.NO_BOLD));
		format2.setAlignment(Alignment.LEFT);
		format2.setWrap(true);
		format2.setVerticalAlignment(VerticalAlignment.CENTRE);

		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(0, 0, 13, 0);
			sheet.mergeCells(0, 1, 13, 1);
			sheet.mergeCells(0, 2, 13, 2);
		} else {
			sheet.mergeCells(0, 0, 12, 0);
			sheet.mergeCells(0, 1, 12, 1);
			sheet.mergeCells(0, 2, 12, 2);
		}

		int j = 0;
		sheet.mergeCells(j, 4, j, 5);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(j + 1, 4, j + 1, 5);
			j++;
		}
		sheet.mergeCells(j + 1, 4, j + 1, 5);
		sheet.mergeCells(j + 2, 4, j + 2, 5);
		sheet.mergeCells(j + 3, 4, j + 3, 5);
		sheet.mergeCells(j + 4, 4, j + 4, 5);
		sheet.mergeCells(j + 5, 4, j + 5, 5);
		sheet.mergeCells(j + 6, 4, j + 6, 5);
		sheet.mergeCells(j + 7, 4, j + 7, 5);
		sheet.mergeCells(j + 8, 4, j + 9, 4);
		sheet.mergeCells(j + 10, 4, j + 10, 5);
		sheet.mergeCells(j + 11, 4, j + 11, 5);
		sheet.mergeCells(j + 12, 4, j + 12, 5);

		// titre du tableau
		if (entrepriseBean.getNom() == null) {
			entrepriseBean.setNom("");
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean groupe = grServ.getGroupeBeanById(idGroupe);
			entrepriseBean.setNom(groupe.getNom());
		}
		Label label = new Label(0, 0, "TABLEAU DE SUIVI DES HABILITATIONS \n"
				+ entrepriseBean.getNom() + " ("
				+ dateFormat.format(new Date()) + ")");
		label.setCellFormat(format);
		sheet.addCell(label);

		Label label2 = new Label(
				0,
				1,
				"Objectif : suivi des obligations de l'entreprise en matière de sécurité au travail / démarche qualité / suivi des documents obligatoires ");
		label2.setCellFormat(format);
		sheet.addCell(label2);

		String filtre;
		switch (present) {
		case 0:
			filtre = "Tous les salariés";
			break;
		case 1:
			filtre = "Seuls les salariés sortis";
			break;
		case 2:
			filtre = "Seuls les salariés présents";
			break;
		default:
			filtre = "Aucun filtre disponible";
			break;
		}
		String debExt = (debutExtraction != null) ? dateFormat
				.format(debutExtraction.getTime()) : "";
		String finExt = (finExtraction != null) ? dateFormat
				.format(finExtraction.getTime()) : "";

		if (!debExt.equals("")) {
			filtre += " - Début : "
					+ dateFormat.format(debutExtraction.getTime());
		}
		if (!finExt.equals("")) {
			filtre += " - Fin : " + dateFormat.format(finExtraction.getTime());
		}
		Label label3 = new Label(0, 2, "Filtre : " + filtre);

		label3.setCellFormat(format);
		sheet.addCell(label3);
		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);

		int row = 6;
		String habilitation = "";
		String debut = "";
		String fin = "";
		String duree = "";
		boolean habilRouge = false;
		boolean validRouge = false;

		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			label = new Label(9, 5, "du");
			label.setCellFormat(format);
			sheet.addCell(label);

			label = new Label(10, 5, "au");
			label.setCellFormat(format);
			sheet.addCell(label);
		} else {
			label = new Label(8, 5, "du");
			label.setCellFormat(format);
			sheet.addCell(label);

			label = new Label(9, 5, "au");
			label.setCellFormat(format);
			sheet.addCell(label);
		}

		ServiceImpl serv = new ServiceImpl();
		SalarieServiceImpl salServ = new SalarieServiceImpl();
		HabilitationServiceImpl ctServ = new HabilitationServiceImpl();
		List<HabilitationBean> habilitationBeanList;
		if (list == null) {
			if (entrepriseBean.getId() > 0) {
				habilitationBeanList = ctServ
						.getHabilitationsListOrderByAnneeNomSalarie(entrepriseBean
								.getId());
			} else {
				habilitationBeanList = ctServ
						.getHabilitationsListOrderByAnneeNomEntrepriseNomSalarie(idGroupe);
			}
		} else {
			habilitationBeanList = list;
		}

		// On applique les bornes voulues
		Iterator<HabilitationBean> ite = habilitationBeanList.iterator();

		boolean remove = false;

		while (ite.hasNext()) {

			remove = false;

			HabilitationBean habilitationBean = ite.next();
			GregorianCalendar debD1 = new GregorianCalendar();
			debD1.setTime(habilitationBean.getDelivrance());
			GregorianCalendar finD1 = new GregorianCalendar();
			finD1.setTime(habilitationBean.getExpiration());

			if (!isInPeriode(debutExtraction, finExtraction, debD1, finD1)) {
				ite.remove();
				remove = true;
			}

			Boolean isPresent = salServ.isSalariePresent(habilitationBean
					.getIdSalarie());
			if (!remove) {
				if (present == 1) {
					if (isPresent) {
						ite.remove();
					}
				}
				if (present == 2) {
					if (!isPresent) {
						ite.remove();
					}
				}
			}
		}

		for (HabilitationBean habilitationBean : habilitationBeanList) {

			SalarieBeanExport salarie = salServ
					.getSalarieBeanExportById(habilitationBean.getIdSalarie());

			if (list != null) {
				salarie.setService(serv.getServiceBeanById(
						salarie.getIdServiceSelected()).getNom());
			}

			habilitation = habilitationBean.getNomTypeHabilitation() + " ";
			// test si l'habilitation a une validité inférieure ou égale à
			// DUREE_VALIDITE_RESTANTE
			// si c'est la cas la ligne est colorée en orange
			if ((0 < habilitationBean.getDureeValidite())
					&& (habilitationBean.getDureeValidite() <= DUREE_VALIDITE_RESTANTE)) {

				habilRouge = true;
			}
			debut = dateFormat.format(habilitationBean.getDelivrance()) + " ";
			fin = dateFormat.format(habilitationBean.getExpiration()) + " ";
			if (habilitationBean.getDureeValidite() < 0) {
				duree = VALIDITE_DEPASSEE + " ";
				validRouge = true;
			} else {
				duree = habilitationBean.getDureeValidite() + " ";
			}

			// }
			if (salarie.getHabilitationBeanList().size() > 0) {
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setWrap(true);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);

				int i = 0;

				// Année référence
				GregorianCalendar annee = new GregorianCalendar();
				annee.setTime(habilitationBean.getDelivrance());
				label = new Label(i, row, annee.get(Calendar.YEAR) + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				// Entreprise
				if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
					EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
					label = new Label(i + 1, row, entServ
							.getEntrepriseBeanById(
									salarie.getIdEntrepriseSelected()).getNom());
					label.setCellFormat(format2);
					sheet.addCell(label);
					i++;
				}

				// Nom
				label = new Label(i + 1, row, salarie.getNom());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizeNom < label.getContents().length()) {
					maxSizeNom = label.getContents().length();
				}

				// Prénom
				label = new Label(i + 2, row, salarie.getPrenom());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizePrenom < label.getContents().length()) {
					maxSizePrenom = label.getContents().length();
				}

				// Date de naissance
				GregorianCalendar naissance = new GregorianCalendar();
				naissance.setTime(salarie.getDateNaissance());
				label = new Label(i + 3, row, dateFormat.format(naissance
						.getTime()) + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				// Lieu de naissance
				label = new Label(i + 4, row, salarie.getLieuNaissance());
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Service
				if (salarie.getService() != null) {
					label = new Label(i + 5, row, salarie.getService());
				} else {
					label = new Label(i + 5, row, "");
				}
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Poste occupé
				label = new Label(i + 6, row, getNomMetier(
						salarie.getParcoursBeanList(),
						habilitationBean.getDelivrance(),
						habilitationBean.getExpiration()));
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizeNomMetier < label.getContents().length()) {
					maxSizeNomMetier = label.getContents().length();
				}

				// test si l'habilitation a une validité inférieure ou égale
				// à DUREE_VALIDITE_RESTANTE
				// si c'est la cas la ligne est colorée en orange

				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.LEFT);
				format.setWrap(true);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
				if (habilRouge) {
					format.setBackground(Colour.ORANGE);
				}
				// Habilitation
				label = new Label(i + 7, row, habilitation);
				label.setCellFormat(format);
				sheet.addCell(label);
				if (maxSizeNomHabilitation < label.getContents().length()) {
					maxSizeNomHabilitation = label.getContents().length();
				}

				habilRouge = false;
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setWrap(true);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);

				// Début validité
				label = new Label(i + 8, row, debut);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Fin de validité
				label = new Label(i + 9, row, fin);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Délai de validité en jours

				WritableFont boldRedFont;
				WritableCellFormat boldRed;

				boldRedFont = new WritableFont(WritableFont.TIMES, 10,
						WritableFont.BOLD);

				boldRedFont.setColour(Colour.RED);
				boldRed = new WritableCellFormat(boldRedFont);
				boldRed.setAlignment(Alignment.LEFT);
				boldRed.setVerticalAlignment(VerticalAlignment.CENTRE);
				boldRed.setWrap(true);

				// test si l'habilitation a une validité inférieure à 0
				// si c'est la cas la ligne est colorée en rouge
				if (validRouge) {
					label = new Label(i + 10, row, duree);
					label.setCellFormat(boldRed);
				} else {
					label = new Label(i + 10, row, duree);
					label.setCellFormat(format2);
				}
				validRouge = false;
				sheet.addCell(label);

				// Commentaires
				label = new Label(i + 11, row,
						habilitationBean.getCommentaire());
				label.setCellFormat(format2);
				sheet.addCell(label);

				row++;
				habilitation = "";
				debut = "";
				fin = "";
				duree = "";
			}
		}
		// }

		int i = 0;

		sheet.setColumnView(i, 12);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.setColumnView(i + 1, 20);
			i++;
		}
		sheet.setColumnView(i + 1, 20);
		sheet.setColumnView(i + 2, 20);
		sheet.setColumnView(i + 3, 12);
		sheet.setColumnView(i + 4, 20);
		sheet.setColumnView(i + 5, 20);
		sheet.setColumnView(i + 6, 20);
		sheet.setColumnView(i + 7, 30);
		sheet.setColumnView(i + 8, 14);
		sheet.setColumnView(i + 9, 14);
		sheet.setColumnView(i + 10, 16);
		sheet.setColumnView(i + 11, 40);
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void exportFormations(List<FormationBean> list) throws Exception {

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		sheet.setColumnView(0, 20);
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 9, WritableFont.NO_BOLD));
		format2.setAlignment(Alignment.LEFT);
		format2.setWrap(true);
		format2.setVerticalAlignment(VerticalAlignment.CENTRE);

		WritableCellFormat format3 = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 9, WritableFont.NO_BOLD));
		format3.setAlignment(Alignment.RIGHT);
		format3.setWrap(true);
		format3.setVerticalAlignment(VerticalAlignment.CENTRE);

		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(0, 0, 18, 0);
			sheet.mergeCells(0, 1, 18, 1);
			sheet.mergeCells(0, 2, 18, 2);
		} else {
			sheet.mergeCells(0, 0, 17, 0);
			sheet.mergeCells(0, 1, 17, 1);
			sheet.mergeCells(0, 2, 17, 2);
		}

		// Fusion des cellules pour les sous-colonnes
		int j = 0;
		sheet.mergeCells(j, 4, j, 5);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(j + 1, 4, j + 1, 5);
			j++;
		}
		sheet.mergeCells(j + 1, 4, j + 1, 5);
		sheet.mergeCells(j + 2, 4, j + 2, 5);
		sheet.mergeCells(j + 3, 4, j + 3, 5);
		sheet.mergeCells(j + 4, 4, j + 4, 5);
		sheet.mergeCells(j + 5, 4, j + 8, 4);
		sheet.mergeCells(j + 9, 4, j + 9, 5);
		sheet.mergeCells(j + 10, 4, j + 11, 4);
		sheet.mergeCells(j + 12, 4, j + 12, 5);
		sheet.mergeCells(j + 13, 4, j + 15, 4);
		sheet.mergeCells(j + 16, 4, j + 17, 4);

		j = 0;
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			j++;
		}
		Label souscolonne1 = new Label(j + 5, 5, "Formations réalisées");
		Label souscolonne2 = new Label(j + 6, 5, "Date de début de formation");
		Label souscolonne3 = new Label(j + 7, 5, "Date de fin de formation");
		Label souscolonne4 = new Label(j + 8, 5, "Nombre d'heures de formation");
		Label souscolonne5 = new Label(j + 10, 5, "Initial");
		Label souscolonne6 = new Label(j + 11, 5, "Atteint");
		Label souscolonne7 = new Label(j + 13, 5, "OPCA");
		Label souscolonne8 = new Label(j + 14, 5, "Entreprise");
		Label souscolonne9 = new Label(j + 15, 5, "Autre");
		Label souscolonne10 = new Label(j + 16, 5,
				"Heures de formation au titre du DIF");
		Label souscolonne11 = new Label(j + 17, 5, "Solde DIF");

		souscolonne1.setCellFormat(format);
		sheet.addCell(souscolonne1);
		souscolonne2.setCellFormat(format);
		sheet.addCell(souscolonne2);
		souscolonne3.setCellFormat(format);
		sheet.addCell(souscolonne3);
		souscolonne4.setCellFormat(format);
		sheet.addCell(souscolonne4);
		souscolonne5.setCellFormat(format);
		sheet.addCell(souscolonne5);
		souscolonne6.setCellFormat(format);
		sheet.addCell(souscolonne6);
		souscolonne7.setCellFormat(format);
		sheet.addCell(souscolonne7);
		souscolonne8.setCellFormat(format);
		sheet.addCell(souscolonne8);
		souscolonne9.setCellFormat(format);
		sheet.addCell(souscolonne9);
		souscolonne10.setCellFormat(format);
		sheet.addCell(souscolonne10);
		souscolonne11.setCellFormat(format);
		sheet.addCell(souscolonne11);

		if (entrepriseBean.getNom() == null) {
			entrepriseBean.setNom("");
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean groupe = grServ.getGroupeBeanById(idGroupe);
			entrepriseBean.setNom(groupe.getNom());
		}
		Label label = new Label(0, 0, "SUIVI DES PARCOURS DE FORMATION \n"
				+ entrepriseBean.getNom() + " ("
				+ dateFormat.format(new Date()) + ")");

		Label label2 = new Label(
				0,
				1,
				"Objectif : utilisation du budget formation / suivi du réalisé du plan de formation / suivi des compteurs DIF");

		String filtre;
		switch (present) {
		case 0:
			filtre = "Tous les salariés";
			break;
		case 1:
			filtre = "Seuls les salariés sortis";
			break;
		case 2:
			filtre = "Seuls les salariés présents";
			break;
		default:
			filtre = "Aucun filtre disponible";
			break;
		}

		String debExt = (debutExtraction != null) ? dateFormat
				.format(debutExtraction.getTime()) : "";
		String finExt = (finExtraction != null) ? dateFormat
				.format(finExtraction.getTime()) : "";

		if (!debExt.equals("")) {
			filtre += " - Début : "
					+ dateFormat.format(debutExtraction.getTime());
		}
		if (!finExt.equals("")) {
			filtre += " - Fin : " + dateFormat.format(finExtraction.getTime());
		}

		Label label3 = new Label(0, 2, "Filtre : " + filtre);

		label.setCellFormat(format);
		sheet.addCell(label);
		label2.setCellFormat(format);
		sheet.addCell(label2);
		label3.setCellFormat(format);
		sheet.addCell(label3);

		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		int row = 6;
		String valueDif = "";

		String formationRealisee = "";
		String debutFormation = "";
		String finFormation = "";
		String volume = "";
		String nature = "";
		String niveauInitial = "";
		String niveauAtteint = "";
		String organisme = "";
		String coutOPCA = "";
		String coutEntreprise = "";
		String coutAutre = "";

		// Ajouts dans les specs -> non rensigné pour l'instant
		String heuresFormationDIF = "";
		String soldeDIF = "";

		ServiceImpl serv = new ServiceImpl();
		SalarieServiceImpl salServ = new SalarieServiceImpl();
		FormationServiceImpl ctServ = new FormationServiceImpl();
		List<FormationBean> formationBeanList;
		if (list == null) {
			if (entrepriseBean.getId() > 0) {
				formationBeanList = ctServ
						.getFormationsListOrderByAnneeNomSalarie(entrepriseBean
								.getId());
			} else {
				formationBeanList = ctServ
						.getFormationsListOrderByAnneeNomEntrepriseNomSalarie(idGroupe);
			}
		} else {
			formationBeanList = list;
		}

		// On applique les bornes voulues
		Iterator<FormationBean> ite = formationBeanList.iterator();

		boolean remove = false;

		while (ite.hasNext()) {

			remove = false;

			FormationBean formationBean = ite.next();
			GregorianCalendar debD1 = new GregorianCalendar();
			debD1.setTime(formationBean.getDebutFormation());
			GregorianCalendar finD1 = new GregorianCalendar();
			finD1.setTime(formationBean.getFinFormation());

			if (!isInPeriode(debutExtraction, finExtraction, debD1, finD1)) {
				ite.remove();
				remove = true;
			}

			Boolean isPresent = salServ.isSalariePresent(formationBean
					.getIdSalarie());
			if (!remove) {
				if (present == 1) {
					if (isPresent) {
						ite.remove();
					}
				}
				if (present == 2) {
					if (!isPresent) {
						ite.remove();
					}
				}
			}
		}

		for (FormationBean formationBean : formationBeanList) {

			SalarieBeanExport salarie = salServ
					.getSalarieBeanExportById(formationBean.getIdSalarie());

			if (list != null) {
				salarie.setService(serv.getServiceBeanById(
						salarie.getIdServiceSelected()).getNom());
			}

			formationRealisee = formationBean.getNomFormation();
			debutFormation = (formationBean.getDebutFormation() != null) ? dateFormat
					.format(formationBean.getDebutFormation()) : "";
			finFormation = (formationBean.getFinFormation() != null) ? dateFormat
					.format(formationBean.getFinFormation()) : "";
			volume = formationBean.getVolumeHoraire() + "";
			nature = formationBean.getNomDomaineFormation();
			niveauInitial = salarie.getNivFormationInit();
			niveauAtteint = salarie.getNivFormationAtteint();
			organisme = formationBean.getOrganismeFormation();
			coutOPCA = (formationBean.getCoutOpca() != null) ? df
					.format(formationBean.getCoutOpca()) : "";
			coutEntreprise = (formationBean.getCoutEntreprise() != null) ? df
					.format(formationBean.getCoutEntreprise()) : "";

			coutAutre = (formationBean.getCoutAutre() != null) ? df
					.format(formationBean.getCoutAutre()) : "";

			heuresFormationDIF = (formationBean.getDif() != null) ? df
					.format(formationBean.getDif()) : "0.0";

			// SOLDE DIF
			valueDif = df.format(salarie.getCreditDif());
			soldeDIF = valueDif + "";

			if (salarie.getFormationBeanList().size() > 0) {

				int i = 0;

				// Année référence
				GregorianCalendar annee = new GregorianCalendar();
				annee.setTime(formationBean.getDebutFormation());
				label = new Label(i, row, annee.get(Calendar.YEAR) + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				// Entreprise
				if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
					EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
					label = new Label(i + 1, row, entServ
							.getEntrepriseBeanById(
									salarie.getIdEntrepriseSelected()).getNom());
					label.setCellFormat(format2);
					sheet.addCell(label);
					i++;
				}

				// Nom
				label = new Label(i + 1, row, salarie.getNom());
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Prénom
				label = new Label(i + 2, row, salarie.getPrenom());
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Service
				if (salarie.getService() != null) {
					label = new Label(i + 3, row, salarie.getService());
				} else {
					label = new Label(i + 3, row, "");
				}
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Poste occupé
				label = new Label(i + 4, row, getNomMetier(
						salarie.getParcoursBeanList(),
						formationBean.getDebutFormation(),
						formationBean.getFinFormation()));
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Formation realisee
				label = new Label(i + 5, row, formationRealisee);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Début de la formation
				label = new Label(i + 6, row, debutFormation);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Fin de la formation
				label = new Label(i + 7, row, finFormation);
				label.setCellFormat(format);
				sheet.addCell(label);

				// volume d'hrs formation
				label = new Label(i + 8, row, volume);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Nature de la formation
				label = new Label(i + 9, row, nature);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Niveau formation initial
				label = new Label(i + 10, row, niveauInitial);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Niveau formation atteint
				label = new Label(i + 11, row, niveauAtteint);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Organisme de formation
				label = new Label(i + 12, row, organisme);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Cout OPCA
				label = new Label(i + 13, row, coutOPCA);
				label.setCellFormat(format3);
				sheet.addCell(label);

				// Cout Entreprise
				label = new Label(i + 14, row, coutEntreprise);
				label.setCellFormat(format3);
				sheet.addCell(label);

				// Cout Autre
				label = new Label(i + 15, row, coutAutre);
				label.setCellFormat(format3);
				sheet.addCell(label);

				// Heures formation DIF
				label = new Label(i + 16, row, heuresFormationDIF);
				label.setCellFormat(format3);
				sheet.addCell(label);

				// Solde DIF
				label = new Label(i + 17, row, soldeDIF);
				label.setCellFormat(format3);
				sheet.addCell(label);

				row++;
				formationRealisee = "";
				debutFormation = "";
				finFormation = "";
				volume = "";
				nature = "";
				niveauInitial = "";
				niveauAtteint = "";
				organisme = "";
				coutOPCA = "";
				coutEntreprise = "";
				heuresFormationDIF = "";
				soldeDIF = "";
			}
		}

		int i = 0;

		sheet.setColumnView(i, 12);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.setColumnView(i + 1, 20);
			i++;
		}
		sheet.setColumnView(i + 1, 20);
		sheet.setColumnView(i + 2, 20);
		sheet.setColumnView(i + 3, 20);
		sheet.setColumnView(i + 4, 20);
		sheet.setColumnView(i + 5, 30);
		sheet.setColumnView(i + 6, 15);
		sheet.setColumnView(i + 7, 15);
		sheet.setColumnView(i + 8, 15);
		sheet.setColumnView(i + 9, 20);
		sheet.setColumnView(i + 10, 12);
		sheet.setColumnView(i + 11, 12);
		sheet.setColumnView(i + 12, 20);
		sheet.setColumnView(i + 13, 15);
		sheet.setColumnView(i + 14, 15);
		sheet.setColumnView(i + 15, 15);
		sheet.setColumnView(i + 16, 15);

	}

	/**
	 * 
	 * @throws Exception
	 */
	private void exportAbsences(List<AbsenceBean> list) throws Exception {
		sheet.setColumnView(0, 20);
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 9, WritableFont.NO_BOLD));
		format2.setAlignment(Alignment.LEFT);
		format2.setWrap(true);
		format2.setVerticalAlignment(VerticalAlignment.CENTRE);

		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(0, 0, 10, 1);
			sheet.mergeCells(0, 2, 10, 2);
			sheet.mergeCells(0, 3, 10, 3);
		} else {
			sheet.mergeCells(0, 0, 9, 1);
			sheet.mergeCells(0, 2, 9, 2);
			sheet.mergeCells(0, 3, 9, 3);
		}
		if (entrepriseBean.getNom() == null) {
			entrepriseBean.setNom("");
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean groupe = grServ.getGroupeBeanById(idGroupe);
			entrepriseBean.setNom(groupe.getNom());
		}

		Label label = new Label(0, 0,
				"TABLEAU DE SUIVI DES ABSENCES ET DES CONGES\n"
						+ entrepriseBean.getNom() + " ("
						+ dateFormat.format(new Date()) + ")");
		label.setCellFormat(format);
		sheet.addCell(label);

		Label label2 = new Label(
				0,
				2,
				"Objectifs : identifier les motifs des absences / le nombre de jours d'absences annuels par salarié ");
		label2.setCellFormat(format);
		sheet.addCell(label2);

		String filtre;
		switch (present) {
		case 0:
			filtre = "Tous les salariés";
			break;
		case 1:
			filtre = "Seuls les salariés sortis";
			break;
		case 2:
			filtre = "Seuls les salariés présents";
			break;
		default:
			filtre = "Aucun filtre disponible";
			break;
		}

		String debExt = (debutExtraction != null) ? dateFormat
				.format(debutExtraction.getTime()) : "";
		String finExt = (finExtraction != null) ? dateFormat
				.format(finExtraction.getTime()) : "";

		if (!debExt.equals("")) {
			filtre += " - Début : "
					+ dateFormat.format(debutExtraction.getTime());
		}
		if (!finExt.equals("")) {
			filtre += " - Fin : " + dateFormat.format(finExtraction.getTime());
		}
		Label label3 = new Label(0, 3, "Filtre : " + filtre);

		label3.setCellFormat(format);
		sheet.addCell(label3);
		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		int row = 5;
		String nature = "";
		String debut = "";
		String fin = "";
		String duree = "";

		ServiceImpl serv = new ServiceImpl();
		SalarieServiceImpl salServ = new SalarieServiceImpl();
		AbsenceServiceImpl absServ = new AbsenceServiceImpl();
		List<AbsenceBean> absenceBeanList;
		if (list == null) {
			if (entrepriseBean.getId() > 0) {
				absenceBeanList = absServ
						.getAbsenceBeanListOrderByYearNomSalarie(entrepriseBean
								.getId());
			} else {
				absenceBeanList = absServ
						.getAbsenceBeanListOrderByYearNomEntrepriseNomSalarie(idGroupe);
			}
		} else {
			absenceBeanList = list;
		}

		Iterator<AbsenceBean> ite = absenceBeanList.iterator();

		boolean remove = false;

		while (ite.hasNext()) {

			remove = false;

			AbsenceBean absenceBean = ite.next();
			if (absenceBean.getDebutAbsence() != null
					&& absenceBean.getFinAbsence() != null) {
				String substringAbs = absenceBean.getNomTypeAbsence()
						.substring(0, 7);
				String substringConge = absenceBean.getNomTypeAbsence()
						.substring(0, 5);
				if (!substringAbs.equals("Absence")
						&& !substringConge.equals("Congé")) {
					ite.remove();
				} else {

					GregorianCalendar debD1 = new GregorianCalendar();
					debD1.setTime(absenceBean.getDebutAbsence());
					GregorianCalendar finD1 = new GregorianCalendar();
					finD1.setTime(absenceBean.getFinAbsence());

					if (!isInPeriode(debutExtraction, finExtraction, debD1,
							finD1)) {
						ite.remove();
						remove = true;
					}

					Boolean isPresent = salServ.isSalariePresent(absenceBean
							.getIdSalarie());
					if (!remove) {
						if (present == 1) {
							if (isPresent) {
								ite.remove();
							}
						}
						if (present == 2) {
							if (!isPresent) {
								ite.remove();
							}
						}
					}
				}
			}
		}

		for (AbsenceBean absenceBean : absenceBeanList) {
			if (absenceBean.getDebutAbsence() != null
					&& absenceBean.getFinAbsence() != null) {

				SalarieBeanExport salarie = salServ
						.getSalarieBeanExportById(absenceBean.getIdSalarie());

				if (list != null) {
					salarie.setService(serv.getServiceBeanById(
							salarie.getIdServiceSelected()).getNom());
				}

				nature = absenceBean.getNomTypeAbsence() + " ";
				debut = dateFormat.format(absenceBean.getDebutAbsence()) + " ";
				fin = dateFormat.format(absenceBean.getFinAbsence()) + " ";
				final long MILISECOND_PER_DAY = 24 * 60 * 60 * 1000;

				GregorianCalendar dateBegin = new GregorianCalendar();
				GregorianCalendar dateEnd = new GregorianCalendar();

				dateBegin.setTime(absenceBean.getDebutAbsence());
				dateEnd.setTime(absenceBean.getFinAbsence());

				duree = absenceBean.getNombreJourOuvre() + " ";
				if (salarie.getAbsenceBeanList().size() > 0) {

					int i = 0;

					// Année référence
					GregorianCalendar annee = new GregorianCalendar();
					annee.setTime(absenceBean.getDebutAbsence());
					label = new Label(i, row, annee.get(Calendar.YEAR) + "");
					label.setCellFormat(format);
					sheet.addCell(label);

					// Entreprise
					if (entrepriseBean.getId() == 0
							|| entrepriseBean.getId() == -1) {
						EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
						label = new Label(i + 1, row, entServ
								.getEntrepriseBeanById(
										salarie.getIdEntrepriseSelected())
								.getNom());
						label.setCellFormat(format2);
						sheet.addCell(label);
						i++;
					}

					// Nom
					label = new Label(i + 1, row, salarie.getNom());
					label.setCellFormat(format2);
					sheet.addCell(label);

					// Prénom
					label = new Label(i + 2, row, salarie.getPrenom());
					label.setCellFormat(format2);
					sheet.addCell(label);

					// Sexe
					String civilite = "";
					if (salarie.getCivilite().equals("Monsieur")) {
						civilite = "H";
					} else {
						civilite = "F";
					}
					label = new Label(i + 3, row, civilite);
					label.setCellFormat(format);
					sheet.addCell(label);

					// Service
					if (salarie.getService() != null) {
						label = new Label(i + 4, row, salarie.getService());
					} else {
						label = new Label(i + 4, row, "");
					}
					label.setCellFormat(format2);
					sheet.addCell(label);

					// Poste occupé
					label = new Label(i + 5, row, getNomMetier(
							salarie.getParcoursBeanList(),
							absenceBean.getDebutAbsence(),
							absenceBean.getFinAbsence()));
					label.setCellFormat(format2);
					sheet.addCell(label);

					// Nature de l'absence
					label = new Label(i + 6, row, nature);
					label.setCellFormat(format2);
					sheet.addCell(label);

					// Début de l'absence
					label = new Label(i + 7, row, debut);
					label.setCellFormat(format);
					sheet.addCell(label);

					// Fin de l'absence
					label = new Label(i + 8, row, fin);
					label.setCellFormat(format);
					sheet.addCell(label);

					// Durée
					label = new Label(i + 9, row, duree);
					label.setCellFormat(format2);
					sheet.addCell(label);

					row++;
					nature = "";
					debut = "";
					fin = "";
					duree = "";
				}
			}
		}

		int i = 0;
		sheet.setColumnView(i, 10);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.setColumnView(i + 1, 20);
			i++;
		}
		sheet.setColumnView(i + 1, 20);
		sheet.setColumnView(i + 2, 20);
		sheet.setColumnView(i + 3, 8);
		sheet.setColumnView(i + 4, 20);
		sheet.setColumnView(i + 5, 20);
		sheet.setColumnView(i + 6, 40);
		sheet.setColumnView(i + 7, 12);
		sheet.setColumnView(i + 8, 12);
		sheet.setColumnView(i + 9, 10);
		sheet.setRowView(0, 400);
		sheet.setColumnView(11, 25);

	}

	/**
	 * 
	 * @throws Exception
	 */
	private void exportAccidents(List<AccidentBean> list) throws Exception {
		sheet.setColumnView(0, 20);
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 9, WritableFont.NO_BOLD));
		format2.setAlignment(Alignment.LEFT);
		format2.setWrap(true);
		format2.setVerticalAlignment(VerticalAlignment.CENTRE);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(0, 0, 14, 0);
			sheet.mergeCells(0, 1, 14, 1);
			sheet.mergeCells(0, 2, 14, 2);
		} else {
			sheet.mergeCells(0, 0, 13, 0);
			sheet.mergeCells(0, 1, 13, 1);
			sheet.mergeCells(0, 2, 13, 2);
		}

		// Fusion cellule pour les sous-colonnes

		int j = 0;
		sheet.mergeCells(j, 4, 0, 5);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(j + 1, 4, 1, 5);
			j++;
		}
		sheet.mergeCells(j + 1, 4, j + 1, 5);
		sheet.mergeCells(j + 2, 4, j + 2, 5);
		sheet.mergeCells(j + 3, 4, j + 3, 5);
		sheet.mergeCells(j + 4, 4, j + 4, 5);
		sheet.mergeCells(j + 5, 4, j + 5, 5);
		sheet.mergeCells(j + 6, 4, j + 7, 4);
		sheet.mergeCells(j + 8, 4, j + 9, 4);
		sheet.mergeCells(j + 10, 4, j + 10, 5);
		sheet.mergeCells(j + 11, 4, j + 12, 4);
		sheet.mergeCells(j + 13, 4, j + 13, 5);

		j = 0;
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			j++;
		}
		Label souscolonne1 = new Label(j + 6, 5, "Initial");
		Label souscolonne2 = new Label(j + 7, 5, "Rechute");
		Label souscolonne3 = new Label(j + 8, 5, "Date de début");
		Label souscolonne4 = new Label(j + 9, 5, "Date de fin");
		Label souscolonne5 = new Label(j + 11, 5, "Initial");
		Label souscolonne6 = new Label(j + 12, 5, "Rechute");

		souscolonne1.setCellFormat(format);
		sheet.addCell(souscolonne1);
		souscolonne2.setCellFormat(format);
		sheet.addCell(souscolonne2);
		souscolonne3.setCellFormat(format);
		sheet.addCell(souscolonne3);
		souscolonne4.setCellFormat(format);
		sheet.addCell(souscolonne4);
		souscolonne5.setCellFormat(format);
		sheet.addCell(souscolonne5);
		souscolonne6.setCellFormat(format);
		sheet.addCell(souscolonne6);

		if (entrepriseBean.getNom() == null) {
			entrepriseBean.setNom("");
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean groupe = grServ.getGroupeBeanById(idGroupe);
			entrepriseBean.setNom(groupe.getNom());
		}
		Label label = new Label(
				0,
				0,
				"TABLEAU DE SUIVI DES ACCIDENTS DE TRAVAIL / ACCIDENTS DE TRAJET / MALADIES PROFESSIONNELLES\n"
						+ entrepriseBean.getNom()
						+ " ("
						+ dateFormat.format(new Date()) + ")");

		Label label2 = new Label(
				0,
				1,
				"Objectif : outil de réflexion sur les conditions de travail / outil d'appui à la démarche sécurité et à l'amélioration des conditions de travail");

		String filtre;
		switch (present) {
		case 0:
			filtre = "Tous les salariés";
			break;
		case 1:
			filtre = "Seuls les salariés sortis";
			break;
		case 2:
			filtre = "Seuls les salariés présents";
			break;
		default:
			filtre = "Aucun filtre disponible";
			break;
		}
		String debExt = (debutExtraction != null) ? dateFormat
				.format(debutExtraction.getTime()) : "";
		String finExt = (finExtraction != null) ? dateFormat
				.format(finExtraction.getTime()) : "";

		if (!debExt.equals("")) {
			filtre += " - Début : "
					+ dateFormat.format(debutExtraction.getTime());
		}
		if (!finExt.equals("")) {
			filtre += " - Fin : " + dateFormat.format(finExtraction.getTime());
		}
		Label label3 = new Label(0, 2, "Filtre : " + filtre);

		label.setCellFormat(format);
		sheet.addCell(label);
		label2.setCellFormat(format);
		sheet.addCell(label2);
		label3.setCellFormat(format);
		sheet.addCell(label3);
		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		int row = 6;
		String type = "";
		String dateInitiale = "";
		String dateRechute = "";
		String dateDebutAbsence = "";
		String dateFinAbsence = "";
		String nbJour = "";
		String siegeInitial = "";
		String siegeRechute = "";
		String cause = "";

		ServiceImpl serv = new ServiceImpl();
		SalarieServiceImpl salServ = new SalarieServiceImpl();
		AccidentServiceImpl accServ = new AccidentServiceImpl();
		List<AccidentBean> accidentBeanList;
		if (list == null) {
			if (entrepriseBean.getId() > 0) {
				accidentBeanList = accServ
						.getAccidentBeanListOrderByYearNomSalarie(entrepriseBean
								.getId());
			} else {
				accidentBeanList = accServ
						.getAccidentBeanListOrderByYearNomEntrepriseNomSalarie(idGroupe);
			}
		} else {
			accidentBeanList = list;
		}

		Iterator<AccidentBean> ite = accidentBeanList.iterator();

		boolean remove = false;

		while (ite.hasNext()) {

			remove = false;

			AccidentBean accidentBean = ite.next();
			GregorianCalendar debD1 = new GregorianCalendar();
			debD1.setTime(accidentBean.getDateAccident());
			GregorianCalendar finD1 = new GregorianCalendar();
			finD1.setTime(accidentBean.getDateAccident());

			if (!isInPeriode(debutExtraction, finExtraction, debD1, finD1)) {
				ite.remove();
				remove = true;
			}

			Boolean isPresent = salServ.isSalariePresent(accidentBean
					.getIdSalarie());
			if (!remove) {
				if (present == 1) {
					if (isPresent) {
						ite.remove();
					}
				}
				if (present == 2) {
					if (!isPresent) {
						ite.remove();
					}
				}
			}
		}

		for (AccidentBean accidentBean : accidentBeanList) {

			SalarieBeanExport salarie = salServ
					.getSalarieBeanExportById(accidentBean.getIdSalarie());

			type = accidentBean.getNomTypeAccident() + " ";
			dateInitiale = (accidentBean.getDateAccident() != null) ? dateFormat
					.format(accidentBean.getDateAccident()) + " "
					: "";
			dateRechute = (accidentBean.getDateRechute() != null) ? dateFormat
					.format(accidentBean.getDateRechute()) + " " : "";
			AbsenceServiceImpl absServ = new AbsenceServiceImpl();
			dateDebutAbsence = (accidentBean.getIdAbsence() != 0
					&& accidentBean.getIdAbsence() != -1 && absServ
					.getAbsenceBeanById(accidentBean.getIdAbsence())
					.getDebutAbsence() != null) ? dateFormat.format(absServ
					.getAbsenceBeanById(accidentBean.getIdAbsence())
					.getDebutAbsence())
					+ " " : "";
			dateFinAbsence = (accidentBean.getIdAbsence() != 0
					&& accidentBean.getIdAbsence() != -1 && absServ
					.getAbsenceBeanById(accidentBean.getIdAbsence())
					.getFinAbsence() != null) ? dateFormat.format(absServ
					.getAbsenceBeanById(accidentBean.getIdAbsence())
					.getFinAbsence())
					+ " " : "";
			if (accidentBean.isInitial()) {
				nbJour = accidentBean.getNombreJourArret() + " ";
			} else {
				nbJour = accidentBean.getNombreJourArretRechute() + "";
			}
			siegeInitial = (accidentBean.getNomTypeLesion() != null) ? accidentBean
					.getNomTypeLesion() + " "
					: "";
			siegeRechute = (accidentBean.getNomTypeLesionRechute() != null) ? accidentBean
					.getNomTypeLesionRechute() + " "
					: "";
			cause = accidentBean.getNomTypeCauseAccident() + " ";

			if (salarie.getAccidentBeanList().size() > 0) {

				int i = 0;

				// Année référence
				GregorianCalendar annee = new GregorianCalendar();
				annee.setTime(accidentBean.getDateAccident());
				label = new Label(i, row, annee.get(Calendar.YEAR) + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				// Entreprise
				if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
					EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
					label = new Label(i + 1, row, entServ
							.getEntrepriseBeanById(
									salarie.getIdEntrepriseSelected()).getNom());
					label.setCellFormat(format2);
					sheet.addCell(label);
					i++;
				}

				// Nom
				label = new Label(i + 1, row, salarie.getNom());
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Prénom
				label = new Label(i + 2, row, salarie.getPrenom());
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Service
				if (salarie.getService() != null) {
					label = new Label(i + 3, row, salarie.getService());
				} else {
					label = new Label(i + 3, row, "");
				}
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Métier
				if (accidentBean.isInitial()) {
					label = new Label(i + 4, row, getNomMetier(
							salarie.getParcoursBeanList(),
							accidentBean.getDateAccident()));
				} else {
					label = new Label(i + 4, row, getNomMetier(
							salarie.getParcoursBeanList(),
							accidentBean.getDateRechute()));
				}
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Type d'accident
				label = new Label(i + 5, row, type);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Date de l'accident inital
				label = new Label(i + 6, row, dateInitiale);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Date de l'accident rechute
				label = new Label(i + 7, row, dateRechute);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Date debut absence
				label = new Label(i + 8, row, dateDebutAbsence);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Date fin absence
				label = new Label(i + 9, row, dateFinAbsence);
				label.setCellFormat(format);
				sheet.addCell(label);

				// nb de jours d'arret
				label = new Label(i + 10, row, nbJour);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Siège de la lésion initale
				label = new Label(i + 11, row, siegeInitial);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Siège de la lésion rechute
				label = new Label(i + 12, row, siegeRechute);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Cause de l'accident
				label = new Label(i + 13, row, cause);
				label.setCellFormat(format2);
				sheet.addCell(label);

				row++;
				type = "";
				dateInitiale = "";
				dateRechute = "";
				dateDebutAbsence = "";
				dateFinAbsence = "";
				nbJour = "";
				siegeInitial = "";
				siegeRechute = "";
				cause = "";
			}

		}

		int i = 0;
		sheet.setColumnView(i, 12);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.setColumnView(i + 1, 20);
			i++;
		}
		sheet.setColumnView(i + 1, 20);
		sheet.setColumnView(i + 2, 20);
		sheet.setColumnView(i + 3, 20);
		sheet.setColumnView(i + 4, 20);
		sheet.setColumnView(i + 5, 30);
		sheet.setColumnView(i + 6, 12);
		sheet.setColumnView(i + 7, 12);
		sheet.setColumnView(i + 8, 12);
		sheet.setColumnView(i + 9, 12);
		sheet.setColumnView(i + 10, 20);
		sheet.setColumnView(i + 11, 30);
		sheet.setColumnView(i + 12, 30);
		sheet.setColumnView(i + 13, 40);

	}

	private void exportParcoursProfessionnels(List<ParcoursBean> list)
			throws Exception {
		sheet.setColumnView(0, 20);
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 9, WritableFont.NO_BOLD));
		format2.setAlignment(Alignment.LEFT);
		format2.setWrap(true);
		format2.setVerticalAlignment(VerticalAlignment.CENTRE);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(0, 0, 15, 0);
			sheet.mergeCells(0, 1, 15, 1);
		} else {
			sheet.mergeCells(0, 0, 14, 0);
			sheet.mergeCells(0, 1, 14, 1);
		}
		if (entrepriseBean.getNom() == null) {
			entrepriseBean.setNom("");
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean groupe = grServ.getGroupeBeanById(idGroupe);
			entrepriseBean.setNom(groupe.getNom());
		}
		Label label = new Label(0, 0, "SUIVI DES PARCOURS PROFESSIONNELS \n"
				+ entrepriseBean.getNom() + " ("
				+ dateFormat.format(new Date()) + ")");
		String filtre;
		switch (present) {
		case 0:
			filtre = "Tous les salariés";
			break;
		case 1:
			filtre = "Seuls les salariés sortis";
			break;
		case 2:
			filtre = "Seuls les salariés présents";
			break;
		default:
			filtre = "Aucun filtre disponible";
			break;
		}
		String debExt = (debutExtraction != null) ? dateFormat
				.format(debutExtraction.getTime()) : "";
		String finExt = (finExtraction != null) ? dateFormat
				.format(finExtraction.getTime()) : "";

		if (!debExt.equals("")) {
			filtre += " - Début : "
					+ dateFormat.format(debutExtraction.getTime());
		}
		if (!finExt.equals("")) {
			filtre += " - Fin : " + dateFormat.format(finExtraction.getTime());
		}
		Label label2 = new Label(0, 1, "Filtre : " + filtre);

		label.setCellFormat(format);
		sheet.addCell(label);
		label2.setCellFormat(format);
		sheet.addCell(label2);
		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		int row = 5;

		String debut = "";
		String fin = "";
		String anciennetePoste = "";
		String ancienneteEntreprise = "";
		String csp = "";
		String coef = "";
		String niveau = "";
		String echelon = "";
		String type = "";
		String service = "";
		String etp = "";

		ServiceImpl serv = new ServiceImpl();
		SalarieServiceImpl salServ = new SalarieServiceImpl();
		ParcoursServiceImpl ctServ = new ParcoursServiceImpl();
		List<ParcoursBean> parcoursBeanList;
		if (list == null) {
			if (entrepriseBean.getId() > 0) {
				parcoursBeanList = ctServ
						.getParcoursListOrderByNomSalarie(entrepriseBean
								.getId());
			} else {
				parcoursBeanList = ctServ
						.getParcoursListOrderByNomEntrepriseNomSalarie(idGroupe);
			}
		} else {
			parcoursBeanList = list;
		}

		// On applique les bornes voulues
		Iterator<ParcoursBean> ite = parcoursBeanList.iterator();

		boolean remove = false;

		while (ite.hasNext()) {

			remove = false;

			ParcoursBean parcoursBean = ite.next();
			GregorianCalendar debD1 = new GregorianCalendar();
			debD1.setTime(parcoursBean.getDebutFonction());
			GregorianCalendar finD1 = new GregorianCalendar();
			if (parcoursBean.getFinFonction() != null) {
				finD1.setTime(parcoursBean.getFinFonction());

				if (!isInPeriode(debutExtraction, finExtraction, debD1, finD1)) {
					ite.remove();
					remove = true;
				}
			} else {
				if (!isInPeriode(debutExtraction, finExtraction, debD1, debD1)) {
					ite.remove();
					remove = true;
				}
			}
			Boolean isPresent = salServ.isSalariePresent(parcoursBean
					.getIdSalarie());
			if (!remove) {
				if (present == 1) {
					if (isPresent) {
						ite.remove();
					}
				}
				if (present == 2) {
					if (!isPresent) {
						ite.remove();
					}
				}
			}
		}

		for (ParcoursBean parcoursBean : parcoursBeanList) {

			SalarieBeanExport salarie = salServ
					.getSalarieBeanExportById(parcoursBean.getIdSalarie());

			if (list != null) {
				salarie.setService(serv.getServiceBeanById(
						salarie.getIdServiceSelected()).getNom());
			}

			debut = dateFormat.format(parcoursBean.getDebutFonction());
			if (parcoursBean.getFinFonction() != null) {
				fin = dateFormat.format(parcoursBean.getFinFonction());
			}
			Calendar d1 = new GregorianCalendar();
			Calendar d2 = new GregorianCalendar();
			d1.setTime(parcoursBean.getDebutFonction());
			if (parcoursBean.getFinFonction() != null) {
				d2.setTime(parcoursBean.getFinFonction());
			} else {
				d2 = d2.getInstance();
			}
			int nbMois = 0;

			while (d1.before(d2)) {
				d1.add(GregorianCalendar.MONTH, 1);
				nbMois++;
			}
			int nbAnnees = nbMois / 12;
			nbMois = (nbMois % 12) - 1;
			if (nbAnnees > 0) {
				if (nbAnnees == 1) {
					if (nbMois > 0) {
						anciennetePoste = nbAnnees + " an - " + nbMois
								+ " mois";
					} else {
						anciennetePoste = nbAnnees + " an";
					}
				} else {
					if (nbMois > 0) {
						anciennetePoste = nbAnnees + " ans - " + nbMois
								+ " mois";
					} else {
						anciennetePoste = nbAnnees + " ans";
					}
				}
			} else {
				if (nbMois > 0) {
					anciennetePoste = nbMois + " mois";
				}
			}
			csp = parcoursBean.getNomTypeStatut();
			coef = parcoursBean.getCoefficient();
			niveau = parcoursBean.getNiveau();
			echelon = parcoursBean.getEchelon();
			type = parcoursBean.getNomTypeContrat();
			ServiceImpl serviceImpl = new ServiceImpl();
			service = serviceImpl.getServiceBeanById(
					salarie.getIdServiceSelected()).getNom();
			ancienneteEntreprise = getAncienneteEntrepriseParcours(parcoursBean);
			etp = (parcoursBean.getEquivalenceTempsPlein() != 0) ? parcoursBean
					.getEquivalenceTempsPlein() + "%" : "";

			// }
			if (salarie.getParcoursBeanList().size() > 0) {

				int i = 0;

				// Entreprise
				if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
					EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
					label = new Label(i, row, entServ.getEntrepriseBeanById(
							salarie.getIdEntrepriseSelected()).getNom());
					label.setCellFormat(format2);
					sheet.addCell(label);
					i++;
				}

				// Nom
				label = new Label(i, row, salarie.getNom());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizeNom < label.getContents().length()) {
					maxSizeNom = label.getContents().length();
				}

				// Prénom
				label = new Label(i + 1, row, salarie.getPrenom());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizePrenom < label.getContents().length()) {
					maxSizePrenom = label.getContents().length();
				}

				// Service
				label = new Label(i + 2, row, service);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Date entree Entreprise
				String dateEntree = getDateEntree(salarie);
				label = new Label(i + 3, row, dateEntree);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Date sortie Entreprise
				String dateSortie = getDateSortie(salarie);
				label = new Label(i + 4, row, dateSortie);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Anciennete entreprise
				label = new Label(i + 5, row, ancienneteEntreprise);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Poste occupé
				label = new Label(i + 6, row, parcoursBean.getNomMetier());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizeNomMetier < label.getContents().length()) {
					maxSizeNomMetier = label.getContents().length();
				}

				// Date Debut fonction
				label = new Label(i + 7, row, debut);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Date Fin fonction
				label = new Label(i + 8, row, fin);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Anciennete poste
				label = new Label(i + 9, row, anciennetePoste);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Niveau initial
				// label = new Label(i + 6, row, niveauI);
				// label.setCellFormat(format2);
				// sheet.addCell(label);

				// Niveau acquis
				// label = new Label(i + 7, row, niveauA);
				// label.setCellFormat(format2);
				// sheet.addCell(label);

				// type contrat
				label = new Label(i + 10, row, type);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// ETP
				label = new Label(i + 11, row, etp);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// CSP
				label = new Label(i + 12, row, csp);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Coef
				label = new Label(i + 13, row, coef);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Niveau
				label = new Label(i + 14, row, niveau);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Echelon
				label = new Label(i + 15, row, echelon);
				label.setCellFormat(format2);
				sheet.addCell(label);

				row++;
				debut = "";
				fin = "";
				anciennetePoste = "";
				csp = "";
				coef = "";
				niveau = "";
				echelon = "";
				type = "";
				service = "";
			}

		}

		int i = 0;

		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.setColumnView(i + 1, 22);
			i++;
		}
		sheet.setColumnView(i, 20);
		sheet.setColumnView(i + 1, 15);
		sheet.setColumnView(i + 2, 20);
		sheet.setColumnView(i + 3, 15);
		sheet.setColumnView(i + 4, 15);
		sheet.setColumnView(i + 5, 15);
		sheet.setColumnView(i + 6, 25);
		sheet.setColumnView(i + 7, 10);
		sheet.setColumnView(i + 8, 10);
		sheet.setColumnView(i + 9, 15);
		sheet.setColumnView(i + 10, 15);
		sheet.setColumnView(i + 11, 10);
		sheet.setColumnView(i + 12, 20);
		sheet.setColumnView(i + 13, 10);
		sheet.setColumnView(i + 14, 10);
		sheet.setColumnView(i + 15, 10);

	}

	private void exportContratTravail(List<ContratTravailBean> list)
			throws Exception {
		sheet.setColumnView(0, 20);
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 9, WritableFont.NO_BOLD));
		format2.setAlignment(Alignment.LEFT);
		format2.setWrap(true);
		format2.setVerticalAlignment(VerticalAlignment.CENTRE);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(0, 0, 12, 0);
			sheet.mergeCells(0, 1, 12, 1);
		} else {
			sheet.mergeCells(0, 0, 11, 0);
			sheet.mergeCells(0, 1, 11, 1);
		}
		if (entrepriseBean.getNom() == null) {
			entrepriseBean.setNom("");
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean groupe = grServ.getGroupeBeanById(idGroupe);
			entrepriseBean.setNom(groupe.getNom());
		}
		Label label = new Label(0, 0, "SUIVI DES CONTRATS DE TRAVAIL \n"
				+ entrepriseBean.getNom() + " ("
				+ dateFormat.format(new Date()) + ")");
		String filtre;
		switch (present) {
		case 0:
			filtre = "Tous les salariés";
			break;
		case 1:
			filtre = "Seuls les salariés sortis";
			break;
		case 2:
			filtre = "Seuls les salariés présents";
			break;
		default:
			filtre = "Aucun filtre disponible";
			break;
		}
		String debExt = (debutExtraction != null) ? dateFormat
				.format(debutExtraction.getTime()) : "";
		String finExt = (finExtraction != null) ? dateFormat
				.format(finExtraction.getTime()) : "";

		if (!debExt.equals("")) {
			filtre += " - Début : "
					+ dateFormat.format(debutExtraction.getTime());
		}
		if (!finExt.equals("")) {
			filtre += " - Fin : " + dateFormat.format(finExtraction.getTime());
		}
		Label label2 = new Label(0, 1, "Filtre : " + filtre);

		label.setCellFormat(format);
		sheet.addCell(label);
		label2.setCellFormat(format);
		sheet.addCell(label2);
		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		int row = 5;

		String service = "";
		String debut = "";
		String fin = "";
		String type = "";
		String ancienneteEntreprise = "";
		String motifRupture = "";

		SalarieServiceImpl salServ = new SalarieServiceImpl();
		ContratTravailServiceImpl ctServ = new ContratTravailServiceImpl();
		List<ContratTravailBean> contratTravailBeanList;
		if (list == null) {
			if (entrepriseBean.getId() > 0) {
				contratTravailBeanList = ctServ
						.getContratTravailsListOrderNomSalarie(entrepriseBean
								.getId());
			} else {
				contratTravailBeanList = ctServ
						.getContratTravailsListOrderByNomEntrepriseNomSalarie(idGroupe);
			}
		} else {
			contratTravailBeanList = list;
		}

		// On applique les bornes voulues
		Iterator<ContratTravailBean> ite = contratTravailBeanList.iterator();

		boolean remove = false;

		while (ite.hasNext()) {

			remove = false;

			ContratTravailBean contratTravailBean = ite.next();
			GregorianCalendar debD1 = new GregorianCalendar();
			debD1.setTime(contratTravailBean.getDebutContrat());
			GregorianCalendar finD1 = new GregorianCalendar();
			if (contratTravailBean.getFinContrat() != null) {
				finD1.setTime(contratTravailBean.getFinContrat());

				if (!isInPeriode(debutExtraction, finExtraction, debD1, finD1)) {
					ite.remove();
					remove = true;
				}
			} else {
				if (!isInPeriode(debutExtraction, finExtraction, debD1, debD1)) {
					ite.remove();
					remove = true;
				}
			}
			Boolean isPresent = salServ.isSalariePresent(contratTravailBean
					.getIdSalarie());
			if (!remove) {
				if (present == 1) {
					if (isPresent) {
						ite.remove();
					}
				}
				if (present == 2) {
					if (!isPresent) {
						ite.remove();
					}
				}
			}
		}

		for (ContratTravailBean contratTravailBean : contratTravailBeanList) {

			SalarieBeanExport salarie = salServ
					.getSalarieBeanExportById(contratTravailBean.getIdSalarie());

			debut = dateFormat.format(contratTravailBean.getDebutContrat());
			if (contratTravailBean.getFinContrat() != null) {
				fin = dateFormat.format(contratTravailBean.getFinContrat());
			}

			motifRupture = contratTravailBean.getNomMotifRuptureContrat();
			type = contratTravailBean.getNomTypeContrat();
			ServiceImpl serviceImpl = new ServiceImpl();
			service = serviceImpl.getServiceBeanById(
					salarie.getIdServiceSelected()).getNom();
			ancienneteEntreprise = getAncienneteEntrepriseContrat(contratTravailBean);

			if (ctServ.getContratTravailBeanListByIdSalarie(salarie.getId())
					.size() > 0) {

				int i = 0;

				// Entreprise
				if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
					EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
					label = new Label(i, row, entServ.getEntrepriseBeanById(
							salarie.getIdEntrepriseSelected()).getNom());
					label.setCellFormat(format2);
					sheet.addCell(label);
					i++;
				}

				// Nom
				label = new Label(i, row, salarie.getNom());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizeNom < label.getContents().length()) {
					maxSizeNom = label.getContents().length();
				}

				// Prénom
				label = new Label(i + 1, row, salarie.getPrenom());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizePrenom < label.getContents().length()) {
					maxSizePrenom = label.getContents().length();
				}

				// Service
				label = new Label(i + 2, row, service);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Date entree Entreprise
				String dateEntree = getDateEntree(salarie);
				label = new Label(i + 3, row, dateEntree);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Date sortie Entreprise
				String dateSortie = getDateSortie(salarie);
				label = new Label(i + 4, row, dateSortie);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Date Debut fonction
				label = new Label(i + 5, row, debut);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Date Fin fonction
				label = new Label(i + 6, row, fin);
				label.setCellFormat(format);
				sheet.addCell(label);

				// type contrat
				label = new Label(i + 7, row, type);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Motif Rupture
				label = new Label(i + 8, row, motifRupture);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Anciennete entreprise
				label = new Label(i + 9, row, ancienneteEntreprise);
				label.setCellFormat(format2);
				sheet.addCell(label);

				row++;
				service = "";
				debut = "";
				fin = "";
				type = "";
				ancienneteEntreprise = "";
				motifRupture = "";

			}

		}

		int i = 0;

		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.setColumnView(i + 1, 22);
			i++;
		}
		sheet.setColumnView(i, 20);
		sheet.setColumnView(i + 1, 20);
		sheet.setColumnView(i + 2, 20);
		sheet.setColumnView(i + 3, 15);
		sheet.setColumnView(i + 4, 15);
		sheet.setColumnView(i + 5, 15);
		sheet.setColumnView(i + 6, 15);
		sheet.setColumnView(i + 7, 20);
		sheet.setColumnView(i + 8, 25);
		sheet.setColumnView(i + 9, 15);

	}

	/**
	 * 
	 * @throws WriteException
	 */
	private void exportCompetences() throws Exception {
		sheet.setColumnView(0, 20);
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		WritableCellFormat format2 = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		WritableCellFormat format3 = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 9, WritableFont.NO_BOLD));
		format3.setAlignment(Alignment.LEFT);
		format3.setWrap(true);
		format3.setVerticalAlignment(VerticalAlignment.CENTRE);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(0, 0, 14, 0);
			sheet.mergeCells(0, 1, 14, 1);
			sheet.mergeCells(0, 2, 14, 2);
		} else {
			sheet.mergeCells(0, 0, 13, 0);
			sheet.mergeCells(0, 1, 13, 1);
			sheet.mergeCells(0, 2, 13, 2);
		}

		int j = 0;
		sheet.mergeCells(j, 4, 0, 5);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(j + 1, 4, j + 1, 5);
			j++;
		}
		sheet.mergeCells(j + 1, 4, j + 1, 5);
		sheet.mergeCells(j + 2, 4, j + 2, 5);
		sheet.mergeCells(j + 3, 4, j + 3, 5);
		sheet.mergeCells(j + 4, 4, j + 4, 5);
		sheet.mergeCells(j + 5, 4, j + 5, 5);
		sheet.mergeCells(j + 6, 4, j + 6, 5);
		sheet.mergeCells(j + 10, 4, j + 10, 5);
		sheet.mergeCells(j + 11, 4, j + 11, 5);
		sheet.mergeCells(j + 12, 4, j + 12, 5);
		sheet.mergeCells(j + 13, 4, j + 13, 5);

		sheet.mergeCells(j + 7, 4, j + 9, 4);

		if (entrepriseBean.getNom() == null) {
			entrepriseBean.setNom("");
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean groupe = grServ.getGroupeBeanById(idGroupe);
			entrepriseBean.setNom(groupe.getNom());
		}
		Label label = new Label(0, 0, "TABLEAU DE SUIVI DES COMPETENCES \n"
				+ entrepriseBean.getNom() + " ("
				+ dateFormat.format(new Date()) + ")");
		label.setCellFormat(format);
		sheet.addCell(label);

		Label label3 = new Label(
				0,
				1,
				"Objectif : vision globale du niveau des compétences de l'entreprise / identifier les carences en compétences  -  la polycompétence - les compétences clés - le potentiel d'évolution");
		label3.setCellFormat(format);
		sheet.addCell(label3);

		String filtre;
		switch (present) {
		case 0:
			filtre = "Tous les salariés";
			break;
		case 1:
			filtre = "Seuls les salariés sortis";
			break;
		case 2:
			filtre = "Seuls les salariés présents";
			break;
		default:
			filtre = "Aucun filtre disponible";
			break;
		}
		Label label2 = new Label(0, 2, "Filtre : " + filtre);

		label2.setCellFormat(format);
		sheet.addCell(label2);

		font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(jxl.format.Alignment.CENTRE);
		format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		format.setWrap(true);

		String[] entete = new String[5];
		entete[0] = "Evaluation du niveau des compétences métier";
		entete[1] = "Evaluation globale au poste de travail";
		entete[2] = "Compétences spécifiques";
		entete[3] = "Activités spécifiques";
		entete[4] = "Décision d'évolution";

		j = 0;
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			j++;
		}
		label = new Label(j + 7, 4, entete[0]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(j + 10, 4, entete[1]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(j + 11, 4, entete[2]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(j + 12, 4, entete[3]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(j + 13, 4, entete[4]);
		label.setCellFormat(format);
		sheet.addCell(label);

		String[] entete2 = new String[11];
		entete2[0] = "Année de référence";
		entete2[1] = "Entreprise";
		entete2[2] = "Nom";
		entete2[3] = "Prénom";
		entete2[4] = "Ancienneté dans l'entreprise";
		entete2[5] = "Service";
		entete2[6] = "Poste occupé";
		entete2[7] = "Ancienneté dans le poste";
		entete2[8] = "Savoirs";
		entete2[9] = "Savoir-faire";
		entete2[10] = "Savoir-être";

		int k = 0;
		int length = entete2.length - 3;
		for (int i = 0; i < length; i++) {
			if (entete2[k].equals("Entreprise")) {
				if (entrepriseBean.getId() == -1 || entrepriseBean.getId() == 0) {
					label = new Label(i, 4, entete2[k]);
					label.setCellFormat(format);
					sheet.addCell(label);
				} else {
					k++;
					length--;
					label = new Label(i, 4, entete2[k]);
					label.setCellFormat(format);
					sheet.addCell(label);
				}
			} else {
				label = new Label(i, 4, entete2[k]);
				label.setCellFormat(format);
				sheet.addCell(label);
			}
			k++;
		}
		j = 0;
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			j++;
		}
		label = new Label(j + 7, 5, entete2[8]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(j + 8, 5, entete2[9]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(j + 9, 5, entete2[10]);
		label.setCellFormat(format);
		sheet.addCell(label);

		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		format2 = new WritableCellFormat(font);
		format2.setAlignment(Alignment.LEFT);
		format2.setWrap(true);
		format2.setVerticalAlignment(VerticalAlignment.TOP);

		int row = 6;
		String anciennete = "";
		String service = "";
		String metierType = "";
		String evaluationGlobale = "";
		String activiteSpecifiques = "";
		String competenceSavoir = "";
		String competenceSavoirFaire = "";
		String competenceSavoirEtre = "";
		String competenceSpecifique = "";
		String competenceAAcquerir = "";
		String competenceAAmeliorer = "";
		String evolutionProfessionnelle = "";

		List<CompetencesExportBean> compList = new ArrayList<CompetencesExportBean>();

		for (SalarieBeanExport salarie : salarieListBean) {

			String ancienneteEntreprise = "";
			String anciennetePoste = "";
			if (salarie.getParcoursBeanList() != null
					&& salarie.getParcoursBeanList().size() > 0) {
				ParcoursBean parcoursBean = new ParcoursBean();
				parcoursBean = getLastParcours(salarie);

				Calendar d1 = new GregorianCalendar();
				Calendar d2 = new GregorianCalendar();
				d1.setTime(parcoursBean.getDebutFonction());
				if (parcoursBean.getFinFonction() != null
						&& parcoursBean.getFinFonction().before(
								d2.getInstance().getTime())) {
					d2.setTime(parcoursBean.getFinFonction());
				} else {
					d2 = d2.getInstance();
				}
				int nbMois = 0;

				while (d1.before(d2)) {
					d1.add(GregorianCalendar.MONTH, 1);
					nbMois++;
				}
				int nbAnnees = nbMois / 12;
				if (nbAnnees > 0) {
					nbMois = (nbMois % 12) - 1;
					if (nbAnnees == 1) {
						if (nbMois > 0) {
							anciennetePoste = nbAnnees + " an - " + nbMois
									+ " mois";
						} else {
							anciennetePoste = nbAnnees + " an";
						}
					} else {
						if (nbMois > 0) {
							anciennetePoste = nbAnnees + " ans - " + nbMois
									+ " mois";
						} else {
							anciennetePoste = nbAnnees + " ans";
						}
					}
				} else {
					if (nbMois > 0) {
						anciennetePoste = nbMois + " mois";
					}
				}

				Calendar d1e = new GregorianCalendar();
				Calendar d2e = new GregorianCalendar();
				d1e.setTime(getFirstParcours(salarie).getDebutFonction());
				if (getLastParcours(salarie).getFinFonction() != null
						&& getLastParcours(salarie).getFinFonction().before(
								d2e.getInstance().getTime())) {
					d2e.setTime(getLastParcours(salarie).getFinFonction());
				} else {
					d2e = d2e.getInstance();
				}
				int nbMoise = 0;

				while (d1e.before(d2e)) {
					d1e.add(GregorianCalendar.MONTH, 1);
					nbMoise++;
				}
				int nbAnneese = nbMoise / 12;
				nbMoise = (nbMoise % 12) - 1;
				if (nbAnneese > 0) {
					if (nbAnneese == 1) {
						if (nbMoise > 0) {
							ancienneteEntreprise = nbAnneese + " an - "
									+ nbMoise + " mois";
						} else {
							ancienneteEntreprise = nbAnneese + " an";
						}
					} else {
						if (nbMoise > 0) {
							ancienneteEntreprise = nbAnneese + " ans - "
									+ nbMoise + " mois";
						} else {
							ancienneteEntreprise = nbAnneese + " ans";
						}
					}
				} else {
					if (nbMoise > 0) {
						ancienneteEntreprise = nbMoise + " mois";
					}
				}

			}

			Collections.sort(salarie.getEntretienBeanList());
			Collections.reverse(salarie.getEntretienBeanList());
			if (salarie.getEntretienBeanList().size() > 0) {
				evolutionProfessionnelle = salarie.getEntretienBeanList()
						.get(0).getEvolutionPerso();
			} else {
				evolutionProfessionnelle = "Non connue";
			}

			FicheDePosteServiceImpl ficheDePosteService = new FicheDePosteServiceImpl();
			FicheDePosteBean ficheDePosteBean = ficheDePosteService
					.getFicheDePosteBeanByIdSalarie(salarie.getId());

			if (salarie.getParcoursBeanList().size() > 0) {
				ServiceImpl serviceImpl = new ServiceImpl();
				service = serviceImpl.getServiceBeanById(
						salarie.getIdServiceSelected()).getNom();
			}

			if (ficheDePosteBean != null) {

				CompetencesExportBean comp = new CompetencesExportBean();

				FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
				FicheMetierBean ficheMetierBean = ficheMetierService
						.getFicheMetierBeanById(ficheDePosteBean
								.getIdFicheMetierType());

				metierType = ficheMetierBean.getNom();
				anciennete = getAnciennetePoste(salarie);

				switch (Integer.parseInt(ficheDePosteBean
						.getEvaluationGlobale())) {
				case -1:
					evaluationGlobale = "-";
					break;
				case 0:
					evaluationGlobale = "=";
					break;
				case 1:
					evaluationGlobale = "+";
					break;
				default:
					evaluationGlobale = "Non évalué";
					break;
				}
				activiteSpecifiques = ficheDePosteBean
						.getActivitesSpecifiques();

				switch (Integer.parseInt(ficheDePosteBean.getSavoir())) {
				case -1:
					competenceSavoir = "-";
					break;
				case 0:
					competenceSavoir = "=";
					break;
				case 1:
					competenceSavoir = "+";
					break;
				default:
					competenceSavoir = "Non évalué";
					break;
				}

				switch (Integer.parseInt(ficheDePosteBean.getSavoirFaire())) {
				case -1:
					competenceSavoirFaire = "-";
					break;
				case 0:
					competenceSavoirFaire = "=";
					break;
				case 1:
					competenceSavoirFaire = "+";
					break;
				default:
					competenceSavoirFaire = "Non évalué";
					break;
				}

				switch (Integer.parseInt(ficheDePosteBean.getSavoirEtre())) {
				case -1:
					competenceSavoirEtre = "-";
					break;
				case 0:
					competenceSavoirEtre = "=";
					break;
				case 1:
					competenceSavoirEtre = "+";
					break;
				default:
					competenceSavoirEtre = "Non évalué";
					break;
				}
				competenceSpecifique = ficheDePosteBean
						.getCompetencesSpecifiquesTexte();
				competenceAAcquerir = ficheDePosteBean
						.getCompetencesNouvelles()
						+ " \n"
						+ ficheDePosteBean.getCompetencesNouvelles2()
						+ " \n"
						+ ficheDePosteBean.getCompetencesNouvelles3()
						+ " \n"
						+ ficheDePosteBean.getCompetencesNouvelles4()
						+ " \n"
						+ ficheDePosteBean.getCompetencesNouvelles5();
				competenceAAmeliorer = ficheDePosteBean.getCommentaire();

				// Année référence
				GregorianCalendar annee = new GregorianCalendar();
				annee.setTime(ficheDePosteBean.getDateCreation());
				ParcoursBean p = new ParcoursBean();
				p = getLastParcours(salarie);
				if (p.getFinFonction() != null) {
					if (p.getFinFonction().before(
							ficheDePosteBean.getDateCreation())) {
						annee.setTime(p.getFinFonction());
					}
				}
				comp.setAnnee(annee.get(Calendar.YEAR));

				// Entreprise
				if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
					EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
					comp.setEntreprise(serv.getEntrepriseBeanById(
							salarie.getIdEntrepriseSelected()).getNom());
				}

				// Nom
				comp.setNom(salarie.getNom());

				// Prénom
				comp.setPrenom(salarie.getPrenom());

				// Ancienneté entreprise
				comp.setAncienneteEntreprise(ancienneteEntreprise);

				// Service
				comp.setService(service);

				// Poste occupé
				Date dateRecherche = ficheDePosteBean.getDateCreation();
				if (p.getFinFonction() != null) {
					if (p.getFinFonction().before(
							ficheDePosteBean.getDateCreation())) {
						dateRecherche = p.getFinFonction();
					}
				}
				comp.setPosteOccupe(getNomMetier(salarie.getParcoursBeanList(),
						dateRecherche));

				// Ancienneté poste
				comp.setAnciennetePoste(anciennetePoste);

				// Compétences savoir
				comp.setSavoir(competenceSavoir);

				// Compétences savoir Faire
				comp.setSavoirFaire(competenceSavoirFaire);

				// Compétences savoir Etre
				comp.setSavoirEtre(competenceSavoirEtre);

				// Evaluation au poste de travail
				comp.setEvaluationGlobale(evaluationGlobale);

				// Compétences spécifiques
				comp.setCompetencesSpecifiques(competenceSpecifique);

				// Activité Spécifiques
				comp.setActivitesSpecifiques(activiteSpecifiques);

				// Compétences à améliorer
				/*
				 * label = new Label(11, row, competenceAAmeliorer);
				 * label.setCellFormat(format2); sheet.addCell(label);
				 */

				// Compétences à acquérir
				/*
				 * label = new Label(12, row, competenceAAcquerir);
				 * label.setCellFormat(format2); sheet.addCell(label);
				 */

				// Evolution
				comp.setDecisionEvolution(evolutionProfessionnelle);

				compList.add(comp);

			}
		}

		Collections.sort(compList);

		for (CompetencesExportBean comp : compList) {
			int i = 0;

			// Année référence
			label = new Label(i, row, comp.getAnnee() + "");
			label.setCellFormat(format);
			sheet.addCell(label);

			// Entreprise
			if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
				label = new Label(i + 1, row, comp.getEntreprise());
				label.setCellFormat(format3);
				sheet.addCell(label);
				i++;
			}

			// Nom
			label = new Label(i + 1, row, comp.getNom());
			label.setCellFormat(format3);
			sheet.addCell(label);
			if (maxSizeNom < label.getContents().length()) {
				maxSizeNom = label.getContents().length();
			}

			// Prénom
			label = new Label(i + 2, row, comp.getPrenom());
			label.setCellFormat(format3);
			sheet.addCell(label);
			if (maxSizePrenom < label.getContents().length()) {
				maxSizePrenom = label.getContents().length();
			}

			// Ancienneté entreprise
			label = new Label(i + 3, row, comp.getAncienneteEntreprise());
			label.setCellFormat(format3);
			sheet.addCell(label);

			// Service
			label = new Label(i + 4, row, comp.getService());
			label.setCellFormat(format3);
			sheet.addCell(label);
			if (maxSizePrenom < label.getContents().length()) {
				maxSizePrenom = label.getContents().length();
			}

			// Poste occupé
			label = new Label(i + 5, row, comp.getPosteOccupe());
			label.setCellFormat(format3);
			sheet.addCell(label);
			if (maxSizeNomMetier < label.getContents().length()) {
				maxSizeNomMetier = label.getContents().length();
			}

			// Ancienneté poste
			label = new Label(i + 6, row, comp.getAnciennetePoste());
			label.setCellFormat(format3);
			sheet.addCell(label);

			// Compétences savoir
			label = new Label(i + 7, row, comp.getSavoir());
			label.setCellFormat(format3);
			sheet.addCell(label);

			// Compétences savoir Faire
			label = new Label(i + 8, row, comp.getSavoirFaire());
			label.setCellFormat(format3);
			sheet.addCell(label);

			// Compétences savoir Etre
			label = new Label(i + 9, row, comp.getSavoirEtre());
			label.setCellFormat(format3);
			sheet.addCell(label);

			// Evaluation au poste de travail
			label = new Label(i + 10, row, comp.getEvaluationGlobale());
			label.setCellFormat(format3);
			sheet.addCell(label);

			// Compétences spécifiques
			label = new Label(i + 11, row, comp.getCompetencesSpecifiques());
			label.setCellFormat(format2);
			sheet.addCell(label);

			// Activité Spécifiques
			label = new Label(i + 12, row, comp.getActivitesSpecifiques());
			label.setCellFormat(format2);
			sheet.addCell(label);

			// Evolution
			label = new Label(i + 13, row, comp.getDecisionEvolution());
			label.setCellFormat(format2);
			sheet.addCell(label);

			row++;
		}

		int i = 0;
		sheet.setColumnView(i, 12);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.setColumnView(i + 1, 20);
			i++;
		}
		sheet.setColumnView(i + 1, 20);
		sheet.setColumnView(i + 2, 20);
		sheet.setColumnView(i + 3, 20);
		sheet.setColumnView(i + 4, 20);
		sheet.setColumnView(i + 5, 20);
		sheet.setColumnView(i + 6, 20);
		sheet.setColumnView(i + 7, 18);
		sheet.setColumnView(i + 8, 18);
		sheet.setColumnView(i + 9, 18);
		sheet.setColumnView(i + 10, 18);
		sheet.setColumnView(i + 11, 40);
		sheet.setColumnView(i + 12, 40);
		sheet.setColumnView(i + 13, 40);

	}

	private void exportEntretiens(List<EntretienBean> list) throws Exception {
		sheet.setColumnView(0, 20);
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 9, WritableFont.NO_BOLD));
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		WritableCellFormat format3 = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 12, WritableFont.BOLD));
		format3.setAlignment(Alignment.CENTRE);
		format3.setWrap(true);
		format3.setVerticalAlignment(VerticalAlignment.CENTRE);

		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(0, 0, 13, 0);
			sheet.mergeCells(0, 1, 13, 1);
			sheet.mergeCells(0, 2, 13, 2);
		} else {
			sheet.mergeCells(0, 0, 12, 0);
			sheet.mergeCells(0, 1, 12, 1);
			sheet.mergeCells(0, 2, 12, 2);
		}
		// titre du tableau
		if (entrepriseBean.getNom() == null) {
			entrepriseBean.setNom("");
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean groupe = grServ.getGroupeBeanById(idGroupe);
			entrepriseBean.setNom(groupe.getNom());
		}
		Label label = new Label(0, 0,
				"SUIVI DES ENTRETIENS ANNUELS / PROFESSIONNELS \n"
						+ entrepriseBean.getNom() + " ("
						+ dateFormat.format(new Date()) + ")");

		Label label2 = new Label(0, 1,
				"Objectif : outil d'aide à la décision du plan de formation");

		String filtre;
		switch (present) {
		case 0:
			filtre = "Tous les salariés";
			break;
		case 1:
			filtre = "Seuls les salariés sortis";
			break;
		case 2:
			filtre = "Seuls les salariés présents";
			break;
		default:
			filtre = "Aucun filtre disponible";
			break;
		}
		String debExt = (debutExtraction != null) ? dateFormat
				.format(debutExtraction.getTime()) : "";
		String finExt = (finExtraction != null) ? dateFormat
				.format(finExtraction.getTime()) : "";

		if (!debExt.equals("")) {
			filtre += " - Début : "
					+ dateFormat.format(debutExtraction.getTime());
		}
		if (!finExt.equals("")) {
			filtre += " - Fin : " + dateFormat.format(finExtraction.getTime());
		}
		Label label3 = new Label(0, 2, "Filtre : " + filtre);

		label.setCellFormat(format);
		sheet.addCell(label);
		label2.setCellFormat(format);
		sheet.addCell(label2);
		label3.setCellFormat(format);
		sheet.addCell(label3);
		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);

		int row = 5;
		String service = "";
		String evaluationCompetence = "";
		String competenceAAmeliorer = "";
		String competenceAAcquerir = "";
		String decisionEvolution = "";
		String besoinFormation = "";
		String objectif = "";
		// String souhaits = "";
		// String formationProgrammee = "";
		// String evolutionPerso = "";
		String crSigne = "";
		String modifPoste = "";

		ServiceImpl serv = new ServiceImpl();
		SalarieServiceImpl salServ = new SalarieServiceImpl();
		EntretienServiceImpl ctServ = new EntretienServiceImpl();
		List<EntretienBean> entretienBeanList;
		if (list == null) {
			if (entrepriseBean.getId() > 0) {
				entretienBeanList = ctServ
						.getEntretiensListOrderByAnneeNomSalarie(entrepriseBean
								.getId());
			} else {
				entretienBeanList = ctServ
						.getEntretiensListOrderByAnneeNomEntrepriseNomSalarie(idGroupe);
			}
		} else {
			entretienBeanList = list;
		}

		Iterator<EntretienBean> ite = entretienBeanList.iterator();

		boolean remove = false;

		while (ite.hasNext()) {

			remove = false;

			EntretienBean entretienBean = ite.next();

			if (debutExtraction != null) {
				if (finExtraction != null) {
					if (entretienBean.getAnneeReference() > finExtraction
							.get(Calendar.YEAR)
							|| entretienBean.getAnneeReference() < debutExtraction
									.get(Calendar.YEAR)) {
						ite.remove();
						remove = true;
					}
				} else {
					if (entretienBean.getAnneeReference() < debutExtraction
							.get(Calendar.YEAR)) {
						ite.remove();
						remove = true;
					}
				}
			} else {
				if (finExtraction != null) {
					if (entretienBean.getAnneeReference() > finExtraction
							.get(Calendar.YEAR)) {
						ite.remove();
						remove = true;
					}
				}
			}
			Boolean isPresent = salServ.isSalariePresent(entretienBean
					.getIdSalarie());
			if (!remove) {
				if (present == 1) {
					if (isPresent) {
						ite.remove();
					}
				}
				if (present == 2) {
					if (!isPresent) {
						ite.remove();
					}
				}
			}
		}

		for (EntretienBean entretienBean : entretienBeanList) {

			SalarieBeanExport salarie = salServ
					.getSalarieBeanExportById(entretienBean.getIdSalarie());

			if (list != null) {
				salarie.setService(serv.getServiceBeanById(
						salarie.getIdServiceSelected()).getNom());
			}

			int i = 0;

			// Lorsque eval des compétences sera défini =>
			competenceAAmeliorer = entretienBean.getCompetence() + " ";
			decisionEvolution = entretienBean.getEvolutionPerso();

			FicheDePosteServiceImpl ficheDePosteService = new FicheDePosteServiceImpl();
			FicheDePosteBean ficheDePosteBean = ficheDePosteService
					.getFicheDePosteBeanByIdSalarie(salarie.getId());
			if (ficheDePosteBean != null) {
				GregorianCalendar dateFDP = new GregorianCalendar();
				dateFDP.setTime(ficheDePosteBean.getDateCreation());
				if (dateFDP.get(Calendar.YEAR) == entretienBean
						.getAnneeReference()) {
					competenceAAcquerir = ficheDePosteBean
							.getCompetencesNouvelles()
							+ "\n"
							+ ficheDePosteBean.getCompetencesNouvelles2()
							+ "\n"
							+ ficheDePosteBean.getCompetencesNouvelles3()
							+ "\n"
							+ ficheDePosteBean.getCompetencesNouvelles4()
							+ "\n"
							+ ficheDePosteBean.getCompetencesNouvelles5()
							+ "\n";
					// evaluationCompetence = ficheDePosteBean
					// .getEvaluationGlobale();
					// switch (Integer.parseInt(ficheDePosteBean
					// .getEvaluationGlobale())) {
					// case -1:
					// evaluationCompetence = "-";
					// break;
					// case 0:
					// evaluationCompetence = "=";
					// break;
					// case 1:
					// evaluationCompetence = "+";
					// break;
					// default:
					// evaluationCompetence = "Non évalué";
					// break;
					// }
				} else {
					competenceAAcquerir = "";
				}
			} else {
				competenceAAcquerir = "Fiche de poste non renseignée. ";
				// evaluationCompetence = "Non évalué";
			}

			if (!entretienBean.getEvaluationGlobale().equals("")) {
				evaluationCompetence = entretienBean.getEvaluationGlobale();
			} else {
				evaluationCompetence = "Non évalué";
			}

			besoinFormation = entretienBean.getFormations() + " \n"
					+ entretienBean.getFormations2() + " \n"
					+ entretienBean.getFormations3() + " \n"
					+ entretienBean.getFormations4() + " \n"
					+ entretienBean.getFormations5() + " \n";

			ObjectifsEntretienServiceImpl objServ = new ObjectifsEntretienServiceImpl();
			List<ObjectifsEntretienBean> listo = new ArrayList<ObjectifsEntretienBean>();
			listo = objServ
					.getObjectifsEntretienListByIdEntretien(entretienBean
							.getId());

			objectif = "";

			for (ObjectifsEntretienBean o : listo) {
				objectif += (o.getIntitule() != null) ? o.getIntitule() + "\n"
						: "\n";
			}

			// evolutionPerso = entretienBean.getEvolutionPerso() + " ";

			crSigne = entretienBean.getCrSigne() + " ";

			modifPoste = (entretienBean.getModifProfil() != null && !entretienBean
					.getModifProfil().equals("null")) ? entretienBean
					.getModifProfil() + " " : " ";

			// }
			if (salarie.getEntretienBeanList().size() > 0) {
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setWrap(true);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);

				format2 = new WritableCellFormat(font);
				format2.setAlignment(Alignment.LEFT);
				format2.setWrap(true);
				format2.setVerticalAlignment(VerticalAlignment.CENTRE);

				// Année référence
				label = new Label(i, row, entretienBean.getAnneeReference()
						+ "");
				label.setCellFormat(format);
				sheet.addCell(label);

				// Entreprise
				if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
					EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
					label = new Label(i + 1, row, entServ
							.getEntrepriseBeanById(
									salarie.getIdEntrepriseSelected()).getNom());
					label.setCellFormat(format2);
					sheet.addCell(label);
					i++;
				}

				// Nom
				label = new Label(i + 1, row, salarie.getNom());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizeNom < label.getContents().length()) {
					maxSizeNom = label.getContents().length();
				}

				// Prénom
				label = new Label(i + 2, row, salarie.getPrenom());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizePrenom < label.getContents().length()) {
					maxSizePrenom = label.getContents().length();
				}

				// Service
				label = new Label(i + 3, row, salarie.getService());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizePrenom < label.getContents().length()) {
					maxSizePrenom = label.getContents().length();
				}

				// Métier
				label = new Label(i + 4, row, getNomMetier(
						salarie.getParcoursBeanList(),
						entretienBean.getAnneeReference()));
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizeNomMetier < label.getContents().length()) {
					maxSizeNomMetier = label.getContents().length();
				}

				// Objectifs annee N
				label = new Label(i + 5, row, objectif);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Evaluation compétence -IDEM QUE PLUS HAUT
				label = new Label(i + 6, row, evaluationCompetence);
				label.setCellFormat(format3);
				sheet.addCell(label);
				if (maxSizeNomHabilitation < label.getContents().length()) {
					maxSizeNomHabilitation = label.getContents().length();
				}

				// Competences a ameliorer
				label = new Label(i + 7, row, competenceAAmeliorer);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Competences a acquerir
				label = new Label(i + 8, row, competenceAAcquerir);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Decision d'evolution
				label = new Label(i + 9, row, decisionEvolution);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Besoins en formation
				label = new Label(i + 10, row, besoinFormation);
				label.setCellFormat(format2);
				sheet.addCell(label);

				// Formation programmee
				// label = new Label(i + 11, row, formationProgrammee);
				// label.setCellFormat(format2);
				// sheet.addCell(label);

				// Souhaits exprimés
				// label = new Label(7, row, souhaits);
				// label.setCellFormat(format2);
				// sheet.addCell(label);

				// Evol perso
				// label = new Label(8, row, evolutionPerso);
				// label.setCellFormat(format2);
				// sheet.addCell(label);

				// CR signé
				label = new Label(i + 11, row, crSigne);
				label.setCellFormat(format);
				sheet.addCell(label);

				// Modif poste
				label = new Label(i + 12, row, modifPoste);
				label.setCellFormat(format2);
				sheet.addCell(label);

				row++;
				service = "";
				evaluationCompetence = "";
				competenceAAmeliorer = "";
				competenceAAcquerir = "";
				decisionEvolution = "";
				besoinFormation = "";
				// souhaits = "";
				// formationProgrammee = "";
				// evolutionPerso = "";
				crSigne = "";
				modifPoste = "";
			}
		}

		int i = 0;
		sheet.setColumnView(i, 12);
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.setColumnView(i + 1, 20);
			i++;
		}
		sheet.setColumnView(i + 1, 20);
		sheet.setColumnView(i + 2, 20);
		sheet.setColumnView(i + 3, 20);
		sheet.setColumnView(i + 4, 20);
		sheet.setColumnView(i + 5, 40);
		sheet.setColumnView(i + 6, 18);
		sheet.setColumnView(i + 7, 40);
		sheet.setColumnView(i + 8, 40);
		sheet.setColumnView(i + 9, 40);
		sheet.setColumnView(i + 10, 40);
		// sheet.setColumnView(i + 11, 40);
		sheet.setColumnView(i + 11, 12);
		sheet.setColumnView(i + 12, 12);

	}

	private void exportRemunerations() throws Exception {
		WritableFont font = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 9, WritableFont.NO_BOLD));
		format2.setAlignment(Alignment.LEFT);
		format2.setWrap(true);
		format2.setVerticalAlignment(VerticalAlignment.CENTRE);

		// Fusionne les 23 cellules pour le titre
		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(0, 0, 23, 0);
		} else {
			sheet.mergeCells(0, 0, 22, 0);
		}
		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		format.setWrap(true);

		if (entrepriseBean.getNom() == null) {
			entrepriseBean.setNom("");
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean groupe = grServ.getGroupeBeanById(idGroupe);
			entrepriseBean.setNom(groupe.getNom());
		}
		Label label = new Label(0, 0, "TABLEAU DE SUIVI DES REMUNERATIONS \n"
				+ entrepriseBean.getNom() + " ("
				+ dateFormat.format(new Date()) + ")");
		label.setCellFormat(format);
		sheet.addCell(label);

		String filtre;
		switch (present) {
		case 0:
			filtre = "Tous les salariés";
			break;
		case 1:
			filtre = "Seuls les salariés sortis";
			break;
		case 2:
			filtre = "Seuls les salariés présents";
			break;
		default:
			filtre = "Aucun filtre disponible";
			break;
		}
		String debExt = (debutExtraction != null) ? dateFormat
				.format(debutExtraction.getTime()) : "";
		String finExt = (finExtraction != null) ? dateFormat
				.format(finExtraction.getTime()) : "";

		if (!debExt.equals("")) {
			filtre += " - Début : "
					+ dateFormat.format(debutExtraction.getTime());
		}
		if (!finExt.equals("")) {
			filtre += " - Fin : " + dateFormat.format(finExtraction.getTime());
		}
		Label label2 = new Label(0, 1, "Filtre : " + filtre);

		if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
			sheet.mergeCells(0, 1, 22, 1);
			label2.setCellFormat(format);
			sheet.addCell(label2);
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);

			label = new Label(11, 3,
					"DONNEES BRUTES ANNUELLES EXPRIMEES EN EUROS");
			label.setCellFormat(format);
			sheet.mergeCells(11, 3, 19, 3);
			sheet.addCell(label);
		} else {
			sheet.mergeCells(0, 1, 23, 1);
			label2.setCellFormat(format);
			sheet.addCell(label2);
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);

			label = new Label(12, 3,
					"DONNEES BRUTES ANNUELLES EXPRIMEES EN EUROS");
			label.setCellFormat(format);
			sheet.mergeCells(12, 3, 20, 3);
			sheet.addCell(label);
		}

		font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		format.setWrap(true);

		int y = 5;

		SalarieServiceImpl salServ = new SalarieServiceImpl();
		RemunerationServiceImpl ctServ = new RemunerationServiceImpl();
		List<RemunerationBean> remunerationBeanList;
		if (entrepriseBean.getId() > 0) {
			remunerationBeanList = ctServ
					.getRemunerationListOrderByAnneeNomSalarie(entrepriseBean
							.getId());
		} else {
			remunerationBeanList = ctServ
					.getRemunerationListOrderByAnneeNomEntrepriseNomSalarie(idGroupe);
		}

		// On applique les bornes voulues
		Iterator<RemunerationBean> ite = remunerationBeanList.iterator();

		boolean remove = false;

		while (ite.hasNext()) {

			remove = false;

			RemunerationBean remunerationBean = ite.next();
			if (debutExtraction != null) {
				if (finExtraction != null) {
					if (remunerationBean.getAnnee() > finExtraction
							.get(Calendar.YEAR)
							|| remunerationBean.getAnnee() < debutExtraction
									.get(Calendar.YEAR)) {
						ite.remove();
						remove = true;
					}
				} else {
					if (remunerationBean.getAnnee() < debutExtraction
							.get(Calendar.YEAR)) {
						ite.remove();
						remove = true;
					}
				}
			} else {
				if (finExtraction != null) {
					if (remunerationBean.getAnnee() > finExtraction
							.get(Calendar.YEAR)) {
						ite.remove();
						remove = true;
					}
				}
			}
			SalarieBeanExport salarie = salServ
					.getSalarieBeanExportById(remunerationBean.getIdSalarie());
			if (!remove) {
				if (getLastParcours(salarie.getParcoursBeanList())
						.getIdTypeContratSelected() == 7
						|| getLastParcours(salarie.getParcoursBeanList())
								.getIdTypeContratSelected() == 3) {
					ite.remove();
					remove = true;
				}
			}

			if (!remove) {
				if (present == 1) {
					if (salarie.isPresent()) {
						ite.remove();
					}
				}
				if (present == 2) {
					if (!salarie.isPresent()) {
						ite.remove();
					}
				}
			}

		}
		for (RemunerationBean bean : remunerationBeanList) {

			SalarieBean salarie = new SalarieBean();
			salarie = salServ.getSalarieBeanById(bean.getIdSalarie());

			String csp = new String();
			String metier = new String();
			ParcoursServiceImpl p = new ParcoursServiceImpl();
			metier = getNomMetier(salarie.getParcoursBeanList(),
					bean.getAnnee());
			csp = getNomCSP(salarie.getParcoursBeanList(), bean.getAnnee());

			LienRemunerationRevenuServiceImpl serv = new LienRemunerationRevenuServiceImpl();
			List<LienRemunerationRevenuBean> lien = new ArrayList<LienRemunerationRevenuBean>();
			lien = serv.getLienRemunerationRevenuFromIdRemu(bean
					.getIdRemuneration());

			RevenuComplementaireServiceImpl revserv = new RevenuComplementaireServiceImpl();
			RevenuComplementaireBean revComp = new RevenuComplementaireBean();

			double montantc = 0;
			double montantpf = 0;
			double montantpv = 0;
			double montantaa = 0;
			double montantana = 0;
			double montantfp = 0;
			for (LienRemunerationRevenuBean lrr : lien) {
				revComp = revserv.getRevenuComplementaireBeanById(lrr
						.getIdRevenuComplementaire());
				if (revComp.getType().equals("commission")) {
					if (Double.valueOf(lrr.getActualisation()) != 0) {
						montantc += Double.valueOf(lrr.getActualisation());
					} else {
						montantc += Double.valueOf(lrr.getMontant());
					}
				}
				if (revComp.getType().equals("prime_fixe")) {
					if (Double.valueOf(lrr.getActualisation()) != 0) {
						montantpf += Double.valueOf(lrr.getActualisation());
					} else {
						montantpf += Double.valueOf(lrr.getMontant());
					}
				}
				if (revComp.getType().equals("prime_variable")) {
					if (Double.valueOf(lrr.getActualisation()) != 0) {
						montantpv += Double.valueOf(lrr.getActualisation());
					} else {
						montantpv += Double.valueOf(lrr.getMontant());
					}
				}
				if (revComp.getType().equals("avantage_assujetti")) {
					if (Double.valueOf(lrr.getActualisation()) != 0) {
						montantaa += Double.valueOf(lrr.getActualisation());
					} else {
						montantaa += Double.valueOf(lrr.getMontant());
					}
				}
				if (revComp.getType().equals("avantage_non_assujetti")) {
					if (Double.valueOf(lrr.getActualisation()) != 0) {
						montantana += Double.valueOf(lrr.getActualisation());
					} else {
						montantana += Double.valueOf(lrr.getMontant());
					}
				}
				if (revComp.getType().equals("frais_professionnel")) {
					montantfp += Double.valueOf(lrr.getMontant());
				}
			}
			double totalBaseBruteAnnuelle = 0;

			double salaireDeBase = 0;
			double augmIndiv = 0;
			double augmColl = 0;
			double heuresSup25 = 0;
			double heuresSup50 = 0;

			if (bean.getSalaireDeBaseActualisation() != 0) {
				salaireDeBase = bean.getSalaireDeBaseActualisation();
			} else {
				salaireDeBase = bean.getSalaireDeBase();
			}

			if (bean.getAugmentationIndividuelleActualisation() != 0) {
				augmIndiv = bean.getAugmentationIndividuelleActualisation();
			} else {
				augmIndiv = bean.getAugmentationIndividuelle();
			}

			if (bean.getAugmentationCollectiveActualisation() != 0) {
				augmColl = bean.getAugmentationCollectiveActualisation();
			} else {
				augmColl = bean.getAugmentationCollective();
			}

			if (bean.getHeuresSup25Actualisation() != 0) {
				heuresSup25 = bean.getHeuresSup25Actualisation();
			} else {
				heuresSup25 = bean.getHeuresSup25();
			}

			if (bean.getHeuresSup50Actualisation() != 0) {
				heuresSup50 = bean.getHeuresSup50Actualisation();
			} else {
				heuresSup50 = bean.getHeuresSup50();
			}

			totalBaseBruteAnnuelle = salaireDeBase + augmIndiv + augmColl
					+ heuresSup25 + heuresSup50;
			double remunerationBruteAnnuelle = 0;
			remunerationBruteAnnuelle = totalBaseBruteAnnuelle + montantc
					+ montantpf + montantpv + montantaa;
			double totalBrutAnnuel = 0;
			totalBrutAnnuel = remunerationBruteAnnuelle + montantana;

			DecimalFormat df = new DecimalFormat();
			DecimalFormat df2 = new DecimalFormat();

			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setDecimalSeparator('.');
			df.setDecimalFormatSymbols(symbols);
			df.applyPattern("0");

			df2.setDecimalFormatSymbols(symbols);
			df2.applyPattern("0.00");

			SalarieServiceImpl salserv = new SalarieServiceImpl();
			SalarieBean sal = new SalarieBean();
			sal = salserv.getSalarieBeanById(bean.getIdSalarie());

			// Bean classic
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			format.setWrap(true);

			int i = 0;

			label = new Label(i, y, bean.getAnnee() + "");
			label.setCellFormat(format);
			sheet.setColumnView(i, 8);
			sheet.addCell(label);

			// Entreprise
			if (entrepriseBean.getId() == 0 || entrepriseBean.getId() == -1) {
				EntrepriseServiceImpl entserv = new EntrepriseServiceImpl();
				label = new Label(i + 1, y, entserv.getEntrepriseBeanById(
						salarie.getIdEntrepriseSelected()).getNom());
				label.setCellFormat(format2);
				sheet.setColumnView(i + 1, 20);
				sheet.addCell(label);
				i++;
			}

			label = new Label(i + 1, y, sal.getNom());
			label.setCellFormat(format2);
			sheet.setColumnView(i + 1, 20);
			sheet.addCell(label);

			label = new Label(i + 2, y, sal.getPrenom());
			label.setCellFormat(format2);
			sheet.setColumnView(i + 2, 15);
			sheet.addCell(label);

			label = new Label(i + 3, y, metier);
			label.setCellFormat(format2);
			sheet.setColumnView(i + 3, 30);
			sheet.addCell(label);

			label = new Label(i + 4, y, csp);
			label.setCellFormat(format2);
			sheet.setColumnView(i + 4, 30);
			sheet.addCell(label);

			label = new Label(i + 5, y, bean.getEchelon());
			label.setCellFormat(format2);
			sheet.setColumnView(i + 5, 10);
			sheet.addCell(label);

			label = new Label(i + 6, y, bean.getNiveau());
			label.setCellFormat(format2);
			sheet.setColumnView(i + 6, 10);
			sheet.addCell(label);

			if (!bean.getCoefficient().equals(""))
				label = new Label(i + 7, y, df.format(Double.valueOf(bean
						.getCoefficient())));
			else
				label = new Label(i + 7, y, bean.getCoefficient());
			label.setCellFormat(format2);
			sheet.setColumnView(i + 7, 8);
			sheet.addCell(label);

			label = new Label(i + 8, y, bean.getHoraireMensuelTravaille() + "");
			format = new WritableCellFormat(font);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			format.setWrap(true);
			format.setAlignment(Alignment.RIGHT);
			label.setCellFormat(format);
			sheet.setColumnView(i + 8, 10);
			sheet.addCell(label);

			label = new Label(i + 9, y, df2.format(bean.getTauxHoraireBrut())
					+ "");
			label.setCellFormat(format);
			sheet.setColumnView(i + 9, 8);
			sheet.addCell(label);

			label = new Label(i + 10, y, df2.format(bean
					.getSalaireMensuelBrut()) + "");
			label.setCellFormat(format);
			sheet.setColumnView(i + 10, 10);
			sheet.addCell(label);

			label = new Label(i + 11, y, df2.format(augmColl) + "");
			label.setCellFormat(format);
			sheet.setColumnView(i + 11, 14);
			sheet.addCell(label);

			label = new Label(i + 12, y, df2.format(augmIndiv) + "");
			label.setCellFormat(format);
			sheet.setColumnView(i + 12, 14);
			sheet.addCell(label);

			label = new Label(i + 13, y, df2.format(heuresSup25 + heuresSup50)
					+ "");
			label.setCellFormat(format);
			sheet.setColumnView(i + 13, 10);
			sheet.addCell(label);

			label = new Label(i + 14, y, df2.format(totalBaseBruteAnnuelle)
					+ "");
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD);
			format.setFont(font);
			label.setCellFormat(format);
			sheet.setColumnView(i + 14, 12);
			sheet.addCell(label);
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
			format.setFont(font);

			label = new Label(i + 15, y, df2.format(montantc) + "");
			label.setCellFormat(format);
			sheet.setColumnView(i + 15, 15);
			sheet.addCell(label);

			label = new Label(i + 16, y, df2.format(montantpf) + "");
			label.setCellFormat(format);
			sheet.setColumnView(i + 16, 10);
			sheet.addCell(label);

			label = new Label(i + 17, y, df2.format(montantpv) + "");
			label.setCellFormat(format);
			sheet.setColumnView(i + 17, 10);
			sheet.addCell(label);

			label = new Label(i + 18, y, df2.format(montantaa) + "");
			label.setCellFormat(format);
			sheet.setColumnView(i + 18, 12);
			sheet.addCell(label);

			label = new Label(i + 19, y, df2.format(remunerationBruteAnnuelle)
					+ "");
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD);
			format.setFont(font);
			label.setCellFormat(format);
			sheet.setColumnView(i + 19, 20);
			sheet.addCell(label);
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
			format.setFont(font);

			label = new Label(i + 20, y, df2.format(montantana) + "");
			label.setCellFormat(format);
			sheet.setColumnView(i + 20, 12);
			sheet.addCell(label);

			label = new Label(i + 21, y, df2.format(montantfp) + "");
			label.setCellFormat(format);
			sheet.setColumnView(i + 21, 15);
			sheet.addCell(label);

			label = new Label(i + 22, y, df2.format(totalBrutAnnuel) + "");
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD);
			format.setFont(font);
			label.setCellFormat(format);
			sheet.setColumnView(i + 22, 12);
			sheet.addCell(label);
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
			format.setFont(font);

			y++;

		}

	}

	private String getAncienneteEntrepriseParcours(ParcoursBean parcours)
			throws Exception {

		ParcoursServiceImpl parServ = new ParcoursServiceImpl();
		List<ParcoursBean> list = new ArrayList<ParcoursBean>();
		list = parServ.getParcoursBeanListByIdSalarie(parcours.getIdSalarie());
		Collections.sort(list);

		int index = 0;
		for (ParcoursBean p : list) {
			if (p.getId() == parcours.getId()) {
				break;
			}
			index++;
		}
		int nbJours = 0;

		for (int i = index; i < list.size() - 1; i++) {
			Calendar d1 = new GregorianCalendar();
			Calendar d2 = new GregorianCalendar();
			nbJours = 0;

			if (list.get(i).getFinFonction() != null) {
				d1.setTime(list.get(i).getFinFonction());
			} else {
				nbJours = 2;
			}
			d2.setTime(list.get(i + 1).getDebutFonction());

			while (d1.before(d2)) {
				if (nbJours > 1) {
					break;
				}
				d1.add(GregorianCalendar.DATE, 1);
				nbJours++;
			}
			if (nbJours > 1) {
				return "";
			}

		}

		nbJours = 0;
		Calendar debut = new GregorianCalendar();
		Calendar fin = new GregorianCalendar();
		Calendar today = new GregorianCalendar();
		today.setTime(new Date());

		if (parcours.getFinFonction() == null) {
			fin.setTime(new Date());
		} else {
			fin.setTime(parcours.getFinFonction());
		}

		if (fin.after(today)) {
			fin = today;
		}

		if (index == 0) {
			debut.setTime(parcours.getDebutFonction());
		} else {
			for (int i = index; i > 0; i--) {
				nbJours = 0;
				Calendar d1 = new GregorianCalendar();
				Calendar d2 = new GregorianCalendar();

				d1.setTime(list.get(i).getDebutFonction());
				if (list.get(i - 1).getFinFonction() != null) {
					d2.setTime(list.get(i - 1).getFinFonction());
				} else {
					nbJours = 2;
				}

				while (d2.before(d1)) {
					if (nbJours > 1) {
						break;
					}
					d2.add(GregorianCalendar.DATE, 1);
					nbJours++;
				}
				if (nbJours > 1) {
					debut.setTime(list.get(i).getDebutFonction());
				} else {
					debut.setTime(list.get(i - 1).getDebutFonction());
				}

			}
		}

		return getAnciennete(debut, fin);
	}

	private String getAncienneteEntrepriseContrat(ContratTravailBean contrat)
			throws Exception {

		ContratTravailServiceImpl ctServ = new ContratTravailServiceImpl();
		List<ContratTravailBean> list = new ArrayList<ContratTravailBean>();
		list = ctServ.getContratTravailBeanListByIdSalarie(contrat
				.getIdSalarie());
		Collections.sort(list);

		int index = 0;
		for (ContratTravailBean p : list) {
			if (p.getId() == contrat.getId()) {
				break;
			}
			index++;
		}
		int nbJours = 0;

		for (int i = index; i < list.size() - 1; i++) {
			Calendar d1 = new GregorianCalendar();
			Calendar d2 = new GregorianCalendar();
			nbJours = 0;

			if (list.get(i).getFinContrat() != null) {
				d1.setTime(list.get(i).getFinContrat());
			} else {
				nbJours = 2;
			}
			d2.setTime(list.get(i + 1).getDebutContrat());

			while (d1.before(d2)) {
				if (nbJours > 1) {
					break;
				}
				d1.add(GregorianCalendar.DATE, 1);
				nbJours++;
			}
			if (nbJours > 1) {
				return "";
			}

		}

		nbJours = 0;
		Calendar debut = new GregorianCalendar();
		Calendar fin = new GregorianCalendar();

		if (contrat.getFinContrat() == null) {
			fin.setTime(new Date());
		} else {
			fin.setTime(contrat.getFinContrat());
		}

		if (index == 0) {
			debut.setTime(contrat.getDebutContrat());
		} else {
			for (int i = index; i > 0; i--) {
				nbJours = 0;
				Calendar d1 = new GregorianCalendar();
				Calendar d2 = new GregorianCalendar();

				d1.setTime(list.get(i).getDebutContrat());
				if (list.get(i - 1).getFinContrat() != null) {
					d2.setTime(list.get(i - 1).getFinContrat());
				} else {
					nbJours = 2;
				}

				while (d2.before(d1)) {
					if (nbJours > 1) {
						break;
					}
					d2.add(GregorianCalendar.DATE, 1);
					nbJours++;
				}
				if (nbJours > 1) {
					debut.setTime(list.get(i).getDebutContrat());
				} else {
					debut.setTime(list.get(i - 1).getDebutContrat());
				}

			}
		}

		return getAnciennete(debut, fin);
	}

	public String getAnciennete(Calendar d1, Calendar d2) {
		String anciennete = "";
		int nbMois = 0;

		while (d1.before(d2)) {
			d1.add(GregorianCalendar.MONTH, 1);
			nbMois++;
		}
		int nbAnnees = nbMois / 12;
		nbMois = (nbMois % 12) - 1;
		if (nbAnnees > 0) {
			if (nbAnnees == 1) {
				if (nbMois > 0) {
					anciennete = nbAnnees + " an - " + nbMois + " mois";
				} else {
					anciennete = nbAnnees + " an";
				}
			} else {
				if (nbMois > 0) {
					anciennete = nbAnnees + " ans - " + nbMois + " mois";
				} else {
					anciennete = nbAnnees + " ans";
				}
			}
		} else {
			if (nbMois > 0) {
				anciennete = nbMois + " mois";
			}
		}
		return anciennete;
	}

	private boolean after(GregorianCalendar d1, GregorianCalendar d2) {
		// On teste si les dates sont egales, car GregorianCalendar ne prend pas
		// en comtpe ce cas, sinon on renvoi
		// true si d1 est avant d2
		if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
			if (d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)) {
				if (d1.get(Calendar.DATE) == d2.get(Calendar.DATE)) {
					return true;
				}
			}
		}
		return (d1.after(d2));
	}

	private boolean before(GregorianCalendar d1, GregorianCalendar d2) {
		// On teste si les dates sont egales, car GregorianCalendar ne prend pas
		// en comtpe ce cas, sinon on renvoi
		// true si d1 est avant d2
		if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
			if (d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)) {
				if (d1.get(Calendar.DATE) == d2.get(Calendar.DATE)) {
					return true;
				}
			}
		}
		return (d1.before(d2));
	}

	private boolean isInEntrepriseWithBorne(GregorianCalendar debutC,
			GregorianCalendar finC, GregorianCalendar debutDate,
			GregorianCalendar finDate) {

		if (finDate != null) {
			if (before(debutDate, debutC)) {
				return false;
			}
			if (before(finDate, debutC)) {
				return false;
			}
			if (after(finDate, finC)) {
				return false;
			}
			if (after(debutDate, finC)) {
				return false;
			}
		} else {
			if (before(debutDate, debutC)) {
				return false;
			}
			if (after(debutDate, finC)) {
				return false;
			}
		}

		return true;
	}

	private boolean isInPeriode(GregorianCalendar debutExt,
			GregorianCalendar finExt, GregorianCalendar debutObj,
			GregorianCalendar finObj) {

		if (debutExt != null) {
			if (finExt != null) {
				if (after(debutObj, debutExt) && before(debutObj, finExt)) {
					return true;
				}
				if (after(finObj, debutExt) && before(finObj, finExt)) {
					return true;
				}
				if (before(debutObj, debutExt) && after(finObj, finExt)) {
					return true;
				}
			} else {
				if (after(debutObj, debutExt)) {
					return true;
				}
				if (after(finObj, debutExt)) {
					return true;
				}
			}
		} else {
			if (finExt != null) {
				if (before(debutObj, finExt)) {
					return true;
				}
				if (before(finObj, finExt)) {
					return true;
				}
			} else {
				return true;
			}
		}

		return false;
	}

	private String getAnciennetePoste(SalarieBean salarie) {

		Calendar dateDebut = new GregorianCalendar();
		Calendar dateFin = new GregorianCalendar();

		ParcoursBean parcourFin = getLastParcours(salarie);
		dateDebut.setTime(parcourFin.getDebutFonction());
		if (parcourFin != null) {
			if (parcourFin.getFinFonction() != null) {
				dateFin.setTime(parcourFin.getFinFonction());
			} else {
				dateFin.setTime(new Date());
			}
		}
		return dateFin.get(Calendar.YEAR) - dateDebut.get(Calendar.YEAR)
				+ " an(s)";
	}

	private ParcoursBean getLastParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		Collections.sort(parcourList);
		ParcoursBean parcoursBean = null;
		if (parcourList != null && parcourList.size() > 0) {
			Collections.sort(parcourList);
			Collections.reverse(parcourList);
			parcoursBean = parcourList.get(0);
		}

		return parcoursBean;
	}

	public String getDateEntree(SalarieBean s) {
		ParcoursBean pb = getFirstParcours(s);
		if (pb != null) {
			return dateFormat.format(pb.getDebutFonction());
		} else {
			return "";
		}
	}

	public String getDateSortie(SalarieBean s) {
		ParcoursBean pb = getLastParcours(s);
		if (pb != null) {
			if (pb.getFinFonction() != null) {
				return dateFormat.format(pb.getFinFonction());
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	private ParcoursBean getFirstParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		Collections.sort(parcourList);
		Collections.reverse(parcourList);
		ParcoursBean pb = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			if (pb == null) {
				pb = parcour;
			}
			if (parcour.getDebutFonction().before(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}

}
