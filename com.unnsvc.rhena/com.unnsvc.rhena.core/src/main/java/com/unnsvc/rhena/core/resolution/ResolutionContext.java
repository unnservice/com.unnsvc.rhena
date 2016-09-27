package com.unnsvc.rhena.core.resolution;

public class ResolutionContext {

	private ResolutionEngine engine;

	public ResolutionContext(ResolutionEngine engine) {
		
		this.engine = engine;
	}

	public ResolutionEngine getEngine() {
		return engine;
	}
}
