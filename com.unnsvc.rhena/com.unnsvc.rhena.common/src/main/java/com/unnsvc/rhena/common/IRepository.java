
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ModuleState;
import com.unnsvc.rhena.common.model.RhenaModule;

public interface IRepository {

	public RhenaModule materialiseState(ModuleIdentifier moduleIdentifier, ModuleState moduleState) throws RhenaException;

}
