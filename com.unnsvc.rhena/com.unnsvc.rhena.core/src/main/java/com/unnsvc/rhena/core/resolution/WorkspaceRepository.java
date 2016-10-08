
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.CompositeScope;
import com.unnsvc.rhena.common.model.CompositeScope.Subscope;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.core.RhenaModuleParser;
import com.unnsvc.rhena.lifecycle.DefaultResourcesLifecycle;
import com.unnsvc.rhena.lifecycle.ILifecycle;
import com.unnsvc.rhena.lifecycle.IResourcesLifecycle;

public class WorkspaceRepository implements IRepository {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File workspaceDirectory;
	// private Map<ModuleIdentifier, ILifecycle> lifecycles;

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
		// DependencyCollectingVisitor dependencyCollectingVisitor = new
		// DependencyCollectingVisitor(CompositeScope.LIFECYCLE);
		// model.visit(dependencyCollectingVisitor);

		// build lifecycle classloader and load lifecycle service

		// execute each lifecycpe stage

		RhenaLifecycleExecution execution = new RhenaLifecycleExecution(model);
		//

//		log.info("Producing lifecycle execution using lifecycle: " + lifecycle.getClass());

		for (Subscope subscope : scope.getSubscopes()) {

			switch (subscope) {
				case RESOURCES:
//					lifecycle.compileResources(lifecycleConfiguration.getResourcePaths(), lifecycleConfiguration.getTargetPath());
					IResourcesLifecycle resourcesLifecycle = getLifecycle(IResourcesLifecycle.class);
					resourcesLifecycle.compileResources(model);
				case COMPILE:
					// Has some sort of classpath dependencies
					// CompiledClasspathElement[] compiledElement =
					// lifecycle.materialiseSources(model.getSourceClasspaths());
//					lifecycle.compileSources(model.get);
				case PACKAGE:

//					PackagedClasspathElement packagedElement = lifecycle.materialisePackage(compiledElement);
//					execution.addLifecycleExecutionClasspath(packagedElement);
				case TEST:
//					lifecycle.materialiseTest();
				case ITEST:
//					lifecycle.materialiseItest();
				default:
					log.info("[" + model.getModuleIdentifier() + "]:"+ scope.toString()+" produced RhenaLifecycleExecution...");
					break;
			}
		}

		// // This is produced somehow here, and it comes from the classpath....
		// execution.addLifecycleExecutionClasspath(lifecycle.compileResources(model.getProperties()));
		// execution.addLifecycleExecutionClasspath(lifecycle.compileSources());

		// execution.addLifecycleExecutionClasspath(new File("target/" +
		// scope.toString().toLowerCase() + "/" + scope));
		return execution;
	}

	private IResourcesLifecycle getLifecycle(Class<? extends ILifecycle> lifecycleInterfaceType) {

		IResourcesLifecycle ret = null;
		if(lifecycleInterfaceType.equals(IResourcesLifecycle.class)) {
			
			ret = new DefaultResourcesLifecycle();
		}
		log.warn("Not implemented, always returning default " + lifecycleInterfaceType.toString());
		return ret;
	}
}
