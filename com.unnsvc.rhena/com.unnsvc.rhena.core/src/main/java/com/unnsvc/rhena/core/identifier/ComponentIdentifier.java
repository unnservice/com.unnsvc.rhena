package com.unnsvc.rhena.core.identifier;

public abstract class ComponentIdentifier extends QualifiedIdentifier {

	private Identifier component;

	ComponentIdentifier(Identifier component) {

		this.component = component;
	}

	public Identifier getComponent() {
		return component;
	}

	@Override
	public String toString() {

		return component.toString();
	}
}
