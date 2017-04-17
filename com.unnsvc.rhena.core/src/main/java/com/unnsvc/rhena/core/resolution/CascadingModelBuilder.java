
package com.unnsvc.rhena.core.resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.ng.execution.IExecutionEdge;
import com.unnsvc.rhena.common.ng.execution.IExecutionModule;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.EExecutionType;
import com.unnsvc.rhena.common.ng.model.ESelectionType;
import com.unnsvc.rhena.common.ng.model.IEntryPoint;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.IRhenaResolver;
import com.unnsvc.rhena.execution.ExecutionEdge;
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
		
		
		log.info("Executing: " + moduleExecutor.getEdges().size() + " relationships.");
		moduleExecutor.execute();
	}

	@Override
	protected void onRelationship(IRhenaModule source, IEntryPoint target) {

		IExecutionModule execSource = new ExecutionModule(source);
		IExecutionModule execTarget = new ExecutionModule(getCache().getModule(target.getTarget()));
		IExecutionEdge execEdge = new ExecutionEdge(execSource, target.getExecutionType(), execTarget);
		moduleExecutor.addEdge(execEdge);
	}

}
