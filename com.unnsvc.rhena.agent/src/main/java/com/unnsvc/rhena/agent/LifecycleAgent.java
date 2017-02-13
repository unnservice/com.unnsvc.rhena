
package com.unnsvc.rhena.agent;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.agent.lifecycle.LifecycleExecutionResult;
import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.ICommandCaller;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.annotation.ProcessorContext;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.ArtifactDescriptor;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.ExplodedArtifact;
import com.unnsvc.rhena.common.execution.IArtifact;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.lifecycle.ICommand;
import com.unnsvc.rhena.common.lifecycle.ICustomLifecycleCommandExecutable;
import com.unnsvc.rhena.common.lifecycle.ICustomLifecycleProcessorExecutable;
import com.unnsvc.rhena.common.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.lifecycle.IGenerator;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;
import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessor;
import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessorExecutable;
import com.unnsvc.rhena.common.lifecycle.IProcessor;
import com.unnsvc.rhena.common.lifecycle.IResource;
import com.unnsvc.rhena.common.logging.ILoggerService;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.search.ExecutionCollectionDependencyVisitor;
import com.unnsvc.rhena.common.search.IDependencies;

/**
 * This agent is executed in a separate agent JVM
 * 
 * @author noname
 *
 */
public class LifecycleAgent extends AbstractLifecycleAgent {

	private static final long serialVersionUID = 1L;

