
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.Constants;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaExecution;
import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.common.model.RhenaModel;

public class WorkspaceRepository extends AbstractRepository {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File workspaceDirectory;
	private IResolutionContext context;

	public WorkspaceRepository(IResolutionContext context, File workspaceDirectory) {

		this.context = context;
		this.workspaceDirectory = workspaceDirectory;
	}

	public RhenaLifecycleExecution materialisePackaged(RhenaModel model) throws RhenaException {

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

	// private IResourcesLifecycle getLifecycle(Class<? extends ILifecycle>
	// lifecycleInterfaceType, RhenaModel model, RhenaEdgeType dependencyType) {
	//
	// IResourcesLifecycle ret = null;
	// if (lifecycleInterfaceType.equals(IResourcesLifecycle.class)) {
	//
	// ret = new DefaultResourcesLifecycle().newDefaultResourcesLifecycle(model,
	// dependencyType);
	// }
	// log.warn("[" + model.getModuleIdentifier() + "] has a custom lifecycle,
	// but custom handling is not implemented, alwyas returning "
	// + lifecycleInterfaceType.toString());
	// return ret;
	// }

	@Override
	public RhenaModel materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		File workspaceProject = new File(workspaceDirectory, moduleIdentifier.getComponentName() + "." + moduleIdentifier.getModuleName());
		File moduleDescriptor = new File(workspaceProject, Constants.MODULE_DESCRIPTOR_FILENAME);
		URI moduleDescriptorUri = moduleDescriptor.toURI();

		if (!moduleDescriptor.exists()) {
			throw new RhenaException("Not in repository");
		}

		RhenaModel model = resolveModel(moduleIdentifier, moduleDescriptorUri);

		return model;
	}

	@Override
	public RhenaExecution materialiseExecution(RhenaModel model, RhenaExecutionType type) throws RhenaException {

		// traverse model to get lifecycle and all of its compile dependencies?
		
		RhenaExecution execution = new RhenaExecution(model.getModuleIdentifier(), type, new File("some/produced/artifact-" + type.toLabel()));
		// perform the actual build
			

		
		if(model.getLifecycleModule() != null) {
			
			RhenaModel lifecycleModel = context.materialiseModel(model.getModuleIdentifier());
			
		}
		
//		throw new UnsupportedOperationException("Not implemented");
		return execution;
	}
}
