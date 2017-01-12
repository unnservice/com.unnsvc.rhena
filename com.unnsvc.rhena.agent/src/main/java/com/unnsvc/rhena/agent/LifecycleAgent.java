
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
	public File executeLifecycle(ILifecycleExecutable lifecycleExecutable, IRhenaModule module, EExecutionType executionType, IDependencies dependencies)
			throws RemoteException {

		try {
			/**
			 * Produce classloader heirarchy etc
			 */
			ILifecycleProcessorExecutable contextExecutable = lifecycleExecutable.getContextExecutable();
			IExecutionContext context = constructProcessor(contextExecutable, IExecutionContext.class, getClass().getClassLoader());
			executeProcessor(context, module, executionType, contextExecutable, dependencies);
			additionalInjectableTypes.put(IExecutionContext.class, context);

			ClassLoader previousClassloader = context.getClass().getClassLoader();
			for (ILifecycleProcessorExecutable processorExecutable : lifecycleExecutable.getProcessorExecutables()) {
				IProcessor processor = constructProcessor(processorExecutable, IProcessor.class, previousClassloader);
				executeProcessor(processor, module, executionType, processorExecutable, dependencies);
				List<IProcessor> additional = (List<IProcessor>) additionalInjectableTypes.get(List.class);
				additional.add(processor);
				previousClassloader = processor.getClass().getClassLoader();
			}

			ILifecycleProcessorExecutable generatorExecutable = lifecycleExecutable.getGeneratorExecutable();
			IGenerator generator = constructProcessor(generatorExecutable, IGenerator.class, previousClassloader);
			executeProcessor(generator, module, executionType, generatorExecutable, dependencies);
			return generator.generate(module, executionType);

		} catch (Throwable ex) {
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

		System.out.println("Executing processor " + processor.getClass().getName());
		if (processor instanceof IProcessor) {
			IProcessor proc = (IProcessor) processor;
//			try {
//				System.out.println("Declaring class fields: " + proc.getClass().getDeclaredFields().length + ": " + proc.getClass().getName() + " classloader: " + proc.getClass().getClassLoader());
//				System.err.println("Encloding " + Arrays.toString(proc.getClass().cast(proc).getClass().getDeclaredFields()));
//				for (Class c : proc.getClass().getDeclaredClasses()) {
//					for (Field field : c.getDeclaredFields()) {
//						if (field.isAnnotationPresent(ProcessorContext.class)) {
//							System.out.println("\tfield:" + field + ":" + field.getType() + ": " + field.get(proc));
//						}
//					}
//				}
//				Class<?> type = proc.getClass().getClassLoader().loadClass(proc.getClass().getName());
//				System.err.println("Fields " + type.getName() + ": " + type.getSuperclass());
//			} catch (Exception ex) {
//				throw new RemoteException(ex.getMessage(), ex);
//			}
			proc.process(module, executionType, dependencies);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T constructProcessor(ILifecycleProcessorExecutable executable, Class<T> marker, ClassLoader parentClassLoader) throws Exception {

		ClassLoader classloader = null;
		if (executable instanceof ICustomLifecycleProcessorExecutable) {
			ICustomLifecycleProcessorExecutable customExecutable = (ICustomLifecycleProcessorExecutable) executable;
			classloader = new ParentLastURLClassLoader(customExecutable.getDependencies(), parentClassLoader);
		} else {
			classloader = new ParentLastURLClassLoader(new ArrayList<URL>(), parentClassLoader);
		}

		Class<?> type = classloader.loadClass(executable.getClazz());
		Constructor<?> constr = type.getConstructor();
		Object instance = constr.newInstance();
		// ensure that it implements the marker interface
		// type.isInstance(obj)
		performInjection(type, instance);

		return (T) instance;
	}

	private void performInjection(Class<?> type, Object instance)
			throws IllegalArgumentException, IllegalAccessException, AccessException, RemoteException, NotBoundException, ClassNotFoundException {

		for (Field field : type.getDeclaredFields()) {

			if (field.isAnnotationPresent(ProcessorContext.class)) {
				field.setAccessible(true);

				if (field.getType().equals(ILoggerService.class)) {

					System.err.println("Injecting ILoggerService to " + getRemoteType(ILoggerService.class.getName()));
					field.set(instance, getRemoteType(ILoggerService.class.getName()));
				} else if (additionalInjectableTypes.containsKey(field.getType())) {
					// try to find in additional types

					field.set(instance, additionalInjectableTypes.get(field.getType()));
				} else {

					throw new RhenaException(
							"Found @ProcessorContext but failed to find an instance to inject. " + field.getType() + " in " + type);
				}

				field.setAccessible(false);
			}
		}
		
		if(type.getSuperclass() != null && !type.getSuperclass().equals(Object.class)) {
//			System.err.println("Casting to " + instance.getClass().getName() + " to " + instance.getClass().getSuperclass().getName());
			performInjection(type.getSuperclass(), instance);
		}
	}
}
