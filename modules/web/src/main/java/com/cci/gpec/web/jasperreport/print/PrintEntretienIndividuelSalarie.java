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

import com.cci.gpec.commons.DomaineFormationBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.EntretienBeanExport;
import com.cci.gpec.commons.FicheDePosteBean;
import com.cci.gpec.commons.ObjectifsEntretienBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.metier.implementation.DomaineFormationServiceImpl;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.EntretienServiceImpl;
import com.cci.gpec.metier.implementation.FicheDePosteServiceImpl;
import com.cci.gpec.metier.implementation.ObjectifsEntretienServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.web.Utils;

public class PrintEntretienIndividuelSalarie extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			printEntretienIndivSalarie(request, response);
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
	public String printEntretienIndivSalarie(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {

			Integer idEntretien = (Integer) request.getSession().getAttribute(
					"idEntretien");

			Integer indexSelectedRow = (Integer) request.getSession()
					.getAttribute("indexSelectedRow");

			String nomGroupe = request.getSession().getAttribute("nomGroupe")
					.toString();

			EntretienServiceImpl entretienService = new EntretienServiceImpl();

			ArrayList<EntretienBeanExport> entretienBeanList = new ArrayList<EntretienBeanExport>();

			EntretienBeanExport entretienBeanExport = entretienService
					.getEntretienBeanExportById(idEntretien);

			EntretienBean entretienBean = entretienService
					.getEntretienBeanById(idEntretien);

			FicheDePosteServiceImpl ficheDePosteService = new FicheDePosteServiceImpl();
			FicheDePosteBean ficheDePosteBean = ficheDePosteService
					.getFicheDePosteBeanByIdSalarie(entretienBeanExport
							.getIdSalarie());

			DomaineFormationServiceImpl domaineFormationService = new DomaineFormationServiceImpl();
			if (entretienBean.getDomainesFormation() != -1) {
				DomaineFormationBean domaineFormationBean = domaineFormationService
						.getDomaineFormationBeanById(entretienBean
								.getDomainesFormation());
				entretienBeanExport.setDomainesFormation(domaineFormationBean
						.getNom());
			}
			if (entretienBean.getDomainesFormation2() != -1) {
				DomaineFormationBean domaineFormationBean2 = domaineFormationService
						.getDomaineFormationBeanById(entretienBean
								.getDomainesFormation2());
				entretienBeanExport.setDomainesFormation2(domaineFormationBean2
						.getNom());
			}
			if (entretienBean.getDomainesFormation3() != -1) {
				DomaineFormationBean domaineFormationBean3 = domaineFormationService
						.getDomaineFormationBeanById(entretienBean
								.getDomainesFormation3());
				entretienBeanExport.setDomainesFormation3(domaineFormationBean3
						.getNom());
			}
			if (entretienBean.getDomainesFormation4() != -1) {
				DomaineFormationBean domaineFormationBean4 = domaineFormationService
						.getDomaineFormationBeanById(entretienBean
								.getDomainesFormation4());
				entretienBeanExport.setDomainesFormation4(domaineFormationBean4
						.getNom());
			}
			if (entretienBean.getDomainesFormation5() != -1) {
				DomaineFormationBean domaineFormationBean5 = domaineFormationService
						.getDomaineFormationBeanById(entretienBean
								.getDomainesFormation5());
				entretienBeanExport.setDomainesFormation5(domaineFormationBean5
						.getNom());
			}

			SalarieServiceImpl salarieService = new SalarieServiceImpl();
			SalarieBean salarieBean = salarieService
					.getSalarieBeanById(entretienBeanExport.getIdSalarie());
			entretienBeanExport.setNom(salarieBean.getNom());
			entretienBeanExport.setPrenom(salarieBean.getPrenom());
			entretienBeanExport.setIdEntreprise(salarieBean
					.getIdEntrepriseSelected());
			EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
			entretienBeanExport.setJustificatif(serv.getEntrepriseBeanById(
					salarieBean.getIdEntrepriseSelected()).getJustificatif());
			entretienBeanExport.setNomEntreprise(serv.getEntrepriseBeanById(
					salarieBean.getIdEntrepriseSelected()).getNom());

			entretienBeanExport.setUrl(Utils.getFileUrl(
					salarieBean.getIdEntrepriseSelected(), "logo_entreprise",
					false, false, true, true, nomGroupe));

			List<ParcoursBean> parcoursList = salarieBean.getParcoursBeanList();
			Collections.sort(parcoursList);
			Collections.reverse(parcoursList);
			entretienBeanExport.setPoste(parcoursList.get(0).getNomMetier());

			ServiceImpl service = new ServiceImpl();
			entretienBeanExport.setService(service.getServiceBeanById(
					salarieBean.getIdServiceSelected()).getNom());

			/*
			 * String rappelObj = "Il n'y a pas eu d'entretien avant celui-ci.";
			 * Collections.sort(salarieBean.getEntretienBeanList());
			 * Collections.reverse(salarieBean.getEntretienBeanList()); if
			 * ((indexSelectedRow + 1) < salarieBean.getEntretienBeanList()
			 * .size()) { EntretienBean entretienBean2 = salarieBean
			 * .getEntretienBeanList().get(indexSelectedRow + 1); rappelObj =
			 * entretienBean2.getObjIntitule(); } else rappelObj = "Il n'y a pas
			 * eu d'entretien avant celui-ci.";
			 */

			ObjectifsEntretienServiceImpl objServ = new ObjectifsEntretienServiceImpl();
			List<ObjectifsEntretienBean> list = new ArrayList<ObjectifsEntretienBean>();
			list = objServ.getObjectifsEntretienListByIdEntretien(idEntretien);

			// String objIntitule = "";
			// String objDelais = "";
			// String objMoyens = "";
			//
			// for (ObjectifsEntretienBean o : list) {
			// objIntitule += (o.getIntitule() != null) ? o.getIntitule()
			// + "\n" : "\n";
			// objDelais += (o.getDelai() != null) ? o.getDelai() + "\n"
			// : "\n";
			// objMoyens += (o.getMoyen() != null) ? o.getMoyen() + "\n"
			// : "\n";
			// }
			//
			// entretienBeanExport.setObjIntitule(objIntitule);
			// entretienBeanExport.setObjDelais(objDelais);
			// entretienBeanExport.setObjMoyens(objMoyens);

			entretienBeanExport.setObjectifN(list);

			// String rappelObj = "";
			// String bilanDessous = "";
			// String commentaireBilan = "";

			Collections.sort(salarieBean.getEntretienBeanList());
			Collections.reverse(salarieBean.getEntretienBeanList());
			List<ObjectifsEntretienBean> list2 = new ArrayList<ObjectifsEntretienBean>();
			if ((indexSelectedRow + 1) < salarieBean.getEntretienBeanList()
					.size()) {
				EntretienBean entretienBean2 = salarieBean
						.getEntretienBeanList().get(indexSelectedRow + 1);
				list2 = objServ
						.getObjectifsEntretienListByIdEntretien(entretienBean2
								.getId());
				// for (ObjectifsEntretienBean o : list2) {
				// rappelObj += (o.getIntitule() != null) ? o.getIntitule()
				// + "\n" : "\n";
				// bilanDessous += (o.getResultat() != null) ? o.getResultat()
				// + "\n" : "\n";
				// commentaireBilan += (o.getCommentaire() != null) ? o
				// .getCommentaire()
				// + "\n" : "\n";
				// }
			}
			// else {
			// rappelObj = "Il n'y a pas eu d'entretien avant celui-ci.";
			// }

			// entretienBeanExport.setRappelObj(rappelObj);
			// entretienBeanExport.setBilanDessous(bilanDessous);
			// entretienBeanExport.setCommentaireBilan(commentaireBilan);

			entretienBeanExport.setObjectifNPrec(list2);

			entretienBeanList.add(entretienBeanExport);

			JRDataSource jRDataSource = new JRBeanCollectionDataSource(
					entretienBeanList);

			String realPath = getServletConfig()
					.getServletContext()
					.getRealPath(
							"/reportTemplates/EntretienIndividuelSalarie.jrxml");

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
					"attachment;filename=EntretienIndividuel.pdf");
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
