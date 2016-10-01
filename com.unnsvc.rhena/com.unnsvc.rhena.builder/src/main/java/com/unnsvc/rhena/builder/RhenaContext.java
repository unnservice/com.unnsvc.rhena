
package com.unnsvc.rhena.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.Identifier;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.identifier.Version;
import com.unnsvc.rhena.builder.model.RhenaModule;
import com.unnsvc.rhena.builder.resolvers.ResolutionEngine;

public class RhenaContext {

	private ResolutionEngine resolution;
	private Stack<ModuleIdentifier> unresolvedIdentifiers;
	private Map<ModuleIdentifier, RhenaModule> modules;

	public RhenaContext(ResolutionEngine resolution) {

		this.resolution = resolution;
		this.modules = new HashMap<ModuleIdentifier, RhenaModule>();
		this.unresolvedIdentifiers = new Stack<ModuleIdentifier>();
	}

	public ResolutionEngine getResolution() {

		return resolution;
	}

	public ModuleIdentifier newModuleIdentifier(String componentIdentifierStr, String moduleIdentifierStr, String versionStr) throws RhenaException {

		Identifier componentIdentifier = Identifier.valueOf(componentIdentifierStr);
		Identifier moduleIdentifier = Identifier.valueOf(moduleIdentifierStr);
		Version version = Version.valueOf(versionStr);

		ModuleIdentifier newModuleIdentifier = new ModuleIdentifier(componentIdentifier, moduleIdentifier, version);
		if (!modules.containsKey(newModuleIdentifier)) {
			unresolvedIdentifiers.push(newModuleIdentifier);
		}
		return newModuleIdentifier;
	}

	public ModuleIdentifier newModuleIdentifier(String moduleIdentifierString) throws RhenaException {

		String[] parts = moduleIdentifierString.split(":");
		return newModuleIdentifier(parts[0], parts[1], parts[2]);
	}

	public Stack<ModuleIdentifier> getUnresolvedIdentifiers() {

		return unresolvedIdentifiers;
	}

	public Map<ModuleIdentifier, RhenaModule> getModules() {

		return modules;
	}

}
