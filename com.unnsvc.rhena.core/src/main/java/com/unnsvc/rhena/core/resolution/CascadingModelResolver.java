
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.EExecutionType;
import com.unnsvc.rhena.common.ng.model.ESelectionType;
import com.unnsvc.rhena.common.ng.model.IEntryPoint;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.IRhenaResolver;
import com.unnsvc.rhena.model.EntryPoint;

public class CascadingModelResolver extends AbstractCachingResolver {

	public CascadingModelResolver(IRhenaResolver resolver, IRhenaCache cache) {

		super(cache, resolver);
	}

	public IRhenaModule resolveModuleTree(ModuleIdentifier identifier) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(EExecutionType.TEST, identifier);

		IRhenaModule module = visitTree(entryPoint, ESelectionType.SCOPE);

		return module;
	}
}
