
package com.unnsvc.rhena.common.execution;

import com.unnsvc.rhena.common.IRhenaBuilder;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IBuilderFactory {

	public IRhenaBuilder createBuilder(IRhenaContext context, IEntryPoint incoming, IRhenaModule module) throws RhenaException;

}
