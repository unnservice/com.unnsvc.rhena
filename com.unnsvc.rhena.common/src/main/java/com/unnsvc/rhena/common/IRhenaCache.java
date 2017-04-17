
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IRhenaCache {

	public IRhenaModule getModule(ModuleIdentifier identifier);

	public void cacheModule(IRhenaModule module) throws RhenaException;
}
