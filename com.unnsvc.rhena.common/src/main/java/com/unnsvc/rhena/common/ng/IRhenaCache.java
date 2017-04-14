
package com.unnsvc.rhena.common.ng;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;

public interface IRhenaCache {

	public IRhenaModule getModule(ModuleIdentifier identifier);

	public void cacheModule(IRhenaModule module);
}
