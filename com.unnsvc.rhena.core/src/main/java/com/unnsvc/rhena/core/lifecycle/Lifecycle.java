
package com.unnsvc.rhena.core.lifecycle;

import java.io.File;
import java.util.List;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IGenerator;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;
import com.unnsvc.rhena.common.visitors.IDependencies;

public class Lifecycle implements ILifecycle {

	private IExecutionContext context;
	private IGenerator generator;
	private List<IProcessor> processors;

	public Lifecycle(IExecutionContext context, IGenerator generator, List<IProcessor> processors) {

		this.context = context;
		this.generator = generator;
		this.processors = processors;
	}

	@Override
	public File executeLifecycle(IRhenaModule module, EExecutionType executionType, IDependencies dependencies) throws RhenaException {

		for (IProcessor processor : processors) {
			processor.process(context, module, executionType, dependencies);
		}

		File generated = generator.generate(context, module, executionType);
		return generated;
	}

}
