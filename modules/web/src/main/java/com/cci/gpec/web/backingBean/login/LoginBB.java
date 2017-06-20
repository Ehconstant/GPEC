package com.cci.gpec.web.backingBean.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.TransmissionBean;
import com.cci.gpec.commons.UserBean;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.TransmissionServiceImpl;
import com.cci.gpec.metier.implementation.UserServiceImpl;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;

public class LoginBB {
	
	private static final Logger				LOGGER					= LoggerFactory.getLogger(LoginBB.class);

	private String login;
	private String password;
	private String profile;
	private UserBean user;
	private boolean level0;
	private boolean level1;
	private boolean level2;
	private boolean isLogged;
	private boolean delog = true;

	private boolean evenement;
	private boolean remuneration;
	private boolean ficheDePoste;
	private boolean entretien;
	private boolean contratTravail;
	private boolean admin;
	private boolean superAdmin;

	private boolean modalTransmissionRendered = false;
	private boolean modalRenderedForgottenPwd = false;
	private boolean displayMailConfirm = false;

	private String mailResetPwd;

	private boolean renderedWarningVersionEssai = false;
	private String finPeriodeEssai = "";

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	public LoginBB() {
		super();
	}

	public static String hashPassword(String password)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(password.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		return sb.toString();
	}

	public String goHome() {
		return "accueil";
	}

