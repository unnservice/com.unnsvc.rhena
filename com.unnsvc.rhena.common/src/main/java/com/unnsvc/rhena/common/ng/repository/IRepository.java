
package com.unnsvc.rhena.common.ng.repository;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;

public interface IRepository {

	public IRhenaModule resolveModule(ModuleIdentifier moduleIdentifier) throws RhenaException;

	public IRepositoryDefinition getDefinition();

	public void setDefinition(IRepositoryDefinition definition);

}
