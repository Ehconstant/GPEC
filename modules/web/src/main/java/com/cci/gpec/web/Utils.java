package com.cci.gpec.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.EmailException;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Component;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.AccidentBean;
import com.cci.gpec.commons.EntretienBean;
import com.cci.gpec.commons.FormationBean;
import com.cci.gpec.commons.HabilitationBean;
import com.cci.gpec.commons.ParcoursBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.AccidentServiceImpl;
import com.cci.gpec.metier.implementation.EntretienServiceImpl;
import com.cci.gpec.metier.implementation.FormationServiceImpl;
import com.cci.gpec.metier.implementation.HabilitationServiceImpl;
import com.cci.gpec.metier.implementation.ParcoursServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.web.backingBean.Admin.AdminFormBB;
import com.cci.gpec.web.backingBean.salarie.SalarieFormBB;

/**
 * Classe utilitaire.
 */
@Component
public class Utils {

	public static final String URL_SEPARATOR = "/";
	public static String FILE_SEPARATOR = "";
	public static final String FICHE_METIER = "FicheMetier";
	public static final String LOGO = "logo_entreprise";
	public static String upload = "upload";
	public static String export = "fichier_export_xml";
	public static String reprise = "fichier_csv_reprise";
	public static String extract = "extraction";
	public static String archived = "archived";
	public static final String CCI_PROPERTIES_FILE = "CCI.properties";
	private static final String UPLOAD_DIR_PROPERTY = "uploadDir";
	private static final String PORT_PROPERTY = "port";
	private static final String UPLOAD_DIR_PROPERTY_EMPTY = "${uploadDir}";
	private static Process p;

	static {
		try {
			Properties properties = new Properties();
			InputStream inputStream = Utils.class.getClassLoader()
					.getResourceAsStream(CCI_PROPERTIES_FILE);
			properties.load(inputStream);
			if (!UPLOAD_DIR_PROPERTY_EMPTY.equals(properties
					.getProperty(UPLOAD_DIR_PROPERTY))) {
				upload = properties.getProperty(UPLOAD_DIR_PROPERTY);
			}
		} catch (IOException e) {
			System.err.println("property file '" + CCI_PROPERTIES_FILE
					+ "' not found in the classpath");
		}
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Windows"))
			FILE_SEPARATOR = "\\";
		else
			FILE_SEPARATOR = "/";
	}

	/**
	 * Donne le path du répertoire upload/id/répertoire dans eclipse.
	 * 
	 * @param session
	 * @return
	 */
	public static String getSessionFileUploadPath(HttpSession session, int id,
			String directory, int second, boolean entreprise, boolean logo,
			String nomGroupe) {
		String applicationPath = new java.io.File(session.getServletContext()
				.getRealPath("/")).getParent();
		nomGroupe = nomGroupe.replace(" ", "_");

		String sessionFileUploadPath = "";
		if (directory != null) {
			if (logo) {
				sessionFileUploadPath = applicationPath + URL_SEPARATOR
						+ upload + URL_SEPARATOR + nomGroupe + URL_SEPARATOR
						+ "logo_entreprise" + URL_SEPARATOR + id
						+ URL_SEPARATOR;
			} else {
				if (!entreprise) {
					sessionFileUploadPath = applicationPath + URL_SEPARATOR
							+ upload + URL_SEPARATOR + nomGroupe
							+ URL_SEPARATOR + id + URL_SEPARATOR + directory
							+ URL_SEPARATOR;
				} else {
					sessionFileUploadPath = applicationPath + URL_SEPARATOR
							+ upload + URL_SEPARATOR + nomGroupe
							+ URL_SEPARATOR + FICHE_METIER + URL_SEPARATOR;
				}
			}
		} else {
			sessionFileUploadPath = applicationPath + URL_SEPARATOR + upload
					+ URL_SEPARATOR + nomGroupe + URL_SEPARATOR + id
					+ URL_SEPARATOR;
		}
		return sessionFileUploadPath;
	}

