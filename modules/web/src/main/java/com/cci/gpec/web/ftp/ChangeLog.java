package com.cci.gpec.web.ftp;

import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

import org.apache.commons.net.ftp.FTPClient;

public class ChangeLog {

	private FTPClient client;
	private String dossierRacine;
	private String latestVersionFtp;
	private String fileName;
	private String changeLog = "";

	public ChangeLog() throws Exception {
		client = new FTPClient();
		ResourceBundle rb = ResourceBundle.getBundle("ftp");
		dossierRacine = rb.getString("latestVersionFile");

	}

	public void connexion(String host, String id, String password, int port)
			throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		try {
			client.connect(host, port);
			client.login(id, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deconnexion() throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		client.disconnect();
	}

	public String getChangeLog(String fichier) throws Exception {
		try {
			InputStream ips = client.retrieveFileStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null) {
				changeLog += ligne + "\n";
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return changeLog;
	}

}
