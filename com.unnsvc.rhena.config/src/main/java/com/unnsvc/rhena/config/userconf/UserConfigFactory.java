
package com.unnsvc.rhena.config.userconf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.config.RhenaConfiguration;

public class UserConfigFactory {

	private static Logger log = LoggerFactory.getLogger(UserConfigFactory.class);
	private static final String PLATFORM_NL = System.getProperty("line.separator");
	public static final File repositoriesConfig = new File(System.getProperty("user.home"), ".rhena" + PLATFORM_NL + "repositories.xml");

	public static IRhenaConfiguration fromUserConfig() throws RhenaException {

		IRhenaConfiguration config = new RhenaConfiguration();
		if (repositoriesConfig.exists()) {
			IRepositoryConfiguration repoConfig = readRepositories();
			config.setRepositoryConfiguration(repoConfig);
		}
		return config;
	}

	public static IRepositoryConfiguration readRepositories(File repoConfig) throws RhenaException {

		Document document = loadDocument(repoConfig);

		RepositoriesUserConfigParser parser = new RepositoriesUserConfigParser();
		return parser.parse(document);
	}

	public static IRepositoryConfiguration readRepositories() throws RhenaException {

		return readRepositories(repositoriesConfig);
	}

	public static void serialiseRepositories(IRhenaConfiguration config) throws RhenaException {

		serialiseRepositories(config.getRepositoryConfiguration());
	}

	public static void serialiseRepositories(IRepositoryConfiguration repoConfig) throws RhenaException {

		RepositoriesUserConfigParser parser = new RepositoriesUserConfigParser();
		byte[] serialised = parser.serialise(repoConfig);
		saveDocument(repositoriesConfig, serialised);
	}

	private static void saveDocument(File configFile, byte[] content) throws RhenaException {

		log.debug("Saving to: " + repositoriesConfig);
		log.debug("\tDocument: " + new String(content));

		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(configFile))) {

			bos.write(content);
			bos.flush();
		} catch (IOException ex) {

			throw new RhenaException(ex);
		}
	}

	private static Document loadDocument(File configFile) throws RhenaException {

		log.debug("Loading: " + configFile);

		try {
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			DocumentBuilder builder = fact.newDocumentBuilder();
			return builder.parse(configFile);
		} catch (ParserConfigurationException | SAXException | IOException ex) {

			throw new RhenaException(ex);
		}
	}

}
