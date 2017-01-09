
package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.ESelectionType;

public class InMemoryEdge extends RhenaEdge {

	public InMemoryEdge(InMemoryModule source, InMemoryModule target, EExecutionType targetType, ESelectionType targetTraverseType) {

		super(source.getIdentifier(), new EntryPoint(targetType, target.getIdentifier()), targetTraverseType);

	}

}
