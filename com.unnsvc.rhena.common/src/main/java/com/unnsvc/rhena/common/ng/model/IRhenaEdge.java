package com.unnsvc.rhena.common.ng.model;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRhenaEdge {

	public ModuleIdentifier getSource();

	public void setSource(ModuleIdentifier source);

	public ESelectionType getTraverseType();

	public void setTraverseType(ESelectionType traverseType);

	public IEntryPoint getEntryPoint();

	public void setEntryPoint(IEntryPoint entryPoint);

}
