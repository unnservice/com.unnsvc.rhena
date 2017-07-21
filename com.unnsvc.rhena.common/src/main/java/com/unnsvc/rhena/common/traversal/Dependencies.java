
package com.unnsvc.rhena.common.traversal;

import java.util.Iterator;
import java.util.List;

import com.unnsvc.rhena.common.execution.IExecutionResponse;

public class Dependencies implements IDependencies {

	private static final long serialVersionUID = 1L;
	private List<IExecutionResponse> dependencyList;

	public Dependencies(List<IExecutionResponse> dependencyList) {

		this.dependencyList = dependencyList;
	}

	@Override
	public Iterator<IExecutionResponse> iterator() {

		return dependencyList.iterator();
	}

	/**
	 * @TODO merge properly, we will just append dependencies for now, nevermind
	 *       duplicates and version selection
	 */
	@Override
	public void merge(IDependencies dependencies) {

		dependencyList.addAll(dependencies.getDependencyList());
	}

	@Override
	public List<IExecutionResponse> getDependencyList() {

		return dependencyList;
	}
}
