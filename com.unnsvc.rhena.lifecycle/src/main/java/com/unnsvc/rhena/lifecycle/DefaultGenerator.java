
package com.unnsvc.rhena.lifecycle;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IGenerator;
import com.unnsvc.rhena.common.model.lifecycle.IResource;

public class DefaultGenerator implements IGenerator {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void configure(IRhenaModule module, Document configuration) {

	}

	@Override
	public File generate(IExecutionContext context, IRhenaModule module, ExecutionType type) throws RhenaException {

		List<IResource> resources = context.getResources(type);

		String fileName = module.getModuleIdentifier().toFileName(type);
		File targetLocation = new File(module.getLocation().getPath(), "target");
		File outLocation = new File(targetLocation, fileName + ".jar");

		try {
			generateJar(resources, outLocation);
			log.debug(module.getModuleIdentifier().toTag(type) + " Generated: " + outLocation.getAbsolutePath());
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}

		return outLocation;
	}

	private void generateJar(List<IResource> resources, File outLocation) throws FileNotFoundException, IOException {

		File outDir = outLocation.getParentFile();
		if (!outDir.exists()) {
			outDir.mkdirs();
		}

		List<File> added = new ArrayList<File>();

		JarOutputStream jos = new JarOutputStream(new FileOutputStream(outLocation));
		for (IResource resource : resources) {

			File target = resource.getTarget();
			String base = target.toString();
			if (target.isDirectory()) {
				for (File contained : target.listFiles()) {
					if (!added.contains(target)) {
						addToJar(base, contained, jos);
						added.add(target);
					}
				}
			}
		}
		jos.close();
	}

	private void addToJar(String base, File source, JarOutputStream target) throws IOException {

		BufferedInputStream in = null;
		try {
			if (source.isDirectory()) {
				String name = source.getPath().replace("\\", "/").substring(base.length() + 1);
				if (!name.isEmpty()) {
					if (!name.endsWith("/"))
						name += "/";
					JarEntry entry = new JarEntry(name);
					entry.setTime(source.lastModified());
					target.putNextEntry(entry);
					target.closeEntry();
				}
				for (File nestedFile : source.listFiles())
					addToJar(base, nestedFile, target);
				return;
			}

			JarEntry entry = new JarEntry(source.getPath().replace("\\", "/").substring(base.length() + 1));
			entry.setTime(source.lastModified());
			target.putNextEntry(entry);
			in = new BufferedInputStream(new FileInputStream(source));

			byte[] buffer = new byte[1024];
			while (true) {
				int count = in.read(buffer);
				if (count == -1)
					break;
				target.write(buffer, 0, count);
			}
			target.closeEntry();
		} finally {
			if (in != null)
				in.close();
		}
	}
}
