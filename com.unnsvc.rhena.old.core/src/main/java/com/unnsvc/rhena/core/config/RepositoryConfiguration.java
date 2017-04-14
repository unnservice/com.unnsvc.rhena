
package com.unnsvc.rhena.core.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.config.IRepositoryDefinition;

public class RepositoryConfiguration implements IRepositoryConfiguration {

	private static final long serialVersionUID = 1L;
	private File localStorageLocation;
	private List<IRepositoryDefinition> repositories;
	private List<IRepositoryDefinition> workspaces;

	public RepositoryConfiguration() {

		this.repositories = new ArrayList<IRepositoryDefinition>();
		this.workspaces = new ArrayList<IRepositoryDefinition>();
	}

	@Override
	public List<IRepositoryDefinition> getRepositories() {

		return repositories;
	}

	@Override
	public void addRepository(IRepositoryDefinition repositoryDefinition) {

		if (!repositories.contains(repositoryDefinition)) {
			repositories.add(repositoryDefinition);
		}
	}

	@Override
	public List<IRepositoryDefinition> getWorkspaces() {

		return workspaces;
	}

	@Override
	public void addWorkspace(IRepositoryDefinition repositoryDefinition) {

		if (!workspaces.contains(repositoryDefinition)) {
			workspaces.add(repositoryDefinition);
		}
	}

	@Override
	public void addWorkspace(File workspaceLocation) {

		if (workspaceLocation.exists()) {
			IRepositoryDefinition def = new RepositoryDefinition(workspaceLocation.getAbsoluteFile().toURI());
			if (!workspaces.contains(def)) {
				workspaces.add(def);
			}
		}
	}

	@Override
	public File getLocalStorageLocation() {

		return localStorageLocation;
	}

}
