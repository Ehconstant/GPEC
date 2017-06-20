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
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

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

import com.cci.gpec.commons.AbsencesMetierBean;
import com.cci.gpec.commons.AbsencesNatureBean;
import com.cci.gpec.commons.AccidentMetierBean;
import com.cci.gpec.commons.ArretTravailBean;
import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.EvaluationCompetencesBean;
import com.cci.gpec.commons.Filtre;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.HeureBean;
import com.cci.gpec.commons.LienRemunerationRevenuBean;
import com.cci.gpec.commons.NegociationSalarialeBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.ProjectionPSBean;
import com.cci.gpec.commons.RemunerationBean;
import com.cci.gpec.commons.RetraiteBean;
import com.cci.gpec.commons.RevenuComplementaireBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.SalarieBeanExport;
import com.cci.gpec.commons.SuiviEffectifBean;
import com.cci.gpec.commons.TurnOverBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.LienRemunerationRevenuServiceImpl;
import com.cci.gpec.metier.implementation.RevenuComplementaireServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;

public class ExportDataTableToExcelServlet extends HttpServlet {

	private static final long serialVersionUID = -7340803130439342513L;
	private WritableWorkbook workbook;
	private WritableSheet sheet;
	private ArrayList<AbsencesNatureBean> absencesNatureBeanList;
	private ArrayList<AbsencesMetierBean> absencesMetierBeanList;
	private ArrayList<AccidentMetierBean> accidentsMetierBeanList;
	private ArrayList<RetraiteBean> departRetraiteBeanList;
	private ArrayList<HeureBean> heureBeanList;
	private ArrayList<HeureBean> heureBeanListFooter;
	private ArrayList<NegociationSalarialeBean> rowList;
	private ArrayList<ProjectionPSBean> projectionlistBean;
	private ArrayList<ArretTravailBean> arretTravailListBean;
	private ArrayList<TurnOverBean> turnOverListBean;
	private ArrayList<SalarieBeanExport> salarieListBean;
	private ArrayList<SalarieBeanExport> filtreListBean;
	private ArrayList<EvaluationCompetencesBean> evaluationCompetencesBeanList;
	private ArrayList<RemunerationBean> remunerationBeanList;
	private ArrayList<SuiviEffectifBean> suiviEffectifBeanList;
	private String[] entete;
	private ArrayList<Filtre> filtres;
	private ByteArrayOutputStream baos;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private static String NOM_FIC_EXCEL_ABSENCE_METIER = "absenceMetier.xls";
	private static String NOM_FIC_EXCEL_ABSENCE_NATURE = "absenceNature.xls";
	private static String NOM_FIC_EXCEL_ACCIDENT_METIER = "accidentMetier.xls";
	private static String NOM_FIC_EXCEL_DEPART_RETRAITE = "departRetraite.xls";
	private static String NOM_FIC_EXCEL_HEURE_FORMATION = "heuresFormation.xls";
	private static String NOM_FIC_EXCEL_PROJECTION_PS = "projectionPS.xls";
	private static String NOM_FIC_EXCEL_ARRET_TRAVAIL = "arretsTravail.xls";
	private static String NOM_FIC_EXCEL_TURN_OVER = "turnOver.xls";
	private static String NOM_FIC_EXCEL_FILTRE_MULTI = "filtreMultiCritere.xls";
	private static String NOM_FIC_EXCEL_LISTE_PERSO = "listeDuPersonnel.xls";
	private static String NOM_FIC_EXCEL_EVAL_COMP = "evaluationCompetences.xls";
	private static String NOM_FIC_EXCEL_REMUNERATION = "remuneration.xls";
	private static String NOM_FIC_EXCEL_NEGOCIATION_SALARIALE = "negociationSalariale.xls";
	private static String NOM_FIC_EXCEL_SUIVI_EFFECTIF = "suiviEffectif.xls";

	private String nomGroupeOrEntreprise = "";
	private String nomService;
	private String nomMetier;

	private EntrepriseBean entrepriseBean;
	private Integer idEntrepriseSelected;

	private String lettre;
	private int present;
	private int idCSP;

