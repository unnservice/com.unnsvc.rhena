
package com.unnsvc.rhena.common.model.lifecycle;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IDependencies;

public interface IProcessor extends ILifecycleProcessor {

	public void process(IExecutionContext context, IRhenaModule module, EExecutionType type, IDependencies dependencies) throws RhenaException;

}
