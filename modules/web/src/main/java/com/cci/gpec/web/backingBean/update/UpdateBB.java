package com.cci.gpec.web.backingBean.update;

import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.cci.gpec.web.ftp.ChangeLog;
import com.cci.gpec.web.ftp.Ftp;

public class UpdateBB {

	private String host;
	private String id;
	private String password;
	private int port;
	private String version;
	private String latestVersion;
	private String fileName;
	private String urlTelechargement;
	private Ftp ftp;
	private ChangeLog infosVersion;
	private String changeLog = "";
	private boolean fileVide, firstCall;

	public UpdateBB() throws Exception {
		version = "";
		ResourceBundle rb = ResourceBundle.getBundle("ftp");
		host = rb.getString("host");
		id = rb.getString("id");
		password = rb.getString("password");
		port = Integer.parseInt(rb.getString("port"));
		version = rb.getString("versionGPEC");
		ftp = new Ftp();
		infosVersion = new ChangeLog();
		ftp.connexion(host, id, password, port);
		latestVersion = ftp.getLatestVersion();
		firstCall = true;
	}

	public String getInfosVersion() throws Exception {
		infosVersion.connexion(host, id, password, port);
		changeLog = infosVersion.getChangeLog(ftp.getChangeLog());
		infosVersion.deconnexion();

		return changeLog;
	}

	public String getMessage() {
		Ftp ftp = null;
		try {
			ftp = new Ftp();
		} catch (Exception e1) {
			FacesMessage messages = new FacesMessage(
					"Erreur pendant l'initialisation.");
			messages.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage("idForm:idVersion",
					messages);
			return "";
		}

		try {
			ftp.connexion(host, id, password, port);
		} catch (NumberFormatException e) {
			FacesMessage messages = new FacesMessage();
			messages.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage("idForm:idVersion",
					messages);
			try {
				ftp.deconnexion();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				e1.printStackTrace();
			} catch (FTPException e1) {
				e1.printStackTrace();
			}
			return "";

		} catch (IllegalStateException e) {
			FacesMessage messages = new FacesMessage(
					"Vous n'\u00eates pas connect\u00e9s au ftp.");
			messages.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage("idForm:idVersion",
					messages);
			try {
				ftp.deconnexion();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				e1.printStackTrace();
			} catch (FTPException e1) {
				e1.printStackTrace();
			}
			return "";

		} catch (IOException e) {
			FacesMessage messages = new FacesMessage(
					"Impossible de se connecter au ftp, v\u00e9rifiez votre configuration ou votre connexion.");
			messages.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage("idForm:idVersion",
					messages);
			try {
				ftp.deconnexion();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				e1.printStackTrace();
			} catch (FTPException e1) {
				e1.printStackTrace();
			}
			return "";

		} catch (FTPIllegalReplyException e) {
			FacesMessage messages = new FacesMessage(
					"Erreur pendant la connexion au ftp.");
			messages.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage("idForm:idVersion",
					messages);
			try {
				ftp.deconnexion();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				e1.printStackTrace();
			} catch (FTPException e1) {
				e1.printStackTrace();
			}
			return "";

		} catch (FTPException e) {
			FacesMessage messages = new FacesMessage(
					"Erreur pendant la connexion au ftp.");
			messages.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage("idForm:idVersion",
					messages);
			try {
				ftp.deconnexion();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				e1.printStackTrace();
			} catch (FTPException e1) {
				e1.printStackTrace();
			}
			return "";
		}
		try {
			// latestVersion = ftp.getLatestVersion();
			if (latestVersion.equals("")) {
				FacesMessage messages = new FacesMessage(
						"Il n'y a pas encore de mise \u00e0 jour sur le serveur ftp");
				messages.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage(
						"idForm:idVersion", messages);
				ftp.deconnexion();
				return "";
			}
		} catch (Exception e) {
			FacesMessage messages = new FacesMessage(
					"Erreur pendant la recherche de la derni\u00e8re version du logiciel.");
			messages.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage("idForm:idVersion",
					messages);
			try {
				ftp.deconnexion();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				e1.printStackTrace();
			} catch (FTPException e1) {
				e1.printStackTrace();
			}
			return "";
		}
		try {
			if (latestVersion.equals(version)) {
				// FacesMessage message = new FacesMessage("Vous utilisez
				// d\u00E9j\u00E0 la derni\u00e8re version du logiciel
				// ("+version+").");
				// message.setSeverity(FacesMessage.SEVERITY_INFO);
				// FacesContext.getCurrentInstance().addMessage("idForm:idVersion",
				// message);
				ftp.deconnexion();
				return "notNeedUpdate";
			} else {
				// FacesMessage message = new FacesMessage("Vous n'utilisez pas
				// la derni\u00e8re version du logiciel ("+version+").");
				// message.setSeverity(FacesMessage.SEVERITY_INFO);
				// FacesContext.getCurrentInstance().addMessage("idForm:idVersion",
				// message);
				ftp.deconnexion();
				return "needUpdate";
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				ftp.deconnexion();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				e1.printStackTrace();
			} catch (FTPException e1) {
				e1.printStackTrace();
			}
		}
		return "";
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) throws Exception {
		this.version = version;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getLatestVersion() throws Exception {
		return latestVersion;
	}

	public void setLatestVersion(String latestVersion) {
		this.latestVersion = latestVersion;
	}

	public String getFileName() {
		return fileName;
	}

	public boolean isFileVide() throws Exception {
		if (firstCall) {
			fileVide = ftp.isFileVide();
			firstCall = false;
		}
		return fileVide;
	}

	public String getUrl() throws Exception {
		String url = "ftp://" + this.id + ":" + this.password + "@" + this.host
				+ "/" + ftp.getURL();
		return url;
	}

	public String getUrlTelechargement() throws Exception {
		if ((urlTelechargement == null) || (urlTelechargement.equals("")))
			urlTelechargement = getUrl();
		return urlTelechargement;
	}

	public String getChangeLog() throws Exception {
		getInfosVersion();
		return changeLog;
	}

	public void setChangeLog(String changeLog) {
		this.changeLog = changeLog;
	}

}
