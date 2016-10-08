
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.CompositeScope;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.core.resolution.parsers.RhenaModuleParser;
import com.unnsvc.rhena.lifecycle.DefaultLifecycle;
import com.unnsvc.rhena.lifecycle.ILifecycle;

public class WorkspaceRepository implements IRepository {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File workspaceDirectory;
//	private Map<ModuleIdentifier, ILifecycle> lifecycles;

	public WorkspaceRepository(File workspaceDirectory) {

		this.workspaceDirectory = workspaceDirectory;
	}

	@Override
	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		File workspaceProject = new File(workspaceDirectory, moduleIdentifier.getComponentName() + "." + moduleIdentifier.getModuleName());
		File moduleDescriptor = new File(workspaceProject, "module.xml");

		if (!moduleDescriptor.exists()) {
			throw new RhenaException("Not in repository");
		}

		URI moduleUri = moduleDescriptor.toURI();
		return new RhenaModuleParser(moduleIdentifier, moduleUri, this).getModule();
	}

	@Override
	public RhenaLifecycleExecution materialiseScope(RhenaModule model, CompositeScope scope) throws RhenaException {

		

		
		// log.info("Executed: " + model.getModuleIdentifier() + ":" + scope);

		// Build some sort of dependency chain for the lifecycle classpath
		//		DependencyCollectingVisitor dependencyCollectingVisitor = new DependencyCollectingVisitor(CompositeScope.LIFECYCLE);
		//		model.visit(dependencyCollectingVisitor);

		// build lifecycle classloader and load lifecycle service

		// execute each lifecycpe stage
		
		ILifecycle lifecycle = null;
		if(model.getLifecycleDeclaration() == null) {
			
//			lifecycle = model.getLifecycleDeclaration();
			lifecycle = new DefaultLifecycle();
		} else {
			
			log.warn("Custom lifecycle not implemented, using default");
			lifecycle = new DefaultLifecycle();
		}
		
		RhenaLifecycleExecution execution = new RhenaLifecycleExecution(model);
//		execution.setLifecycle(lifecycle);
//		

		log.info("Producing lifecycle execution using lifecycle: " + lifecycle.getClass());
		
//		for(Subscope subscope : scope.getSubscopes()) {
//			
//			switch(subscope) {
//				case RESOURCES:
//					// Has some sort of classpath dependencies
//					CompiledClasspathElement[] compiledElement = lifecycle.materialiseResources(model.getResourceClasspaths());
//					break;
//				case COMPILE:									
//					// Has some sort of classpath dependencies
//					CompiledClasspathElement[] compiledElement = lifecycle.materialiseSources(model.getSourceClasspaths());
//					break;
//				case PACKAGE:
//					
//					PackagedClasspathElement packagedElement = lifecycle.materialisePackage(compiledElement);
//					execution.addLifecycleExecutionClasspath(packagedElement);
//					break;
//				case TEST:
//					lifecycle.materialiseTest();
//					break;
//				case ITEST:
//					lifecycle.materialiseItest();
//					break;
//			}
//		}
		
//		// This is produced somehow here, and it comes from the classpath....
//		execution.addLifecycleExecutionClasspath(lifecycle.compileResources(model.getProperties()));
//		execution.addLifecycleExecutionClasspath(lifecycle.compileSources());

//		execution.addLifecycleExecutionClasspath(new File("target/" + scope.toString().toLowerCase() + "/" + scope));
		return execution;
	}
}
