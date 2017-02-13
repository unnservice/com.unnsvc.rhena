
package com.unnsvc.rhena.core.config;

import java.net.URI;

import com.unnsvc.rhena.common.config.IRepositoryDefinition;

public class RepositoryDefinition implements IRepositoryDefinition {

	private URI location;

	public RepositoryDefinition(URI location) {

		this.location = location;
	}

	@Override
	public URI getLocation() {

		return location;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
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
		RepositoryDefinition other = (RepositoryDefinition) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

}
