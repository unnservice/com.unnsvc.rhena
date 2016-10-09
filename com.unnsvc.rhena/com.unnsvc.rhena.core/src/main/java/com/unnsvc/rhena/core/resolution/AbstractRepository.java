
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaModel;

public abstract class AbstractRepository implements IRepository {

	protected RhenaModel resolveModel(ModuleIdentifier moduleIdentifier, URI moduleDescriptorUri) throws RhenaException {

		RhenaModel model = new RhenaModuleParser(moduleIdentifier, moduleDescriptorUri, this).getModel();
		return model;
	}
}
