
package com.unnsvc.rhena.model;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.ESelectionType;
import com.unnsvc.rhena.common.ng.model.IEntryPoint;
import com.unnsvc.rhena.common.ng.model.IRhenaEdge;

public class RhenaEdge implements IRhenaEdge {

	private ModuleIdentifier source;
	private ESelectionType traverseType;
	private IEntryPoint entryPoint;

	public RhenaEdge(ModuleIdentifier source, ESelectionType traverseType, IEntryPoint entryPoint) {

		this.source = source;
		this.traverseType = traverseType;
		this.entryPoint = entryPoint;
	}

	public ModuleIdentifier getSource() {

		return source;
	}

	public void setSource(ModuleIdentifier source) {

		this.source = source;
	}

	public ESelectionType getTraverseType() {

		return traverseType;
	}

	public void setTraverseType(ESelectionType traverseType) {

		this.traverseType = traverseType;
	}

	public IEntryPoint getEntryPoint() {

		return entryPoint;
	}

	public void setEntryPoint(IEntryPoint entryPoint) {

		this.entryPoint = entryPoint;
	}

}
