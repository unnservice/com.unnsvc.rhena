
package com.unnsvc.rhena.builder;

import java.util.HashMap;
import java.util.Map;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.Identifier;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.identifier.Version;
import com.unnsvc.rhena.builder.model.RhenaModuleDescriptor;
import com.unnsvc.rhena.builder.resolvers.ResolutionContext;

public class RhenaContext {

	private ResolutionContext resolution;
	private Map<CompositeScope, Map<ModuleIdentifier, RhenaModuleDescriptor>> resolvedStates = new HashMap<CompositeScope, Map<ModuleIdentifier, RhenaModuleDescriptor>>();

	public RhenaContext(ResolutionContext resolution) {

		this.resolution = resolution;
		for (CompositeScope existing : CompositeScope.values()) {
			this.resolvedStates.put(existing, new HashMap<ModuleIdentifier, RhenaModuleDescriptor>());
		}
	}

	public ResolutionContext getResolution() {

		return resolution;
	}

	public ModuleIdentifier newModuleIdentifier(String componentIdentifierStr, String moduleIdentifierStr, String versionStr) throws RhenaException {

		Identifier componentIdentifier = Identifier.valueOf(componentIdentifierStr);
		Identifier moduleIdentifier = Identifier.valueOf(moduleIdentifierStr);
		Version version = Version.valueOf(versionStr);

		return new ModuleIdentifier(componentIdentifier, moduleIdentifier, version);
	}

	public ModuleIdentifier newModuleIdentifier(String moduleIdentifierString) throws RhenaException {

		String[] parts = moduleIdentifierString.split(":");
		return newModuleIdentifier(parts[0], parts[1], parts[2]);
	}

	public Map<CompositeScope, Map<ModuleIdentifier, RhenaModuleDescriptor>> getResolvedStates() {

		return resolvedStates;
	}

}
