
package com.unnsvc.rhena.lifecycle;

import java.io.File;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.annotation.ProcessorContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.lifecycle.IGenerator;
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
		File outputDirectory = context.getOutputDirectory(module);

		String fileName = Utils.toFileName(module.getIdentifier(), caller.getExecutionType());
		File outputLocation = new File(outputDirectory, fileName + ".jar");

		try {
			File inputLocation = new File(outputDirectory, caller.getExecutionType().literal().toLowerCase());

			JarHelper helper = new JarHelper(logger, inputLocation, outputLocation);
			helper.packageJar();
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}

		return outputLocation;
	}
}
