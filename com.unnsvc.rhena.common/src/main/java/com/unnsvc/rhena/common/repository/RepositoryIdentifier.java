
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

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RepositoryIdentifier other = (RepositoryIdentifier) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
}
