
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.core.model.RhenaModule;

public abstract class AbstractRepository implements IRepository {

	private Logger log = LoggerFactory.getLogger(getClass());

	protected RhenaModule resolveModel(ModuleIdentifier moduleIdentifier, URI projectLocationUri) throws RhenaException {

		RhenaModule model = new RhenaModuleParser(moduleIdentifier, projectLocationUri, this).getModel();
		return model;
	}
}
