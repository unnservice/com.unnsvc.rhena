
package com.unnsvc.rhena.common.traversal;

import java.io.Serializable;
import java.util.List;

import com.unnsvc.rhena.common.execution.IExecutionResponse;

public interface IDependencies extends Serializable, Iterable<IExecutionResponse> {

	/**
	 * Merge other into this
	 * 
	 * @param dependencies
	 */
	public void merge(IDependencies dependencies);

	public List<IExecutionResponse> getDependencyList();

}
