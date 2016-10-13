package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface EdgeVisitationHandler  extends VisitationHandler {

	public void handleEdge(IRhenaModule module, IRhenaEdge edge) throws RhenaException;
}
