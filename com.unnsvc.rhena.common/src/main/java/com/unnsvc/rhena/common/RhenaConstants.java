
package com.unnsvc.rhena.common;

import java.rmi.registry.Registry;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class RhenaConstants {

	public static final String MODULE_DESCRIPTOR_FILENAME = "module.xml";
	public static final String ARTIFACTS_DESCRIPTOR_FILENAME = "artifacts.xml";

	public static final String NS_RHENA_MODULE = "urn:rhena:module";
	public static final String NS_RHENA_DEPENDENCY = "urn:rhena:dependency";
	public static final String NS_RHENA_PROPERTIES = "urn:rhena:properties";
	public static final String NS_RHENA_EXECUTION = "urn:rhena:artifacts";

	public static final String NS_RHENA_SETTINGS = "urn:rhena:settings";

	public static final String DEFAULT_LIFECYCLE_NAME = "default";

	public static final ModuleIdentifier DEFAULT_LIFECYCLE_MODULE = createDefault();

	public static final String DEFAULT_OUTPUT_DIRECTORY_NAME = "target";
	public static final Object RHENA_VERSION = "1.0";
	public static final int DEFAULT_LIFECYCLE_AGENT_PORT = Registry.REGISTRY_PORT;
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String DEFAULT_ARTIFACT_TAG = "default";

	private static ModuleIdentifier createDefault() {

		try {
			return ModuleIdentifier.valueOf("com.unnsvc.rhena:lifecycle:0.0.1");
		} catch (RhenaException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
