
package com.unnsvc.rhena.execution;

import java.util.HashSet;
import java.util.Set;

import com.unnsvc.rhena.common.ng.execution.IExecutionEdge;
import com.unnsvc.rhena.common.ng.execution.IExecutionModule;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;

/**
 * Execution module is a work unit that can be executed
 * 
 * @author noname
 *
 */
public class ExecutionModule implements IExecutionModule {

	private IRhenaModule module;
	private Set<IExecutionEdge> edges;

	public ExecutionModule(IRhenaModule module) {

		this.module = module;
		this.edges = new HashSet<IExecutionEdge>();
	}

	/**
	 * Called by main thread to check whether we can build
	 */
	@Override
	public synchronized boolean isBuildable() {

		return edges.isEmpty();
	}
	
	@Override
	public IRhenaModule getModule() {

		return module;
	}

	/**
	 * Called from children
	 */
	@Override
	public synchronized void removeExecuted(IExecutionEdge edge) {

		this.edges.remove(edge);
	}
}
