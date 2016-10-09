
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IResolver;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaModel;

public class RhenaModelProcessingVisitor extends ModelResolvingVisitor {

	private ModuleCallback callback;

	public RhenaModelProcessingVisitor(IResolver resolver, ModuleCallback callback) {

		super(resolver);
		this.callback = callback;
	}

	@Override
	public void endModel(RhenaModel model) throws RhenaException {

		callback.onModel(model);
	}

	public static interface ModuleCallback {

		public void onModel(RhenaModel model);
	}

}
