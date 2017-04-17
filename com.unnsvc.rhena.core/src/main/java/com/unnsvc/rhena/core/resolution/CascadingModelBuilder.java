
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.EExecutionType;
import com.unnsvc.rhena.common.ng.repository.IRhenaResolver;

/**
 * This class will first create a simplified model before executing it in
 * parallel
 * 
 * @author noname
 *
 */
public class CascadingModelBuilder extends AbstractCachingResolver {

	public CascadingModelBuilder(IRhenaCache cache, IRhenaResolver resolver) {

		super(cache, resolver);
	}

	public void executeModel(EExecutionType type, ModuleIdentifier identifier) {
		
		
	}

}