	/**
	 * Donne le path du répertoire upload dans eclipse.
	 * 
	 * @param session
	 * @return
	 */
	public static String getSessionFileUploadPath(HttpSession session) {
		String applicationPath = new java.io.File(session.getServletContext()
				.getRealPath("/")).getParent();

		String sessionFileUploadPath = applicationPath + URL_SEPARATOR + upload;
		return sessionFileUploadPath;
	}

	public static String getImgPath(HttpSession session) {
		String fullPath = session.getServletContext().getRealPath("/");

		String sessionFileUploadPath = fullPath + URL_SEPARATOR + "img"
				+ URL_SEPARATOR;
		return sessionFileUploadPath;
	}

	public static String getSessionFileUploadPathExport(HttpSession session) {
		String applicationPath = new java.io.File(session.getServletContext()
				.getRealPath("/")).getParent();

		if (!new File(applicationPath + URL_SEPARATOR + export).exists()) {
			new File(applicationPath + URL_SEPARATOR + export).mkdirs();
		}
		String sessionFileUploadPath = applicationPath + URL_SEPARATOR + export
				+ URL_SEPARATOR;
		return sessionFileUploadPath;
	}

	public static String getSessionFileUploadPathReprise(HttpSession session)
			throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = Utils.class.getClassLoader()
				.getResourceAsStream(CCI_PROPERTIES_FILE);
		properties.load(inputStream);

