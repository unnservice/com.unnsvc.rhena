
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ServiceLoader;

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
import com.unnsvc.rhena.core.visitors.DependencyCollectionVisitor;
import com.unnsvc.rhena.lifecycle.DefaultLifecycleFactory;
import com.unnsvc.rhena.lifecycle.ILifecycleFactory;

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

		ILifecycleFactory lifecycleFactory = produceLifecycleFactory(model);

		// throw new UnsupportedOperationException("Not implemented");
		return execution;
	}

	private ILifecycleFactory produceLifecycleFactory(RhenaModel model) throws RhenaException {

		if (model.getLifecycleModule() != null) {

			RhenaModel lifecycleModel = context.materialiseModel(model.getLifecycleModule().getTarget());
			DependencyCollectionVisitor collector = new DependencyCollectionVisitor(context, RhenaExecutionType.COMPILE);
			lifecycleModel.visit(collector);

			RhenaExecution lifecycleArtifact = context.materialiseModuleType(lifecycleModel, RhenaExecutionType.COMPILE);

			URLClassLoader lifecycleDependencies = new URLClassLoader(collector.getDependencyChainURL().toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
			URLClassLoader lifecycleClassloader = new URLClassLoader(new URL[] { lifecycleArtifact.getArtifactURL() }, lifecycleDependencies);
			for(URL depUrl : lifecycleClassloader.getURLs()) {
				log.debug(model.getModuleIdentifier().toTag() + ":lifecycle classloader: " + depUrl);
			}
			ServiceLoader<ILifecycleFactory> lifecycleFactory = ServiceLoader.load(ILifecycleFactory.class, lifecycleClassloader);
			if (!lifecycleFactory.iterator().hasNext()) {
				log.warn(model.getModuleIdentifier().toTag() + ":lifecycle Could not find an implementation for " + ILifecycleFactory.class.getName() + " in " + model.getLifecycleModule().getTarget() + ", falling through to use default factory");
			} else {
				ILifecycleFactory fact = lifecycleFactory.iterator().next();
				log.debug(model.getModuleIdentifier().toTag() + ":lifecycle type: " + fact.getClass());
				return fact;
			}
		}

		return new DefaultLifecycleFactory();
	}
}
