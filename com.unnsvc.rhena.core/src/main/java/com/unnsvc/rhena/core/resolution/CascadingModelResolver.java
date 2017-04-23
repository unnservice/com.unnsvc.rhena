
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.traversal.AbstractCachingResolver;
import com.unnsvc.rhena.model.EntryPoint;

public class CascadingModelResolver extends AbstractCachingResolver {

	public CascadingModelResolver(IRhenaContext context) {

		super(context);
	}

	/**
	 * Cache entry points
	 */
	@Override
	protected void onRelationship(IRhenaModule source, IEntryPoint entryPoint) {

		getContext().getCache().cacheEntryPoint(entryPoint);
	}

	public IRhenaModule resolveModuleTree(ModuleIdentifier identifier) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(EExecutionType.TEST, identifier);

		IRhenaModule module = visitTree(entryPoint, ESelectionType.SCOPE);

		return module;
	}
}