	public String log() throws Exception {

		// UserServiceImpl servb = new UserServiceImpl();
		// UserBean us = new UserBean();
		// us.setLogin("nrm");
		// us.setPassword(hashPassword("nrm"));
		// us.setProfile("level2");
		// servb.saveOrUppdate(us);

		displayMailConfirm = false;

		UserServiceImpl serv = new UserServiceImpl();
		user = serv.getUser(login, hashPassword(password));

		if (user != null) {
			user.setLogin(login);
			if (!user.isSuperAdmin()) {
				GroupeServiceImpl grServ = new GroupeServiceImpl();
				GroupeBean groupe = grServ
						.getGroupeBeanById(user.getIdGroupe());
				GregorianCalendar today = new GregorianCalendar();
				today.setTime(new Date());
				if (groupe.getFinPeriodeEssai() != null) {
					if (groupe.getFinPeriodeEssai().equals(today.getTime())
							|| groupe.getFinPeriodeEssai().before(
									today.getTime())) {
						session.setAttribute("logged", "no");
						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"La période d'essai est terminée, veuillez contacter votre conseillère CCI pour poursuivre l'utilisation de cette application",
								"La période d'essai est terminée, veuillez contacter votre conseillère CCI pour poursuivre l'utilisation de cette application");
						FacesContext.getCurrentInstance().addMessage(
								"j_id52:username", message);
						login = null;
						password = null;
						return "error";
					}
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(groupe.getFinPeriodeEssai());
					cal.add(Calendar.DATE, -10);
					if (today.getTime().after(cal.getTime())) {
						renderedWarningVersionEssai = true;

						SimpleDateFormat formater = new SimpleDateFormat(
								"dd/MM/yyyy");

						finPeriodeEssai = formater.format(groupe
								.getFinPeriodeEssai());
					} else {
						renderedWarningVersionEssai = false;
					}
				} else {
					renderedWarningVersionEssai = false;
				}
			}

			session.setAttribute("logged", "yes");
			session.setAttribute("level", user.getProfile());

			session.setAttribute("groupe", user.getIdGroupe());

			session.setAttribute("evenement", user.isEvenement());
			session.setAttribute("remuneration", user.isRemuneration());
			session.setAttribute("ficheDePoste", user.isFicheDePoste());
			session.setAttribute("entretien", user.isEntretien());
			session.setAttribute("contratTravail", user.isContratTravail());
			session.setAttribute("admin", user.isAdmin());
			session.setAttribute("superAdmin", user.isSuperAdmin());

			login = null;
			password = null;

			String c = Utils.init();
			// transfert();
			if (!user.isSuperAdmin()) {
				TransmissionServiceImpl transmissionService = new TransmissionServiceImpl();
				TransmissionBean transmissionBean = transmissionService
						.getTransmissionBean(Integer.parseInt(session
								.getAttribute("groupe").toString()));
				if (transmissionBean.getDateTransmission() != null) {
					GregorianCalendar date = new GregorianCalendar();
					date.add(Calendar.MONTH, -6);
					if (transmissionBean.getDateTransmission().compareTo(
							date.getTime()) < 0) {
						date.add(Calendar.MONTH, 5);
						if (transmissionBean.getDateDerniereDemande()
								.compareTo(date.getTime()) < 0) {
							modalTransmissionRendered = true;

						}
					}
				} else {
					GregorianCalendar date = new GregorianCalendar();
					date.add(Calendar.MONTH, -5);
					transmissionBean.setDateTransmission(date.getTime());
					transmissionService.saveOrUppdate(transmissionBean,
							Integer.parseInt(session.getAttribute("groupe")
									.toString()));
				}
				SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
						.getCurrentInstance().getCurrentInstance()
						.getExternalContext().getSessionMap()
						.get("salarieFormBB");
				salarieFormBB = new SalarieFormBB();
			}
			return "accueil";
		} else {
			session.setAttribute("logged", "no");
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"La combinaison Identifiant/Mot de passe est incorrecte",
					"La combinaison Identifiant/Mot de passe est incorrecte");
			LOGGER.info("Error while trying to log in. Username : " + login + " . Password : " + password + " .");
			FacesContext.getCurrentInstance().addMessage("j_id52:username", message);
			login = null;
			password = null;
			return "error";
		}

	}

	public void toggleModalRenderedForgottenPwd() {
		this.mailResetPwd = "";
		modalRenderedForgottenPwd = !modalRenderedForgottenPwd;
	}

	public void annulerReset() {
		this.mailResetPwd = "";
		modalRenderedForgottenPwd = !modalRenderedForgottenPwd;
	}

	public void validerReset(ActionEvent evt) throws Exception {
		UserServiceImpl usrServ = new UserServiceImpl();
		UserBean user = usrServ.getUserByLoginExceptSuperAdmin(mailResetPwd);
		if (user != null) {
			String password = Utils.generatePassword(8);

			String subject = "GPEC - mise à jour mot de passe";
			String content = "<html><body>Bonjour,<br/><br/>Votre mot de passe a été réinitialisé. Voici votre nouveau mot de passe :<br/>"
					+ "Login : "
					+ user.getLogin()
					+ "<br/>"
					+ "Mot de passe : "
					+ password
					+ "<br/><br/>Cordialement,<br/>L'équipe GPEC.</body></html>";
			try {
				Utils.sendHtmlEmail(mailResetPwd, subject, content);
			} catch (EmailException e) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Erreur lors de l'envoi du mail.",
						"Erreur lors de l'envoi du mail.");
				FacesContext.getCurrentInstance().addMessage("j_id52:mail",
						message);
				displayMailConfirm = false;
				e.printStackTrace();
			}
			user.setPassword(Utils.hashPassword(password));
			usrServ.saveOrUppdate(user, user.getIdGroupe());
			displayMailConfirm = true;
			toggleModalRenderedForgottenPwd();
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Aucun compte n'existe avec cet identifiant.",
					"Aucun compte n'existe avec cet identifiant.");
			FacesContext.getCurrentInstance()
					.addMessage("loginForm:mail", message);
			displayMailConfirm = false;
		}

	}

	public void toggleModalTransmissionRendered(ActionEvent event)
			throws Exception {
		TransmissionServiceImpl transmissionService = new TransmissionServiceImpl();
		TransmissionBean transmissionBean = transmissionService
				.getTransmissionBean(Integer.parseInt(session.getAttribute(
						"groupe").toString()));
		transmissionBean.setDateDerniereDemande(new Date());
		transmissionService.saveOrUppdate(transmissionBean,
				Integer.parseInt(session.getAttribute("groupe").toString()));
		modalTransmissionRendered = !modalTransmissionRendered;
	}

	public boolean isModalTransmissionRendered() throws Exception {
		return modalTransmissionRendered;
	}

	public void setModalTransmissionRendered(boolean modalTransmissionRendered) {
		this.modalTransmissionRendered = modalTransmissionRendered;
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

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public boolean isLevel0() {
		if (session.getAttribute("level") != null) {
			return session.getAttribute("level").equals("level0");
		} else {
			return false;
		}
	}

	public void setLevel0(boolean level0) {
		this.level0 = level0;
	}

	public boolean isLevel1() {
		if (session.getAttribute("level") != null) {
			return session.getAttribute("level").equals("level1");
		} else {
			return false;
		}
	}

	public void setLevel1(boolean level1) {
		this.level1 = level1;
	}

	public boolean isLevel2() {
		if (session.getAttribute("level") != null) {
			return session.getAttribute("level").equals("level2");
		} else {
			return false;
		}
	}

	public void setLevel2(boolean level2) {
		this.level2 = level2;
	}

	public boolean isLogged() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		if (session.getAttribute("logged") != null
				&& session.getAttribute("logged").equals("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setLogged(boolean isLogged) {
		this.isLogged = isLogged;
	}

	public boolean isEvenement() {
		if (session.getAttribute("evenement") != null) {
			return session.getAttribute("evenement").toString().equals("true");
		} else {
			return false;
		}
	}

	public void setEvenement(boolean evenement) {
		this.evenement = evenement;
	}

	public boolean isContratTravail() {
		if (session.getAttribute("contratTravail") != null) {
			return session.getAttribute("contratTravail").toString()
					.equals("true");
		} else {
			return false;
		}
	}

	public void setContratTravail(boolean contratTravail) {
		this.contratTravail = contratTravail;
	}

	public boolean isRemuneration() {
		if (session.getAttribute("remuneration") != null) {
			return session.getAttribute("remuneration").toString()
					.equals("true");
		} else {
			return false;
		}
	}

	public void setRemuneration(boolean remuneration) {
		this.remuneration = remuneration;
	}

	public boolean isAdmin() {
		if (session.getAttribute("admin") != null) {
			return session.getAttribute("admin").toString().equals("true");
		} else {
			return false;
		}
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isSuperAdmin() {
		if (session.getAttribute("superAdmin") != null) {
			return session.getAttribute("superAdmin").toString().equals("true");
		} else {
			return false;
		}
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	public boolean isFicheDePoste() {
		if (session.getAttribute("ficheDePoste") != null) {
			return session.getAttribute("ficheDePoste").toString()
					.equals("true");
		} else {
			return false;
		}
	}

	public void setFicheDePoste(boolean ficheDePoste) {
		this.ficheDePoste = ficheDePoste;
	}

	public boolean isEntretien() {
		if (session.getAttribute("entretien") != null) {
			return session.getAttribute("entretien").toString().equals("true");
		} else {
			return false;
		}
	}

	public void setEntretien(boolean entretien) {
		this.entretien = entretien;
	}

	public boolean isDelog() {
		session.setAttribute("logged", "no");

		// session.invalidate();
		return delog;
	}

	public boolean isRenderedWarningVersionEssai() {
		return renderedWarningVersionEssai;
	}

	public void setRenderedWarningVersionEssai(
			boolean renderedWarningVersionEssai) {
		this.renderedWarningVersionEssai = renderedWarningVersionEssai;
	}

	public String getFinPeriodeEssai() {
		return finPeriodeEssai;
	}

	public void setFinPeriodeEssai(String finPeriodeEssai) {
		this.finPeriodeEssai = finPeriodeEssai;
	}

	public boolean isModalRenderedForgottenPwd() {
		return modalRenderedForgottenPwd;
	}

	public void setModalRenderedForgottenPwd(boolean modalRenderedForgottenPwd) {
		this.modalRenderedForgottenPwd = modalRenderedForgottenPwd;
	}

	public String getMailResetPwd() {
		return mailResetPwd;
	}

	public void setMailResetPwd(String mailResetPwd) {
		this.mailResetPwd = mailResetPwd;
	}

	public boolean isDisplayMailConfirm() {
		return displayMailConfirm;
	}

	public void setDisplayMailConfirm(boolean displayMailConfirm) {
		this.displayMailConfirm = displayMailConfirm;
	}

	public UserBean getUser() throws Exception {
		UserServiceImpl usrServ = new UserServiceImpl();
		return usrServ.getUserByLogin(user.getLogin());
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

}
