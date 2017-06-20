package com.cci.gpec.web.backingBean.Admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.EmailException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.AdminBean;
import com.cci.gpec.commons.ContratTravailBean;
import com.cci.gpec.commons.EntrepriseBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.EvenementBean;
import com.cci.gpec.commons.FicheDePosteBean;
import com.cci.gpec.commons.FicheMetierBean;
import com.cci.gpec.commons.FicheMetierEntrepriseBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.GroupeBean;
import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.LienRemunerationRevenuBean;
import com.cci.gpec.commons.MappingRepriseBean;
import com.cci.gpec.commons.MetierBean;
import com.cci.gpec.commons.ObjectifsEntretienBean;
import com.cci.gpec.commons.ParamsGenerauxBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.PersonneAChargeBean;
import com.cci.gpec.commons.PiecesJustificativesBean;
import com.cci.gpec.commons.RemunerationBean;
import com.cci.gpec.commons.RevenuComplementaireBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.ServiceBean;
import com.cci.gpec.commons.TransmissionBean;
import com.cci.gpec.commons.TypeAbsenceBean;
import com.cci.gpec.commons.TypeHabilitationBean;
import com.cci.gpec.commons.UserBean;
import com.cci.gpec.db.connection.HibernateUtil;
import com.cci.gpec.icefaces.component.inputFile.InputFileData;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.AccidentServiceImpl;
import com.cci.gpec.metier.implementation.AdminServiceImpl;
import com.cci.gpec.metier.implementation.ContratTravailServiceImpl;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.EntretienServiceImpl;
import com.cci.gpec.metier.implementation.EvenementServiceImpl;
import com.cci.gpec.metier.implementation.FicheDePosteServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierEntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.FicheMetierServiceImpl;
import com.cci.gpec.metier.implementation.FormationServiceImpl;
import com.cci.gpec.metier.implementation.GroupeServiceImpl;
import com.cci.gpec.metier.implementation.HabilitationServiceImpl;
import com.cci.gpec.metier.implementation.ImportExportBDD;
import com.cci.gpec.metier.implementation.LienRemunerationRevenuServiceImpl;
import com.cci.gpec.metier.implementation.MappingRepriseServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.ObjectifsEntretienServiceImpl;
import com.cci.gpec.metier.implementation.ParamsGenerauxServiceImpl;
import com.cci.gpec.metier.implementation.ParcoursServiceImpl;
import com.cci.gpec.metier.implementation.PersonneAChargeServiceImpl;
import com.cci.gpec.metier.implementation.PiecesJustificativesServiceImpl;
import com.cci.gpec.metier.implementation.RemunerationServiceImpl;
import com.cci.gpec.metier.implementation.RevenuComplementaireServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.metier.implementation.TransmissionServiceImpl;
import com.cci.gpec.metier.implementation.TypeAbsenceServiceImpl;
import com.cci.gpec.metier.implementation.TypeHabilitationServiceImpl;
import com.cci.gpec.metier.implementation.UserServiceImpl;
import com.cci.gpec.web.MysqlToXls;
import com.cci.gpec.web.Utils;
import com.cci.gpec.web.backingBean.BackingBeanForm;
import com.cci.gpec.web.backingBean.login.LoginBB;
import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.component.paneltabset.TabChangeEvent;
import com.icesoft.faces.context.effects.JavascriptContext;

public class AdminFormBB extends BackingBeanForm {

	private static final String CCI_PROPERTIES_FILE = "CCI.properties";
	
	private static final Logger	LOGGER = LoggerFactory.getLogger(AdminFormBB.class);

	private List<UserBean> userList;
	private List<UserBean> superAdminList;
	private List<UserBean> demandesVersionEssaiList;
	private List<UserBean> versionEssaiList;
	private List<SelectItem> groupeList;

	private int idGroupeSelected = -1;
	private int idGroupeSelectedUser = -1;

	private String login;
	private String password;
	private boolean evenement;
	private boolean remuneration;
	private boolean ficheDePoste;
	private boolean entretien;
	private boolean contratTravail;
	private boolean admin;
	private String nomUser;
	private String prenomUser;
	private String nomAdmin;
	private String prenomAdmin;
	private String telephoneAdmin;

	private boolean essai = false;
	private boolean importBase = false;
	private boolean superAdminForm = false;
	private boolean specificImportWithSplitByEntreprise = false;

	private String nomGroupe;
	private String mailAdmin;
	private String superAdminPassword;

	private boolean modif = false;

	private boolean resetPassword = false;

	private boolean myAccount = false;

	private int indexSelectedRow = -1;

	private boolean modalAccesSuperAdminRendered = false;
	private boolean modalRendered = false;
	private boolean modalRenderedAccount = false;

	private InputFileData currentFile;

	private boolean fileError = false;

	private List<File> fileListAdmin = new ArrayList<File>();
	private List<File> extractionFileList = new ArrayList<File>();
	private String currentFilePath;

	private int fileProgressAdmin = 0;
	private boolean modalRenderedDelFile = false;

	private boolean displayCreationOk = false;
	private boolean displayCreationError = false;
	private boolean displayCreationErrorSplit = false;

	private boolean displaySuppressionOk = false;
	private boolean displaySuppressionError = false;

	private boolean displayEnvoiMailError = false;

	private List<File> deletedFiles = new ArrayList<File>();

