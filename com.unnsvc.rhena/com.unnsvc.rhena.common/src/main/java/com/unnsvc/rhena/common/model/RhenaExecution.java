
package com.unnsvc.rhena.common.model;

import java.io.File;

public class RhenaExecution {

	private ModuleIdentifier moduleIdentifier;
	private RhenaExecutionType edgeType;
	private File artifact;

	public RhenaExecution(ModuleIdentifier moduleIdentifier, RhenaExecutionType edgeType, File artifact) {

		this.moduleIdentifier = moduleIdentifier;
		this.edgeType = edgeType;
		this.artifact = artifact;
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public RhenaExecutionType getEdgeType() {

		return edgeType;
	}

	public File getArtifact() {

		return artifact;
	}
}
