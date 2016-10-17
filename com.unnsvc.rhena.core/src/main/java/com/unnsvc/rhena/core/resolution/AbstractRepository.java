
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ModuleType;
import com.unnsvc.rhena.core.model.RhenaModule;

public abstract class AbstractRepository implements IRepository {

	// private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaContext context;

	public AbstractRepository(IRhenaContext context) {

		this.context = context;
	}

	protected RhenaModule resolveModel(ModuleType moduleType, ModuleIdentifier moduleIdentifier, URI projectLocationUri) throws RhenaException {

		RhenaModule model = new RhenaModuleParser(context, moduleType, moduleIdentifier, projectLocationUri, this).getModel();
		return model;
	}

	public IRhenaContext getContext() {

		return context;
	}
}
