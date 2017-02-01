
package com.unnsvc.rhena.common.search;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.logging.ILogger;

public class Dependencies implements IDependencies {

	private static final long serialVersionUID = 1L;
	private EExecutionType type;
	private Map<EExecutionType, List<IRhenaExecution>> dependencies;

	public Dependencies(EExecutionType type, Map<EExecutionType, List<IRhenaExecution>> dependencies) {

		this.type = type;
		this.dependencies = dependencies;
	}

	@Override
	public List<IRhenaExecution> getDependencies(EExecutionType type) {

		return dependencies.get(type);
	}

	@Override
	public List<IRhenaExecution> getDependencies() {

		List<IRhenaExecution> exec = new ArrayList<IRhenaExecution>();
		for (int i = 0; i <= type.ordinal(); i++) {
			EExecutionType type = EExecutionType.values()[i];
			exec.addAll(dependencies.get(type));
		}
		return exec;
	}

	@Override
	public String getAsClasspath(EExecutionType type) {

		StringBuffer sb = new StringBuffer();

		for (IRhenaExecution exec : dependencies.get(type)) {
			for (IArtifactDescriptor descriptor : exec.getArtifacts()) {
				sb.append(descriptor.getArtifactUrl().getPath());
				sb.append(File.pathSeparator);
			}
		}

		return sb.toString();
	}

	@Override
	public List<URL> getAsURLs(EExecutionType type) {

		List<URL> urls = new ArrayList<URL>();
		for (IRhenaExecution exec : dependencies.get(type)) {
			for (IArtifactDescriptor descriptor : exec.getArtifacts()) {
				urls.add(descriptor.getArtifactUrl());
			}
		}
		return urls;
	}

	@Override
	public List<URL> getAsURLs() {

		List<URL> urls = new ArrayList<URL>();
		for (int i = 0; i <= type.ordinal(); i++) {
			EExecutionType type = EExecutionType.values()[i];
			urls.addAll(getAsURLs(type));
		}
		return urls;
	}

	@Override
	public String toString() {

		return "Dependencies [type=" + type + ", dependencies=" + dependencies + "]";
	}

	/**
	 * Debug helper
	 * 
	 * @param identifier
	 */
	public void debug(ILogger logger, ModuleIdentifier identifier, EExecutionType type) {

		logger.trace(getClass(), "Dependencies for " + identifier + ":" + type);
		for (EExecutionType et : dependencies.keySet()) {
			if (!dependencies.get(et).isEmpty()) {
				for (IRhenaExecution exec : dependencies.get(et)) {
					logger.trace(getClass(), "\t" + et + " " + exec);
				}
			}
		}
	}
}
