
package com.unnsvc.rhena.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.execution.IExecutionModule;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;

/**
 * Edge worker implements the execution of the edge
 * 
 * @author noname
 *
 */
public class ExecutionEdgeWorker extends ExecutionEdge {

	private Logger log = LoggerFactory.getLogger(getClass());

	public ExecutionEdgeWorker(IExecutionModule source, EExecutionType type, IExecutionModule target) {

		super(source, type, target);
	}

	@Override
	public IExecutionResult call() throws Exception {

		log.info("Executing: " + getTarget().getModule().getIdentifier() + ":" + getType().toString().toLowerCase());

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

		return new IExecutionResult() {

			@Override
			public EExecutionType getType() {

				return ExecutionEdgeWorker.this.getType();
			}

			@Override
			public ModuleIdentifier getIdentifier() {

				return ExecutionEdgeWorker.this.getTarget().getModule().getIdentifier();
			}
		};

		// throw new RhenaException("Not implemented");
	}

}
