
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

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

	private IRhenaContext config;
	private int indents;
	// private String prefix;
	private IRhenaEngine context;

	public DebugModelVisitor(IRhenaContext config, int indents, IRhenaEngine context) {

		this(config, indents, context, "");
	}

	public DebugModelVisitor(IRhenaContext config, int indents, IRhenaEngine context, String prefix) {

		this.config = config;
		this.indents = indents;
		// this.prefix = prefix;
		this.context = context;
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

		if (module.getDependencies().isEmpty()) {

			config.getLogger().debug(getClass(), getLeading(module).append(" />").toString());
		} else {

			config.getLogger().debug(getClass(), getLeading(module).append(">").toString());

			for (IRhenaEdge edge : module.getDependencies()) {

				IRhenaModule dep = context.materialiseModel(edge.getEntryPoint().getTarget());
				dep.visit(new DebugModelVisitor(config, indents + 1, context, edge.getEntryPoint().getExecutionType().toString()));
			}

			config.getLogger().debug(getClass(), i(indents) + "</" + module.getIdentifier() + ">");
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
