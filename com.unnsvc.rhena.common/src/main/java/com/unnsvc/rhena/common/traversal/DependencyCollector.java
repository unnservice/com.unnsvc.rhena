
package com.unnsvc.rhena.common.traversal;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * 
 * @author noname
 *
 */
public class DependencyCollector extends AbstractCachingResolver {

	private IEntryPoint caller;
	private List<IExecutionResult> dependencyList;

	public DependencyCollector(IRhenaContext context, IEntryPoint entryPoint) throws RhenaException {

		super(context);

		this.dependencyList = new ArrayList<IExecutionResult>();
		this.caller = entryPoint;
		visitTree(entryPoint, ESelectionType.SCOPE);
	}

	@Override
	protected void onRelationship(IRhenaModule source, IEntryPoint outgoing) throws RhenaException {

		if (source != null) {

			IExecutionResult result = getContext().getCache().getCachedExecution(outgoing);
			dependencyList.add(result);
		}
	}

	public List<IExecutionResult> getDependencyList() {

		return dependencyList;
	}
}
