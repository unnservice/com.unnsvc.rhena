
package com.unnsvc.rhena.common.model;

/**
 * Supertype to a rhena model node.
 * 
 * @author noname
 *
 */
public abstract class RhenaReference {

	private ModuleIdentifier moduleIdentifier;

	public RhenaReference(ModuleIdentifier moduleIdentifier) {

		this.moduleIdentifier = moduleIdentifier;
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}
}
