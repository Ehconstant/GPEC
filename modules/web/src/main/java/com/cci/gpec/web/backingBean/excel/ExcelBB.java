package com.cci.gpec.web.backingBean.excel;

import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.SalarieBeanExport;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.web.ftp.Ftp;

public class ExcelBB {

	private WritableWorkbook workbook;
	private File file;
	private WritableSheet sheet;
	private String host, login, password, port;
	private String salariePath;
	private int idEntreprise;
	private EntrepriseBean entrepriseBean;
	private List<SalarieBeanExport> salariesInventory;
	public String erreur;

	public ExcelBB(String host, String login, String password, String port,
			String salariePath, int idEntreprise,
			List<SalarieBeanExport> salariesInventory) throws Exception {

		this.host = host;
		this.login = login;
		this.password = password;
		this.port = port;
		this.salariePath = salariePath;
		this.idEntreprise = idEntreprise;
		this.salariesInventory = salariesInventory;
		init();
	}

	public String init() throws Exception {
		file = new File(getNameOfFile() + ".xls");
		workbook = Workbook.createWorkbook(file);
		sheet = workbook.createSheet("First Sheet", 0);
		setEntete();
		fillSheet();
		end();
		send();
		file.delete();
		return "";
	}

	private String getNameOfFile() throws Exception {
		EntrepriseServiceImpl serv = new EntrepriseServiceImpl();
		EntrepriseBean bean = serv.getEntrepriseBeanById(idEntreprise);
		entrepriseBean = bean;
		return bean.getNumSiret() + "_" + bean.getCodeApe();

	}

	private void end() throws Exception {
		workbook.write();
		workbook.close();
	}

