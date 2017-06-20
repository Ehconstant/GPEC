package com.cci.gpec.web.backingBean.VersionEssai;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.mail.EmailException;

import com.cci.gpec.commons.UserBean;
import com.cci.gpec.metier.implementation.UserServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.BackingBeanForm;

public class VersionEssaiFormBB extends BackingBeanForm {

	private String nomEntreprise;
	private String nomAdmin;
	private String prenomAdmin;
	private String telephoneAdmin;
	private String mailAdmin;

	private boolean confirmDemande = false;

	public void checkMail(ValueChangeEvent evt) {
		if (evt != null) {
			mailAdmin = evt.getNewValue().toString();
		}
		if (mailAdmin != null && !mailAdmin.equals("")) {
			String emailRegex = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)";
			Pattern emailPattern = Pattern.compile(emailRegex);
			Matcher matcher = emailPattern.matcher(mailAdmin);
			if (!matcher.matches()) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Email non valide.",
						"Email non valide.");
				FacesContext.getCurrentInstance().addMessage("idForm:mail",
						message);
			}
		}
	}

	public void saveOrUpdateUser(ActionEvent event)
			throws NoSuchAlgorithmException, InterruptedException {

		if (mailAdmin != null && !mailAdmin.equals("")) {
			String emailRegex = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)";
			Pattern emailPattern = Pattern.compile(emailRegex);
			Matcher matcher = emailPattern.matcher(mailAdmin);
			if (!matcher.matches()) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Email non valide.",
						"Email non valide.");
				FacesContext.getCurrentInstance().addMessage("idForm:mail",
						message);
				if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
					event.setPhaseId(PhaseId.INVOKE_APPLICATION);
					event.queue();
					return;
				}
				return;
			}
		}

		if (telephoneAdmin != null && !telephoneAdmin.equals("")) {
			String telRegex = "^0[0-9]([ .-]?[0-9]{2}){4}$";
			Pattern telPattern = Pattern.compile(telRegex);
			Matcher matcher = telPattern.matcher(telephoneAdmin);
			if (!matcher.matches()) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Numéro de téléphone non valide.",
						"Numéro de téléphone non valide.");
				FacesContext.getCurrentInstance().addMessage(
						"idForm:telephoneAdmin", message);
				if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
					event.setPhaseId(PhaseId.INVOKE_APPLICATION);
					event.queue();
					return;
				}
				return;
			}
		}

		UserBean user = new UserBean();
		user.setId(0);
		user.setNomEntreprise(nomEntreprise);
		user.setNom(nomAdmin);
		user.setPrenom(prenomAdmin);
		user.setTelephone(telephoneAdmin);
		user.setLogin(mailAdmin);
		user.setDateDemandeVersionEssai(new Date());
		user.setEvenement(true);
		user.setRemuneration(true);
		user.setFicheDePoste(true);
		user.setEntretien(true);
		user.setContratTravail(true);
		user.setAdmin(true);
		user.setSuperAdmin(false);

		UserServiceImpl userServ = new UserServiceImpl();
		userServ.saveOrUppdate(user, 0);

		String subject = "GPEC - demande d'accès à la version d'essai";
		String content = "<html><body>Bonjour,<br/><br/>l'entreprise \""
				+ nomEntreprise
				+ "\" a fait une demande d'accès à la version d'essai."
				+ "<br/><br/>Cordialement,<br/>L'équipe GPEC.</body></html>";
		try {
			Properties mailSettings = Utils.getEmailPropertiesFromClasspath("conf/emailSettings.properties");
			String mail = mailSettings.getProperty("mail.contact");
			Utils.sendHtmlEmail(mail, subject, content);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EmailException e) {
			e.printStackTrace();
		}
		confirmDemande = true;
	}

	public void cancel() {
		nomEntreprise = "";
		nomAdmin = "";
		prenomAdmin = "";
		telephoneAdmin = "";
		mailAdmin = "";
		confirmDemande = false;
	}

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public String getNomAdmin() {
		return nomAdmin;
	}

	public void setNomAdmin(String nomAdmin) {
		this.nomAdmin = nomAdmin;
	}

	public String getPrenomAdmin() {
		return prenomAdmin;
	}

	public void setPrenomAdmin(String prenomAdmin) {
		this.prenomAdmin = prenomAdmin;
	}

	public String getTelephoneAdmin() {
		return telephoneAdmin;
	}

	public void setTelephoneAdmin(String telephoneAdmin) {
		this.telephoneAdmin = telephoneAdmin;
	}

	public String getMailAdmin() {
		return mailAdmin;
	}

	public void setMailAdmin(String mailAdmin) {
		this.mailAdmin = mailAdmin;
	}

	public boolean isConfirmDemande() {
		return confirmDemande;
	}

	public void setConfirmDemande(boolean confirmDemande) {
		this.confirmDemande = confirmDemande;
	}

}
