
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.annotation.ProcessorContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IJavaProcessor;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;
import com.unnsvc.rhena.common.visitors.IDependencies;

public class DefaultJavaProcessor implements IProcessor, IJavaProcessor {

	private static final long serialVersionUID = 1L;
	@ProcessorContext
	private IExecutionContext context;
	@ProcessorContext
	private IRhenaCache cache;
	@ProcessorContext
	private ILogger logger;

	public DefaultJavaProcessor() {

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

		try {
			logger.trace(getClass(),
					"Executing " + getClass() + " from own jar location: " + getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}

		File outputDirectory = new File(context.getOutputDirectory(module), type.literal().toLowerCase());
		outputDirectory.mkdirs();

		List<String> options = new ArrayList<String>();
		options.add("-d");
		options.add(outputDirectory.getAbsolutePath());

		List<File> resources = context.selectResources(type, "^.*\\.java$");

		if (resources.isEmpty()) {
			logger.warn(getClass(), "No resources selected for compilation in " + module.getIdentifier() + ":" + type.literal());
			return;
		}

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

		Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(resources);
		CompilationTask compilationTask = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits1);
		compilationTask.call();

		Diagnostic<? extends JavaFileObject> firstError = null;
		for (Diagnostic<? extends JavaFileObject> diag : diagnostics.getDiagnostics()) {

			logger.trace(getClass(), "Compiler diagnostic: " + diag);
			if (diag.getKind().equals(Kind.ERROR) && firstError == null) {
				firstError = diag;
			}
		}

		if (firstError != null) {
			throw new RhenaException(firstError.getMessage(null));
		}
	}

}
