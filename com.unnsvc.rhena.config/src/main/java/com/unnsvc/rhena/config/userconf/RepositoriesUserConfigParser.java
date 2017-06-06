
package com.unnsvc.rhena.config.userconf;

import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.repository.ERepositoryType;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;
import com.unnsvc.rhena.common.utils.DocumentHelper;
import com.unnsvc.rhena.config.RepositoryConfiguration;
import com.unnsvc.rhena.config.RepositoryDefinition;

public class RepositoriesUserConfigParser extends AbstractUserConfigParser {

	@Override
	public IRepositoryConfiguration parse(Document document) throws RhenaException {

		IRepositoryConfiguration repo = new RepositoryConfiguration();

		try {

			for (Node child : DocumentHelper.childNodes(document)) {

				for (Node repoNode : DocumentHelper.childNodes(child)) {

					for (Node repoDef : DocumentHelper.childNodes(repoNode)) {

						String location = repoDef.getAttributes().getNamedItem("location").getNodeValue();
						RepositoryIdentifier id = new RepositoryIdentifier(location);

						IRepositoryDefinition def = null;
						String repoType = repoNode.getNodeName();

						if (repoType.equals("workspace")) {
							def = new RepositoryDefinition(ERepositoryType.WORKSPACE, id, new URI(location));
						} else if (repoType.equals("cache")) {
							def = new RepositoryDefinition(ERepositoryType.CACHE, id, new URI(location));
						} else if (repoType.equals("remote")) {
							def = new RepositoryDefinition(ERepositoryType.REMOTE, id, new URI(location));
						} else {
							throw new RhenaException("Unknown repository type: " + repoType);
						}

						repo.addRepository(def);
					}
				}
			}
		} catch (URISyntaxException mue) {
			throw new RhenaException(mue);
		}

		return repo;
	}

	@Override
	public byte[] serialise(IRepositoryConfiguration repoConfig) throws RhenaException {

		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append(PLATFORM_NL);
		sb.append("<repositories xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:rhena:repositories\" xsi:schemaLocation=\"urn:rhena:repositories http://schema.unnsvc.com/rhena/repositories.xsd\">");
		sb.append(PLATFORM_NL);
		
		serialiseRepoConfig(repoConfig, sb);

		sb.append("</repositories>").append(PLATFORM_NL);
		return sb.toString().getBytes();
	}

	private static void serialiseRepoConfig(IRepositoryConfiguration repositoryConfiguration, StringBuilder sb) {

		sb.append(in(1) + "<workspace>").append(PLATFORM_NL);
		for (IRepositoryDefinition def : repositoryConfiguration.getWorkspaceRepositories()) {
			serialiseRepoDefConfig(2, def, sb);
		}
		sb.append(in(1) + "</workspace>").append(PLATFORM_NL);
		sb.append(in(1) + "<cache>").append(PLATFORM_NL);
		for (IRepositoryDefinition def : repositoryConfiguration.getCacheRepositories()) {
			serialiseRepoDefConfig(2, def, sb);
		}
		sb.append(in(1) + "</cache>").append(PLATFORM_NL);
		sb.append(in(1) + "<remote>").append(PLATFORM_NL);
		for (IRepositoryDefinition def : repositoryConfiguration.getRemoteRepositories()) {
			serialiseRepoDefConfig(2, def, sb);
		}
		sb.append(in(1) + "</remote>").append(PLATFORM_NL);
	}

	private static void serialiseRepoDefConfig(int i, IRepositoryDefinition def, StringBuilder sb) {

		sb.append(in(i) + "<repository location=\"" + def.getLocation().toString() + "\" />").append(PLATFORM_NL);
	}
}
