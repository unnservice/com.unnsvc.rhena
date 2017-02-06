
package com.unnsvc.rhena.core.settings;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.settings.IRhenaSettings;

public class RhenaSettingsParser implements IRhenaSettings {

	public RhenaSettingsParser() throws RhenaException {

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

				}
			}
		}
	}
}
