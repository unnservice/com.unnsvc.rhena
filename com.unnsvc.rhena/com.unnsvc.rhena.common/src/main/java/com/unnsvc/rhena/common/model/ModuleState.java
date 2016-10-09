package com.unnsvc.rhena.common.model;


public enum ModuleState {
	
	UNRESOLVED, MODEL, RESOLVED;
		
	public String toLabel() {
		
		return toString().toLowerCase();
	}
}
