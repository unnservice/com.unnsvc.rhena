package com.unnsvc.rhena.builder.resolvers;

import com.unnsvc.rhena.RhenaContext;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModule;

public interface IRepository {

	public RhenaModule resolveModule(RhenaContext context, ModuleIdentifier moduleIdentifier)throws RhenaException;

}
