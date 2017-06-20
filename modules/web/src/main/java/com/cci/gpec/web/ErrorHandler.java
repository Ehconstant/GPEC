package com.cci.gpec.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Extend HttpServlet class
public class ErrorHandler extends HttpServlet {

	// Method to handle GET method request.
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Analyze the servlet exception
		Throwable throwable = (Throwable) request
				.getAttribute("javax.servlet.error.exception");
		Integer statusCode = (Integer) request
				.getAttribute("javax.servlet.error.status_code");
		String servletName = (String) request
				.getAttribute("javax.servlet.error.servlet_name");
		if (servletName == null) {
			servletName = "Inconnu";
		}
		String requestUri = (String) request
				.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Inconnu";
		}

		// Set response content type
		response.setContentType("text/html");

		String content = "";

		PrintWriter out = response.getWriter();
		String title = "Erreur";

		String docType = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		out.println(docType + "<html>\n" + "<head><title>" + title
				+ "</title></head>\n"
				+ "<body style=\"background-color: #f5f5f5;\">\n");

		String docTypeMail = "<!doctype html public \"-//w3c//dtd html 4.0 "
				+ "transitional//en\">\n";
		content += docTypeMail + "<html>\n" + "<head><title>" + title
				+ "</title></head>\n" + "<body bgcolor=\"#f0f0f0\">\n";

		out.println("<br/><br/><div align=\"center\"><img src=\"/app/img/picto-attention.png\" width=\"100\" height=\"100\" />");

		out.println("<h4>Une erreur s'est produite</h4><br><br>");

		out.println("Veuillez signaler ce problème à votre conseillère CCI en précisant bien l'action ayant provoqué cette erreur.<br><br>");

		out.println("<p align=\"center\"><a href=\"mailto:" + getEmail()
				+ "\">Envoyer un mail</a></p>");

		if (throwable == null && statusCode == null) {
			content += "<h4>Aucune information sur l'erreur</h4>";
		} else if (statusCode != null) {
			// out.println("Type d'erreur : " + statusCode);
			content += "Type d'erreur : " + statusCode;
			if (throwable != null) {
				// out.println("<h4>Informations sur l'erreur :</h4>");
				content += "<h4>Informations sur l'erreur :</h4>";
				// out.println("Nom de servlet : " + servletName +
				// "</br></br>");
				content += "Nom de servlet : " + servletName + "\n";
				// out.println("Type d'exception : "
				// + throwable.getClass().getName() + "</br></br>");
				content += "Type d'exception : "
						+ throwable.getClass().getName() + "\n";
				// out.println("URI de la requête : " + requestUri +
				// "<br><br>");
				content += "URI de la requête : " + requestUri + "\n";
				content += "The exception message: " + throwable.getMessage();
			}
		} else {
			// out.println("<h4>Informations sur l'erreur :</h4>");
			content += "<h4>Informations sur l'erreur :</h4>";
			// out.println("Nom de servlet : " + servletName + "</br></br>");
			content += "Nom de servlet : " + servletName + "\n";
			// out.println("Type d'exception : " +
			// throwable.getClass().getName()
			// + "</br></br>");
			content += "Type d'exception : " + throwable.getClass().getName()
					+ "\n";
			// out.println("URI de la requête : " + requestUri + "<br><br>");
			content += "URI de la requête : " + requestUri + "\n";
			content += "The exception message: " + throwable.getMessage();
		}

		out.println("<br><INPUT Type='button' VALUE='Retour' onclick=\"window.location='/app/gpec/salaries.xhtml';\" /></div>");
		// out.println("</body>");
		content += "</body>";
		// out.println("</html>");
		content += "</html>";

		String subject = "GPEC - ERREUR";
		Properties mailSettings;
		mailSettings = Utils
				.getEmailPropertiesFromClasspath("conf/emailSettings.properties");
		String mail = mailSettings.getProperty("mail.contact");
		// try {
		// Utils.sendHtmlEmail(mail, subject, content);
		// } catch (EmailException e) {
		// e.printStackTrace();
		// }

	}

	// Method to handle POST method request.
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private String getEmail() {
		Properties properties = new Properties();
		InputStream inputStream = Utils.class.getClassLoader()
				.getResourceAsStream(Utils.CCI_PROPERTIES_FILE);
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty("emailSupport");
	}
}