
package com.unnsvc.rhena.config;

import java.net.URI;

import com.unnsvc.rhena.common.repository.ERepositoryType;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;

public class RepositoryDefinition implements IRepositoryDefinition {

	private ERepositoryType repositoryType;
	private RepositoryIdentifier identifier;
	private URI location;

	public RepositoryDefinition(ERepositoryType repositoryType, RepositoryIdentifier identifier, URI location) {

		this.repositoryType = repositoryType;
		this.identifier = identifier;
		this.location = location;
	}

	public static IRepositoryDefinition newLocal(String identifier, URI location) {

		return new RepositoryDefinition(ERepositoryType.CACHE, new RepositoryIdentifier(identifier), location);
	}

	public static IRepositoryDefinition newWorkspace(String identifier, URI location) {

		return new RepositoryDefinition(ERepositoryType.WORKSPACE, new RepositoryIdentifier(identifier), location);
	}

	public static IRepositoryDefinition newRemote(String identifier, URI location) {

		return new RepositoryDefinition(ERepositoryType.REMOTE, new RepositoryIdentifier(identifier), location);
	}

	@Override
	public ERepositoryType getRepositoryType() {

		return repositoryType;
	}

	@Override
	public RepositoryIdentifier getIdentifier() {

		return identifier;
	}

	@Override
	public URI getLocation() {

		return location;
	}

	@Override
	public String toString() {

		return "RepositoryDefinition [repositoryType=" + repositoryType + ", identifier=" + identifier + ", location=" + location + "]";
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((repositoryType == null) ? 0 : repositoryType.hashCode());
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
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (repositoryType != other.repositoryType)
			return false;
		return true;
	}

}
