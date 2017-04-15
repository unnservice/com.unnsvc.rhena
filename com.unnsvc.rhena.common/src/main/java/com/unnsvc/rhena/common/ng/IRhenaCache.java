
package com.unnsvc.rhena.common.ng;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;

public interface IRhenaCache {

	public IRhenaModule getModule(ModuleIdentifier identifier);

	public void cacheModule(IRhenaModule module) throws RhenaException;
}
