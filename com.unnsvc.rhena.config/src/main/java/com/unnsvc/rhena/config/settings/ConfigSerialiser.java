
package com.unnsvc.rhena.config.settings;

import java.io.IOException;

import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;

public class ConfigSerialiser {

	private ConfigSerialiser() {

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
		// serialiseAgentConfig(indent + 1, config.getAgentConfiguration(),
		// writer);
		// serialiseBuildConfig(indent + 1, config.getBuildConfiguration(),
		// writer);

		writer.write(indent, "</settings>");
	}

	// private static void serialiseBuildConfig(int i, IBuildConfiguration
	// buildConfiguration, SerialisationWriter writer) throws IOException {
	//
	// writer.write(i, "<build>");
	// writer.write(i, "</build>");
	// }

	// private static void serialiseAgentConfig(int i, IAgentConfiguration
	// agentConfiguration, SerialisationWriter writer) throws IOException {
	//
	// writer.write(i, "<agent>");
	// writer.write(i, "</agent>");
	// }

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

	public static String indents(int indents) {

		char[] indent = new char[indents];
		for (int i = 0; i < indents; i++) {

			indent[i] = '\t';
		}
		return new String(indent);
	}
}
