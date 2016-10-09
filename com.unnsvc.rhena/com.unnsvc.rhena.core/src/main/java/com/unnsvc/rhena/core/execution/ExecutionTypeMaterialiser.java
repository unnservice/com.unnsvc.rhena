
package com.unnsvc.rhena.core.execution;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaExecution;
import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.core.visitors.DependencyCollectionVisitor;
import com.unnsvc.rhena.lifecycle.DefaultLifecycleFactory;
import com.unnsvc.rhena.lifecycle.ILifecycleFactory;

public class ExecutionTypeMaterialiser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;
	private RhenaExecutionType type;

	public ExecutionTypeMaterialiser(IResolutionContext context, RhenaExecutionType type) {

		this.context = context;
		this.type = type;
	}

	public RhenaExecution materialiseExecution(RhenaModel model) throws RhenaException {

		
		ILifecycleFactory lifecycleFactory = produceLifecycleFactory(model);
		


		RhenaExecution execution = new RhenaExecution(model.getModuleIdentifier(), type, new File("some/produced/artifact-" + type.toLabel()));

		
		
		return execution;
	}

	private ILifecycleFactory produceLifecycleFactory(RhenaModel model) throws RhenaException {

		if (model.getLifecycleModule() != null) {
			
			RhenaModel lifecycleModel = context.materialiseModel(model.getLifecycleModule());
			DependencyCollectionVisitor collector = new DependencyCollectionVisitor(context, RhenaExecutionType.COMPILE);
			lifecycleModel.visit(collector);
			
			RhenaExecution lifecycleArtifact = context.materialiseExecution(lifecycleModel, RhenaExecutionType.COMPILE);

			URLClassLoader lifecycleDependencies = new URLClassLoader(collector.getDependencyChainURL().toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
			URLClassLoader lifecycleClassloader = new URLClassLoader(new URL[] { lifecycleArtifact.getArtifactURL() }, lifecycleDependencies);
			for (URL depUrl : lifecycleClassloader.getURLs()) {
				log.info(model.getModuleIdentifier().toTag() + ":lifecycle classloader: " + depUrl);
			}
			ServiceLoader<ILifecycleFactory> lifecycleFactory = ServiceLoader.load(ILifecycleFactory.class, lifecycleClassloader);
			if (!lifecycleFactory.iterator().hasNext()) {
				throw new RhenaException("Failed to find a custom lifecycle in " + lifecycleModel.getModuleIdentifier().toTag() + " which is required by " + model.getModuleIdentifier().toTag());
			} else {
				ILifecycleFactory fact = lifecycleFactory.iterator().next();
				log.debug(model.getModuleIdentifier().toTag() + ":lifecycle type: " + fact.getClass());
				return fact;
			}
		}

		return new DefaultLifecycleFactory();
	}
}
