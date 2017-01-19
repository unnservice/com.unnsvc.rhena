
package com.unnsvc.rhena.core.config;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.config.IRhenaSettings;
import com.unnsvc.rhena.common.config.RepositoryDefinition;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class RhenaSettingsParser implements IRhenaSettings {

	private static final long serialVersionUID = 1L;
	private List<RepositoryDefinition> repositoryDefinitions;

	public RhenaSettingsParser() throws RhenaException {

		this.repositoryDefinitions = new ArrayList<RepositoryDefinition>();
		try {
			File homeDir = new File(System.getProperty("user.home"));
			File rhenaHome = new File(homeDir, ".rhena");
			File settingsFile = new File(rhenaHome, "settings.xml");
			if (settingsFile.exists()) {
				parseSettings(settingsFile);
			}
		} catch (Exception ex) {

			throw new RhenaException(ex.getMessage(), ex);
		}
	}

	private void parseSettings(File settingsFile) throws SAXException, IOException, ParserConfigurationException, DOMException, URISyntaxException {

		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		fact.setNamespaceAware(true);

		DocumentBuilder builder = fact.newDocumentBuilder();
		Document document = builder.parse(settingsFile);
		Node settingsNode = Utils.getChildNode(document, "settings");
		Node repositoriesNode = Utils.getChildNode(settingsNode, "repositories");

		NodeList childNodes = repositoriesNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {

			Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNamespaceURI().equals(RhenaConstants.NS_RHENA_SETTINGS_REPOSITORY)) {

					String type = node.getLocalName();
					String name = node.getAttributes().getNamedItem("name").getNodeValue();
					URI uri = new URI(node.getAttributes().getNamedItem("url").getNodeValue());
					RepositoryDefinition repoDef = new RepositoryDefinition(name, uri, type);
					repositoryDefinitions.add(repoDef);
				}
			}
		}
	}

	@Override
	public List<RepositoryDefinition> getRepositoryDefinitions() {

		return repositoryDefinitions;
	}
}
