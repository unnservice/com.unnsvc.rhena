
package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.annotation.ProcessorContext;
import com.unnsvc.rhena.common.lifecycle.IExecutionContext;

/**
 * @author noname
 *
 */
public class DefaultGenerator { //implements IGenerator {

	private static final long serialVersionUID = 1L;
	@ProcessorContext
	private IExecutionContext context;
//	@ProcessorContext
//	private ILoggerService logger;

	public DefaultGenerator() {

	}
//
//	@Override
//	public void configure(ICaller caller, Document configuration) {
//
//	}
//
//	@Override
//	public List<IArtifactDescriptor> generate(ICaller caller) throws RhenaException {
//
//		IRhenaModule module = caller.getModule();
//
//		Set<File> compiledLocations = new HashSet<File>();
//		for (IResource resource : context.getResources()) {
//			if (resource.getResourceType().equals(caller.getExecutionType())) {
//				// System.err.println("Packaging " + new
//				// File(resource.getBaseDirectory(),
//				// resource.getRelativeOutputPath()));
//				compiledLocations.add(new File(resource.getBaseDirectory(), resource.getRelativeOutputPath()));
//			}
//		}
//
//		Set<File> sourceLocations = new HashSet<File>();
//		for (IResource resource : context.getResources()) {
//			if (resource.getResourceType().equals(caller.getExecutionType())) {
//				// System.err.println("Packaging " + new
//				// File(resource.getBaseDirectory(),
//				// resource.getRelativeOutputPath()));
//				sourceLocations.add(new File(resource.getBaseDirectory(), resource.getOriginalRelativeSourcePath()));
//			}
//		}
//
//		File outputDirectory = context.getOutputDirectory(module);
//
//		String fileName = LifecycleUtils.toFileName(module.getIdentifier(), caller.getExecutionType());
//		File outputLocation = new File(outputDirectory, fileName + "." + getExtension());
//		File sourceOutputLocation = new File(outputDirectory, fileName + "-sources." + getExtension());
//
//		try {
//
////			logger.fireLogEvent(ELogLevel.INFO, getClass().getName(), null, "Building: " + outputLocation, null);
//			JarHelper packagingHelper = new JarHelper(compiledLocations, outputLocation);
//			packagingHelper.packageJar();
//
////			logger.fireLogEvent(ELogLevel.INFO, getClass().getName(), null, "Building: " + sourceOutputLocation, null);
//			JarHelper sourcePackagingHelper = new JarHelper(sourceLocations, sourceOutputLocation);
//			sourcePackagingHelper.packageJar();
//
//		} catch (Exception ex) {
//			throw new RhenaException(ex.getMessage(), ex);
//		}
//
//		// return outputLocation;
//		try {
//			List<IArtifactDescriptor> results = new ArrayList<IArtifactDescriptor>();
//			String outputSha1 = Utils.generateSha1(outputLocation);
//			String sourceOutputSha1 = Utils.generateSha1(sourceOutputLocation);
//
//			PackagedArtifact primaryArtifact = new PackagedArtifact(outputLocation.getName(), outputLocation.toURI().toURL(), outputSha1);
//			PackagedArtifact sourcesArtifact = new PackagedArtifact(sourceOutputLocation.getName(), sourceOutputLocation.toURI().toURL(), sourceOutputSha1);
//
//			IArtifactDescriptor descriptor = new ArtifactDescriptor(IArtifactDescriptor.DEFAULT_CLASSIFIER, primaryArtifact, sourcesArtifact, null);
//
//			results.add(descriptor);
//			return results;
//		} catch (Exception ex) {
//			throw new RhenaException(ex.getMessage(), ex);
//		}
//	}
//
//	/**
//	 * Three letter extension
//	 * 
//	 * @return
//	 */
//	public String getExtension() {
//
//		return "jar";
//	}
}