	public LifecycleAgent() throws RemoteException {

		super();
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized ILifecycleExecutionResult executeLifecycle(IRhenaCache cache, IRhenaConfiguration config, ICaller caller,
			ILifecycleExecutable lifecycleExecutable) throws RemoteException {

		Map<Class<?>, Object> additionalInjectableTypes = new HashMap<Class<?>, Object>();
		additionalInjectableTypes.put(List.class, new ArrayList<IProcessor>());
		additionalInjectableTypes.put(IRhenaCache.class, cache);

		try {
			IDependencies dependencies = getDependencies(caller, cache);

			// System.err.println("Executing " + caller + " with " +
			// dependencies);
			// ClassLoader previousClassloader = new
			// ParentLastURLClassLoader(dependencies.getAsURLs(),
			// getClass().getClassLoader());
			ClassLoader previousClassloader = getClass().getClassLoader();
			// for(URL url : dependencies.getAsURLs()) {
			// System.err.println("Executing context with url " + url);
			// }
			/**
			 * Produce classloader heirarchy etc
			 */
			ILifecycleProcessorExecutable contextExecutable = lifecycleExecutable.getContextExecutable();
			IExecutionContext context = constructProcessor(contextExecutable, IExecutionContext.class, previousClassloader, additionalInjectableTypes);
			executeProcessor(caller, context, contextExecutable, dependencies);
			additionalInjectableTypes.put(IExecutionContext.class, context);
			previousClassloader = context.getClass().getClassLoader();

			for (ILifecycleProcessorExecutable processorExecutable : lifecycleExecutable.getProcessorExecutables()) {
				IProcessor processor = constructProcessor(processorExecutable, IProcessor.class, previousClassloader, additionalInjectableTypes);
				executeProcessor(caller, processor, processorExecutable, dependencies);
				List<IProcessor> additional = (List<IProcessor>) additionalInjectableTypes.get(List.class);
				additional.add(processor);
			}

			List<IResource> inputs = context.getResources();

			if (config.getBuildConfiguration().isPackageWorkspace()) {

				ILifecycleProcessorExecutable generatorExecutable = lifecycleExecutable.getGeneratorExecutable();
				IGenerator generator = constructProcessor(generatorExecutable, IGenerator.class, previousClassloader, additionalInjectableTypes);
				executeProcessor(caller, generator, generatorExecutable, dependencies);

				List<IArtifactDescriptor> results = generator.generate(caller);

				executeCommand(caller, lifecycleExecutable, previousClassloader, additionalInjectableTypes, dependencies);

				return new LifecycleExecutionResult(results, inputs);
			} else {

				executeCommand(caller, lifecycleExecutable, previousClassloader, additionalInjectableTypes, dependencies);

				List<IArtifactDescriptor> generated = new ArrayList<IArtifactDescriptor>();
				for (IResource resource : inputs) {
					if (resource.getResourceType().equals(caller.getExecutionType())) {
						File sourceDir = new File(resource.getBaseDirectory(), resource.getRelativeSourcePath()).getCanonicalFile().getAbsoluteFile();
						File outputDir = new File(resource.getBaseDirectory(), resource.getRelativeOutputPath()).getCanonicalFile().getAbsoluteFile();
						if (outputDir.exists() && outputDir.list().length > 0) {

							IArtifact mainArtifact = new ExplodedArtifact(outputDir.getName(), outputDir.toURI().toURL());
							IArtifact sourceArtifact = new ExplodedArtifact(sourceDir.getName(), sourceDir.toURI().toURL());

							IArtifactDescriptor descriptor = new ArtifactDescriptor(IArtifactDescriptor.DEFAULT_CLASSIFIER, mainArtifact, sourceArtifact, null);
							generated.add(descriptor);
						}
					}
				}
				return new LifecycleExecutionResult(generated, inputs);
			}
		} catch (Throwable ex) {
			throw new RemoteException(ex.getMessage(), ex);
		} finally {
			additionalInjectableTypes.clear();
		}

	}

	private IDependencies getDependencies(ICaller caller, IRhenaCache cache) throws RhenaException {

		// ((Dependencies)
		// depvisitor.getDependencies()).debug(getContext().getLogger(),
		// caller.getIdentifier(), caller.getExecutionType());
		ExecutionCollectionDependencyVisitor depvisitor = new ExecutionCollectionDependencyVisitor(cache, caller.getExecutionType(), ESelectionType.SCOPE);
		caller.getModule().visit(depvisitor);

		/**
		 * Up to, but not with, the ordinal, becauuse that's the one we will
		 * create next by executing a lifecycle Start at 1 so we skip MODEL
		 */
		for (int i = 0; i < caller.getExecutionType().ordinal(); i++) {
			// 0 is model

			EExecutionType t = EExecutionType.values()[i];
			IRhenaExecution exec = cache.getExecution(caller.getIdentifier()).get(t);
			depvisitor.getExecutions(t).add(0, exec);
		}
		return depvisitor.getDependencies();
	}

	private void executeCommand(ICaller caller, ILifecycleExecutable lifecycleExecutable, ClassLoader previousClassloader,
			Map<Class<?>, Object> additionalInjectableTypes, IDependencies dependencies) throws Exception {

		if (caller instanceof ICommandCaller) {
			ICommandCaller commandCaller = (ICommandCaller) caller;
			ICustomLifecycleCommandExecutable foundCommandExec = null;
			for (ICustomLifecycleCommandExecutable commandExec : lifecycleExecutable.getCommandExecutables()) {
				if (commandExec.getCommandName().equals(commandCaller.getCommand())) {
					foundCommandExec = commandExec;
					break;
				}
			}

			if (foundCommandExec == null) {
				throw new RhenaException("Command not found: " + caller.toString());
			}

			// execute
			ICommand command = constructProcessor(foundCommandExec, ICommand.class, previousClassloader, additionalInjectableTypes);
			executeProcessor(caller, command, foundCommandExec, dependencies);
		}
	}

	private void executeProcessor(ICaller caller, ILifecycleProcessor processor, ILifecycleProcessorExecutable executable, IDependencies dependencies)
			throws RemoteException {

		if (executable instanceof ICustomLifecycleProcessorExecutable) {
			ICustomLifecycleProcessorExecutable customExecutable = (ICustomLifecycleProcessorExecutable) executable;
			processor.configure(caller, customExecutable.getConfiguration());
		} else {
			processor.configure(caller, null);
		}

		// System.out.println("Executing processor " +
		// processor.getClass().getName());5
		if (processor instanceof IProcessor) {

			IProcessor proc = (IProcessor) processor;
			proc.process(caller, dependencies);
		}
	}

	@SuppressWarnings({ "unchecked", "resource" })
	private <T> T constructProcessor(ILifecycleProcessorExecutable executable, Class<T> marker, ClassLoader parentClassLoader,
			Map<Class<?>, Object> additionalInjectableTypes) throws Exception {

		ClassLoader classloader = null;
		if (executable instanceof ICustomLifecycleProcessorExecutable) {
			ICustomLifecycleProcessorExecutable customExecutable = (ICustomLifecycleProcessorExecutable) executable;
			// classloader = new
			// ParentLastURLClassLoader(customExecutable.getDependencies().getAsURLs(),
			// parentClassLoader);
			classloader = new URLClassLoader(customExecutable.getDependencies().getAsURLs().toArray(new URL[0]), parentClassLoader);
		} else {
			// classloader = new ParentLastURLClassLoader(new ArrayList<URL>(),
			// parentClassLoader);
			classloader = new URLClassLoader(new URL[0], parentClassLoader);
		}

		Class<?> type = classloader.loadClass(executable.getClazz());
		Constructor<?> constr = type.getConstructor();
		Object instance = constr.newInstance();
		performInjection(type, instance, additionalInjectableTypes);

		// System.err.println("Casting " + instance + " from classloader " +
		// instance.getClass().getClassLoader() + " to interface "+marker+" from
		// classloader " + marker.getClassLoader());
		return (T) instance;
	}

	private void performInjection(Class<?> type, Object instance, Map<Class<?>, Object> additionalInjectableTypes)
			throws IllegalArgumentException, IllegalAccessException, AccessException, RemoteException, NotBoundException, ClassNotFoundException {

		for (Field field : type.getDeclaredFields()) {

			if (field.isAnnotationPresent(ProcessorContext.class)) {
				field.setAccessible(true);

				if (field.getType().equals(ILoggerService.class)) {

					field.set(instance, getRemoteType(ILoggerService.class.getName()));
				} else if (additionalInjectableTypes.containsKey(field.getType())) {

					field.set(instance, additionalInjectableTypes.get(field.getType()));
				} else {

					throw new RhenaException("Found @ProcessorContext but failed to find an instance to inject. " + field.getType() + " in " + type);
				}

				field.setAccessible(false);
			}
		}

		if (type.getSuperclass() != null && !type.getSuperclass().equals(Object.class)) {
			// System.err.println("Casting to " + instance.getClass().getName()
			// + " to " + instance.getClass().getSuperclass().getName());
			performInjection(type.getSuperclass(), instance, additionalInjectableTypes);
		}
	}
}
