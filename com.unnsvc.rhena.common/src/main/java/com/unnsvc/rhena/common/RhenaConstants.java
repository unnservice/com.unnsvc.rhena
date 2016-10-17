
package com.unnsvc.rhena.common;

import java.io.File;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class RhenaConstants {

	public static final String MODULE_DESCRIPTOR_FILENAME = "module.xml";
	public static final String EXECUTION_DESCRIPTOR_FILENAME = "execution.xml";
	
	public static final String NS_RHENA_MODULE = "urn:rhena:module";
	public static final String NS_RHENA_DEPENDENCY = "urn:rhena:dependency";
	public static final String NS_RHENA_PROPERTIES = "urn:rhena:properties";
	public static final String NS_RHENA_EXECUTION = "urn:rhena:execution";
	
	public static final String DEFAULT_LIFECYCLE_NAME = "default";

	public static final ModuleIdentifier DEFAULT_LIFECYCLE_MODULE = createDefault();
	
	public static final File LOCAL_REPOSITORY_PATH = new File(System.getProperty("user.home"), ".rhena/repository");

	private static ModuleIdentifier createDefault() {

		try {
			return ModuleIdentifier.valueOf("com.unnsvc.rhena:lifecycle:0.0.1");
		} catch (RhenaException e) {
			throw new IllegalArgumentException(e);
		}
	}

	// public static final ILifecycleDeclaration defaultLifecycle =
	// createDefaultLifecycle();

	// private static ILifecycleDeclaration createDefaultLifecycle() throws
	// RhenaException {
	//
	// LifecycleDeclaration decl = new
	// LifecycleDeclaration(RhenaConstants.DEFAULT_LIFECYCLE_NAME);
	//
	// decl.setContext(new ContextReference(getDefaultProcessorEdge(),
	// DefaultContext.class.getName(), null, Utils.newEmptyDocument()));
	// decl.addProcessor(new ProcessorReference(getDefaultProcessorEdge(),
	// DefaultProcessor.class.getName(), null, Utils.newEmptyDocument()));
	// decl.setGenerator(new GeneratorReference(getDefaultProcessorEdge(),
	// DefaultGenerator.class.getName(), null, Utils.newEmptyDocument()));
	//
	// return decl;
	// }
	//
	// private static IRhenaEdge getDefaultProcessorEdge() throws RhenaException
	// {
	//
	// return new RhenaEdge(EExecutionType.FRAMEWORK, new
	// RhenaReference(ModuleIdentifier.valueOf("com.unnsvc.rhena:lifecycle:0.0.1")),
	// TraverseType.SCOPE);
	// }

}