	private String send() {
		try {
			Ftp ftp;
			ftp = new Ftp();
			ftp.connexion(host, login, password, Integer.parseInt(port));
			ftp.upload(file, this.salariePath);
			ftp.deconnexion();
		} catch (NumberFormatException e) {
			erreur = "Le num\u00e9ro de port de connexion du ftp n'est pas valide. V\u00e9rifiez dans le fichier de configuration du ftp.";
		} catch (IllegalStateException e) {
			erreur = "L'application n'est pas pr\u00eate \u00e0 \u00eatre appel\u00e9e.";
		} catch (IOException e) {
			erreur = "Erreur pendant la connexion au FTP. Veuillez v\u00E9rifier votre configuration ou votre connexion internet.";
		} catch (FTPIllegalReplyException e) {
			erreur = "Erreur pendant la connexion au FTP.";
		} catch (FTPException e) {
			erreur = "Erreur pendant la connexion au FTP.";
		} catch (Exception e) {
			erreur = "Erreur pendant la transmission \u00E0 la CCI. Veuillez v\u00E9rifier votre configuration ou votre connexion internet.";
		}

		return "";
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
			/* && !parcourFin.getFinFonction().equals(UtilsDate.FIN_NULL) */) {
				dateFin.setTime(parcourFin.getFinFonction());
			} else {
				dateFin.setTime(new Date());
			}
		}
		return dateFin.get(Calendar.YEAR) - dateDebut.get(Calendar.YEAR);
	}

	public String getDateEntree(SalarieBean s) {
		ParcoursBean pb = getFirstParcours(s);
		if (pb != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			return dateFormat.format(pb.getDebutFonction());
		} else {
			return "";
		}
	}

	public String getDateSortie(SalarieBean s) {
		ParcoursBean pb = getLastParcours(s);
		if (pb != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
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

	private void fillSheet() throws Exception {
		int maxSizeDateNaissance = 0;
		int maxSizeEmploi = 10;
		int maxSizeDateD = 0;
		// int maxSizeDateF=0;
		int maxSizeNbHrsFormCumul = 10;
		int maxSizeSoldeDif = 10;
		int maxSizeAnciennete = 14;
		int maxSizeTypeContrat = 10;
		int maxSizeEquivTpsPlein = 10;
		int maxSizeCSP = 10;

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		WritableFont font = new WritableFont(WritableFont.TIMES, 11,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		format.setAlignment(Alignment.CENTRE);
		sheet.mergeCells(5, 0, 8, 0);
		Label label = new Label(5, 0,
				"Caract\u00e9ristiques de l'entreprise des salari\u00e9s");
		sheet.setRowView(0, 700);
		label.setCellFormat(format);
		sheet.addCell(label);
		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		int val = 1;
		label = new Label(5, val, "Entreprise");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(6, val, "Num. Siret");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(7, val, "Code ape");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(8, val, "Date de l'envoi");
		label.setCellFormat(format);
		sheet.addCell(label);
		font = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
		format = new WritableCellFormat(font);
		format.setAlignment(Alignment.CENTRE);
		label = new Label(5, val + 1, entrepriseBean.getNom());
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(6, val + 1, entrepriseBean.getNumSiret() + "");
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(7, val + 1, entrepriseBean.getCodeApe());
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(8, val + 1, dateFormat.format(new Date()));
		label.setCellFormat(format);
		sheet.addCell(label);
		int row = 5;
		for (int i = 0; i < salariesInventory.size(); i++) {
			SalarieBean salarie = salariesInventory.get(i);
			// Date de naisance
			String dat = dateFormat.format(salarie.getDateNaissance());
			label = new Label(0, row, dat);
			// sheet.setColumnView(10, 12);
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeDateNaissance < label.getContents().length()) {
				maxSizeDateNaissance = label.getContents().length();
			}

			// Age
			label = new Label(1, row, calculer(salarie.getDateNaissance()) + "");
			label.setCellFormat(format);
			sheet.addCell(label);

			// Sexe
			String civilite = "";
			if (salarie.getCivilite().equals("Monsieur")) {
				civilite = "H";
			} else {
				civilite = "F";
			}
			label = new Label(2, row, civilite);
			label.setCellFormat(format);
			sheet.addCell(label);

			// Date entree
			String dateEntree = getDateEntree(salarie);
			label = new Label(3, row, dateEntree);
			label.setCellFormat(format);
			sheet.addCell(label);

			// Date sortie
			String sortie = getDateSortie(salarie);
			label = new Label(4, row, sortie);
			label.setCellFormat(format);
			sheet.addCell(label);

			// Metier
			ParcoursBean pb = getLastParcours(salarie);
			if (pb != null) {
				label = new Label(5, row, pb.getNomMetier());
			} else {
				label = new Label(5, row, "");
			}
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeEmploi < label.getContents().length()) {
				maxSizeEmploi = label.getContents().length();
			}

			// Debut fonction
			if (pb != null) {
				String date2 = dateFormat.format(pb.getDebutFonction());
				label = new Label(6, row, date2);
			} else {
				label = new Label(6, row, "");
			}
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeDateD < label.getContents().length()) {
				maxSizeDateD = label.getContents().length();
			}

			// Volume horaire formation
			label = new Label(7, row, nbreHeureFormation(salarie) + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeNbHrsFormCumul < label.getContents().length()) {
				maxSizeNbHrsFormCumul = label.getContents().length();
			}

			// Solde DIF
			label = new Label(8, row, salarie.getCreditDif() + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeSoldeDif < label.getContents().length()) {
				maxSizeSoldeDif = label.getContents().length();
			}

			// Ancieneté
			label = new Label(9, row, getAnciennete(salarie) + "");
			label.setCellFormat(format);
			sheet.addCell(label);
			if (maxSizeAnciennete < label.getContents().length()) {
				maxSizeAnciennete = label.getContents().length();
			}

			if (getLastParcours(salarie) != null) {
				// Contrat
				label = new Label(10, row, getLastParcours(salarie)
						.getNomTypeContrat());
				label.setCellFormat(format);
				sheet.addCell(label);
				if (maxSizeTypeContrat < label.getContents().length()) {
					maxSizeTypeContrat = label.getContents().length();
				}

				// Equivalence temps plein
				label = new Label(11, row, getLastParcours(salarie)
						.getEquivalenceTempsPlein().intValue()
						+ "%");
				label.setCellFormat(format);
				sheet.addCell(label);
				if (maxSizeEquivTpsPlein < label.getContents().length()) {
					maxSizeEquivTpsPlein = label.getContents().length();
				}

				// CSP
				label = new Label(12, row, getLastParcours(salarie)
						.getNomTypeStatut());
				label.setCellFormat(format);
				sheet.addCell(label);
				if (maxSizeCSP < label.getContents().length()) {
					maxSizeCSP = label.getContents().length();
				}
			} else {
				label = new Label(10, row, "");
				label.setCellFormat(format);
				sheet.addCell(label);
				// Equivalence temps plein
				label = new Label(11, row, "");
				label.setCellFormat(format);
				sheet.addCell(label);
				// CSP
				label = new Label(12, row, "");
				label.setCellFormat(format);
				sheet.addCell(label);
			}

			row++;
		}

		sheet.setColumnView(0, maxSizeDateNaissance);
		sheet.setColumnView(1, 10);
		sheet.setColumnView(2, 10);
		sheet.setColumnView(3, 13);
		sheet.setColumnView(4, 13);
		sheet.setColumnView(5, maxSizeEmploi);
		sheet.setColumnView(6, 14);
		sheet.setColumnView(7, maxSizeNbHrsFormCumul);
		sheet.setColumnView(8, 14);
		sheet.setColumnView(9, maxSizeAnciennete);
		sheet.setColumnView(10, maxSizeTypeContrat);
		sheet.setColumnView(11, maxSizeEquivTpsPlein);
		sheet.setColumnView(12, maxSizeCSP);
	}

	private ParcoursBean getLastParcours(SalarieBean salarie) {
		List<ParcoursBean> parcourList = salarie.getParcoursBeanList();
		ParcoursBean pb = null;
		for (int i = 0; i < parcourList.size(); i++) {
			ParcoursBean parcour = parcourList.get(i);
			if (pb == null)
				pb = parcour;
			if (parcour.getDebutFonction().after(pb.getDebutFonction())) {
				pb = parcour;
			}
		}
		return pb;
	}

	private void setEntete() throws RowsExceededException, WriteException,
			IOException {
		int ligne = 4;
		sheet.setRowView(ligne, 1600);
		WritableFont font = new WritableFont(WritableFont.TIMES, 11,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(jxl.format.Alignment.CENTRE);
		format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		format.setWrap(true);
		Label label = new Label(0, ligne, "Date de naissance");
		sheet.setColumnView(0, 14);
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(1, ligne, "Age");
		sheet.setColumnView(1, 5);
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(2, ligne, "Sexe");
		sheet.setColumnView(2, 8);
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(3, ligne, "Entrée dans l'entreprise");
		sheet.setColumnView(3, 8);
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(4, ligne, "Sortie de l'entreprise");
		sheet.setColumnView(4, 8);
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(5, ligne, "M\u00E9tier");
		sheet.setColumnView(5, 12);
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(6, ligne, "Entr\u00E9e dans le poste");
		sheet.setColumnView(6, 15);
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(7, ligne, "Nombre d'hrs de formation cumul\u00E9es");
		sheet.setColumnView(7, 4);
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(8, ligne, "Solde DIF");
		sheet.setColumnView(8, 20);
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(9, ligne, "Anc.");
		sheet.setColumnView(9, 14);
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(10, ligne, "Type de contrat");
		sheet.setColumnView(10, 14);
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(11, ligne, "Equiv temps plein");
		sheet.setColumnView(11, 12);
		label.setCellFormat(format);
		sheet.addCell(label);
		label = new Label(12, ligne, "CSP");
		sheet.setColumnView(12, 8);
		label.setCellFormat(format);
		sheet.addCell(label);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSalariePath() {
		return salariePath;
	}

	public void setSalariePath(String salariePath) {
		this.salariePath = salariePath;
	}

	public int getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(int idEntreprise) {
		this.idEntreprise = idEntreprise;
	}

	public String getErreur() {
		return erreur;
	}

	public void setErreur(String erreur) {
		this.erreur = erreur;
	}
}
