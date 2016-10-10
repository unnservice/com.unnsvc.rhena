
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaModule;

public abstract class AbstractRepository implements IRepository {

	protected RhenaModule resolveModel(ModuleIdentifier moduleIdentifier, URI moduleDescriptorUri) throws RhenaException {

		RhenaModule model = new RhenaModuleParser(moduleIdentifier, moduleDescriptorUri, this).getModel();
		return model;
	}
}
