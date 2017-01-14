
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.core.model.EntryPoint;

public class Caller implements ICaller {

	private static final long serialVersionUID = 1L;
	private IEntryPoint entryPoint;

	public Caller(ModuleIdentifier identifier, EExecutionType executionType) {

		this.entryPoint = new EntryPoint(executionType, identifier);
	}

	public Caller(IEntryPoint entryPoint) {

		this.entryPoint = entryPoint;
	}

	@Override
	public ModuleIdentifier getIdentifier() {

		return entryPoint.getTarget();
	}

	@Override
	public EExecutionType getExecutionType() {

		return entryPoint.getExecutionType();
	}

	@Override
	public IEntryPoint getEntryPoint() {

		return entryPoint;
	}

}
