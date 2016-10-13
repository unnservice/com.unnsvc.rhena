
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
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.ExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
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

/**
 * @author noname
 *
 */
public class WorkspaceProjectMaterialiser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;
	private IRhenaModule module;
	private ExecutionType type;

	public WorkspaceProjectMaterialiser(IResolutionContext context, IRhenaModule module, ExecutionType type) {

		this.context = context;
		this.module = module;
		this.type = type;
	}

	public IRhenaExecution materialiseExecution() throws RhenaException {

		String lifecycleName = module.getLifecycleName();
		if (lifecycleName == null) {
			lifecycleName = RhenaConstants.DEFAULT_LIFECYCLE_NAME;
		}

		ILifecycleDeclaration declaration = module.getLifecycleDeclaration(lifecycleName);
		IExecutionReference executionContextReference = declaration.getContext();
		List<IProcessorReference> processorReferences = declaration.getProcessors();
		IGeneratorReference generatorReference = declaration.getGenerator();
		return processLifecycleReferences(module, executionContextReference, processorReferences, generatorReference);
	}

	private IRhenaExecution processLifecycleReferences(IRhenaModule module, IExecutionReference contextReference, List<IProcessorReference> processorReferences,
			IGeneratorReference generatorReference) throws RhenaException {

		IExecutionContext executionContext = instantiateProcessor(module, contextReference, IExecutionContext.class);
		executionContext.configure(module, contextReference.getConfiguration());

		for (IProcessorReference pref : processorReferences) {

			IProcessor processor = instantiateProcessor(module, pref, IProcessor.class);
			processor.configure(module, pref.getConfiguration());
			processor.process(executionContext, module, type);
		}

		IGenerator generator = instantiateProcessor(module, generatorReference, IGenerator.class);
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

		l.addAll(processor.getTarget().visit(new RhenaDependencyCollectionVisitor(context, ExecutionType.FRAMEWORK, TraverseType.SCOPE)).getDependenciesURL());
		URLClassLoader dependenciesLoader = new URLClassLoader(l.toArray(new URL[l.size()]), Thread.currentThread().getContextClassLoader());
		URLClassLoader mainLoader = new URLClassLoader(new URL[] { context.materialiseExecution(processor.getTarget(), ExecutionType.FRAMEWORK).getArtifact().getArtifactUrl() }, dependenciesLoader);

//		try {
//			Class c = mainLoader.loadClass(IResolutionContext.class.getName());
//			System.err.println("loaded");
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}

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
	private <T extends ILifecycleProcessor> T instantiateProcessor(IRhenaModule model, ILifecycleProcessorReference processor, Class<T> clazzType)
			throws RhenaException {

		URLClassLoader loader = createClassloader(processor);
		try {

			Class<?> c = loader.loadClass(processor.getClazz());
			// @TODO enable constructor selection so we can pass in model if
			// such a constructor exists??? Do injection instead???
			Constructor<?> constr = c.getConstructor(IResolutionContext.class);
			Object o = constr.newInstance(context);
			return (T) o;
		} catch (Exception ex) {
			log.error(processor.getTarget().getModuleIdentifier().toTag(type) + " lifecycle classloader has " + loader.getURLs().length + " urls");
			for (URL url : loader.getURLs()) {
				log.error(processor.getTarget().getModuleIdentifier().toTag(type) + " lifecycle contains " + url);
			}
			throw new RhenaException(model.getModuleIdentifier().toTag(type) + " Failed to instantiate: " + processor.getClazz(), ex);
		}
	}

}

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
