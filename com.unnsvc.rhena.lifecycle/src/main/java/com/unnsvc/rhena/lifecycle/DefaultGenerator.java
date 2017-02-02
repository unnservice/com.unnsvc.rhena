
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.annotation.ProcessorContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.PackagedArtifactDescriptor;
import com.unnsvc.rhena.common.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.lifecycle.IGenerator;
import com.unnsvc.rhena.common.lifecycle.IResource;
import com.unnsvc.rhena.common.logging.ELogLevel;
import com.unnsvc.rhena.common.logging.ILoggerService;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.lifecycle.helpers.JarHelper;

/**
 * @author noname
 *
 */
public class DefaultGenerator implements IGenerator {

	private static final long serialVersionUID = 1L;
	@ProcessorContext
	private IExecutionContext context;
	@ProcessorContext
	private ILoggerService logger;

	public DefaultGenerator() {

	}

	@Override
	public void configure(ICaller caller, Document configuration) {

	}

	@Override
	public List<IArtifactDescriptor> generate(ICaller caller) throws RhenaException {

		IRhenaModule module = caller.getModule();

		Set<File> compiledLocations = new HashSet<File>();
		for (IResource resource : context.getResources()) {
			if (resource.getResourceType().equals(caller.getExecutionType())) {
				// System.err.println("Packaging " + new
				// File(resource.getBaseDirectory(),
				// resource.getRelativeOutputPath()));
				compiledLocations.add(new File(resource.getBaseDirectory(), resource.getRelativeOutputPath()));
			}
		}

		Set<File> sourceLocations = new HashSet<File>();
		for (IResource resource : context.getResources()) {
			if (resource.getResourceType().equals(caller.getExecutionType())) {
				// System.err.println("Packaging " + new
				// File(resource.getBaseDirectory(),
				// resource.getRelativeOutputPath()));
				sourceLocations.add(new File(resource.getBaseDirectory(), resource.getOriginalRelativeSourcePath()));
			}
		}

		File outputDirectory = context.getOutputDirectory(module);

		String fileName = Utils.toFileName(module.getIdentifier(), caller.getExecutionType());
		File outputLocation = new File(outputDirectory, fileName + "." + getExtension());
		File sourceOutputLocation = new File(outputDirectory, fileName + "-sources." + getExtension());

		try {

			logger.fireLogEvent(ELogLevel.INFO, getClass().getName(), null, "Building: " + outputLocation, null);
			JarHelper packagingHelper = new JarHelper(logger, compiledLocations, outputLocation);
			packagingHelper.packageJar();

			
			logger.fireLogEvent(ELogLevel.INFO, getClass().getName(), null, "Building: " + sourceOutputLocation, null);
			JarHelper sourcePackagingHelper = new JarHelper(logger, sourceLocations, sourceOutputLocation);
			sourcePackagingHelper.packageJar();

		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}

		// return outputLocation;
		try {
			List<IArtifactDescriptor> results = new ArrayList<IArtifactDescriptor>();
			String outputSha1 = Utils.generateSha1(outputLocation);
			String sourceOutputSha1 = Utils.generateSha1(sourceOutputLocation);
			
			results.add(new PackagedArtifactDescriptor(IArtifactDescriptor.DEFAULT, outputLocation.getName(), outputLocation.toURI().toURL(), outputSha1));
			results.add(new PackagedArtifactDescriptor(IArtifactDescriptor.SOURCES, sourceOutputLocation.getName(), sourceOutputLocation.toURI().toURL(), sourceOutputSha1));
			return results;
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}
	}

	/**
	 * Three letter extension
	 * 
	 * @return
	 */
	public String getExtension() {

		return "jar";
	}
}
