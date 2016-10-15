
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.core.model.RhenaModule;

public abstract class AbstractRepository implements IRepository {

	// private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;

	public AbstractRepository(IResolutionContext context) {

		this.context = context;
	}

	protected RhenaModule resolveModel(ModuleIdentifier moduleIdentifier, URI projectLocationUri) throws RhenaException {

		RhenaModule model = new RhenaModuleParser(context, moduleIdentifier, projectLocationUri, this).getModel();
		return model;
	}

	public IResolutionContext getContext() {

		return context;
	}
}
