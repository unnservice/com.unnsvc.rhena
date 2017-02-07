
package com.unnsvc.rhena.core.settings;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class RhenaSettingsParser extends RhenaSettings {

	public RhenaSettingsParser() throws RhenaException {

		File homeDir = new File(System.getProperty("user.home"));
		File rhenaHome = new File(homeDir, ".rhena");
		File rhenaSettingsFile = new File(rhenaHome, "settings.xml");
		try {
			parseSettings(rhenaSettingsFile);
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}
	}

	public RhenaSettingsParser(File rhenaSettingsFile) throws RhenaException {

		try {
			parseSettings(rhenaSettingsFile);
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}
	}

	private void parseSettings(File settingsFile) throws SAXException, IOException, ParserConfigurationException, DOMException, URISyntaxException {

		try {

			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);

			DocumentBuilder builder = fact.newDocumentBuilder();
			Document document = builder.parse(settingsFile);
			
			// validate
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(getClass().getClassLoader().getResource("META-INF/schema/settings.xsd"));

			try {
				Validator validator = schema.newValidator();
				validator.validate(new DOMSource(document));
			} catch (Exception ex) {
				throw new RhenaException("Schema validation error for: " + settingsFile.toString(), ex);
			}

			Node settingsNode = Utils.getChildNode(document, "settings");

			for (Node child : Utils.getNodeChildren(settingsNode)) {

				if (child.getLocalName().equals("repositories")) {

					for (Node repoNode : Utils.getNodeChildren(child)) {

						String repositoryName = repoNode.getAttributes().getNamedItem("name").getNodeValue();
						String location = repoNode.getAttributes().getNamedItem("location").getNodeValue();
						getRepositories().add(new RepositoryDefinition(repositoryName, new URI(location)));
					}
				} else if (child.getLocalName().equals("workspaces")) {

					for (Node workNode : Utils.getNodeChildren(child)) {

						String repositoryName = workNode.getAttributes().getNamedItem("name").getNodeValue();
						String location = workNode.getAttributes().getNamedItem("location").getNodeValue();
						getWorkspaces().add(new RepositoryDefinition(repositoryName, new URI(location)));
					}
				}
			}
		} catch (Exception ex) {

			throw new RhenaException(ex.getMessage(), ex);
		}
	}
}
