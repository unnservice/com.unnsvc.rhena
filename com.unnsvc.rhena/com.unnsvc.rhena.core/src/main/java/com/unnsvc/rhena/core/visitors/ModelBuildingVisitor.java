
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolver;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaModel;

public class ModelBuildingVisitor implements IModelVisitor {

	private IResolver resolver;

	public ModelBuildingVisitor(IResolver resolver) {

		this.resolver = resolver;
	}

	@Override
	public void startModel(RhenaModel model) throws RhenaException {

		// TODO Auto-generated method stub

	}

	@Override
	public void endModel(RhenaModel model) throws RhenaException {

//		RhenaExecution execution = getResolver().materialiseModuleType(model, RhenaEdgeType.ITEST);
	}
}
