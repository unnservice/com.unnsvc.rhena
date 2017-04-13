
package com.unnsvc.rhena.common.ng.repository;

import java.net.URI;

public class RepositoryIdentifier {

	private URI location;

	public RepositoryIdentifier(URI location) {

		this.location = location;
	}

	public URI getLocation() {

		return location;
	}

	public void setLocation(URI location) {

		this.location = location;
	}
}
