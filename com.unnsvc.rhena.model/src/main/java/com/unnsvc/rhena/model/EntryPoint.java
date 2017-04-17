
package com.unnsvc.rhena.model;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IEntryPoint;

public class EntryPoint implements IEntryPoint {

	private EExecutionType executionType;
	private ModuleIdentifier target;

	public EntryPoint(EExecutionType executionType, ModuleIdentifier target) {

		this.executionType = executionType;
		this.target = target;
	}

	@Override
	public EExecutionType getExecutionType() {

		return executionType;
	}

	@Override
	public void setExecutionType(EExecutionType executionType) {

		this.executionType = executionType;
	}

	@Override
	public ModuleIdentifier getTarget() {

		return target;
	}

	@Override
	public void setTarget(ModuleIdentifier target) {

		this.target = target;
	}

}
