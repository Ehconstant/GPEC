package com.cci.gpec.db.connection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Classe permettant la création de la session hibernate.
 * 
 */
public class HibernateUtil {
	public static SessionFactory sessionFactory;
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	// public static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";
	// public static final String DRIVER =
	// "org.apache.derby.jdbc.EmbeddedDriver";
	public static String PATH_DATABASE;
	private static final String DATABASE_CONNECTION_URL_LINUX_REPRISE = "database.connection.urlLinuxReprise";
	private static final String DATABASE_CONNECTION_URL_MAC_REPRISE = "database.connection.urlMacReprise";
	private static final String DATABASE_CONNECTION_URL_WINDOWS_REPRISE = "database.connection.urlWindowsReprise";
	private static final String DATABASE_CONNECTION_URL = "database.connection.url";
	private static final String DATABASE_EMBEDDED = "database.embedded";
	private static final String DATABASE_PROPERTIES_FILE = "database.properties";

	public static SessionFactory createSession() {
		try {
			String urlBDD = "";

			// String osName = System.getProperty("os.name");
			// if (osName.startsWith("Mac OS")) {
			// urlBDD = "jdbc:derby:../../Database/GPEC_TEST";
			// } else if (osName.startsWith("Linux")) {
			// urlBDD = "jdbc:derby:Database/GPEC_TEST";
			// } else if (osName.startsWith("Windows")) {

			urlBDD = getDatabaseUrl();

			// }

			Properties properties = new Properties();
			InputStream inputStream = HibernateUtil.class.getClassLoader()
					.getResourceAsStream(DATABASE_PROPERTIES_FILE);
			if (inputStream == null) {
				throw new FileNotFoundException("property file '"
						+ DATABASE_PROPERTIES_FILE
						+ "' not found in the classpath");
			}
			properties.load(inputStream);

			sessionFactory = new Configuration()
					.configure()
					.setProperty("hibernate.connection.url", urlBDD)
					.setProperty(
							"hibernate.connection.username",
							properties
									.getProperty("hibernate.connection.username"))
					.setProperty(
							"hibernate.connection.password",
							properties
									.getProperty("hibernate.connection.password"))
					.buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed " + ex);
			throw new ExceptionInInitializerError(ex);
		}
		return sessionFactory;
	}

	public static final ThreadLocal session = new ThreadLocal();

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null ){
			createSession();
		}
		return sessionFactory;
	}

	public static SessionFactory getSessionFactory(String path) {
		PATH_DATABASE = new String();
		PATH_DATABASE = path;
		if (sessionFactory == null)
			return createSession();
		else
			return sessionFactory;
	}

	public static String getPATH_DATABASE() {
		return PATH_DATABASE;
	}

	public static void setPATH_DATABASE(String path) {
		PATH_DATABASE = new String();
		PATH_DATABASE = path;
	}

	public static String getDatabaseUrlReprise() throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = HibernateUtil.class.getClassLoader()
				.getResourceAsStream(DATABASE_PROPERTIES_FILE);
		if (inputStream == null) {
			throw new FileNotFoundException("property file '"
					+ DATABASE_PROPERTIES_FILE + "' not found in the classpath");
		}
		properties.load(inputStream);

		String urlBDDReprise = "";

		String osName = System.getProperty("os.name");
		if (osName.startsWith("Mac OS")) {
			urlBDDReprise = properties
					.getProperty(DATABASE_CONNECTION_URL_MAC_REPRISE);
		} else if (osName.startsWith("Linux")) {
			urlBDDReprise = properties
					.getProperty(DATABASE_CONNECTION_URL_LINUX_REPRISE);
		} else if (osName.startsWith("Windows")) {
			urlBDDReprise = properties
					.getProperty(DATABASE_CONNECTION_URL_WINDOWS_REPRISE);
		}
		return urlBDDReprise;
	}

	public static String getDatabaseUrl() throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = HibernateUtil.class.getClassLoader()
				.getResourceAsStream(DATABASE_PROPERTIES_FILE);
		if (inputStream == null) {
			throw new FileNotFoundException("property file '"
					+ DATABASE_PROPERTIES_FILE + "' not found in the classpath");
		}
		properties.load(inputStream);

		String urlBDD = "";
		String embedded = "";

		urlBDD = properties.getProperty(DATABASE_CONNECTION_URL);
		embedded = properties.getProperty(DATABASE_EMBEDDED);

		if (embedded.equals("false"))

			return urlBDD;

		else
			return urlBDD + "?server.basedir=" + System.getenv("databasedir")
					+ "&server.datadir=" + System.getenv("datadir")
					+ "&createDatabaseIfNotExist=true";

	}
}