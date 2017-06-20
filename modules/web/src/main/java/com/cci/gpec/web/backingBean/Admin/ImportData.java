package com.cci.gpec.web.backingBean.Admin;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cci.gpec.db.connection.HibernateUtil;

public class ImportData {
	// ATTRIBUT
	private Map<String, Map<String, Integer>> mappingPK;
	private File xml;
	private boolean ignoreReferenceTable;
	
	// CONSTRUCTOR
	public ImportData(File xml) {
		super();
		ignoreReferenceTable = true;
		this.xml = xml;
		mappingPK = new HashMap<>();
	}

	// GET / SET
	public File getXml() {
		return xml;
	}

	public void setXml(File xml) {
		this.xml = xml;
	}
	
	public boolean isIgnoreReferenceTable() {
		return ignoreReferenceTable;
	}

	public void setIgnoreReferenceTable(boolean ignoreReferenceTable) {
		this.ignoreReferenceTable = ignoreReferenceTable;
	}

	// COMMAND
	public void importXML() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xml);
		
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		try {
			List<DbElementType> elts = DbElementType.getElement(!ignoreReferenceTable);
			
			for (DbElementType e : elts) {
				NodeList children = doc.getDocumentElement().getElementsByTagName(e.getTableName());
				
				for (int nbChild = 0; nbChild < children.getLength(); nbChild++) {
					Node node = children.item(nbChild);
					saveElement(node, session, e);
				}
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		}
	}
	
	// PRIVATE
	private void saveElement(Node node, Session session, DbElementType typeElement) {
		String qName = node.getNodeName();
		NamedNodeMap attributes = node.getAttributes();
		
		if ("root".equalsIgnoreCase(qName)) {
			return;
		}
		
		int nbAttr = attributes.getLength();
		Map<String, String> colValue =  new HashMap<String, String>();
		String originalAIid = "";
		 
		for (int i = 0; i < nbAttr; i++) {
			String attributeName = attributes.item(i).getNodeName();
			
			// cas particulier pour la table fichemetierentreprise car la pk est une fk
			if (typeElement.getAutoIncrementField().equalsIgnoreCase(attributeName) 
					&& !typeElement.getTableName().equalsIgnoreCase(DbElementType.ENTERPRISE_POSITION_DESCRIPTION.getTableName())) {
				originalAIid = attributes.item(i).getNodeValue();
				continue; // IgnoredFile
			}
			
			// cas particulier ou le nom de la fk est différent de la pk qu'il référence
			String attributeNameTmp;
			if (attributeName.equalsIgnoreCase("ID_METIER_TYPE")) {
				attributeNameTmp = DbElementType.POSITION_DESCRIPTION.getAutoIncrementField();
			} else {
				attributeNameTmp = attributeName;
			}
			if (mappingPK.containsKey(attributeNameTmp.toLowerCase())) {
				Map<String, Integer> idMap = mappingPK.get(attributeNameTmp.toLowerCase());
				Integer id = idMap.get(attributes.item(i).getNodeValue());
				colValue.put(attributeName, id == null ? null : id.toString());
			} else if (StringUtils.isEmpty(attributes.item(i).getNodeValue())) {
				colValue.put(attributeName, null);
			} else {
				colValue.put(attributeName, attributes.item(i).getNodeValue());
			}
		}
		
		insertNewElement(session, colValue, qName);
		
		// Add id
		saveLastId(session, typeElement, originalAIid);
	}
	
	private void insertNewElement(Session session, Map<String, String> colValue, String qName) {
		StringBuilder parameters = new StringBuilder();
		StringBuilder colName = new StringBuilder();
		String prefix = "";
		
		for (String set : colValue.keySet()) {
			parameters.append(prefix).append(":").append(set);
			colName.append(prefix).append(set);
			prefix = ",";
		}
		
		String sql = "INSERT INTO " + qName + "(" + colName + ") VALUES (" + parameters + ")";
		Query q = session.createSQLQuery(sql);
		
		for (Map.Entry<String, String> entry : colValue.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}
		
		q.executeUpdate();
	}
	
	private void saveLastId(Session session, DbElementType typeElement, String originalAIid) {
		BigInteger lastId = (BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult();
		Map<String, Integer> idMap = mappingPK.get(typeElement.getAutoIncrementField());
		
		if (idMap == null) {
			idMap = new HashMap<>();
			mappingPK.put(typeElement.getAutoIncrementField().toLowerCase(), idMap);
		}
		
		idMap.put(originalAIid, lastId.intValue());
	}
}
