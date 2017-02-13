
package com.unnsvc.rhena.common.config;

import java.util.List;

public interface IRepositoryConfiguration {

	public void addRepository(IRepositoryDefinition repositoryDefinition);

	public List<IRepositoryDefinition> getRepositories();

	public void addWorkspace(IRepositoryDefinition repositoryDefinition);

	public List<IRepositoryDefinition> getWorkspaces();

}
