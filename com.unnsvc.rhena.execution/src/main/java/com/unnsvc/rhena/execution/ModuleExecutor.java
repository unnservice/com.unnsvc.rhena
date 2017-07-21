
package com.unnsvc.rhena.execution;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaBuilder;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.common.execution.IModuleExecutor;
import com.unnsvc.rhena.common.execution.IModuleExecutorCallback;

public class ModuleExecutor extends ThreadPoolExecutor implements IModuleExecutor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Set<IModuleExecutorCallback> callbacks;

	public ModuleExecutor(IRhenaContext context) {

		super(context.getConfig().getAgentConfiguration().getThreads(), context.getConfig().getAgentConfiguration().getThreads(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		this.callbacks = new HashSet<IModuleExecutorCallback>();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void afterExecute(Runnable runnable, Throwable t) {

		if (t != null) {
			log.error("Exception found in execution result, this shouldn't happen in theory, report bug", t);
			callbacks.forEach(callback -> callback.onException(t));
		}

		if (runnable instanceof Future<?>) {

			try {
				Future<IExecutionResponse> future = (Future<IExecutionResponse>) runnable;
				IExecutionResponse result = future.get();
				callbacks.forEach(callback -> callback.onExecuted(result));
			} catch (Throwable e) {

				// Abort all executions
				log.error(e.getMessage(), e);
				callbacks.forEach(callback -> callback.onException(e));
			}
		}
	}

	@Override
	public void close() throws RhenaException {

		try {
			shutdown();
			awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException ex) {
			throw new RhenaException(ex);
		}
	}

	@Override
	public void submit(IRhenaBuilder builder) {

		super.submit(builder);
	}

	@Override
	public void addCallback(IModuleExecutorCallback callback) {

		this.callbacks.add(callback);
	}
}
