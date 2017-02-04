
package com.unnsvc.rhena.common.visitors;

import java.util.List;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * Example:
 * 
 * <pre>
 * 	moduleId
 * 		moduleId:p
 * 		moduleId:l
 * 		moduleId:c
 * 			moduleId:p
 * 			moduleId:c
 * 		moduleId:c:printed
 * </pre>
 * 
 * @author noname
 *
 */
public class DebugModelVisitor implements IModelVisitor {

	private IRhenaContext context;
	private int indents;
	// private String prefix;
	private IRhenaEngine engine;

	public DebugModelVisitor(IRhenaContext context, int indents, IRhenaEngine engine) {

		this(context, indents, engine, "");
	}

	public DebugModelVisitor(IRhenaContext context, int indents, IRhenaEngine engine, String prefix) {

		this.context = context;
		this.indents = indents;
		// this.prefix = prefix;
		this.engine = engine;
	}

	public String i(int indent) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < indent; i++) {
			sb.append("    ");
		}
		return sb.toString();
	}

	@Override
	public void visit(IRhenaModule module) throws RhenaException {

		List<IRhenaEdge> mergedEdges = module.getMergedDependencies(context.getCache());
		if (mergedEdges.isEmpty()) {

			context.getLogger().debug(getClass(), getLeading(module).append(" />").toString());
		} else {

			context.getLogger().debug(getClass(), getLeading(module).append(">").toString());

			for (IRhenaEdge edge : mergedEdges) {

				IRhenaModule dep = engine.getContext().getCache().getModule(edge.getEntryPoint().getTarget());
				dep.visit(new DebugModelVisitor(context, indents + 1, engine, edge.getEntryPoint().getExecutionType().toString()));
			}

			context.getLogger().debug(getClass(), i(indents) + "</" + module.getIdentifier() + ">");
		}
	}

	private StringBuffer getLeading(IRhenaModule module) {

		StringBuffer sb = new StringBuffer();
		sb.append(i(indents) + "<" + module.getIdentifier().toString());

		if (module.getParent() != null) {

			sb.append(" parent=\"" + module.getParent().getEntryPoint().getTarget() + "\"");
		}

		if (module.getLifecycleName() != null && module.getLifecycleName() != RhenaConstants.DEFAULT_LIFECYCLE_NAME) {

			String l = module.getLifecycleName();
			sb.append(" lifecycle=\"" + l + "\"");
		}

		return sb;
	}

}
