package com.unnsvc.rhena.core.identifier;

import com.unnsvc.rhena.core.exceptions.RhenaParserException;

public class QualifiedIdentifier {

	public static ComponentIdentifier valueOfComponent(String componentIdentifier) throws RhenaParserException {

		Identifier component = Identifier.valueOf(componentIdentifier);
		return new ComponentIdentifier(component) {
		};
	}

	public static ProjectIdentifier valueOfProject(String qualifiedIdentifier) throws RhenaParserException {

		String[] parts = qualifiedIdentifier.split(":");

		if (parts.length == 3) {
			// We have component, project, version

			Identifier component = Identifier.valueOf(parts[0]);
			Identifier project = Identifier.valueOf(parts[1]);
			Version version = Version.valueOf(parts[2]);
			return new ProjectIdentifier(component, project, version) {
			};
		} else if (parts.length == 2) {
			// We don't have a componnent, just project:version

			Identifier project = Identifier.valueOf(parts[0]);
			Version version = Version.valueOf(parts[1]);
			return new ProjectIdentifier(project, version) {
			};
		}

		throw new RhenaParserException("Invalid qualified identifier: " + qualifiedIdentifier);
	}
}
