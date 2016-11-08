
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.core.model.RhenaEdge;

public class TestUtil {

	public static IRhenaEdge createEdge(EExecutionType et) throws Exception {

		return createEdge(et, TraverseType.SCOPE);
	}

	public static IRhenaEdge createEdge(EExecutionType et, TraverseType tt) throws Exception {

		return createEdge(et, "test:test:0.0.1", tt);
	}

	public static IRhenaEdge createEdge(EExecutionType et, String moduleIdentifier, TraverseType tt) throws Exception {

		return new RhenaEdge(et, ModuleIdentifier.valueOf(moduleIdentifier), tt);
	}
}
