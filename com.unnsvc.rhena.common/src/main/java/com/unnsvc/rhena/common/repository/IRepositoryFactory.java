package com.unnsvc.rhena.common.repository;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IRepositoryFactory {

	public IRepository createRepository(IRhenaContext context, IRepositoryDefinition definition) throws RhenaException;

}
