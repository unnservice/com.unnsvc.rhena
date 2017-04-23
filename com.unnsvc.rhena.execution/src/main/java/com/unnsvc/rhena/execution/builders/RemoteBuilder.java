
package com.unnsvc.rhena.execution.builders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.execution.IExecutionResult;
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
	private IEntryPoint entryPoint;
	private IRhenaModule module;

	public RemoteBuilder(IEntryPoint entryPoint, IRhenaModule module) {

		this.entryPoint = entryPoint;
		this.module = module;
	}

	@Override
	public IExecutionResult call() throws Exception {

		log.info("Builder building: " + module.getIdentifier());

		return new Result(entryPoint, module);
	}

	private static class Result implements IExecutionResult {

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

	}

}
