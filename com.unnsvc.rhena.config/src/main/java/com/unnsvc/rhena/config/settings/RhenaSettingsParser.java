
package com.unnsvc.rhena.config.settings;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.repository.ERepositoryType;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;
import com.unnsvc.rhena.config.RepositoryDefinition;

public class RhenaSettingsParser extends DefaultHandler {

	private IRepositoryConfiguration repoConfig;

	public RhenaSettingsParser(IRepositoryConfiguration repoConfig) {

		this.repoConfig = repoConfig;
	}

	public void parseSettings(File location) throws RhenaException {

		try {
			SAXParserFactory fact = SAXParserFactory.newInstance();
			fact.setNamespaceAware(true);
			SAXParser parser = fact.newSAXParser();
			parser.parse(location, this);

			/**
			 * @TODO schema validation
			 */

		} catch (ParserConfigurationException | SAXException | IOException e) {

			throw new RhenaException(e);
		}
	}

	private boolean workspace = false;
	private boolean cache = false;
	private boolean remote = false;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (localName.equals("workspace")) {
			workspace = true;
			return;
		} else if (localName.equals("cache")) {
			cache = true;
			return;
		} else if (localName.equals("remote")) {
			remote = true;
			return;
		}

		try {
			if (localName.equals("repository")) {
				String locationStr = attributes.getValue("location");
				URI location = new URI(locationStr);

				RepositoryIdentifier identifier = new RepositoryIdentifier(locationStr);
				
				if (workspace) {
					
					RepositoryDefinition def = new RepositoryDefinition(ERepositoryType.WORKSPACE, identifier, location);
					repoConfig.addWorkspaceRepositories(def);
				} else if (cache) {
					
					RepositoryDefinition def = new RepositoryDefinition(ERepositoryType.CACHE, identifier, location);
					repoConfig.addCacheRepository(def);
				} else if (remote) {

					RepositoryDefinition def = new RepositoryDefinition(ERepositoryType.REMOTE, identifier, location);
					repoConfig.addRemoteRepository(def);
				}
			}
		} catch (URISyntaxException e) {

			throw new SAXException(e);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (localName.equals("workspace")) {
			workspace = false;
		} else if (localName.equals("cache")) {
			cache = false;
		} else if (localName.equals("remote")) {
			remote = false;
		}
	}

}
