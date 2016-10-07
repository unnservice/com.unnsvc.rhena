
package com.unnsvc.rhena.builder.resolvers;

import com.unnsvc.rhena.builder.CompositeScope;
import com.unnsvc.rhena.builder.RhenaContext;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModuleDescriptor;

public interface RhenaMaterialiser {

	public RhenaModuleDescriptor materialiseScope(RhenaContext context, CompositeScope scope, ModuleIdentifier moduleIdentifier) throws RhenaException;

}
