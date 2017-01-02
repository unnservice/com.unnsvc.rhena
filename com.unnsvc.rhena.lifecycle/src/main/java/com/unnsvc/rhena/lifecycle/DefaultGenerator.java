
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
import java.util.jar.Manifest;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IGenerator;

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

		// List<IResource> res = context.getResources(type);

		String fileName = Utils.toFileName(module.getIdentifier(), type);
		File outLocation = new File(outputDirectory, fileName + ".jar");

		try {
			File inputLocation = new File(outputDirectory, type.literal().toLowerCase());
			generateJar(inputLocation, outLocation);
			// log.debug(module.get(), type, "Generated: " +
			// outLocation.getAbsolutePath());
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}

		return outLocation;
	}

	private void generateJar(File inputDirectory, File outLocation) throws FileNotFoundException, IOException {

		List<File> added = new ArrayList<File>();
		JarOutputStream jos = null;

		File manifestFile = new File(inputDirectory, "META-INF/MANIFEST.MF");
		if (manifestFile.isFile()) {

			try (FileInputStream manifestIs = new FileInputStream(manifestFile)) {
				jos = new JarOutputStream(new FileOutputStream(outLocation), new Manifest(manifestIs));
			}
		} else {
			jos = new JarOutputStream(new FileOutputStream(outLocation));
		}

		String base = inputDirectory.toString();
		if (inputDirectory.isDirectory()) {
			for (File contained : inputDirectory.listFiles()) {
				// @TODO why "added" check? Seems to go into an infinite loop
				// otherwise
				if (!added.contains(inputDirectory)) {
					addToJar(base, contained, jos);
					added.add(inputDirectory);
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
				for (File nestedFile : source.listFiles()) {

					addToJar(base, nestedFile, target);
				}
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
