package com.unnsvc.rhena.common.repository;

import java.net.URI;

public interface IRepositoryDefinition {

	public ERepositoryType getRepositoryType();

	public RepositoryIdentifier getIdentifier();

	public URI getLocation();
}
