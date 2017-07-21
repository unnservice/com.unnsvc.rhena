
package com.unnsvc.rhena.common.traversal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * 
 * @author noname
 *
 */
public class DependencyCollector extends AbstractCachingResolver {

	// private IEntryPoint caller;
	private List<IExecutionResponse> dependencyList;

	public DependencyCollector(IRhenaContext context, IEntryPoint entryPoint) throws RhenaException {

		super(context);

		this.dependencyList = new ArrayList<IExecutionResponse>();
		// this.caller = entryPoint;
		visitTree(entryPoint, ESelectionType.SCOPE);
	}

	@Override
	protected void onRelationship(IRhenaModule source, IEntryPoint outgoing) throws RhenaException {

		if (source != null) {

			IExecutionResponse result = getContext().getCache().getCachedExecution(outgoing);
			
			for(Iterator<IExecutionResponse> iter = dependencyList.iterator(); iter.hasNext();) {
				
				IExecutionResponse next = iter.next();
				if(next.getEntryPoint().equals(outgoing)) {
					iter.remove();
				}
			}
			
			dependencyList.add(result);
		}
	}

	/**
	 * Create a new object for results because we can't serialize the actual
	 * collector
	 * 
	 * @return
	 */
	public IDependencies toDependencyChain() {

		return new Dependencies(dependencyList);
	}
}
