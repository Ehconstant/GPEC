package com.cci.gpec.web.jasperreport.print;

import java.io.IOException;
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

import com.cci.gpec.commons.FicheMetierBean;
import com.cci.gpec.commons.FicheMetierBeanExport;
import com.cci.gpec.commons.FicheMetierEntrepriseBean;
import com.cci.gpec.commons.StatutBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierEntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierServiceImpl;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.cci.gpec.web.Utils;

public class PrintFicheMetier extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Integer idFicheMetier = (Integer) request.getSession()
					.getAttribute("idFicheMetier");
			String entrepriseExport = (String) request.getSession()
					.getAttribute("entrepriseExport");
			Boolean empty = (Boolean) request.getSession()
					.getAttribute("empty");
			if (empty) {
				printOneFicheMetier(request, response, idFicheMetier,
						entrepriseExport, empty);
			} else {
				if (idFicheMetier != 0) {
					printOneFicheMetier(request, response, idFicheMetier,
							entrepriseExport, empty);
				} else {
					printFicheMetier(request, response);
				}
			}
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
	public String printFicheMetier(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();

			FicheMetierEntrepriseServiceImpl ficheMetierEntrepriseService = new FicheMetierEntrepriseServiceImpl();

			Integer idEntreprise = null;
			if (request.getSession().getAttribute("idEntreprise") != null)
				idEntreprise = (Integer) request.getSession().getAttribute(
						"idEntreprise");

			Integer idGroupe = (Integer) request.getSession().getAttribute(
					"groupe");

			String nomGroupe = request.getSession().getAttribute("nomGroupe")
					.toString();

			List<FicheMetierBeanExport> ficheMetierBeanListE = new ArrayList<FicheMetierBeanExport>();

			List<FicheMetierBean> ficheMetierBeanList = new ArrayList<FicheMetierBean>();

			List<FicheMetierEntrepriseBean> ficheMetierEntrepriseBeanList = ficheMetierEntrepriseService
					.getFicheMetierEntrepriseList(idGroupe);

			List<Integer> idList = new ArrayList<Integer>();

			for (FicheMetierEntrepriseBean fme : ficheMetierEntrepriseBeanList) {
				FicheMetierBean fm = ficheMetierService
						.getFicheMetierBeanById(fme.getIdFicheMetier());
				if (!idList.contains(fm.getId())) {
					ficheMetierBeanList.add(fm);
					idList.add(fm.getId());
				}
			}

			Collections.sort(ficheMetierBeanList);

			for (int i = 0; i < ficheMetierBeanList.size(); i++) {
				StatutServiceImpl statutService = new StatutServiceImpl();
				if (ficheMetierBeanList.get(i).getCspReference() != -1) {
					StatutBean statutBean = statutService
							.getStatutBeanById(ficheMetierBeanList.get(i)
									.getCspReference());
					ficheMetierBeanList.get(i).setCsp(statutBean.getNom());
				} else {
					ficheMetierBeanList.get(i).setCsp("Non renseignée");
				}

				if (i == ficheMetierBeanList.size() - 1)
					ficheMetierBeanList.get(i).setCatalogue(false);
				else
					ficheMetierBeanList.get(i).setCatalogue(true);

				ficheMetierBeanList.get(i).setIdEntreprise(idEntreprise);
				if (idEntreprise != null && idEntreprise != -1) {
					EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
					ficheMetierBeanList.get(i).setJustificatif(
							serv.getEntrepriseBeanById(idEntreprise)
									.getJustificatif());
					ficheMetierBeanList.get(i).setUrl(
							Utils.getFileUrl(idEntreprise, "logo_entreprise",
									false, false, true, false, nomGroupe));
				}

			}

			FicheMetierBeanExport fm = new FicheMetierBeanExport();
			fm.setFichesMetierList(ficheMetierBeanList);

			ficheMetierBeanListE.add(fm);

			JRDataSource jRDataSource = new JRBeanCollectionDataSource(
					ficheMetierBeanListE);

			String realPath = getServletConfig().getServletContext()
					.getRealPath("/reportTemplates/FichesMetier.jrxml");

			JasperDesign jasperDesign = JRXmlLoader.load(realPath);

			JasperDesign jasperDesignSub = JRXmlLoader.load(request
					.getSession().getServletContext().getRealPath("/")
					+ "reportTemplates/FichesMetier_subreport1.jrxml");

			HashMap<String, Object> jasperMap = new HashMap<String, Object>();

			// COMPILATION des template
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);

			JasperCompileManager.compileReportToFile(jasperDesignSub, request
					.getSession().getServletContext().getRealPath("/")
					+ "reportTemplates/FichesMetier_subreport1.jasper");

			// création du virtualizer
			JRFileVirtualizer virtualizer = new JRFileVirtualizer(100);

			jasperMap.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

			jasperMap.put("SUBREPORT_DIR", request.getSession()
					.getServletContext().getRealPath("/")
					+ "reportTemplates/");

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
					"attachment;filename=FichesMetier.pdf");
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

	public String printOneFicheMetier(HttpServletRequest request,
			HttpServletResponse response, Integer idFicheMetier,
			String entrepriseExport, boolean empty) throws Exception {

		try {

			List<FicheMetierBeanExport> ficheMetierBeanListE = new ArrayList<FicheMetierBeanExport>();
			FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();

			if (!empty) {

				Integer idEntreprise = null;
				if (request.getSession().getAttribute("idEntreprise") != null)
					idEntreprise = (Integer) request.getSession().getAttribute(
							"idEntreprise");

				String nomGroupe = request.getSession()
						.getAttribute("nomGroupe").toString();

				FicheMetierBean ficheMetierBean = ficheMetierService
						.getFicheMetierBeanById(idFicheMetier);

				StatutServiceImpl statutService = new StatutServiceImpl();
				if (ficheMetierBean.getCspReference() != -1) {
					StatutBean statutBean = statutService
							.getStatutBeanById(ficheMetierBean
									.getCspReference());
					ficheMetierBean.setCsp(statutBean.getNom());
				} else {
					ficheMetierBean.setCsp("Non renseignée");
				}

				// ficheMetierBean.setIdEntreprise(idEntreprise);
				if (idEntreprise != null) {
					EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
					ficheMetierBean.setJustificatif(serv.getEntrepriseBeanById(
							idEntreprise).getJustificatif());
					ficheMetierBean.setUrl(Utils.getFileUrl(idEntreprise,
							"logo_entreprise", false, false, true, true,
							nomGroupe));
				}

				ficheMetierBean.setLibelleEntreprise(entrepriseExport);

				ficheMetierBean.setCatalogue(false);

				List<FicheMetierBean> ficheMetierBeanList = new ArrayList<FicheMetierBean>();
				ficheMetierBeanList.add(ficheMetierBean);

				FicheMetierBeanExport fm = new FicheMetierBeanExport();
				fm.setFichesMetierList(ficheMetierBeanList);

				ficheMetierBeanListE.add(fm);
			} else {
				FicheMetierBeanExport fm = new FicheMetierBeanExport();
				List<FicheMetierBean> ficheMetierBeanList = new ArrayList<FicheMetierBean>();
				ficheMetierBeanList.add(new FicheMetierBean());
				fm.setFichesMetierList(ficheMetierBeanList);
				ficheMetierBeanListE.add(fm);
			}
			JRDataSource jRDataSource = new JRBeanCollectionDataSource(
					ficheMetierBeanListE);

			String realPath = getServletConfig().getServletContext()
					.getRealPath("/reportTemplates/FichesMetier.jrxml");

			JasperDesign jasperDesign = JRXmlLoader.load(realPath);

			JasperDesign jasperDesignSub = JRXmlLoader.load(request
					.getSession().getServletContext().getRealPath("/")
					+ "reportTemplates/FichesMetier_subreport1.jrxml");

			HashMap<String, Object> jasperMap = new HashMap<String, Object>();

			// COMPILATION des template
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			JasperCompileManager.compileReportToFile(jasperDesignSub, request
					.getSession().getServletContext().getRealPath("/")
					+ "reportTemplates/FichesMetier_subreport1.jasper");

			// création du virtualizer
			JRFileVirtualizer virtualizer = new JRFileVirtualizer(100);

			jasperMap.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

			jasperMap.put("SUBREPORT_DIR", request.getSession()
					.getServletContext().getRealPath("/")
					+ "reportTemplates/");

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
					"attachment;filename=FichesMetier.pdf");
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
