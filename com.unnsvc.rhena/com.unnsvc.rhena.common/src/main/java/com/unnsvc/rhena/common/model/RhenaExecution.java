
package com.unnsvc.rhena.common.model;

import java.io.File;

public class RhenaExecution {

	private ModuleIdentifier moduleIdentifier;
	private RhenaEdgeType edgeType;
	private File artifact;

	public RhenaExecution(ModuleIdentifier moduleIdentifier, RhenaEdgeType edgeType, File artifact) {

		this.moduleIdentifier = moduleIdentifier;
		this.edgeType = edgeType;
		this.artifact = artifact;
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public RhenaEdgeType getEdgeType() {

		return edgeType;
	}

	public File getArtifact() {

		return artifact;
	}
}
