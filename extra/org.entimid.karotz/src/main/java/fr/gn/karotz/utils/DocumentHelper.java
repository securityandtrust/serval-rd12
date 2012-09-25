/*
 * Copyright (c) 2011. Gregory Nain.
 */
package fr.gn.karotz.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;


/**
 * Utility class for XML Documents
  * User: Gregory NAIN
 *
 */
public abstract class DocumentHelper {

	/**
	 * Convert a document node into a new Document which root node is the original node.
	 * @param n The node to be converted into full document.
	 * @return An instance of Document, which root is the node passed in parameter.
	 */
	public static Document asDocument(Node n) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document newDoc = null;
		try {

			db = dbf.newDocumentBuilder();
			newDoc = db.newDocument();
			newDoc.appendChild(newDoc.importNode(n, true));

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return newDoc;
	}

	
	/**
	 * Convert a document into a formated readable string.
	 * @param document The document to convert into string.
	 * @return A String representation of the document.
	 */
	public static String convertToString(Document document) {

		try {

			DOMSource domSource = new DOMSource(document);
			StringWriter sw = new StringWriter();
			Result result = new StreamResult(sw);

			// create an instance of TransformerFactory
			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer trans;
			trans = transFact.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.transform(domSource, result);
			return sw.toString();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
