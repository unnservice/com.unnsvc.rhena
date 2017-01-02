
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IJavaProcessor;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;
import com.unnsvc.rhena.common.visitors.IDependencies;

public class DefaultProcessor implements IProcessor, IJavaProcessor {

	private IExecutionContext context;
	private IRhenaCache cache;

	public DefaultProcessor(IRhenaCache cache, IExecutionContext context) {

		this.context = context;
		this.cache = cache;
	}

	@Override
	public void configure(IRhenaModule module, Document configuration) {

	}

	/**
	 * @throws RhenaException
	 * @TODO batch instead of running for each resource
	 */
	@Override
	public void process(IExecutionContext context, IRhenaModule module, EExecutionType type, IDependencies dependencies) throws RhenaException {

		File outputDirectory = new File(module.getLocation().getPath(), RhenaConstants.DEFAULT_OUTPUT_DIRECTORY_NAME + "/" + type.literal().toLowerCase());
		if (!outputDirectory.isDirectory()) {
			outputDirectory.mkdirs();
		}

		List<String> options = new ArrayList<String>();
		options.add("-d");
		options.add(outputDirectory.getAbsolutePath());

		List<File> resources = context.selectResources(type, "^.*\\.java$");

		if (resources.isEmpty()) {
			// @TODO WARN LOGGING, NO RESOURCES SELECTED
			return;
		}

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

		Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(resources);
		compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits1).call();

		// compile(module, resources, type, dependencies);

		// // System.err.println("Context is " + context + " type is " + type);
		// EResourceType restype = type.equals(EExecutionType.MAIN) ?
		// EResourceType.MAIN : EResourceType.TEST;
		// for (IResource resource :
		// context.getResources().getResources(restype)) {
		//
		// if (resource.getSourcePath().listFiles().length > 0) {
		// if (!resource.getTargetFile().exists()) {
		// resource.getTargetFile().mkdirs();
		// }
		// compile(module, resource, type, dependencies);
		// } else {
		// // log.debug(module.getIdentifier(), type, "skipping empty
		// // resource: " + resource);
		// }
		// }
	}

	// private void compile(IRhenaModule module, List<File> resources,
	// EExecutionType type, IDependencies dependencies) throws RhenaException {
	//
	// CompilationProgress progress = new CompilationProgress() {
	//
	// @Override
	// public void begin(int remainingWork) {
	//
	// }
	//
	// @Override
	// public void done() {
	//
	// }
	//
	// @Override
	// public boolean isCanceled() {
	//
	// return false;
	// }
	//
	// @Override
	// public void setTaskName(String name) {
	//
	// }
	//
	// @Override
	// public void worked(int workIncrement, int remainingWork) {
	//
	// }
	// };
	//
	// File source = resource.getSourceFile();
	// File target = resource.getTargetFile();
	//
	// // System.err.println("Compiling path " + source);
	// // System.err.println("Compiling with classpath: " +
	// // dependencies.getAsClasspath());
	// String cmdline = "-1.8 -classpath " + dependencies.getAsClasspath() + "
	// -d " + target + " " + source;
	// // System.err.println("Compiling with cmdline: " + cmdline);
	//
	// if (!BatchCompiler.compile(cmdline, new PrintWriter(System.out), new
	// PrintWriter(System.err), progress)) {
	// throw new RhenaException("Compilation did not finish successfully..");
	// }
	// }

}
