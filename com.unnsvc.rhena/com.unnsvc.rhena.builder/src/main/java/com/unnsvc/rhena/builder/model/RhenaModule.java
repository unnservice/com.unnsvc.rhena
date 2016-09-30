
package com.unnsvc.rhena.builder.model;

import com.unnsvc.rhena.builder.identifier.ComponentIdentifier;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.identifier.Version;

public class RhenaModule {

	private ModuleIdentifier moduleIdentifier;
	private ModuleIdentifier parentModule;
	private ComponentIdentifier componentIdentifier;
	private Version version;
	private ModuleIdentifier lifecycleDeclaration;

	public RhenaModule(ModuleIdentifier moduleIdentifier) {

		this.moduleIdentifier = moduleIdentifier;
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public void setExtends(ModuleIdentifier parentModule) {

		this.parentModule = parentModule;
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