	private DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);
	
	private boolean notAutoGenPwd = true;

	public AdminFormBB() throws Exception {
		super();
		init();
	}

	public AdminFormBB(int id, String nom) throws Exception {
		super(id, nom);
		init();
	}

	public void displayFileList(ValueChangeEvent event) throws Exception {

		importBase = Boolean.valueOf(event.getNewValue().toString());

		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	public void init() throws Exception {
		super.id = 0;
		super.nom = "";

		this.login = "";
		this.nomUser = "";
		this.prenomUser = "";
		this.nomAdmin = "";
		this.prenomAdmin = "";
		this.telephoneAdmin = "";

		this.nomGroupe = "";
		this.mailAdmin = "";
		this.deletedFiles.clear();
		this.extractionFileList.clear();

		this.displayCreationErrorSplit = false;
		this.displayCreationError = false;
		this.displayCreationOk = false;
		this.displaySuppressionError = false;
		this.displaySuppressionOk = false;
		this.displayEnvoiMailError = false;

		this.idGroupeSelected = -1;
		this.idGroupeSelectedUser = -1;
		GroupeServiceImpl grpServ = new GroupeServiceImpl();
		this.groupeList = new ArrayList<SelectItem>();
		for (GroupeBean g : grpServ.getGroupeBeanList(true)) {
			this.groupeList.add(new SelectItem(g.getId(), g.getNom()));
		}

		evenement = false;
		remuneration = false;
		ficheDePoste = false;
		entretien = false;
		contratTravail = false;
		admin = false;

		UserServiceImpl userServ = new UserServiceImpl();
		this.userList = userServ.getUserList(
				session.getAttribute("groupe") != null ? Integer
						.parseInt(session.getAttribute("groupe").toString())
						: -1, false, false, true);
		this.superAdminList = userServ.getUserList(0, true, false, false);
		this.demandesVersionEssaiList = userServ.getDemandesVersionEssaiList();
		this.versionEssaiList = userServ.getVersionEssaiList();
		GroupeServiceImpl groupeServ = new GroupeServiceImpl();
		for (UserBean u : versionEssaiList) {
			GroupeBean g = groupeServ.getGroupeBeanById(u.getIdGroupe());
			if (g.getFinPeriodeEssai().before(new Date())) {
				u.setPeriodeEssaiTerminee(true);
			} else {
				u.setPeriodeEssaiTerminee(false);
			}
			u.setFinPeriodeEssai(g.getFinPeriodeEssai());
		}

		// return "addUser";
	}

	public void initSuperAdminForm() {
		superAdminForm = true;
		initUserForm();
	}

	public String initUserFormForMyAccount() throws Exception {

		this.myAccount = true;

		LoginBB loginBB = (LoginBB) FacesContext.getCurrentInstance()
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("loginBB");

		this.id = loginBB.getUser().getId();
		this.login = loginBB.getUser().getLogin();
		this.password = "";
		this.resetPassword = false;
		this.nomUser = loginBB.getUser().getNom();
		this.prenomUser = loginBB.getUser().getPrenom();
		this.evenement = loginBB.getUser().isEvenement();
		this.remuneration = loginBB.getUser().isRemuneration();
		this.ficheDePoste = loginBB.getUser().isFicheDePoste();
		this.entretien = loginBB.getUser().isEntretien();
		this.contratTravail = loginBB.getUser().isContratTravail();
		this.admin = loginBB.getUser().isAdmin();

		modalRenderedAccount = !modalRenderedAccount;

		return "admin";

	}

	public void initUserForm() {
		super.id = 0;
		super.nom = "";

		this.login = "";
		this.password = "";
		this.resetPassword = false;
		this.nomUser = "";
		this.prenomUser = "";

		this.nomGroupe = "";
		this.mailAdmin = "";
		this.nomAdmin = "";
		this.prenomAdmin = "";
		this.telephoneAdmin = "";
		this.deletedFiles.clear();
		this.extractionFileList.clear();

		this.displayCreationErrorSplit = false;
		this.displayCreationError = false;
		this.displayCreationOk = false;
		this.displayEnvoiMailError = false;

		evenement = false;
		remuneration = false;
		ficheDePoste = false;
		entretien = false;
		contratTravail = false;
		admin = false;

		modalRendered = !modalRendered;
	}

	public void onGroupChanged(ValueChangeEvent evt) {
		idGroupeSelected = Integer.parseInt(evt.getNewValue().toString());
		this.displaySuppressionError = false;
		this.displaySuppressionOk = false;
		extractionFileList.clear();
		if (idGroupeSelected > 0) {

			File directoryExtraction = new File(
					Utils.getSessionFileUploadPathExtraction(session)
							+ Utils.FILE_SEPARATOR + idGroupeSelected
							+ Utils.FILE_SEPARATOR);
			if (directoryExtraction.exists()
					&& directoryExtraction.listFiles().length > 0) {
				for (File f : directoryExtraction.listFiles()) {
					if (f.getName().endsWith(".rar")) {
						extractionFileList.add(f);
					}
				}
			}
		}
	}

	public void onGroupChangedUser(ValueChangeEvent evt)
			throws NumberFormatException, Exception {
		idGroupeSelectedUser = Integer.parseInt(evt.getNewValue().toString());
	}

	private HtmlDataTable getParentDatatable(UIComponent compo) {
		if (compo == null) {
			return null;
		}
		if (compo instanceof HtmlDataTable) {
			return (HtmlDataTable) compo;
		}
		return getParentDatatable(compo.getParent());
	}

	public void modifSuperAdmin(ActionEvent evt) {
		this.superAdminForm = true;
		modif(evt);
	}

	public void resetMyAccount(ActionEvent evt) {
		this.myAccount = false;
	}

	public void refresh(ActionEvent event) {
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	public void modif(ActionEvent evt) {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		UserBean user = (UserBean) table.getRowData();

		this.id = user.getId();
		this.login = user.getLogin();
		this.resetPassword = false;
		this.nomUser = user.getNom();
		this.prenomUser = user.getPrenom();
		this.password = "";

		this.displayEnvoiMailError = false;

		this.evenement = user.isEvenement();
		this.remuneration = user.isRemuneration();
		this.ficheDePoste = user.isFicheDePoste();
		this.entretien = user.isEntretien();
		this.contratTravail = user.isContratTravail();
		this.admin = user.isAdmin();

		this.modif = true;
		modalRendered = !modalRendered;
	}

	// public String addLink() {
	// return "addUser";
	// }

	public void supprimer(ActionEvent evt) throws NumberFormatException,
			Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		UserBean user = (UserBean) table.getRowData();

		UserServiceImpl userServ = new UserServiceImpl();
		userServ.suppression(user);

		this.userList.clear();
		this.userList = userServ.getUserList(
				Integer.parseInt(session.getAttribute("groupe").toString()),
				false, false, true);
		this.superAdminList.clear();
		this.superAdminList = userServ.getUserList(
				Integer.parseInt(session.getAttribute("groupe").toString()),
				true, false, false);

	}

	public void supprimerDemande(ActionEvent evt) {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		UserBean user = (UserBean) table.getRowData();

		UserServiceImpl userServ = new UserServiceImpl();
		userServ.suppression(user);

		demandesVersionEssaiList.remove(user);

	}

	public void validerDemande(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		UserBean user = (UserBean) table.getRowData();

		nomGroupe = user.getNomEntreprise();
		mailAdmin = user.getLogin();
		essai = true;
		creerWithUSer(user);

		demandesVersionEssaiList.remove(user);
	}

	public void validerEssaiAvecDonnees(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		UserBean user = (UserBean) table.getRowData();

		GroupeServiceImpl grpServ = new GroupeServiceImpl();
		GroupeBean g = grpServ.getGroupeBeanById(user.getIdGroupe());
		g.setFinPeriodeEssai(null);
		grpServ.saveOrUppdate(g);

		versionEssaiList.remove(user);
	}

	public void validerEssaiSansDonnees(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		UserBean user = (UserBean) table.getRowData();

		GroupeServiceImpl grpServ = new GroupeServiceImpl();
		GroupeBean g = grpServ.getGroupeBeanById(user.getIdGroupe());
		g.setFinPeriodeEssai(null);
		grpServ.saveOrUppdate(g);

		deleteDataFromGroupe(user.getIdGroupe(), true);

		versionEssaiList.remove(user);
	}

	public void supprimerEssai(ActionEvent evt) throws Exception {
		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		UserBean user = (UserBean) table.getRowData();

		deleteDataFromGroupe(user.getIdGroupe(), false);

		versionEssaiList.remove(user);
	}

	public void checkLogin(ValueChangeEvent evt) {
		if (evt != null) {
			login = evt.getNewValue().toString();
		}
		if (login != null && !login.equals("")) {
			String emailRegex = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)";
			Pattern emailPattern = Pattern.compile(emailRegex);
			Matcher matcher = emailPattern.matcher(login);
			if (!matcher.matches()) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"L'identifiant doit être une adresse mail.",
						"L'identifiant doit être une adresse mail.");
				FacesContext.getCurrentInstance().addMessage("idForm:nom",
						message);
			}
		}
	}

	public void toggleModalAccesSuperAdminRendered(ActionEvent event) {
		modalAccesSuperAdminRendered = !modalAccesSuperAdminRendered;
		this.superAdminPassword = "";
	}

	public void saveOrUpdateUser() throws NumberFormatException, Exception {

		LoginBB loginBB = (LoginBB) FacesContext.getCurrentInstance()
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("loginBB");

		UserServiceImpl usrServ = new UserServiceImpl();
		UserBean u = usrServ.getUserByLogin(login);
		if (!myAccount && !modif && u != null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Cette adresse mail est déjà utilisée.",
					"Cette adresse mail est déjà utilisée.");
			FacesContext.getCurrentInstance()
					.addMessage("idForm:mail", message);
			return;
		}

		if (myAccount && u != null) {
			if (u.getId() != loginBB.getUser().getId()) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Cette adresse mail est déjà utilisée.",
						"Cette adresse mail est déjà utilisée.");
				FacesContext.getCurrentInstance().addMessage("idAccount:mail",
						message);
				return;
			}
		}

		if (login != null && !login.equals("")) {
			String emailRegex = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)";
			Pattern emailPattern = Pattern.compile(emailRegex);
			Matcher matcher = emailPattern.matcher(login);
			if (!matcher.matches()) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"L'identifiant doit être une adresse mail.",
						"L'identifiant doit être une adresse mail.");
				FacesContext.getCurrentInstance().addMessage("idForm:mail",
						message);
				return;
			}
		}

		UserBean user = new UserBean();
		user.setId(this.id);

		if (password != null && !password.equals("")) {
			if (!Utils.verifyPassword(password)) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le mot de passe n'est pas assez sécurisé. Veuillez consulter l'infobulle pour plus d'informations.",
						"Le mot de passe n'est pas assez sécurisé. Veuillez consulter l'infobulle pour plus d'informations.");
				FacesContext.getCurrentInstance().addMessage("idForm:pwd",
						message);
				return;
			}
			user.setPassword(Utils.hashPassword(password));
		} else if (resetPassword) {
//			if (!myAccount) {
				password = Utils.generatePassword(8);
//			}
		}

		user.setLogin(this.login);
		if (((modif && resetPassword) || !modif) && password != null
				&& !password.equals("")) {
			if (!Utils.verifyPassword(password)) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le mot de passe n'est pas assez sécurisé. Veuillez consulter l'infobulle pour plus d'informations.",
						"Le mot de passe n'est pas assez sécurisé. Veuillez consulter l'infobulle pour plus d'informations.");
				FacesContext.getCurrentInstance().addMessage("idForm:pwd",
						message);
				return;
			}
			user.setPassword(hashPassword(password));
		}
		user.setEvenement(this.evenement);
		user.setRemuneration(this.remuneration);
		user.setFicheDePoste(this.ficheDePoste);
		user.setEntretien(this.entretien);
		user.setContratTravail(this.contratTravail);
		user.setAdmin(this.admin);
		if (superAdminForm) {
			user.setSuperAdmin(true);
		} else {
			user.setSuperAdmin(false);
			if (session.getAttribute("superAdmin").toString().equals("true")) {
				user.setIdGroupe(idGroupeSelectedUser);
			} else {
				user.setIdGroupe(Integer.parseInt(session
						.getAttribute("groupe").toString()));
			}
		}
		user.setPrenom(this.prenomUser);
		user.setNom(this.nomUser);

		UserServiceImpl userServ = new UserServiceImpl();
		userServ.saveOrUppdate(user, user.getIdGroupe());

		loginBB.setUser(user);

		if (!myAccount && (modif && password != null && !password.equals(""))
				|| (modif && resetPassword)) {
			String subject = "GPEC - mise à jour mot de passe";
			String content = "<html><body>Bonjour,<br/><br/>Votre mot de passe a été réinitialisé. Voici votre nouveau mot de passe :<br/>"
					+ "Login : "
					+ login
					+ "<br/>"
					+ "Mot de passe : "
					+ password
					+ "<br/><br/>Cordialement,<br/>L'équipe GPEC.</body></html>";
			try {
				Utils.sendHtmlEmail(login, subject, content);
			} catch (IOException e) {
				this.displayEnvoiMailError = true;
				password = "";
				e.printStackTrace();
			} catch (EmailException e) {
				this.displayEnvoiMailError = true;
				password = "";
				e.printStackTrace();
			}
		}
		if (!myAccount && !modif && password != null && !password.equals("")) {
			Properties mailSettings;
			mailSettings = Utils
					.getEmailPropertiesFromClasspath("conf/emailSettings.properties");
			String url = mailSettings.getProperty("mail.application.url");

			String subject = "GPEC - création compte";
			String content = "<html><body>Bonjour,<br/><br/>Votre compte a été créé. Voici vos identifiants :<br/>"
					+ "Login : "
					+ login
					+ "<br/>"
					+ "Mot de passe : "
					+ password
					+ "<br/><br/>Connectez-vous à l'application en suivant ce lien : <a href=\""
					+ url
					+ "\">GPEC</a>"
					+ "<br/><br/>Cordialement,<br/>L'équipe GPEC.</body></html>";
			try {
				Utils.sendHtmlEmail(login, subject, content);
			} catch (IOException e) {
				this.displayEnvoiMailError = true;
				password = "";
				e.printStackTrace();
			} catch (EmailException e) {
				this.displayEnvoiMailError = true;
				password = "";
				e.printStackTrace();
			}
		}
		this.userList.clear();
		this.userList = userServ.getUserList(
				Integer.parseInt(session.getAttribute("groupe").toString()),
				false, false, true);
		this.superAdminList.clear();
		this.superAdminList = userServ.getUserList(
				Integer.parseInt(session.getAttribute("groupe").toString()),
				true, false, false);
		this.modif = false;
		this.superAdminForm = false;
		modalRendered = !modalRendered;
		notAutoGenPwd = true;
	}

	public String annuler() {

		this.modif = false;
		this.superAdminForm = false;

		return "admin";
	}

	public String creer() throws Exception {
		return creerWithUSer(null);
	}

	public String creerWithUSer(UserBean user) throws Exception {

		if (!essai) {
			boolean validationOk = true;
			if (nomGroupe == null || nomGroupe.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le nom de groupe est obligatoire",
						"Le nom de groupe est obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idForm:idSuperAdminTabSet:0:idGroupe", message);
				validationOk = false;
			}
			if (nomAdmin == null || nomAdmin.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Le nom est obligatoire",
						"Le nom est obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idForm:idSuperAdminTabSet:0:idNomAdmin", message);
				validationOk = false;
			}
			if (prenomAdmin == null || prenomAdmin.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le prénom est obligatoire",
						"Le prénom est obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idForm:idSuperAdminTabSet:0:idPrenomAdmin", message);
				validationOk = false;
			}
			if (telephoneAdmin == null || telephoneAdmin.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le numéro de téléphone est obligatoire",
						"Le numéro de téléphone est obligatoire");
				FacesContext.getCurrentInstance()
						.addMessage(
								"idForm:idSuperAdminTabSet:0:idTelephoneAdmin",
								message);
				validationOk = false;
			}
			if (mailAdmin == null || mailAdmin.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"L'adresse mail est obligatoire",
						"L'adresse mail est obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idForm:idSuperAdminTabSet:0:idMailAdmin", message);
				validationOk = false;
			} else {
				UserServiceImpl usrServ = new UserServiceImpl();
				UserBean u = usrServ.getUserByLogin(mailAdmin);
				if (u != null) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Cette adresse mail est déjà utilisée.",
							"Cette adresse mail est déjà utilisée.");
					FacesContext.getCurrentInstance().addMessage(
							"idForm:idSuperAdminTabSet:0:idMailAdmin", message);
					validationOk = false;
				}
			}
			if (!validationOk) {
				return "";
			}
		}

		if (nomGroupe != null && !nomGroupe.equals("")) {
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean g;
			Session sessionf = null;
			Transaction tx = null;
			try {
				g = grServ.getGroupeBeanByName(nomGroupe);
				sessionf = HibernateUtil.getSessionFactory().getCurrentSession();
				tx = sessionf.beginTransaction();
				if (g == null) {
					if (!essai) {
						if (telephoneAdmin != null
								&& !telephoneAdmin.equals("")) {
							String telRegex = "^0[0-9]([ .-]?[0-9]{2}){4}$";
							Pattern telPattern = Pattern.compile(telRegex);
							Matcher matcher = telPattern
									.matcher(telephoneAdmin);
							if (!matcher.matches()) {
								FacesMessage message = new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"Numéro de téléphone non valide.",
										"Numéro de téléphone non valide.");
								FacesContext
										.getCurrentInstance()
										.addMessage(
												"idForm:idSuperAdminTabSet:0:idTelephoneAdmin",
												message);
								return "";
							}
						} else {
							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Le numéro de téléphone est obligatoire",
									"Le numéro de téléphone est obligatoire");
							FacesContext
									.getCurrentInstance()
									.addMessage(
											"idForm:idSuperAdminTabSet:0:idTelephoneAdmin",
											message);
							return "";
						}
						if (mailAdmin != null && !mailAdmin.equals("")) {
							String emailRegex = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)";
							Pattern emailPattern = Pattern.compile(emailRegex);
							Matcher matcher = emailPattern.matcher(mailAdmin);
							if (!matcher.matches()) {

								FacesMessage message = new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"Email incorrect", "Email incorrect");
								FacesContext
										.getCurrentInstance()
										.addMessage(
												"idForm:idSuperAdminTabSet:0:idMailAdmin",
												message);
								return "";
							}
						} else {
							FacesMessage message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"L'email est obligatoire",
									"L'email est obligatoire");
							FacesContext.getCurrentInstance().addMessage(
									"idForm:idSuperAdminTabSet:0:idMailAdmin",
									message);
							return "";
						}
					}

					GroupeServiceImpl grpServ = new GroupeServiceImpl();
					GroupeBean gr = new GroupeBean();
					gr.setId(-1);
					gr.setNom(nomGroupe);
					gr.setDeleted(false);
					if (essai) {
						GregorianCalendar finPeriodeEssai = new GregorianCalendar();
						finPeriodeEssai.setTime(new Date());
						finPeriodeEssai.add(Calendar.MONTH, 1);
						gr.setFinPeriodeEssai(finPeriodeEssai.getTime());
						user.setFinPeriodeEssai(finPeriodeEssai.getTime());
					}
					grpServ.save(gr);

					GregorianCalendar date = new GregorianCalendar();

					TransmissionServiceImpl transServ = new TransmissionServiceImpl();
					TransmissionBean t = new TransmissionBean();
					t.setId(-1);
					t.setIdGroupe(gr.getId());
					t.setDateDerniereDemande(date.getTime());
					t.setDateTransmission(date.getTime());
					transServ.save(t, gr.getId());

					String password = Utils.generatePassword(8);

					UserServiceImpl usrServ = new UserServiceImpl();
					if (essai) {
						user.setIdGroupe(gr.getId());
						user.setPassword(hashPassword(password));
						user.setDateDemandeVersionEssai(null);
						usrServ.saveOrUppdateWithoutTransaction(user,
								gr.getId());
					} else {
						UserBean usr = new UserBean();
						usr.setId(0);
						usr.setNom(nomAdmin);
						usr.setPrenom(prenomAdmin);
						usr.setTelephone(telephoneAdmin);
						usr.setNomEntreprise(nomGroupe);
						usr.setAdmin(true);
						usr.setContratTravail(true);
						usr.setEntretien(true);
						usr.setEvenement(true);
						usr.setFicheDePoste(true);
						usr.setIdGroupe(gr.getId());
						usr.setLogin(mailAdmin);
						usr.setPassword(hashPassword(password));
						usr.setRemuneration(true);
						usr.setSuperAdmin(false);
						usrServ.save(usr);
					}

					Properties mailSettings;
					mailSettings = Utils
							.getEmailPropertiesFromClasspath("conf/emailSettings.properties");
					String url = mailSettings
							.getProperty("mail.application.url");

					String subject = "GPEC - création compte";
					String content = "<html><body>Bonjour,<br/><br/>Votre compte a été créé. Voici vos identifiants d'administrateur :<br/>"
							+ "Login : "
							+ mailAdmin
							+ "<br/>"
							+ "Mot de passe : "
							+ password
							+ "<br/><br/>Connectez-vous à l'application en suivant ce lien : <a href=\""
							+ url
							+ "\">GPEC</a>"
							+ "<br/><br/>Cordialement,<br/>L'équipe GPEC.</body></html>";
					Utils.sendHtmlEmail(mailAdmin, subject, content);
					tx.commit();
					demandesVersionEssaiList.remove(user);
					versionEssaiList.add(user);
					displayCreationOk = true;
					displayCreationError = false;
					this.nomGroupe = "";
					this.nomAdmin = "";
					this.prenomAdmin = "";
					this.telephoneAdmin = "";
					this.mailAdmin = "";
					this.essai = false;
					this.importBase = false;

				} else {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Ce nom de groupe est déjà utilisé",
							"Ce nom de groupe est déjà utilisé");
					FacesContext.getCurrentInstance().addMessage(
							"idForm:idSuperAdminTabSet:0:idGroupe", message);
				}
			} catch (HibernateException e) {
				if (tx != null) {
					tx.rollback();
				}
				LOGGER.error("Hibernate Session Error", e);
				throw e;
			} finally {
				if (sessionf != null && sessionf.isOpen()) {
					sessionf.close();
				}
			}
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le nom de groupe est obligatoire",
					"Le nom de groupe est obligatoire");
			FacesContext.getCurrentInstance().addMessage(
					"idForm:idSuperAdminTabSet:0:idGroupe", message);
		}

		return "";
	}

	public String getUrl() throws Exception {
		return Utils.getExtractionFileUrl(idGroupeSelected);
	}
	
	public void importAction(ActionEvent event) {
		InputFile inputFile = (InputFile) event.getSource();
		String idMessage = "idForm:idSuperAdminTabSet:0:importUpload";
		
		if (inputFile.getFileInfo().getStatus() != InputFile.SAVED) {
			showMessage("Une erreur s'est produite durant l'upload", FacesMessage.SEVERITY_ERROR, idMessage);
			return;
		}
		
		File f = inputFile.getFileInfo().getFile();
		ImportData importData = new ImportData(f);
		
		try {
			importData.importXML();
		} catch (Exception e) {
			showMessage("Une erreur s'est produite durant le traitement du document", FacesMessage.SEVERITY_ERROR, idMessage);
		}
		
		showMessage("Upload réussi avec succès", FacesMessage.SEVERITY_INFO, idMessage);
		return;
	}

	 public String extract() throws Exception {
		 if (idGroupeSelected > 0) {
			 GroupeServiceImpl grSrv = new GroupeServiceImpl();
			 String nomGroupe = grSrv.getGroupeBeanById(idGroupeSelected).getNom();
			 nomGroupe = nomGroupe.replaceAll(" ", "_");
			 
			 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			 String directoryName = nomGroupe + "_" + dateFormat.format(new Date());
			 String extractPath = Utils.getSessionFileUploadPathExtraction(session)
					 + directoryName + Utils.FILE_SEPARATOR;
			 
			 new File(extractPath).mkdirs();
			 
			 String[] tablesRef = { 
				 	"domaineformation", "famillemetier",
				 	"servicegenerique",  "typeaccident",
				 	"typecauseaccident", "typecontrat", 
				 	"typelesion", "typerecoursinterim", 
				 	"motifrupturecontrat", "statut" 
			 };
			 String[] tablesRefCustom = {
					 "typeabsence", "typehabilitation"
			 };
			 String[] tablesWithoutJoin = { 
					"admin", "entreprise", "gpec_user",
					"groupe", "metier", "ref_revenu_complementaire",
					"transmission"
			 };
			 String[] tablesWithJoinE = { 
					 "paramsgeneraux", "salarie",
					 "service", "fichemetierentreprise"
			 };
			 String[] tablesWithJoinSE = { 
					 "absence", "accident", "contrat_travail", "entretien", "evenement",
					 "fichedeposte", "formation", "habilitation", "parcours",
					 "personne_a_charge", "pieces_justificatives", "remuneration"
			 };
			 
			 String joinSE = " LEFT JOIN salarie AS s ON s.id_salarie=x.id_salarie LEFT JOIN entreprise AS e ON e.id_entreprise=s.id_entreprise";
			 String joinE = " LEFT JOIN entreprise AS e ON e.id_entreprise=x.id_entreprise";
			 String where = " WHERE x.id_groupe=" + idGroupeSelected;
			 String whereCustom = " WHERE x.id_groupe IS NULL OR  x.id_groupe=" + idGroupeSelected;
			 String whereE = " WHERE e.id_groupe=" + idGroupeSelected;
			
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			 Document document = docBuilder.newDocument();
			
			 Element racine = document.createElement("root");
			 document.appendChild(racine);
			 
			StringBuilder sb = new StringBuilder();
			
			 File xml = new File(extractPath + "extraction_" + directoryName + ".xml");
			
			 MysqlToXls mysqlToXls = new MysqlToXls();
			
			 try {
				 String query = "";
				 for (String from : tablesRefCustom) {
					 query = "select * from " + from + " AS x " + whereCustom;
					 document = mysqlToXls.generateXml(document, query, from);
					 sb = mysqlToXls.generateCsv(sb, document, query, from);
				 }
				 for (String from : tablesWithoutJoin) {
					 query = "select * from " + from + " AS x " + where;
					 document = mysqlToXls.generateXml(document, query, from);
					 sb = mysqlToXls.generateCsv(sb, document, query, from);
				 }
				 for (String from : tablesWithJoinE) {
					 query = "select x.* from " + from + " AS x " + joinE + whereE;
					 document = mysqlToXls.generateXml(document, query, from);
					 sb = mysqlToXls.generateCsv(sb, document, query, from);
				 }
				 for (String from : tablesWithJoinSE) {
					 query = "select x.* from " + from + " AS x " + joinSE + whereE;
					 document = mysqlToXls.generateXml(document, query, from);
					 sb = mysqlToXls.generateCsv(sb, document, query, from);
				 }
				 for (String from : tablesRef) {
					 query = "select x.* from " + from + " AS x";
					 document = mysqlToXls.generateXml(document, query, from);
					 sb = mysqlToXls.generateCsv(sb, document, query, from);
				 }
				 query =  "select x.* from lien_remuneration_revenu_complementaire AS x LEFT JOIN remuneration AS r ON x.id_remuneration=r.id_remuneration LEFT JOIN salarie AS s ON s.id_salarie=r.id_salarie LEFT JOIN entreprise AS e ON e.id_entreprise=s.id_entreprise WHERE e.id_groupe=" + idGroupeSelected;
				 document = mysqlToXls.generateXml(document, query, "lien_remuneration_revenu_complementaire");
				 sb = mysqlToXls.generateCsv(sb, document, query, "lien_remuneration_revenu_complementaire");
				
				 query = "select x.* from fichemetier AS x LEFT JOIN fichemetierentreprise AS f ON x.id_fiche_metier=f.id_fiche_metier LEFT JOIN entreprise AS e ON e.id_entreprise=f.id_entreprise WHERE e.id_groupe=" + idGroupeSelected;
				 document = mysqlToXls.generateXml(document, query, "fichemetier");
				 sb = mysqlToXls.generateCsv(sb, document, query, "fichemetier");
				
				 query = "select x.* from objectifsentretien AS x LEFT JOIN entretien AS e ON x.id_entretien=e.id_entretien LEFT JOIN salarie AS s ON s.id_salarie=e.id_salarie LEFT JOIN entreprise AS ent ON ent.id_entreprise=s.id_entreprise WHERE ent.id_groupe=" + idGroupeSelected;
				 document = mysqlToXls.generateXml(document, query, "objectifsentretien");
				 sb = mysqlToXls.generateCsv(sb, document, query, "objectifsentretien");
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
			 
			 mysqlToXls.writeCsv(sb, new File(extractPath + "extraction_" + directoryName + ".csv"));
			 
			 TransformerFactory factory = TransformerFactory.newInstance();
			 Transformer transformer = factory.newTransformer();
			
			 transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			 transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
			 StringWriter sw = new StringWriter();
			 StreamResult result = new StreamResult(sw);
			 DOMSource source = new DOMSource(document);
			 transformer.transform(source, result);
			 String xmlString = sw.toString();
			 OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(xml), "UTF-8");
			 BufferedWriter bw = new BufferedWriter(fw);
			 bw.write(xmlString);
			 bw.flush();
			 bw.close();
			 
//			 if (new File(Utils.getSessionFileUploadPath(session) + Utils.FILE_SEPARATOR + nomGroupe + Utils.FILE_SEPARATOR).exists()) {
//				 FileUtils.copyDirectoryToDirectory(new File(Utils.getSessionFileUploadPath(session)
//						 + Utils.FILE_SEPARATOR + nomGroupe
//						 + Utils.FILE_SEPARATOR), new File(extractPath));
//			 }
//			 String osName = System.getProperty("os.name");
//			
//			 if (osName.startsWith("Windows")) {
//				 Utils.compressCommandLine(Utils.getSessionFileUploadPathExtraction(session) + directoryName + ".rar", extractPath, directoryName, session);
//			 } else {
//				 Utils.compressCommandLine("", "", Utils.getSessionFileUploadPathExtraction(session) + directoryName, session);
//			 }
			 
			 extractionFileList.clear();
			 extractionFileList.add(new File(Utils.getSessionFileUploadPathExtraction(session) + directoryName + ".rar"));
		 } else {
			 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Le nom de groupe est obligatoire",
					 "Le nom de groupe est obligatoire");
			 FacesContext.getCurrentInstance().addMessage("idForm:idSuperAdminTabSet:0:idGroupeList", message);
		 }
		 
		 return "";
	 }

	public void download(ActionEvent evt) throws Exception {

		HtmlDataTable table = getParentDatatable((UIComponent) evt.getSource());
		// On récupère l'objet affiché à la bonne ligne de la datatable.
		File zip = (File) table.getRowData();
		// On récupère aussi son index
		indexSelectedRow = table.getRowIndex();
		// Suite du traitement sur l'objet sélectionné.

		ExternalContext eContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		eContext.getSessionMap().put("fichier", zip.getAbsolutePath());

		JavascriptContext
				.addJavascriptCall(
						FacesContext.getCurrentInstance(),
						"window.open(\"servlet.download?contentType="
								+ Files.probeContentType(new File(zip
										.getAbsolutePath()).toPath())
								+ " \",\"_Download\");");

	}

	public void processTabChange(TabChangeEvent tabChangeEvent) {
		idGroupeSelected = -1;
		idGroupeSelectedUser = -1;
		extractionFileList.clear();
		this.nomGroupe = "";
		this.mailAdmin = "";
		this.nomAdmin = "";
		this.prenomAdmin = "";
		this.telephoneAdmin = "";
	}

	public String delete() throws Exception {
		if (idGroupeSelected > 0) {
			deleteDataFromGroupe(this.idGroupeSelected, false);
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le nom de groupe est obligatoire",
					"Le nom de groupe est obligatoire");
			FacesContext.getCurrentInstance().addMessage(
					"idForm:idSuperAdminTabSet:0:idGroupeList2", message);
		}
		return "";
	}

	public void deleteDataFromGroupe(int idGroupe, boolean partialDelete)
			throws Exception {

		/*
		 * objent acc form lien_remu parc contrat entretien hab*type cause acc
		 * piece just even*typeacc **tyle lesion pers a charge abs*dom form remu
		 * fdp fichemetierent*statut **typerecoursinterim **typecontrat **motif
		 * rupt cont metier salarie fiche metier*fam metier --type hab serv
		 * params --type abs ref rev comp admin gpec user*mapping entreprise
		 * transmission groupe
		 */

		ObjectifsEntretienServiceImpl objServ = new ObjectifsEntretienServiceImpl();
		List<ObjectifsEntretienBean> objList = objServ
				.getObjectifsEntretienList(idGroupe);
		AccidentServiceImpl accServ = new AccidentServiceImpl();
		List<AccidentBean> accList = accServ.getAccidentsList(idGroupe);
		FormationServiceImpl formServ = new FormationServiceImpl();
		List<FormationBean> formList = formServ.getFormationsList(idGroupe);
		LienRemunerationRevenuServiceImpl lienServ = new LienRemunerationRevenuServiceImpl();
		List<LienRemunerationRevenuBean> lienList = lienServ
				.getLienRemunerationRevenuList(idGroupe);
		ParcoursServiceImpl parcServ = new ParcoursServiceImpl();
		List<ParcoursBean> parcList = parcServ.getParcourssList(idGroupe);
		ContratTravailServiceImpl contServ = new ContratTravailServiceImpl();
		List<ContratTravailBean> contList = contServ
				.getContratTravailsList(idGroupe);
		EntretienServiceImpl entServ = new EntretienServiceImpl();
		List<EntretienBean> entList = entServ.getEntretiensList(idGroupe);
		HabilitationServiceImpl habServ = new HabilitationServiceImpl();
		List<HabilitationBean> habList = habServ.getHabilitationsList(idGroupe);
		PiecesJustificativesServiceImpl pjServ = new PiecesJustificativesServiceImpl();
		List<PiecesJustificativesBean> pjList = pjServ
				.getPiecesJustificativesBeanList(idGroupe);
		EvenementServiceImpl evServ = new EvenementServiceImpl();
		List<EvenementBean> evList = evServ.getEvenementsList(idGroupe);
		PersonneAChargeServiceImpl persServ = new PersonneAChargeServiceImpl();
		List<PersonneAChargeBean> persList = persServ
				.getPersonneAChargeBeanList(idGroupe);
		AbsenceServiceImpl absServ = new AbsenceServiceImpl();
		List<AbsenceBean> absList = absServ.getAbsencesList(idGroupe);
		RemunerationServiceImpl remuServ = new RemunerationServiceImpl();
		List<RemunerationBean> remuList = remuServ
				.getRemunerationList(idGroupe);
		FicheDePosteServiceImpl fdpServ = new FicheDePosteServiceImpl();
		List<FicheDePosteBean> fdpList = fdpServ.getFicheDePostesList(idGroupe);
		FicheMetierEntrepriseServiceImpl fmeServ = new FicheMetierEntrepriseServiceImpl();
		FicheMetierServiceImpl fmServ = new FicheMetierServiceImpl();
		List<FicheMetierEntrepriseBean> fmeList = fmeServ
				.getFicheMetierEntrepriseList(idGroupe);
		List<FicheMetierBean> fmList = new ArrayList<FicheMetierBean>();
		for (FicheMetierEntrepriseBean fmeBean : fmeList) {
			fmList.add(fmServ.getFicheMetierBeanById(fmeBean.getIdFicheMetier()));
		}
		MetierServiceImpl metServ = new MetierServiceImpl();
		List<MetierBean> metList = metServ.getMetiersList(idGroupe);
		SalarieServiceImpl salServ = new SalarieServiceImpl();
		List<SalarieBean> salList = salServ.getSalariesList(idGroupe);
		TypeHabilitationServiceImpl typeHabServ = new TypeHabilitationServiceImpl();
		List<TypeHabilitationBean> typeHabList = typeHabServ
				.getTypeHabilitationListForGroupWithoutCommon(idGroupe);
		ServiceImpl serv = new ServiceImpl();
		List<ServiceBean> servList = serv.getServicesList(idGroupe);
		ParamsGenerauxServiceImpl pgServ = new ParamsGenerauxServiceImpl();
		List<ParamsGenerauxBean> pgList = pgServ
				.getParamsGenerauxBeanList(idGroupe);
		TypeAbsenceServiceImpl typeAbsServ = new TypeAbsenceServiceImpl();
		List<TypeAbsenceBean> typeAbsList = typeAbsServ
				.getTypeAbsenceListForGroupWithoutCommon(idGroupe);
		RevenuComplementaireServiceImpl revCompServ = new RevenuComplementaireServiceImpl();
		List<RevenuComplementaireBean> revCompList = revCompServ
				.getRevenuComplementaireList(idGroupe);
		AdminServiceImpl admServ = new AdminServiceImpl();
		AdminBean adm = admServ.getAdminBean(idGroupe);
		UserServiceImpl userServ = new UserServiceImpl();
		List<UserBean> userList;
		if (partialDelete) {
			userList = userServ.getUserList(idGroupe, true, true, false);
		} else {
			userList = userServ.getUserList(idGroupe, true, true, true);
		}
		EntrepriseServiceImpl entrServ = new EntrepriseServiceImpl();
		List<EntrepriseBean> entrList = entrServ.getEntreprisesList(idGroupe);
		TransmissionServiceImpl transServ = new TransmissionServiceImpl();
		TransmissionBean trans = transServ.getTransmissionBean(idGroupe);
		GroupeServiceImpl grpServ = new GroupeServiceImpl();
		GroupeBean grp = grpServ.getGroupeBeanById(idGroupe);

		Session sessionf = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = sessionf.beginTransaction();
			for (ObjectifsEntretienBean o : objList) {
				objServ.deleteObjectifWithoutTransaction(o);
			}
			for (AccidentBean a : accList) {
				accServ.deleteAccidentWithoutTransaction(a);
			}
			for (FormationBean f : formList) {
				formServ.deleteFormationWithoutTransaction(f);
			}
			for (LienRemunerationRevenuBean l : lienList) {
				lienServ.deleteLienRemuRevCompWithoutTransaction(l);
			}
			for (ParcoursBean p : parcList) {
				parcServ.deleteParcoursWithoutTransaction(p);
			}
			for (ContratTravailBean ct : contList) {
				contServ.deleteContratTravailWithoutTransaction(ct);
			}
			for (EntretienBean e : entList) {
				entServ.deleteEntretienWithoutTransaction(e);
			}
			for (HabilitationBean h : habList) {
				habServ.deleteHabilitationWithoutTransaction(h);
			}
			for (PiecesJustificativesBean pj : pjList) {
				pjServ.deletePieceJustificativeWithoutTransaction(pj);
			}
			for (EvenementBean e : evList) {
				evServ.deleteEvenementWithoutTransaction(e);
			}
			for (PersonneAChargeBean p : persList) {
				persServ.deleteersonneAChargeWithoutTransaction(p);
			}
			for (AbsenceBean a : absList) {
				absServ.deleteAbsenceWithoutTransaction(a);
			}
			for (RemunerationBean r : remuList) {
				remuServ.deleteRemunerationWithoutTransaction(r);
			}
			for (FicheDePosteBean f : fdpList) {
				fdpServ.deleteFicheDePosteWithoutTransaction(f);
			}
			for (FicheMetierEntrepriseBean f : fmeList) {
				fmeServ.deleteFicheMetierEntrepriseWithoutTransaction(f);
			}
			for (MetierBean f : metList) {
				metServ.deleteMetierWithoutTransaction(f);
			}
			for (SalarieBean s : salList) {
				salServ.deleteSalarieWithoutTransaction(s);
			}
			for (FicheMetierBean f : fmList) {
				fmServ.deleteFicheMetierWithoutTransaction(f);
			}
			for (TypeHabilitationBean t : typeHabList) {
				typeHabServ.deleteTypeHabilitationWithoutTransaction(t);
			}
			for (ServiceBean s : servList) {
				serv.deleteServiceWithoutTransaction(s);
			}
			for (ParamsGenerauxBean p : pgList) {
				pgServ.deleteParamsGenerauxWithoutTransaction(p);
			}
			for (TypeAbsenceBean t : typeAbsList) {
				typeAbsServ.deleteTypeAbsenceWithoutTransaction(t);
			}
			for (RevenuComplementaireBean r : revCompList) {
				revCompServ.deleteRevenuComplementaireWithoutTransaction(r);
			}
			if (adm != null) {
				if (!partialDelete) {
					admServ.deleteAdminWithoutTransaction(adm);
				}
			}
			for (UserBean u : userList) {
				userServ.deleteUserWithoutTransaction(u);
			}
			for (EntrepriseBean e : entrList) {
				entrServ.deleteEntrepriseWithoutTransaction(e);
			}
			transServ.deleteTransmissionWithoutTransaction(trans);
			if (!partialDelete) {
				grpServ.deleteGroupeWithoutTransaction(grp);
			}
			tx.commit();
			displaySuppressionError = false;
			displaySuppressionOk = true;

			this.idGroupeSelected = -1;
			this.idGroupeSelectedUser = -1;
			this.groupeList.clear();
			for (GroupeBean g : grpServ.getGroupeBeanList(false)) {
				this.groupeList.add(new SelectItem(g.getId(), g.getNom()));
			}

			File uploadFolder = new File(
					Utils.getSessionFileUploadPath(session)
							+ Utils.FILE_SEPARATOR
							+ grp.getNom().replace(" ", "_")
							+ Utils.FILE_SEPARATOR);
			if (uploadFolder.exists() && uploadFolder.isDirectory()) {
				uploadFolder.delete();
			}

		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} finally {
			if (sessionf != null && sessionf.isOpen()) {
				sessionf.close();
			}
		}
	}

	public String importe(ActionEvent evt) throws Exception {
		String date = dateFormat.format(new Date());
		if (nomGroupe != null && !nomGroupe.equals("")) {
			String nomGroupeWithoutSpace = nomGroupe.replaceAll(" ", "_");
			GroupeServiceImpl grServ = new GroupeServiceImpl();
			GroupeBean g;
			g = grServ.getGroupeBeanByName(nomGroupe);

			if (nomAdmin == null || nomAdmin.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Le nom est obligatoire",
						"Le nom est obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idForm:idSuperAdminTabSet:0:idNomAdmin", message);
				return "";
			}
			if (prenomAdmin == null || prenomAdmin.equals("")) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Le prénom est obligatoire",
						"Le prénom est obligatoire");
				FacesContext.getCurrentInstance().addMessage(
						"idForm:idSuperAdminTabSet:0:idPrenomAdmin", message);
				return "";
			}
			if (g == null) {
				if (mailAdmin != null && !mailAdmin.equals("")) {
					String emailRegex = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)";
					Pattern emailPattern = Pattern.compile(emailRegex);
					Matcher matcher = emailPattern.matcher(mailAdmin);
					if (matcher.matches()) {
						if (telephoneAdmin != null
								&& !telephoneAdmin.equals("")) {
							String telRegex = "^0[0-9]([ .-]?[0-9]{2}){4}$";
							Pattern telPattern = Pattern.compile(telRegex);
							matcher = telPattern.matcher(telephoneAdmin);
							if (matcher.matches()) {

								HtmlDataTable table = getParentDatatable((UIComponent) evt
										.getSource());
								File file = (File) table.getRowData();

								File dest = new File(
										Utils.getSessionFileUploadPathReprise(session)
												+ file.getName().replace(
														".rar", ""));
								if (dest.exists() && dest.isDirectory()) {
									dest.delete();
								}
								// dest.mkdirs();

								Properties properties = new Properties();
								InputStream inputStream = Utils.class
										.getClassLoader().getResourceAsStream(
												CCI_PROPERTIES_FILE);
								properties.load(inputStream);

								executeCmd("gpg --homedir "
										+ properties.getProperty("keyPath")
										+ " --batch --passphrase=gpec --output "
										+ Utils.getSessionFileUploadPathReprise(session)
										+ file.getName().replace(".gpg", "")
										+ " --decrypt "
										+ Utils.getSessionFileUploadPathReprise(session)
										+ file.getName());

								String[] cmd = {
										"/bin/sh",
										"-c",
										"cd "
												+ Utils.getSessionFileUploadPathReprise(session)
												+ "; unrar x "
												+ file.getName().replace(
														".gpg", "")
												+ " "
												+ file.getName().replace(
														".rar.gpg", "") };
								executeCd(cmd);

								file = new File(file.getAbsolutePath().replace(
										".gpg", ""));

								// executeCmd("unrar x " + file.getName() + " "
								// + file.getName().replace(".rar", ""));

								if (file != null && file.exists()) {
									Utils.importDataAndUploadedFiles(
											file.getAbsolutePath().replace(
													".rar", ""), nomGroupe,
											session, date);
									// file.delete();
									ImportExportBDD ie = new ImportExportBDD();
									int idGroupe = ie.importe(
											nomGroupe,
											file.getAbsolutePath().replace(
													".rar", "")
													+ Utils.FILE_SEPARATOR
													+ "root_export.csv", essai);
									// renommer les dossiers des salariés
									File newUpload = new File(
											Utils.getSessionFileUploadPath(session)
													+ Utils.FILE_SEPARATOR
													+ nomGroupeWithoutSpace
													+ Utils.FILE_SEPARATOR);
									if (newUpload.isDirectory()) {
										for (File f : newUpload.listFiles()) {
											if (f.isDirectory()) {
												try {
													int idSalarie = Integer
															.parseInt(f
																	.getName());
													if (idSalarie != 0) {
														MappingRepriseServiceImpl map = new MappingRepriseServiceImpl();
														MappingRepriseBean m = map
																.getMappingRepriseByOldIdSalarie(
																		idSalarie,
																		idGroupe);
														if (m != null) {
															f.renameTo(new File(
																	newUpload
																			.getAbsolutePath()
																			+ "/"
																			+ m.getNewId()));
														}
													}
												} catch (NumberFormatException e) {
												}
											}
										}
									}
									Utils.correctionSauvegardeFichier(
											nomGroupe, idGroupe, session);
									// renommer le dossier des logos entreprise
									File newLogo = new File(
											Utils.getSessionFileUploadPath(session)
													+ Utils.FILE_SEPARATOR
													+ nomGroupeWithoutSpace
													+ Utils.FILE_SEPARATOR
													+ "logo_entreprise"
													+ Utils.FILE_SEPARATOR);
									if (newLogo.isDirectory()) {
										for (File f : newLogo.listFiles()) {
											if (f.isDirectory()) {
												try {
													int idEntreprise = Integer
															.parseInt(f
																	.getName());
													if (idEntreprise != 0) {
														MappingRepriseServiceImpl map = new MappingRepriseServiceImpl();
														MappingRepriseBean m = map
																.getMappingRepriseByOldIdEntreprise(
																		idEntreprise,
																		idGroupe);
														if (m != null) {
															f.renameTo(new File(
																	newLogo.getAbsolutePath()
																			+ "/"
																			+ m.getNewId()));
														}
													}
												} catch (NumberFormatException e) {
												}
											}
										}
									}
									File crypte = new File(
											file.getAbsolutePath() + ".gpg");
									executeCmd("rm -rf "
											+ file.getAbsolutePath().replace(
													".rar", ""));
									file.delete();
									crypte.delete();

									Session sessionf = null;
									Transaction tx = null;
									String password = Utils.generatePassword(8);
									try {
										sessionf = HibernateUtil.getSessionFactory().getCurrentSession();
										tx = sessionf.beginTransaction();

										UserServiceImpl usrServ = new UserServiceImpl();
										UserBean usr = new UserBean();
										usr.setId(0);
										usr.setAdmin(true);
										usr.setContratTravail(true);
										usr.setEntretien(true);
										usr.setEvenement(true);
										usr.setFicheDePoste(true);
										usr.setIdGroupe(idGroupe);
										usr.setLogin(mailAdmin);
										usr.setPrenom(prenomAdmin);
										usr.setNom(nomAdmin);
										usr.setPassword(hashPassword(password));
										usr.setRemuneration(true);
										usr.setSuperAdmin(false);
										usrServ.save(usr);

										tx.commit();
									} catch (Exception e) {
										if (tx != null) {
											tx.rollback();
										}
										displayCreationError = true;
										displayCreationOk = false;
										e.printStackTrace();
									} finally {
										if (sessionf != null && sessionf.isOpen()) {
											sessionf.close();
										}
									}

									Properties mailSettings;
									mailSettings = Utils
											.getEmailPropertiesFromClasspath("conf/emailSettings.properties");
									String url = mailSettings
											.getProperty("mail.application.url");

									String subject = "GPEC - création compte";
									if (!specificImportWithSplitByEntreprise) {
										String content = "<html><body>Bonjour,<br/><br/>Votre compte a été créé. Voici vos identifiants d'administrateur :<br/>"
												+ "Login : "
												+ mailAdmin
												+ "<br/>"
												+ "Mot de passe : "
												+ password
												+ "<br/><br/>Connectez-vous à l'application en suivant ce lien : <a href=\""
												+ url
												+ "\">GPEC</a>"
												+ "<br/><br/>Cordialement,<br/>L'équipe GPEC.</body></html>";
										Utils.sendHtmlEmail(mailAdmin, subject,
												content);
									}

									if (specificImportWithSplitByEntreprise) {
										List<Integer> newGroupes = ie
												.splitByEntreprise(idGroupe);
										if (newGroupes != null) {
											displayCreationErrorSplit = false;
											for (Integer groupe : newGroupes) {

												password = Utils
														.generatePassword(8);
												UserServiceImpl usrServ = new UserServiceImpl();
												UserBean usr = new UserBean();
												usr.setId(0);
												usr.setAdmin(true);
												usr.setContratTravail(true);
												usr.setEntretien(true);
												usr.setEvenement(true);
												usr.setFicheDePoste(true);
												usr.setIdGroupe(idGroupe);
												usr.setLogin(mailAdmin);
												usr.setPrenom(prenomAdmin);
												usr.setNom(nomAdmin);
												usr.setPassword(hashPassword(password));
												usr.setRemuneration(true);
												usr.setSuperAdmin(false);
												usrServ.saveOrUppdate(usr,
														groupe);

												String content = "<html><body>Bonjour,<br/><br/>Le compte administrateur a été créé pour l'entreprise \""
														+ grServ.getGroupeBeanById(
																groupe)
																.getNom()
														+ "\". Merci de transmettre les identifiants ci-dessous au nouvel administrateur de l'entreprise :<br/>"
														+ "Login : "
														+ mailAdmin
														+ "<br/>"
														+ "Mot de passe : "
														+ password
														+ "<br/>Une fois connecté, le nouvel administrateur devra modifier son login pour mettre son adresse mail via la page de gestion des utilisateurs dans les paramètres."
														+ "<br/><br/>Connectez-vous à l'application en suivant ce lien : <a href=\""
														+ url
														+ "\">GPEC</a>"
														+ "<br/><br/>Cordialement,<br/>L'équipe GPEC.</body></html>";
												Utils.sendHtmlEmail(mailAdmin,
														subject, content);
											}
										} else {
											displayCreationErrorSplit = true;
										}
										GroupeBean groupe = new GroupeBean();
										groupe.setId(idGroupe);
										grServ.suppression(groupe);
									}
									displayCreationOk = true;
									displayCreationError = false;
									this.nomGroupe = "";
									this.mailAdmin = "";
									this.nomAdmin = "";
									this.prenomAdmin = "";
									this.telephoneAdmin = "";
									this.essai = false;
									this.importBase = false;

									GroupeServiceImpl grpServ = new GroupeServiceImpl();
									this.groupeList.clear();
									for (GroupeBean gr : grpServ
											.getGroupeBeanList(true)) {
										this.groupeList.add(new SelectItem(gr
												.getId(), gr.getNom()));
									}
								}
							} else {
								FacesMessage message = new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"Numéro de téléphone non valide.",
										"Numéro de téléphone non valide.");
								FacesContext
										.getCurrentInstance()
										.addMessage(
												"idForm:idSuperAdminTabSet:0:idTelephoneAdmin",
												message);
							}
						}
					} else {
						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR, "Email incorrect",
								"Email incorrect");
						FacesContext.getCurrentInstance().addMessage(
								"idForm:idSuperAdminTabSet:0:idMailAdmin",
								message);
					}
				} else {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"L'email est obligatoire",
							"L'email est obligatoire");
					FacesContext.getCurrentInstance().addMessage(
							"idForm:idSuperAdminTabSet:0:idMailAdmin", message);
				}
			} else {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Ce nom de groupe est déjà utilisé",
						"Ce nom de groupe est déjà utilisé");
				FacesContext.getCurrentInstance().addMessage(
						"idForm:idSuperAdminTabSet:0:idGroupe", message);
			}
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Le nom de groupe est obligatoire",
					"Le nom de groupe est obligatoire");
			FacesContext.getCurrentInstance().addMessage(
					"idForm:idSuperAdminTabSet:0:idGroupe", message);
		}

		return "";
	}

	private void executeCmd(String command) throws IOException,
			InterruptedException {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(output.toString());
	}

	private void executeCd(String[] command) throws IOException,
			InterruptedException {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(output.toString());
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

	public boolean isFileError() {
		return fileError;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public String getUrlReprise() {
		return Utils.reprise;
	}

	public static void copyFile(final String currentFile, final String newFile)
			throws FileNotFoundException, IOException {
		FileInputStream in = new FileInputStream(currentFile);
		try {
			FileOutputStream out = new FileOutputStream(newFile);
			byte[] buffer = new byte[1024 * 4];
			// ca peut être bien de rendre la taille du buffer paramétrable
			int nbRead;
			try {
				while ((nbRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, nbRead);
				}
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}

	public void remove(ActionEvent evt) {
		modalRenderedDelFile = true;
	}

	public void cancelRemove(ActionEvent evt) {
		modalRenderedDelFile = false;
	}

	public List<UserBean> getUserList() throws Exception {
		UserServiceImpl userServ = new UserServiceImpl();
		if (myAccount) {
			LoginBB loginBB = (LoginBB) FacesContext.getCurrentInstance()
					.getCurrentInstance().getExternalContext().getSessionMap()
					.get("loginBB");

			List<UserBean> users = new ArrayList<UserBean>();
			users.add(userServ.getUserByLogin(loginBB.getUser().getLogin()));
			return users;

		} else {
			if (idGroupeSelectedUser == -1) {
				if (!session.getAttribute("groupe").toString().equals("null")) {
					idGroupeSelectedUser = Integer.parseInt(session
							.getAttribute("groupe").toString());
				}
			}
			return userServ.getUserList(idGroupeSelectedUser, false, false,
					false);
		}
	}

	public void toggleModal(ActionEvent event) {
		this.modif = false;
		this.superAdminForm = false;
		modalRendered = !modalRendered;
	}

	public void toggleModalAccount(ActionEvent event) {
		this.myAccount = false;
		modalRenderedAccount = !modalRenderedAccount;
	}

	public void setUserList(List<UserBean> userList) {
		this.userList = userList;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public boolean isEvenement() {
		return evenement;
	}

	public void setEvenement(boolean evenement) {
		this.evenement = evenement;
	}

	public boolean isRemuneration() {
		return remuneration;
	}

	public void setRemuneration(boolean remuneration) {
		this.remuneration = remuneration;
	}

	public boolean isFicheDePoste() {
		return ficheDePoste;
	}

	public void setFicheDePoste(boolean ficheDePoste) {
		this.ficheDePoste = ficheDePoste;
	}

	public boolean isEntretien() {
		return entretien;
	}

	public void setEntretien(boolean entretien) {
		this.entretien = entretien;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isModif() {
		return modif;
	}

	public void setModif(boolean modif) {
		this.modif = modif;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public boolean isContratTravail() {
		return contratTravail;
	}

	public void setContratTravail(boolean contratTravail) {
		this.contratTravail = contratTravail;
	}

	public String getNomGroupe() {
		return nomGroupe;
	}

	public void setNomGroupe(String nomGroupe) {
		this.nomGroupe = nomGroupe;
	}

	public InputFileData getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFile(InputFileData currentFile) {
		this.currentFile = currentFile;
	}

	public List<File> getFileListAdmin() throws IOException {
		fileListAdmin.clear();

		File directory = new File(
				Utils.getSessionFileUploadPathReprise(session));
		if (!directory.exists()) {
			directory.mkdirs();
		}
		if (directory.list().length > 0) {
			for (String f : directory.list()) {
				if (new File(directory + Utils.FILE_SEPARATOR + f).exists()
						&& new File(directory + Utils.FILE_SEPARATOR + f)
								.isFile() && f.endsWith(".rar.gpg")) {
					fileListAdmin.add(new File(directory + Utils.FILE_SEPARATOR
							+ f));
				}
			}
		}
		return fileListAdmin;
	}

	public void setFileListAdmin(List<File> fileListAdmin) {
		this.fileListAdmin = fileListAdmin;
	}

	public String getCurrentFilePath() {
		return currentFilePath;
	}

	public void setCurrentFilePath(String currentFilePath) {
		this.currentFilePath = currentFilePath;
	}

	public int getFileProgressAdmin() {
		return fileProgressAdmin;
	}

	public void setFileProgressAdmin(int fileProgressAdmin) {
		this.fileProgressAdmin = fileProgressAdmin;
	}

	public boolean isModalRenderedDelFile() {
		return modalRenderedDelFile;
	}

	public void setModalRenderedDelFile(boolean modalRenderedDelFile) {
		this.modalRenderedDelFile = modalRenderedDelFile;
	}

	public List<File> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<File> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public String getMailAdmin() {
		return mailAdmin;
	}

	public void setMailAdmin(String mailAdmin) {
		this.mailAdmin = mailAdmin;
	}

	public List<SelectItem> getGroupeList() throws Exception {
		GroupeServiceImpl grpServ = new GroupeServiceImpl();
		this.groupeList = new ArrayList<SelectItem>();
		for (GroupeBean g : grpServ.getGroupeBeanList(true)) {
			this.groupeList.add(new SelectItem(g.getId(), g.getNom()));
		}
		return groupeList;
	}

	public void setGroupeList(List<SelectItem> groupeList) {
		this.groupeList = groupeList;
	}

	public Integer getIdGroupeSelected() {
		return idGroupeSelected;
	}

	public void setIdGroupeSelected(Integer idGroupeSelected) {
		this.idGroupeSelected = idGroupeSelected;
	}

	public boolean isDisplayCreationOk() {
		return displayCreationOk;
	}

	public void setDisplayCreationOk(boolean displayCreationOk) {
		this.displayCreationOk = displayCreationOk;
	}

	public boolean isDisplayCreationError() {
		return displayCreationError;
	}

	public void setDisplayCreationError(boolean displayCreationError) {
		this.displayCreationError = displayCreationError;
	}

	public List<File> getExtractionFileList() {
		return extractionFileList;
	}

	public void setExtractionFileList(List<File> extractionFileList) {
		this.extractionFileList = extractionFileList;
	}

	public boolean isDisplaySuppressionOk() {
		return displaySuppressionOk;
	}

	public void setDisplaySuppressionOk(boolean displaySuppressionOk) {
		this.displaySuppressionOk = displaySuppressionOk;
	}

	public boolean isDisplaySuppressionError() {
		return displaySuppressionError;
	}

	public void setDisplaySuppressionError(boolean displaySuppressionError) {
		this.displaySuppressionError = displaySuppressionError;
	}

	public boolean isEssai() {
		return essai;
	}

	public void setEssai(boolean essai) {
		this.essai = essai;
	}

	public boolean isResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(boolean resetPassword) {
		this.resetPassword = resetPassword;
	}

	public boolean isDisplayEnvoiMailError() {
		return displayEnvoiMailError;
	}

	public void setDisplayEnvoiMailError(boolean displayEnvoiMailError) {
		this.displayEnvoiMailError = displayEnvoiMailError;
	}

	public String getSuperAdminPassword() {
		return superAdminPassword;
	}

	public void setSuperAdminPassword(String superAdminPassword) {
		this.superAdminPassword = superAdminPassword;
	}

	public boolean isModalAccesSuperAdminRendered() {
		return modalAccesSuperAdminRendered;
	}

	public void setModalAccesSuperAdminRendered(
			boolean modalAccesSuperAdminRendered) {
		this.modalAccesSuperAdminRendered = modalAccesSuperAdminRendered;
	}

	public boolean isImportBase() {
		return importBase;
	}

	public void setImportBase(boolean importBase) {
		this.importBase = importBase;
	}

	public int getIdGroupeSelectedUser() {
		return idGroupeSelectedUser;
	}

	public void setIdGroupeSelectedUser(int idGroupeSelectedUser) {
		this.idGroupeSelectedUser = idGroupeSelectedUser;
	}

	public List<UserBean> getSuperAdminList() {
		return superAdminList;
	}

	public void setSuperAdminList(List<UserBean> superAdminList) {
		this.superAdminList = superAdminList;
	}

	public boolean isModalRendered() {
		return modalRendered;
	}

	public void setModalRendered(boolean modalRendered) {
		this.modalRendered = modalRendered;
	}

	public List<UserBean> getDemandesVersionEssaiList() {
		return demandesVersionEssaiList;
	}

	public void setDemandesVersionEssaiList(
			List<UserBean> demandesVersionEssaiList) {
		this.demandesVersionEssaiList = demandesVersionEssaiList;
	}

	public List<UserBean> getVersionEssaiList() {
		return versionEssaiList;
	}

	public void setVersionEssaiList(List<UserBean> versionEssaiList) {
		this.versionEssaiList = versionEssaiList;
	}

	public String getNomUser() {
		return nomUser;
	}

	public void setNomUser(String nomUser) {
		this.nomUser = nomUser;
	}

	public String getPrenomUser() {
		return prenomUser;
	}

	public void setPrenomUser(String prenomUser) {
		this.prenomUser = prenomUser;
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

	public boolean isSuperAdminForm() {
		return superAdminForm;
	}

	public void setSuperAdminForm(boolean superAdminForm) {
		this.superAdminForm = superAdminForm;
	}

	public boolean isSpecificImportWithSplitByEntreprise() {
		return specificImportWithSplitByEntreprise;
	}

	public void setSpecificImportWithSplitByEntreprise(
			boolean specificImportWithSplitByEntreprise) {
		this.specificImportWithSplitByEntreprise = specificImportWithSplitByEntreprise;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isMyAccount() {
		return myAccount;
	}

	public void setMyAccount(boolean myAccount) {
		this.myAccount = myAccount;
	}

	public boolean isModalRenderedAccount() {
		return modalRenderedAccount;
	}

	public void setModalRenderedAccount(boolean modalRenderedAccount) {
		this.modalRenderedAccount = modalRenderedAccount;
	}
	
	public boolean getNotAutoGenPwd() {
		return this.notAutoGenPwd;
	}
	
	public void switchNotAutoGenPwd(ValueChangeEvent event) {
	    this.notAutoGenPwd = !this.notAutoGenPwd;
	}
	
	// Private
	/**
	 * Facilities to show a message
	 * @param msg
	 * 		The message to show
	 * @param severity
	 * 		The severity of the message
	 * @param idWhereShow
	 * 		The location to show the message
	 */
	private void showMessage(String msg, Severity severity, String idWhereShow) {
		FacesMessage message = new FacesMessage(severity, msg, msg);
		FacesContext.getCurrentInstance().addMessage(idWhereShow, message);
	}
}
