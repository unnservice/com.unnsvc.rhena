
package com.unnsvc.rhena.core.status;

import com.unnsvc.rhena.common.model.IEntryPoint;

public class BuildStatus {

	private IEntryPoint entryPoint;

	public BuildStatus(IEntryPoint entryPoint) {

		this.entryPoint = entryPoint;
	}

	public IEntryPoint getEntryPoint() {

		return entryPoint;
	}
}
