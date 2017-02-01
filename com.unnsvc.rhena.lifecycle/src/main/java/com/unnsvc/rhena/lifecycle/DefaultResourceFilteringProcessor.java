
package com.unnsvc.rhena.lifecycle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.annotation.ProcessorContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.lifecycle.IFilteredResource;
import com.unnsvc.rhena.common.lifecycle.IProcessor;
import com.unnsvc.rhena.common.lifecycle.IResource;
import com.unnsvc.rhena.common.lifecycle.Resource;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.search.IDependencies;

/**
 * Resource filtering processor performs resource filtering and transforms
 * IFilteredResource into IResource into the context
 * 
 * @author noname
 *
 */
public class DefaultResourceFilteringProcessor implements IProcessor {

	private static final long serialVersionUID = 1L;
	@ProcessorContext
	private IExecutionContext context;

	@Override
	public void configure(ICaller caller, Document configuration) throws RhenaException {

	}

	@Override
	public void process(ICaller caller, IDependencies dependencies) throws RemoteException {

		try {
			List<IResource> resources = context.getResources();
			List<IResource> newResources = new ArrayList<IResource>();
			for (IResource resource : resources) {

				IResource newResource = resource;
				if (resource instanceof IFilteredResource) {
					IResource filtered = filterResource(caller.getModule(), (IFilteredResource) resource);
					newResource = filtered;
				}
				newResources.add(newResource);
			}
			context.setResources(newResources);
		} catch (IOException ioe) {
			throw new RhenaException(ioe.getMessage(), ioe);
		}
	}

	private IResource filterResource(IRhenaModule module, IFilteredResource unfiltered) throws IOException {

		File sourcePath = new File(unfiltered.getBaseDirectory(), unfiltered.getRelativeSourcePath()).getCanonicalFile();
		File intermediaryPath = new File(unfiltered.getBaseDirectory(), unfiltered.getRelativeIntermediaryPath()).getCanonicalFile();
		filterResources(module, sourcePath.getPath(), sourcePath, intermediaryPath);
		return new Resource(unfiltered.getResourceType(), unfiltered.getBaseDirectory(), unfiltered.getRelativeIntermediaryPath(), unfiltered.getRelativeOutputPath(), unfiltered.getRelativeSourcePath());
	}

	private void filterResources(IRhenaModule module, String basePath, File currentPath, File intermediaryPath) throws FileNotFoundException, IOException {

		if (currentPath.isDirectory()) {
			for (File contained : currentPath.listFiles()) {
				filterResources(module, basePath, contained, intermediaryPath);
			}
		} else {
			// is file so copy

			String relativePath = currentPath.getPath().substring(basePath.length());
			File targetFile = new File(intermediaryPath, relativePath);
			
			if (targetFile.lastModified() <= currentPath.lastModified()) {
				if (!targetFile.getParentFile().exists()) {
					targetFile.getParentFile().mkdirs();
				}
				copy(module, currentPath, targetFile);
			}
		}
	}

	private void copy(IRhenaModule module, File currentPath, File targetFile) throws FileNotFoundException, IOException {

//		System.err.println("Filtering " + currentPath + " to " + targetFile);
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(currentPath))) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int buff = -1;
			while ((buff = bis.read()) != -1) {
				baos.write(buff);
			}
			String read = new String(baos.toByteArray(), Charset.forName("UTF-8"));
			Properties props = module.getProperties();

			for (Object keyObj : props.keySet()) {
				String key = (String) keyObj;
				String value = (String) props.getProperty(key);
				read = read.replace("${" + key + "}", value);
			}

			try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile))) {

				bos.write(read.getBytes(), 0, read.getBytes().length);
				bos.flush();
			}
		}
	}
}
