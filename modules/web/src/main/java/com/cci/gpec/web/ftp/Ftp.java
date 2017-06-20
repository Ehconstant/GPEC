package com.cci.gpec.web.ftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

public class Ftp extends MyTransferListener{
	
	private FTPClient client; 
	private String dossierRacine;
	private String latestVersionFtp;
	private String fileName;
	
	public Ftp() throws Exception {
		client = new FTPClient();
		ResourceBundle rb = ResourceBundle.getBundle("ftp");
		dossierRacine = rb.getString("latestVersionFile");
		
	}
	
	public void connexion(String host, String id, String password, int port) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		client.connect(host, port);
		client.login(id, password);	
	}
	
	public void upload(File f, String salariePath) throws IllegalStateException, FileNotFoundException, 
									  IOException, FTPIllegalReplyException, 
									  FTPException, FTPDataTransferException, FTPAbortedException {
		StringTokenizer st = new StringTokenizer(salariePath, "/");
	     while (st.hasMoreTokens()) {
	         client.changeDirectory(st.nextToken());
	     }

		client.upload(f,new MyTransferListener());
	}
	
	public void deconnexion() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		client.disconnect(true);
	}
	
	public String getLatestVersionDirectory() throws NumberFormatException, IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException, FTPListParseException {
		client.changeDirectory(dossierRacine);
		int x = -1;
		int y = -1;
		int z = -1;
		if (client.listNames().length == 0) {
			latestVersionFtp = "";
			client.changeDirectoryUp();
			return latestVersionFtp;
		}
		for (int i=0; i<client.listNames().length; i++) {
			String file = client.listNames()[i];
			if (!file.equals(".") && !file.equals("..")) {
				StringTokenizer sk = new StringTokenizer(file,".");
				int valX = -1;
				int valY = -1;
				int valZ = -1;
				if (sk.hasMoreElements()) {
					
					switch(sk.countTokens()) {
						case 1 : String s = sk.nextToken();
								 valX = Integer.parseInt(s);
								 break;
						case 2 : String str = sk.nextToken();
								 valX = Integer.parseInt(str);
								 str = sk.nextToken();
								 valY = Integer.parseInt(str); 
								 break;
						case 3 : str = sk.nextToken();
								 valX = Integer.parseInt(str);
								 str = sk.nextToken();
								 valY = Integer.parseInt(str); 
								 str = sk.nextToken();
								 valZ = Integer.parseInt(str);
								 break;
					}
					
					if (x < valX) {
						x = valX;
						y = -1;
						z = -1;
					}
					
					if ((x == valX) && (y < valY)) {
						y = valY;
						z = -1;
					}
					if ((x == valX) && (y == valY) && (z < valZ)) {
						z = valZ;
					}
				}
			}
		}
		String res="";
		if (x > -1)
			res += x;
		if (y > -1)
			res += "."+y;
		if (z > -1)
			res += "."+z;
		latestVersionFtp = res;
		client.changeDirectoryUp();
		return res;
	}
	
	public String getLatestVersion() throws Exception {
		getLatestVersionDirectory();
		return latestVersionFtp;
	}
	
	public String getURL() throws Exception {
		latestVersionFtp = getLatestVersion();
		client.changeDirectory(dossierRacine);
		client.changeDirectory(latestVersionFtp+"/");
		String nom = "";
		for (int j=0; j<client.listNames().length; j++) {
			nom = client.listNames()[j];
			if (!nom.equals(".") && !nom.equals(".."))
				break;
		}
		String url = dossierRacine+latestVersionFtp+"/"+nom;
		client.changeDirectoryUp();
		client.changeDirectoryUp();
		return url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public boolean isFileVide() throws Exception {

		getLatestVersion();
		client.changeDirectory(dossierRacine);
		client.changeDirectory(latestVersionFtp+"/");
		int nbFile = client.listNames().length;
		if (nbFile>0) {
			client.changeDirectoryUp();
			client.changeDirectoryUp();
			return false;
		}
		client.changeDirectoryUp();
		client.changeDirectoryUp();
		return true;
	}
	public String getChangeLog() throws Exception {
		latestVersionFtp = getLatestVersion();
		client.changeDirectory(dossierRacine);
		client.changeDirectory(latestVersionFtp+"/");

		String url = "/changelog/changelog.txt";
		client.changeDirectoryUp();
		client.changeDirectoryUp();
		return url;
	}
}
