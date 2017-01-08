
package com.unnsvc.rhena.common.model;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRhenaEdge {

	public IEntryPoint getEntryPoint();

	public ESelectionType getTraverseType();

	public ModuleIdentifier getSource();

}
