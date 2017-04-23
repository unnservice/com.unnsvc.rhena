
package com.unnsvc.rhena.core.status;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.core.resolution.CascadingModelBuilder;

/**
 * Implements hooks for status
 * 
 * @author noname
 *
 */
public class StatusCascadingModelBuilder extends CascadingModelBuilder {

	private Logger log = LoggerFactory.getLogger(getClass());
	private List<BuildStatus> executedStatus;

	public StatusCascadingModelBuilder(IRhenaContext context) {

		super(context);
		this.executedStatus = new ArrayList<BuildStatus>();
	}

	@Override
	protected void onExecutionComplete() {

		log.info("Execution result:");
		for (BuildStatus status : executedStatus) {
			
			log.info("\tExecuted: " + status.getEntryPoint().getTarget() + ":" + status.getEntryPoint().getExecutionType().toString().toLowerCase());
		}
	}

	@Override
	protected void onSubmitted(IEntryPoint entryPoint) {

		executedStatus.add(new BuildStatus(entryPoint));
	}
}
