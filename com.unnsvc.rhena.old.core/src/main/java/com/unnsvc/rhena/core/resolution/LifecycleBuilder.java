
package com.unnsvc.rhena.core.resolution;

public class LifecycleBuilder { 
//	implements ILifecycleBuilder {
//}

//	private static final long serialVersionUID = 1L;
//	private IRhenaModule module;
//	private IRhenaContext context;
//
//	public LifecycleBuilder(IRhenaModule module, IRhenaContext context) {
//
//		this.module = module;
//		this.context = context;
//	}
//
//	@Override
//	public ILifecycle buildLifecycle(IEntryPoint entryPoint, String lifecycleName) throws RhenaException {
//
//		if (module.getLifecycleName().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {
//			return constructDefaultLifecycle(entryPoint);
//		} else {
//			return constructCustomLifecycle(entryPoint);
//		}
//	}
//
//	private ILifecycle constructDefaultLifecycle(IEntryPoint entryPoint) throws RhenaException {
//
//		try {
//			/**
//			 * Build and configure lifecycle
//			 */
//			IExecutionContext contextProc = new DefaultContext();
//			performInjection(contextProc, null);
//			contextProc.configure(module, createDefaultContextConfiguration());
//
//			IProcessor javaProcessor = new DefaultJavaProcessor();
//			performInjection(javaProcessor, contextProc);
//			javaProcessor.configure(module, Utils.newEmptyDocument());
//			IProcessor manifestProcessor = new DefaultManifestProcessor();
//			performInjection(manifestProcessor, contextProc);
//			manifestProcessor.configure(module, Utils.newEmptyDocument());
//
//			List<IProcessor> processors = new ArrayList<IProcessor>();
//			processors.add(javaProcessor);
//			processors.add(manifestProcessor);
//
//			IGenerator generator = new DefaultGenerator();
//			performInjection(generator, contextProc);
//			generator.configure(module, Utils.newEmptyDocument());
//
//			ILifecycle lifecycle = new Lifecycle(contextProc, generator, processors);
//
//			return lifecycle;
//		} catch (Exception ex) {
//			throw new RhenaException(ex.getMessage(), ex);
//		}
//	}
//
//	private ILifecycle constructCustomLifecycle(IEntryPoint entryPoint) throws RhenaException {
//
//		ILifecycleReference lifecycleRef = module.getLifecycleDeclarations().get(module.getLifecycleName());
//
//		IExecutionContext contextProcessor = constructLifecycleProcessor(lifecycleRef.getContext(), IExecutionContext.class, null);
//		contextProcessor.configure(module, lifecycleRef.getContext().getConfiguration());
//
//		List<IProcessor> processors = new ArrayList<IProcessor>();
//		for (ILifecycleProcessorReference proc : lifecycleRef.getProcessors()) {
//
//			IProcessor processor = constructLifecycleProcessor(proc, IProcessor.class, contextProcessor);
//			processor.configure(module, proc.getConfiguration());
//			processors.add(processor);
//		}
//
//		// and finally, execute the generator
//		IGenerator generator = constructLifecycleProcessor(lifecycleRef.getGenerator(), IGenerator.class, contextProcessor);
//		generator.configure(module, lifecycleRef.getGenerator().getConfiguration());
//
//		ILifecycle lifecycle = new Lifecycle(contextProcessor, generator, processors);
//		return lifecycle;
//	}
//
//	/**
//	 * 
//	 * @param processor
//	 *            the processor reference we're building
//	 * @param marker
//	 *            marker for use in returning a properly typed processor
//	 * @param executionContext
//	 *            May be null if we're constructing the actual executionContext
//	 *            processor
//	 * @return
//	 * @throws RhenaException
//	 */
//	@SuppressWarnings("unchecked")
//	private <T extends ILifecycleProcessor> T constructLifecycleProcessor(ILifecycleProcessorReference processor, Class<T> marker,
//			IExecutionContext executionContext) throws RhenaException {
//
//		DependencyCollectionVisitor coll = new DependencyCollectionVisitor(context.getCache(), processor.getModuleEdge());
//		List<URL> deps = new ArrayList<URL>();
//		for (IRhenaExecution exec : coll.getDependencies()) {
//			deps.add(exec.getArtifact().getArtifactUrl());
//		}
//
//		/**
//		 * @TODO Determine strategy for closing the classloader later in the
//		 *       lifecycle?
//		 */
//		// URLClassLoader urlc = new URLClassLoader(deps.toArray(new
//		// URL[deps.size()]), Thread.currentThread().getContextClassLoader());
//		ClassLoader urlc = new ParentLastURLClassLoader(deps, getClass().getClassLoader());
//
//		try {
//			Class<T> clazz = (Class<T>) urlc.loadClass(processor.getClazz());
//			Constructor<T> constr = clazz.getConstructor();
//			T instance = constr.newInstance();
//			performInjection(instance, executionContext);
//			return instance;
//		} catch (NoSuchMethodException nsme) {
//			throw new RhenaException("Failed to find default no-arg constructor for: " + processor.getClazz());
//		} catch (Exception ex) {
//			throw new RhenaException(ex.getMessage(), ex);
//		}
//
//	}
//
//	private void performInjection(Object instance, IExecutionContext executionContext) throws IllegalArgumentException, IllegalAccessException, RhenaException {
//
//		for (Field field : instance.getClass().getDeclaredFields()) {
//
//			if (field.isAnnotationPresent(ProcessorContext.class)) {
//
//				field.setAccessible(true);
//				if (field.getType().equals(IRhenaContext.class)) {
//					field.set(instance, context);
//				} else if (field.getType().equals(IRhenaCache.class)) {
//					field.set(instance, context.getCache());
//				} else if (field.getType().equals(ILogger.class)) {
//					field.set(instance, context.getLogger());
//				} else if (field.getType().equals(IExecutionContext.class)) {
//					if (instance instanceof IExecutionContext) {
//						throw new RhenaException("Can not inject IExecutionContext into a processor of type IExecutionContext");
//					} else {
//						field.set(instance, executionContext);
//					}
//				} else {
//					throw new RhenaException("Attempting to inject a @ProcessorContext object of unknown type: " + field.getType().getName());
//				}
//				field.setAccessible(false);
//			}
//		}
//	}
//
//	private Document createDefaultContextConfiguration() throws RhenaException {
//
//		Document doc = null;
//		try {
//			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
//			fact.setNamespaceAware(true);
//			DocumentBuilder db = fact.newDocumentBuilder();
//			InputSource is = new InputSource();
//			is.setCharacterStream(new StringReader(""
//					+ "			<context module=\"com.unnsvc.rhena:lifecycle:0.0.1\" class=\"com.unnsvc.rhena.lifecycle.DefaultContext\">\n"
//					+ "				<resources>\n" + "					<main path=\"src/main/java\" />\n"
//					+ "					<main path=\"src/main/resources\" filter=\"true\" />\n" + "					<test path=\"src/test/java\" />\n"
//					+ "					<test path=\"src/test/resources\" filter=\"true\" />\n" + "				</resources>\n" + "			</context>"));
//
//			doc = db.parse(is);
//		} catch (Exception ex) {
//			throw new RhenaException(ex.getMessage(), ex);
//		}
//		return doc;
//	}

}
