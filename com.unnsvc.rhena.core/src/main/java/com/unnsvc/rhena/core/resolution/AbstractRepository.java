
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaContext;

public abstract class AbstractRepository implements IRepository {

	// private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaContext context;

	public AbstractRepository(IRhenaContext context) {

		this.context = context;
	}

//	protected RhenaModule resolveModel(ModuleType moduleType, ModuleIdentifier moduleIdentifier, URI projectLocationUri) throws RhenaException {
//
//		RhenaModule model = new RhenaModuleParser(context, moduleType, moduleIdentifier, projectLocationUri, this).getModel();
//		return model;
//	}

	public IRhenaContext getContext() {

		return context;
	}
}
