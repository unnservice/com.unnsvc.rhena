
package com.unnsvc.rhena.config.settings;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.utils.DocumentHelper;
import com.unnsvc.rhena.config.RhenaConfiguration;

public class ConfigSerialisationHelper {

	public static IRhenaConfiguration parseConfig(URI location) throws URISyntaxException, IOException, ParserConfigurationException, SAXException {

		URL locationURL = location.toURL();
		return parseConfig(locationURL);
	}

	public static IRhenaConfiguration parseConfig(URL location) throws URISyntaxException, IOException, ParserConfigurationException, SAXException {

		IRhenaConfiguration config = new RhenaConfiguration();

		URLConnection conn = location.openConnection();
		InputStream is = conn.getInputStream();
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		fact.setNamespaceAware(true);
		DocumentBuilder builder = fact.newDocumentBuilder();
		Document document = builder.parse(is);

		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(RhenaConstants.class.getClassLoader().getResource("META-INF/schema/settings.xsd"));
		Validator validator = schema.newValidator();
		validator.validate(new DOMSource(document));

		for(Node child : DocumentHelper.childNodes(document)) {
			
			
		}

		return config;
	}
}
