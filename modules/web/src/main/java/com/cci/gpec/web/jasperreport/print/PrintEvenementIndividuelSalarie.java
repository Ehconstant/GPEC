package com.cci.gpec.web.jasperreport.print;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

import com.cci.gpec.commons.EvenementBean;
import com.cci.gpec.commons.EvenementBeanExport;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.EvenementServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.web.Utils;

public class PrintEvenementIndividuelSalarie extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Integer idEvenement = (Integer) request.getSession().getAttribute(
					"idEvenement");
			if (idEvenement != null)
				printEvenementIndivSalarie(request, response);
			else
				printEvenementsSalarie(request, response);
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
	public String printEvenementIndivSalarie(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {

			Integer idEvenement = (Integer) request.getSession().getAttribute(
					"idEvenement");
			String nom = (String) request.getSession().getAttribute("nom");
			String prenom = (String) request.getSession()
					.getAttribute("prenom");
			String nomEntreprise = (String) request.getSession().getAttribute(
					"nomEntreprise");
			Integer idEntreprise = (Integer) request.getSession().getAttribute(
					"idEntreprise");
			String nomGroupe = request.getSession().getAttribute("nomGroupe")
					.toString();

			EvenementServiceImpl EvenementService = new EvenementServiceImpl();

			ArrayList<EvenementBean> EvenementBeanList = new ArrayList<EvenementBean>();

			EvenementBean evenementBean = EvenementService
					.getEvenementBeanById(idEvenement);

			evenementBean.setPrenom(prenom);
			evenementBean.setNom(nom);
			evenementBean.setNomEntreprise(nomEntreprise);
			evenementBean.setIdEntreprise(idEntreprise);
			evenementBean.setUrl(Utils.getFileUrl(idEntreprise,
					"logo_entreprise", false, false, true, true, nomGroupe));

			EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
			evenementBean.setJustificatif(serv.getEntrepriseBeanById(
					idEntreprise).getJustificatif());

			ServiceImpl service = new ServiceImpl();
			SalarieServiceImpl sal = new SalarieServiceImpl();
			evenementBean.setService(service.getServiceBeanById(
					sal.getSalarieBeanById(evenementBean.getIdSalarie())
							.getIdServiceSelected()).getNom());

			EvenementBeanList.add(evenementBean);

			JRDataSource jRDataSource = new JRBeanCollectionDataSource(
					EvenementBeanList);

			String realPath = getServletConfig()
					.getServletContext()
					.getRealPath(
							"/reportTemplates/EvenementIndividuelSalarie.jrxml");

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
					"attachment;filename=EvenementIndividuel.pdf");
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

	/*
	 */
	public String printEvenementsSalarie(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {

			Date debutExtraction = (Date) request.getSession().getAttribute(
					"debutExtraction");
			Date finExtraction = (Date) request.getSession().getAttribute(
					"finExtraction");
			Integer idEvenement = (Integer) request.getSession().getAttribute(
					"idEvenement");
			String nom = (String) request.getSession().getAttribute("nom");
			String prenom = (String) request.getSession()
					.getAttribute("prenom");
			String nomEntreprise = (String) request.getSession().getAttribute(
					"nomEntreprise");
			Integer idEntreprise = (Integer) request.getSession().getAttribute(
					"idEntreprise");
			Integer idSalarie = (Integer) request.getSession().getAttribute(
					"idSalarie");
			String nomGroupe = request.getSession().getAttribute("nomGroupe")
					.toString();

			EvenementServiceImpl EvenementService = new EvenementServiceImpl();

			ArrayList<EvenementBeanExport> EvenementBeanList = new ArrayList<EvenementBeanExport>();

			EvenementBeanExport evenementBeanE = new EvenementBeanExport();

			evenementBeanE.setPrenom(prenom);
			evenementBeanE.setNom(nom);
			evenementBeanE.setNomEntreprise(nomEntreprise);
			evenementBeanE.setIdEntreprise(idEntreprise);
			evenementBeanE.setUrl(Utils.getFileUrl(idEntreprise,
					"logo_entreprise", false, false, true, true, nomGroupe));

			evenementBeanE.setPath(request.getSession().getServletContext()
					.getRealPath("/")
					+ "reportTemplates/");

			EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
			evenementBeanE.setJustificatif(serv.getEntrepriseBeanById(
					idEntreprise).getJustificatif());

			List<EvenementBean> l = new ArrayList<EvenementBean>();
			if (debutExtraction != null) {
				if (finExtraction != null) {
					for (EvenementBean e : EvenementService
							.getEvenementBeanListByIdSalarie(idSalarie)) {
						if (e.getDateEvenement().after(debutExtraction)
								&& e.getDateEvenement().before(finExtraction))
							l.add(e);
					}
				} else {
					for (EvenementBean e : EvenementService
							.getEvenementBeanListByIdSalarie(idSalarie)) {
						if (e.getDateEvenement().after(debutExtraction))
							l.add(e);
					}
				}
			} else {
				l.addAll(EvenementService
						.getEvenementBeanListByIdSalarie(idSalarie));
			}
			ServiceImpl service = new ServiceImpl();
			SalarieServiceImpl sal = new SalarieServiceImpl();
			for (EvenementBean e : l) {
				e.setNom(nom);
				e.setPrenom(prenom);
				e.setService(service.getServiceBeanById(
						sal.getSalarieBeanById(idSalarie)
								.getIdServiceSelected()).getNom());
			}
			evenementBeanE.setEvenementList(l);

			EvenementBeanList.add(evenementBeanE);

			JRDataSource jRDataSource = new JRBeanCollectionDataSource(
					EvenementBeanList);

			String realPath = getServletConfig().getServletContext()
					.getRealPath("/reportTemplates/EvenementsSalarie.jrxml");

			JasperDesign jasperDesign = JRXmlLoader.load(realPath);

			JasperDesign jasperDesignSub = JRXmlLoader.load(request
					.getSession().getServletContext().getRealPath("/")
					+ "reportTemplates/EvenementsSalarie_subreport.jrxml");

			HashMap<String, Object> jasperMap = new HashMap<String, Object>();

			// COMPILATION des template
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			JasperCompileManager.compileReportToFile(jasperDesignSub, request
					.getSession().getServletContext().getRealPath("/")
					+ "reportTemplates/EvenementsSalarie_subreport.jasper");

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
					"attachment;filename=EvenementIndividuel.pdf");
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
