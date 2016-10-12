
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
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaModule;
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
import com.unnsvc.rhena.core.model.RhenaExecution;
import com.unnsvc.rhena.lifecycle.DefaultConfigurator;
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
	 * @TODO clean up, DRRRRRRYYYYYYYY, and fix logic
	 * @TODO caching of lifecycle building blocks so they can be reused....
	 */
	public IRhenaExecution materialiseExecution() throws RhenaException {

		File generatedArtifact = null;

		if (module.getLifecycleName() != null) {

			ILifecycleDeclaration declaration = module.getLifecycleDeclaration(module.getLifecycleName());
			IConfiguratorReference configuratorReference = declaration.getConfigurator();
			List<IProcessorReference> processorReferences = declaration.getProcessors();
			IGeneratorReference generatorReference = declaration.getGenerator();
			return processLifecycleReferences(module, configuratorReference, processorReferences, generatorReference);
		} else {

			DefaultConfigurator configurator = new DefaultConfigurator();
			DefaultProcessor processor = new DefaultProcessor();
			DefaultGenerator generator = new DefaultGenerator();
			configurator.configure(getConfiguration(null), type);
			processor.configure(getConfiguration(null), type);
			processor.process(module, configurator);
			generator.configure(getConfiguration(null), type);
			generatedArtifact = generator.generate(module, configurator);
		}

		if (generatedArtifact == null || !generatedArtifact.isFile()) {
			throw new RhenaException(module.getModuleIdentifier().toTag() + ": generated missing or invalid artifact: " + generatedArtifact);
		}

		return new RhenaExecution(module.getModuleIdentifier(), type, generatedArtifact);
	}

	private IRhenaExecution processLifecycleReferences(IRhenaModule module, IConfiguratorReference configuratorReference,
			List<IProcessorReference> processorReferences, IGeneratorReference generatorReference) throws RhenaException {

		IConfigurator configurator = instantiateProcessor(module, configuratorReference, IConfigurator.class);
		configurator.configure(configuratorReference.getConfiguration(), type);

		for (IProcessorReference pref : processorReferences) {

			IProcessor processor = instantiateProcessor(module, configuratorReference, IProcessor.class);
			processor.configure(pref.getConfiguration(), type);
			processor.process(module, configurator);
		}

		IGenerator generator = instantiateProcessor(module, configuratorReference, IGenerator.class);
		generator.configure(generatorReference.getConfiguration(), type);
		File artifact = generator.generate(module, configurator);

		if (artifact == null) {
			throw new RhenaException(module.getModuleIdentifier().toTag() + ":generator " + generator.getClass().getName() + " produced null artifact.");
		} else if (!artifact.exists() || !artifact.isFile()) {
			throw new RhenaException(module.getModuleIdentifier().toTag() + ":generator " + generator.getClass().getName()
					+ " produced an artifact which is either not a file, or does not exist: " + artifact);
		}

		return new RhenaExecution(module.getModuleIdentifier(), type, artifact);
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
				new URL[] { context.materialiseExecution(processor.getTarget(), ExecutionType.FRAMEWORK).getArtifactURL() }, dependenciesLoader);

		return mainLoader;
	}

	@SuppressWarnings("unchecked")
	private <T extends ILifecycleProcessor> T instantiateProcessor(IRhenaModule model, ILifecycleProcessorReference processor, Class<T> type)
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
			for (URL url : loader.getURLs()) {
				log.error(processor.getTarget().getModuleIdentifier().toTag() + ": classloader has " + url);
			}
			throw new RhenaException(model.getModuleIdentifier().toTag() + ": Failed to instantiate: " + processor.getClazz(), ex);
		}
	}

}
