
package com.unnsvc.rhena.repository;

import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.repository.IRepositoryDefinition;

public class WorkspaceRepository extends LocalRepository {

	public WorkspaceRepository(IRepositoryDefinition definition, IRhenaCache cache) {

		super(definition, cache);
	}
}
