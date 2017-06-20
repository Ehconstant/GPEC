package com.cci.gpec.web.servlet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {

	static final long serialVersionUID = 1L;
	private static final int BUFSIZE = 4096;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String fichier = (String) request.getSession().getAttribute("fichier");
		File file = new File(fichier);
		int length = 0;
		ServletOutputStream outStream = response.getOutputStream();
		ServletContext context = getServletConfig().getServletContext();
		String mimetype = context.getMimeType(fichier);

		// sets response content type
		if (mimetype == null) {
			mimetype = "application/octet-stream";
		}
		response.setContentType(mimetype);
		response.setContentLength((int) file.length());
		String fileName = (new File(fichier)).getName();

		// sets HTTP header
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");

		byte[] byteBuffer = new byte[BUFSIZE];
		DataInputStream in = new DataInputStream(new FileInputStream(file));

		// reads the file's bytes and writes them to the response stream
		while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
			outStream.write(byteBuffer, 0, length);
		}

		in.close();
		outStream.close();
	}
}
