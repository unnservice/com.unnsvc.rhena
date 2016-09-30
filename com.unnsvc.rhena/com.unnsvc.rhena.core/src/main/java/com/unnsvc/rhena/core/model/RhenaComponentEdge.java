
package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.core.identifier.ComponentIdentifier;

public class RhenaComponentEdge implements RhenaEdge<ComponentIdentifier> {

	private ComponentIdentifier identifier;
	private RhenaComponent component;

	public RhenaComponentEdge(ComponentIdentifier identifier) {

		this.identifier = identifier;
	}

	@Override
	public ComponentIdentifier getIdentifier() {

		return identifier;
	}

	public void setComponent(RhenaComponent component) {

		this.component = component;
	}

	public RhenaComponent getComponent() {

		return component;
	}

}
