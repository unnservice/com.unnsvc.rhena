package com.unnsvc.rhena.execution;

import com.unnsvc.rhena.common.IRhenaBuilder;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IBuilderFactory;
import com.unnsvc.rhena.common.model.EModuleType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.execution.builders.RemoteBuilder;
import com.unnsvc.rhena.execution.builders.WorkspaceBuilder;

public class BuilderFactory implements IBuilderFactory {

	@Override
	public IRhenaBuilder createBuilder(IRhenaContext context, IEntryPoint incoming, IRhenaModule module) throws RhenaException {

		// log.info("Create builder for: " + entryPoint + " module: " + module);
		if (module.getModuleType() == EModuleType.WORKSPACE) {

			return new WorkspaceBuilder(context, incoming, module);
		} else if (module.getModuleType() == EModuleType.REMOTE) {

			return new RemoteBuilder(context, incoming, module);
		} else {

			throw new RhenaException("Framework doesn't know how to handle module of type: " + module.getModuleType());
		}
	}

}
