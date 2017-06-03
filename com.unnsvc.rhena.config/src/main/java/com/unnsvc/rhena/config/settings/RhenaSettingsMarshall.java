
package com.unnsvc.rhena.config.settings;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.config.RhenaConfiguration;

public class RhenaSettingsMarshall {

	public static IRhenaConfiguration loadSettings(URI location) throws RhenaException {

		IRhenaConfiguration config = new RhenaConfiguration();

		try {
			URL locationUrl = location.toURL();
			return loadSettings_(config, locationUrl);
		} catch (MalformedURLException e) {

			throw new RhenaException(e);
		}
	}

	public static IRhenaConfiguration loadSettings(URL location) throws RhenaException {

		IRhenaConfiguration config = new RhenaConfiguration();
		return loadSettings_(config, location);
	}

	public static IRhenaConfiguration loadSettings_(IRhenaConfiguration config, URL settingsXml) throws RhenaException {

		try {
			URLConnection conn = settingsXml.openConnection();

			InputStream is = conn.getInputStream();
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			DocumentBuilder builder = fact.newDocumentBuilder();
			Document document = builder.parse(is);

			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(RhenaConstants.class.getClassLoader().getResource("META-INF/schema/settings.xsd"));
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document));
			
			// Now construct a configuration

			return config;
		} catch (Exception ex) {

			throw new RhenaException(ex);
		}
	}
}