		String reprise = properties.getProperty("databaseExtractionFolder");
		if (!new File(reprise).exists()) {
			new File(reprise).mkdirs();
		}
		String sessionFileUploadPath = reprise + URL_SEPARATOR;
		return sessionFileUploadPath;
	}

	public static String getSessionFileUploadPathExtraction(HttpSession session) {
		String applicationPath = new java.io.File(session.getServletContext()
				.getRealPath("/")).getParent();

		if (!new File(applicationPath + URL_SEPARATOR + extract).exists()) {
			new File(applicationPath + URL_SEPARATOR + extract).mkdirs();
		}
		String sessionFileUploadPath = applicationPath + URL_SEPARATOR
				+ extract + URL_SEPARATOR;
		return sessionFileUploadPath;
	}

	public static String init() throws HibernateException, IOException {
		return "init";
	}

	public static String getHomePageUrl() {
		String startUrl = "";
		try {
			startUrl = "http://" + InetAddress.getLocalHost().getHostName()
					+ ":" + getPort() + "/gpec/";
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return startUrl;
	}

	public static String getExtractionFileUrl(int idGroupe)
			throws UnknownHostException {
		String url = "";

		String startUrl = "http://" + InetAddress.getLocalHost().getHostName()
				+ ":" + getPort() + "/";

		url = startUrl + extract + URL_SEPARATOR + idGroupe + URL_SEPARATOR;
		return url;
	}

	/**
	 * @param salarieFormBB
	 * @throws UnknownHostException
	 */
	public static String getFileUrl(int idSalarie, String directory,
			boolean rattachement, boolean entreprise, boolean logo,
			boolean export, String nomGroupe) throws UnknownHostException {
		nomGroupe = nomGroupe.replace(" ", "_");

		if (!logo) {
			SalarieFormBB salarieFormBB = (SalarieFormBB) FacesContext
					.getCurrentInstance().getCurrentInstance()
					.getExternalContext().getSessionMap().get("salarieFormBB");

			if (!rattachement && !entreprise) {
				idSalarie = salarieFormBB.getId();
			}
		}
		String osName = System.getProperty("os.name");

		String url = "";
		osName = System.getProperty("os.name");

		if (osName.startsWith("Mac OS") || osName.startsWith("Linux")) {
			String startUrl = "http://localhost:" + getPort() + "/";
			if (directory != null) {
				if (logo) {
					if (export) {
						url = startUrl + upload + URL_SEPARATOR + nomGroupe
								+ URL_SEPARATOR + LOGO + URL_SEPARATOR
								+ idSalarie + URL_SEPARATOR;
					} else {
						url = startUrl + upload + URL_SEPARATOR + nomGroupe
								+ URL_SEPARATOR + LOGO + URL_SEPARATOR;
					}
				} else {
					if (!entreprise) {
						url = startUrl + upload + URL_SEPARATOR + nomGroupe
								+ URL_SEPARATOR + idSalarie + URL_SEPARATOR
								+ directory + URL_SEPARATOR;
					} else {
						url = startUrl + upload + URL_SEPARATOR + nomGroupe
								+ URL_SEPARATOR + FICHE_METIER + URL_SEPARATOR;
					}
				}
			} else {
				url = startUrl + upload + URL_SEPARATOR + nomGroupe
						+ URL_SEPARATOR + idSalarie + URL_SEPARATOR;
			}
		} else if (osName.startsWith("Windows")) {
			String startUrl = "http://"
					+ InetAddress.getLocalHost().getHostName() + ":"
					+ getPort() + "/";

			if (directory != null) {
				if (logo) {
					if (export) {
						url = "http://localhost:" + getPort() + "/" + upload
								+ URL_SEPARATOR + nomGroupe + URL_SEPARATOR
								+ LOGO + URL_SEPARATOR + idSalarie
								+ URL_SEPARATOR;
					} else {
						url = startUrl + upload + URL_SEPARATOR + nomGroupe
								+ URL_SEPARATOR + LOGO + URL_SEPARATOR;
					}
				} else {
					if (!entreprise) {
						url = startUrl + upload + URL_SEPARATOR + nomGroupe
								+ URL_SEPARATOR + idSalarie + URL_SEPARATOR
								+ directory + URL_SEPARATOR;
					} else {
						url = startUrl + upload + URL_SEPARATOR + nomGroupe
								+ URL_SEPARATOR + FICHE_METIER + URL_SEPARATOR;
					}
				}
			} else {
				url = startUrl + upload + URL_SEPARATOR + nomGroupe
						+ URL_SEPARATOR + idSalarie + URL_SEPARATOR;
			}
		}
		return url;
	}

	public static String generatePassword(int length) {
		String all = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ&?(-'_^@)=+°$*%¨!:/;.,?<>";
		String number = "0123456789";
		String min = "abcdefghijklmnopqrstuvwxyz";
		String maj = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String spec = "&?(-'_^@)=+°$*%¨!:/;.,?<>";
		Random rand = new Random(System.currentTimeMillis());

		// on récupère aléatoirement 1 occurence de chaque type
		int pos = rand.nextInt(number.length());
		String numberRand = number.substring(pos, pos + 1);
		pos = rand.nextInt(min.length());
		String minRand = min.substring(pos, pos + 1);
		pos = rand.nextInt(maj.length());
		String majRand = maj.substring(pos, pos + 1);
		pos = rand.nextInt(spec.length());
		String specRand = spec.substring(pos, pos + 1);

		StringBuilder sb = new StringBuilder();
		sb = new StringBuilder();
		pos = rand.nextInt(all.length());
		sb.append(all.charAt(pos));
		sb.append(numberRand);
		pos = rand.nextInt(all.length());
		sb.append(all.charAt(pos));
		sb.append(minRand);
		pos = rand.nextInt(all.length());
		sb.append(all.charAt(pos));
		sb.append(specRand);
		pos = rand.nextInt(all.length());
		sb.append(all.charAt(pos));
		sb.append(majRand);

		return sb.toString();

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

	public static boolean verifyPassword(String pwd) {

		if (pwd.length() < 8) {
			return false;
		}

		String pwdMaj = "[A-Z]";
		String pwdMin = "[a-z]";
		String pwdSC = "[\\W]";
		String pwdNumber = "[0-9]";

		Pattern passwordMajPattern = Pattern.compile(pwdMaj);
		Matcher matcher = passwordMajPattern.matcher(pwd);
		if (matcher.find()) {
			passwordMajPattern = Pattern.compile(pwdMin);
			matcher = passwordMajPattern.matcher(pwd);
			if (matcher.find()) {
				passwordMajPattern = Pattern.compile(pwdSC);
				matcher = passwordMajPattern.matcher(pwd);
				if (matcher.find()) {
					passwordMajPattern = Pattern.compile(pwdNumber);
					matcher = passwordMajPattern.matcher(pwd);
					if (matcher.find()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String getHostName() throws UnknownHostException {
		String hostName = "";
		hostName = "http://" + InetAddress.getLocalHost().getHostName() + ":"
				+ getPort();
		return hostName;
	}

	public static String getPort() {
		String port = "";
		try {
			Properties properties = new Properties();
			InputStream inputStream = Utils.class.getClassLoader()
					.getResourceAsStream(CCI_PROPERTIES_FILE);
			properties.load(inputStream);

			port = properties.getProperty(PORT_PROPERTY);

		} catch (IOException e) {
			System.err.println("property file '" + CCI_PROPERTIES_FILE
					+ "' not found in the classpath");
		}
		return port;
	}

	/**
	 * Copy file from currentFile to newFile
	 * 
	 * @param currentFile
	 * @param newFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
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

	public static boolean after(GregorianCalendar d1, GregorianCalendar d2) {
		// On teste si les dates sont egales, car GregorianCalendar ne prend pas
		// en comtpe ce cas, sinon on renvoi
		// true si d1 est avant d2
		if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
			if (d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)) {
				if (d1.get(Calendar.DATE) == d2.get(Calendar.DATE)) {
					return true;
				}
			}
		}
		return (d1.after(d2));
	}

	public static boolean before(GregorianCalendar d1, GregorianCalendar d2) {
		// On teste si les dates sont egales, car GregorianCalendar ne prend pas
		// en comtpe ce cas, sinon on renvoi
		// true si d1 est avant d2
		if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
			if (d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)) {
				if (d1.get(Calendar.DATE) == d2.get(Calendar.DATE)) {
					return true;
				}
			}
		}
		return (d1.before(d2));
	}

	public static boolean isInEntreprise(List<ParcoursBean> parcoursBeanList,
			Date debutEvenementd, Date finEvenementd) {

		GregorianCalendar debutEvenement = new GregorianCalendar();
		GregorianCalendar finEvenement = new GregorianCalendar();

		debutEvenement.setTime(debutEvenementd);
		if (finEvenementd != null)
			finEvenement.setTime(finEvenementd);

		for (ParcoursBean p : parcoursBeanList) {
			GregorianCalendar debutParcours = new GregorianCalendar();
			GregorianCalendar finParcours = new GregorianCalendar();
			debutParcours.setTime(p.getDebutFonction());
			if (p.getFinFonction() == null) {
				if (debutEvenement.after(debutParcours)
						|| debutEvenement.equals(debutParcours)) {
					return true;
				}
				if (finEvenement.after(debutParcours)
						|| finEvenement.equals(debutParcours)) {
					return true;
				}
			} else {
				finParcours.setTime(p.getFinFonction());
				if (finEvenement == null) {
					if (debutEvenement.equals(debutParcours)) {
						return true;
					}
					if (debutEvenement.equals(finParcours)) {
						return true;
					}
					if (debutEvenement.after(debutParcours)
							&& debutEvenement.before(finParcours)) {
						return true;
					}
				} else {
					if (debutEvenement.equals(debutParcours)) {
						return true;
					}
					if (debutEvenement.equals(finParcours)) {
						return true;
					}
					if (finEvenement.equals(debutParcours)) {
						return true;
					}
					if (finEvenement.equals(finParcours)) {
						return true;
					}
					if (debutEvenement.after(debutParcours)
							&& debutEvenement.before(finParcours)) {
						return true;
					}
					if (finEvenement.after(debutParcours)
							&& finEvenement.before(finParcours)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public static void correctionSauvegardeFichier(String nomGroupe,
			int idGroupe, HttpSession session) throws FileNotFoundException,
			IOException, Exception {

		nomGroupe = nomGroupe.replace(" ", "_");

		SalarieServiceImpl sal = new SalarieServiceImpl();

		for (SalarieBean s : sal.getSalariesList(idGroupe)) {

			if (s.getCv() != null && !s.getCv().equals("")) {
				s.setCv(s.getCv().replaceAll("/", "|").replaceAll("\\\\", "|"));

				if (s.getCv().lastIndexOf("|") != -1) {
					// on ne garde que le nom du fichier

					s.setCv(s.getCv().substring(s.getCv().lastIndexOf("|") + 1,
							s.getCv().length()));
					if (!new File(Utils.getSessionFileUploadPath(session,
							s.getId(), null, 0, false, false, nomGroupe)
							+ s.getCv()).exists()) {
						s.setCv(null);
						sal.saveOrUppdate(s);
					} else {
						sal.saveOrUppdate(s);
					}
				}
			}

			ParcoursServiceImpl servpar = new ParcoursServiceImpl();

			List<ParcoursBean> parList = new ArrayList<ParcoursBean>();
			parList = servpar.getParcoursBeanListByIdSalarie(s.getId());
			Collections.sort(parList);
			Collections.reverse(parList);
			for (ParcoursBean p : parList) {

				if (p.getJustificatif() != null
						&& !p.getJustificatif().equals("")) {
					p.setJustificatif(p.getJustificatif().replaceAll("/", "|")
							.replaceAll("\\\\", "|"));

					if (p.getJustificatif().lastIndexOf("|") != -1) {
						// on ne garde que le nom du fichier

						p.setJustificatif(p.getJustificatif().substring(
								p.getJustificatif().lastIndexOf("|") + 1,
								p.getJustificatif().length()));
						if (!new File(Utils.getSessionFileUploadPath(session,
								s.getId(), "parcours", 0, false, false,
								nomGroupe) + p.getJustificatif()).exists()) {
							p.setJustificatif(null);
							servpar.saveOrUppdate(p);
						} else {
							servpar.saveOrUppdate(p);
						}
					}
				}
			}

			HabilitationServiceImpl serv = new HabilitationServiceImpl();

			List<HabilitationBean> habList = new ArrayList<HabilitationBean>();
			habList = serv.getHabilitationsListFromIdSalarie(s.getId());
			Collections.sort(habList);
			Collections.reverse(habList);
			for (HabilitationBean h : habList) {

				if (h.getJustificatif() != null
						&& !h.getJustificatif().equals("")) {
					h.setJustificatif(h.getJustificatif().replaceAll("/", "|")
							.replaceAll("\\\\", "|"));
					if (h.getJustificatif().lastIndexOf("|") != -1) {
						// on ne garde que le nom du fichier

						h.setJustificatif(h.getJustificatif().substring(
								h.getJustificatif().lastIndexOf("|") + 1,
								h.getJustificatif().length()));
						if (!new File(Utils.getSessionFileUploadPath(session,
								s.getId(), "habilitation", 0, false, false,
								nomGroupe) + h.getJustificatif()).exists()) {
							h.setJustificatif(null);
							serv.saveOrUppdate(h);
						} else {
							serv.saveOrUppdate(h);
						}
					}
				}
			}

			FormationServiceImpl servform = new FormationServiceImpl();

			List<FormationBean> formList = new ArrayList<FormationBean>();
			formList = servform.getFormationBeanListByIdSalarie(s.getId());
			Collections.sort(formList);
			Collections.reverse(formList);
			for (FormationBean f : formList) {

				if (f.getJustificatif() != null
						&& !f.getJustificatif().equals("")) {
					f.setJustificatif(f.getJustificatif().replaceAll("/", "|")
							.replaceAll("\\\\", "|"));

					if (f.getJustificatif().lastIndexOf("|") != -1) {
						// on ne garde que le nom du fichier

						f.setJustificatif(f.getJustificatif().substring(
								f.getJustificatif().lastIndexOf("|") + 1,
								f.getJustificatif().length()));
						if (!new File(Utils.getSessionFileUploadPath(session,
								s.getId(), "formation", 0, false, false,
								nomGroupe) + f.getJustificatif()).exists()) {
							f.setJustificatif(null);
							servform.saveOrUppdate(f);
						} else {
							servform.saveOrUppdate(f);
						}
					}
				}
			}

			AbsenceServiceImpl servabs = new AbsenceServiceImpl();

			List<AbsenceBean> absList = new ArrayList<AbsenceBean>();
			absList = servabs.getAbsenceBeanListByIdSalarie(s.getId());
			Collections.sort(absList);
			Collections.reverse(absList);
			for (AbsenceBean a : absList) {

				if (a.getJustificatif() != null
						&& !a.getJustificatif().equals("")) {
					a.setJustificatif(a.getJustificatif().replaceAll("/", "|")
							.replaceAll("\\\\", "|"));

					if (a.getJustificatif().lastIndexOf("|") != -1) {
						// on ne garde que le nom du fichier

						a.setJustificatif(a.getJustificatif().substring(
								a.getJustificatif().lastIndexOf("|") + 1,
								a.getJustificatif().length()));
						if (!new File(Utils.getSessionFileUploadPath(session,
								s.getId(), "absence", 0, false, false,
								nomGroupe) + a.getJustificatif()).exists()) {
							a.setJustificatif(null);
							servabs.saveOrUppdate(a);
						} else {
							servabs.saveOrUppdate(a);
						}
					}
				}
			}

			AccidentServiceImpl servacc = new AccidentServiceImpl();

			List<AccidentBean> accList = new ArrayList<AccidentBean>();
			accList = servacc.getAccidentBeanListByIdSalarie(s.getId());
			Collections.sort(accList);
			Collections.reverse(accList);
			for (AccidentBean a : accList) {

				if (a.getJustificatif() != null
						&& !a.getJustificatif().equals("")) {
					a.setJustificatif(a.getJustificatif().replaceAll("/", "|")
							.replaceAll("\\\\", "|"));

					if (a.getJustificatif().lastIndexOf("|") != -1) {
						// on ne garde que le nom du fichier

						a.setJustificatif(a.getJustificatif().substring(
								a.getJustificatif().lastIndexOf("|") + 1,
								a.getJustificatif().length()));
						if (!new File(Utils.getSessionFileUploadPath(session,
								s.getId(), "accident", 0, false, false,
								nomGroupe) + a.getJustificatif()).exists()) {
							a.setJustificatif(null);
							servacc.saveOrUppdate(a);
						} else {
							servacc.saveOrUppdate(a);
						}
					}
				}
			}

			EntretienServiceImpl servent = new EntretienServiceImpl();

			List<EntretienBean> entList = new ArrayList<EntretienBean>();
			entList = servent.getEntretiensListByIdSalarie(s.getId());
			Collections.sort(entList);
			Collections.reverse(entList);
			for (EntretienBean e : entList) {

				if (e.getJustificatif() != null
						&& !e.getJustificatif().equals("")) {
					e.setJustificatif(e.getJustificatif().replaceAll("/", "|")
							.replaceAll("\\\\", "|"));

					if (e.getJustificatif().lastIndexOf("|") != -1) {
						// on ne garde que le nom du fichier

						e.setJustificatif(e.getJustificatif().substring(
								e.getJustificatif().lastIndexOf("|") + 1,
								e.getJustificatif().length()));
						if (!new File(Utils.getSessionFileUploadPath(session,
								s.getId(), "entretien", 0, false, false,
								nomGroupe) + e.getJustificatif()).exists()) {
							e.setJustificatif(null);
							servent.saveOrUppdate(e);
						} else {
							servent.saveOrUppdate(e);
						}
					}
				}
			}

		}
	}

	private static final int BUFFER_SIZE = 4096;

	private static void extractFile(ZipInputStream in, File outdir, String name)
			throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(new File(outdir, name)));
		int count = -1;
		while ((count = in.read(buffer)) != -1)
			out.write(buffer, 0, count);
		out.close();
	}

	private static void mkdirs(File outdir, String path) {
		File d = new File(outdir, path);
		if (!d.exists()) {
			d.mkdirs();
		}
	}

	private static String dirpart(String name) {
		int s = name.lastIndexOf(File.separatorChar);
		return s == -1 ? null : name.substring(0, s);
	}

	public static void importDataAndUploadedFiles(String groupFolder,
			String nomGroupe, HttpSession session, String date) {

		try {
			new File(getSessionFileUploadPathReprise(session) + archived
					+ FILE_SEPARATOR).mkdirs();
			// copie du répertoire à importer dans le dossier archived
			FileUtils.copyFile(new File(groupFolder + ".rar.gpg"), new File(
					getSessionFileUploadPathReprise(session) + archived
							+ FILE_SEPARATOR + new File(groupFolder).getName()
							+ "_" + date + ".rar.gpg"));
			File upload = new File(getSessionFileUploadPathReprise(session)
					+ new File(groupFolder).getName() + FILE_SEPARATOR
					+ "upload" + FILE_SEPARATOR);
			// on renomme le dossier /nomGroupe/upload en /nomGroupe/nomGroupe
			// pour le copier dans le dossier upload de l'application
			if (upload.exists() && upload.isDirectory()) {
				upload.renameTo(new File(
						getSessionFileUploadPathReprise(session)
								+ new File(groupFolder).getName()
								+ FILE_SEPARATOR + nomGroupe.replace(" ", "_")
								+ FILE_SEPARATOR));
			}
			upload = new File(getSessionFileUploadPathReprise(session)
					+ new File(groupFolder).getName() + FILE_SEPARATOR
					+ nomGroupe.replace(" ", "_") + FILE_SEPARATOR);
			File newUpload = new File(getSessionFileUploadPath(session)
					+ FILE_SEPARATOR);
			newUpload.mkdirs();
			if (upload.exists() && upload.isDirectory()) {
				FileUtils.copyDirectoryToDirectory(upload, newUpload);
			}
			File group = new File(groupFolder);
			group.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void sendHtmlEmail(String userEmail, String subject,
			String content) throws IOException, EmailException,
			InterruptedException {

		new ThreadMailSender(userEmail, subject, content).start();
	}

	public static Properties getEmailPropertiesFromClasspath(
			String propertiesFileName) throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = AdminFormBB.class.getClassLoader()
				.getResourceAsStream(propertiesFileName);
		if (inputStream == null) {
			throw new FileNotFoundException("property file '"
					+ propertiesFileName + "' not found in the classpath");
		}
		properties.load(inputStream);
		return properties;
	}

	public static void cd(String path) {
		try {
			Runtime r = Runtime.getRuntime();
			String osName = System.getProperty("os.name");

			p = r.exec(new String[] { "/bin/sh", "-c", "cd " + path });

			p.waitFor();
			int myStatus = -1;
			for (boolean ready = false; !ready;)
				try {
					myStatus = p.exitValue();
					ready = true;
				} catch (IllegalThreadStateException illegalthreadstateexception) {
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 public static void compressCommandLine(String zipFile, String filesType, String directory, HttpSession session) {
		 try {
			 Runtime r = Runtime.getRuntime();
			 String osName = System.getProperty("os.name");
			
			 if (osName.startsWith("Windows")) {
				 p = r.exec(new String[] { "C:\\Program Files (x86)\\7-Zip\\7z.exe", "a", zipFile, filesType });
			 } else {
				 p = r.exec(new String[] { "7za", "a", "-r", directory + ".zip", directory });
			 }
			 
			 p.waitFor();
			 
			 int myStatus = -1;
			 for (boolean ready = false; !ready;) {
				 try {
					 myStatus = p.exitValue();
					 ready = true;
				 } catch (IllegalThreadStateException illegalthreadstateexception) {
					 illegalthreadstateexception.printStackTrace();
				 }
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	 }
}
