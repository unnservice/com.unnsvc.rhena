
package com.unnsvc.rhena.common.repository;

import java.io.Serializable;

public class RepositoryIdentifier implements Serializable {

	private static final long serialVersionUID = 1L;
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
