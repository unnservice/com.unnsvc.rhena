
package com.unnsvc.rhena.core.settings;

import java.net.URI;

import com.unnsvc.rhena.common.settings.IRepositoryDefinition;

public class RepositoryDefinition implements IRepositoryDefinition {

	private String name;
	private URI location;

	public RepositoryDefinition(String name, URI location) {

		this.name = name;
		this.location = location;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public URI getLocation() {

		return location;
	}
}
