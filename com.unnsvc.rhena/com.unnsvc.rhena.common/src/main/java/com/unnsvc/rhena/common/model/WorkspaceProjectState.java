package com.unnsvc.rhena.common.model;


public enum WorkspaceProjectState {

	UNRESOLVED, MODEL, COMPILED, PACKAGED, TESTED, DEPLOYED;
	
	public WorkspaceProjectState moduleStateToWorkspaceProjectState(ModuleState equivalentTo) {
		
		switch(equivalentTo) {
			case UNRESOLVED:
				return WorkspaceProjectState.UNRESOLVED;
			case MODEL:
				return WorkspaceProjectState.MODEL;
			case RESOLVED:
				return WorkspaceProjectState.PACKAGED;
		}
		
		if(equivalentTo.equals(ModuleState.UNRESOLVED)) {
			return WorkspaceProjectState.UNRESOLVED;
		} else if (equivalentTo.equals(ModuleState.MODEL)) {
			return WorkspaceProjectState.MODEL;
		} else {
			return WorkspaceProjectState.PACKAGED;
		}
	}
}
