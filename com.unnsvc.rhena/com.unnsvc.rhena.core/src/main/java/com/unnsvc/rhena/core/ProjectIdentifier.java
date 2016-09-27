package com.unnsvc.rhena.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectIdentifier implements Comparable<ProjectIdentifier> {

	private String componentName;
	private String projectName;
	private Version version;

	public static final String IDENTIFIER = "[a-Z0-9](\\.[a-Z0-9])*";
	public static final String VERSION = "[0-9]*\\.[0-9]*\\.[0-9]*(-SNAPSHOT)?";
	
	public ProjectIdentifier(String componentName, String projectName, Version version) {

		this.componentName = componentName;
		this.projectName = projectName;
		this.version = version;
	}

	public static ProjectIdentifier parse(String identifierString) {

		Pattern p = Pattern.compile("(" + IDENTIFIER + "):([a-z\\.]*):([0-9]*\\.[0-9]*\\.[0-9]*)");
		Matcher m = p.matcher(identifierString);
//		if (m.find() || m.groupCount()) {
//
//			String componentName = m.group(1);
//			String projectName = m.group(2);
//			String versionStr = m.group(3);
//
//			Version version = Version.parse(versionStr);
//			return new ProjectIdentifier(componentName, projectName, version);
//		}
		throw new IllegalArgumentException("Not a valid project identifier: " + identifierString);
	}

	@Override
	public int compareTo(ProjectIdentifier o) {

		throw new UnsupportedOperationException("Not implemented");
	}
}
