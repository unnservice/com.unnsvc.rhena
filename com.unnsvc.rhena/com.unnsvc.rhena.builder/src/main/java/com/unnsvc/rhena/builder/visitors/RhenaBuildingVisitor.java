
package com.unnsvc.rhena.builder.visitors;

import com.unnsvc.rhena.builder.RhenaContext;
import com.unnsvc.rhena.builder.model.RhenaModule;
import com.unnsvc.rhena.builder.model.Scope;

public class RhenaBuildingVisitor implements IVisitor {

	private RhenaContext context;
	private Scope scope;

	public RhenaBuildingVisitor(RhenaContext context, Scope scope) {

		this.context = context;
		this.scope = scope;
	}

	@Override
	public void visiting(RhenaModule module) {

		
	}
}
