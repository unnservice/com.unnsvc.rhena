
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

import com.unnsvc.rhena.common.annotation.ProcessorContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.lifecycle.ICustomLifecycleProcessorExecutable;
import com.unnsvc.rhena.common.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.lifecycle.IGenerator;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;
import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessor;
import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessorExecutable;
import com.unnsvc.rhena.common.lifecycle.IProcessor;
import com.unnsvc.rhena.common.logging.ILoggerService;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IDependencies;

/**
 * This agent is executed in a separate agent JVM
 * 
 * @author noname
 *
 */
public class LifecycleAgent extends AbstractLifecycleAgent {

	private static final long serialVersionUID = 1L;
	private Map<Class<?>, Object> additionalInjectableTypes;

	public LifecycleAgent() throws RemoteException {

		super();
		this.additionalInjectableTypes = new HashMap<Class<?>, Object>();
		this.additionalInjectableTypes.put(List.class, new ArrayList<IProcessor>());
	}

	@Override
	@SuppressWarnings("unchecked")
	public File executeLifecycle(ILifecycleExecutable lifecycleExecutable, IRhenaModule module, EExecutionType executionType, IDependencies dependencies) throws RemoteException {

		try {
			/**
			 * Produce classloader heirarchy etc
			 */
			ILifecycleProcessorExecutable contextExecutable = lifecycleExecutable.getContextExecutable();
			IExecutionContext context = constructProcessor(contextExecutable, IExecutionContext.class, getClass().getClassLoader());
			executeProcessor(context, module, executionType, contextExecutable, dependencies);
			additionalInjectableTypes.put(IExecutionContext.class, context);

			for (ILifecycleProcessorExecutable processorExecutable : lifecycleExecutable.getProcessorExecutables()) {
				IProcessor processor = constructProcessor(processorExecutable, IProcessor.class, context.getClass().getClassLoader());
				executeProcessor(processor, module, executionType, processorExecutable, dependencies);
				List<IProcessor> additional = (List<IProcessor>) additionalInjectableTypes.get(List.class);
				additional.add(processor);
			}

			ILifecycleProcessorExecutable generatorExecutable = lifecycleExecutable.getGeneratorExecutable();
			IGenerator generator = constructProcessor(generatorExecutable, IGenerator.class, context.getClass().getClassLoader());
			executeProcessor(generator, module, executionType, generatorExecutable, dependencies);
			return generator.generate(module, executionType);

		} catch (Throwable ex) {
			System.err.println("Thrown: " + ex);
			throw new RemoteException(ex.getMessage(), ex);
		}
	}

	private void executeProcessor(ILifecycleProcessor processor, IRhenaModule module, EExecutionType executionType, ILifecycleProcessorExecutable executable,
			IDependencies dependencies) throws RemoteException {

		if (executable instanceof ICustomLifecycleProcessorExecutable) {
			ICustomLifecycleProcessorExecutable customExecutable = (ICustomLifecycleProcessorExecutable) executable;
			processor.configure(module, customExecutable.getConfiguration());
		} else {
			processor.configure(module, null);
		}

		if (processor instanceof IProcessor) {
			IProcessor proc = (IProcessor) processor;
			proc.process(module, executionType, dependencies);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T constructProcessor(ILifecycleProcessorExecutable executable, Class<T> marker, ClassLoader parentClassLoader) throws Exception {

		ClassLoader classloader = null;
		if (executable instanceof ICustomLifecycleProcessorExecutable) {
			ICustomLifecycleProcessorExecutable customExecutable = (ICustomLifecycleProcessorExecutable) executable;
			classloader = new ParentLastURLClassLoader(customExecutable.getDependencies(), getClass().getClassLoader());
		} else {
			classloader = new ParentLastURLClassLoader(new ArrayList<URL>(), getClass().getClassLoader());
		}

		Class<?> type = classloader.loadClass(executable.getClazz());
		Constructor<?> constr = type.getConstructor();
		Object instance = constr.newInstance();
		// ensure that it implements the marker interface
		// type.isInstance(obj)
		performInjection(instance);

		return (T) instance;
	}

	private void performInjection(Object instance)
			throws IllegalArgumentException, IllegalAccessException, AccessException, RemoteException, NotBoundException {

		for (Field field : instance.getClass().getDeclaredFields()) {

			field.setAccessible(true);
			if (field.isAnnotationPresent(ProcessorContext.class)) {
				if (field.getType().equals(ILoggerService.class)) {
					field.set(instance, getRemoteType(ILoggerService.class));
				} else if (additionalInjectableTypes.containsKey(field.getType())) {
					// try to find in additional types

					field.set(instance, additionalInjectableTypes.get(field.getType()));
				} else {
					
					throw new RhenaException(
							"Found @ProcessorContext but failed to find an instance to inject. " + field.getType() + " in " + instance.getClass());
				}
			}
			field.setAccessible(false);
		}
	}
}
