package com.cci.gpec.web.ftp;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class MyTransferListener implements FTPDataTransferListener {

	public void started() {
		
	}

	public void transferred(int length) {
		// Yet other length bytes has been transferred since the last time this
		// method was called
	}

	public void completed() {
		// Transfer completed
		FacesMessage message = new FacesMessage("Le fichier Excel contenant les salari\u00E9s a bien \u00E9t\u00E9 copi\u00E9 sur le serveur!"); 
		FacesMessage message2 = new FacesMessage("Le fichier xml contenant les salari\u00E9s a bien \u00E9t\u00E9 copi\u00E9 sur le serveur!"); 
		message.setSeverity(FacesMessage.SEVERITY_INFO);
		
		FacesContext.getCurrentInstance().addMessage("idForm:transmission", message2);
	}

	public void aborted() {
		FacesMessage message = new FacesMessage("Le transfert a \u00E9t\u00E9 annul\u00E9."); 
		message.setSeverity(FacesMessage.SEVERITY_INFO);
		FacesContext.getCurrentInstance().addMessage("idForm:transmission", message);
	}

	public void failed() {
		FacesMessage message = new FacesMessage("Le transfert du fichier Excel contenant les salari\u00E9s a echou\u00E9."); 
		FacesMessage message2 = new FacesMessage("Le transfert du fichier Xml contenant les salari\u00E9s a echou\u00E9."); 
		message.setSeverity(FacesMessage.SEVERITY_FATAL);
		FacesContext.getCurrentInstance().addMessage("idForm:transmission", message2);
	}



}
