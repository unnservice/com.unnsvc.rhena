
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.rmi.RemoteException;
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

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.annotation.ProcessorContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.lifecycle.IJavaProcessor;
import com.unnsvc.rhena.common.lifecycle.IProcessor;
import com.unnsvc.rhena.common.logging.ELogLevel;
import com.unnsvc.rhena.common.logging.ILoggerService;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IDependencies;

public class DefaultJavaProcessor implements IProcessor, IJavaProcessor {

	private static final long serialVersionUID = 1L;
	@ProcessorContext
	private IExecutionContext context;
	// @ProcessorContext
	// private IRhenaCache cache;
	@ProcessorContext
	private ILoggerService logger;

	public DefaultJavaProcessor() {

	}

	@Override
	public void configure(ICaller caller, Document configuration) {

	}

	/**
	 * @throws RemoteException
	 * @TODO batch instead of running for each resource
	 */
	@Override
	public void process(ICaller caller, IDependencies dependencies) throws RemoteException {

		IRhenaModule module = caller.getModule();
		EExecutionType type = caller.getExecutionType();
		// logger.trace(getClass().getName(), "Executing " +
		// getClass().getName());
		logger.fireLogEvent(ELogLevel.TRACE, getClass().getName(), module.getIdentifier(), "Executing " + getClass().getName(), null);

		File outputDirectory = new File(context.getOutputDirectory(module), type.literal().toLowerCase());
		outputDirectory.mkdirs();

		List<String> options = new ArrayList<String>();
		// options.add("-cp");
		// options.add(System.getProperty("java.class.path"));
		options.add("-d");
		options.add(outputDirectory.getAbsolutePath());

		List<File> resources = context.selectResources(type, "^.*\\.java$");

		if (resources.isEmpty()) {
			// logger.warn(getClass(), "No resources selected for compilation in
			// " + module.getIdentifier() + ":" + type.literal());
			logger.fireLogEvent(ELogLevel.TRACE, getClass().getName(), module.getIdentifier(),
					"No resources selected for compilation in " + module.getIdentifier() + ":" + type.literal(), null);
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

			// logger.trace(getClass(), "Compiler diagnostic: " + diag);
			logger.fireLogEvent(ELogLevel.TRACE, getClass().getName(), module.getIdentifier(), "Compiler diagnostic: " + diag, null);
			if (diag.getKind().equals(Kind.ERROR) && firstError == null) {
				firstError = diag;
			}
		}

		if (firstError != null) {
			throw new RhenaException(firstError.getMessage(null));
		}
	}

}
