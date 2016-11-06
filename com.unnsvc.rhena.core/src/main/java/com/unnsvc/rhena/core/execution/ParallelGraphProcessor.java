
package com.unnsvc.rhena.core.execution;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.logging.IRhenaLoggingHandler;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.ModuleType;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;

public class ParallelGraphProcessor {

	private IRhenaLoggingHandler log;
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
		this.log = context.getLogger(getClass());
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

				log.debug("Completed " + result.getEdge().getTarget().toTag(result.getEdge().getExecutionType()) + ", executors remaining in running state: "
						+ executing.size());

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

		IRhenaModule target = context.materialiseModel(edge.getTarget());

		if (target.getLifecycleName() != null) {
			ILifecycleDeclaration lifecycle = context.materialiseModel(edge.getTarget()).getLifecycleDeclaration(target.getLifecycleName());
			if (!executed.contains(lifecycle.getContext().getModuleEdge())) {
				// log.debug(edge.getTarget().getModuleIdentifier().toTag(edge.getExecutionType())
				// + " waiting on context " +
				// lifecycle.getContext().getModuleEdge());
				debugPrintWaitingOn(edge, lifecycle.getContext().getModuleEdge());
				return true;
			}
			for (IProcessorReference processor : lifecycle.getProcessors()) {
				if (!executed.contains(processor.getModuleEdge())) {
					debugPrintWaitingOn(edge, processor.getModuleEdge());
					return true;
				}
			}
			if (!executed.contains(lifecycle.getGenerator().getModuleEdge())) {
				debugPrintWaitingOn(edge, lifecycle.getGenerator().getModuleEdge());
				return true;
			}
		}

		for (IRhenaEdge dependency : target.getDependencyEdges()) {

			if (edge.getExecutionType().canTraverse(dependency.getExecutionType())) {
				if (!executed.contains(dependency)) {
					debugPrintWaitingOn(edge, dependency);
					return true;
				}
			}
		}

		return false;
	}

	private void debugPrintWaitingOn(IRhenaEdge thisEdge, IRhenaEdge waitingOn) {

		String tag = thisEdge.getTarget().toTag(thisEdge.getExecutionType());
		String thatTag = thisEdge.getTarget().toTag(thisEdge.getExecutionType());
		log.debug(tag + " -> waiting on -> " + thatTag);
	}
}
