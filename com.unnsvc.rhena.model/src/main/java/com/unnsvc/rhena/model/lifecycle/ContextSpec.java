
package com.unnsvc.rhena.model.lifecycle;

import java.util.List;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.IContextSpec;
import com.unnsvc.rhena.common.model.IRhenaEdge;

public class ContextSpec extends AbstractProcessorSpec implements IContextSpec {

	public ContextSpec(String schema, String clazz, Document config, List<IRhenaEdge> processorDeps) {

		super(schema, clazz, config, processorDeps);
	}
}
