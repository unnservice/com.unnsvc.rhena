
package com.unnsvc.rhena.execution;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.execution.IExecutionEdge;
import com.unnsvc.rhena.common.ng.execution.IExecutionModule;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;

/**
 * Execution module is a work unit that can be executed
 * @author noname
 *
 */
public class ExecutionModule implements IExecutionModule {

	private IRhenaCache cache;
	private IRhenaModule module;
	private List<IExecutionEdge> edges;

	public ExecutionModule(IRhenaCache cache, IRhenaModule module) {

		this.cache = cache;
		this.module = module;
		this.edges = new ArrayList<IExecutionEdge>();
	} 

	public void run() {
		// traverse tree to collect dependencies?
	}
	
	public synchronized 

	/**
	 * Called by main thread to check whether we can build
	 */
	@Override
	public synchronized boolean isBuildable() {

		return edges.isEmpty();
	}
}
