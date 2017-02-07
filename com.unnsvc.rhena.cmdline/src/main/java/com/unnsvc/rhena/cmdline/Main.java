
package com.unnsvc.rhena.cmdline;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.cmdline.flags.ArgumentFlag;
import com.unnsvc.rhena.cmdline.flags.ArgumentParser;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.settings.IRhenaSettings;
import com.unnsvc.rhena.core.RhenaConfiguration;
import com.unnsvc.rhena.core.settings.RepositoryDefinition;
import com.unnsvc.rhena.core.settings.RhenaSettingsParser;

public class Main extends AbstractMain {

	private Logger log = LoggerFactory.getLogger(getClass());

	public Main() {

	}

	@Override
	public void runWithArgs(ArgumentParser parser) throws RhenaException {

		IRhenaSettings settings = new RhenaSettingsParser().parseDefault();

		for (ArgumentFlag flag : parser.getFlag("repository", "r")) {
			try {
				URI location = new URI(flag.getArgument());
				settings.getRepositories().add(new RepositoryDefinition(location));
			} catch (URISyntaxException u) {
				throw new RhenaException("Invalid repository location: " + flag.getArgument());
			}
		}

		for (ArgumentFlag flag : parser.getFlag("workspace", "w")) {
			File workspaceLocation = new File(flag.getArgument());
			if (!workspaceLocation.exists() || !workspaceLocation.isDirectory()) {
				throw new RhenaException("Workspace location does not exist: " + workspaceLocation);
			}
			settings.getWorkspaces().add(new RepositoryDefinition(workspaceLocation.toURI()));
		}

		IRhenaConfiguration config = new RhenaConfiguration(settings);

	}

}
