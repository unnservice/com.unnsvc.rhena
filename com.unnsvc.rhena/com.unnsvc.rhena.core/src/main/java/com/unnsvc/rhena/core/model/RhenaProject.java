package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.core.identifier.Version;

public class RhenaProject implements RhenaNode {

	private RhenaComponentDescriptor componentDescriptor;
	private String name;
	private Version version;

	public RhenaProject(RhenaComponentDescriptor componentDescriptor, String projectName) {

		this.componentDescriptor = componentDescriptor;
		this.name = projectName;
	}

	public String getName() {

		return name;
	}

	public Version getVersion() {

		if(this.version != null) {
			
			return version;
		}
		return componentDescriptor.getVersion();
	}

}
