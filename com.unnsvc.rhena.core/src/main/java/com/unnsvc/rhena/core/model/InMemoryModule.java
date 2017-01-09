
package com.unnsvc.rhena.core.model;

import java.net.URI;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.core.resolution.InMemoryRepository;

public class InMemoryModule extends RhenaModule {

	public InMemoryModule(String identifier, InMemoryRepository repository) throws RhenaException {

		super(ModuleIdentifier.valueOf(identifier), null, repository);
	}

	@Override
	public URI getLocation() {

		throw new UnsupportedOperationException("Not implemented");
	}

}
