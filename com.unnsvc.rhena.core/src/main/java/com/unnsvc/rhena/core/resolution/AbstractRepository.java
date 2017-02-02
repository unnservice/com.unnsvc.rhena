
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaEngine;

public abstract class AbstractRepository implements IRepository {

	private IRhenaEngine context;

	public AbstractRepository(IRhenaEngine context) {

		this.context = context;
	}

	public IRhenaEngine getContext() {

		return context;
	}
}
