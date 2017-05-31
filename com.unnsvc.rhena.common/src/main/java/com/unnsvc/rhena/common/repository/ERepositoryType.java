
package com.unnsvc.rhena.common.repository;

public enum ERepositoryType {

	WORKSPACE("Workspace"), CACHE("Cache"), REMOTE("Remote");

	private String title;

	private ERepositoryType(String title) {

		this.title = title;
	}

	@Override
	public String toString() {

		return title;
	}
}
