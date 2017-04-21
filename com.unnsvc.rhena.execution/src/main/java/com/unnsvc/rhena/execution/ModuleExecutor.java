
package com.unnsvc.rhena.execution;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaBuilder;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.execution.IModuleExecutor;
import com.unnsvc.rhena.common.execution.IModuleExecutorCallback;
import com.unnsvc.rhena.execution.threading.LimitedQueue;

public class ModuleExecutor extends ThreadPoolExecutor implements IModuleExecutor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Set<IModuleExecutorCallback> callbacks;

	public ModuleExecutor(IRhenaConfiguration config) {

		super(config.getThreads(), config.getThreads(), 0L, TimeUnit.MILLISECONDS, new LimitedQueue<Runnable>(config.getThreads()));
		this.callbacks = new HashSet<IModuleExecutorCallback>();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void afterExecute(Runnable runnable, Throwable t) {

		if (t != null) {
			log.error("Exception found in execution result, this shouldn't happen in theory, report bug", t);
		}

		if (runnable instanceof Future<?>) {

			try {
				Future<IExecutionResult> future = (Future<IExecutionResult>) runnable;
				IExecutionResult result = future.get();
				callbacks.forEach(callback -> callback.onExecuted(result));
			} catch (ExecutionException | InterruptedException e) {

				// Abort all executions
				log.error(e.getMessage(), e);
			}
		}
	}

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
