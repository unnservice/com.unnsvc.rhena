package com.unnsvc.rhena.common.model;


public enum ModuleState {

	MODEL, COMPILED, PACKAGED, TESTED, DEPLOYED;
	
	public String toLabel() {
		
		return toString().toLowerCase();
	}
}
