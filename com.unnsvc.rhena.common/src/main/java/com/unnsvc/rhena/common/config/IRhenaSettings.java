
package com.unnsvc.rhena.common.config;

import java.io.Serializable;
import java.util.List;

public interface IRhenaSettings extends Serializable {

	public List<RepositoryDefinition> getRepositoryDefinitions();

}
