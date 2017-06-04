
package com.unnsvc.rhena.config.settings;

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
import com.unnsvc.rhena.common.config.IAgentConfiguration;
import com.unnsvc.rhena.common.config.IBuildConfiguration;
import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.repository.ERepositoryType;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;
import com.unnsvc.rhena.common.utils.DocumentHelper;
import com.unnsvc.rhena.config.RepositoryDefinition;
import com.unnsvc.rhena.config.RhenaConfiguration;

public class ConfigSerialisationHelper {

	private ConfigSerialisationHelper() {

	}

	public static IRhenaConfiguration parseConfig(URI location) throws RhenaException {

		try {
			URL locationURL = location.toURL();
			return parseConfig(locationURL);
		} catch (MalformedURLException ex) {
			throw new RhenaException(ex);
		}
	}

	public static IRhenaConfiguration parseConfig(URL location) throws RhenaException {

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

	public static interface SerialisationWriter {

		public void write(int indent, String line) throws IOException;
	}

	public static void serialiseConfig(IRhenaConfiguration config, SerialisationWriter writer) throws IOException {

		int indent = 0;

		writer.write(indent, "<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		writer.write(indent,
				"<settings xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:rhena:settings\" xsi:schemaLocation=\"urn:rhena:settings http://schema.unnsvc.com/rhena/settings.xsd\">");

		serialiseRepoConfig(indent + 1, config.getRepositoryConfiguration(), writer);
		serialiseAgentConfig(indent + 1, config.getAgentConfiguration(), writer);
		serialiseBuildConfig(indent + 1, config.getBuildConfiguration(), writer);

		writer.write(indent, "</settings>");
	}

	private static void serialiseBuildConfig(int i, IBuildConfiguration buildConfiguration, SerialisationWriter writer) throws IOException {

		writer.write(i, "<build>");
		writer.write(i, "</build>");
	}

	private static void serialiseRepoConfig(int i, IRepositoryConfiguration repositoryConfiguration, SerialisationWriter writer) throws IOException {

		writer.write(i, "<repositories>");
		writer.write(i + 1, "<workspace>");
		for (IRepositoryDefinition def : repositoryConfiguration.getWorkspaceRepositories()) {
			serialiseRepoDefConfig(i + 2, def, writer);
		}
		writer.write(i + 1, "</workspace>");
		writer.write(i + 1, "<cache>");
		for (IRepositoryDefinition def : repositoryConfiguration.getCacheRepositories()) {
			serialiseRepoDefConfig(i + 2, def, writer);
		}
		writer.write(i + 1, "</cache>");
		writer.write(i + 1, "<remote>");
		for (IRepositoryDefinition def : repositoryConfiguration.getRemoteRepositories()) {
			serialiseRepoDefConfig(i + 2, def, writer);
		}
		writer.write(i + 1, "</remote>");
		writer.write(i, "</repositories>");
	}

	private static void serialiseRepoDefConfig(int i, IRepositoryDefinition def, SerialisationWriter writer) throws IOException {

		writer.write(i, "<repository location=\"" + def.getLocation().toString() + "\" />");
	}

	private static void serialiseAgentConfig(int i, IAgentConfiguration agentConfiguration, SerialisationWriter writer) throws IOException {

		writer.write(i, "<agent>");
		writer.write(i, "</agent>");
	}

	public static String indents(int indents) {

		char[] indent = new char[indents];
		for (int i = 0; i < indents; i++) {

			indent[i] = '\t';
		}
		return new String(indent);
	}
}
