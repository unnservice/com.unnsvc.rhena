
package com.unnsvc.rhena.common.config;

import java.io.Serializable;
import java.net.URI;

public class RepositoryDefinition implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private URI uri;
	private String type;

	public RepositoryDefinition(String name, URI uri, String type) {

		this.name = name;
		this.uri = uri;
		this.type = type;
	}

	public String getName() {

		return name;
	}

	public URI getUri() {

		return uri;
	}

	public String getType() {

		return type;
	}
}
