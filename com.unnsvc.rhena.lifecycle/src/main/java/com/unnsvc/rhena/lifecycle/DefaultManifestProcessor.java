
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.annotation.ProcessorContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.lifecycle.IProcessor;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.search.IDependencies;

/**
 * Produces a MANIFEST.MF
 * 
 * @author noname
 *
 */
public class DefaultManifestProcessor implements IProcessor {

	private static final long serialVersionUID = 1L;
	@ProcessorContext
	private IExecutionContext context;

	public DefaultManifestProcessor() {

	}

	@Override
	public void configure(ICaller caller, Document configuration) throws RhenaException {

	}

	@Override
	public void process(ICaller caller, IDependencies dependencies) throws RhenaException {

		IRhenaModule module = caller.getModule();
		File outputDirectory = new File(context.getOutputDirectory(module), caller.getExecutionType().literal().toLowerCase());
		outputDirectory.mkdirs();

		File metainfDirectory = new File(outputDirectory, "META-INF");
		if (!metainfDirectory.isDirectory()) {
			metainfDirectory.mkdirs();
		}
		File manifestFile = new File(metainfDirectory, "MANIFEST.MF");
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		manifest.getMainAttributes().put(new Attributes.Name("Rhena-Version"), RhenaConstants.RHENA_VERSION);
		try (FileOutputStream fos = new FileOutputStream(manifestFile)) {
			manifest.write(fos);
			fos.flush();
		} catch (IOException ioe) {
			throw new RhenaException(ioe.getMessage(), ioe);
		}
	}

}
