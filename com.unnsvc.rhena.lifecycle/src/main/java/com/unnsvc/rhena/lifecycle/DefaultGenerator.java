
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.annotation.ProcessorContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.lifecycle.IGenerator;
import com.unnsvc.rhena.common.lifecycle.IResource;
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
	public File generate(ICaller caller) throws RhenaException {

		IRhenaModule module = caller.getModule();

		
		Set<File> toPackage = new HashSet<File>();
		for(IResource resource  : context.getResources()) {
			if(resource.getResourceType().equals(caller.getExecutionType())) {
//				System.err.println("Packaging " + new File(resource.getBaseDirectory(), resource.getRelativeOutputPath()));
				toPackage.add(new File(resource.getBaseDirectory(), resource.getRelativeOutputPath()));
			}
		}
		
		File outputDirectory = context.getOutputDirectory(module);
		
		String fileName = Utils.toFileName(module.getIdentifier(), caller.getExecutionType());
		File outputLocation = new File(outputDirectory, fileName + "." + getExtension());

		try {
//			File inputLocation = new File(outputDirectory, caller.getExecutionType().literal().toLowerCase());

			JarHelper packagingHelper = new JarHelper(logger, toPackage, outputLocation);
			packagingHelper.packageJar();
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}

		return outputLocation;
	}
	
	/**
	 * Three letter extension
	 * @return
	 */
	public String getExtension() {
		
		return "jar";
	}
}
