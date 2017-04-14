
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.model.EntryPoint;

public class Caller implements ICaller {

	private static final long serialVersionUID = 1L;
	private IRhenaModule module;
	private IEntryPoint entryPoint;

	public Caller(IRhenaModule module, EExecutionType executionType) {

		this.module = module;
		this.entryPoint = new EntryPoint(executionType, module.getIdentifier());
	}

	@Override
	public IRhenaModule getModule() {

		return module;
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

	@Override
	public String toString() {

		return "Caller [entryPoint=" + entryPoint + "]";
	}
}
