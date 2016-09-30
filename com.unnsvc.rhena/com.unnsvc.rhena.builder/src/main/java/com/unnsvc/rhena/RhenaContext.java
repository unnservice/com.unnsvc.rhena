
package com.unnsvc.rhena;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModule;
import com.unnsvc.rhena.builder.resolvers.ResolutionEngine;

public class RhenaContext {

	private ResolutionEngine resolution;
	private Stack<ModuleIdentifier> unresolvedIdentifiers;
	private Map<ModuleIdentifier, RhenaModule> resolvedIdentifiers;

	public RhenaContext(ResolutionEngine resolution) {

		this.resolution = resolution;
		this.unresolvedIdentifiers = new Stack<ModuleIdentifier>();
		this.resolvedIdentifiers = new HashMap<ModuleIdentifier, RhenaModule>();
	}

	public ResolutionEngine getResolution() {

		return resolution;
	}
	
	
	public Map<ModuleIdentifier, RhenaModule> getResolvedIdentifiers() {

		return resolvedIdentifiers;
	}

	public Stack<ModuleIdentifier> getUnresolvedIdentifiers() {

		return unresolvedIdentifiers;
	}


	public void addUnresolvedModuleIdentifier(ModuleIdentifier moduleIdentifier) {

		this.unresolvedIdentifiers.push(moduleIdentifier);
	}

}
