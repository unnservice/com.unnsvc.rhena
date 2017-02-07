
package com.unnsvc.rhena.common.settings;

import java.io.Serializable;
import java.util.List;

public interface IRhenaSettings extends Serializable {

	public List<IRepositoryDefinition> getRepositories();

	public List<IRepositoryDefinition> getWorkspaces();

}
