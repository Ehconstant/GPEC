package com.cci.gpec.web.servlet;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.HibernateException;

import com.cci.gpec.db.connection.HibernateUtil;
import com.cci.gpec.web.Utils;

public class InitializeConnection extends GenericServlet {

	private static final long serialVersionUID = 1L;

	private static Process p;

	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		try {

			File f = new File(new File(config.getServletContext().getRealPath(
					"/")).getParent());
			String applicationPath = new File(f.getParent()).getParent();

			HibernateUtil.getSessionFactory(applicationPath)
					.getCurrentSession();

			String osName = System.getProperty("os.name");
			if (osName.startsWith("Mac OS")) {
				runBrowserUnderMac();
			} else if (osName.startsWith("Windows")) {
				// InsertSalarieBean insert = new InsertSalarieBean();
				// insert.insertSalarieBean();
				runBrowser();
			}

		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	// open -a Safari

	public void runBrowserUnderMac() {
		try {
			String[] command = { "open", "-a", "safari.app",
					"http://localhost:" + Utils.getPort() + "/gpec/" };

			Runtime r = Runtime.getRuntime();
			p = r.exec(command);
			p.waitFor();
			int myStatus = -1;
			boolean ready = false;
			// --- Wait the end of the execution

			while (!ready) {
				try {
					myStatus = p.exitValue();
					ready = true;
				} catch (IllegalThreadStateException e) {
					// myOut.read();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void runBrowser() {

		try {
			String[] command = {
					"cmd.exe",
					"/C",
					"Start",
					"http://" + InetAddress.getLocalHost().getHostName() + ":"
							+ Utils.getPort() + "/gpec/" };

			Runtime r = Runtime.getRuntime();
			p = r.exec(command);
			p.waitFor();
			int myStatus = -1;
			boolean ready = false;
			// --- Wait the end of the execution

			while (!ready) {
				try {
					myStatus = p.exitValue();
					ready = true;
				} catch (IllegalThreadStateException e) {
					// myOut.read();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Process getP() {
		return p;
	}

}
