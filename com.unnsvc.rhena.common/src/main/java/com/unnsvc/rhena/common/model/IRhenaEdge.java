package com.unnsvc.rhena.common.model;

import java.io.Serializable;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRhenaEdge extends Serializable {

	public ModuleIdentifier getSource();

	public void setSource(ModuleIdentifier source);

	public ESelectionType getTraverseType();

	public void setTraverseType(ESelectionType traverseType);

	public IEntryPoint getEntryPoint();

	public void setEntryPoint(IEntryPoint entryPoint);

}
