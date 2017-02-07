
package com.unnsvc.rhena.core.settings;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.settings.IRepositoryDefinition;
import com.unnsvc.rhena.common.settings.IRhenaSettings;

public class RhenaSettings implements IRhenaSettings {

	private static final long serialVersionUID = 1L;
	private List<IRepositoryDefinition> repositories;
	private List<IRepositoryDefinition> workspaces;

	public RhenaSettings() {

		this.repositories = new ArrayList<IRepositoryDefinition>();
		this.workspaces = new ArrayList<IRepositoryDefinition>();
	}

	@Override
	public List<IRepositoryDefinition> getRepositories() {

		return repositories;
	}

	@Override
	public List<IRepositoryDefinition> getWorkspaces() {

		return workspaces;
	}
}
