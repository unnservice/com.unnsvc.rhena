
package com.unnsvc.rhena.core.resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionEdge;
import com.unnsvc.rhena.common.execution.IExecutionModule;
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

		this.moduleExecutor = new ModuleExecutor(config.getThreads());
	}

	public void executeModel(EExecutionType type, ModuleIdentifier identifier) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(type, identifier);
		visitTree(entryPoint, ESelectionType.SCOPE);

		moduleExecutor.execute();
	}

	@Override
	protected void onRelationship(IRhenaModule source, IEntryPoint target) {

		IRhenaModule targetModule = getCache().getModule(target.getTarget());

		IExecutionModule execSource = new ExecutionModule(source);
		IExecutionModule execTarget = new ExecutionModule(targetModule);

		ModuleIdentifier sourceIdentifier = execSource.getModule() == null ? null : execSource.getModule().getIdentifier();
		log.debug("onRelationship " + sourceIdentifier + " -> " + execTarget.getModule().getIdentifier());

		/**
		 * Add all up to
		 */
		for (EExecutionType type : EExecutionType.values()) {
			IExecutionEdge execEdge = new ExecutionEdgeWorker(execSource, type, execTarget);
			moduleExecutor.addEdge(execEdge);
			if (type == target.getExecutionType()) {
				break;
			}
		}
	}

}
