
package com.unnsvc.rhena.common.config;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public interface IRepositoryConfiguration extends Serializable {

	public void addRepository(IRepositoryDefinition repositoryDefinition);

	public List<IRepositoryDefinition> getRepositories();

	public void addWorkspace(IRepositoryDefinition repositoryDefinition);

	public void addWorkspace(File workspaceLocation);

	public List<IRepositoryDefinition> getWorkspaces();

	public File getLocalStorageLocation();

}
