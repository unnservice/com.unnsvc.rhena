
package com.unnsvc.rhena.model;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;

public class RhenaEdge implements IRhenaEdge {

	private ModuleIdentifier source;
	private ESelectionType traverseType;
	private IEntryPoint entryPoint;

	public RhenaEdge(ModuleIdentifier source, ESelectionType traverseType, IEntryPoint entryPoint) {

		this.source = source;
		this.traverseType = traverseType;
		this.entryPoint = entryPoint;
	}

	@Override
	public ModuleIdentifier getSource() {

		return source;
	}

	@Override
	public void setSource(ModuleIdentifier source) {

		this.source = source;
	}

	@Override
	public ESelectionType getTraverseType() {

		return traverseType;
	}

	@Override
	public void setTraverseType(ESelectionType traverseType) {

		this.traverseType = traverseType;
	}

	@Override
	public IEntryPoint getEntryPoint() {

		return entryPoint;
	}

	@Override
	public void setEntryPoint(IEntryPoint entryPoint) {

		this.entryPoint = entryPoint;
	}

}
