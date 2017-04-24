
package com.unnsvc.rhena.lifecycle.execution;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.lifecycle.ICommandInstance;
import com.unnsvc.rhena.common.lifecycle.ILifecycleInstance;
import com.unnsvc.rhena.common.lifecycle.IProcessorInstance;
import com.unnsvc.rhena.lifecycle.instance.ContextProcessorInstance;
import com.unnsvc.rhena.lifecycle.instance.GeneratorProcessorInstance;

public class DefaultLifecycleFactory {

	public static ILifecycleInstance createDefaultLifecycle() {

		ContextProcessorInstance context = new ContextProcessorInstance();
		List<IProcessorInstance> processors = new ArrayList<IProcessorInstance>();
		GeneratorProcessorInstance generator = new GeneratorProcessorInstance();
		List<ICommandInstance> commands = new ArrayList<ICommandInstance>();

		return new DefaultLifecycleInstance(context, processors, generator, commands);
	}

}
