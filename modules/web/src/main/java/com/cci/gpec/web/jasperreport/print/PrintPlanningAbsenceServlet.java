package com.cci.gpec.web.jasperreport.print;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.cci.gpec.commons.GanttChartBean;
import com.cci.gpec.commons.SalarieBeanExport;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.primefaces.component.schedule.GanttChartBeanMaker;

public class PrintPlanningAbsenceServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// printPlanningAbsence(request,response);
			printPlanningAbsenceGantt(request, response);
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

	public void printPlanningAbsence(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {

			Integer[] idSelected = (Integer[]) request.getSession()
					.getAttribute("idSelected");

			Integer idGroupe = (Integer) request.getSession().getAttribute(
					"groupe");

			SalarieServiceImpl salarieService = new SalarieServiceImpl();

			ArrayList<SalarieBeanExport> salarieBeanList;

			salarieBeanList = (ArrayList<SalarieBeanExport>) salarieService
					.getSalariesListToExport(idGroupe);

			// filtrage de la liste des salariés suivant les valeurs
			// séléctionnées dans les liste déroulantes de la page
			ArrayList<SalarieBeanExport> salarieBeanListTmp = new ArrayList<SalarieBeanExport>();
			boolean flag;
			int idEntrepriseSelected = idSelected[0];
			int idServiceSelected = idSelected[1];
			int idMetierSelected = idSelected[2];

			for (SalarieBeanExport salarieBeanExport : salarieBeanList) {
				flag = false;

				if (idEntrepriseSelected == -1 && idServiceSelected == -1) {
					flag = true;
				} else {
					if (salarieBeanExport.getIdEntrepriseSelected() == idEntrepriseSelected) {

						if (idServiceSelected == -1) {
							flag = true;
						} else if (salarieBeanExport.getIdServiceSelected() == idServiceSelected) {
							flag = true;
						}
					}
				}

				if (flag) {

					salarieBeanListTmp.add(salarieBeanExport);
				}
			}

			JRBeanCollectionDataSource jRDataSource = new JRBeanCollectionDataSource(
					salarieBeanListTmp);

			String realPath = getServletConfig().getServletContext()
					.getRealPath("/reportTemplates/PlanningAbsence.jrxml");

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

			JExcelApiExporter exporterXLS = new JExcelApiExporter();

			exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT,
					jasperPrint);

			exporterXLS.setParameter(
					JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);

			exporterXLS.setParameter(
					JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.FALSE);

			exporterXLS.setParameter(
					JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					Boolean.TRUE);

			// If you want to export the XLS report to physical file.
			// xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,"report.xls");

			// This'll allows users to directly download the XLS report without
			// having to save XLS report on server.
			exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,
					response.getOutputStream());

			response.addHeader("Content-disposition",
					"attachment;filename=planningDesAbsences.xls");

			// This'll trigger the browser to use excel application to deal with
			// downloaded file.
			response.setContentType("application/vnd.ms-excel");

			exporterXLS.exportReport();

			// byte[] bytes =
			// JasperExportManager.exportReportToPdf(jasperPrint);
			//
			// /***********************************************************************
			// * Pour afficher une boîte de dialogue pour enregistrer le fichier
			// sous
			// * le nom rapport.pdf
			// **********************************************************************/
			// response.addHeader("Content-disposition",
			// "attachment;filename=planningDesAbsences.xls");
			// response.setContentLength(bytes.length);
			// response.getOutputStream().write(bytes);
			// response.setContentType("application/xls");
			//

			// vidage du virtualizer
			virtualizer.cleanup();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printPlanningAbsenceGantt(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {

			Integer[] idSelected = (Integer[]) request.getSession()
					.getAttribute("idSelected");

			Integer idGroupe = (Integer) request.getSession().getAttribute(
					"groupe");

			SalarieServiceImpl salarieService = new SalarieServiceImpl();

			ArrayList<SalarieBeanExport> salarieBeanList;

			salarieBeanList = (ArrayList<SalarieBeanExport>) salarieService
					.getSalariesListToExport(idGroupe);

			// filtrage de la liste des salariés suivant les valeurs
			// séléctionnées dans les liste déroulantes de la page
			ArrayList<SalarieBeanExport> salarieBeanListTmp = new ArrayList<SalarieBeanExport>();
			boolean flag;
			int idEntrepriseSelected = idSelected[0];
			int idServiceSelected = idSelected[1];

			for (SalarieBeanExport salarieBeanExport : salarieBeanList) {
				flag = false;

				if (idEntrepriseSelected == -1 && idServiceSelected == -1) {
					flag = true;
				} else {
					if (salarieBeanExport.getIdEntrepriseSelected() == idEntrepriseSelected) {

						if (idServiceSelected == -1) {
							flag = true;
						} else if (salarieBeanExport.getIdServiceSelected() == idServiceSelected) {
							flag = true;
						}
					}
				}

				if (flag) {

					salarieBeanListTmp.add(salarieBeanExport);
				}
			}
			GanttChartBeanMaker ganttChartBeanMaker = new GanttChartBeanMaker();
			ArrayList<GanttChartBean> ganttChartBeanList = ganttChartBeanMaker
					.getGanttChartData(salarieBeanListTmp);

			JRDataSource jRDataSource = new JRBeanCollectionDataSource(
					ganttChartBeanList);

			String realPath = getServletConfig().getServletContext()
					.getRealPath("/reportTemplates/ganttchart.jrxml");

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

			JExcelApiExporter exporterXLS = new JExcelApiExporter();

			exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT,
					jasperPrint);

			exporterXLS.setParameter(
					JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);

			exporterXLS.setParameter(
					JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.FALSE);

			exporterXLS.setParameter(
					JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					Boolean.TRUE);

			// If you want to export the XLS report to physical file.
			// xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,"report.xls");

			// This'll allows users to directly download the XLS report without
			// having to save XLS report on server.
			exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,
					response.getOutputStream());

			response.addHeader("Content-disposition",
					"attachment;filename=planningDesAbsences.xls");

			// This'll trigger the browser to use excel application to deal with
			// downloaded file.
			response.setContentType("application/vnd.ms-excel");

			exporterXLS.exportReport();

			// vidage du virtualizer
			virtualizer.cleanup();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
