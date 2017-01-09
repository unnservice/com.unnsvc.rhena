
package com.unnsvc.rhena.lifecycle.helpers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarHelper {

	private File basePath;
	private File targetJar;

	/**
	 * 
	 * @param basePath
	 * @param targetJar
	 * @param manifestFile
	 *            optional, can be null
	 */
	public JarHelper(File basePath, File targetJar) {

		this.basePath = basePath;
		this.targetJar = targetJar;
	}

	public void packageJar() throws FileNotFoundException, IOException {

		Manifest manifest = null;
		// try seeing if there's a manifest in what we're packaging
		File manifestFile = new File(basePath, "META-INF/MANIFEST.MF");
		if (manifestFile.exists()) {
			try (FileInputStream manifestIs = new FileInputStream(manifestFile)) {
				manifest = new Manifest(manifestIs);
			}
		}

		try (FileOutputStream targetJarOs = new FileOutputStream(targetJar)) {
			try (JarOutputStream jos = manifest == null ? new JarOutputStream(targetJarOs) : new JarOutputStream(targetJarOs, manifest)) {
				for (File contained : basePath.getAbsoluteFile().listFiles()) {

					addToJar(basePath.getAbsoluteFile(), contained, jos);
				}
			}
		}
	}

	private void addToJar(File basePath, File currentPath, JarOutputStream output) throws IOException {

		// normalize paths
		String relativePath = currentPath.getAbsolutePath().substring(basePath.getAbsolutePath().length() + 1).replace("\\", "/");

		if (currentPath.isDirectory()) {
			relativePath += "/";
			System.err.println("Add to jar: " + relativePath);

			JarEntry entry = new JarEntry(relativePath);
			entry.setTime(currentPath.lastModified());
			output.putNextEntry(entry);
			output.closeEntry();
			for (File contained : currentPath.listFiles()) {
				addToJar(basePath, contained, output);
			}
		} else if (currentPath.isFile() && !relativePath.equals("META-INF/MANIFEST.MF")) {
			System.err.println("Add to jar: " + relativePath);

			JarEntry entry = new JarEntry(relativePath);
			entry.setTime(currentPath.lastModified());
			output.putNextEntry(entry);
			try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(currentPath))) {
				int buff = -1;
				while ((buff = bis.read()) != -1) {
					output.write(buff);
				}
			}
			output.closeEntry();
		}
	}

	// private void addToJar2(String base, File source, JarOutputStream target)
	// throws IOException {
	//
	// if (source.isDirectory()) {
	// String name = source.getPath().replace("\\", "/").substring(base.length()
	// + 1);
	// if (!name.isEmpty()) {
	// if (!name.endsWith("/")) {
	// name += "/";
	// }
	// JarEntry entry = new JarEntry(name);
	// entry.setTime(source.lastModified());
	// target.putNextEntry(entry);
	// target.closeEntry();
	// }
	// for (File nestedFile : source.listFiles()) {
	//
	// addToJar(base, nestedFile, target);
	// }
	// return;
	// }
	//
	// JarEntry entry = new JarEntry(source.getPath().replace("\\",
	// "/").substring(base.length() + 1));
	// entry.setTime(source.lastModified());
	// target.putNextEntry(entry);
	// try (BufferedInputStream in = new BufferedInputStream(new
	// FileInputStream(source))) {
	//
	// byte[] buffer = new byte[1024];
	// while (true) {
	// int count = in.read(buffer);
	// if (count == -1)
	// break;
	// target.write(buffer, 0, count);
	// }
	// }
	// target.closeEntry();
	// }
}
