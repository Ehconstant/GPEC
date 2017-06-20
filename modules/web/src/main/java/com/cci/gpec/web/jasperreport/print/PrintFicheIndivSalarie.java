package com.cci.gpec.web.jasperreport.print;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.PersonneAChargeBean;
import com.cci.gpec.commons.PiecesJustificativesBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.SalarieBeanExport;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.PersonneAChargeServiceImpl;
import com.cci.gpec.metier.implementation.PiecesJustificativesServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.web.Utils;

public class PrintFicheIndivSalarie extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			printFicheIndivSalarie(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	private static final long serialVersionUID = 704992979558623612L;

	public String printFicheIndivSalarie(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {

			Integer idSalarie = (Integer) request.getSession().getAttribute(
					"idSalarie");
			Date debutExtraction = (Date) request.getSession().getAttribute(
					"debutExtractionCV");
			Date finExtraction = (Date) request.getSession().getAttribute(
					"finExtractionCV");
			String nomGroupe = request.getSession().getAttribute("nomGroupe")
					.toString();

			HttpSession session = (HttpSession) request.getSession()
					.getAttribute("session");

			SalarieServiceImpl salarieService = new SalarieServiceImpl();
			PiecesJustificativesServiceImpl pjserv = new PiecesJustificativesServiceImpl();
			PersonneAChargeServiceImpl pacserv = new PersonneAChargeServiceImpl();

			List<SalarieBeanExport> salarieBeanList = new ArrayList<SalarieBeanExport>();
			List<PiecesJustificativesBean> pjBeanList = new ArrayList<PiecesJustificativesBean>();
			List<PersonneAChargeBean> pacBeanList = new ArrayList<PersonneAChargeBean>();

			SalarieBeanExport salarieBeanExport = salarieService
					.getSalarieBeanExportById(idSalarie);

			PiecesJustificativesBean pj = new PiecesJustificativesBean();
			if (pjserv.getPiecesJustificativesBeanListByIdSalarie(idSalarie) != null
					&& pjserv.getPiecesJustificativesBeanListByIdSalarie(
							idSalarie).size() > 0) {
				pj = pjserv.getPiecesJustificativesBeanListByIdSalarie(
						idSalarie).get(0);
			}

			pjBeanList.add(pj);

			pacBeanList = pacserv
					.getPersonneAChargeBeanListByIdSalarie(idSalarie);

			salarieBeanExport.setPersonneAChargeBeanList(pacBeanList);

			List<AbsenceBean> absenceBeanList = salarieBeanExport
					.getAbsenceBeanList();

			Iterator<AbsenceBean> ite = absenceBeanList.iterator();

			while (ite.hasNext()) {
				AbsenceBean absenceBean = ite.next();
				if (!absenceBean.getNomTypeAbsence().startsWith("Absence pour")) {
					ite.remove();
				}
			}

			salarieBeanExport.setAbsenceBeanList(absenceBeanList);

			Collections.sort(salarieBeanExport.getHabilitationBeanList(),
					Collections.reverseOrder());
			Collections.sort(salarieBeanExport.getFormationBeanList(),
					Collections.reverseOrder());
			Collections.sort(salarieBeanExport.getParcoursBeanList(),
					Collections.reverseOrder());
			Collections.sort(salarieBeanExport.getAbsenceBeanList(),
					Collections.reverseOrder());
			Collections.sort(salarieBeanExport.getAccidentBeanList(),
					Collections.reverseOrder());

			if (debutExtraction != null && finExtraction != null) {
				Calendar debut = new GregorianCalendar();
				debut.setTime(debutExtraction);
				Calendar fin = new GregorianCalendar();
				fin.setTime(finExtraction);

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				salarieBeanExport.setDebutExtraction(dateFormat
						.format(debutExtraction));
				salarieBeanExport.setFinExtraction(dateFormat
						.format(finExtraction));

				List<ParcoursBean> listP = new ArrayList<ParcoursBean>();
				listP = salarieBeanExport.getParcoursBeanList();
				Iterator<ParcoursBean> iteP = listP.iterator();

				while (iteP.hasNext()) {
					ParcoursBean p = iteP.next();
					Calendar debutP = new GregorianCalendar();
					debutP.setTime(p.getDebutFonction());
					Calendar finP = new GregorianCalendar();
					if (p.getFinFonction() != null) {
						finP.setTime(p.getFinFonction());
					} else {
						finP = null;
					}
					if (finP == null) {
						if (debutP.before(debut) || debutP.after(fin)) {
							iteP.remove();
						}
					} else {
						if (debutP.before(debut) || finP.after(fin)
								|| debutP.after(fin) || finP.before(debut)) {
							iteP.remove();
						}
					}
				}
				salarieBeanExport.setParcoursBeanList(listP);

				List<FormationBean> listF = new ArrayList<FormationBean>();
				listF = salarieBeanExport.getFormationBeanList();

				Iterator<FormationBean> iteF = listF.iterator();

				while (iteF.hasNext()) {
					FormationBean f = iteF.next();
					Calendar debutF = new GregorianCalendar();
					debutF.setTime(f.getDebutFormation());
					Calendar finF = new GregorianCalendar();
					if (f.getFinFormation() != null) {
						finF.setTime(f.getFinFormation());
					} else {
						finF = null;
					}
					if (finF == null) {
						if (debutF.before(debut) || debutF.after(fin)) {
							iteF.remove();
						}
					} else {
						if (debutF.before(debut) || finF.after(fin)
								|| debutF.after(fin) || finF.before(debut)) {
							iteF.remove();
						}
					}
				}
				salarieBeanExport.setFormationBeanList(listF);

				List<AbsenceBean> listA = new ArrayList<AbsenceBean>();
				listA = salarieBeanExport.getAbsenceBeanList();

				Iterator<AbsenceBean> iteAbs = listA.iterator();

				while (iteAbs.hasNext()) {
					AbsenceBean a = iteAbs.next();
					Calendar debutA = new GregorianCalendar();
					debutA.setTime(a.getDebutAbsence());
					Calendar finA = new GregorianCalendar();
					if (a.getFinAbsence() != null) {
						finA.setTime(a.getFinAbsence());
					} else {
						finA = null;
					}
					if (finA == null) {
						if (debutA.before(debut) || debutA.after(fin)) {
							iteAbs.remove();
						}
					} else {
						if (debutA.before(debut) || finA.after(fin)
								|| debutA.after(fin) || finA.before(debut)) {
							iteAbs.remove();
						}
					}
				}
				salarieBeanExport.setAbsenceBeanList(listA);

				List<HabilitationBean> listH = new ArrayList<HabilitationBean>();
				listH = salarieBeanExport.getHabilitationBeanList();

				Iterator<HabilitationBean> iteH = listH.iterator();

				while (iteH.hasNext()) {
					HabilitationBean h = iteH.next();
					Calendar finH = new GregorianCalendar();
					if (h.getExpiration() != null) {
						finH.setTime(h.getExpiration());
					} else {
						finH = null;
					}
					if (finH != null) {
						if (finH.after(fin) || finH.before(debut)) {
							iteH.remove();
						}
					}
				}
				salarieBeanExport.setHabilitationBeanList(listH);

				List<AccidentBean> listAcc = new ArrayList<AccidentBean>();
				listAcc = salarieBeanExport.getAccidentBeanList();

				Iterator<AccidentBean> iteAcc = listAcc.iterator();

				while (iteAcc.hasNext()) {
					AccidentBean a = iteAcc.next();
					Calendar debutA = new GregorianCalendar();
					if (a.getDateAccident() != null) {
						debutA.setTime(a.getDateAccident());
					} else {
						debutA = null;
					}
					if (debutA != null) {
						if (debutA.before(debut) || debutA.after(fin)) {
							iteAcc.remove();
						}
					}
				}
				salarieBeanExport.setAccidentBeanList(listAcc);

				List<EntretienBean> listE = new ArrayList<EntretienBean>();
				listE = salarieBeanExport.getEntretienBeanList();

				Iterator<EntretienBean> iteE = listE.iterator();

				while (iteE.hasNext()) {
					EntretienBean e = iteE.next();
					Calendar debutE = new GregorianCalendar();
					if (e.getDateEntretien() != null) {
						debutE.setTime(e.getDateEntretien());
					} else {
						debutE = null;
					}
					if (debutE != null) {
						if (debutE.before(debut) || debutE.after(fin)) {
							iteE.remove();
						}
					}
				}
				salarieBeanExport.setEntretienBeanList(listE);

			} else {
				salarieBeanExport.setDebutExtraction(null);
				salarieBeanExport.setFinExtraction(null);
			}

			salarieBeanExport.setPj(pj);

			EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
			salarieBeanExport.setNomEntreprise(serv.getEntrepriseBeanById(
					salarieBeanExport.getIdEntrepriseSelected()).getNom());
			salarieBeanExport.setJustificatif(serv.getEntrepriseBeanById(
					salarieBeanExport.getIdEntrepriseSelected())
					.getJustificatif());
			salarieBeanExport.setIdEntreprise(salarieBeanExport
					.getIdEntrepriseSelected());
			salarieBeanExport.setUrl(Utils.getFileUrl(
					salarieBeanExport.getIdEntrepriseSelected(),
					"logo_entreprise", false, false, true, true, nomGroupe));

			salarieBeanExport.setUrlChecked(Utils.getImgPath(session)
					+ "checked.jpg");
			salarieBeanExport.setUrlUnchecked(Utils.getImgPath(session)
					+ "unchecked.jpg");

			ServiceImpl s = new ServiceImpl();
			salarieBeanExport.setService(s.getServiceBeanById(
					salarieBeanExport.getIdServiceSelected()).getNom());

			salarieBeanExport.setAnciennete(getAnciennete(salarieBeanExport));

			if (salarieBeanExport.getIdLienSubordination() != null) {
				if (salarieBeanExport.getIdLienSubordination() != 0) {
					SalarieBean sal = salarieService
							.getSalarieBeanById(salarieBeanExport
									.getIdLienSubordination());
					salarieBeanExport.setNomLienSubordination(sal.getNom()
							+ " " + sal.getPrenom());
				} else {
					salarieBeanExport.setNomLienSubordination("");
				}
			} else {
				salarieBeanExport.setNomLienSubordination("");
			}

			salarieBeanList.add(salarieBeanExport);

			JRDataSource jRDataSource = new JRBeanCollectionDataSource(
					salarieBeanList);

			String realPath = getServletConfig().getServletContext()
					.getRealPath(
							"/reportTemplates/FicheIndividuelleSalarie.jrxml");

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
					"attachment;filename=ficheIndividuelle.pdf");
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

	private String getAnciennete(SalarieBeanExport salarie) {
		ParcoursBean parcourDeb = getFirstParcours(salarie);
		Calendar dateDebut = new GregorianCalendar();
		Calendar dateFin = new GregorianCalendar();
		if (parcourDeb != null) {
			dateDebut.setTime(parcourDeb.getDebutFonction());
		}
		ParcoursBean parcourFin = getLastParcours(salarie);
		if (parcourFin != null) {
			if (parcourFin.getFinFonction() != null
					&& parcourFin.getFinFonction().before(new Date())) {
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
