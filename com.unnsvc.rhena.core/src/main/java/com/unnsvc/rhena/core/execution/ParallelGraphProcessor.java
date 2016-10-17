
package com.unnsvc.rhena.core.execution;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.ModuleType;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;

public class ParallelGraphProcessor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaContext context;
	private ExecutorService executor;
	private CompletionService<ExecutionResult> completionService;
	/**
	 * Will only be modified on this objects control thread
	 */
	private Set<IRhenaEdge> executed;
	private Set<IRhenaEdge> executing;

	public ParallelGraphProcessor(IRhenaContext context) {

		this.context = context;
		this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		this.executed = new HashSet<IRhenaEdge>();
		this.completionService = new ExecutorCompletionService<ExecutionResult>(executor);
		this.executing = new HashSet<IRhenaEdge>();
	}

	public void processEdges(Set<IRhenaEdge> edges) throws RhenaException {

		while (selectReady(edges) > 0 && !executing.isEmpty()) {

			try {
				// wait for one to finish
				ExecutionResult result = completionService.take().get();
				executing.remove(result.getEdge());
				executed.add(result.getEdge());

				log.debug("Completed "+result.getEdge().getTarget().getModuleIdentifier().toTag(result.getEdge().getExecutionType())+", executors remaining in running state: " + executing.size());

			} catch (InterruptedException | ExecutionException ie) {

				throw new RhenaException(ie.getMessage(), ie);
			}
		}

	}

	private int selectReady(Set<IRhenaEdge> allEdges) throws RhenaException {

		int submitted = 0;

		for (IRhenaEdge edge : allEdges) {
			if (!isWaiting(edge)) {
				if ((edge.getExecutionType() != EExecutionType.MODEL) && !executed.contains(edge)) {

					// selected.add(new ExecutingCallable(context, edge));
					executing.add(edge);
					completionService.submit(new ExecutingCallable(context, edge));
					submitted++;
				}
			}
		}

		log.debug("Parallel executor submitted " + submitted + " for execution.");

		return submitted;
	}

	private boolean isWaiting(IRhenaEdge edge) throws RhenaException {

		// check if all edges have been executed
		// if they have, select the edge

		IRhenaModule target = edge.getTarget();

		if (target.getLifecycleName() != null) {
			ILifecycleDeclaration lifecycle = edge.getTarget().getLifecycleDeclaration(target.getLifecycleName());
			if (!executed.contains(lifecycle.getContext().getModuleEdge())) {
				log.debug(edge.getTarget().getModuleIdentifier().toTag(edge.getExecutionType()) + " waiting on " + lifecycle.getContext().getModuleEdge());
				return true;
			}
			for (IProcessorReference processor : lifecycle.getProcessors()) {
				if (!executed.contains(processor.getModuleEdge())) {
					log.debug(edge.getTarget().getModuleIdentifier().toTag(edge.getExecutionType()) + " waiting on " + processor.getModuleEdge());
					return true;
				}
			}
			if (!executed.contains(lifecycle.getGenerator().getModuleEdge())) {
				log.debug(edge.getTarget().getModuleIdentifier().toTag(edge.getExecutionType()) + " waiting on " + lifecycle.getGenerator().getModuleEdge());
				return true;
			}
		}

		for (IRhenaEdge dependency : target.getDependencyEdges()) {

			if (edge.getTarget().getModuleType() != ModuleType.REFERENCE) {
				if (edge.getExecutionType().canTraverse(dependency.getExecutionType())) {
					// this node is interesting to us, so we are
					// waiting on it
					log.debug(edge.getTarget().getModuleIdentifier().toTag(edge.getExecutionType()) + " waiting on " + edge.getTarget());
					return true;
				}
			}
		}

		return false;
	}
}
