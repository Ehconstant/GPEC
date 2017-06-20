package com.cci.gpec.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class SalarieBeanExport extends SalarieBean {

	private String age;
	private String sexe;
	private String posteOccupe;
	private String depuis;
	private String typeContrat;
	private String ccp;
	private String dateNaissanceExport;
	private String dateEntree;
	private String dateSortie;
	private String nomLienSubordination;
	private String anciennete;
	private String service;
	private String debutExtraction;
	private String finExtraction;
	private String justificatif;
	private Integer idEntreprise;
	private String url;
	private String urlChecked;
	private String urlUnchecked;
	private String nomEntreprise;

	private PiecesJustificativesBean pj = new PiecesJustificativesBean();

	private Boolean pjCI;
	private Boolean pjASS;
	private Boolean pjPC;
	private Boolean pjRIB;
	private Boolean pjD;
	private Boolean pjCT;
	private Boolean pjAVM;
	private Boolean pjH;

	public PiecesJustificativesBean getPj() {
		return pj;
	}

	public void setPj(PiecesJustificativesBean pj) {
		this.pj = pj;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getAge() {
		age = new String();
		GregorianCalendar calendar = new GregorianCalendar();
		GregorianCalendar today = new GregorianCalendar();
		if (super.getDateNaissance() != null) {
			if (!super.getDateNaissance().equals("")) {
				// Initialisé à la date et l'heure courrante.
				calendar.setTime(super.getDateNaissance());

				int yearActuel = Calendar.getInstance().get(Calendar.YEAR);

				int yearNaissance = calendar.get(Calendar.YEAR);

				int ageTmp = 0;

				if (calendar.get(Calendar.MONTH) <= today.get(Calendar.MONTH))
					if (calendar.get(Calendar.MONTH) == today
							.get(Calendar.MONTH))
						if (calendar.get(Calendar.DAY_OF_MONTH) < today
								.get(Calendar.DAY_OF_MONTH))
							ageTmp = yearActuel - yearNaissance;
						else
							ageTmp = yearActuel - yearNaissance - 1;
					else
						ageTmp = yearActuel - yearNaissance;
				else
					ageTmp = yearActuel - yearNaissance - 1;

				age = "" + ageTmp;
			}
		}
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSexe() {

		if (super.getCivilite().equals("Monsieur")) {
			sexe = "M";
		} else {
			sexe = "F";
		}

		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getPosteOccupe() {
		return posteOccupe;
	}

	public void setPosteOccupe(String posteOccupe) {
		this.posteOccupe = posteOccupe;
	}

	public String getDateNaissanceExport() {
		return dateNaissanceExport;
	}

	public void setDateNaissanceExport(String dateNaissanceExport) {
		this.dateNaissanceExport = dateNaissanceExport;
	}

	public String getDepuis() throws ParseException {
		return depuis;
	}

	public void setDepuis(String depuis) {
		this.depuis = depuis;
	}

	public String getTypeContrat() {
		return typeContrat;
	}

	public void setTypeContrat(String typeContrat) {
		this.typeContrat = typeContrat;
	}

	public String getCcp() {
		return ccp;
	}

	public void setCcp(String ccp) {
		this.ccp = ccp;
	}

	// public String getNomLienSubordination() {
	// return nomLienSubordination;
	// }

	// public void setNomLienSubordination(String nomLienSubordination) {
	// this.nomLienSubordination = nomLienSubordination;
	// }

	public String getAnciennete() {
		return anciennete;
	}

	public void setAnciennete(String anciennete) {
		this.anciennete = anciennete;
	}

	public String getDateEntree() {
		ParcoursBean pb = getFirstParcours();
		if (pb != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			return dateFormat.format(pb.getDebutFonction());
		} else {
			return "";
		}
	}

	public void setDateEntree(String dateEntree) {
		this.dateEntree = dateEntree;
	}

	public String getDateSortie() {
		ParcoursBean pb = getLastParcours();
		if (pb != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			if (pb.getFinFonction() != null) {
				return dateFormat.format(pb.getFinFonction());
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	public void setDateSortie(String dateSortie) {
		this.dateSortie = dateSortie;
	}

	private ParcoursBean getFirstParcours() {
		List<ParcoursBean> parcourList = this.getParcoursBeanList();
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

	private ParcoursBean getLastParcours() {
		List<ParcoursBean> parcourList = this.getParcoursBeanList();

		ParcoursBean parcoursBean = null;
		if (parcourList != null && parcourList.size() > 0) {
			Collections.sort(parcourList);
			Collections.reverse(parcourList);
			parcoursBean = parcourList.get(0);
		}

		return parcoursBean;
	}

	public Boolean getPjCI() {
		return pj.isCarteIdentite();
	}

	public void setPjCI(Boolean pjCI) {
		this.pjCI = pjCI;
	}

	public Boolean getPjASS() {
		return pj.isAttestationSecu();
	}

	public void setPjASS(Boolean pjASS) {
		this.pjASS = pjASS;
	}

	public Boolean getPjPC() {
		return pj.isPermisConduire();
	}

	public void setPjPC(Boolean pjPC) {
		this.pjPC = pjPC;
	}

	public Boolean getPjRIB() {
		return pj.isRib();
	}

	public void setPjRIB(Boolean pjRIB) {
		this.pjRIB = pjRIB;
	}

	public Boolean getPjD() {
		return pj.isDiplomes();
	}

	public void setPjD(Boolean pjD) {
		this.pjD = pjD;
	}

	public Boolean getPjCT() {
		return pj.isCertificatTravail();
	}

	public void setPjCT(Boolean pjCT) {
		this.pjCT = pjCT;
	}

	public Boolean getPjAVM() {
		return pj.isAttestaionVisiteMedicale();
	}

	public void setPjAVM(Boolean pjAVM) {
		this.pjAVM = pjAVM;
	}

	public Boolean getPjH() {
		return pj.isHabilitations();
	}

	public void setPjH(Boolean pjH) {
		this.pjH = pjH;
	}

	public String getDebutExtraction() {
		return debutExtraction;
	}

	public void setDebutExtraction(String debutExtraction) {
		this.debutExtraction = debutExtraction;
	}

	public String getFinExtraction() {
		return finExtraction;
	}

	public void setFinExtraction(String finExtraction) {
		this.finExtraction = finExtraction;
	}

	public String getJustificatif() {
		return justificatif;
	}

	public void setJustificatif(String justificatif) {
		this.justificatif = justificatif;
	}

	public Integer getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(Integer idEntreprise) {
		this.idEntreprise = idEntreprise;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public String getNomLienSubordination() {
		return nomLienSubordination;
	}

	public void setNomLienSubordination(String nomLienSubordination) {
		this.nomLienSubordination = nomLienSubordination;
	}

	public String getUrlChecked() {
		return urlChecked;
	}

	public void setUrlChecked(String urlChecked) {
		this.urlChecked = urlChecked;
	}

	public String getUrlUnchecked() {
		return urlUnchecked;
	}

	public void setUrlUnchecked(String urlUnchecked) {
		this.urlUnchecked = urlUnchecked;
	}

}
