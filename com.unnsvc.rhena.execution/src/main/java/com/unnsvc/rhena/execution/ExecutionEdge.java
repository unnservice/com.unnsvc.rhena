
package com.unnsvc.rhena.execution;

import com.unnsvc.rhena.common.ng.execution.IExecutionEdge;
import com.unnsvc.rhena.common.ng.execution.IExecutionModule;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.EExecutionType;

public abstract class ExecutionEdge implements IExecutionEdge {

	private IExecutionModule source;
	private EExecutionType type;
	private IExecutionModule target;

	public ExecutionEdge(IExecutionModule source, EExecutionType type, IExecutionModule target) {

		this.source = source;
		this.type = type;
		this.target = target;
	}

	@Override
	public IExecutionModule getSource() {

		return source;
	}

	@Override
	public EExecutionType getType() {

		return type;
	}

	@Override
	public IExecutionModule getTarget() {

		return target;
	}

	@Override
	public String toString() {

		/**
		 * Source may be null at the root of the tree as the caller is the source
		 */
		ModuleIdentifier sourceIdentifier = source.getModule() == null ? null : source.getModule().getIdentifier();
		return "ExecutionEdge [source=" + sourceIdentifier + ", type=" + type + ", target=" + target.getModule().getIdentifier() + "]";
	}
}
