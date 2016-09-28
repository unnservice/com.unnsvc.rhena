package com.unnsvc.rhena.core.identifier;

public abstract class QualifiedIdentifier {

//	public static ComponentIdentifier valueOfComponent(String componentIdentifier) throws RhenaParserException {
//
//		Identifier component = Identifier.valueOf(componentIdentifier);
//		return new ComponentIdentifier(component) {
//		};
//	}
//
//	public static ProjectIdentifier valueOfProject(String qualifiedIdentifier) throws RhenaParserException {
//
//		String[] parts = qualifiedIdentifier.split(":");
//
//		if (parts.length == 3) {
//			// We have component, project, version
//
//			Identifier component = Identifier.valueOf(parts[0]);
//			Identifier project = Identifier.valueOf(parts[1]);
//			Version version = Version.valueOf(parts[2]);
//			return new ProjectIdentifier(component, project, version) {
//			};
//		} else if (parts.length == 2) {
//			// We don't have a componnent, just project:version
//
//			Identifier project = Identifier.valueOf(parts[0]);
//			Version version = Version.valueOf(parts[1]);
//			return new ProjectIdentifier(project, version) {
//			};
//		}
//
//		throw new RhenaParserException("Invalid qualified identifier: " + qualifiedIdentifier);
//	}
	
	
	
//	private boolean componentDeclaration;
//	private Identifier component;
//	private Identifier project;
//	private Version version;
	
	/**
	 * 
	 * @param id
	 * @param component true if this is a component identifier
	 * @throws RhenaParserException 
	 */
//	public QualifiedIdentifier(String id, boolean componentDeclaration) throws RhenaParserException {
//		
//		this.componentDeclaration = componentDeclaration;
//		if(componentDeclaration) {
//			parseComponentIdentifier(id.split(":"));
//		} else {
//			parseProjectIdentifier(id.split(":"));
//		}
//	}

//	private void parseProjectIdentifier(String[] parts) throws RhenaParserException {
//
//
//	}
//
//	private void parseComponentIdentifier(String[] parts) throws RhenaParserException {
//
//
//	}
}
