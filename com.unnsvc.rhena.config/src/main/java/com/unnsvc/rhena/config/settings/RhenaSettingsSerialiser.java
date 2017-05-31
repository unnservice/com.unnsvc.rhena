
package com.unnsvc.rhena.config.settings;

import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;

public class RhenaSettingsSerialiser {

	private IRhenaConfiguration config;

	public RhenaSettingsSerialiser(IRhenaConfiguration config) {

		this.config = config;
	}

	public String serialise() {

		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append(System.getProperty("line.separator"));
		sb.append("<settings xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:rhena:settings\">")
				.append(System.getProperty("line.separator"));
		sb.append("\t<repositories>").append(System.getProperty("line.separator"));

		sb.append("\t\t<workspace>").append(System.getProperty("line.separator"));
		config.getRepositoryConfiguration().getWorkspaceRepositories().forEach(repo -> {
			serialiseRepo(repo, sb);
		});
		sb.append("\t\t</workspace>").append(System.getProperty("line.separator"));

		sb.append("\t\t<cache>").append(System.getProperty("line.separator"));
		config.getRepositoryConfiguration().getCacheRepositories().forEach(repo -> {
			serialiseRepo(repo, sb);
		});
		sb.append("\t\t</cache>").append(System.getProperty("line.separator"));

		sb.append("\t\t<remote>").append(System.getProperty("line.separator"));
		config.getRepositoryConfiguration().getRemoteRepositories().forEach(repo -> {
			serialiseRepo(repo, sb);
		});
		sb.append("\t\t</remote>").append(System.getProperty("line.separator"));

		sb.append("\t</repositories>").append(System.getProperty("line.separator"));
		sb.append("</settings>").append(System.getProperty("line.separator"));

		return sb.toString();
	}

	private void serialiseRepo(IRepositoryDefinition repo, StringBuilder sb) {

		sb.append("\t\t\t<repository location=\"" + repo.getLocation().toString() + "\" />").append(System.getProperty("line.separator"));
	}
}
