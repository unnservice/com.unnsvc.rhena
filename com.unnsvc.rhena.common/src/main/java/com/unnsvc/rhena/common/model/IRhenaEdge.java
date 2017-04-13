
package com.unnsvc.rhena.common.model;

import java.io.Serializable;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRhenaEdge extends Serializable {

	/**
	 * The source becomes relevant in tracing backward to select based on
	 * traverse type
	 * 
	 * @return
	 */
	public ModuleIdentifier getSource();

	public IEntryPoint getEntryPoint();

	public ESelectionType getTraverseType();
}
