
package com.unnsvc.rhena.execution;

import com.unnsvc.rhena.common.ng.execution.IExecutionModule;
import com.unnsvc.rhena.common.ng.model.EExecutionType;

/**
 * Edge worker implements the execution of the edge
 * 
 * @author noname
 *
 */
public class ExecutionEdgeWorker extends ExecutionEdge {

	public ExecutionEdgeWorker(IExecutionModule source, EExecutionType type, IExecutionModule target) {

		super(source, type, target);
	}

	@Override
	public void run() {

		// collect dependencies from target
		// submit target to agent will all exeuction information
		// removal from of edge from source

		/**
		 * Source may be null for the root of the tree, because the caller is
		 * the source
		 */
		if (getSource() != null) {
			getSource().removeExecuted(this);
		}

		throw new UnsupportedOperationException("Not implemented");
	}

}
