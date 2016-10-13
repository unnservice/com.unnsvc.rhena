
package com.unnsvc.rhena.lifecycle.resource;

import com.unnsvc.rhena.common.model.lifecycle.IResource;

public class Resource implements IResource {

	private String source;
	private String target;

	public Resource(String source, String target) {

		this.source = source;
		this.target = target;
	}

}
