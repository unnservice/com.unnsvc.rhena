
package com.unnsvc.rhena.core.execution;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.unnsvc.rhena.common.IResolutionContext;
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
import com.unnsvc.rhena.lifecycle.DefaultContext;
import com.unnsvc.rhena.lifecycle.DefaultGenerator;
import com.unnsvc.rhena.lifecycle.DefaultProcessor;

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

	/**
	 * @TODO remove direct instantiation of lifecycle
	 */
	public IRhenaExecution materialiseExecution() throws RhenaException {

		File generatedArtifact = null;

		if (module.getLifecycleName() != null) {

			ILifecycleDeclaration declaration = module.getLifecycleDeclaration(module.getLifecycleName());
			IExecutionReference executionContextReference = declaration.getConfigurator();
			List<IProcessorReference> processorReferences = declaration.getProcessors();
			IGeneratorReference generatorReference = declaration.getGenerator();
			return processLifecycleReferences(module, executionContextReference, processorReferences, generatorReference);
		} else {

			DefaultContext context = new DefaultContext();
			DefaultProcessor processor = new DefaultProcessor();
			DefaultGenerator generator = new DefaultGenerator();
			context.configure(module, getConfiguration(null));
			processor.configure(module, getConfiguration(null));
			processor.process(context, module, type);
			generator.configure(module, getConfiguration(null));
			generatedArtifact = generator.generate(context, module, type);
		}

		if (generatedArtifact == null || !generatedArtifact.isFile()) {
			throw new RhenaException(module.getModuleIdentifier().toTag(type) + ": generated missing or invalid artifact: " + generatedArtifact);
		}

		// @TODO calc sha1
		ArtifactDescriptor descriptor = new ArtifactDescriptor(generatedArtifact.getName(), Utils.toUrl(generatedArtifact), "sha1-not-implemented");
		return new RhenaExecution(module.getModuleIdentifier(), type, descriptor);
	}

	private IRhenaExecution processLifecycleReferences(IRhenaModule module, IExecutionReference contextReference, List<IProcessorReference> processorReferences,
			IGeneratorReference generatorReference) throws RhenaException {

		IExecutionContext executionContext = instantiateProcessor(module, contextReference, IExecutionContext.class);
		executionContext.configure(module, contextReference.getConfiguration());

		for (IProcessorReference pref : processorReferences) {

			IProcessor processor = instantiateProcessor(module, contextReference, IProcessor.class);
			processor.configure(module, pref.getConfiguration());
			processor.process(executionContext, module, type);
		}

		IGenerator generator = instantiateProcessor(module, contextReference, IGenerator.class);
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

	private Document getConfiguration(Node configNode) throws RhenaException {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

			if (configNode != null) {
				Node importedNode = document.importNode(configNode, true);
				document.appendChild(importedNode);
			}

			return document;
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}
	}

	private URLClassLoader createClassloader(ILifecycleProcessorReference processor) throws RhenaException {

		List<URL> l = new ArrayList<URL>();

		l.addAll(processor.getTarget().visit(new RhenaDependencyCollectionVisitor(context, ExecutionType.FRAMEWORK, TraverseType.NONE)).getDependenciesURL());

		URLClassLoader dependenciesLoader = new URLClassLoader(l.toArray(new URL[l.size()]), Thread.currentThread().getContextClassLoader());
		URLClassLoader mainLoader = new URLClassLoader(
				new URL[] { context.materialiseExecution(processor.getTarget(), ExecutionType.FRAMEWORK).getArtifact().getArtifactUrl() }, dependenciesLoader);

		return mainLoader;
	}

	@SuppressWarnings("unchecked")
	private <T extends ILifecycleProcessor> T instantiateProcessor(IRhenaModule model, ILifecycleProcessorReference processor, Class<T> clazzType)
			throws RhenaException {

		URLClassLoader loader = createClassloader(processor);
		try {
			Class<?> c = loader.loadClass(processor.getClazz());
			// @TODO enable constructor selection so we can pass in model if
			// such a constructor exists
			Constructor<?> constr = c.getConstructor();
			Object o = constr.newInstance();
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
