
package com.unnsvc.rhena.lifecycle.ng;

import java.util.Iterator;

import com.unnsvc.rhena.common.ng.model.ILifecycleConfiguration;
import com.unnsvc.rhena.common.ng.model.ILifecycleReference;

public class DefaultLifecycleConfiguration implements ILifecycleConfiguration {

	private String lifecycleName;

	public DefaultLifecycleConfiguration(String lifecycleName) {

		this.lifecycleName = lifecycleName;
	}

	@Override
	public Iterator<ILifecycleReference> iterator() {

		throw new UnsupportedOperationException("Iteration over default lifecycle configuration not implemented");
	}

	@Override
	public void setName(String lifecycleName) {

		this.lifecycleName = lifecycleName;
	}

	@Override
	public String getName() {

		return lifecycleName;
	}

}
