
package com.unnsvc.rhena.core.resolution;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IArtifact;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.RhenaArtifactsDescriptorParser;
import com.unnsvc.rhena.core.model.RhenaModuleParser;

/**
 * 
 * 
 * Can't really cause a path collision as we check the module.xml contents
 * before using it
 * 
 * <pre>
 * 	workspace		</some/path/repository>/com/component/module1/version/module.xml
 *  remote			</some/path/repository>/com/component/module1/version/<execution>/artifacts.xml
 *  remote			</some/path/repository>/com/component/module1/version/<execution>/component1.module1-deliverable-version.ext
 * </pre>
 * 
 * @author noname
 *
 */
public class RemoteRepository implements IRepository {

	public static final String USER_AGENT = "Rhena 1.0";
	private IRhenaContext context;
	private URI location;
	private ILogger logger;

	public RemoteRepository(IRhenaContext context, URI location) {

		this.context = context;
		this.location = location;
		this.logger = context.getLogger();
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		StringBuilder moduleDescriptorPath = new StringBuilder(getModuleBase(moduleIdentifier));
		moduleDescriptorPath.append(RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
		URI moduleDescriptor = Utils.toUri(moduleDescriptorPath.toString());

		try {
			logger.info(getClass(), moduleIdentifier, "Downloading " + moduleDescriptor);

			StringBuilder localModuleDescriptorPath = new StringBuilder(getLocalModuleBase(moduleIdentifier));
			localModuleDescriptorPath.append(RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
			File localModuleDescriptor = new File(localModuleDescriptorPath.toString()).getCanonicalFile();
			localModuleDescriptor.getParentFile().mkdirs();

			downloadArtifact(moduleIdentifier, moduleDescriptor, localModuleDescriptor);

			return new RhenaModuleParser(context, this, moduleIdentifier, localModuleDescriptor.toURI()).getModel();
		} catch (IOException e) {

			logger.debug(getClass(), moduleIdentifier, e.getMessage(), e);
			return null;
		}
	}

	public void downloadArtifact(ModuleIdentifier identifier, URI moduleDescriptor, File localModuleDescriptor) throws IOException {

		URL url = moduleDescriptor.toURL();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();

		if (responseCode != 200) {
			logger.warn(getClass(), identifier, "Response code on downloading module descriptor: " + responseCode);
			throw new IOException("HTTP status not 200: " + responseCode);
		}

		try (BufferedInputStream in = new BufferedInputStream(con.getInputStream())) {

			try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localModuleDescriptor))) {

				int buff = -1;
				while ((buff = in.read()) != -1) {

					out.write(buff);
				}
			}
		}
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, ICaller caller) throws RhenaException {

		IRhenaModule module = caller.getModule();
		ModuleIdentifier identifier = module.getIdentifier();

		StringBuilder artifactsDescriptorStr = new StringBuilder(getModuleBase(module.getIdentifier()));
		artifactsDescriptorStr.append(File.separator).append(caller.getExecutionType().literal()).append(File.separator);
//		artifactsDescriptorStr.append(RhenaConstants.ARTIFACTS_DESCRIPTOR_FILENAME);

		try {
			URI artifactsDescriptor = new URI(artifactsDescriptorStr.toString() + RhenaConstants.ARTIFACTS_DESCRIPTOR_FILENAME);

			logger.info(getClass(), identifier, "Downloading " + RhenaConstants.ARTIFACTS_DESCRIPTOR_FILENAME + " " + artifactsDescriptor);

			StringBuilder localArtifactsDescriptorPath = new StringBuilder(getLocalModuleBase(identifier));
			localArtifactsDescriptorPath.append(caller.getExecutionType().literal()).append(File.separator);
			File localModuleDescriptor = new File(localArtifactsDescriptorPath.toString(), RhenaConstants.ARTIFACTS_DESCRIPTOR_FILENAME).getCanonicalFile();
			localModuleDescriptor.getParentFile().mkdirs();

			downloadArtifact(identifier, artifactsDescriptor, localModuleDescriptor);

			IRhenaExecution execution = new RhenaArtifactsDescriptorParser(identifier, caller.getExecutionType(), localModuleDescriptor.toURI()).getExecution();

			for (IArtifactDescriptor desc : execution.getArtifacts()) {

				IArtifact primary = desc.getPrimaryArtifact();
				downloadArtifact(identifier, primary.getArtifactUrl().toURI(), new File(localArtifactsDescriptorPath.toString(), primary.getArtifactName()));
				
				IArtifact sources = desc.getSourceArtifact();
				if(sources != null) {
					downloadArtifact(identifier, sources.getArtifactUrl().toURI(), new File(localArtifactsDescriptorPath.toString(), sources.getArtifactName()));
				}
				
				IArtifact javadoc = desc.getJavadocArtifact();
				if(javadoc != null) {
					downloadArtifact(identifier, javadoc.getArtifactUrl().toURI(), new File(localArtifactsDescriptorPath.toString(), javadoc.getArtifactName()));
				}
			}

			return execution;

		} catch (IOException | URISyntaxException ioe) {

			logger.debug(getClass(), identifier, ioe.getMessage(), ioe);
			return null;
		}
	}

	private String getModuleBase(ModuleIdentifier moduleIdentifier) {

		StringBuilder moduleBase = new StringBuilder();
		moduleBase.append(location.toString()).append("/");
		moduleBase.append("main").append("/");
		moduleBase.append(moduleIdentifier.getComponentName().toString().replaceAll("\\.", "/")).append("/");
		moduleBase.append(moduleIdentifier.getModuleName()).append("/");
		moduleBase.append(moduleIdentifier.getVersion()).append("/");
		return moduleBase.toString();
	}

	private String getLocalModuleBase(ModuleIdentifier moduleIdentifier) {

		StringBuilder localBase = new StringBuilder();
		localBase.append(context.getConfig().getInstanceHome().toString()).append(File.separator);
		localBase.append("repository").append(File.separator);
		localBase.append(moduleIdentifier.getComponentName().toString().replace(".", File.separator)).append(File.separator);
		localBase.append(moduleIdentifier.getModuleName().toString()).append(File.separator);
		localBase.append(moduleIdentifier.getVersion().toString()).append(File.separator);
		return localBase.toString();
	}

	@Override
	public URI getLocation() {

		return location;
	}
}
// extends AbstractRepository {
//
// public RemoteRepository(IRhenaContext context) {
//
// super(context);
// }
//
// @Override
// public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier)
// throws RhenaException {
//
//
// return null;
// }
//
// @Override
// public IRhenaExecution materialiseExecution(IModelResolver resolver,
// IEntryPoint entryPoint) throws RhenaException {
//
//
// return null;
// }
//
// @Override
// public URI getLocation() {
//
//
// return null;
// }

