package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.core.Constants;
import com.unnsvc.rhena.core.identifier.QualifiedIdentifier;

public class ResolutionRequest {

	private QualifiedIdentifier identifier;
	private String resolver;
	
	public ResolutionRequest(QualifiedIdentifier identifier) {
		
		this(identifier, Constants.DEFAULT_RESOLVER);
	}
	
	public ResolutionRequest(QualifiedIdentifier identifier, String resolver) {
		
		this.identifier = identifier;
		this.resolver = resolver;
	}
}
