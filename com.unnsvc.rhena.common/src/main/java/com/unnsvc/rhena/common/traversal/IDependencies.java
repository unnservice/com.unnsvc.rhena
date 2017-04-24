
package com.unnsvc.rhena.common.traversal;

import java.io.Serializable;
import java.util.List;

import com.unnsvc.rhena.common.execution.IExecutionResult;

public interface IDependencies extends Serializable, Iterable<IExecutionResult> {

	/**
	 * Merge other into this
	 * 
	 * @param dependencies
	 */
	public void merge(IDependencies dependencies);

	public List<IExecutionResult> getDependencyList();

}
