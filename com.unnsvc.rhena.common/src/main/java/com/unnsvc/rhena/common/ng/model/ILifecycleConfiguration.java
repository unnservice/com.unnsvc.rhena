
package com.unnsvc.rhena.common.ng.model;

public interface ILifecycleConfiguration extends Iterable<ILifecycleReference> {

	public void setName(String name);

	public String getName();
}
