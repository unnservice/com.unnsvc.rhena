
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface ModuleVisitationHandler extends VisitationHandler {

	public void startModule(IRhenaModule module) throws RhenaException;

	public void endModule(IRhenaModule module) throws RhenaException;
}
