
package com.unnsvc.rhena.core.resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionEdge;
import com.unnsvc.rhena.common.execution.IExecutionModule;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.execution.ExecutionEdgeWorker;
import com.unnsvc.rhena.execution.ExecutionModule;
import com.unnsvc.rhena.execution.ModuleExecutor;
import com.unnsvc.rhena.model.EntryPoint;

/**
 * This class will first create a simplified model before executing it in
 * parallel
 * 
 * @author noname
 *
 */
public class CascadingModelBuilder extends AbstractCachingResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ModuleExecutor moduleExecutor;

	public CascadingModelBuilder(IRhenaConfiguration config, IRhenaCache cache, IRhenaResolver resolver) {

		super(cache, resolver);

		this.moduleExecutor = new ModuleExecutor(config, cache);
	}

	public IExecutionResult executeModel(EExecutionType type, ModuleIdentifier identifier) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(type, identifier);

		visitTree(entryPoint, ESelectionType.SCOPE);

		moduleExecutor.execute();

		return getCache().getCachedExecution(entryPoint);
	}
	
	@Override
	protected void onRelationship(IRhenaModule source, IEntryPoint target) {
	
		
		log.debug("Relationship: " + (source == null ? "(caller)" : source.getIdentifier()) + " -> " + target);
	}

	protected void onRelationship2(IRhenaModule source, IEntryPoint target) {

		IRhenaModule targetModule = getCache().getModule(target.getTarget());

		// sources reused, targets always new
		IExecutionModule execSource = moduleExecutor.executionModule(source);
		// new ExecutionModule(source);

		/**
		 * If the requested execution type is greater than MAIN, add
		 * dependencies on execution types that go first
		 */
		for (EExecutionType type : EExecutionType.values()) {

			// sources reused, targets always new
			IExecutionModule execTarget = new ExecutionModule(targetModule);

			if (type.equals(target.getExecutionType())) {
				IExecutionEdge execEdge = new ExecutionEdgeWorker(execSource, type, execTarget);
				execSource.addEdge(execEdge);
				moduleExecutor.addEdge(execEdge);
				break;
			} else {
				IExecutionModule newSource = moduleExecutor.executionModule(execTarget.getModule());
				IExecutionEdge execEdge = new ExecutionEdgeWorker(newSource, type, execTarget);
				execTarget.addEdge(execEdge);
				moduleExecutor.addEdge(execEdge);
			}
		}
	}

}
