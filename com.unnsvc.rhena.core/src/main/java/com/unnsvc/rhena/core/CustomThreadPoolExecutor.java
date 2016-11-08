
package com.unnsvc.rhena.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Custom thread pool executor which keeps track of active jobs
 * 
 * @author noname
 *
 */
public class CustomThreadPoolExecutor extends ThreadPoolExecutor {

	private Set<Runnable> executing;

	public CustomThreadPoolExecutor(int poolSize) {

		super(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		this.executing = Collections.synchronizedSet(new HashSet<Runnable>());
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {

		super.beforeExecute(t, r);
		this.executing.add(r);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {

		super.afterExecute(r, t);
		this.executing.remove(r);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {

		return super.submit(task);
	}
	
	public boolean isExecuting() {
		
		return !executing.isEmpty();
	}
}
