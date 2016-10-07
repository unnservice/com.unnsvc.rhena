
package com.unnsvc.rhena.ng.resolution;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.ng.model.RhenaModule;

public interface IRepository {

	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

}
