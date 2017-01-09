
package com.unnsvc.rhena.lifecycle;

import java.io.File;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IGenerator;
import com.unnsvc.rhena.lifecycle.helpers.JarHelper;

/**
 * @author noname
 *
 */
public class DefaultGenerator implements IGenerator {

	private IExecutionContext context;

	public DefaultGenerator(IRhenaCache cache, IExecutionContext context) {

		this.context = context;
	}

	@Override
	public void configure(IRhenaModule module, Document configuration) {

	}

	@Override
	public File generate(IExecutionContext context, IRhenaModule module, EExecutionType type) throws RhenaException {

		File outputDirectory = context.getOutputDirectory(module);

		String fileName = Utils.toFileName(module.getIdentifier(), type);
		File outputLocation = new File(outputDirectory, fileName + ".jar");

		try {
			File inputLocation = new File(outputDirectory, type.literal().toLowerCase());

			JarHelper helper = new JarHelper(inputLocation, outputLocation);
			helper.packageJar();
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}

		return outputLocation;
	}
}
