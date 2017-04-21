
package com.unnsvc.rhena.core.resolution;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaBuilder;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.execution.IModuleExecutor;
import com.unnsvc.rhena.common.execution.IModuleExecutorCallback;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.EModuleType;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.execution.ExecutionFrame;
import com.unnsvc.rhena.execution.ModuleExecutor;
import com.unnsvc.rhena.execution.builders.RemoteBuilder;
import com.unnsvc.rhena.execution.builders.WorkspaceBuilder;
import com.unnsvc.rhena.model.EntryPoint;

public class CascadingModelBuilder extends AbstractCachingResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IModuleExecutor moduleExecutor;
	private Set<ExecutionFrame> executionFrames;

	public CascadingModelBuilder(IRhenaConfiguration config, IRhenaCache cache, IRhenaResolver resolver) {

		super(cache, resolver);

		this.moduleExecutor = new ModuleExecutor(config);
		this.executionFrames = new HashSet<ExecutionFrame>();

		this.moduleExecutor.addCallback(new IModuleExecutorCallback() {

			@Override
			public void onExecuted(IExecutionResult executionResult) {

				IRhenaModule module = executionResult.getModule();
				ModuleIdentifier moduleIdentifier = module == null ? null : module.getIdentifier();
				IEntryPoint entryPoint = executionResult.getEntryPoint();

				synchronized (executionFrames) {

					/**
					 * After it has executed, we want to remove the outgoing,
					 * and the actual frame
					 */
					for (Iterator<ExecutionFrame> iter = executionFrames.iterator(); iter.hasNext();) {

						ExecutionFrame frame = iter.next();
						IEntryPoint frameIncoming = frame.getIncoming();
						
						/**
						 * Module is null (source caller), it will not have any
						 * incoming
						 */
						if (frame.isModuleIdentifier(moduleIdentifier) && frameIncoming.getExecutionType().equals(entryPoint.getExecutionType())) {

							// null module caller will never be in outgoing so
							// we can continue; loop
							iter.remove();
						} else {

							// remove outgoing relationship
							frame.removeOutgoing(entryPoint);
						}
					}

					System.err.println("Notify all");
					executionFrames.notifyAll();
				}
			}
		});

	}

	@Override
	protected void onRelationship(IRhenaModule source, IEntryPoint outgoing) {

		ModuleIdentifier sourceIdentifier = source == null ? null : source.getIdentifier();
		IRhenaModule sourceModule = source;

		ModuleIdentifier targetIdentifier = outgoing.getTarget();
		IRhenaModule targetModule = getCache().getModule(targetIdentifier);

		boolean sourceExisted = false;
		boolean targetExisted = false;
		for (Iterator<ExecutionFrame> iter = executionFrames.iterator(); iter.hasNext();) {

			ExecutionFrame frame = iter.next();
			if (frame.getModule() != null && frame.getModule().getIdentifier().equals(sourceIdentifier)) {
				// add outgoing
				sourceExisted = true;
			} else if (frame.getModule() != null && frame.getModule().getIdentifier().equals(targetIdentifier)) {
				// att incoming
				targetExisted = true;
			}
		}

		if (!sourceExisted) {
			ExecutionFrame frame = new ExecutionFrame(sourceModule);
			frame.addOutgoing(outgoing);
			executionFrames.add(frame);
		}

		if (!targetExisted) {
			ExecutionFrame frame = new ExecutionFrame(targetModule);
			frame.setIncoming(outgoing);
			executionFrames.add(frame);
		}
	}

	@Override
	protected void onTraversalComplete() throws RhenaException {

//		try {
			while (!executionFrames.isEmpty()) {

				synchronized (executionFrames) {

					for (Iterator<ExecutionFrame> iter = executionFrames.iterator(); iter.hasNext();) {
						ExecutionFrame next = iter.next();
						if (next.getOutgoing().isEmpty()) {
							iter.remove();

							/**
							 * Find highest incoming
							 */
							IEntryPoint incoming = next.getIncoming();
							IRhenaBuilder builder = createBuilder(incoming, next.getModule());
							moduleExecutor.submit(builder);
						}
					}

//					System.err.println("wait");
//					executionFrames.wait();
				}
			}
//		} catch (InterruptedException ie) {
//			throw new RhenaException(ie);
//		}
	}

	private IRhenaBuilder createBuilder(IEntryPoint entryPoint, IRhenaModule module) throws RhenaException {

		// log.info("Submitting for execution: " + targeting);
		if (module.getModuleType() == EModuleType.WORKSPACE) {

			return new WorkspaceBuilder(entryPoint, module);
		} else if (module.getModuleType() == EModuleType.REMOTE) {

			return new RemoteBuilder(entryPoint, module);
		} else {

			throw new RhenaException("Framework doesn't know how to handle module of type: " + module.getModuleType());
		}
	}

	@Override
	protected void onModuleResolved(IRhenaModule resolvedModule) throws RhenaException {

	}

	public IExecutionResult executeModel(EExecutionType type, ModuleIdentifier identifier) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(type, identifier);

		visitTree(entryPoint, ESelectionType.SCOPE);

		return getCache().getCachedExecution(identifier);
	}

}
