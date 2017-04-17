
package com.unnsvc.rhena.execution.threading;

import java.util.concurrent.FutureTask;

import com.unnsvc.rhena.common.execution.IExecutionEdge;

public class RhenaFutureTask extends FutureTask<Object> {

	private IExecutionEdge worker;

	public RhenaFutureTask(IExecutionEdge worker) {

		super(worker);
		this.worker = worker;
	}

	public IExecutionEdge getWorker() {

		return worker;
	}
}
