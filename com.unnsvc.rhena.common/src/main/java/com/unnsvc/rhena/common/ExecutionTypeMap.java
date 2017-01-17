
package com.unnsvc.rhena.common;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;

public class ExecutionTypeMap extends EnumMap<EExecutionType, List<IRhenaExecution>> {

	private static final long serialVersionUID = 1L;

	public ExecutionTypeMap() {

		super(EExecutionType.class);
		for (EExecutionType t : EExecutionType.values()) {
			put(t, new ArrayList<IRhenaExecution>());
		}
	}

}
