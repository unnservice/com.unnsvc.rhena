
package com.unnsvc.rhena.core.config;

import java.io.File;
import java.net.URI;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class RhenaSettingsParser {

	public RhenaSettingsParser() throws RhenaException {

	}

	public void loadUserSettings(IRhenaConfiguration config) throws RhenaException {

		File homeDir = new File(System.getProperty("user.home"));
		File rhenaHome = new File(homeDir, ".rhena");
		File rhenaSettingsFile = new File(rhenaHome, "settings.xml");

		loadSettings(config, rhenaSettingsFile);
	}

	public void loadSettings(IRhenaConfiguration config, File settingsFile) throws RhenaException {

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

						String location = repoNode.getAttributes().getNamedItem("location").getNodeValue();
						config.getRepositoryConfiguration().addRepository(new RepositoryDefinition(new URI(location)));
					}
				} else if (child.getLocalName().equals("workspaces")) {

					for (Node workNode : Utils.getNodeChildren(child)) {

						String location = workNode.getAttributes().getNamedItem("location").getNodeValue();
						config.getRepositoryConfiguration().addWorkspace(new RepositoryDefinition(new URI(location)));
					}
				}
			}
		} catch (Exception ex) {

			throw new RhenaException(ex.getMessage(), ex);
		}
	}
}
