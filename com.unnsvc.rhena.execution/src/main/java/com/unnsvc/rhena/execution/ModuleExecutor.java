
package com.unnsvc.rhena.execution;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionEdge;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.execution.threading.LimitedQueue;
import com.unnsvc.rhena.execution.threading.RhenaFutureTask;
import com.unnsvc.rhena.model.EntryPoint;

/**
 * This class is considered consumed after one execution
 * 
 * @author noname
 *
 */
public class ModuleExecutor extends ThreadPoolExecutor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaCache cache;
	private Set<IExecutionEdge> executed;
	private Set<IExecutionEdge> edges;
	private Object lock;
	private AtomicReference<Throwable> errorState;

	public ModuleExecutor(IRhenaConfiguration config, IRhenaCache cache) {

		super(config.getThreads(), config.getThreads(), 0L, TimeUnit.MILLISECONDS, new LimitedQueue<Runnable>(config.getThreads()));

		this.cache = cache;
		this.lock = new Object();
		this.executed = Collections.synchronizedSet(new HashSet<IExecutionEdge>());
		this.edges = new HashSet<IExecutionEdge>();
		this.errorState = new AtomicReference<Throwable>();
	}

	public void execute() throws RhenaException {

		try {
			debug();
			innerExecute();
		} catch (InterruptedException t) {
			throw new RhenaException(t);
		}
	}

	private void debug() {

		log.debug("Relationships in executor: " + edges.size());
		for (IExecutionEdge edge : edges) {
			log.debug(edge.toString());
		}
	}

	protected void innerExecute() throws InterruptedException, RhenaException {

		while (!edges.isEmpty() && errorState.get() == null) {
			for (Iterator<IExecutionEdge> iter = edges.iterator(); iter.hasNext();) {
				IExecutionEdge edge = iter.next();

				if (executed.contains(edge)) {

					iter.remove();
				} else if (edge.getTarget().isBuildable()) {

					iter.remove();
					preSubmit(edge);
				}

				/**
				 * Don't continue submitting work for execution if error state
				 */
				if (errorState.get() != null) {
					break;
				}

				// here by the time it goes through t remainder of the iterator
				// for a very large list, the threads might finish before we
				// reach wait()
				// wait blocks forever
			}

			/**
			 * @TODO need an additional guard here before wait() so it doesn't
			 *       block forever
			 */

			/**
			 * Lock and wait for a completion to wake us up
			 */
			synchronized (lock) {
				lock.wait();
			}
		}

		shutdown();
		awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

		if (errorState.get() != null) {
			throw new RhenaException("Worker execution resulted in error state", errorState.get());
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	protected void afterExecute(Runnable runnable, Throwable throwable) {

		if (throwable != null) {
			log.error(throwable.getMessage(), throwable);
			errorState.set(throwable);
		}

		if (runnable instanceof Future<?>) {
			try {
				Future<IExecutionResult> future = (Future<IExecutionResult>) runnable;

				IExecutionResult result = future.get();
				IEntryPoint entryPoint = new EntryPoint(result.getType(), result.getIdentifier());
				cache.cacheExecution(entryPoint, result);

				/**
				 * @TODO result eneds to be an execution result which we add to
				 *       the cache?
				 */
			} catch (Exception ex) {

				errorState.set(ex);
			}
		}

		synchronized (lock) {
			lock.notifyAll();
		}
	}

	/**
	 * When the executor is full, the submit method will block
	 * 
	 * @param execmod
	 */
	protected void preSubmit(IExecutionEdge edge) {

		/**
		 * It will either complete or throw an error, either way it must wake up
		 * the main thread once the submit() has completed, this is achieved
		 * through afterExecute()
		 */
		submit(new RhenaFutureTask(edge));
	}

	public void addEdge(IExecutionEdge edge) {

		this.edges.add(edge);
	}
}
