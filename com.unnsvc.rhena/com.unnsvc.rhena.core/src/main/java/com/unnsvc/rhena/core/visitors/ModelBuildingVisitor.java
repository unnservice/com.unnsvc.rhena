
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IResolver;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaEdgeType;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.common.model.RhenaExecution;

public class ModelBuildingVisitor extends ModelResolvingVisitor {

	public ModelBuildingVisitor(IResolver resolver) {

		super(resolver);
	}

	@Override
	public void endModel(RhenaModel model) throws RhenaException {

		RhenaExecution execution = getResolver().materialiseModuleType(model, RhenaEdgeType.ITEST);
	}
}
