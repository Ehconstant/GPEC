package com.cci.gpec.web.backingBean.navigation;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.cci.gpec.web.backingBean.ficheMetier.FicheMetierFormBB;

public class NavigationBB {

	private boolean modalLoginRendered = false;
	private boolean loggedRendered = false;
	private String password;
	private boolean personnelEntreprise = false;
	private boolean analyse = false;
	private boolean parametres = false;

	private String version;

	private boolean exitWithoutSaveSalarie = false;

	public NavigationBB() {
		this.exitWithoutSaveSalarie = false;
		ResourceBundle rb = ResourceBundle.getBundle("version");
		version = rb.getString("versionGPEC");
	}

	public boolean isModalLoginRendered() {
		return modalLoginRendered;
	}

	public void setModalLoginRendered(boolean modalLoginRendered) {
		this.modalLoginRendered = modalLoginRendered;
	}

	public boolean isLoggedRendered() {
		return loggedRendered;
	}

	public void setLoggedRendered(boolean loggedRendered) {
		this.loggedRendered = loggedRendered;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toggleModalLoginRendered() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("identifie") != null) {
			this.loggedRendered = true;
			return "tabCompetences";
		} else {
			this.loggedRendered = false;
			this.password = new String();
			modalLoginRendered = !modalLoginRendered;
			return "";
		}

	}

	public String log() {
		ResourceBundle bundle = ResourceBundle.getBundle("CCI");

		String mdp = bundle.getString("cci");

		if (mdp.equals(this.password)) {
			modalLoginRendered = false;
			loggedRendered = true;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);
			session.setAttribute("identifie", true);
			return "tabCompetences";
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Mot de passe non valide",
					"Mot de passe non valide");
			FacesContext.getCurrentInstance().addMessage(
					"idNavigation:TxtPwTabComp", message);

			return "";
		}
	}

	public boolean isExitWithoutSaveSalarie() {
		return exitWithoutSaveSalarie;
	}

	public void setExitWithoutSaveSalarie(boolean exitWithoutSaveSalarie) {
		this.exitWithoutSaveSalarie = exitWithoutSaveSalarie;
	}

	public String exportPyramideAge() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = true;
		parametres = false;
		return "exportPyramideAge";
	}

	public String exportPyramideAnciennete() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = true;
		parametres = false;
		return "exportPyramideAnciennete";
	}

	public String salaries() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = true;
		analyse = false;
		parametres = false;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("retour") != null)
			session.setAttribute("retour", null);

		return "salaries";
	}

	public String organigramme() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = true;
		analyse = false;
		parametres = false;
		return "organigramme";
	}

	public String tableauxSuivi() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = true;
		analyse = false;
		parametres = false;
		return "tableauxSuivi";
	}

	public String planningAbsence() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = true;
		analyse = false;
		parametres = false;
		return "planningAbsence";
	}

	public String filtreMultiCriteres() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = true;
		analyse = false;
		parametres = false;
		return "filtreMultiCriteres";
	}

	public String departRetraite() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = true;
		parametres = false;
		return "departRetraite";
	}

	public String heuresFormation() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = true;
		parametres = false;
		return "heuresFormation";
	}

	public String tabCompetences() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = true;
		parametres = false;
		return "tabCompetences";
	}

	public String negociationSalariale() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = true;
		parametres = false;
		return "negociationSalariale";
	}

	public String suiviEffectif() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = true;
		parametres = false;
		return "suiviEffectif";
	}

	public String projectionPS() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = true;
		parametres = false;
		return "projectionPS";
	}

	public String accidentMetier() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = true;
		parametres = false;
		return "accidentMetier";
	}

	public String arretTravail() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = true;
		parametres = false;
		return "arretTravail";
	}

	public String absencesMetier() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = true;
		parametres = false;
		return "absencesMetier";
	}

	public String absencesNature() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		return "absencesNature";
	}

	public String turnOver() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = true;
		parametres = false;
		return "turnOver";
	}

	public String update() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "update";
	}

	public String link() {
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "link";
	}

	public String admin() {
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "admin";
	}

	public String superAdmin() {
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "superAdmin";
	}

	// Action de la Partie Paramètres de l'application
	public String groupe() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "groupe";
	}

	public String entreprises() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "entreprises";
	}

	public String services() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "services";
	}

	public String metiers() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "metiers";
	}

	public String typesAbsences() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "typesAbsences";
	}

	public String typesHabilitation() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "typesHabilitation";
	}

	public String fraisProf() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "fraisProfs";
	}

	public String revenusComplementaires() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "revenusComplementaires";
	}

	public String paramsGeneraux() {
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "paramsGeneraux";
	}

	public String ficheMetier() throws Exception {

		FicheMetierFormBB ficheMetierFormBB = (FicheMetierFormBB) FacesContext
				.getCurrentInstance().getCurrentInstance().getExternalContext()
				.getSessionMap().get("ficheMetierFormBB");
		if (ficheMetierFormBB != null)
			ficheMetierFormBB.init();
		if (exitWithoutSaveSalarie) {
			exitWithoutSaveSalarie = false;
		}
		personnelEntreprise = false;
		analyse = false;
		parametres = true;
		return "ficheMetier";
	}

	public boolean isPersonnelEntreprise() {
		return personnelEntreprise;
	}

	public void setPersonnelEntreprise(boolean personnelEntreprise) {
		this.personnelEntreprise = personnelEntreprise;
	}

	public boolean isAnalyse() {
		return analyse;
	}

	public void setAnalyse(boolean analyse) {
		this.analyse = analyse;
	}

	public boolean isParametres() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("superAdmin") != null) {
			if (session.getAttribute("superAdmin").toString().equals("true")) {
				return true;
			} else {
				return parametres;
			}
		} else {
			return parametres;
		}
	}

	public void setParametres(boolean parametres) {
		this.parametres = parametres;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
