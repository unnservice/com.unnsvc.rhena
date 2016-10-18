
package com.unnsvc.rhena.core.execution;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.logging.IRhenaLogger;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionReference;
import com.unnsvc.rhena.common.model.lifecycle.IGenerator;
import com.unnsvc.rhena.common.model.lifecycle.IGeneratorReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessor;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.common.visitors.RhenaDependencyCollectionVisitor;
import com.unnsvc.rhena.lifecycle.DefaultContext;
import com.unnsvc.rhena.lifecycle.DefaultGenerator;
import com.unnsvc.rhena.lifecycle.DefaultProcessor;

/**
 * @author noname
 *
 */
public class WorkspaceProjectMaterialiser {

	private IRhenaLogger log;
	private IRhenaContext context;

	public WorkspaceProjectMaterialiser(IRhenaContext context) {

		this.context = context;
		this.log = context.getLogger(getClass());
	}

	public IRhenaExecution materialiseExecution(IRhenaModule module, EExecutionType type) throws RhenaException {

		String lifecycleName = module.getLifecycleName();

		// lifecycleName = RhenaConstants.DEFAULT_LIFECYCLE_NAME;
		/**
		 * @TODO I don't think it's right to use a null lifecycle as a default,
		 *       but until a better strategy is thought up, because adding the
		 *       lifecycle into the model would cause circular dependency
		 *       lifecycleModule->defaultLifecycle->lifecycleModule
		 */

		if (module.getLifecycleName() != null) {
			ILifecycleDeclaration declaration = module.getLifecycleDeclaration(lifecycleName);
			IExecutionReference executionContextReference = declaration.getContext();
			List<IProcessorReference> processorReferences = declaration.getProcessors();
			IGeneratorReference generatorReference = declaration.getGenerator();

			return processUsingLifecycleReferences(module, type, declaration.getName(), executionContextReference, processorReferences, generatorReference);
		} else {
			return processUsingDefaultLifecycle(module, type);
		}
	}

	private IRhenaExecution processUsingDefaultLifecycle(IRhenaModule module, EExecutionType type) throws RhenaException {

		DefaultContext contextProc = new DefaultContext(context);
		contextProc.configure(module, Utils.newEmptyDocument());
		validateContext(RhenaConstants.DEFAULT_LIFECYCLE_NAME, contextProc);

		DefaultProcessor procProc = new DefaultProcessor(context);
		procProc.configure(module, Utils.newEmptyDocument());
		procProc.process(contextProc, module, type);

		DefaultGenerator genProc = new DefaultGenerator(context);
		genProc.configure(module, Utils.newEmptyDocument());
		File result = genProc.generate(contextProc, module, type);

		return new RhenaExecution(module.getModuleIdentifier(), type, new ArtifactDescriptor(result.getName(), Utils.toUrl(result), "not-implemented"));
	}

	private void validateContext(String lifecycleName, IExecutionContext contextProc) throws RhenaException {

		for (EExecutionType type : EExecutionType.values()) {

			if (contextProc.getResources(type) == null) {
				if (type != EExecutionType.MODEL) {
					throw new RhenaException(
							"Lifecycle context \"" + lifecycleName + "\", is invalid. Missing resources for execution type: " + type.literal());
				}
			}
		}
	}

	private IRhenaExecution processUsingLifecycleReferences(IRhenaModule module, EExecutionType type, String lifecycleName,
			IExecutionReference contextReference, List<IProcessorReference> processorReferences, IGeneratorReference generatorReference) throws RhenaException {

		IExecutionContext executionContext = instantiateProcessor(module, contextReference, IExecutionContext.class, type);
		executionContext.configure(module, contextReference.getConfiguration());
		validateContext(lifecycleName, executionContext);

		for (IProcessorReference pref : processorReferences) {

			IProcessor processor = instantiateProcessor(module, pref, IProcessor.class, type);
			processor.configure(module, pref.getConfiguration());
			processor.process(executionContext, module, type);
		}

		IGenerator generator = instantiateProcessor(module, generatorReference, IGenerator.class, type);
		generator.configure(module, generatorReference.getConfiguration());
		File artifact = generator.generate(executionContext, module, type);

		if (artifact == null) {
			throw new RhenaException(module.getModuleIdentifier().toTag(type) + " " + generator.getClass().getName() + " produced null artifact.");
		} else if (!artifact.exists() || !artifact.isFile()) {
			throw new RhenaException(module.getModuleIdentifier().toTag(type) + " " + generator.getClass().getName()
					+ " produced an artifact which is either not a file, or does not exist: " + artifact);
		}

		ArtifactDescriptor descriptor = new ArtifactDescriptor(artifact.getName(), Utils.toUrl(artifact), "sha1-not-implemented");
		return new RhenaExecution(module.getModuleIdentifier(), type, descriptor);
	}

