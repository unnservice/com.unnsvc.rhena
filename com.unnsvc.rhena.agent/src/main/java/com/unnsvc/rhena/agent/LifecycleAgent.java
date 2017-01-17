
package com.unnsvc.rhena.agent;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
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
import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.annotation.ProcessorContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
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
import com.unnsvc.rhena.common.visitors.IDependencies;

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
	public synchronized ILifecycleExecutionResult executeLifecycle(ICaller caller, ILifecycleExecutable lifecycleExecutable, IDependencies dependencies)
			throws RemoteException {

		Map<Class<?>, Object> additionalInjectableTypes;
		additionalInjectableTypes = new HashMap<Class<?>, Object>();
		additionalInjectableTypes.put(List.class, new ArrayList<IProcessor>());

		try {
			System.err.println("Executing  " + caller + " with " + dependencies);
			ClassLoader previousClassloader = new ParentLastURLClassLoader(dependencies.getAsURLs(), getClass().getClassLoader());
			for(URL url : dependencies.getAsURLs()) {
				System.err.println("Executing context with url " + url);
			}
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
				previousClassloader = processor.getClass().getClassLoader();
			}

			ILifecycleProcessorExecutable generatorExecutable = lifecycleExecutable.getGeneratorExecutable();
			IGenerator generator = constructProcessor(generatorExecutable, IGenerator.class, previousClassloader, additionalInjectableTypes);
			executeProcessor(caller, generator, generatorExecutable, dependencies);

			List<IResource> inputs = context.getResources();
			File generatedFile = generator.generate(caller);
			previousClassloader = generator.getClass().getClassLoader();

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

			LifecycleExecutionResult result = new LifecycleExecutionResult(generatedFile, inputs);
			return result;
		} catch (Throwable ex) {
			throw new RemoteException(ex.getMessage(), ex);
		} finally {
			additionalInjectableTypes.clear();
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
			classloader = new ParentLastURLClassLoader(customExecutable.getDependencies().getAsURLs(), parentClassLoader);
		} else {
			classloader = new ParentLastURLClassLoader(new ArrayList<URL>(), parentClassLoader);
		}

		Class<?> type = classloader.loadClass(executable.getClazz());
		Constructor<?> constr = type.getConstructor();
		Object instance = constr.newInstance();
		performInjection(type, instance, additionalInjectableTypes);

		System.err.println("Casting " + instance + " from classloader " + instance.getClass().getClassLoader() + " to interface "+marker+" from classloader " + marker.getClassLoader());
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
