
package com.unnsvc.rhena.common.settings;

import java.util.List;

public interface IRhenaSettings {

	public List<IRepositoryDefinition> getRepositories();

	public List<IRepositoryDefinition> getWorkspaces();

}
