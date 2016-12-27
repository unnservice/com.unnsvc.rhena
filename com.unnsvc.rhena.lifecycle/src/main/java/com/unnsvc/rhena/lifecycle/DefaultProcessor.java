
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.io.PrintWriter;

import org.eclipse.jdt.core.compiler.CompilationProgress;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import org.w3c.dom.Document;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;
import com.unnsvc.rhena.common.model.lifecycle.IResource;
import com.unnsvc.rhena.common.visitors.IDependencies;

public class DefaultProcessor implements IProcessor {

//	private IRhenaCache cache;

	public DefaultProcessor(IRhenaCache cache, IExecutionContext context) {

//		this.cache = cache;
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

		// System.err.println("Context is " + context + " type is " + type);
		for (IResource resource : context.getResources(type)) {

			if (resource.getSourceFile().exists() && resource.getSourceFile().listFiles().length > 0) {
				if (!resource.getTargetFile().exists()) {
					resource.getTargetFile().mkdirs();
				}
				compile(module, resource, type, dependencies);
			} else {
//				log.debug(module.getIdentifier(), type, "skipping empty resource: " + resource);
			}
		}
	}

	private void compile(IRhenaModule module, IResource resource, EExecutionType type, IDependencies dependencies) throws RhenaException {

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

		File source = resource.getSourceFile();
		File target = resource.getTargetFile();

//		System.err.println("Compiling path " + source);
//		System.err.println("Compiling with classpath: " + dependencies.getAsClasspath());
		String cmdline = "-1.8 -classpath " + dependencies.getAsClasspath() + " -d " + target + " " + source;
//		System.err.println("Compiling with cmdline: " + cmdline);
		
		if(!BatchCompiler.compile(cmdline, new PrintWriter(System.out), new PrintWriter(System.err), progress)) {
			throw new RhenaException("Compilation did not finish successfully..");
		}
	}



}
