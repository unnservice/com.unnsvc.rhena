
package com.unnsvc.rhena.common.ng.repository;

import java.net.URI;

public class RepositoryIdentifier {

	private String identifier;

	public RepositoryIdentifier(String identifier) {

		this.identifier = identifier;
	}

	public String getIdentifier() {

		return identifier;
	}

	public void setIdentifier(String identifier) {

		this.identifier = identifier;
	}

	@Override
	public String toString() {

		return "RepositoryIdentifier [identifier=" + identifier + "]";
	}
}
