
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.DependencyType;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ModuleState;
import com.unnsvc.rhena.common.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.core.RhenaModuleParser;
import com.unnsvc.rhena.lifecycle.DefaultResourcesLifecycle;
import com.unnsvc.rhena.lifecycle.ILifecycle;
import com.unnsvc.rhena.lifecycle.IResourcesLifecycle;

public class WorkspaceRepository implements IRepository {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File workspaceDirectory;
	private Map<ModuleIdentifier, RhenaModule> resolvedModules;

	public WorkspaceRepository(File workspaceDirectory) {

		this.workspaceDirectory = workspaceDirectory;
		this.resolvedModules = new HashMap<ModuleIdentifier, RhenaModule>();
	}

	@Override
	public RhenaModule materialiseState(ModuleIdentifier moduleIdentifier, ModuleState moduleState) throws RhenaException {

		RhenaModule module = resolvedModules.get(moduleIdentifier);
		
		switch (moduleState) {
			case MODEL:
				if(!resolvedModules.containsKey(moduleIdentifier)) {
					module = resolveModel(moduleIdentifier);
					resolvedModules.put(moduleIdentifier, module);
				}
				module.setModuleState(ModuleState.MODEL);
				if (moduleState.equals(ModuleState.MODEL))
					break;
			case COMPILED:
				if (moduleState.equals(ModuleState.COMPILED))
					break;
			case PACKAGED:
				if (moduleState.equals(ModuleState.PACKAGED))
					break;
			case TESTED:
				if (moduleState.equals(ModuleState.TESTED))
					break;
			case DEPLOYED:
				if (moduleState.equals(ModuleState.DEPLOYED))
					break;
		}

		log.info("[" + module + "]:" + moduleState.toLabel() + " resolved");

		return module;
	}

	public RhenaModule resolveModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		File workspaceProject = new File(workspaceDirectory, moduleIdentifier.getComponentName() + "." + moduleIdentifier.getModuleName());
		File moduleDescriptor = new File(workspaceProject, "module.xml");

		if (!moduleDescriptor.exists()) {
			throw new RhenaException("Not in repository");
		}

		URI moduleUri = moduleDescriptor.toURI();

		RhenaModule module = new RhenaModuleParser(moduleIdentifier, moduleUri).getModule();
		module.setRepository(this);
		return module;
	}

	public RhenaLifecycleExecution materialisePackaged(RhenaModule model) throws RhenaException {

		// log.info("Executed: " + model.getModuleIdentifier() + ":" + scope);

		// Build some sort of dependency chain for the lifecycle classpath
		// DependencyCollectingVisitor dependencyCollectingVisitor = new
		// DependencyCollectingVisitor(CompositeScope.LIFECYCLE);
		// model.visit(dependencyCollectingVisitor);

		// build lifecycle classloader and load lifecycle service

		// execute each lifecycpe stage

		RhenaLifecycleExecution execution = new RhenaLifecycleExecution(model);
		//

		// log.info("Producing lifecycle execution using lifecycle: " +
		// lifecycle.getClass());

		// for (Subscope subscope : scope.getSubscopes()) {
		//
		// switch (subscope) {
		// case RESOURCES:
		// //
		// lifecycle.compileResources(lifecycleConfiguration.getResourcePaths(),
		// // lifecycleConfiguration.getTargetPath());
		// IResourcesLifecycle resourcesLifecycle =
		// getLifecycle(IResourcesLifecycle.class, model, scope);
		// resourcesLifecycle.compileResources();
		// case COMPILE:
		// // Has some sort of classpath dependencies
		// // CompiledClasspathElement[] compiledElement =
		// // lifecycle.materialiseSources(model.getSourceClasspaths());
		// // lifecycle.compileSources(model.get);
		// case PACKAGE:
		//
		// // PackagedClasspathElement packagedElement =
		// // lifecycle.materialisePackage(compiledElement);
		// // execution.addLifecycleExecutionClasspath(packagedElement);
		// case TEST:
		// // lifecycle.materialiseTest();
		// case ITEST:
		// // lifecycle.materialiseItest();
		// default:
		// break;
		// }
		// }
		//
		// log.info("[" + model.getModuleIdentifier() + "]:" + scope.toString()
		// + " produced " + execution.toString());
		// if(scope.equals(CompositeScope.MODEL)) {
		// new Exception("diag").printStackTrace(System.out);
		// }
		// // This is produced somehow here, and it comes from the classpath....
		// execution.addLifecycleExecutionClasspath(lifecycle.compileResources(model.getProperties()));
		// execution.addLifecycleExecutionClasspath(lifecycle.compileSources());

		// execution.addLifecycleExecutionClasspath(new File("target/" +
		// scope.toString().toLowerCase() + "/" + scope));
		return execution;
	}

	private IResourcesLifecycle getLifecycle(Class<? extends ILifecycle> lifecycleInterfaceType, RhenaModule model, DependencyType dependencyType) {

		IResourcesLifecycle ret = null;
		if (lifecycleInterfaceType.equals(IResourcesLifecycle.class)) {

			ret = new DefaultResourcesLifecycle().newDefaultResourcesLifecycle(model, dependencyType);
		}
		log.warn("[" + model.getModuleIdentifier() + "] has a custom lifecycle, but custom handling is not implemented, alwyas returning "
				+ lifecycleInterfaceType.toString());
		return ret;
	}
}
