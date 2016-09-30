package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.core.identifier.QualifiedIdentifier;

public interface RhenaEdge <T extends QualifiedIdentifier> {
	
	public T getIdentifier();
}
