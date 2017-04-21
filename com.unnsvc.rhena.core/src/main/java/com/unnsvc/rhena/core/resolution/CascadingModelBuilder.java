
package com.unnsvc.rhena.core.resolution;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaBuilder;
import com.unnsvc.rhena.common.IRhenaContext;
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
import com.unnsvc.rhena.execution.CallerFrame;
import com.unnsvc.rhena.execution.ExecutionFrame;
import com.unnsvc.rhena.execution.ModuleExecutor;
import com.unnsvc.rhena.execution.builders.RemoteBuilder;
import com.unnsvc.rhena.execution.builders.WorkspaceBuilder;
import com.unnsvc.rhena.model.EntryPoint;

public class CascadingModelBuilder extends AbstractCachingResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IModuleExecutor moduleExecutor;
	private Set<ExecutionFrame> executionFrames;
	private Throwable exceptionState;

	public CascadingModelBuilder(IRhenaContext context) {

		super(context);

		this.moduleExecutor = new ModuleExecutor(getContext());
		this.executionFrames = new HashSet<ExecutionFrame>();

		this.moduleExecutor.addCallback(new IModuleExecutorCallback() {

			@Override
			public void onExecuted(IExecutionResult executionResult) {

				IEntryPoint entryPoint = executionResult.getEntryPoint();

				synchronized (executionFrames) {

					/**
					 * After it has executed, we want to remove the outgoing,
					 * and the actual frame
					 */
					for (ExecutionFrame frame : executionFrames) {

						frame.removeOutgoing(entryPoint);
					}

					getContext().getCache().cacheExecution(entryPoint, executionResult);
					executionFrames.notifyAll();
				}
			}

			@Override
			public void onException(Exception ex) {

				synchronized (executionFrames) {

					exceptionState = ex;
				}
			}
		});
	}

	@Override
	protected void onRelationship(IRhenaModule source, IEntryPoint outgoing) {

		ModuleIdentifier targetIdentifier = outgoing.getTarget();
		IRhenaModule targetModule = getContext().getCache().getModule(targetIdentifier);

		synchronized (executionFrames) {

			boolean sourceExisted = false;
			boolean targetExisted = false;

			for (ExecutionFrame executionFrame : executionFrames) {
				if (executionFrame.isForModule(source)) {
					executionFrame.addOutgoing(outgoing);
					sourceExisted = true;
				} else if (executionFrame.isForModule(targetModule)) {
					executionFrame.setIncomingIfGreater(outgoing);
					targetExisted = true;
				}
			}

			if (!sourceExisted) {
				ExecutionFrame sourceFrame = new ExecutionFrame(source);
				if (source == null) {
					sourceFrame = new CallerFrame();
				}
				sourceFrame.addOutgoing(outgoing);
				executionFrames.add(sourceFrame);
			}

			if (!targetExisted) {
				ExecutionFrame targetFrame = new ExecutionFrame(targetModule);
				targetFrame.setIncomingIfGreater(outgoing);
				executionFrames.add(targetFrame);
			}
		}
	}

	/**
	 * We care about incoming to determine build type, we care about outgoing to
	 * check whether it is resolved
	 */
	@Override
	protected void onTraversalComplete() throws RhenaException {

		try {
			while (!executionFrames.isEmpty()) {

				synchronized (executionFrames) {

					if (exceptionState != null) {

						throw new RhenaException(exceptionState);
					}

					for (Iterator<ExecutionFrame> iter = executionFrames.iterator(); iter.hasNext();) {
						ExecutionFrame next = iter.next();
						if (next instanceof CallerFrame) {
							iter.remove();

						} else if (next.getOutgoing().isEmpty()) {
							iter.remove();

							/**
							 * Find highest incoming
							 */
							IEntryPoint incoming = next.getIncoming();
							IRhenaBuilder builder = createBuilder(incoming, next.getModule());
							moduleExecutor.submit(builder);
						}
					}

					executionFrames.wait();
				}
			}
		} catch (InterruptedException ie) {
			throw new RhenaException(ie);
		} finally {

			// release references and close executor
			moduleExecutor.close();
			executionFrames.clear();
		}
	}

	private IRhenaBuilder createBuilder(IEntryPoint entryPoint, IRhenaModule module) throws RhenaException {

		log.info("Create builder for: " + entryPoint + " module: " + module);
		if (module.getModuleType() == EModuleType.WORKSPACE) {

			return new WorkspaceBuilder(getContext(), entryPoint, module);
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

		return getContext().getCache().getCachedExecution(entryPoint);
	}

}
