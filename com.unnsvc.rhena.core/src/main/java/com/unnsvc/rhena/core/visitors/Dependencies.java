
package com.unnsvc.rhena.core.visitors;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.visitors.IDependencies;

public class Dependencies implements IDependencies {

	private static final long serialVersionUID = 1L;
	private EExecutionType executionType;
	private Map<EExecutionType, List<IRhenaExecution>> dependencies;

	public Dependencies(EExecutionType executionType) {

		this.executionType = executionType;
		this.dependencies = new EnumMap<EExecutionType, List<IRhenaExecution>>(EExecutionType.class);

		for (EExecutionType et : EExecutionType.values()) {
			dependencies.put(et, new ArrayList<IRhenaExecution>());
		}
	}

	@Override
	public void addDependency(EExecutionType executionType, IRhenaExecution execution) {

		if (dependencies.get(executionType).contains(execution)) {
			dependencies.get(executionType).remove(execution);
		}
		dependencies.get(executionType).add(execution);
	}

	@Override
	public Map<EExecutionType, List<IRhenaExecution>> getDependencies() {

		return dependencies;
	}

	@Override
	public String getAsClasspath() {

		StringBuilder sb = new StringBuilder();
		
//		for(EExecutionType deptype : executionType.getTraversables() ) {
//			sb.append(getAsClasspath(deptype));
//		}
//		sb.append(getAsClasspath(executionType));
		
		for(int i = 0; i <= executionType.ordinal(); i++) {
			sb.append(getAsClasspath(EExecutionType.values()[i]));
		}
		
		
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	private String getAsClasspath(EExecutionType deptype) {

		StringBuffer sb = new StringBuffer();
		
		for(IRhenaExecution exec : dependencies.get(deptype)) {
			if(exec != null) {
				sb.append(exec.getArtifact().getArtifactUrl().getPath());
				sb.append(File.pathSeparator);
			}
		}
		
		return sb.toString();
	}
}
