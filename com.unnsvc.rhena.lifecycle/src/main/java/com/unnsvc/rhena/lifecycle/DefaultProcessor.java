
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.io.PrintWriter;

import org.eclipse.jdt.core.compiler.CompilationProgress;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;
import com.unnsvc.rhena.common.model.lifecycle.IResource;
import com.unnsvc.rhena.lifecycle.misc.LoggingPrintWriter;
import com.unnsvc.rhena.lifecycle.misc.LoggingPrintWriter.FileDescriptor;

public class DefaultProcessor implements IProcessor {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void configure(IRhenaModule module, Document configuration) {

	}

	/**
	 * @throws RhenaException
	 * @TODO batch instead of running for each resource
	 */
	@Override
	public void process(IExecutionContext context, IRhenaModule module, ExecutionType type) throws RhenaException {

		log.debug(module.getModuleIdentifier().toTag() + ": Calling compilation on : " + context + " resources: " + context.getResources(type).size()
				+ " type: " + type);

		for (IResource resource : context.getResources(type)) {

			if (resource.getSource().exists() && resource.getSource().list().length > 0) {
				if(!resource.getTarget().exists()) {
					resource.getTarget().mkdirs();
				}
				compile(module, resource);
			}
		}
	}

	private void compile(IRhenaModule module, IResource resource) throws RhenaException {

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

		File source = resource.getSource();
		File target = resource.getTarget();

		String cmdline = "-classpath rt.jar -d " + target + " " + source;

		log.debug(module.getModuleIdentifier().toTag() + ": Calling compilation on : " + resource);
		if (!BatchCompiler.compile(cmdline, new PrintWriter(new LoggingPrintWriter(log, FileDescriptor.OUT)),
				new PrintWriter(new LoggingPrintWriter(log, FileDescriptor.ERR)), progress)) {
			throw new RhenaException("Compilation did not finish successfully");
		}
	}

}
