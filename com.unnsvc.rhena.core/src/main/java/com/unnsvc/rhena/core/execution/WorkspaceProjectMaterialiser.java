
package com.unnsvc.rhena.core.execution;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.RhenaExecution;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.common.model.lifecycle.IConfigurator;
import com.unnsvc.rhena.common.model.lifecycle.IConfiguratorReference;
import com.unnsvc.rhena.common.model.lifecycle.IGenerator;
import com.unnsvc.rhena.common.model.lifecycle.IGeneratorReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessor;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.common.visitors.RhenaDependencyCollectionVisitor;

public class WorkspaceProjectMaterialiser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;
	private ExecutionType type;

	public WorkspaceProjectMaterialiser(IResolutionContext context, ExecutionType type) {

		this.context = context;
		this.type = type;
	}

	public RhenaExecution materialiseExecution(IRhenaModule model) throws RhenaException {

		ILifecycleDeclaration lifecycleDeclaration = model.getLifecycleDeclarations().get(model.getLifecycleName());
		//
		IConfiguratorReference configuratorReference = lifecycleDeclaration.getConfigurator();
		List<IProcessorReference> processorReferences = lifecycleDeclaration.getProcessors();
		IGeneratorReference generatorReference = lifecycleDeclaration.getGenerator();

		IConfigurator configurator = instantiateProcessor(model, configuratorReference, IConfigurator.class);
		configurator.configure(configuratorReference.getConfiguration());
		
		for(IProcessorReference pref : processorReferences) {
			
			IProcessor processor = instantiateProcessor(model, configuratorReference, IProcessor.class);
			processor.configure(pref.getConfiguration());
			processor.process(configurator, model);
		}
		
		IGenerator generator = instantiateProcessor(model, configuratorReference, IGenerator.class);
		generator.configure(generatorReference.getConfiguration());
		File artifact = generator.generate(configurator, model);
		
		RhenaExecution execution = new RhenaExecution(model.getModuleIdentifier(), type, artifact);
		return execution;
	}

	private URLClassLoader createClassloader(ILifecycleProcessorReference processor) throws RhenaException {

		List<URL> l = new ArrayList<URL>();

		l.addAll(processor.getModule().visit(new RhenaDependencyCollectionVisitor(context, ExecutionType.FRAMEWORK, TraverseType.NONE)).getDependenciesURL());

		URLClassLoader dependenciesLoader = new URLClassLoader(l.toArray(new URL[l.size()]), Thread.currentThread().getContextClassLoader());
		URLClassLoader mainLoader = new URLClassLoader(new URL[] {context.materialiseExecution(processor.getModule(), ExecutionType.FRAMEWORK).getArtifactURL()}, dependenciesLoader);
		
		return mainLoader;
	}

	@SuppressWarnings("unchecked")
	private <T extends ILifecycleProcessor> T instantiateProcessor(IRhenaModule model, ILifecycleProcessorReference processor, Class<T> type) throws RhenaException {

		URLClassLoader loader = createClassloader(processor);
		try {
			Class<?> c = loader.loadClass(processor.getClazz());
			// @TODO enable constructor selection so we can pass in model if
			// such a constructor exists
			Constructor<?> constr = c.getConstructor();
			Object o = constr.newInstance();
			return (T) o;
		} catch (Exception ex) {
			for (URL url : loader.getURLs()) {
				log.error(processor.getModule().getModuleIdentifier().toTag() + ": classloader has " + url);
			}
			throw new RhenaException("Failed to instantiate: " + processor.getClass().getName(), ex);
		}
	}
	//
	// private ILifecycleFactory produceLifecycleFactory(RhenaModel model)
	// throws RhenaException {
	//
	// if (model.getLifecycleModule() != null) {
	//
	// RhenaModel lifecycleModel =
	// context.materialiseModel(model.getLifecycleModule());
	// DependencyCollectionVisitor collector = new
	// DependencyCollectionVisitor(context, RhenaExecutionType.COMPILE);
	// lifecycleModel.visit(collector);
	//
	// RhenaExecution lifecycleArtifact =
	// context.materialiseExecution(lifecycleModel, RhenaExecutionType.COMPILE);
	//
	// URLClassLoader lifecycleDependencies = new
	// URLClassLoader(collector.getDependencyChainURL().toArray(new URL[0]),
	// Thread.currentThread().getContextClassLoader());
	// URLClassLoader lifecycleClassloader = new URLClassLoader(new URL[] {
	// lifecycleArtifact.getArtifactURL() }, lifecycleDependencies);
	// for (URL depUrl : lifecycleClassloader.getURLs()) {
	// log.info(model.getModuleIdentifier().toTag() + ":lifecycle classloader: "
	// + depUrl);
	// }
	// ServiceLoader<ILifecycleFactory> lifecycleFactory =
	// ServiceLoader.load(ILifecycleFactory.class, lifecycleClassloader);
	// if (!lifecycleFactory.iterator().hasNext()) {
	// log.warn(model.getModuleIdentifier().toTag() + ":lifecycle custom
	// lifecycle not found in " + lifecycleModel.getModuleIdentifier() + ",
	// always using default as this is not fully implemented, this will
	// otherwise be a RhenaException error");
	// // throw new RhenaException("Failed to find a custom lifecycle
	// // in " + lifecycleModel.getModuleIdentifier().toTag() + " which
	// // is required by " + model.getModuleIdentifier().toTag());
	// } else {
	// ILifecycleFactory fact = lifecycleFactory.iterator().next();
	// log.debug(model.getModuleIdentifier().toTag() + ":lifecycle type: " +
	// fact.getClass());
	// return fact;
	// }
	// }
	//
	// return new DefaultLifecycleFactory();
	// }
}
