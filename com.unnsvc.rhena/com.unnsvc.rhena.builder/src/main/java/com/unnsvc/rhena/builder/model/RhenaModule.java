package com.unnsvc.rhena.builder.model;

import com.unnsvc.rhena.builder.identifier.ComponentIdentifier;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.identifier.Version;

public class RhenaModule {
	
	private ModuleIdentifier rhenaModuleEdge;
	private ComponentIdentifier componentIdentifier;
	private Version version;
	private ModuleIdentifier lifecycleDeclaration;

	public void setExtends(ModuleIdentifier rhenaModuleEdge) {

		this.rhenaModuleEdge = rhenaModuleEdge;
	}

	public void setComponent(ComponentIdentifier componentIdentifier) {

		this.componentIdentifier = componentIdentifier;
	}

	public void setVersion(Version version) {

		this.version = version;
	}

	public void setLifecycleDeclaration(ModuleIdentifier lifecycleDeclaration) {

		this.lifecycleDeclaration = lifecycleDeclaration;
	}

}
