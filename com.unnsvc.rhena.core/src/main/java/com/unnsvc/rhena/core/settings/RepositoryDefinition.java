
package com.unnsvc.rhena.core.settings;

import java.net.URI;

import com.unnsvc.rhena.common.settings.IRepositoryDefinition;

public class RepositoryDefinition implements IRepositoryDefinition {

	private URI location;

	public RepositoryDefinition(URI location) {

		this.location = location;
	}

	@Override
	public URI getLocation() {

		return location;
	}
}
