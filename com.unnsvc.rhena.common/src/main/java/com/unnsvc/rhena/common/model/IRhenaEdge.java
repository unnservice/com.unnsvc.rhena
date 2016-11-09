
package com.unnsvc.rhena.common.model;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRhenaEdge {

	public EExecutionType getExecutionType();

	public ModuleIdentifier getTarget();

	public ESelectionType getTraverseType();

}
