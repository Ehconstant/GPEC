package com.cci.gpec.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.cci.gpec.web.backingBean.Admin.AdminFormBB;

public class ThreadMailSender extends Thread {

	private String userEmail;

	private String subject;

	private String content;

	public ThreadMailSender(String userEmail, String subject, String content) {

		this.userEmail = userEmail;

		this.subject = subject;

		this.content = content;

	}

	public Properties getEmailPropertiesFromClasspath(String propertiesFileName)
			throws IOException {
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

	public void run() {

		HtmlEmail email = new HtmlEmail();

		Properties mailSettings;
		try {
			mailSettings = getEmailPropertiesFromClasspath("conf/emailSettings.properties");
			email.setHostName(mailSettings.getProperty("mail.host"));
			if ("true".equalsIgnoreCase(mailSettings.getProperty("mail.auth"))) {
				email.setAuthentication(
						mailSettings.getProperty("mail.username"),
						mailSettings.getProperty("mail.password"));
			}
			email.setSmtpPort(Integer.valueOf(mailSettings
					.getProperty("mail.smtp.port")));
			email.setCharset("UTF-8");
			email.setSSL("true".equalsIgnoreCase(mailSettings
					.getProperty("mail.ssl")));
			email.setFrom(mailSettings.getProperty("mail.from.email"),
					mailSettings.getProperty("mail.from.name"));
			if (userEmail.contains(",")) {
				for (String mail : userEmail.split(",")) {
					email.addTo(mail);
				}
			} else {
				email.addTo(userEmail);
			}
			email.setSubject(subject);
			email.setHtmlMsg(content);
			email.addHeader("MIME-Version", "1.0");
			email.setDebug(false);
			email.send();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EmailException e) {
			e.printStackTrace();
		}

	}

}