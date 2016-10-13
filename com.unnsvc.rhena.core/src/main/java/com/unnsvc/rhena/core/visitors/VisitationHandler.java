
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface VisitationHandler {

	public boolean canEnter(IRhenaModule module, IRhenaEdge edge) throws RhenaException;
}
