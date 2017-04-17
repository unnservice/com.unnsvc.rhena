
package com.unnsvc.rhena.execution;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.ng.execution.IExecutionEdge;
import com.unnsvc.rhena.execution.threading.LimitedQueue;

public class ModuleExecutor extends ThreadPoolExecutor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Set<IExecutionEdge> executed;
	private Set<IExecutionEdge> edges;
	private Object lock;

	public ModuleExecutor(int threads) {

		super(threads, threads, 0L, TimeUnit.MILLISECONDS, new LimitedQueue<Runnable>(threads));

		this.lock = new Object();
		this.executed = Collections.synchronizedSet(new HashSet<IExecutionEdge>());
		this.edges = new HashSet<IExecutionEdge>();
	}

	@Override
	protected void afterExecute(Runnable runnable, Throwable throwable) {

		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public void execute() throws RhenaException {

		try {
			debug();
			innerExecute();
		} catch (Throwable t) {
			throw new RhenaException(t);
		}
	}

	private void debug() {

		log.debug("Relationships in executor: " + edges.size());
		for(IExecutionEdge edge : edges) {
			log.debug(edge.toString());
		}
	}

	protected void innerExecute() throws InterruptedException {

		while (!edges.isEmpty()) {
			for (Iterator<IExecutionEdge> iter = edges.iterator(); iter.hasNext();) {
				IExecutionEdge edge = iter.next();

				if (executed.contains(edge)) {

					iter.remove();
				} else if (edge.getTarget().isBuildable()) {

					iter.remove();
					preSubmit(edge);
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
	}

	/**
	 * When the executor is full, the submit method will block
	 * 
	 * @param execmod
	 */
	protected void preSubmit(IExecutionEdge edge) {

		/**
		 * It will either complete or throw an error, either way it must wake up
		 * the main thread
		 */
		submit(edge);
	}

	public void addEdge(IExecutionEdge edge) {

		this.edges.add(edge);
	}

}
