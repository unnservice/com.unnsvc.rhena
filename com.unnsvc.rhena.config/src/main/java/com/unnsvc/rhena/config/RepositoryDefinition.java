
package com.unnsvc.rhena.config;

import java.net.URI;

import com.unnsvc.rhena.common.ng.repository.ERepositoryType;
import com.unnsvc.rhena.common.ng.repository.IRepositoryDefinition;
import com.unnsvc.rhena.common.ng.repository.RepositoryIdentifier;

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

		return new RepositoryDefinition(ERepositoryType.LOCAL, new RepositoryIdentifier(identifier), location);
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
}
