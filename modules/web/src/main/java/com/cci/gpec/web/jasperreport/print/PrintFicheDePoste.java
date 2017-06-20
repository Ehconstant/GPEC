package com.cci.gpec.web.jasperreport.print;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
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

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.FicheDePosteBean;
import com.cci.gpec.commons.FicheDePosteBeanExport;
import com.cci.gpec.commons.FicheMetierBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.commons.StatutBean;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FicheDePosteServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.metier.implementation.StatutServiceImpl;
import com.cci.gpec.web.Utils;

public class PrintFicheDePoste extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			printFicheDePoste(request, response);
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
	public String printFicheDePoste(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {

			Integer idFicheDePoste = (Integer) request.getSession()
					.getAttribute("idFicheDePoste");
			String nomGroupe = request.getSession().getAttribute("nomGroupe")
					.toString();

			String display = "";
			if (request.getSession().getAttribute("display") != null) {
				display = (String) request.getSession().getAttribute("display");
			}

			FicheDePosteServiceImpl ficheDePosteService = new FicheDePosteServiceImpl();

			ArrayList<FicheDePosteBeanExport> ficheDePosteBeanList = new ArrayList<FicheDePosteBeanExport>();

			FicheDePosteBeanExport ficheDePosteBeanExport = ficheDePosteService
					.getFicheDePosteBeanExportById(idFicheDePoste);
			FicheDePosteBean ficheDePosteBean = ficheDePosteService
					.getFicheDePosteBeanById(idFicheDePoste);
			FicheMetierServiceImpl ficheMetierService = new FicheMetierServiceImpl();
			FicheMetierBean ficheMetierBean = ficheMetierService
					.getFicheMetierBeanById(ficheDePosteBean
							.getIdFicheMetierType());

			SalarieServiceImpl salarieService = new SalarieServiceImpl();
			SalarieBean salarieBean = salarieService
					.getSalarieBeanById(ficheDePosteBeanExport.getIdSalarie());
			ficheDePosteBeanExport.setNom(salarieBean.getNom());
			ficheDePosteBeanExport.setPrenom(salarieBean.getPrenom());

			/* Ajout MANTIS 2552 */
			ServiceImpl serviceImpl = new ServiceImpl();
			ServiceBean serviceBean = serviceImpl
					.getServiceBeanById(salarieBean.getIdServiceSelected());
			ficheDePosteBeanExport.setLibelleService(serviceBean.getNom());
			ficheDePosteBeanExport.setFormationRef(ficheMetierBean
					.getNiveauFormationRequis());
			ficheDePosteBeanExport.setFormation(salarieBean
					.getNivFormationAtteint());

			EntrepriseServiceImpl entrepriseServiceImpl = new EntrepriseServiceImpl();
			EntrepriseBean entrepriseBean = entrepriseServiceImpl
					.getEntrepriseBeanById(salarieBean
							.getIdEntrepriseSelected());
			ficheDePosteBeanExport
					.setLibelleEntreprise(entrepriseBean.getNom());
			ficheDePosteBeanExport.setJustificatif(entrepriseBean
					.getJustificatif());
			ficheDePosteBeanExport.setUrl(Utils.getFileUrl(
					salarieBean.getIdEntrepriseSelected(), "logo_entreprise",
					false, false, true, true, nomGroupe));

			/* Fin MANTIS 2552 */

			StatutServiceImpl statutService = new StatutServiceImpl();
			StatutBean statutBean = statutService
					.getStatutBeanById(ficheMetierBean.getCspReference());
			ficheDePosteBeanExport.setCspRef(statutBean.getNom());

			List<ParcoursBean> parcoursList = salarieBean.getParcoursBeanList();
			Collections.sort(parcoursList);
			Collections.reverse(parcoursList);
			ficheDePosteBeanExport.setIntitulePoste(parcoursList.get(0)
					.getNomMetier());
			ficheDePosteBeanExport
					.setAncienneteEntreprise(getAncienneteEntreprise(salarieBean));
			ficheDePosteBeanExport
					.setAnciennetePoste(getAnciennetePoste(salarieBean));
			ficheDePosteBeanExport.setFicheMetier(ficheMetierBean
					.getIntituleFicheMetier());
			ficheDePosteBeanExport.setCsp(parcoursList.get(0)
					.getNomTypeStatut());
			ficheDePosteBeanExport.setFinalitePoste(ficheMetierBean
					.getFinaliteFicheMetier());
			ficheDePosteBeanExport.setActiviteResponsabilite(ficheMetierBean
					.getActiviteResponsabilite());
			ficheDePosteBeanExport.setSavoirMetier(ficheMetierBean.getSavoir());
			ficheDePosteBeanExport.setSavoirFaireMetier(ficheMetierBean
					.getSavoirFaire());
			ficheDePosteBeanExport.setSavoirEtreMetier(ficheMetierBean
					.getSavoirEtre());
			ficheDePosteBeanExport.setParticularitePoste(ficheMetierBean
					.getParticularite());
			ficheDePosteBeanExport.setCategorieCompetence(ficheDePosteBean
					.getCategorieCompetence());

			ficheDePosteBeanExport.setSavoir2(ajouterLegende(ficheDePosteBean
					.getSavoir()));
			ficheDePosteBeanExport
					.setSavoirEtre2(ajouterLegende(ficheDePosteBean
							.getSavoirEtre()));
			ficheDePosteBeanExport
					.setSavoirFaire2(ajouterLegende(ficheDePosteBean
							.getSavoirFaire()));
			ficheDePosteBeanExport
					.setCompetencesSpecifiques2(ajouterLegende(ficheDePosteBean
							.getCompetencesSpecifiques()));
			ficheDePosteBeanExport
					.setEvaluationGlobale2(ajouterLegende(ficheDePosteBean
							.getEvaluationGlobale()));
			if (!display.equals("") && display.equals("true"))
				ficheDePosteBeanExport.setDisplay(true);
			else
				ficheDePosteBeanExport.setDisplay(false);

			ficheDePosteBeanList.add(ficheDePosteBeanExport);

			JRDataSource jRDataSource = new JRBeanCollectionDataSource(
					ficheDePosteBeanList);

			String realPath = "";
			if (!display.equals("") && display.equals("true"))
				realPath = getServletConfig().getServletContext().getRealPath(
						"/reportTemplates/FicheDePosteEval.jrxml");
			else
				realPath = getServletConfig().getServletContext().getRealPath(
						"/reportTemplates/FicheDePoste.jrxml");

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
					"attachment;filename=FicheDePoste.pdf");
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

	private String ajouterLegende(String eval) {
		if (eval.equals("1"))
			return "+ (maîtrisé)";
		if (eval.equals("0"))
			return "= (acquis)";
		if (eval.equals("-1"))
			return "- (à améliorer)";
		return "Non évalué";
	}

	private String getAncienneteEntreprise(SalarieBean salarie) {
		ParcoursBean parcourDeb = getFirstParcours(salarie);
		Calendar dateDebut = new GregorianCalendar();
		Calendar dateFin = new GregorianCalendar();
		if (parcourDeb != null) {
			dateDebut.setTime(parcourDeb.getDebutFonction());
		}
		ParcoursBean parcourFin = getLastParcours(salarie);
		if (parcourFin != null) {
			if (parcourFin.getFinFonction() != null
			/* && !parcourFin.getFinFonction().equals(UtilsDate.FIN_NULL) */) {
				dateFin.setTime(parcourFin.getFinFonction());
			} else {
				dateFin.setTime(new Date());
			}
		}
		return dateFin.get(Calendar.YEAR) - dateDebut.get(Calendar.YEAR)
				+ " an(s)";
	}

	private String getAnciennetePoste(SalarieBean salarie) {

		Calendar dateDebut = new GregorianCalendar();
		Calendar dateFin = new GregorianCalendar();

		ParcoursBean parcourFin = getLastParcours(salarie);
		dateDebut.setTime(parcourFin.getDebutFonction());
		if (parcourFin != null) {
			if (parcourFin.getFinFonction() != null
			/* && !parcourFin.getFinFonction().equals(UtilsDate.FIN_NULL) */) {
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

		ParcoursBean parcoursBean = null;
		if (parcourList != null && parcourList.size() > 0) {
			Collections.sort(parcourList);
			Collections.reverse(parcourList);
			parcoursBean = parcourList.get(0);
		}

		return parcoursBean;
	}

	private ParcoursBean getFirstParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		ParcoursBean pb = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			if (pb == null)
				pb = parcour;
			if (parcour.getDebutFonction().before(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}
}
