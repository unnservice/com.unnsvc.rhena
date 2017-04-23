
package com.unnsvc.rhena.execution.builders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaBuilder;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.traversal.DependencyCollector;

public abstract class AbstractBuilder implements IRhenaBuilder {

	private Logger log = LoggerFactory.getLogger(getClass());

	protected void debugBuilderRun(IRhenaModule module, IRhenaContext context, IEntryPoint entryPoint) throws RhenaException {

		if (log.isDebugEnabled()) {
			log.info("Builder building: " + module.getIdentifier());

			DependencyCollector collector = new DependencyCollector(context, entryPoint);
			for (IExecutionResult dep : collector.getDependencyList()) {
				log.info("\tdependency: " + dep.getEntryPoint());
			}
		}
	}
}
