
package com.unnsvc.rhena.common.model;

import java.io.Serializable;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRhenaEdge extends Serializable {

	public IEntryPoint getEntryPoint();

	public ESelectionType getTraverseType();

	public ModuleIdentifier getSource();

}
