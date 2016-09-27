package com.unnsvc.rhena.core.identifier;

import com.unnsvc.rhena.core.exceptions.RhenaParserException;

public class QualifiedIdentifier {

	private Identifier component;
	private Identifier project;
	private Version version;

	private QualifiedIdentifier(Identifier component, Identifier project, Version version) {

		this.component = component;
		this.project = project;
		this.version = version;
	}

	public Identifier getComponent() {
		return component;
	}

	public Identifier getProject() {
		return project;
	}

	public Version getVersion() {
		return version;
	}
	
	@Override
	public String toString() {
		
		return (component == null ? "" : component.toString() + ":") + project.toString() + ":" + version.toString();
	}

	private QualifiedIdentifier(Identifier project, Version version) {

		this(null, project, version);
	}

	public static QualifiedIdentifier valueOf(String qualifiedIdentifier) throws RhenaParserException {

		String[] parts = qualifiedIdentifier.split(":");

		if (parts.length == 3) {
			// We have component, project, version

			Identifier component = Identifier.valueOf(parts[0]);
			Identifier project = Identifier.valueOf(parts[1]);
			Version version = Version.valueOf(parts[2]);
			return new QualifiedIdentifier(component, project, version);
		} else if (parts.length == 2) {
			// We don't have a componnent, just project:version

			Identifier project = Identifier.valueOf(parts[0]);
			Version version = Version.valueOf(parts[1]);
			return new QualifiedIdentifier(project, version);
		}

		throw new RhenaParserException("Invalid qualified identifier: " + qualifiedIdentifier);
	}
}