// private IRhenaLoggingHandler log;
// private URI location;
//
// public RemoteRepository(IRhenaContext context, URI location) {
//
// super(context);
// this.location = location.normalize();
// this.log = context.getLogger(getClass());
// }
//
// @Override
// public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier)
// throws RhenaException {
//
// StringBuilder moduleDescriptorPath = new
// StringBuilder(getModuleBase(moduleIdentifier));
// moduleDescriptorPath.append(RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
// URI moduleDescriptor = Utils.toUri(moduleDescriptorPath.toString());
//
// if (Utils.exists(moduleDescriptor)) {
//
// return resolveModel(ModuleType.REMOTE, moduleIdentifier,
// Utils.toUri(getModuleBase(moduleIdentifier)));
// } else {
//
// log.debug(moduleIdentifier, EExecutionType.MODEL, " was not found at: " +
// moduleDescriptor.toASCIIString());
// return null;
// }
// }
//
// private String getModuleBase(ModuleIdentifier moduleIdentifier) {
//
// StringBuilder moduleBase = new StringBuilder();
// moduleBase.append(location.toString()).append("/");
// moduleBase.append(moduleIdentifier.getComponentName().toString().replaceAll("\\.",
// "/")).append("/");
// moduleBase.append(moduleIdentifier.getModuleName()).append("/");
// moduleBase.append(moduleIdentifier.getVersion()).append("/");
// return moduleBase.toString();
// }
//
// @Override
// public IRhenaExecution materialiseExecution(IRhenaModule module,
// EExecutionType type) throws RhenaException {
//
// StringBuilder base = new
// StringBuilder(getModuleBase(module.getModuleIdentifier()));
// base.append(type.toString().toLowerCase()).append("/");
//
// // ------------ @TODO clean
// StringBuilder executionDescriptorPath = new StringBuilder(base);
// executionDescriptorPath.append(RhenaConstants.EXECUTION_DESCRIPTOR_FILENAME);
// URI executionDescriptor = Utils.toUri(executionDescriptorPath.toString());
// if (!Utils.exists(executionDescriptor)) {
//
// throw new NotExistsException("Descriptor does not exist: " +
// executionDescriptor);
// }
// // ------------
//
// RhenaExecutionDescriptorParser parser = new
// RhenaExecutionDescriptorParser(getContext(), module.getModuleIdentifier(),
// type, Utils.toUri(base.toString()));
// IRhenaExecution execution = parser.getExecution();
//
// return execution;
// }
//
// // Add a publish method which can know whether this repository is local and
// // can install accordingly
//
// }
