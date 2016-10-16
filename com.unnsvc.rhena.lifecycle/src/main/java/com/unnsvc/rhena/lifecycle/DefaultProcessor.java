
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.compiler.CompilationProgress;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;
import com.unnsvc.rhena.common.model.lifecycle.IResource;
import com.unnsvc.rhena.common.visitors.RhenaDependencyCollectionVisitor;
import com.unnsvc.rhena.lifecycle.misc.LoggingPrintWriter;
import com.unnsvc.rhena.lifecycle.misc.LoggingPrintWriter.FileDescriptor;

public class DefaultProcessor implements IProcessor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;

	public DefaultProcessor(IResolutionContext context) {

		this.context = context;
	}

	@Override
	public void configure(IRhenaModule module, Document configuration) {

	}

	/**
	 * @throws RhenaException
	 * @TODO batch instead of running for each resource
	 */
	@Override
	public void process(IExecutionContext context, IRhenaModule module, EExecutionType type) throws RhenaException {

//		System.err.println("Context is " + context + " type is " + type);
		for (IResource resource : context.getResources(type)) {

			if (resource.getSource().exists() && resource.getSource().listFiles().length > 0) {
				if (!resource.getTarget().exists()) {
					resource.getTarget().mkdirs();
				}
				compile(module, resource, type);
			} else {
				log.debug(module.getModuleIdentifier().toTag(type) + " skipping empty resource: " + resource);
			}
		}
	}

	private void compile(IRhenaModule module, IResource resource, EExecutionType type) throws RhenaException {

		CompilationProgress progress = new CompilationProgress() {

			@Override
			public void begin(int remainingWork) {

			}

			@Override
			public void done() {

			}

			@Override
			public boolean isCanceled() {

				return false;
			}

			@Override
			public void setTaskName(String name) {

			}

			@Override
			public void worked(int workIncrement, int remainingWork) {

			}
		};

		List<URL> deps = new ArrayList<URL>();
		for (IRhenaEdge edge : module.getDependencyEdges()) {

			if (edge.getExecutionType().equals(type)) {
				deps.addAll(module.visit(new RhenaDependencyCollectionVisitor(context, EExecutionType.FRAMEWORK, TraverseType.SCOPE)).getDependenciesURL());
			}
		}

		File source = resource.getSource();
		File target = resource.getTarget();

		String cmdline = "-1.8 " + toClasspathFlag(deps) + " -d " + target + " " + source;

		log.debug(module.getModuleIdentifier().toTag(type) + " compiling");
		for (URL classpath : deps) {
			log.debug(module.getModuleIdentifier().toTag(type) + " -> " + classpath);
		}

		if (!BatchCompiler.compile(cmdline, new PrintWriter(new LoggingPrintWriter(log, FileDescriptor.OUT)),
				new PrintWriter(new LoggingPrintWriter(log, FileDescriptor.ERR)), progress)) {
			throw new RhenaException("Compilation did not finish successfully");
		}
	}

	private String toClasspathFlag(List<URL> deps) {

		if (deps.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (URL url : deps) {
			sb.append(url.getPath()).append(File.pathSeparatorChar);
		}

		return "-classpath " + sb.toString().substring(0, sb.toString().length() - 1);
	}

}
