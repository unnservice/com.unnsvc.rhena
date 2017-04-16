
package com.unnsvc.rhena.model;

import java.util.Iterator;

import com.unnsvc.rhena.common.ng.model.ILifecycleReference;

public class UnresolvedLifecycleConfiguration extends LifecycleConfiguration {

	public UnresolvedLifecycleConfiguration(String name) {

		super(name);
	}

	@Override
	public Iterator<ILifecycleReference> iterator() {

		throw new UnsupportedOperationException("Unable to iterate over an unresolved lifecycle configuration");
	}
}
