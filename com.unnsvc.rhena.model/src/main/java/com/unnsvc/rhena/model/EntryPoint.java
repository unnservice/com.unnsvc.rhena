
package com.unnsvc.rhena.model;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.EExecutionType;
import com.unnsvc.rhena.common.ng.model.IEntryPoint;

public class EntryPoint implements IEntryPoint {

	private EExecutionType executionType;
	private ModuleIdentifier target;

	public EntryPoint(EExecutionType executionType, ModuleIdentifier target) {

		this.executionType = executionType;
		this.target = target;
	}

	public EExecutionType getExecutionType() {

		return executionType;
	}

	public void setExecutionType(EExecutionType executionType) {

		this.executionType = executionType;
	}

	public ModuleIdentifier getTarget() {

		return target;
	}

	public void setTarget(ModuleIdentifier target) {

		this.target = target;
	}

}
