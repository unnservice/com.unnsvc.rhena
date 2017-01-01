
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

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
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

		File inputDirectory = new File(module.getLocation().getPath(), RhenaConstants.DEFAULT_OUTPUT_DIRECTORY_NAME + "/" + type.literal().toLowerCase());
		if (!inputDirectory.isDirectory()) {
			inputDirectory.mkdirs();
		}

		// List<IResource> res = context.getResources(type);

		String fileName = toFileName(module.getIdentifier(), type);
		File targetLocation = new File(module.getLocation().getPath(), "target");
		File outLocation = new File(targetLocation, fileName + ".jar");

		try {
			generateJar(inputDirectory, outLocation);
			// log.debug(module.get(), type, "Generated: " +
			// outLocation.getAbsolutePath());
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}

		return outLocation;
	}

	private String toFileName(ModuleIdentifier identifier, EExecutionType type) {

		return identifier.getComponentName().toString() + "." + identifier.getModuleName().toString() + "-" + type.toString().toLowerCase() + "-"
				+ identifier.getVersion().toString();
	}

	private void generateJar(File inputDirectory, File outLocation) throws FileNotFoundException, IOException {

		File outDir = outLocation.getParentFile();
		if (!outDir.exists()) {
			outDir.mkdirs();
		}

		List<File> added = new ArrayList<File>();

		JarOutputStream jos = new JarOutputStream(new FileOutputStream(outLocation));

		String base = inputDirectory.toString();
		if (inputDirectory.isDirectory()) {
			for (File contained : inputDirectory.listFiles()) {
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