	public void doGet(HttpServletRequest req, HttpServletResponse res) {

		String nomFichier = "";
		entete = (String[]) req.getSession().getAttribute("datatableEnTete");

		idEntrepriseSelected = 0;
		if (req.getSession().getAttribute("idEntreprise") != null) {
			idEntrepriseSelected = (Integer) req.getSession().getAttribute(
					"idEntreprise");
		}

		int idGroupe = (Integer) req.getSession().getAttribute("groupe");

		nomService = "";
		if (req.getSession().getAttribute("nomService") != null) {
			nomService = (String) req.getSession().getAttribute("nomService");
		}
		nomMetier = "";
		if (req.getSession().getAttribute("nomMetier") != null) {
			nomMetier = (String) req.getSession().getAttribute("nomMetier");
		}

		idCSP = 1;
		if (req.getSession().getAttribute("idCSP") != null) {
			idCSP = (Integer) req.getSession().getAttribute("idCSP");
		}

		if (req.getSession().getAttribute("alphabet") != null) {
			lettre = (String) req.getSession().getAttribute("alphabet");
		}
		if (req.getSession().getAttribute("present") != null) {
			present = (Integer) req.getSession().getAttribute("present");
		}

		entrepriseBean = new EntrepriseBean();
		if (idEntrepriseSelected != -1 && idEntrepriseSelected != 0) {
			EntrepriseServiceImpl entrepriseService = new EntrepriseServiceImpl();
			try {
				entrepriseBean = entrepriseService
						.getEntrepriseBeanById(idEntrepriseSelected);
				nomGroupeOrEntreprise = entrepriseBean.getNom();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// si l'on choisit de faire un export du tableau d'analyse sans
			// avoir séléctionner d'entreprise
			// on affiche donc le nom du groupe
			GroupeServiceImpl groupeService = new GroupeServiceImpl();
			try {
				nomGroupeOrEntreprise = groupeService.getGroupeBeanById(
						idGroupe).getNom();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		baos = new ByteArrayOutputStream();
		try {
			workbook = Workbook.createWorkbook(baos);
			sheet = workbook.createSheet("First Sheet", 0);

			String name = (String) req.getSession().getAttribute("name");
			if ((name != "Turn over") && (name != "EvalComp")
					&& name != "Filtre multi" && name != "Heure formation"
					&& name != "Negociation salariale"
					&& name != "Absence par nature"
					&& name != "Arret de travail"
					&& name != "Accident par metier"
					&& name != "Gestion effectif") {
				setEntete();
			}

			if (name == "Absence par metier") {
				absencesMetierBeanList = (ArrayList<AbsencesMetierBean>) req
						.getSession().getAttribute("datatable");
				HashMap<String, Integer> mapTotaux = (HashMap<String, Integer>) req
						.getSession().getAttribute("mapTotaux");
				fillSheetAbsencesMetier(mapTotaux);
				nomFichier = NOM_FIC_EXCEL_ABSENCE_METIER;
			}
			if (name.equals("Absence par nature")) {
				absencesNatureBeanList = (ArrayList<AbsencesNatureBean>) req
						.getSession().getAttribute("datatable");
				fillSheetAbsencesNature();
				nomFichier = NOM_FIC_EXCEL_ABSENCE_NATURE;
			}
			if (name == "Accident par metier") {
				accidentsMetierBeanList = (ArrayList<AccidentMetierBean>) req
						.getSession().getAttribute("datatable");
				HashMap<String, String> mapTaux = (HashMap<String, String>) req
						.getSession().getAttribute("mapTaux");
				HashMap<String, Integer> mapTotaux = (HashMap<String, Integer>) req
						.getSession().getAttribute("mapTotaux");
				fillSheetAccidentsMetier(mapTaux, mapTotaux);
				nomFichier = NOM_FIC_EXCEL_ACCIDENT_METIER;
			}
			if (name == "Depart retraite") {
				departRetraiteBeanList = (ArrayList<RetraiteBean>) req
						.getSession().getAttribute("datatable");
				HashMap<String, Integer> mapTotaux = (HashMap<String, Integer>) req
						.getSession().getAttribute("mapTotaux");
				fillSheetDepartRetraite(mapTotaux);
				nomFichier = NOM_FIC_EXCEL_DEPART_RETRAITE;
			}
			if (name == "Heure formation") {
				heureBeanList = (ArrayList<HeureBean>) req.getSession()
						.getAttribute("datatable");
				heureBeanListFooter = (ArrayList<HeureBean>) req.getSession()
						.getAttribute("datatable2");
				fillSheetHeureFormation();
				nomFichier = NOM_FIC_EXCEL_HEURE_FORMATION;
			}
			if (name == "Negociation salariale") {
				rowList = (ArrayList<NegociationSalarialeBean>) req
						.getSession().getAttribute("datatable");
				if (idCSP == 1 || idCSP == 2)
					fillSheetNegociationSalariale();
				else
					fillSheetNegociationSalarialeParSexe();
				nomFichier = NOM_FIC_EXCEL_NEGOCIATION_SALARIALE;
			}
			if (name == "Plan senior") {
				projectionlistBean = (ArrayList<ProjectionPSBean>) req
						.getSession().getAttribute("datatable");
				HashMap<String, Integer> mapTotaux = (HashMap<String, Integer>) req
						.getSession().getAttribute("mapTotaux");
				fillSheetProjectionPS(mapTotaux);
				nomFichier = NOM_FIC_EXCEL_PROJECTION_PS;
			}
			if (name == "Arret de travail") {
				arretTravailListBean = (ArrayList<ArretTravailBean>) req
						.getSession().getAttribute("datatable");
				HashMap<String, Integer> mapTotaux = (HashMap<String, Integer>) req
						.getSession().getAttribute("mapTotaux");
				HashMap<String, String> mapTaux = (HashMap<String, String>) req
						.getSession().getAttribute("mapTaux");
				fillSheetArretTravail(mapTotaux, mapTaux);
				nomFichier = NOM_FIC_EXCEL_ARRET_TRAVAIL;
			}
			if (name == "Remuneration") {
				remunerationBeanList = (ArrayList<RemunerationBean>) req
						.getSession().getAttribute("datatable");
				fillSheetRemuneration();
				nomFichier = NOM_FIC_EXCEL_REMUNERATION;
			}
			if (name == "Turn over") {
				turnOverListBean = (ArrayList<TurnOverBean>) req.getSession()
						.getAttribute("datatable");
				HashMap<String, Integer> mapTotaux = (HashMap<String, Integer>) req
						.getSession().getAttribute("mapTotaux");
				HashMap<String, String> mapTaux = (HashMap<String, String>) req
						.getSession().getAttribute("mapTaux");
				fillSheetTurnOver();
				nomFichier = NOM_FIC_EXCEL_TURN_OVER;
			}
			if (name == "Filtre multi") {
				filtreListBean = (ArrayList<SalarieBeanExport>) req
						.getSession().getAttribute("datatable");
				filtres = (ArrayList<Filtre>) req.getSession().getAttribute(
						"filtres");
				fillSheetFiltre();
				nomFichier = NOM_FIC_EXCEL_FILTRE_MULTI;
			}

			if (name == "Liste Salaries") {
				salarieListBean = (ArrayList<SalarieBeanExport>) req
						.getSession().getAttribute("datatable");
				fillSheetListeDuPersonnel();
				nomFichier = NOM_FIC_EXCEL_LISTE_PERSO;
			}

			if (name == "EvalComp") {
				evaluationCompetencesBeanList = (ArrayList<EvaluationCompetencesBean>) req
						.getSession().getAttribute("datatable");
				fillSheetEvaluationCompetences();
				nomFichier = NOM_FIC_EXCEL_EVAL_COMP;
			}
			if (name == "Gestion effectif") {
				suiviEffectifBeanList = (ArrayList<SuiviEffectifBean>) req
						.getSession().getAttribute("datatable");
				fillSheetGestionEffectif();
				nomFichier = NOM_FIC_EXCEL_SUIVI_EFFECTIF;
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
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		doGet(req, res);
	}

	public void end() throws Exception {
		workbook.write();
		workbook.close();
	}

	private void setEntete() throws Exception {
		sheet.setColumnView(0, 30);
		sheet.setRowView(0, 800);
		WritableFont font = new WritableFont(WritableFont.TIMES, 11,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(jxl.format.Alignment.CENTRE);
		format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		format.setWrap(true);

		Label label;
		for (int i = 0; i < entete.length; i++) {
			if (!entete[i].equals("")) {
				label = new Label(i, 4, entete[i]);
				label.setCellFormat(format);
				sheet.addCell(label);
			}
		}
	}

	private void setEntete(int y) throws Exception {
		sheet.setRowView(0, 800);
		WritableFont font = new WritableFont(WritableFont.TIMES, 11,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(jxl.format.Alignment.CENTRE);
		format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		format.setWrap(true);

		Label label;
		for (int i = 0; i < entete.length; i++) {
			label = new Label(i, y, entete[i]);
			label.setCellFormat(format);
			sheet.addCell(label);
		}
	}

	private void fillSheetAbsencesNature() throws Exception {
		WritableFont font;
		WritableCellFormat format;

		int i = 0;
		Label label;

		// Fusionne les 3 cellules pour le titre
		sheet.mergeCells(0, 0, 3, 0);
		sheet.mergeCells(0, 1, 3, 1);
		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		label = new Label(0, 0, "Absences par nature");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(0, 1, nomGroupeOrEntreprise + " ("
				+ dateFormat.format(new Date()) + ")");
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(0, 3, "Absences par nature en jours travaill\u00E9s");
		label.setCellFormat(format);
		sheet.mergeCells(0, 3, 3, 3);
		sheet.addCell(label);
		label = new Label(0, 4, "Taux d'absent\u00E9isme / ann\u00E9e");
		label.setCellFormat(format);
		sheet.mergeCells(0, 4, 3, 4);
		sheet.addCell(label);

		setEntete(5);

		WritableCellFormat format2;
		for (AbsencesNatureBean absenceNatureBean : absencesNatureBeanList) {
			if (absenceNatureBean.isFooter()) {
				font = new WritableFont(WritableFont.TIMES, 10,
						WritableFont.BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.RIGHT);
				format2 = new WritableCellFormat(font);
				format2.setAlignment(Alignment.CENTRE);
			} else {
				font = new WritableFont(WritableFont.TIMES, 9,
						WritableFont.NO_BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.LEFT);
				format2 = new WritableCellFormat(font);
				format2.setAlignment(Alignment.LEFT);
			}

			label = new Label(0, i + 6, absenceNatureBean.getNomTypeAbsence());
			label.setCellFormat(format2);
			sheet.setColumnView(0, 30);
			sheet.addCell(label);

			if (absenceNatureBean.getNbAbsenceByNatureTwoYearAgo() != null) {

				label = new Label(1, i + 6, absenceNatureBean
						.getNbAbsenceByNatureTwoYearAgo().toString());
				label.setCellFormat(format);
				sheet.setColumnView(1, 15);
				sheet.addCell(label);

				label = new Label(2, i + 6, absenceNatureBean
						.getNbAbsenceByNatureOneYearAgo().toString());
				label.setCellFormat(format);
				sheet.setColumnView(2, 15);
				sheet.addCell(label);

				label = new Label(3, i + 6, absenceNatureBean
						.getNbAbsenceByNatureCurrentYear().toString());
				label.setCellFormat(format);
				sheet.setColumnView(3, 15);
				sheet.addCell(label);
			} else {
				if (absenceNatureBean.isFooter()) {

					label = new Label(1, i + 6, absenceNatureBean
							.getNbAbsenceByNatureTwoYearAgoTaux().toString());
					label.setCellFormat(format);
					sheet.setColumnView(1, 15);
					sheet.addCell(label);

					label = new Label(2, i + 6, absenceNatureBean
							.getNbAbsenceByNatureOneYearAgoTaux().toString());
					label.setCellFormat(format);
					sheet.setColumnView(2, 15);
					sheet.addCell(label);

					label = new Label(3, i + 6, absenceNatureBean
							.getNbAbsenceByNatureCurrentYearTaux().toString());
					label.setCellFormat(format);
					sheet.setColumnView(3, 15);
					sheet.addCell(label);
				}
			}
			i++;
		}

	}

	private void fillSheetAbsencesMetier(HashMap<String, Integer> mapTotaux)
			throws Exception {

		WritableFont font = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		Label label;

		// Fusionne les 3 cellules pour le titre
		sheet.mergeCells(0, 0, 3, 0);
		sheet.mergeCells(0, 1, 3, 1);
		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		label = new Label(0, 0, "Absences par m\u00E9tier");
		label.setCellFormat(format);
		sheet.addCell(label);
		String titre = nomGroupeOrEntreprise;
		if (!nomService.equals("")) {
			titre += " - " + nomService;
		}
		titre += " (" + dateFormat.format(new Date()) + ")";
		label = new Label(0, 1, titre);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(0, 3, "Absences par m\u00E9tier en jours travaillés");
		label.setCellFormat(format);
		sheet.mergeCells(0, 3, 3, 3);
		sheet.addCell(label);

		for (int i = 0; i < absencesMetierBeanList.size(); i++) {
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.LEFT);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);

			AbsencesMetierBean bean = absencesMetierBeanList.get(i);
			label = new Label(0, i + 5, bean.getNom());
			label.setCellFormat(format);
			sheet.addCell(label);

			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.LEFT);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);

			label = new Label(1, i + 5, bean.getNbAbsenceByMetierTwoYearAgo()
					+ "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(2, i + 5, bean.getNbAbsenceByMetierOneYearAgo()
					+ "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(3, i + 5, bean.getNbAbsenceByMetierCurYear() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
		}
		font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);

		label = new Label(0, absencesMetierBeanList.size() + 5,
				"Nombre de jours d'absence / ann\u00e9e");
		label.setCellFormat(format);
		sheet.addCell(label);

		font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.RIGHT);

		label = new Label(1, absencesMetierBeanList.size() + 5,
				mapTotaux.get("nbJourAbsenceTwoYearAgo") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(2, absencesMetierBeanList.size() + 5,
				mapTotaux.get("nbJourAbsenceOneYearAgo") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(3, absencesMetierBeanList.size() + 5,
				mapTotaux.get("nbJourAbsenceCurYear") + "");
		label.setCellFormat(format);
		sheet.addCell(label);

		sheet.setColumnView(1, 12);
		sheet.setColumnView(2, 12);
		sheet.setColumnView(3, 12);
	}

	private void fillSheetAccidentsMetier(HashMap<String, String> mapTaux,
			HashMap<String, Integer> mapTotaux) throws Exception {

		WritableFont font = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		Label label;

		// Fusionne les 3 cellules pour le titre
		sheet.mergeCells(0, 0, 3, 0);
		sheet.mergeCells(0, 1, 3, 1);

		sheet.setColumnView(0, 35);

		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		label = new Label(0, 0, "Accidents du travail par m\u00E9tier");
		label.setCellFormat(format);
		sheet.addCell(label);
		String titre = nomGroupeOrEntreprise;
		if (!nomService.equals("")) {
			titre += " - " + nomService;
		}
		titre += " (" + dateFormat.format(new Date()) + ")";
		label = new Label(0, 1, titre);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(0, 3, "Accidents du travail par m\u00E9tier ");
		label.setCellFormat(format);
		sheet.mergeCells(0, 3, 3, 3);
		sheet.addCell(label);

		label = new Label(0, 4, "Taux de fr\u00E9quence / ann\u00E9e");
		label.setCellFormat(format);
		sheet.mergeCells(0, 4, 3, 4);
		sheet.addCell(label);

		setEntete(5);

		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.LEFT);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		for (int i = 0; i < accidentsMetierBeanList.size(); i++) {
			AccidentMetierBean bean = accidentsMetierBeanList.get(i);
			label = new Label(0, i + 6, bean.getNom());
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(1, i + 6, bean.getNbAccidentTwoYearAgo() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(2, i + 6, bean.getNbAccidentOneYearAgo() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(3, i + 6, bean.getNbAccidentCurYear() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
		}
		font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.RIGHT);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		ResourceBundle rb = ResourceBundle.getBundle("accents");
		label = new Label(0, accidentsMetierBeanList.size() + 6,
				rb.getString("Nombre d'accidents du travail / annee"));
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(1, accidentsMetierBeanList.size() + 6,
				mapTotaux.get("nbAccidentTwoYearAgo") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(2, accidentsMetierBeanList.size() + 6,
				mapTotaux.get("nbAccidentOneYearAgo") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(3, accidentsMetierBeanList.size() + 6,
				mapTotaux.get("nbAccidentCurYear") + "");
		label.setCellFormat(format);
		sheet.addCell(label);

		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);

		label = new Label(0, accidentsMetierBeanList.size() + 7,
				rb.getString("Taux de frequence / annee"));
		label.setCellFormat(format);
		sheet.addCell(label);

		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.RIGHT);

		label = new Label(1, accidentsMetierBeanList.size() + 7,
				mapTaux.get("tauxAccidentTwoYearAgo"));
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(2, accidentsMetierBeanList.size() + 7,
				mapTaux.get("tauxAccidentOneYearAgo"));
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(3, accidentsMetierBeanList.size() + 7,
				mapTaux.get("tauxAccidentCurYear"));
		label.setCellFormat(format);
		sheet.addCell(label);

	}

	private void fillSheetDepartRetraite(HashMap<String, Integer> mapTotaux)
			throws Exception {

		WritableFont font = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		Label label;

		// Fusionne les 6 cellules pour le titre
		if (mapTotaux.keySet().size() == 3) {
			sheet.mergeCells(0, 0, 3, 0);
			sheet.mergeCells(0, 1, 3, 1);
		} else {
			sheet.mergeCells(0, 0, 6, 0);
			sheet.mergeCells(0, 1, 6, 1);
		}
		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		label = new Label(0, 0,
				"Planning pr\u00E9visionnel des d\u00E9parts en retraite ");
		label.setCellFormat(format);
		sheet.addCell(label);
		String titre = nomGroupeOrEntreprise;
		if (!nomService.equals("")) {
			titre += " - " + nomService;
		}
		titre += " (" + dateFormat.format(new Date()) + ")";
		label = new Label(0, 1, titre);
		label.setCellFormat(format);
		sheet.addCell(label);

		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.RIGHT);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		for (int i = 0; i < departRetraiteBeanList.size(); i++) {
			RetraiteBean bean = departRetraiteBeanList.get(i);
			label = new Label(0, i + 5, bean.getNom());
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(1, i + 5, bean.getDepartYear1() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(2, i + 5, bean.getDepartYear2() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(3, i + 5, bean.getDepartYear3() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			if (mapTotaux.keySet().size() > 3) {
				label = new Label(4, i + 5, bean.getDepartYear4() + "");
				label.setCellFormat(format);
				sheet.addCell(label);
				label = new Label(5, i + 5, bean.getDepartYear5() + "");
				label.setCellFormat(format);
				sheet.addCell(label);
				label = new Label(6, i + 5, bean.getDepartYear6() + "");
				label.setCellFormat(format);
				sheet.addCell(label);
			}
		}
		font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		ResourceBundle rb = ResourceBundle.getBundle("accents");
		label = new Label(0, departRetraiteBeanList.size() + 5,
				rb.getString("Total des departs"));
		label.setCellFormat(format);
		sheet.addCell(label);

		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.RIGHT);
		label = new Label(1, departRetraiteBeanList.size() + 5,
				mapTotaux.get("nbDepartCurYear") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(2, departRetraiteBeanList.size() + 5,
				mapTotaux.get("nbDepartYear1") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(3, departRetraiteBeanList.size() + 5,
				mapTotaux.get("nbDepartYear2") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		if (mapTotaux.keySet().size() > 3) {
			label = new Label(4, departRetraiteBeanList.size() + 5,
					mapTotaux.get("nbDepartYear3") + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(5, departRetraiteBeanList.size() + 5,
					mapTotaux.get("nbDepartYear4") + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(6, departRetraiteBeanList.size() + 5,
					mapTotaux.get("nbDepartYear5") + "");
			label.setCellFormat(format);
			sheet.addCell(label);
		}

		sheet.setColumnView(1, 10);
		sheet.setColumnView(2, 10);
		sheet.setColumnView(3, 10);
		if (mapTotaux.keySet().size() > 3) {
			sheet.setColumnView(4, 10);
			sheet.setColumnView(5, 10);
			sheet.setColumnView(6, 10);
		}
	}

	private void fillSheetHeureFormation() throws Exception {

		WritableFont font = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		WritableCellFormat format3 = new WritableCellFormat();
		format.setAlignment(Alignment.CENTRE);
		Label label;

		// Fusionne les 3 cellules pour le titre
		sheet.mergeCells(0, 0, 18, 0);
		sheet.mergeCells(0, 1, 18, 1);
		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		label = new Label(0, 0,
				"Nombre d'heures de formation - Coût de la formation ");
		label.setCellFormat(format);
		sheet.addCell(label);
		String titre = nomGroupeOrEntreprise;
		if (!nomService.equals("")) {
			titre += " - " + nomService;
		}
		titre += " (" + dateFormat.format(new Date()) + ")";
		label = new Label(0, 1, titre);
		label.setCellFormat(format);
		sheet.addCell(label);

		int i = 0;
		font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		format.setWrap(true);
		label = new Label(0, 3,
				"Nombre d'heures de formation - Coût de la formation");
		label.setCellFormat(format);
		sheet.mergeCells(0, 3, 18, 3);
		sheet.addCell(label);

		// intitule
		label = new Label(0, 4, entete[0]);
		label.setCellFormat(format);
		sheet.setColumnView(0, 30);
		sheet.mergeCells(0, 4, 0, 6);
		sheet.addCell(label);

		// annee N-2
		label = new Label(1, 4, entete[1]);
		label.setCellFormat(format);
		sheet.mergeCells(1, 4, 6, 4);
		sheet.addCell(label);

		// annee N-1
		label = new Label(7, 4, entete[2]);
		label.setCellFormat(format);
		sheet.mergeCells(7, 4, 12, 4);
		sheet.addCell(label);

		// annee N
		label = new Label(13, 4, entete[3]);
		label.setCellFormat(format);
		sheet.mergeCells(13, 4, 18, 4);
		sheet.addCell(label);

		// nombre d'heures
		sheet.setColumnView(1, 15);
		sheet.setColumnView(7, 15);
		sheet.setColumnView(13, 15);

		label = new Label(1, 5, entete[4]);
		label.setCellFormat(format);
		sheet.mergeCells(1, 5, 1, 6);
		sheet.addCell(label);

		label = new Label(7, 5, entete[4]);
		label.setCellFormat(format);
		sheet.mergeCells(7, 5, 7, 6);
		sheet.addCell(label);

		label = new Label(13, 5, entete[4]);
		label.setCellFormat(format);
		sheet.mergeCells(13, 5, 13, 6);
		sheet.addCell(label);

		// hommes
		label = new Label(2, 5, entete[5]);
		label.setCellFormat(format);
		sheet.mergeCells(2, 5, 2, 6);
		sheet.addCell(label);

		label = new Label(8, 5, entete[5]);
		label.setCellFormat(format);
		sheet.mergeCells(8, 5, 8, 6);
		sheet.addCell(label);

		label = new Label(14, 5, entete[5]);
		label.setCellFormat(format);
		sheet.mergeCells(14, 5, 14, 6);
		sheet.addCell(label);

		// femmes
		label = new Label(3, 5, entete[6]);
		label.setCellFormat(format);
		sheet.mergeCells(3, 5, 3, 6);
		sheet.addCell(label);

		label = new Label(9, 5, entete[6]);
		label.setCellFormat(format);
		sheet.mergeCells(9, 5, 9, 6);
		sheet.addCell(label);

		label = new Label(15, 5, entete[6]);
		label.setCellFormat(format);
		sheet.mergeCells(15, 5, 15, 6);
		sheet.addCell(label);

		// repartition du cout de la formation
		sheet.setColumnView(4, 10);
		sheet.setColumnView(5, 15);
		sheet.setColumnView(6, 10);

		sheet.setColumnView(10, 10);
		sheet.setColumnView(11, 15);
		sheet.setColumnView(12, 10);

		sheet.setColumnView(16, 10);
		sheet.setColumnView(17, 15);
		sheet.setColumnView(18, 10);

		label = new Label(4, 5, entete[7]);
		label.setCellFormat(format);
		sheet.mergeCells(4, 5, 6, 5);
		sheet.addCell(label);

		label = new Label(10, 5, entete[7]);
		label.setCellFormat(format);
		sheet.mergeCells(10, 5, 12, 5);
		sheet.addCell(label);

		label = new Label(16, 5, entete[7]);
		label.setCellFormat(format);
		sheet.mergeCells(16, 5, 18, 5);
		sheet.addCell(label);

		// OPCA
		label = new Label(4, 6, entete[8]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(10, 6, entete[8]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(16, 6, entete[8]);
		label.setCellFormat(format);
		sheet.addCell(label);

		// ENTREPRISE
		label = new Label(5, 6, entete[9]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(11, 6, entete[9]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(17, 6, entete[9]);
		label.setCellFormat(format);
		sheet.addCell(label);

		// AUTRE
		label = new Label(6, 6, entete[10]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(12, 6, entete[10]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(18, 6, entete[10]);
		label.setCellFormat(format);
		sheet.addCell(label);

		for (HeureBean bean : heureBeanList) {

			WritableCellFormat format2;
			if (bean.isFooter()) {
				font = new WritableFont(WritableFont.TIMES, 10,
						WritableFont.BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.RIGHT);
				format2 = new WritableCellFormat(font);
				format2.setBackground(Colour.GRAY_25);
				format2.setAlignment(Alignment.RIGHT);
				format3 = new WritableCellFormat(font);
				format3.setAlignment(Alignment.CENTRE);
				// format2.setBorder(Border.ALL,
				// BorderLineStyle.MEDIUM_DASH_DOT,
				// Colour.BLACK);
			} else {
				font = new WritableFont(WritableFont.TIMES, 9,
						WritableFont.NO_BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.LEFT);
				format2 = new WritableCellFormat(font);
				format2.setBackground(Colour.GRAY_25);
				format2.setAlignment(Alignment.LEFT);
				format3 = new WritableCellFormat(font);
				format3.setAlignment(Alignment.LEFT);
				// format2.setBorder(Border.ALL,
				// BorderLineStyle.MEDIUM_DASH_DOT,
				// Colour.BLACK);
			}

			format.setWrap(true);
			format2.setWrap(true);
			format3.setWrap(true);

			label = new Label(0, i + 7, bean.getLibelle());
			label.setCellFormat(format3);
			sheet.addCell(label);

			if (!bean.isTotalCoutFormation()) {
				label = new Label(1, i + 7, bean.getHeureTwoYearAgo() + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(2, i + 7, bean.getHeureTwoYearAgoH() + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(3, i + 7, bean.getHeureTwoYearAgoF() + "");
				label.setCellFormat(format);
				sheet.addCell(label);
			} else {

				label = new Label(1, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);

				label = new Label(2, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);

				label = new Label(3, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);
			}

			if (!bean.isTotalHeureFormation()) {
				label = new Label(4, i + 7, bean.getCoutOPCATwoYearAgo() + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(5, i + 7, bean.getCoutENTTwoYearAgo() + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(6, i + 7, bean.getCoutAUTRETwoYearAgo() + "");
				label.setCellFormat(format);
				sheet.addCell(label);
			} else {
				label = new Label(4, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);

				label = new Label(5, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);

				label = new Label(6, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);
			}

			if (!bean.isTotalCoutFormation()) {
				label = new Label(7, i + 7, bean.getHeureOneYearAgo() + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(8, i + 7, bean.getHeureOneYearAgoH() + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(9, i + 7, bean.getHeureOneYearAgoF() + "");
				label.setCellFormat(format);
				sheet.addCell(label);
			} else {
				label = new Label(7, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);

				label = new Label(8, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);

				label = new Label(9, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);
			}

			if (!bean.isTotalHeureFormation()) {
				label = new Label(10, i + 7, bean.getCoutOPCAOneYearAgo() + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(11, i + 7, bean.getCoutENTOneYearAgo() + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(12, i + 7, bean.getCoutAUTREOneYearAgo() + "");
				label.setCellFormat(format);
				sheet.addCell(label);
			} else {
				label = new Label(10, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);

				label = new Label(11, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);

				label = new Label(12, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);
			}

			if (!bean.isTotalCoutFormation()) {
				label = new Label(13, i + 7, bean.getHeureCurYear() + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(14, i + 7, bean.getHeureCurYearH() + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(15, i + 7, bean.getHeureCurYearF() + "");
				label.setCellFormat(format);
				sheet.addCell(label);
			} else {
				label = new Label(13, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);

				label = new Label(14, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);

				label = new Label(15, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);
			}

			if (!bean.isTotalHeureFormation()) {
				label = new Label(16, i + 7, bean.getCoutOPCACurYear() + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(17, i + 7, bean.getCoutENTCurYear() + "");
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(18, i + 7, bean.getCoutAUTRECurYear() + "");
				label.setCellFormat(format);
				sheet.addCell(label);
			} else {
				label = new Label(16, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);

				label = new Label(17, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);

				label = new Label(18, i + 7, "");
				label.setCellFormat(format2);
				sheet.addCell(label);
			}

			i++;
		}

		i += 10;

		if (idEntrepriseSelected != null && idEntrepriseSelected > -1) {

			font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);

			// intitule
			label = new Label(0, i, entete[0]);
			label.setCellFormat(format);
			sheet.setColumnView(0, 30);
			sheet.addCell(label);

			// annee N-2
			label = new Label(1, i, entete[1]);
			label.setCellFormat(format);
			sheet.addCell(label);

			// annee N-1
			label = new Label(2, i, entete[2]);
			label.setCellFormat(format);
			sheet.addCell(label);

			// annee N
			label = new Label(3, i, entete[3]);
			label.setCellFormat(format);
			sheet.addCell(label);

			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.LEFT);

			i++;

			for (HeureBean bean : heureBeanListFooter) {
				label = new Label(0, i, bean.getLibelle());
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(1, i, bean.getCoutOPCATwoYearAgo());
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(2, i, bean.getCoutOPCAOneYearAgo());
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(3, i, bean.getCoutOPCACurYear());
				label.setCellFormat(format);
				sheet.addCell(label);

				i++;
			}
		}

	}

	private void fillSheetNegociationSalariale() throws Exception {

		WritableFont font = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		Label label;

		// Fusionne les 3 cellules pour le titre
		sheet.mergeCells(0, 0, 9, 0);
		sheet.mergeCells(0, 1, 9, 1);
		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		label = new Label(0, 0,
				"Négociation salariale - Suivi de la rémunération ");
		label.setCellFormat(format);
		sheet.addCell(label);
		String titre = nomGroupeOrEntreprise;
		if (!nomService.equals("")) {
			titre += " - " + nomService;
		}
		titre += " (" + dateFormat.format(new Date()) + ")";
		label = new Label(0, 1, titre);
		label.setCellFormat(format);
		sheet.addCell(label);

		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		if (idCSP == 1) {
			label = new Label(
					0,
					3,
					"Coût de la rémunération - Moyennes par CSP (données annuelles exprimées en €) ");
		} else {
			label = new Label(
					0,
					3,
					"Coût de la rémunération - Moyennes par métiers (données annuelles exprimées en €)");
		}
		label.setCellFormat(format);
		sheet.mergeCells(0, 3, 9, 3);
		sheet.addCell(label);

		font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		format.setWrap(true);

		// intitule
		if (idCSP == 1) {
			label = new Label(0, 4, "CSP");
		} else {
			label = new Label(0, 4, "Métiers");
		}
		label.setCellFormat(format);
		sheet.mergeCells(0, 4, 0, 5);
		sheet.addCell(label);

		// effectif
		label = new Label(1, 4, entete[6]);
		label.setCellFormat(format);
		sheet.mergeCells(1, 4, 1, 5);
		sheet.addCell(label);

		// annee
		label = new Label(2, 4, entete[3]);
		label.setCellFormat(format);
		sheet.mergeCells(2, 4, 9, 4);
		sheet.addCell(label);

		// salaires bruts annuels

		label = new Label(2, 5, entete[7]);
		label.setCellFormat(format);
		sheet.addCell(label);

		// remuneration globale brute annuelle
		label = new Label(4, 5, entete[8]);
		label.setCellFormat(format);
		sheet.addCell(label);

		// heures sup annuelles
		label = new Label(6, 5, entete[9]);
		label.setCellFormat(format);
		sheet.addCell(label);

		// avantages non assujettis annuels
		label = new Label(8, 5, entete[10]);
		label.setCellFormat(format);
		sheet.addCell(label);

		// Moyenne
		int cpt = 3;
		for (int j = 0; j < 4; j++) {
			label = new Label(cpt, 5, entete[11]);
			label.setCellFormat(format);
			sheet.addCell(label);
			cpt += 2;
		}

		int row = 6;
		for (NegociationSalarialeBean bean : rowList) {

			WritableCellFormat format2;
			WritableCellFormat format3;
			if (bean.isFooter()) {
				font = new WritableFont(WritableFont.TIMES, 10,
						WritableFont.BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.RIGHT);
				format2 = new WritableCellFormat(font);
				format2.setAlignment(Alignment.RIGHT);
				format3 = new WritableCellFormat(font);
				format3.setAlignment(Alignment.CENTRE);
			} else {
				font = new WritableFont(WritableFont.TIMES, 9,
						WritableFont.NO_BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.LEFT);
				format2 = new WritableCellFormat(font);
				format2.setAlignment(Alignment.LEFT);
				format3 = new WritableCellFormat(font);
				format3.setAlignment(Alignment.LEFT);
			}
			format.setWrap(true);
			format2.setWrap(true);
			format3.setWrap(true);

			// libelle
			label = new Label(0, row, bean.getLibelle());
			label.setCellFormat(format3);
			sheet.addCell(label);

			// effectif
			label = new Label(1, row, bean.getEffectif() + "");
			label.setCellFormat(format2);
			sheet.addCell(label);

			// salaire brut annuel
			label = new Label(2, row, bean.getSalaireDeBaseBrutAnnuel());
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenne
			label = new Label(3, row, bean.getSalaireDeBaseBrutAnnuelMoy());
			label.setCellFormat(format);
			sheet.addCell(label);

			// remuneration globale
			label = new Label(4, row, bean.getRemuGlobaleBruteAnnuelle());
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenne
			label = new Label(5, row, bean.getRemuGlobaleBruteAnnuelleMoy());
			label.setCellFormat(format);
			sheet.addCell(label);

			// heures sup annuelles
			label = new Label(6, row, bean.getHeureSupAnnuelles());
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenne
			label = new Label(7, row, bean.getHeureSupAnnuellesMoy());
			label.setCellFormat(format);
			sheet.addCell(label);

			// avantages non assujettis annuels
			label = new Label(8, row, bean.getAvantagesNonAssujettisAnnuels());
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenne
			label = new Label(9, row,
					bean.getAvantagesNonAssujettisAnnuelsMoy());
			label.setCellFormat(format);
			sheet.addCell(label);

			row++;
		}
		sheet.setColumnView(0, 20);
		sheet.setColumnView(2, 20);
		sheet.setColumnView(4, 20);
		sheet.setColumnView(6, 20);
		sheet.setColumnView(8, 20);

		sheet.setColumnView(3, 10);
		sheet.setColumnView(5, 10);
		sheet.setColumnView(7, 10);
		sheet.setColumnView(9, 10);

	}

	private void fillSheetGestionEffectif() throws Exception {

		WritableFont font = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		Label label;
		sheet.mergeCells(0, 0, 9, 0);
		sheet.mergeCells(0, 1, 9, 1);

		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		label = new Label(0, 0, "Gestion des effectifs");
		label.setCellFormat(format);
		sheet.addCell(label);
		String titre = nomGroupeOrEntreprise;
		if (!nomService.equals("")) {
			titre += " - " + nomService;
		}
		titre += " (" + dateFormat.format(new Date()) + ")";
		label = new Label(0, 1, titre);
		label.setCellFormat(format);
		sheet.addCell(label);

		int row = 4;

		font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		if (entete[1].endsWith("CSP")) {
			label = new Label(0, row,
					"Gestion des effectifs - Moyennes par CSP");
		} else {
			label = new Label(0, row,
					"Gestion des effectifs - Moyennes par métiers");
		}
		label.setCellFormat(format);
		sheet.mergeCells(0, row, 9, row);
		sheet.addCell(label);

		row++;

		label = new Label(0, row, entete[0]);
		label.setCellFormat(format);
		sheet.mergeCells(0, row, 9, row);
		sheet.addCell(label);

		row++;

		label = new Label(0, row, entete[1]);
		label.setCellFormat(format);
		sheet.mergeCells(0, row, 0, row + 2);
		sheet.addCell(label);

		label = new Label(1, row, entete[2]);
		label.setCellFormat(format);
		sheet.mergeCells(1, row, 1, row + 2);
		sheet.addCell(label);

		label = new Label(2, row, entete[3]);
		label.setCellFormat(format);
		sheet.mergeCells(2, row, 2, row + 2);
		sheet.addCell(label);

		label = new Label(3, row, entete[4]);
		label.setCellFormat(format);
		sheet.mergeCells(3, row, 3, row + 2);
		sheet.addCell(label);

		label = new Label(4, row, entete[5]);
		label.setCellFormat(format);
		sheet.mergeCells(4, row, 9, row);
		sheet.addCell(label);

		row++;

		label = new Label(4, row, entete[6]);
		label.setCellFormat(format);
		sheet.mergeCells(4, row, 5, row);
		sheet.addCell(label);

		label = new Label(6, row, entete[7]);
		label.setCellFormat(format);
		sheet.mergeCells(6, row, 7, row);
		sheet.addCell(label);

		label = new Label(8, row, entete[8]);
		label.setCellFormat(format);
		sheet.mergeCells(8, row, 9, row);
		sheet.addCell(label);

		row++;

		label = new Label(4, row, entete[9]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(5, row, entete[10]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(6, row, entete[9]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(7, row, entete[10]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(8, row, entete[9]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(9, row, entete[10]);
		label.setCellFormat(format);
		sheet.addCell(label);

		row++;

		for (SuiviEffectifBean s : suiviEffectifBeanList) {

			if (s.isFooter()) {
				font = new WritableFont(WritableFont.TIMES, 10,
						WritableFont.BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.RIGHT);
				format.setWrap(true);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
			} else {
				font = new WritableFont(WritableFont.TIMES, 9,
						WritableFont.NO_BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.LEFT);
				format.setWrap(true);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
			}

			// métier
			label = new Label(0, row, s.getLibelle());
			label.setCellFormat(format);
			sheet.addCell(label);

			// effectif
			label = new Label(1, row, s.getEffectif() + "");
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenne d'age
			label = new Label(2, row, s.getMoyenneAge());
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenne d'ancienneté
			label = new Label(3, row, s.getMoyenneAnciennete());
			label.setCellFormat(format);
			sheet.addCell(label);

			// effectif H
			label = new Label(4, row, s.getEffectifH() + "");
			label.setCellFormat(format);
			sheet.addCell(label);

			// effectif F
			label = new Label(5, row, s.getEffectifF() + "");
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenne d'age H
			label = new Label(6, row, s.getMoyenneAgeH());
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenne d'age F
			label = new Label(7, row, s.getMoyenneAgeF());
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenne d'ancieneté H
			label = new Label(8, row, s.getMoyenneAncienneteH());
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenne d'ancienneté F
			label = new Label(9, row, s.getMoyenneAncienneteF());
			label.setCellFormat(format);
			sheet.addCell(label);

			row++;
		}

		sheet.setColumnView(0, 25);
		sheet.setColumnView(1, 10);
		sheet.setColumnView(2, 10);
		sheet.setColumnView(3, 14);
		sheet.setColumnView(4, 8);
		sheet.setColumnView(5, 8);
		sheet.setColumnView(6, 8);
		sheet.setColumnView(7, 8);
		sheet.setColumnView(8, 12);
		sheet.setColumnView(9, 12);

	}

	private void fillSheetNegociationSalarialeParSexe() throws Exception {

		WritableFont font = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		Label label;

		// Fusionne les 3 cellules pour le titre
		sheet.mergeCells(0, 0, 14, 0);
		sheet.mergeCells(0, 1, 14, 1);
		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		label = new Label(0, 0,
				"Négociation salariale - Suivi de la rémunération ");
		label.setCellFormat(format);
		sheet.addCell(label);
		String titre = nomGroupeOrEntreprise;
		if (!nomService.equals("")) {
			titre += " - " + nomService;
		}
		titre += " (" + dateFormat.format(new Date()) + ")";
		label = new Label(0, 1, titre);
		label.setCellFormat(format);
		sheet.addCell(label);

		font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		if (idCSP == 3) {
			label = new Label(
					0,
					3,
					"Coût de la rémunération par CSP - Moyennes par sexe (données annuelles exprimées en €) : "
							+ nomGroupeOrEntreprise
							+ " ("
							+ dateFormat.format(new Date()) + ")");
		} else {
			label = new Label(
					0,
					3,
					"Coût de la rémunération par métiers - Moyennes par sexe (données annuelles exprimées en €) : "
							+ nomGroupeOrEntreprise
							+ " ("
							+ dateFormat.format(new Date()) + ")");
		}
		label.setCellFormat(format);
		sheet.mergeCells(0, 3, 14, 3);
		sheet.addCell(label);

		// intitule
		if (idCSP == 3) {
			label = new Label(0, 4, "CSP");
		} else {
			label = new Label(0, 4, "Métiers");
		}
		label.setCellFormat(format);
		sheet.mergeCells(0, 4, 0, 6);
		sheet.addCell(label);

		// effectif
		label = new Label(1, 4, entete[6]);
		label.setCellFormat(format);
		sheet.mergeCells(1, 4, 2, 5);
		sheet.addCell(label);

		// annee N
		label = new Label(2, 4, entete[3]);
		label.setCellFormat(format);
		sheet.mergeCells(2, 4, 13, 4);
		sheet.addCell(label);

		// salaires bruts annuels

		label = new Label(3, 5, entete[7]);
		label.setCellFormat(format);
		sheet.mergeCells(3, 5, 3, 6);
		sheet.addCell(label);

		// remuneration globale brute annuelle
		label = new Label(6, 5, entete[8]);
		label.setCellFormat(format);
		sheet.mergeCells(6, 5, 6, 6);
		sheet.addCell(label);

		// heures sup annuelles
		label = new Label(9, 5, entete[9]);
		label.setCellFormat(format);
		sheet.mergeCells(9, 5, 9, 6);
		sheet.addCell(label);

		// avantages non assujettis annuels
		label = new Label(12, 5, entete[10]);
		label.setCellFormat(format);
		sheet.mergeCells(12, 5, 12, 6);
		sheet.addCell(label);

		// Moyenne
		int cpt = 4;
		for (int j = 0; j < 4; j++) {
			label = new Label(cpt, 5, entete[11]);
			label.setCellFormat(format);
			sheet.mergeCells(cpt, 5, cpt + 1, 5);
			sheet.addCell(label);
			cpt += 3;
		}

		// H/F
		cpt = 1;
		for (int j = 0; j < 5; j++) {
			label = new Label(cpt, 6, entete[12]);
			label.setCellFormat(format);
			sheet.addCell(label);
			cpt++;
			label = new Label(cpt, 6, entete[13]);
			label.setCellFormat(format);
			sheet.addCell(label);

			cpt += 2;
		}

		int row = 7;
		for (NegociationSalarialeBean bean : rowList) {

			WritableCellFormat format2;
			if (bean.isFooter()) {
				font = new WritableFont(WritableFont.TIMES, 9,
						WritableFont.BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.RIGHT);
				format2 = new WritableCellFormat(font);
				format2.setAlignment(Alignment.RIGHT);
			} else {
				font = new WritableFont(WritableFont.TIMES, 9,
						WritableFont.NO_BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.LEFT);
				format2 = new WritableCellFormat(font);
				format2.setAlignment(Alignment.LEFT);
			}
			format2.setWrap(true);
			format.setWrap(true);

			// libelle
			label = new Label(0, row, bean.getLibelle());
			label.setCellFormat(format);
			sheet.addCell(label);

			// effectifH
			label = new Label(1, row, bean.getEffectifH() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			// effectifF
			label = new Label(2, row, bean.getEffectifF() + "");
			label.setCellFormat(format);
			sheet.addCell(label);

			// salaire brut annuel
			label = new Label(3, row, bean.getSalaireDeBaseBrutAnnuel());
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenneH
			label = new Label(4, row, bean.getSalaireDeBaseBrutAnnuelMoyH());
			label.setCellFormat(format);
			sheet.addCell(label);
			// moyenneF
			label = new Label(5, row, bean.getSalaireDeBaseBrutAnnuelMoyF());
			label.setCellFormat(format);
			sheet.addCell(label);

			// remuneration globale
			label = new Label(6, row, bean.getRemuGlobaleBruteAnnuelle());
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenneH
			label = new Label(7, row, bean.getRemuGlobaleBruteAnnuelleMoyH());
			label.setCellFormat(format);
			sheet.addCell(label);
			// moyenneF
			label = new Label(8, row, bean.getRemuGlobaleBruteAnnuelleMoyF());
			label.setCellFormat(format);
			sheet.addCell(label);

			// heures sup annuelles
			label = new Label(9, row, bean.getHeureSupAnnuelles());
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenneH
			label = new Label(10, row, bean.getHeureSupAnnuellesMoyH());
			label.setCellFormat(format);
			sheet.addCell(label);
			// moyenneF
			label = new Label(11, row, bean.getHeureSupAnnuellesMoyF());
			label.setCellFormat(format);
			sheet.addCell(label);

			// avantages non assujettis annuels
			label = new Label(12, row, bean.getAvantagesNonAssujettisAnnuels());
			label.setCellFormat(format);
			sheet.addCell(label);

			// moyenneH
			label = new Label(13, row,
					bean.getAvantagesNonAssujettisAnnuelsMoyH());
			label.setCellFormat(format);
			sheet.addCell(label);
			// moyenneF
			label = new Label(14, row,
					bean.getAvantagesNonAssujettisAnnuelsMoyF());
			label.setCellFormat(format);
			sheet.addCell(label);

			row++;
		}
		sheet.setColumnView(0, 20);
		sheet.setColumnView(3, 20);
		sheet.setColumnView(6, 20);
		sheet.setColumnView(9, 20);
		sheet.setColumnView(12, 20);

		// moyenne
		sheet.setColumnView(1, 8);
		sheet.setColumnView(2, 8);
		sheet.setColumnView(4, 8);
		sheet.setColumnView(5, 8);
		sheet.setColumnView(7, 8);
		sheet.setColumnView(8, 8);
		sheet.setColumnView(10, 8);
		sheet.setColumnView(11, 8);
		sheet.setColumnView(13, 8);
		sheet.setColumnView(14, 8);

	}

	private void fillSheetProjectionPS(HashMap<String, Integer> mapTotaux)
			throws Exception {

		WritableFont font = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		Label label;

		// Fusionne les 3 cellules pour le titre
		sheet.mergeCells(0, 0, 4, 0);
		sheet.mergeCells(0, 1, 4, 1);
		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		format.setWrap(true);
		label = new Label(0, 0, "Projection plan d'action s\u00E9nior ");
		label.setCellFormat(format);
		sheet.addCell(label);
		String titre = "";
		titre = nomGroupeOrEntreprise;
		if (!nomService.equals("")) {
			titre += " - " + nomService;
		}
		if (!nomMetier.equals("")) {
			titre += " - " + nomMetier;
		}
		titre += " (" + dateFormat.format(new Date()) + ")";
		label = new Label(0, 1, titre);
		label.setCellFormat(format);
		sheet.addCell(label);

		sheet.setColumnView(4, 25);
		label = new Label(0, 3, "Projection plan d'action s\u00E9nior");
		label.setCellFormat(format);
		sheet.mergeCells(0, 3, 4, 3);
		sheet.addCell(label);
		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.LEFT);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		for (int i = 0; i < projectionlistBean.size(); i++) {
			ProjectionPSBean bean = projectionlistBean.get(i);
			label = new Label(0, i + 5, bean.getNom());
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(1, i + 5, bean.getTranche1() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(2, i + 5, bean.getTranche2() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(3, i + 5, bean.getTranche3() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(4, i + 5, bean.getTaux() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
		}
		font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		ResourceBundle rb = ResourceBundle.getBundle("accents");
		label = new Label(0, projectionlistBean.size() + 5,
				rb.getString("Total / annee"));
		label.setCellFormat(format);
		sheet.addCell(label);

		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.RIGHT);
		label = new Label(1, projectionlistBean.size() + 5,
				mapTotaux.get("totalTwoYearAgo") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(2, projectionlistBean.size() + 5,
				mapTotaux.get("totalOneYearAgo") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(3, projectionlistBean.size() + 5,
				mapTotaux.get("totalCurYear") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(4, projectionlistBean.size() + 5,
				mapTotaux.get("total") + "");
		label.setCellFormat(format);
		sheet.addCell(label);

		sheet.setColumnView(1, 12);
		sheet.setColumnView(2, 12);
		sheet.setColumnView(3, 12);
	}

	private void fillSheetArretTravail(HashMap<String, Integer> mapTotaux,
			HashMap<String, String> mapTaux) throws Exception {

		WritableFont font = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		Label label;
		sheet.setColumnView(0, 35);
		// Fusionne les 3 cellules pour le titre
		sheet.mergeCells(0, 0, 3, 0);
		sheet.mergeCells(0, 1, 3, 1);
		sheet.mergeCells(0, 3, 3, 3);
		sheet.mergeCells(0, 4, 3, 4);
		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		label = new Label(0, 0,
				"Nombre de jours d’arr\u00EAts du travail / nature d'accident");
		label.setCellFormat(format);
		sheet.addCell(label);

		String titre = nomGroupeOrEntreprise;
		if (!nomService.equals("")) {
			titre += " - " + nomService;
		}
		if (!nomMetier.equals("")) {
			titre += " - " + nomMetier;
		}
		titre += " (" + dateFormat.format(new Date()) + ")";
		label = new Label(0, 1, titre);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(0, 3,
				"Nombre de jours d’arr\u00EAt du travail / nature");
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(0, 4, "Taux de gravit\u00E9 / ann\u00E9e");
		label.setCellFormat(format);
		sheet.addCell(label);

		setEntete(5);

		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.LEFT);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		int row = 6;
		for (int i = 0; i < arretTravailListBean.size(); i++) {
			ArretTravailBean bean = arretTravailListBean.get(i);
			if (!bean.isBoolTaux()
					&& !bean.isBoolTitre()
					&& !bean.isEspace()
					&& !bean.getNom()
							.equals("Nombre de jours d'arr\u00EAt du travail / ann\u00E9e")) {
				label = new Label(0, row, bean.getNom());
				label.setCellFormat(format);
				sheet.addCell(label);
				label = new Label(1, row, bean.getNbJourTwoYearAgo() + "");
				label.setCellFormat(format);
				sheet.addCell(label);
				label = new Label(2, row, bean.getNbJourOneYearAgo() + "");
				label.setCellFormat(format);
				sheet.addCell(label);
				label = new Label(3, row, bean.getNbJourCurYear() + "");
				label.setCellFormat(format);
				sheet.addCell(label);
				row++;
			}
		}
		font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		label = new Label(0, row,
				"Nombre de jours d'arr\u00EAt du travail / ann\u00E9e");
		label.setCellFormat(format);
		sheet.addCell(label);

		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.RIGHT);

		label = new Label(1, row, mapTotaux.get("nbArretTwoYearAgo") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(2, row, mapTotaux.get("nbArretOneYearAgo") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(3, row, mapTotaux.get("nbArretCurYear") + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		row++;

		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);

		label = new Label(0, row, "Taux de gravit\u00E9 des AT / ann\u00E9e");
		label.setCellFormat(format);
		sheet.addCell(label);

		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.RIGHT);

		label = new Label(1, row, mapTaux.get("tauxGraviteTwoYearAgo"));
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(2, row, mapTaux.get("tauxGraviteOneYearAgo"));
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(3, row, mapTaux.get("tauxGraviteCurYear"));
		label.setCellFormat(format);
		sheet.addCell(label);

		sheet.setColumnView(1, 12);
		sheet.setColumnView(2, 12);
		sheet.setColumnView(3, 12);

	}

	private void fillSheetRemuneration() throws Exception {

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.applyPattern("0.00");

		WritableFont font = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		// Fusionne les 22 cellules pour le titre
		sheet.mergeCells(0, 0, 22, 0);
		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		format.setWrap(true);
		Label label = new Label(0, 0, "TABLEAU DE SUIVI DE LA REMUNERATION");
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(11, 3, "DONNEES BRUTES ANNUELLES EXPRIMEES EN EUROS");
		label.setCellFormat(format);
		sheet.mergeCells(11, 3, 18, 3);
		sheet.addCell(label);

		sheet.setColumnView(0, 25);

		font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		format.setWrap(true);

		for (int i = 0; i < entete.length; i++) {
			label = new Label(i, 4, entete[i]);
			label.setCellFormat(format);
			sheet.addCell(label);
		}

		int y = 5;
		for (int i = 0; i < remunerationBeanList.size(); i++) {
			RemunerationBean bean = remunerationBeanList.get(i);

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
					if (lrr.getActualisation() != null
							&& Double.valueOf(lrr.getActualisation()) != 0.0) {
						montantc += Double.valueOf(lrr.getActualisation());
					} else {
						montantc += Double.valueOf(lrr.getMontant());
					}
				}
				if (revComp.getType().equals("prime_fixe")) {
					if (lrr.getActualisation() != null
							&& Double.valueOf(lrr.getActualisation()) != 0.0) {
						montantpf += Double.valueOf(lrr.getActualisation());
					} else {
						montantpf += Double.valueOf(lrr.getMontant());
					}
				}
				if (revComp.getType().equals("prime_variable")) {
					if (lrr.getActualisation() != null
							&& Double.valueOf(lrr.getActualisation()) != 0.0) {
						montantpv += Double.valueOf(lrr.getActualisation());
					} else {
						montantpv += Double.valueOf(lrr.getMontant());
					}
				}
				if (revComp.getType().equals("avantage_assujetti")) {
					if (lrr.getActualisation() != null
							&& Double.valueOf(lrr.getActualisation()) != 0.0) {
						montantaa += Double.valueOf(lrr.getActualisation());
					} else {
						montantaa += Double.valueOf(lrr.getMontant());
					}
				}
				if (revComp.getType().equals("avantage_non_assujetti")) {
					if (lrr.getActualisation() != null
							&& Double.valueOf(lrr.getActualisation()) != 0.0) {
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
			double augmI = 0;
			double augmC = 0;
			double hs25 = 0;
			double hs50 = 0;

			if (bean.getSalaireDeBaseActualisation() != null
					&& bean.getSalaireDeBaseActualisation() != 0) {
				salaireDeBase = bean.getSalaireDeBaseActualisation();
			} else {
				salaireDeBase = bean.getSalaireDeBase();
			}

			if (bean.getAugmentationIndividuelleActualisation() != null
					&& bean.getAugmentationIndividuelleActualisation() != 0) {
				augmI = bean.getAugmentationIndividuelleActualisation();
			} else {
				augmI = bean.getAugmentationIndividuelle();
			}

			if (bean.getAugmentationCollectiveActualisation() != null
					&& bean.getAugmentationCollectiveActualisation() != 0) {
				augmC = bean.getAugmentationCollectiveActualisation();
			} else {
				augmC = bean.getAugmentationCollective();
			}

			if (bean.getHeuresSup25Actualisation() != null
					&& bean.getHeuresSup25Actualisation() != 0) {
				hs25 = bean.getHeuresSup25Actualisation();
			} else {
				hs25 = bean.getHeuresSup25();
			}

			if (bean.getHeuresSup50Actualisation() != null
					&& bean.getHeuresSup50Actualisation() != 0) {
				hs50 = bean.getHeuresSup50Actualisation();
			} else {
				hs50 = bean.getHeuresSup50();
			}

			totalBaseBruteAnnuelle = salaireDeBase + augmI + augmC + hs25
					+ hs50;

			double remunerationBruteAnnuelle = 0;
			remunerationBruteAnnuelle = totalBaseBruteAnnuelle + montantc
					+ montantpf + montantpv + montantaa;
			double totalBrutAnnuel = 0;
			totalBrutAnnuel = remunerationBruteAnnuelle + montantana;

			SalarieServiceImpl salserv = new SalarieServiceImpl();
			SalarieBean sal = new SalarieBean();
			sal = salserv.getSalarieBeanById(bean.getIdSalarie());

			// Bean classic
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.LEFT);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			format.setWrap(true);

			label = new Label(0, y, bean.getAnnee() + "");
			label.setCellFormat(format);
			sheet.setColumnView(0, 8);
			sheet.addCell(label);

			label = new Label(1, y, sal.getNom());
			label.setCellFormat(format);
			sheet.setColumnView(1, 20);
			sheet.addCell(label);

			label = new Label(2, y, sal.getPrenom());
			label.setCellFormat(format);
			sheet.setColumnView(2, 15);
			sheet.addCell(label);

			label = new Label(3, y, bean.getNomMetier());
			label.setCellFormat(format);
			sheet.setColumnView(3, 30);
			sheet.addCell(label);

			label = new Label(4, y, bean.getNomCsp());
			label.setCellFormat(format);
			sheet.setColumnView(4, 30);
			sheet.addCell(label);

			label = new Label(5, y, bean.getEchelon());
			label.setCellFormat(format);
			sheet.setColumnView(5, 10);
			sheet.addCell(label);

			label = new Label(6, y, bean.getNiveau());
			label.setCellFormat(format);
			sheet.setColumnView(6, 10);
			sheet.addCell(label);

			label = new Label(7, y, bean.getCoefficient());
			label.setCellFormat(format);
			sheet.setColumnView(7, 8);
			sheet.addCell(label);

			label = new Label(8, y, bean.getHoraireMensuelTravaille());
			format = new WritableCellFormat(font);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			format.setWrap(true);
			format.setAlignment(Alignment.LEFT);
			label.setCellFormat(format);
			sheet.setColumnView(8, 8);
			sheet.addCell(label);

			label = new Label(9, y, df.format(bean.getTauxHoraireBrut()));
			label.setCellFormat(format);
			sheet.setColumnView(9, 8);
			sheet.addCell(label);

			label = new Label(10, y, df.format(bean.getSalaireMensuelBrut()));
			label.setCellFormat(format);
			sheet.setColumnView(10, 10);
			sheet.addCell(label);

			label = new Label(11, y, df.format(bean
					.getAugmentationCollectiveActualisation()));
			label.setCellFormat(format);
			sheet.setColumnView(11, 14);
			sheet.addCell(label);

			label = new Label(12, y, df.format(bean
					.getAugmentationIndividuelleActualisation()));
			label.setCellFormat(format);
			sheet.setColumnView(12, 14);
			sheet.addCell(label);

			label = new Label(13, y, df.format((bean
					.getHeuresSup25Actualisation() + bean
					.getHeuresSup50Actualisation())));
			label.setCellFormat(format);
			sheet.setColumnView(13, 10);
			sheet.addCell(label);

			label = new Label(14, y, df.format(totalBaseBruteAnnuelle));
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD);

			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.RIGHT);
			label.setCellFormat(format);
			sheet.setColumnView(14, 12);
			sheet.addCell(label);
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.LEFT);

			label = new Label(15, y, df.format(montantc));
			label.setCellFormat(format);
			sheet.setColumnView(15, 15);
			sheet.addCell(label);

			label = new Label(16, y, df.format(montantpf));
			label.setCellFormat(format);
			sheet.setColumnView(16, 10);
			sheet.addCell(label);

			label = new Label(17, y, df.format(montantpv));
			label.setCellFormat(format);
			sheet.setColumnView(17, 10);
			sheet.addCell(label);

			label = new Label(18, y, df.format(montantaa));
			label.setCellFormat(format);
			sheet.setColumnView(18, 10);
			sheet.addCell(label);

			label = new Label(19, y, df.format(remunerationBruteAnnuelle));
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.RIGHT);
			label.setCellFormat(format);
			sheet.setColumnView(19, 15);
			sheet.addCell(label);
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.LEFT);

			label = new Label(20, y, df.format(montantana));
			label.setCellFormat(format);
			sheet.setColumnView(20, 10);
			sheet.addCell(label);

			label = new Label(21, y, df.format(totalBrutAnnuel));
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.RIGHT);
			label.setCellFormat(format);
			sheet.setColumnView(21, 12);
			sheet.addCell(label);
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.LEFT);

			label = new Label(22, y, df.format(montantfp));
			label.setCellFormat(format);
			sheet.setColumnView(22, 15);
			sheet.addCell(label);

			y++;
		}
	}

	private void fillSheetTurnOver() throws Exception {

		WritableFont font = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		// Fusionne les 3 cellules pour le titre
		sheet.mergeCells(0, 0, 6, 0);
		sheet.mergeCells(0, 1, 6, 1);
		font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		Label label = new Label(0, 0, "Turn Over ");
		label.setCellFormat(format);
		sheet.addCell(label);

		String titre = nomGroupeOrEntreprise;
		if (!nomService.equals("")) {
			titre += " - " + nomService;
		}
		titre += " (" + dateFormat.format(new Date()) + ")";
		label = new Label(0, 1, titre);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(0, 3, "Entr\u00E9es - Sorties / ann\u00E9e ");
		label.setCellFormat(format);
		sheet.mergeCells(0, 3, 6, 3);
		sheet.addCell(label);
		label = new Label(0, 4, "Taux de turn over");
		label.setCellFormat(format);
		sheet.mergeCells(0, 4, 6, 4);
		sheet.addCell(label);

		// On fusionne les cellules
		sheet.mergeCells(0, 5, 0, 6);
		sheet.mergeCells(1, 5, 2, 5);
		sheet.mergeCells(3, 5, 4, 5);
		sheet.mergeCells(5, 5, 6, 5);

		sheet.setColumnView(0, 30);

		font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		label = new Label(0, 5, "Entreprise / Service / M\u00E9tier");
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(1, 5, entete[1]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(3, 5, entete[2]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(5, 5, entete[3]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(1, 6, "Entr\u00E9es");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(2, 6, "Sorties");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(3, 6, "Entr\u00E9es");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(4, 6, "Sorties");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(5, 6, "Entr\u00E9es");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(6, 6, "Sorties");
		label.setCellFormat(format);
		sheet.addCell(label);

		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.LEFT);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		int y = 7;
		for (int i = 0; i < turnOverListBean.size(); i++) {
			TurnOverBean bean = turnOverListBean.get(i);
			if (bean.isFooter()) {
				font = new WritableFont(WritableFont.TIMES, 10,
						WritableFont.BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.RIGHT);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
				if (bean.getTauxN() != null) {
					sheet.mergeCells(1, y, 2, y);
					sheet.mergeCells(3, y, 4, y);
					sheet.mergeCells(5, y, 6, y);

					format = new WritableCellFormat(font);
					format.setAlignment(Alignment.CENTRE);
					label = new Label(0, y, bean.getLibelle());
					label.setCellFormat(format);
					sheet.addCell(label);

					format = new WritableCellFormat(font);
					format.setAlignment(Alignment.RIGHT);
					label = new Label(1, y, "");
					label.setCellFormat(format);
					sheet.addCell(label);

					label = new Label(3, y, bean.getTauxN1());
					label.setCellFormat(format);
					sheet.addCell(label);

					label = new Label(5, y, bean.getTauxN());
					label.setCellFormat(format);
					sheet.addCell(label);

					return;
				}
			}
			if (bean.isFooter() && bean.getEntreesN() != null) {
				// Total / annee
				font = new WritableFont(WritableFont.TIMES, 10,
						WritableFont.BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
			} else {
				// Bean classic
				font = new WritableFont(WritableFont.TIMES, 9,
						WritableFont.NO_BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.LEFT);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
			}
			label = new Label(0, y, bean.getLibelle());
			label.setCellFormat(format);
			sheet.addCell(label);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.RIGHT);
			label = new Label(1, y, bean.getEntreesN2() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(2, y, bean.getSortiesN2() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(3, y, bean.getEntreesN1() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(4, y, bean.getSortiesN1() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(5, y, bean.getEntreesN() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			label = new Label(6, y, bean.getSortiesN() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			y++;
		}
	}

	private ParcoursBean getLastParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		Collections.sort(parcourList);
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

	public static int calculer(Date d) {
		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(d);
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
		dateOfBirth.add(Calendar.YEAR, age);
		if (today.before(dateOfBirth)) {
			age--;
		}
		return age;
	}

	private int nbreHeureFormation(SalarieBean salarie) {
		List<FormationBean> l = salarie.getFormationBeanList();
		int res = 0;
		for (int i = 0; i < l.size(); i++) {
			res += l.get(i).getVolumeHoraire().intValue();
		}
		return res;
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

	private String getAnciennete(SalarieBean salarie) {

		String anciennete = "";

		if (getFirstParcours(salarie) != null
				&& getLastParcours(salarie) != null) {
			ParcoursBean parcourDeb = getFirstParcours(salarie);

			ParcoursBean parcourFin = getLastParcours(salarie);
			Calendar d1 = new GregorianCalendar();
			Calendar d2 = new GregorianCalendar();
			d1.setTime(parcourDeb.getDebutFonction());
			if (parcourFin.getFinFonction() != null
					&& (parcourFin.getFinFonction().before(
							d2.getInstance().getTime()) || parcourFin
							.getFinFonction().equals(new Date()))) {
				d2.setTime(parcourFin.getFinFonction());
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
		} else
			return "";
	}

	private void fillSheetFiltre() throws Exception {
		int maxSizeNom = 20;
		int maxSizeNomEntreprise = 20;
		int maxSizePrenom = 15;
		int maxSizeSexe = 10;
		int maxSizeDateNaissance = 25;
		int maxSizeAge = 10;
		int maxSizeAdresse = 50;
		int maxSizeTelFixe = 18;
		int maxSizeTelPortable = 18;
		int maxSizePAPNom = 20;
		int maxSizePAPPrenom = 15;
		int maxSizePAPTel = 30;
		int maxSizeEntreeEntreprise = 25;
		int maxSizeSortieEntreprise = 25;
		int maxSizeAnciennete = 15;
		int maxSizeTypeContrat = 25;
		int maxSizeService = 10;
		int maxSizePosteOccupe = 30;
		int maxSizeCSP = 20;

		sheet.setColumnView(0, 20);
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		WritableFont font2 = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format2 = new WritableCellFormat(font2);
		format2.setAlignment(Alignment.LEFT);
		format2.setWrap(true);
		format2.setVerticalAlignment(VerticalAlignment.CENTRE);

		Label label = new Label(0, 0,
				"R\u00E9sultats filtre multi-crit\u00e8res du "
						+ dateFormat.format(new Date()));

		label.setCellFormat(format);
		sheet.addCell(label);

		int row = 3;

		sheet.mergeCells(1, row, 4, row);
		font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		label = new Label(1, row, "Filtre(s) utilis\u00E9(s)");
		label.setCellFormat(format);
		sheet.addCell(label);
		row++;
		for (int i = 0; i < filtres.size(); i++) {
			Filtre f = filtres.get(i);
			sheet.mergeCells(1, row, 2, row);
			sheet.mergeCells(3, row, 4, row);
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			label = new Label(1, row, f.getFiltre());
			label.setCellFormat(format);
			sheet.addCell(label);
			font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			label = new Label(3, row, f.getValue().toString());
			label.setCellFormat(format);
			sheet.addCell(label);
			row++;
		}
		row++;

		sheet.mergeCells(0, 0, 20, 0);
		sheet.mergeCells(0, 1, 20, 1);
		sheet.mergeCells(0, 2, 20, 2);
		sheet.mergeCells(9, row, 11, row);

		sheet.mergeCells(0, row, 0, row + 1);
		sheet.mergeCells(1, row, 1, row + 1);
		sheet.mergeCells(2, row, 2, row + 1);
		sheet.mergeCells(3, row, 3, row + 1);
		sheet.mergeCells(4, row, 4, row + 1);
		sheet.mergeCells(5, row, 5, row + 1);
		sheet.mergeCells(6, row, 6, row + 1);
		sheet.mergeCells(7, row, 7, row + 1);
		sheet.mergeCells(8, row, 8, row + 1);
		sheet.mergeCells(12, row, 12, row + 1);
		sheet.mergeCells(13, row, 13, row + 1);
		sheet.mergeCells(14, row, 14, row + 1);
		sheet.mergeCells(15, row, 15, row + 1);
		sheet.mergeCells(16, row, 16, row + 1);
		sheet.mergeCells(17, row, 17, row + 1);
		sheet.mergeCells(18, row, 18, row + 1);
		sheet.mergeCells(19, row, 19, row + 1);
		sheet.mergeCells(20, row, 20, row + 1);

		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		setEntete(row);
		row++;

		Label labelPAPNom = new Label(9, row, "Nom");
		sheet.addCell(labelPAPNom);

		Label labelPAPPrenom = new Label(10, row, "Prenom");
		sheet.addCell(labelPAPPrenom);

		Label labelPAPTel = new Label(11, row, "Tél.");
		sheet.addCell(labelPAPTel);

		row++;

		for (int i = 0; i < filtreListBean.size(); i++) {
			int j = 0;
			SalarieBeanExport salarie = filtreListBean.get(i);
			EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
			SalarieServiceImpl salServ = new SalarieServiceImpl();

			// Entreprise
			label = new Label(j, row, entServ.getEntrepriseBeanById(
					salServ.getSalarieBeanById(salarie.getId())
							.getIdEntrepriseSelected()).getNom());
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeNomEntreprise < label.getContents().length()) {
				maxSizeNomEntreprise = label.getContents().length();
			}
			j++;

			// Nom
			label = new Label(j, row, salarie.getNom());
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeNom < label.getContents().length()) {
				maxSizeNom = label.getContents().length();
			}

			// Prénom
			label = new Label(j + 1, row, salarie.getPrenom());
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizePrenom < label.getContents().length()) {
				maxSizePrenom = label.getContents().length();
			}

			// Sexe
			String civilite = "";
			if (salarie.getCivilite().equals("Monsieur")) {
				civilite = "H";
			} else {
				civilite = "F";
			}
			label = new Label(j + 2, row, civilite);
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeSexe < label.getContents().length()) {
				maxSizeSexe = label.getContents().length();
			}

			// Date de naisance
			String dat = dateFormat.format(salarie.getDateNaissance());
			label = new Label(j + 3, row, dat);
			// sheet.setColumnView(10, 12);
			label.setCellFormat(format);
			// sheet.setColumnView(2, 20);
			sheet.addCell(label);
			if (maxSizeDateNaissance < label.getContents().length()) {
				maxSizeDateNaissance = label.getContents().length();
			}

			// Age
			label = new Label(j + 4, row, calculer(salarie.getDateNaissance())
					+ "");
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeAge < label.getContents().length()) {
				maxSizeAge = label.getContents().length();
			}

			// Adresse
			label = new Label(j + 5, row, salarie.getAdresse() + "");
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeAdresse < label.getContents().length()) {
				maxSizeAdresse = label.getContents().length();
			}

			// tel fixe
			label = new Label(j + 6, row, salarie.getTelephone() + "");
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeTelFixe < label.getContents().length()) {
				maxSizeTelFixe = label.getContents().length();
			}

			// tel port
			if (salarie.getTelephonePortable() != null)
				label = new Label(j + 7, row, salarie.getTelephonePortable()
						+ "");
			else
				label = new Label(j + 7, row, "");
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeTelPortable < label.getContents().length()) {
				maxSizeTelPortable = label.getContents().length();
			}

			// Personne a prevenir - Nom
			if (salarie.getPersonneAPrevenirNom() != null) {
				label = new Label(j + 8, row, salarie.getPersonneAPrevenirNom()
						+ "");
			} else {
				label = new Label(j + 8, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizePAPNom < label.getContents().length()) {
				maxSizePAPNom = label.getContents().length();
			}

			// Personne a prevenir - Prenom
			if (salarie.getPersonneAPrevenirPrenom() != null) {
				label = new Label(j + 9, row,
						salarie.getPersonneAPrevenirPrenom() + "");
			} else {
				label = new Label(j + 9, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizePAPPrenom < label.getContents().length()) {
				maxSizePAPPrenom = label.getContents().length();
			}

			// Personne a prevenir - Tel
			if (salarie.getPersonneAPrevenirTelephone() != null) {
				label = new Label(j + 10, row,
						salarie.getPersonneAPrevenirTelephone() + "");
			} else {
				label = new Label(j + 10, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizePAPTel < label.getContents().length()) {
				maxSizePAPTel = label.getContents().length();
			}

			// Date entree Entreprise
			String dateEntree = getDateEntree(salarie);
			label = new Label(j + 11, row, dateEntree);
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeEntreeEntreprise < label.getContents().length()) {
				maxSizeEntreeEntreprise = label.getContents().length();
			}

			// Date sortie entreprise
			String sortie = getDateSortie(salarie);
			label = new Label(j + 12, row, sortie);
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeSortieEntreprise < label.getContents().length()) {
				maxSizeSortieEntreprise = label.getContents().length();
			}

			// Ancienneté
			label = new Label(j + 13, row, getAnciennete(salarie) + "");
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeAnciennete < label.getContents().length()) {
				maxSizeAnciennete = label.getContents().length();
			}

			// Motif rupture contrat
			/*
			 * if (getLastParcours(salarie) != null) label = new Label(j + 14,
			 * row, getLastParcours(salarie) .getNomMotifRuptureContrat()); else
			 * label = new Label(j + 14, row, ""); label.setCellFormat(format2);
			 * sheet.addCell(label); if (maxSizeAnciennete <
			 * label.getContents().length()) { maxSizeAnciennete =
			 * label.getContents().length(); }
			 */

			if (getLastParcours(salarie) != null) {
				// Contrat
				label = new Label(j + 15, row, getLastParcours(salarie)
						.getNomTypeContrat());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizeTypeContrat < label.getContents().length()) {
					maxSizeTypeContrat = label.getContents().length();
				}

			} else {
				// Contrat
				label = new Label(j + 15, row, "");
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizeTypeContrat < label.getContents().length()) {
					maxSizeTypeContrat = label.getContents().length();
				}
			}

			// Poste occupé
			ParcoursBean pb = getLastParcours(salarie);
			if (pb != null) {
				label = new Label(j + 16, row, pb.getNomMetier());
			} else {
				label = new Label(j + 16, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizePosteOccupe < label.getContents().length()) {
				maxSizePosteOccupe = label.getContents().length();
			}

			// Service
			if (salarie.getService() != null) {
				label = new Label(j + 17, row, salarie.getService() + "");
			} else {
				label = new Label(j + 17, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeService < label.getContents().length()) {
				maxSizeService = label.getContents().length();
			}

			// ETP
			format2 = new WritableCellFormat(font);
			format2.setAlignment(Alignment.RIGHT);
			format2.setVerticalAlignment(VerticalAlignment.CENTRE);
			if (pb != null) {
				label = new Label(j + 18, row, pb.getEquivalenceTempsPlein()
						+ "%");
			} else {
				label = new Label(j + 18, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeAnciennete < label.getContents().length()) {
				maxSizeAnciennete = label.getContents().length();
			}
			format2 = new WritableCellFormat(font);
			format2.setAlignment(Alignment.LEFT);
			format2.setVerticalAlignment(VerticalAlignment.CENTRE);

			// CSP
			if (pb != null) {
				label = new Label(j + 19, row, pb.getNomTypeStatut() + "");
			} else {
				label = new Label(j + 19, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeCSP < label.getContents().length()) {
				maxSizeCSP = label.getContents().length();
			}

			row++;
		}

		int j = 0;
		sheet.setColumnView(j, maxSizeNomEntreprise);
		j++;
		sheet.setColumnView(j, maxSizeNom);
		sheet.setColumnView(j + 1, maxSizePrenom);
		sheet.setColumnView(j + 2, maxSizeSexe);
		sheet.setColumnView(j + 3, maxSizeDateNaissance);
		sheet.setColumnView(j + 4, maxSizeAge);
		sheet.setColumnView(j + 5, maxSizeAdresse);
		sheet.setColumnView(j + 6, maxSizeTelFixe);
		sheet.setColumnView(j + 7, maxSizeTelPortable);
		sheet.setColumnView(j + 8, maxSizePAPNom);
		sheet.setColumnView(j + 9, maxSizePAPPrenom);
		sheet.setColumnView(j + 10, maxSizePAPTel);
		sheet.setColumnView(j + 11, maxSizeEntreeEntreprise);
		sheet.setColumnView(j + 12, maxSizeSortieEntreprise);
		sheet.setColumnView(j + 13, maxSizeAnciennete);
		sheet.setColumnView(j + 14, 20);
		sheet.setColumnView(j + 15, maxSizeTypeContrat);
		sheet.setColumnView(j + 16, maxSizePosteOccupe);
		sheet.setColumnView(j + 17, maxSizeService);
		sheet.setColumnView(j + 18, 12);
		sheet.setColumnView(j + 19, maxSizeCSP);

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
			if (pb.getFinFonction() != null
			/* && !pb.getFinFonction().equals(UtilsDate.FIN_NULL) */) {
				return dateFormat.format(pb.getFinFonction());
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	private void fillSheetListeDuPersonnel() throws Exception {
		int maxSizeNom = 20;
		int maxSizeNomEntreprise = 20;
		int maxSizePrenom = 15;
		int maxSizeSexe = 10;
		int maxSizeDateNaissance = 25;
		int maxSizeAge = 10;
		int maxSizeAdresse = 50;
		int maxSizeTelFixe = 18;
		int maxSizeTelPortable = 18;
		int maxSizePAPNom = 20;
		int maxSizePAPPrenom = 15;
		int maxSizePAPTel = 30;
		int maxSizeEntreeEntreprise = 25;
		int maxSizeSortieEntreprise = 25;
		int maxSizeAnciennete = 18;
		int maxSizeTypeContrat = 25;
		int maxSizeService = 10;
		int maxSizePosteOccupe = 30;
		int maxSizeCSP = 20;

		sheet.setColumnView(0, 20);
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		WritableFont font2 = new WritableFont(WritableFont.TIMES, 9,
				WritableFont.NO_BOLD);
		WritableCellFormat format2 = new WritableCellFormat(font2);
		format2.setAlignment(Alignment.LEFT);
		format2.setWrap(true);
		format2.setVerticalAlignment(VerticalAlignment.CENTRE);

		if (idEntrepriseSelected == 0 || idEntrepriseSelected == -1) {
			sheet.mergeCells(0, 0, 19, 0);
			sheet.mergeCells(0, 1, 19, 1);
			sheet.mergeCells(0, 2, 19, 2);
			sheet.mergeCells(9, 4, 11, 4);

			sheet.mergeCells(0, 4, 0, 5);
			sheet.mergeCells(1, 4, 1, 5);
			sheet.mergeCells(2, 4, 2, 5);
			sheet.mergeCells(3, 4, 3, 5);
			sheet.mergeCells(4, 4, 4, 5);
			sheet.mergeCells(5, 4, 5, 5);
			sheet.mergeCells(6, 4, 6, 5);
			sheet.mergeCells(7, 4, 7, 5);
			sheet.mergeCells(8, 4, 8, 5);
			sheet.mergeCells(12, 4, 12, 5);
			sheet.mergeCells(13, 4, 13, 5);
			sheet.mergeCells(14, 4, 14, 5);
			sheet.mergeCells(15, 4, 15, 5);
			sheet.mergeCells(16, 4, 16, 5);
			sheet.mergeCells(17, 4, 17, 5);
			sheet.mergeCells(18, 4, 18, 5);
			sheet.mergeCells(19, 4, 19, 5);
		} else {
			sheet.mergeCells(0, 0, 18, 0);
			sheet.mergeCells(0, 1, 18, 1);
			sheet.mergeCells(0, 2, 18, 2);
			sheet.mergeCells(8, 4, 10, 4);

			sheet.mergeCells(0, 4, 0, 5);
			sheet.mergeCells(1, 4, 1, 5);
			sheet.mergeCells(2, 4, 2, 5);
			sheet.mergeCells(3, 4, 3, 5);
			sheet.mergeCells(4, 4, 4, 5);
			sheet.mergeCells(5, 4, 5, 5);
			sheet.mergeCells(6, 4, 6, 5);
			sheet.mergeCells(7, 4, 7, 5);
			sheet.mergeCells(11, 4, 11, 5);
			sheet.mergeCells(12, 4, 12, 5);
			sheet.mergeCells(13, 4, 13, 5);
			sheet.mergeCells(14, 4, 14, 5);
			sheet.mergeCells(15, 4, 15, 5);
			sheet.mergeCells(16, 4, 16, 5);
			sheet.mergeCells(17, 4, 17, 5);
			sheet.mergeCells(18, 4, 18, 5);
		}

		Label label = new Label(0, 0, "Suivi administratif du personnel ("
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
		filtre += (lettre != null) ? " - A partir de la lettre " + lettre : "";

		Label label2 = new Label(0, 2, "Filtre : " + filtre);

		Label label3 = new Label(
				0,
				1,
				"Objectif : gestion des données administratives du personnel / suivi du registre unique du personnel");

		label.setCellFormat(format);
		sheet.addCell(label);
		label2.setCellFormat(format);
		sheet.addCell(label2);
		label3.setCellFormat(format);
		sheet.addCell(label3);
		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		int row = 5;

		if (idEntrepriseSelected == 0 || idEntrepriseSelected == -1) {
			Label labelPAPNom = new Label(9, row, "Nom");
			sheet.addCell(labelPAPNom);

			Label labelPAPPrenom = new Label(10, row, "Prenom");
			sheet.addCell(labelPAPPrenom);

			Label labelPAPTel = new Label(11, row, "Tél.");
			sheet.addCell(labelPAPTel);
		} else {
			Label labelPAPNom = new Label(8, row, "Nom");
			sheet.addCell(labelPAPNom);

			Label labelPAPPrenom = new Label(9, row, "Prenom");
			sheet.addCell(labelPAPPrenom);

			Label labelPAPTel = new Label(10, row, "Tél.");
			sheet.addCell(labelPAPTel);
		}

		row = 6;

		for (int i = 0; i < salarieListBean.size(); i++) {
			int j = 0;
			SalarieBeanExport salarie = salarieListBean.get(i);

			// Entreprise
			if (idEntrepriseSelected == 0 || idEntrepriseSelected == -1) {
				label = new Label(j, row, salarie.getNomEntreprise());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizeNomEntreprise < label.getContents().length()) {
					maxSizeNomEntreprise = label.getContents().length();
				}
				j++;
			}

			// Nom
			label = new Label(j, row, salarie.getNom());
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeNom < label.getContents().length()) {
				maxSizeNom = label.getContents().length();
			}

			// Prénom
			label = new Label(j + 1, row, salarie.getPrenom());
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizePrenom < label.getContents().length()) {
				maxSizePrenom = label.getContents().length();
			}

			// Sexe
			String civilite = "";
			if (salarie.getCivilite().equals("Monsieur")) {
				civilite = "H";
			} else {
				civilite = "F";
			}
			label = new Label(j + 2, row, civilite);
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeSexe < label.getContents().length()) {
				maxSizeSexe = label.getContents().length();
			}

			// Date de naisance
			String dat = dateFormat.format(salarie.getDateNaissance());
			label = new Label(j + 3, row, dat);
			// sheet.setColumnView(10, 12);
			label.setCellFormat(format);
			// sheet.setColumnView(2, 20);
			sheet.addCell(label);
			if (maxSizeDateNaissance < label.getContents().length()) {
				maxSizeDateNaissance = label.getContents().length();
			}

			// Age
			label = new Label(j + 4, row, calculer(salarie.getDateNaissance())
					+ "");
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeAge < label.getContents().length()) {
				maxSizeAge = label.getContents().length();
			}

			// Adresse
			label = new Label(j + 5, row, salarie.getAdresse() + "");
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeAdresse < label.getContents().length()) {
				maxSizeAdresse = label.getContents().length();
			}

			// tel fixe
			label = new Label(j + 6, row, salarie.getTelephone() + "");
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeTelFixe < label.getContents().length()) {
				maxSizeTelFixe = label.getContents().length();
			}

			// tel port
			if (salarie.getTelephonePortable() != null) {
				label = new Label(j + 7, row, salarie.getTelephonePortable()
						+ "");
			} else {
				label = new Label(j + 7, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeTelPortable < label.getContents().length()) {
				maxSizeTelPortable = label.getContents().length();
			}

			// Personne a prevenir - Nom
			if (salarie.getPersonneAPrevenirNom() != null) {
				label = new Label(j + 8, row, salarie.getPersonneAPrevenirNom()
						+ "");
			} else {
				label = new Label(j + 8, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizePAPNom < label.getContents().length()) {
				maxSizePAPNom = label.getContents().length();
			}

			// Personne a prevenir - Prenom
			if (salarie.getPersonneAPrevenirPrenom() != null) {
				label = new Label(j + 9, row,
						salarie.getPersonneAPrevenirPrenom() + "");
			} else {
				label = new Label(j + 9, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizePAPPrenom < label.getContents().length()) {
				maxSizePAPPrenom = label.getContents().length();
			}

			// Personne a prevenir - Tel
			if (salarie.getPersonneAPrevenirTelephone() != null) {
				label = new Label(j + 10, row,
						salarie.getPersonneAPrevenirTelephone() + "");
			} else {
				label = new Label(j + 10, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizePAPTel < label.getContents().length()) {
				maxSizePAPTel = label.getContents().length();
			}

			// Date entree Entreprise
			String dateEntree = getDateEntree(salarie);
			label = new Label(j + 11, row, dateEntree);
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeEntreeEntreprise < label.getContents().length()) {
				maxSizeEntreeEntreprise = label.getContents().length();
			}

			// Date sortie entreprise
			String sortie = getDateSortie(salarie);
			label = new Label(j + 12, row, sortie);
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeSortieEntreprise < label.getContents().length()) {
				maxSizeSortieEntreprise = label.getContents().length();
			}

			// Ancienneté
			label = new Label(j + 13, row, getAnciennete(salarie) + "");
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeAnciennete < label.getContents().length()) {
				maxSizeAnciennete = label.getContents().length();
			}

			// Motif rupture contrat
			/*
			 * if (getLastParcours(salarie) != null) label = new Label(j + 14,
			 * row, getLastParcours(salarie) .getNomMotifRuptureContrat()); else
			 * label = new Label(j + 14, row, ""); label.setCellFormat(format2);
			 * sheet.addCell(label); if (maxSizeAnciennete <
			 * label.getContents().length()) { maxSizeAnciennete =
			 * label.getContents().length(); }
			 */

			if (getLastParcours(salarie) != null) {
				// Contrat
				label = new Label(j + 14, row, getLastParcours(salarie)
						.getNomTypeContrat());
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizeTypeContrat < label.getContents().length()) {
					maxSizeTypeContrat = label.getContents().length();
				}

			} else {
				// Contrat
				label = new Label(j + 14, row, "");
				label.setCellFormat(format2);
				sheet.addCell(label);
				if (maxSizeTypeContrat < label.getContents().length()) {
					maxSizeTypeContrat = label.getContents().length();
				}
			}

			// Poste occupé
			ParcoursBean pb = getLastParcours(salarie);
			if (pb != null) {
				label = new Label(j + 15, row, pb.getNomMetier());
			} else {
				label = new Label(j + 15, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizePosteOccupe < label.getContents().length()) {
				maxSizePosteOccupe = label.getContents().length();
			}

			// Service
			if (salarie.getService() != null) {
				label = new Label(j + 16, row, salarie.getService() + "");
			} else {
				label = new Label(j + 16, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeService < label.getContents().length()) {
				maxSizeService = label.getContents().length();
			}

			// ETP
			format2 = new WritableCellFormat(font);
			format2.setAlignment(Alignment.RIGHT);
			format2.setVerticalAlignment(VerticalAlignment.CENTRE);
			if (pb != null) {
				label = new Label(j + 17, row, pb.getEquivalenceTempsPlein()
						+ "%");
			} else {
				label = new Label(j + 17, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			format2 = new WritableCellFormat(font);
			format2.setAlignment(Alignment.LEFT);
			format2.setVerticalAlignment(VerticalAlignment.CENTRE);

			// CSP
			if (pb != null) {
				label = new Label(j + 18, row, pb.getNomTypeStatut() + "");
			} else {
				label = new Label(j + 18, row, "");
			}
			label.setCellFormat(format2);
			sheet.addCell(label);
			if (maxSizeCSP < label.getContents().length()) {
				maxSizeCSP = label.getContents().length();
			}

			row++;
		}

		int j = 0;
		if (idEntrepriseSelected == 0 || idEntrepriseSelected == -1) {
			sheet.setColumnView(j, maxSizeNomEntreprise);
			j++;
		}
		sheet.setColumnView(j, maxSizeNom);
		sheet.setColumnView(j + 1, maxSizePrenom);
		sheet.setColumnView(j + 2, maxSizeSexe);
		sheet.setColumnView(j + 3, maxSizeDateNaissance);
		sheet.setColumnView(j + 4, maxSizeAge);
		sheet.setColumnView(j + 5, maxSizeAdresse);
		sheet.setColumnView(j + 6, maxSizeTelFixe);
		sheet.setColumnView(j + 7, maxSizeTelPortable);
		sheet.setColumnView(j + 8, maxSizePAPNom);
		sheet.setColumnView(j + 9, maxSizePAPPrenom);
		sheet.setColumnView(j + 10, maxSizePAPTel);
		sheet.setColumnView(j + 11, maxSizeEntreeEntreprise);
		sheet.setColumnView(j + 12, maxSizeSortieEntreprise);
		sheet.setColumnView(j + 13, maxSizeAnciennete);
		sheet.setColumnView(j + 14, maxSizeTypeContrat);
		sheet.setColumnView(j + 15, maxSizePosteOccupe);
		sheet.setColumnView(j + 16, maxSizeService);
		sheet.setColumnView(j + 17, 12);
		sheet.setColumnView(j + 18, maxSizeCSP);

	}

	public ArrayList<SalarieBeanExport> getFiltreListBean() {
		return filtreListBean;
	}

	public void setFiltreListBean(ArrayList<SalarieBeanExport> filtreListBean) {
		this.filtreListBean = filtreListBean;
	}

	private void fillSheetEvaluationCompetences() throws Exception {

		sheet.setColumnView(0, 30);
		sheet.setColumnView(1, 15);
		sheet.setColumnView(2, 15);
		sheet.setColumnView(3, 15);

		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		sheet.mergeCells(0, 0, 3, 0);
		sheet.setRowView(0, 600);
		Label label = new Label(0, 0, "Evaluation des comp\u00E9tences ");
		label.setCellFormat(format);
		sheet.addCell(label);
		sheet.mergeCells(0, 1, 3, 1);
		sheet.setRowView(0, 600);
		String titre = nomGroupeOrEntreprise;
		if (!nomService.equals("")) {
			titre += " - " + nomService;
		}
		titre += " (" + dateFormat.format(new Date()) + ")";
		label = new Label(0, 1, titre);
		label.setCellFormat(format);
		sheet.addCell(label);

		font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		sheet.mergeCells(0, 4, 0, 6);
		label = new Label(0, 4, "Intitul\u00E9");
		label.setCellFormat(format);
		sheet.addCell(label);

		sheet.mergeCells(1, 4, 3, 4);
		sheet.mergeCells(1, 5, 3, 5);

		label = new Label(1, 4, entete[3]);
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(1, 5, "Niveau d'\u00E9valuation des comp\u00E9tences");
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(1, 6, "-");
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(2, 6, "=");
		label.setCellFormat(format);
		sheet.addCell(label);

		label = new Label(3, 6, "+");
		label.setCellFormat(format);
		sheet.addCell(label);

		int row = 7;
		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.LEFT);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		for (EvaluationCompetencesBean evaluationCompetencesBean : evaluationCompetencesBeanList) {
			if (!evaluationCompetencesBean.isFooter()) {
				font = new WritableFont(WritableFont.TIMES, 9,
						WritableFont.NO_BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.LEFT);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
				label = new Label(0, row,
						evaluationCompetencesBean.getIntituleCol());
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(1, row,
						evaluationCompetencesBean.getNbCompMoinsCurrentYear()
								+ "");
				label.setCellFormat(format);
				sheet.addCell(label);
				label = new Label(2, row,
						evaluationCompetencesBean.getNbCompEgaleCurrentYear()
								+ "");
				label.setCellFormat(format);
				sheet.addCell(label);
				label = new Label(3, row,
						evaluationCompetencesBean.getNbCompPlusCurrentYear()
								+ "");
				label.setCellFormat(format);
				sheet.addCell(label);
				row++;
			} else {
				font = new WritableFont(WritableFont.TIMES, 10,
						WritableFont.BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);

				label = new Label(0, row,
						evaluationCompetencesBean.getIntituleCol());
				label.setCellFormat(format);
				sheet.addCell(label);
				font = new WritableFont(WritableFont.TIMES, 9,
						WritableFont.BOLD);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.RIGHT);
				format.setVerticalAlignment(VerticalAlignment.CENTRE);
				label = new Label(1, row,
						evaluationCompetencesBean
								.getNbCompMoinsCurrentYearTaux());
				label.setCellFormat(format);
				sheet.addCell(label);
				label = new Label(2, row,
						evaluationCompetencesBean
								.getNbCompEgaleCurrentYearTaux());
				label.setCellFormat(format);
				sheet.addCell(label);
				label = new Label(3, row,
						evaluationCompetencesBean
								.getNbCompPlusCurrentYearTaux());
				label.setCellFormat(format);
				sheet.addCell(label);
				row++;
			}

		}
	}

}
