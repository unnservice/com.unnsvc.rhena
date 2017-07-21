
package com.unnsvc.rhena.execution.builders;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.artifact.IArtifact;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * Builders are executed inside of separate threads
 * 
 * @author noname
 *
 */
public class RemoteBuilder extends AbstractBuilder {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaContext context;
	private IEntryPoint entryPoint;
	private IRhenaModule module;

	public RemoteBuilder(IRhenaContext context, IEntryPoint entryPoint, IRhenaModule module) {

		this.context = context;
		this.entryPoint = entryPoint;
		this.module = module;
	}

	@Override
	public IExecutionResponse call() throws Exception {

		debugBuilderRun(module, context, entryPoint);

		return new Result(entryPoint, module);
	}

	private static class Result implements IExecutionResponse {

		private static final long serialVersionUID = 1L;
		private IEntryPoint entryPoint;
		private IRhenaModule module;

		public Result(IEntryPoint entryPoint, IRhenaModule module) {

			this.entryPoint = entryPoint;
			this.module = module;
		}

		@Override
		public IEntryPoint getEntryPoint() {

			return entryPoint;
		}

		@Override
		public IRhenaModule getModule() {

			return module;
		}

		@Override
		public List<IArtifact> getArtifacts() {

			throw new UnsupportedOperationException("Not implemented");
		}

	}

}
