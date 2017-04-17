package com.unnsvc.rhena.common.repository;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IRhenaResolver {

	public IRhenaModule resolveModule(ModuleIdentifier identifier) throws RhenaException;

}
