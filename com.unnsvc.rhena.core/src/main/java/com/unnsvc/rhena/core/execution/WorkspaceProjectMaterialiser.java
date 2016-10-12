
package com.unnsvc.rhena.core.execution;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ExecutionType;
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

public class WorkspaceProjectMaterialiser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;
	private ExecutionType type;

	public WorkspaceProjectMaterialiser(IResolutionContext context, ExecutionType type) {

		this.context = context;
		this.type = type;
	}

	/**
	 * @TODO DRRRRRRYYYYYYYY
	 */
	public RhenaExecution materialiseExecution(IRhenaModule model) throws RhenaException {

		ILifecycleDeclaration lifecycleDeclaration = model.getLifecycleDeclarations().get(model.getLifecycleName());
		if (lifecycleDeclaration == null) {
			log.debug(model.getModuleIdentifier().toTag() + ": no lifecycle declaration found, using framework default.");

			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.newDocument();

				DefaultConfigurator configurator = new DefaultConfigurator();
				configurator.configure(document);

				DefaultProcessor processor = new DefaultProcessor();
				processor.configure(document);
				processor.process(model, configurator);

				DefaultGenerator generator = new DefaultGenerator();
				generator.configure(document);
				File artifact = generator.generate(model, configurator);

				if (artifact == null) {
					throw new RhenaException(model.getModuleIdentifier().toTag() + ":generator " + generator.getClass().getName() + " produced null artifact.");
				} else if (!artifact.exists() || !artifact.isFile()) {
					throw new RhenaException(model.getModuleIdentifier().toTag() + ":generator " + generator.getClass().getName()
							+ " produced an artifact which is either not a file, or does not exist: " + artifact);
				}

				return new RhenaExecution(model.getModuleIdentifier(), type, artifact);

			} catch (ParserConfigurationException e) {

				throw new RhenaException(e.getMessage(), e);
			}

		} else {
			//
			IConfiguratorReference configuratorReference = lifecycleDeclaration.getConfigurator();
			List<IProcessorReference> processorReferences = lifecycleDeclaration.getProcessors();
			IGeneratorReference generatorReference = lifecycleDeclaration.getGenerator();

			IConfigurator configurator = instantiateProcessor(model, configuratorReference, IConfigurator.class);
			configurator.configure(configuratorReference.getConfiguration());

			for (IProcessorReference pref : processorReferences) {

				IProcessor processor = instantiateProcessor(model, configuratorReference, IProcessor.class);
				processor.configure(pref.getConfiguration());
				processor.process(model, configurator);
			}

			IGenerator generator = instantiateProcessor(model, configuratorReference, IGenerator.class);
			generator.configure(generatorReference.getConfiguration());
			File artifact = generator.generate(model, configurator);

			if (artifact == null) {
				throw new RhenaException(model.getModuleIdentifier().toTag() + ":generator " + generator.getClass().getName() + " produced null artifact.");
			} else if (!artifact.exists() || !artifact.isFile()) {
				throw new RhenaException(model.getModuleIdentifier().toTag() + ":generator " + generator.getClass().getName()
						+ " produced an artifact which is either not a file, or does not exist: " + artifact);
			}

			return new RhenaExecution(model.getModuleIdentifier(), type, artifact);
		}
	}

	private URLClassLoader createClassloader(ILifecycleProcessorReference processor) throws RhenaException {

		List<URL> l = new ArrayList<URL>();

		l.addAll(processor.getTarget().visit(new RhenaDependencyCollectionVisitor(context, ExecutionType.FRAMEWORK, TraverseType.NONE)).getDependenciesURL());

		URLClassLoader dependenciesLoader = new URLClassLoader(l.toArray(new URL[l.size()]), Thread.currentThread().getContextClassLoader());
		URLClassLoader mainLoader = new URLClassLoader(new URL[] { context.materialiseExecution(processor.getTarget(), ExecutionType.FRAMEWORK).getArtifactURL() }, dependenciesLoader);

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
