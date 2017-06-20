package com.cci.gpec.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.cci.gpec.db.connection.HibernateUtil;

public class MysqlToXls {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MysqlToXls.class);

	public MysqlToXls() throws ClassNotFoundException, SQLException,
			IOException {
	}

	public void generateXls(String filename, String query) throws SQLException,
			FileNotFoundException, IOException {
		// Create new Excel workbook and sheet
		HSSFWorkbook xlsWorkbook = new HSSFWorkbook();
		HSSFSheet xlsSheet = xlsWorkbook.createSheet();
		short rowIndex = 0;

		
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();

			transaction = session.beginTransaction();
			// Execute SQL query
			PreparedStatement stmt = session.connection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
	
			// Get the list of column names and store them as the first
			// row of the spreadsheet.
			ResultSetMetaData colInfo = rs.getMetaData();
			List<String> colNames = new ArrayList<String>();
			HSSFRow titleRow = xlsSheet.createRow(rowIndex++);
	
			for (int i = 1; i <= colInfo.getColumnCount(); i++) {
				colNames.add(colInfo.getColumnName(i));
				titleRow.createCell((short) (i - 1)).setCellValue(
						new HSSFRichTextString(colInfo.getColumnName(i)));
				xlsSheet.setColumnWidth((short) (i - 1), (short) 4000);
			}
	
			// Save all the data from the database table rows
			while (rs.next()) {
				HSSFRow dataRow = xlsSheet.createRow(rowIndex++);
				short colIndex = 0;
				for (String colName : colNames) {
					dataRow.createCell(colIndex++).setCellValue(
							new HSSFRichTextString(rs.getString(colName)));
				}
			}
	
			// Write to disk
			FileOutputStream fos = new FileOutputStream(filename);
			xlsWorkbook.write(fos);
			transaction.commit();
			stmt.close();
			fos.flush();
			fos.close();
		} catch (HibernateException e) {
			if (transaction != null && !transaction.wasRolledBack()) {
				transaction.rollback();
			}
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}
	
	public StringBuilder generateCsv(StringBuilder sb, Document document, String query,
			String tableName) throws SQLException {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		
		boolean hasTransaction = session.getTransaction().isActive();
		if (!hasTransaction) {
			session.beginTransaction();
		}
		
		try {
			// Execute SQL query
			session.connection().nativeSQL("SET NAMES utf8");
			
			PreparedStatement stmt = session.connection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			// Get the list of column names and store them
			ResultSetMetaData colInfo = rs.getMetaData();
			List<String> colNames = new ArrayList<String>();
	
			for (int i = 1; i <= colInfo.getColumnCount(); i++) {
				colNames.add(colInfo.getColumnName(i));
			}
	
			int i = 0;
			sb.append(tableName);
			sb.append(';');
			for (String colName : colNames) {
				sb.append(colName);
				if (i+1 < colNames.size()) {
					sb.append(';');
				} else {
					sb.append('\n');
				}
				i++;
			}
			// Save all the data from the database table rows
			while (rs.next()) {
				i = 0;
				sb.append(tableName);
				sb.append(';');
				for (String colName : colNames) {
			        sb.append(rs.getString(colName) == null ? ""
							: rs.getString(colName));
			        if (i+1 < colNames.size()) {
			        	sb.append(';');
			        } else {
			        	sb.append('\n');
			        }
			        i++;
				}
			}
			
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
        
        return sb;
	}
	
	public void writeCsv(StringBuilder sb, File csv) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(csv);
        pw.write(sb.toString());
        pw.close();
	}

	public Document generateXml(Document document, String query,
			String tableName) throws SQLException, FileNotFoundException,
			IOException, ParserConfigurationException, SAXException,
			TransformerException {

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		
		boolean hasTransaction = session.getTransaction().isActive();
		if (!hasTransaction) {
			session.beginTransaction();
		}
		
		try {
			// Execute SQL query
			session.connection().nativeSQL("SET NAMES utf8");
			
			PreparedStatement stmt = session.connection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			// Get the list of column names and store them
			ResultSetMetaData colInfo = rs.getMetaData();
			List<String> colNames = new ArrayList<String>();
	
			for (int i = 1; i <= colInfo.getColumnCount(); i++) {
				colNames.add(colInfo.getColumnName(i));
			}
	
			// Save all the data from the database table rows
			while (rs.next()) {
				Element table = document.createElement(tableName);
				for (String colName : colNames) {
					if (colName.equalsIgnoreCase("justificatif")) {
						if (rs.getString(colName) != null && !rs.getString(colName).equals("")) {
							String nameTmp = rs.getString(colName).replaceAll("/", "|").replaceAll("\\\\", "|");

							if (nameTmp.lastIndexOf("|") != -1) {
								// on ne garde que le nom du fichier

								table.setAttribute(colName, nameTmp.substring(nameTmp.lastIndexOf("|") + 1,
										nameTmp.length()));
								
							}
						} else {
							table.setAttribute(colName, "");
						}
					} else {
						table.setAttribute(colName, rs.getString(colName) == null ? ""
								: rs.getString(colName));
					}
				}
				document.getFirstChild().appendChild(table);
			}
			
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			
			LOGGER.error("Hibernate Session Error", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return document;

	}
}