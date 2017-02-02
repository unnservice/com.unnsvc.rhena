
package com.unnsvc.rhena.common.lifecycle;

import java.util.List;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;

public interface IGenerator extends ILifecycleProcessor {

	public List<IArtifactDescriptor> generate(ICaller caller) throws RhenaException;

}