	private URLClassLoader createClassloader(ILifecycleProcessorReference processor) throws RhenaException {

		List<URL> l = new ArrayList<URL>();

		l.addAll(processor.getModuleEdge().getTarget().visit(new RhenaDependencyCollectionVisitor(context, EExecutionType.FRAMEWORK, TraverseType.SCOPE)).getDependenciesURL());
		URLClassLoader dependenciesLoader = new URLClassLoader(l.toArray(new URL[l.size()]), Thread.currentThread().getContextClassLoader());
		URLClassLoader mainLoader = new URLClassLoader(
				new URL[] { context.materialiseExecution(processor.getModuleEdge().getTarget(), EExecutionType.FRAMEWORK).getArtifact().getArtifactUrl() },
				dependenciesLoader);

		// try {
		// Class c = mainLoader.loadClass(IResolutionContext.class.getName());
		// System.err.println("loaded");
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }

		// work out some method to enable classloader debugging during usage

		return mainLoader;
	}

	/**
	 * @TODO throw rhena exception if the constructor we require does not
	 *       exist..
	 * @param model
	 * @param processor
	 * @param clazzType
	 * @return
	 * @throws RhenaException
	 */
	@SuppressWarnings("unchecked")
	private <T extends ILifecycleProcessor> T instantiateProcessor(IRhenaModule model, ILifecycleProcessorReference processor, Class<T> clazzType,
			EExecutionType type) throws RhenaException {

		URLClassLoader loader = createClassloader(processor);
		try {

			Class<?> c = loader.loadClass(processor.getClazz());
			// @TODO enable constructor selection so we can pass in model if
			// such a constructor exists??? Do injection instead???

			Constructor<?> constr = c.getConstructor(IRhenaContext.class);
			Object o = constr.newInstance(context);
			return (T) o;
		} catch (Throwable ex) {

			String tag = processor.getModuleEdge().getTarget().getModuleIdentifier().toTag(type);
			debugClassloader(model.getModuleIdentifier(), tag, "dependencies", (URLClassLoader) loader.getParent());
			debugClassloader(model.getModuleIdentifier(), tag, "lifecycle", loader);

			throw new RhenaException(model.getModuleIdentifier().toTag(type) + " Failed to instantiate: " + processor.getClazz(), ex);
		}
	}

	private void debugClassloader(ModuleIdentifier identifier, String moduleTag, String loaderName, URLClassLoader loader) {

		for (URL url : loader.getURLs()) {

			log.error(identifier, EExecutionType.MODEL, moduleTag + " Classloader [" + loaderName + "] contains: " + url);
		}
	}

}
// private ILifecycleDeclaration getDefaultLifecycle() throws RhenaException {
//
// LifecycleDeclaration decl = new
// LifecycleDeclaration(RhenaConstants.DEFAULT_LIFECYCLE_NAME);
//
// decl.setContext(new ContextReference(getDefaultProcessorEdge(),
// DefaultContext.class.getName(), null, Utils.newEmptyDocument()));
// decl.addProcessor(new ProcessorReference(getDefaultProcessorEdge(),
// DefaultProcessor.class.getName(), null, Utils.newEmptyDocument()));
// decl.setGenerator(new GeneratorReference(getDefaultProcessorEdge(),
// DefaultGenerator.class.getName(), null, Utils.newEmptyDocument()));
//
// return decl;
// }
//
// private IRhenaEdge getDefaultProcessorEdge() throws RhenaException {
//
// return new RhenaEdge(EExecutionType.FRAMEWORK, new
// RhenaReference(ModuleIdentifier.valueOf("com.unnsvc.rhena:lifecycle:0.0.1")),
// TraverseType.SCOPE);
// }

// private Document getConfiguration(Node configNode) throws RhenaException {
//
// try {
// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
// factory.setNamespaceAware(true);
// DocumentBuilder builder = factory.newDocumentBuilder();
// Document document = builder.newDocument();
//
// if (configNode != null) {
// Node importedNode = document.importNode(configNode, true);
// document.appendChild(importedNode);
// }
//
// return document;
// } catch (Exception ex) {
// throw new RhenaException(ex.getMessage(), ex);
// }
// }
