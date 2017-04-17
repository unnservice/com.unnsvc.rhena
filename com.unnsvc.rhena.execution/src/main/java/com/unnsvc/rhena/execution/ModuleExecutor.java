
package com.unnsvc.rhena.execution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.unnsvc.rhena.common.ng.execution.IExecutionEdge;
import com.unnsvc.rhena.common.ng.execution.IExecutionModule;
import com.unnsvc.rhena.execution.threading.LimitedQueue;

public class ModuleExecutor extends ThreadPoolExecutor {

	private List<IExecutionModule> executed;
	private List<IExecutionEdge> edges;
	private Lock lock;

	public ModuleExecutor(int threads) {

		super(threads, threads, 0L, TimeUnit.MILLISECONDS, new LimitedQueue<Runnable>(threads));

		this.lock = new ReentrantLock();
		this.executed = new ArrayList<IExecutionModule>();
		this.edges = new ArrayList<IExecutionEdge>();
	}

	@Override
	protected void afterExecute(Runnable runnable, Throwable throwable) {

		lock.unlock();
	}

	public void execute() {

		while (!edges.isEmpty()) {
			for (Iterator<IExecutionEdge> iter = edges.iterator(); iter.hasNext();) {
				IExecutionEdge edge = iter.next();

				if (edge.getTarget().isBuildable()) {
					iter.remove();
					preSubmit(edge.getTarget());
				}
			}
		}
	}

	/**
	 * When the executor is full, the submit method will block
	 * 
	 * @param execmod
	 */
	protected void preSubmit(IExecutionModule execmod) {

		submit(execmod);
	}

	public void addEdge(IExecutionEdge edge) {

		this.edges.add(edge);
	}
}
