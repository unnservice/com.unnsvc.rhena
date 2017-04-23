
package com.unnsvc.rhena.common.traversal;

import java.util.Iterator;
import java.util.List;

import com.unnsvc.rhena.common.execution.IExecutionResult;

public class Dependencies implements IDependencies {

	private static final long serialVersionUID = 1L;
	private List<IExecutionResult> dependencyList;

	public Dependencies(List<IExecutionResult> dependencyList) {

		this.dependencyList = dependencyList;
	}

	@Override
	public Iterator<IExecutionResult> iterator() {

		return dependencyList.iterator();
	}

}
