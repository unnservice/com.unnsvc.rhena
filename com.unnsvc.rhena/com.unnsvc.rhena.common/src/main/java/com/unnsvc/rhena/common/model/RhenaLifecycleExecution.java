
package com.unnsvc.rhena.common.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RhenaLifecycleExecution {

	private List<File> lifecycleExecutionClasspath;

	public RhenaLifecycleExecution(RhenaModule model) {

		this(model, new ArrayList<File>());
	}
	
	public RhenaLifecycleExecution(RhenaModule model, List<File> lifecycleExecutionClasspath) {
		
		this.lifecycleExecutionClasspath = lifecycleExecutionClasspath;
	}
	
	public RhenaLifecycleExecution(RhenaModule model, File lifecycleExecutionClasspath) {
		
		this(model, Collections.singletonList(lifecycleExecutionClasspath));
	}

	public List<File> getLifecycleExecutionClasspath() {

		return lifecycleExecutionClasspath;
	}
	
	public void addLifecycleExecutionClasspath(File lifecycleExecutionClasspath) {
		
		this.lifecycleExecutionClasspath.add(lifecycleExecutionClasspath);
	}

}
