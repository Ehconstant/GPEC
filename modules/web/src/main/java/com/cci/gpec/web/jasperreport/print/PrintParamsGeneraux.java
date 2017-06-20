package com.cci.gpec.web.jasperreport.print;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.context.FacesContext;
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

import com.cci.gpec.commons.ParamsGenerauxBeanExport;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.ParamsGenerauxServiceImpl;
import com.cci.gpec.web.Utils;

public class PrintParamsGeneraux extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			printParamsGeneraux(request, response);
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
	public String printParamsGeneraux(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {

			Integer idEntreprise = (Integer) request.getSession().getAttribute(
					"idEntreprise");

			String nomGroupe = request.getSession().getAttribute("nomGroupe")
					.toString();

			ParamsGenerauxServiceImpl paramsGenerauxService = new ParamsGenerauxServiceImpl();

			ArrayList<ParamsGenerauxBeanExport> paramsGenerauxBeanExportList = new ArrayList<ParamsGenerauxBeanExport>();

			ParamsGenerauxBeanExport paramsGenerauxBeanExport = paramsGenerauxService
					.getParamsGenerauxBeanExportListByIdEntreprise(idEntreprise)
					.get(0);
			EntrepriseServiceImpl ent = new EntrepriseServiceImpl();
			paramsGenerauxBeanExport.setLibelleEntreprise(ent
					.getEntrepriseBeanById(
							paramsGenerauxBeanExport.getIdEntreprise())
					.getNom());
			// .getParamsGenerauxBeanByIdEntreprise(idEntreprise);
			paramsGenerauxBeanExport.setIdEntreprise(paramsGenerauxBeanExport
					.getIdEntreprise());
			paramsGenerauxBeanExport.setJustificatif(ent.getEntrepriseBeanById(
					paramsGenerauxBeanExport.getIdEntreprise())
					.getJustificatif());
			paramsGenerauxBeanExport.setUrl(Utils.getFileUrl(
					paramsGenerauxBeanExport.getIdEntreprise(),
					"logo_entreprise", false, false, true, true, nomGroupe));
			FacesContext facesContext = FacesContext.getCurrentInstance();

			paramsGenerauxBeanExportList.add(paramsGenerauxBeanExport);

			JRDataSource jRDataSource = new JRBeanCollectionDataSource(
					paramsGenerauxBeanExportList);

			String realPath = getServletConfig().getServletContext()
					.getRealPath("/reportTemplates/ParamsGeneraux.jrxml");

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
					"attachment;filename=ParametresGeneraux.pdf");
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

}
