
package com.unnsvc.rhena.core.execution;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.RhenaExecution;
import com.unnsvc.rhena.common.model.lifecycle.IConfigurator;
import com.unnsvc.rhena.common.model.lifecycle.IConfiguratorReference;
import com.unnsvc.rhena.common.model.lifecycle.IGeneratorReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessor;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;

public class ExecutionTypeMaterialiser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;
	private ExecutionType type;

	public ExecutionTypeMaterialiser(IResolutionContext context, ExecutionType type) {

		this.context = context; 
		this.type = type;
	}

	public RhenaExecution materialiseExecution(IRhenaModule model) throws RhenaException {
		
		
		
//		ILifecycleDeclaration lifecycleDeclaration = model.getLifecycleDeclarations(.get(model.getLifecycleName());
//		
//		IConfiguratorReference configuratorReference = lifecycleDeclaration.getConfigurator();
//		List<IProcessorReference> processorReferences = lifecycleDeclaration.getProcessors();
//		IGeneratorReference generatorReference = lifecycleDeclaration.getGenerator();
//
//		Class<? extends IConfigurator> configuratorType = resolveType(model, configuratorReference);
		

//		ILifecycleFactory lifecycleFactory = produceLifecycleFactory(model);
//		
//		
//		IResourcesProcessor resourcesProcessor = lifecycleFactory.newResourceProcessor(model, type);
//		/**
//		 * It is supposed to get a little configuration here from the model to pass down into the lifecycle
//		 */
//		resourcesProcessor.processResources(model);
//
//		RhenaExecution execution = new RhenaExecution(model.getModuleIdentifier(), type, new File("some/produced/artifact-" + type.toLabel()));
//
//		return execution;
		
		
		RhenaExecution execution = new RhenaExecution(model.getModuleIdentifier(), type, new File("some/produced/artifact-" + type.toLabel()));
		return execution;
	}

//	private Class<? extends IConfigurator> resolveType(IRhenaModule model, ILifecycleProcessor processor) {
//
//		
//		return null;
//	}
//
//	private ILifecycleFactory produceLifecycleFactory(RhenaModel model) throws RhenaException {
//
//		if (model.getLifecycleModule() != null) {
//
//			RhenaModel lifecycleModel = context.materialiseModel(model.getLifecycleModule());
//			DependencyCollectionVisitor collector = new DependencyCollectionVisitor(context, RhenaExecutionType.COMPILE);
//			lifecycleModel.visit(collector);
//
//			RhenaExecution lifecycleArtifact = context.materialiseExecution(lifecycleModel, RhenaExecutionType.COMPILE);
//
//			URLClassLoader lifecycleDependencies = new URLClassLoader(collector.getDependencyChainURL().toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
//			URLClassLoader lifecycleClassloader = new URLClassLoader(new URL[] { lifecycleArtifact.getArtifactURL() }, lifecycleDependencies);
//			for (URL depUrl : lifecycleClassloader.getURLs()) {
//				log.info(model.getModuleIdentifier().toTag() + ":lifecycle classloader: " + depUrl);
//			}
//			ServiceLoader<ILifecycleFactory> lifecycleFactory = ServiceLoader.load(ILifecycleFactory.class, lifecycleClassloader);
//			if (!lifecycleFactory.iterator().hasNext()) {
//				log.warn(model.getModuleIdentifier().toTag() + ":lifecycle custom lifecycle not found in  " + lifecycleModel.getModuleIdentifier() + ", always using default as this is not fully implemented, this will otherwise be a RhenaException error");
//				// throw new RhenaException("Failed to find a custom lifecycle
//				// in " + lifecycleModel.getModuleIdentifier().toTag() + " which
//				// is required by " + model.getModuleIdentifier().toTag());
//			} else {
//				ILifecycleFactory fact = lifecycleFactory.iterator().next();
//				log.debug(model.getModuleIdentifier().toTag() + ":lifecycle type: " + fact.getClass());
//				return fact;
//			}
//		}
//
//		return new DefaultLifecycleFactory();
//	}
}
