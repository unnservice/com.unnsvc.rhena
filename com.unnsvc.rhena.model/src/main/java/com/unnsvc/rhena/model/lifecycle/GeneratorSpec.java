
package com.unnsvc.rhena.model.lifecycle;

import java.util.List;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.IGeneratorSpec;
import com.unnsvc.rhena.common.model.IRhenaEdge;

public class GeneratorSpec extends AbstractProcessorSpec implements IGeneratorSpec {

	public GeneratorSpec(String schema, String clazz, Document config, List<IRhenaEdge> processorDeps) {

		super(schema, clazz, config, processorDeps);
	}

}
