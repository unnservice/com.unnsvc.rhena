
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaEngine;

public abstract class AbstractRepository implements IRepository {

	private static final long serialVersionUID = 1L;
	// private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaEngine context;

	public AbstractRepository(IRhenaEngine context) {

		this.context = context;
	}

	public IRhenaEngine getContext() {

		return context;
	}
}
