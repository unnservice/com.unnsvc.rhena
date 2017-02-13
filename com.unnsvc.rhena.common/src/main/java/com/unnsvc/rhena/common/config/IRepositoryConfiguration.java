
package com.unnsvc.rhena.common.config;

import java.io.Serializable;
import java.util.List;

public interface IRepositoryConfiguration extends Serializable {

	public void addRepository(IRepositoryDefinition repositoryDefinition);

	public List<IRepositoryDefinition> getRepositories();

	public void addWorkspace(IRepositoryDefinition repositoryDefinition);

	public List<IRepositoryDefinition> getWorkspaces();

}
