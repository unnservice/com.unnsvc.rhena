package com.unnsvc.rhena.config.userconf;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
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
import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.repository.ERepositoryType;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;
import com.unnsvc.rhena.common.utils.DocumentHelper;
import com.unnsvc.rhena.config.RepositoryDefinition;
import com.unnsvc.rhena.config.RhenaConfiguration;

public class SettingsParser {

	public static IRhenaConfiguration parseSettings(URI location) throws RhenaException {

		try {
			URL locationURL = location.toURL();
			return parseSettings(locationURL);
		} catch (MalformedURLException ex) {
			throw new RhenaException(ex);
		}
	}

	public static IRhenaConfiguration parseSettings(URL location) throws RhenaException {

		try {
			IRhenaConfiguration config = new RhenaConfiguration();

			URLConnection conn = location.openConnection();
			InputStream is = conn.getInputStream();
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			DocumentBuilder builder = fact.newDocumentBuilder();
			Document document = builder.parse(is);

			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(RhenaConstants.class.getClassLoader().getResource("META-INF/schema/settings.xsd"));

			try {
				Validator validator = schema.newValidator();
				validator.validate(new DOMSource(document));
			} catch (SAXException ex) {

				throw new RhenaException("Failed to validate document: " + ex.getMessage(), ex);
			}

			for (Node child : DocumentHelper.childNodes(document)) {

				if (child.getNodeName().equals("settings")) {
					parseSettings(config, child);
				}
			}

			return config;
		} catch (IOException | ParserConfigurationException | SAXException ex) {

			throw new RhenaException(ex);
		}
	}

	private static void parseSettings(IRhenaConfiguration config, Node settingsNode) {

		for (Node child : DocumentHelper.childNodes(settingsNode)) {

			if (child.getNodeName().equals("repositories")) {

				parseRepositories(config.getRepositoryConfiguration(), child);
			}
		}
	}

	private static void parseRepositories(IRepositoryConfiguration repoConfig, Node repositoriesNode) {

		for (Node child : DocumentHelper.childNodes(repositoriesNode)) {

			if (child.getNodeName().equals("workspace")) {

				parseRepository(ERepositoryType.WORKSPACE, repoConfig, child);
			} else if (child.getNodeName().equals("cache")) {

				parseRepository(ERepositoryType.CACHE, repoConfig, child);
			} else if (child.getNodeName().equals("remote")) {

				parseRepository(ERepositoryType.REMOTE, repoConfig, child);
			}
		}
	}

	private static void parseRepository(ERepositoryType type, IRepositoryConfiguration repoConfig, Node repositoryNode) {

		for (Node child : DocumentHelper.childNodes(repositoryNode)) {

			String location = child.getAttributes().getNamedItem("location").getNodeValue();
			URI locationURI = URI.create(location);

			RepositoryIdentifier identifier = new RepositoryIdentifier(location);
			RepositoryDefinition def = new RepositoryDefinition(type, identifier, locationURI);

			switch (type) {
				case CACHE:
					repoConfig.getCacheRepositories().add(def);
					break;
				case REMOTE:
					repoConfig.getRemoteRepositories().add(def);
					break;
				case WORKSPACE:
					repoConfig.getWorkspaceRepositories().add(def);
					break;
			}
		}
	}
}
