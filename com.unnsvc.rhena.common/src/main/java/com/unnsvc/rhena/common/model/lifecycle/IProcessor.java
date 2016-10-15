
package com.unnsvc.rhena.common.model.lifecycle;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.executiontype.IExecutionType;

public interface IProcessor extends ILifecycleProcessor {

	public void process(IExecutionContext context, IRhenaModule module, IExecutionType type) throws RhenaException;

}
