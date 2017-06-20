package com.cci.gpec.web.backingBean.exit;

import java.io.IOException;
import java.sql.SQLException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class ExitBean {
	String disconnect;

	public String goToExitPage() throws SQLException {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		session.setAttribute("logged", "no");
		session.setAttribute("level", null);
		session.invalidate();

		return "exit";
	}

	public String goToHomePage() {
		return "home";
	}

	public String getDisconnect() {

		// disconnectApplication();

		return disconnect;
	}

	public void disconnectApplication() {

		try {
			String osName = System.getProperty("os.name");
			if (osName.startsWith("Mac OS")) {
				String[] command = { "/bin/sh", "-c", "./shutdown.sh" };
				executeCmd(command);
			} else if (osName.startsWith("Windows")) {
				String[] command = { "cmd.exe", "/C", "Start /B /MIN ",
						".\\apache-tomcat-5.5.29\\bin\\shutdown.bat" };
				executeCmd(command);
			} else {
				String[] command = { "sh ",
						"./apache-tomcat-5.5.29/bin/shutdown.sh" };
				executeCmd(command);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void executeCmd(String[] command) throws IOException,
			InterruptedException {
		Runtime r = Runtime.getRuntime();
		Process p = r.exec(command);
		p.waitFor();

		int myStatus = -1;
		boolean ready = false;

		while (!ready) {
			try {
				myStatus = p.exitValue();
				ready = true;
			} catch (IllegalThreadStateException e) {
			}
		}
	}

}
