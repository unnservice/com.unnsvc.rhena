
package com.unnsvc.rhena.cmdline;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.cmdline.flags.ArgumentException;
import com.unnsvc.rhena.cmdline.flags.ArgumentFlag;
import com.unnsvc.rhena.cmdline.flags.ArgumentParser;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.settings.IRhenaSettings;
import com.unnsvc.rhena.core.CommandCaller;
import com.unnsvc.rhena.core.RhenaConfiguration;
import com.unnsvc.rhena.core.RhenaContext;
import com.unnsvc.rhena.core.RhenaEngine;
import com.unnsvc.rhena.core.settings.RepositoryDefinition;
import com.unnsvc.rhena.core.settings.RhenaSettingsParser;

public class Main extends AbstractMain {

	private Logger log = LoggerFactory.getLogger(getClass());

	public Main() {

	}

	@Override
	public void configureWithArgs(ArgumentParser parser) throws RhenaException, ArgumentException {

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

		for (ArgumentFlag flag : parser.getFlag("agentClasspath", "a")) {
			config.setAgentClasspath(flag.getArgument());
		}

		for (ArgumentFlag flag : parser.getFlag("profilerClasspath", "b")) {
			config.setProfilerClasspath(flag.getArgument());
		}

		for (ArgumentFlag flag : parser.getFlag("packageWorkspace", "p")) {
			config.setPackageWorkspace(Boolean.valueOf(flag.getArgument()));
		}

		for (ArgumentFlag flag : parser.getFlag("parallel", "t")) {
			config.setParallel(Boolean.valueOf(flag.getArgument()));
		}

		for (ArgumentFlag flag : parser.getFlag("instanceHome", "i")) {
			File instanceHome = new File(flag.getArgument());
			if (!instanceHome.isDirectory()) {
				throw new RhenaException("Instance home location does not exist: " + instanceHome);
			}
			config.setInstanceHome(instanceHome);
		}

		if (parser.getCommands().isEmpty() || parser.getCommands().get(0).contains(":")) {

			throw new ArgumentException("Did not provide a module identifier and execution to build on.");
		}

		ModuleIdentifier identifier = null;
		EExecutionType execution = null;

		List<String> commands = new ArrayList<String>();

		for (int i = 0; i < parser.getCommands().size(); i++) {

			if (i == 0) {
				String[] parts = parser.getCommands().get(i).split(":");
				identifier = ModuleIdentifier.valueOf(parts[0]);
				execution = EExecutionType.valueOf(parts[1]);
			} else {
				commands.add(parser.getCommands().get(i));
			}
		}

		runWithConfiguration(config, identifier, execution, commands);
	}

	private void runWithConfiguration(IRhenaConfiguration config, ModuleIdentifier identifier, EExecutionType executionType, List<String> commands) throws RhenaException {

		IRhenaContext context = new RhenaContext(config);
		IRhenaEngine engine = new RhenaEngine(context);
		IRhenaModule module = engine.materialiseModel(identifier);
		engine.materialiseExecution(new CommandCaller(module, executionType, commands.get(0)));
		log.info("Built: " + identifier);
	}

}
