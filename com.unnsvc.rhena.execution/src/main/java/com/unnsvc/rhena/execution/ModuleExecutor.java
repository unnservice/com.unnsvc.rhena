
package com.unnsvc.rhena.execution;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.execution.threading.LimitedQueue;

public class ModuleExecutor extends ThreadPoolExecutor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaCache cache;

	public ModuleExecutor(IRhenaConfiguration config, IRhenaCache cache) {

		super(config.getThreads(), config.getThreads(), 0L, TimeUnit.MILLISECONDS, new LimitedQueue<Runnable>(config.getThreads()));
		this.cache = cache;
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
				cache.cacheExecution(result.getModule().getIdentifier(), result);
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
}
