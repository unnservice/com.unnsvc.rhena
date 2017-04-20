
package com.unnsvc.rhena.core.resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.model.EntryPoint;

public class CascadingModelBuilder2 extends AbstractCachingResolver {

	private Logger log = LoggerFactory.getLogger(getClass());

	public CascadingModelBuilder2(IRhenaConfiguration config, IRhenaCache cache, IRhenaResolver resolver) {

		super(cache, resolver);
	}

	@Override
	protected void onModuleResolved(IEntryPoint resolvedEntryPoint) {

		log.info("onModuleResolved: " + resolvedEntryPoint);
		
		/**
		 * By the time this is called, all of its relationships will have been
		 * resolved already and onRelationship() calls issued
		 * 
		 * But what we really want, are the relationships of the modules
		 * targeting this module, so we know which execution type we want to
		 * build.
		 */
	}

	@Override
	protected void onRelationship(IRhenaModule source, IEntryPoint entryPoint) {

		/**
		 * 
		 */
		log.info("\tonRelationship " + entryPoint.getTarget());
	}

	public IExecutionResult executeModel(EExecutionType type, ModuleIdentifier identifier) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(type, identifier);

		visitTree(entryPoint, ESelectionType.SCOPE);

		return getCache().getCachedExecution(entryPoint);
	}

}
